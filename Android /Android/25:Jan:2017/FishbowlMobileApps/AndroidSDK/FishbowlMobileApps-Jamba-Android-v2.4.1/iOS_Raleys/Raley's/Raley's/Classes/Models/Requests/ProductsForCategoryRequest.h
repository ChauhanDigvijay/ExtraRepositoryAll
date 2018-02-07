//
//  ProductsForCategoryRequest.h
//  Raley's
//
//  Created by Bill Lewis on 11/7/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface ProductsForCategoryRequest : JsonObject

@property (nonatomic, assign)int      storeNumber;
@property (nonatomic, assign)int      cat1Id;
@property (nonatomic, assign)int      cat2Id;
@property (nonatomic, assign)int      cat3Id;
@property (nonatomic, strong)NSString *searchText;
@property (nonatomic, assign)int      startIndex;
@property (nonatomic, assign)int      count;

@end
