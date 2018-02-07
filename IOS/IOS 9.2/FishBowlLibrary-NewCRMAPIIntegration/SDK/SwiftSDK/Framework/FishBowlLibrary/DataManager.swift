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
    
    public typealias APIResponseDataCompletion = (ResponseBO?, DataManagerError?) -> ()
    
    
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
    
     public func getTokenApi(completion: @escaping APIResponseDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetToken)"          // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["clientId"] = strClientId as AnyObject
        dictBody["clientSecret"] = strClientSecret as AnyObject
        dictBody["tenantId"] = strTanentId as AnyObject
        dictBody["deviceId"] = self.deviceID() as AnyObject

        
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
  
     public func signUpSDKApi(objRegistration: FBRegistrationBO, completion: @escaping APIResponseDataCompletion) {
        
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
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["firstName"] = objRegistration.FirstName as AnyObject
        dictBody["lastName"] =  objRegistration.LastName as AnyObject
        dictBody["email"] =     objRegistration.EmailAddress as AnyObject
        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
        dictBody["password"] = objRegistration.Password as AnyObject
        dictBody["birthDate"] = objRegistration.DOB as AnyObject
        dictBody["gender"] = objRegistration.Gender as AnyObject
        dictBody["addressStreet"] = objRegistration.Address as AnyObject
        dictBody["favoriteStore"] = objRegistration.FavoriteStore as AnyObject
        dictBody["phone"] = objRegistration.PhoneNumber as AnyObject
        dictBody["smsOptIn"] = objRegistration.SMSOptIn as AnyObject
        dictBody["pushOptIn"] = objRegistration.PushOptIn as AnyObject
        dictBody["addressState"] = objRegistration.State as AnyObject
        dictBody["addressCity"] = objRegistration.City as AnyObject
        dictBody["addressZipCode"] = objRegistration.ZipCode as AnyObject
        dictBody["storeCode"] = objRegistration.StoreCode as AnyObject
        dictBody["Bonus"] = objRegistration.Bonus as AnyObject
        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
        dictBody["addressCountry"] = objRegistration.Country as AnyObject
        dictBody["appId"] = objRegistration.appId as AnyObject
        dictBody["deviceId"] = objRegistration.appId as AnyObject
        dictBody["deviceOSVersion"] = objRegistration.deviceOSVersion as AnyObject
        dictBody["deviceType"] = objRegistration.deviceType as AnyObject
        dictBody["pushToken"] = objRegistration.pushToken as AnyObject
        dictBody["custom_1"] = objRegistration.Custom1 as AnyObject
        dictBody["custom_2"] = objRegistration.Custom2 as AnyObject
        dictBody["custom_3"] = objRegistration.Custom3 as AnyObject
        dictBody["custom_4"] = objRegistration.Custom4 as AnyObject
        dictBody["custom_5"] = objRegistration.Custom5 as AnyObject
        dictBody["custom_6"] = objRegistration.Custom6 as AnyObject
        dictBody["custom_7"] = objRegistration.Custom7 as AnyObject
        dictBody["custom_8"] = objRegistration.Custom8 as AnyObject
        dictBody["custom_9"] = objRegistration.Custom9 as AnyObject
        dictBody["custom_10"] = objRegistration.Custom10 as AnyObject
        dictBody["custom_11"] = objRegistration.Custom11 as AnyObject
        dictBody["custom_12"] = objRegistration.Custom12 as AnyObject
        dictBody["custom_13"] = objRegistration.Custom13 as AnyObject
        dictBody["custom_14"] = objRegistration.Custom14 as AnyObject
        dictBody["custom_15"] = objRegistration.Custom15 as AnyObject
        dictBody["custom_16"] = objRegistration.Custom16 as AnyObject
        dictBody["custom_17"] = objRegistration.Custom17 as AnyObject
        dictBody["custom_18"] = objRegistration.Custom18 as AnyObject
        dictBody["custom_19"] = objRegistration.Custom19 as AnyObject
        dictBody["custom_20"] = objRegistration.Custom20 as AnyObject
        dictBody["custom_21"] = objRegistration.Custom21 as AnyObject
        dictBody["custom_22"] = objRegistration.Custom22 as AnyObject
        dictBody["custom_23"] = objRegistration.Custom23 as AnyObject
        dictBody["custom_24"] = objRegistration.Custom24 as AnyObject
        dictBody["custom_25"] = objRegistration.Custom25 as AnyObject
        dictBody["custom_26"] = objRegistration.Custom26 as AnyObject
        dictBody["custom_27"] = objRegistration.Custom27 as AnyObject
        dictBody["custom_28"] = objRegistration.Custom28 as AnyObject
        dictBody["custom_29"] = objRegistration.Custom29 as AnyObject
        dictBody["custom_30"] = objRegistration.Custom30 as AnyObject
        dictBody["custom_31"] = objRegistration.Custom31 as AnyObject
        dictBody["custom_32"] = objRegistration.Custom32 as AnyObject
        dictBody["custom_33"] = objRegistration.Custom33 as AnyObject
        dictBody["custom_34"] = objRegistration.Custom34 as AnyObject
        dictBody["custom_35"] = objRegistration.Custom35 as AnyObject
        dictBody["custom_36"] = objRegistration.Custom36 as AnyObject
        dictBody["custom_37"] = objRegistration.Custom37 as AnyObject
        dictBody["custom_38"] = objRegistration.Custom38 as AnyObject
        dictBody["custom_39"] = objRegistration.Custom39 as AnyObject
        dictBody["custom_40"] = objRegistration.Custom40 as AnyObject
        dictBody["custom_41"] = objRegistration.Custom41 as AnyObject
        dictBody["custom_42"] = objRegistration.Custom42 as AnyObject
        dictBody["custom_43"] = objRegistration.Custom43 as AnyObject
        dictBody["custom_44"] = objRegistration.Custom44 as AnyObject
        dictBody["custom_45"] = objRegistration.Custom45 as AnyObject
        dictBody["custom_46"] = objRegistration.Custom46 as AnyObject
        dictBody["custom_47"] = objRegistration.Custom47 as AnyObject
        dictBody["custom_48"] = objRegistration.Custom48 as AnyObject
        dictBody["custom_49"] = objRegistration.Custom49 as AnyObject
        dictBody["custom_50"] = objRegistration.Custom50 as AnyObject
        dictBody["custom_51"] = objRegistration.Custom51 as AnyObject
        dictBody["custom_52"] = objRegistration.Custom52 as AnyObject
        dictBody["custom_53"] = objRegistration.Custom53 as AnyObject
        dictBody["custom_54"] = objRegistration.Custom54 as AnyObject
        dictBody["custom_55"] = objRegistration.Custom55 as AnyObject
        dictBody["custom_56"] = objRegistration.Custom56 as AnyObject
        dictBody["custom_57"] = objRegistration.Custom57 as AnyObject
        dictBody["custom_58"] = objRegistration.Custom58 as AnyObject
        dictBody["custom_59"] = objRegistration.Custom59 as AnyObject
        dictBody["custom_60"] = objRegistration.Custom60 as AnyObject
        dictBody["custom_61"] = objRegistration.Custom61 as AnyObject
        dictBody["custom_62"] = objRegistration.Custom62 as AnyObject
        dictBody["custom_63"] = objRegistration.Custom63 as AnyObject
        dictBody["custom_64"] = objRegistration.Custom64 as AnyObject
        dictBody["custom_65"] = objRegistration.Custom65 as AnyObject
        dictBody["custom_66"] = objRegistration.Custom66 as AnyObject
        dictBody["custom_67"] = objRegistration.Custom67 as AnyObject
        dictBody["custom_68"] = objRegistration.Custom68 as AnyObject
        dictBody["custom_69"] = objRegistration.Custom69 as AnyObject
        dictBody["custom_70"] = objRegistration.Custom70 as AnyObject
        dictBody["custom_71"] = objRegistration.Custom71 as AnyObject
        dictBody["custom_72"] = objRegistration.Custom72 as AnyObject
        dictBody["custom_73"] = objRegistration.Custom73 as AnyObject
        dictBody["custom_74"] = objRegistration.Custom74 as AnyObject
        dictBody["custom_75"] = objRegistration.Custom75 as AnyObject
        dictBody["custom_76"] = objRegistration.Custom76 as AnyObject
        dictBody["custom_77"] = objRegistration.Custom77 as AnyObject
        dictBody["custom_78"] = objRegistration.Custom78 as AnyObject
        dictBody["custom_79"] = objRegistration.Custom79 as AnyObject
        dictBody["custom_80"] = objRegistration.Custom80 as AnyObject
        dictBody["custom_81"] = objRegistration.Custom81 as AnyObject
        dictBody["custom_82"] = objRegistration.Custom82 as AnyObject
        dictBody["custom_83"] = objRegistration.Custom83 as AnyObject
        dictBody["custom_84"] = objRegistration.Custom84 as AnyObject
        dictBody["custom_85"] = objRegistration.Custom85 as AnyObject
        dictBody["custom_86"] = objRegistration.Custom86 as AnyObject
        dictBody["custom_87"] = objRegistration.Custom87 as AnyObject
        dictBody["custom_88"] = objRegistration.Custom88 as AnyObject
        dictBody["custom_89"] = objRegistration.Custom89 as AnyObject
        dictBody["custom_90"] = objRegistration.Custom90 as AnyObject
        dictBody["custom_91"] = objRegistration.Custom91 as AnyObject
        dictBody["custom_92"] = objRegistration.Custom92 as AnyObject
        dictBody["custom_93"] = objRegistration.Custom93 as AnyObject
        dictBody["custom_94"] = objRegistration.Custom94 as AnyObject
        dictBody["custom_95"] = objRegistration.Custom95 as AnyObject
        dictBody["custom_96"] = objRegistration.Custom96 as AnyObject
        dictBody["custom_97"] = objRegistration.Custom97 as AnyObject
        dictBody["custom_98"] = objRegistration.Custom98 as AnyObject
        dictBody["custom_99"] = objRegistration.Custom99 as AnyObject
        dictBody["custom_100"] = objRegistration.Custom100 as AnyObject
        

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
    
    public func loginSDKApi(objLogin: FBLoginBO, completion: @escaping APIResponseDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.ExternalMemberLogin)"       // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                    // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                               // Request Creation Start
        request.httpMethod = MethodType.POST                                    // Method Type i.e. POST,GET,UPDATE
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["username"] = objLogin.userName as AnyObject
        dictBody["deviceId"] = objLogin.deviceId as AnyObject
        
        
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
    
    public func getMemberSDKApi(completion: @escaping APIResponseDataCompletion) {
        
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
 
    public func updateDeviceSDKApi(objDevice: FBDeviceBO, completion: @escaping APIResponseDataCompletion) {
        
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
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["appId"] = objDevice.appId as AnyObject
        dictBody["deviceId"] = self.deviceID() as AnyObject
        dictBody["deviceOSVersion"] = objDevice.deviceOSVersion as AnyObject
        dictBody["deviceType"] =  "iPhone" as AnyObject
        dictBody["memberid"] = objDevice.memberId as AnyObject
        dictBody["pushToken"] = objDevice.pushToken as AnyObject
        
        
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
    
    public func loginAPI(objLogin: FBLoginBO, completion: @escaping APIResponseDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.MemberLogin)"       // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["username"] = objLogin.userName as AnyObject
        dictBody["password"] = objLogin.password as AnyObject
        
        
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
    
    public func logoutAPI(completion: @escaping APIResponseDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.MemberLogout)"      // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["Application"] = applicationType as AnyObject
        
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
    
    public func signupAPI(objRegistration: FBRegistrationBO, completion: @escaping APIResponseDataCompletion) {
        
        print("call getTokenApi");
        getTokenApi() { (response, error) in
            
            if error != nil
            {
                return
            }
                
            else
            {
                
                if var objResponse = response as? FBRespnoseBO {
                    
                    if case objResponse.successFlag = true
                    {
                        let strToken = objResponse.message as String
                        let defaults = UserDefaults.standard
                        defaults.set(strToken, forKey: "fishbowl_access_token")
                        defaults.synchronize()
                        
                        
                        print("call signupAPI");
                        
                        
                        let urlStr: String = "\(strBaseURL)\(SUBURL.MemberSignup)"      // Full URL Creation of API
                        let url : URL = URL(string: urlStr)!                            // Changing String to URL
                        
                        
                        var request = URLRequest(url: url as URL)                       // Request Creation Start
                        request.httpMethod = MethodType.POST                            // Method Type i.e. POST,GET,UPDATE
                        
                        
                        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
                        let TokenValue : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
                        
                        
                        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
                        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
                        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
                        request.setValue(TokenValue, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
                        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
                        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
                        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
                        
                        
                        
                        var dictBody = Dictionary<String, AnyObject>()
                        dictBody["firstName"] = objRegistration.FirstName as AnyObject
                        dictBody["lastName"] =  objRegistration.LastName as AnyObject
                        dictBody["email"] =     objRegistration.EmailAddress as AnyObject
                        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
                        dictBody["password"] = objRegistration.Password as AnyObject
                        dictBody["birthDate"] = objRegistration.DOB as AnyObject
                        dictBody["gender"] = objRegistration.Gender as AnyObject
                        dictBody["addressStreet"] = objRegistration.Address as AnyObject
                        dictBody["favoriteStore"] = objRegistration.FavoriteStore as AnyObject
                        dictBody["phone"] = objRegistration.PhoneNumber as AnyObject
                        dictBody["smsOptIn"] = objRegistration.SMSOptIn as AnyObject
                        dictBody["pushOptIn"] = objRegistration.PushOptIn as AnyObject
                        dictBody["addressState"] = objRegistration.State as AnyObject
                        dictBody["addressCity"] = objRegistration.City as AnyObject
                        dictBody["addressZipCode"] = objRegistration.ZipCode as AnyObject
                        dictBody["storeCode"] = objRegistration.StoreCode as AnyObject
                        dictBody["Bonus"] = objRegistration.Bonus as AnyObject
                        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
                        dictBody["addressCountry"] = objRegistration.Country as AnyObject
                        dictBody["appId"] = objRegistration.appId as AnyObject
                        dictBody["deviceId"] = objRegistration.appId as AnyObject
                        dictBody["deviceOSVersion"] = objRegistration.deviceOSVersion as AnyObject
                        dictBody["deviceType"] = objRegistration.deviceType as AnyObject
                        dictBody["pushToken"] = objRegistration.pushToken as AnyObject
                        dictBody["custom_1"] = objRegistration.Custom1 as AnyObject
                        dictBody["custom_2"] = objRegistration.Custom2 as AnyObject
                        dictBody["custom_3"] = objRegistration.Custom3 as AnyObject
                        dictBody["custom_4"] = objRegistration.Custom4 as AnyObject
                        dictBody["custom_5"] = objRegistration.Custom5 as AnyObject
                        dictBody["custom_6"] = objRegistration.Custom6 as AnyObject
                        dictBody["custom_7"] = objRegistration.Custom7 as AnyObject
                        dictBody["custom_8"] = objRegistration.Custom8 as AnyObject
                        dictBody["custom_9"] = objRegistration.Custom9 as AnyObject
                        dictBody["custom_10"] = objRegistration.Custom10 as AnyObject
                        dictBody["custom_11"] = objRegistration.Custom11 as AnyObject
                        dictBody["custom_12"] = objRegistration.Custom12 as AnyObject
                        dictBody["custom_13"] = objRegistration.Custom13 as AnyObject
                        dictBody["custom_14"] = objRegistration.Custom14 as AnyObject
                        dictBody["custom_15"] = objRegistration.Custom15 as AnyObject
                        dictBody["custom_16"] = objRegistration.Custom16 as AnyObject
                        dictBody["custom_17"] = objRegistration.Custom17 as AnyObject
                        dictBody["custom_18"] = objRegistration.Custom18 as AnyObject
                        dictBody["custom_19"] = objRegistration.Custom19 as AnyObject
                        dictBody["custom_20"] = objRegistration.Custom20 as AnyObject
                        dictBody["custom_21"] = objRegistration.Custom21 as AnyObject
                        dictBody["custom_22"] = objRegistration.Custom22 as AnyObject
                        dictBody["custom_23"] = objRegistration.Custom23 as AnyObject
                        dictBody["custom_24"] = objRegistration.Custom24 as AnyObject
                        dictBody["custom_25"] = objRegistration.Custom25 as AnyObject
                        dictBody["custom_26"] = objRegistration.Custom26 as AnyObject
                        dictBody["custom_27"] = objRegistration.Custom27 as AnyObject
                        dictBody["custom_28"] = objRegistration.Custom28 as AnyObject
                        dictBody["custom_29"] = objRegistration.Custom29 as AnyObject
                        dictBody["custom_30"] = objRegistration.Custom30 as AnyObject
                        dictBody["custom_31"] = objRegistration.Custom31 as AnyObject
                        dictBody["custom_32"] = objRegistration.Custom32 as AnyObject
                        dictBody["custom_33"] = objRegistration.Custom33 as AnyObject
                        dictBody["custom_34"] = objRegistration.Custom34 as AnyObject
                        dictBody["custom_35"] = objRegistration.Custom35 as AnyObject
                        dictBody["custom_36"] = objRegistration.Custom36 as AnyObject
                        dictBody["custom_37"] = objRegistration.Custom37 as AnyObject
                        dictBody["custom_38"] = objRegistration.Custom38 as AnyObject
                        dictBody["custom_39"] = objRegistration.Custom39 as AnyObject
                        dictBody["custom_40"] = objRegistration.Custom40 as AnyObject
                        dictBody["custom_41"] = objRegistration.Custom41 as AnyObject
                        dictBody["custom_42"] = objRegistration.Custom42 as AnyObject
                        dictBody["custom_43"] = objRegistration.Custom43 as AnyObject
                        dictBody["custom_44"] = objRegistration.Custom44 as AnyObject
                        dictBody["custom_45"] = objRegistration.Custom45 as AnyObject
                        dictBody["custom_46"] = objRegistration.Custom46 as AnyObject
                        dictBody["custom_47"] = objRegistration.Custom47 as AnyObject
                        dictBody["custom_48"] = objRegistration.Custom48 as AnyObject
                        dictBody["custom_49"] = objRegistration.Custom49 as AnyObject
                        dictBody["custom_50"] = objRegistration.Custom50 as AnyObject
                        dictBody["custom_51"] = objRegistration.Custom51 as AnyObject
                        dictBody["custom_52"] = objRegistration.Custom52 as AnyObject
                        dictBody["custom_53"] = objRegistration.Custom53 as AnyObject
                        dictBody["custom_54"] = objRegistration.Custom54 as AnyObject
                        dictBody["custom_55"] = objRegistration.Custom55 as AnyObject
                        dictBody["custom_56"] = objRegistration.Custom56 as AnyObject
                        dictBody["custom_57"] = objRegistration.Custom57 as AnyObject
                        dictBody["custom_58"] = objRegistration.Custom58 as AnyObject
                        dictBody["custom_59"] = objRegistration.Custom59 as AnyObject
                        dictBody["custom_60"] = objRegistration.Custom60 as AnyObject
                        dictBody["custom_61"] = objRegistration.Custom61 as AnyObject
                        dictBody["custom_62"] = objRegistration.Custom62 as AnyObject
                        dictBody["custom_63"] = objRegistration.Custom63 as AnyObject
                        dictBody["custom_64"] = objRegistration.Custom64 as AnyObject
                        dictBody["custom_65"] = objRegistration.Custom65 as AnyObject
                        dictBody["custom_66"] = objRegistration.Custom66 as AnyObject
                        dictBody["custom_67"] = objRegistration.Custom67 as AnyObject
                        dictBody["custom_68"] = objRegistration.Custom68 as AnyObject
                        dictBody["custom_69"] = objRegistration.Custom69 as AnyObject
                        dictBody["custom_70"] = objRegistration.Custom70 as AnyObject
                        dictBody["custom_71"] = objRegistration.Custom71 as AnyObject
                        dictBody["custom_72"] = objRegistration.Custom72 as AnyObject
                        dictBody["custom_73"] = objRegistration.Custom73 as AnyObject
                        dictBody["custom_74"] = objRegistration.Custom74 as AnyObject
                        dictBody["custom_75"] = objRegistration.Custom75 as AnyObject
                        dictBody["custom_76"] = objRegistration.Custom76 as AnyObject
                        dictBody["custom_77"] = objRegistration.Custom77 as AnyObject
                        dictBody["custom_78"] = objRegistration.Custom78 as AnyObject
                        dictBody["custom_79"] = objRegistration.Custom79 as AnyObject
                        dictBody["custom_80"] = objRegistration.Custom80 as AnyObject
                        dictBody["custom_81"] = objRegistration.Custom81 as AnyObject
                        dictBody["custom_82"] = objRegistration.Custom82 as AnyObject
                        dictBody["custom_83"] = objRegistration.Custom83 as AnyObject
                        dictBody["custom_84"] = objRegistration.Custom84 as AnyObject
                        dictBody["custom_85"] = objRegistration.Custom85 as AnyObject
                        dictBody["custom_86"] = objRegistration.Custom86 as AnyObject
                        dictBody["custom_87"] = objRegistration.Custom87 as AnyObject
                        dictBody["custom_88"] = objRegistration.Custom88 as AnyObject
                        dictBody["custom_89"] = objRegistration.Custom89 as AnyObject
                        dictBody["custom_90"] = objRegistration.Custom90 as AnyObject
                        dictBody["custom_91"] = objRegistration.Custom91 as AnyObject
                        dictBody["custom_92"] = objRegistration.Custom92 as AnyObject
                        dictBody["custom_93"] = objRegistration.Custom93 as AnyObject
                        dictBody["custom_94"] = objRegistration.Custom94 as AnyObject
                        dictBody["custom_95"] = objRegistration.Custom95 as AnyObject
                        dictBody["custom_96"] = objRegistration.Custom96 as AnyObject
                        dictBody["custom_97"] = objRegistration.Custom97 as AnyObject
                        dictBody["custom_98"] = objRegistration.Custom98 as AnyObject
                        dictBody["custom_99"] = objRegistration.Custom99 as AnyObject
                        dictBody["custom_100"] = objRegistration.Custom100 as AnyObject
                        
                        print("dictBody is \(dictBody)")
                        
                        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End
                        
                        // Create Data Task
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
                    
                }
            }
        }

    }
   
    
    // MARK: -  Change Member Password API
    
    public func changePasswordAPI(objLogin: FBLoginBO, completion: @escaping APIResponseDataCompletion) {
        
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
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["oldPassword"] = objLogin.oldPassword as AnyObject
        dictBody["password"] = objLogin.password as AnyObject
        
        
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
    
    public func getMemberAPI(completion: @escaping APIResponseDataCompletion) {
        
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
    
    public func updateMemberAPI(objRegistration: FBRegistrationBO, completion: @escaping APIResponseDataCompletion) {
        
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
        
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["firstName"] = objRegistration.FirstName as AnyObject
        dictBody["lastName"] =  objRegistration.LastName as AnyObject
        dictBody["email"] =     objRegistration.EmailAddress as AnyObject
        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
        dictBody["password"] = objRegistration.Password as AnyObject
        dictBody["birthDate"] = objRegistration.DOB as AnyObject
        dictBody["gender"] = objRegistration.Gender as AnyObject
        dictBody["addressStreet"] = objRegistration.Address as AnyObject
        dictBody["favoriteStore"] = objRegistration.FavoriteStore as AnyObject
        dictBody["phone"] = objRegistration.PhoneNumber as AnyObject
        dictBody["smsOptIn"] = objRegistration.SMSOptIn as AnyObject
        dictBody["pushOptIn"] = objRegistration.PushOptIn as AnyObject
        dictBody["addressState"] = objRegistration.State as AnyObject
        dictBody["addressCity"] = objRegistration.City as AnyObject
        dictBody["addressZipCode"] = objRegistration.ZipCode as AnyObject
        dictBody["storeCode"] = objRegistration.StoreCode as AnyObject
        dictBody["Bonus"] = objRegistration.Bonus as AnyObject
        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
        dictBody["addressCountry"] = objRegistration.Country as AnyObject
        dictBody["appId"] = objRegistration.appId as AnyObject
        dictBody["deviceId"] = objRegistration.appId as AnyObject
        dictBody["deviceOSVersion"] = objRegistration.deviceOSVersion as AnyObject
        dictBody["deviceType"] = objRegistration.deviceType as AnyObject
        dictBody["pushToken"] = objRegistration.pushToken as AnyObject
        dictBody["custom_1"] = objRegistration.Custom1 as AnyObject
        dictBody["custom_2"] = objRegistration.Custom2 as AnyObject
        dictBody["custom_3"] = objRegistration.Custom3 as AnyObject
        dictBody["custom_4"] = objRegistration.Custom4 as AnyObject
        dictBody["custom_5"] = objRegistration.Custom5 as AnyObject
        dictBody["custom_6"] = objRegistration.Custom6 as AnyObject
        dictBody["custom_7"] = objRegistration.Custom7 as AnyObject
        dictBody["custom_8"] = objRegistration.Custom8 as AnyObject
        dictBody["custom_9"] = objRegistration.Custom9 as AnyObject
        dictBody["custom_10"] = objRegistration.Custom10 as AnyObject
        dictBody["custom_11"] = objRegistration.Custom11 as AnyObject
        dictBody["custom_12"] = objRegistration.Custom12 as AnyObject
        dictBody["custom_13"] = objRegistration.Custom13 as AnyObject
        dictBody["custom_14"] = objRegistration.Custom14 as AnyObject
        dictBody["custom_15"] = objRegistration.Custom15 as AnyObject
        dictBody["custom_16"] = objRegistration.Custom16 as AnyObject
        dictBody["custom_17"] = objRegistration.Custom17 as AnyObject
        dictBody["custom_18"] = objRegistration.Custom18 as AnyObject
        dictBody["custom_19"] = objRegistration.Custom19 as AnyObject
        dictBody["custom_20"] = objRegistration.Custom20 as AnyObject
        dictBody["custom_21"] = objRegistration.Custom21 as AnyObject
        dictBody["custom_22"] = objRegistration.Custom22 as AnyObject
        dictBody["custom_23"] = objRegistration.Custom23 as AnyObject
        dictBody["custom_24"] = objRegistration.Custom24 as AnyObject
        dictBody["custom_25"] = objRegistration.Custom25 as AnyObject
        dictBody["custom_26"] = objRegistration.Custom26 as AnyObject
        dictBody["custom_27"] = objRegistration.Custom27 as AnyObject
        dictBody["custom_28"] = objRegistration.Custom28 as AnyObject
        dictBody["custom_29"] = objRegistration.Custom29 as AnyObject
        dictBody["custom_30"] = objRegistration.Custom30 as AnyObject
        dictBody["custom_31"] = objRegistration.Custom31 as AnyObject
        dictBody["custom_32"] = objRegistration.Custom32 as AnyObject
        dictBody["custom_33"] = objRegistration.Custom33 as AnyObject
        dictBody["custom_34"] = objRegistration.Custom34 as AnyObject
        dictBody["custom_35"] = objRegistration.Custom35 as AnyObject
        dictBody["custom_36"] = objRegistration.Custom36 as AnyObject
        dictBody["custom_37"] = objRegistration.Custom37 as AnyObject
        dictBody["custom_38"] = objRegistration.Custom38 as AnyObject
        dictBody["custom_39"] = objRegistration.Custom39 as AnyObject
        dictBody["custom_40"] = objRegistration.Custom40 as AnyObject
        dictBody["custom_41"] = objRegistration.Custom41 as AnyObject
        dictBody["custom_42"] = objRegistration.Custom42 as AnyObject
        dictBody["custom_43"] = objRegistration.Custom43 as AnyObject
        dictBody["custom_44"] = objRegistration.Custom44 as AnyObject
        dictBody["custom_45"] = objRegistration.Custom45 as AnyObject
        dictBody["custom_46"] = objRegistration.Custom46 as AnyObject
        dictBody["custom_47"] = objRegistration.Custom47 as AnyObject
        dictBody["custom_48"] = objRegistration.Custom48 as AnyObject
        dictBody["custom_49"] = objRegistration.Custom49 as AnyObject
        dictBody["custom_50"] = objRegistration.Custom50 as AnyObject
        dictBody["custom_51"] = objRegistration.Custom51 as AnyObject
        dictBody["custom_52"] = objRegistration.Custom52 as AnyObject
        dictBody["custom_53"] = objRegistration.Custom53 as AnyObject
        dictBody["custom_54"] = objRegistration.Custom54 as AnyObject
        dictBody["custom_55"] = objRegistration.Custom55 as AnyObject
        dictBody["custom_56"] = objRegistration.Custom56 as AnyObject
        dictBody["custom_57"] = objRegistration.Custom57 as AnyObject
        dictBody["custom_58"] = objRegistration.Custom58 as AnyObject
        dictBody["custom_59"] = objRegistration.Custom59 as AnyObject
        dictBody["custom_60"] = objRegistration.Custom60 as AnyObject
        dictBody["custom_61"] = objRegistration.Custom61 as AnyObject
        dictBody["custom_62"] = objRegistration.Custom62 as AnyObject
        dictBody["custom_63"] = objRegistration.Custom63 as AnyObject
        dictBody["custom_64"] = objRegistration.Custom64 as AnyObject
        dictBody["custom_65"] = objRegistration.Custom65 as AnyObject
        dictBody["custom_66"] = objRegistration.Custom66 as AnyObject
        dictBody["custom_67"] = objRegistration.Custom67 as AnyObject
        dictBody["custom_68"] = objRegistration.Custom68 as AnyObject
        dictBody["custom_69"] = objRegistration.Custom69 as AnyObject
        dictBody["custom_70"] = objRegistration.Custom70 as AnyObject
        dictBody["custom_71"] = objRegistration.Custom71 as AnyObject
        dictBody["custom_72"] = objRegistration.Custom72 as AnyObject
        dictBody["custom_73"] = objRegistration.Custom73 as AnyObject
        dictBody["custom_74"] = objRegistration.Custom74 as AnyObject
        dictBody["custom_75"] = objRegistration.Custom75 as AnyObject
        dictBody["custom_76"] = objRegistration.Custom76 as AnyObject
        dictBody["custom_77"] = objRegistration.Custom77 as AnyObject
        dictBody["custom_78"] = objRegistration.Custom78 as AnyObject
        dictBody["custom_79"] = objRegistration.Custom79 as AnyObject
        dictBody["custom_80"] = objRegistration.Custom80 as AnyObject
        dictBody["custom_81"] = objRegistration.Custom81 as AnyObject
        dictBody["custom_82"] = objRegistration.Custom82 as AnyObject
        dictBody["custom_83"] = objRegistration.Custom83 as AnyObject
        dictBody["custom_84"] = objRegistration.Custom84 as AnyObject
        dictBody["custom_85"] = objRegistration.Custom85 as AnyObject
        dictBody["custom_86"] = objRegistration.Custom86 as AnyObject
        dictBody["custom_87"] = objRegistration.Custom87 as AnyObject
        dictBody["custom_88"] = objRegistration.Custom88 as AnyObject
        dictBody["custom_89"] = objRegistration.Custom89 as AnyObject
        dictBody["custom_90"] = objRegistration.Custom90 as AnyObject
        dictBody["custom_91"] = objRegistration.Custom91 as AnyObject
        dictBody["custom_92"] = objRegistration.Custom92 as AnyObject
        dictBody["custom_93"] = objRegistration.Custom93 as AnyObject
        dictBody["custom_94"] = objRegistration.Custom94 as AnyObject
        dictBody["custom_95"] = objRegistration.Custom95 as AnyObject
        dictBody["custom_96"] = objRegistration.Custom96 as AnyObject
        dictBody["custom_97"] = objRegistration.Custom97 as AnyObject
        dictBody["custom_98"] = objRegistration.Custom98 as AnyObject
        dictBody["custom_99"] = objRegistration.Custom99 as AnyObject
        dictBody["custom_100"] = objRegistration.Custom100 as AnyObject
        
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
    
    public func getOffersAPI(CustomerId:String, completion: @escaping APIResponseDataCompletion) {
        
        
        let NumberofOffers : String = "/100"

        let urlStr: String = "\(strBaseURL)\(SUBURL.Getoffers)\(CustomerId)\(NumberofOffers)"         // Full URL Creation of API
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
    
    public func getRewardsAPI(CustomerId:String, completion: @escaping APIResponseDataCompletion) {
       
        let NumberofRewards : String = "/100"
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetRewards)\(CustomerId)\(NumberofRewards)"        // Full URL Creation of API
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
    
    public func getLoyalityPointsAPI(customerId:String, completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func getOfferbyOfferIdAPI(offerId:String, completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func getAllStoresAPI(completion: @escaping APIResponseDataCompletion) {
        
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
    
    public func updateDeviceAPI(objDevice: FBDeviceBO, completion: @escaping APIResponseDataCompletion) {
        
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
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["appId"] = objDevice.appId as AnyObject
        dictBody["deviceId"] = self.deviceID() as AnyObject
        dictBody["deviceOSVersion"] = objDevice.deviceOSVersion as AnyObject
        dictBody["deviceType"] =  "iPhone" as AnyObject
        dictBody["memberid"] = objDevice.memberId as AnyObject
        dictBody["pushToken"] = objDevice.pushToken as AnyObject
        

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
    
    public func ForgotPasswordAPI(emailAddress: String, completion: @escaping APIResponseDataCompletion) {
        
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
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["email"] = emailAddress as AnyObject
        
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
    
    public func storeSearchAPI(objStore: FBStoreBO, completion: @escaping APIResponseDataCompletion) {
        
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
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["query"] = objStore.query as AnyObject
        dictBody["radius"] = objStore.radius as AnyObject
        dictBody["count"] = objStore.count as AnyObject
        
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
    
    public func favouriteStoreApi(objStore: FBStoreBO, completion: @escaping APIResponseDataCompletion) {
        
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
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["memberid"] = objStore.memberId as AnyObject
        dictBody["storeCode"] = objStore.storeCode as AnyObject
        
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
    
    public func stateListAPI(completion: @escaping APIResponseDataCompletion) {
        
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
    
    public func getPointBankOfferAPI(CustomerId:String, completion: @escaping APIResponseDataCompletion) {
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.PointBankoffer)\(CustomerId)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                            // Changing String to URL
        
        
        var request = URLRequest(url: url as URL)                       // Request Creation Start
        request.httpMethod = MethodType.GET                             // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                     // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()     // Retrieve Token Value
        
        print("strToken is \(strToken)")
        print("urlStr is \(urlStr)")
        
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
    
    public func useOfferAPI(objOffer: FBOfferBO, completion: @escaping APIResponseDataCompletion) {
        
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
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["memberId"] = objOffer.memberId as AnyObject
        dictBody["offerId"] = objOffer.offerId as AnyObject
        dictBody["tenantId"] =  strTanentId as AnyObject
        dictBody["claimPoints"] = objOffer.claimPoints as AnyObject
        
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
    
    public func getMenuCategoryAPI(storeId:String, completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func getMenuSubCategoryAPI(storeId:String,categoryId: String, completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func getMenuProductListAPI(storeId:String,categoryId: String,subCategoryId:String, completion: @escaping APIResponseDataCompletion) {
        
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
    
    public func getMenuProductAttributesAPI(storeId:String,categoryId: String,subCategoryId:String,productId:String, completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func getAllRewardOfferAPI(completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func signupRuleListAPI(completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func getStoreDetailsAPI(storeId:String, completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func mobileAppEventsAPI(arrEvents: NSArray, completion: @escaping APIResponseDataCompletion) {
        
        
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
        
        
        
        let arrRegisteredEvents : NSMutableArray  = []
        var dictBody = Dictionary<String, AnyObject>()
        
        
        for  i in (0..<arrEvents.count) {
            
            let objEvent : FBEventBO = arrEvents[i] as! FBEventBO
            var dictSubBody = Dictionary<String, AnyObject>()
            dictSubBody["item_id"] = objEvent.itemId as AnyObject
            dictSubBody["item_name"] =  objEvent.itemName as AnyObject
            dictSubBody["event_name"] = objEvent.eventName as AnyObject
            dictSubBody["memberid"] = objEvent.memberId as AnyObject
            dictSubBody["lat"] = objEvent.latitude as AnyObject
            dictSubBody["lon"] = objEvent.longitude as AnyObject
            dictSubBody["tenantid"] = strTanentId as AnyObject
            dictSubBody["device_type"] = objEvent.deviceType as AnyObject
            dictSubBody["device_os_ver"] = objEvent.deviceOsVersion as AnyObject
            
            arrRegisteredEvents.add(dictSubBody)
            
        }
        
        dictBody["mobileAppEvent"] = arrRegisteredEvents as NSMutableArray

        
        
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
    
    public func GuestUserMobileAppEventsAPI(arrEvents: NSArray, completion: @escaping APIResponseDataCompletion) {
        
        
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
        
        let arrRegisteredEvents : NSMutableArray  = []
        var dictBody = Dictionary<String, AnyObject>()


        for  i in (0..<arrEvents.count) {
            
            let objEvent : FBEventBO = arrEvents[i] as! FBEventBO
            var dictSubBody = Dictionary<String, AnyObject>()
            dictSubBody["item_id"] = objEvent.itemId as AnyObject
            dictSubBody["item_name"] =  objEvent.itemName as AnyObject
            dictSubBody["event_name"] = objEvent.eventName as AnyObject
            dictSubBody["memberid"] = objEvent.memberId as AnyObject
            dictSubBody["lat"] = objEvent.latitude as AnyObject
            dictSubBody["lon"] = objEvent.longitude as AnyObject
            dictSubBody["tenantid"] = strTanentId as AnyObject
            dictSubBody["device_type"] = objEvent.deviceType as AnyObject
            dictSubBody["device_os_ver"] = objEvent.deviceOsVersion as AnyObject
            
            arrRegisteredEvents.add(dictSubBody)

        }
        
        dictBody["mobileAppEvent"] = arrRegisteredEvents as NSMutableArray

        
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
    
    public func getLoyaltySettingsAPI(completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func getDefaultThemeAPI(completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    public func InCommTokenAPI(objGift: FBGiftCardBO, completion: @escaping APIResponseDataCompletion) {
        
        
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
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["key"] = objGift.key as AnyObject
        dictBody["authorization"] = objGift.authorization as AnyObject
        dictBody["spendgoId"] =  objGift.spendgoId as AnyObject
        
        
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
    
    public func OrderValueAPI(objOrderValue: FBGiftCardOrderValueBO, completion: @escaping APIResponseDataCompletion) {
        
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
        
        
        var dictBody = Dictionary<String, AnyObject>()
        var dictQueryBody = Dictionary<String, AnyObject>()

        dictQueryBody["amount"] = objOrderValue.amount as AnyObject
        dictQueryBody["messageTo"] = objOrderValue.messageTo as AnyObject
        var dictQueryInCommBody = Dictionary<String, AnyObject>()
        dictQueryInCommBody["key"] = objOrderValue.key as AnyObject
        dictQueryInCommBody["authorization"] = objOrderValue.authorization as AnyObject
        dictQueryInCommBody["spendgoId"] = objOrderValue.spendgoId as AnyObject
        dictQueryBody["incommInput"] = dictQueryInCommBody as AnyObject
        dictQueryBody["incommToken"] = objOrderValue.incommToken as AnyObject
        
        dictBody["query"] = dictQueryBody as AnyObject
        
        var dictResultBody = Dictionary<String, AnyObject>()
        var dictPaymentBody = Dictionary<String, AnyObject>()
        dictPaymentBody["OrderPaymentMethod"] = objOrderValue.orderPaymentMethod as AnyObject
        dictResultBody["Payment"] = dictPaymentBody as AnyObject
        var dictPurchaserBody = Dictionary<String, AnyObject>()
        dictPurchaserBody["EmailAddress"] = objOrderValue.purchaserEmailAddress as AnyObject
        dictPurchaserBody["EmailAddress"] = objOrderValue.purchaserFirstName as AnyObject
        dictPurchaserBody["LastName"] = objOrderValue.purchaserLastName as AnyObject
        dictPurchaserBody["Country"] = objOrderValue.purchaserCountry as AnyObject //
        dictPurchaserBody["SuppressReceiptEmail"] = objOrderValue.purchaserSuppressReceiptEmail as AnyObject
        dictResultBody["Purchaser"] = dictPurchaserBody as AnyObject
        
        dictBody["Purchaser"] = dictResultBody as AnyObject
        
        var dictRecipientsBody = Dictionary<String, AnyObject>()
        let arrRecipientsBody : NSMutableArray = []

        for  i in (0..<objOrderValue.arrRecipients.count) {

            let objRecipients : RecipientsBO = objOrderValue.arrRecipients[i] as! RecipientsBO
            var dictRecipientsBody = Dictionary<String, AnyObject>()
            dictRecipientsBody["EmailAddress"] = objRecipients.emailAddress as AnyObject
            dictRecipientsBody["FirstName"] = objRecipients.firstName as AnyObject
            dictRecipientsBody["LastName"] = objRecipients.lastName as AnyObject
            dictRecipientsBody["Country"] = objRecipients.country as AnyObject
            arrRecipientsBody.add(dictRecipientsBody)

        }
        dictRecipientsBody["Recipients"] = arrRecipientsBody as NSArray

        dictBody["query"] = dictRecipientsBody as AnyObject
      
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
    
    public func InCommOrderIDAPI(objGift: FBGiftCardBO,completion: @escaping APIResponseDataCompletion) {
        
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
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["customerId"] = objGift.memberId as AnyObject
        
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
    
    public func updateIncommTransactionAPI(objGift: FBGiftCardBO, completion: @escaping APIResponseDataCompletion) {
        

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
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["inCommOrderId"] = objGift.memberId as AnyObject
        dictBody["orderStatus"] = objGift.orderStatus as AnyObject
        dictBody["errorReason"] = objGift.errorReason as AnyObject
        dictBody["customerId"] = objGift.memberId as AnyObject
        dictBody["orderDateTime"] = objGift.orderDateTime as AnyObject
        
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
    
    public func getMemberByExternalMemberIdAPI(spendgoId:String, completion: @escaping APIResponseDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.getMemberbyExternalMemberId)\(spendgoId)"    // Full URL Creation of API
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
    
    
    // MARK: -  Client Mobile Setting API
    
    public func mobileSettingAPI(completion: @escaping APIResponseDataCompletion) {
        
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
    
    
    // MARK: -  Member update by email API
    
    public func MemberUpdatebyEmailAPI(dictBody: [String : AnyObject], completion: @escaping APIResponseDataCompletion) {
        
        
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
    
    
    
    // MARK: -  New CRM API

    // MARK: -  Member Create API
    
    public func CreateNewMemberAPI(objRegistration: FBRegistrationBO, completion: @escaping APIResponseDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.CreateNewMember)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                                     // Changing String to URL
        
        var request = URLRequest(url: url as URL)                                                // Request Creation Start
        request.httpMethod = MethodType.POST                                                      // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                              // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()              // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["firstName"] = objRegistration.FirstName as AnyObject
        dictBody["lastName"] =  objRegistration.LastName as AnyObject
        dictBody["email"] =     objRegistration.EmailAddress as AnyObject
        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
        dictBody["password"] = objRegistration.Password as AnyObject
        dictBody["birthDate"] = objRegistration.DOB as AnyObject
        dictBody["gender"] = objRegistration.Gender as AnyObject
        dictBody["addressStreet"] = objRegistration.Address as AnyObject
        dictBody["favoriteStore"] = objRegistration.FavoriteStore as AnyObject
        dictBody["phone"] = objRegistration.PhoneNumber as AnyObject
        dictBody["smsOptIn"] = objRegistration.SMSOptIn as AnyObject
        dictBody["pushOptIn"] = objRegistration.PushOptIn as AnyObject
        dictBody["addressState"] = objRegistration.State as AnyObject
        dictBody["addressCity"] = objRegistration.City as AnyObject
        dictBody["addressZipCode"] = objRegistration.ZipCode as AnyObject
        dictBody["storeCode"] = objRegistration.StoreCode as AnyObject
        dictBody["Bonus"] = objRegistration.Bonus as AnyObject
        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
        dictBody["addressCountry"] = objRegistration.Country as AnyObject
        dictBody["appId"] = objRegistration.appId as AnyObject
        dictBody["deviceId"] = objRegistration.appId as AnyObject
        dictBody["deviceOSVersion"] = objRegistration.deviceOSVersion as AnyObject
        dictBody["deviceType"] = objRegistration.deviceType as AnyObject
        dictBody["pushToken"] = objRegistration.pushToken as AnyObject
        dictBody["custom_1"] = objRegistration.Custom1 as AnyObject
        dictBody["custom_2"] = objRegistration.Custom2 as AnyObject
        dictBody["custom_3"] = objRegistration.Custom3 as AnyObject
        dictBody["custom_4"] = objRegistration.Custom4 as AnyObject
        dictBody["custom_5"] = objRegistration.Custom5 as AnyObject
        dictBody["custom_6"] = objRegistration.Custom6 as AnyObject
        dictBody["custom_7"] = objRegistration.Custom7 as AnyObject
        dictBody["custom_8"] = objRegistration.Custom8 as AnyObject
        dictBody["custom_9"] = objRegistration.Custom9 as AnyObject
        dictBody["custom_10"] = objRegistration.Custom10 as AnyObject
        dictBody["custom_11"] = objRegistration.Custom11 as AnyObject
        dictBody["custom_12"] = objRegistration.Custom12 as AnyObject
        dictBody["custom_13"] = objRegistration.Custom13 as AnyObject
        dictBody["custom_14"] = objRegistration.Custom14 as AnyObject
        dictBody["custom_15"] = objRegistration.Custom15 as AnyObject
        dictBody["custom_16"] = objRegistration.Custom16 as AnyObject
        dictBody["custom_17"] = objRegistration.Custom17 as AnyObject
        dictBody["custom_18"] = objRegistration.Custom18 as AnyObject
        dictBody["custom_19"] = objRegistration.Custom19 as AnyObject
        dictBody["custom_20"] = objRegistration.Custom20 as AnyObject
        dictBody["custom_21"] = objRegistration.Custom21 as AnyObject
        dictBody["custom_22"] = objRegistration.Custom22 as AnyObject
        dictBody["custom_23"] = objRegistration.Custom23 as AnyObject
        dictBody["custom_24"] = objRegistration.Custom24 as AnyObject
        dictBody["custom_25"] = objRegistration.Custom25 as AnyObject
        dictBody["custom_26"] = objRegistration.Custom26 as AnyObject
        dictBody["custom_27"] = objRegistration.Custom27 as AnyObject
        dictBody["custom_28"] = objRegistration.Custom28 as AnyObject
        dictBody["custom_29"] = objRegistration.Custom29 as AnyObject
        dictBody["custom_30"] = objRegistration.Custom30 as AnyObject
        dictBody["custom_31"] = objRegistration.Custom31 as AnyObject
        dictBody["custom_32"] = objRegistration.Custom32 as AnyObject
        dictBody["custom_33"] = objRegistration.Custom33 as AnyObject
        dictBody["custom_34"] = objRegistration.Custom34 as AnyObject
        dictBody["custom_35"] = objRegistration.Custom35 as AnyObject
        dictBody["custom_36"] = objRegistration.Custom36 as AnyObject
        dictBody["custom_37"] = objRegistration.Custom37 as AnyObject
        dictBody["custom_38"] = objRegistration.Custom38 as AnyObject
        dictBody["custom_39"] = objRegistration.Custom39 as AnyObject
        dictBody["custom_40"] = objRegistration.Custom40 as AnyObject
        dictBody["custom_41"] = objRegistration.Custom41 as AnyObject
        dictBody["custom_42"] = objRegistration.Custom42 as AnyObject
        dictBody["custom_43"] = objRegistration.Custom43 as AnyObject
        dictBody["custom_44"] = objRegistration.Custom44 as AnyObject
        dictBody["custom_45"] = objRegistration.Custom45 as AnyObject
        dictBody["custom_46"] = objRegistration.Custom46 as AnyObject
        dictBody["custom_47"] = objRegistration.Custom47 as AnyObject
        dictBody["custom_48"] = objRegistration.Custom48 as AnyObject
        dictBody["custom_49"] = objRegistration.Custom49 as AnyObject
        dictBody["custom_50"] = objRegistration.Custom50 as AnyObject
        dictBody["custom_51"] = objRegistration.Custom51 as AnyObject
        dictBody["custom_52"] = objRegistration.Custom52 as AnyObject
        dictBody["custom_53"] = objRegistration.Custom53 as AnyObject
        dictBody["custom_54"] = objRegistration.Custom54 as AnyObject
        dictBody["custom_55"] = objRegistration.Custom55 as AnyObject
        dictBody["custom_56"] = objRegistration.Custom56 as AnyObject
        dictBody["custom_57"] = objRegistration.Custom57 as AnyObject
        dictBody["custom_58"] = objRegistration.Custom58 as AnyObject
        dictBody["custom_59"] = objRegistration.Custom59 as AnyObject
        dictBody["custom_60"] = objRegistration.Custom60 as AnyObject
        dictBody["custom_61"] = objRegistration.Custom61 as AnyObject
        dictBody["custom_62"] = objRegistration.Custom62 as AnyObject
        dictBody["custom_63"] = objRegistration.Custom63 as AnyObject
        dictBody["custom_64"] = objRegistration.Custom64 as AnyObject
        dictBody["custom_65"] = objRegistration.Custom65 as AnyObject
        dictBody["custom_66"] = objRegistration.Custom66 as AnyObject
        dictBody["custom_67"] = objRegistration.Custom67 as AnyObject
        dictBody["custom_68"] = objRegistration.Custom68 as AnyObject
        dictBody["custom_69"] = objRegistration.Custom69 as AnyObject
        dictBody["custom_70"] = objRegistration.Custom70 as AnyObject
        dictBody["custom_71"] = objRegistration.Custom71 as AnyObject
        dictBody["custom_72"] = objRegistration.Custom72 as AnyObject
        dictBody["custom_73"] = objRegistration.Custom73 as AnyObject
        dictBody["custom_74"] = objRegistration.Custom74 as AnyObject
        dictBody["custom_75"] = objRegistration.Custom75 as AnyObject
        dictBody["custom_76"] = objRegistration.Custom76 as AnyObject
        dictBody["custom_77"] = objRegistration.Custom77 as AnyObject
        dictBody["custom_78"] = objRegistration.Custom78 as AnyObject
        dictBody["custom_79"] = objRegistration.Custom79 as AnyObject
        dictBody["custom_80"] = objRegistration.Custom80 as AnyObject
        dictBody["custom_81"] = objRegistration.Custom81 as AnyObject
        dictBody["custom_82"] = objRegistration.Custom82 as AnyObject
        dictBody["custom_83"] = objRegistration.Custom83 as AnyObject
        dictBody["custom_84"] = objRegistration.Custom84 as AnyObject
        dictBody["custom_85"] = objRegistration.Custom85 as AnyObject
        dictBody["custom_86"] = objRegistration.Custom86 as AnyObject
        dictBody["custom_87"] = objRegistration.Custom87 as AnyObject
        dictBody["custom_88"] = objRegistration.Custom88 as AnyObject
        dictBody["custom_89"] = objRegistration.Custom89 as AnyObject
        dictBody["custom_90"] = objRegistration.Custom90 as AnyObject
        dictBody["custom_91"] = objRegistration.Custom91 as AnyObject
        dictBody["custom_92"] = objRegistration.Custom92 as AnyObject
        dictBody["custom_93"] = objRegistration.Custom93 as AnyObject
        dictBody["custom_94"] = objRegistration.Custom94 as AnyObject
        dictBody["custom_95"] = objRegistration.Custom95 as AnyObject
        dictBody["custom_96"] = objRegistration.Custom96 as AnyObject
        dictBody["custom_97"] = objRegistration.Custom97 as AnyObject
        dictBody["custom_98"] = objRegistration.Custom98 as AnyObject
        dictBody["custom_99"] = objRegistration.Custom99 as AnyObject
        dictBody["custom_100"] = objRegistration.Custom100 as AnyObject
        
        


        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End

        
        // Create Data Tasks
        URLSession.shared.dataTask(with: request) { (data, response, error) in
            self.didFetchCreateNewMemberData(data: data, response: response, error: error, completion: completion)
            }.resume()
        
    }
    
    // MARK: -   Get Member Details API

    public func GetMemberDetailsAPI(completion: @escaping APIResponseDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.GetMemberDetails)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                                     // Changing String to URL
        
        var request = URLRequest(url: url as URL)                         // Request Creation Start
        request.httpMethod = MethodType.GET                               // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                              // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()              // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        
        
        // Create Data Tasks
        URLSession.shared.dataTask(with: request) { (data, response, error) in
            self.didFetchMemberDetailsData(data: data, response: response, error: error, completion: completion)
            }.resume()
        
    }

    
    // MARK: -   Update Member Details API
    
    public func UpdateMemberDetailsAPI(objRegistration: FBRegistrationBO, completion: @escaping APIResponseDataCompletion) {
        
        
        let urlStr: String = "\(strBaseURL)\(SUBURL.UpdateMemberDetails)"    // Full URL Creation of API
        let url : URL = URL(string: urlStr)!                                                     // Changing String to URL
        
        var request = URLRequest(url: url as URL)                         // Request Creation Start
        request.httpMethod = MethodType.PUT                               // Method Type i.e. POST,GET,UPDATE
        
        
        let mySingleton = ModelClass.sharedInstance                              // Call Singleton object
        let strToken : String = mySingleton.retrieveFishbowlToken()              // Retrieve Token Value
        
        
        request.setValue(applicationType, forHTTPHeaderField: APIHeaderkey.Application)    //Manadatory Header ApplicationType
        request.setValue(strClientId, forHTTPHeaderField: APIHeaderkey.ClientId)           //Manadatory Header ClientId
        request.setValue(contentType, forHTTPHeaderField: APIHeaderkey.ContentType)        //Manadatory Header ContentType
        request.setValue(strTanentId, forHTTPHeaderField: APIHeaderkey.TenantId)           //Manadatory Header TenantId
        request.setValue(self.deviceID(), forHTTPHeaderField: APIHeaderkey.DeviceId)       //Manadatory Header DeviceId
        request.setValue(strClientSecret, forHTTPHeaderField: APIHeaderkey.ClientSecret)   //Manadatory Header ClientSecret
        request.setValue(strToken, forHTTPHeaderField: APIHeaderkey.AccessToken)           //Manadatory Header AccessToken
        
        
        
        var dictBody = Dictionary<String, AnyObject>()
        dictBody["firstName"] = objRegistration.FirstName as AnyObject
        dictBody["lastName"] =  objRegistration.LastName as AnyObject
        dictBody["email"] =     objRegistration.EmailAddress as AnyObject
        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
        dictBody["password"] = objRegistration.Password as AnyObject
        dictBody["birthDate"] = objRegistration.DOB as AnyObject
        dictBody["gender"] = objRegistration.Gender as AnyObject
        dictBody["addressStreet"] = objRegistration.Address as AnyObject
        dictBody["favoriteStore"] = objRegistration.FavoriteStore as AnyObject
        dictBody["phone"] = objRegistration.PhoneNumber as AnyObject
        dictBody["smsOptIn"] = objRegistration.SMSOptIn as AnyObject
        dictBody["pushOptIn"] = objRegistration.PushOptIn as AnyObject
        dictBody["addressState"] = objRegistration.State as AnyObject
        dictBody["addressCity"] = objRegistration.City as AnyObject
        dictBody["addressZipCode"] = objRegistration.ZipCode as AnyObject
        dictBody["storeCode"] = objRegistration.StoreCode as AnyObject
        dictBody["Bonus"] = objRegistration.Bonus as AnyObject
        dictBody["emailOptIn"] = objRegistration.EmailOptIn as AnyObject
        dictBody["addressCountry"] = objRegistration.Country as AnyObject
        dictBody["appId"] = objRegistration.appId as AnyObject
        dictBody["deviceId"] = objRegistration.appId as AnyObject
        dictBody["deviceOSVersion"] = objRegistration.deviceOSVersion as AnyObject
        dictBody["deviceType"] = objRegistration.deviceType as AnyObject
        dictBody["pushToken"] = objRegistration.pushToken as AnyObject
        dictBody["custom_1"] = objRegistration.Custom1 as AnyObject
        dictBody["custom_2"] = objRegistration.Custom2 as AnyObject
        dictBody["custom_3"] = objRegistration.Custom3 as AnyObject
        dictBody["custom_4"] = objRegistration.Custom4 as AnyObject
        dictBody["custom_5"] = objRegistration.Custom5 as AnyObject
        dictBody["custom_6"] = objRegistration.Custom6 as AnyObject
        dictBody["custom_7"] = objRegistration.Custom7 as AnyObject
        dictBody["custom_8"] = objRegistration.Custom8 as AnyObject
        dictBody["custom_9"] = objRegistration.Custom9 as AnyObject
        dictBody["custom_10"] = objRegistration.Custom10 as AnyObject
        dictBody["custom_11"] = objRegistration.Custom11 as AnyObject
        dictBody["custom_12"] = objRegistration.Custom12 as AnyObject
        dictBody["custom_13"] = objRegistration.Custom13 as AnyObject
        dictBody["custom_14"] = objRegistration.Custom14 as AnyObject
        dictBody["custom_15"] = objRegistration.Custom15 as AnyObject
        dictBody["custom_16"] = objRegistration.Custom16 as AnyObject
        dictBody["custom_17"] = objRegistration.Custom17 as AnyObject
        dictBody["custom_18"] = objRegistration.Custom18 as AnyObject
        dictBody["custom_19"] = objRegistration.Custom19 as AnyObject
        dictBody["custom_20"] = objRegistration.Custom20 as AnyObject
        dictBody["custom_21"] = objRegistration.Custom21 as AnyObject
        dictBody["custom_22"] = objRegistration.Custom22 as AnyObject
        dictBody["custom_23"] = objRegistration.Custom23 as AnyObject
        dictBody["custom_24"] = objRegistration.Custom24 as AnyObject
        dictBody["custom_25"] = objRegistration.Custom25 as AnyObject
        dictBody["custom_26"] = objRegistration.Custom26 as AnyObject
        dictBody["custom_27"] = objRegistration.Custom27 as AnyObject
        dictBody["custom_28"] = objRegistration.Custom28 as AnyObject
        dictBody["custom_29"] = objRegistration.Custom29 as AnyObject
        dictBody["custom_30"] = objRegistration.Custom30 as AnyObject
        dictBody["custom_31"] = objRegistration.Custom31 as AnyObject
        dictBody["custom_32"] = objRegistration.Custom32 as AnyObject
        dictBody["custom_33"] = objRegistration.Custom33 as AnyObject
        dictBody["custom_34"] = objRegistration.Custom34 as AnyObject
        dictBody["custom_35"] = objRegistration.Custom35 as AnyObject
        dictBody["custom_36"] = objRegistration.Custom36 as AnyObject
        dictBody["custom_37"] = objRegistration.Custom37 as AnyObject
        dictBody["custom_38"] = objRegistration.Custom38 as AnyObject
        dictBody["custom_39"] = objRegistration.Custom39 as AnyObject
        dictBody["custom_40"] = objRegistration.Custom40 as AnyObject
        dictBody["custom_41"] = objRegistration.Custom41 as AnyObject
        dictBody["custom_42"] = objRegistration.Custom42 as AnyObject
        dictBody["custom_43"] = objRegistration.Custom43 as AnyObject
        dictBody["custom_44"] = objRegistration.Custom44 as AnyObject
        dictBody["custom_45"] = objRegistration.Custom45 as AnyObject
        dictBody["custom_46"] = objRegistration.Custom46 as AnyObject
        dictBody["custom_47"] = objRegistration.Custom47 as AnyObject
        dictBody["custom_48"] = objRegistration.Custom48 as AnyObject
        dictBody["custom_49"] = objRegistration.Custom49 as AnyObject
        dictBody["custom_50"] = objRegistration.Custom50 as AnyObject
        dictBody["custom_51"] = objRegistration.Custom51 as AnyObject
        dictBody["custom_52"] = objRegistration.Custom52 as AnyObject
        dictBody["custom_53"] = objRegistration.Custom53 as AnyObject
        dictBody["custom_54"] = objRegistration.Custom54 as AnyObject
        dictBody["custom_55"] = objRegistration.Custom55 as AnyObject
        dictBody["custom_56"] = objRegistration.Custom56 as AnyObject
        dictBody["custom_57"] = objRegistration.Custom57 as AnyObject
        dictBody["custom_58"] = objRegistration.Custom58 as AnyObject
        dictBody["custom_59"] = objRegistration.Custom59 as AnyObject
        dictBody["custom_60"] = objRegistration.Custom60 as AnyObject
        dictBody["custom_61"] = objRegistration.Custom61 as AnyObject
        dictBody["custom_62"] = objRegistration.Custom62 as AnyObject
        dictBody["custom_63"] = objRegistration.Custom63 as AnyObject
        dictBody["custom_64"] = objRegistration.Custom64 as AnyObject
        dictBody["custom_65"] = objRegistration.Custom65 as AnyObject
        dictBody["custom_66"] = objRegistration.Custom66 as AnyObject
        dictBody["custom_67"] = objRegistration.Custom67 as AnyObject
        dictBody["custom_68"] = objRegistration.Custom68 as AnyObject
        dictBody["custom_69"] = objRegistration.Custom69 as AnyObject
        dictBody["custom_70"] = objRegistration.Custom70 as AnyObject
        dictBody["custom_71"] = objRegistration.Custom71 as AnyObject
        dictBody["custom_72"] = objRegistration.Custom72 as AnyObject
        dictBody["custom_73"] = objRegistration.Custom73 as AnyObject
        dictBody["custom_74"] = objRegistration.Custom74 as AnyObject
        dictBody["custom_75"] = objRegistration.Custom75 as AnyObject
        dictBody["custom_76"] = objRegistration.Custom76 as AnyObject
        dictBody["custom_77"] = objRegistration.Custom77 as AnyObject
        dictBody["custom_78"] = objRegistration.Custom78 as AnyObject
        dictBody["custom_79"] = objRegistration.Custom79 as AnyObject
        dictBody["custom_80"] = objRegistration.Custom80 as AnyObject
        dictBody["custom_81"] = objRegistration.Custom81 as AnyObject
        dictBody["custom_82"] = objRegistration.Custom82 as AnyObject
        dictBody["custom_83"] = objRegistration.Custom83 as AnyObject
        dictBody["custom_84"] = objRegistration.Custom84 as AnyObject
        dictBody["custom_85"] = objRegistration.Custom85 as AnyObject
        dictBody["custom_86"] = objRegistration.Custom86 as AnyObject
        dictBody["custom_87"] = objRegistration.Custom87 as AnyObject
        dictBody["custom_88"] = objRegistration.Custom88 as AnyObject
        dictBody["custom_89"] = objRegistration.Custom89 as AnyObject
        dictBody["custom_90"] = objRegistration.Custom90 as AnyObject
        dictBody["custom_91"] = objRegistration.Custom91 as AnyObject
        dictBody["custom_92"] = objRegistration.Custom92 as AnyObject
        dictBody["custom_93"] = objRegistration.Custom93 as AnyObject
        dictBody["custom_94"] = objRegistration.Custom94 as AnyObject
        dictBody["custom_95"] = objRegistration.Custom95 as AnyObject
        dictBody["custom_96"] = objRegistration.Custom96 as AnyObject
        dictBody["custom_97"] = objRegistration.Custom97 as AnyObject
        dictBody["custom_98"] = objRegistration.Custom98 as AnyObject
        dictBody["custom_99"] = objRegistration.Custom99 as AnyObject
        dictBody["custom_100"] = objRegistration.Custom100 as AnyObject
        
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])    // Request Creation End

        
        // Create Data Tasks
        URLSession.shared.dataTask(with: request) { (data, response, error) in
            self.didFetchUpdateMemberDetailsData(data: data, response: response, error: error, completion: completion)
            }.resume()
        
    }



    

    // MARK: - Helper Methods
    
    private func didFetchTokenData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO

                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchSignupData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchLoginSDKData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO

                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMemberData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            
            if response.statusCode == 200 {
                var myModel : FBRegistrationBO
                print("response before is \(response.description)")
                print("data before is \(data.description)")
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    print("jsonResult before is \(JSON)")

                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        print("jsonResult is \(jsonResult)")
                        
                        guard let newdata = FBRegistrationBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRegistrationBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchUpdateDeviceData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchLoginData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        guard error == nil && data != nil else
        {
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            print("response before is \(response.description)")
            
            if response.statusCode == 200 {
                var myModel : FBRespnoseBO

              
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {

                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                          return
                        }
                        
                         myModel = newdata as FBRespnoseBO
                         completion(myModel as ResponseBO,nil)
                    }
                }
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
            
        }
            
        else {
            
            if let data = data
            {
                processFailedData(data: data, completion: completion)
            }

        }
        
    }
    
    
    // MARK: - Helper Methods
    
    private func didFetchLogoutData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO

                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMemberSignupData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMobileSettingData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBMobileSettingsBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBMobileSettingsBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBMobileSettingsBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchChangePasswordData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchGetMemberData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            if response.statusCode == 200 {
                var myModel : FBRegistrationBO
                print("response before is \(response.description)")
                print("data before is \(data.description)")
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    print("jsonResult before is \(JSON)")
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        print("jsonResult is \(jsonResult)")
                        
                        guard let newdata = FBRegistrationBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRegistrationBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchUpdateMemberData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchOffersData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBOfferBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBOfferBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBOfferBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchRewardsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
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
                var myModel : FBOfferBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    print("JSON before is \(JSON)")

                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        print("jsonResult before is \(jsonResult)")

                        
                        guard let newdata = FBOfferBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBOfferBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchLoyalityPointsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
        guard error == nil && data != nil else
        {
            // let strMessage:String = error!.localizedDescription
            return
        }
        
        if let _ = error {
            processFailedData(data: data!, completion: completion)
            
            
        } else if let data = data, let response = response as? HTTPURLResponse {
            
            
            if response.statusCode == 200 {
                print("response before is \(response.description)")

                var myModel : FBLoyaltyPointsBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    print("JSON before is \(JSON)")
                    
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        print("jsonResult before is \(jsonResult)")
                        
                        guard let newdata = FBLoyaltyPointsBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBLoyaltyPointsBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchOfferbyOfferIdData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBOfferBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    print("JSON before is \(JSON)")
                    
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        print("jsonResult before is \(jsonResult)")
                        
                        
                        guard let newdata = FBOfferBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBOfferBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchStoreListData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBStoreBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    print("JSON before is \(JSON)")
                    
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        print("jsonResult before is \(jsonResult)")
                        
                        
                        guard let newdata = FBStoreBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBStoreBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchupdateDeviceData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchForgotPasswordData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchStoreSearchData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBStoreBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    print("JSON before is \(JSON)")
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        print("jsonResult before is \(jsonResult)")
                        
                        
                        guard let newdata = FBStoreBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBStoreBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchFavouriteStoreData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchStateListData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
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
                var myModel : FBStateBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    print("JSON before is \(JSON)")
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        print("jsonResult before is \(jsonResult)")
                        
                        guard let newdata = FBStateBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBStateBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchPointBankOfferData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBOfferBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBOfferBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBOfferBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchUseOfferData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMenuCategoryData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBMenuCategoryBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBMenuCategoryBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBMenuCategoryBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMenuSubCategoryData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBMenuSubCategoryBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBMenuSubCategoryBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBMenuSubCategoryBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMenuProductListData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBMenuProductListBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBMenuProductListBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBMenuProductListBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMenuProductAttributesData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBMenuProductAttributeBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBMenuProductAttributeBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBMenuProductAttributeBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchRewardOfferData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : RewardOfferBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = RewardOfferBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as RewardOfferBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchSignupRuleListData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBsignupRuleListBO
                
                print("JSON is 200")

                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    print("JSON is \(JSON)")

                    if let jsonResult = JSON as? NSArray {
                        
                        print("jsonResult is \(jsonResult)")
                        guard let newdata = FBsignupRuleListBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBsignupRuleListBO
                        print("myModel is \(myModel)")
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchStoreDetailsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBStoreBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    print("JSON before is \(JSON)")
                    
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        print("jsonResult before is \(jsonResult)")
                        
                        
                        guard let newdata = FBStoreBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBStoreBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMobileAppEventsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBEventRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? NSArray {

                        print("jsonResult is \(jsonResult)")
                        guard let newdata = FBEventRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBEventRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchGuestUserMobileAppEventsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBEventRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? NSArray {
                        
                        print("jsonResult is \(jsonResult)")
                        guard let newdata = FBEventRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBEventRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchLoyaltySettingsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBLoyaltySettingsBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    print("JSON before is \(JSON)")
                    
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        print("jsonResult before is \(jsonResult)")
                        
                        
                        guard let newdata = FBLoyaltySettingsBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBLoyaltySettingsBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchDefaultThemeData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBThemeSettingsBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    print("JSON before is \(JSON)")
                    
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        print("jsonResult before is \(jsonResult)")
                        
                        
                        guard let newdata = FBThemeSettingsBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBThemeSettingsBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchInCommTokenData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBGiftCardRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBGiftCardRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBGiftCardRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchOrderValueData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBGiftCardRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBGiftCardRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBGiftCardRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchInCommOrderIDData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
        
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
                var myModel : FBGiftCardRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBGiftCardRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBGiftCardRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchUpdateIncommTransactionData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBGiftCardRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBGiftCardRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBGiftCardRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMemberByExternalMemberIdData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRegistrationBO
                print("response before is \(response.description)")
                print("data before is \(data.description)")
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    print("jsonResult before is \(JSON)")
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        print("jsonResult is \(jsonResult)")
                        
                        guard let newdata = FBRegistrationBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRegistrationBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didMemberUpdatebyEmailData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                //processData(data: data, completion: completion)
                
                
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
    
    private func didFetchCreateNewMemberData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchMemberDetailsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRegistrationBO
                print("response before is \(response.description)")
                print("data before is \(data.description)")
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    print("jsonResult before is \(JSON)")
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        print("jsonResult is \(jsonResult)")
                        
                        guard let newdata = FBRegistrationBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRegistrationBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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
    
    private func didFetchUpdateMemberDetailsData(data: Data?, response: URLResponse?, error: Error?, completion: @escaping APIResponseDataCompletion) {
        
        
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
                var myModel : FBRespnoseBO
                
                if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                    
                    if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                        
                        guard let newdata = FBRespnoseBO(json: jsonResult) else {
                            
                            return
                        }
                        
                        myModel = newdata as FBRespnoseBO
                        completion(myModel as ResponseBO,nil)
                    }
                }
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


    
    
  
    private func processInternalServerErrorData(data: Data, completion: @escaping APIResponseDataCompletion) {
        
        var errorModel : ErrorBO
        
        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
            
            if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                
                guard let errorData =  ErrorBO(json:jsonResult) else
                {
                    return
                }
                
                errorModel = errorData as ErrorBO
                completion(errorModel as ResponseBO,.InternalServerError)
            }
            
        }
        else {
            completion(nil, .InternalServerError)
        }
        
//        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
//            completion(JSON, .InternalServerError)
//        } else {
//            completion(nil, .InternalServerError)
//        }
    }
    
    
    
    private func processServerUnavailableData(data: Data, completion: @escaping APIResponseDataCompletion) {
        
            var errorModel : ErrorBO
            
            if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                
                if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                    
                    guard let errorData =  ErrorBO(json:jsonResult) else
                    {
                        return
                    }
                    
                    errorModel = errorData as ErrorBO
                    completion(errorModel as ResponseBO,.ServerUnavailable)
                }
                
            }
            else {
                completion(nil, .ServerUnavailable)
            }
        }
        
        
//        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
//            completion(JSON, .ServerUnavailable)
//        } else {
//            completion(nil, .ServerUnavailable)
//        }
    

    private func processBadRequestData(data: Data, completion: @escaping APIResponseDataCompletion) {
        
        var errorModel : ErrorBO
        
        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
            
            if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                
                guard let errorData =  ErrorBO(json:jsonResult) else
                {
                    return
                }
                
                errorModel = errorData as ErrorBO
                completion(errorModel as ResponseBO,.BadRequest)
            }
            
        }
        else {
            completion(nil, .BadRequest)
        }
    }
    
    
    private func processTokenExpirationData(data: Data, completion: @escaping APIResponseDataCompletion) {
        
            var errorModel : ErrorBO
        
            if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
                
                if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                    
                    guard let errorData =  ErrorBO(json:jsonResult) else
                    {
                        return
                    }
                    
                    errorModel = errorData as ErrorBO
                    completion(errorModel as ResponseBO,.TokenExpiration)
                }
                
            }
            else {
                completion(nil, .TokenExpiration)
        }
    }
        
    
    
//        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
//            completion(JSON, .TokenExpiration)
//        } else {
//            completion(nil, .TokenExpiration)
//        }
    
    private func processFailedData(data: Data, completion: @escaping APIResponseDataCompletion) {
//        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
//            completion(JSON, .InvalidResponse)
//        } else {
//            completion(nil, .InvalidResponse)
//        }
        
        
        var errorModel : ErrorBO
        
        if let JSON = try? JSONSerialization.jsonObject(with: data, options: []) as AnyObject {
            
            if let jsonResult = JSON as? Dictionary<String, AnyObject> {
                
                guard let errorData =  ErrorBO(json:jsonResult) else
                {
                    return
                }
                
                errorModel = errorData as ErrorBO
                completion(errorModel as ResponseBO,.InvalidResponse)
            }
            
        }
        else {
                completion(nil, .InvalidResponse)
        }
        
        
        
    }

}
