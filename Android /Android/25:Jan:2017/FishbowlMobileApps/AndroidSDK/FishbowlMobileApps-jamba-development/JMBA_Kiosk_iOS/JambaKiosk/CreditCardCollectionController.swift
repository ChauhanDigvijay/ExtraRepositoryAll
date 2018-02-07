//
//  CreditCardCollectionController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/18/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit


protocol CreditCardCollectionControllerDelegate {
    func openZipcodeNumberPadPopover(view: UIView)
    func openCVVNumberPadPopover(view: UIView)
    func userSwipedCreditCard(creditCard: SwipedCreditCard)
}

class CreditCardCollectionController: NSObject, UICollectionViewDataSource, UICollectionViewDelegate, DTDeviceDelegate, SwipedCreditCardCollectionViewCellDelegate {

    var delegate: CreditCardCollectionControllerDelegate?

    private var collectionView: UICollectionView?
    private var storedCreditCards: [UserSavedCreditCard] {
        return CreditCardService.sharedInstance.storedCreditCards
    }


    func initialize(collectionView: UICollectionView) {
        self.collectionView = collectionView
        collectionView.delegate = self
        collectionView.dataSource = self

        // Credit Card Scanner initialization
        guard let device = DTDevices.sharedDevice() as? DTDevices else {
            return
        }
        device.delegate = self
        device.connect()

//        // Fake stored card for testing
//        storedCreditCards = [UserSavedCreditCard(accountId: 0, cardType: "American Express", cardSuffix:"5544", expiration: "12/31")]
    }

    func reloadCardsAnimated(animated: Bool) {
        collectionView?.reloadData()

        // Scroll to last item (swiped card)
        let indexPath = NSIndexPath(forItem: storedCreditCards.count, inSection: 0)
        collectionView?.selectItemAtIndexPath(indexPath, animated: animated, scrollPosition: .CenteredHorizontally)
    }

    func loadUserCards(complete: Void -> Void) {
        if UserService.sharedUser != nil {
            CreditCardService.sharedInstance.userCreditCards({ (error) -> Void in
                self.reloadCardsAnimated(false)
                complete()
            })
        }
    }

    func selectedCreditCard() -> Either<SwipedCreditCard, UserSavedCreditCard>? {
        guard let indexPath = collectionView?.indexPathsForSelectedItems()?.first else {
            return nil
        }
        guard let cell = collectionView?.cellForItemAtIndexPath(indexPath) else {
            return nil
        }
        if cell is SwipedCreditCardCollectionViewCell && hasSwipedCard() {
            return Either.Left(CreditCardService.sharedInstance.swipedCreditCard!)
        }
        if cell is StoredCreditCardCollectionViewCell {
            if let card = storedCreditCards.getAt(indexPath.row) {
                return Either.Right(card)
            }
        }
        return nil
    }

    // Programatically triggered, uses delay to allow colection view to reload and animate
    func openZipcodePopover() {
        UIApplication.afterDelay(0.5) {
            let indexPath = NSIndexPath(forItem: self.storedCreditCards.count, inSection: 0)
            if let cell = self.collectionView?.cellForItemAtIndexPath(indexPath) as? SwipedCreditCardCollectionViewCell {
                cell.selectZipcodeField()
            }
        }
    }

    // Programatically triggered, uses delay to allow colection view to reload and animate
    func openCVVPopover() {
        UIApplication.afterDelay(0.5) {
            let indexPath = NSIndexPath(forItem: self.storedCreditCards.count, inSection: 0)
            if let cell = self.collectionView?.cellForItemAtIndexPath(indexPath) as? SwipedCreditCardCollectionViewCell {
                cell.selectCVVField()
            }
        }
    }

    func hasUserCards() -> Bool {
        return storedCreditCards.count > 0
    }

    func hasSwipedCard() -> Bool {
        return CreditCardService.sharedInstance.swipedCreditCard != nil
    }


    // MARK: UICollectionViewDatasource

    func numberOfSectionsInCollectionView(collectionView: UICollectionView) -> Int {
        return 1
    }

    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return storedCreditCards.count + 1
    }

    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        if indexPath.row < storedCreditCards.count {
            guard let cell = collectionView.dequeueReusableCellWithReuseIdentifier("StoredCardCell", forIndexPath: indexPath) as? StoredCreditCardCollectionViewCell else {
                fatalError("Could not load cell")
            }
            guard let card = storedCreditCards.getAt(indexPath.row) else {
                fatalError("Could not get stored card")
            }
            cell.update(card)
            return cell
        } else if let card = CreditCardService.sharedInstance.swipedCreditCard {
            guard let cell = collectionView.dequeueReusableCellWithReuseIdentifier("SwipedCardCell", forIndexPath: indexPath) as? SwipedCreditCardCollectionViewCell else {
                fatalError("Could not load cell")
            }
            cell.update(card)
            cell.delegate = self
            return cell
        } else {
            return collectionView.dequeueReusableCellWithReuseIdentifier("EmptySwipedCardCell", forIndexPath: indexPath)
        }
    }


    // MARK: UICollectionViewDelegate

    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        collectionView.scrollToItemAtIndexPath(indexPath, atScrollPosition: .CenteredHorizontally, animated: true)
        updateCellSelection(collectionView.cellForItemAtIndexPath(indexPath))
    }

    func collectionView(collectionView: UICollectionView, didDeselectItemAtIndexPath indexPath: NSIndexPath) {
        updateCellSelection(collectionView.cellForItemAtIndexPath(indexPath))
    }

    private func updateCellSelection(cell: UICollectionViewCell?) {
        (cell as? StoredCreditCardCollectionViewCell)?.updateSelection()
        (cell as? SwipedCreditCardCollectionViewCell)?.updateSelection()
    }


    // MARK: UIScrollViewDelegate

    func scrollViewWillBeginDragging(scrollView: UIScrollView) {
        SessionExpirationService.sharedInstance.trackUserActivity()
    }

    func scrollViewDidEndDecelerating(scrollView: UIScrollView) {
        SessionExpirationService.sharedInstance.trackUserActivity()
    }


    // MARK: SwipedCreditCardCollectionViewCellDelegate

    func swipedCreditCardEnterZipcode(view: UIView) {
        delegate?.openZipcodeNumberPadPopover(view)
    }

    func swipedCreditCardEnterCVV(view: UIView) {
        delegate?.openCVVNumberPadPopover(view)
    }


    // MARK: DTDeviceDelegate

    func magneticCardData(track1: String!, track2: String!, track3: String!) {
        guard let device = DTDevices.sharedDevice() as? DTDevices else {
            log.error("Card reader: Could not get card reader")
            return
        }
        guard let card = device.msProcessFinancialCard(track1, track2: track2) else {
            log.error("Card reader: Could not get card")
            return
        }
        guard let cardNumber = card["accountNumber"] as? String else {
            log.error("Card reader: Could not get card number")
            return
        }
        guard let firstName = card["firstName"] as? String else {
            log.error("Card reader: Could not get first name")
            return
        }
        guard let lastName = card["lastName"] as? String else {
            log.error("Card reader: Could not get last name")
            return
        }
        guard let month = card["expirationMonth"] as? String else {
            log.error("Card reader: Could not get expiration month")
            return
        }
        guard let year = card["expirationYear"] as? String else {
            log.error("Card reader: Could not get expiration year")
            return
        }

        let swipedCreditCard = SwipedCreditCard(cardNumber: cardNumber, firstName: firstName, lastName: lastName, expirationMonth: month, expirationYear: year, zipcode: nil, cvv: nil)
        CreditCardService.sharedInstance.updateSwipedCard(swipedCreditCard)
        reloadCardsAnimated(true)
        SessionExpirationService.sharedInstance.trackUserActivity()
        delegate?.userSwipedCreditCard(swipedCreditCard)
        openZipcodePopover()
    }

}
