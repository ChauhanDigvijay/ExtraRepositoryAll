//
//  UpdateGiftCardNameViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 25/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke
import SVProgressHUD

//MARK: UpdateGiftCardNameViewController Delegate
protocol UpdateGiftCardNameViewControllerDelegate: class {
    func giftCardNameChanged(inCommUserCard:InCommUserGiftCard)
}

class UpdateGiftCardNameViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var giftCardName          : UITextField!
    @IBOutlet weak var saveButtonBottomSpace : NSLayoutConstraint!
    weak var delegate                        : UpdateGiftCardNameViewControllerDelegate?
    var inCommUserGiftCardId                 : Int32!
    var inCommUserCard:InCommUserGiftCard{
        get{
            return GiftCardCreationService.sharedInstance.getUserGiftCard(inCommUserGiftCardId)!
        }
    }
    
    override func viewDidLoad() {
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(UpdateGiftCardNameViewController.keyboardDidShow(_:)), name: UIKeyboardWillShowNotification, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(UpdateGiftCardNameViewController.keyboardWillDismiss(_:)), name: UIKeyboardWillHideNotification, object: nil)
        super.viewDidLoad()
    }
    
    override func viewDidAppear(animated: Bool) {
        giftCardName.becomeFirstResponder()
    }
    
    //MARK: - Text field delegates
    func keyboardDidShow(notification: NSNotification) {
        let userInfo:NSDictionary = notification.userInfo!
        let keyboardFrame:NSValue = userInfo.valueForKey(UIKeyboardFrameEndUserInfoKey) as! NSValue
        let keyboardRectangle = keyboardFrame.CGRectValue()
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue) {
            self.saveButtonBottomSpace.constant = keyboardRectangle.height
            self.view.layoutIfNeeded()
        }
    }
    
    func keyboardWillDismiss(notification: NSNotification) {
        let userInfo:NSDictionary = notification.userInfo!
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue) {
            self.saveButtonBottomSpace.constant = 0
            self.view.layoutIfNeeded()
        }
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        view.endEditing(true)
        saveGiftCardName()
        return true
    }
    
    //MARK: - Validation
    func validation() -> Bool{
        giftCardName.resignFirstResponder()
        let giftCardNameText = giftCardName.text?.trim()
        if (giftCardNameText?.characters.count == 0) {
            self.presentOkAlert("Alert", message: "Please enter valid gift card name")
            return false
        }
        return true
    }
    
    //MARK: - IBAction methods
    @IBAction func saveGiftCardName() {
        if (!validation()) {
            return
        }
        SVProgressHUD.showWithStatus("Updating...", maskType: .Clear)
        GiftCardCreationService.sharedInstance.updateUserGiftCardName(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: inCommUserCard.cardId, cardName: giftCardName.text?.trim()) { (userGiftCard, error) in
            SVProgressHUD.dismiss()
            if (error != nil) {
                self.presentError(error)
                return
            } else {
                var incommUserGiftCard:InCommUserGiftCard = self.inCommUserCard
                incommUserGiftCard.cardName = (self.giftCardName.text?.trim())!
                incommUserGiftCard.lastModifiedDate = NSDate()
                self.delegate?.giftCardNameChanged(incommUserGiftCard)
                NSNotificationCenter.defaultCenter().postNotificationName(GiftCardAppConstants.refreshGiftCardHomePage, object: nil)
                self.popViewController()
            }
        }
    }
    
    //MARK: Hide the keyboard
    @IBAction func closeKeyBoardInView() {
        view.endEditing(true)
    }
    
    //MARK: - deinit
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
}