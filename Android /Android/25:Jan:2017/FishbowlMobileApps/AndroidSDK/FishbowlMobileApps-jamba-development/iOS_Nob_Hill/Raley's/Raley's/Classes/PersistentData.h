//
//  PersistentData.h
//  Raley's
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import "Login.h"
#import "Raleys.h"
#import "AccountRequest.h"

@class Login;

@interface PersistentData : NSObject <NSCoding>

@property (nonatomic, strong)NSString *_currentShoppingListId;
@property (nonatomic, assign)long _lastOfferUpdateTime;
@property (nonatomic, assign)long _lastProductCategoryCacheTime;
@property (nonatomic, assign)long _lastPromoCategoryCacheTime;
@property (nonatomic, assign)long _lastMissingImageUpdateTime;
@property (nonatomic, strong)Login *_login;
@property (nonatomic, strong)Raleys *_raleys;
@property (nonatomic, strong)CLLocation *_homeStoreLocation;
@property (nonatomic, strong)NSMutableDictionary *_storeList;

@property (nonatomic, strong)AccountRequest *account;

@end
