//
//  AccountRequest.h
//  Raley's
//
//  Created by Billy Lewis on 10/6/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface AccountRequest : JsonObject

@property (nonatomic, strong)NSString *crmNumber;
@property (nonatomic, strong)NSString *accountId;
@property (nonatomic, strong)NSString *password;
@property (nonatomic, strong)NSString *favoriteDept;
@property (nonatomic, strong)NSString *linkSource;
@property (nonatomic, strong)NSString *modifiedBy;
@property (nonatomic, strong)NSString *email;
@property (nonatomic, strong)NSString *firstName;
@property (nonatomic, strong)NSString *lastName;
@property (nonatomic, strong)NSString *employee;
@property (nonatomic, strong)NSString *employeeID;
@property (nonatomic, strong)NSString *cardStatus;
@property (nonatomic, strong)NSString *phone;
@property (nonatomic, strong)NSString *mobilePhone;
@property (nonatomic, strong)NSString *address;
@property (nonatomic, strong)NSString *city;
@property (nonatomic, strong)NSString *state;
@property (nonatomic, strong)NSString *zip;
@property (nonatomic, strong)NSString *loyaltyNumber;
@property (nonatomic, strong)NSString *middleName;
@property (nonatomic, strong)NSString *customerStatus;
@property (nonatomic, strong)NSString *prefix;
@property (nonatomic, strong)NSString *suffix;
@property (nonatomic, strong)NSString *voucherMethod;
@property (nonatomic, assign)NSNumber *sendEmailsFlag;
@property (nonatomic, assign)NSNumber *sendTextsFlag;
@property (nonatomic, assign)NSNumber *issueCardFlag;
@property (nonatomic, assign)NSNumber *termsAcceptedFlag;
@property (nonatomic, assign)int      storeNumber;
@property (nonatomic, strong)NSNumber *dateOfBirth;
@property (nonatomic, strong)NSNumber *enrollmentDate;
@property (nonatomic, strong)NSNumber *dateCreated;
@property (nonatomic, strong)NSNumber *dateModified;

@end
