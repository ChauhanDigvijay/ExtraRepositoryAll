//
//  clpsdk.m
//  clpsdk
//
//  Created by VT02 on 1/1/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//

#import "clpsdk.h"
#import "AFNetworking.h"
#import "ApiClasses.h"
#import "ModelClass.h"
#import "Constant.h"

@implementation clpsdk{
    NSDate *lastlocation_updatetime;
    NSDate *lastbeacon_updatetime;
    CLLocation *lastlocation_updatetoserver;
    NSMutableArray * arrAppEvent;
    NSMutableArray *storeList;
    NSMutableArray * arrEventNewSetting;
    int lastValueDelayTimer;
    NSURL *staticPassURL;
}


@synthesize mobileSettings,digitalEventSettings,isInRegion,storeUpdateTime,DISTANCE_FILTER,DISTANCE_STORE,GEOFENCE_RADIUS,STORE_REFRESH_TIME,SERVER_URL,ENABLE_LOCAL_NOTIFICATION,LOCATION_UPDATE_PING_FREQUENCY,BEACON_PING_FREQUENCY,INSIDE_REGIONCHK_FREQ,nearestStoreList,mobileAPIKey;

@synthesize MAX_STORE_COUNT,companyID,isFirstTimeAppLaunch,isStoreListPopulated,beaconRegion,beaconRegions,storeBeacons,beaconRegionsAlreadyFound,startTransientRegion,transientRegion,lastInsideRegionCheck,isTriggerAppEvent,isTriggerBeacon,isTriggerGeoFence,isTriggerErrorEvent,isPushNotification,intMaxEvents,intDelayTimer,isInAPPOffer;

@synthesize delegate;
static clpsdk* __strong instance = nil;

#pragma mark - shared instance method

+(clpsdk *)sharedInstanceWithAPIKey
{
    //https://stg-hbh.fishbowlcloud.com/       // stg
    
    if(instance){
        return instance;
    }else{
        instance =[[clpsdk alloc]init];
        instance.isInRegion=false;
        instance.isFirstTimeAppLaunch=true;
       // instance.SERVER_URL = @"https://demo-live.fishbowlcloud.com/";//demo
        // instance.SERVER_URL = @"https://qa-jamba.fishbowlcloud.com/";//qa
        //instance.SERVER_URL = @"http://devdmz-jamba.fishbowlcloud.com/";//dev
        instance.SERVER_URL = @"https://stg-hbh.fishbowlcloud.com/";//stg
        
        // Default Settings in Mobile SDK
        instance.DISTANCE_FILTER=50;
        instance.DISTANCE_STORE=100000.00;
        instance.GEOFENCE_RADIUS=1000;
        instance.STORE_REFRESH_TIME=1800;//refrest store list
        instance.MAX_STORE_COUNT=15;//max store count
        instance.ENABLE_LOCAL_NOTIFICATION=0;
        instance.LOCATION_UPDATE_PING_FREQUENCY = 10; //seconds
        instance.BEACON_PING_FREQUENCY = 15; //seconds
        instance.INSIDE_REGIONCHK_FREQ = 180; //seconds
        instance->lastValueDelayTimer = 0;
        //
        
        [instance openDatabase];
        
        //instance.mobileAPIKey=mobileAPIKey;
        instance->storeList=[[NSMutableArray alloc]init];
        instance.nearestStoreList=[[NSMutableArray alloc]init];
        
        if(instance.arrEventSettings==nil)
        {
        instance.arrEventSettings =  [[NSMutableArray alloc]init];
        }
        // Get new settings from server
        [instance getMobilePreference];
        return instance;
    }
}


#pragma mark - getMobile Prefrence Api

-(void)getMobilePreference{
    
    if(arrEventNewSetting==nil)
    {
        arrEventNewSetting = [[NSMutableArray alloc]init];
    }
    
    mobileSettings=[[NSDictionary alloc]init];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSString * accessToken = [[NSUserDefaults standardUserDefaults]valueForKey:@"access_token"];
    
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:contentType forHTTPHeaderField:@"Content-Type"];
    [manager.requestSerializer setValue:applicationType forHTTPHeaderField:@"Application"];
    [manager.requestSerializer setValue:TanentID forHTTPHeaderField:@"tenantid"];
    [manager.requestSerializer setValue:accessToken forHTTPHeaderField:@"access_token"];
    [manager.requestSerializer setValue:@"fishbowl" forHTTPHeaderField:@"tenantName"];
    [manager.requestSerializer setValue:clientID forHTTPHeaderField:@"client_id"];
    [manager.requestSerializer setValue:ClientSecret forHTTPHeaderField:@"client_secret"];


    [manager GET:[SERVER_URL stringByAppendingString:@"clpapi/mobile/settings/getmobilesettings"]
      parameters:nil
         success:^(AFHTTPRequestOperation *operation, id responseObject) {
             
            NSLog(@"responseObject %@",responseObject);
             
             if ([responseObject isKindOfClass:[NSDictionary class]]) {
                 if([[[(NSDictionary*)responseObject valueForKey:@"mobSettingList"] valueForKey:@"mobileSettings"] isKindOfClass:[NSArray class] ]){
                     mobileSettings=[[(NSArray*)responseObject valueForKey:@"mobSettingList"]valueForKey:@"mobileSettings"];
                     
                     NSLog( @"settings are %@", mobileSettings.description);
                     
                     if([self getObjectForMobileSettingsName:@"DISTANCE_FILTER"]!=nil){
                         DISTANCE_FILTER=[[self getObjectForMobileSettingsName:@"DISTANCE_FILTER"] floatValue];
                     }
                     if([self getObjectForMobileSettingsName:@"DISTANCE_STORE"]!=nil){
                         DISTANCE_STORE=[[self getObjectForMobileSettingsName:@"DISTANCE_STORE"] floatValue];
                     }
                     if([self getObjectForMobileSettingsName:@"GEOFENCE_RADIUS"]!=nil){
                         GEOFENCE_RADIUS=[[self getObjectForMobileSettingsName:@"GEOFENCE_RADIUS"] floatValue];
                     }
                     if([self getObjectForMobileSettingsName:@"STORE_REFRESH_TIME"]!=nil){
                         STORE_REFRESH_TIME=[[self getObjectForMobileSettingsName:@"STORE_REFRESH_TIME"] floatValue];
                     }
                     if([self getObjectForMobileSettingsName:@"MAX_STORE_COUNT"]!=nil){
                         MAX_STORE_COUNT=[[self getObjectForMobileSettingsName:@"MAX_STORE_COUNT"] intValue];
                     }
                     if([self getObjectForMobileSettingsName:@"LOCATION_UPDATE_PING_FREQUENCY"]!=nil){
                         LOCATION_UPDATE_PING_FREQUENCY=[[self getObjectForMobileSettingsName:@"LOCATION_UPDATE_PING_FREQUENCY"] intValue];
                     }
                     if([self getObjectForMobileSettingsName:@"BEACON_PING_FREQUENCY"]!=nil){
                         BEACON_PING_FREQUENCY=[[self getObjectForMobileSettingsName:@"BEACON_PING_FREQUENCY"] intValue];
                     }
                     if([self getObjectForMobileSettingsName:@"ENABLE_LOCAL_NOTIFICATION"]!=nil){
                         ENABLE_LOCAL_NOTIFICATION=[[self getObjectForMobileSettingsName:@"ENABLE_LOCAL_NOTIFICATION"] intValue];
                     }
                     if([self getObjectForMobileSettingsName:@"INSIDE_REGIONCHK_FREQ"]!=nil){
                         INSIDE_REGIONCHK_FREQ=[[self getObjectForMobileSettingsName:@"INSIDE_REGIONCHK_FREQ"] intValue];
                     }
                 
                     if ([self getObjectForMobileSettingsName:@"TRIGGER_APP_EVENTS"]!=nil) {
                         isTriggerAppEvent = [[self getObjectForMobileSettingsName:@"TRIGGER_APP_EVENTS"] intValue];
                     }
                     if ([self getObjectForMobileSettingsName:@"TRIGGER_GEOFENCE"]!=nil) {
                         isTriggerGeoFence = [[self getObjectForMobileSettingsName:@"TRIGGER_GEOFENCE"] intValue];
                     }
                     if ([self getObjectForMobileSettingsName:@"TRIGGER_BEACON"]!=nil) {
                         isTriggerBeacon = [[self getObjectForMobileSettingsName:@"TRIGGER_BEACON"] intValue];
                     }
                     if ([self getObjectForMobileSettingsName:@"TRIGGER_ERROR_EVENT"]!=nil) {
                         isTriggerErrorEvent = [[self getObjectForMobileSettingsName:@"TRIGGER_ERROR_EVENT"] intValue];
                     }
                     if ([self getObjectForMobileSettingsName:@"TRIGGER_PUSH_NOTIFICATION"]!=nil) {
                         isPushNotification = [[self getObjectForMobileSettingsName:@"TRIGGER_PUSH_NOTIFICATION"] intValue];
                     }
                     
                     if ([self getObjectForMobileSettingsName:@"EVENT_QUEUE_SIZE_THRESHOLD"]!=nil) {
                        intMaxEvents = [[self getObjectForMobileSettingsName:@"EVENT_QUEUE_SIZE_THRESHOLD"] intValue];
                     }
                     
                     if ([self getObjectForMobileSettingsName:@"TIMER_DELAY_IN_SECONDS"]!=nil) {
                         intDelayTimer = [[self getObjectForMobileSettingsName:@"TIMER_DELAY_IN_SECONDS"] intValue];
                     }
                     if ([self getObjectForMobileSettingsName:@"INAPPOFFER_ENABLED"]!=nil) {
                         isInAPPOffer = [[self getObjectForMobileSettingsName:@"INAPPOFFER_ENABLED"] intValue];
                     }
                 }
                 
                 if ([[NSUserDefaults standardUserDefaults]valueForKey:@"GuestCustomer"] != nil)
                 {
                    GuestCustomer = [[NSUserDefaults standardUserDefaults]valueForKey:@"GuestCustomer"];
                    NSLog(@"guest customer information ------------ %@",GuestCustomer);
                 }
                 
                 if (arrEventNewSetting.count!=0)
                 {
                     [arrEventNewSetting removeAllObjects];
                 }
//                 
//                    if (intDelayTimer != lastValueDelayTimer)
//                    {
//                        [delegate fireTimer:intDelayTimer];
//                    }
                 
                 if([[[(NSDictionary*)responseObject valueForKey:@"digitalEventList"] valueForKey:@"digitalEventList"] isKindOfClass:[NSArray class] ]){
                     digitalEventSettings =[[(NSArray*)responseObject valueForKey:@"digitalEventList"]valueForKey:@"digitalEventList"];
                     NSLog(@"digitalEventSettings are %@",digitalEventSettings.description);
                     
                     for (NSDictionary *dict  in digitalEventSettings) {
                         
                         if([dict objectForKey:@"eventTypeName"]!=nil){
                             NSString *strEventName =[dict objectForKey:@"eventTypeName"];
                             //[_arrEventSettings addObject:strEventName];
                             [arrEventNewSetting addObject:strEventName];
                         }
                     }
                     
                     NSLog(@"arrAllEventSettings---%@",arrEventNewSetting);
                     
                     if(isOpen == NO)
                     {
                         NSMutableDictionary * dict = [[NSMutableDictionary alloc]init];
                         [dict setValue:@"APP_OPEN" forKey:@"event_name"];
                         [self updateAppEvent:dict];
                         isOpen = YES;
                     }
                 }
             }
             
         }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
             NSLog(@"clpapi/mobilesettings Error: %@", error);
         }];
}


-(id)getObjectForMobileSettingsName:(NSString*)name{
    id value=nil;
    for(NSDictionary* dic in mobileSettings){
        if([dic objectForKey:@"settingName"]!=nil && [[dic objectForKey:@"settingName"] isEqualToString:name] && [dic objectForKey:@"settingValue"]!=nil){
            value=[dic objectForKey:@"settingValue"];
        }
    }
    return value;
}


#pragma mark - get All Stores Api

-(void)getAllStore:(BOOL)forceUpdate :(void (^)(BOOL success, NSError *error))response {
    
    NSLog(@" getAllStore is calling");
    if(forceUpdate){
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        manager.requestSerializer = [AFJSONRequestSerializer serializer];
        [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
        [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];

        [manager GET:[SERVER_URL stringByAppendingString:@"clpapi/mobile/stores/getstores"]

          parameters:nil
             success:^(AFHTTPRequestOperation *operation, id responseObject) {
                 
                 if ([responseObject isKindOfClass:[NSDictionary class]]) {
                     NSDictionary *storeresponse=(NSDictionary*)responseObject;
                     if ([[storeresponse valueForKey:@"stores"] isKindOfClass:[NSArray class]]){
                         NSArray *arraydic=[storeresponse objectForKey:@"stores"];
                         storeList=[[NSMutableArray alloc]init];
                         for(NSDictionary* store in arraydic){
                             CLPStore *storeObj=[[CLPStore alloc]init];
                             storeObj.storeID=[[store valueForKey:@"storeID"]intValue];
                             storeObj.companyID=[[store valueForKey:@"companyID"]intValue];
                             storeObj.storeName=[store valueForKey:@"storeName"];
                             storeObj.contactPerson=[store valueForKey:@"contactPerson"];
                             storeObj.address=[store valueForKey:@"address"];
                             storeObj.city=[store valueForKey:@"city"];
                             storeObj.state=[store valueForKey:@"state"];
                             storeObj.zip=[store valueForKey:@"zip"];
                             storeObj.country=[store valueForKey:@"country"];
                             storeObj.latitude=[store valueForKey:@"latitude"];
                             storeObj.longitude=[store valueForKey:@"longitude"];
                             storeObj.geoFenceCorrFactor = [[store valueForKey:@"geoCorrectionFactor"]floatValue];
                             storeObj.storeNumber = [[store valueForKey:@"storeNumber"] intValue];
                             [storeList addObject:storeObj];
                         }
                         
                         NSData *encodedObject = [NSKeyedArchiver archivedDataWithRootObject:storeList];
                         NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
                         [defaults setObject:encodedObject forKey:@"storeArray"];
                         
                         storeUpdateTime =[[NSDate alloc]init];
                         response(true,nil);
                     }else{
                         response(false,nil);
                         NSLog(@"store key not found %@",storeresponse);
                     }
                 }else{
                     response(false,nil);
                     NSLog(@"Dictionary not found %@",responseObject);
                 }
             }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                 NSLog(@"Error: %@", error);
                 response(false,error);
             }];
      }
}


#pragma mark - Static Pass Open

-(void)openStaticPass
{
    [self openPassbookAndShow:staticPassURL];
}

-(int) filterFavoritearray:(NSString *)StoreNumber
{
    NSLog(@"StoreNumber.integerValue is %ld",(long)StoreNumber.integerValue);
    
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"storeNumber == %d", StoreNumber.integerValue];
    
   NSArray *filteredArray = [self->storeList filteredArrayUsingPredicate:predicate];
    
    NSLog(@"self.storeList count is %lu",(unsigned long)self->storeList.count);
    NSLog(@" HEARE %@",filteredArray);
    
    CLPStore *filteredstore;
    
    if(filteredArray.count>0)
    {
         filteredstore = [filteredArray objectAtIndex:0];
    }
    
    return filteredstore.storeID;
}

#pragma mark - createCustomer for guest

- (void)createCustomer:(FBGuest *)customerInfo :(void (^)(FBGuest *cusInfo, NSError *error))response
    {
          NSLog(@"FBGuest Creat Customer");
        
        AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
        manager.requestSerializer = [AFJSONRequestSerializer serializer];
        [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [manager.requestSerializer setValue:@"mobile" forHTTPHeaderField:@"Application"];
        [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];
        [manager.requestSerializer setValue:@"fishbowl" forHTTPHeaderField:@"tenantName"];
        [manager.requestSerializer setValue:@"201969E1BFD242E189FE7B6297B1B5A5" forHTTPHeaderField:@"client_id"];

        
        NSMutableDictionary * dict = [NSMutableDictionary new];
        
        dict = [customerInfo toDictionary].mutableCopy;
        
        [dict setValue:@"com.olo.jambajuiceapp" forKey:@"appId"];
        
        NSLog(@"customerInfo is %@",dict);
        
        SERVER_URL = @"https://demo-fb.fishbowlcloud.com/";
        
        
        [manager POST:[SERVER_URL stringByAppendingString:@"clpapi/mobile/appcustomer"] parameters:dict success:^(AFHTTPRequestOperation *operation, id responseObject) {
                NSLog(@"SUCCESS JSON: response %@", responseObject);
            
              NSLog(@"SUCCESS JSON CREAT CUSTOMER API: response %@", responseObject);
            
            if ([responseObject isKindOfClass:[NSDictionary class]]) {
                NSDictionary *customerresponse=(NSDictionary*)responseObject;
                GuestCustomer=[[FBGuest alloc]init];
                
                GuestCustomer.firstName=[customerresponse valueForKey:@"firstName"];
                GuestCustomer.memberid=[customerresponse valueForKey:@"memberid"];
                GuestCustomer.successFlag=[[customerresponse valueForKey:@"successFlag"] boolValue];
                GuestCustomer.tenantid=[customerresponse valueForKey:@"tenantid"];
                GuestCustomer.pushToken=[customerresponse valueForKey:@"pushToken"];
                GuestCustomer.deviceType = [UIDevice currentDevice].model;
                GuestCustomer.deviceOsVersion = [UIDevice currentDevice].systemVersion;
                GuestCustomer.deviceId = [customerresponse valueForKey:@"deviceId"];
                
                [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"CurrentEvents"];

                NSLog(@"app customer device id-------%@",[customerresponse valueForKey:@"deviceId"]);
                
                
                NSLog(@"FB getCustomer info--------%@",GuestCustomer.description);
                
                NSData *encodedObject = [NSKeyedArchiver archivedDataWithRootObject:GuestCustomer];
                [[NSUserDefaults standardUserDefaults]setObject:encodedObject forKey:@"GuestCustomer"];
                [[NSUserDefaults standardUserDefaults]synchronize];
            }

            response(GuestCustomer,nil);
           // NSLog(@"GuestCustomer is First %@",GuestCustomer.description);

        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            response(GuestCustomer,error);
            NSLog(@" Something wrong Error: %@", [error description]);
        }];

}

#pragma mark - loginCustomer for guest

- (void)loginCustomer:(FBGuest *)customerInfo :(void (^)(FBGuest *cusInfo, NSError *error))response
{
     NSLog(@"FBGuest Login Customer");
    
    AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
    [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];

    [manager POST:[SERVER_URL stringByAppendingString:@"clpapi/mobile/appcustomer"] parameters:[customerInfo toDictionary] success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"SUCCESS JSON: response %@", responseObject);
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            NSDictionary *customerresponse=(NSDictionary*)responseObject;
            GuestCustomer=[[FBGuest alloc]init];
            GuestCustomer.firstName=[customerresponse valueForKey:@"firstName"];
            GuestCustomer.memberid=[customerresponse valueForKey:@"memberid"];
            GuestCustomer.successFlag=[[customerresponse valueForKey:@"successFlag"] boolValue];
            GuestCustomer.tenantid=[customerresponse valueForKey:@"tenantid"];
            
          //  GuestCustomer.appId=[customerresponse valueForKey:@"appId"];
        }
        response(GuestCustomer,nil);
       // NSLog(@"GuestCustomer is AFTER %@",GuestCustomer.description);
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        response(GuestCustomer,error);
        NSLog(@"Error: %@", [error description]);
    }];
}


#pragma mark - loginCustomer for guest

-(void)saveCustomer:(CLPCustomer *)customerInfo :(void (^)(CLPCustomer *cusInfo, NSError *error))response {
    
   NSLog(@"CLPCustomer Save Customer");
    
    AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
    if(customerInfo && (!customerInfo.pushToken || customerInfo.pushToken.length==0)){
        customerInfo.pushToken=@"SIMULATOR";
    }
    if(([CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorized)||
       ([CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedWhenInUse))
        customerInfo.locationEnabled=@"Y";
    else customerInfo.locationEnabled=@"N";
    
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:mobileAPIKey forHTTPHeaderField:@"CLP-API-KEY"];
    NSLog(@"Request Dictiona%@",[customerInfo toDictionary].description);
    
     NSData *encodedObject = [NSKeyedArchiver archivedDataWithRootObject:customerInfo];
    [[NSUserDefaults standardUserDefaults]setObject:encodedObject forKey:@"customer"];
    [[NSUserDefaults standardUserDefaults]synchronize];

    [manager POST:[SERVER_URL stringByAppendingString:@"clpapi/mobile/appcustomer"] parameters:[customerInfo toDictionary] success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"Save Customer Response SUCCESS JSON: response %@", responseObject);
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            NSDictionary *customerresponse=(NSDictionary*)responseObject;
            currentCustomer=[[CLPCustomer alloc]init];
            currentCustomer.customerID=[customerresponse valueForKey:@"memberid"];
            currentCustomer.companyID=[[customerresponse valueForKey:@"memberid"]intValue];
            currentCustomer.firstName=[customerresponse valueForKey:@"firstName"];
            currentCustomer.lastName=[customerresponse valueForKey:@"lastName"];
            currentCustomer.emailID=[customerresponse valueForKey:@"email"];
            if([currentCustomer.loginID isEqualToString:@""])
            {
                
            }
            else
            {
                currentCustomer.loginID=[customerresponse valueForKey:@"loginID"];
            }
            currentCustomer.password=[customerresponse valueForKey:@"password"];
            currentCustomer.loyalityNo=[customerresponse valueForKey:@"loyalityNo"];
            currentCustomer.loyalityLevel=[customerresponse valueForKey:@"loyalityLevel"];
            currentCustomer.homePhone=[customerresponse valueForKey:@"homePhone"];
            currentCustomer.cellPhone=[customerresponse valueForKey:@"cellPhone"];
            currentCustomer.additionalPhone=[customerresponse valueForKey:@"additionalPhone"];
            currentCustomer.addressLine1=[customerresponse valueForKey:@"addressLine1"];
            currentCustomer.addressLine2=[customerresponse valueForKey:@"addressLine2"];
            currentCustomer.addressCity=[customerresponse valueForKey:@"addressCity"];
            currentCustomer.addressState=[customerresponse valueForKey:@"addressState"];
            currentCustomer.addressZip=[customerresponse valueForKey:@"addressZip"];
            currentCustomer.addressCountry=[customerresponse valueForKey:@"addressCountry"];
            currentCustomer.customerTenantID=[customerresponse valueForKey:@"customerTenantID"];
            currentCustomer.customerStatus=[customerresponse valueForKey:@"customerStatus"];
            currentCustomer.lastActivtiy=[customerresponse valueForKey:@"lastActivtiy"];
            currentCustomer.statusCode=[[customerresponse valueForKey:@"statusCode"] intValue];
            currentCustomer.registeredIP=[customerresponse valueForKey:@"registeredIP"];
            currentCustomer.customerGender=[customerresponse valueForKey:@"customerGender"];
            currentCustomer.dateOfBirth=[customerresponse valueForKey:@"dateOfBirth"];
            currentCustomer.customerAge=[customerresponse valueForKey:@"customerAge"];
            currentCustomer.homeStore=[customerresponse valueForKey:@"homeStore"];
            currentCustomer.homeStoreID=[customerresponse valueForKey:@"homeStoreID"];
            currentCustomer.favoriteDepartment=[customerresponse valueForKey:@"favoriteDepartment"];
            currentCustomer.pushOpted=[customerresponse valueForKey:@"pushOpted"];
            currentCustomer.smsOpted=[customerresponse valueForKey:@"smsOpted"];
            currentCustomer.emailOpted=[customerresponse valueForKey:@"emailOpted"];
            currentCustomer.phoneOpted=[customerresponse valueForKey:@"phoneOpted"];
            currentCustomer.adOpted=[customerresponse valueForKey:@"adOpted"];
            currentCustomer.loyalityRewards=[customerresponse valueForKey:@"loyalityRewards"];
            currentCustomer.modifiedBy=[customerresponse valueForKey:@"modifiedBy"];
            currentCustomer.deviceID=[customerresponse valueForKey:@"deviceID"];
            currentCustomer.pushToken=[customerresponse valueForKey:@"pushToken"];
            currentCustomer.deviceType=[customerresponse valueForKey:@"deviceType"];
            currentCustomer.deviceOsVersion=[customerresponse valueForKey:@"deviceOsVersion"];
            currentCustomer.deviceVendor=[customerresponse valueForKey:@"deviceVendor"];
            currentCustomer.enabledFlag=[customerresponse valueForKey:@"enabledFlag"];
            currentCustomer.createdBy=[customerresponse valueForKey:@"createdBy"];
            currentCustomer.locationEnabled=[customerresponse valueForKey:@"locationEnabled"];
            db.customer = currentCustomer;
            [self saveDatabase];

        }else{
            
        }
        response(currentCustomer,nil);
        
        NSLog(@"ClpCustomerDescription----------%@",currentCustomer.description);
   
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        response(currentCustomer,error);
        NSLog(@"Error: %@", [error description]);
    }];
    
}


#pragma mark - CustomerCreation for customer

- (void)CustomerCreation:(CLPCustomer *)customerInfo :(void (^)(CLPCustomer *cusInfo, NSError *error))response
{

    AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
    
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
    [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];
    [manager.requestSerializer setValue:@"fishbowl" forHTTPHeaderField:@"tenantName"];
    [manager.requestSerializer setValue:@"201969E1BFD242E189FE7B6297B1B5A5" forHTTPHeaderField:@"client_id"];
    [manager.requestSerializer setValue:@"C65A0DC0F28C469FB7376F972DEFBCB9" forHTTPHeaderField:@"client_secret"];
    
    NSDictionary * dict ;
    dict = [customerInfo toDictionary];
    
    NSMutableDictionary * dict1 = [NSMutableDictionary new];
    dict1 = dict.mutableCopy;
    
    
    NSUserDefaults *defaults1 = [NSUserDefaults standardUserDefaults];
    NSData *encodedObject1 = [defaults1 objectForKey:@"GuestCustomer"];
    NSDictionary *object = [NSKeyedUnarchiver unarchiveObjectWithData:encodedObject1];
    
    if(GuestCustomer != nil)
    {
        [dict1 setValue:[object valueForKey:@"memberid"] forKey:@"memberid"];
    }
    else
    {
        NSString * str = [[NSUserDefaults standardUserDefaults]valueForKey:@"Newmemberid"];
        [dict1 setValue:str forKey:@"memberid"];
    }
    
    if([[dict valueForKey:@"smsOpted"] isEqualToString:@"Y"])
    {
        [dict1 setValue:@"true" forKey:@"smsOptIn"];
    }
    else
    {
        [dict1 setValue:@"false" forKey:@"smsOptIn"];
    }

    if([[dict valueForKey:@"emailOpted"] isEqualToString:@"Y"])
    {
        [dict1 setValue:@"true" forKey:@"emailOptIn"];
    }
    else
    {
        [dict1 setValue:@"false" forKey:@"emailOptIn"];
    }
    
    NSLog(@"create member ----------%@",dict1.description);
    
    [manager POST:[SERVER_URL stringByAppendingString:@"clpapi/member/create"] parameters:dict1  success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"member Response SUCCESS JSON: response %@", responseObject);
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            NSDictionary *customerresponse=(NSDictionary*)responseObject;
            
            NSLog(@"ClpmemberDescription----------%@",customerresponse);
            
            if ([customerresponse valueForKey:@"memberid"] != (NSString *)[NSNull null])
            {
            currentCustomer=[[CLPCustomer alloc]init];
            currentCustomer.customerID=[customerresponse valueForKey:@"memberid"];
            currentCustomer.deviceType = [customerresponse valueForKey:@"deviceType"];
            currentCustomer.deviceID = [UIDevice currentDevice].identifierForVendor.UUIDString;
            currentCustomer.deviceOsVersion = [customerresponse valueForKey:@"deviceOSVersion"];
            currentCustomer.pushToken = customerInfo.pushToken;

             NSString * strPushToken = [[NSUserDefaults standardUserDefaults]valueForKey:@"pushToken"];
                
              //[[customerresponse valueForKey:@"deviceId"] isEqualToString:[UIDevice currentDevice].identifierForVendor.UUIDString] &&
                
//            NSLog(@"customer device id--------%@",[customerresponse valueForKey:@"deviceId"]);
//            NSLog(@"current system device id--------%@",currentCustomer.deviceID);

               
                NSLog(@"push token for app customer-------%@",strPushToken);
                
                NSLog(@"push token for create customer-------%@",[customerresponse valueForKey:@"pushToken"]);
                
                
                if ([[customerresponse valueForKey:@"deviceId"] isKindOfClass:[NSNull class]] || [[customerresponse valueForKey:@"pushToken"] isKindOfClass:[NSNull class]])
                {
                    [self updateDevice];
                     NSLog(@"callUpdateDevice in case device null");
                }
           else if([[customerresponse valueForKey:@"pushToken"] isEqualToString:strPushToken])
                {
                    NSLog(@"No need callUpdateDevice");
                }
                else
                {
                     GuestCustomer = nil;
                    [self updateDevice];
                    NSLog(@"callUpdateDevice");
                }
                }
                
              [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"GuestCustomer"];
              [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"CurrentEvents"];

                NSLog(@"member id is %@",[customerresponse valueForKey:@"memberid"]);
            
            [[NSUserDefaults standardUserDefaults]synchronize];
    
        }else{
            
        }
        NSLog(@"CreateApi----------%@",currentCustomer.description);
 
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", [error description]);
    }];
    
}



// update device for jamba juice

-(void)updateDevice
{
    AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
    
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
    [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];
    
    NSString * memberid;
    NSString * deviceid;
    NSString * pushToken;
    NSString * storeCode;
    NSMutableDictionary * dict1 = [NSMutableDictionary new];
    
    if (GuestCustomer != nil)
    {
      memberid = GuestCustomer.memberid;
      deviceid = GuestCustomer.deviceId;
      pushToken = GuestCustomer.pushToken;
        
    }
    else
    {
        memberid  = currentCustomer.customerID;
        deviceid  = currentCustomer.deviceID;
        pushToken = currentCustomer.pushToken;
        storeCode = currentCustomer.homeStoreID;
        NSLog(@"store code-------%@",storeCode);
        [dict1 setValue:storeCode forKey:@"storecode"];
    }
    
    
             [dict1 setValue:@"com.jamba.service" forKey:@"appId"];
             [dict1 setValue:[UIDevice currentDevice].model forKey:@"deviceType"];
             [dict1 setValue:[UIDevice currentDevice].systemVersion forKey:@"deviceOsVersion"];
        NSLog(@"memberid is %@ deviceid is %@ pushToken is %@",memberid,deviceid,pushToken);
        [dict1 setValue:memberid forKey:@"memberid"];
        [dict1 setValue:deviceid forKey:@"deviceId"];
        [dict1 setValue:pushToken forKey:@"pushToken"];
    
        NSLog(@"updateDevice description is %@",dict1.description);
    
    SERVER_URL = @"https://demo-fb.fishbowlcloud.com/";
    
    [manager POST:[SERVER_URL stringByAppendingString:@"clpapi/member/deviceUpdate"] parameters:dict1  success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"member Response SUCCESS JSON: response %@", responseObject);
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            NSDictionary *customerresponse=(NSDictionary*)responseObject;
            
            NSLog(@"ClpmemberDescription----------%@",customerresponse);
            
            
            [[NSUserDefaults standardUserDefaults]setValue:memberid forKey:@"Newmemberid"];
            
            [[NSUserDefaults standardUserDefaults]setValue:deviceid forKey:@"NewdeviceId"];
            [[NSUserDefaults standardUserDefaults]setValue:pushToken forKey:@"NewpushToken"];
            
            GuestCustomer = nil;
//           currentCustomer.customerID=[[customerresponse valueForKey:@"memberid"] intValue];
//
//            [[NSUserDefaults standardUserDefaults]setValue:[customerresponse valueForKey:@"memberid"] forKey:@"Newmemberid"];
//            [[NSUserDefaults standardUserDefaults]setValue:[customerresponse valueForKey:@"deviceId"] forKey:@"NewdeviceId"];
//            [[NSUserDefaults standardUserDefaults]setValue:[customerresponse valueForKey:@"pushToken"] forKey:@"NewpushToken"];
//            [[NSUserDefaults standardUserDefaults]synchronize];
        }
        else
        {
            
        }
        NSLog(@"CreateApi----------%@",currentCustomer.description);
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", [error description]);
}];
    
}

// process push message - check push and pass

-(void)processPushMessage:(NSDictionary*)userInfo{
    @try{
        
        NSLog(@"In processPushMessage......");
        NSString *url = [userInfo objectForKey:@"url"];//passbook url
        [[NSUserDefaults standardUserDefaults]setValue:url forKey:@"urlKey"];

        NSString *ntype = [userInfo objectForKey:@"ntype"];//ntype
        NSString *alert=[[userInfo objectForKey:@"aps"] objectForKey:@"alert"];//message
        NSString *badge;
        NSString *offerid;
        NSString *eoid;
        NSString *custid;
        NSString *clpnid=@"";
        NSString *promoCode;
        NSString *strMessageType;
        
        NSString *sound;
        
        NSLog(@"push user info detail--------%@",userInfo);
        
        if([[userInfo objectForKey:@"aps"] valueForKey:@"badge"]){
            badge=[[userInfo objectForKey:@"aps"] objectForKey:@"badge"];
        }
        if([[userInfo objectForKey:@"aps"] objectForKey:@"sound"]){
            sound =[[userInfo objectForKey:@"aps"] objectForKey:@"sound"];
        }
        if([userInfo objectForKey:@"offerid"]){
            offerid=[userInfo objectForKey:@"offerid"];
        }
        if([userInfo objectForKey:@"eoid"]){
            eoid= [userInfo objectForKey:@"eoid"];
        }
        if([userInfo  objectForKey:@"custid"]){
            custid=[userInfo  objectForKey:@"custid"];
        }
        if([userInfo  objectForKey:@"clpnid"]){
            clpnid=[userInfo  objectForKey:@"clpnid"];
        }
        
        if([userInfo  objectForKey:@"mt"]){
            strMessageType=[userInfo  objectForKey:@"mt"];
        }
        NSLog(@"ntype is %@",ntype);
        
//        if(url){
            if(([ntype caseInsensitiveCompare:@"pass"]==NSOrderedSame)){
                NSLog(@"Push with Pass received:%@.. opening passbook",url);
                
                NSLog(@"before Offer is %@",offerid);
                
               // [self.delegate openDynamicPassViaPN:offerid];
                
                NSString * str = [NSString stringWithFormat:@"/mobile/getPass"];
                NSMutableDictionary * dicValue = [[NSMutableDictionary alloc]init];
                [dicValue setValue:custid forKey:@"customerID"];
                [dicValue setValue:offerid forKey:@"campaignId"];
                
                NSLog(@"dicValue is %@",dicValue.description);
                
                ApiClasses * apiCall;
                apiCall=[ApiClasses sharedManager];

                // get offer api
                [apiCall PassOpen:dicValue url:str withTarget:self withSelector:@selector(getPass:)];
                
                
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"isPushBackGround"];
                
                NSLog(@"self.delegate is %@",self.delegate);
                
                
                staticPassURL = [NSURL URLWithString:url];
                NSLog(@"staticPassURL is %@",staticPassURL);
                
            }else if([ntype caseInsensitiveCompare:@"web"]==NSOrderedSame){
                NSLog(@"Push with URL received:%@ Opening browser",url);
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
            }
//        }
        if(alert){
            //do not do anything
            if(([ntype caseInsensitiveCompare:@"pass"]!=NSOrderedSame))
            {
                NSLog(@"Push Message received:%@",alert);
                promoCode = [userInfo objectForKey:@"pc"];
                
                NSLog(@"Promo Code is %@",promoCode);
                
                NSLog(@"self.delegate is %@",self.delegate);

                //[[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"isPushBackGround"];
                [delegate clpPushDataBinding:promoCode withId:offerid withMessageType:strMessageType];
            }
        }
        
//        NSMutableDictionary *clpEvent=[[NSMutableDictionary alloc]init];
//        [clpEvent setValue:[NSString stringWithFormat:@"CLPEvent"] forKey:@"action"];
//        [clpEvent setValue:[NSNumber numberWithLong:currentCustomer.companyID] forKey:@"companyid"];
//         [clpEvent setValue:[[NSNumber alloc] initWithFloat:currentLocation.coordinate.latitude] forKey:@"lat"];
//        [clpEvent setValue:[[NSNumber alloc] initWithFloat:currentLocation.coordinate.longitude] forKey:@"lon"];
//        if(currentCustomer && currentCustomer.customerID && currentCustomer.customerID!=0){
//            [clpEvent setValue:currentCustomer.customerID forKey:@"customerid"];
//        }else{
//            [clpEvent setValue:custid forKey:@"customerid"];
//        }
//        
//        [clpEvent setValue:[self stringFromDate:[NSDate date]] forKey:@"event_time"];
//        [clpEvent setValue:clpnid forKey:@"notifid"];
//        [clpEvent setValue:offerid forKey:@"offerid"];
//        [clpEvent setValue:offerid forKey:@"item_id"];
//        [clpEvent setValue:alert forKey:@"item_name"];
//        
//        
//        //        [clpEvent setValue:@"" forKey:@"storeid"];
//        //        [clpEvent setValue:channelName forKey:@"channel_name"];
//        //        [clpEvent setValue:@"" forKey:@"event_trig"];
//        [clpEvent setValue:currentCustomer.deviceType forKey:@"device_type"];
//        [clpEvent setValue:currentCustomer.deviceOsVersion forKey:@"device_os_ver"];
//        //        [clpEvent setValue:@"" forKey:@"device_carrier"];
//        
//        NSMutableDictionary *pushopen = [[NSMutableDictionary alloc]initWithDictionary:clpEvent];
//        [pushopen setValue:@"PUSH_OPEN" forKey:@"event_name"];
//        [self updateOfferEvent:pushopen];
//        
//        [clpEvent setValue:@"ACCEPT_OFFER" forKey:@"event_name"];
//        [self performSelector:@selector(updateOfferEvent:) withObject:clpEvent afterDelay:2.0];
        
    }
    @catch(NSException *exception){
        NSLog(@"ProcessPushMessage error: %@",exception);
    }
}


-(void)getPass:(id)response
{
   // [obj removeLoadingView:self.view];
    
    NSLog(@"response pass success---------%@",response);
    clpsdk * obj22 = [[clpsdk alloc]init];
    [obj22 openPassbookAndShowwithData:response];
}

-(NSString*)stringFromDate:(NSDate*)date{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ssZ"];
    
    //Optionally for time zone conversions
    //    [formatter setTimeZone:[NSTimeZone timeZoneWithName:@"..."]];
    
    NSString *stringFromDate = [formatter stringFromDate:date];
    return stringFromDate;
}

-(void)openPassbookAndShow:(NSURL*)url
{
    @try
    {
        [delegate clpOpenPassbook];//starts loader
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, (unsigned long) NULL), ^{
            if(url==nil){
                [delegate clpClosePassbook:@"No URL present to open a pass"];
                return;
            }
            
            [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
            NSError *error;
            NSData *data = [NSData dataWithContentsOfURL:url options:NSDataReadingUncached error:&error];
            
            NSLog(@"passbook length : %d",(int)[data length]);
            
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
                // Generate the file path
                NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
                NSString *documentsDirectory = [paths objectAtIndex:0];
                NSString *dataPath = [documentsDirectory stringByAppendingPathComponent:@"pass.zip"];
                NSLog(@"dataPath is %@", dataPath);
                NSLog(@"documentsDirectory is %@", documentsDirectory);
                
//                 NSString *passPath = [[NSBundle mainBundle] pathForResource:@"boardingPass" ofType:@"pkpass"];
//                [data writeToFile:passPath atomically:YES];

                // Save it into file system
               [data writeToFile:dataPath atomically:YES];
                
            });
            
            if(error){
                NSLog(@"pass error %@",[error localizedDescription]);
                [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
                [delegate clpClosePassbook:error.debugDescription];
                return;
            }
            error = nil;
            dispatch_async(dispatch_get_main_queue(), ^{
                NSError *error;
                //init a pass object with the data
                PKPass *pass = [[PKPass alloc] initWithData:data error:&error];
                
                if(error || pass==nil){
               
                    [delegate clpClosePassbook:@"Invalid Pass"];

                }else{
                    //check if pass library contains this pass already
                    if([_passLib containsPass:pass]) {
                        //pass already exists in library, show an error message
                        [delegate clpClosePassbook:@"The pass you are trying to add to Passbook is already present."];
                    } else {
                        NSLog(@"Opening Passbook");
                        
                       // [delegate clpClosePassbook:@""];
                        
                        //present view controller to add the pass to the library
                        PKAddPassesViewController *vc = [[PKAddPassesViewController alloc] initWithPass:pass];
                        [vc setDelegate:nil];
                        if(vc!=nil){
                            UIViewController *currentviewctrl = [clpsdk getTopController];
                            if(currentviewctrl){
                                if ([currentviewctrl respondsToSelector:@selector(presentViewController:animated:completion:)]){
                                    [currentviewctrl presentViewController:vc animated:YES completion:nil];
                                } else {
                                    [currentviewctrl presentModalViewController:vc animated:YES];
                                }
                            }
                        }
                    }
                }
                
                [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
            });
        });
    }
    @catch (NSException *exception)
    {
        [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    }
}


#pragma mark - openPass Book With Data

-(void)openPassbookAndShowwithData:(NSData*)data
{
    ModelClass * obj = [ModelClass sharedManager];
       [obj  RemoveBottomView];
    
    @try
    {
        [delegate clpOpenPassbook];//starts loader
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, (unsigned long) NULL), ^{
            if(data==nil){
                [delegate clpClosePassbook:@"No URL present to open a pass"];
                return;
            }
            
            [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
            NSError *error;
            
            NSLog(@"passbook length : %d",(int)[data length]);
            
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
                // Generate the file path
                
                NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
                NSString *documentsDirectory = [paths objectAtIndex:0];
                NSString *dataPath = [documentsDirectory stringByAppendingPathComponent:@"pass.zip"];
                
//                NSURL *rtfUrl = [[NSBundle mainBundle] URLForResource:@"pass" withExtension:@".pkpass"];
//                
//                NSString * str = [NSString stringWithFormat:@"%@",rtfUrl];
              
                
                NSLog(@"dataPath is %@", dataPath);
               NSLog(@"documentsDirectory is %@", documentsDirectory);
                
                // Save it into file system
               [data writeToFile:dataPath atomically:YES];
               // [data writeToFile:str atomically:YES];
                
            });
            
            if(error){
                NSLog(@"pass error %@",[error localizedDescription]);
                [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
                [obj setBottomframe];
                [delegate clpClosePassbook:error.debugDescription];
                return;
            }
            error = nil;
            dispatch_async(dispatch_get_main_queue(), ^{
                NSError *error;
                //init a pass object with the data
                PKPass *pass = [[PKPass alloc] initWithData:data error:&error];
                
                if(error || pass==nil){
                    [obj setBottomframe];
                [delegate clpClosePassbook:@"Invalid Pass"];
                    
                }else{
                    //check if pass library contains this pass already
                    if([_passLib containsPass:pass]) {
                        //pass already exists in library, show an error message
                        //[obj setBottomframe];
                        [delegate clpClosePassbook:@"The pass you are trying to add to Passbook is already present."];
                        
                    } else {
                        NSLog(@"Opening Passbook");
                        
                        // [delegate clpClosePassbook:@""];
                        
                        //present view controller to add the pass to the library
                        PKAddPassesViewController *vc = [[PKAddPassesViewController alloc] initWithPass:pass];
                        [vc setDelegate:nil];
                        if(vc!=nil){
                            
                            UIViewController *currentviewctrl = [clpsdk getTopController];
                            if(currentviewctrl){
                                if ([currentviewctrl respondsToSelector:@selector(presentViewController:animated:completion:)]){
                                    [currentviewctrl presentViewController:vc animated:YES completion:nil];
                                }
                                else
                                {
                                    [currentviewctrl presentModalViewController:vc animated:YES];
                                }
                            }
                        }
                    }
                }
                
                [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
            });
        });
    }
    @catch (NSException *exception)
    {
        [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    }
}


+ (UIViewController*) getTopController
{
    UIViewController *topController = [UIApplication sharedApplication].keyWindow.rootViewController;
    while (topController.presentedViewController) {
        topController = topController.presentedViewController;
    }
    return topController;
}
-(void)updateLocation:(CLPMobileEvent *)event{
    NSLog(@"In updateLocation");
    
    // avoid same location
    if(lastlocation_updatetoserver && (float)lastlocation_updatetoserver.coordinate.latitude==(float)currentLocation.coordinate.latitude
       &&
       (float)lastlocation_updatetoserver.coordinate.longitude==(float)currentLocation.coordinate.longitude){
        NSLog(@" updateLocation is ignored because of same location");
        return;
    }
    lastlocation_updatetoserver = currentLocation;
    
    // avoid frequent update
    if(lastlocation_updatetime && fabs([lastlocation_updatetime timeIntervalSinceNow])<LOCATION_UPDATE_PING_FREQUENCY){
        NSLog(@" updateLocation is ignored because of time");
        return;
    }
    lastlocation_updatetime = [NSDate date];
    
    AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
    [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];
    [manager POST:[SERVER_URL stringByAppendingString:@"clpapi/mobile/locationchanged"] parameters:[event toDictionary] success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"SUCCESS JSON: response %@", responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", [error description]);
    }];
}


-(void)updateAppEvent:(NSDictionary *)submitAppEvents{
    
    if (isTriggerAppEvent == YES)
    {
        
    if(arrAppEvent==nil)
    {
        arrAppEvent = [[NSMutableArray alloc]init];
    }
    
    NSMutableDictionary *mydict=[[NSMutableDictionary alloc]init];
    @try {
        
        NSLog(@"In updateAppEvent");
        for(NSString *key in [submitAppEvents allKeys]) {
            NSLog(@"Inputs:  %@",[submitAppEvents objectForKey:key]);
        }
        mydict=[submitAppEvents mutableCopy];
        
        [mydict setValue:@"AppEvent" forKey:@"action"];
        
        NSLog(@"coordinate coordinate CurrentCustomer-------%f",currentLocation.coordinate.latitude);
        [mydict setValue:[[NSNumber alloc] initWithFloat:currentLocation.coordinate.latitude] forKey:@"lat"];
        
        [mydict setValue:[[NSNumber alloc] initWithFloat:currentLocation.coordinate.longitude] forKey:@"lon"];
 
            NSString * str = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];
            [mydict setValue:str forKey:@"memberid"];
            [mydict setValue:@"1173" forKey:@"tenantid"];
            
            [mydict setValue:[[NSString alloc] initWithString:[UIDevice currentDevice].model] forKey:@"device_type"];
            [mydict setValue:[[NSString alloc] initWithString:[UIDevice currentDevice].systemVersion] forKey:@"device_os_ver"];
       // }
        
            NSLog(@"updateAppEvent--------%@",mydict);
        
         //  NSLog(@"arrAllEvent--------%@",arrEventNewSetting);
        

            [arrAppEvent addObject:mydict];
       
        
         NSLog(@"arrReadEvent--------%@",arrAppEvent);
        
         NSArray * arr = arrAppEvent;
         [[NSUserDefaults standardUserDefaults]setObject:arr forKey:@"event"];
         [[NSUserDefaults standardUserDefaults]synchronize];
        
        
        [self appEventTimerMethodCall];
//        if(arrAppEvent.count>=intMaxEvents)
//        {
//            [self appEventTimerMethodCall];
//            [delegate fireTimer:intDelayTimer];
//        }
    }
    @catch (NSException *exception) {
        NSLog(@"Catched Error: %@", [exception description]);
    }
        
    }
}

// Timer call
-(void)appEventTimerMethodCall
{
    if (isTriggerAppEvent == YES)
    {
          NSLog(@"appEventTimerMethodCall");
        
    NSArray * arrEventRequest = [[NSUserDefaults standardUserDefaults]valueForKey:@"event"];
    if(arrEventRequest.count>0)
    {
        ApiClasses * api = [ApiClasses sharedManager];
        
       // TanentID
        AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
      //  access token
        NSString * accessToken = [[NSUserDefaults standardUserDefaults]valueForKey:@"access_token"];
        manager.requestSerializer = [AFJSONRequestSerializer serializer];
//      [manager.requestSerializer setValue:mobileAPIKey forHTTPHeaderField:@"CLP-API-KEY"];
        [manager.requestSerializer setValue:contentType forHTTPHeaderField:@"Content-Type"];
        [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
        [manager.requestSerializer setValue:TanentID forHTTPHeaderField:@"tenantid"];
        [manager.requestSerializer setValue:accessToken forHTTPHeaderField:@"access_token"];
        [manager.requestSerializer setValue:clientID forHTTPHeaderField:@"client_id"];
        [manager.requestSerializer setValue:ClientSecret forHTTPHeaderField:@"client_secret"];
        [manager.requestSerializer setValue:[api deviceID] forHTTPHeaderField:@"deviceId"];
        NSLog(@"appEventTimerMethodCall1");
        
        NSLog(@"access token events ---------- %@",accessToken);
         NSLog(@"access token events ---------- %@",manager.description);
        NSLog(@"requested array is %@",arrEventRequest);
        NSMutableDictionary *dictPost = [[NSMutableDictionary alloc]init];
        [dictPost setValue:arrAppEvent forKey:@"mobileAppEvent"];
        NSLog(@"dictPost.description is %@",dictPost.description);
        [manager POST:[SERVER_URL stringByAppendingString:@"clpapi/event/submitallappevents"] parameters:dictPost success:^(AFHTTPRequestOperation *operation, id responseObject) {
            NSLog(@"SUCCESS JSON: response clpapi/mobile/submitappevents %@", responseObject);
            [arrAppEvent removeAllObjects];
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"event"];
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"Error: %@", [error description]);
        }];
    }
    }
}

-(void)updateOfferEvent:(NSDictionary *)submitClpEvents{
    
    NSLog(@" updateOfferEvent is calling");
    
    if (isTriggerAppEvent == YES) {
    AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
        [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
        [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];
        [manager POST:[SERVER_URL stringByAppendingString:@"clpapi/mobile/submitclpevents"] parameters:submitClpEvents success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", [error description]);
    }];
        
    }
}

-(void)updateOfferEventWithPass:(NSDictionary *)submitClpEvents{
    
    NSLog(@" updateOfferEvent is calling");
    
    if (isTriggerAppEvent == YES)
    {
        NSMutableDictionary *mydict=[[NSMutableDictionary alloc]init];
        NSLog(@"In updateAppEvent");
        for(NSString *key in [submitClpEvents allKeys]) {
            NSLog(@"Inputs:  %@",[submitClpEvents objectForKey:key]);
        }
        mydict=[submitClpEvents mutableCopy];
        
        [mydict setValue:@"CLPEvent" forKey:@"action"];
        
        
        
        if([[NSUserDefaults standardUserDefaults]boolForKey:@"CurrentEvents"] == NO)
        {
            [mydict setValue:GuestCustomer.memberid forKey:@"memberid"];
            [mydict setValue:GuestCustomer.tenantid forKey:@"tenantid"];
    
            [mydict setValue:[[NSNumber alloc] initWithFloat:currentLocation.coordinate.latitude] forKey:@"lat"];
            [mydict setValue:[[NSNumber alloc] initWithFloat:currentLocation.coordinate.longitude] forKey:@"lon"];
            
           if(GuestCustomer != nil && GuestCustomer.deviceType != nil)
           {
            [mydict setValue:[[NSString alloc] initWithString:GuestCustomer.deviceType] forKey:@"device_type"];
            [mydict setValue:[[NSString alloc] initWithString:GuestCustomer.deviceOsVersion] forKey:@"device_os_ver"];}
            
           }
        else if ([[NSUserDefaults standardUserDefaults]boolForKey:@"CurrentEvents"] == YES)
        {
  
            NSLog(@"coordinate-------%f",currentLocation.coordinate.latitude);
            [mydict setValue:[[NSNumber alloc] initWithFloat:currentLocation.coordinate.latitude] forKey:@"lat"];
            
            [mydict setValue:[[NSNumber alloc] initWithFloat:currentLocation.coordinate.longitude] forKey:@"lon"];
           // NSString * str = [[NSUserDefaults standardUserDefaults]valueForKey:@"Newmemberid"];
            NSString * str = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];

            
            [mydict setValue:str forKey:@"memberid"];
            [mydict setValue:@"1173" forKey:@"tenantid"];
            
            
            //[mydict setValue:[[NSString alloc] initWithString:[UIDevice currentDevice].model] forKey:@"device_type"];
            [mydict setValue:[[NSString alloc] initWithString:[UIDevice currentDevice].systemVersion] forKey:@"device_os_ver"];
            
            
//            [mydict setValue:[[NSString alloc] initWithString:currentCustomer.deviceType] forKey:@"device_type"];
//            [mydict setValue:[[NSString alloc] initWithString:currentCustomer.deviceOsVersion] forKey:@"device_os_ver"];
        }
        
        NSLog(@"updateAppEvent--------%@",mydict);
        AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
        manager.requestSerializer = [AFJSONRequestSerializer serializer];
        [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
        [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];
        [manager POST:[SERVER_URL stringByAppendingString:@"clpapi/mobile/submitclpevents"] parameters:mydict success:^(AFHTTPRequestOperation *operation, id responseObject) {
            
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"Error: %@", [error description]);
        }];
        
    }
}


//GPS

# pragma mark - Beacon Monitoring Methods Start
-(void)StartBeaconRegionMonitoring:(NSString *)storeId {
    
    NSLog(@"in StartBeaconRegionMonitoring");
    
    /*init location manager and set the delegate*/
    if (locationManager == nil) {
        locationManager = [[CLLocationManager alloc] init];
        locationManager.delegate = self;
    } else {                                                    //If Beacon region is already monitored exit
        if (self.beaconRegion)                                  //Assumption here is that the tenant uses only one UUID for all stores
            if ([[locationManager monitoredRegions] containsObject:self.beaconRegion])
                return;
    }
    
    /*Get All beacons for the store*/
    NSString *apiPath = [@"clpapi/mobile/beacons/" stringByAppendingString:storeId];
    NSString *restURL = [SERVER_URL stringByAppendingString:apiPath];
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
    [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];
    
    [manager GET:restURL parameters:nil
         success:^(AFHTTPRequestOperation *operation, id responseObject) {
             NSLog(@"%@ - SUCCESS JSON: response %@",restURL,responseObject);
             
             NSDictionary *restResponse=(NSDictionary*)responseObject;
             if ([restResponse valueForKey:@"successFlag"]) {
                 
                 if ([[restResponse valueForKey:@"beacons"] isKindOfClass:[NSArray class]]){
                     NSArray *arraydic=[restResponse objectForKey:@"beacons"];
                     
                     if (self.storeBeacons==nil) {
                         self.storeBeacons = [[NSMutableArray alloc] init];
                     } else {[storeBeacons removeAllObjects];}
                     
                     for(NSDictionary* beacon in arraydic){
                         NSUUID *beaconUUID = [[NSUUID alloc] initWithUUIDString:[beacon valueForKey:@"udid"]];
                         CLPBeacon *clpbeaconObj =[[CLPBeacon alloc]init];
                         clpbeaconObj.beaconUUID=beaconUUID;//[beacon valueForKey:@"udid"];
                         clpbeaconObj.beaconMajor=[beacon valueForKey:@"major"];
                         clpbeaconObj.beaconMinor=[beacon valueForKey:@"minor"];
                         clpbeaconObj.clpBeaconId=[beacon valueForKey:@"beaconId"];
                         clpbeaconObj.clpBeaconName=[beacon valueForKey:@"name"];
                         clpbeaconObj.clpBeaconStoreId=[beacon valueForKey:@"storeID"];
                         [storeBeacons addObject:clpbeaconObj];
                     }
                     NSLog(@"Number of beacons in store %lu",(unsigned long)storeBeacons.count);
                     
                     /*Create a region with only UUID and start monitoring the region*/
                     NSUUID *proximityUUID = [[storeBeacons firstObject] beaconUUID];
                     beaconRegion = [[CLBeaconRegion alloc] initWithProximityUUID:proximityUUID identifier:@"com.clp.beaconregion"];
                     if([[[UIDevice currentDevice]systemVersion]floatValue]>=8.0){
                         [locationManager requestAlwaysAuthorization];
                     }
                     [locationManager startMonitoringForRegion:self.beaconRegion];
                     NSLog(@"Beacon Region Monitoring Started %@", beaconRegion);
//                     [self showLocalNotification:[NSString stringWithFormat:@"Start UUID Beacon Region minitoring %@", beaconRegion]];
                 }
             }
         } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
             NSLog(@"%@ - Error: %@",restURL, [error description]);
         }];
}

//Delegate Implementation for Beacon Region Entry Event
- (void)HandleBeaconEnterRegion:(CLRegion *)region {
    
    NSLog(@"Beacon Region Entry. Will start Ranging for Beacons %@", region);
    
    if (isTriggerBeacon) {

    
    //Create Regions for each beacon and start Ranging beacons
    if (!beaconRegions) beaconRegions = [[NSMutableArray alloc]init];
    else [beaconRegions removeAllObjects];
    
    /*Get the Nearest store beacons*/
    int nearestStoreId = [self getNearestStoreId];
    if (nearestStoreId==0) {
        NSLog(@"No stores found to range for beacons");
        return;
    }
    NSString *nearestStrStoreId = [NSString stringWithFormat:@"%i",nearestStoreId];
    NSString *apiPath = [@"clpapi/mobile/beacons/" stringByAppendingString:nearestStrStoreId];
    NSString *restURL = [SERVER_URL stringByAppendingString:apiPath];
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
        [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [manager.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
        [manager.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];
        
    [manager GET:restURL parameters:nil
         success:^(AFHTTPRequestOperation *operation, id responseObject) {
             NSLog(@"%@ - SUCCESS JSON: response %@",restURL,responseObject);
             
             NSDictionary *restResponse=(NSDictionary*)responseObject;
             if ([restResponse valueForKey:@"successFlag"]) {
                 
                 if ([[restResponse valueForKey:@"beacons"] isKindOfClass:[NSArray class]]){
                     NSArray *arraydic=[restResponse objectForKey:@"beacons"];
                     
                     if (self.storeBeacons==nil) {
                         self.storeBeacons = [[NSMutableArray alloc] init];
                     } else {[storeBeacons removeAllObjects];}
                     
                     for(NSDictionary* beacon in arraydic){
                         NSUUID *beaconUUID = [[NSUUID alloc] initWithUUIDString:[beacon valueForKey:@"udid"]];
                         CLPBeacon *clpbeaconObj =[[CLPBeacon alloc]init];
                         clpbeaconObj.beaconUUID=beaconUUID;//[beacon valueForKey:@"udid"];
                         clpbeaconObj.beaconMajor=[beacon valueForKey:@"major"];
                         clpbeaconObj.beaconMinor=[beacon valueForKey:@"minor"];
                         clpbeaconObj.clpBeaconId=[beacon valueForKey:@"beaconId"];
                         clpbeaconObj.clpBeaconName=[beacon valueForKey:@"name"];
                         clpbeaconObj.clpBeaconStoreId=[beacon valueForKey:@"storeID"];
                         [storeBeacons addObject:clpbeaconObj];
                     }
                     NSLog(@"Number of beacons in store %lu",(unsigned long)storeBeacons.count);
                 }
             }
         } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
             NSLog(@"%@ - Error: %@",restURL, [error description]);
         }];
    
    /*Start Ranging beacons*/
    if (storeBeacons != nil) {
        [self stopMonitoringOutSideStoreRegion]; //stop location region monitoring for outside regions
        for (CLPBeacon* beacon in self.storeBeacons) {
            
            CLBeaconRegion *newbeaconRegion = [[CLBeaconRegion alloc] initWithProximityUUID:beacon.beaconUUID major:[beacon.beaconMajor unsignedShortValue] minor:[beacon.beaconMinor unsignedShortValue] identifier:[beacon.clpBeaconId stringValue]];
            
            [beaconRegions addObject:newbeaconRegion];
            [locationManager  startRangingBeaconsInRegion:newbeaconRegion];
        }
        
        NSLog(@"All Store Beacon Ranging Started %lu", (unsigned long)[beaconRegions count]);
    }
        
    }
}

-(void)stopRangingAllBeacons {
    if((beaconRegions) && (beaconRegions.count>0)) {
        for(CLBeaconRegion *lclBeaconRegion in beaconRegions) {
            [locationManager stopRangingBeaconsInRegion:lclBeaconRegion];
        }
    }
}

# pragma mark - Becon Monitoring Methods End


# pragma mark - Becon Monitoring Delegates

/*Delegate method to process when Becon found*/
-(void)locationManager:(CLLocationManager *)manager didRangeBeacons:(NSArray *)beacons
              inRegion:(CLBeaconRegion *)region {
    
    if((!beacons)||(beacons.count<1)) return;                    //No beacons in Range for the region
    
    int beaconId = [[region identifier] intValue];
    if (!beaconRegionsAlreadyFound)                               //Initialize beaconRegionsAlreadyFound if not already initialized
        beaconRegionsAlreadyFound = [[NSMutableArray alloc] init];
    
    if(lastbeacon_updatetime && fabs([lastbeacon_updatetime timeIntervalSinceNow])<BEACON_PING_FREQUENCY){
        if (![beaconRegionsAlreadyFound containsObject:region])  {
            [beaconRegionsAlreadyFound addObject:region];
        } else return;
    } else [beaconRegionsAlreadyFound removeAllObjects];        //Clear already found beacons since ping frequency has passed
    lastbeacon_updatetime = [NSDate date];
    
    //[self showLocalNotification: [NSString stringWithFormat:@"New Beacons in Range for region%@", [region identifier]]];
    NSLog(@" New Beacons in Range %d", beaconId);
    //[self DisplayMonitoredRegion]; //todo : for debugging remove after debug
    
    if (isTriggerBeacon == YES) {
 
    /*Send Beacon found event to server*/
    AFHTTPRequestOperationManager *httpReqopmgr = [AFHTTPRequestOperationManager manager];
    httpReqopmgr.requestSerializer = [AFJSONRequestSerializer serializer];
        [httpReqopmgr.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [httpReqopmgr.requestSerializer setValue:@"mobilesdk" forHTTPHeaderField:@"Application"];
        [httpReqopmgr.requestSerializer setValue:@"1173" forHTTPHeaderField:@"tenantid"];
        
    NSString *restURL = [SERVER_URL stringByAppendingString:@"clpapi/mobile/beaconsinrange"];
    
    CLPMobileEvent *beaconEvt = [[CLPMobileEvent alloc] init];
    beaconEvt.action=@"BeaconInRange";
    beaconEvt.company=currentCustomer.companyID;
    beaconEvt.customerid=currentCustomer.customerID;
    beaconEvt.beaconid=beaconId;
    beaconEvt.device_os_ver=currentCustomer.deviceOsVersion;
    beaconEvt.device_type=currentCustomer.deviceType;
    beaconEvt.device_carrier=@"";
    beaconEvt.lat=currentLocation.coordinate.latitude;
    beaconEvt.lon=currentLocation.coordinate.longitude;
    
    [httpReqopmgr POST:restURL parameters:[beaconEvt toDictionary]
               success:^(AFHTTPRequestOperation *operation, id responseObject) {
                   NSLog(@"%@ - SUCCESS JSON: response %@",restURL, responseObject);
                   
               }
               failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                   NSLog(@"%@ - Error: %@",restURL, [error description]);
               }];
    }
}

# pragma mark  - Beacon Monitoring Delegates End


# pragma mark - Location Manager Delegate
-(void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status{
    
    if([[[UIDevice currentDevice]systemVersion]floatValue]>=8.0){
        switch (status) {
            case kCLAuthorizationStatusAuthorizedAlways:
                [locationManager startUpdatingLocation];
                break;
            case kCLAuthorizationStatusNotDetermined:
                if([locationManager respondsToSelector:@selector(requestAlwaysAuthorization)]){
                    [locationManager requestAlwaysAuthorization];
                }
                break;
            case kCLAuthorizationStatusDenied:
            case kCLAuthorizationStatusRestricted:
                // location update denied
                break;
            default:
                break;
        }
    }else{
        switch (status) {
            case kCLAuthorizationStatusAuthorized:
                [locationManager startUpdatingLocation];
                break;
            case kCLAuthorizationStatusNotDetermined:
                if([locationManager respondsToSelector:@selector(requestAlwaysAuthorization)]){
                    [locationManager requestAlwaysAuthorization];
                }
                break;
            case kCLAuthorizationStatusDenied:
            case kCLAuthorizationStatusRestricted:
                // location update denied
                break;
            default:
                break;
        }
    }
}

// new location update
-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations{
    NSLog(@"Location Manager Called delegate");
    
    
    [self stopStandardUpdate];

    if(locations.count==0)return;
    if(!currentLocation){
        currentLocation = [locations lastObject];
    }
    currentLocation = [locations firstObject];
    
    if (startTransientRegion) {
        currentLocation = [locations lastObject];
        transientRegion = [self registerRegionWithCircularOverlay:[MKCircle circleWithCenterCoordinate:[currentLocation coordinate] radius:100] andIdentifier: @"com.clp.transientRegion"];
        startTransientRegion = false;
    }
    
    NSDictionary * dict;
    [dict setValue:@"LocationChange" forKey:@"event_name"];
    [self updateAppEvent:dict];

    
    NSTimeInterval lastUpdateInterval=[self storeUpdateTimeInterval];
    if(lastUpdateInterval>=STORE_REFRESH_TIME){
        [self getAllStore:true :^(BOOL success, NSError *error) {
            NSLog(@"Store List Refreshed :%d %lu",success, (unsigned long)storeList.count);
            isStoreListPopulated=true;

            if(storeList!=nil && storeList.count!=0){
                [self getNearestStores:currentLocation];
            }
        }];
    }
    
 
    if([self isTimeForInsideRegionCheck]) {                           //If First time app launch or more than region check threshold
        [self checkInsideStoreRegion:currentLocation];                //check user inside region
    }                                                                 //Else rely on Region monitoring to find out in Region or out region
    
    if(isInRegion && isTriggerGeoFence == YES){                                                   //send location update to server only when user in region
        CLPMobileEvent *locationEvent=[[CLPMobileEvent alloc]init];
        locationEvent.company=currentCustomer.companyID;
        locationEvent.customerid=[currentCustomer.customerID integerValue];
        locationEvent.device_os_ver=currentCustomer.deviceOsVersion;
        locationEvent.device_type=currentCustomer.deviceType;
        locationEvent.device_carrier=@"";
        locationEvent.lat=currentLocation.coordinate.latitude;
        locationEvent.lon=currentLocation.coordinate.longitude;
        locationEvent.action=@"LocationChange";
        [self updateLocation:locationEvent];
    }else{
        
        NSLog(@"Location update outside the region of store");
    }
    
    
    if (isStoreListPopulated){                                    //Store list is populated and outer all store,
        [self stopStandardUpdate];
        [self startLocationBasedRegionMonitoring];
    }
    
    
    // Becon Monitoring
    
    if(isFirstTimeAppLaunch && isInRegion){                            //Start Beacon Region Monitoring
        [self startMonitoringInsideStoreRegions];
        if (inRegionStoreList!=nil && inRegionStoreList.count!=0) {
            for (CLPStore *store in inRegionStoreList) {
                [self StartBeaconRegionMonitoring:[NSString stringWithFormat:@"%d",store.storeID]];
            }
        }
        isFirstTimeAppLaunch=false;
    }else if(isInRegion){
        //Do Nothing
    }
    
}

// get all stores from store api
-(void)getALLStoreList
{
    NSLog(@"ingetstoreListFirstTime");
//    

    NSUserDefaults *defaults1 = [NSUserDefaults standardUserDefaults];
    NSData *encodedObject1 = [defaults1 objectForKey:@"storeArray"];
    NSArray *object = [NSKeyedUnarchiver unarchiveObjectWithData:encodedObject1];
    
    [self->storeList addObjectsFromArray:object];
    
    if (storeList.count == 0)
    {
        [self getAllStore:true :^(BOOL success, NSError *error) {
            
            if (error == nil) {
            
        NSLog(@"First  Time Store List Came :%d %lu",success, (unsigned long)storeList.count);
        isStoreListPopulated=true;
            
            if(storeList!=nil && storeList.count!=0){
                [self getNearestStores:currentLocation];
            }
                [self.delegate storeListResponseSucceed:storeList];
            }
            else
            {
                [self.delegate storeListResponseFailed];
            }
   
        }];
        
    }
    else
    {
        [self.delegate storeListResponseSucceed:storeList];

    }
}

-(void)locationManger:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    NSLog(@"didFailWithError: %@", error);
}

-(void)locationManager:(CLLocationManager *)manager didEnterRegion:(CLRegion *)region{
    isInRegion=true;
    CLPStore *currentStore = nil;
    
    if ([[region identifier] isEqualToString:@"com.clp.transientRegion"]) {
        return;
    }
    
    if (![[region identifier] isEqualToString: @"com.clp.beaconregion"]) {              //If location region entry
        startTransientRegion = true;
        if ((storeList != nil) && (storeList.count > 0)) {
            for (CLPStore *store in storeList) {                                        //Determine store for the entered region
                if ([[store storeName]isEqualToString:[region identifier]]) {           // And Add to the inRegionStoreList
                    currentStore = store;
                    if (inRegionStoreList == nil){
                        inRegionStoreList = [[NSMutableArray alloc] init];}
                    [inRegionStoreList addObject:store];
                    NSLog(@"Added Store %@ to inRegionStoreList", store.storeName);
                    break;
                }
            }
        }
        [locationManager startUpdatingLocation];
        if (currentStore != nil)
            [self StartBeaconRegionMonitoring:                                  //Start Beacon Region Monitoring
             [NSString stringWithFormat:@"%d",currentStore.storeID]];
    } else {                                                                    //Beacon Region Entry
        [self HandleBeaconEnterRegion:region];                                  //Start Beacon Ranging on Beacon region Entry
    }
}

-(void)locationManager:(CLLocationManager *)manager didExitRegion:(CLRegion *)region{
    
    if ([[region identifier] isEqualToString: @"com.clp.beaconregion"]) {       //Exited a beacon Region
        //[locationManager stopRangingBeaconsInRegion:beaconRegion];            //Stop Ranging Beacons
        [self stopRangingAllBeacons];
        beaconRegionsAlreadyFound = nil;                                        //Reset Found beacon Regions
//        [self showLocalNotification:[NSString stringWithFormat:@" Exit Beacon Region %@",region.identifier]];
        NSLog(@"Exited Beacon Region %@",region);
        
    }else if([[region identifier] isEqualToString:@"com.clp.transientRegion"]) { //Exited a Transient region
        startTransientRegion = true;
        [locationManager startUpdatingLocation];
        
    }else {                                                                        //Exited a location Region
//        [self showLocalNotification:[NSString stringWithFormat:@"Exit region:%@",region.identifier]];
        NSLog(@"Exit region:%@",region.identifier);
        for (CLPStore *store in inRegionStoreList) {
            if ([[store storeName] isEqualToString:[region identifier]]) {
                [inRegionStoreList removeObject:store];
                break;
            }
        }
        if ([inRegionStoreList count]==0) {
            isInRegion=false;
            [locationManager stopMonitoringForRegion: beaconRegion];        //Stop Beacon region monitoring
            [locationManager stopUpdatingLocation];                         //Stop GPS location pdate if outside of all regions
            [self startLocationBasedRegionMonitoring];                      //Start location region monitoring
            NSLog(@"Stopped Monitoring Beacon Region. Started monitoring normal location region");
        }
    }
}

-(void)locationManager:(CLLocationManager *)manager monitoringDidFailForRegion:(CLRegion *)region withError:(NSError *)error{
//    [self showLocalNotification:[error localizedDescription]];
}

- (void)locationManager:(CLLocationManager *)manager
      didDetermineState:(CLRegionState)state forRegion:(CLRegion *)region{
    
    switch (state) {
        case CLRegionStateInside:
            isInRegion=true;
            break;
        case CLRegionStateUnknown:
        case CLRegionStateOutside:
            
        default:
            //isInRegion=false;
        break; }
}

# pragma mark - Location Manager Delegate End


-(NSTimeInterval)storeUpdateTimeInterval{
    NSDate *currentTime = [NSDate date];
    NSTimeInterval timeInterval =  [currentTime timeIntervalSinceDate:storeUpdateTime];
    return timeInterval;//return in seconds
}

-(BOOL)isTimeForInsideRegionCheck{
    BOOL retValue = false;
    
    if (isFirstTimeAppLaunch) {
        lastInsideRegionCheck = [NSDate date];
        return true;
    }
    
    NSTimeInterval timeSinceLastInsideStoreCheck = [[NSDate date] timeIntervalSinceDate:lastInsideRegionCheck];
    if (timeSinceLastInsideStoreCheck > INSIDE_REGIONCHK_FREQ) {
        retValue = true;
    }
    
    lastInsideRegionCheck = [NSDate date];
    return retValue;
}


-(void)checkInsideStoreRegion:(CLLocation*)userLocation{
    isInRegion=false;
    for(CLPStore *store in storeList){
        CLLocation *storeLocation= [[CLLocation alloc]initWithLatitude:[store.latitude floatValue] longitude:[store.longitude floatValue]];
        if([userLocation distanceFromLocation:storeLocation]<=GEOFENCE_RADIUS){
            isInRegion=true;
            if (inRegionStoreList == nil)
                inRegionStoreList = [[NSMutableArray alloc] init];
            [inRegionStoreList addObject:store];
            //break;                                                        //User might be in multiple overlapping Geo location
        }                                                                   //Do Not Exit For loop
    }
}

-(void)stopStandardUpdate{
    NSLog(@" In stopStandardUpdate");
    [locationManager stopUpdatingLocation];
    [locationManager stopMonitoringSignificantLocationChanges];
}
-(void)startStandardUpdate{
    dispatch_async(dispatch_get_main_queue(), ^{
        NSLog(@"In startStandardUpdate");
        if(locationManager!=nil){
            [self stopMonitoringRegion];
            [locationManager stopUpdatingLocation];
            [locationManager stopMonitoringSignificantLocationChanges];
            locationManager.delegate = nil;
        }
        locationManager = [[CLLocationManager alloc]init];
        [locationManager setDelegate:self];
        [locationManager setDistanceFilter:DISTANCE_FILTER]; // in meters
        [locationManager setDesiredAccuracy:kCLLocationAccuracyBest];
        if([CLLocationManager locationServicesEnabled]){
            if([[[UIDevice currentDevice]systemVersion]floatValue]>=8.0){
                [locationManager requestAlwaysAuthorization];
            }
            [locationManager startUpdatingLocation];
            [locationManager startMonitoringSignificantLocationChanges];
        }
    });
}

-(void)stopAllLocationServices{
    if(locationManager!=nil){
        [self stopMonitoringRegion];
        [locationManager stopUpdatingLocation];
        //[locationManager stopMonitoringSignificantLocationChanges];
    }
}

-(void)stopMonitoringRegion{
    NSLog(@"In stopMonitoringRegion");
    for(CLRegion *monitored in [locationManager monitoredRegions])
        [locationManager stopMonitoringForRegion:monitored];
//    [self showLocalNotification:[NSString stringWithFormat:@" All Region Monitoring stopped"]];
}

-(void)stopMonitoringOutSideStoreRegion {
    for(CLRegion *monitored in [locationManager monitoredRegions]) {
        if(![[monitored identifier] isEqualToString:@"com.clp.beaconregion"]){          //Do not stop Beacon Region monitoring
            Boolean inSideRegion=false;
            for (CLPStore *inRegionStore in inRegionStoreList) {
                if([[monitored identifier] isEqualToString:[inRegionStore storeName]]) {
                    inSideRegion = true;
                    break;
                }
            }
            if (!inSideRegion)
                [locationManager stopMonitoringForRegion:monitored];
        }
    }
    
//    [self showLocalNotification:[NSString stringWithFormat:@"All Region Monitoring stopped except inRegion stores, to monitor Beacon Region"]];
}

-(void)DisplayMonitoredRegion {
    NSMutableString *monitoredRegions = [[NSMutableString alloc] init];
    for(CLRegion *monitored in [locationManager monitoredRegions]) {
        [monitoredRegions appendString:[monitored identifier]];
    }
//    [self showLocalNotification:monitoredRegions];
}

//calcualtions
# pragma mark - Region Monitoring

-(int)getNearestStoreDistance{
    if(nearestStoreList && nearestStoreList.count!=0){
        CLPStore *store=[nearestStoreList objectAtIndex:0];
        CLLocation *storeLocation1= [[CLLocation alloc]initWithLatitude:[store.latitude floatValue] longitude:[store.longitude floatValue]];
        return [currentLocation distanceFromLocation:storeLocation1];
    }else{
        return -1;
    }
}

-(NSString*)getNearestStoreName{
    if(nearestStoreList && nearestStoreList.count!=0){
        CLPStore *store=[nearestStoreList objectAtIndex:0];
        
        return store.storeName;
    }else{
        return @"OUTSIDE ALL-STORE";
    }
}

-(int)getNearestStoreId{
    if(nearestStoreList && nearestStoreList.count!=0){
        CLPStore *store=[nearestStoreList objectAtIndex:0];
        
        return store.storeID;
    }else{
        return 0;
    }
}

- (void)getNearestStores:(CLLocation *)userLocation;
{
    nearestStoreList=[[NSMutableArray alloc]init];//clear old list
    NSArray *unsorted=[[NSArray alloc]initWithArray:storeList];
    NSArray *sorted=[[NSArray alloc]init];
    sorted = [unsorted sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2)
              {
                  CLPStore *firstStore = (CLPStore *)obj1;
                  CLPStore *secondStore = (CLPStore *)obj2;
                  CLLocation *storeLocation1= [[CLLocation alloc]initWithLatitude:[firstStore.latitude floatValue] longitude:[firstStore.longitude floatValue]];
                  CLLocation *storeLocation2= [[CLLocation alloc]initWithLatitude:[secondStore.latitude floatValue] longitude:[secondStore.longitude floatValue]];
                  double firstValue =[userLocation distanceFromLocation:storeLocation1];
                  double secondValue = [userLocation distanceFromLocation:storeLocation2];
                  
                  if (firstValue < secondValue) return NSOrderedAscending;
                  else if (firstValue > secondValue) return NSOrderedDescending;
                  return NSOrderedSame;
                  //                  NSNumber *first = [NSNumber numberWithDouble:firstValue];
                  //                  NSNumber *second = [NSNumber numberWithDouble:secondValue];
                  //                  return [first compare:second]; // ascending
              }];
    
    nearestStoreList =[[NSMutableArray alloc]initWithArray:sorted];
    if(nearestStoreList.count>MAX_STORE_COUNT){
        nearestStoreList =[[NSMutableArray alloc]initWithArray:[sorted subarrayWithRange:NSMakeRange(0, MAX_STORE_COUNT)]];
    }
    //NSLog(@"Nearest Store List: %@",nearestStoreList);
}

-(void)startLocationBasedRegionMonitoring{
    NSLog(@"In startLocationBasedRegionMonitoring.....");
    [self stopMonitoringRegion];
    if([CLLocationManager locationServicesEnabled] && nearestStoreList!=nil && nearestStoreList.count!=0){
        CGFloat circular_radius = GEOFENCE_RADIUS;
        for(CLPStore *store in nearestStoreList){
            if ([store geoFenceCorrFactor]!=0)
                circular_radius = [store geoFenceCorrFactor] * GEOFENCE_RADIUS;
            // NSLog(@"Geofence radius for store: %@ is set to:%f",[store storeName], circular_radius);
            [self registerRegionWithCircularOverlay:[MKCircle circleWithCenterCoordinate:CLLocationCoordinate2DMake([store.latitude floatValue],[store.longitude floatValue]) radius:circular_radius] andIdentifier:store.storeName];
        }
//        [self showLocalNotification:[NSString stringWithFormat:@"All Location Region Monitoring Started %lu", (unsigned long)nearestStoreList.count]];
    }else{
        //[self stopStandardUpdateStartSignificantUpdate]; //Should be just StopStandardupdate. Subrat Commented
        [locationManager stopUpdatingLocation]; //Subrat Added
//        [self showLocalNotification:[NSString stringWithFormat:@"Standard Update stopped."]];
    }
}

- (CLRegion *)registerRegionWithCircularOverlay:(MKCircle*)overlay andIdentifier:(NSString*)identifier {
    
    //NSLog(@"in registerRegionWithCircularOverlay......");
    
    CLLocationDegrees radius = overlay.radius;                          // Clamp the radius to the max value.
    if (radius > locationManager.maximumRegionMonitoringDistance) {
        radius = locationManager.maximumRegionMonitoringDistance;
    }
    
    CLCircularRegion *geoRegion = [[CLCircularRegion alloc]             // Create the geographic region to be monitored.
                                   initWithCenter:overlay.coordinate
                                   radius:radius
                                   identifier:identifier];
    [locationManager startMonitoringForRegion:geoRegion];
    return geoRegion;
}

-(void)startMonitoringInsideStoreRegions {
    
    if([CLLocationManager locationServicesEnabled] && nearestStoreList!=nil && nearestStoreList.count!=0){
        if (inRegionStoreList!=nil && inRegionStoreList.count!=0) {
            CGFloat circular_radius = GEOFENCE_RADIUS;
            for (CLPStore *store in inRegionStoreList) {
                [self registerRegionWithCircularOverlay:[MKCircle circleWithCenterCoordinate:CLLocationCoordinate2DMake([store.latitude floatValue],[store.longitude floatValue]) radius:circular_radius] andIdentifier:store.storeName];
            }
        }
    }
    
//    [self showLocalNotification:[NSString stringWithFormat:@"Region Monitoring started for inside store regions total %lu regions", (unsigned long)inRegionStoreList.count]];
}

# pragma mark - Database

-(NSString*)getDBPath{
    NSString *documentdir = [[NSString alloc] initWithString:[NSSearchPathForDirectoriesInDomains(NSApplicationSupportDirectory, NSUserDomainMask, YES) lastObject]];
    NSString *dbname = @"CLPDatabase.xml";
    NSString *dbfullPath = [NSString stringWithFormat:@"%@/%@",documentdir,dbname];
    return dbfullPath;
}

-(void)openDatabase{
    NSString *dbfullPath = [self getDBPath];
    db = [[Database alloc]init];
    @try {
        NSFileManager *fileManager = [NSFileManager defaultManager];
        if([fileManager fileExistsAtPath:dbfullPath]){
            db = [NSKeyedUnarchiver unarchiveObjectWithFile:dbfullPath];
            if(db == nil){
                NSLog(@"Failed to open databse");
            }else{
                [self getCustomerFromDB];
            }
        }else{
            [self saveDatabase];
        }
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception.reason);
    }
}

-(void)getCustomerFromDB{
    currentCustomer = db.customer;
}

-(void)saveDatabase{
    NSString *dbfullPath = [self getDBPath];
    if([NSKeyedArchiver archiveRootObject:db toFile:dbfullPath] == YES)
        NSLog(@"DB Saved");
    else
        NSLog(@"Failed to Save DB");
}

-(void)logoutClpSdk{
    currentCustomer=[[CLPCustomer alloc]init];
    [self saveDatabase];
}

//-(void)stopStandardUpdateStartSignificantUpdate{
//    NSLog(@"stopStandardUpdateStartSignificantUpdate is calling...");
//    [locationManager stopUpdatingLocation];
//    [locationManager startMonitoringSignificantLocationChanges];
//    [self showLocalNotification:[NSString stringWithFormat:@"GPS Location Update stopped. SCL update started"]];
//}
//-(void)stopSignificantUpdateStartStandardUpdate{
//    NSLog(@"stopSignificantUpdateStartStandardUpdate is calling...");
//    [locationManager stopMonitoringSignificantLocationChanges];
//    [self startStandardUpdate];
//    [self showLocalNotification:[NSString stringWithFormat:@"SCL Stopped. GPS Location update started"]];
//}
//-(void)startRegionMonitoringInSuspendMode{
//    NSLog(@"Inside startRegionMonitoringInSuspendMode");
//    if(locationManager!=nil){
//        [self stopMonitoringRegion];
//        [locationManager stopUpdatingLocation];
//        [locationManager stopMonitoringSignificantLocationChanges];
//    }
//    NSLog(@"Initiating Location manager");
//
//    locationManager = [[CLLocationManager alloc]init];
//    [locationManager setDelegate:self];
//    [locationManager setDistanceFilter:200]; // in meters
//    [locationManager setDesiredAccuracy:kCLLocationAccuracyBest];
//    NSLog(@"Location manager initiation completed");
//
//    if([CLLocationManager locationServicesEnabled]){
//        if([[[UIDevice currentDevice]systemVersion]floatValue]>=8.0){
//            [locationManager requestAlwaysAuthorization];
//        }
//
//        Store *newStore = [[Store alloc] init];
//        newStore.storeName = @"New Mowry Store";
//        newStore.storeID=50;
////        newStore.latitude=1;
////        newStore.longitude=1
//
//        NSLog(@"Initited store. Starting Region Monitoring");
//        [self startMonitoringInsideStoreRegion:newStore];
//         NSLog(@"Started Region Monitoring");
//        [self showLocalNotification:[NSString stringWithFormat:@"Region Monitoring started for Store: %@", newStore.storeName]];
//    } else NSLog(@"Location Not Enabled.");
//}

@end
