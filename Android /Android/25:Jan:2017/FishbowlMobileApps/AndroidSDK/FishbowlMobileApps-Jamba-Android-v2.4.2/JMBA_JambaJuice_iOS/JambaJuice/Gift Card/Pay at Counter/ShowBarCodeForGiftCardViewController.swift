//
//  ShowBarCodeForGiftCardViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 16/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK

import SVProgressHUD

//MARK: ShowBarCodeForGiftCardViewController Delegate
protocol ShowBarCodeForGiftCardViewControllerDelegate: class {
    func amountUsedInPayAtCounter(_ inCommUserCard: InCommUserGiftCard)
}

class ShowBarCodeForGiftCardViewController: UIViewController {
    
    @IBOutlet weak var balanceLabel         : UILabel!
    @IBOutlet weak var barCodeValueLabel    : UILabel!
    @IBOutlet weak var barCodeImage         : UIImageView!
    @IBOutlet weak var barCodeContentView   : UIView!
    weak var delegate                       : ShowBarCodeForGiftCardViewControllerDelegate?
    var brigtnessValue                      = UIScreen.main.brightness
    var inCommUserGiftCardId                : Int32!
    var inCommUserCard                      : InCommUserGiftCard{
        get{
            return GiftCardCreationService.sharedInstance.getUserGiftCard(inCommUserGiftCardId)!
        }
    }
    //brightness variable declaration
    
    override func viewDidLoad() {
        NotificationCenter.default.addObserver(self, selector: #selector(ShowBarCodeForGiftCardViewController.reduceBrightness), name: NSNotification.Name.UIApplicationWillResignActive, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ShowBarCodeForGiftCardViewController.increaseBrightness), name: NSNotification.Name.UIApplicationDidBecomeActive, object: nil)
        
        UIScreen.main.brightness = 1.0
        
        //get latest balance
        barCodeContentView.isHidden = true
        initialCall()
        
        super.viewDidLoad()
    }
    
    //call the get balance details
    func initialCall() {
        SVProgressHUD.show(withStatus: "Refreshing...")
        SVProgressHUD.setDefaultMaskType(.clear)
        getLatestBalance { (success, error) in
            if !success {
                SVProgressHUD.dismiss()
                self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry") {(confirmed) -> Void in
                    if confirmed{
                        self.initialCall()
                        return
                    } else {
                        UIScreen.main.brightness = self.brigtnessValue
                        self.dismiss(animated: true, completion: nil)
                    }
                }
            } else {
                self.updateFrame()
            }
        }
    }
    
    //MARK: - Get card balance
    func getLatestBalance( _ callback: @escaping (_ success: Bool, _ error:NSError?) -> Void) {
        
        InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: inCommUserCard.cardId) { (giftCardBalance, error) in
            if(error != nil){
                if error?.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: self.inCommUserCard.cardId) { (giftCardBalance, error) in
                                if error != nil{
                                    callback(false, error)
                                    return
                                } else {
                                    //update latest information
                                    var inCommUserGiftCard:InCommUserGiftCard = self.inCommUserCard
                                    inCommUserGiftCard.refreshTime = NSDate() as Date
                                    inCommUserGiftCard.balance = (giftCardBalance?.balance)!
                                    GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommUserGiftCard)
                                    callback(true, nil)
                                }
                            }
                        }
                        else{
                            return callback(false, error)
                        }
                    })
                } else {
                    callback(false, error)
                    return
                }
            } else {
                //update latest information
                var inCommUserGiftCard:InCommUserGiftCard = self.inCommUserCard
                inCommUserGiftCard.refreshTime = NSDate() as Date
                inCommUserGiftCard.balance = (giftCardBalance?.balance)!
                GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommUserGiftCard)
                
                callback(true, nil)
            }
        }
    }
    
    //MARK: - Update frame
    //update the view
    func updateFrame(){
        let balanceFormatted = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommUserCard.balance);
        balanceLabel.text = balanceFormatted
        barCodeValueLabel.text = inCommUserCard.cardNumber;
        
        if let url  = URL(string:inCommUserCard.barcodeImageUrl){
            barCodeImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            barCodeImage.layer.masksToBounds = true
            
            barCodeImage.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (imageData) in
                if let image = imageData.value {
                    self.barCodeImage.image = image
                    self.barCodeImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    self.barCodeImage.layer.masksToBounds = false
                    SVProgressHUD.dismiss()
                    self.barCodeContentView.isHidden = false
                } else {
                    SVProgressHUD.dismiss()
                    self.presentConfirmation("Error", message: "Cannot able to download QR code", buttonTitle: "Retry") {(confirmed) -> Void in
                        if confirmed{
                            self.updateFrame()
                        }
                    }
                }
            })
        }
    }
    
    //MARK: - IBAction methods
    //close the screen & update the balance
    @IBAction func close(){
        UIScreen.main.brightness = brigtnessValue
        SVProgressHUD.show(withStatus: "Refreshing...")
        SVProgressHUD.setDefaultMaskType(.clear)
        getLatestBalance { (success, error) in
            SVProgressHUD.dismiss()
            if !success {
                self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry") {(confirmed) -> Void in
                    if confirmed{
                        self.close()
                        return
                    } else {
                        self.dismiss(animated: true, completion: {
                            self.delegate?.amountUsedInPayAtCounter(self.inCommUserCard)
                        })
                    }
                }
                return
            } else {
                self.dismiss(animated: true, completion: {
                    self.delegate?.amountUsedInPayAtCounter(self.inCommUserCard)
                })
            }
        }
    }
    
    //close the screen
    @IBAction func closeScreenWithoutAPICall(){
//        UIScreen.mainScreen().brightness = brigtnessValue
//        self.dismissViewControllerAnimated(true, completion: {
//            self.delegate?.amountUsedInPayAtCounter(self.inCommUserCard)
//        })
        close()
    }
    
    //MARK: - NSnotificartion
    @objc func reduceBrightness()  {
        UIScreen.main.brightness = brigtnessValue
    }
    
    @objc func increaseBrightness()  {
        brigtnessValue = UIScreen.main.brightness
        UIScreen.main.brightness = 1.0
    }
    
    //MARK: - deinit
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    
}
