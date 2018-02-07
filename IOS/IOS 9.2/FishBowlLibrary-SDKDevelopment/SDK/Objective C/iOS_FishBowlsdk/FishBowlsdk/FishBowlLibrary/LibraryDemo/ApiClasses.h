//
//  ApiClasses.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 23/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ModelClass.h"

@interface ApiClasses : NSObject
{
    id m_callBackTarget;
    SEL m_callBackSelector;
}

+ (id)sharedManager;


//===================================== GetToken Api =============================================//


#pragma mark - set getTokenApi method
-(void)getTokenApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


//======================================== Guest User ==========================================//

#pragma mark - GuestUser
-(void)GuestUserAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// ===================================== Basic app Api ======================================= //

  #pragma mark - login 
-(void)loginAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


 #pragma mark - registration
-(void)signupAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


 #pragma mark - logout
-(void)logoutAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark - get member
-(void)getMember:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark - update profile
-(void)updateMember:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark - changepassword
-(void)changePassword:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark - ForgotPassword api method
-(void)ForgotPasswordApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// ===================================== Mobile Setting Api ==================================== //

#pragma mark - mobileSetting
-(void)mobileSettingAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// =================================== Offer And Rewards Api ==================================== //


#pragma mark - getoffer api method
-(void)getOffers:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

#pragma mark - getRewards api method
-(void)getRewards:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

#pragma mark - getPromocode api method
-(void)getPromocode:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

#pragma mark - getOfferByOfferId api method
-(void)getuserOffer:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

#pragma mark -  getallrewardofferApi API
-(void)getallrewardofferApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

#pragma mark -  redeemed API
-(void)redeemedApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// =================================== getLoyalityPoints Api  ==================================== //


#pragma mark - getLoyalityPoints api method
-(void)getLoyalityPoints:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// ===================================== Pass Open Api ========================================= //

 #pragma mark - PassOpen api method
-(void)PassOpen:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// ==================================  Store & State Api ========================================//

#pragma mark - getAllStores api method
-(void)getAllStores:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

#pragma mark - storeSearch api method
-(void)storeSearchApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

#pragma mark - set favourit store  api method
-(void)favouritStorehApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark - getStoreDetails method
-(void)getStoreDetails:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

#pragma mark - stateSearch method
-(void)stateSearch:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// ==================================== Update Device Api =====================================//

#pragma mark - updateDevice api method
-(void)updateDevice:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// ======================================  FaceBook Api  ====================================== //

#pragma mark - set faceBookregisterAPI method
-(void)faceBookregisterAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// ===================================== Claim Offer Api ====================================== //

#pragma mark - getPointBankOffer api method
-(void)getPointBankOffer:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark - useOffer api method
-(void)useOffer:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


 //====================================== signupRuleList Method ===================================//
                            // ============= Bonus Api ============= //

#pragma mark - signupRuleList Method
-(void)signupRuleList:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


 //====================================== menuApi Method ====================================== //

 #pragma mark - menu_Family api method
-(void)menuFamily:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


 #pragma mark -  Get_Category api method
-(void)getCategory:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


 #pragma mark -  Get_SubCategory api method
-(void)SubCategory:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


 #pragma mark -  Get Product List(Menu) API
-(void)ProductList:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


 #pragma mark -  Get Product Attributes API
-(void)ProductAttributes:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark -  Get Category with relevant Subcategory API
-(void)categorySubCategory:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark -  categoryProductList API
-(void)categoryProductList:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// ======================================== mobile events =======================================  //


#pragma mark -  mobileAppEvents API
-(void)mobileAppEvents:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

@end
