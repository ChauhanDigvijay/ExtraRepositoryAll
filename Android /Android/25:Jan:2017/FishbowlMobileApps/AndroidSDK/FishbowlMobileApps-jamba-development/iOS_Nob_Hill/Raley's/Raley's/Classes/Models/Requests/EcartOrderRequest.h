//
//  EcartOrderRequest.h
//  Raley's
//
//  Created by Bill Lewis on 3/13/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface EcartOrderRequest : JsonObject

@property (nonatomic, strong)NSString *accountId;
@property (nonatomic, assign)int      storeNumber;
@property (nonatomic, strong)NSString *listId;
@property (nonatomic, strong)NSString *substitutionPreferenceName;
@property (nonatomic, assign)int substitutionPreference;
@property (nonatomic, strong)NSString *bagPreferenceName;
@property (nonatomic, assign)int bagPreference;
@property (nonatomic, strong)NSString *appointmentDay;
@property (nonatomic, strong)NSString *appointmentTime;
@property (nonatomic, strong)NSString *instructions;
@property (nonatomic, strong)NSNumber *orderId;

@end
