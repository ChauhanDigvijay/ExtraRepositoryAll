//
//  DataManager.swift
//  FishBowlLibrary
//
//  Created by Puneet  on 17/07/17.
//  Copyright © 2017 Fishbowl. All rights reserved.
//

import Foundation

public enum DataManagerError: Error {
    
    case Unknown
    case FailedRequest
    case InvalidResponse
    case TokenExpiration
    case BadRequest
    case ServerUnavailable
    case InternalServerError
    case NOInternetConnection
    case Timeout
    
}


public class DataManager {
    
    
    public typealias APIDataCompletion = (AnyObject?, DataManagerError?) -> ()
    
    
    // MARK: - Initialization
    
   public init() {
    
    
    }
    
    
    // MARK: DeviceID
    
    public func deviceID() -> String {
        var string: String = UIDevice.current.identifierForVendor!.uuidString
        string = string.replacingOccurrences(of: "-", with: "")
        string = "\(string)"
        return string
    }
    
 
    

    // MARK: -  Security Token Api
    
     public func getTokenApi(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetToken)"          // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.PUT                             // Method Type i.e. POST,GET,UPDATE
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
    
        let mySingleton = ModelClass.sharedInstance                           // Call Singleton object
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchTokenData(data: data, response: response, error: error, completion: completion)
            }.resume()
            
        }
        else
        {
            completion(nil, .NOInternetConnection)
            
        }
        
        
    }
    
    
    // MARK: - Member SignUpSDK API
  
     public func signUpSDKApi(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.ExternalMemberSignup)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                  // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                             // Request Creation Start
        request.httpMethod = MethodType.POST                                  // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                           // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()           // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Task
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchSignupData(data: data, response: response, error: error, completion: completion)
            }.resume()
            
        }
        else
        {
            completion(nil, .NOInternetConnection)

        }
        
    }
    

    // MARK: -  Member LoginSDK API for Client
    
    public func loginSDKApi(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.ExternalMemberLogin)"       // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                    // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                               // Request Creation Start
        request.httpMethod = MethodType.POST                                    // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                                     // Call Singleton object
        let signature : String = mySingleton.retrieveSignature()                        // Retrieve key Value
        let spendgoKey : String = mySingleton.retrievekey()                             // Retrieve Signature Value
        let strExternalCustomerId : String = mySingleton.reterieveSpendGoCustomerId()   // Retrieve ExternalCustomerId Value
        let strExternalAccessToken : String = mySingleton.reterieveSpendGoToken()       // Retrieve ExternalAccessToken Value
        

        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)                   //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)                          //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)                       //Manadatory Header ContentType
        request.setValue(signature, forHTTPHeaderField: APIHeaderkey.Signature)                           //Manadatory Header Signature
        request.setValue(spendgoKey, forHTTPHeaderField: APIHeaderkey.Key)                              //Manadatory Header signatureKey
        request.setValue(strExternalCustomerId, forHTTPHeaderField: APIHeaderkey.ExternalCustomerId)      //Manadatory Header ExternalCustomerId
        request.setValue(strExternalAccessToken, forHTTPHeaderField: APIHeaderkey.ExternalAccessToken)    //Manadatory Header ExternalAccessToken
        request.setValue(strSpendgoBaseUrl, forHTTPHeaderField: APIHeaderkey.SpendGoApiBaseUrl)           //Manadatory Header SpendGoApiBaseUrl
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)                          //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)                      //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)                  //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])        // Request Creation End
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Task
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchLoginSDKData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)

        }
        
    }
    
    // MARK: -  Member Details API
    
    public func getMemberSDKApi(completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetMemberProfile)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                              // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                         // Request Creation Start
        request.httpMethod = MethodType.GET                               // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                       // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()       // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        // Create Data Task
        if(mySingleton.checkNetworkConnection())
        {
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchMemberData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)

        }
        
    }
    
    
    // MARK: -  Member Device Update API
 
    public func updateDeviceSDKApi(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.UpdateDevice)"      // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.POST                            // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        // Create Data Task
        if(mySingleton.checkNetworkConnection())
        {
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchUpdateDeviceData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
            
        }
        
    }
    
    
    // MARK: -  Member Login API
    
    public func loginAPI(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.MemberLogin)"       // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
//        print("urlStr is \(urlStr)")
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.POST                            // Method Type i.e. POST,GET,UPDATE
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
//        print("request is \(request)")
//        print("strClientId is \(strClientId)")
//        print("applicationType is \(applicationType)")
//        print("contentType is \(contentType)") 
//        print("strTanentId is \(strTanentId)")
//        print("self.deviceID() is \(self.deviceID())")
//        print("strClientSecret is \(strClientSecret)")
        
        let mySingleton = ModelClass.sharedInstance                           // Call Singleton object
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Task
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchLoginData(data: data, response: response, error: error, completion: completion)

            }.resume()
            
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Member logout API
    
    public func logoutAPI(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.MemberLogout)"      // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.POST                            // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchLogoutData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Member Signup API
    
    public func signupAPI(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.MemberSignup)"      // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.POST                            // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Task
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchMemberSignupData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Client Mobile Setting API
    
    public func mobileSettingAPI(completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.MobileSetting)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                            // Method Type i.e. POST,GET,UPDATE
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object

        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchMobileSettingData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Change Member Password API
    
    public func changePasswordAPI(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.ChangePassword)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.PUT                            // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()    // Retrieve Token Value

        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End

        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchChangePasswordData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Member Details API
    
    public func getMemberAPI(completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetMemberProfile)"   // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                             // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchGetMemberData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Update Member Details API
    
    public func updateMemberAPI(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.MemberUpdate)"      // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.PUT                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End

        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchUpdateMemberData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Member Offers API
    
    public func getOffersAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        
        let NumberofOffers : String = "/100"
        let memberId : String = (URLBody[BodyParameter.CustomerId] as? String)!        // Adding URL Parameters to URL

        let urlStr: String = "\(strBaseURL)\(SUBURL.Getoffers)\(memberId)\(NumberofOffers)"         // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchOffersData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Member Rewards API
    
    public func getRewardsAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
       
        let memberId : String = (URLBody[BodyParameter.CustomerId] as? String)!        // Adding URL Parameters to URL
        let NumberofRewards : String = "/100"

        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetRewards)\(memberId)\(NumberofRewards)"        // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchRewardsData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Member LoyalityPoints API
    
    public func getLoyalityPointsAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        
        let customerId: String = (URLBody[BodyParameter.CustomerId] as? String)!      // Adding URL Parameters to URL


        let urlStr: String = "\(strBaseURL)\(SUBURL.PointRewardInfo)\(customerId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                          // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                                     // Request Creation Start
        request.httpMethod = MethodType.GET                                           // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                                   // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()                   // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchLoyalityPointsData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Offer Details by OfferId API
    
    public func getOfferbyOfferIdAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        let offerId: String = (URLBody[BodyParameter.OfferId] as? String)!           // Adding URL Parameters to URL

        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetOfferByOfferId)\(offerId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                         // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                                    // Request Creation Start
        request.httpMethod = MethodType.GET                                          // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                                  // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()                  // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchOfferbyOfferIdData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
  
    
    // MARK: -  Client AllStores API
    
    public func getAllStoresAPI(completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetStoreList)"      // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object

        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                print("error is \(err.localizedDescription)")
                completion(nil, .Timeout)
                return
                }
            self.didFetchStoreListData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }

    
    // MARK: -  Update Member Device API
    
    public func updateDeviceAPI(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.UpdateDevice)"      // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.POST                            // Method Type i.e. POST,GET,UPDATE
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        

        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchupdateDeviceData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  ForgotPassword API
    
    public func ForgotPasswordAPI(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.ForgotPassword)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.POST                            // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value

        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchForgotPasswordData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Store Search API
    
    public func storeSearchAPI(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.SearchStores)"      // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.POST                            // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchStoreSearchData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Make Favourite Store Api
    
    public func favouriteStoreApi(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.FavStoreUpdate)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.PUT                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchFavouriteStoreData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Client stateList API
    
    public func stateListAPI(completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetStates)"         // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object

        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchStateListData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Member PointBankOffer API
    
    public func getPointBankOfferAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        let memberId : String = (URLBody[BodyParameter.CustomerId] as? String)!        // Adding URL Parameters to URL

        let urlStr: String = "\(strBaseURL)\(SUBURL.PointBankoffer)\(memberId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchPointBankOfferData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  UseOffer Claim API
    
    public func useOfferAPI(dictBody: [NSObject : AnyObject], completion: @escaping APIDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.Useoffer)"          // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.PUT                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchUseOfferData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Client Menu Category API
    
    public func getMenuCategoryAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        let storeId : String = (URLBody[BodyParameter.StoreId] as? String)!        // Adding URL Parameters to URL
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetMenuCategory)\(storeId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                       // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                                  // Request Creation Start
        request.httpMethod = MethodType.GET                                        // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                                // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()                // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchMenuCategoryData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Client Menu SubCategory API
    
    public func getMenuSubCategoryAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        let storeId : String = (URLBody[BodyParameter.StoreId] as? String)!         // Adding URL Parameters to URL
        let categoryId : String = (URLBody[BodyParameter.CategoryId] as? String)!   // Adding URL Parameters to URL
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.SubCategoryURLPart1)\(storeId)\(SUBURL.SubCategoryURLPart2)\(categoryId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchMenuSubCategoryData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Client Menu ProductList API
    
    public func getMenuProductListAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        let storeId : String = (URLBody[BodyParameter.StoreId] as? String)!                 // Adding URL Parameters to URL
        let categoryId : String = (URLBody[BodyParameter.CategoryId] as? String)!           // Adding URL Parameters to URL
        let subCategoryId : String = (URLBody[BodyParameter.SubCategoryId] as? String)!     // Adding URL Parameters to URL
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.ProductListURLPart1)\(storeId)\(SUBURL.ProductListURLPart2)\(categoryId)\(SUBURL.ProductListURLPart3)\(subCategoryId)"                       // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchMenuProductListData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }

    
    // MARK: -  Client Menu ProductAttributes API
    
    public func getMenuProductAttributesAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        let storeId : String = (URLBody[BodyParameter.StoreId] as? String)!                 // Adding URL Parameters to URL
        let categoryId : String = (URLBody[BodyParameter.CategoryId] as? String)!           // Adding URL Parameters to URL
        let subCategoryId : String = (URLBody[BodyParameter.SubCategoryId] as? String)!     // Adding URL Parameters to URL
        let productId : String = (URLBody[BodyParameter.ProductId] as? String)!             // Adding URL Parameters to URL
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.productAttributeURLPart1)\(storeId)\(SUBURL.productAttributeURLPart2)\(categoryId)\(SUBURL.productAttributeURLPart3)\(subCategoryId)\(SUBURL.productAttributeURLPart4)\(productId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchMenuProductAttributesData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Member AllRewardOffer API
    
    public func getAllRewardOfferAPI(completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetAllRewardOffers)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                           // Request Creation Start
        request.httpMethod = MethodType.GET                                 // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                         // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()         // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchRewardOfferData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Client signupRuleList API
    
    public func signupRuleListAPI(completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.SignupRuleList)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object

        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchSignupRuleListData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Client StoreDetails API
    
    public func getStoreDetailsAPI(URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        
        let storeId : String = (URLBody[BodyParameter.StoreId] as? String)!        // Adding URL Parameters to URL

        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetStoreDetails)\(storeId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                       // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                                  // Request Creation Start
        request.httpMethod = MethodType.GET                                        // Method Type i.e. POST,GET,UPDATE
        
        let mySingleton = ModelClass.sharedInstance                                // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()                // Retrieve Token Value
        

        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchStoreDetailsData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  RegisterUserMobileEvents API
    
    public func mobileAppEventsAPI(dictBody: [String : AnyObject], completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.RegisterUserMobileEvents)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                      // Changing String to URL
        
        var request = URLRequest(url: url as URL)                                 // Request Creation Start
        request.httpMethod = MethodType.POST                                      // Method Type i.e. POST,GET,UPDATE
        
        let mySingleton = ModelClass.sharedInstance                               // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()               // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchMobileAppEventsData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  GuestUserMobileEvents API
    
    public func GuestUserMobileAppEventsAPI(dictBody: [String : AnyObject], completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GuestUserMobileEvents)"     // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                    // Changing String to URL
        
        var request = URLRequest(url: url as URL)                               // Request Creation Start
        request.httpMethod = MethodType.POST                                    // Method Type i.e. POST,GET,UPDATE
        
        let mySingleton = ModelClass.sharedInstance                             // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()             // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchGuestUserMobileAppEventsData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
  
    
    // MARK: -  Client LoyaltySettings API
    
    public func getLoyaltySettingsAPI(completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.LoyalitySetting)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                             // Changing String to URL
        
        var request = URLRequest(url: url as URL)                        // Request Creation Start
        request.httpMethod = MethodType.GET                              // Method Type i.e. POST,GET,UPDATE
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object

        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchLoyaltySettingsData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Client DefaultTheme API
    
    public func getDefaultThemeAPI(completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.DefaultTheme)"      // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object

        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchDefaultThemeData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Client InCommToken API
    
    public func InCommTokenAPI(dictBody: [String : AnyObject], completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.IncommToken)"       // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.POST                            // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchInCommTokenData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
    }
    
    // MARK: -  Client OrderValue API
    
    public func OrderValueAPI(dictBody: [String : AnyObject], completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.IncommOrderValue)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                              // Changing String to URL
        
        var request = URLRequest(url: url as URL)                         // Request Creation Start
        request.httpMethod = MethodType.POST                              // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                       // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()       // Retrieve Token Value
    
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchOrderValueData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Client InCommOrderID API
    
    public func InCommOrderIDAPI(dictBody: [String : AnyObject],completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.IncommOrderId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                     // Changing String to URL
        
        var request = URLRequest(url: url as URL)                                // Request Creation Start
        request.httpMethod = MethodType.POST                                     // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                              // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()              // Retrieve Token Value

        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchInCommOrderIDData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    // MARK: -  Client UpdateIncommTransaction API
    
    public func updateIncommTransactionAPI(dictBody: [String : AnyObject], completion: @escaping APIDataCompletion) {
        

        let urlStr: String = "\(strBaseURL)\(SUBURL.IncommOrderId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                     // Changing String to URL
        
        var request = URLRequest(url: url as URL)                                // Request Creation Start
        request.httpMethod = MethodType.PUT                                      // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                              // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()              // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
        
        
        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchUpdateIncommTransactionData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Member Details ByExternalMemberId API
    
    public func getMemberByExternalMemberIdAPI(dictBody: [String : AnyObject],URLBody:[String:AnyObject], completion: @escaping APIDataCompletion) {
        
        let spendGoId : String = (URLBody[BodyParameter.SpendgoId] as? String)!                  // Adding URL Parameters to URL
    
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.getMemberbyExternalMemberId)\(spendGoId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                                     // Changing String to URL
        
        var request = URLRequest(url: url as URL)                                                // Request Creation Start
        request.httpMethod = MethodType.GET                                                      // Method Type i.e. POST,GET,UPDATE
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object

        if(mySingleton.checkNetworkConnection())
        {
        // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
            self.didFetchMemberByExternalMemberIdData(data: data, response: response, error: error, completion: completion)
            }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    // MARK: -  Member update by email API
    
    public func MemberUpdatebyEmailAPI(dictBody: [String : AnyObject], completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.MemberUpdateByEmail)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                                     // Changing String to URL
        
        var request = URLRequest(url: url as URL)                                                // Request Creation Start
        request.httpMethod = MethodType.POST                                                      // Method Type i.e. POST,GET,UPDATE
        
        let mySingleton = ModelClass.sharedInstance                              // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()              // Retrieve Token Value
        
        print("strToken is \(strToken)")
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End

        
        if(mySingleton.checkNetworkConnection())
        {
            // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
                self.didMemberUpdatebyEmailData(data: data, response: response, error: error, completion: completion)
                }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    
    
    
    // MARK: - WalletRefreshToken API
    
    public func getWalletRefreshTokenAPI(memberId:String, completion: @escaping APIDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.WalletRefreshToken)\(memberId)"         // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        
        if(mySingleton.checkNetworkConnection())
        {
            // Create Data Tasks
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 60
            let session = URLSession(configuration: config)
            session.dataTask(with: request) { (data, response, error) in
                
                if let err : Error = error
                {
                    print("error is \(err.localizedDescription)")
                    completion(nil, .Timeout)
                    return
                }
                self.didFetchWalletRefreshTokenData(data: data, response: response, error: error, completion: completion)
                }.resume()
        }
        else
        {
            completion(nil, .NOInternetConnection)
        }
        
    }
    
    

    // MARK: - Helper Methods
    
    private func didFetchTokenData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)

            
        } else if let data = data, let response = response as? HTTPURLResponse {

            print("response before is \(response.description)")

            if response.statusCode == 200 {
                processData(data: data, completion: completion)


            }
                else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)

            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }

            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")

            completion(nil, .Unknown)
            
        }
            
        }
    
    // MARK: - Helper Methods
    
    private func didFetchSignupData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchLoginSDKData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchMemberData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchUpdateDeviceData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchLoginData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchLogoutData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchMemberSignupData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchMobileSettingData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchChangePasswordData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchGetMemberData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchUpdateMemberData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchOffersData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchRewardsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchLoyalityPointsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchOfferbyOfferIdData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchStoreListData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchupdateDeviceData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchForgotPasswordData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchStoreSearchData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchFavouriteStoreData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchStateListData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchPointBankOfferData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchUseOfferData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchMenuCategoryData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchMenuSubCategoryData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchMenuProductListData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchMenuProductAttributesData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchRewardOfferData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchSignupRuleListData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchStoreDetailsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchMobileAppEventsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchGuestUserMobileAppEventsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchLoyaltySettingsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchDefaultThemeData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchInCommTokenData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchOrderValueData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchInCommOrderIDData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchUpdateIncommTransactionData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    // MARK: - Helper Methods
    
    private func didFetchMemberByExternalMemberIdData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didMemberUpdatebyEmailData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }

    
    // MARK: - Helper Methods
    
    private func didFetchWalletRefreshTokenData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                processData(data: data, completion: completion)
                
                
            }
            else if response.statusCode == 401 {
                processTokenExpirationData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 400 {
                processBadRequestData(data: data, completion: completion)
                
            }
                
            else if response.statusCode == 500 {
                processInternalServerErrorData(data: data, completion: completion)
                
            }
            else if response.statusCode == 503 {
                processServerUnavailableData(data: data, completion: completion)
                
            }
                
            else {
                processFailedData(data: data, completion: completion)
            }
            
        } else {
            print("response is \(String(describing: response?.description))")
            
            completion(nil, .Unknown)
            
        }
        
    }
    
    
    
    // MARK: - Helper Methods

  
    private func processInternalServerErrorData(data: Data, completion: @escaping APIDataCompletion) {
        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
            completion(JSON, .InternalServerError)
        } else {
            completion(nil, .InternalServerError)
        }
    }
    
    
    
    private func processServerUnavailableData(data: Data, completion: @escaping APIDataCompletion) {
        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
            completion(JSON, .ServerUnavailable)
        } else {
            completion(nil, .ServerUnavailable)
        }
    }
    

    private func processBadRequestData(data: Data, completion: @escaping APIDataCompletion) {
        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
            completion(JSON, .BadRequest)
        } else {
            completion(nil, .BadRequest)
        }
    }
    
    
    private func processTokenExpirationData(data: Data, completion: @escaping APIDataCompletion) {
        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
            completion(JSON, .TokenExpiration)
        } else {
            completion(nil, .TokenExpiration)
        }
    }
    
    private func processFailedData(data: Data, completion: @escaping APIDataCompletion) {
        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
            completion(JSON, .InvalidResponse)
        } else {
            completion(nil, .InvalidResponse)
        }
    }
    
    
    private func processData(data: Data, completion: @escaping APIDataCompletion) {
        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
            completion(JSON, nil)
        } else {
            completion(nil, .InvalidResponse)
        }
    }
    
}
