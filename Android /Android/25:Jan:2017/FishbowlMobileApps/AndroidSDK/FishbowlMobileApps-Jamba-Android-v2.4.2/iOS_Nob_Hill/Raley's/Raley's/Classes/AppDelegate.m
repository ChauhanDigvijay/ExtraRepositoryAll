//
//  AppDelegate.m
//  Raley's
//
//  Created by Billy Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "AppDelegate.h"
#import "ShoppingList.h"
#import "ListRequest.h"
#import "PromoCategoriesRequest.h"
#import "MissingImagesRequest.h"

#import "SBJson.h"
#import "Store.h"
#import "Offer.h"
#import "OfferRequest.h"
#import "WebService.h"
#import "Utility.h"
#import "Logging.h"
#import "ShoppingScreenVC.h"
#import "LoginOptionScreenVC.h"
#import <CoreTelephony/CTTelephonyNetworkInfo.h>
#import <CoreTelephony/CTCarrier.h>
#import "Reachability.h"
#import <sys/sysctl.h>
#import "PassbookLoader.h"
#import "Raleys.h"

#define MAX_CACHE_FILES 5000
#define MAX_LOG_FILES 10


@implementation AppDelegate
{
    
}

@synthesize _imageDir;
@synthesize _shoppingScreenVC;
@synthesize _deviceType;
@synthesize _viewWidth;
@synthesize _viewHeight;
@synthesize _headerHeight;
@synthesize _footerHeight;
@synthesize _offerTableWidth;
@synthesize _offerTableCellHeight;
@synthesize _productTableWidth;
@synthesize _productTableCellHeight;
@synthesize _shoppingListTableWidth;
@synthesize _shoppingListTableCellHeight;
@synthesize _normalFont;
@synthesize _boldFont;
@synthesize _internetAvailable;
@synthesize _gpsAvailable;
@synthesize _retrievingShoppingList;
@synthesize _locateForAccount;
@synthesize _currentLocation;
@synthesize _currentView;
@synthesize _currentProduct;
@synthesize _productToAdd;
@synthesize _productToModify;
@synthesize _productToDelete;
@synthesize _productCategoriesFile;
@synthesize _promoCategoriesFile;
@synthesize _productCategories;
@synthesize _promoCategories;
@synthesize _currentShoppingList;
@synthesize _currentEcartPreOrderResponse;
@synthesize _currentAccountRequest;
@synthesize _currentAppointmentList;
@synthesize _acceptedOffersList;
@synthesize _personalizedOffersList;
@synthesize _extraFriendzyOffersList;
@synthesize _moreForYouOffersList;
@synthesize _allStoresList;
@synthesize _missingImagesList;
@synthesize _selectedShoppingList;
@synthesize _loginFont;
@synthesize beaconAlertView;
@synthesize API_BASE_URL;
@synthesize beaconSchedule;
@synthesize _clpSDK;
@synthesize token;

static AppDelegate* instance = nil;

-(id)init {
    self = [super init];
    if (self) {
        instance = self;
        
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        _nearbyNotificationsLastMessageTimestamp = 0;
        if ([defaults objectForKey:kNearbyNotificationsLastMessageTimestamp]) {
            _nearbyNotificationsLastMessageTimestamp = [defaults doubleForKey:kNearbyNotificationsLastMessageTimestamp];
        }
    }
    return self;
}



- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
//    _clpSDK=[clpsdk sharedInstanceWithAPIKey:@"5bccfcdc00b2639232feaa75ab73ba1e"]; //Dev Key
    _clpSDK=[clpsdk sharedInstanceWithAPIKey:@"941778af15702f75b8882931f60e3cd0"];    //Prod Key
    
    
    if ([launchOptions objectForKey:UIApplicationLaunchOptionsLocationKey]) {
        [_clpSDK startStandardUpdate];
    }
    
    //else {
        
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)
    {
        [application registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge) categories:nil]];
        [application registerForRemoteNotifications];
    }
    else
    {
        [application registerForRemoteNotificationTypes:UIRemoteNotificationTypeAlert|UIRemoteNotificationTypeBadge|UIRemoteNotificationTypeSound];
    }
        
        
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
        
    // part of the fix for the status bar overlap problem on iOS7, also see Info.plist 'View controller-based status bar appearance'
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        [application setStatusBarStyle:UIStatusBarStyleLightContent animated:YES];
        
    _currentShoppingList = nil;
    _selectedShoppingList = nil;
        
//        __myStoreRefreshFlag=false;
        
    _acceptedOffersList = [[NSMutableArray alloc] init];
    _personalizedOffersList = [[NSMutableArray alloc] init];
    _extraFriendzyOffersList = [[NSMutableArray alloc] init];
    _moreForYouOffersList = [[NSMutableArray alloc] init];
    _allStoresList = [[NSMutableDictionary alloc] init];
    _missingImagesList = [[NSMutableDictionary alloc] init];
    _imageThreadCountLock = [[NSObject alloc] init];
        
    _normalFont = kFontRegular;
    _boldFont = kFontBold;
    _loginFont=kFontLoginRegular;
        
    _currentLocation = CLLocationCoordinate2DMake(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        
        
    NSError *error;
    _fileManager = [NSFileManager defaultManager];
    _supportDir = [[NSString alloc] initWithString:[NSSearchPathForDirectoriesInDomains(NSApplicationSupportDirectory, NSUserDomainMask, YES) lastObject]];
    _documentDir = [[NSString alloc] initWithString:[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) lastObject]];
        
    // log file
    NSDateFormatter *logDateFormatter = [[NSDateFormatter alloc] init];
    [logDateFormatter setDateFormat:@"MMdd_HH.mm.ss"];
    NSString *dateString = [logDateFormatter stringFromDate:[NSDate date]];
    NSString *fileName =[NSString stringWithFormat:@"Raleys_%@.log", dateString];
    NSString *logFilePath = [_documentDir stringByAppendingPathComponent:fileName];
    freopen([logFilePath cStringUsingEncoding:NSASCIIStringEncoding], "a+", stderr);
        
    _productCategoriesFile = @"productCategories.xml";
    _promoCategoriesFile = @"promoCategories.xml";
    _persistentDataFile = @"appdata.xml";
    _persistentData = [[PersistentData alloc] init];
    [self getPersistentDataFromCache];
    _productCategories = [self getProductCategoriesFromCache:_productCategories file:_productCategoriesFile];
    _promoCategories = [self getProductCategoriesFromCache:_promoCategories file:_promoCategoriesFile];
        
    _imageDir = [_supportDir stringByAppendingPathComponent:@"Images"];
    
    if(![_fileManager fileExistsAtPath:_imageDir])
        [_fileManager createDirectoryAtPath:_imageDir withIntermediateDirectories:NO attributes:nil error:&error];
        
    if(![Utility isEmpty:_persistentData._storeList])
        _allStoresList = _persistentData._storeList;
        
        
    [Raleys shared];
        
    if([Utility isNetworkAvailable] == YES)
    {
        _internetAvailable = YES;
        [NSThread detachNewThreadSelector:@selector(getAllStoresThread:) toTarget:self withObject:nil];
            
        if([self isLoggedIn])
        {
            [self getAvailableOffers];
            [NSThread detachNewThreadSelector:@selector(getProductCategoriesThread:) toTarget:self withObject:PROMO_CATEGORIES_URL];
                
            if(![Utility isEmpty:_persistentData._currentShoppingListId])
                [NSThread detachNewThreadSelector:@selector(getCurrentShoppingListThread:) toTarget:self withObject:nil];
        }
    }
    else
    {
        _internetAvailable = NO;
        LogError(@"Internet connection is not available");
    }
    
    _managerDateFormatter = [[NSDateFormatter alloc] init];
    [_managerDateFormatter setDateStyle:NSDateFormatterShortStyle];
    [_managerDateFormatter setTimeStyle:NSDateFormatterShortStyle];
    [NSThread detachNewThreadSelector:@selector(startAppManagerThread:) toTarget:self withObject:nil];
        
    [self animateNextScreen];
        
    //Accept push notification when app is not open
    NSDictionary *remoteNotif = [launchOptions objectForKey: UIApplicationLaunchOptionsRemoteNotificationKey];
    if (remoteNotif) {
        [self application:application didReceiveRemoteNotification:remoteNotif];
    }
        
        
    [self performSelector:@selector(checkPushIsRegisterOrNot) withObject:nil afterDelay:5];
    
    return YES;
}



- (void)animateNextScreen
{
    // as this is the first visible screen we'll get the screen dimensions and set the common dimensions for components used by the app
    
    CGRect frame;
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        frame = CGRectMake(0, 20, self.window.frame.size.width, self.window.frame.size.height - 20);
    else
        frame = CGRectMake(0, 0, self.window.frame.size.width, self.window.frame.size.height-20);
    
    _viewWidth = frame.size.width; // default is portrait mode
    _viewHeight = frame.size.height; // default is portrait mode
    
    if(_viewWidth <= 320) // iPhone
    {
        if(_viewHeight >= 548)
        {
            _deviceType = IPHONE_5;
            _headerHeight = _viewHeight * .07;
            _footerHeight = _viewHeight * .05;
        }
        else
        {
            _deviceType = IPHONE;
            _headerHeight = _viewHeight * .08;
            _footerHeight = _viewHeight * .06;
        }
        _headerHeight=44.0f;
    }
    else // iPad
    {
        _deviceType = IPAD;
        _headerHeight = _viewHeight * .07;
        _footerHeight = _viewHeight * .05;
    }
    
    
    if([self isLoggedIn] == YES)
    {
        ShoppingScreenVC *shoppingScreenVC = [[ShoppingScreenVC alloc] init];
        self._shoppingScreenVC = shoppingScreenVC;
        //        [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
        //        [self presentViewController:shoppingScreenVC animated:NO completion:nil];
        self.window.rootViewController=shoppingScreenVC;
    }
    else
    {
        LoginOptionScreenVC *loginOptionScreenVC = [[LoginOptionScreenVC alloc] init];
        self.window.rootViewController=loginOptionScreenVC;
        //        [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
        //        [self presentViewController:loginOptionScreenVC animated:NO completion:nil];
        
    }
    
    [self.window makeKeyAndVisible];
}


- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    @try {                                                                
    NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
    //new
    [params setValue:@"Closed" forKey:@"event_name"];
    //[params setValue:[self stringFromDate:[NSDate date]] forKey:@"event_time"];
#ifdef CLP_ANALYTICS
    [_clpSDK updateAppEvent:params];
#endif
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
}


- (void)applicationDidEnterBackground:(UIApplication *)application
{
    //    [[NSUserDefaults standardUserDefaults] synchronize];
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    
    
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    @try {
        NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
        [params setValue:@"Opened" forKey:@"event_name"];
#ifdef CLP_ANALYTICS
        [_clpSDK updateAppEvent:params];
        Raleys *raleys = [[Raleys alloc] init];
        [raleys userRegister];
#endif
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// app manager begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (void)startAppManagerThread:(NSObject *)obj
{
    while(YES)
    {
        [NSThread sleepForTimeInterval:(NSTimeInterval)HOUR];  // check every 1 hour
        [NSThread detachNewThreadSelector:@selector(appManagerThread:) toTarget:self withObject:nil];
    }
}


- (void)appManagerThread:(NSObject *)obj
{
    @autoreleasepool
    {
        @try
        {
            NSDate *now = [NSDate date];
            long currentTime = (long)[now timeIntervalSince1970];
            LogInfo(@"appManagerThread executing %@", [_managerDateFormatter stringFromDate:now]);
            
            
            //--------------------------------------------
            // check product category cache, update weekly
            //--------------------------------------------
            long timeDiff = currentTime - _persistentData._lastProductCategoryCacheTime;
            long days = timeDiff / DAY;
            long hours = (timeDiff - (days * DAY)) / HOUR;
            long minutes = (timeDiff - (days * DAY) - (hours * HOUR)) / MINUTE;
            LogInfo(@"product category cache is %ld days, %ld hours, %ld minutes old.", days, hours, minutes);
            
            if(days >= 7 || [Utility isEmpty:_productCategories])
            {
                LogInfo(@"appManagerThread updating product category cache");
                [NSThread detachNewThreadSelector:@selector(getProductCategoriesThread:) toTarget:self withObject:PRODUCT_CATEGORIES_URL];
            }
            
            
            if([self isLoggedIn])
            {
                //-----------------------------------------
                // check promo category cache, update daily
                //-----------------------------------------
                timeDiff = currentTime - _persistentData._lastPromoCategoryCacheTime;
                days = timeDiff / DAY;
                hours = (timeDiff - (days * DAY)) / HOUR;
                minutes = (timeDiff - (days * DAY) - (hours * HOUR)) / MINUTE;
                LogInfo(@"promo category cache is %ld days, %ld hours, %ld minutes old.", days, hours, minutes);
                
                if(days >= 1 || [Utility isEmpty:_promoCategories] || [Utility isEmpty:_promoCategories.categoryList])
                {
                    LogInfo(@"appManagerThread updating promo category cache");
                    [NSThread detachNewThreadSelector:@selector(getProductCategoriesThread:) toTarget:self withObject:PROMO_CATEGORIES_URL];
                }
                
                
                //---------------------------
                // check offers, update daily
                //---------------------------
                timeDiff = currentTime - _persistentData._lastOfferUpdateTime;
                days = timeDiff / DAY;
                hours = (timeDiff - (days * DAY)) / HOUR;
                minutes = (timeDiff - (days * DAY) - (hours * HOUR)) / MINUTE;
                LogInfo(@"last offer update was %ld days, %ld hours, %ld minutes ago.", days, hours, minutes);
                
                if(days >= 1 && [self isLoggedIn])
                {
                    LogInfo(@"appManagerThread updating offers");
                    [self getAvailableOffers];
                }
            }
            
            
            //-----------------------------------
            // check missing images, update daily
            //-----------------------------------
            timeDiff = currentTime - _persistentData._lastMissingImageUpdateTime;
            days = timeDiff / DAY;
            hours = (timeDiff - (days * DAY)) / HOUR;
            minutes = (timeDiff - (days * DAY) - (hours * HOUR)) / MINUTE;
            LogInfo(@"last missing image update was %ld days, %ld hours, %ld minutes ago, image count = %lu", days, hours, minutes, (unsigned long)[_missingImagesList allValues].count);
            
            if(hours >= 1 && [_missingImagesList allValues].count > 0)
            {
                LogInfo(@"appManagerThread reporting missing images");
                [NSThread detachNewThreadSelector:@selector(reportMissingImagesThread:) toTarget:self withObject:nil];
            }
            
            
            //--------------------------
            // check product image cache
            //--------------------------
            NSError *error;
            int count = 0;
            long long totalSize = 0;
            NSString *filePath;
            NSDictionary *fileAttributes;
            NSNumber *fileSizeNumber;
            NSMutableArray *fileArray = [[NSMutableArray alloc] initWithArray:(NSMutableArray *)[[NSFileManager defaultManager] contentsOfDirectoryAtPath:_imageDir error:&error]];
            NSDictionary *directoryAttributes = [[NSFileManager defaultManager] attributesOfFileSystemForPath:_imageDir error: &error];
            
            if(directoryAttributes)
            {
                NSNumber *fileSystemSizeInBytes = [directoryAttributes objectForKey: NSFileSystemSize];
                NSNumber *freeFileSystemSizeInBytes = [directoryAttributes objectForKey:NSFileSystemFreeSize];
                long long totalSpace = [fileSystemSizeInBytes unsignedLongLongValue];
                long long totalFreeSpace = [freeFileSystemSizeInBytes unsignedLongLongValue];
                LogInfo(@"Directory capacity = %llu MB, free space = %llu MB", ((totalSpace/1024ll)/1024ll), ((totalFreeSpace/1024ll)/1024ll));
            }
            else
            {
                LogError(@"Error obtaining free disk space: Domain = %@, Code = %@", [error domain], [error localizedDescription]);
            }
            
            for(NSString *fileName in fileArray)
            {
                count++;
                filePath = [NSString stringWithFormat:@"%@/%@", _imageDir, fileName];
                fileAttributes = [_fileManager attributesOfItemAtPath:filePath error:&error];
                fileSizeNumber = [fileAttributes objectForKey:NSFileSize];
                long long fileSize = [fileSizeNumber longLongValue];
                
                if(fileSize == 0)
                {
                    [_fileManager removeItemAtPath:filePath error:&error];
                    LogError(@"Deleting zero length file: %@", filePath);
                    continue;
                }
                
                totalSize += fileSize;
                //LogInfo(@"Image file: %@, size:%lld", fileName, fileSize);
            }
            
            LogInfo(@"Image cache file count: %d, totalSize:%lld", count, totalSize);
            
            // build an array of files sorted by last modified date
            NSArray *sortedArray = [fileArray sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2)
                                    {
                                        NSDate *date1 = [_fileManager attributesOfItemAtPath:[NSString stringWithFormat:@"%@/%@", _imageDir, obj1] error:nil][@"NSFileCreationDate"];
                                        NSDate *date2 = [_fileManager attributesOfItemAtPath:[NSString stringWithFormat:@"%@/%@", _imageDir, obj2] error:nil][@"NSFileCreationDate"];
                                        return [date1 compare:date2];
                                    }];
            
            if(sortedArray.count > MAX_CACHE_FILES)
            {
                int filesToDelete = (int)sortedArray.count - MAX_CACHE_FILES;
                LogInfo(@"Removing %d files from image cache", filesToDelete);
                
                for(int i = 0; i < filesToDelete; i++)
                {
                    NSString *fullPath = [NSString stringWithFormat:@"%@/%@", _imageDir, [sortedArray objectAtIndex:i]];
                    [_fileManager removeItemAtPath:fullPath error:&error];
                    //NSDate *date = [_fileManager attributesOfItemAtPath:fullPath error:nil][@"NSFileCreationDate"];
                    //LogInfo(@"Deleting file: %@:%@", [sortedArray objectAtIndex:i], [_managerDateFormatter stringFromDate:date]);
                }
            }
            
            [fileArray removeAllObjects];
            fileArray = nil;
            sortedArray = nil;
            
            
            //----------------
            // check log files
            //----------------
            fileArray = [[NSMutableArray alloc] initWithArray:(NSMutableArray *)[[NSFileManager defaultManager] contentsOfDirectoryAtPath:_documentDir error:&error]];
            
            // build an array of files sorted by last modified date
            sortedArray = [fileArray sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2)
                           {
                               NSDate *date1 = [_fileManager attributesOfItemAtPath:[NSString stringWithFormat:@"%@/%@", _documentDir, obj1] error:nil][@"NSFileCreationDate"];
                               NSDate *date2 = [_fileManager attributesOfItemAtPath:[NSString stringWithFormat:@"%@/%@", _documentDir, obj2] error:nil][@"NSFileCreationDate"];
                               return [date1 compare:date2];
                           }];
            
            LogInfo(@"Found %lu files in log directory", (unsigned long)sortedArray.count);
            
            if(sortedArray.count > MAX_LOG_FILES)
            {
                for(int i = 0; i < sortedArray.count - MAX_LOG_FILES; i++)
                {
                    NSDate *fileDate = [_fileManager attributesOfItemAtPath:[NSString stringWithFormat:@"%@/%@", _documentDir, [sortedArray objectAtIndex:i]] error:nil][@"NSFileCreationDate"];
                    NSTimeInterval interval = [now timeIntervalSinceDate:fileDate];
                    long timeDiff = (int)interval;
                    LogInfo(@"log file %@ is %ld days, %ld hours old", [sortedArray objectAtIndex:i], (timeDiff / DAY), (timeDiff / HOUR));
                    
                    if((timeDiff / DAY) > 1)
                    {
                        LogInfo(@"removing log file %@", [sortedArray objectAtIndex:i]);
                        NSString *fullPath = [NSString stringWithFormat:@"%@/%@", _documentDir, [sortedArray objectAtIndex:i]];
                        [_fileManager removeItemAtPath:fullPath error:&error];
                    }
                }
            }
        }
        @catch (NSException *exception)
        {
            LogError(@"Caught exception in appManagerThread, exception:%@", [exception description]);
        }
    } // end of autoreleasepool block
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// app manager end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// persistent data methods begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (void)getPersistentDataFromCache
{
    @try {
        //Give full file path for file manager (iOS-8 fix)
        NSString *fullPath = [NSString stringWithFormat:@"%@/%@",_supportDir,_persistentDataFile];
        if([_fileManager changeCurrentDirectoryPath:_supportDir] == YES)
        {
            if([_fileManager isReadableFileAtPath:_persistentDataFile] == YES)
            {
                _persistentData = [NSKeyedUnarchiver unarchiveObjectWithFile:_persistentDataFile];
                
                if(_persistentData == nil)
                {
                    if([NSKeyedArchiver archiveRootObject:_persistentData toFile:fullPath] == YES)
                        LogInfo(@"Initialized persistent data file %@", _persistentDataFile);
                    else
                        LogError(@"Failed to initialize persistent data to file %@", _persistentDataFile);
                }
                else
                {
                    LogInfo(@"Unarchived persistent data file %@", _persistentDataFile);
                }
            }
            else if([[NSFileManager defaultManager] createFileAtPath:fullPath contents:nil attributes:nil] == YES)
            {
                if([NSKeyedArchiver archiveRootObject:_persistentData toFile:fullPath] == YES)
                    LogInfo(@"Initialized persistent data file %@", _persistentDataFile);
                else
                    LogError(@"Failed to initialize persistent data to file %@", _persistentDataFile);
            }
        }
        else
        {
            NSError *error;
            BOOL ret = [_fileManager createDirectoryAtPath:_supportDir withIntermediateDirectories:NO attributes:nil error:&error];
            
            if(ret == NO)
                LogError(@"Failed to create app support dir: %@", error);
        }    }
    @catch (NSException *exception) {
        LogError(@"Error in reading persistant data from file:%@", [exception description]);
    }
    
}


- (PersistentData *)getPersistentData
{
    return _persistentData;
}


- (void)storePersistentData
{
    @try {
        //Give full file path for file manager (iOS-8 fix)
        NSString *fullPath = [NSString stringWithFormat:@"%@/%@",_supportDir,_persistentDataFile];
        if([_fileManager changeCurrentDirectoryPath:_supportDir] == YES)
        {
            if([_fileManager isReadableFileAtPath:fullPath] == YES)
            {
                if([NSKeyedArchiver archiveRootObject:_persistentData toFile:fullPath] == YES)
                    LogInfo(@"Updated %@ file", _persistentDataFile);
                else
                    LogError(@"Failed to update %@ file", _persistentDataFile);
            }
            else if([[NSFileManager defaultManager] createFileAtPath:fullPath contents:nil attributes:nil] == YES)
            {
                if([NSKeyedArchiver archiveRootObject:_persistentData toFile:fullPath] == YES)
                    LogInfo(@"Updated %@ file", _persistentDataFile);
                else
                    LogError(@"Failed to update %@ file", _persistentDataFile);
            }
        }
        else
        {
            LogError(@"Failed to change to document directory %@", _supportDir);
        }    }
    @catch (NSException *exception) {
        LogError(@"Error writing persistant data:%@", [exception description]);
    }
    
    
}

-(void)storeRaleys{
    _persistentData._raleys = [Raleys shared];
    [self storePersistentData];
}

-(Raleys*)getRaleys{
    return _persistentData._raleys;
}

- (Login *)getLogin
{
    return _persistentData._login;
}


- (void)storeLogin:(Login *)login
{
    _persistentData._login = login;
    
    // keep the users current store to use if GPS is not enabled
    if(_allStoresList.count > 0)
    {
        Store *store = [_allStoresList objectForKey:[NSString stringWithFormat:@"%d", login.storeNumber]];
        
        if(store != nil)
        {
            CLLocation *location = [[CLLocation alloc] initWithLatitude:[store.latitude doubleValue] longitude:[store.longitude doubleValue]];
            _persistentData._homeStoreLocation = location;
        }
    }
    
    [self storePersistentData];
}


- (BOOL)isLoggedIn
{
    if(_persistentData._login.accountId != nil && _persistentData._login.crmNumber != nil && _persistentData._login.pointsBalance != nil)
        return YES;
    else
        return NO;
}


- (void)logout
{
    
    
    @try {
    //Analytics
    NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
    [data setObject:[NSDate date] forKey:@"Time"];
    
    //new
    [data setValue:@"SignedOut" forKey:@"event_name"];
    [data setValue:[self stringFromDate:[NSDate date]] forKey:@"event_time"];
#ifdef CLP_ANALYTICS
    [_clpSDK updateAppEvent:data];
#endif

    _persistentData._login = [[Login alloc] init];
    _persistentData._currentShoppingListId = nil;
    _persistentData._raleys = [[Raleys alloc]init];
    _persistentData.account=nil;
    [self storePersistentData];
    _currentShoppingList = nil;
    _selectedShoppingList = nil;
    [_personalizedOffersList removeAllObjects];
    [_acceptedOffersList removeAllObjects];
    [_extraFriendzyOffersList removeAllObjects];
    [_moreForYouOffersList removeAllObjects];
    
    [self animateNextScreen];
    
    
 
    [_clpSDK logoutClpSdk];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
    
}


- (int)getStoreNumber
{
    return _persistentData._login.storeNumber;
}


- (void)updateOfferUpdateSyncTime:(long)syncTime
{
    _persistentData._lastOfferUpdateTime = syncTime;
    [self storePersistentData];
}


- (void)storeShoppingListId:(NSString *)listId
{
    _persistentData._currentShoppingListId = listId;
    [self storePersistentData];
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// persistent data methods end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// product/category cache methods begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (ProductCategory *)getProductCategoriesFromCache:(ProductCategory *)productCategories file:(NSString *)fileName
{
    @try {
        ProductCategory *categories = nil;
        //Give full file path for file manager (iOS-8 fix)
        NSString *fullPath = [NSString stringWithFormat:@"%@/%@",_supportDir,fileName];
        if([_fileManager changeCurrentDirectoryPath:_supportDir] == YES)
        {
            if([_fileManager isReadableFileAtPath:fullPath] == YES)
            {
                categories = [NSKeyedUnarchiver unarchiveObjectWithFile:fullPath];
                
                if(categories == nil)
                    LogError(@"Failed to get product categories from  file %@", fileName);
                else
                    LogInfo(@"Retrieved categories from file %@", fileName);
            }
            else if([[NSFileManager defaultManager] createFileAtPath:fullPath contents:nil attributes:nil] == YES)
            {
                if([NSKeyedArchiver archiveRootObject:productCategories toFile:fullPath] == YES)
                    LogInfo(@"Initialized persistent data file %@", fileName);
                else
                    LogError(@"Failed to initialize persistent data to file %@", fileName);
            }
        }
        else
        {
            LogError(@"Failed to change to document directory %@", _supportDir);
        }
        return categories;
    }
    @catch (NSException *exception) {
        LogError(@"Error in reading product categories from file:%@", [exception description]);
    }
    
}


- (void)storeProductCategoriesInCache:(ProductCategory *)categories file:(NSString *)fileName
{
    //Give full file path for file manager (iOS-8 fix)
    NSString *fullpath = [NSString stringWithFormat:@"%@/%@",_supportDir,fileName];
    
    if([_fileManager changeCurrentDirectoryPath:_supportDir] == YES)
    {
        if([_fileManager isReadableFileAtPath:fullpath] == YES)
        {
            if([NSKeyedArchiver archiveRootObject:categories toFile:fullpath] == YES)
                LogInfo(@"Updated %@ file", fileName);
            else
                LogError(@"Failed to update %@ file", fileName);
        }
        else if([[NSFileManager defaultManager] createFileAtPath:fullpath contents:nil attributes:nil] == YES)
        {
            if([NSKeyedArchiver archiveRootObject:categories toFile:fullpath] == YES)
                LogInfo(@"Updated %@ file", fileName);
            else
                LogError(@"Failed to update %@ file", fileName);
        }
    }
    else
    {
        LogError(@"Failed to change to document directory %@", _supportDir);
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// product/category cache methods end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



- (BOOL)internetAvailable
{
    return _internetAvailable;
}


// thread function for downloading all product categories
- (void)getProductCategoriesThread:(NSString *)categoryUrl
{
    @autoreleasepool
    {
        @try
        {
            NSURL *url = [NSURL URLWithString:categoryUrl];
            ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
            [request setTimeOutSeconds:HTTP_TIME_OUT]; // 15 seconds
            [request setValidatesSecureCertificate:YES];
            [request addRequestHeader:CONTENT_TYPE value:CONTENT_TYPE_VALUE];
            [request setRequestMethod:POST];
            
            if([categoryUrl isEqualToString:PROMO_CATEGORIES_URL])
            {
                Login *login = [self getLogin];
                PromoCategoriesRequest *categoriesRequest = [[PromoCategoriesRequest alloc] init];
                categoriesRequest.storeNumber = login.storeNumber;
                [request appendPostData:(NSMutableData *)[[categoriesRequest objectToJson] dataUsingEncoding:[NSString defaultCStringEncoding]]];
                LogInfo(@"PromoCategoriesRequest: %@", [categoriesRequest objectToJson]);
            }
            
            [request startSynchronous];
            NSError *error = [request error];
            
            if(request.responseStatusCode == 200 && error == nil)
            {
                //LogInfo(@"%@", request.responseString);
                SBJsonParser *parser = [[SBJsonParser alloc] init];
                NSDictionary *jsonContent = [parser objectWithString:request.responseString error:nil];
                id responseObj = nil;
                Class responseObjClass = objc_getClass([@"ProductCategory" cStringUsingEncoding:NSASCIIStringEncoding]);
                responseObj = [responseObjClass objectForDictionary:jsonContent];
                
                
//                NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
//                [data setObject:jsonContent forKey:@"Category_List"];
//                [data setObject:categoryUrl forKey:@"Category_URL"];
//                
//                //new
//                [data setValue:@"Category List" forKey:@"event_name"];
//                [data setValue:[self stringFromDate:[NSDate date]] forKey:@"event_time"];
//#ifdef CLP_ANALYTICS
//                [_clpSDK updateAppEvent:data];
//#endif
                
                
                if([categoryUrl isEqualToString:PRODUCT_CATEGORIES_URL])
                {
                    _productCategories = (ProductCategory *)responseObj;
                    [self storeProductCategoriesInCache:_productCategories file:_productCategoriesFile];
                    _persistentData._lastProductCategoryCacheTime = (long)[[NSDate date] timeIntervalSince1970];
                    [self storePersistentData];
                    LogInfo(@"Found %lu categories", (unsigned long)_productCategories.categoryList.count);
                }
                else // PROMO_CATEGORIES_URL
                {
                    _promoCategories = (ProductCategory *)responseObj;
                    [self storeProductCategoriesInCache:_promoCategories file:_promoCategoriesFile];
                    _persistentData._lastPromoCategoryCacheTime = (long)[[NSDate date] timeIntervalSince1970];
                    [self storePersistentData];
                    LogInfo(@"Found %lu categories", (unsigned long)_promoCategories.categoryList.count);
                }
            }
            else if(request.responseStatusCode == 422) // service error
            {
                if([categoryUrl isEqualToString:PRODUCT_CATEGORIES_URL])
                    LogError(@"getProductCategoriesThread received 422 error: %@", request.responseString);
                else // PROMO_CATEGORIES_URL
                    LogError(@"getPromoCategoriesThread received 422 error: %@", request.responseString);
            }
            else // responseStatusCode != 200
            {
                if(error != nil)
                {
                    if(error.code == ASIRequestTimedOutErrorType)
                    {
                        LogError("getProductCategoriesThread request timed out: url = %@", categoryUrl);
                    }
                    else
                    {
                        LogError("getProductCategoriesThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, categoryUrl);
                        [Utility logError:error];
                    }
                }
                else
                {
                    LogError("getProductCategoriesThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, categoryUrl);
                    [Utility logError:error];
                }
            }
            
        }
        @catch (NSException *exception)
        {
            LogError(@"Caught exception in getAccountThread, exception:%@", [exception description]);
        }
    } // end of autoreleasepool block
}


// thread function for downloading the user's current shopping list
- (void)getCurrentShoppingListThread:(NSString *)url
{
    _retrievingShoppingList = YES;
    
    @autoreleasepool
    {
        @try
        {
            Login *login = _persistentData._login;
            ListRequest *listRequest = [[ListRequest alloc] init];
            listRequest.accountId = login.accountId;
            listRequest.listId = _persistentData._currentShoppingListId;
            listRequest.returnCurrentList = [NSNumber numberWithBool:YES];
            listRequest.appListUpdateTime = [NSNumber numberWithLong:0];
            
            NSString *urlString = [NSString stringWithFormat:@"%@", LIST_GET_BY_ID_URL];
            NSURL *url = [NSURL URLWithString:urlString];
            ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
            [request setTimeOutSeconds:HTTP_TIME_OUT]; // 15 seconds
            [request setValidatesSecureCertificate:YES];
            [request addRequestHeader:AUTH value:login.authKey];
            [request addRequestHeader:CONTENT_TYPE value:CONTENT_TYPE_VALUE];
            [request setRequestMethod:POST];
            [request appendPostData:(NSMutableData *)[[listRequest objectToJson] dataUsingEncoding:[NSString defaultCStringEncoding]]];
            [request startSynchronous];
            NSError *error = [request error];
            
            if(request.responseStatusCode == 200 && error == nil)
            {
                SBJsonParser *parser = [[SBJsonParser alloc] init];
                NSDictionary *jsonContent = [parser objectWithString:request.responseString error:nil];
                id responseObj = nil;
                Class responseObjClass = objc_getClass([@"ShoppingList" cStringUsingEncoding:NSASCIIStringEncoding]);
                responseObj = [responseObjClass objectForDictionary:jsonContent];
                _currentShoppingList = (ShoppingList *)responseObj;
                
                if(_currentShoppingList != nil)
                {
                    LogInfo("SHOPPING_LIST, _currentShoppingList count: %lu, serverUpdateTime = %llu", (unsigned long)_currentShoppingList.productList.count, [_currentShoppingList.serverUpdateTime longLongValue]);
                    LogInfo(@"getCurrentShoppingListThread, Found %lu products for list %@", (unsigned long)_currentShoppingList.productList.count, _currentShoppingList.name);
                }
                else
                {
                    LogInfo(@"SHOPPING_LIST, Failed to parse _currentShoppingList");
                    LogError(@"getCurrentShoppingListThread, Failed to parse _currentShoppingList");
                }
            }
            else if(request.responseStatusCode == 422) // service error
            {
                LogError(@"getCurrentShoppingListThread received 422 error: %@", request.responseString);
            }
            else // responseStatusCode != 200
            {
                if(error != nil)
                {
                    if(error.code == ASIRequestTimedOutErrorType)
                    {
                        LogError("getCurrentShoppingListThread request timed out: url = %@", urlString);
                    }
                    else
                    {
                        LogError("getCurrentShoppingListThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                        [Utility logError:error];
                    }
                }
                else
                {
                    LogError("getCurrentShoppingListThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                    [Utility logError:error];
                }
            }
        }
        @catch (NSException *exception)
        {
            LogError(@"Caught exception in getCurrentShoppingListThread, exception:%@", [exception description]);
        }
    } // end of autoreleasepool block
    
    _retrievingShoppingList = NO;
}


// thread function for downloading all stores list
- (void)getAllStoresThread:(NSString *)url
{
    @autoreleasepool
    {
        @try
        {
            NSString *urlString = GET_STORES_URL;
            NSURL *url = [NSURL URLWithString:urlString];
            ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
            [request setTimeOutSeconds:HTTP_TIME_OUT]; // 15 seconds
            [request setValidatesSecureCertificate:YES];
            [request setRequestMethod:POST];
            
            while(_allStoresList.count == 0)
            {
                [request startSynchronous];
                NSError *error = [request error];
                
                if(request.responseStatusCode == 200 && error == nil)
                {
                    SBJsonParser *parser = [[SBJsonParser alloc] init];
                    LogInfo(@"%@", request.responseString);
                    NSDictionary *jsonContent = [parser objectWithString:request.responseString error:nil];
                    id responseObj = nil;
                    Class responseObjClass = objc_getClass([@"Store" cStringUsingEncoding:NSASCIIStringEncoding]);
                    responseObj = [responseObjClass objectForDictionary:jsonContent];
                    Store *response = (Store *)responseObj;
                    
                    for(int i = 0; i < response.storeList.count; i++)
                    {
                        Store *store = [response.storeList objectAtIndex:i];
                        
                        if([store.chain isEqualToString:@"Raley's"] || [store.chain isEqualToString:@"Bel Air"] || [store.chain isEqualToString:@"Nob Hill Foods"])
                            [_allStoresList setObject:store forKey:[NSString stringWithFormat:@"%d", store.storeNumber]];
                    }
                    
                    
                    _persistentData._storeList = _allStoresList;
                    [self storePersistentData];
                    LogInfo(@"_allStoresList contains %lu records", (unsigned long)_allStoresList.count);
                }
                else if(request.responseStatusCode == 422) // service error
                {
                    LogError(@"getAllStoresThread received 422 error: %@", request.responseString);
                }
                else // responseStatusCode != 200
                {
                    if(error != nil)
                    {
                        if(error.code == ASIRequestTimedOutErrorType)
                        {
                            LogError("getAllStoresThread request timed out: url = %@", urlString);
                        }
                        else
                        {
                            LogError("getAllStoresThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                            [Utility logError:error];
                        }
                    }
                    else
                    {
                        LogError("getAllStoresThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                        [Utility logError:error];
                    }
                }
                
                [NSThread sleepForTimeInterval:60.0];  // keep retrying every 60 seconds and pray
            }
        }
        @catch (NSException *exception)
        {
            LogError(@"Caught exception in getAllStoresThread, exception:%@", [exception description]);
        }
    } // end of autoreleasepool block
}


- (void)getAvailableOffers
{
    _offerThreadCount = 0;
    _imageThreadCount = 0;
    [_personalizedOffersList removeAllObjects];
    [_extraFriendzyOffersList removeAllObjects];
    [_moreForYouOffersList removeAllObjects];
    [self getOfferList:OFFERS_PERSONALIZED_URL];
    [self getOfferList:OFFERS_EXTRA_FRIENDZY_URL];
    [self getOfferList:OFFERS_MORE_FOR_YOU_URL];
    [self updateOfferUpdateSyncTime:(long)[[NSDate date] timeIntervalSince1970]];
}


- (void)getAcceptedOffers
{
    _offerThreadCount = 0;
    _imageThreadCount = 0;
    [_acceptedOffersList removeAllObjects];
    [self getOfferList:OFFERS_ACCEPTED_URL];
}


- (void)getOfferList:(NSString *)offerType
{
    [NSThread detachNewThreadSelector:@selector(getOfferListThread:) toTarget:self withObject:offerType];
}


// thread function for downloading offers list
- (void)getOfferListThread:(NSString *)offerType
{
    NSString *offertype = offerType;
    
    @autoreleasepool
    {
        @try
        {
            _offerThreadCount++;
            Login *login = _persistentData._login;
            OfferRequest *offerRequest = [[OfferRequest alloc] init];
            offerRequest.crmNumber = login.crmNumber;
            
            NSString *urlString = [NSString stringWithFormat:@"%@", offertype];
            NSURL *url = [NSURL URLWithString:urlString];
            ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
            [request setTimeOutSeconds:HTTP_TIME_OUT]; // 15 seconds
            [request setValidatesSecureCertificate:YES];
            [request addRequestHeader:AUTH value:login.authKey];
            [request addRequestHeader:CONTENT_TYPE value:CONTENT_TYPE_VALUE];
            [request setRequestMethod:POST];
            [request appendPostData:(NSMutableData *)[[offerRequest objectToJson] dataUsingEncoding:[NSString defaultCStringEncoding]]];
            [request startSynchronous];
            NSError *error = [request error];
            
            if(request.responseStatusCode == 200 && error == nil)
            {
                SBJsonParser *parser = [[SBJsonParser alloc] init];
                
                NSString *jsonresponse = request.responseString;
                //                jsonresponse = [jsonresponse stringByReplacingOccurrencesOfString:@"/Jpg_90/" withString:@"/Jpg_200/"];
                
                NSDictionary *jsonContent = [parser objectWithString:jsonresponse error:nil];
                id responseObj = nil;
                Class responseObjClass = objc_getClass([@"Offer" cStringUsingEncoding:NSASCIIStringEncoding]);
                responseObj = [responseObjClass objectForDictionary:jsonContent];
                Offer *response = (Offer *)responseObj;
                LogInfo(@"Found %lu offers for offer type %@", (unsigned long)response.offerList.count, offerType);
                
                @synchronized(response.offerList)
                {
                    for(int i = 0; i < response.offerList.count; i++)
                    {
                        @try
                        {
                            Offer *offer = [response.offerList objectAtIndex:i];
                            offer._acceptableOffer = NO;
                            offer._acceptedOffer = NO;
                            
                            if([offertype isEqualToString:OFFERS_ACCEPTED_URL])
                            {
                                offer._acceptedOffer = YES;
                                [_acceptedOffersList addObject:offer];
                            }
                            else if([offertype isEqualToString:OFFERS_PERSONALIZED_URL])
                            {
                                offer._acceptableOffer = YES;
                                [_personalizedOffersList addObject:offer];
                            }
                            else if([offertype isEqualToString:OFFERS_EXTRA_FRIENDZY_URL])
                            {
                                offer._acceptableOffer = YES;
                                [_extraFriendzyOffersList addObject:offer];
                            }
                            else if([offertype isEqualToString:OFFERS_MORE_FOR_YOU_URL])
                            {
                                [_moreForYouOffersList addObject:offer];
                            }
                            
                            if(![Utility isEmpty:offer.offerProductImageFile])
                                [self getImageAsync:[NSString stringWithFormat:@"https://www.raleys.com/images/imagescoupons/%@", offer.offerProductImageFile]];
                        }
                        @catch(NSException *exception)
                        {
                            LogError(@"Failed to get Offer, exception:%@", [exception description]);
                        }
                    }
                }
            }
            else if(request.responseStatusCode == 422) // service error
            {
                SBJsonParser *parser = [[SBJsonParser alloc] init];
                WebServiceError *serviceError = nil;
                NSDictionary *jsonContent = [parser objectWithString:request.responseString error:nil];
                
                if(![Utility isEmpty:jsonContent])
                    serviceError = (WebServiceError *)[WebServiceError.class objectForDictionary:jsonContent];
                
                if(serviceError == nil || serviceError.errorCode != 504) // don't log no offers available error
                    LogError(@"getOfferListThread received 422 error: %@", request.responseString);
            }
            else // non service error
            {
                if(error != nil)
                {
                    if(error.code == ASIRequestTimedOutErrorType)
                    {
                        LogError(@"getOfferListThread request timed out: url = %@", urlString);
                    }
                    else
                    {
                        LogError(@"getOfferListThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                        [Utility logError:error];
                    }
                }
                else
                {
                    LogError(@"getOfferListThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                    [Utility logError:error];
                }
            }
            
            _offerThreadCount--;
            //LogInfo(@"_offerThreadCount = %d", _offerThreadCount);
        }
        @catch (NSException *exception)
        {
            LogError(@"Caught exception in getOfferListThread, exception:%@", [exception description]);
        }
    } // end of autoreleasepool block
}


// thread function for reporting missing images to server
- (void)reportMissingImagesThread:(NSObject *)obj
{
    @autoreleasepool
    {
        @try
        {
            MissingImagesRequest *missingImageRequest = [[MissingImagesRequest alloc] init];
            missingImageRequest.imageUrlList = [_missingImagesList allValues];
            
            NSString *urlString = MISSING_IMAGES_URL;
            NSURL *url = [NSURL URLWithString:urlString];
            ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
            [request setTimeOutSeconds:HTTP_TIME_OUT]; // 15 seconds
            [request setValidatesSecureCertificate:YES];
            [request addRequestHeader:CONTENT_TYPE value:CONTENT_TYPE_VALUE];
            [request setRequestMethod:POST];
            [request appendPostData:(NSMutableData *)[[missingImageRequest objectToJson] dataUsingEncoding:[NSString defaultCStringEncoding]]];
            [request startSynchronous];
            NSError *error = [request error];
            
            if(request.responseStatusCode == 200 && error == nil)
            {
                LogInfo(@"Reported %lu missing images to server", (unsigned long)missingImageRequest.imageUrlList.count);
                [_missingImagesList removeAllObjects];
                _persistentData._lastMissingImageUpdateTime = (long)[[NSDate date] timeIntervalSince1970];
                [self storePersistentData];
            }
            else if(request.responseStatusCode == 422) // service error
            {
                LogError(@"reportMissingImagesThread received 422 error: %@", request.responseString);
            }
            else // responseStatusCode != 200
            {
                if(error != nil)
                {
                    if(error.code == ASIRequestTimedOutErrorType)
                    {
                        LogError("reportMissingImagesThread request timed out: url = %@", urlString);
                    }
                    else
                    {
                        LogError("reportMissingImagesThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                        [Utility logError:error];
                    }
                }
                else
                {
                    LogError("reportMissingImagesThread, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                    [Utility logError:error];
                }
            }
        }
        @catch(NSException *exception)
        {
            LogError(@"Caught exception in reportMissingImagesThread, exception:%@", [exception description]);
        }
    } // end of autoreleasepool block
}


- (void)getProductImages:(NSArray *)productList
{
    _imageThreadCount = 0;
    
    for(Product *product in productList)
    {
        if(![Utility isEmpty:product.imagePath])
            [self getImageAsync:product.imagePath]; // stores the image data to the disk cache
    }
}


- (UIImage *)getCachedImage:(NSString *)fileName
{
    if([Utility isEmpty:fileName])
        return nil;
    
    // check disk image cache
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *filePath = [NSString stringWithFormat:@"%@/%@", _imageDir, fileName];
    
    if([fileManager fileExistsAtPath:filePath])
    {
        // update the file's timestamp so that the cache manager knows what files have been used most recently
        NSDictionary *attributes = [NSDictionary dictionaryWithObjectsAndKeys: [[NSDate alloc] init], NSFileCreationDate, NULL];
        [[NSFileManager defaultManager] setAttributes:attributes ofItemAtPath:filePath error: NULL];
        
        // create the image from the file
        NSData *imageData = [[NSData alloc] initWithContentsOfFile:filePath];
        UIImage *image = [UIImage imageWithData:imageData];
        
        // use this to log the image size
        //NSData *data = UIImageJPEGRepresentation(image, 1.0);
        //LogInfo(@"[AppDelegate getImage]: Found image file on disk: %@, file size = %lu, image size = %lu", fileName, (unsigned long)imageData.length, (unsigned long)data.length);
        return image;
    }
    
    //LogInfo(@"getImage: Did not find image file on disk: %@", fileName);
    return nil;
}



- (NSString *)getImageFullPath:(NSString *)fileName
{
    if([Utility isEmpty:fileName])
        return nil;
    
    // check disk image cache
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *filePath = [NSString stringWithFormat:@"%@/%@", _imageDir, fileName];
    
    if([fileManager fileExistsAtPath:filePath])
    {
        // update the file's timestamp so that the cache manager knows what files have been used most recently
        NSDictionary *attributes = [NSDictionary dictionaryWithObjectsAndKeys: [[NSDate alloc] init], NSFileCreationDate, NULL];
        [[NSFileManager defaultManager] setAttributes:attributes ofItemAtPath:filePath error: NULL];
        
        // create the image from the file
        // NSData *imageData = [[NSData alloc] initWithContentsOfFile:filePath];
        // UIImage *image = [UIImage imageWithData:imageData];
        
        // use this to log the image size
        //NSData *data = UIImageJPEGRepresentation(image, 1.0);
        //LogInfo(@"[AppDelegate getImage]: Found image file on disk: %@, file size = %lu, image size = %lu", fileName, (unsigned long)imageData.length, (unsigned long)data.length);
        return filePath;
    }
    
    //LogInfo(@"getImage: Did not find image file on disk: %@", fileName);
    return nil;
}




- (UIImage *)getImageSync:(NSString *)urlString
{
    @try
    {
        NSString *fileName = [urlString lastPathComponent];
        UIImage *image = [self getCachedImage:fileName];
        
        if(image != nil)
            return image;
        
        if([Utility isNetworkAvailable] == NO)
        {
            LogError(@"Network is not available");
            return nil;
        }
        
        //LogInfo(@"Downloading image: %@", urlString);
        NSURL *url = [NSURL URLWithString:urlString];
        ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
        [request setTimeOutSeconds:HTTP_TIME_OUT]; // 15 seconds
        [request setValidatesSecureCertificate:YES];
        [request startSynchronous];
        NSError *error = [request error];
        
        if(request.responseStatusCode == 200 && error == nil)
        {
            image = [UIImage imageWithData:request.responseData];
            
            if(image != nil)
            {
                NSString *filePath = [NSString stringWithFormat:@"%@/%@", _imageDir, fileName];
                [[request responseData] writeToFile:filePath atomically:YES];
                return image;
            }
            else
            {
                LogInfo("Downloaded image %@ not in proper format", urlString);
            }
        }
        else
        {
            if(error != nil)
            {
                if(error.code == ASIRequestTimedOutErrorType)
                {
                    LogError("downloadImage request timed out: url = %@", urlString);
                }
                else
                {
                    LogError("downloadImage, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                    [Utility logError:error];
                }
            }
            else
            {
                if(request.responseStatusCode == 404)
                {
                    LogInfo("Missing Image %@", urlString);
                    @synchronized(_missingImagesList)
                    {
                        [_missingImagesList setObject:urlString forKey:[urlString lastPathComponent]];
                    }
                }
                else
                {
                    LogError("downloadImage, ASIHTTPRequest error: _httpResponseCode = %d, url = %@", request.responseStatusCode, urlString);
                    [Utility logError:error];
                }
            }
        }
        
        return nil;
    }
    @catch (NSException *exception)
    {
        LogError(@"Caught exception in getOfferListThread, exception:%@", [exception description]);
    }
}


- (void)getImageAsync:(NSString *)urlString
{
    [NSThread detachNewThreadSelector:@selector(downloadImageThread:) toTarget:self withObject:urlString];
}


// thread function for downloading deal images
- (void)downloadImageThread:(NSString *)urlString
{
    @autoreleasepool
    {
        while(_imageThreadCount >= 200)
            [NSThread sleepForTimeInterval:0.01];
        
        @synchronized(_imageThreadCountLock) { _imageThreadCount++; }
        //LogInfo(@"_imageThreadCount = %d", _imageThreadCount);
        [self getImageSync:urlString];
        @synchronized(_imageThreadCountLock) { _imageThreadCount--; }
        //LogInfo(@"_imageThreadCount = %d", _imageThreadCount);
    } // end of autoreleasepool block
}


- (BOOL)offerThreadsDone
{
    if(_offerThreadCount <= 0 && _imageThreadCount <= 0)
        return  YES;
    else
        return NO;
}


- (BOOL)imageThreadsDone
{
    //LogInfo(@"SATAN: _imageThreadCount = %d", _imageThreadCount);
    
    if(_imageThreadCount <= 0)
        return  YES;
    else
        return NO;
}


- (NSArray *)getNearestStores:(CLLocation *)currentLocation;
{
    NSMutableArray *unsortedArray = [[NSMutableArray alloc] init];
    NSArray *sortedArray;
    
    if(_allStoresList.count > 0)
    {
        Store *store;
        
        for(NSString *key in [_allStoresList allKeys])
        {
            store = [_allStoresList objectForKey:key];
            
            if([Utility isEmpty:store.latitude] || [Utility isEmpty:store.longitude])
                continue;
            
            CLLocation *location = [[CLLocation alloc] initWithLatitude:[store.latitude doubleValue] longitude:[store.longitude doubleValue]];
            store._distanceFromLocation = [currentLocation distanceFromLocation:location];  // add this back ' / METERS_PER_MILE'
            [unsortedArray addObject:store];
        }
        
        sortedArray = [unsortedArray sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2)
                       {
                           Store *firstStore = (Store *)obj1;
                           Store *secondStore = (Store *)obj2;
                           double firstValue = firstStore._distanceFromLocation;
                           double secondValue = secondStore._distanceFromLocation;
                           NSNumber *first = [NSNumber numberWithDouble:firstValue];
                           NSNumber *second = [NSNumber numberWithDouble:secondValue];
                           return [first compare:second]; // ascending
                       }];
    }
    
    return sortedArray;
}


# pragma mark - Push Notification

-(void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken{
    token = [[deviceToken description] stringByTrimmingCharactersInSet:      [NSCharacterSet characterSetWithCharactersInString:@"<>"]];
    token = [token stringByReplacingOccurrencesOfString:@" " withString:@""];
    [Raleys shared].pushDeviceToken = token;
    NSLog(@"Token: %@",token);
}

-(void)checkPushIsRegisterOrNot{
    @try{
        [Raleys shared].pushDeviceToken = @"";
        if([[[UIDevice currentDevice]systemVersion]floatValue]>=8.0){
            if([UIApplication sharedApplication].isRegisteredForRemoteNotifications){
                if(token){
                    [Raleys shared].pushDeviceToken = token;
                }
            }
        }else{
            //        if([UIApplication sharedApplication].enabledRemoteNotificationTypes != UIRemoteNotificationTypeNone){
            UIRemoteNotificationType types = [[UIApplication sharedApplication] enabledRemoteNotificationTypes];
            if (types & UIRemoteNotificationTypeAlert){
                if(token){
                    [Raleys shared].pushDeviceToken = token;
                }
            }
        }
    }@catch(NSException *exception){
        
    }
    //isRegisteredForRemoteNotifications
}

-(void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error{
    NSLog(@"%@",error.description);
}
-(void)clpOpenPassbook{
    if(pass_loader==nil){
        UIViewController *viewctrl = [clpsdk getTopController];
        pass_loader = [[PassbookLoader alloc]initWithFrame:viewctrl.view.frame];
        
        [pass_loader.bgview setBackgroundColor:[UIColor colorWithRed:0 green:0 blue:0 alpha:0.7]];
        [pass_loader.bgview.layer setCornerRadius:7.0f];
        
        [pass_loader startAnimation];
        
        [viewctrl.view addSubview:pass_loader];
    }
}
-(void)clpClosePassbook{
    if(pass_loader!=nil){
        [pass_loader removeFromSuperview];
    }
    pass_loader = nil;
}
-(void)clpResponseFail:(NSError *)error{
    
}
-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    @try {
//        NSString *url = [userInfo objectForKey:@"url"];
//        NSString *couponId = [userInfo objectForKey:@"couponId"];
//        NSString *externalId = [userInfo objectForKey:@"eoid"]; // AMS external Offer Id
//        NSString *promo_code = [userInfo objectForKey:@"pc"];
//        NSString *title = [[userInfo objectForKey:@"aps"]objectForKey:@"alert"];
//        NSString *endDate = [userInfo objectForKey:@"ed"];
//        NSString *acceptGroup = [userInfo objectForKey:@"ag"];
        
        [_clpSDK setDelegate:self];
        [_clpSDK processPushMessage:userInfo];
        
        // ACCEPT OFFER // Commented since offer acceptance is handled in the platform.
//        Offer *new_offer = [[Offer alloc]init];
//        new_offer.dynamicOffer = YES;
//        
//        if(externalId){
//            new_offer.offerId = [userInfo objectForKey:@"eoid"];
//        }
//        else if(couponId){
//            new_offer.offerId = [userInfo objectForKey:@"couponId"];
//        }else{
//            // exceptional case
//            new_offer.offerId = @"";
//        }
//        
//        if(title){
//            // alert message
//            new_offer.consumerTitle = title;
//        }
//        if (acceptGroup)
//            new_offer.acceptGroup = acceptGroup;
//        else
//            new_offer.acceptGroup = @"";
//        
//        if(promo_code){
//            // promo code
//            new_offer.promoCode = promo_code;
//        }
//        
//        if(endDate){
//            // end date
//            new_offer.endDate = endDate;
//        }
//        // Accept offer
//        [self._shoppingScreenVC acceptOffer:new_offer];

        /// TRACK NOTIFICATION OPENED // Commented since already handled in SDK
//        NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
//        [params setObject:[NSDate date] forKey:@"time"];
//        if(couponId){
//            [params setObject:couponId forKey:@"objId"];
//        }
//        
//        if(externalId){
//            [params setObject:externalId forKey:@"eoid"];
//        }
//        if(acceptGroup){
//            [params setObject:acceptGroup forKey:@"ag"];
//        }
//        
//        if(promo_code){
//            [params setObject:promo_code forKey:@"pc"];
//        }
//        
//        if(title){
//            [params setObject:title forKey:@"title"];
//        }
//        
//        if(endDate){
//            [params setObject:endDate forKey:@"ed"];
//        }
//        
//        if(url){
//            [params setObject:url forKey:@"url"];
//        }

        //new
//        [params setValue:@"Notification Opened" forKey:@"event_name"];
//        [params setValue:[self stringFromDate:[NSDate date]] forKey:@"event_time"];
//#ifdef CLP_ANALYTICS
//        [_clpSDK updateAppEvent:params];
//#endif
        
    }
    @catch (NSException *exception) {
        LogInfo(@"didReceiveRemoteNotification : %@",exception.reason);
    }
}


//get current active shopping list id from plist
-(NSString*)getCurrentActiveListIdForAccountId:(NSString*)accId
{
    NSString *listId=@"";
    listId=[[NSUserDefaults standardUserDefaults] valueForKey:[NSString stringWithFormat:@"_activeList_%@",accId]];
    if(listId==nil){
        return @"";
    }
    else{
        return listId;
    }
    
}
//set current active shopping list id, name , total product count from plist
-(void)setCurrentActiveListId:(NSString*)listId setName:(NSString*)name setTotalProduct:(NSString*)total ForAccountId:(NSString*)accId{
    [[NSUserDefaults standardUserDefaults] setValue:listId forKey:[NSString stringWithFormat:@"_activeList_%@",accId]];
    [[NSUserDefaults standardUserDefaults] setValue:name forKey:[NSString stringWithFormat:@"_activeListName_%@",accId]];
    [[NSUserDefaults standardUserDefaults] setValue:total forKey:[NSString stringWithFormat:@"_activeListTotalProduct_%@",accId]];
    
    [[NSUserDefaults standardUserDefaults]synchronize];
}
//get current active shopping list name from plist
-(NSString*)getCurrentActiveListNameForAccountId:(NSString*)accId{
    NSString *listName=@"";
    listName=[[NSUserDefaults standardUserDefaults] valueForKey:[NSString stringWithFormat:@"_activeListName_%@",accId]];
    if(listName==nil){
        return @"";
    }
    else{
        return listName;
    }
}
//get current active shopping list product count from plist
-(NSString*)getCurrentActiveListProdTotalForAccountId:(NSString*)accId{
    NSString *listTotal=@"";
    listTotal=[[NSUserDefaults standardUserDefaults] valueForKey:[NSString stringWithFormat:@"_activeListTotalProduct_%@",accId]];
    if(listTotal==nil){
        return @"";
    }
    else{
        return listTotal;
    }
}
-(NSString*)stringFromDate:(NSDate*)date{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ssZ"];
    
    //Optionally for time zone conversions
//    [formatter setTimeZone:[NSTimeZone timeZoneWithName:@"..."]];
    
    NSString *stringFromDate = [formatter stringFromDate:date];
    return stringFromDate;
}
@end
