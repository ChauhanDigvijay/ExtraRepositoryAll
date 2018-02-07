//
//  Store.m
//  Raley's
//
//  Created by Billy Lewis on 9/20/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "Store.h"

@implementation Store

@synthesize storeNumber;
@synthesize address;
@synthesize city;
@synthesize state;
@synthesize zip;
@synthesize longitude;
@synthesize latitude;
@synthesize groceryPhoneNo;
@synthesize chain;
@synthesize ecart;
@synthesize storeList;
@synthesize _distanceFromLocation; // not part of json content

- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"storeList"])
        return @"Store";
    else
        return nil;
}

#pragma mark NSCoding Protocol
- (void)encodeWithCoder:(NSCoder *)encoder;
{
    [encoder encodeInt:[self storeNumber] forKey:@"storeNumber"];
    [encoder encodeObject:[self address] forKey:@"address"];
    [encoder encodeObject:[self city] forKey:@"city"];
    [encoder encodeObject:[self state] forKey:@"state"];
    [encoder encodeObject:[self zip] forKey:@"zip"];
    [encoder encodeObject:[self longitude] forKey:@"longitude"];
    [encoder encodeObject:[self latitude] forKey:@"latitude"];
    [encoder encodeObject:[self groceryPhoneNo] forKey:@"groceryPhoneNo"];
    [encoder encodeObject:[self chain] forKey:@"chain"];
    [encoder encodeObject:[self ecart] forKey:@"ecart"];
}

- (id)initWithCoder:(NSCoder *)decoder;
{
    self = [super init];

    if(self == nil)
        return nil;

    self.storeNumber = [decoder decodeIntForKey:@"storeNumber"];
    self.address = [decoder decodeObjectForKey:@"address"];
    self.city = [decoder decodeObjectForKey:@"city"];
    self.state = [decoder decodeObjectForKey:@"state"];
    self.zip = [decoder decodeObjectForKey:@"zip"];
    self.longitude = [decoder decodeObjectForKey:@"longitude"];
    self.latitude = [decoder decodeObjectForKey:@"latitude"];
    self.groceryPhoneNo = [decoder decodeObjectForKey:@"groceryPhoneNo"];
    self.chain = [decoder decodeObjectForKey:@"chain"];
    self.ecart = [decoder decodeObjectForKey:@"ecart"];

    return self;
}

@end
