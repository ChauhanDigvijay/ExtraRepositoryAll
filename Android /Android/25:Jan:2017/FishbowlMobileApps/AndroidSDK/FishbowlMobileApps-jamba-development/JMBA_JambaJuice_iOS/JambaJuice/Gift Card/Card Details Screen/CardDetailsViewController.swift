//
//  CardDetailsViewController.swift
//  JambaGiftCards
//
//  Created by vThink on 5/24/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke
import SVProgressHUD

//MARK: Delegate
protocol CardDetailsViewControllerDelegate: class{
    func updateUserGiftCardDetails(inCommUserGiftCard:InCommUserGiftCard)
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
    var inCommUserCardId                             :Int32!
    var inCommUserCard:InCommUserGiftCard!{
        get{
            return GiftCardCreationService.sharedInstance.getUserGiftCard(inCommUserCardId)
        }
    }
    var screenIsPresented                            :Bool!
    var delegte                                      :CardDetailsViewControllerDelegate?
    enum SegueIdentifiers:String {
        case showUpdateGiftCardNameVC = "showUpdateGiftCardNameVC"
    }
    
    override func viewDidLoad() {
        updateFrame()
        updateCardDetails()
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == SegueIdentifiers.showUpdateGiftCardNameVC.rawValue {
            let targetController =  segue.destinationViewController as! UpdateGiftCardNameViewController
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
            backButton.setImage(GiftCardAppConstants.backButtonImageWhenPresented, forState: .Normal)
        }
        else {
            backButton.setImage(GiftCardAppConstants.backButtonImageWhenPushed, forState: .Normal)
        }
    }
    
    //fill the card details
    func updateCardDetails(){
        cardNameLabel.text = inCommUserCard.cardName
        cardNumberLabel.text = inCommUserCard.cardNumber
        
        balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommUserCard.balance)
        if let url  = NSURL(string:inCommUserCard.imageUrl){
            self.cardImageView.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            self.cardImageView.layer.masksToBounds = true
            cardImageView.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage,success:{(image) in
                self.cardImageView.image = image
                self.cardImageView.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.cardImageView.layer.masksToBounds = false
            })
        }
    }
    
    //MARK: - IBAction methods
    @IBAction func showPinNumber(){
        if pinNumberShowButton.titleLabel?.text == "Show" {
            pinNumberShowButton.setTitle(inCommUserCard.cardPin, forState: .Normal)
            pinNumberShowButton.setTitleColor(UIColor(red: 94.0/255.0, green: 94.0/255.0, blue: 94.0/255.0, alpha: 1.0) , forState: .Normal)
        } else {
            pinNumberShowButton.setTitle("Show", forState: .Normal)
            pinNumberShowButton.setTitleColor(UIColor(red: 14.0/255.0, green: 122.0/255.0, blue: 255.0/255.0, alpha: 1.0) , forState: .Normal)
        }
    }
    
    @IBAction func close(){
        if (screenIsPresented != nil && screenIsPresented) {
            self.dismissViewControllerAnimated(true, completion: nil)
        } else {
            popViewController()
            
        }
    }
    
    @IBAction func showUpdateGiftCardNameVC(){
        performSegueWithIdentifier(SegueIdentifiers.showUpdateGiftCardNameVC.rawValue, sender: nil)
    }
    
    //MARK: - update gift card name delegate
    func giftCardNameChanged(inCommUserGiftCard:InCommUserGiftCard) {
        
        GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommUserGiftCard)
        cardNameLabel.text = inCommUserGiftCard.cardName
    }
    
    // MARK: - de alloc Notification
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
}
