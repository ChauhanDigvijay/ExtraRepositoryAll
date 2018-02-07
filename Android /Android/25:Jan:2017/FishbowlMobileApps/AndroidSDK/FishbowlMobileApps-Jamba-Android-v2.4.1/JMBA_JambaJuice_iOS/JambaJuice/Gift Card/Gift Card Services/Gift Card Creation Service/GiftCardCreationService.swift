
//
//  GiftCardCreationService.swift
//  JambaGiftCard
//
//  Created by vThink on 6/6/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import InCommSDK

public typealias InCommJSONDictionary = [String : Any]
typealias IncommBrandServiceCallBack =  (_ brand:InCommBrand?,_ error:NSError?) -> Void
typealias IncommSubmitOrderCallBack = (_ order:InCommOrder?,_ error:NSError?) -> Void
typealias IncommPaymentDetailCallBack = (_ payment:InCommCard?,_ error:NSError?) -> Void
typealias IncommUserGiftCardsCountCallBack = (_ userGiftCards: [InCommUserGiftCard]) -> Void
typealias IncommUserGiftCardsCallBack = (_ userGiftCards: [InCommUserGiftCard], _ error: NSError?) -> Void
typealias InCommAutoReloadDetailsCallBack = (_ inCommAutoReloadSavable: InCommAutoReloadSavable?, _ error: NSError?) -> Void
typealias InCommAssociateGiftCardToUserCallBack = (_ inCommUserGiftCard: InCommUserGiftCard?, _ error: NSError?) -> Void
typealias InCommAssociatePaymentAccountToUserCallBack = (_ inCommUserPaymentAccount: InCommUserPaymentAccount?, _ error: NSError?) -> Void
typealias InCommAutoReloadGiftCardsForPaymentCallBack = (_ giftCardsDetailsWithAutoReloadDetails:[CardAutoReloadDetails], _ error:NSError?) -> Void
typealias InCommDeleteCallBack = (_ error: NSError?) -> Void
typealias InCommAutoReloadAndCreditCardDeleteCallBack = (_ giftCardsDetailsWithAutoReloadDetails:[CardAutoReloadDetails], _ error:NSError?) -> Void
typealias InCommTransactionHistoryCallBack = (_ inCommTransactionHistory: InCommGiftCardTransactionHistory?, _ error:NSError?) -> Void


typealias InCommVestaWebSession = (vestaOrgId: String, vestaWebSessionId: String)
typealias InCommVestaWebSessionCallback = (_ vestaWebSession: InCommVestaWebSession?, _ error: NSError?) -> Void
typealias IncommUserGiftCardCallback = (_ userGiftCard: InCommUserGiftCard?, _ error: NSError?) -> Void
typealias InCommCardCallback = (_ card: InCommCard?, _ error: NSError?) -> Void
typealias InCommGiftCardBalanceCallback = (_ giftCardBalance: InCommGiftCardBalance?, _ error: NSError?) -> Void

// InComm Order Id Callback
typealias InCommOrderIdCallback = (_ inCommOrderId: String?, _ error: NSError?) -> Void



class GiftCardCreationService{
    
    static let sharedInstance = GiftCardCreationService()
    
    fileprivate(set) var brand:InCommBrand!
    fileprivate(set) var incommBrandImageCode:String!
    fileprivate(set) var cardAmount:String!
    fileprivate(set) var cardQuantity:UInt32!
    fileprivate(set) var paymentDetails:InCommSubmitPayment?
    fileprivate(set) var paymentDetailsId:InCommSubmitPaymentWithId?
    fileprivate(set) var existingPaymentDetails:InCommUserPaymentAccount?
    fileprivate(set) var recipientEmail:String!
    fileprivate(set) var recipientMessage:String!
    fileprivate(set) var recipientSelf:Bool!
    fileprivate(set) var purchaserInformationFromPayment:Bool!
    fileprivate(set) var purchaserDetails:InCommOrderPurchaser!
    fileprivate(set) var savePaymentDetails:InCommUserPaymentAccountDetails!
    fileprivate(set) var recipientDetails:InCommOrderRecipientDetails!
    fileprivate(set) var inCommUserGiftCards:[InCommUserGiftCard]?
    
    fileprivate(set) var inCommAuthToken:String?
    
    // private var autoReloadGiftCards:[InCommUserGiftCard] = []
    fileprivate var giftCardsDetailsWithAutoReloadDetails:[CardAutoReloadDetails] = []
    
    func setIncommBrandImageCodeAndAmount(_ imageCode:String?,amount:String?){
        self.incommBrandImageCode = imageCode
        self.cardAmount = amount
    }
    
    func setIncommBrandImageCode(_ imageCode:String?){
        self.incommBrandImageCode = imageCode
    }
    
    // Billing detail payment
    func setPaymentDetails(_ paymentDetails:InCommSubmitPayment){
        self.paymentDetails = paymentDetails
    }
    
    //set payment id for user associated cards
    func setPaymentDetailsId(_ paymentDetailsId:InCommSubmitPaymentWithId){
        self.paymentDetailsId = paymentDetailsId
    }
    
    func updatePaymentDetailsAmount(_ amount:Double){
        if (self.paymentDetails != nil) {
            self.paymentDetails!.amount = amount
        } else if (self.paymentDetailsId != nil) {
            self.paymentDetailsId!.amount = amount
        }
    }
    
    //set individual card amount
    func setCardAmout(_ amount:String) {
        self.cardAmount = amount
    }
    
    //set payment details for user associated cards
    func setexistingPaymentDetails(_ existingPaymentDetails:InCommUserPaymentAccount){
        self.existingPaymentDetails = existingPaymentDetails
    }
    
    func setCardQuantity(_ Quantity:Int){
        self.cardQuantity = UInt32(Quantity)
    }
    
    func setPurchaserDetails(_ purchaserDetails:InCommOrderPurchaser){
        self.purchaserDetails = purchaserDetails
    }
    
    func setReceipientDetails(_ recipientDetails:InCommOrderRecipientDetails){
        self.recipientDetails = recipientDetails
    }
    
    func setReceipientMessage(_ recipientMessage:String){
        self.recipientMessage = recipientMessage
    }
    
    //set the value for purchaser information get from payment
    func setPurchaserInformationFromPayment(_ purchaserInformationFromPayment:Bool){
        self.purchaserInformationFromPayment = purchaserInformationFromPayment
    }
    
    func setReceipientSelf(_ recipientSelf:Bool){
        self.recipientSelf = recipientSelf
    }
    
    //reset all shared instance values
    func resetSharedInstanceValues() {
        self.incommBrandImageCode = nil
        self.cardAmount = nil
        self.cardQuantity = nil
        self.paymentDetails = nil
        self.paymentDetailsId = nil
        self.existingPaymentDetails = nil
        self.recipientEmail = nil
        self.recipientMessage = nil
        self.recipientSelf = nil
        self.purchaserDetails = nil
        self.savePaymentDetails = nil
        self.recipientDetails = nil
    }
    
    //reset existing payment details
    func resetPaymentDetailsId() {
        self.paymentDetailsId = nil
    }
    
    //reset payment details & associate to user details
    func resetPaymentDetails() {
        self.paymentDetails = nil
        self.savePaymentDetails = nil
    }
    
    //For saving the payment Details to associate the user
    func setSavePaymentDetails(_ savePaymentDetails:InCommUserPaymentAccountDetails){
        self.savePaymentDetails = savePaymentDetails
    }
    
    func associateGiftCardToUser(_ cardToken:String?, callback:@escaping InCommAssociateGiftCardToUserCallBack){
        
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        
        if cardToken == nil{
            return callback(nil, GiftCardAppConstants.generalError)
        }
        
        if userId == nil{
            return callback(nil, GiftCardAppConstants.generalError)
        }
        
        InCommUserGiftCardService.associateGiftCard(userId, cardToken: cardToken) { (userGiftCard, error) in
            if error != nil{
                if error?.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.associateGiftCard(userId, cardToken: cardToken){ (userGiftCard, error) in
                                if error != nil{
                                    return callback(nil,error)
                                }
                                else{
                                    return callback(userGiftCard, nil)
                                }
                            }
                        }
                        else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil, error)
                }
            }
            else{
                return callback(userGiftCard, nil)
            }
        }
    }
    
    func associatePaymentDetailsToUser(_ paymentDetails: InCommUserPaymentAccountDetails!, callback: @escaping InCommAssociatePaymentAccountToUserCallBack){
        
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        
        if userId == nil{
            return callback(nil, GiftCardAppConstants.generalError)
        }
        
        InCommUserPaymentService.addPaymentAccount(userId, userPaymentAccount: paymentDetails) { (userPaymentAccount, error) in
            if error != nil{
                if error?.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserPaymentService.addPaymentAccount(userId, userPaymentAccount: paymentDetails) { (userPaymentAccount, error) in
                                if error != nil{
                                    return callback(nil, error)
                                }
                                else{
                                    return callback(userPaymentAccount, nil)
                                }
                            }
                        }
                        else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil, error)
                }
            }
            else{
                return callback(userPaymentAccount, nil)
            }
        }
    }
    
    
    
    func createOrSendGiftCard(_ inCommSubmitOrder:InCommSubmitOrder!, callback: @escaping IncommSubmitOrderCallBack){
        InCommOrdersService.submitOrder(inCommSubmitOrder) { (order, error) in
            if(error != nil){
                if (error?.code == GiftCardAppConstants.errorCodeInvalidUser) {
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommOrdersService.submitOrder(inCommSubmitOrder) { (order, error) in
                                if(error != nil){
                                    return callback(nil,error)
                                }
                                else{
                                    return callback(order,nil)
                                }
                            }
                            
                        }
                        else{
                            return callback(nil,error)
                            
                        }
                    })
                }
                else{
                    return callback(nil,error)
                }
            }
            else{
                return callback(order,nil)
            }
        }
    }
    
    
    //MARK: Associate gift card by Number and Pin
    func associateGiftCardToUser(_ cardNumber:String!,cardPin: String!, callback:@escaping InCommAssociateGiftCardToUserCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        let brandId = InCommGiftCardBrandDetails.sharedInstance.brandID
        
        if userId == nil{
            return callback(nil, GiftCardAppConstants.generalError)
        }
        
        InCommUserGiftCardService.associateGiftCard(userId, brandId: brandId, cardNumber: cardNumber, cardPin: cardPin) { (userGiftCard, error) in
            if error != nil{
                if error?.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.associateGiftCard(userId, brandId: brandId, cardNumber: cardNumber, cardPin: cardPin) { (userGiftCard, error) in
                                if error != nil{
                                    return callback(nil, error)
                                }
                                else{
                                    return callback(userGiftCard, nil)
                                }
                            }
                        }
                        else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil, error)
                }
            }
            else{
                return callback(userGiftCard, nil)
            }
        }
    }
    
    //MARK: Get auto reload giftcards for payment account
    func getAutoReloadGiftCardsForPaymentAccount(_ paymentAccountId:Int32, callback:@escaping InCommAutoReloadGiftCardsForPaymentCallBack){
        giftCardsDetailsWithAutoReloadDetails = []
        let deleteError:NSError? = NSError.init(description: "An Unexpected error occured while processing your request, \n delete credit card - failure", code: GiftCardAppConstants.creditCardDeleteError)
        self.getAutoReloadGiftCards { (userGiftCards, error) in
            if error != nil{
                return callback([],deleteError)
            }
            else{
                let autoReloadGiftCards = userGiftCards
                UIApplication.inMainThread{
                    self.giftCardsDetailsWithAutoReloadDetails{
                        if autoReloadGiftCards.count == self.giftCardsDetailsWithAutoReloadDetails.count{
                            let deleteAutoReloadGiftCards = self.giftCardsDetailsWithAutoReloadDetails
                            return callback(deleteAutoReloadGiftCards, nil)
                        }
                        else{
                            return callback([], deleteError)
                        }
                    }
                }
            }
        }
    }
    
    //MARK: Get auto reload gift cards
    func getAutoReloadGiftCards(_ callcack:@escaping IncommUserGiftCardsCallBack){
        self.getAllGiftCards { (userGiftCards, error) in
            if error != nil{
                return callcack([], error)
            }
            else{
                let autoReloadGiftCards = userGiftCards.filter{ $0.autoReloadId != nil}
                for autoReloadGiftCard in autoReloadGiftCards{
                    let cardAutoReloadDetail = CardAutoReloadDetails.init(userGiftCard: autoReloadGiftCard, autoReloadDetails: nil)
                    self.giftCardsDetailsWithAutoReloadDetails.append(cardAutoReloadDetail)
                }
                return callcack(autoReloadGiftCards, nil)
            }
        }
    }
    
    //MARK: Get gift card auto reload details
    func giftCardsDetailsWithAutoReloadDetails(_ complete: @escaping (Void) -> Void) {
        let autoReloadGiftCards:[CardAutoReloadDetails] = self.giftCardsDetailsWithAutoReloadDetails
        self.giftCardsDetailsWithAutoReloadDetails = []
        
        let group = DispatchGroup()
        for giftCardDetailsWithAutoReloadDetails in autoReloadGiftCards {
            group.enter()
            let userGiftCard =  giftCardDetailsWithAutoReloadDetails.userGiftCard!
            let cardId = userGiftCard.cardId
            let autoRelaodId = userGiftCard.autoReloadId
            self.getAutoReloadDetails(cardId, autoReloadId: autoRelaodId) {(inCommAutoReloadSavable, error) in
                if error == nil{
                    let cardAutoReloadDetails = CardAutoReloadDetails.init(userGiftCard: userGiftCard, autoReloadDetails: inCommAutoReloadSavable)
                    self.giftCardsDetailsWithAutoReloadDetails.append(cardAutoReloadDetails)
                }
                group.leave()
            }
        }
        // Wait for get all gift card auto reload details
        group.notify(queue: DispatchQueue.main) {
            complete()
        }
    }
    
    //MARK: Get all gift cards
    func getAllGiftCards(_ callback:@escaping IncommUserGiftCardsCallBack){
        InCommUserConfigurationService.sharedInstance.inCommUserValidation { (status) in
            if status{
                InCommUserGiftCardService.getGiftCards(InCommUserConfigurationService.sharedInstance.inCommUserId) { (userGiftCards, error) in
                    if error != nil{
                        if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                            InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus,inCommError) in
                                if inCommUserStatus{
                                    InCommUserGiftCardService.getGiftCards(InCommUserConfigurationService.sharedInstance.inCommUserId){ (userGiftCards, error) in
                                        if error != nil{
                                            return callback([], error)
                                        }
                                        else{
                                            self.inCommUserGiftCards = userGiftCards
                                            // Refresh gift card count in dashboard
                                            self.postNotificationForGiftCardCount()
                                            return callback(userGiftCards, nil)
                                        }
                                    }
                                }
                                else{
                                    return callback([], GiftCardAppConstants.generalError)
                                }
                            })
                        }else {
                            return callback([], error)
                        }
                    } else {
                        self.inCommUserGiftCards = userGiftCards
                        // Refresh gift card count in dashboard
                        self.postNotificationForGiftCardCount()
                        return callback(userGiftCards, nil)
                    }
                }
            }
            else{
                return callback([], GiftCardAppConstants.generalError)
            }
        }
        
    }
    
    //MARK: Get auto reload details
    func getAutoReloadDetails(_ cardId:Int32!,autoReloadId:Int32!,callback:@escaping InCommAutoReloadDetailsCallBack){
        InCommUserGiftCardService.getAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId) { (autoReloadSavable, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.getAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId) { (autoReloadSavable, error) in
                                if error != nil{
                                    return callback(nil, error)
                                }
                                else{
                                    return callback(autoReloadSavable, nil)
                                }
                            }
                        }
                        else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil, error)
                }
            }
            else{
                return callback(autoReloadSavable, nil)
            }
        }
    }
    
    //MARK: Delete auto reload details
    func deleteAutoReloadDetails(_ cardId:Int32!,autoReloadId:Int32!,callback:@escaping InCommDeleteCallBack){
        InCommUserGiftCardService.deleteAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId) { (error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.deleteAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId){ (error)  in
                                if error != nil{
                                    return callback(error)
                                }
                                else{
                                    return callback(nil)
                                }
                            }
                        }
                        else{
                            return callback(GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(error)
                }
            }
            else{
                return callback(nil)
            }
        }
    }
    
    //MARK: Delete payment account
    func deletePaymentAccount(_ paymentId:Int32, callback: @escaping InCommDeleteCallBack){
        InCommUserPaymentService.deletePaymentAccount(InCommUserConfigurationService.sharedInstance.inCommUserId, paymentAccountId: paymentId) { (error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserPaymentService.deletePaymentAccount(InCommUserConfigurationService.sharedInstance.inCommUserId, paymentAccountId: paymentId) { (error) in
                                if error != nil{
                                    return callback(error)
                                }
                                else{
                                    return callback(nil)
                                }
                            }
                        }
                        else{
                            return callback(GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(error)
                }
            }
            else{
                return callback(nil)
            }
        }
    }
    
    //MARK: Get gift cards
    func getUserGiftCardsCount(_ callback:@escaping IncommUserGiftCardsCountCallBack){
        if inCommUserGiftCards != nil{
            return callback(inCommUserGiftCards!)
        }
        InCommUserConfigurationService.sharedInstance.inCommUserValidation { (status) in
            if status{
                self.getAllGiftCards{ (userGiftCards, error) in
                    if error != nil{
                        return callback([])
                    }
                    else{
                        return callback(userGiftCards)
                    }
                }
            }
            else{
                return callback([])
            }
        }
    }
    
    //MARK: Transaction history
    func getTransactionHistoryDetails(_ cardId:Int32!, callback:@escaping InCommTransactionHistoryCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        if userId == nil{
            return callback(nil, GiftCardAppConstants.generalError)
        }
        
        InCommUserGiftCardService.getGiftCardTransactionHistory(userId, cardId: cardId) { (giftCardTransactionHistory, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.getGiftCardTransactionHistory(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId) { (giftCardTransactionHistory, error) in
                                if error != nil{
                                    return callback(nil,error)
                                }
                                else{
                                    return callback(giftCardTransactionHistory,nil)
                                }
                            }
                        }
                        else{
                            return callback(nil,GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil,error)
                }
            }
            else{
                return callback(giftCardTransactionHistory,nil)
            }
        }
    }
    
    //MARK: Set Auto reload config details
    func setAutoReloadConfig(_ cardId:Int32,autoReload:InCommAutoReload,callback:@escaping InCommAutoReloadDetailsCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        if userId == nil{
            return callback(nil, GiftCardAppConstants.generalError)
        }
        InCommUserGiftCardService.createAutoReload(userId, cardId: cardId, autoReload: autoReload) { (autoReloadSavable, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.createAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReload: autoReload) { (autoReloadSavable, error) in
                                if error != nil{
                                    return callback(nil,error)
                                }
                                else{
                                    return callback(autoReloadSavable,nil)
                                }
                            }
                        }
                        else{
                            return callback(nil,GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil,error)
                }
            }
            else{
                return callback(autoReloadSavable,nil)
            }
        }
    }
    
    
    //MARK: Post notification for gift card count
    func postNotificationForGiftCardCount(){
        NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.JambaGiftCardCountRefresh.rawValue), object: nil);
    }
    
    //MARK: Reset incomm user gift cards
    func resetInCommUserGiftCards(){
        inCommUserGiftCards = nil
    }
    
    //MARK: Reset incomm auth token
    func resetInCommAuthToken(){
        inCommAuthToken = nil
    }
    
    //MARK: Get payment account details
    func getUserPaymentAccountDetails(_ cardId:Int32,paymentAccountId:Int32,callback:@escaping InCommAssociatePaymentAccountToUserCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        if userId == nil{
            return callback(nil, GiftCardAppConstants.generalError)
        }
        InCommUserPaymentService.getPaymentAccountDetails(userId, paymentAccountId: paymentAccountId) { (userPaymentAccount, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserPaymentService.getPaymentAccountDetails(InCommUserConfigurationService.sharedInstance.inCommUserId, paymentAccountId: paymentAccountId) { (userPaymentAccount, error) in
                                if error != nil{
                                    return callback(nil,error)
                                }
                                else{
                                    return callback(userPaymentAccount,nil)
                                }
                            }
                        }
                        else{
                            return callback(nil,GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil,error)
                }
            }
            else{
                return callback(userPaymentAccount,nil)
            }
        }
    }
    
    //MARK: Get payment account details
    func updateAutoReloadConfigStatus(_ cardId:Int32,autoReloadId:Int32,status:Bool,callback:@escaping InCommDeleteCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        if userId == nil{
            return callback(GiftCardAppConstants.generalError)
        }
        InCommUserGiftCardService.updateAutoReloadStatus(userId, cardId: cardId, autoReloadId: autoReloadId, active: status) { (error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.updateAutoReloadStatus(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId, active: status) { (error) in
                                if error != nil{
                                    return callback(error)
                                }
                                else{
                                    return callback(nil)
                                }
                            }
                        }
                        else{
                            return callback(GiftCardAppConstants.generalError)
                        }
                    })
                    
                }
                else{
                    return callback(error)
                }
            }
            else{
                return callback(nil)
            }
        }
    }
    
    func getUserGiftCard(_ cardId: Int32) -> InCommUserGiftCard?{
        if inCommUserGiftCards  == nil{
            return nil
        }
        var userGiftCard:InCommUserGiftCard?
        for (i,giftCard) in inCommUserGiftCards!.enumerated(){
            if giftCard.cardId == cardId{
                userGiftCard = inCommUserGiftCards![i]
                break
            }
        }
        return userGiftCard
    }
    
    func updateUserGiftCard(_ userGiftCard:InCommUserGiftCard){
        if inCommUserGiftCards  == nil{
            return
        }
        for (i,giftCard) in inCommUserGiftCards!.enumerated(){
            if userGiftCard.cardId == giftCard.cardId{
                inCommUserGiftCards![i] = userGiftCard
                break
            }
        }
    }
    
    func updateUserGiftCards(_ userGiftCards:[InCommUserGiftCard]){
        self.inCommUserGiftCards = userGiftCards
    }
    
    func getUserCardIndex(_ cardId:Int32) -> Int{
        if inCommUserGiftCards  == nil{
            return 0
        }
        var indexValue:Int = 0
        for (i,giftCard) in inCommUserGiftCards!.enumerated(){
            if giftCard.cardId == cardId{
                indexValue = i
                break
            }
        }
        return indexValue
    }
    
    func deleteGiftCardFromUserGiftCards(_ cardId:Int32){
        if inCommUserGiftCards == nil{
            return
        }
        for (i,giftCard) in inCommUserGiftCards!.enumerated(){
            if giftCard.cardId == cardId{
                inCommUserGiftCards!.remove(at: i)
                self.postNotificationForGiftCardCount()
                break
            }
        }
    }
    
    // MARK: WebSession
    func createVestWebSession(_ callback: @escaping InCommVestaWebSessionCallback){
        InCommVestaService.vestaWebSession { (vestaWebSession, error) in
            if error == nil{
                return callback(vestaWebSession, nil)
            }
            else{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, incommError) in
                        if inCommUserStatus{
                            InCommVestaService.vestaWebSession{(vestaWebSession, error) in
                                if error == nil{
                                    return callback(vestaWebSession, nil)
                                }
                                else{
                                    return callback(nil, error)
                                }
                            }
                        }
                        else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil, GiftCardAppConstants.generalError)
                }
            }
        }
    }
    
    // MARK: Set InComm AuthToken
    
    func setInCommAuthToken(_ inCommAuthToken:String){
        self.inCommAuthToken = inCommAuthToken
    }
    
    // MARK: PromoOrder
    func submitPromoOrder(_ promoOrder: InCommPromoOrder){
        
        let fisbowlIncommService = FishbowlIncommService()
        fisbowlIncommService.submitPromoOrder(promoOrder)
        
//                clpAnalyticsService.sharedInstance.clpsdkobj?.submitInCommPromoOrder(promoOrder.serializeAsJSONDictionary()) { (result, error) -> Void in
//                    if error != nil{
//                        if error.code == GiftCardAppConstants.incommTokenExpirationOrInvalidUserId{
//                            InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
//                                if inCommUserStatus{
//                                    clpAnalyticsService.sharedInstance.clpsdkobj?.submitInCommPromoOrder(promoOrder.serializeAsJSONDictionary()){ (result, error) -> Void in
//                                        // No Retry
//                                    }
//                                }
//        
//                            })
//                        }
//                    }
//                    else{
//                        if result != nil {
//                            let code = result["code"]
//                            if code != nil && code!.integerValue == GiftCardAppConstants.errorCodeInvalidUser{
//                                InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
//                                    if inCommUserStatus{
//                                        clpAnalyticsService.sharedInstance.clpsdkobj?.submitInCommPromoOrder(promoOrder.serializeAsJSONDictionary()){ (result, error) -> Void in
//                                            // No Retry
//                                        }
//                                    }
//        
//                                })
//                            }
//                        }
//                    }
//        }
        
    }
    
    // MARK: Submit Reload Order
    func submitReloadOrder(_ submitReloadOrder:InCommReloadOrder, callback:@escaping IncommSubmitOrderCallBack){
        InCommOrdersService.submitReloadOrder(submitReloadOrder) { (order, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommOrdersService.submitReloadOrder(submitReloadOrder, callback: { (order, error) in
                                return callback(order, error)
                            })
                        }
                        else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(nil, error)
                }
            }
            else{
                return callback(order, nil)
            }
        }
    }
    
    //MARK: Update Gift card name
    func updateUserGiftCardName(_ userId: Int32!, cardId: Int32!, cardName: String!,callback:@escaping IncommUserGiftCardCallback){
        InCommUserGiftCardService.updateUserGiftCardName(userId, cardId: cardId, cardName: cardName) { (userGiftCard, error) in
            if  error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.updateUserGiftCardName(userId, cardId: cardId, cardName: cardName){ (userGiftCard, error) in
                                return callback(userGiftCard, error)
                            }
                        }else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }else{
                    return callback(nil, error)
                }
            }else{
                return callback(userGiftCard, nil)
            }
        }
    }
    
    //MARK: Add Existing gift card
    func getExistingGiftCardDetails(_ brandId:String, cardNumber:String, cardPin:String, lastBalance:Bool, callback:@escaping InCommCardCallback){
        InCommCardsService.card(brandId, cardNumber: cardNumber, pin: cardPin, getLatestBalance: lastBalance) { (card, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommCardsService.card(brandId, cardNumber: cardNumber, pin: cardPin, getLatestBalance: lastBalance, callback: { (card, error) in
                                return callback(card, error)
                            })
                        }else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }else{
                    return callback(nil, error)
                }
            }else{
                return callback(card, nil)
            }
            
        }
    }
    //MARK: Get latest balance
    // Get latest balance for user card
    func getLatestBalance(_ cardID:Int32, callback:@escaping InCommGiftCardBalanceCallback){
        InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardID) { (giftCardBalance, error) in
            if let incommError = error{
                if incommError.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardID, callback: { (giftCardBalance, error) in
                                return callback(giftCardBalance, error)
                            })
                        }else{
                            return callback(nil, GiftCardAppConstants.generalError)
                        }
                    })
                }else{
                    return callback(nil, error)
                }
            }else{
                return callback(giftCardBalance, nil)
            }
        }
    }
    
    //MARK: Get incomm order id
    // Get incomm order id
    func getInCommOrderId(_ callback:@escaping InCommOrderIdCallback){
        // check incomm user id is nil or not for precaution
        guard (UserService.sharedUser?.id) != nil else{
            return callback(nil, GiftCardAppConstants.generalError)
        }
        
        let fishbowlIncommservice = FishbowlIncommService()
        
        fishbowlIncommservice.getIncommOrderId { (incommOrderId, error) in
            callback(incommOrderId, error)
        }
        
    
//                let inCommUserIdDetails = SubmitInCommOrderIdDetails.init(customerId: spendGoUserId)
//                print("getInCommOrderId")
//                clpAnalyticsService.sharedInstance.clpsdkobj?.getInCommOrderId(inCommUserIdDetails.serializeAsJSONDictionary()) { (result, error) -> Void in
//                    if error != nil{
//                        return callback(inCommOrderId: nil, error: error)
//                    }
//                    else{
//                        if result != nil{
//                            if let inCommOrderId = result["inCommOrderId"]{
//                                if let inCommOrderIdString = inCommOrderId as? String{
//                                    return callback(inCommOrderId: inCommOrderIdString, error:nil)
//                                }else{
//                                    return callback(inCommOrderId: nil, error: GiftCardAppConstants.generalError)
//                                }
//                            }
//                            else{
//                                return callback(inCommOrderId: nil, error: GiftCardAppConstants.generalError)
//                            }
//                        }
//                        else{
//                            return callback(inCommOrderId: nil, error: GiftCardAppConstants.generalError)
//                        }
//                    }
//                }
    }
    
    //MARK: Update incomm order status
    // Update incomm order status
    func updateInCommOrderStatus(_ inCommOrderStatusDetails: InCommOrderStatusDetails){
        guard let spendGoUserId = UserService.sharedUser?.id else{
            return
        }
        var orderStatusDetails = inCommOrderStatusDetails
        orderStatusDetails.customerId = spendGoUserId
        
        let fishbowlIncommService = FishbowlIncommService()
        
        fishbowlIncommService.updateTransactionDetails(orderStatusDetails)
    }
}
