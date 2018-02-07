//
//  AddExistingCardViewController.swift
//
//  Created by vThink on 6/7/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import HDK
import SVProgressHUD

class AddExistingGiftCardViewController: UIViewController,UITextFieldDelegate{
    @IBOutlet weak var giftCardNumberTextField      :   UITextField!
    @IBOutlet weak var giftCardPinNumberTextField   :   UITextField!
    @IBOutlet weak var buttonBottomSpaceConstraint  :   NSLayoutConstraint!
    var inCommCard                                  :   InCommCard!
    var existingCardViewControllerDelegate          :   ExistingGiftCardDetailsViewControllerDelegate?
    
    // MARK: - View did load
    override func viewDidLoad() {
        giftCardPinNumberTextField.delegate     = self
        giftCardNumberTextField.delegate        = self
        
        NotificationCenter.default.addObserver(self, selector: #selector(AddExistingGiftCardViewController.keyBoardWillShown(_:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        
        NotificationCenter.default.addObserver(self, selector:#selector(AddExistingGiftCardViewController.keyBoardWillHidden(_:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        
        super.viewDidLoad()
    }
    
    // MARK: - Did receive memory warning
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: - Close button pressed
    @IBAction func close(){
        self.dismiss(animated: true, completion: nil)
    }
    
    // MARK: - Check balance button pressed
    @IBAction func checkBalanceButtonPressed(_ sender:UIButton){
        validateGiftCardExistsOrNot()
    }
    
    // MARK: - Validate card exists or not
    func validateGiftCardExistsOrNot(){
        if(giftCardNumberTextField.text == nil || giftCardNumberTextField.text!.isEmpty){
            presentOkAlert("Alert", message: "Please enter your gift card number",callback: {
                self.giftCardNumberTextField.becomeFirstResponder()
            })
            return
        }
        else if validateGiftCardNumber() == false{
            presentOkAlert("Alert", message: "Please enter valid gift card number", callback: {
                self.giftCardNumberTextField.becomeFirstResponder()
            })
            return
        }
        else if (giftCardPinNumberTextField.text == nil || giftCardPinNumberTextField.text!.isEmpty){
            presentOkAlert("Alert", message: "Please enter your gift card pin number",callback: {
                self.giftCardPinNumberTextField.becomeFirstResponder()
            })
            return
        }
        else if validateGiftCardPinNumber() == false{
            presentOkAlert("Alert", message: "Please enter valid gift card pin number", callback: {
                self.giftCardPinNumberTextField.becomeFirstResponder()
            })
            return
        }
        else{
            giftCardNumberTextField.resignFirstResponder()
            giftCardPinNumberTextField.resignFirstResponder()
            SVProgressHUD.show(withStatus: "Getting Card Details...")
            SVProgressHUD.setDefaultMaskType(.clear)
            GiftCardCreationService.sharedInstance.getExistingGiftCardDetails(InCommGiftCardBrandDetails.sharedInstance.brandID, cardNumber: giftCardNumberTextField.text!, cardPin: giftCardPinNumberTextField.text!, lastBalance: true, callback: { (card, error) in
                SVProgressHUD.dismiss()
                if error != nil{
                    self.presentError(error)
                    return
                }
                else{
                    self.inCommCard = card
                    self.performSegue(withIdentifier: "ExistingGiftCardDetailsSegue", sender: self)
                    return
                }
            })
            
         /*   InCommCardsService.card(InCommGiftCardBrandDetails.sharedInstance.brandID, cardNumber: giftCardNumberTextField.text!, pin: giftCardPinNumberTextField.text, getLatestBalance: true) { (card, error) in
               
                if let error = error {
                    if error.code == GiftCardAppConstants.errorCodeInvalidUser {
                        InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                            if inCommUserStatus{
                                InCommCardsService.card(InCommGiftCardBrandDetails.sharedInstance.brandID, cardNumber: self.giftCardNumberTextField.text!, pin: self.giftCardPinNumberTextField.text, getLatestBalance: true) { (card, error) in
                                    if error != nil{
                                        self.presentError(error)
                                        return
                                    } else {
                                        self.inCommCard = card
                                        self.performSegueWithIdentifier("ExistingGiftCardDetailsSegue", sender: self)
                                        return
                                    }
                                }
                            }
                            else{
                                self.presentError(GiftCardAppConstants.generalError)
                                return
                            }
                        })
                    } else {
                        self.presentError(error)
                        return
                    }
                } else {
                    self.inCommCard = card
                    self.performSegueWithIdentifier("ExistingGiftCardDetailsSegue", sender: self)
                }
            } */
        }
    }
    
    // MARK: - Dismiss keyboard
    @IBAction func dismissKeyBoard(){
        self.giftCardPinNumberTextField.resignFirstResponder()
        self.giftCardNumberTextField.resignFirstResponder()
    }
    
    //MARK: - Text field delegates
    //calculate keyboard height 7 move the table view
    func keyBoardWillShown(_ notification:Notification){
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        let keyboardFrame:NSValue = userInfo.value(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.buttonBottomSpaceConstraint.constant = keyboardFrame.cgRectValue.height
            self.view.layoutIfNeeded()
        }) 
    }
    
    //validate refill amount
    func keyBoardWillHidden(_ notification:Notification){
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.buttonBottomSpaceConstraint.constant = 0
            self.view.layoutIfNeeded()
        }) 
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField == giftCardNumberTextField{
            giftCardPinNumberTextField.becomeFirstResponder()
        }
        else if textField == giftCardPinNumberTextField{
            validateGiftCardExistsOrNot()
        }
        return true
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if string == " "{
            return false
        } else if string == "" {
            return true
        } else if ((textField.text?.length)! >= GiftCardAppConstants.giftCardMaximumNumberOfDigits) {
            return false
        } else{
            return true
        }
    }
    
    // MARK: - Validate CardNumber
    func validateGiftCardNumber() -> Bool{
        let regexText = NSPredicate(format: "SELF MATCHES %@", "^[a-zA-Z0-9]+$")
        return regexText.evaluate(with: giftCardNumberTextField.text)
    }
    
    // MARK: - Validate Gift Card Pin Number
    func validateGiftCardPinNumber() -> Bool{
        let regexText = NSPredicate(format: "SELF MATCHES %@", "^[0-9]+$")
        return regexText.evaluate(with: giftCardPinNumberTextField.text)
    }
    
    // MARK: - Prepare for segue
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ExistingGiftCardDetailsSegue"{
            let destinationViewController = segue.destination as! ExistingGiftCardDetailsViewController
            destinationViewController.inCommCard = self.inCommCard
            destinationViewController.delegate = existingCardViewControllerDelegate
        }
    }
    
    //MARK: - deinit
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}
