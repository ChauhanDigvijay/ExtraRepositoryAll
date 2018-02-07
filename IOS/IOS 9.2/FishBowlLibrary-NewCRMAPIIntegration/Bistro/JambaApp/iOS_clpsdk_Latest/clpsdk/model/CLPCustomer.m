//
//  Customer.m
//  clpsdk
//
//  Created by VT02 on 1/3/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//

#import "CLPCustomer.h"

@implementation CLPCustomer

-(id)initWithCoder:(NSCoder *)aDecoder{
    
    self = [super init];
    
    if(self==nil)return nil;
    
    self.customerID = [aDecoder decodeObjectForKey:@"customerID"];
    self.companyID = [aDecoder decodeIntegerForKey:@"companyID"];
    self.firstName = [aDecoder decodeObjectForKey:@"firstName"];
    self.lastName = [aDecoder decodeObjectForKey:@"lastName"];
    self.email = [aDecoder decodeObjectForKey:@"email"];
    self.loginID = [aDecoder decodeObjectForKey:@"loginID"];
    self.password = [aDecoder decodeObjectForKey:@"password"];
    self.loyalityNo = [aDecoder decodeObjectForKey:@"loyalityNo"];
    self.loyalityLevel = [aDecoder decodeObjectForKey:@"loyalityLevel"];
    self.homePhone = [aDecoder decodeObjectForKey:@"homePhone"];
    self.phone = [aDecoder decodeObjectForKey:@"phone"];
    self.additionalPhone = [aDecoder decodeObjectForKey:@"additionalPhone"];
    self.addressLine1 = [aDecoder decodeObjectForKey:@"addressLine1"];
    self.addressLine2 = [aDecoder decodeObjectForKey:@"addressLine2"];
    self.addressCity = [aDecoder decodeObjectForKey:@"addressCity"];
    self.addressState = [aDecoder decodeObjectForKey:@"addressState"];
    self.addressZip = [aDecoder decodeObjectForKey:@"addressZip"];
    self.addressCountry = [aDecoder decodeObjectForKey:@"addressCountry"];
    self.customerTenantID = [aDecoder decodeObjectForKey:@"customerTenantID"];
    self.customerStatus = [aDecoder decodeObjectForKey:@"customerStatus"];
    self.lastActivtiy = [aDecoder decodeObjectForKey:@"lastActivtiy"];
    self.lastActivityTime = [aDecoder decodeObjectForKey:@"lastActivityTime"];
    self.lastLoginTime = [aDecoder decodeObjectForKey:@"lastLogime"];
    self.statusCode = [aDecoder decodeIntForKey:@"statusCode"];
    self.registeredDate = [aDecoder decodeObjectForKey:@"registeredDate"];
    self.registeredIP = [aDecoder decodeObjectForKey:@"registeredIP"];
    self.invitationDate = [aDecoder decodeObjectForKey:@"invitationDate"];
    self.customerGender = [aDecoder decodeObjectForKey:@"customerGender"];
    self.birthDate = [aDecoder decodeObjectForKey:@"birthDate"];
    self.customerAge = [aDecoder decodeObjectForKey:@"customerAge"];
    self.storeCode = [aDecoder decodeObjectForKey:@"storeCode"];
    self.favoriteStore = [aDecoder decodeObjectForKey:@"favoriteStore"];
    self.pushOpted = [aDecoder decodeObjectForKey:@"pushOpted"];
    self.smsOpted = [aDecoder decodeObjectForKey:@"smsOpted"];
    self.emailOpted = [aDecoder decodeObjectForKey:@"emailOpted"];
    self.phoneOpted = [aDecoder decodeObjectForKey:@"phoneOpted"];
    self.adOpted = [aDecoder decodeObjectForKey:@"adOpted"];
        self.loyalityRewards = [aDecoder decodeObjectForKey:@"loyalityRewards"];
        self.createdBy = [aDecoder decodeObjectForKey:@"createdBy"];
        self.createdDate = [aDecoder decodeObjectForKey:@"createdDate"];
        self.modifiedBy = [aDecoder decodeObjectForKey:@"modifiedBy"];
        self.modifedDate = [aDecoder decodeObjectForKey:@"modifedDate"];
        self.customerDeviceID = [aDecoder decodeIntegerForKey:@"customerDeviceID"];
    self.deviceId = [aDecoder decodeObjectForKey:@"deviceId"];
    self.pushToken = [aDecoder decodeObjectForKey:@"pushToken"];
    self.deviceType = [aDecoder decodeObjectForKey:@"deviceType"];
    self.deviceOSVersion = [aDecoder decodeObjectForKey:@"deviceOSVersion"];
    self.deviceVendor = [aDecoder decodeObjectForKey:@"deviceVendor"];
    self.modifiedDate = [aDecoder decodeObjectForKey:@"modifiedDate"];
    self.enabledFlag = [aDecoder decodeObjectForKey:@"enabledFlag"];
    self.status = [aDecoder decodeObjectForKey:@"status"];
    
     self.username = [aDecoder decodeObjectForKey:@"username"];
    self.addressStreet = [aDecoder decodeObjectForKey:@"addressStreet"];

    
    return self;
}

-(void)encodeWithCoder:(NSCoder *)aCoder{
    
    [aCoder encodeObject:self.username forKey:@"username"];
    [aCoder encodeObject:self.addressStreet forKey:@"addressStreet"];
    
    [aCoder encodeObject:self.customerID forKey:@"customerID"];
    [aCoder encodeInteger:self.companyID forKey:@"companyID"];
    [aCoder encodeObject:self.firstName forKey:@"firstName"];
    [aCoder encodeObject:self.lastName forKey:@"lastName"];
   [aCoder encodeObject:self.email forKey:@"email"];
    [aCoder encodeObject:self.loginID forKey:@"loginID"];
    [aCoder encodeObject:self.password forKey:@"password"];
    [aCoder encodeObject:self.loyalityNo forKey:@"loyalityNo"];
    [aCoder encodeObject:self.loyalityLevel forKey:@"loyalityLevel"];
    [aCoder encodeObject:self.homePhone forKey:@"homePhone"];
    [aCoder encodeObject:self.phone forKey:@"phone"];
    [aCoder encodeObject:self.additionalPhone forKey:@"additionalPhone"];
    [aCoder encodeObject:self.addressLine1 forKey:@"addressLine1"];
    [aCoder encodeObject:self.addressLine2 forKey:@"addressLine2"];
    [aCoder encodeObject:self.addressCity forKey:@"addressCity"];
    [aCoder encodeObject:self.addressState forKey:@"addressState"];
    [aCoder encodeObject:self.addressZip forKey:@"addressZip"];
    [aCoder encodeObject:self.addressCountry forKey:@"addressCountry"];
    [aCoder encodeObject:self.customerTenantID forKey:@"customerTenantID"];
    [aCoder encodeObject:self.customerStatus forKey:@"customerStatus"];
    [aCoder encodeObject:self.lastActivtiy forKey:@"lastActivtiy"];
    [aCoder encodeObject:self.lastActivityTime forKey:@"lastActivityTime"];
    [aCoder encodeObject:self.lastLoginTime forKey:@"lastLoginTime"];
    [aCoder encodeInt:self.statusCode forKey:@"statusCode"];
    [aCoder encodeObject:self.registeredDate forKey:@"registeredDate"];
    [aCoder encodeObject:self.registeredIP forKey:@"registeredIP"];
    [aCoder encodeObject:self.invitationDate forKey:@"invitationDate"];
    [aCoder encodeObject:self.customerGender forKey:@"customerGender"];
    [aCoder encodeObject:self.birthDate forKey:@"birthDate"];
    [aCoder encodeObject:self.customerAge forKey:@"customerAge"];
    [aCoder encodeObject:self.storeCode forKey:@"storeCode"];
    [aCoder encodeObject:self.favoriteStore forKey:@"favoriteStore"];
    [aCoder encodeObject:self.pushOpted forKey:@"pushOpted"];
    [aCoder encodeObject:self.smsOpted forKey:@"smsOpted"];
    [aCoder encodeObject:self.emailOpted forKey:@"emailOpted"];
    [aCoder encodeObject:self.phoneOpted forKey:@"phoneOpted"];
    [aCoder encodeObject:self.adOpted forKey:@"adOpted"];
    [aCoder encodeObject:self.loyalityRewards forKey:@"loyalityRewards"];
    [aCoder encodeObject:self.createdBy forKey:@"createdBy"];
    [aCoder encodeObject:self.createdDate forKey:@"createdDate"];
    [aCoder encodeObject:self.modifiedBy forKey:@"modifiedBy"];
    [aCoder encodeObject:self.modifedDate forKey:@"modifedDate"];
    [aCoder encodeInteger:self.customerDeviceID forKey:@"customerDeviceID"];
    [aCoder encodeObject:self.deviceId forKey:@"deviceId"];
    [aCoder encodeObject:self.pushToken forKey:@"pushToken"];
    [aCoder encodeObject:self.deviceType forKey:@"deviceType"];
    [aCoder encodeObject:self.deviceOSVersion forKey:@"deviceOSVersion"];
    [aCoder encodeObject:self.deviceVendor forKey:@"deviceVendor"];
    [aCoder encodeObject:self.modifiedDate forKey:@"modifiedDate"];
    [aCoder encodeObject:self.enabledFlag forKey:@"enabledFlag"];
    [aCoder encodeObject:self.status forKey:@"status"];
}

@end
