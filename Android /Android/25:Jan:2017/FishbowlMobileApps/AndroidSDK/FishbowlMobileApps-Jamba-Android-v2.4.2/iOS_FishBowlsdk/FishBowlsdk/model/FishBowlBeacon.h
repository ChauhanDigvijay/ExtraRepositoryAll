//
//  CLPBeacon.h
//  Clyptech
//
//  Created by Subrat Singh on 1/12/15.
//  Copyright (c) 2015 Subrat. All rights reserved.
//

@interface FishBowlBeacon : NSObject

@property (strong, nonatomic) NSNumber *clpBeaconId;
@property (strong, nonatomic) NSString *clpBeaconName;
@property (strong, nonatomic) NSNumber *clpBeaconStoreId;
@property (strong, nonatomic) NSUUID   *beaconUUID;
@property (strong, nonatomic) NSNumber *beaconMajor;
@property (strong, nonatomic) NSNumber *beaconMinor;
@property (strong, nonatomic) NSString *color;
@property (strong, nonatomic) NSNumber *rssi;
@property (strong, nonatomic) NSNumber *distance;

@end
