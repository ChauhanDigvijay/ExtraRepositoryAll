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


//"successFlag": true,
//"message": "Success",



//addressCity = chennaicity2;
//addressState = chennaistate2;
//addressStreet = chennaistreet2;
//deviceId = 58fe4c9abf1b5f61j8yh6gm77;
//dob = "2016-05-23";
//email = "test@gmail.com";
//emailOptIn = YES;
//favoriteStore = store2;
//firstName = vthink1;
//gender = Male;
//lastName = user;
//loginID = 12547;
//memberid = 38663149840;
//password = 123456;
//phone = "222-123-7890";
//sendWelcomeEmail = ss;
//smsOptIn = YES;
//username = vthink;


//-------------------------------------------- New Api Parameters ------------------------------------//


@property (strong, nonatomic) NSString          *addressCity;
@property (strong, nonatomic) NSString          *addressState;
@property (strong, nonatomic) NSString          *deviceID;//18
@property (strong, nonatomic) NSString          *dateOfBirth;
@property (strong, nonatomic) NSString          *emailID;
@property (strong, nonatomic) NSString          *emailOpted;//4
@property (strong, nonatomic) NSString          *favoriteDepartment;
@property (strong, nonatomic) NSString          *firstName;//5
@property (strong, nonatomic) NSString          *customerGender;
@property (strong, nonatomic) NSString          *lastName;//6
@property (strong, nonatomic) NSString          *loginID;//9
@property (assign, nonatomic) NSString           *customerID;//17
@property (strong, nonatomic) NSString          *password;//10
@property (strong, nonatomic) NSString          *cellPhone;
@property (strong, nonatomic) NSString          *smsOpted;//14
@property (strong, nonatomic) NSString          *username;//14
@property (strong, nonatomic) NSString          *addressStreet;

//-------------------------------------------- old Api Parameters ------------------------------------//

@property (assign, nonatomic) long companyID;

@property (strong, nonatomic) NSString          *loyalityNo;
@property (strong, nonatomic) NSString          *loyalityLevel;
@property (strong, nonatomic) NSString          *homePhone;
@property (strong, nonatomic) NSString          *additionalPhone;
@property (strong, nonatomic) NSString          *addressLine1;
@property (strong, nonatomic) NSString          *addressLine2;
@property (strong, nonatomic) NSString          *addressZip;
@property (strong, nonatomic) NSString          *addressCountry;
@property (strong, nonatomic) NSString          *customerTenantID;//16
@property (strong, nonatomic) NSString          *customerStatus;
@property (strong, nonatomic) NSString          *lastActivtiy;
@property (strong, nonatomic) NSString          *lastActivityTime;
@property (strong, nonatomic) NSString          *lastLoginTime;
@property (assign, nonatomic) int                 statusCode;//15
@property (strong, nonatomic) NSString          *registeredDate;
@property (strong, nonatomic) NSString          *registeredIP;
@property (strong, nonatomic) NSString          *invitationDate;
@property (strong, nonatomic) NSString          *customerAge;
@property (strong, nonatomic) NSString          *homeStore;//7
@property (strong, nonatomic) NSString          *homeStoreID;
@property (strong, nonatomic) NSString          *pushOpted;//12
@property (strong, nonatomic) NSString          *phoneOpted;//11
@property (strong, nonatomic) NSString          *adOpted; //1
@property (strong, nonatomic) NSString          *loyalityRewards;
@property (strong, nonatomic) NSString          *createdBy;
@property (strong, nonatomic) NSString          *createdDate;
@property (strong, nonatomic) NSString          *modifiedBy;
@property (strong, nonatomic) NSString          *modifedDate;
@property (assign, nonatomic) long              customerDeviceID;
@property (strong, nonatomic) NSString          *pushToken;//13
@property (strong, nonatomic) NSString          *deviceType;//3
@property (strong, nonatomic) NSString          *deviceOsVersion;//2
@property (strong, nonatomic) NSString          *deviceVendor;
@property (strong, nonatomic) NSString          *modifiedDate;
@property (strong, nonatomic) NSString          *enabledFlag;
@property (strong, nonatomic) NSString          *status;
@property (strong, nonatomic) NSString          *locationEnabled;//8
@end
