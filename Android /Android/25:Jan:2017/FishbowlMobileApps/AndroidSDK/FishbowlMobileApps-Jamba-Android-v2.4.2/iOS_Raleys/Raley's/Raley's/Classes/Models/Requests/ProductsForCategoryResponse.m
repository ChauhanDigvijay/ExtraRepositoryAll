//
//  ProductsForCategoryResponse.m
//  Raley's
//
//  Created by Bill Lewis on 3/3/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "ProductsForCategoryResponse.h"

@implementation ProductsForCategoryResponse

@synthesize productsInCategoryCount;
@synthesize responseStartIndex;
@synthesize responseCount;
@synthesize productList;


- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"productList"])
        return @"Product";
    else
        return nil;
}

@end
