//
//  SignInViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/23/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
import SVProgressHUD


class SignInViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var greenCopy: UILabel!
    @IBOutlet weak var redButtonHeightConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var emailPhoneTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var signInButton: UIButton!
    @IBOutlet weak var forgotPasswordButton: UIButton!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        configureNavigationBar(.lightBlue)
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)
        AnalyticsService.trackEvent("user_account", action: "login_start")
        
        if UIScreen.main.is35inch() {
            greenCopy.removeFromSuperview()
            redButtonHeightConstraint.constant = 0
        }
        
        // If redirected from Sign Up screen, reuse existing info
        if let phone = UserService.signUpUserInfo?.phoneNumber {
            emailPhoneTextField.text = phone
        } else {
            emailPhoneTextField.text = UserService.signUpUserInfo?.emailAddress
        }
        
        if emailPhoneTextField.text!.trim().isEmpty {
            emailPhoneTextField.becomeFirstResponder()
        } else {
            passwordTextField.becomeFirstResponder()
        }
        
        
    }
    
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func signIn(_ sender: UIButton) {
        
        
        
        let appdelegate = UIApplication.shared.delegate as? AppDelegate
        if appdelegate!.isReachable == true
        {
            
            trackButtonPress(sender)
            signIn()
        }
            
        else
        {
            presentOkAlert("NO Internet", message: "You seems to be offline. Please check your Internet Connection.")
            
        }
        
    }
    
    fileprivate func signIn() {
        // Resign First Responder
        view.endEditing(true)
        
        // Validate email not empty
        if emailPhoneTextField.text == nil || emailPhoneTextField.text!.trim().isEmpty {
            presentOkAlert("Email or phone required", message: "Please enter an email address or 10-digit phone number") {
                self.emailPhoneTextField.becomeFirstResponder()
            }
            return
        }
        
        // Validate email or phone
        if emailPhoneTextField.text!.isEmail() == false && emailPhoneTextField.text!.isTenDigitPhone() == false {
            presentOkAlert("Invalid email or phone", message: "Please enter a valid email address or 10-digit phone number") {
                self.emailPhoneTextField.becomeFirstResponder()
            }
            return
        }
        
        // Validate password not empty
        if passwordTextField.text == nil || passwordTextField.text!.trim().isEmpty {
            presentOkAlert("Password required", message: "Please enter a password") {
                self.passwordTextField.becomeFirstResponder()
            }
            return
        }
        
        // No need to check password length on log in
        // Remove non-numeric characters if user entered a phone number
        let username = emailPhoneTextField.text!.isTenDigitPhone() ? emailPhoneTextField.text!.stringByRemovingNonNumericCharacters() : emailPhoneTextField.text!
        
        SVProgressHUD.show(withStatus: "Logging in...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.signInUser(username.trim(), password: passwordTextField.text!) { (user, error) -> Void in
            //            SVProgressHUD.dismiss()
            if error != nil {
                SVProgressHUD.dismiss()
                /// Special case for exisintg Olo users, prompt them to create a new account
                if error!.code == 2000 {
                    self.presentConfirmation("Invalid Credentials", message: "Invalid user name or password.\n\nYou need a Jamba Insider account in order to log in. If you do not have one, please proceed to sign up.", buttonTitle: "Sign Up") { (confirmed) -> Void in
                        if confirmed {
                            if self.emailPhoneTextField.text!.isEmail() {
                                UserService.signUpUserInfo = SignUpUserInfo()
                                UserService.signUpUserInfo?.emailAddress = self.emailPhoneTextField.text!
                            }
                            NonSignedInViewController.sharedInstance().openSignUpScreen();
                            
                        }
                        return
                    }
                    return
                }
                self.presentError(error)
                return
            }
            
            ProductService.deleteAllRecentlyOrderedProducts()
            AnalyticsService.setUserId(self.emailPhoneTextField.text!)
           NonSignedInViewController.sharedInstance().deinitNotificationCenter()
           SignedInMainViewController.sharedInstance().deinitNotificationCenter()
            SVProgressHUD.dismiss()
           // UIApplication.inMainThread {
                self.performSegue(withIdentifier: "SignInSegue", sender: self)
            //}
//           NonSignedInViewController.sharedInstance().deinitNotificationCenter()
//           SignedInMainViewController.sharedInstance().deinitNotificationCenter()
//            FishbowlApiClassService.sharedInstance.fishbowlLogin {
//                SVProgressHUD.dismiss()
//                let appDelegate = UIApplication.shared.delegate as! AppDelegate
//                appDelegate.registerPushNotification()
//                UIApplication.inMainThread {
//                    self.performSegue(withIdentifier: "SignInSegue", sender: self)
//                }
//            }
    }
    

    
    // Convert date to string
    func convertDateToString(_ date:Date)-> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat="yyyy-MM-dd"
        let string = dateFormatter.string(from: date)
        return string;
    }
    // MARK: TextFieldDelegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        trackKeyboardReturn()
        if textField == emailPhoneTextField {
            passwordTextField.becomeFirstResponder()
        }
        else {
            let appdelegate = UIApplication.shared.delegate as? AppDelegate
            if appdelegate!.isReachable == true
            {
                signIn()
            }
            else
            {
                presentOkAlert("NO Internet", message: "You seems to be offline. Please check your Internet Connection.")
                
            }
        }
        return true
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == passwordTextField {
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoPasswordMaxLength)
        }
        else {
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: max(Constants.phoneNumberInputLimit, Constants.spendGoEmailAddressLimit))
        }
    }
    
//    func firstLaunch(){
//        if SettingsManager.setting(.FirstTimeLaunchForUser) != nil{
//            FishbowlApiClassService.sharedInstance.submitMobileAppEvent("FIRST_TIME_LAUNCH")
//            SettingsManager.deleteSetting(.FirstTimeLaunchForUser)
//        }
//    }
    }
    
}
