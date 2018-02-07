//
//  SignUpPushDOBViewController.swift
//  JambaJuice
//
//  Created by VT02 on 9/7/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
class SignUpPushConfirmationViewController: UIViewController {
    @IBOutlet weak var dotsPageControl: UIPageControl!
     @IBOutlet weak var greenCopyLabel: UILabel!
    @IBOutlet weak var personalizedUpdatesSwitch: UISwitch!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        if UIScreen.main.is35inch() {
            dotsPageControl.removeFromSuperview()
            greenCopyLabel.removeFromSuperview()
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
       UserService.signUpUserInfo?.enrollForPushUpdates = self.personalizedUpdatesSwitch.isOn
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
     @IBAction func continueSignUp() {
        // Per legal requirements, confirm for opt-in twice
         if personalizedUpdatesSwitch.isOn {
            presentConfirmation("Personalized Offers", message: "I agree to receive personalized updates and offers via push notifications.", buttonTitle: "Agree", callback: { (confirmed) -> Void in
                if !confirmed {
                    self.personalizedUpdatesSwitch.setOn(false, animated: true)
                }
                 self.performSegue(withIdentifier: "SignupDOB", sender: nil)
                })
         } else { 
            self.performSegue(withIdentifier: "SignupDOB", sender: nil)
        }

    }
    
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */


