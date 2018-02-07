//
//  BeaconTime.h
//  LibiOS
//
//  Created by VT001 on 30/06/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface BeaconTime : NSObject


@property (nonatomic, readwrite) NSInteger              startTime;
@property (nonatomic, readwrite) NSInteger              endTime;
@property (nonatomic, readwrite) float                  pingFrequency;
@property (nonatomic, readwrite) float                  beaconFrequency;
@property (nonatomic, retain) NSString                  *context;
@property (nonatomic, readwrite) BOOL                   enableBeacon;
@property (nonatomic, readwrite) BOOL                   maintain;
@property (nonatomic, retain) NSString                  *clpApiBaseURL;
@property (nonatomic, readwrite) NSInteger              beaconRange;
@property (nonatomic, readwrite) NSInteger              distanceBeacon;
@property (nonatomic, readwrite) float                  gpsUpdateDistance;
@property (nonatomic, readwrite) NSInteger              beaconQueueFrequency;
@property (nonatomic, readwrite) float                  distanceFromStore;
@property (nonatomic, readwrite) BOOL                   enableLocationTracking;

@property (nonatomic, readwrite) NSInteger              beaconValidDuration;
@property (nonatomic, readwrite) NSInteger              beaconValidOffersCnt;

@property (nonatomic, readwrite) int                    serverSidePingDistance;
@property (nonatomic, readwrite) int                    serverSidePingDuration;

@property (nonatomic, readwrite) int                    gpsSwitchOffDuration;
@property (nonatomic, readwrite) int                    gpsSwitchOnDuration;


// If true check the nearest store beacon start and stop condition Else Don't stop the beacon (Always start)
@property (nonatomic, readwrite) BOOL                   enableInStoreCheck;
// If true show the beacon local notification (DEBUG)
@property (nonatomic, readwrite) BOOL                   enableLocalNotif;

// enable or disable the SE by Google sub menu
@property (nonatomic, readwrite) BOOL                   enableGoogleOffer;

// enable or disable the ExtraFrency in the available offers page
@property (nonatomic, readwrite) BOOL                   enableExtraFrency;

@property (nonatomic, readwrite) float              beaconListFresh;


@end
