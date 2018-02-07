//
//  LoginRequest.h
//  Raley's
//
//  Created by Bill Lewis on 11/22/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface LoginRequest : JsonObject

@property (nonatomic, strong)NSString *email;
@property (nonatomic, strong)NSString *password;
@property (nonatomic, strong)NSString *platform;
@property (nonatomic, strong)NSString *platformVersion;

@end
