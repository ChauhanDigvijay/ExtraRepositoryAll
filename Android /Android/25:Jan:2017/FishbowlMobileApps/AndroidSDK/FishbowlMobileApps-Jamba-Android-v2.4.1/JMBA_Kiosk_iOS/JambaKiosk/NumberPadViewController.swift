//
//  NumberPadViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/9/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit


class NumberPadViewController: UIViewController {

    @IBOutlet var titleLabel: UILabel!
    @IBOutlet var numberLabel: UILabel!

    // Caller VC should configure these properties
    var initialValue: String?
    var maxLength: Int = 0
    var completeCallback: ((enteredNumber: String?) -> Void)?
    var cancelCallback: (Void -> Void)?

    // Flag to indicate if user has typed or deleted a value
    private var hasInitialValue: Bool = true

    // Flag to indicate user hit the Enter button
    private var hasEnteredValue: Bool = false


    override func viewDidLoad() {
        super.viewDidLoad()
        titleLabel.text = title?.uppercaseString
        numberLabel.text = initialValue ?? ""
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        SessionExpirationService.sharedInstance.trackUserActivity()
    }

    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(animated)
        if hasEnteredValue == false {
            cancelCallback?()
        }
    }

    @IBAction func didEnterNumber(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        if hasInitialValue {
            numberLabel.text = ""
            hasInitialValue = false
        }
        if numberLabel.text?.characters.count < maxLength {
            let currentValue = numberLabel.text ?? ""
            let newDigit = sender.titleLabel?.text ?? ""
            numberLabel.text = currentValue + newDigit
        }
    }

    @IBAction func didRemoveNumber(sender: UIButton) {
        trackButtonPressWithName("Backspace")
        SessionExpirationService.sharedInstance.trackUserActivity()
        if hasInitialValue {
            hasInitialValue = false
        }
        if numberLabel.text?.characters.count > 0 {
            let currentValue = numberLabel.text ?? ""
            numberLabel.text = currentValue.substringToIndex(currentValue.endIndex.predecessor())
        }
    }

    @IBAction func finishEnteringNumber(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        hasEnteredValue = true
        completeCallback?(enteredNumber: numberLabel.text)
        dismissModalController()
    }

}
