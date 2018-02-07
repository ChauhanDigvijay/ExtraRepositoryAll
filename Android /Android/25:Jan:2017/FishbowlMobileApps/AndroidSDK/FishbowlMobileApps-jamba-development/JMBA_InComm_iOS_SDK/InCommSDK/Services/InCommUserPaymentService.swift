//
//  InCommUserPaymentService.swift
//  InCommSDK
//
//  Created by vThink on 8/11/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation

public typealias UserPaymentAccountCallback = (userPaymentAccount: InCommUserPaymentAccount?, error: NSError?) -> Void
public typealias UserPaymentAccountsCallback = (userPaymentAccounts: [InCommUserPaymentAccount], error: NSError?) -> Void
public typealias EmptyCallback = (error: NSError?) -> Void

public class InCommUserPaymentService{
    
    // MARK: AddPaymentAccount
    public class func addPaymentAccount(userId: Int32!, userPaymentAccount:InCommUserPaymentAccountDetails, callback: UserPaymentAccountCallback){
        let parameters = userPaymentAccount.serializeAsJSONDictionary()
        InCommService.post("/Users/\(userId)/PaymentAccounts", parameters: parameters){(response, error) -> Void in
            if error != nil{
                callback(userPaymentAccount: nil, error: error)
                return
            }
            callback(userPaymentAccount: InCommUserPaymentAccount(json:response), error: error)
        }
    }
    
    // MARK: DeletePaymentAccount
    public class func deletePaymentAccount(userId: Int32!, paymentAccountId: Int32!, callback: EmptyCallback){
        InCommService.delete("/Users/\(userId)/PaymentAccounts/\(paymentAccountId)"){(response, error) -> Void in
            if error != nil{
                callback(error: error)
                return
            }
            callback(error: nil)
        }
    }
    
    // MARK: GetPaymentAccount
    public class func getPaymentAccountDetails(userId: Int32!, paymentAccountId: Int32!, callback: UserPaymentAccountCallback){
        InCommService.get("/Users/\(userId)/PaymentAccounts/\(paymentAccountId)"){(response, error) -> Void in
            if error != nil{
                callback(userPaymentAccount: nil, error: error)
                return
            }
            callback(userPaymentAccount: InCommUserPaymentAccount(json: response),error: nil)
        }
    }
    
    // MARK: GetPaymentAccounts
    public class func getPaymentAccountsDetails(userId: Int32!, callback: UserPaymentAccountsCallback){
        InCommService.get("/Users/\(userId)/PaymentAccounts"){(response, error) -> Void in
            if error != nil{
                callback(userPaymentAccounts: [], error: error)
                return
            }
            callback(userPaymentAccounts: response.arrayValue.map{InCommUserPaymentAccount(json: $0)}, error: error)
        }
    }
}