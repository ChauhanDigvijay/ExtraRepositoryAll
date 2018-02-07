//
//  UIViewController+HDK.swift
//  HDK
//
//  Created by Eneko Alonso on 12/19/14.
//  Copyright (c) 2014 hathway. All rights reserved.
//

import UIKit

extension UIViewController {

    /// Instantiate a view controller from a storyboard
    /// - parameter identifier: String Identifier of the storyboard to be instantiated
    /// - parameter storyboard: String Storyboard name
    /// - returns: UIViewController Instance of the view controller
    @objc public class func instantiate(_ identifier: String, storyboard: String = "Main") -> UIViewController {
        let board = UIStoryboard(name: storyboard, bundle: nil)
        return board.instantiateViewController(withIdentifier: identifier) 
    }

    /// Segue for unwinding push transitions (popping controllers)
    /// Usage: Connect an action to the Exit controller
    /// - parameter sender: Storyboard segue to unwind
    @IBAction public func unwindToViewController (_ sender: UIStoryboardSegue) {}

    /// Segue for unwinding modal controllers
    /// Usage: Connect an action to the view controller
    @IBAction public func dismissModalController() {
        view.endEditing(true)
        self.dismiss(animated: true, completion: nil)
    }
    
    /// Pop current view controller
    /// Usage: Connect an action to the view controller
    @IBAction public func popViewController() {
        view.endEditing(true)
        _ = navigationController?.popViewController(animated: true)
    }
    
    /// Pop to root view controller, if any
    /// Usage: Connect an action to the view controller
    @IBAction public func popToRootViewController() {
        view.endEditing(true)
       _ = navigationController?.popToRootViewController(animated: true)
    }
    
    
    // MARK: UIAlertController helpers
    
    /// Present alert with multiple actions
    public func presentAlert(_ title: String, message: String, actions: UIAlertAction...) {
        self.presentAlert(title, message: message, actionsArray: actions)
    }

    /// Present alert with multiple actions - refactored to allow call from objc
    @objc public func presentAlert(_ title: String, message: String, actionsArray actions: [UIAlertAction]) {
        if actions.count == 0 {
            presentOkAlert(title, message: message)
            return
        }
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        for action in actions {
            alertController.addAction(action)
        }
        present(alertController, animated: true, completion: nil)
    }


    /// Present single button alert with callback over current controller
    @objc public func presentAlert(_ title: String, message: String, buttonTitle: String, callback: (() -> Void)? = nil) {
        let action = UIAlertAction(title: buttonTitle, style: .cancel) { action in
            callback?()
        }
        presentAlert(title, message: message, actions: action)
    }
    
    /// Present single Ok button alert with callback over current controller
    @objc public func presentOkAlert(_ title: String, message: String, callback: (() -> Void)? = nil) {
        presentAlert(title, message: message, buttonTitle: "OK", callback: callback)
    }
    
    
    // MARK: Alert Related Methods
 
    
    /// Present single button error alert with callback over current controller
    @objc public func presentError(_ error: NSError?, callback: (() -> Void)? = nil) {
        if error != nil {
            presentAlert("Error", message: error!.localizedDescription, buttonTitle: "OK", callback: callback)
        }
    }
    
    /// Present two button alert with callback over current controller
    @objc public func presentConfirmation(_ title: String, message: String, buttonTitle: String, callback: @escaping (_ confirmed: Bool) -> Void) {
        let okAction = UIAlertAction(title: buttonTitle, style: .default) { action in
            callback(true)
        }
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel) { action in
            callback(false)
        }
        presentAlert(title, message: message, actions: okAction, cancelAction)
    }
    
    /// Present confirmation with yes or no alert
    @objc public func presentConfirmationWithYesOrNo(_ title: String, message: String, buttonTitle: String, callback: @escaping (_ confirmed: Bool) -> Void) {
        let okAction = UIAlertAction(title: buttonTitle, style: .default) { action in
            callback(true)
        }
        let cancelAction = UIAlertAction(title: "No", style: .cancel) { action in
            callback(false)
        }
        presentAlert(title, message: message, actions: okAction, cancelAction)
    }

}
