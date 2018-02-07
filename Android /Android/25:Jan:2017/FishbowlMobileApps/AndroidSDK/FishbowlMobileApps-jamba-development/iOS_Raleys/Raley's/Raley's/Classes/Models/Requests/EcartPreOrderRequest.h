//
//  EcartPreOrderRequest.h
//  Raley's
//
//  Created by Bill Lewis on 3/18/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface EcartPreOrderRequest : JsonObject

@property (nonatomic, strong)NSString *accountId;
@property (nonatomic, assign)int      storeNumber;
@property (nonatomic, strong)NSString *listId;

@end
