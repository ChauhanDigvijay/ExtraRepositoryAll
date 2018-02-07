//
//  ShoppingListName.h
//  Raley's
//
//  Created by Bill Lewis on 12/2/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface ShoppingListName : JsonObject

@property (nonatomic, strong)NSString *listId;
@property (nonatomic, strong)NSString *name;
@property (nonatomic, strong)NSArray *nameList;

@end
