//
//  ModelClass.swift
//  FishBowlLibrary
//
//  Created by Puneet  on 7/20/17.
//  Copyright © 2017 Fishbowl. All rights reserved.
//

import Foundation


class ModelClass {
  
    static let sharedInstance:ModelClass = {
        let sharedMyManager = ModelClass ()
        return sharedMyManager
    } ()
    
    
    // MARK: - TextField Padding
    func addLeftPadding(to textField: UITextField) {
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: 16, height: 15))
        textField.leftView = paddingView
        textField.leftViewMode = .always
    }
    
    // MARK: - NSuserDefault Methods
    func saveUserDefaultData(_ value: String, and key: String) {
        let defaults = UserDefaults.standard
        defaults.set(value, forKey: key)
        defaults.synchronize()
    }
    
    func reterieveuserDefaultData(_ keyName: String) -> String {
        let defaults = UserDefaults.standard
        let salesName: String? = defaults.object(forKey: keyName) as? String
        return salesName!
    }
    
    // network check
    
    func checkNetworkConnection() -> Bool {
        print("check reachability call method")
        let networkReachability = NetworkReachabilityManager()
        if  networkReachability!.isReachable == true {
            print("There IS internet connection")
            return true
        }
        else {
            print("There IS NO internet connection")
            return false
        }
        
    }
    
    
    func reterieveSpendGoToken() -> String {
        
        let spendGoToken: String = UserDefaults.standard.value(forKey: "external_access_token") as? String ?? ""
        
        return spendGoToken
    }
    
    func reterieveSpendGoCustomerId() -> String {
        
        let spendGoCustomerId: String = UserDefaults.standard.value(forKey: "external_customer_id") as? String ?? ""
        
        return spendGoCustomerId

    }
    
    func retrieveFishbowlToken() -> String {
        
        let fishBowlAccessToken: String = UserDefaults.standard.value(forKey: "fishbowl_access_token") as? String ?? ""
        
        return fishBowlAccessToken

    }
    
    func retrieveDeviceId() -> String {
        
        let deviceId: String = UserDefaults.standard.value(forKey: "device_id") as? String ?? ""

        
        return deviceId
    }
    
    func retrieveSignature() -> String {
        let signature: String = UserDefaults.standard.value(forKey: "signature") as? String ?? ""

        return signature
    }
    
    func retrievekey() -> String {
        let key: String = UserDefaults.standard.value(forKey: "externalkey") as? String ?? ""
        
        return key
    }
    
    

    
}
