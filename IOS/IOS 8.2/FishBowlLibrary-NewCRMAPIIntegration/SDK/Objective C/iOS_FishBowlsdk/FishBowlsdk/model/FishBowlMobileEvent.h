//
//  MobileEvent.h
//  clpsdk
//
//  Created by VT02 on 1/3/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//

#import "FishBowlsdk.h"

@interface FishBowlMobileEvent : JSONModel

@property (strong, nonatomic) NSString          *action;
@property (assign, nonatomic) long               company;
@property (assign, nonatomic) long               customerid;
//location
@property (assign, nonatomic) float             lat;
@property (assign, nonatomic) float             lon;
@property (strong, nonatomic) NSString          *device_type;
@property (strong, nonatomic) NSString          *device_os_ver;
@property (strong, nonatomic) NSString          *device_carrier;
//beacon
@property (assign, nonatomic) int               beaconid;
@end
