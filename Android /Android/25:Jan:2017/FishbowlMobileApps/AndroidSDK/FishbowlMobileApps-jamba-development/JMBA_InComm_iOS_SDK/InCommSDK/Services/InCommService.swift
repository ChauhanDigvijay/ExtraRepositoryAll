//
//  InCommService.swift
//  InCommSDK
//
//  Created by Taha Samad on 8/6/15.
//  Copyright (c) 2015 Fishbowl. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON


public typealias InCommJSONDictionary = [String : AnyObject]

public typealias InCommServiceCallback = (response: JSON, error: NSError?) -> Void

public class InCommService {
    
    static let configuration = InCommServiceConfiguration()
    
    public class func configurationForService(baseURL: String, clientId: String, apiKey: String, testMode: Bool = false, printLog: Bool = false) {
        InCommService.configuration.baseURL = baseURL
        InCommService.configuration.clientId = clientId
        InCommService.configuration.apiKey = apiKey
        InCommService.configuration.testMode = testMode
        InCommService.configuration.printLog = printLog
    }
    
    class func get(path: String, parameters: InCommJSONDictionary? = nil,callback: InCommServiceCallback) {
        sendRequest(.GET, path: path, parameters: parameters, callback: callback)
    }
    
    class func post(path: String, parameters: InCommJSONDictionary?, callback: InCommServiceCallback) {
        sendRequest(.POST, path: path, parameters: parameters, callback: callback)
    }
    
    class func put(path: String, parameters: InCommJSONDictionary? = nil, callback: InCommServiceCallback) {
        sendRequest(.PUT, path: path, parameters: parameters, callback: callback)
    }
    
    class func delete(path: String, parameters: InCommJSONDictionary? = nil, callback: InCommServiceCallback) {
        sendRequest(.DELETE, path: path, parameters: parameters, callback: callback)
    }
    
    /// REST API request wrapper, depends on Alamofire, returns response as SwiftyJSON
    class private func sendRequest(method: Alamofire.Method, path: String, parameters: InCommJSONDictionary?, callback: InCommServiceCallback) {
        // Build InComm url
        let urlString = self.buildInCommURL(path)
        
        //Make Request
        let URL = NSURL(string: urlString)
        
        if URL == nil{
            let URLError = InCommUtils.error("Unexpected error encountered during the request.", code: 0, domain: InCommErrorDomain.GeneralError.rawValue)
            callback(response: JSON.null, error: URLError)
            return
        }
        
        
        var mutableURLRequest = NSMutableURLRequest(URL: URL!)
        
        //Set HTTP Method
        mutableURLRequest.HTTPMethod = method.rawValue
        
        //Encode parameters
        let (URLRequest, error): (NSMutableURLRequest, NSError?)
        //Making encoding JSON.
        var encoding: ParameterEncoding = .URL
        if method == .POST || method == .PUT || method == .DELETE {
            encoding = .JSON
        }
        (URLRequest, error) = encoding.encode(mutableURLRequest, parameters: parameters)
        if error != nil {
            callback(response: JSON.null, error: error)
            return
        }
        mutableURLRequest = URLRequest.mutableCopy() as! NSMutableURLRequest
        
        // Add headers
        mutableURLRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
        mutableURLRequest.setValue("application/json", forHTTPHeaderField: "Accept")
        mutableURLRequest.setValue(configuration.clientId, forHTTPHeaderField: "ClientId")
        mutableURLRequest.setValue("ClientAuth \(configuration.apiKey)", forHTTPHeaderField: "Authorization")
   
        //Make request
        if configuration.printLog {
            print("******** NEW INCOMM REQUEST ********")
            print("Url: \(URL?.absoluteString)")
            print("Method: \(method.rawValue)")
            print("Parameters: \(JSON(parameters ?? [:]))")
        }
        let request = Alamofire.request(mutableURLRequest.copy() as! NSURLRequest)
        
        // Handle Response
        // All useful responses are in JSON. But some responses like error of IP Whitelist is in text/html.
        request.responseString(encoding: NSUTF8StringEncoding) { (response) -> Void in
            var error: NSError? = nil
            if let responseError = response.result.error{
                error = responseError
            }
            
            
            let bodyString = response.result.value
            //Parse the body string
            let json = self.parseBody(response.response, bodyString: bodyString)
            var toBePassed = error
            //If json is nil and we do not have an error. Then make an error from body string.
            if response.result.isFailure && json == nil && error == nil {
                toBePassed = InCommUtils.error("Received response in unexpected format.", code: 0, technicalDescription: bodyString ?? "")
            }
            //Logging Code
            if configuration.printLog {
                print(" INCOMM RESPONSE ")
                print("Request:\(response.request)")
            }
            if response.response != nil {
                if configuration.printLog {
                    print("Status Code: \(response.response!.statusCode)")
                    print("Headers: \(JSON(response.response!.allHeaderFields))")
                }
            }
            if json == nil && bodyString != nil {
                if configuration.printLog {
                    print("Body: \(bodyString!)")
                }
            }
            else if json != nil {
                if configuration.printLog {
                    print("JSON Body: \(json!)")
                }
            }
            if error != nil {
                if configuration.printLog {
                    print("Error: \(error!.localizedDescription)")
                }
            }
            self.responseHandling(response.request, res: response.response, json: json, error: toBePassed, callback: callback)
        }
    }
    
    class private func responseHandling(request:NSURLRequest? , res:NSHTTPURLResponse?, json:JSON?, error:NSError?, callback:InCommServiceCallback) -> Void {
        // Check for request errors
        if error != nil {
            callback(response: JSON.null, error: error)
        }
            //If res is nil
        else if res == nil {
            callback(response: JSON.null, error: InCommUtils.error("Received no response from server", code: 0))
        }
            //If res code is not 200/201
        else if res!.statusCode != 200 && res!.statusCode != 201 {
            //Check if we got any JSON response
            if json != nil {
                let resultCode = json!["Code"].int
                if  resultCode != nil {
                    var errorDescription = ""
                    let resultMessage = json!["Message"].string
                    let validationErrors:InCommValidationErrors = InCommValidationErrors(json: json!["ValidationErrors"])
                    
                    if validationErrors.creditCardNumber.count != 0{
                        for errorMessage in validationErrors.creditCardNumber{
                            errorDescription = errorDescription + " \n " + errorMessage
                        }
                    }
                    if validationErrors.orderPaymentAmount.count != 0{
                        for errorMessage in validationErrors.orderPaymentAmount{
                            errorDescription = errorDescription + " \n " + errorMessage
                        }
                    }
                
                    if validationErrors.streetAddress1.count != 0{
                        for errorMessage in validationErrors.streetAddress1{
                            errorDescription = errorDescription + " \n " + errorMessage
                        }
                    }
                    
                    if validationErrors.stateProvinceCode.count != 0{
                        for errorMessage in validationErrors.stateProvinceCode{
                            errorDescription = errorDescription + " \n " + errorMessage
                        }
                    }
                    if validationErrors.creditCardExpirationMonth.count != 0 || validationErrors.creditCardExpirationYear.count != 0{
                        var creditCardExpirationMonth = ""
                        var creditCardExpirationYear  = ""
                        var finalErrorMessage = ""
                        for errorMessage in validationErrors.creditCardExpirationMonth{
                            creditCardExpirationMonth = errorMessage
                        }
                        for errorMessage in validationErrors.creditCardExpirationYear{
                            creditCardExpirationYear = errorMessage
                        }
                        
                        if creditCardExpirationMonth != "" && creditCardExpirationYear != ""{
                            finalErrorMessage = creditCardExpirationMonth
                        }
                        else if creditCardExpirationMonth != ""{
                            finalErrorMessage = creditCardExpirationMonth
                        }
                        else{
                            finalErrorMessage = creditCardExpirationYear
                        }
                        
                       errorDescription = errorDescription + " \n " + finalErrorMessage
                    }
                   
                    
                    if errorDescription.isEmpty{
                        if resultMessage != nil{
                            errorDescription = resultMessage!
                        }
                        else{
                         errorDescription = "Unexpected error encountered during the request."
                        }
                    }
                
                    let error = InCommUtils.error(errorDescription, code: resultCode!, domain: InCommErrorDomain.ServiceErrorFromJSONBody.rawValue)
                    callback(response: JSON.null, error: error)
                    return
                }
            }
            let error = InCommUtils.error("Unexpected error encountered during the request.", code: res!.statusCode, domain: InCommErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue)
            callback(response: JSON.null, error: error)
        }
            // if res code is 200/201
        else if json != nil {
            callback(response: json!, error: nil)
        }
        else {
            callback(response: JSON.null, error: nil)
        }
    }
    
    private class func buildInCommURL(path: String) -> String {
        return "\(InCommService.configuration.baseURL)\(path)"
    }
    
    private class func isJSON(response: NSHTTPURLResponse?) -> Bool {
        if let contentType = response?.allHeaderFields["Content-Type"] as? String {
            return contentType.rangeOfString("application/json") != nil
        }
        return false
    }
    
    /// Parse plain text string into SwiftyJSON, return nil if string is not JSON.
    private class func parseBody(response: NSHTTPURLResponse?, bodyString: String?) -> JSON? {
        if isJSON(response) {
            if bodyString == nil {
                return JSON.null
            }
            else if let dataFromString = bodyString!.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false) {
                return JSON(data: dataFromString)
            }
        }
        return nil
    }
    
}
