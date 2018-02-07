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
@interface StorelocatorViewController ()<UITableViewDelegate,UITableViewDataSource,UITextFieldDelegate,MKMapViewDelegate,CLLocationManagerDelegate>
{
    NSDictionary *storesDict;
    __weak IBOutlet UITextField *searchOutlet;
    
    CLLocation *currentLocation;
    
    CLGeocoder *geocoder1;
    CLPlacemark *placemark1;
    
    PlaceAnnotation* myAnnotation;
    NSMutableArray *annotations;
    NSMutableArray *arrayLatitude;
    NSMutableArray *arrayLongitude;
    NSMutableArray *arrayName;
    BOOL mapCliicked;
    NSString *strCity1;
    NSString *strAddress;
    ModelClass *obj;
    
    
}
@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentListMap;
@property(nonatomic, strong) CLLocationManager *locationManager;
@property (weak, nonatomic) IBOutlet UIImageView *loadingimageView;
@property (weak, nonatomic) IBOutlet UIView *storeObtionView;


@property (weak, nonatomic) IBOutlet UIView *nearBystore;


- (IBAction)searchStore:(id)sender;
@property (weak, nonatomic) IBOutlet UITableView *storetableView;
@property (weak, nonatomic) IBOutlet UIView *mapView;

@end

@implementation StorelocatorViewController

@synthesize mapViewMain;

- (void)viewDidLoad {
    [super viewDidLoad];
    obj=[ModelClass sharedManager];
    
    _locationManager = [[CLLocationManager alloc] init];
    _locationManager.delegate = self;
    _locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    _locationManager.distanceFilter = kCLDistanceFilterNone;
    
    if([_locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)])
    {
        [_locationManager requestWhenInUseAuthorization];
    }
    
    [_locationManager startUpdatingLocation];
    
    [self searchStore:nil];
}

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
- (IBAction)selectTab:(UISegmentedControl *)sender
{
    switch (sender.selectedSegmentIndex)
    {
        case 0:
            [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"mapClicked"];
            self.mapView.hidden=YES;
            _nearBystore.hidden=NO;
            _storetableView.hidden=NO;
            break;
        case 1:
            _nearBystore.hidden=YES;
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
            _nearBystore.hidden=NO;
            _storetableView.hidden=NO;
            
            break;
        case 3:
            [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"mapClicked"];
            break;
            
        default:
            break;
    }
    
    
}
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
    _segmentListMap.selectedSegmentIndex = 0;
  
    [_segmentListMap setTitleTextAttributes:@{
                                                              NSForegroundColorAttributeName : [UIColor grayColor]
                                                              } forState:UIControlStateNormal];
   
     [obj removeLoadingView:self.view];
    mapViewMain.mapType=MKMapTypeStandard;
    _storetableView.hidden=NO;
   
    searchOutlet.delegate=self;
    self.mapView.hidden=YES;
       _storetableView.separatorStyle = UITableViewCellSeparatorStyleNone;
  self.navigationController.navigationBarHidden = YES;
}
-(void)callStoreApi
{
 
    NSString *strSearch=[NSString stringWithFormat:@"%@",searchOutlet.text];
    NSString *strBaseUrl=@"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/store/";
    
    NSString *stringUrl=[NSString stringWithFormat:@"%@%@/103",strBaseUrl,strSearch];
    NSLog(@" string url ====%@",stringUrl);
    NSString *encoded=[stringUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    manager.requestSerializer =requestSerializer;
    
    [manager GET:encoded parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject)
     
     
     {
         // _storetableView.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
          [obj removeLoadingView:self.view];
         NSLog(@"JSON:==%@",responseObject);
         storesDict=[responseObject valueForKey:@"categories"];
         NSLog(@"storesDict:==%@",storesDict);
         
         mapCliicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"mapClicked"]boolValue];
         if(mapCliicked==YES)
         {
             _nearBystore.hidden=YES;
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
             for (int i=0; i<[storesDict count]; i++)
             {
                 [self callLatLangFRomAddress:i];
                 
                 
             }
         }
         else
         {
             [_storetableView reloadData];
             self.mapView.hidden=YES;
             _storetableView.hidden=NO;
         }
     
     }
         failure:^(AFHTTPRequestOperation *operation,NSError *error)
     {
         
         UIAlertController * alert=   [UIAlertController
                                       alertControllerWithTitle:@"Failure"
                                       message:@"Request timeout"
                                       preferredStyle:UIAlertControllerStyleAlert];
         UIAlertAction* yesButton = [UIAlertAction
                                     actionWithTitle:@"OK"
                                     style:UIAlertActionStyleDefault
                                     handler:^(UIAlertAction * action)
                                     {
                                         //Handel your yes please button action here
                                         [alert dismissViewControllerAnimated:YES completion:nil];
                                         
                                     }];
         [alert addAction:yesButton];
         
         [self presentViewController:alert animated:YES completion:nil];
     }];
    
    
    
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
             CLLocation *location =placemark.location;
             [arrayLatitude addObject:[NSString stringWithFormat:@"%f",location.coordinate.latitude]];
             [arrayLongitude addObject:[NSString stringWithFormat:@"%f",location.coordinate.longitude]];
             NSLog(@"lat:%f,lon:%f",location.coordinate.latitude,location.coordinate.longitude);
             
             [arrayName addObject:[NSString stringWithFormat:@"%@",[[storesDict valueForKey:@"address"]objectAtIndex:i]]];
             [self callMap];
         }
     }];
    
}

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
        [mapViewMain addAnnotation:myAnnotation];
        [annotations addObject:myAnnotation];
        mapViewMain.delegate = self;
        mapViewMain.mapType=MKMapTypeStandard;
        mapViewMain.showsUserLocation = YES;
        mapViewMain.userInteractionEnabled = YES;
    }
    
    
    NSLog(@"%lu",(unsigned long)[annotations count]);
    
    
    MKMapRect flyTo = MKMapRectNull;
    for (id <MKAnnotation> annotation in annotations)
    {
        MKMapPoint annotationPoint = MKMapPointForCoordinate(annotation.coordinate);
        MKMapRect pointRect = MKMapRectMake(annotationPoint.x, annotationPoint.y, 0, 0);
        if (MKMapRectIsNull(flyTo))
        {
            flyTo = pointRect;
        }
        else
        {
            flyTo = MKMapRectUnion(flyTo, pointRect);
        }
    }
    mapViewMain.visibleMapRect = flyTo;
    
    
    
}
- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
    if ([annotation isKindOfClass:[MKUserLocation class]])
        return nil;
    
    static NSString* AnnotationIdentifier = @"AnnotationIdentifier";
    MKPinAnnotationView* pinView = [[MKPinAnnotationView alloc]
                                    initWithAnnotation:annotation reuseIdentifier:AnnotationIdentifier];
    pinView.animatesDrop   = NO;
    pinView.canShowCallout = YES;
    pinView.pinColor = MKPinAnnotationColorRed;
    
    UIImageView *icon = [[UIImageView alloc] initWithImage:[UIImage imageNamed:[NSString stringWithFormat:@"%i.jpg",rand()%3908+1]]];
    pinView.leftCalloutAccessoryView = icon;
    
    
    UIButton* rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
    [rightButton setTitle:nil forState:UIControlStateNormal];
    [rightButton addTarget:self
                    action:@selector(myMethod:)
          forControlEvents:UIControlEventTouchUpInside];
    pinView.rightCalloutAccessoryView = rightButton;
    
    return pinView;
}
-(IBAction)myMethod:(id)sender
{
        NSLog(@"clicked");
        NSLog(@"city==%@",strCity1);
        NSLog(@"Address==%@",strAddress);
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    OrderCompleteViewController *shoMenu=(OrderCompleteViewController*)[storyBoard instantiateViewControllerWithIdentifier:@"OrderCompleteViewController"];
    shoMenu.strSelectCity=strCity1;
    shoMenu.strSelectAddress=strAddress;
      [self.navigationController pushViewController:shoMenu animated:YES];
}


- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view
{
    
    [[NSUserDefaults standardUserDefaults]setObject:[NSString stringWithFormat:@"%@",[storesDict valueForKey:@"storeID"]] forKey:@"storeID"];
    
    NSLog(@"Title     : %@",view.annotation.title);
    strAddress=view.annotation.title;
    NSString *code = [strAddress substringFromIndex: [strAddress length] - 5];
    NSLog(@"code==%@",code);
    for (int i=0; i<[storesDict count]; i++)
    {
        if([code isEqualToString:[[storesDict valueForKey:@"zipcode"]objectAtIndex:i]])
        {
            strCity1=[[storesDict valueForKey:@"city"]objectAtIndex:i];
            break;
        }
    }
    NSLog(@"Latitude  : %f", view.annotation.coordinate.latitude);
    NSLog(@"Longitude : %f", view.annotation.coordinate.longitude);
   
    
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSLog(@"%lu",(unsigned long)[storesDict count]);
    return [storesDict count];
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    StorelocatorTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"StorelocatorTableViewCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"StorelocatorTableViewCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
//    if(indexPath.row%2 == 0)
//    {
    
        cell.backgroundColor=[UIColor clearColor];
//    }
//    else
//    {
//        cell.backgroundColor=[UIColor clearColor];
//        cell.backgroundColor = [UIColor colorWithRed:0.969 green:0.941 blue:0.902 alpha:1]; /*#f7f0e6*/
//        
//        
//    }
    //    cell.contentView.layer.borderWidth=.5f;
    //    cell.contentView.layer.borderColor=[UIColor lightGrayColor].CGColor;
    cell.city.text=[[storesDict valueForKey:@"city"]objectAtIndex:indexPath.row];
    cell.address.text=[[storesDict valueForKey:@"address"]objectAtIndex:indexPath.row];
    cell.address.adjustsFontSizeToFitWidth=YES;
    cell.zipcode.text=[[storesDict valueForKey:@"zipcode"]objectAtIndex:indexPath.row];
    cell.phone.text=[[storesDict valueForKey:@"phone"]objectAtIndex:indexPath.row];
    cell.state.text=[[storesDict valueForKey:@"state"]objectAtIndex:indexPath.row];
    
    return cell;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
   
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    OrderCompleteViewController *shoMenu=(OrderCompleteViewController*)[storyBoard instantiateViewControllerWithIdentifier:@"OrderCompleteViewController"];
    shoMenu.strSelectCity=[[storesDict valueForKey:@"city"]objectAtIndex:indexPath.row];
    shoMenu.strSelectAddress=[[storesDict valueForKey:@"address"]objectAtIndex:indexPath.row];;
    [self.navigationController pushViewController:shoMenu animated:YES];
 
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

- (IBAction)searchStore:(id)sender
{
    [obj addLoadingView:self.view];
    [self callStoreApi];
    
}
- (void)textFieldDidEndEditing:(UITextField *)quantityText
{
 [obj addLoadingView:self.view];
    [self callStoreApi];
    [searchOutlet resignFirstResponder];
    
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    return YES;
}


@end
