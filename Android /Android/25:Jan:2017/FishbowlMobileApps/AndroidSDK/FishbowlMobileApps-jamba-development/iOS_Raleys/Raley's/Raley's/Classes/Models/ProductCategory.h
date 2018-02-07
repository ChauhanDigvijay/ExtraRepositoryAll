//
//  ProductCategory.h
//  Raley's
//
//  Created by Bill Lewis on 11/1/13.
//  Copyright (c) 2013 Bill Lewis. All rights reserved.
//

#import "JsonObject.h"

@interface ProductCategory : JsonObject <NSCoding>

@property (nonatomic, assign)int productCategoryId;
@property (nonatomic, assign)int parentCategoryId;
@property (nonatomic, assign)int grandParentCategoryId;
@property (nonatomic, strong)NSString *parentCategoryName;
@property (nonatomic, strong)NSString *grandParentCategoryName;
@property (nonatomic, strong)NSString *name;
@property (nonatomic, strong)NSString *type;
@property (nonatomic, assign)int level;
@property (nonatomic, assign)int priority;
@property (nonatomic, strong)NSArray *subCategoryList;
@property (nonatomic, strong)NSArray *categoryList;

- (id)initWithName:(NSString *)categoryName categoryId:(int)categoryId categoryLevel:(int)level;

@end
