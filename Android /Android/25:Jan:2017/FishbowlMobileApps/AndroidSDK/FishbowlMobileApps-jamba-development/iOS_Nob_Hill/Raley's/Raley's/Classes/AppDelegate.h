 //
//  AppDelegate.h
//  Raley's
//
//  Created by Billy Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "Login.h"
#import "ShoppingList.h"
#import "Reachability.h"
#import "PersistentData.h"
#import "Offer.h"
#import "Product.h"
#import "Store.h"   
#import "ProductCategory.h"
#import "EcartPreOrderResponse.h"
#import "AccountRequest.h"
#import <PassKit/PassKit.h>
#import "Raleys.h"

#import "BeaconTime.h"

#import "clpsdk.h"
#import "PassbookLoader.h"


@class SplashScreenVC;
@class ShoppingScreenVC;
@class LoginOptionScreenVC;
@class Login;

#define IPHONE    1
#define IPHONE_5  2
#define IPAD      3

#define AUTH @"authKey"

#define BASE_URL @"https://www.raleys.com/www-ws/services/"
//#define BASE_URL @"https://webqa.vs.raleys.com/www-ws/services/"
//#define BASE_URL @"https://webqa2.vs.raleys.com/www-ws/services/"

#define LOGIN_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"customer/login"]
#define CHANGE_STORE_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"customer/changeDefaultStore"]
#define GET_STORES_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"store/getStoreList"]

#define ACCOUNT_GET_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"customer/getCustomer"]
#define ACCOUNT_REGISTRATION_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"customer/customerSignUp"]
#define ACCOUNT_UPDATE_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"customer/customerUpdate"]

#define OFFER_ACCEPT_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"offer/acceptOffer"]
#define OFFERS_ACCEPTED_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"offer/getAcceptedOffers"]
#define OFFERS_PERSONALIZED_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"offer/getPersonalizedOffers"]
#define OFFERS_EXTRA_FRIENDZY_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"offer/getExtraFriendzyOffers"]
#define OFFERS_MORE_FOR_YOU_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"offer/getMoreForYouOffers"]

#define PRODUCT_CATEGORIES_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"category/getMainCategories"]
#define PROMO_CATEGORIES_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"category/getPromoCategories"]
#define PRODUCT_FOR_UPC_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"item/findByUpc"]
#define PRODUCTS_FOR_CATEGORY_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"item/findItemsForCategory"]
#define PRODUCTS_FOR_PROMO_CATEGORY_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"item/findItemsForPromoCategory"]
#define PRODUCTS_BY_SEARCH_CATEGORY_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"/item/findBySearchText"]

#define LIST_CREATE_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"list/createList"]
#define LIST_DELETE_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"list/deleteList"]
#define LIST_GET_ALL_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"list/findListNamesForCustomer"]
#define LIST_GET_BY_ID_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"list/getListByAccountIdAndListId"]
#define LIST_ADD_ITEM_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"list/addItemToList"]
#define LIST_DELETE_ITEM_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"list/deleteItemFromList"]

#define ECART_PREORDER_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"order/ecartPreOrder"]
#define ECART_ORDER_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"order/ecartOrder"]

#define MISSING_IMAGES_URL [NSString stringWithFormat:@"%@%@", BASE_URL, @"image/missingImage"]

#define DEFAULT_LATITUDE 38.577160
#define DEFAULT_LONGITUDE -121.495560
#define METERS_PER_MILE 1609.344
#define MINUTE 60
#define HOUR 3600
#define DAY 86400

#define font_size27 27
#define font_size20 20
#define font_size17 17
#define font_size13 13
#define font_size10 10

#pragma mark - Keys
#define kNearbyNotificationsLastMessageTimestamp @"Nearby Notifications Timestamp of the last posted message"

#define COMMON_ERROR_MSG @"Unable to process your request. Please try again"

#define CLP_ANALYTICS 1

// Captions
// Headings
// Button , Titles font
// Description
// Product type

@class PersistentData;

@interface AppDelegate : UIResponder <UIApplicationDelegate, CLLocationManagerDelegate,clpSdkDelegate>
{
    int                   _offerThreadCount;
    int                   _imageThreadCount;
    NSString              *_supportDir;
    NSString              *_documentDir;
    
    NSString              *_persistentDataFile;
    NSObject              *_imageThreadCountLock;
    NSDateFormatter       *_managerDateFormatter;
    NSFileManager         *_fileManager;

    PersistentData        *_persistentData;
    
    // Clp iPad Beacon
    NSDate            *timeOfLastClpBeaconPoll;
    //
    
    // FOR PASSBOOK
    PKPassLibrary *_passLib;
    //
    
    clpsdk *_clpSDK;
    PassbookLoader              *pass_loader;

    
    
}

@property (strong, nonatomic) NSString              *_imageDir;

@property (strong, nonatomic) BeaconTime            *beaconSchedule;

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) SplashScreenVC *_splashScreenVC;
@property (strong, nonatomic) ShoppingScreenVC *_shoppingScreenVC;
@property (strong, nonatomic) LoginOptionScreenVC *_loginOptionScreenVC;


@property (nonatomic, assign) BOOL              notificationShown;
@property (nonatomic, strong) UIAlertView *     beaconAlertView;
@property (strong, nonatomic) CLBeaconRegion*   beaconRegion;
//@property (strong, nonatomic) CLLocationManager*locationManager;
@property (nonatomic) NSTimer* locationUpdateTimer;
@property (nonatomic, retain) clpsdk     *_clpSDK;
@property (nonatomic, retain) NSString    *token;
//@property (nonatomic, assign) BOOL _myStoreRefreshFlag;


@property (nonatomic, assign)int _deviceType;
@property (nonatomic, assign)int _viewWidth;
@property (nonatomic, assign)int _viewHeight;
@property (nonatomic, assign)int _headerHeight;
@property (nonatomic, assign)int _footerHeight;
@property (nonatomic, assign)int _offerTableWidth;
@property (nonatomic, assign)int _offerTableCellHeight;
@property (nonatomic, assign)int _productTableWidth;
@property (nonatomic, assign)int _productTableCellHeight;
@property (nonatomic, assign)int _shoppingListTableWidth;
@property (nonatomic, assign)int _shoppingListTableCellHeight;
@property (nonatomic, strong)NSString *_normalFont;
@property (nonatomic, strong)NSString *_boldFont;
@property (nonatomic, assign)BOOL _internetAvailable;
@property (nonatomic, assign)BOOL _gpsAvailable;
@property (nonatomic, assign)BOOL _retrievingShoppingList;
@property (nonatomic, assign)BOOL _locateForAccount;

@property (nonatomic, assign)CLLocationCoordinate2D _currentLocation;

@property (atomic, strong)UIView *_currentView;
@property (atomic, strong)Offer *_currentOffer;
@property (atomic, strong)Product *_currentProduct;
@property (atomic, strong)Product *_productToAdd;
@property (atomic, strong)Product *_productToModify;
@property (atomic, strong)Product *_productToDelete;
@property (atomic, strong)NSString *_productCategoriesFile;
@property (atomic, strong)NSString *_promoCategoriesFile;
@property (atomic, strong)ProductCategory *_productCategories;
@property (atomic, strong)ProductCategory *_promoCategories;
@property (atomic, strong)ShoppingList *_currentShoppingList,*_selectedShoppingList;
@property (atomic, strong)EcartPreOrderResponse *_currentEcartPreOrderResponse;
@property (atomic, strong)AccountRequest *_currentAccountRequest;
@property (atomic, strong)Store *_storeForAccount;
@property (atomic, strong)Store *_storeForEcart;
@property (atomic, strong)NSArray *_currentAppointmentList;
@property (atomic, strong)NSMutableArray *_acceptedOffersList;
@property (atomic, strong)NSMutableArray *_personalizedOffersList;
@property (atomic, strong)NSMutableArray *_extraFriendzyOffersList;
@property (atomic, strong)NSMutableArray *_moreForYouOffersList;
@property (atomic, strong)NSMutableDictionary *_allStoresList;

@property (atomic, strong)NSMutableDictionary  *_missingImagesList;

@property (nonatomic, strong)NSString *_loginFont;

//
@property (nonatomic) NSTimeInterval		nearbyNotificationsLastMessageTimestamp;

// CONSTANTS
@property (atomic, strong) NSString              *API_BASE_URL;


//

//+(AppDelegate*) shared;

-(void)storeRaleys;
-(Raleys*)getRaleys;


- (BOOL)internetAvailable;
- (BOOL)offerThreadsDone;
- (BOOL)imageThreadsDone;
- (void)getAvailableOffers;
- (void)getAcceptedOffers;
- (void)getProductImages:(NSArray *)productList;
- (UIImage *)getImageSync:(NSString *)fileName;
- (void)getImageAsync:(NSString *)urlString;
- (UIImage *)getCachedImage:(NSString *)fileName;
- (NSString *)getImageFullPath:(NSString *)fileName;
- (NSArray *)getNearestStores:(CLLocation *)currentLocation;

// persistent data methods
- (PersistentData *)getPersistentData;
- (void)storePersistentData;
- (Login *)getLogin;
- (void)storeLogin:(Login *)login;
- (BOOL)isLoggedIn;
- (void)logout;
- (int)getStoreNumber;
- (void)updateOfferUpdateSyncTime:(long)syncTime;
- (void)storeShoppingListId:(NSString *)listId;

// category cache methods
- (ProductCategory *)getProductCategoriesFromCache:(ProductCategory *)productCategories file:(NSString *)fileName;
- (void)storeProductCategoriesInCache:(ProductCategory *)categories file:(NSString *)fileName;


//- (void)startBeaconFound;
//- (void)showNotificationView:(NSString *)msgStr;
//- (void)hideBeaconNotificationView;
//- (void)startAirLocationFound;
//- (void)initRegion;

//for maintaining currentactivelist locally
-(NSString*)getCurrentActiveListIdForAccountId:(NSString*)accId;
-(void)setCurrentActiveListId:(NSString*)listId setName:(NSString*)name setTotalProduct:(NSString*)total ForAccountId:(NSString*)accId;
-(NSString*)getCurrentActiveListNameForAccountId:(NSString*)accId;
-(NSString*)getCurrentActiveListProdTotalForAccountId:(NSString*)accId;
- (void)getCurrentShoppingListThread:(NSString *)url;//existing func. but made it as public

//- (void)refreshMethodforBeacon:(NSTimer *)theTimer;
-(NSString*)stringFromDate:(NSDate*)date;
@end

#pragma mark - Keys
#define kNearbyNotificationsLastMessageTimestamp @"Nearby Notifications Timestamp of the last posted message"
