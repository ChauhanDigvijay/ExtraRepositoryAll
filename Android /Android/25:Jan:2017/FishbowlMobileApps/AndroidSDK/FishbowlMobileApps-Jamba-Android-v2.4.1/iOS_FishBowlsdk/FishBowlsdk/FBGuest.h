//
//  FBGuest.h
//  clpsdk
//
//  Created by Puneet  on 6/23/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JSONModel.h"


@interface FBGuest : JSONModel


@property (strong, nonatomic) NSString          *firstName;
@property (strong, nonatomic) NSString          *deviceId;
@property (strong, nonatomic) NSString          *pushToken;
@property (strong, nonatomic) NSString          *deviceType;
@property (strong, nonatomic) NSString          *deviceOsVersion;
@property (strong, nonatomic) NSString          *memberid;
@property (assign, nonatomic) BOOL               successFlag;
@property (strong, nonatomic) NSString          *tenantid;
@property (strong, nonatomic) NSString          *appId;

@end
