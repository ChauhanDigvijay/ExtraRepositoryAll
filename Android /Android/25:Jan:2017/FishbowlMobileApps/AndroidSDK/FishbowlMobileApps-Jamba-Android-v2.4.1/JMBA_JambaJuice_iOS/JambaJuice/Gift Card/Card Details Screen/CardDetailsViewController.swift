//
//  CardDetailsViewController.swift
//  JambaGiftCards
//
//  Created by vThink on 5/24/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import SVProgressHUD

//MARK: Delegate
protocol CardDetailsViewControllerDelegate: class{
    func updateUserGiftCardDetails(_ inCommUserGiftCard:InCommUserGiftCard)
}

class CardDetailsViewController: UIViewController,UIScrollViewDelegate, UpdateGiftCardNameViewControllerDelegate {
    @IBOutlet weak var cardImageView                 :UIImageView!
    @IBOutlet weak var scrollView                    :UIScrollView!
    @IBOutlet weak var contentView                   :UIView!
    @IBOutlet weak var cardNameLabel                 :UILabel!
    @IBOutlet weak var cardNumberLabel               :UILabel!
    @IBOutlet weak var balanceLabel                  :UILabel!
    @IBOutlet weak var cardImageViewHeightConstraint :NSLayoutConstraint!
    @IBOutlet weak var cardImageViewWidthConstraint  :NSLayoutConstraint!
    @IBOutlet weak var contentViewHeightConstraint   :NSLayoutConstraint!
    @IBOutlet weak var contentViewWidthConstraint    :NSLayoutConstraint!
    @IBOutlet weak var backButton                    :UIButton!
    @IBOutlet weak var pinNumberShowButton           :UIButton!
    
    @IBOutlet weak var removeButton : UIButton!

    var removeButtonHidden:Bool! = false
    
    var inCommUserCardId                             :Int32!
     var continueDeleteWithoutAlert = false
    var inCommUserCard:InCommUserGiftCard!{
        get{
            return GiftCardCreationService.sharedInstance.getUserGiftCard(inCommUserCardId)
        }
    }
    var screenIsPresented                            :Bool!
    var delegate                                      :CardDetailsViewControllerDelegate?
    enum SegueIdentifiers:String {
        case showUpdateGiftCardNameVC = "showUpdateGiftCardNameVC"
    }
    
    override func viewDidLoad() {
        removeButton.isHidden = removeButtonHidden
        updateFrame()
        updateCardDetails()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == SegueIdentifiers.showUpdateGiftCardNameVC.rawValue {
            let targetController =  segue.destination as! UpdateGiftCardNameViewController
            targetController.inCommUserGiftCardId = inCommUserCard.cardId
            targetController.delegate = self
        }
    }
    
    //MARK: - Update UI & gift card details
    func updateFrame(){
        contentViewWidthConstraint.constant = self.view.frame.size.width
        cardImageViewWidthConstraint.constant  = self.view.frame.size.width - GiftCardAppConstants.giftCardLeadingAndTrailingSpace - 16;
        
        if GiftCardAppConstants.isiPhone {
            cardImageViewWidthConstraint.constant  = self.view.frame.size.width - GiftCardAppConstants.giftCardLeadingAndTrailingSpace - 16;
        } else {
            cardImageViewWidthConstraint.constant  = self.view.frame.size.width / 2
        }
        
        cardImageViewHeightConstraint.constant =  cardImageViewWidthConstraint.constant * CGFloat(GiftCardAppConstants.GiftCardRatioHeight)
        contentViewHeightConstraint.constant = cardImageViewHeightConstraint.constant + 350
        scrollView.contentSize.height = contentViewHeightConstraint.constant
        cardImageView.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
        cardImageView.layer.masksToBounds = true
        
        //change back button based screen present style
        if (screenIsPresented != nil && screenIsPresented) {
            backButton.setImage(GiftCardAppConstants.backButtonImageWhenPresented, for: UIControlState())
        }
        else {
            backButton.setImage(GiftCardAppConstants.backButtonImageWhenPushed, for: UIControlState())
        }
    }
    
    //fill the card details
    func updateCardDetails(){
        cardNameLabel.text = inCommUserCard.cardName
        cardNumberLabel.text = inCommUserCard.cardNumber
        
        balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommUserCard.balance)
        if let url  = URL(string:inCommUserCard.imageUrl){
            self.cardImageView.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            self.cardImageView.layer.masksToBounds = true
            cardImageView.af_setImage(withURL: url, placeholderImage:
                GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: DispatchQueue.main, imageTransition: UIImageView.ImageTransition.noTransition, runImageTransitionIfCached: false, completion: { (response) in
                    if let image = response.result.value{
                        self.cardImageView.image = image
                        self.cardImageView.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                        self.cardImageView.layer.masksToBounds = false
                    }
            })
        }
    }
    
    //MARK: - IBAction methods
    @IBAction func showPinNumber(){
        if pinNumberShowButton.titleLabel?.text == "Show" {
            pinNumberShowButton.setTitle(inCommUserCard.cardPin, for: .normal)
            pinNumberShowButton.setTitleColor(UIColor(red: 94.0/255.0, green: 94.0/255.0, blue: 94.0/255.0, alpha: 1.0) , for: UIControlState())
        } else {
            pinNumberShowButton.setTitle("Show", for: UIControlState())
            pinNumberShowButton.setTitleColor(UIColor(red: 14.0/255.0, green: 122.0/255.0, blue: 255.0/255.0, alpha: 1.0) , for: UIControlState())
        }
    }
    
    @IBAction func close(){
        if (screenIsPresented != nil && screenIsPresented) {
            self.dismiss(animated: true, completion: nil)
        } else {
            popViewController()
            
        }
    }
    
    @IBAction func showUpdateGiftCardNameVC(){
        performSegue(withIdentifier: SegueIdentifiers.showUpdateGiftCardNameVC.rawValue, sender: nil)
    }
    
    //MARK: - update gift card name delegate
    func giftCardNameChanged(_ inCommUserGiftCard:InCommUserGiftCard) {
        
        GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommUserGiftCard)
        cardNameLabel.text = inCommUserGiftCard.cardName
    }
    
    // MARK: - de alloc Notification
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    @IBAction func deleteGiftCard() {
        if continueDeleteWithoutAlert {
            deleteCard()
        } else {
            self.presentConfirmation("Gift Card Removal", message: "Are you sure you want to remove this card from your account?", buttonTitle: "OK") {(confirmed) -> Void in
                if confirmed{
                    self.deleteCard()
                }
            }
        }
    }
    
    func deleteCard() {
        if inCommUserCard.autoReloadId != nil {
            deleteAutoReloadRules()
        } else {
            deleteCardAPICall()
        }
    }
    
    func deleteAutoReloadRules()  {
        SVProgressHUD.show(withStatus: "Removing auto reload rules...")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService.sharedInstance.deleteAutoReloadDetails(inCommUserCard.cardId, autoReloadId: inCommUserCard.autoReloadId) { (error) in
            if error != nil{
                SVProgressHUD.dismiss()
                self.retryConfirmationMessage(error!)
            }
            else{
                //remove the auto reload details from the gift card manually
                var cardDetails = self.inCommUserCard
                cardDetails!.autoReloadId = nil
                self.updateUserGiftCardDetails(cardDetails!)
                self.continueDeleteWithoutAlert = true
                self.deleteCardAPICall()
            }
        }
    }
    
    func deleteCardAPICall() {
        SVProgressHUD.show(withStatus: "Removing gift card...")
        SVProgressHUD.setDefaultMaskType(.clear)
        InCommUserGiftCardService.deleteGiftCard(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: inCommUserCard.cardId) { (error) in
            if (error != nil) {
                if (error?.code == GiftCardAppConstants.errorCodeInvalidUser) {
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            self.deleteCardAPICall()
                        }
                        else{
                            SVProgressHUD.dismiss()
                            self.presentError(inCommError)
                            return
                        }
                    })
                } else {
                    SVProgressHUD.dismiss()
                    self.retryConfirmationMessage(error!)
                    return
                }
            } else {
                SVProgressHUD.dismiss()
                
                let message = "Gift card \"" + (self.inCommUserCard.cardName.lowercased()) + "\" removed successfully"
                GiftCardCreationService.sharedInstance.deleteGiftCardFromUserGiftCards(self.inCommUserCard.cardId)
                self.presentOkAlert("Removed", message: message, callback: {
                     NotificationCenter.default.post(name: Notification.Name(rawValue: GiftCardAppConstants.giftCardRemoved), object: nil);
                })
            }
        }
    }
    
    func retryConfirmationMessage(_ error:NSError) {
        self.presentConfirmation("Error", message: error.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
            if confirmed{
                self.deleteCard()
            }
            else{
                return
            }
        })
    }
    
    func updateUserGiftCardDetails(_ inCommUserGiftCard: InCommUserGiftCard) {
        var inCommGiftCard = inCommUserGiftCard
        inCommGiftCard = inCommUserGiftCard
        GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommGiftCard)
    }
}
