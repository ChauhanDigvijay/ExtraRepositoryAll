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

public typealias SpendGoJSONDictonary = [String: AnyObject]
public typealias SpendGoServiceCallback = (response: JSON, error: NSError?) -> Void

public class SpendGoService {
    
    static var configuration: SpendGoServiceConfiguration?
    static var logger: XCGLogger? {
        return configuration?.logger
    }

    public class func configurationForService(APIBaseURL: String, APIKey: String, SigningKey: String, logger: XCGLogger) {
        configuration = SpendGoServiceConfiguration(apiBaseURL: APIBaseURL, apiKey: APIKey, signingKey: SigningKey, logger: logger)
    }
    
    public class func get(path: String, parameters: SpendGoJSONDictonary? = nil, needsAuthToken: Bool = false, callback: SpendGoServiceCallback) {
        sendRequest(.GET, path: path, parameters: parameters, needsAuthToken: needsAuthToken, callback: callback)
    }
    
    public class func post(path: String, parameters: SpendGoJSONDictonary, needsAuthToken: Bool = false, callback: SpendGoServiceCallback) {
        sendRequest(.POST, path: path, parameters: parameters, needsAuthToken: needsAuthToken, callback: callback)
    }
    
    public class func put(path: String, parameters: SpendGoJSONDictonary, needsAuthToken: Bool = false, callback: SpendGoServiceCallback) {
        sendRequest(.PUT, path: path, parameters: parameters, needsAuthToken: needsAuthToken, callback: callback)
    }

    public class func delete(path: String, parameters: SpendGoJSONDictonary? = nil, needsAuthToken: Bool = false, callback: SpendGoServiceCallback) {
        sendRequest(.DELETE, path: path, parameters: parameters, needsAuthToken: needsAuthToken, callback: callback)
    }

    class func sendRequest(method: Alamofire.Method, path: String, parameters: SpendGoJSONDictonary?, needsAuthToken: Bool = false, callback: SpendGoServiceCallback) {

        // Build OLO url
        let urlString = self.buildSpendGoURL(path)

        // Make Request
        let URL = NSURL(string: urlString)
        var mutableURLRequest = NSMutableURLRequest(URL: URL!)

        // Set HTTP Method
        mutableURLRequest.HTTPMethod = method.rawValue

        // Encode parameters
        let (URLRequest, error): (NSMutableURLRequest, NSError?)

        // Encode as JSON Body if PUT or POST
        if method == .PUT || method == .POST {
            let encoding = Alamofire.ParameterEncoding.JSON
            (URLRequest, error) = encoding.encode(mutableURLRequest, parameters: parameters)
        }
        // Encode as Query String
        else {
            let encoding = Alamofire.ParameterEncoding.URL
            (URLRequest, error) = encoding.encode(mutableURLRequest, parameters: parameters)
        }
        if error != nil {
            callback(response: JSON.null, error: error)
            return
        }
        mutableURLRequest = URLRequest.mutableCopy() as! NSMutableURLRequest

        // Add Authentication Token To Headers if needed
        if needsAuthToken {
            if let authToken = SpendGoSessionService.authToken {
                mutableURLRequest.setValue(authToken, forHTTPHeaderField: "Authorization")
            }
            else {
                callback(response: JSON.null, error: SpendGoUtils.error("User is not authenticated.", code: 0))
                return
            }
        }

        // Calculate Signature
        let urlAndBodyData = URL!.path!.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false)!.mutableCopy() as! NSMutableData
        if mutableURLRequest.HTTPBody?.length > 0 {
            urlAndBodyData.appendData(mutableURLRequest.HTTPBody!)
        }
        let signatureString = calculateSignature(urlAndBodyData)

        // Add headers
        mutableURLRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
        mutableURLRequest.setValue("application/json", forHTTPHeaderField: "Accept")
        mutableURLRequest.setValue(configuration?.apiKey, forHTTPHeaderField: "X-Class-Key")
        mutableURLRequest.setValue(signatureString, forHTTPHeaderField: "X-Class-Signature")

        logger?.info("******** NEW SPENDGO REQUEST ********")
        logger?.info("Url: \(URL?.absoluteString)")
        logger?.verbose("Method: \(method.rawValue)")
        logger?.verbose("Parameters: \(JSON(parameters ?? [:]))")

        // Make request
        let request = Alamofire.request(mutableURLRequest.copy() as! NSURLRequest)
        // Handle Response (support both JSON and Plain Text responses)
        request.responseString(encoding: NSUTF8StringEncoding) { response in
            logger?.info("******** SPENDGO RESPONSE ********")
            guard let httpRequest = response.request else {
                fatalError("Could not get request from server")
            }
            guard let httpResponse = response.response else {
                fatalError("Could not get response from server")
            }

            switch response.result {
            case .Success(let value):
                logger?.info("Status Code: \(httpResponse.statusCode)")
                logger?.verbose("Headers: \(JSON(httpResponse.allHeaderFields))")
                if self.isJSON(httpResponse) {
                    let json = self.parseBody(value ?? "")
                    logger?.verbose("Body: \(json)")
                } else {
                    logger?.verbose("Body: \(value)")
                }
            case .Failure(let error):
                logger?.error("Error: \(error.localizedDescription)")
            }
            self.handleResponse(httpRequest, response: httpResponse, body: response.result.value, error: response.result.error, callback: callback)
        }
    }
    
    private class func handleResponse(request: NSURLRequest, response: NSHTTPURLResponse?, body: String?, error: NSError?, callback: SpendGoServiceCallback) {
        
        // Check for request errors
        if error != nil {
            callback(response: JSON.null, error: error)
            return
        }
        
        // If response is missing or not 200 OK, return error
        if response == nil {
            callback(response: JSON.null, error: SpendGoUtils.error("Received no response from server"))
            return
        }
        
        if response!.statusCode != 200 {
            //Special Handling for 202
            if response!.statusCode == 202 {
                callback(response: JSON.null, error: SpendGoUtils.error("Success. Changes will be appled after validation is complete.", code: 202, domain: SpendGoErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue))
                return
            }
            // If error, attempt to get the message from JSON
            else if isJSON(response!) {
                let json = parseBody(body ?? "")
                let message = json["details"].stringValue
                let code = json["code"].intValue
                //If we have auth error try to check if we have invalid auth callback. Then let the callback handle and dictate the behavior                
                if code == 1010 && SpendGoSessionService.invalidAuthTokenCallback != nil {
                    SpendGoSessionService.invalidAuthTokenCallback!(request: request, res: response!, jsonObject: json, callback: callback)
                }
                else {
                    callback(response: json, error: SpendGoUtils.error(message, code: code, domain: SpendGoErrorDomain.ServiceErrorFromJSONBody.rawValue))
                }
                return
            }
            
            // Attempt to get error message from body
            logger?.warning("Warning: plain/text response received!!")
            callback(response: JSON.null, error: SpendGoUtils.error(body ?? "Unexpected error encountered during the request", code: response!.statusCode, domain: SpendGoErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue))
            return
        }
        
        // 200 OK with JSON body
        if isJSON(response!) {
            callback(response: parseBody(body ?? ""), error: nil)
            return
        }
        
        // 200 OK with plain/text body, attempt converting body to JSON, if not, assume error
        if body != nil && body!.isEmpty == false {
            logger?.warning("Warning: plain/text response received with 200 OK!!!")
            let json = parseBody(body!)
            if json == JSON.null {
                callback(response: JSON.null, error: SpendGoUtils.error(body!))
                return
            }
            callback(response: json, error: nil)
            return
        }
        
        // 200 OK, empty body
        callback(response: JSON.null, error: nil)
    }
    
    private class func isJSON(response: NSHTTPURLResponse) -> Bool {
        if let contentType = response.allHeaderFields["Content-Type"] as? String {
            return contentType.rangeOfString("application/json") != nil
        }
        return false
    }
    
    /// Parse plain text string into SwiftyJSON, return null if string is not JSON
    private class func parseBody(body: String) -> JSON {
        if let dataFromString = body.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false) {
            return JSON(data: dataFromString)
        }
        return JSON.null
    }
    
    private class func buildSpendGoURL(path: String) -> String {
        guard let config = configuration else {
            fatalError("Missing configuration")
        }
        return config.apiBaseURL + path
    }
    
    private class func calculateSignature(data: NSData) -> String {
        guard let config = configuration else {
            fatalError("Missing configuration")
        }
        //Convert Base64 Encoded String
        let keyData = NSData(base64EncodedString: config.signingKey, options: NSDataBase64DecodingOptions(rawValue: 0))
        logger?.verbose("Key-HEX:\(keyData!.toHexString)")
        logger?.verbose("DATA-HEX:\(data.toHexString)")

        //Make array of keys
        var keyArray = [UInt8](count: keyData!.length, repeatedValue: 0)
        keyData!.getBytes(&keyArray, length: keyData!.length)

        //Make array of data
        var dataArray:[UInt8] = [UInt8](count: data.length, repeatedValue: 0)
        data.getBytes(&dataArray, length: data.length)
        guard let hmac = try? Authenticator.HMAC(key: keyArray, variant: .sha256).authenticate(dataArray) else {
            logger?.error("Error generating SpendGo signature")
            fatalError("Error generating SpendGo signature")
        }
        let signatureData = NSData(bytes: hmac, length: hmac.count)
        let signatureString = signatureData.base64EncodedStringWithOptions(NSDataBase64EncodingOptions(rawValue: 0))

        logger?.verbose(signatureString)
        return signatureString
    }
    
    //Make URL safe:
    //Replace + with -
    //signatureString = signatureString.stringByReplacingOccurrencesOfString("+", withString: "-", options: .CaseInsensitiveSearch, range: nil)
    //Replace / with _
    //signatureString = signatureString.stringByReplacingOccurrencesOfString("/", withString: "_", options: .CaseInsensitiveSearch, range: nil)
    //Remove ending padding =
    /*if signatureString[signatureString.endIndex.predecessor()] == "=" {
    signatureString = signatureString.substringToIndex(signatureString.endIndex.predecessor())
    }*/
    
}
