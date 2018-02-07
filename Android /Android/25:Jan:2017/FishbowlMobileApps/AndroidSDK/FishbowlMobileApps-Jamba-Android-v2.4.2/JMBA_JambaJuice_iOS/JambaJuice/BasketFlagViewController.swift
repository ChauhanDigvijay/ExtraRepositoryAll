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
    static let basketFlagViewSize = CGSize(width: 88, height: 56)

    @IBOutlet weak var tapGestureRecognizer: UITapGestureRecognizer!
    @IBOutlet weak var swipeLeftGestureRecognizer: UISwipeGestureRecognizer!
    @IBOutlet weak var bgImageView: UIImageView!
    @IBOutlet weak var badgeLabel: UILabel!

    fileprivate var basketNavigationController: UINavigationController?
    
    class func makeBasketFlagViewController() -> BasketFlagViewController {
        return UIViewController.instantiate("BasketFlagViewController", storyboard: "Main") as! BasketFlagViewController
    }
    
    /// Only called when app closes
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        badgeLabel.isHidden = true
        NotificationCenter.default.addObserver(self, selector: #selector(BasketFlagViewController.sharedBasketUpdated(_:)), name: NSNotification.Name(rawValue: JambaNotification.SharedBasketUpdated.rawValue), object: nil)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    /// Attach basket flag to main application window
    func attachToMainWindow() {
        let window = UIApplication.shared.delegate!.window!!
        window.addSubview(view)
        view.frame = CGRect(x: window.bounds.width - BasketFlagViewController.basketFlagViewSize.width, y: 120, width: BasketFlagViewController.basketFlagViewSize.width, height: BasketFlagViewController.basketFlagViewSize.height)
        view.isHidden = false
        updateUI()
    }
    
    @IBAction func openBasketVC(_ sender: UIGestureRecognizer) {
        trackGesture(sender)
        openBasketVC()
    }
    
    func openBasketVC() {
        view.isHidden = true
        // Keep reference to the controller
        basketNavigationController = BasketNavigationController.showBasketScreen()
        NotificationCenter.default.addObserver(self, selector: #selector(BasketFlagViewController.basketVCDismissed(_:)), name: NSNotification.Name(rawValue: JambaNotification.BasketNCDismissed.rawValue), object: nil)
    }
    
    @objc func sharedBasketUpdated(_ notification: Notification) {
        updateUI()
    }
    
    @objc func basketVCDismissed(_ notification: Notification) {
        basketNavigationController = nil
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name(rawValue: JambaNotification.BasketNCDismissed.rawValue), object: nil)
        updateUI()
    }
    
    fileprivate func updateUI() {
        view.superview!.bringSubview(toFront: view)
        if BasketService.sharedBasket == nil {
            view.isHidden = true
            return
        }

        let productCount = BasketService.itemsInBasket()
        // Do not show empty basket
        if productCount < 1 {
            view.isHidden = true
            return
        }
        
        badgeLabel.text = "\(productCount)"
        badgeLabel.isHidden = productCount < 1

        view.isHidden = basketNavigationController != nil
    }
    
    //MARK: Hide flag
     func hideFlag(){
        view.isHidden = true
    }
    
    //MARK: Show flag
    func showFlag(){
        updateUI()
    }
    

}
