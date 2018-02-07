//
//  FeedbackSentViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 9/7/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class FeedbackSentViewController: UIViewController {
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func close(sender: UIButton) {
        (self.presentingViewController as? UINavigationController)?.popToRootViewControllerAnimated(false)
        dismissModalController()
    }
    
}
