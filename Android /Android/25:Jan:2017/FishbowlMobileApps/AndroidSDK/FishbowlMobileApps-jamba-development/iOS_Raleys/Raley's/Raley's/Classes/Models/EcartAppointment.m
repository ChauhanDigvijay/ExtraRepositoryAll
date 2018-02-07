//
//  EcartAppointment.m
//  Raley's
//
//  Created by Bill Lewis on 3/18/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "EcartAppointment.h"

@implementation EcartAppointment

@synthesize storeNumber;
@synthesize appointmentDate;
@synthesize appointmentTimeList;

- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"appointmentTimeList"])
        return @"NSString";
    else
        return nil;
}

@end
