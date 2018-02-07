//
//  ShowBarCodeForGiftCardViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 16/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke
import SVProgressHUD

//MARK: ShowBarCodeForGiftCardViewController Delegate
protocol ShowBarCodeForGiftCardViewControllerDelegate: class {
    func amountUsedInPayAtCounter(inCommUserCard: InCommUserGiftCard)
}

class ShowBarCodeForGiftCardViewController: UIViewController {
    
    @IBOutlet weak var balanceLabel         : UILabel!
    @IBOutlet weak var barCodeValueLabel    : UILabel!
    @IBOutlet weak var barCodeImage         : UIImageView!
    @IBOutlet weak var barCodeContentView   : UIView!
    weak var delegate                       : ShowBarCodeForGiftCardViewControllerDelegate?
    var brigtnessValue                      = UIScreen.mainScreen().brightness
    var inCommUserGiftCardId                : Int32!
    var inCommUserCard                      : InCommUserGiftCard{
        get{
            return GiftCardCreationService.sharedInstance.getUserGiftCard(inCommUserGiftCardId)!
        }
    }
    //brightness variable declaration
    
    override func viewDidLoad() {
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ShowBarCodeForGiftCardViewController.reduceBrightness), name: UIApplicationWillResignActiveNotification, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ShowBarCodeForGiftCardViewController.increaseBrightness), name: UIApplicationDidBecomeActiveNotification, object: nil)
        
        UIScreen.mainScreen().brightness = 1.0
        
        //get latest balance
        barCodeContentView.hidden = true
        initialCall()
        
        super.viewDidLoad()
    }
    
    //call the get balance details
    func initialCall() {
        SVProgressHUD.showWithStatus("Refreshing...", maskType: .Clear)
        getLatestBalance { (success, error) in
            if !success {
                SVProgressHUD.dismiss()
                self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry") {(confirmed) -> Void in
                    if confirmed{
                        self.initialCall()
                        return
                    } else {
                        UIScreen.mainScreen().brightness = self.brigtnessValue
                        self.dismissViewControllerAnimated(true, completion: nil)
                    }
                }
            } else {
                self.updateFrame()
            }
        }
    }
    
    //MARK: - Get card balance
    func getLatestBalance( callback: (success: Bool, error:NSError?) -> Void) {
        
        InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: inCommUserCard.cardId) { (giftCardBalance, error) in
            if(error != nil){
                if error?.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: self.inCommUserCard.cardId) { (giftCardBalance, error) in
                                if error != nil{
                                    callback(success: false, error: error)
                                    return
                                } else {
                                    //update latest information
                                    var inCommUserGiftCard:InCommUserGiftCard = self.inCommUserCard
                                    inCommUserGiftCard.refreshTime = NSDate()
                                    inCommUserGiftCard.balance = (giftCardBalance?.balance)!
                                    GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommUserGiftCard)
                                    callback(success: true, error: nil)
                                }
                            }
                        }
                        else{
                            return callback(success: false, error: error)
                        }
                    })
                } else {
                    callback(success: false, error: error)
                    return
                }
            } else {
                //update latest information
                var inCommUserGiftCard:InCommUserGiftCard = self.inCommUserCard
                inCommUserGiftCard.refreshTime = NSDate()
                inCommUserGiftCard.balance = (giftCardBalance?.balance)!
                GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommUserGiftCard)
                
                callback(success: true, error: nil)
            }
        }
    }
    
    //MARK: - Update frame
    //update the view
    func updateFrame(){
        let balanceFormatted = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommUserCard.balance);
        balanceLabel.text = balanceFormatted
        barCodeValueLabel.text = inCommUserCard.cardNumber;
        
        if let url  = NSURL(string:inCommUserCard.barcodeImageUrl){
            barCodeImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            barCodeImage.layer.masksToBounds = true
            barCodeImage.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success: { (image) -> Void in
                self.barCodeImage.image = image
                self.barCodeImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.barCodeImage.layer.masksToBounds = false
                SVProgressHUD.dismiss()
                self.barCodeContentView.hidden = false
                }, failure: { (error) -> Void in
                    SVProgressHUD.dismiss()
                    self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry") {(confirmed) -> Void in
                        if confirmed{
                            self.updateFrame()
                        }
                    }
            })
        }
    }
    
    //MARK: - IBAction methods
    //close the screen & update the balance
    @IBAction func close(){
        UIScreen.mainScreen().brightness = brigtnessValue
        SVProgressHUD.showWithStatus("Refreshing...", maskType: .Clear)
        getLatestBalance { (success, error) in
            SVProgressHUD.dismiss()
            if !success {
                self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry") {(confirmed) -> Void in
                    if confirmed{
                        self.close()
                        return
                    } else {
                        self.dismissViewControllerAnimated(true, completion: {
                            self.delegate?.amountUsedInPayAtCounter(self.inCommUserCard)
                        })
                    }
                }
                return
            } else {
                self.dismissViewControllerAnimated(true, completion: {
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
    func reduceBrightness()  {
        UIScreen.mainScreen().brightness = brigtnessValue
    }
    
    func increaseBrightness()  {
        brigtnessValue = UIScreen.mainScreen().brightness
        UIScreen.mainScreen().brightness = 1.0
    }
    
    //MARK: - deinit
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    
}