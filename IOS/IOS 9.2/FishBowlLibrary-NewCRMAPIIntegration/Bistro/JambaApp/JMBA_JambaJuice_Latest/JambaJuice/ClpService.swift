//
//  ClpService.swift
//  JambaJuice
//
//  Created by Joe Jayabalan on 2/18/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON
import CryptoSwift
import XCGLogger

public typealias ClpJSONDictionary = [String : AnyObject]
public typealias ClpErrorCallback = (error: NSError?) -> Void

public typealias ClpServiceCallback = (response: JSON, error: NSError?) -> Void
typealias ClpServiceTransferProgressCallback = (totalBytesTransferred: Int64, totalBytesExpectedToBeTransferred: Int64) -> Void
typealias ClpServiceDownloadCallback = (downloadedFileDestionationURL: NSURL?, error: NSError?) -> Void

public class ClpService {
    static var configuration: ClpServiceConfiguration?
    
    static var logger: XCGLogger? {
        return configuration?.logger
    }
    
    public class func configurationForService(APIBaseURL: String, APIKey: String, logger: XCGLogger) {
        ClpService.configuration = ClpServiceConfiguration(apiBaseURL: APIBaseURL, apiKey: APIKey, logger: logger)
    }
    
    class func get(path: String, parameters: ClpJSONDictionary? = nil, callback: ClpServiceCallback) {
        sendRequest(.GET, path: path, parameters: parameters, callback: callback)
    }
     
    class func post(path: String, parameters: ClpJSONDictionary, callback: ClpServiceCallback) {
        sendRequest(.POST, path: path, parameters: parameters, callback: callback)
    }
    
    class func put(path: String, parameters: ClpJSONDictionary, callback: ClpServiceCallback) {
        sendRequest(.PUT, path: path, parameters: parameters, callback: callback)
    }
    
    class func delete(path: String, parameters: ClpJSONDictionary? = nil, callback: ClpServiceCallback) {
        sendRequest(.DELETE, path: path, parameters: parameters, callback: callback)
    }

    class private func sendRequest(method: Alamofire.Method, path: String, parameters: ClpJSONDictionary?, callback: ClpServiceCallback) {
        // Build OLO url
        let url = path
        //let url = "http://jamba.clpqa.com/clpapi/mobile/getofferforcustomer/7143548/3"
        //Making encoding JSON. As per Olo doc other encoding's will be dropped in next version.
        var encoding: ParameterEncoding = .URL
        if method == .POST || method == .PUT {
            encoding = .JSON
        }
         logger?.info("Url: \(url)")
        logger?.verbose("Method: \(method.rawValue)")
        logger?.verbose("Parameters: \(JSON(parameters ?? [:]))")
        
        if  let strToken  = NSUserDefaults.standardUserDefaults().stringForKey("access_token")
        {
            let clientID : String = (clpAnalyticsService.sharedInstance.clpsdkobj?.client_ID)!;
            
            
            if let deviceId : String = UIDevice.currentDevice().identifierForVendor!.UUIDString
            {
                print("already there")
            
            
            let headers = ["Content-Type": "application/json","Application":"mobilesdk","tenantid":"1173","access_token":strToken,"client_id":clientID,"tenantName":"fishbowl","client_secret":"C65A0DC0F28C469FB7376F972DEFBCB8","deviceId":String(deviceId)];
            
            
            Alamofire.request(method, url, headers: headers, encoding: encoding).responseData { response in
                //Debug
                logger?.info("******** CLP RESPONSE ********")
                logger?.info("Status Code: \(response.response?.statusCode)")
                logger?.verbose("Headers: \(response.response?.allHeaderFields)")
                self.handleResponse(response, callback: callback)
            }
                
            }

        }
    }
    class private func handleResponse(response: Response<NSData, NSError>, callback: ClpServiceCallback) -> Void {
        
        // security implementation code enabled        
        if response.response?.statusCode == 401
        {
            let jsonobject : NSDictionary = ["statuscode" : (response.response?.statusCode)!]
             let json = JSON(jsonobject)
            
            callback(response: json, error: nil)
            return

        }
        
        switch response.result {
            
        case .Success(let value):
        guard response.request != nil else {
            logger?.info("Failed to get request from server")
            return
        }
        guard response.response != nil else {
            logger?.info("Failed to get response from server")
            return
        }
        guard let jsonObject : NSDictionary = try? NSJSONSerialization.JSONObjectWithData(value, options: NSJSONReadingOptions.MutableContainers) as! NSDictionary else {
            logger?.info("Unable to parse JSON")
            callback(response: JSON.null, error: nil)
            return
        }

        let json = JSON(jsonObject)
            
        callback(response: json, error: nil)

            //        if self.isJSON(httpResponse) {
            //            let datastring = NSString(data:value, encoding:NSUTF8StringEncoding) as! String
            //            let json = self.parseBody(datastring)
            //            logger?.verbose("Body: \(json)")
            //            callback(response: json, error: nil)
            //        } else {
            //            logger?.verbose("Body: \(value)")
            //        }
            
        case .Failure(let error):
            logger?.error("Error: \(error.localizedDescription)")
            
            clpAnalyticsService.sharedInstance.clpTrackScreenError("AppError")

            
            callback(response: JSON.null, error: error)
            
        }
    }
    
    /// Parse plain text string into SwiftyJSON, return null if string is not JSON
    private class func parseBody(body: String) -> JSON {
        if let dataFromString = body.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false) {
            return JSON(data: dataFromString)
        }
        return JSON.null
    }
    
    private class func isJSON(response: NSHTTPURLResponse) -> Bool {
        if let contentType = response.allHeaderFields["Content-Type"] as? String {
            return contentType.rangeOfString("application/json") != nil
        }
        return false
    }
    
//    class private func handleResponse(response: Response<NSData, NSError>, callback: ClpServiceCallback) -> Void {
//        switch response.result {
//            
//        case .Success(let value):
//            guard let httpRequest = response.request else {
//                callback(response: JSON.null, error: OloUtils.error("Failed to get request from server", code: 0))
//                return
//            }
//            guard let httpResponse = response.response else {
//                callback(response: JSON.null, error: OloUtils.error("Failed to get response from server", code: 0))
//                return
//            }
//            guard let jsonObject = try? NSJSONSerialization.JSONObjectWithData(value, options: NSJSONReadingOptions()) else {
//                let jsonParsingError = OloUtils.error("Unable to parse JSON", code: httpResponse.statusCode)
//                callback(response: JSON.null, error: jsonParsingError)
//                return
//            }
//            
//            let json = JSON(jsonObject)
//            logger?.verbose("JSON: \(json)")
//            
//            // If res code is not 200 OK
//            if httpResponse.statusCode != 200 {
//                let message = json["message"].stringValue
//                let oloErrorCode = json["num"].intValue
//                //If we have auth error try to check if we have invalid auth callback. Then let the callback handle and dictate the behavior.
//                if (oloErrorCode == 100 || oloErrorCode == 101) && ClpSessionService.invalidAuthTokenCallback != nil {
//                    ClpSessionService.invalidAuthTokenCallback!(request: httpRequest, res: httpResponse, jsonObject: json, callback: callback)
//                }
//                else if oloErrorCode >= 200 && oloErrorCode < 300 {
//                    // Olo message is presentable to user.
//                    callback(response: JSON.null, error: OloUtils.error(message, code: oloErrorCode, domain: OloErrorDomain.ServiceErrorFromJSONBody.rawValue))
//                }
//                else {
//                    //Give out a general message
//                    callback(response: JSON.null, error: OloUtils.error("Unexpected error encountered during the request.", code: oloErrorCode, technicalDescription: message, domain: OloErrorDomain.ServiceErrorFromJSONBody.rawValue))
//                }
//                return
//            }
//            
//            // Successful request
//            callback(response: json, error: nil)
//            
//            
//        case .Failure(let error):
//            logger?.error("Error: \(error.localizedDescription)")
//            callback(response: JSON.null, error: error)
//        }
//    }
    
//    class private func buildOloURL(path: String) -> String {
//        guard let configuration = ClpService.configuration else {
//            fatalError("Clp Service configuration missing")
//        }
//        var baseURLAndPath = "\(configuration.apiBaseURL)\(path)"
//        let queryStringKeyParam = "key=\(configuration.apiKey)"
//        if let _ = baseURLAndPath.rangeOfString("?") {
//            baseURLAndPath += "&"
//        }
//        else {
//            baseURLAndPath += "?"
//        }
//        baseURLAndPath += queryStringKeyParam
//        return baseURLAndPath
//    }
    
    class private func buildClpURL(path: String) -> String {
        let baseURLAndPath = "\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getofferforcustomer/7143548/3"
        return baseURLAndPath
    }
}
