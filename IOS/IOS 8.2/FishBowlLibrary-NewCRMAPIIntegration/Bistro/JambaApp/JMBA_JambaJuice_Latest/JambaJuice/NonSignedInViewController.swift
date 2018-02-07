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
    
    
    
    override func viewWillAppear(animated: Bool) {
        self.updateStuff()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        instance = self
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(NonSignedInViewController.updateStuff), name: "appDidBecomeActive", object: nil)
        
        // Start Reorder notification start an new order for changed store
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(NonSignedInViewController.startReorder), name: JambaNotification.ReStartOrderForGuest.rawValue, object: nil)
        
        // Basket transfer notification refresh menu for changed store
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(NonSignedInViewController.basketTransferred), name: JambaNotification.BasketTransferredForGuest.rawValue, object: nil)
        
        // Attach basket controller to main window
        BasketFlagViewController.sharedInstance.attachToMainWindow()
        StatusBarStyleManager.pushStyle(.Default, viewController: self)
        
        let signUpLabelString:NSString = "Don't have an account? Sign Up";
        var signUpLabelMutableString = NSMutableAttributedString();
        signUpLabelMutableString  = NSMutableAttributedString(string: signUpLabelString as String, attributes: [NSFontAttributeName:UIFont(name: "Archer-Medium", size: 14.0)!])
        
        let color = UIColor(red: 201.0/255.0, green: 54.0/255.0, blue: 47.0/255.0, alpha: 1.0);
        
        signUpLabelMutableString.addAttribute(NSForegroundColorAttributeName, value: color, range: NSRange(location:23,length:7))
        
        signUpLabel?.attributedText=signUpLabelMutableString;
    }
    
    func updateStuff()
    {
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
        if appdelegate?.isPushOpen == true
        {
            self.startOrderButtonClicked()
        }
    }
    
    @IBAction func startOrderButtonClicked(){
        trackButtonPressWithName("OrderNow")
        // Validate current store is nil or not
        if CurrentStoreService.sharedInstance.currentStore == nil {
            self.performSegueWithIdentifier("StoreLocatorSegue", sender: self)
        }
            // Validate current store contain feature products or not
        else{
            if CurrentStoreService.sharedInstance.storeBasedFeatureProducts.count>0 {
                self.performSegueWithIdentifier("ProductMenuSegue", sender: self)
            }
            else{
                self.performSegueWithIdentifier("FullMenuSegue", sender: self)
            }
        }
    }
    
    // Closes a main navigation screen, with completion callback
    func closeModalScreen(complete: (() -> Void)? = nil) {
        if presentedViewController == nil {
            complete?()
            dismissViewControllerAnimated(true) { () -> Void in
                complete?()
            }
            return
        }
        dismissViewControllerAnimated(true) { () -> Void in
            complete?()
        }
    }
    
    /// Open Menu Landign screen, closing other menu screens if needed
    func openOrderScreen() {
        closeModalScreen() {
            self.performSegueWithIdentifier("FullMenuSegue", sender: self)
        }
    }
    
    // Open Product Menu Screen
    func openProductMenuScreen() {
        closeModalScreen() {
            self.performSegueWithIdentifier("ProductMenuSegue", sender: self)
        }
    }
    
    // Opne Login Screen
    func openLoginScreen(){
        closeModalScreen(){
            self.performSegueWithIdentifier("LoginSegue", sender: self)
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
            self.performSegueWithIdentifier("SignUpSegue", sender: self)
        }
    }
    
    // Terms and FeedBack Screen
    @IBAction func openTermsAndFeedback() {
        // trackButtonPressWithName("Terms and Conditions")
        performSegueWithIdentifier("Terms&Feedback", sender: self)
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
        if CurrentStoreService.sharedInstance.storeBasedFeatureProducts.count>0 {
            self.performSegueWithIdentifier("ProductMenuSegue", sender: self)
        }
        else{
            self.performSegueWithIdentifier("FullMenuSegue", sender: self)
        }
    }
    
    // Prepare a segue for store locator screen navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if(segue.identifier == "StoreLocatorSegue"){
            let nc = segue.destinationViewController as! UINavigationController
            let storeLocatorViewController = nc.viewControllers[0] as! StoreLocatorViewController
            // To show order ahead supported stores
            storeLocatorViewController.storeSearchTypeOrderAhead = true
        }
    }
    
    // Remove observer
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
}
