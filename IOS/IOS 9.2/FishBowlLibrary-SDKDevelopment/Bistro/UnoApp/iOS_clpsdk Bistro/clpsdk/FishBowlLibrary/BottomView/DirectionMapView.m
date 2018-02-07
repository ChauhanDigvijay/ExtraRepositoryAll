//
//  DirectionMapView.m
//  VibesGuide
//
//  Created by Gaurav Shukla on 6/17/15.
//  Copyright (c) 2015 softProdigy. All rights reserved.
//

#import "DirectionMapView.h"
#import <CoreLocation/CoreLocation.h>
#import "MBProgressHUD.h"
#import "ModelClass.h"



@interface DirectionMapView ()<UIWebViewDelegate,CLLocationManagerDelegate>
{
    CLLocationManager  *locationManager;
    UIView *_blockerView;
    UIActivityIndicatorView	*spinner1;
    UIView *subv;
     ModelClass       * obj;
}

@property (weak, nonatomic) IBOutlet UIWebView *aWebView;
@property (strong, nonatomic) UIActivityIndicatorView *spinner1;
@property (unsafe_unretained, nonatomic) IBOutlet UIView *directionView;


@end

@implementation DirectionMapView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        CGSize result = [[UIScreen mainScreen] bounds].size;
        if(result.height == 480)
        {
            _blockerView = [[UIView alloc] initWithFrame: CGRectMake(0,0, 320, 480)];
            subv=[[UIView alloc]initWithFrame:CGRectMake(110, 180, 100, 90)];
            
            _blockerView.backgroundColor = [UIColor colorWithWhite: 0.0 alpha: 0.5];
            _blockerView.alpha = 1.0;
            [self.aWebView addSubview: _blockerView];
            subv.backgroundColor= [UIColor colorWithWhite: 0.0 alpha: 0.8];
            subv.alpha=1.0;
            if ([subv.layer respondsToSelector: @selector(setCornerRadius:)]) [(id) subv.layer setCornerRadius: 10];
            UILabel *label = [[UILabel alloc] init ];
            label.frame = CGRectMake(0,60, subv.bounds.size.width, 15);
            NSString*   str1=[[NSString alloc]initWithFormat:@"Loading..."];
            label.text = NSLocalizedString(str1, nil);
            label.backgroundColor = [UIColor clearColor];
            label.textColor = [UIColor whiteColor];
            label.textAlignment =NSTextAlignmentCenter;
            label.font = [UIFont boldSystemFontOfSize: 15];
            [subv addSubview: label];
            [_blockerView addSubview:subv];
            spinner1 = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle: UIActivityIndicatorViewStyleWhite];
            spinner1.center = CGPointMake(subv.bounds.size.width / 2, subv.bounds.size.height / 2);
            [subv addSubview: spinner1];
            [spinner1 startAnimating];
            
        }
        else
        {
            [self addLoadingView];
        }
    }
     //obj=[ModelClass sharedManager];
    //[obj RemoveBottomView];
    
    [self shadoOffect:self.directionView];
    [self drawDirection];
  
}


-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    //    shadoView.layer.shadowColor = [[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:.3] CGColor];
}


-(CLLocationCoordinate2D) getLocation
{
    locationManager = [[CLLocationManager alloc] init];
    locationManager.delegate = self;
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    locationManager.distanceFilter = kCLDistanceFilterNone;
    [locationManager startUpdatingLocation];
    CLLocation *location = [locationManager location];
    CLLocationCoordinate2D coordinate = [location coordinate];
    
    return coordinate;
}


- (void)drawDirection
{
    
   // CLLocation *location1 = [[CLLocation alloc] initWithLatitude: 37.3462302 longitude:-121.9417057];
    
//    CLLocationCoordinate2D coordinate = [self getLocation];
//    NSString * latitude = [NSString stringWithFormat:@"%f", coordinate.latitude];
//    NSString *  longitude = [NSString stringWithFormat:@"%f", coordinate.longitude];
    
    
        NSString * latitude = [NSString stringWithFormat:@"%f", 37.3462302];
        NSString *  longitude = [NSString stringWithFormat:@"%f",-121.9417057];
    
    NSString *startAddress = [NSString stringWithFormat:@"%@,%@",latitude,longitude];
    NSString *destiAddress = [NSString stringWithFormat:@"%@,%@",self.latitudeDirection,self.longitudeDirection];
    
    NSString *urlString = [NSString stringWithFormat:@"http://maps.google.com/?saddr=%@&daddr=%@",startAddress,destiAddress];
    
    NSURL * url = [NSURL URLWithString:urlString];
    [self.aWebView loadRequest:[NSURLRequest requestWithURL:url]];
}

#pragma mark - Webview delegate

-(void)webViewDidStartLoad:(UIWebView *)webView
{
    
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        CGSize result = [[UIScreen mainScreen] bounds].size;
        if(result.height == 480)
        {
            [spinner1 startAnimating];
        }
    }
    
    NSLog(@"start");
}

-(void)webViewDidFinishLoad:(UIWebView *)webView
{
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        CGSize result = [[UIScreen mainScreen] bounds].size;
        if(result.height == 480)
        {
            [spinner1 stopAnimating];
            [spinner1 setHidesWhenStopped:YES];
            [_blockerView setAlpha:0.0];
            
            [subv setAlpha:0.0];
        }
        else
        {
            [self removeLoadingView];
        }
    }
    
    NSLog(@"finish");
}

-(void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
    
    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:nil message:@"Please check url" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    [alert show];
    
    NSLog(@"Error for WEBVIEW: %@",[error description]);
    [self removeLoadingView];
    
}


#pragma mark - MBProgressHUD method

- (void)addLoadingView
{
    [MBProgressHUD showHUDAddedTo:self.aWebView animated:TRUE];
    NSLog(@"addLoadingView");
    
}

- (void)removeLoadingView {
    NSLog(@"removeLoadingView");
    [MBProgressHUD hideHUDForView:self.aWebView animated:TRUE];
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
   
}

- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}


@end
