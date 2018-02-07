//
//  Raleys.h
//  Raley's
//
//  Created by VT01 on 02/06/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import "Utility.h"
#import "WebService.h"

@interface Raleys : NSObject <WebServiceListener>
{
    WebService *account_service;
}

@property(atomic, retain) NSString *pushDeviceToken;
@property(atomic, retain) NSString *userName;
@property(atomic, retain) NSString *fullName;

@property(atomic, retain) NSString *email;
@property(atomic, retain) NSString *password;


@property(atomic, readwrite) CLLocationCoordinate2D latestLocation;

+(Raleys*)shared;
-(void)userRegister;

+(NSString*)base64EncodeString:(NSString*)value;
+(NSString*)base64DecodeString:(NSString*)value;

@end
