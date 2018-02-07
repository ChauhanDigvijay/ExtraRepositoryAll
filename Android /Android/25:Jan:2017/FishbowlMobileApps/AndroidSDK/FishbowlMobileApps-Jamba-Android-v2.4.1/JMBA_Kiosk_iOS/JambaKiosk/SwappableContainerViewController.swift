//
//  SwappableContainerViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/12/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

typealias SwappableContainerViewControllerCallback = (viewController: UIViewController) -> Void

class SwappableContainerViewController: UIViewController {

    // viewControllerWillLoad is called the first time the controller is instantiated
    private var viewControllerWillLoadCallback: SwappableContainerViewControllerCallback?

    // viewControllerWillAppear is called each time the controlled is presented in the container
    private var viewControllerWillAppearCallback: SwappableContainerViewControllerCallback?

    // Keep track of active controller to prevent swapping with itself
    private var activeSegueIdentifier: String?

    // Cache instantiated view controllers for fast switching and to preserve state
    private var viewControllers: [String: UIViewController] = [:]


    func swapToViewControllerWithSegueIdentifier(identifier: String,
        viewControllerWillLoad: ((viewController: UIViewController) -> Void)? = nil,
        viewControllerWillAppear: ((viewController: UIViewController) -> Void)? = nil)
    {
        if identifier == activeSegueIdentifier {
            return
        }

        viewControllerWillLoadCallback = viewControllerWillLoad
        viewControllerWillAppearCallback = viewControllerWillAppear

        if let activeSegue = activeSegueIdentifier,
            let fromViewController = viewControllers[activeSegue],
            let toViewController = viewControllers[identifier] {
            swapFromViewController(fromViewController, toViewController: toViewController)
        } else {
            performSegueWithIdentifier(identifier, sender: self)
        }

        activeSegueIdentifier = identifier
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        guard let identifier = segue.identifier else {
            return // Identifiers are required
        }

        let toViewController = segue.destinationViewController
        viewControllers[identifier] = toViewController
        viewControllerWillLoadCallback?(viewController: toViewController)

        if let fromViewController = childViewControllers.first {
            swapFromViewController(fromViewController, toViewController: toViewController)
        } else {
            swapToInitialViewController(toViewController)
        }
    }

    private func swapFromViewController(fromViewController: UIViewController,  toViewController: UIViewController) {
        viewControllerWillAppearCallback?(viewController: toViewController)

        toViewController.view.frame = view.frame
        fromViewController.willMoveToParentViewController(nil)
        addChildViewController(toViewController)

        transitionFromViewController(fromViewController, toViewController:toViewController, duration: 0.4, options: .TransitionCrossDissolve, animations: nil) { (finished) in
            fromViewController.removeFromParentViewController()
            toViewController.didMoveToParentViewController(self)
        }
    }

    private func swapToInitialViewController(viewController: UIViewController) {
        viewControllerWillAppearCallback?(viewController: viewController)

        addChildViewController(viewController)
        viewController.view.frame = view.frame
        view.addSubview(viewController.view)
        viewController.didMoveToParentViewController(self)
    }

}
