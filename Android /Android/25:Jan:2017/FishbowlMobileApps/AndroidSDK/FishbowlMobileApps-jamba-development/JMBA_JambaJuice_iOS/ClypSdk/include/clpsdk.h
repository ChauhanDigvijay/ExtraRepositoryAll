//
//  clpsdk.h
//  clpsdk
//
//  Created by VT02 on 1/1/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//
#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "CLPCustomer.h"
#import "CLPMobileEvent.h"
#import "CLPStore.h"
#import "CLPBeacon.h"
#import <PassKit/PassKit.h>
#import <CoreLocation/CoreLocation.h>
#import "Database.h"
#import <MapKit/MapKit.h>
#import "FBGuest.h"

//! Project version number for clpsdk.
FOUNDATION_EXPORT double clpsdkVersionNumber;
//! Project version string for clpsdk.
FOUNDATION_EXPORT const unsigned char clpsdkVersionString[];
// In this header, you should import all the public headers of your framework using statements like #import <clpsdk/PublicHeader.h>
@protocol clpSdkDelegate <NSObject>
@optional
-(void)clpOpenPassbook;
-(void)clpClosePassbook:(NSString*)strError;
-(void)clpPushDataBinding:(NSString *)strOfferTitle withId:(NSString *)strOfferId;
-(void)clpResponseFail:(NSError*)error;
-(void)fireTimer:(NSInteger)intDelayFire;
-(void)openDynamicPassViaPN:(NSString *)strofferid;
-(void)storeListResponseSucceed:(NSArray *)arryStore;
-(void)storeListResponseFailed;

@end
@interface clpsdk : NSObject<CLLocationManagerDelegate,MKMapViewDelegate>
{
    CLLocationManager *locationManager;
    CLLocation        *currentLocation;
    CLPCustomer *currentCustomer;
    FBGuest *GuestCustomer;
    NSMutableArray *nearestStoreList;
    NSString *mobileAPIKey;
    long companyID;
    NSMutableArray *inRegionStoreList;
    Database *db;
    BOOL isOpen;
    
}
@property (assign, nonatomic) id <clpSdkDelegate> delegate;
@property (nonatomic, readwrite)NSString *mobileAPIKey;
@property(nonatomic,readwrite) long companyID;
@property (nonatomic, readwrite)NSDictionary *mobileSettings;
@property (nonatomic, readwrite)NSDictionary *digitalEventSettings;

@property (strong, nonatomic) PKPassLibrary *passLib;
@property (nonatomic, readwrite) NSMutableArray *nearestStoreList,*arrEventSettings;

@property (nonatomic, readwrite)NSString *SERVER_URL;

@property (nonatomic, readwrite)BOOL isInRegion,isFirstTimeAppLaunch,isStoreListPopulated;
@property (nonatomic, readwrite)NSDate *storeUpdateTime;
@property (nonatomic, readwrite)BOOL startTransientRegion;
@property (nonatomic, readwrite)CLRegion *transientRegion;
@property (nonatomic, readwrite)NSDate *lastInsideRegionCheck;

//db
@property (nonatomic, readwrite)int DISTANCE_FILTER;
@property (nonatomic, readwrite)float DISTANCE_STORE;
@property (nonatomic, readwrite)float GEOFENCE_RADIUS;
@property (nonatomic, readwrite)float STORE_REFRESH_TIME;
@property (nonatomic, readwrite)int ENABLE_LOCAL_NOTIFICATION;
@property (nonatomic, readwrite)int LOCATION_UPDATE_PING_FREQUENCY;
@property (nonatomic, readwrite)int BEACON_PING_FREQUENCY;
@property (nonatomic, readwrite)int INSIDE_REGIONCHK_FREQ;

//new
@property (nonatomic, readwrite)int MAX_STORE_COUNT;

//Becon Monitoring
@property (strong, nonatomic) CLBeaconRegion *beaconRegion;
@property (strong, nonatomic) NSMutableArray *beaconRegions;
@property (strong, nonatomic) NSMutableArray *storeBeacons;
@property (strong, nonatomic) NSMutableArray *beaconRegionsAlreadyFound;

//Event Management
@property (nonatomic, readwrite)BOOL isTriggerAppEvent,isTriggerGeoFence,isTriggerBeacon,isTriggerErrorEvent,isPushNotification,isInAPPOffer;
@property (nonatomic, assign) NSInteger intDelayTimer;
@property (nonatomic, assign) NSInteger intMaxEvents;


//+(clpsdk *) sharedInstanceWithAPIKey:(NSString*)mobileAPIKey withBaseURL:(NSString*)serverURL;
//+(clpsdk *) sharedInstanceWithAPIKey: withBaseURL:(NSString*)serverURL;



+(clpsdk *) sharedInstanceWithAPIKey;

+(clpsdk *) sharedInstanceWithAPIKey :(NSString *)serverURL;

-(void)startStandardUpdate;
-(void)openPassbookAndShow:(NSURL*)url;
-(void)openStaticPass;
-(void)getMobilePreference;
- (void)saveCustomer:(CLPCustomer *)customerInfo :(void (^)(CLPCustomer *cusInfo, NSError *error))response;
-(void)getAllStore:(BOOL)forceUpdate :(void (^)(BOOL success, NSError *error))response;
-(void)processPushMessage:(NSDictionary*)userInfo;
//-(void)updateLocation:(MobileEvent*)event;
-(void)StartBeaconRegionMonitoring:(NSString *)storeId;
-(void)startRegionMonitoringInSuspendMode;
-(void)updateAppEvent:(NSDictionary *)submitAppEvents;
+ (UIViewController*) getTopController;
-(void)logoutClpSdk;
-(void)updateOfferEvent:(NSDictionary *)submitClpEvents;
- (int) filterFavoritearray:(NSString *)StoreNumber;
-(void)appEventTimerMethodCall;
-(void)openPassbookAndShowwithData:(NSData*)data;
-(void)getALLStoreList;
-(void)updateOfferEventWithPass:(NSDictionary *)submitClpEvents;
- (void)createCustomer:(FBGuest *)customerInfo :(void (^)(FBGuest *cusInfo, NSError *error))response;
- (void)loginCustomer:(FBGuest *)customerInfo :(void (^)(FBGuest *cusInfo, NSError *error))response;
- (void)CustomerCreation:(CLPCustomer *)customerInfo :(void (^)(CLPCustomer *cusInfo, NSError *error))response;
-(void)appCustomerForGuest;
-(void)updateDeviceInSDK:(NSString *)customerID;

-(void)getAllStoresFromStore;



@end
