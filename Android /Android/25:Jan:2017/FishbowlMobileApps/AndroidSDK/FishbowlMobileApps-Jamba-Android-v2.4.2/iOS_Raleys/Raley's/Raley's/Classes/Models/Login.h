//
//  Login.h
//  Raley's
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"
#import "AppDelegate.h"

@interface Login : JsonObject

@property (nonatomic, strong)NSString *login;
@property (nonatomic, strong)NSString *accountId;
@property (nonatomic, strong)NSString *authKey;
@property (nonatomic, strong)NSString *displayName;
@property (nonatomic, strong)NSString *deviceId;
@property (nonatomic, strong)NSString *firstName;
@property (nonatomic, strong)NSString *lastName;
@property (nonatomic, strong)NSString *email;
@property (nonatomic, strong)NSString *password;
@property (nonatomic, strong)NSString *crmNumber;
@property (nonatomic, assign)int      storeNumber;
@property (nonatomic, strong)NSString *pointsBalance;

@end
