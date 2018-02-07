//
//  InCommUserService.swift
//  InCommSDK
//
//  Created by vThink on 8/20/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import CryptoSwift

public typealias InCommUserAccessTokenCallback = (inCommUserAccessToken: InCommUserAccessToken?, error: NSError?) -> Void

public typealias InCommUserProfileDetailsCallback = (error: NSError?) -> Void

public class InCommUserService{
    
    public class func getUserInCommAuthToken(customerId: String!, secretKey: String!) -> String{
        
        var expiresAt: NSTimeInterval {
            return NSDate().timeIntervalSince1970 + (7200) // 2 hours(Hours(2)*minutes(60)*seconds(60))
        }
        
        let expires = Int64(expiresAt)
       
        let secretkey = secretKey
       
        let customerid = customerId
      
        var auth_string = "id=\(customerid)&expires=\(expires)"
       
        let hashkey = getHashKey(auth_string, secretkey: secretkey)
    
        auth_string = "\(auth_string)&Signature=\(hashkey)"
        
        auth_string = base64_encode(auth_string)
       
        return auth_string
    }
    

    public class func base64_encode(content: String) -> String {
        let data = content.dataUsingEncoding(NSUTF8StringEncoding)
        return data!.base64EncodedStringWithOptions(NSDataBase64EncodingOptions(rawValue: 0))
    }
    
    public class func getHashKey(content: String, secretkey: String) -> String {
        
        //Convert Base64 Encoded String
        let keyData = NSData(base64EncodedString: secretkey, options: NSDataBase64DecodingOptions(rawValue: 0))
        //Make array of keys
        var keyArray = [UInt8](count: keyData!.length, repeatedValue: 0)
        keyData!.getBytes(&keyArray, length: keyData!.length)
        
        //Make array of data
        let data = content.dataUsingEncoding(NSUTF8StringEncoding)
        var dataArray:[UInt8] = [UInt8](count: data!.length, repeatedValue: 0)
        data!.getBytes(&dataArray, length: data!.length)
        let hmac: Array<UInt8> = try! Authenticator.HMAC(key: keyArray, variant: .sha256).authenticate(dataArray)
        let signatureData = NSData(bytes: hmac, length: hmac.count)
        let signatureString = signatureData.base64EncodedStringWithOptions(NSDataBase64EncodingOptions(rawValue: 0))
        return signatureString
    }
    
    
    public class func base64_decode(content: String) -> String? {
        //        let value = base64PaddingWithEqual(content)
        if let data = NSData(base64EncodedString: content, options: NSDataBase64DecodingOptions(rawValue: 0)) {
            return String(data: data, encoding: NSUTF8StringEncoding)//(data: data!, encoding: NSUTF8StringEncoding)!
        }
        return nil
    }
    public class func getInCommUserAccessToken(callback:InCommUserAccessTokenCallback){
        InCommService.post("/Accesstokens", parameters: nil) { (response, error) in
            if error != nil{
                callback(inCommUserAccessToken: nil, error: error)
                return
            }
            callback(inCommUserAccessToken: InCommUserAccessToken(json: response), error: nil)
            return
        }
    }
    
    public class func updateUserProfileDetails(userId:Int32, userProfileDetails: InCommUserProfileDetails, callback:InCommUserProfileDetailsCallback){
        let parameters = userProfileDetails.serializeAsJSONDictionary()

        InCommService.put("/Users/\(userId)", parameters: parameters) {(response, error) in
            if error != nil{
                callback(error: error)
                return
            }else{
                callback(error: nil)
                return
            }
        }
    }
}