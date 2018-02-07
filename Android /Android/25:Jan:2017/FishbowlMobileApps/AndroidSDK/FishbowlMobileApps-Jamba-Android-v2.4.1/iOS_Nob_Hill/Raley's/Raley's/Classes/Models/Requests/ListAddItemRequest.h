//
//  ListAddItemRequest.h
//  Raley's
//
//  Created by Bill Lewis on 12/3/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface ListAddItemRequest : JsonObject

@property (nonatomic, strong)NSString *accountId;
@property (nonatomic, strong)NSString *listId;
@property (nonatomic, strong)NSString *sku;
@property (nonatomic, assign)int      qty;
@property (nonatomic, assign)float    weight;
@property (nonatomic, strong)NSString *customerComment;
@property (nonatomic, strong)NSString *purchaseBy;
//@property (nonatomic, strong)NSNumber *appListUpdateTime;
@property (nonatomic, strong)NSString *appListUpdateTime; // my code
//@property (nonatomic, strong)NSNumber *returnCurrentList;
@property (nonatomic, strong)NSString *returnCurrentList;//my code

@end
