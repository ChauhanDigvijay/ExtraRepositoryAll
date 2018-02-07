//
//  EcartPreOrderResponse.m
//  Raley's
//
//  Created by Bill Lewis on 3/18/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "EcartPreOrderResponse.h"

@implementation EcartPreOrderResponse

@synthesize accountId;
@synthesize listId;
@synthesize sePoints;
@synthesize productPrice;
@synthesize salesTax;
@synthesize crv;
@synthesize fees;
@synthesize totalPrice;
@synthesize appointmentList;

- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"appointmentList"])
        return @"EcartAppointment";
    else
        return nil;
}

@end
