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


public typealias InCommJSONDictionary = [String : Any]

public typealias InCommServiceCallback = (_ response: JSON, _ error: NSError?) -> Void

open class InCommService {
    
    static let configuration = InCommServiceConfiguration()
    
    open class func configurationForService(_ baseURL: String, clientId: String, apiKey: String, testMode: Bool = false, printLog: Bool = false) {
        InCommService.configuration.baseURL = baseURL
        InCommService.configuration.clientId = clientId
        InCommService.configuration.apiKey = apiKey
        InCommService.configuration.testMode = testMode
        InCommService.configuration.printLog = printLog
    }
    
    class func get(_ path: String, parameters: InCommJSONDictionary? = nil,callback: @escaping InCommServiceCallback) {
        sendRequest(.get, path: path, parameters: parameters, callback: callback)
    }
    
    class func post(_ path: String, parameters: InCommJSONDictionary?, callback: @escaping InCommServiceCallback) {
        sendRequest(.post, path: path, parameters: parameters, callback: callback)
    }
    
    class func put(_ path: String, parameters: InCommJSONDictionary? = nil, callback: @escaping InCommServiceCallback) {
        sendRequest(.put, path: path, parameters: parameters, callback: callback)
    }
    
    class func delete(_ path: String, parameters: InCommJSONDictionary? = nil, callback: @escaping InCommServiceCallback) {
        sendRequest(.delete, path: path, parameters: parameters, callback: callback)
    }
    
    /// REST API request wrapper, depends on Alamofire, returns response as SwiftyJSON
    class fileprivate func sendRequest(_ method: Alamofire.HTTPMethod, path: String, parameters: InCommJSONDictionary?, callback: @escaping InCommServiceCallback) {
        // Build InComm url
        let urlString = self.buildInCommURL(path)
        
        //Make Request
        let URL = Foundation.URL(string: urlString)
        
        if URL == nil{
            let URLError = InCommUtils.error("Unexpected error encountered during the request.", code: 0, domain: InCommErrorDomain.GeneralError.rawValue)
            callback(JSON.null, URLError)
            return
        }
        
        
        var requestPreparation = URLRequest(url: URL!)
        
        //Set HTTP Method
        requestPreparation.httpMethod = method.rawValue
        
        //Encode parameters
        let UrlRequest:URLRequestConvertible
        //Making encoding JSON.
        var encoding: ParameterEncoding = URLEncoding.default
        if method == .post || method == .put || method == .delete {
            encoding = JSONEncoding.default
        }
        
        do {
           UrlRequest = try encoding.encode(requestPreparation as URLRequestConvertible, with: parameters)
        } catch{
                callback(JSON.null, error as NSError?)
                return
        }
        let mutableURLRequest = UrlRequest  as! NSMutableURLRequest
        
        // Add headers
        mutableURLRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
        mutableURLRequest.setValue("application/json", forHTTPHeaderField: "Accept")
        mutableURLRequest.setValue(configuration.clientId, forHTTPHeaderField: "ClientId")
        mutableURLRequest.setValue("ClientAuth \(configuration.apiKey)", forHTTPHeaderField: "Authorization")
   
        //Make request
        if configuration.printLog {
            print("******** NEW INCOMM REQUEST ********")
            print("Url: \(String(describing: URL?.absoluteString))")
            print("Method: \(method.rawValue)")
            print("Parameters: \(JSON(parameters ?? [:]))")
        }
        let request = Alamofire.request(mutableURLRequest.copy() as! Foundation.URLRequest)
        
        // Handle Response
        // All useful responses are in JSON. But some responses like error of IP Whitelist is in text/html.
        request.responseString(encoding: String.Encoding.utf8) { (response) -> Void in
            var error: NSError? = nil
            if let responseError = response.result.error{
                error = responseError as NSError?
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
                print("Request:\(String(describing: response.request))")
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
    
    class fileprivate func responseHandling(_ request:URLRequest? , res:HTTPURLResponse?, json:JSON?, error:NSError?, callback:InCommServiceCallback) -> Void {
        // Check for request errors
        if error != nil {
            callback(JSON.null, error)
        }
            //If res is nil
        else if res == nil {
            callback(JSON.null, InCommUtils.error("Received no response from server", code: 0))
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
                    callback(JSON.null, error)
                    return
                }
            }
            let error = InCommUtils.error("Unexpected error encountered during the request.", code: res!.statusCode, domain: InCommErrorDomain.ServiceErrorFromHTTPStatusCode.rawValue)
            callback(JSON.null, error)
        }
            // if res code is 200/201
        else if json != nil {
            callback(json!, nil)
        }
        else {
            callback(JSON.null, nil)
        }
    }
    
    fileprivate class func buildInCommURL(_ path: String) -> String {
        return "\(InCommService.configuration.baseURL)\(path)"
    }
    
    fileprivate class func isJSON(_ response: HTTPURLResponse?) -> Bool {
        if let contentType = response?.allHeaderFields["Content-Type"] as? String {
            return contentType.range(of: "application/json") != nil
        }
        return false
    }
    
    /// Parse plain text string into SwiftyJSON, return nil if string is not JSON.
    fileprivate class func parseBody(_ response: HTTPURLResponse?, bodyString: String?) -> JSON? {
        if isJSON(response) {
            if bodyString == nil {
                return JSON.null
            }
            else if let dataFromString = bodyString!.data(using: String.Encoding.utf8, allowLossyConversion: false) {
                return JSON(data: dataFromString)
            }
        }
        return nil
    }
    
}
