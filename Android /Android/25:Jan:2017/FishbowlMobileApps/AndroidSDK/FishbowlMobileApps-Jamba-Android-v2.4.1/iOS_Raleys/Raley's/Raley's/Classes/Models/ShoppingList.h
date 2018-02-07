//
//  ShoppingList.h
//  Raley's
//
//  Created by Bill Lewis on 12/2/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface ShoppingList : JsonObject

@property (nonatomic, strong)NSString  *listId;
@property (nonatomic, strong)NSString  *name;
@property (nonatomic, assign)int       storeNumber;
@property (nonatomic, strong)NSString  *accountId;
@property (nonatomic, strong)NSString  *listType;
@property (nonatomic, strong)NSString  *modifiedBy;
@property (nonatomic, assign)float     totalPrice;
@property (nonatomic, assign)float     totalCrv;
@property (nonatomic, strong)NSNumber  *serverUpdateTime;
@property (nonatomic, assign)BOOL      productListReturned;
@property (nonatomic, strong)NSMutableArray  *productList;

@end
