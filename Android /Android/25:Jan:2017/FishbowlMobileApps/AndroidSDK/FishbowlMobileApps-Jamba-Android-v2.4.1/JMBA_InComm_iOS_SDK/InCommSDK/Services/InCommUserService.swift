//
//  InCommUserService.swift
//  InCommSDK
//
//  Created by vThink on 8/20/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import CryptoSwift

public typealias InCommUserAccessTokenCallback = (_ inCommUserAccessToken: InCommUserAccessToken?, _ error: NSError?) -> Void

public typealias InCommUserProfileDetailsCallback = (_ error: NSError?) -> Void

open class InCommUserService{
    
    open class func getUserInCommAuthToken(_ customerId: String!, secretKey: String!) -> String{
        
        var expiresAt: TimeInterval {
            return Date().timeIntervalSince1970 + (7200) // 2 hours(Hours(2)*minutes(60)*seconds(60))
        }
        
        let expires = Int64(expiresAt)
       
        let secretkey = secretKey
       
        let customerid = customerId
      
        var auth_string = "id=\(String(describing: customerid))&expires=\(expires)"
       
        let hashkey = getHashKey(auth_string, secretkey: secretkey!)
    
        auth_string = "\(auth_string)&Signature=\(hashkey)"
        
        auth_string = base64_encode(auth_string)
       
        return auth_string
    }
    

    open class func base64_encode(_ content: String) -> String {
        let data = content.data(using: String.Encoding.utf8)
        return data!.base64EncodedString(options: NSData.Base64EncodingOptions(rawValue: 0))
    }
    
    open class func getHashKey(_ content: String, secretkey: String) -> String {
        
        //Convert Base64 Encoded String
        let keyData = Data(base64Encoded: secretkey, options: NSData.Base64DecodingOptions(rawValue: 0))
        //Make array of keys
        var keyArray = [UInt8](repeating: 0, count: keyData!.count)
        (keyData! as NSData).getBytes(&keyArray, length: keyData!.count)
        
        //Make array of data
        let data = content.data(using: String.Encoding.utf8)
        var dataArray:[UInt8] = [UInt8](repeating: 0, count: data!.count)
        (data! as NSData).getBytes(&dataArray, length: data!.count)
        let hmac: Array<UInt8> = try! HMAC(key: keyArray, variant: .sha256).authenticate(dataArray)
        let signatureData = Data(bytes: UnsafePointer<UInt8>(hmac), count: hmac.count)
        let signatureString = signatureData.base64EncodedString(options: NSData.Base64EncodingOptions(rawValue: 0))
        return signatureString
    }
    
    
    open class func base64_decode(_ content: String) -> String? {
        //        let value = base64PaddingWithEqual(content)
        if let data = Data(base64Encoded: content, options: NSData.Base64DecodingOptions(rawValue: 0)) {
            return String(data: data, encoding: String.Encoding.utf8)//(data: data!, encoding: NSUTF8StringEncoding)!
        }
        return nil
    }
    open class func getInCommUserAccessToken(_ callback:@escaping InCommUserAccessTokenCallback){
        InCommService.post("/Accesstokens", parameters: nil) { (response, error) in
            if error != nil{
                callback(nil, error)
                return
            }
            callback(InCommUserAccessToken(json: response), nil)
            return
        }
    }
    
    open class func updateUserProfileDetails(_ userId:Int32!, userProfileDetails: InCommUserProfileDetails, callback:@escaping InCommUserProfileDetailsCallback){
        let parameters = userProfileDetails.serializeAsJSONDictionary()

        InCommService.put("/Users/\(userId!)", parameters: parameters) {(response, error) in
            if error != nil{
                callback(error)
                return
            }else{
                callback(nil)
                return
            }
        }
    }
}
