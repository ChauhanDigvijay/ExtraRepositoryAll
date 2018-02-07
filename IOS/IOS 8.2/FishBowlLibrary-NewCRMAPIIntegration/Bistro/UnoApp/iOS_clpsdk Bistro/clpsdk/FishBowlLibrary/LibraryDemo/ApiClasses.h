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

#pragma mark Custom Methods


-(NSString *)deviceID;


 // login
-(void)loginAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

 // registration
-(void)registerAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

 // mobileSetting
-(void)mobileSettingAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

 // logout
-(void)logoutAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

 // changepassword
-(void)changePassword:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// get member
-(void)getMember:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

 // update profile
-(void)updateProfile:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// getoffer api method
-(void)getOffers:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// getRewards api method
-(void)getRewards:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// getLoyalityPoints api method
-(void)getLoyalityPoints:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// getPromocode api method
-(void)getPromocode:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// getOfferByOfferId api method
-(void)getuserOffer:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// PassOpen api method
-(void)PassOpen:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// getAllStores api method
-(void)getAllStores:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// getStoreDetail api method
-(void)getStoreDetailApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// updateDevice api method
-(void)updateDevice:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// ForgotPassword api method
-(void)ForgotPasswordApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// storeSearch api method
-(void)storeSearchApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// set favourit store  api method
-(void)favouritStorehApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// set getTokenApi method
-(void)getTokenApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

// set faceBookregisterAPI method
-(void)faceBookregisterAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// stateSearch method
-(void)stateSearch:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// Country method
-(void)countryApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// Loyality Card Method
-(void)LoyaltyCardApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;



// LoyaltyActivityApi Method
-(void)LoyaltyActivityApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// signupRuleList Method
-(void)signupRuleList:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// inAppAd Method
-(void)inAppAd:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// getLoyaltyMessageType Method
-(void)getLoyaltyMessageType:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// getLoyaltyAreaType Method
-(void)getLoyaltyAreaType:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// Loyalty message Method
-(void)messages:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


// email Api
-(void)emailApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark -  Menu Api method
-(void)menuApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark -  Get_SubCategory api method
-(void)SubCategory:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark -  Get Product List(Menu) API method
-(void)ProductList:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark -  Get ProductAttributes API method
-(void)ProductAttributes:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark -  Get getallrewardoffer API method

-(void)getallrewardoffer:(NSDictionary* )dictValue url:(NSString* )baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark -  UseOffer API method
-(void)useOffer:(NSDictionary* )dictValue url:(NSString* )baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


#pragma mark -  UseOffer API method
-(void)sms:(NSDictionary* )dictValue url:(NSString* )baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

# pragma mark - MobileSetting Api

-(void)ThemeSettingsAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;



@end
