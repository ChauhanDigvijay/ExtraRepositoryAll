//
//  ShoppingListName.m
//  Raley's
//
//  Created by Bill Lewis on 12/2/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ShoppingListName.h"

@implementation ShoppingListName

@synthesize listId;
@synthesize name;
@synthesize nameList;

- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"nameList"])
        return @"ShoppingList";
    else
        return nil;
}

@end
