//
//  SpendGoService.swift
//  SpendGoSDK
//
//  Created by Taha Samad on 5/11/15.
//  Copyright (c) 2015 Hathway, Inc. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON
import CryptoSwift
import XCGLogger
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
    switch (lhs, rhs) {
    case let (l?, r?):
        return l < r
    case (nil, _?):
        return true
    default:
        return false
    }
}

// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
    switch (lhs, rhs) {
    case let (l?, r?):
        return l > r
    default:
        return rhs < lhs
    }
}


public typealias SpendGoJSONDictonary = [String: AnyObject]
public typealias SpendGoServiceCallback = (_ response: JSON, _ error: NSError?) -> Void

open class SpendGoService {
    
    static var configuration: SpendGoServiceConfiguration?
    static var logger: XCGLogger? {
        return configuration?.logger
    }
    
    open class func configurationForService(_ APIBaseURL: String, APIKey: String, SigningKey: String, logger: XCGLogger) {
        configuration = SpendGoServiceConfiguration(apiBaseURL: APIBaseURL, apiKey: APIKey, signingKey: SigningKey, logger: logger)
    }
    
    open class func get(_ path: String, parameters: SpendGoJSONDictonary? = nil, needsAuthToken: Bool = false, callback: @escaping SpendGoServiceCallback) {
        sendRequest(.get, path: path, parameters: parameters, needsAuthToken: needsAuthToken, callback: callback)
    }
    
    open class func post(_ path: String, parameters: SpendGoJSONDictonary, needsAuthToken: Bool = false, callback: @escaping SpendGoServiceCallback) {
        sendRequest(.post, path: path, parameters: parameters, needsAuthToken: needsAuthToken, callback: callback)
    }
    
    open class func put(_ path: String, parameters: SpendGoJSONDictonary, needsAuthToken: Bool = false, callback: @escaping SpendGoServiceCallback) {
        sendRequest(.put, path: path, parameters: parameters, needsAuthToken: needsAuthToken, callback: callback)
    }
    
    open class func delete(_ path: String, parameters: SpendGoJSONDictonary? = nil, needsAuthToken: Bool = false, callback: @escaping SpendGoServiceCallback) {
        sendRequest(.delete, path: path, parameters: parameters, needsAuthToken: needsAuthToken, callback: callback)
    }
    
    class func sendRequest(_ method: Alamofire.HTTPMethod, path: String, parameters: SpendGoJSONDictonary?, needsAuthToken: Bool = false, callback: @escaping SpendGoServiceCallback) {
        
        // Build OLO url
        let urlString = self.buildSpendGoURL(path)
        
        // Make Request
        let URL = Foundation.URL(string: urlString)
        var requestPreparation = URLRequest(url: URL!)
        
        // Set HTTP Method
        requestPreparation.httpMethod = method.rawValue
        
        // Encode parameters
        var urlRequest:URLRequestConvertible
        // Encode as JSON Body if PUT or POST
        if method == .put || method == .post {
            let encoding = JSONEncoding.default
            do {
                urlRequest = try encoding.encode(requestPreparation as URLRequestConvertible, with: parameters)
            }
            catch{
                callback(JSON.null, SpendGoUtils.error("Url not converted", code: 0))
                return
            }
        }
            // Encode as Query String
        else {
            let encoding = URLEncoding.default
            do {
                urlRequest = try encoding.encode(requestPreparation as URLRequestConvertible, with: parameters)
            }
            catch{
                callback(JSON.null, SpendGoUtils.error("Url not converted", code: 0))
                return
            }
        }
        let mutableURLRequest:NSMutableURLRequest = urlRequest as! NSMutableURLRequest
        // Add Authentication Token To Headers if needed
        if needsAuthToken {
            if let authToken = SpendGoSessionService.authToken {
                mutableURLRequest.setValue(authToken, forHTTPHeaderField: "Authorization")
            }
            else {
                callback(JSON.null, SpendGoUtils.error("User is not authenticated.", code: 0))
                return
            }
        }
        
        // Calculate Signature
        let urlAndBodyData = (URL!.path.data(using: String.Encoding.utf8, allowLossyConversion: false)! as NSData).mutableCopy() as! NSMutableData
        if mutableURLRequest.httpBody?.count > 0 {
            urlAndBodyData.append(mutableURLRequest.httpBody!)
        }
        let signatureString = calculateSignature(urlAndBodyData as Data)
        
        // Add headers
        mutableURLRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
        mutableURLRequest.setValue("application/json", forHTTPHeaderField: "Accept")
        mutableURLRequest.setValue(configuration?.apiKey, forHTTPHeaderField: "X-Class-Key")
        mutableURLRequest.setValue(signatureString, forHTTPHeaderField: "X-Class-Signature")
        
        logger?.info("******** NEW SPENDGO REQUEST ********")
        logger?.info("Url: \(String(describing: URL?.absoluteString))")
        logger?.verbose("Method: \(method.rawValue)")
        logger?.verbose("Parameters: \(JSON(parameters ?? [:]))")
        
        // Make request
        let request = Alamofire.request(mutableURLRequest.copy() as! Foundation.URLRequest)
        // Handle Response (support both JSON and Plain Text responses)
        request.responseString(encoding: String.Encoding.utf8) { response in
            logger?.info("******** SPENDGO RESPONSE ********")
            guard let httpRequest = response.request else {
                //  fatalError("Could not get request from server")
                self.handleResponse(mutableURLRequest as URLRequest, response: nil, body: response.result.value, error: SpendGoUtils.error("User is not authenticated.", code: 0), callback: callback)
                
                return
            }
            guard let httpResponse = response.response else {
                self.handleResponse(mutableURLRequest as URLRequest, response: nil, body: response.result.value, error: SpendGoUtils.error("User is not authenticated.", code: 0), callback: callback)
                
                // fatalError("Could not get response from server")
                return
                
            }
            
            switch response.result {
            case .success(let value):
                logger?.info("Status Code: \(httpResponse.statusCode)")
                logger?.verbose("Headers: \(JSON(httpResponse.allHeaderFields))")
                if self.isJSON(httpResponse) {
                    let json = self.parseBody(value )
                    logger?.verbose("Body: \(json)")
                } else {
                    logger?.verbose("Body: \(value)")
                }
            case .failure(let error):
                logger?.error("Error: \(error.localizedDescription)")
            }
            self.handleResponse(httpRequest, response: httpResponse, body: response.result.value, error: response.result.error as NSError?, callback: callback)
        }
    }
    
    fileprivate class func handleResponse(_ request: URLRequest, response: HTTPURLResponse?, body: String?, error: NSError?, callback: SpendGoServiceCallback) {
        
        // Check for request errors
        if error != nil {
            callback(JSON.null, error)
            return
        }
        
        // If response is missing or not 200 OK, return error
        if response == nil {
            callback(JSON.null, SpendGoUtils.error("Received no response from server"))
            return
        }
        
        if response!.statusCode != 200 {
            //Special Handling for 202
            if response!.statusCode == 202 {
                callback(JSON.null, SpendGoUtils.error("Success. Changes will be appled after validation is complete.", code: 202, domain: SpendGoErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue))
                return
            }
                // If error, attempt to get the message from JSON
            else if isJSON(response!) {
                let json = parseBody(body ?? "")
                let message = json["details"].stringValue
                let code = json["code"].intValue
                //If we have auth error try to check if we have invalid auth callback. Then let the callback handle and dictate the behavior
                if code == 1010 && SpendGoSessionService.invalidAuthTokenCallback != nil {
                    SpendGoSessionService.invalidAuthTokenCallback!(request, response!, json, callback)
                }
                else {
                    callback(json, SpendGoUtils.error(message, code: code, domain: SpendGoErrorDomain.ServiceErrorFromJSONBody.rawValue))
                }
                return
            }
            
            // Attempt to get error message from body
            logger?.warning("Warning: plain/text response received!!")
            callback(JSON.null, SpendGoUtils.error(body ?? "Unexpected error encountered during the request", code: response!.statusCode, domain: SpendGoErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue))
            return
        }
        
        // 200 OK with JSON body
        if isJSON(response!) {
            callback(parseBody(body ?? ""), nil)
            return
        }
        
        // 200 OK with plain/text body, attempt converting body to JSON, if not, assume error
        if body != nil && body!.isEmpty == false {
            logger?.warning("Warning: plain/text response received with 200 OK!!!")
            let json = parseBody(body!)
            if json == JSON.null {
                callback(JSON.null, SpendGoUtils.error(body!))
                return
            }
            callback(json, nil)
            return
        }
        
        // 200 OK, empty body
        callback(JSON.null, nil)
    }
    
    fileprivate class func isJSON(_ response: HTTPURLResponse) -> Bool {
        if let contentType = response.allHeaderFields["Content-Type"] as? String {
            return contentType.range(of: "application/json") != nil
        }
        return false
    }
    
    /// Parse plain text string into SwiftyJSON, return null if string is not JSON
    fileprivate class func parseBody(_ body: String) -> JSON {
        if let dataFromString = body.data(using: String.Encoding.utf8, allowLossyConversion: false) {
            return JSON(data: dataFromString)
        }
        return JSON.null
    }
    
    fileprivate class func buildSpendGoURL(_ path: String) -> String {
        guard let config = configuration else {
            fatalError("Missing configuration")
        }
        return config.apiBaseURL + path
    }
    
    fileprivate class func calculateSignature(_ data: Data) -> String {
        guard let config = configuration else {
            fatalError("Missing configuration")
        }
        //Convert Base64 Encoded String
        let keyData = Data(base64Encoded: config.signingKey, options: NSData.Base64DecodingOptions(rawValue: 0))
        logger?.verbose("Key-HEX:\(keyData!.toHexString)")
        logger?.verbose("DATA-HEX:\(data.toHexString)")
        
        //Make array of keys
        var keyArray = [UInt8](repeating: 0, count: keyData!.count)
        (keyData! as NSData).getBytes(&keyArray, length: keyData!.count)
        
        //Make array of data
        var dataArray:[UInt8] = [UInt8](repeating: 0, count: data.count)
        (data as NSData).getBytes(&dataArray, length: data.count)
        guard let hmac = try? HMAC(key: keyArray, variant: .sha256).authenticate(dataArray) else {
            logger?.error("Error generating SpendGo signature")
            fatalError("Error generating SpendGo signature")
        }
        let signatureData = Data(bytes: hmac)
        let signatureString = signatureData.base64EncodedString(options: NSData.Base64EncodingOptions(rawValue: 0))
        //        let signatureData = Data(bytes: hmac, length: hmac.count)
        //        let signatureString = signatureData.base64EncodedStringWithOptions(NSData.Base64EncodingOptions(rawValue: 0))
        
        logger?.verbose(signatureString)
        return signatureString
    }
    
    open class func calculateSignatureForFishbowlLogin(_ method: Alamofire.HTTPMethod, path: String, parameters: SpendGoJSONDictonary?, needsAuthToken: Bool = false) -> String {
        
        // Build OLO url
        let urlString = self.buildSpendGoURL(path)
        
        // Make Request
        let URL = Foundation.URL(string: urlString)
        var requestPreparation = URLRequest(url: URL!)
        
        // Set HTTP Method
        requestPreparation.httpMethod = method.rawValue
        
        // Encode parameters
        var urlRequest:URLRequestConvertible?
        // Encode as JSON Body if PUT or POST
        if method == .put || method == .post {
            let encoding = JSONEncoding.default
            do {
                urlRequest = try encoding.encode(requestPreparation as URLRequestConvertible, with: parameters)
            }
            catch{
                return ""
            }
        }
            // Encode as Query String
        else {
            let encoding = URLEncoding.default
            do {
                urlRequest = try encoding.encode(requestPreparation as URLRequestConvertible, with: parameters)
            }
            catch{
                return ""
            }
        }
        let mutableURLRequest = urlRequest as! NSMutableURLRequest
        
        // Add Authentication Token To Headers if needed
        if needsAuthToken {
            if let authToken = SpendGoSessionService.authToken {
                mutableURLRequest.setValue(authToken, forHTTPHeaderField: "Authorization")
            }
            else {
                return ""
            }
        }
        
        // Calculate Signature
        let urlAndBodyData = (URL!.path.data(using: String.Encoding.utf8, allowLossyConversion: false)! as NSData).mutableCopy() as! NSMutableData
        if mutableURLRequest.httpBody?.count > 0 {
            urlAndBodyData.append(mutableURLRequest.httpBody!)
        }
        return calculateSignature(urlAndBodyData as Data)
        
    }
    
}
