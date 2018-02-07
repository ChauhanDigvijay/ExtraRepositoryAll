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
    id  m_callBackTarget;
    SEL m_callBackSelector;
}

+ (id)sharedManager;

#pragma mark Custom Methods

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

 // getPromocode api method
-(void)getPromocode:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

 // getOfferByOfferId api method
-(void)getuserOffer:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

 // PassOpen api method
-(void)PassOpen:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

 // getAllStores api method
-(void)getAllStores:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;

 // updateDevice api method
-(void)updateDevice:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;


@end
