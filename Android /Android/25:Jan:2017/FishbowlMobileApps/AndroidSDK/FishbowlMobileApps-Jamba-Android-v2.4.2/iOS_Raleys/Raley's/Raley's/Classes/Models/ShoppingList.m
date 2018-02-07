//
//  ShoppingList.m
//  Raley's
//
//  Created by Bill Lewis on 12/2/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ShoppingList.h"

@implementation ShoppingList

@synthesize listId;
@synthesize name;
@synthesize storeNumber;
@synthesize accountId;
@synthesize listType;
@synthesize modifiedBy;
@synthesize totalPrice;
@synthesize totalCrv;
@synthesize serverUpdateTime;
@synthesize productListReturned;
@synthesize productList;

- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"productList"])
        return @"Product";
    else
        return nil;
}

@end
