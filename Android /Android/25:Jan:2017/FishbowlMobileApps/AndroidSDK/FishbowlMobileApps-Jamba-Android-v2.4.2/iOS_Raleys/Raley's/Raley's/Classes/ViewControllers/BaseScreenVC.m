//
//  BaseScreenVC.m
//  Raley's
//
//  Created by Billy Lewis on 10/3/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//
#define kNavigationBarHeight 44.0f

#import "BaseScreenVC.h"
#import "StoreLocatorScreenVC.h"
#import "Utility.h"
#import "Logging.h"

@interface BaseScreenVC ()

@end

@implementation BaseScreenVC


- (void)viewDidLoad
{
    [super viewDidLoad];

    // last part of the fix the status bar overlap problem on iOS7, also see AppDelegate:didFinishLaunchingWithOptions
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        View = [[UIView alloc] initWithFrame:CGRectMake(0, 20, self.view.frame.size.width, self.view.frame.size.height - 20)];
    else
        View = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];

    [self.view addSubview:View];

    _app = (id)[[UIApplication sharedApplication] delegate];
    _normalFont = _navButtonFont = _app._normalFont;
    _boldFont = _app._boldFont;
    _navigationBarHeight = kNavigationBarHeight;
    _menuViewWidth = _app._viewWidth * .34;
    _menuButtonHeight = _app._headerHeight;
    int footerTextYOrigin = kNavigationBarHeight * .12;
    int footerTextHeight = kNavigationBarHeight * .8;

    [View setBackgroundColor:[UIColor colorWithRed:0.95 green:0.95 blue:.95 alpha:1.0]];

    // screen header
    _headerImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _app._viewWidth, _app._headerHeight)];
    [_headerImageView setImage:[UIImage imageNamed:@"header"]];
    [View addSubview:_headerImageView];
    
    // back button
//    int buttonPad = _app._viewWidth * .01;
    //int backButtonSize = _app._headerHeight * .6;
    int backButtonSize = 35;//28.0f;
    _backButton = [[UIButton alloc] init];
    [_backButton setFrame:CGRectMake(0, (_app._headerHeight - backButtonSize) / 2, 70, 40)];
    [_backButton setImage:[UIImage imageNamed:@"back_button_white_small"] forState:UIControlStateNormal];
    [_backButton setImage:[UIImage imageNamed:@"back_button_white_small"] forState:UIControlStateSelected];
    _backButton.contentEdgeInsets = UIEdgeInsetsMake(5, 5, 5, 35);
    [_backButton addTarget:self action:@selector(backButtonPressed) forControlEvents:UIControlEventTouchUpInside];
    [View addSubview:_backButton];

    // navigation bar for subclasses
    _navigationBar = [[UIView alloc] initWithFrame:CGRectMake(0, _app._headerHeight, _app._viewWidth, _navigationBarHeight)];
    [View addSubview:_navigationBar];
    
    _navigationBarXOrigin = _navigationBar.frame.origin.x;
    _navigationBarYOrigin = _navigationBar.frame.origin.y;
    _navigationBarWidth = _navigationBar.frame.size.width;
    
    // content view for subclasses
//    _contentView = [[UIView alloc] initWithFrame:CGRectMake(0, _app._headerHeight, _app._viewWidth, _app._viewHeight - _app._headerHeight - kNavigationBarHeight)];
//     _contentView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, _app._viewWidth, _app._viewHeight - kNavigationBarHeight)];
    
    int total_tap_bar_height=kNavigationBarHeight-2;
    _contentView = [[UIView alloc] initWithFrame:CGRectMake(0, _app._headerHeight + total_tap_bar_height, _app._viewWidth, _app._viewHeight - _app._headerHeight - total_tap_bar_height)];
    [View addSubview:_contentView];
    
    _contentViewXOrigin = _contentView.frame.origin.x;
    _contentViewYOrigin = _contentView.frame.origin.y;
    _contentViewWidth = _contentView.frame.size.width;
    _contentViewHeight = _contentView.frame.size.height;
    
    // screen footer
    UIImageView *footerImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, _app._viewHeight - kNavigationBarHeight, _app._viewWidth, kNavigationBarHeight)];
    [footerImageView setImage:[UIImage imageNamed:@"footer"]];
//    [View addSubview:footerImageView];

    // footer top border
    UILabel *border = [[UILabel alloc] initWithFrame:CGRectMake(0, _app._viewHeight - kNavigationBarHeight, _app._viewWidth, 1)];
    border.backgroundColor = [UIColor blackColor];
//    [View addSubview:border];

    // shopping list image
    int shoppingListImageSize = kNavigationBarHeight * .6;
    UIImageView *shoppingListImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, (kNavigationBarHeight - shoppingListImageSize) / 2, shoppingListImageSize, shoppingListImageSize)];
    [shoppingListImageView setImage:[UIImage imageNamed:@"footer_listicon"]];
    [footerImageView addSubview:shoppingListImageView];

    int xPad = _contentViewWidth * .01;
    int footerDivider = (_app._viewWidth * .6);
    UIFont *font = [Utility fontForFamily:_app._normalFont andHeight:footerTextHeight];

    // footer labels
    _listNameValueLabel = [[UILabel alloc] initWithFrame:CGRectMake(shoppingListImageSize + xPad, footerTextYOrigin, footerDivider - shoppingListImageSize - xPad, footerTextHeight)];
    _listNameValueLabel.font = font;
    _listNameValueLabel.textAlignment = NSTextAlignmentLeft;
    _listNameValueLabel.backgroundColor = [UIColor clearColor];
    _listNameValueLabel.textColor = [UIColor whiteColor];
    [footerImageView addSubview:_listNameValueLabel];

    _listTotalValueLabel = [[UILabel alloc] initWithFrame:CGRectMake(footerDivider + xPad, footerTextYOrigin, _app._viewWidth * .39, footerTextHeight)];
    _listTotalValueLabel.font = font;
    _listTotalValueLabel.textAlignment = NSTextAlignmentRight;
    _listTotalValueLabel.backgroundColor = [UIColor clearColor];
    _listTotalValueLabel.textColor = [UIColor whiteColor];
    [footerImageView addSubview:_listTotalValueLabel];
}


- (NSUInteger)supportedInterfaceOrientations // iOS 6 and above
{
    return UIInterfaceOrientationMaskPortrait | UIInterfaceOrientationMaskPortraitUpsideDown;
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)setFooterDetails
{
    if(![Utility isEmpty:_app._currentShoppingList])
    {
        _listNameValueLabel.text = [NSString stringWithFormat:@"%@", _app._currentShoppingList.name];
        _listTotalValueLabel.text = [NSString stringWithFormat:@"$%.2f", _app._currentShoppingList.totalPrice];
    }
    else
    {
        _listNameValueLabel.text = @"";
        _listTotalValueLabel.text = @"";
    }
}


- (void)backButtonPressed
{
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [self.presentingViewController dismissViewControllerAnimated:NO completion:nil];
}


@end
