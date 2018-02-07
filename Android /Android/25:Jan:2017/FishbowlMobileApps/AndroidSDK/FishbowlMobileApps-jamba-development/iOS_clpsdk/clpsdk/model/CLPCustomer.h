//
//  Customer.h
//  clpsdk
//
//  Created by VT02 on 1/3/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JSONModel.h"
@interface CLPCustomer : JSONModel

@property (assign, nonatomic) long customerID;
@property (assign, nonatomic) long companyID;

@property (strong, nonatomic) NSString          *firstName;
@property (strong, nonatomic) NSString          *lastName;
@property (strong, nonatomic) NSString          *emailID;

@property (strong, nonatomic) NSString          *loginID;
@property (strong, nonatomic) NSString          *loginPassword;
@property (strong, nonatomic) NSString          *loyalityNo;
@property (strong, nonatomic) NSString          *loyalityLevel;
@property (strong, nonatomic) NSString          *homePhone;
@property (strong, nonatomic) NSString          *cellPhone;
@property (strong, nonatomic) NSString          *additionalPhone;
@property (strong, nonatomic) NSString          *addressLine1;
@property (strong, nonatomic) NSString          *addressLine2;
@property (strong, nonatomic) NSString          *addressCity;
@property (strong, nonatomic) NSString          *addressState;
@property (strong, nonatomic) NSString          *addressZip;
@property (strong, nonatomic) NSString          *addressCountry;
@property (strong, nonatomic) NSString          *customerTenantID;
@property (strong, nonatomic) NSString          *customerStatus;
@property (strong, nonatomic) NSString          *lastActivtiy;
@property (strong, nonatomic) NSString          *lastActivityTime;
@property (strong, nonatomic) NSString          *lastLoginTime;
@property (assign, nonatomic) int statusCode;
@property (strong, nonatomic) NSString          *registeredDate;
@property (strong, nonatomic) NSString          *registeredIP;
@property (strong, nonatomic) NSString          *invitationDate;
@property (strong, nonatomic) NSString          *customerGender;
@property (strong, nonatomic) NSString          *dateOfBirth;
@property (strong, nonatomic) NSString          *customerAge;
@property (strong, nonatomic) NSString          *homeStore;
@property (strong, nonatomic) NSString          *homeStoreID;
@property (strong, nonatomic) NSString          *favoriteDepartment;
@property (strong, nonatomic) NSString          *pushOpted;
@property (strong, nonatomic) NSString          *smsOpted;
@property (strong, nonatomic) NSString          *emailOpted;
@property (strong, nonatomic) NSString          *phoneOpted;
@property (strong, nonatomic) NSString          *adOpted;
@property (strong, nonatomic) NSString          *loyalityRewards;
@property (strong, nonatomic) NSString          *createdBy;
@property (strong, nonatomic) NSString          *createdDate;
@property (strong, nonatomic) NSString          *modifiedBy;
@property (strong, nonatomic) NSString          *modifedDate;
@property (assign, nonatomic) long customerDeviceID;
@property (strong, nonatomic) NSString          *deviceID;
@property (strong, nonatomic) NSString          *pushToken;
@property (strong, nonatomic) NSString          *deviceType;
@property (strong, nonatomic) NSString          *deviceOsVersion;
@property (strong, nonatomic) NSString          *deviceVendor;
@property (strong, nonatomic) NSString          *modifiedDate;
@property (strong, nonatomic) NSString          *enabledFlag;
@property (strong, nonatomic) NSString          *status;
@property (strong, nonatomic) NSString          *locationEnabled;
@end
