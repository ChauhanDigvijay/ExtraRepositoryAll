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
        

        
        if UIScreen.main.is35inch() {
            redButtonHeightConstraint.constant = 0
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func addPromotionCode(_ sender: UIButton) {
        trackButtonPress(sender)
        addPromotionCode()
    }
    
    fileprivate func addPromotionCode() {
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
        
        self.validatePromoAndOffer()
        
//        self.presentConfirmationWithYesOrNo("Alert", message: "Only one coupon / reward may be applied at a time. Do you want to remove the existing coupon / reward and apply a new one?", buttonTitle: "Yes") { (confirmed) in
//            if confirmed{
//                BasketService.removePromotionCode({ (basket, error) in
//                    if error != nil{
//                        self.presentError(error)
//                    }else{
//                        self.applyReward(reward)
//                    }
//                })
//            } else{
//                return
//            }
//        }
    }
    
    //MARK: UITextFieldDelegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        trackKeyboardReturn()
        addPromotionCode()
        return true
    }
    
    func applyPromotionCode(){
        continueButton.isEnabled = false
        SVProgressHUD.show(withStatus: "Applying promotion code...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.applyPromotionCode(codeTextField.text!) { (basket, error) -> Void in
            SVProgressHUD.dismiss()
            self.continueButton.isEnabled = true
            if error != nil {
                self.presentError(error)
                return
            }
            self.popToRootViewController() // Close all the way to basket screen
        }
    }
    
    func validatePromoAndOffer(){
        if BasketService.validateAppliedOffer(){
            self.presentConfirmationWithYesOrNo("Alert", message: "Only one coupon / reward may be applied at a time. Do you want to remove the existing coupon / reward and apply a new one?", buttonTitle: "Yes") { (confirmed) in
                if confirmed{
                    BasketService.removePromotionCode({ (basket, error) in
                        if error != nil{
                            self.presentError(error)
                        }else{
                            self.applyPromotionCode()
                        }
                    })
                } else{
                    return
                }
            }
        } else if BasketService.validateAppliedReward(){
            self.presentConfirmationWithYesOrNo("Alert", message: "Only one coupon / reward may be applied at a time. Do you want to remove the existing coupon / reward and apply a new one?", buttonTitle: "Yes") { (confirmed) in
                if confirmed{
                    BasketService.removeRewards({ (basket, error) in
                        if error != nil{
                            self.presentError(error)
                        }else{
                            self.applyPromotionCode()
                        }
                    })
                } else{
                    return
                }
            }
        }
        else{
            self.applyPromotionCode()
        }
    }
}
