//
//  CheckoutViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/9/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

class CheckoutViewController: UIViewController, CreditCardCollectionControllerDelegate, BasketSummaryViewControllerDelegate, GuestUserViewControllerDelegate, GuestWithPhoneViewControllerDelegate, AuthenticatedUserViewControllerDelegate, SignInFormViewControllerDelegate, EmailReceiptViewControllerDelegate {

    @IBOutlet var sessionExpirationLabel: UILabel!
    @IBOutlet var creditCardCollectionView: UICollectionView!
    @IBOutlet var swipeCardCoachMarkView: UIView!
    @IBOutlet var selectCardCoachMarkView: UIView!
    @IBOutlet var checkoutButton: UIButton!

    private var basketSummaryViewController: BasketSummaryViewController?
    private var userStatusContainerViewController: SwappableContainerViewController?

    // Class helpers
    private var creditCardCollectionController = CreditCardCollectionController()
    private var checkoutPopoverController = CheckoutPopoverController()

    private var receiptEmailAddress: String?


    override func viewDidLoad() {
        super.viewDidLoad()

        creditCardCollectionController.delegate = self
        creditCardCollectionController.initialize(creditCardCollectionView)
        checkoutPopoverController.creditCardCollectionController = creditCardCollectionController

        updateUI()

        if UserService.sharedUser == nil {
            showGuestUserPanel()
        } else if RewardService.sharedInstance.availableRewards.count > 0 {
            showAuthenticatedUserPanel()
        } else {
            showAuthenticatedNoRewadsPanel()
        }
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "sessionExpirationUpdate:", name: SessionExpirationService.secondsLeftNotificatioName, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "sessionExpirationReset:", name: SessionExpirationService.sessionResetNotificationName, object: nil)
        creditCardCollectionController.reloadCardsAnimated(true)
    }

    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }

    private func updateUI() {
        sessionExpirationLabel.hidden = true
        swipeCardCoachMarkView.hidden = creditCardCollectionController.hasSwipedCard()
        selectCardCoachMarkView.hidden = (creditCardCollectionController.hasUserCards() == false)
        updateTotals()
    }

    private func updateTotals() {
        basketSummaryViewController?.updateTotals()
        let buttonTitle = String(format: "Submit Order      $%.2f", BasketService.sharedInstance.currentBasket?.total ?? 0)
        checkoutButton.setTitle(buttonTitle, forState: .Normal)
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        switch segue.identifier ?? "" {
        case "UserStatusContainerSegue":
            userStatusContainerViewController = segue.destinationViewController as? SwappableContainerViewController
        case "BasketSummaryContainerSegue":
            basketSummaryViewController = segue.destinationViewController as? BasketSummaryViewController
            basketSummaryViewController?.delegate = self
        case "LoginFormSegue":
            let vc = segue.destinationViewController as? SignInFormViewController
            vc?.delegate = self
        case "EmailReceiptSegue":
            let vc = segue.destinationViewController as? EmailReceiptViewController
            vc?.delegate = self
        case "OrderCompleteSegue":
            let vc = segue.destinationViewController as? OrderConfirmationViewController
            vc?.emailReceiptSent = (UserService.sharedUser != nil || receiptEmailAddress != nil)
        default:
            break
        }
    }


    // MARK: - Navigation

    @IBAction func backToMenu(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        popViewController()
    }

    @IBAction func cancelOrder(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        performSegueWithIdentifier("CancelOrderSegue", sender: self)
    }

    @IBAction func submitOrder(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        if UserService.sharedUser == nil {
            submitOrderAsGuest()
        } else {
            submitOrder()
        }
    }

    private func submitOrderAsGuest() {
        guard let creditCard = CreditCardService.sharedInstance.swipedCreditCard else {
            presentOkAlert("Error", message: "Please swipe a credit or debit card to proceed with the checkout.")
            return
        }
        if validateSwipedCard(creditCard) {
            performSegueWithIdentifier("EmailReceiptSegue", sender: self)
        }
    }

    private func submitOrder() {
        guard let selectedCard = creditCardCollectionController.selectedCreditCard() else {
            presentOkAlert("Error", message: "Please swipe a credit or debit card to proceed with the checkout.")
            return
        }
        switch selectedCard {
        case .Left(let creditCard):  submitOrderWithSwipedCard(creditCard)
        case .Right(let creditCard): submitOrderWithSavedCreditCard(creditCard)
        }
    }

    private func validateSwipedCard(creditCard: SwipedCreditCard) -> Bool {
        if creditCard.zipcode == nil {
            presentError(NSError(description: "Please enter your card billing zip code."))
            return false
        }
        if creditCard.zipcode?.characters.count != 5 {
            presentError(NSError(description: "Zip code must be 5 digits long."))
            return false
        }
        if creditCard.cvv == nil {
            presentError(NSError(description: "Please enter your card CVV code."))
            return false
        }
        if creditCard.cvv?.characters.count < 3 {
            presentError(NSError(description: "CVV code must be 3 or 4 digits long."))
            return false
        }
        return true
    }

    private func submitOrderWithSwipedCard(creditCard: SwipedCreditCard) {
        if validateSwipedCard(creditCard) == false {
            return
        }
        SVProgressHUD.showWithStatus("Processing order...", maskType: .Black)
        BasketService.sharedInstance.placeOrderWithSwipedCreditCard(creditCard, receiptEmailAddress: receiptEmailAddress) { orderStatus, error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            self.performSegueWithIdentifier("OrderCompleteSegue", sender: self)
        }
    }

    private func submitOrderWithSavedCreditCard(creditCard: UserSavedCreditCard) {
        SVProgressHUD.showWithStatus("Processing order...", maskType: .Black)
        BasketService.sharedInstance.placeOrderWithSavedCreditCard(creditCard) { orderStatus, error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            self.performSegueWithIdentifier("OrderCompleteSegue", sender: self)
        }
    }

    @IBAction func fakeSwipeCard(sender: UIButton) {
        SessionExpirationService.sharedInstance.trackUserActivity()
        let swipedCreditCard = SwipedCreditCard(cardNumber: "4111111111111111", firstName: "You R.", lastName: "Awesome", expirationMonth: "12", expirationYear: "16", zipcode: nil, cvv: nil)
        CreditCardService.sharedInstance.updateSwipedCard(swipedCreditCard)
        creditCardCollectionController.reloadCardsAnimated(true)
        creditCardCollectionController.openZipcodePopover()
        updateUI()
    }


    // MARK: EmailReceiptViewControllerDelegate

    func userEnteredReceiptEmailAddress(emailAddress: String?) {
        receiptEmailAddress = emailAddress
        submitOrder()
        dismissModalController()
    }


    // MARK: BasketSummaryViewControllerDelegate

    func didRemoveProductFromBasket() {
        updateTotals()
        if let basket = BasketService.sharedInstance.currentBasket {
            if basket.products.count == 0 {
                popViewController()
            }
        }
    }

    func didEmptyBasket() {
        SessionExpirationService.sharedInstance.trackUserActivity()
        popViewController()
    }


    // MARK: UserStatusContainer Swapping

    func showGuestUserPanel() {
        userStatusContainerViewController?.swapToViewControllerWithSegueIdentifier("GuestUserSegue", viewControllerWillLoad: { viewController in
            let vc = viewController as? GuestUserViewController
            vc?.delegate = self
        })
    }

    func showGuestWithPhoneUserPanel() {
        userStatusContainerViewController?.swapToViewControllerWithSegueIdentifier("GuestWithPhoneSegue", viewControllerWillLoad: { viewController in
            let vc = viewController as? GuestWithPhoneViewController
            vc?.delegate = self
        })
    }

    func showAuthenticatedUserPanel() {
        userStatusContainerViewController?.swapToViewControllerWithSegueIdentifier("AuthenticatedUserSegue", viewControllerWillLoad: { viewController in
            let vc = viewController as? AuthenticatedUserViewController
            vc?.delegate = self
        })
    }

    func showAuthenticatedNoRewadsPanel() {
        userStatusContainerViewController?.swapToViewControllerWithSegueIdentifier("AuthenticatedNoRewardsSegue", viewControllerWillLoad: { viewController in
            // Nothing to do
        })
    }


    // MARK: GuestUserViewControllerDelegate

    func authenticateUser() {
        performSegueWithIdentifier("LoginFormSegue", sender: self)
    }


    // MARK: AuthenticatedUserViewControllerDelegate

    func applyRewardToOrder(reward: Reward) {
        SVProgressHUD.showWithStatus("Applying reward...", maskType: .Black)
        RewardService.sharedInstance.applyReward(reward) { error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            self.updateTotals()
        }
    }

    func removeRewardFromOrder() {
        SVProgressHUD.showWithStatus("Removing reward...", maskType: .Black)
        RewardService.sharedInstance.removeRewards { error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            self.updateTotals()
        }
    }


    // MARK: SignInFormViewControllerDelegate

    func authenticateUser(username: String, password: String) {
        SVProgressHUD.showWithStatus("Authenticating user...", maskType: .Black)
        UserService.signInUser(username, password: password) { user, error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            self.userLoggedIn()
        }
    }

    private func userLoggedIn() {
        SVProgressHUD.showWithStatus("Loading available rewards...", maskType: .Black)
        RewardService.sharedInstance.loadAvailableRewards { rewards, error in
            if let reward = rewards.first {
                RewardService.sharedInstance.applyReward(reward) { error in
                    SVProgressHUD.dismiss()
                    if error != nil {
                        // Ignore error, treat as no rewards
                        self.showAuthenticatedNoRewadsPanel()
                    } else {
                        self.showAuthenticatedUserPanel()
                        self.updateTotals()
                    }
                }
            } else {
                SVProgressHUD.dismiss()
                self.showAuthenticatedNoRewadsPanel()
            }
        }

        creditCardCollectionController.loadUserCards {
            self.updateUI()
        }
    }


    // MARK: CreditCardCollectionControllerDelegate

    func openZipcodeNumberPadPopover(view: UIView) {
        checkoutPopoverController.openZipcodePopoverForView(view, inViewController: self)
    }

    func openCVVNumberPadPopover(view: UIView) {
        checkoutPopoverController.openCVVPopoverForView(view, inViewController: self)
    }

    func userSwipedCreditCard(creditCard: SwipedCreditCard) {
        updateUI()
    }


    // MARK: Notifications

    func sessionExpirationReset(notification: NSNotification) {
        sessionExpirationLabel.hidden = true
    }

    func sessionExpirationUpdate(notification: NSNotification) {
        let secondsLeft: Int = notification.userInfo?[SessionExpirationService.secondsLeftKey] as? Int ?? 0
        sessionExpirationLabel.hidden = false
        sessionExpirationLabel.text = "Your session will expire in \(secondsLeft) seconds..."
    }

}
