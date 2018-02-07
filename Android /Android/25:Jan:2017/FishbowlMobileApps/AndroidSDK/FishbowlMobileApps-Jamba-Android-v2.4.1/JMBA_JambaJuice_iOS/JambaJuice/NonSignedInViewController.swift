//
//  NonSignedInViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/16/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
import SVProgressHUD

private var instance: NonSignedInViewController?

class NonSignedInViewController: UIViewController {
    
    @IBOutlet weak var logoImageView: UIImageView!
    
    @IBOutlet weak var signUpLabel: UILabel?
    
    
    class func sharedInstance() -> NonSignedInViewController {
        if instance == nil {
            instance = NonSignedInViewController() // Should never be called
        }
        return instance!
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        instance = self
        
        // Start Reorder notification start an new order for changed store
        NotificationCenter.default.addObserver(self, selector: #selector(NonSignedInViewController.startReorder), name: NSNotification.Name(rawValue: JambaNotification.ReStartOrderForGuest.rawValue), object: nil)
        
        // Basket transfer notification refresh menu for changed store
        NotificationCenter.default.addObserver(self, selector: #selector(NonSignedInViewController.basketTransferred), name: NSNotification.Name(rawValue: JambaNotification.BasketTransferredForGuest.rawValue), object: nil)
        
        
        
        // Attach basket controller to main window
        BasketFlagViewController.sharedInstance.attachToMainWindow()
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        
        let signUpLabelString:NSString = "Don't have an account? Sign Up";
        var signUpLabelMutableString = NSMutableAttributedString();
        signUpLabelMutableString  = NSMutableAttributedString(string: signUpLabelString as String, attributes: [NSFontAttributeName:UIFont.init(name: "Archer-Medium", size: 14.0)!])
        
        let color = UIColor(red: 201.0/255.0, green: 54.0/255.0, blue: 47.0/255.0, alpha: 1.0);
        
        signUpLabelMutableString.addAttribute(NSForegroundColorAttributeName, value: color, range: NSRange(location:23,length:7))
        
        signUpLabel?.attributedText=signUpLabelMutableString;
        
        // Observer for quick action shortcut item
        NotificationCenter.default.addObserver(self, selector: #selector(NonSignedInViewController.openOrderMenuForShortCutItem), name: NSNotification.Name(rawValue: JambaNotification.OrderMenuWhenNonSignedIn.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(NonSignedInViewController.openStoreLoctionForShortCutItem), name: NSNotification.Name(rawValue: JambaNotification.LocationSearchWhenNonSignedIn.rawValue), object: nil)
        
        // trigger fuction when app suspended state to active state
        let appdelegate = (UIApplication.shared.delegate) as! AppDelegate
        if appdelegate.currentShortCutItem == JambaNotification.OrderMenuWhenNonSignedIn {
            openOrderMenuForShortCutItem()
        }else if appdelegate.currentShortCutItem == JambaNotification.LocationSearchWhenNonSignedIn{
            openStoreLoctionForShortCutItem()
        }
    }
    
    @IBAction func startOrderButtonClicked(){
        trackButtonPressWithName("OrderNow")
        // Validate current store is nil or not
        if CurrentStoreService.sharedInstance.currentStore == nil {
            self.performSegue(withIdentifier: "StoreLocatorSegue", sender: self)
        }
            // Validate current store contain feature products or not
        else{
            CurrentStoreService.sharedInstance.loadAds { (adsList, error) in
                //check is there any ads or feature products
                if CurrentStoreService.sharedInstance.storeBasedFeatureProducts.count>0 || adsList.adsDetailList.count > 0 {
                    self.performSegue(withIdentifier: "ProductMenuSegue", sender: self)
                }
                else{
                    self.performSegue(withIdentifier: "FullMenuSegue", sender: self)
                }
            }
        }
    }
    
    // Closes a main navigation screen, with completion callback
    func closeModalScreen(_ complete: (() -> Void)? = nil) {
        if presentedViewController == nil {
            complete?()
            return
        }
        dismiss(animated: true) { () -> Void in
            complete?()
        }
    }
    
    /// Open Menu Landign screen, closing other menu screens if needed
    func openOrderScreen() {
        closeModalScreen() {
            self.performSegue(withIdentifier: "FullMenuSegue", sender: self)
        }
    }
    
    // Open Product Menu Screen
    func openProductMenuScreen() {
        closeModalScreen() {
            self.performSegue(withIdentifier: "ProductMenuSegue", sender: self)
        }
    }
    
    // Opne Login Screen
    func openLoginScreen(){
        closeModalScreen(){
            self.performSegue(withIdentifier: "LoginSegue", sender: self)
        }
    }
    
    // Open Order screen with Products
    func openOrderScreenWithProducts() {
        closeModalScreen(){
            self.startOrderButtonClicked()
        }
    }
    
    // Open Signup Screen
    func openSignUpScreen(){
        closeModalScreen(){
            self.performSegue(withIdentifier: "SignUpSegue", sender: self)
        }
    }
    
    // Terms and FeedBack Screen
    @IBAction func openTermsAndFeedback() {
        // trackButtonPressWithName("Terms and Conditions")
        performSegue(withIdentifier: "Terms&Feedback", sender: self)
    }
    
    // Re-order
    func startReorder() {
        closeModalScreen() {
            self.openWhenRestartOrder()
        }
    }
    
    // Basket Successfully transferred
    func basketTransferred(){
        closeModalScreen()
    }
    
    // Basket transfer
    func openWhenRestartOrder(){
        CurrentStoreService.sharedInstance.loadAds { (adsList, error) in
            //check is there any ads or feature products
            if CurrentStoreService.sharedInstance.storeBasedFeatureProducts.count>0 || adsList.adsDetailList.count > 0 {
                self.performSegue(withIdentifier: "ProductMenuSegue", sender: self)
            }
            else{
                self.performSegue(withIdentifier: "FullMenuSegue", sender: self)
            }
        }
        
    }
    
    // Prepare a segue for store locator screen navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "StoreLocatorSegue"){
            let nc = segue.destination as! UINavigationController
            let storeLocatorViewController = nc.viewControllers[0] as! StoreLocatorViewController
            // To show order ahead supported stores
            storeLocatorViewController.storeSearchTypeOrderAhead = true
        }
    }
    
    // Remove observer
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    func deinitNotificationCenter() {
        NotificationCenter.default.removeObserver(self)
    }
    
    // ShortCutItme Methods
    func openStoreLoctionForShortCutItem(){
        let appdelegate = (UIApplication.shared.delegate) as! AppDelegate
        appdelegate.currentShortCutItem = JambaNotification.None
        closeModalScreen(){
            self.performSegue(withIdentifier: "StoreLocatorSegue", sender: self)
        }
    }
    func openOrderMenuForShortCutItem(){
        let appdelegate = (UIApplication.shared.delegate) as! AppDelegate
        appdelegate.currentShortCutItem = JambaNotification.None
        closeModalScreen(){
            self.startOrderButtonClicked()
        }
    }
}
