//
//  BaseScreenVC.h
//  Raley's
//
//  Created by Billy Lewis on 10/3/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "ShoppingList.h"
#import "ProgressDialog.h"
#import "TextDialog.h"


@interface BaseScreenVC : UIViewController
{
    int   _menuViewWidth;
    int   _menuButtonHeight;
    int   _navigationBarXOrigin;
    int   _navigationBarYOrigin;
    int   _navigationBarWidth;
    int   _navigationBarHeight;
    int   _contentViewXOrigin;
    int   _contentViewYOrigin;
    int   _contentViewWidth;
    int   _contentViewHeight;
    UIView          *View;
    NSString        *_normalFont;
    NSString        *_boldFont;
    NSString        *_navButtonFont;
    UIButton        *_backButton;
    UIImageView     *_headerImageView;
    UIView          *_navigationBar;
    UIView          *_contentView;
    UILabel         *_listNameValueLabel;
    UILabel         *_listTotalValueLabel;
    AppDelegate     *_app;
    ProgressDialog  *_progressDialog;
    TextDialog      *_textDialog;
}

- (void)setFooterDetails;

@end
