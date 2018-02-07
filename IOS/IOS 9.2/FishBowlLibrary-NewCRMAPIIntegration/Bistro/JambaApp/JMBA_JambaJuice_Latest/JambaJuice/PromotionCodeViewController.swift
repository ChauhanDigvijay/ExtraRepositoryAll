//
//  PromotionCodeViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 10/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import HDK

class PromotionCodeViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var redButtonHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var continueButton: UIButton!
    @IBOutlet weak var codeTextField: UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()
        codeTextField.becomeFirstResponder()
        

        
        if UIScreen.mainScreen().is35inch() {
            redButtonHeightConstraint.constant = 0
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func addPromotionCode(sender: UIButton) {
        trackButtonPress(sender)
        addPromotionCode()
    }
    
    private func addPromotionCode() {
        view.endEditing(true)

        guard let codeText = codeTextField.text else {
            presentOkAlert("Promotion Code Required", message: "Please enter a promotion code") {
                self.codeTextField.becomeFirstResponder()
            }
            return
        }
        
        if codeText.trim().isEmpty {
            presentOkAlert("Promotion Code Required", message: "Please enter a promotion code") {
                self.codeTextField.becomeFirstResponder()
            }
            return
        }

        continueButton.enabled = false
        SVProgressHUD.showWithStatus("Applying promotion code...", maskType: .Clear)
        BasketService.applyPromotionCode(codeTextField.text!) { (basket, error) -> Void in
            SVProgressHUD.dismiss()
            self.continueButton.enabled = true
            if error != nil {
                self.presentError(error)
                return
            }
            self.popToRootViewController() // Close all the way to basket screen
        }
    }
    
    //MARK: UITextFieldDelegate
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        trackKeyboardReturn()
        addPromotionCode()
        return true
    }
    
}
