//
//  HeaderView.m
//  Raley's
//
//  Created by Samar Gupta on 5/21/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "HeaderView.h"
#import "AppDelegate.h"

@implementation HeaderView
@synthesize delegate;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        
        [self addHeader];
    }
    return self;
}

-(id)init{
    self = [super init];
    if(self){
        [self addHeader];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)decoder{
    self = [super init];
    if(self){
        
    }
    return self;
}

-(void)addHeader
{
//    int yAxis;
//    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
//        yAxis = 20;
//    else
//        yAxis = 0;
    
    // screen header
    UIImageView *headerImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 320, 44.0)];
    [headerImageView setImage:[UIImage imageNamed:@"header"]];
    [self addSubview:headerImageView];
    
    // back button
    UIButton *backButton = [UIButton buttonWithType:UIButtonTypeCustom];
//    [backButton setFrame:CGRectMake(8.0, 2.0, 18.0, 31.0)];
//    [backButton setBackgroundImage:[UIImage imageNamed:@"back_button_white"] forState:UIControlStateNormal];
    [backButton setFrame:CGRectMake(0, 2, 70.0, 40.0)];
    [backButton setImage:[UIImage imageNamed:@"back_button_white_small"] forState:UIControlStateNormal];
    [backButton setUserInteractionEnabled:YES];
    backButton.contentEdgeInsets = UIEdgeInsetsMake(5, 5, 5, 35);
//    backButton.contentEdgeInsets = UIEdgeInsetsMake(2.5, 0.0, 0.0, 0.0);
    [backButton addTarget:self action:@selector(headerbackButtonPressed) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:backButton];
}

-(void)headerbackButtonPressed
{
    if ([delegate respondsToSelector:@selector(backButtonClicked)])
    {
        [delegate backButtonClicked];
    }
}

@end
