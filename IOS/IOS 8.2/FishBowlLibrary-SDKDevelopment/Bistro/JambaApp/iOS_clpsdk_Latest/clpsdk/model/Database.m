//
//  Database.m
//  clpsdk
//
//  Created by VT001 on 29/01/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//

#import "Database.h"

#define customer_key @"CLPCustomer"

@implementation Database
@synthesize customer;

#pragma mark NSCoding Protocol

-(id)initWithCoder:(NSCoder *)aDecoder{
    self = [super init];
    
    if(self==nil)return nil;
    
    self.customer = [aDecoder decodeObjectForKey:customer_key];
    
    return self;
}

-(void)encodeWithCoder:(NSCoder *)aCoder{
    [aCoder encodeObject:customer forKey:customer_key];
}

@end