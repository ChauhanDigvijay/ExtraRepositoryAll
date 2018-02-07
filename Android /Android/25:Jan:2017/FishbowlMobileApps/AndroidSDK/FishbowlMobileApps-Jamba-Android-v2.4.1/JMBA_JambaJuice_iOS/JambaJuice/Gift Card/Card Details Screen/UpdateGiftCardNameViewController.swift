//
//  UpdateGiftCardNameViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 25/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK

import SVProgressHUD

//MARK: UpdateGiftCardNameViewController Delegate
protocol UpdateGiftCardNameViewControllerDelegate: class {
    func giftCardNameChanged(_ inCommUserCard:InCommUserGiftCard)
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
        NotificationCenter.default.addObserver(self, selector: #selector(UpdateGiftCardNameViewController.keyboardDidShow(_:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(UpdateGiftCardNameViewController.keyboardWillDismiss(_:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        giftCardName.becomeFirstResponder()
    }
    
    //MARK: - Text field delegates
    func keyboardDidShow(_ notification: Notification) {
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        let keyboardFrame:NSValue = userInfo.value(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
        let keyboardRectangle = keyboardFrame.cgRectValue
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.saveButtonBottomSpace.constant = keyboardRectangle.height
            self.view.layoutIfNeeded()
        }) 
    }
    
    func keyboardWillDismiss(_ notification: Notification) {
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.saveButtonBottomSpace.constant = 0
            self.view.layoutIfNeeded()
        }) 
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
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
        SVProgressHUD.show(withStatus: "Updating...")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService.sharedInstance.updateUserGiftCardName(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: inCommUserCard.cardId, cardName: giftCardName.text?.trim()) { (userGiftCard, error) in
            SVProgressHUD.dismiss()
            if (error != nil) {
                self.presentError(error)
                return
            } else {
                var incommUserGiftCard:InCommUserGiftCard = self.inCommUserCard
                incommUserGiftCard.cardName = (self.giftCardName.text?.trim())!
                incommUserGiftCard.lastModifiedDate = NSDate() as Date
                self.delegate?.giftCardNameChanged(incommUserGiftCard)
                NotificationCenter.default.post(name: NSNotification.Name(rawValue: GiftCardAppConstants.refreshGiftCardHomePage), object: nil)
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
        NotificationCenter.default.removeObserver(self)
    }
}
