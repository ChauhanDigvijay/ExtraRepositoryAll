//
//  OloService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 4/15/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import Alamofire
import SwiftyJSON
import XCGLogger

public typealias OloJSONDictionary = [String : AnyObject]
public typealias OloErrorCallback = (error: NSError?) -> Void

public typealias OloServiceCallback = (response: JSON, error: NSError?) -> Void
typealias OloServiceTransferProgressCallback = (totalBytesTransferred: Int64, totalBytesExpectedToBeTransferred: Int64) -> Void
typealias OloServiceDowloadCallback = (downloadedFileDestionationURL: NSURL?, error: NSError?) -> Void


public class OloService {
    
    static var configuration: OloServiceConfiguration?

    static var logger: XCGLogger? {
        return configuration?.logger
    }

    public class func configurationForService(APIBaseURL: String, APIKey: String, logger: XCGLogger) {
        OloService.configuration = OloServiceConfiguration(apiBaseURL: APIBaseURL, apiKey: APIKey, logger: logger)
    }
    
    class func get(path: String, parameters: OloJSONDictionary? = nil, callback: OloServiceCallback) {
        sendRequest(.GET, path: path, parameters: parameters, callback: callback)
    }
    
    class func post(path: String, parameters: OloJSONDictionary, callback: OloServiceCallback) {
        sendRequest(.POST, path: path, parameters: parameters, callback: callback)
    }
    
    class func put(path: String, parameters: OloJSONDictionary, callback: OloServiceCallback) {
        sendRequest(.PUT, path: path, parameters: parameters, callback: callback)
    }
    
    class func delete(path: String, parameters: OloJSONDictionary? = nil, callback: OloServiceCallback) {
        sendRequest(.DELETE, path: path, parameters: parameters, callback: callback)
    }
    
//    class func download(path: String, downloadFileDestinationURL: NSURL? = nil, progressCallback: OloServiceTransferProgressCallback?, completionCallback: OloServiceDowloadCallback) {
//        //Build URL
//        let url = self.buildOloURL(path)
//        //A variable to store destination file url
//        var filePermanantDestinationURL:NSURL?
//        //Make Request
//        Alamofire.download(.GET, url) { (URL, response) -> (NSURL) in
//            let fileManager = NSFileManager.defaultManager()
//            if downloadFileDestinationURL != nil {
//                filePermanantDestinationURL = downloadFileDestinationURL
//            }
//            else {
//                let directoryURL = fileManager.URLsForDirectory(.DocumentDirectory,inDomains: .UserDomainMask)[0]
//                let pathComponent = response.suggestedFilename
//                filePermanantDestinationURL = directoryURL.URLByAppendingPathComponent(pathComponent!)
//            }
//            do {
//                try fileManager.removeItemAtPath(filePermanantDestinationURL!.path!)
//            }
//            catch {
//                //Do nothing
//            }
//            return filePermanantDestinationURL!
//        }
//        .progress { (bytesRead, totalBytesRead, totalBytesExpectedToRead) -> Void in
//            if progressCallback != nil {
//                progressCallback!(totalBytesTransferred: totalBytesRead, totalBytesExpectedToBeTransferred: totalBytesExpectedToRead)
//            }
//        }
//        .response { (req, res, _, error) -> Void in
//            var jsonDict: NSDictionary? = nil
//            //We need to read json from file
//            if filePermanantDestinationURL != nil && (error != nil || res?.statusCode != 200) {
//                let jsonData = NSData(contentsOfFile: filePermanantDestinationURL!.path!)
//                if jsonData != nil {
//                    do {
//                        jsonDict = try NSJSONSerialization.JSONObjectWithData(jsonData!, options: NSJSONReadingOptions(rawValue: 0)) as? NSDictionary
//                    }
//                    catch {
//                        //Do nothing
//                    }
//                    
//                }
//                //Delete the file, as it has useless data
//                do {
//                    try NSFileManager.defaultManager().removeItemAtPath(filePermanantDestinationURL!.path!)
//                }
//                catch {
//                    //Do Nothing
//                }
//                
//            }
//            // TODO: Are we sure we will always get req?
//            self.responseHandling(req!, res: res, jsonObject: jsonDict, error: error) { (response, error) -> Void in
//                if error != nil {
//                    completionCallback(downloadedFileDestionationURL: nil, error: error)
//                }
//                else {
//                    completionCallback(downloadedFileDestionationURL: filePermanantDestinationURL, error: error)
//                }
//            }
//        }
//    }

    /// REST API request wrapper, depends on Alamofire, returns response as SwiftyJSON
    ///
    /// :param:     method      HTTP method
    /// :param:     path        String contating the path of the url service to hit
    /// :param:     parameters  Dictionary with list of parameters to be sent as JSON payload on POST and PUT requests and query parameters on GET and DELETE requests
    /// :param:     callback    Callback method to handle response and errors
    class private func sendRequest(method: Alamofire.Method, path: String, parameters: OloJSONDictionary?, callback: OloServiceCallback) {
        // Build OLO url
        let url = self.buildOloURL(path)
        //Making encoding JSON. As per Olo doc other encoding's will be dropped in next version.
        var encoding: ParameterEncoding = .URL
        if method == .POST || method == .PUT {
            encoding = .JSON
        }
        logger?.info("******** NEW OLO REQUEST ********")
        logger?.info("Url: \(url)")
        logger?.verbose("Method: \(method.rawValue)")
        logger?.verbose("Parameters: \(JSON(parameters ?? [:]))")
        Alamofire.request(method, url, parameters: parameters, encoding: encoding).responseData { response in
            //Debug
            logger?.info("******** OLO RESPONSE ********")
            logger?.info("Status Code: \(response.response?.statusCode)")
            logger?.verbose("Headers: \(response.response?.allHeaderFields)")
            self.handleResponse(response, callback: callback)
        }
    }

    class private func handleResponse(response: Response<NSData, NSError>, callback: OloServiceCallback) -> Void {
        switch response.result {

        case .Success(let value):
            guard let httpRequest = response.request else {
                callback(response: JSON.null, error: OloUtils.error("Failed to get request from server", code: 0))
                return
            }
            guard let httpResponse = response.response else {
                callback(response: JSON.null, error: OloUtils.error("Failed to get response from server", code: 0))
                return
            }
            guard let jsonObject = try? NSJSONSerialization.JSONObjectWithData(value, options: NSJSONReadingOptions()) else {
                let jsonParsingError = OloUtils.error("Unable to parse JSON", code: httpResponse.statusCode)
                callback(response: JSON.null, error: jsonParsingError)
                return
            }

            let json = JSON(jsonObject)
            logger?.verbose("JSON: \(json)")

            // If res code is not 200 OK
            if httpResponse.statusCode != 200 {
                let message = json["message"].stringValue
                let oloErrorCode = json["num"].intValue
                //If we have auth error try to check if we have invalid auth callback. Then let the callback handle and dictate the behavior.
                if (oloErrorCode == 100 || oloErrorCode == 101) && OloSessionService.invalidAuthTokenCallback != nil {
                    OloSessionService.invalidAuthTokenCallback!(request: httpRequest, res: httpResponse, jsonObject: json, callback: callback)
                }
                else if oloErrorCode >= 200 && oloErrorCode < 300 {
                    // Olo message is presentable to user.
                    callback(response: JSON.null, error: OloUtils.error(message, code: oloErrorCode, domain: OloErrorDomain.ServiceErrorFromJSONBody.rawValue))
                }
                else {
                    //Give out a general message
                    callback(response: JSON.null, error: OloUtils.error("Unexpected error encountered during the request.", code: oloErrorCode, technicalDescription: message, domain: OloErrorDomain.ServiceErrorFromJSONBody.rawValue))
                }
                return
            }

            // Successful request
            callback(response: json, error: nil)


        case .Failure(let error):
            logger?.error("Error: \(error.localizedDescription)")
            callback(response: JSON.null, error: error)
        }
    }
    
    class private func buildOloURL(path: String) -> String {
        guard let configuration = OloService.configuration else {
            fatalError("Service configuration missing")
        }
        var baseURLAndPath = "\(configuration.apiBaseURL)\(path)"
        let queryStringKeyParam = "key=\(configuration.apiKey)"
        if let _ = baseURLAndPath.rangeOfString("?") {
            baseURLAndPath += "&"
        }
        else {
            baseURLAndPath += "?"
        }
        baseURLAndPath += queryStringKeyParam
        return baseURLAndPath
    }
    
}
