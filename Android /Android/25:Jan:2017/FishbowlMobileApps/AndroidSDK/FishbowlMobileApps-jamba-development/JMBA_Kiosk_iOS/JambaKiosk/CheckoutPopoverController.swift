//
//  CheckoutPopoverController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/19/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

class CheckoutPopoverController {

    static let sharedInstance = CheckoutPopoverController()
    var creditCardCollectionController: CreditCardCollectionController?

    func openZipcodePopoverForView(view: UIView, inViewController: UIViewController) {
        let vc = instantiateNumberPadController(sourceView: view, maxLength: 5, initialValue: CreditCardService.sharedInstance.swipedCreditCard?.zipcode ?? "00000", title: "Enter your billing zip code")
        vc.completeCallback = { enteredNumber in
            CreditCardService.sharedInstance.updateSwipedCardZipcode(enteredNumber)
            self.creditCardCollectionController?.reloadCardsAnimated(false)
            self.creditCardCollectionController?.openCVVPopover()
        }
        vc.cancelCallback = {
            self.creditCardCollectionController?.reloadCardsAnimated(false)
        }
        inViewController.presentViewController(vc, animated: true, completion: nil)
    }

    func openCVVPopoverForView(view: UIView, inViewController: UIViewController) {
        let vc = instantiateNumberPadController(sourceView: view, maxLength: 4, initialValue: CreditCardService.sharedInstance.swipedCreditCard?.cvv ?? "000", title: "Enter your card CVV")
        vc.completeCallback = { enteredNumber in
            CreditCardService.sharedInstance.updateSwipedCardCVV(enteredNumber)
            self.creditCardCollectionController?.reloadCardsAnimated(false)
        }
        vc.cancelCallback = {
            self.creditCardCollectionController?.reloadCardsAnimated(false)
        }
        inViewController.presentViewController(vc, animated: true, completion: nil)
    }

    private func instantiateNumberPadController(sourceView sourceView: UIView, maxLength: Int, initialValue: String, title: String) -> NumberPadViewController {
        guard let vc = UIStoryboard(name: "NumberPad", bundle: nil).instantiateInitialViewController() as? NumberPadViewController else {
            fatalError("Could not instantiate number pad popover")
        }
        vc.modalPresentationStyle = .Popover
        let rect = CGRectMake(sourceView.bounds.origin.x - 10, sourceView.bounds.origin.y + 22, 0, 0)
        vc.popoverPresentationController?.sourceView = sourceView
        vc.popoverPresentationController?.sourceRect = rect
        vc.maxLength = maxLength
        vc.initialValue = initialValue
        vc.title = title
        return vc
    }

}
