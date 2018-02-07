//
//  SDKConstants.swift
//  FishBowlLibrary
//
//  Created by Puneet  on 17/07/17.
//  Copyright © 2017 Fishbowl. All rights reserved.
//

import UIKit



struct APIHeaderkey {
    
    static let AccessToken = "access_token"
    static let ContentType = "Content-Type"
    static let ClientId = "client_id"
    static let Application = "Application"
    static let TenantId = "tenantId"
    static let DeviceId = "deviceId"
    static let ClientSecret = "client_secret"
    static let TenantName = "tenantName"
    static let Signature = "X-Class-Signature"
    static let Key = "X-Class-Key"
    static let ExternalCustomerId = "ExternalCustomerId"
    static let ExternalAccessToken = "externalAccessToken"
    static let SpendGoApiBaseUrl = "SpendGoApiBaseUrl"
    
}




struct MethodType {
    
    static let POST = "POST"
    static let GET = "GET"
    static let PUT = "PUT"
    static let DELETE = "DELETE"
}

struct SUBURL {
    
    static let MemberLogin = "member/login"
    static let ExternalMemberSignup = "member/create"
    static let ExternalMemberLogin = "member/loginSDK"
    static let GetToken = "mobile/getToken"
    static let MobileSetting = "mobile/settings/getmobilesettings"
    static let LoyalitySetting = "loyalty/viewLoyaltySettings"
    static let DefaultTheme = "theme/getDefaultThemeForAllPages"
    static let UpdateDevice = "member/deviceUpdate"
    static let MemberLogout = "member/logout"
    static let MemberSignup = "member/create"
    static let MemberUpdate = "member/update"
    static let ChangePassword = "member/changePassword"
    static let ForgotPassword = "member/forgetPassMail"
    static let GetMemberProfile = "member/getMember"
    static let PointRewardInfo = "mobile/getPointRewardInfo/"
    static let PointBankoffer = "mobile/getPointBankOffer/"
    static let Getoffers = "mobile/getoffers/"
    static let GetRewards = "mobile/getrewards/"
    static let GetOfferByOfferId = "mobile/getOfferByOfferId/"
    static let PassOpen = "mobile/passOpen/"
    static let GetAllRewardOffers = "loyaltyw/getallrewardoffer"
    static let GetStates = "states"
    static let GetStoreList = "mobile/stores/getstores"
    static let GetStoreDetails = "mobile/stores/getStoreDetails/"
    static let SearchStores = "store/searchStores"
    static let FavStoreUpdate = "member/updateStore"
    static let RegisterUserMobileEvents = "event/submitallappevents"
    static let GuestUserMobileEvents  = "event/guestappevents"
    static let GetMenuCategory = "menu/category?storeId="
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
    static let IncommToken =  "mobile/incomm/token"
    static let IncommOrderValue = "mobile/incomm/rewards"
    static let IncommOrderId = "mobile/incomm/orderId"
    static let getMemberbyExternalMemberId = "member/getMemberByExternalCustomerId/"
    static let CreateNewMember  =   "v1/fbmember/create"
    static let GetMemberDetails  =   "v1/fbmember/getMember"
    static let UpdateMemberDetails = "v1/fbmember/update"
    static let MemberUpdateByEmail = "member/createUpdateByEmail"

    
}


struct BodyParameter {
    
    static let StoreId = "storeid"
    static let CategoryId = "categoryid"
    static let SubCategoryId = "subCategoryid"
    static let ProductId = "productid"
    static let OrderId  = "orderid"
    static let SpendgoId = "spendgoid"
    static let OfferId  = "offerid"
    static let CustomerId  = "customerid"


}









