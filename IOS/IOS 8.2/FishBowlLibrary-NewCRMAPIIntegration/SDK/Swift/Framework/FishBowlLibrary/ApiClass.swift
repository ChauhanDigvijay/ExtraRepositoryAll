//
//  ApiClass.swift
//  FishBowlLibrary
//
//  Created by FishBowl  on 01/02/17.
//  Copyright © 2017 Fishbowl. All rights reserved.
//


import UIKit




public class ApiClass: NSObject {
   
    
    var m_callBackTarget: AnyObject!
    var m_callBackSelector: Selector!
    
    

    // MARK: DeviceID
    
    public func deviceID() -> String {
        var string: String = UIDevice.current.identifierForVendor!.uuidString
        string = string.replacingOccurrences(of: "-", with: "")
        string = "\(string)"
        return string
    }
    
  
    // MARK: GuestUserApi
    
    public func  GuestUserAPI(subUrl: String,dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        
  
        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        var request = URLRequest(url: url as URL)
        request.httpMethod = MethodType.POST
        
        request.setValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.setValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.setValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else
            {
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                self.callServerError(str1: error.localizedFailureReason! as AnyObject)
            }
        }
        task.resume()
    }
    
  
    // MARK: loginAPI
    
  public func  loginAPI(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.MemberLogin
        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        var request = URLRequest(url: url as URL)
        request.httpMethod = MethodType.POST
        
        request.setValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.setValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else
            {
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                self.callServerError(str1: error.localizedFailureReason! as AnyObject)
            }
        }
        task.resume()
    }
    
    
    
    // MARK: getTokenAPI
    
    public func getToken()
    {
        
        // 1
        let subUrl = SUBURL.MobileToken
        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.PUT
        request.setValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        
        var inputs = [String : AnyObject]()
        
        inputs["strClientId"] = strClientId as AnyObject?
        inputs["clientSecret"] = strClientSecret as AnyObject?
        inputs["tenantid"] = strTanentId as AnyObject?
        inputs["deviceId"] = self.deviceID() as AnyObject?
        
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: inputs, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                
                let strToken : String = responseObject?.object(forKey: "message") as! String
                UserDefaults.standard.set(strToken, forKey: APIkey.AccessToken) //String
                
//                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
                
            } catch let error as NSError
            {
                print(error)
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    
    
    
    // MARK: getTokenAPI
    
    public func getTokenAPI(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.MobileToken
        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.PUT
        request.setValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        
        var inputs = [String : AnyObject]()
        
        inputs["strClientId"] = strClientId as AnyObject?
        inputs["clientSecret"] = strClientSecret as AnyObject?
        inputs["tenantid"] = strTanentId as AnyObject?
        inputs["deviceId"] = self.deviceID() as AnyObject?
        
   
        request.httpBody = try! JSONSerialization.data(withJSONObject: inputs, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
                
            } catch let error as NSError
            {
                print(error)
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: mobileSettingAPI
    
   public  func mobileSettingAPI(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget   = tempTarget
        // 1
        let subUrl = SUBURL.MobileSetting

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
        
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                }
                catch
                {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: viewLoyaltySettingsAPI
    
     public  func viewLoyaltySettingsAPI(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget   = tempTarget
        // 1
        let subUrl = SUBURL.LoyalitySetting
        
        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
       // let request : NSMutableURLRequest = NSMutableURLRequest()
       // request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
       // request.httpMethod = MethodType.GET
       // request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET

        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                }
                catch
                {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
        
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: Theme API integration
    
     public  func getDefaultTheme(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget   = tempTarget
        // 1
        
        let subUrl = SUBURL.DefaultTheme
        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET

        
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response))")

                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                }
                catch
                {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    

    
    
    // MARK: updateDevice Api
    
     public func updateDevice(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.UpdateDevice
        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.PUT
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                print(error)
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
 
    // MARK: logoutAPI
    
    public func logoutAPI(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.MemberLogout
        
        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.POST
        
      if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
      {
          request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
      }
        request.setValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.setValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.setValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                
                self.callServerError(str1: error.localizedFailureReason! as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: signUpAPI
    
    public func signUpAPI(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.MemberSignup

        let urlStr: String = "\(strURL)\(subUrl)"
        print(urlStr)
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.POST
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
     
        request.setValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
            
                self.callServerError(str1: error.localizedFailureReason! as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: updateMemberAPI
    
   public func updateMemberAPI(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        
        let subUrl = SUBURL.MemberUpdate

        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.PUT
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {

                self.m_callBackTarget.perform(self.m_callBackSelector, with: httpResponse.statusCode)

            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                print(error)
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
     // MARK: changePasswordAPI
    
   public func changePasswordAPI(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.ChangePassword
        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.PUT
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        
        request.setValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                print(error)
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: Forgot Password
    
    public func ForgotPasswordApi(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.ForgotPassword
        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.POST
        
   
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.setValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                print(error)
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    
    // MARK: getMemberAPI
    
   public func getMemberAPI(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.GetmemberProfile

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET

        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
       
        
         let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    
                    print("error serializing JSON: \(error)")
                    let strMessage:String = error as! String
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
        
            default:
                
                print("wallet GET request got response \(httpResponse.statusCode)")
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode) "
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: getLoyalityPoints
    
    public func getLoyalityPoints(customerId : String, subUrl: String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
 
        // 1
        let subUrl = SUBURL.PointRewardInfo + customerId

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET

        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription 
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
       
       
    }
    
    
    // MARK: getPointBankOffer
    
 public func getPointBankOffer(customerId: String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
      
        let subUrl =  SUBURL.PointBankoffer + customerId

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
 //       let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
      
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
       
        
    }
    
    
    // MARK: getOffersApi
    
   public func getOffersApi(customerId: String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
       
        let subUrl =   SUBURL.Getoffers + customerId + "/0"

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break

            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: getRewardsApi
    
   public func getRewardsApi(customerId: String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl =  SUBURL.GetRewards + customerId + "/0"

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
          
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
          task.resume()
    }
    
    
    // MARK: GetOfferByOfferId API
    
    public func getuserOffer(offerId: String,Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        
        let subUrl =  SUBURL.GetofferByOfferId + offerId

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)

        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
        
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    
    // MARK: getallrewardoffer API
    
    public func getAllRewardOffer(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.GetAllRewardOffers
        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
           
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
      // MARK: getLoyaltyCardAPI
    
   public func getLoyaltyCardAPI(customerID: String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl =  SUBURL.GetLoyaltyCard + customerID

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        
        print(request.description)
       let task = session.dataTask(with: request as URLRequest) { data, response, error in
        
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                self.m_callBackTarget.perform(self.m_callBackSelector, with: receivedData)
                break
           
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
   
    
    // MARK: State And City API
    
    public func getCityAndStateAPI(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.GetStates

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
//        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
       let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
            
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
                
            }
        }
        task.resume()
    }
    
    
    
    // MARK: getAllStoresAPI
    
   public func getAllStores(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.GetStoreList
        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
     
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
      
        print(request.description)
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
           
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
                
            }
        }
        task.resume()
    }
    

    // MARK: getStoreDetails
    
   public func getStoreDetails(storeId: String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.GetStoreDetails + storeId
        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
  
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
       
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
                       default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
                
            }
        }
        task.resume()
    }
    
    
    // MARK: storeSearchApi
    
    public func storeSearchApi(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.SearchStores

        let urlStr: String = "\(strURL)\(subUrl)"
        let session = URLSession.shared
//        let request = NSMutableURLRequest(url: url as URL)
//        request.httpMethod = MethodType.POST
//        request.timeoutInterval = 60
//        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlStr) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.POST
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    
    // MARK: favouritStorehApi
    
    public func favouritStorehApi(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.UpdateStore
        let urlStr: String = "\(strURL)\(subUrl)"
//       let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
//        let request = NSMutableURLRequest(url: url as URL)
//        request.httpMethod = MethodType.PUT
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlStr) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.PUT
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
       
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: mobileAppEvents
    
    public func mobileAppEvents(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.SubmitEvents

        let urlStr: String = "\(strURL)\(subUrl)"
//        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
//        let request = NSMutableURLRequest(url: url as URL)
//        request.httpMethod = MethodType.POST
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlStr) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.POST
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    // MARK: Menu(Category)
    
    public  func menuApi(storeId: String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget   = tempTarget
        // 1
        let subUrl =  SUBURL.GetMenu + storeId

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        print("get url string is \(urlString)")
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
//        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                }
                catch
                {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
                       default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    // MARK: Menu(SubCategory)
    
    public  func SubCategory(storeId: String,categoryId :String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget   = tempTarget
        // 1
        let subUrl =  SUBURL.SubCategoryURLPart1 + storeId +  SUBURL.SubCategoryURLPart2 + categoryId
        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        print("get url string is \(urlString)")
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                }
                catch
                {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
           
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    
    // MARK: Menu Drawer (Product List)
    
    public  func ProductList(storeId: String,categoryId :String,subCategoryId:String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget   = tempTarget
        // 1
        let subUrl =   SUBURL.ProductListURLPart1 + storeId + SUBURL.ProductListURLPart2 + categoryId + SUBURL.ProductListURLPart3 + subCategoryId

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                }
                catch
                {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
           
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    
    
    // MARK: MenuProductDetail
    
    public  func ProductAttributes(storeId: String,categoryId :String,subCategoryId:String,productId:String,withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget   = tempTarget
        // 1
        let subUrl =  SUBURL.productAttributeURLPart1 + storeId + SUBURL.productAttributeURLPart2 + categoryId + SUBURL.productAttributeURLPart3 + subCategoryId + SUBURL.productAttributeURLPart4 + productId

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
//        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                }
                catch
                {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
            
            default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
     // MARK: SignupRuleListAPI
    
   public func signupRuleListAPI(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.SignupRuleList

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET

        
       let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
                      default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: useOfferAPI
    
   public func useOfferAPI(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.Useoffer

        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.PUT
        let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)!
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
        self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                print(error)
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    
    // MARK: RedeemedAPI
    
   public func redeemedApi(Target tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.RedeemdedOffer

        let urlStr: String = "\(strURL)\(subUrl)"
        let configuration = URLSessionConfiguration .default
        let session = URLSession(configuration: configuration)
        let urlString = NSString(format: urlStr as NSString)
//        let request : NSMutableURLRequest = NSMutableURLRequest()
//        request.url = NSURL(string: NSString(format: "%@", urlString) as String) as URL?
//        request.httpMethod = MethodType.GET
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlString) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.GET
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        
        
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // 1: Check HTTP Response for successful GET request
            guard let httpResponse = response as? HTTPURLResponse, let receivedData = data
                else {
                    print(error!)
                    let strMessage:String = error!.localizedDescription
                    self.callServerError(str1: strMessage as AnyObject)
                    return
            }
            switch (httpResponse.statusCode)
            {
            case 200:
                let response = NSString (data: receivedData, encoding: String.Encoding.utf8.rawValue)
                print("response is \(String(describing: response?.description))")
                do {
                    let getResponse = try JSONSerialization.jsonObject(with: receivedData, options: .allowFragments)
                    self.m_callBackTarget.perform(self.m_callBackSelector, with: getResponse)
                } catch {
                    let strMessage:String = error as! String
                    self.callServerError(str1: strMessage as AnyObject)
                }
                break
                default:
                let strMessage:String = "Server Error response code is: \(httpResponse.statusCode)"
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    // MARK: Gitft Card API
    
    // MARK: Get In Common Token API
    
    public func getinCommonTokenAPI(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        
        let subUrl = SUBURL.IncommToken

         let urlStr: String = "\(strURL)\(subUrl)"
//        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
//        let request = NSMutableURLRequest(url: url as URL)
//        request.httpMethod = MethodType.POST
//        request.timeoutInterval = 60
        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlStr) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.POST
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                print(error)
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: orderValue API

    
    public func orderValueAPI(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.IncommRewards

        let urlStr: String = "\(strURL)\(subUrl)"
//        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
//        let request = NSMutableURLRequest(url: url as URL)
//        request.httpMethod = MethodType.POST
//        request.timeoutInterval = 60
        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlStr) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.POST
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }


    // MARK: IncommonOrderId API

    
    public func getIncommonOrderId(orderId: String,dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl =  SUBURL.IncommOrder + orderId

        
        let urlStr: String = "\(strURL)\(subUrl)"
//        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
//        let request = NSMutableURLRequest(url: url as URL)
//        request.httpMethod = MethodType.POST
//        request.timeoutInterval = 60
        
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlStr) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.POST
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    
    // MARK: updateIncommTransactionDetails API

    
    public func updateIncommTransactionDetails(orderId: String,dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl =  SUBURL.IncommOrder  + orderId
        let urlStr: String = "\(strURL)\(subUrl)"
//        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
//        let request = NSMutableURLRequest(url: url as URL)
//        request.httpMethod = MethodType.PUT
//        request.timeoutInterval = 60
        
        let request = NSMutableURLRequest(url: URL(string: NSString(format: "%@", urlStr) as String)!, cachePolicy: NSURLRequest.CachePolicy.reloadIgnoringLocalCacheData, timeoutInterval: 60)
        request.httpMethod = MethodType.PUT
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        request.addValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }
    
    // MARK: GuestUserAPI
    
    public func GuestUserAPI(dictBody: [NSObject : AnyObject], withTarget tempTarget: AnyObject, withSelector tempSelector: Selector)
    {
        m_callBackSelector = tempSelector
        m_callBackTarget = tempTarget
        // 1
        let subUrl = SUBURL.GuestRegiter
        
        let urlStr: String = "\(strURL)\(subUrl)"
        let url: NSURL = NSURL(string: urlStr)!
        let session = URLSession.shared
        let request = NSMutableURLRequest(url: url as URL)
        request.httpMethod = MethodType.POST
        
        if  let str_access_token: String = UserDefaults.standard.string(forKey: APIkey.AccessToken)
        {
            request.addValue(str_access_token, forHTTPHeaderField: APIkey.AccessToken)
        }
        
        
        request.addValue(contentType, forHTTPHeaderField: APIkey.ContentType)
        request.addValue(strClientId, forHTTPHeaderField: APIkey.ClientId)
        request.addValue(applicationType, forHTTPHeaderField: APIkey.Application)
        
        request.setValue(strTanentId, forHTTPHeaderField: APIkey.TenantId)
        request.setValue(strClientSecret, forHTTPHeaderField: APIkey.ClientSecret)
        request.setValue(self.deviceID(), forHTTPHeaderField: APIkey.DeviceId)
        
        
        request.httpBody = try! JSONSerialization.data(withJSONObject: dictBody, options: [])
        let task = session.dataTask(with: request as URLRequest) { data, response, error in
            // handle fundamental network errors (e.g. no connectivity)
            guard error == nil && data != nil else {
                print(error!)
                let strMessage:String = error!.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
                return
            }
            // check that http status code was 200
            if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200
            {
                print(httpResponse.statusCode)
            }
            // parse the JSON response
            do {
                let responseObject = try JSONSerialization.jsonObject(with: data!, options: []) as? NSDictionary
                self.m_callBackTarget.perform(self.m_callBackSelector, with: responseObject)
            } catch let error as NSError
            {
                let strMessage:String = error.localizedDescription
                self.callServerError(str1: strMessage as AnyObject)
            }
        }
        task.resume()
    }

    
    // MARK: ServerError
    
   public func callServerError(str1:AnyObject)
    {
        var myDict = [String: AnyObject]()
        myDict["message"] = str1 as! String as AnyObject?
        myDict["successFlag"] = false as AnyObject?
        self.m_callBackTarget.perform(self.m_callBackSelector, with: myDict)
    }
    
    
}
