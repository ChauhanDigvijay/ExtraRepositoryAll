//
//  BasketFlagViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 28/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class BasketFlagViewController: UIViewController {
    
    static let sharedInstance = BasketFlagViewController.makeBasketFlagViewController()
    static let basketFlagViewSize = CGSizeMake(88, 56)

    @IBOutlet weak var tapGestureRecognizer: UITapGestureRecognizer!
    @IBOutlet weak var swipeLeftGestureRecognizer: UISwipeGestureRecognizer!
    @IBOutlet weak var bgImageView: UIImageView!
    @IBOutlet weak var badgeLabel: UILabel!

    private var basketNavigationController: UINavigationController?
    
    class func makeBasketFlagViewController() -> BasketFlagViewController {
        return UIViewController.instantiate("BasketFlagViewController", storyboard: "Main") as! BasketFlagViewController
    }
    
    /// Only called when app closes
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        badgeLabel.hidden = true
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(BasketFlagViewController.sharedBasketUpdated(_:)), name: JambaNotification.SharedBasketUpdated.rawValue, object: nil)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    /// Attach basket flag to main application window
    func attachToMainWindow() {
        let window = UIApplication.sharedApplication().delegate!.window!!
        window.addSubview(view)
        view.frame = CGRectMake(window.bounds.width - BasketFlagViewController.basketFlagViewSize.width, 120, BasketFlagViewController.basketFlagViewSize.width, BasketFlagViewController.basketFlagViewSize.height)
        view.hidden = false
        updateUI()
    }
    
    @IBAction func openBasketVC(sender: UIGestureRecognizer) {
        trackGesture(sender)
        openBasketVC()
    }
    
    func openBasketVC() {
        view.hidden = true
        // Keep reference to the controller
        basketNavigationController = BasketNavigationController.showBasketScreen()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(BasketFlagViewController.basketVCDismissed(_:)), name: JambaNotification.BasketNCDismissed.rawValue, object: nil)
    }
    
    func sharedBasketUpdated(notification: NSNotification) {
        updateUI()
    }
    
    func basketVCDismissed(notification: NSNotification) {
        basketNavigationController = nil
        NSNotificationCenter.defaultCenter().removeObserver(self, name: JambaNotification.BasketNCDismissed.rawValue, object: nil)
        updateUI()
    }
    
    private func updateUI() {
        view.superview!.bringSubviewToFront(view)
        if BasketService.sharedBasket == nil {
            view.hidden = true
            return
        }

        let productCount = BasketService.itemsInBasket()
        // Do not show empty basket
        if productCount < 1 {
            view.hidden = true
            return
        }
        
        badgeLabel.text = "\(productCount)"
        badgeLabel.hidden = productCount < 1

        view.hidden = basketNavigationController != nil
    }
    
    //MARK: Hide flag
     func hideFlag(){
        view.hidden = true
    }
    
    //MARK: Show flag
    func showFlag(){
        updateUI()
    }
    

}
