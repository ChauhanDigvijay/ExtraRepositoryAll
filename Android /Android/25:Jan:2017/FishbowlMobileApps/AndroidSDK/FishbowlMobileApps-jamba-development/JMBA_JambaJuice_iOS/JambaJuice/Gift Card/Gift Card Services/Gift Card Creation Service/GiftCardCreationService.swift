
//
//  GiftCardCreationService.swift
//  JambaGiftCard
//
//  Created by vThink on 6/6/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import InCommSDK


typealias IncommBrandServiceCallBack =  (brand:InCommBrand?,error:NSError?) -> Void
typealias IncommSubmitOrderCallBack = (order:InCommOrder?,error:NSError?) -> Void
typealias IncommPaymentDetailCallBack = (payment:InCommCard?,error:NSError?) -> Void
typealias IncommUserGiftCardsCountCallBack = (userGiftCards: [InCommUserGiftCard]) -> Void
typealias IncommUserGiftCardsCallBack = (userGiftCards: [InCommUserGiftCard], error: NSError?) -> Void
typealias InCommAutoReloadDetailsCallBack = (inCommAutoReloadSavable: InCommAutoReloadSavable?, error: NSError?) -> Void
typealias InCommAssociateGiftCardToUserCallBack = (inCommUserGiftCard: InCommUserGiftCard?, error: NSError?) -> Void
typealias InCommAssociatePaymentAccountToUserCallBack = (inCommUserPaymentAccount: InCommUserPaymentAccount?, error: NSError?) -> Void
typealias InCommAutoReloadGiftCardsForPaymentCallBack = (giftCardsDetailsWithAutoReloadDetails:[CardAutoReloadDetails], error:NSError?) -> Void
typealias InCommDeleteCallBack = (error: NSError?) -> Void
typealias InCommAutoReloadAndCreditCardDeleteCallBack = (giftCardsDetailsWithAutoReloadDetails:[CardAutoReloadDetails], error:NSError?) -> Void
typealias InCommTransactionHistoryCallBack = (inCommTransactionHistory: InCommGiftCardTransactionHistory?, error:NSError?) -> Void


typealias InCommVestaWebSession = (vestaOrgId: String, vestaWebSessionId: String)
typealias InCommVestaWebSessionCallback = (vestaWebSession: InCommVestaWebSession?, error: NSError?) -> Void
typealias IncommUserGiftCardCallback = (userGiftCard: InCommUserGiftCard?, error: NSError?) -> Void
typealias InCommCardCallback = (card: InCommCard?, error: NSError?) -> Void
typealias InCommGiftCardBalanceCallback = (giftCardBalance: InCommGiftCardBalance?, error: NSError?) -> Void

// InComm Order Id Callback
typealias InCommOrderIdCallback = (inCommOrderId: String?, error: NSError?) -> Void



class GiftCardCreationService{
    
    static let sharedInstance = GiftCardCreationService()
    
    private(set) var brand:InCommBrand!
    private(set) var incommBrandImageCode:String!
    private(set) var cardAmount:String!
    private(set) var cardQuantity:UInt32!
    private(set) var paymentDetails:InCommSubmitPayment?
    private(set) var paymentDetailsId:InCommSubmitPaymentWithId?
    private(set) var existingPaymentDetails:InCommUserPaymentAccount?
    private(set) var recipientEmail:String!
    private(set) var recipientMessage:String!
    private(set) var recipientSelf:Bool!
    private(set) var purchaserInformationFromPayment:Bool!
    private(set) var purchaserDetails:InCommOrderPurchaser!
    private(set) var savePaymentDetails:InCommUserPaymentAccountDetails!
    private(set) var recipientDetails:InCommOrderRecipientDetails!
    private(set) var inCommUserGiftCards:[InCommUserGiftCard]?
    
    private(set) var inCommAuthToken:String?
    
    // private var autoReloadGiftCards:[InCommUserGiftCard] = []
    private var giftCardsDetailsWithAutoReloadDetails:[CardAutoReloadDetails] = []
    
    func setIncommBrandImageCodeAndAmount(imageCode:String?,amount:String?){
        self.incommBrandImageCode = imageCode
        self.cardAmount = amount
    }
    
    func setIncommBrandImageCode(imageCode:String?){
        self.incommBrandImageCode = imageCode
    }
    
    // Billing detail payment
    func setPaymentDetails(paymentDetails:InCommSubmitPayment){
        self.paymentDetails = paymentDetails
    }
    
    //set payment id for user associated cards
    func setPaymentDetailsId(paymentDetailsId:InCommSubmitPaymentWithId){
        self.paymentDetailsId = paymentDetailsId
    }
    
    func updatePaymentDetailsAmount(amount:Double){
        if (self.paymentDetails != nil) {
            self.paymentDetails!.amount = amount
        } else if (self.paymentDetailsId != nil) {
            self.paymentDetailsId!.amount = amount
        }
    }
    
    //set individual card amount
    func setCardAmout(amount:String) {
        self.cardAmount = amount
    }
    
    //set payment details for user associated cards
    func setexistingPaymentDetails(existingPaymentDetails:InCommUserPaymentAccount){
        self.existingPaymentDetails = existingPaymentDetails
    }
    
    func setCardQuantity(Quantity:Int){
        self.cardQuantity = UInt32(Quantity)
    }
    
    func setPurchaserDetails(purchaserDetails:InCommOrderPurchaser){
        self.purchaserDetails = purchaserDetails
    }
    
    func setReceipientDetails(recipientDetails:InCommOrderRecipientDetails){
        self.recipientDetails = recipientDetails
    }
    
    func setReceipientMessage(recipientMessage:String){
        self.recipientMessage = recipientMessage
    }
    
    //set the value for purchaser information get from payment
    func setPurchaserInformationFromPayment(purchaserInformationFromPayment:Bool){
        self.purchaserInformationFromPayment = purchaserInformationFromPayment
    }
    
    func setReceipientSelf(recipientSelf:Bool){
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
    func setSavePaymentDetails(savePaymentDetails:InCommUserPaymentAccountDetails){
        self.savePaymentDetails = savePaymentDetails
    }
    
    func associateGiftCardToUser(cardToken:String?, callback:InCommAssociateGiftCardToUserCallBack){
        
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        
        if cardToken == nil{
            return callback(inCommUserGiftCard: nil, error: GiftCardAppConstants.generalError)
        }
        
        if userId == nil{
            return callback(inCommUserGiftCard: nil, error: GiftCardAppConstants.generalError)
        }
        
        InCommUserGiftCardService.associateGiftCard(userId, cardToken: cardToken) { (userGiftCard, error) in
            if error != nil{
                if error?.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.associateGiftCard(userId, cardToken: cardToken){ (userGiftCard, error) in
                                if error != nil{
                                    return callback(inCommUserGiftCard: nil, error: error)
                                }
                                else{
                                    return callback(inCommUserGiftCard:  userGiftCard, error:  nil)
                                }
                            }
                        }
                        else{
                            return callback(inCommUserGiftCard: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(inCommUserGiftCard: nil, error: error)
                }
            }
            else{
                return callback(inCommUserGiftCard: userGiftCard, error: nil)
            }
        }
    }
    
    func associatePaymentDetailsToUser(paymentDetails: InCommUserPaymentAccountDetails!, callback: InCommAssociatePaymentAccountToUserCallBack){
        
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        
        if userId == nil{
            return callback(inCommUserPaymentAccount: nil, error: GiftCardAppConstants.generalError)
        }
        
        InCommUserPaymentService.addPaymentAccount(userId, userPaymentAccount: paymentDetails) { (userPaymentAccount, error) in
            if error != nil{
                if error?.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserPaymentService.addPaymentAccount(userId, userPaymentAccount: paymentDetails) { (userPaymentAccount, error) in
                                if error != nil{
                                    return callback(inCommUserPaymentAccount: nil, error: error)
                                }
                                else{
                                    return callback(inCommUserPaymentAccount: userPaymentAccount, error: nil)
                                }
                            }
                        }
                        else{
                            return callback(inCommUserPaymentAccount: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(inCommUserPaymentAccount: nil, error: error)
                }
            }
            else{
                return callback(inCommUserPaymentAccount: userPaymentAccount, error: nil)
            }
        }
    }
    
    
    
    func createOrSendGiftCard(inCommSubmitOrder:InCommSubmitOrder!, callback: IncommSubmitOrderCallBack){
        InCommOrdersService.submitOrder(inCommSubmitOrder) { (order, error) in
            if(error != nil){
                if (error?.code == GiftCardAppConstants.errorCodeInvalidUser) {
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommOrdersService.submitOrder(inCommSubmitOrder) { (order, error) in
                                if(error != nil){
                                    return callback(order:nil,error:error)
                                }
                                else{
                                    return callback(order:order,error:nil)
                                }
                            }
                            
                        }
                        else{
                            return callback(order:nil,error:error)
                            
                        }
                    })
                }
                else{
                    return callback(order:nil,error:error)
                }
            }
            else{
                return callback(order:order,error:nil)
            }
        }
    }
    
    
    //MARK: Associate gift card by Number and Pin
    func associateGiftCardToUser(cardNumber:String!,cardPin: String!, callback:InCommAssociateGiftCardToUserCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        let brandId = InCommGiftCardBrandDetails.sharedInstance.brandID
        
        if userId == nil{
            return callback(inCommUserGiftCard: nil, error: GiftCardAppConstants.generalError)
        }
        
        InCommUserGiftCardService.associateGiftCard(userId, brandId: brandId, cardNumber: cardNumber, cardPin: cardPin) { (userGiftCard, error) in
            if error != nil{
                if error?.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.associateGiftCard(userId, brandId: brandId, cardNumber: cardNumber, cardPin: cardPin) { (userGiftCard, error) in
                                if error != nil{
                                    return callback(inCommUserGiftCard: nil, error: error)
                                }
                                else{
                                    return callback(inCommUserGiftCard:  userGiftCard, error:  nil)
                                }
                            }
                        }
                        else{
                            return callback(inCommUserGiftCard: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(inCommUserGiftCard: nil, error: error)
                }
            }
            else{
                return callback(inCommUserGiftCard: userGiftCard, error: nil)
            }
        }
    }
    
    //MARK: Get auto reload giftcards for payment account
    func getAutoReloadGiftCardsForPaymentAccount(paymentAccountId:Int32, callback:InCommAutoReloadGiftCardsForPaymentCallBack){
        giftCardsDetailsWithAutoReloadDetails = []
        let deleteError:NSError? = NSError.init(description: "An Unexpected error occured while processing your request, \n delete credit card - failure", code: GiftCardAppConstants.creditCardDeleteError)
        self.getAutoReloadGiftCards { (userGiftCards, error) in
            if error != nil{
                return callback(giftCardsDetailsWithAutoReloadDetails:[],error: deleteError)
            }
            else{
                let autoReloadGiftCards = userGiftCards
                UIApplication.inMainThread{
                    self.giftCardsDetailsWithAutoReloadDetails{
                        if autoReloadGiftCards.count == self.giftCardsDetailsWithAutoReloadDetails.count{
                            let deleteAutoReloadGiftCards = self.giftCardsDetailsWithAutoReloadDetails
                            return callback(giftCardsDetailsWithAutoReloadDetails:deleteAutoReloadGiftCards, error: nil)
                        }
                        else{
                            return callback(giftCardsDetailsWithAutoReloadDetails: [], error: deleteError)
                        }
                    }
                }
            }
        }
    }
    
    //MARK: Get auto reload gift cards
    func getAutoReloadGiftCards(callcack:IncommUserGiftCardsCallBack){
        self.getAllGiftCards { (userGiftCards, error) in
            if error != nil{
                return callcack(userGiftCards: [], error: error)
            }
            else{
                let autoReloadGiftCards = userGiftCards.filter{ $0.autoReloadId != nil}
                for autoReloadGiftCard in autoReloadGiftCards{
                    let cardAutoReloadDetail = CardAutoReloadDetails.init(userGiftCard: autoReloadGiftCard, autoReloadDetails: nil)
                    self.giftCardsDetailsWithAutoReloadDetails.append(cardAutoReloadDetail)
                }
                return callcack(userGiftCards:autoReloadGiftCards, error: nil)
            }
        }
    }
    
    //MARK: Get gift card auto reload details
    func giftCardsDetailsWithAutoReloadDetails(complete: Void -> Void) {
        let autoReloadGiftCards:[CardAutoReloadDetails] = self.giftCardsDetailsWithAutoReloadDetails
        self.giftCardsDetailsWithAutoReloadDetails = []
        
        let group = dispatch_group_create()
        for giftCardDetailsWithAutoReloadDetails in autoReloadGiftCards {
            dispatch_group_enter(group)
            let userGiftCard =  giftCardDetailsWithAutoReloadDetails.userGiftCard!
            let cardId = userGiftCard.cardId
            let autoRelaodId = userGiftCard.autoReloadId
            self.getAutoReloadDetails(cardId, autoReloadId: autoRelaodId) {(inCommAutoReloadSavable, error) in
                if error == nil{
                    let cardAutoReloadDetails = CardAutoReloadDetails.init(userGiftCard: userGiftCard, autoReloadDetails: inCommAutoReloadSavable)
                    self.giftCardsDetailsWithAutoReloadDetails.append(cardAutoReloadDetails)
                }
                dispatch_group_leave(group)
            }
        }
        // Wait for get all gift card auto reload details
        dispatch_group_notify(group, dispatch_get_main_queue()) {
            complete()
        }
    }
    
    //MARK: Get all gift cards
    func getAllGiftCards(callback:IncommUserGiftCardsCallBack){
        InCommUserConfigurationService.sharedInstance.inCommUserValidation { (status) in
            if status{
                InCommUserGiftCardService.getGiftCards(InCommUserConfigurationService.sharedInstance.inCommUserId) { (userGiftCards, error) in
                    if error != nil{
                        if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                            InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                                if inCommUserStatus{
                                    InCommUserGiftCardService.getGiftCards(InCommUserConfigurationService.sharedInstance.inCommUserId){ (userGiftCards, error) in
                                        if error != nil{
                                            return callback(userGiftCards: [], error: error)
                                        }
                                        else{
                                            self.inCommUserGiftCards = userGiftCards
                                            // Refresh gift card count in dashboard
                                            self.postNotificationForGiftCardCount()
                                            return callback(userGiftCards: userGiftCards, error: nil)
                                        }
                                    }
                                }
                                else{
                                    return callback(userGiftCards: [], error: GiftCardAppConstants.generalError)
                                }
                            })
                        }else {
                            return callback(userGiftCards: [], error: error)
                        }
                    } else {
                        self.inCommUserGiftCards = userGiftCards
                        // Refresh gift card count in dashboard
                        self.postNotificationForGiftCardCount()
                        return callback(userGiftCards: userGiftCards, error: nil)
                    }
                }
            }
            else{
                return callback(userGiftCards: [], error: GiftCardAppConstants.generalError)
            }
        }
        
    }
    
    //MARK: Get auto reload details
    func getAutoReloadDetails(cardId:Int32!,autoReloadId:Int32!,callback:InCommAutoReloadDetailsCallBack){
        InCommUserGiftCardService.getAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId) { (autoReloadSavable, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.getAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId) { (autoReloadSavable, error) in
                                if error != nil{
                                    return callback(inCommAutoReloadSavable: nil, error: error)
                                }
                                else{
                                    return callback(inCommAutoReloadSavable: autoReloadSavable, error:nil)
                                }
                            }
                        }
                        else{
                            return callback(inCommAutoReloadSavable: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(inCommAutoReloadSavable: nil, error: error)
                }
            }
            else{
                return callback(inCommAutoReloadSavable: autoReloadSavable, error: nil)
            }
        }
    }
    
    //MARK: Delete auto reload details
    func deleteAutoReloadDetails(cardId:Int32!,autoReloadId:Int32!,callback:InCommDeleteCallBack){
        InCommUserGiftCardService.deleteAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId) { (error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.deleteAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId){ (error)  in
                                if error != nil{
                                    return callback(error: error)
                                }
                                else{
                                    return callback(error:nil)
                                }
                            }
                        }
                        else{
                            return callback(error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(error: error)
                }
            }
            else{
                return callback(error: nil)
            }
        }
    }
    
    //MARK: Delete payment account
    func deletePaymentAccount(paymentId:Int32, callback: InCommDeleteCallBack){
        InCommUserPaymentService.deletePaymentAccount(InCommUserConfigurationService.sharedInstance.inCommUserId, paymentAccountId: paymentId) { (error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserPaymentService.deletePaymentAccount(InCommUserConfigurationService.sharedInstance.inCommUserId, paymentAccountId: paymentId) { (error) in
                                if error != nil{
                                    return callback(error: error)
                                }
                                else{
                                    return callback(error:nil)
                                }
                            }
                        }
                        else{
                            return callback(error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(error: error)
                }
            }
            else{
                return callback(error: nil)
            }
        }
    }
    
    //MARK: Get gift cards
    func getUserGiftCardsCount(callback:IncommUserGiftCardsCountCallBack){
        if inCommUserGiftCards != nil{
            return callback(userGiftCards: inCommUserGiftCards!)
        }
        InCommUserConfigurationService.sharedInstance.inCommUserValidation { (status) in
            if status{
                self.getAllGiftCards{ (userGiftCards, error) in
                    if error != nil{
                        return callback(userGiftCards: [])
                    }
                    else{
                        return callback(userGiftCards: userGiftCards)
                    }
                }
            }
            else{
                return callback(userGiftCards: [])
            }
        }
    }
    
    //MARK: Transaction history
    func getTransactionHistoryDetails(cardId:Int32!, callback:InCommTransactionHistoryCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        if userId == nil{
            return callback(inCommTransactionHistory: nil, error: GiftCardAppConstants.generalError)
        }
        
        InCommUserGiftCardService.getGiftCardTransactionHistory(userId, cardId: cardId) { (giftCardTransactionHistory, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.getGiftCardTransactionHistory(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId) { (giftCardTransactionHistory, error) in
                                if error != nil{
                                    return callback(inCommTransactionHistory: nil,error: error)
                                }
                                else{
                                    return callback(inCommTransactionHistory: giftCardTransactionHistory,error:nil)
                                }
                            }
                        }
                        else{
                            return callback(inCommTransactionHistory: nil,error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(inCommTransactionHistory: nil,error: error)
                }
            }
            else{
                return callback(inCommTransactionHistory: giftCardTransactionHistory,error:nil)
            }
        }
    }
    
    //MARK: Set Auto reload config details
    func setAutoReloadConfig(cardId:Int32,autoReload:InCommAutoReload,callback:InCommAutoReloadDetailsCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        if userId == nil{
            return callback(inCommAutoReloadSavable: nil, error: GiftCardAppConstants.generalError)
        }
        InCommUserGiftCardService.createAutoReload(userId, cardId: cardId, autoReload: autoReload) { (autoReloadSavable, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.createAutoReload(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReload: autoReload) { (autoReloadSavable, error) in
                                if error != nil{
                                    return callback(inCommAutoReloadSavable: nil,error: error)
                                }
                                else{
                                    return callback(inCommAutoReloadSavable: autoReloadSavable,error:nil)
                                }
                            }
                        }
                        else{
                            return callback(inCommAutoReloadSavable: nil,error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(inCommAutoReloadSavable: nil,error: error)
                }
            }
            else{
                return callback(inCommAutoReloadSavable: autoReloadSavable,error:nil)
            }
        }
    }
    
    
    //MARK: Post notification for gift card count
    func postNotificationForGiftCardCount(){
        NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.JambaGiftCardCountRefresh.rawValue, object: nil);
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
    func getUserPaymentAccountDetails(cardId:Int32,paymentAccountId:Int32,callback:InCommAssociatePaymentAccountToUserCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        if userId == nil{
            return callback(inCommUserPaymentAccount: nil, error: GiftCardAppConstants.generalError)
        }
        InCommUserPaymentService.getPaymentAccountDetails(userId, paymentAccountId: paymentAccountId) { (userPaymentAccount, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserPaymentService.getPaymentAccountDetails(InCommUserConfigurationService.sharedInstance.inCommUserId, paymentAccountId: paymentAccountId) { (userPaymentAccount, error) in
                                if error != nil{
                                    return callback(inCommUserPaymentAccount: nil,error: error)
                                }
                                else{
                                    return callback(inCommUserPaymentAccount: userPaymentAccount,error:nil)
                                }
                            }
                        }
                        else{
                            return callback(inCommUserPaymentAccount: nil,error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(inCommUserPaymentAccount: nil,error: error)
                }
            }
            else{
                return callback(inCommUserPaymentAccount: userPaymentAccount,error:nil)
            }
        }
    }
    
    //MARK: Get payment account details
    func updateAutoReloadConfigStatus(cardId:Int32,autoReloadId:Int32,status:Bool,callback:InCommDeleteCallBack){
        let userId  = InCommUserConfigurationService.sharedInstance.inCommUserId
        if userId == nil{
            return callback(error: GiftCardAppConstants.generalError)
        }
        InCommUserGiftCardService.updateAutoReloadStatus(userId, cardId: cardId, autoReloadId: autoReloadId, active: status) { (error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.updateAutoReloadStatus(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardId, autoReloadId: autoReloadId, active: status) { (error) in
                                if error != nil{
                                    return callback(error: error)
                                }
                                else{
                                    return callback(error:nil)
                                }
                            }
                        }
                        else{
                            return callback(error: GiftCardAppConstants.generalError)
                        }
                    })
                    
                }
                else{
                    return callback(error: error)
                }
            }
            else{
                return callback(error:nil)
            }
        }
    }
    
    func getUserGiftCard(cardId: Int32) -> InCommUserGiftCard?{
        if inCommUserGiftCards  == nil{
            return nil
        }
        var userGiftCard:InCommUserGiftCard?
        for (i,giftCard) in inCommUserGiftCards!.enumerate(){
            if giftCard.cardId == cardId{
                userGiftCard = inCommUserGiftCards![i]
                break
            }
        }
        return userGiftCard
    }
    
    func updateUserGiftCard(userGiftCard:InCommUserGiftCard){
        if inCommUserGiftCards  == nil{
            return
        }
        for (i,giftCard) in inCommUserGiftCards!.enumerate(){
            if userGiftCard.cardId == giftCard.cardId{
                inCommUserGiftCards![i] = userGiftCard
                break
            }
        }
    }
    
    func updateUserGiftCards(userGiftCards:[InCommUserGiftCard]){
        self.inCommUserGiftCards = userGiftCards
    }
    
    func getUserCardIndex(cardId:Int32) -> Int{
        if inCommUserGiftCards  == nil{
            return 0
        }
        var indexValue:Int = 0
        for (i,giftCard) in inCommUserGiftCards!.enumerate(){
            if giftCard.cardId == cardId{
                indexValue = i
                break
            }
        }
        return indexValue
    }
    
    func deleteGiftCardFromUserGiftCards(cardId:Int32){
        if inCommUserGiftCards == nil{
            return
        }
        for (i,giftCard) in inCommUserGiftCards!.enumerate(){
            if giftCard.cardId == cardId{
                inCommUserGiftCards!.removeAtIndex(i)
                self.postNotificationForGiftCardCount()
                break
            }
        }
    }
    
    // MARK: WebSession
    func createVestWebSession(callback: InCommVestaWebSessionCallback){
        InCommVestaService.vestaWebSession { (vestaWebSession, error) in
            if error == nil{
                return callback(vestaWebSession: vestaWebSession, error: nil)
            }
            else{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommVestaService.vestaWebSession{(vestaWebSession, error) in
                                if error == nil{
                                    return callback(vestaWebSession: vestaWebSession, error: nil)
                                }
                                else{
                                    return callback(vestaWebSession: nil, error: error)
                                }
                            }
                        }
                        else{
                            return callback(vestaWebSession: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(vestaWebSession: nil, error: GiftCardAppConstants.generalError)
                }
            }
        }
    }
    
    // MARK: Set InComm AuthToken
    
    func setInCommAuthToken(inCommAuthToken:String){
        self.inCommAuthToken = inCommAuthToken
    }
    
    // MARK: PromoOrder
    func submitPromoOrder(promoOrder: InCommPromoOrder){
        //        clpAnalyticsService.sharedInstance.clpsdkobj?.submitInCommPromoOrder(promoOrder.serializeAsJSONDictionary()) { (result, error) -> Void in
        //            if error != nil{
        //                if error.code == GiftCardAppConstants.incommTokenExpirationOrInvalidUserId{
        //                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
        //                        if inCommUserStatus{
        //                            clpAnalyticsService.sharedInstance.clpsdkobj?.submitInCommPromoOrder(promoOrder.serializeAsJSONDictionary()){ (result, error) -> Void in
        //                                // No Retry
        //                            }
        //                        }
        //
        //                    })
        //                }
        //            }
        //            else{
        //                if result != nil {
        //                    let code = result["code"]
        //                    if code != nil && code!.integerValue == GiftCardAppConstants.errorCodeInvalidUser{
        //                        InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
        //                            if inCommUserStatus{
        //                                clpAnalyticsService.sharedInstance.clpsdkobj?.submitInCommPromoOrder(promoOrder.serializeAsJSONDictionary()){ (result, error) -> Void in
        //                                    // No Retry
        //                                }
        //                            }
        //
        //                        })
        //                    }
        //                }
        //            }
        //        }
        
    }
    
    // MARK: Submit Reload Order
    func submitReloadOrder(submitReloadOrder:InCommReloadOrder, callback:IncommSubmitOrderCallBack){
        InCommOrdersService.submitReloadOrder(submitReloadOrder) { (order, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommOrdersService.submitReloadOrder(submitReloadOrder, callback: { (order, error) in
                                return callback(order: order, error: error)
                            })
                        }
                        else{
                            return callback(order: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }
                else{
                    return callback(order: nil, error: error)
                }
            }
            else{
                return callback(order: order, error: nil)
            }
        }
    }
    
    //MARK: Update Gift card name
    func updateUserGiftCardName(userId: Int32!, cardId: Int32!, cardName: String!,callback:IncommUserGiftCardCallback){
        InCommUserGiftCardService.updateUserGiftCardName(userId, cardId: cardId, cardName: cardName) { (userGiftCard, error) in
            if  error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.updateUserGiftCardName(userId, cardId: cardId, cardName: cardName){ (userGiftCard, error) in
                                return callback(userGiftCard: userGiftCard, error: error)
                            }
                        }else{
                            return callback(userGiftCard: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }else{
                    return callback(userGiftCard: nil, error: error)
                }
            }else{
                return callback(userGiftCard: userGiftCard, error: nil)
            }
        }
    }
    
    //MARK: Add Existing gift card
    func getExistingGiftCardDetails(brandId:String, cardNumber:String, cardPin:String, lastBalance:Bool, callback:InCommCardCallback){
        InCommCardsService.card(brandId, cardNumber: cardNumber, pin: cardPin, getLatestBalance: lastBalance) { (card, error) in
            if error != nil{
                if error!.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommCardsService.card(brandId, cardNumber: cardNumber, pin: cardPin, getLatestBalance: lastBalance, callback: { (card, error) in
                                return callback(card: card, error: error)
                            })
                        }else{
                            return callback(card: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }else{
                    return callback(card: nil, error: error)
                }
            }else{
                return callback(card: card, error: nil)
            }
            
        }
    }
    //MARK: Get latest balance
    // Get latest balance for user card
    func getLatestBalance(cardID:Int32, callback:InCommGiftCardBalanceCallback){
        InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardID) { (giftCardBalance, error) in
            if let incommError = error{
                if incommError.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: cardID, callback: { (giftCardBalance, error) in
                                return callback(giftCardBalance: giftCardBalance, error: error)
                            })
                        }else{
                            return callback(giftCardBalance: nil, error: GiftCardAppConstants.generalError)
                        }
                    })
                }else{
                    return callback(giftCardBalance: nil, error: error)
                }
            }else{
                return callback(giftCardBalance: giftCardBalance, error: nil)
            }
        }
    }
    
    //MARK: Get incomm order id
    // Get incomm order id
    func getInCommOrderId(callback:InCommOrderIdCallback){
        // check incomm user id is nil or not for precaution
        guard (UserService.sharedUser?.id) != nil else{
            return callback(inCommOrderId: nil, error: GiftCardAppConstants.generalError)
        }
        //        let inCommUserIdDetails = SubmitInCommOrderIdDetails.init(customerId: spendGoUserId)
        //        print("getInCommOrderId")
        //        clpAnalyticsService.sharedInstance.clpsdkobj?.getInCommOrderId(inCommUserIdDetails.serializeAsJSONDictionary()) { (result, error) -> Void in
        //            if error != nil{
        //                return callback(inCommOrderId: nil, error: error)
        //            }
        //            else{
        //                if result != nil{
        //                    if let inCommOrderId = result["inCommOrderId"]{
        //                        if let inCommOrderIdString = inCommOrderId as? String{
        //                            return callback(inCommOrderId: inCommOrderIdString, error:nil)
        //                        }else{
        //                            return callback(inCommOrderId: nil, error: GiftCardAppConstants.generalError)
        //                        }
        //                    }
        //                    else{
        //                        return callback(inCommOrderId: nil, error: GiftCardAppConstants.generalError)
        //                    }
        //                }
        //                else{
        //                    return callback(inCommOrderId: nil, error: GiftCardAppConstants.generalError)
        //                }
        //            }
        //        }
    }
    
    //MARK: Update incomm order status
    // Update incomm order status
    func updateInCommOrderStatus(inCommOrderStatusDetails: InCommOrderStatusDetails){
        guard let spendGoUserId = UserService.sharedUser?.id else{
            return
        }
        var orderStatusDetails = inCommOrderStatusDetails
        orderStatusDetails.customerId = spendGoUserId
        //        clpAnalyticsService.sharedInstance.clpsdkobj?.updateInCommTransactionDetails(orderStatusDetails.serializeAsJSONDictionary()) { (result, error) -> Void in
        //        }
    }
}
