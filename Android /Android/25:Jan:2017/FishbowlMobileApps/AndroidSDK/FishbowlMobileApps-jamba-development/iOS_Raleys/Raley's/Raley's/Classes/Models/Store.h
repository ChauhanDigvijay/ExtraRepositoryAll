//
//  Store.h
//  Raley's
//
//  Created by Billy Lewis on 9/20/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface Store : JsonObject <NSCoding>

@property (nonatomic, assign)int      storeNumber;
@property (nonatomic, strong)NSString *address;
@property (nonatomic, strong)NSString *city;
@property (nonatomic, strong)NSString *state;
@property (nonatomic, strong)NSString *zip;
@property (nonatomic, strong)NSString *longitude;
@property (nonatomic, strong)NSString *latitude;
@property (nonatomic, strong)NSString *groceryPhoneNo;
@property (nonatomic, strong)NSString *chain;
@property (nonatomic, strong)NSString *ecart;
@property (nonatomic, strong)NSArray  *storeList;
@property (nonatomic, assign)double   _distanceFromLocation; // not part of json content

@end
