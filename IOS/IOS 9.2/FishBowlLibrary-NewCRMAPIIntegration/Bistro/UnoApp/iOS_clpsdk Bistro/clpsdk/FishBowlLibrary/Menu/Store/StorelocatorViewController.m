//
//  StorelocatorViewController.m
//  iOS_FBTemplate1
//
//  Created by HARSH on 22/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "StorelocatorViewController.h"
#import "StorelocatorTableViewCell.h"
#import "AFHTTPRequestOperationManager.h"
#import "OrderPageViewController.h"
#import "MenuViewController.h"
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>
#import "PlaceAnnotation.h"
#import <AddressBook/AddressBook.h>
#import "OrderCompleteViewController.h"
#import "ModelClass.h"
#import "ShowStoreViewController.h"
#import "SideMenuObject.h"
#import "MenuViewController.h"
#import "HomeViewController.h"
#import "UserProfileView.h"
#import "RewardsAndOfferView.h"
#import "LoyalityView.h"
#import "ApiClasses.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "clpsdk.h"
#import "FAQViewViewController.h"
#import "ContactUSView.h"


@interface StorelocatorViewController ()<UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate,MKMapViewDelegate,CLLocationManagerDelegate,PushNavigation>
{
       NSDictionary             * storesDict;
    __weak IBOutlet UITextField * searchOutlet;
    
    __weak IBOutlet UIImageView * imgBG;
    CLLocation                  * currentLocation;
    CLGeocoder                  * geocoder1;
    CLPlacemark                 * placemark1;
    PlaceAnnotation             * myAnnotation;
    NSMutableArray              * annotations;
    NSMutableArray              * arrayLatitude;
    NSMutableArray              * arrayLongitude;
    NSMutableArray              * arrayName;
    BOOL                          mapCliicked;
    NSString                    * strCity1;
    NSString                    * strAddress;
    NSString                    * strLong;
    NSString                    * strLat;
    NSString                    * strPhone;
    NSString                    * strStoreName;
    ModelClass                  * obj;
    SideMenuObject              * sideObject;
    UIViewController            * myController;
    ApiClasses                  * apiCall;
    NSArray                     * arrayHours1;
    BOOL                          isMapButton;
    clpsdk                      * clpObj;
   
}


@property (weak, nonatomic) IBOutlet UISegmentedControl * segmentListMap;
@property(nonatomic, strong) CLLocationManager          * locationManager;
@property (weak, nonatomic) IBOutlet UIButton           * btnStoreListOutlet;
@property (weak, nonatomic) IBOutlet UIButton           * btnStoreMapOutlet;
@property (weak, nonatomic) IBOutlet UISegmentedControl * segmentButton;

@property (weak, nonatomic) IBOutlet UIImageView        *locationView;



- (IBAction)searchStore:(id)sender;

@property (weak, nonatomic) IBOutlet UITableView        * storetableView;
@property (weak, nonatomic) IBOutlet UIView             * mapView;

@end

@implementation StorelocatorViewController

@synthesize mapViewMain;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    obj=[ModelClass sharedManager];
    sideObject = [[SideMenuObject alloc]init];
    
    _storetableView.hidden=NO;
     self.mapView.hidden=YES;
    
    isMapButton = NO;
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"mapClicked"];
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self->imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
 
    _locationManager = [[CLLocationManager alloc] init];
    _locationManager.delegate = self;
    _locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    _locationManager.distanceFilter = kCLDistanceFilterNone;
    
    if([_locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)])
    {
        [_locationManager requestWhenInUseAuthorization];
    }
    [_locationManager startUpdatingLocation];
    
   // searchOutlet.backgroundColor= [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    _btnStoreListOutlet.backgroundColor= [obj colorWithHexString:@"AF0013"];
    _btnStoreMapOutlet.backgroundColor= [obj colorWithHexString:@"DF0000"];
    
      [self arrayCategory];
    
    [searchOutlet addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];

    //[self searchStore:nil];
    
  
    
    mapViewMain.delegate = self;
    
    [self shadoOffect:self.locationView];
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    
}


// location manager delegate
- (void)locationManager:(CLLocationManager *)manager
     didUpdateLocations:(NSArray<CLLocation *> *)locations
{
    NSLog(@"didUpdateToLocation: %@", locations);
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    NSLog(@"didUpdateToLocation: %@", newLocation);
    CLLocation *currentLocation1 = newLocation;
    
    if (currentLocation1 != nil) {
        
        //        longitudeLabel.text = [NSString stringWithFormat:@"%.8f", currentLocation.coordinate.longitude];
        //        latitudeLabel.text = [NSString stringWithFormat:@"%.8f", currentLocation.coordinate.latitude];
    }
    
    // Stop Location Manager
    [_locationManager stopUpdatingLocation];
    
    NSLog(@"Resolving the Address");
    [geocoder1 reverseGeocodeLocation:currentLocation1 completionHandler:^(NSArray *placemarks, NSError *error) {
        NSLog(@"Found placemarks: %@, error: %@", placemarks, error);
        if (error == nil && [placemarks count] > 0) {
            placemark1 = [placemarks lastObject];
            
            //            addressLabel.text = [NSString stringWithFormat:@"%@ %@\n%@ %@\n%@\n%@",
            //                                 placemark.subThoroughfare, placemark.thoroughfare,
            //                                 placemark.postalCode, placemark.locality,
            //                                 placemark.administrativeArea,
            //                                 placemark.country];
            
            NSString *addressLabel = [NSString stringWithFormat:@"%@ %@\n%@ %@\n%@\n%@",
                                      placemark1.subThoroughfare, placemark1.thoroughfare,
                                      placemark1.postalCode, placemark1.locality,
                                      placemark1.administrativeArea,
                                      placemark1.country];
            NSLog(@"%@",addressLabel);
        } else {
            NSLog(@"%@", error.debugDescription);
        }
    } ];
    
}


#pragma mark -  SegmentedControl Method

- (IBAction)selectTab:(UISegmentedControl *)sender
{
    switch (sender.selectedSegmentIndex)
    {
        case 0:
            [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"mapClicked"];
            self.mapView.hidden=YES;
            _storetableView.hidden=NO;
            break;
        case 1:
            _storetableView.hidden=YES;
            [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"mapClicked"];
            mapViewMain.showsUserLocation = YES;
            self.mapView.hidden=NO;
            arrayName = [[NSMutableArray alloc] init];
            arrayLatitude = [[NSMutableArray alloc] init];
            arrayLongitude = [[NSMutableArray alloc] init];
            mapViewMain.mapType = MKMapTypeStandard;
            mapViewMain.showsUserLocation = YES;
            mapViewMain.scrollEnabled = YES;
            mapViewMain.zoomEnabled = YES;
            
            mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
            for (int i=0; i<[storesDict count]; i++)
            {
                [self callLatLangFRomAddress:i];
            }
            break;
        case 2:
            [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"mapClicked"];
            self.mapView.hidden=YES;
            _storetableView.hidden=NO;
            break;
        case 3:
            [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"mapClicked"];
            break;
        default:
            break;
    }
    
}

#pragma mark -  Map view Method


- (IBAction)mapType:(UISegmentedControl *)sender
{
    switch (sender.selectedSegmentIndex) {
        case 0:
            mapViewMain.mapType=MKMapTypeStandard;
            break;
        case 1:
            mapViewMain.mapType=MKMapTypeSatellite;
            break;
        case 2:
            mapViewMain.mapType=MKMapTypeHybrid;
            break;
        default:
            break;
    }
}
- (IBAction)backClicked:(id)sender
{
        [self.navigationController popViewControllerAnimated:YES];
    
}
-(void)viewWillAppear:(BOOL)animated

{
    [super viewWillAppear:YES];
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"mapClicked"];
    _btnStoreListOutlet.selected=YES;
       [obj removeLoadingView:self.view];
     mapViewMain.mapType=MKMapTypeStandard;
     searchOutlet.delegate=self;
   
    _storetableView.separatorStyle = UITableViewCellSeparatorStyleNone;
     self.navigationController.navigationBarHidden = YES;
    
//    _btnStoreListOutlet.backgroundColor= [obj colorWithHexString:@"AF0013"];
//    _btnStoreMapOutlet.backgroundColor= [obj colorWithHexString:@"DF0000"];
}


#pragma mark -  arrayCategory Method

-(void)arrayCategory
{
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"storesKey"];
    storesDict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
//    NSString * strState = [[NSUserDefaults standardUserDefaults]valueForKey:@"StateCode"];
//    NSString * strCountry = [[NSUserDefaults standardUserDefaults]valueForKey:@"CountryCode"];
//    
//    if(storesDict.count != 0 )
//    {
//        NSArray *arr = (NSArray *)storesDict;
//        
//        if(strState.length!=0 && strCountry.length!=0)
//        {
//           // arrCategory = [NSMutableArray new];
//            
//            NSArray * str1 = [arr filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(state == %@)",strState]];
//            
//            
//            storesDict = (NSDictionary *)str1;
//            
////            for(int i =0 ; i< [str1 count]; i++)
////            {
////               // [arrCategory addObject:[[str1 objectAtIndex:i]valueForKey:@"storeName"]];
////            }
//            
//            //[self.tblSearch reloadData];
//           // NSLog(@"store name according state code----- %@",arrCategory);
//            
//        }
//        else
//        {
//            storesDict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
//            //arrCategory  = [dictValue valueForKey:@"storeName"];
//        }
//    }
//    
    
    
    NSLog(@"storesDict:==%@",storesDict);
    
    mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
    if(mapCliicked==YES)
    {
        _btnStoreListOutlet.backgroundColor= [obj colorWithHexString:@"DF0000"];
        _btnStoreMapOutlet.backgroundColor= [obj colorWithHexString:@"AF0013"];
        _storetableView.hidden=YES;
        arrayName = [[NSMutableArray alloc] init];
        arrayLatitude = [[NSMutableArray alloc] init];
        arrayLongitude = [[NSMutableArray alloc] init];
        mapViewMain.mapType = MKMapTypeStandard;
        mapViewMain.showsUserLocation = YES;
        mapViewMain.scrollEnabled = YES;
        mapViewMain.zoomEnabled = YES;
        isMapButton = YES;
        _storetableView.hidden=YES;
        self.mapView.hidden=NO;
        mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
        
        for (int i=0; i<[storesDict count]; i++)
        {
            [self callLatLangFRomAddress:i];
        }
    }
    else
    {
        
        _btnStoreListOutlet.backgroundColor= [obj colorWithHexString:@"AF0013"];
        _btnStoreMapOutlet.backgroundColor= [obj colorWithHexString:@"DF0000"];
        
//        _btnStoreListOutlet.backgroundColor= [obj colorWithHexString:@"DF0000"];
//        _btnStoreMapOutlet.backgroundColor= [obj colorWithHexString:@"AF0013"];
        isMapButton = NO;
        [_storetableView reloadData];
        self.mapView.hidden=YES;
        _storetableView.hidden=NO;
    }
    
    // is strores available
    if(storesDict.count == 0)
    {
        [obj addLoadingView:self.view];
        apiCall=[ApiClasses sharedManager];
        [apiCall getAllStores:nil url:@"/mobile/stores/getstores" withTarget:self withSelector:@selector(storeApi:)];
        apiCall = nil;
    }
}

-(void)storeApi:(id)response
{
    [obj removeLoadingView:self.view];
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
        NSData *data = [NSKeyedArchiver archivedDataWithRootObject:[response valueForKey:@"stores"]];
        [currentDefaults setObject:data forKey:@"storesKey"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        NSUserDefaults *currentDefaults1 = [NSUserDefaults standardUserDefaults];
        NSData *data1 = [currentDefaults1 objectForKey:@"storesKey"];
        storesDict = [NSKeyedUnarchiver unarchiveObjectWithData:data1];
        
        NSLog(@"storesDict:==%@",storesDict);
        
        mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
        if(mapCliicked==YES)
        {
            _btnStoreListOutlet.backgroundColor= [obj colorWithHexString:@"DF0000"];
            _btnStoreMapOutlet.backgroundColor= [obj colorWithHexString:@"AF0013"];
            _storetableView.hidden=YES;
            arrayName = [[NSMutableArray alloc] init];
            arrayLatitude = [[NSMutableArray alloc] init];
            arrayLongitude = [[NSMutableArray alloc] init];
            mapViewMain.mapType = MKMapTypeStandard;
            mapViewMain.showsUserLocation = YES;
            mapViewMain.scrollEnabled = YES;
            mapViewMain.zoomEnabled = YES;
            isMapButton = YES;
            _storetableView.hidden=YES;
            self.mapView.hidden=NO;
            mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
            
            for (int i=0; i<[storesDict count]; i++)
            {
                [self callLatLangFRomAddress:i];
            }
        }
        else
        {
            _btnStoreListOutlet.backgroundColor= [obj colorWithHexString:@"DF0000"];
            _btnStoreMapOutlet.backgroundColor= [obj colorWithHexString:@"AF0013"];
            isMapButton = NO;
            [_storetableView reloadData];
            self.mapView.hidden=YES;
            _storetableView.hidden=NO;
        }
    }
    else
    {
        [self alert_Action:@"Due to slow internet connection Stores are not loaded"];
        
    }
    NSLog(@"response store api ------- %@", response);
}

// alert action
-(void)alert_Action:(NSString *)str
{
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:str
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                              }];
    
    [alert addAction:cancel1];
    [self presentViewController:alert animated:YES completion:nil];
}


#pragma mark -  CallStoresSearch Api

-(void)CallStoresSearch
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    [dict setValue:searchOutlet.text forKey:@"query"];
    [dict setValue:@"1000" forKey:@"radius"];
    [dict setValue:@"10" forKey:@"count"];
    
    [apiCall storeSearchApi:dict url:@"/store/searchStores" withTarget:self withSelector:@selector(storeSearchApi:)];
    dict = nil;
    apiCall = nil;
}


#pragma mark -  Store Search Method

-(void)storeSearchApi:(id)response
{
    [obj removeLoadingView:self.view];

    NSLog(@"store response -------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue]==1)
    {
        NSDictionary * dict = (NSDictionary *)response;
        
        storesDict = [dict valueForKey:@"storeList"];
        
        [_storetableView reloadData];
        
        if(isMapButton == YES)
        {
            NSLog(@"map clicked");
            
            _storetableView.hidden=YES;
            arrayName = [[NSMutableArray alloc] init];
            arrayLatitude = [[NSMutableArray alloc] init];
            arrayLongitude = [[NSMutableArray alloc] init];
            mapViewMain.mapType = MKMapTypeStandard;
            mapViewMain.showsUserLocation = YES;
            mapViewMain.scrollEnabled = YES;
            mapViewMain.zoomEnabled = YES;
            
            _storetableView.hidden=YES;
            self.mapView.hidden=NO;
            mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
            
            for( id annotation in mapViewMain.annotations)
            {
                [self.mapViewMain removeAnnotation:annotation];
            }
            
            // [self.mapViewMain removeAnnotations:[self.mapViewMain annotations]];
            
            for (int i=0; i<[storesDict count]; i++)
            {
                [self callLatLangFRomAddress:i];
            }
        }
        
        NSLog(@"store search api %@", storesDict);
    }
    else
    {
        [self alertViewDelegate:[response valueForKey:@"message"]];
    }

}


#pragma mark -  TextField Add Observer Method

-(void)textFieldDidChange :(UITextField *)theTextField
{
    if(theTextField.text.length == 0)
    {
        NSLog(@"string search value after method ------ %ld",(long)searchOutlet.text.length);
        [self arrayCategory];
        [searchOutlet resignFirstResponder];
    }
    NSLog( @"text changed: %@", theTextField.text);
    
}



-(void)callStoreApi
{
    [self arrayCategory];
    
    
//    NSString *strSearch=[NSString stringWithFormat:@"%@",searchOutlet.text];
//    NSString *strBaseUrl=@"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/store/";
//    
//    NSString *stringUrl=[NSString stringWithFormat:@"%@%@/104",strBaseUrl,strSearch];
//    NSLog(@" string url ====%@",stringUrl);
//    
//    NSString *encoded=[stringUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
//    
//    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
//    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
//    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
//    
//    manager.requestSerializer =requestSerializer;
//    
//    [manager GET:encoded parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject)
//     
//     {
//         // _storetableView.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
//          [obj removeLoadingView:self.view];
//         NSLog(@"JSON:==%@",responseObject);
//         storesDict=[responseObject valueForKey:@"categories"];
//         NSLog(@"storesDict:==%@",storesDict);
//         
//         mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
//         if(mapCliicked==YES)
//         {
//            // _nearBystore.hidden=YES;
//             _storetableView.hidden=YES;
//             arrayName = [[NSMutableArray alloc] init];
//             arrayLatitude = [[NSMutableArray alloc] init];
//             arrayLongitude = [[NSMutableArray alloc] init];
//             mapViewMain.mapType = MKMapTypeStandard;
//             mapViewMain.showsUserLocation = YES;
//             mapViewMain.scrollEnabled = YES;
//             mapViewMain.zoomEnabled = YES;
//             
//             _storetableView.hidden=YES;
//             self.mapView.hidden=NO;
//             mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
//             
//             for (int i=0; i<[storesDict count]; i++)
//             {
//                 [self callLatLangFRomAddress:i];
//             }
//         }
//         else
//         {
//             [_storetableView reloadData];
//             self.mapView.hidden=YES;
//             _storetableView.hidden=NO;
//         }
//     }
//         failure:^(AFHTTPRequestOperation *operation,NSError *error)
//     {
//         
//         UIAlertController * alert=   [UIAlertController
//                                       alertControllerWithTitle:@"Failure"
//                                       message:@"Request timeout"
//                                       preferredStyle:UIAlertControllerStyleAlert];
//         
//         UIAlertAction* yesButton = [UIAlertAction
//                                     actionWithTitle:@"OK"
//                                     style:UIAlertActionStyleDefault
//                                     handler:^(UIAlertAction * action)
//                                     {
//                                         //Handel your yes please button action here
//                                         [alert dismissViewControllerAnimated:YES completion:nil];
//                                     }];
//         
//         [alert addAction:yesButton];
//         
//         [self presentViewController:alert animated:YES completion:nil];
//     }];
    
}


-(void)callLatLangFRomAddress:(int)i
{
    CLGeocoder *geoCoder = [[CLGeocoder alloc] init];
    NSString *address =[NSString stringWithFormat:@"%@",[[storesDict valueForKey:@"address"]objectAtIndex:i]];
    
    //NSString *address =@"25522 Marguerite Parkway Mission Viejo, CA 92692";
    
    [geoCoder geocodeAddressString:address completionHandler:^(NSArray *placemarks, NSError *error)
     {
         if(error){
             NSLog(@"error");
         }
         if(placemarks && placemarks.count > 0){
             CLPlacemark *placemark = placemarks[0];
             CLLocation  *location = placemark.location;
             [arrayLatitude addObject:[NSString stringWithFormat:@"%f",location.coordinate.latitude]];
             [arrayLongitude addObject:[NSString stringWithFormat:@"%f",location.coordinate.longitude]];
             NSLog(@"lat:%f,lon:%f",location.coordinate.latitude,location.coordinate.longitude);
             
             [arrayName addObject:[NSString stringWithFormat:@"%@",[[storesDict valueForKey:@"mailingAddress1"]objectAtIndex:i]]];
             [self callMap];
         }
     }];
}

#pragma mark -  Map Call Method

-(void)callMap
{
    annotations = [[NSMutableArray alloc] init];
   
    for (int i=0; i<[arrayLatitude count]; i++)
    {
        CLLocationCoordinate2D theCoordinate1;
        
        theCoordinate1.latitude  = [[arrayLatitude objectAtIndex:i] floatValue];
        theCoordinate1.longitude = [[arrayLongitude objectAtIndex:i] floatValue];
  
        myAnnotation = [[PlaceAnnotation alloc] init];
        myAnnotation.coordinate = theCoordinate1;
        myAnnotation.title      = [arrayName objectAtIndex:i];
        
       dispatch_async(dispatch_get_main_queue(), ^{
            
            [mapViewMain addAnnotation:myAnnotation];
            [annotations addObject:myAnnotation];
            
        });
        mapViewMain.mapType=MKMapTypeStandard;
        mapViewMain.showsUserLocation = YES;
        mapViewMain.userInteractionEnabled = YES;
        
       // float lat = mapViewMain.userLocation.coordinate.latitude;
        
    }
    
    NSLog(@"%lu",(unsigned long)[annotations count]);
    
  
//    MKMapRect flyTo = MKMapRectNull;
//    for (id <MKAnnotation> annotation in annotations)
//    {
//        MKMapPoint annotationPoint = MKMapPointForCoordinate(annotation.coordinate);
//        MKMapRect pointRect = MKMapRectMake(annotationPoint.x, annotationPoint.y, 0, 0);
//        if (MKMapRectIsNull(flyTo))
//        {
//            flyTo = pointRect;
//            flyTo = MKMapRectUnion(flyTo, pointRect);
//        }
//        else
//        {
//            flyTo = MKMapRectUnion(flyTo, pointRect);
//        }
//    }
//     mapViewMain.visibleMapRect = flyTo;
    [self zoomToFitMapAnnotations:mapViewMain];

}


// mapview annotation zoom

- (void)zoomToFitMapAnnotations:(MKMapView *)mapView {
    if ([mapView.annotations count] == 0) return;
    
    CLLocationCoordinate2D topLeftCoord;
    topLeftCoord.latitude = -90;
    topLeftCoord.longitude = 180;
    
    CLLocationCoordinate2D bottomRightCoord;
    bottomRightCoord.latitude = 90;
    bottomRightCoord.longitude = -180;
    
    for(id<MKAnnotation> annotation in mapView.annotations) {
        topLeftCoord.longitude = fmin(topLeftCoord.longitude, annotation.coordinate.longitude);
        topLeftCoord.latitude = fmax(topLeftCoord.latitude, annotation.coordinate.latitude);
        bottomRightCoord.longitude = fmax(bottomRightCoord.longitude, annotation.coordinate.longitude);
        bottomRightCoord.latitude = fmin(bottomRightCoord.latitude, annotation.coordinate.latitude);
    }
    
    MKCoordinateRegion region;
    region.center.latitude = topLeftCoord.latitude - (topLeftCoord.latitude - bottomRightCoord.latitude) * 0.5;
    region.center.longitude = topLeftCoord.longitude + (bottomRightCoord.longitude - topLeftCoord.longitude) * 0.5;
    
    // Add a little extra space on the sides
    region.span.latitudeDelta = fabs(topLeftCoord.latitude - bottomRightCoord.latitude) * 1.1;
    region.span.longitudeDelta = fabs(bottomRightCoord.longitude - topLeftCoord.longitude) * 1.1;
    
    region = [mapView regionThatFits:region];
    [mapView setRegion:region animated:YES];
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
    
//    UIImageView *icon = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"location@3x.png"]];
//   pinView.leftCalloutAccessoryView = icon;
    
    UIButton* rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
    [rightButton setTitle:nil forState:UIControlStateNormal];
    [rightButton addTarget:self
                    action:@selector(myMethod:)
          forControlEvents:UIControlEventTouchUpInside];
    
    rightButton.tag = pinView.tag;
    
    pinView.rightCalloutAccessoryView = rightButton;
    
    return pinView;
}

-(IBAction)myMethod:(UIButton *)sender
{
        NSLog(@"clicked");
        NSLog(@"city==%@",strCity1);
        NSLog(@"Address==%@",strAddress);

    ShowStoreViewController * orderCompObj = [[ShowStoreViewController alloc]initWithNibName:@"ShowStoreViewController" bundle:nil];
     orderCompObj.arrayHours=arrayHours1;
    
    
    if([[storesDict valueForKey:@"storeHourList"]objectAtIndex:[sender tag]]!=(NSString *)[NSNull null])
    {
        arrayHours1=nil;
        arrayHours1 = [[storesDict valueForKey:@"storeHourList"]objectAtIndex:[sender tag]];
        NSLog(@"arrayHours==%@",arrayHours1);
        
    }
    else
    {
        arrayHours1=nil;
    }
    
    [self.navigationController pushViewController:orderCompObj animated:YES];
    
}

#pragma mark -  Map View

- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view
{
    
    [[NSUserDefaults standardUserDefaults]setObject:[NSString stringWithFormat:@"%@",[storesDict valueForKey:@"storeID"]] forKey:@"storeID"];
    
    PlaceAnnotation *annot = view.annotation;
    NSInteger indexOfTheObject = [mapView.annotations indexOfObject:annot];
    
    NSLog(@"%ld",(long)indexOfTheObject);
    NSLog(@"Title     : %@",view.annotation.title);
    strAddress=view.annotation.title;
    
    
//    if([[storesDict valueForKey:@"storeHourList"]objectAtIndex:indexOfTheObject]!=(NSString *)[NSNull null])
//    {
//        arrayHours1=nil;
//        arrayHours1 = [[storesDict valueForKey:@"storeHourList"]objectAtIndex:indexOfTheObject];
//        NSLog(@"arrayHours==%@",arrayHours1);
//        
//    }
//    else
//    {
//        arrayHours1=nil;
//    }
    
    
    
    NSLog(@"Latitude  : %f", view.annotation.coordinate.latitude);
    NSLog(@"Longitude : %f", view.annotation.coordinate.longitude);
    
    
    NSLog(@"storeName : %@", [[storesDict valueForKey:@"storeName"]objectAtIndex:indexOfTheObject]);
    
    
    NSUserDefaults *userDefaults=[NSUserDefaults standardUserDefaults];
    
    if([[storesDict valueForKey:@"city"]objectAtIndex:indexOfTheObject]!=(NSString *)[NSNull null])
    {
    [userDefaults setValue:[[storesDict valueForKey:@"city"]objectAtIndex:indexOfTheObject] forKey:@"city"];
    }
    
    [userDefaults setValue:strAddress forKey:@"address"];
    
    
    [userDefaults setValue:[NSString stringWithFormat:@"%f",view.annotation.coordinate.latitude] forKey:@"latitude"];
    
    [userDefaults setValue:[NSString stringWithFormat:@"%f",view.annotation.coordinate.longitude] forKey:@"longitude"];
    
    
    if([[storesDict valueForKey:@"phone"]objectAtIndex:indexOfTheObject]!=(NSString *)[NSNull null])
    {
      [userDefaults setValue:[[storesDict valueForKey:@"phone"]objectAtIndex:indexOfTheObject] forKey:@"phone"];
    }
  
    if([[storesDict valueForKey:@"storeName"]objectAtIndex:indexOfTheObject]!=(NSString *)[NSNull null])
    {
      [userDefaults setValue:[[storesDict valueForKey:@"storeName"]objectAtIndex:indexOfTheObject] forKey:@"storeName"];
    }
    
    
    if([[storesDict valueForKey:@"storeNumber"]objectAtIndex:indexOfTheObject]!=(NSString *)[NSNull null])
    {
        [userDefaults setValue:[[storesDict valueForKey:@"storeNumber"]objectAtIndex:indexOfTheObject] forKey:@"storeNumber"];
    }
    
 

//        if([[storesDict valueForKey:@"storeId"]objectAtIndex:indexOfTheObject]!=(NSString *)[NSNull null])
//        {
//            [userDefaults setValue:[[storesDict valueForKey:@"storeId"]objectAtIndex:indexOfTheObject] forKey:@"storeID"];
//        }
//        

//    }
   
   
        if([[storesDict valueForKey:@"storeID"]objectAtIndex:indexOfTheObject]!=(NSString *)[NSNull null])
        {
            [userDefaults setValue:[[storesDict valueForKey:@"storeID"]objectAtIndex:indexOfTheObject] forKey:@"storeID"];
        }
   

    [userDefaults synchronize];
   
}


#pragma mark -  Table View Data Source


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
  //  NSLog(@"%lu",(unsigned long)[storesDict count]);
    return [storesDict count];
    //return 10;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    StorelocatorTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"StorelocatorTableViewCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"StorelocatorTableViewCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }

    
    cell.backgroundColor=[UIColor clearColor];
    cell.contentView.layer.borderColor=[UIColor lightGrayColor].CGColor;
    cell.layer.shouldRasterize = YES;
    cell.layer.rasterizationScale = [UIScreen mainScreen].scale;
  
   
    dispatch_queue_t backgroundQueue = dispatch_queue_create("dispatch_queue_#1", 0);
    dispatch_async(backgroundQueue, ^{
        
        dispatch_async(dispatch_get_main_queue(), ^{
            
            if([[storesDict valueForKey:@"storeName"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
            {
               // NSString * cityName = [[storesDict valueForKey:@"city"]objectAtIndex:indexPath.row];
               // NSString * cityStore = [NSString stringWithFormat:@"%@, %@",[[storesDict valueForKey:@"storeName"]objectAtIndex:indexPath.row],cityName];
                cell.city.text=[[storesDict valueForKey:@"storeName"]objectAtIndex:indexPath.row];
            }
            if([[storesDict valueForKey:@"mailingAddress1"]objectAtIndex:indexPath.row] != (NSString *)[NSNull null])
            {
                cell.address.text=[[storesDict valueForKey:@"mailingAddress1"]objectAtIndex:indexPath.row];
                cell.address.adjustsFontSizeToFitWidth=YES;
            }
            
            if([[storesDict valueForKey:@"zip"]objectAtIndex:indexPath.row] != (NSString *)[NSNull null])
            {
                cell.zipcode.text=[[storesDict valueForKey:@"zip"]objectAtIndex:indexPath.row];
            }
            
            if([[storesDict valueForKey:@"phone"]objectAtIndex:indexPath.row] != (NSString *)[NSNull null])
            {
                cell.phone.text=[[storesDict valueForKey:@"phone"]objectAtIndex:indexPath.row];
            }
            
            if([[storesDict valueForKey:@"address"]objectAtIndex:indexPath.row] != (NSString *)[NSNull null])
            {
                cell.state.text=[[storesDict valueForKey:@"address"]objectAtIndex:indexPath.row];
            }
            
            if([[storesDict valueForKey:@"latitude"]objectAtIndex:indexPath.row] != (NSString *)[NSNull null] && [[storesDict valueForKey:@"longitude"]objectAtIndex:indexPath.row] != (NSString *)[NSNull null])
                
            {
                CLLocation *location1 = [[CLLocation alloc] initWithLatitude: 37.3462302 longitude:-121.9417057];
                CLLocation *location2 = [[CLLocation alloc] initWithLatitude:[[[storesDict valueForKey:@"latitude"]objectAtIndex:indexPath.row] floatValue] longitude:[[[storesDict valueForKey:@"longitude"]objectAtIndex:indexPath.row] floatValue]];
                
                float miles = [location1 distanceFromLocation:location2]/1000 * 0.6213;
                cell.distanceLbl.text = [NSString stringWithFormat:@"%.0fmi",miles];
            }
            
            [cell bringSubviewToFront:cell.directionBtn];
            [cell.directionBtn addTarget:self action:@selector(getDirection:) forControlEvents:UIControlEventTouchUpInside];
            
            cell.directionBtn.tag = indexPath.row;
            
        });
    });

    return cell;
}


#pragma mark -  getDirection Button

-(void)getDirection:(UIButton *)sender
{
    
    NSLog(@"direction button action --------- ");
    
   if([[storesDict valueForKey:@"latitude"]objectAtIndex:[sender tag]] != (NSString *)[NSNull null] && [[storesDict valueForKey:@"longitude"]objectAtIndex:[sender tag]] != (NSString *)[NSNull null])
   {
       NSString * strLat1 = [[storesDict valueForKey:@"latitude"]objectAtIndex:[sender tag]];
       NSString * strLong1 = [[storesDict valueForKey:@"longitude"]objectAtIndex:[sender tag]];
       
       NSString* url = [NSString stringWithFormat:@"http://maps.apple.com/?saddr=%f,%f&daddr=%f,%f",37.3462302, -121.9417057, strLat1.floatValue,strLong1.floatValue];
       [[UIApplication sharedApplication] openURL: [NSURL URLWithString: url]];
   }
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


#pragma mark -  Table View Delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
 
     ShowStoreViewController * orderCompObj = [[ShowStoreViewController alloc]initWithNibName:@"ShowStoreViewController" bundle:nil];
   

        NSUserDefaults *userDefaults=[NSUserDefaults standardUserDefaults];
    
    if([[storesDict valueForKey:@"city"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    {
      [userDefaults setValue:[[storesDict valueForKey:@"city"]objectAtIndex:indexPath.row] forKey:@"city"];
    }
    
    if([[storesDict valueForKey:@"mailingAddress1"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    {
       [userDefaults setValue:[[storesDict valueForKey:@"mailingAddress1"]objectAtIndex:indexPath.row] forKey:@"address"];
    }
    
    if([[storesDict valueForKey:@"phone"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    {
          [userDefaults setValue:[[storesDict valueForKey:@"phone"]objectAtIndex:indexPath.row] forKey:@"phone"];
    }
    
    if([[storesDict valueForKey:@"storeName"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    {
       [userDefaults setValue:[[storesDict valueForKey:@"storeName"]objectAtIndex:indexPath.row] forKey:@"storeName"];
    }
    
    if([[storesDict valueForKey:@"latitude"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    {
        [userDefaults setValue:[[storesDict valueForKey:@"latitude"]objectAtIndex:indexPath.row] forKey:@"latitude"];
    }
    if([[storesDict valueForKey:@"longitude"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    {
        [userDefaults setValue:[[storesDict valueForKey:@"longitude"]objectAtIndex:indexPath.row] forKey:@"longitude"];
    }
    if([[storesDict valueForKey:@"storeNumber"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    {
         [userDefaults setValue:[[storesDict valueForKey:@"storeNumber"]objectAtIndex:indexPath.row] forKey:@"storeNumber"];
    }
    
        if([[storesDict valueForKey:@"storeId"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
        {
            [userDefaults setValue:[[storesDict valueForKey:@"storeId"]objectAtIndex:indexPath.row] forKey:@"SearchstoreID"];
        }
        
        if([[storesDict valueForKey:@"address"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
        {
            [userDefaults setValue:[[storesDict valueForKey:@"address"]objectAtIndex:indexPath.row] forKey:@"address"];
        }
  
         NSLog(@"store id is 12------------- %@",[[storesDict valueForKey:@"storeId"]objectAtIndex:indexPath.row]);
    
 
    if([[storesDict valueForKey:@"storeID"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
    {
        [userDefaults setValue:[[storesDict valueForKey:@"storeID"]objectAtIndex:indexPath.row] forKey:@"SearchstoreID"];
        
        NSLog(@"store id is 11------------- %@",[[storesDict valueForKey:@"storeID"]objectAtIndex:indexPath.row]);
    }
  
  
//    if([[storesDict valueForKey:@"storeHourList"]objectAtIndex:indexPath.row]!=(NSString *)[NSNull null])
//    {
//        arrayHours1=nil;
//        arrayHours1 = [[storesDict valueForKey:@"storeHourList"]objectAtIndex:indexPath.row];
//        NSLog(@"arrayHours==%@",arrayHours1);
//        orderCompObj.arrayHours=arrayHours1;
//    }
//    else
//    {
//        arrayHours1=nil;
//    }

        [userDefaults synchronize];
       [self.navigationController pushViewController:orderCompObj animated:YES];
}


#pragma mark -  Memory Warning

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



#pragma mark -  Search Stores  Methods

- (IBAction)searchStore:(id)sender
{
    // [obj addLoadingView:self.view];
   // [self callStoreApi];
    
    if(isMapButton == YES)
    {
    [self.mapViewMain setCenterCoordinate:self.mapViewMain.userLocation.coordinate animated:YES];
    [self.view endEditing:YES];
    }
   else
   {
    if(searchOutlet.text.length!=0)
    {
        [self CallStoresSearch];
        [self.view endEditing:YES];
    }
    else
    {
         [self alertViewDelegate:@"Enter valid text"];
    }
   }
   
}

#pragma mark -  textField Delegate Methods

- (void)textFieldDidEndEditing:(UITextField *)quantityText
{
   
    //[self callStoreApi];
   // [self CallStoresSearch];
    [searchOutlet resignFirstResponder];
}



- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    if(textField.text.length!=0)
    {
        [self CallStoresSearch];
    }
    return YES;
}


#pragma mark -  Tap Store List

- (IBAction)tapStoreList:(UIButton *)sender
{
    isMapButton = NO;
    
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"mapClicked"];
     self.mapView.hidden=YES;
    _storetableView.hidden=NO;
    _btnStoreListOutlet.backgroundColor= [obj colorWithHexString:@"AF0013"];
    _btnStoreMapOutlet.backgroundColor= [obj colorWithHexString:@"DF0000"];
    
    [self arrayCategory];
}


#pragma mark -  Tap MapStore List

- (IBAction)tapStoreMap:(UIButton *)sender
{
     isMapButton = YES;
    
    _btnStoreMapOutlet.backgroundColor= [obj colorWithHexString:@"AF0013"];
    _btnStoreListOutlet.backgroundColor= [obj colorWithHexString:@"DF0000"];
    _storetableView.hidden=YES;
    [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"mapClicked"];
    mapViewMain.showsUserLocation = YES;
    self.mapView.hidden=NO;
    arrayName = [[NSMutableArray alloc] init];
    arrayLatitude = [[NSMutableArray alloc] init];
    arrayLongitude = [[NSMutableArray alloc] init];
    mapViewMain.mapType = MKMapTypeStandard;
    mapViewMain.showsUserLocation = YES;
    mapViewMain.scrollEnabled = YES;
    mapViewMain.zoomEnabled = YES;
    
    mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
    
    NSLog(@"store dict count ------- %lu",(unsigned long)storesDict.count);
    
    for (int i=0; i<[storesDict count]; i++)
    {
        [self callLatLangFRomAddress:i];
    }
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
       [sideObject removeSideNave];
        [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"MenuToStore"];
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


# pragma mark - alert method
-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
}


#pragma mark -  check controller exist

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


#pragma mark -  LogOut Button

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





#pragma mark - Search Button Action

- (IBAction)searchButton_Action:(id)sender
{
    [self CallStoresSearch];
}

@end
