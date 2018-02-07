//
//  ProductsForCategoryResponse.h
//  Raley's
//
//  Created by Bill Lewis on 3/3/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface ProductsForCategoryResponse : JsonObject

@property (nonatomic, assign)int productsInCategoryCount;
@property (nonatomic, assign)int responseStartIndex;
@property (nonatomic, assign)int responseCount;
@property (nonatomic, strong)NSArray  *productList;

@end
