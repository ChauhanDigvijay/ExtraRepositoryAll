//
//  AccounrScreenVC.h
//  Raley's
//
//  Created by Billy Lewis on 1/31/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "BaseScreenVC.h"
#import "SmartTextView.h"
#import "OffsetTextField.h"
#import "AccountRequest.h"
#import "WebService.h"
#import "HeaderView.h"

#define STATE_DEFAULT_TEXT @"State"
#define STORE_DEFAULT_TEXT @"Select Your Favorite Location"
#define DEPARTMENT_DEFAULT_TEXT @"Select Your Favorite Department"

@interface AccountScreenVC : BaseScreenVC <UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate, WebServiceListener,HeaderViewDelegate>
{
    int               _tableCellHeight;
    UIColor           *_textColor;
    UIColor           *_hintColor;
    UIImage           *_activeViewImage;
    UIImage           *_inactiveViewImage;
    UIImage           *_checkedBitmap;
    UIImage           *_uncheckedBitmap;
    UIView            *_accountView;
    UIView            *_editView;
    UIView            *_commonHeader;
    UIView            *_page5Header;
    UIView            *_view1;
    UIView            *_view2;
    UIView            *_view3;
    UIView            *_view4;
    UIView            *_view5;
    UIView            *_view6;
    UIButton          *_view1NextButton;
    UIButton          *_view2NextButton;
    UIButton          *_view2PreviousButton;
    UIButton          *_view3NextButton;
    UIButton          *_view3PreviousButton;
    UIButton          *_view4NextButton;
    UIButton          *_view4PreviousButton;
    UIButton          *_view5NextButton;
    UIButton          *_view5PreviousButton;
    UIButton          *_view6PreviousButton;
    UIButton          *_stateButton;
    UIButton          *_storeButton;
    UIButton          *_departmentButton;
    UIButton          *_agreeCheckBox;
    UIButton          *_emailCheckBox;
    UIButton          *_textCheckBox;
    UIButton          *_cardCheckBox;
    UIButton          *_cardLessCheckBox;
    UIImageView       *_view1Tab;
    UIImageView       *_view2Tab;
    UIImageView       *_view3Tab;
    UIImageView       *_view4Tab;
    UIImageView       *_view5Tab;
    UIImageView       *_view6Tab;
    UITableView       *_stateTable;
    UITableView       *_departmentTable;
    NSArray           *_states;
    NSArray           *_departments;
    NSNumberFormatter *_numberFormatter;
    NSString          *_dialogText;
    SmartTextView     *_stateText;
    SmartTextView     *_storeText;
    SmartTextView     *_departmentText;
    OffsetTextField   *_emailTextField;
    OffsetTextField   *_passwordTextField;
    OffsetTextField   *_confirmPasswordTextField;
    OffsetTextField   *_firstNameTextField;
    OffsetTextField   *_lastNameTextField;
    OffsetTextField   *_homePhoneTextField;
    OffsetTextField   *_cellPhoneTextField;
    OffsetTextField   *_addressTextField;
    OffsetTextField   *_cityTextField;
    OffsetTextField   *_zipcodeTextField;
    OffsetTextField   *_loyaltyTextField;
    OffsetTextField   *_loyaltyConfirmTextField;
    AccountRequest    *_accountRequest;
    WebService        *_service;
}

@property (nonatomic, assign)BOOL _registrationPage;
-(void)backButtonClicked;

@end
