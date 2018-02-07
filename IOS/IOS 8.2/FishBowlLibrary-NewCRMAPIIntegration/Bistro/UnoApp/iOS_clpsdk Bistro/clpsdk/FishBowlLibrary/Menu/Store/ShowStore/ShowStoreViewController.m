//
//  ShowStoreViewController.m
//  clpsdk
//
//  Created by surendra pathak on 22/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "ShowStoreViewController.h"
#import "OrderCompleteViewController.h"
#import <MapKit/MapKit.h>
#import "PlaceAnnotation.h"
#import <CoreLocation/CoreLocation.h>
#import "SideMenuObject.h"
#import "MenuViewController.h"
#import "StorelocatorViewController.h"
#import "HomeViewController.h"
#import "UserProfileView.h"
#import "RewardsAndOfferView.h"
#import "ModelClass.h"
#import "LoyalityView.h"
#import "ShowTimeDayTableViewCell.h"
#import "ApiClasses.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "clpsdk.h"
#import "ContactUSView.h"
#import "FAQViewViewController.h"


@interface ShowStoreViewController ()<MKMapViewDelegate,CLLocationManagerDelegate,PushNavigation>
{
    PlaceAnnotation   * myAnnotation;
    SideMenuObject    * sideObject;
    UIViewController  * myController;
    ModelClass        * obj;
    MKPointAnnotation * annotation1;
    ApiClasses        * apiCall;
    clpsdk            * clpObj;
}

@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblStoreName;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightTableViewConst;
@property (unsafe_unretained, nonatomic) IBOutlet MKMapView *mapView;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblStoreName2;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lbldistance;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblPhoneNumber;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblAddress;
@property (weak, nonatomic) IBOutlet UIImageView *backgroundImage;
@property (weak, nonatomic) IBOutlet UITableView *tblHours;

@end

@implementation ShowStoreViewController
@synthesize arrayHours;
- (void)viewDidLoad {
    [super viewDidLoad];
   
    obj=[ModelClass sharedManager];
    sideObject = [[SideMenuObject alloc]init];
    
    [self StoreDetailApi];
    
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
    // Dispose of any resources that can be recreated.
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
   
   
     CLLocationCoordinate2D coordinate;
     NSUserDefaults *userDefaults=[NSUserDefaults standardUserDefaults];
     NSString *strLat=[userDefaults valueForKey:@"latitude"];
     NSString *strLong=[userDefaults valueForKey:@"longitude"];
     NSString *strPhone=[userDefaults valueForKey:@"phone"];
     NSString *strStoreName=[userDefaults valueForKey:@"storeName"];
     NSString *strAddress=[userDefaults valueForKey:@"address"];
   
  
    // distance calculator
    
    CLLocation *location1 = [[CLLocation alloc] initWithLatitude: 37.3462302 longitude:-121.9417057];
    CLLocation *location2 = [[CLLocation alloc] initWithLatitude:[strLat floatValue] longitude:[strLong floatValue]];
    NSLog(@"Distance i meters: %f", [location1 distanceFromLocation:location2]/1000);
    
    float miles = [location1 distanceFromLocation:location2]/1000 * 0.6213;
    
    self.lbldistance.text = [NSString stringWithFormat:@"%.0fmi",miles];
    
    _lblStoreName.text=strStoreName;
    _lblStoreName2.text=[[NSUserDefaults standardUserDefaults]valueForKey:@"city"];
    _lblAddress.text=strAddress;
    _lblPhoneNumber.text=strPhone;
    
    self.mapView.delegate = self;
  
    CLLocationManager *locationManager;
    
    locationManager = [[CLLocationManager alloc] init];
    locationManager.delegate = self;
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    locationManager.distanceFilter = kCLDistanceFilterNone;
    
    if([locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)])
    {
        [locationManager requestWhenInUseAuthorization];
    }
    
    [locationManager startUpdatingLocation];
    
     self.mapView.showsUserLocation = YES;
     self.mapView.scrollEnabled = YES;
     self.mapView.zoomEnabled = YES;
  
     [userDefaults synchronize];
    
     NSString *strCity=[userDefaults valueForKey:@"city"];
    
        coordinate.latitude  = [strLat floatValue];
        coordinate.longitude = [strLong floatValue];
        
        NSLog(@"Latitude is subMap %f and Longitude is %f",coordinate.longitude,coordinate.latitude);
        
         annotation1 = [[MKPointAnnotation alloc] init];
        [annotation1 setCoordinate:coordinate];
    
        
        if(strCity.length!=0)
        {
           [annotation1 setTitle:strCity]; //You can set the subtitle too
        }
        [self.mapView addAnnotation:annotation1];

      [self callMap];
  
}

#pragma  mark - Menu Api

-(void)StoreDetailApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSString * strStoreID = [def1 valueForKey:@"SearchstoreID"];
    NSLog(@"store ID --------- %@",strStoreID);
    NSString * str = [NSString stringWithFormat:@"/mobile/stores/getStoreDetails/%@",strStoreID];
    [apiCall getStoreDetailApi:nil url:str withTarget:self withSelector:@selector(getStoreRespomse:)];
    apiCall = nil;
}


-(void)getStoreRespomse:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"StoreDetail api response --------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        arrayHours = [[response valueForKey:@"mobileStores"]valueForKey:@"storeHourList"];
        [self.tblHours reloadData];
        
        if (arrayHours.count!=0) {
            if (19*(arrayHours.count) <_heightTableViewConst.constant)
            {
                
            }
            else
            {
              _heightTableViewConst.constant=20*(arrayHours.count);
            }
        }
    }
    else
    {
        
    }
}

# pragma mark - map nearest distance

-(void)callMap
{
        CLLocationCoordinate2D theCoordinate1;
        NSUserDefaults *userDefaults=[NSUserDefaults standardUserDefaults];
        NSString *strLat=[userDefaults valueForKey:@"latitude"];
        NSString *strLong=[userDefaults valueForKey:@"longitude"];
  
        theCoordinate1.latitude  = [strLat floatValue];
        theCoordinate1.longitude = [strLong floatValue];
        
        myAnnotation = [[PlaceAnnotation alloc] init];
        myAnnotation.coordinate = theCoordinate1;
         NSString *strCity=[userDefaults valueForKey:@"city"];
        [userDefaults synchronize];
         myAnnotation.title   = strCity;
        [_mapView addAnnotation:myAnnotation];
    
            _mapView.delegate = self;
        _mapView.mapType=MKMapTypeStandard;
        _mapView.showsUserLocation = YES;
        _mapView.userInteractionEnabled = YES;
    
    MKCoordinateRegion region;
    MKCoordinateSpan span;
    span.latitudeDelta = 0.01;
    span.longitudeDelta = 0.01;
    CLLocationCoordinate2D zoomLocation = myAnnotation.coordinate;
    region.center = zoomLocation;
    region.span = span;
    region = [self.mapView regionThatFits:region];
    [self.mapView setRegion:region animated:NO];
    
}


#pragma mark -  Map View Annotation

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
    if ([annotation isKindOfClass:[MKUserLocation class]])
        return nil;
    
    static NSString* AnnotationIdentifier = @"AnnotationIdentifier";
    MKAnnotationView* pinView = [[MKAnnotationView alloc]
                                 initWithAnnotation:annotation reuseIdentifier:AnnotationIdentifier];
    
    //pinView.animatesDrop   = NO;
    pinView.canShowCallout = YES;
    pinView.image = [UIImage imageNamed:@"location.png"];
    //pinView.pinColor = MKPinAnnotationColorRed;
  
    return pinView;
}



# pragma mark - tapBack method

- (IBAction)tapBack:(UIButton *)sender
{
[self.navigationController popViewControllerAnimated:YES];
}

# pragma mark - Reservation method

- (IBAction)tapMakeAReservation:(UIButton *)sender
{
  
    BOOL fromMenuToStore=[[[NSUserDefaults standardUserDefaults]valueForKey:@"MenuToStore"]boolValue];
    if (fromMenuToStore==YES) {
        MenuViewController * orderCompObj = [[MenuViewController alloc]initWithNibName:@"MenuViewController" bundle:nil];
        [self.navigationController pushViewController:orderCompObj animated:YES];
    }
    else
    {
        OrderCompleteViewController * orderCompObj = [[OrderCompleteViewController alloc]initWithNibName:@"OrderCompleteViewController" bundle:nil];
        [self.navigationController pushViewController:orderCompObj animated:YES];
    }
    
}



# pragma mark - Order Online

- (IBAction)tapOrderOnline:(UIButton *)sender
{
    BOOL fromMenuToStore=[[[NSUserDefaults standardUserDefaults]valueForKey:@"MenuToStore"]boolValue];
    if (fromMenuToStore==YES) {
        MenuViewController * orderCompObj = [[MenuViewController alloc]initWithNibName:@"MenuViewController" bundle:nil];
        [self.navigationController pushViewController:orderCompObj animated:YES];
    }
    else
    {
        OrderCompleteViewController * orderCompObj = [[OrderCompleteViewController alloc]initWithNibName:@"OrderCompleteViewController" bundle:nil];
        [self.navigationController pushViewController:orderCompObj animated:YES];
    }
}


# pragma mark - mapType method

- (IBAction)mapType:(UISegmentedControl *)sender
{
    switch (sender.selectedSegmentIndex) {
        case 0:
            self.mapView.mapType=MKMapTypeStandard;
            break;
        case 1:
            self.mapView.mapType=MKMapTypeSatellite;
            break;
        case 2:
            self.mapView.mapType=MKMapTypeHybrid;
            break;
        default:
            break;
    }
}

#pragma mark -  Table View Data Source


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    //  NSLog(@"%lu",(unsigned long)[storesDict count]);
    return arrayHours.count;
   
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    ShowTimeDayTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ShowTimeDayTableViewCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"ShowTimeDayTableViewCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
    //    if(indexPath.row%2 == 0)
    //    {
    
    cell.backgroundColor=[UIColor clearColor];
//    if([[arrayHours valueForKey:@"dayOfTheWeek"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
//    {
    NSInteger dayStr;
    dayStr=[[[arrayHours valueForKey:@"dayOfTheWeek"]objectAtIndex:indexPath.row] integerValue];
    
    switch (dayStr)
    {
        case 0:
            cell.lblWeekDay.text=@"Sunday";
            break;
        case 1:
            cell.lblWeekDay.text=@"Monday";
            break;
        case 2:
            cell.lblWeekDay.text=@"Tuesday";
            break;
        case 3:
            cell.lblWeekDay.text=@"Wednesday";
            break;
        case 4:
            cell.lblWeekDay.text=@"Thursdayday";
            break;
        case 5:
            cell.lblWeekDay.text=@"Friday";
            break;
        default:
            cell.lblWeekDay.text=@"Saturday";
            break;
    }
    
    //}
    //if([[arrayHours valueForKey:@"openingTime"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    //{
//        cell.lblStartTime.text=[NSString stringWithFormat:@"%@",[[arrayHours valueForKey:@"openingTime"]objectAtIndex:indexPath.row]];
    
    
    NSString *dats1 = [NSString stringWithFormat:@"%@",[[arrayHours valueForKey:@"openingTime"]objectAtIndex:indexPath.row]];
    NSDateFormatter *dateFormatter3 = [[NSDateFormatter alloc] init];
    [dateFormatter3 setDateStyle:NSDateFormatterMediumStyle];
    [dateFormatter3 setDateFormat:@"HH:mm:ss"];
    NSDate *date1 = [dateFormatter3 dateFromString:dats1];
    NSLog(@"date1 : %@", date1);
    // Here returning (null)**
    
   // NSDateFormatter *formatter1 = [[NSDateFormatter alloc]init];
    [dateFormatter3 setDateFormat:@"hh:mm a"];
    NSLog(@"Current Date: %@", [dateFormatter3 stringFromDate:date1]);
    NSString *startADate=[NSString stringWithFormat:@"%@",[dateFormatter3 stringFromDate:date1]];
    
    
    NSString *datsend = [NSString stringWithFormat:@"%@",[[arrayHours valueForKey:@"closingTime"]objectAtIndex:indexPath.row]];
    NSDateFormatter *dateFormatterEnd = [[NSDateFormatter alloc] init];
    [dateFormatterEnd setDateStyle:NSDateFormatterMediumStyle];
    [dateFormatterEnd setDateFormat:@"HH:mm:ss"];
    NSDate *dateEnd = [dateFormatterEnd dateFromString:datsend];
    NSLog(@"date1 : %@", datsend);
    // Here returning (null)**
    
    // NSDateFormatter *formatter1 = [[NSDateFormatter alloc]init];
    [dateFormatterEnd setDateFormat:@"hh:mm a"];
    NSLog(@"Current Date: %@", [dateFormatterEnd stringFromDate:dateEnd]);
    
    NSString *EndDate=[NSString stringWithFormat:@"%@",[dateFormatterEnd stringFromDate:dateEnd]];
    
    
    
    NSString *strTime=[NSString stringWithFormat:@"%@ to %@",startADate,EndDate];
    //}
    //if([[arrayHours valueForKey:@"closingTime"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    //{
    cell.lblEndTime.text=strTime;
    //}


    return cell;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
}

# pragma mark - sideMenu method

- (IBAction)menuBtn_Action:(id)sender
{
    sideObject.delegate = self;
    [sideObject SideMenuAction:self.view];
}


// delegate method
-(void)didSelectAtIndexPathRow:(NSIndexPath *)indexPath
{
    if(indexPath.row == 0)
    {
        if([self isViewControllerExist:[HomeViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
    }
    else if(indexPath.row == 1)
    {
        
        if([self isViewControllerExist:[LoyalityView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
        LoyalityView * menuObj = [[LoyalityView alloc]initWithNibName:@"LoyalityView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
        
        
        //[sideObject removeSideNave];
    }
    else if(indexPath.row == 2)
    {
        if([self isViewControllerExist:[MenuViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"MenuToStore"];
            MenuViewController * menuObj = [[MenuViewController alloc]initWithNibName:@"MenuViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 3)
    {
        if([self isViewControllerExist:[StorelocatorViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"MenuToStore"];
            StorelocatorViewController * menuObj = [[StorelocatorViewController alloc]initWithNibName:@"StorelocatorViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 4)
    {
        if([self isViewControllerExist:[RewardsAndOfferView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            RewardsAndOfferView * menuObj = [[RewardsAndOfferView alloc]initWithNibName:@"RewardsAndOfferView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
//    else if(indexPath.row == 5)
//    {
//        [sideObject removeSideNave];
//        [self alertViewDelegate:@"No Setting Available"];
//    }
    else if(indexPath.row == 5)
    {
        if([self isViewControllerExist:[UserProfileView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            UserProfileView * menuObj = [[UserProfileView alloc]initWithNibName:@"UserProfileView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 6)
    {
        if([self isViewControllerExist:[FAQViewViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            FAQViewViewController * menuObj = [[FAQViewViewController alloc]initWithNibName:@"FAQViewViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 7)
    {
        if([self isViewControllerExist:[ContactUSView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            ContactUSView * menuObj = [[ContactUSView alloc]initWithNibName:@"ContactUSView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else
    {
        [sideObject removeSideNave];
    }
}

# pragma mark - alert view method

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
}


# pragma mark - check controller exist

-(BOOL)isViewControllerExist:(Class)isController
{
    for (UIViewController *controller in self.navigationController.viewControllers)
    {
        if ([controller isKindOfClass:isController])
        {
            myController = controller;
            return YES;
        }
    }
    return NO;
}


# pragma mark - logOutBt Action

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
                                  [obj RemoveBottomView];
                                  [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"changePassword"];
                                  
                                  // clp mobile setting api
                                  clpObj = [clpsdk sharedInstanceWithAPIKey];
                                  
                                  // event tracking method
                                  NSMutableDictionary * eventDic = [NSMutableDictionary new];
                                  NSString * userName = [[NSUserDefaults standardUserDefaults]valueForKey:@"userName"];
                                  [eventDic setValue:@"LOGOUT" forKey:@"event_name"];
                                  [eventDic setValue:userName forKey:@"item_name"];
                                  [clpObj updateAppEvent:eventDic];
                                  eventDic = nil;
                                  [self.navigationController popToRootViewControllerAnimated:YES];
                                  [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"login"];
                                  
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



# pragma mark - favStoreApi

-(void)favStoreApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    
     NSString * customerID = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];
     [dict setValue:customerID forKey:@"memberid"];
    
     NSString * storeNumber = [[NSUserDefaults standardUserDefaults]valueForKey:@"storeNumber"];
     [dict setValue:storeNumber forKey:@"storeCode"];
    
    [apiCall favouritStorehApi:dict url:@"/member/updateStore" withTarget:self withSelector:@selector(favouritStore:)];
    apiCall = nil;
}


// favStore api response
-(void)favouritStore:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"favouritStoreh response value------%@",response);
   
    // response success
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
         [self alertViewDelegate:@"Store Updated Successfully"];
        
         NSString * storeID = [[NSUserDefaults standardUserDefaults]valueForKey:@"SearchstoreID"];
        
         NSLog(@"store id for store -------- %@",storeID);
        
        [[NSUserDefaults standardUserDefaults]setValue:storeID forKey:@"storeFavID"];
        
        NSUserDefaults *userDefaults=[NSUserDefaults standardUserDefaults];
        NSString *strStoreName=[userDefaults valueForKey:@"storeName"];
        NSString *strAddress=[userDefaults valueForKey:@"address"];
        NSString *storeNumber = [userDefaults valueForKey:@"phone"];
        
        obj.bottomObj.lblStoreAddress.text = strAddress;
        obj.bottomObj.lblStoreName.text = strStoreName;
        
        [[NSUserDefaults standardUserDefaults]setValue:storeNumber forKey:@"phoneNumber"];
        
        NSString *strLat=[userDefaults valueForKey:@"latitude"];
        NSString *strLong=[userDefaults valueForKey:@"longitude"];
        
        CLLocation *location1 = [[CLLocation alloc] initWithLatitude: 37.3462302 longitude:-121.9417057];
        CLLocation *location2 = [[CLLocation alloc] initWithLatitude:[strLat floatValue] longitude:[strLong floatValue]];
        NSLog(@"Distance i miles: %f", [location1 distanceFromLocation:location2]/1000);
        
        CLLocationDistance distance = [location1 distanceFromLocation:location2];

        NSLog(@"Calculated Miles %@", [NSString stringWithFormat:@"%.1fmi",(distance/1609.344)]);

        float miles = [location1 distanceFromLocation:location2]/1000 * 0.6213;
        
        obj.bottomObj.lblDistance.text = [NSString stringWithFormat:@"%.0fmi",miles];
        
        // clp mobile setting api
        clpObj = [clpsdk sharedInstanceWithAPIKey];
        
        // event tracking method
        NSMutableDictionary * eventDic = [NSMutableDictionary new];
        [eventDic setValue:@"FAV_STORE" forKey:@"event_name"];
        [eventDic setValue:strStoreName forKey:@"item_name"]; // store name
        [clpObj updateAppEvent:eventDic];
        eventDic = nil;
        
//        [[NSUserDefaults standardUserDefaults]setValue:obj.bottomObj.lblDistance.text forKey:@"Distance"];
        
        NSLog(@"store number -------- %@",storeNumber);
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        [self getMemberApi];
    }
    else
    {
        [self alertViewDelegate:[response valueForKey:@"message"]];
    }
}

# pragma mark - getMember Api

- (void)getMemberApi
{
    //[obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall getMember:nil url:@"/member/getMember" withTarget:self withSelector:@selector(getMember:)];
    apiCall = nil;
}

// api response

-(void)getMember:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"member response value------%@",response);
    
    if(response != nil)
    {
             NSUserDefaults *def = [NSUserDefaults standardUserDefaults];
            [def setObject:[NSKeyedArchiver archivedDataWithRootObject:response] forKey:@"MyData"];
            [def synchronize];
    }
}


# pragma mark - DirectionButton Action

- (IBAction)directionButton_Action:(id)sender
{
    NSUserDefaults *userDefaults=[NSUserDefaults standardUserDefaults];
    NSString *strLat=[userDefaults valueForKey:@"latitude"];
    NSString *strLong=[userDefaults valueForKey:@"longitude"];
    
    NSString* url = [NSString stringWithFormat:@"http://maps.apple.com/?saddr=%f,%f&daddr=%f,%f",37.3462302, -121.9417057, strLat.floatValue,strLong.floatValue];
    [[UIApplication sharedApplication] openURL: [NSURL URLWithString: url]];
    
}

# pragma mark - setFavouritBtn Action

- (IBAction)setFavouritBtn_Action:(id)sender
{
    [self favStoreApi];
}

@end
