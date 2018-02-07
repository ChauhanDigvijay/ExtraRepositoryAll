//
//  Login.m
//  Raley's
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "Login.h"

@implementation Login

@synthesize login;
@synthesize accountId;
@synthesize authKey;
@synthesize displayName;
@synthesize deviceId;
@synthesize firstName;
@synthesize lastName;
@synthesize email;
@synthesize password;
@synthesize crmNumber;
@synthesize storeNumber;
@synthesize pointsBalance;

#pragma mark NSCoding Protocol
- (void)encodeWithCoder:(NSCoder *)encoder;
{
    [encoder encodeObject:[self login] forKey:@"login"];
    [encoder encodeObject:[self accountId] forKey:@"accountId"];
    [encoder encodeObject:[self authKey] forKey:@"authKey"];
    [encoder encodeObject:[self displayName] forKey:@"displayName"];
    [encoder encodeObject:[self deviceId] forKey:@"deviceId"];
    [encoder encodeObject:[self firstName] forKey:@"firstName"];
    [encoder encodeObject:[self lastName] forKey:@"lastName"];
    [encoder encodeObject:[self email] forKey:@"email"];
    [encoder encodeObject:[self password] forKey:@"password"];
    [encoder encodeObject:[self crmNumber] forKey:@"crmNumber"];
    [encoder encodeInteger:[self storeNumber] forKey:@"storeNumber"];
    [encoder encodeObject:[self pointsBalance] forKey:@"pointsBalance"];
}

- (id)initWithCoder:(NSCoder *)decoder;
{
    self = [super init];

    if(self == nil)
        return nil;
    
    self.login = [decoder decodeObjectForKey:@"login"];
    self.accountId = [decoder decodeObjectForKey:@"accountId"];
    self.authKey = [decoder decodeObjectForKey:@"authKey"];
    self.displayName = [decoder decodeObjectForKey:@"displayName"];
    self.deviceId = [decoder decodeObjectForKey:@"deviceId"];
    self.firstName = [decoder decodeObjectForKey:@"firstName"];
    self.lastName = [decoder decodeObjectForKey:@"lastName"];
    self.email = [decoder decodeObjectForKey:@"email"];
    self.password = [decoder decodeObjectForKey:@"password"];
    self.crmNumber = [decoder decodeObjectForKey:@"crmNumber"];
    self.storeNumber = (int)[decoder decodeIntegerForKey:@"storeNumber"];
    self.pointsBalance = [decoder decodeObjectForKey:@"pointsBalance"];

    return self;
}


@end
