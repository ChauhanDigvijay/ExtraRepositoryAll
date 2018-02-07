//
//  ListDeleteItemRequest.h
//  Raley's
//
//  Created by Bill Lewis on 12/6/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface ListDeleteItemRequest : JsonObject

@property (nonatomic, strong)NSString *accountId;
@property (nonatomic, strong)NSString *listId;
@property (nonatomic, strong)NSString *sku;
@property (nonatomic, strong)NSNumber *appListUpdateTime;
@property (nonatomic, strong)NSNumber *returnCurrentList;

@end
