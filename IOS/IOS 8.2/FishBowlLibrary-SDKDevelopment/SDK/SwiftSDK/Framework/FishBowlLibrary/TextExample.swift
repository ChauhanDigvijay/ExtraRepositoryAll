//
//  TextExample.swift
//  FishBowlLibrary
//
//  Created by Puneet  on 7/26/17.
//  Copyright © 2017 Fishbowl. All rights reserved.
//

import Foundation


import UIKit

class ViewController: UIViewController

{
    
    
    private let objDataManager = DataManager()

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Example how you need to call the DataManager class.
        
        let tempDict = NSMutableDictionary()
        
        let userName : String = "puneetkathuria@yahoo.com"
        let password : String = "passsword"
        
        tempDict.setValue(userName, forKey : "username")
        tempDict.setValue(password, forKey : "passsword")
        
        objDataManager.loginAPI(dictBody: tempDict as [NSObject : AnyObject])
        { (response, error) in
            let response = response as? HTTPURLResponse
            let responseData:String = response!.description

            print("response is \(responseData)")
            if(response?.statusCode != 200)
            {
                if(response?.statusCode == 401)
                {
                    //Token Expiration
                    //Please call the getToken API and get the token save it in userdefaults and call the same api again.
                }
                else
                {
                    // Please check your internet connection first if it's fine then Inform the Fishbowl server team or SDK team regarding the error.
                let strError:String = error!.localizedDescription
                print("error is \(strError)");
                }
            }

        }
        
    }
    
    
}
