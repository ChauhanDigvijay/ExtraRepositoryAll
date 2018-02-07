//
//  SDKConstants.swift
//  FishBowlLibrary
//
//  Created by Puneet  on 5/22/17.
//  Copyright © 2017 Fishbowl. All rights reserved.
//

import UIKit



let applicationType = "mobilesdk"
let contentType = "application/json"


struct APIkey {
    
    static let AccessToken = "access_token"
    static let ContentType = "Content-Type"
    static let ClientId = "client_id"
    static let Application = "Application"
    static let TenantId = "tenantid"
    static let DeviceId = "deviceId"
    static let ClientSecret = "client_secret"
    static let TenantName = "tenantName"
    
}

struct MethodType {
    
    static let POST = "POST"
    static let GET = "GET"
    static let PUT = "PUT"
    static let DELETE = "DELETE"
}

struct SUBURL {
    
    static let MemberLogin = "member/login"
    static let MobileToken = "mobile/getToken"
    static let MobileSetting = "mobile/settings/getmobilesettings"
    static let LoyalitySetting = "loyalty/viewLoyaltySettings"
    static let DefaultTheme = "theme/getDefaultThemeForAllPages"
    static let UpdateDevice = "member/deviceUpdate"
    static let MemberLogout = "member/logout"
    static let MemberSignup = "member/create"
    static let MemberUpdate = "member/update"
    static let ChangePassword = "member/changePassword"
    static let ForgotPassword = "member/forgetPassMail"
    static let GetmemberProfile = "member/getMember"
    static let PointRewardInfo = "mobile/getPointRewardInfo/"
    static let PointBankoffer = "mobile/getPointBankOffer/"
    static let Getoffers = "mobile/getoffers/"
    static let GetRewards = "mobile/getrewards/"
    static let GetofferByOfferId = "mobile/getOfferByOfferId/"
    static let GetAllRewardOffers = "loyaltyw/getallrewardoffer"
    static let GetLoyaltyCard = "mobile/getLoyaltyCard/"
    static let GetStates = "states"
    static let GetStoreList = "mobile/stores/getstores"
    static let GetStoreDetails = "mobile/stores/getStoreDetails/"
    static let SearchStores = "store/searchStores"
    static let UpdateStore = "member/updateStore"
    static let SubmitEvents = "event/submitallappevents"
    static let GetMenu = "menu/category?storeId="
    static let SubCategoryURLPart1 = "menu/subCategory?storeId="
    static let SubCategoryURLPart2 = "&categoryId="
    static let ProductListURLPart1 = "menu/menuDrawer?storeId="
    static let ProductListURLPart2 = "&categoryId="
    static let ProductListURLPart3 = "&subCategoryId="
    static let productAttributeURLPart1 = "menu/product?storeId="
    static let productAttributeURLPart2 = "&categoryId="
    static let productAttributeURLPart3 = "&subCategoryId="
    static let productAttributeURLPart4 = "&productId="
    static let SignupRuleList = "loyalty/signupRuleList"
    static let Useoffer = "loyalty/useOffer"
    static let RedeemdedOffer = "mobile/redeemed/memberId/offerId"
    static let IncommToken =  "mobile/incomm/token"
    static let IncommRewards = "mobile/incomm/rewards"
    static let IncommOrder = "mobile/incomm/"
    static let GuestRegiter = "event/guestRegister"
    
    
}
