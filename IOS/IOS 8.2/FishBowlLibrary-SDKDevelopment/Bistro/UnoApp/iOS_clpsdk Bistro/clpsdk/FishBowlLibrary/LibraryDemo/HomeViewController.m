//
//  HomeViewController.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 19/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//



#import "HomeViewController.h"
#import "StorelocatorViewController.h"
#import "MenuCell.h"
#import "collectionCell.h"
#import "ModelClass.h"
#import "ApiClasses.h"
#import "clpsdk.h"
#import "OfferDetailView.h"
#import "OfferCell.h"
#import "HomeCellTable.h"
#import "UserProfileView.h"
#import "MenuTableViewCell.h"
#import "MenuViewController.h"
#import "ChangePasswordView.h"
#import "SideMenuObject.h"
#import "LoyalityView.h"
#import "RewardsAndOfferView.h"
#import "StorelocatorViewController.h"
#import "BottomView.h"
#import "DirectionMapView.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "clpsdk.h"
#import "FAQViewViewController.h"
#import "ContactUSView.h"
#import "MyLoyalityView.h"
#import "HtmlOffersAndRewards.h"



@interface HomeViewController ()<UIScrollViewDelegate,UIGestureRecognizerDelegate,PushNavigation,bottomView>
{
    NSMutableArray   * arrImageBanner;
    NSMutableArray   * arrOffer;
    int i;
    ModelClass       * obj;
    ApiClasses       * apiCall;
    NSDictionary     * dict;
    BOOL               menuOpen;
    SideMenuObject   * sideObject;
    NSMutableArray   * arrRewards;
    NSString         * strLat;
    NSString         * strLong;
    clpsdk           * clpObj;
}

@property (weak, nonatomic) IBOutlet UIImageView   *imagePage;
@property (weak, nonatomic) IBOutlet UIPageControl *pageControl;
@property (weak, nonatomic) IBOutlet UIImageView   *backgroundImage;
@property (weak, nonatomic) IBOutlet UILabel       *rewardsAndOfferLbl;
@property (weak, nonatomic) IBOutlet UILabel       *loyalityPointLbl;
@property (weak, nonatomic) IBOutlet UIView        *dashboardView;

@end


@implementation HomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
      obj=[ModelClass sharedManager];
   
      sideObject = [[SideMenuObject alloc]init];
       [obj AddBottomView];
      arrImageBanner = [[NSMutableArray alloc]initWithObjects: [UIImage imageNamed:@"Happy.png"],[UIImage imageNamed:@"Banner03.png"],[UIImage imageNamed:@"Banner04.png"], nil];

    // corner radius of view
//    [self addCornerRadius:self.locationView];
//    [self addCornerRadius:self.myLoyalityView];
//    [self addCornerRadius:self.menuView];
//    [self addCornerRadius:self.rewardsAndOfferView];
    
     // shado offect
    [self shadoOffect:self.locationView];
    [self shadoOffect:self.myLoyalityView];
    [self shadoOffect:self.menuView];
    [self shadoOffect:self.rewardsAndOfferView];
    
    //[self shadoOffect:self.dashboardView];
    
    
//
//    // corner radius of label
//    [self addLabelCornerRadius:self.loyalityLabel];
//    [self addLabelCornerRadius:self.rewardsLabel];
    
    
    //mobile setting
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    //self.imagePage.image = [arrImageBanner objectAtIndex:0];
        
    i=0;
    
    // timer method call
    [self changeBannerAutomatically];
    
    self.pageControlView.numberOfPages=arrImageBanner.count;
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(receiveTestNotification:)
                                                 name:@"TestNotification"
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(pushBackground) name:@"appDidBecomeActive" object:nil];
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(RewardsAndOffer:) name:@"RewardsAndOffer" object:nil];
    
//    [self getRewardsCount];
    
}

- (void)RewardsAndOffer:(NSNotification *) notification
{
    
    if ([[notification name] isEqualToString:@"RewardsAndOffer"])
    {
         [self getRewardsCount];
    }
}



- (void) receiveTestNotification:(NSNotification *) notification
{
    
    
    if ([[notification name] isEqualToString:@"TestNotification"])
    {
//        DirectionMapView * mapObj = [[DirectionMapView alloc]initWithNibName:@"DirectionMapView" bundle:nil];
//        
//        mapObj.latitudeDirection = strLat;
//        mapObj.longitudeDirection = strLong;
//    [self.navigationController pushViewController:mapObj animated:YES];
        
        
        NSString* url = [NSString stringWithFormat:@"http://maps.apple.com/?saddr=%f,%f&daddr=%f,%f",37.3462302, -121.9417057, strLat.floatValue,strLong.floatValue];
        [[UIApplication sharedApplication] openURL: [NSURL URLWithString: url]];
        
        
        
//        Class mapItemClass = [MKMapItem class];
//        if (mapItemClass && [mapItemClass respondsToSelector:@selector(openMapsWithItems:launchOptions:)])
//        {
//            // Create an MKMapItem to pass to the Maps app
//            CLLocationCoordinate2D coordinate =
//            CLLocationCoordinate2DMake([strLat floatValue], [strLong floatValue]);
//            MKPlacemark *placemark = [[MKPlacemark alloc] initWithCoordinate:coordinate
//                                                           addressDictionary:nil];
//            MKMapItem *mapItem = [[MKMapItem alloc] initWithPlacemark:placemark];
//            [mapItem setName:@"My Place"];
//            
//            // Set the directions mode to "Walking"
//            // Can use MKLaunchOptionsDirectionsModeDriving instead
//            NSDictionary *launchOptions = @{MKLaunchOptionsDirectionsModeKey : MKLaunchOptionsDirectionsModeDriving};
//            // Get the "Current User Location" MKMapItem
//            MKMapItem *currentLocationMapItem = [MKMapItem mapItemForCurrentLocation];
//            // Pass the current location and destination map items to the Maps app
//            // Set the direction mode in the launchOptions dictionary
//            [MKMapItem openMapsWithItems:@[currentLocationMapItem, mapItem] 
//                           launchOptions:launchOptions];
//        }
        

        NSLog (@"Successfully received the test notification!");
    }
}



-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    [self favouritStoreAndAddress];

    
    if([[NSUserDefaults standardUserDefaults]boolForKey:@"pushPassCome"] == YES)
    {
        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"pushPassCome"];
        
        dispatch_async( dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            dispatch_async( dispatch_get_main_queue(), ^{
                
                    [self getRewardsCount];
                
            });
        });
    }
    else
    {
        
    }
    
    
    
    [obj setBottomframe];
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];

    [self getRewardsCount];

}


-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 3;
    shadoView.layer.shadowOpacity = 0.2;
//    shadoView.layer.shadowColor = [[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:.3] CGColor];
}

// fav storeName and Favourite storeAddress
-(void)favouritStoreAndAddress
{
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data2 = [def1 objectForKey:@"MyData"];
    NSDictionary *retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data2];
    
    NSString * str = [retrievedDictionary valueForKey:@"homeStoreID"];
    NSInteger strFavID = [str integerValue];
    
    NSLog(@"favID is %@",str);
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"storesKey"];
    NSArray * arrStoreNumber = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    NSArray * str1 = [arrStoreNumber filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(storeID == %ld)",(long)strFavID]];
    
    NSLog(@"str code value --------- %@",str1);
    if(str1.count!=0)
    {
        obj.bottomObj.lblStoreName.text    =  [[str1 objectAtIndex:0]valueForKey:@"storeName"];
        obj.bottomObj.lblStoreAddress.text =  [[str1 objectAtIndex:0]valueForKey:@"address"];
        [[NSUserDefaults standardUserDefaults]setValue:[[str1 objectAtIndex:0]valueForKey:@"phone"] forKey:@"phoneNumber"];
        
        strLat  =[[str1 objectAtIndex:0]valueForKey:@"latitude"];
        strLong =[[str1 objectAtIndex:0]valueForKey:@"longitude"];
        
    CLLocation *location1 = [[CLLocation alloc] initWithLatitude: 37.3462302 longitude:-121.9417057];
    CLLocation *location2 = [[CLLocation alloc] initWithLatitude:[strLat floatValue] longitude:[strLong floatValue]];
        
        NSLog(@"Distance i meters: %.0f", [location1 distanceFromLocation:location2]*0.000621371);
        float miles = [location1 distanceFromLocation:location2]/1000 * 0.6213;
        obj.bottomObj.lblDistance.text = [NSString stringWithFormat:@"%.0fmi",miles];
        
        NSLog(@"favourit store name and address and lat and long -------- %@ %@ %@ %@", obj.bottomObj.lblStoreName.text,obj.bottomObj.lblStoreAddress.text,strLat,strLong);
    }
    else
    {
        NSLog(@"favourit store id -------- %ld",(long)strFavID);
    }

}


// push notification
-(void)pushBackground
{
    if([[NSUserDefaults standardUserDefaults]boolForKey:@"isPushBackGround"]==YES)
    {
        
        if([[[NSUserDefaults standardUserDefaults] valueForKey:@"MessageType"] isEqualToString:@"me"])
        {
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"isPushBackGround"];
            LoyalityView * offerObject = [[LoyalityView alloc]initWithNibName:@"LoyalityView" bundle:nil];
            [self.navigationController pushViewController:offerObject animated:YES];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"isPushBackGround"];

            NSString *strURL = [[NSUserDefaults standardUserDefaults] valueForKey:@"urlKey"];
            
            if ([[[NSUserDefaults standardUserDefaults] valueForKey:@"promocode"] isEqualToString:@""])
            {
                
            }
            else if (strURL.length>0)
            {

                    HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                    offerObj.rewardsAndOfferTitle = @"Rewards";
                    offerObj.htmlBody = strURL;
                    [self.navigationController pushViewController:offerObj animated:YES];
            }
            
            else
            {
        
        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"isPushBackGround"];
        OfferDetailView * offerObject = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
        [self.navigationController pushViewController:offerObject animated:YES];
            }
        }
    }
}


# pragma mark - View Corner Radius

-(void)addCornerRadius:(UIView*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 2.0;
    textField.layer.borderColor = [[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:.3] CGColor];
}

-(void)addLabelCornerRadius:(UILabel*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 0.5;
    textField.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
}



# pragma mark - alert method
-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
}


# pragma mark - Timer method

-(void)changeBannerAutomatically
{
    [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(slideChange:) userInfo:nil repeats:YES];
}


# pragma mark - Page Controller

- (IBAction)slideChange:(id)sender
{
    if (i<arrImageBanner.count)
    {
        self.imagePage.image = [arrImageBanner objectAtIndex:i];
        self.pageControl.currentPage=i;
        i++;
        if (i==arrImageBanner.count)
        {
            i=0;
        }
    }
}

# pragma mark - Gesture Methods

- (IBAction)gestureLeft:(id)sender
{
   // left
    NSInteger p=self.pageControl.currentPage;
    if (p==arrImageBanner.count-1)
    {
        self.imagePage.image = [arrImageBanner objectAtIndex:0];
        self.pageControl.currentPage=0;
    }
    else
    {
        self.imagePage.image = [arrImageBanner objectAtIndex:p+1];
        self.pageControl.currentPage=p+1;
    }
    
}


-(IBAction)gestureRight:(id)sender
{
   // right
    long p=self.pageControl.currentPage;
    if (p==0)
    {
        NSInteger n=arrImageBanner.count;
        self.imagePage.image = [arrImageBanner objectAtIndex:n-1];
        self.pageControl.currentPage=n-1;
    }
    else
    {
        self.imagePage.image = [arrImageBanner objectAtIndex:p-1];
        self.pageControl.currentPage=p-1;
    }
}

# pragma mark - memory method

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


# pragma mark - sideMenu method

- (IBAction)sideMenu_Action:(id)sender
{
       sideObject.delegate = self;
       [sideObject SideMenuAction:self.view];
}


// delegate method
-(void)didSelectAtIndexPathRow:(NSIndexPath *)indexPath
{
    
    if(indexPath.row == 0)
    {
        [sideObject removeSideNave];
    }
    else if(indexPath.row == 1)
    {
        LoyalityView * menuObj = [[LoyalityView alloc]initWithNibName:@"LoyalityView" bundle:nil];
        [self.navigationController pushViewController:menuObj animated:YES];
    
    }
    else if(indexPath.row == 2)
    {
        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"MenuToStore"];
        MenuViewController * menuObj = [[MenuViewController alloc]initWithNibName:@"MenuViewController" bundle:nil];
        [self.navigationController pushViewController:menuObj animated:YES];
    }
    else if(indexPath.row == 3)
    {
        [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"MenuToStore"];
        StorelocatorViewController * menuObj = [[StorelocatorViewController alloc]initWithNibName:@"StorelocatorViewController" bundle:nil];
        [self.navigationController pushViewController:menuObj animated:YES];
    }
    else if(indexPath.row == 4)
    {
            RewardsAndOfferView * menuObj = [[RewardsAndOfferView alloc]initWithNibName:@"RewardsAndOfferView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
    }
//    else if(indexPath.row == 5)
//    {
//        [sideObject removeSideNave];
//        [self alertViewDelegate:@"No Setting available"];
//    }
    else if(indexPath.row == 5)
    {
        UserProfileView * menuObj = [[UserProfileView alloc]initWithNibName:@"UserProfileView" bundle:nil];
        [self.navigationController pushViewController:menuObj animated:YES];
    }
    else if(indexPath.row == 6)
    {
            FAQViewViewController * menuObj = [[FAQViewViewController alloc]initWithNibName:@"FAQViewViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
    }
    else if(indexPath.row == 7)
    {
        ContactUSView * menuObj = [[ContactUSView alloc]initWithNibName:@"ContactUSView" bundle:nil];
        [self.navigationController pushViewController:menuObj animated:YES];
    }
    else
    {
         [sideObject removeSideNave];
    }
    
}

#pragma mark - Dashboard Buttons Action

- (IBAction)dashboardMenu_Action:(id)sender
{
    UIButton * btn = (UIButton *)sender;
    // my Loyality
    if(btn.tag == 1)
    {
        LoyalityView * menuObj = [[LoyalityView alloc]initWithNibName:@"LoyalityView" bundle:nil];
        [self.navigationController pushViewController:menuObj animated:YES];
        
//        MyLoyalityView * menuObj = [[MyLoyalityView alloc]initWithNibName:@"MyLoyalityView" bundle:nil];
//        [self.navigationController pushViewController:menuObj animated:YES];
        
    }
    // menu
   else if(btn.tag == 2)
    {
        //obj.itemDictArray=nil;
        
        [obj.itemDictArray removeAllObjects];
        
        
         [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"MenuToStore"];
        MenuViewController * menuObj = [[MenuViewController alloc]initWithNibName:@"MenuViewController" bundle:nil];
        [self.navigationController pushViewController:menuObj animated:YES];
    }
    // Location
   else if(btn.tag == 3)
   {
      // obj.itemDictArray=nil;
       [obj.itemDictArray removeAllObjects];
       
          [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"MenuToStore"];
       StorelocatorViewController * menuObj = [[StorelocatorViewController alloc]initWithNibName:@"StorelocatorViewController" bundle:nil];
       [self.navigationController pushViewController:menuObj animated:YES];
   }
    // Rewards And Offers
   else if(btn.tag == 4)
   {
       RewardsAndOfferView * menuObj = [[RewardsAndOfferView alloc]initWithNibName:@"RewardsAndOfferView" bundle:nil];
       [self.navigationController pushViewController:menuObj animated:YES];
   }
}


// logout button Action
-(void)logOutBt_Action
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:@"Do you want to Logout."
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                                  
                            FBSDKLoginManager *loginManager = [[FBSDKLoginManager alloc] init];
                            [loginManager logOut];
                            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"changePassword"];

                            [obj RemoveBottomView];
                                  
                                  // clp mobile setting api
                                  clpObj = [clpsdk sharedInstanceWithAPIKey];
                                  
                                  // event tracking method
                                  NSMutableDictionary * eventDic = [NSMutableDictionary new];
                                  NSString * userName = [[NSUserDefaults standardUserDefaults]valueForKey:@"userName"];
                                  [eventDic setValue:@"LOGOUT" forKey:@"event_name"];
                                  [eventDic setValue:userName forKey:@"item_name"];
                                  [clpObj updateAppEvent:eventDic];
                                  eventDic = nil;
                                  
                                  
                                  [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"login"];
                                  [self.navigationController popToRootViewControllerAnimated:YES];
                              }];
    
    [alert addAction:cancel1];
    UIAlertAction* noButton = [UIAlertAction
                               actionWithTitle:@"NO"
                               style:UIAlertActionStyleDefault
                               handler:^(UIAlertAction * action)
                               {
                                   //Handel your yes please button action here
                                   [alert dismissViewControllerAnimated:YES completion:nil];
                                   
                               }];
    [alert addAction:noButton];
    
    [self presentViewController:alert animated:YES completion:nil];
}


// dealloc method

-(void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


// getRewards api

-(void)getRewardsCount
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerID = [dic valueForKey:@"customerID"];
    NSLog(@"customer id---------%@",strCustomerID);
    NSString * str = [NSString stringWithFormat:@"/mobile/getrewards/%@/0",strCustomerID];
    
    [apiCall getRewards:nil url:str withTarget:self withSelector:@selector(getRewardsCount:)];
    apiCall = nil;
}

// getoffers api response

-(void)getRewardsCount:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"get getrewards response---------%@",response);
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        arrRewards = (NSMutableArray *)[response valueForKey:@"inAppOfferList"];
    }
    else
    {
        [obj removeLoadingView:self.view];
    }
    
    [self getCustomerOffersCount];
}




# pragma mark - Offers Api

-(void)getCustomerOffersCount
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerID = [dic valueForKey:@"customerID"];
    NSLog(@"customer id---------%@",strCustomerID);
    NSString * str = [NSString stringWithFormat:@"/mobile/getoffers/%@/0",strCustomerID];
    
    [apiCall getOffers:nil url:str withTarget:self withSelector:@selector(getOffersCount:)];
    apiCall = nil;
}

// getoffers api response

-(void)getOffersCount:(id)response
{
     [obj removeLoadingView:self.view];
    
    NSLog(@"get offers response---------%@",response);
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        NSArray * arr = (NSArray *)[response valueForKey:@"inAppOfferList"];
        self.rewardsAndOfferLbl.text = [NSString stringWithFormat:@"%ld",(long)arr.count + arrRewards.count];
        
//        if(arr.count!=0)
//        {
//            self.rewardsAndOfferLbl.text = [NSString stringWithFormat:@"%ld",(long)arr.count + arrRewards.count];
//        }
//        else
//        {
//            if(arrRewards.count!=0)
//            {
//            self.rewardsAndOfferLbl.text = [NSString stringWithFormat:@"%ld",(long)arrRewards.count];
//            }
//            else
//            {
//                self.rewardsAndOfferLbl.text = [NSString stringWithFormat:@"0"];
//            }
//        }
    }
    else
    {
        self.rewardsAndOfferLbl.text = [NSString stringWithFormat:@"%ld",(long)arrRewards.count];

        
//        if(arrRewards.count!=0)
//        {
//        self.rewardsAndOfferLbl.text = [NSString stringWithFormat:@"%ld",(long)arrRewards.count];
//        }
//        else
//        {
//            self.rewardsAndOfferLbl.text = [NSString stringWithFormat:@"0"];
//        }
    }
    
    [self loyalityApi];
}


#pragma mark - Loyality Api

-(void)loyalityApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerID = [dic valueForKey:@"customerID"];
    NSLog(@"customer id---------%@",strCustomerID);
    NSString * str = [NSString stringWithFormat:@"/mobile/getPointRewardInfo/%@",strCustomerID];
    
    [apiCall getLoyalityPoints:nil url:str withTarget:self withSelector:@selector(getLoyalityCount:)];
    apiCall = nil;
}

-(void)getLoyalityCount:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"loyality api response ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        if([response valueForKey:@"earnedPoints"] != (NSString *)[NSNull null])
        {
        NSNumber  *strMax = [response valueForKey:@"earnedPoints"];
        self.loyalityPointLbl.text = [strMax stringValue];
        }
    }
}

//-(void)tapCallMethod
//{
//    DirectionMapView * mapObj = [[DirectionMapView alloc]initWithNibName:@"DirectionMapView" bundle:nil];
//    [self.navigationController pushViewController:mapObj animated:YES];
//}


#pragma mark - Adds Api

-(void)AddsApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerID = [dic valueForKey:@"customerID"];
    NSLog(@"customer id---------%@",strCustomerID);
    NSString * str = [NSString stringWithFormat:@"/inAppAd/getAdByAdSourcePageLocationId?sourcePageLocationId=94&memberId=%@",strCustomerID];
    
    [apiCall inAppAd:nil url:str withTarget:self withSelector:@selector(inAppAd:)];
    apiCall = nil;
}


-(void)inAppAd:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"ads response value-------%@",response);
}

@end
