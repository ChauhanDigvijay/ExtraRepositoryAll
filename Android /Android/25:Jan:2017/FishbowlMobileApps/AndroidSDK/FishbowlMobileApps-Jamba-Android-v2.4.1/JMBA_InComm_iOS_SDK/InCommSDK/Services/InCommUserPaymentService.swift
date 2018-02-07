//
//  InCommUserPaymentService.swift
//  InCommSDK
//
//  Created by vThink on 8/11/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation

public typealias UserPaymentAccountCallback = (_ userPaymentAccount: InCommUserPaymentAccount?, _ error: NSError?) -> Void
public typealias UserPaymentAccountsCallback = (_ userPaymentAccounts: [InCommUserPaymentAccount], _ error: NSError?) -> Void
public typealias EmptyCallback = (_ error: NSError?) -> Void

open class InCommUserPaymentService{
    
    // MARK: AddPaymentAccount
    open class func addPaymentAccount(_ userId: Int32!, userPaymentAccount:InCommUserPaymentAccountDetails, callback: @escaping UserPaymentAccountCallback){
        let parameters = userPaymentAccount.serializeAsJSONDictionary()
        InCommService.post("/Users/\(userId!)/PaymentAccounts", parameters: parameters){(response, error) -> Void in
            if error != nil{
                callback(nil, error)
                return
            }
            callback(InCommUserPaymentAccount(json:response), error)
        }
    }
    
    // MARK: DeletePaymentAccount
    open class func deletePaymentAccount(_ userId: Int32!, paymentAccountId: Int32!, callback: @escaping EmptyCallback){
        InCommService.delete("/Users/\(userId!)/PaymentAccounts/\(paymentAccountId!)"){(response, error) -> Void in
            if error != nil{
                callback(error)
                return
            }
            callback(nil)
        }
    }
    
    // MARK: GetPaymentAccount
    open class func getPaymentAccountDetails(_ userId: Int32!, paymentAccountId: Int32!, callback: @escaping UserPaymentAccountCallback){
        InCommService.get("/Users/\(userId!)/PaymentAccounts/\(paymentAccountId!)"){(response, error) -> Void in
            if error != nil{
                callback(nil, error)
                return
            }
            callback(InCommUserPaymentAccount(json: response),nil)
        }
    }
    
    // MARK: GetPaymentAccounts
    open class func getPaymentAccountsDetails(_ userId: Int32!, callback: @escaping UserPaymentAccountsCallback){
        InCommService.get("/Users/\(userId!)/PaymentAccounts"){(response, error) -> Void in
            if error != nil{
                callback([], error)
                return
            }
            callback(response.arrayValue.map{InCommUserPaymentAccount(json: $0)}, error)
        }
    }
}
