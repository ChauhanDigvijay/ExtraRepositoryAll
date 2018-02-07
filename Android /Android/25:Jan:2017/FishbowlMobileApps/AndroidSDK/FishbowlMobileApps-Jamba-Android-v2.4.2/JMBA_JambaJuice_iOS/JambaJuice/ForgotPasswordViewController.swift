//
//  ForgotPasswordViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 5/2/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class ForgotPasswordViewController: UIViewController, UITextFieldDelegate, UIAlertViewDelegate {
    
    @IBOutlet weak var greenCopy: UILabel!
    @IBOutlet weak var redButtonHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var emailAddressTextField: UITextField!
    @IBOutlet weak var sendRequestButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.lightBlue)
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)
        emailAddressTextField.becomeFirstResponder()

        if UIScreen.main.is35inch() {
            greenCopy.removeFromSuperview()
            redButtonHeightConstraint.constant = 0
        }
    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func sendPasswordResetRequest(_ sender: UIButton) {
        trackButtonPress(sender)
        sendPasswordResetRequest()
    }
    
    fileprivate func sendPasswordResetRequest() {
        // Resign first responder
        view.endEditing(true)

        guard let emailAddress = emailAddressTextField.text else {
            presentOkAlert("Email required", message: "Please enter an email address") {
                self.emailAddressTextField.becomeFirstResponder()
            }
            return
        }
        
        // Validate email not empty
        if emailAddress.trim().isEmpty {
            presentOkAlert("Email required", message: "Please enter an email address") {
                self.emailAddressTextField.becomeFirstResponder()
            }
            return
        }
            
        // Validate email address
        if !emailAddress.isEmail() {
            presentOkAlert("Invalid email", message: "Please enter a valid email address") {
                self.emailAddressTextField.becomeFirstResponder()
            }
            return
        }
        let appdelgate = UIApplication.shared.delegate as! AppDelegate
        
        if appdelgate.isReachable == true
        {
        
        SVProgressHUD.show(withStatus: "Sending request...")
            SVProgressHUD.setDefaultMaskType(.clear)
        sendRequestButton.isEnabled = false
        UserService.forgotPassword(emailAddressTextField.text!) { (error) in
            SVProgressHUD.dismiss()
            self.sendRequestButton.isEnabled = true
            if error != nil {
                //clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")
                return
            }
            self.sendRequestButton.isHidden = true
            self.performSegue(withIdentifier: "RequestSentSegue", sender: self)
        }
            
        }
        
        else
        {
            presentOkAlert("NO Internet", message: "You seems to be offline. Please check your Internet Connection.")

        }
    }
    
    
    // MARK: - TextFieldDelegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        trackKeyboardReturn()
        sendPasswordResetRequest()
        return true
    }

}
