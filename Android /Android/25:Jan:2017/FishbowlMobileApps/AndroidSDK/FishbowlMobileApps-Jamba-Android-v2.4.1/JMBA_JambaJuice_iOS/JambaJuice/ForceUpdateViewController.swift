//
//  ForceUpdateViewController.swift
//  JambaJuice
//
//  Created by VT010 on 2/24/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit



class ForceUpdateViewController : UIViewController{
    
    @IBOutlet weak var updateMessage:UILabel!
    @IBOutlet weak var dimissButton:UIButton!
    @IBOutlet weak var remindMeLaterButton:UIButton!
    
    var forceUpdate:Bool = false
    var updateMessageString:String = ""
    
    override func viewDidLoad() {
        updateScreen()
        super.viewDidLoad()
    }
    
   override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func update(){
        let iTunesLink: String = "https://itunes.apple.com/us/app/jamba-juice/id932885438?mt=8"
        UIApplication.shared.openURL(URL(string: iTunesLink)!)
    }
    
    @IBAction func remindMe(){
        let forceUpdateDefaults=UserDefaults.standard
        forceUpdateDefaults.set("true", forKey: "forceUpdateRemaindMeLater")
        forceUpdateDefaults.set("false", forKey: "forceUpdateDismiss")
        forceUpdateDefaults.synchronize()
        self.remindMeDismiss()
    }
    
    @IBAction func dismiss(){
        let forceUpdateDefaults=UserDefaults.standard
        forceUpdateDefaults.set("false", forKey: "forceUpdateRemaindMeLater")
        forceUpdateDefaults.set("true", forKey: "forceUpdateDismiss")
        let appStoreVersionNumber = FishbowlApiClassService.sharedInstance.getAppStoreVersionNumber()
        forceUpdateDefaults.set(appStoreVersionNumber, forKey: "forceUpdateDismissVersionNumber")
        forceUpdateDefaults.synchronize()
        self.view.removeFromSuperview()
    }
    
    func updateScreen(){
        if forceUpdate{
            self.dimissButton.isHidden = true
            self.remindMeLaterButton.isHidden = true
        }else{
            self.dimissButton.isHidden = false
            self.remindMeLaterButton.isHidden = false
        }
        self.updateMessage.text = updateMessageString
    }
    
    func remindMeDismiss(){
        self.view.removeFromSuperview()
    }

}
