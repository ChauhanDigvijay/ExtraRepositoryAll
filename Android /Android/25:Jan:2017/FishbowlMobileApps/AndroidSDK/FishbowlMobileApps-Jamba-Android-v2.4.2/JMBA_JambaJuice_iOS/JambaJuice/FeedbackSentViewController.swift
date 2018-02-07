//
//  FeedbackSentViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 9/7/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class FeedbackSentViewController: UIViewController {
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func close(_ sender: UIButton) {
        _ = (self.presentingViewController as? UINavigationController)?.popToRootViewController(animated: false)
        dismissModalController()
    }
    
}
