//
//  SignUpLocationsViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 7/17/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class SignUpLocationsViewController: UIViewController {

    @IBOutlet weak var dotsPageControl: UIPageControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if UIScreen.main.is35inch() {
            dotsPageControl.removeFromSuperview()
        }
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
}
