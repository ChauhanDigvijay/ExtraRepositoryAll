//
//  Termsconditions.m
//  Raley's
//
//  Created by trg02 on 6/9/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "Termsconditions.h"

@interface Termsconditions ()

@end

@implementation Termsconditions

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    
    
    HeaderView *headerView;
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){  // IOS 7 and above
        headerView = [[HeaderView alloc] initWithFrame:CGRectMake(0.0, 20.0, self.view.frame.size.width, 35.0)];
    }
    else{
        headerView = [[HeaderView alloc] initWithFrame:CGRectMake(0.0, 0.0, self.view.frame.size.width, 35.0)];
    }
    
    [headerView setDelegate:self];
    
    [self.view addSubview:headerView];
    
    [background_view setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]];
    
    [details setTextAlignment:NSTextAlignmentJustified];
    
    [radius_view.layer setCornerRadius:5.0f];
    [radius_view.layer setShadowColor:[UIColor darkGrayColor].CGColor];
    [radius_view.layer setShadowOpacity:0.6];
    [radius_view.layer setShadowRadius:0.3];
    [radius_view.layer setShadowOffset:CGSizeMake(0.0,0.6)];
    
    
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

-(IBAction)backButtonClicked
{
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [self.presentingViewController dismissViewControllerAnimated:NO completion:nil];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
