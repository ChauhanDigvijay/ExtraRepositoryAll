//
//  FBGuest.m
//  clpsdk
//
//  Created by Puneet  on 6/23/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "FBGuest.h"

@implementation FBGuest

@synthesize firstName,deviceId,pushToken,deviceType,deviceOsVersion;


-(id)initWithCoder:(NSCoder *)aDecoder{
    
    self = [super init];
    
    if(self==nil)return nil;
    
    self.firstName =       [aDecoder decodeObjectForKey:@"firstName"];
    self.deviceId =        [aDecoder decodeObjectForKey:@"deviceID"];
    self.pushToken =       [aDecoder decodeObjectForKey:@"pushToken"];
    self.deviceType =      [aDecoder decodeObjectForKey:@"deviceType"];
    self.deviceOsVersion = [aDecoder decodeObjectForKey:@"deviceOsVersion"];
    self.memberid =        [aDecoder decodeObjectForKey:@"memberid"];
    self.tenantid =        [aDecoder decodeObjectForKey:@"tenantid"];
    self.successFlag =     [aDecoder decodeBoolForKey:@"successFlag"];
    self.appId    =        [aDecoder decodeObjectForKey:@"appId"];
    
    return self;
}


-(void)encodeWithCoder:(NSCoder *)aCoder{
    
    [aCoder encodeObject:self.firstName forKey:@"firstName"];
    [aCoder encodeObject:self.deviceId forKey:@"deviceID"];
    [aCoder encodeObject:self.pushToken forKey:@"pushToken"];
    [aCoder encodeObject:self.deviceType forKey:@"deviceType"];
    [aCoder encodeObject:self.deviceOsVersion forKey:@"deviceOsVersion"];
    [aCoder encodeObject:self.memberid forKey:@"memberid"];
    [aCoder encodeObject:self.tenantid forKey:@"tenantid"];
    [aCoder encodeBool:self.successFlag forKey:@"successFlag"];
    [aCoder encodeObject:self.appId forKey:@"appId"];


}




@end
