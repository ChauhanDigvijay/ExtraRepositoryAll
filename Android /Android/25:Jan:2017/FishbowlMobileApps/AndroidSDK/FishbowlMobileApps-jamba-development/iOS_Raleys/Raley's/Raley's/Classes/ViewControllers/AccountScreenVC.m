//
//  AccountScreenVC.m
//  Raley's
//
//  Created by Billy Lewis on 1/31/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "AccountScreenVC.h"
#import "Login.h"
#import "StoreLocatorScreenVC.h"
#import "SmartTextView.h"
#import "UIImage+scaleToSize.h"
#import "Utility.h"
#import "Logging.h"

@implementation AccountScreenVC

@synthesize _registrationPage;


- (void)viewDidLoad
{
    [super viewDidLoad];

    _states = [[NSArray alloc] initWithObjects:
                @"Alabama", @"Alaska", @"Arizona", @"Arkansas", @"California", @"Colorado", @"Connecticut", @"Delaware", @"DC", @"Florida",
                @"Georgia", @"Hawaii", @"Idaho", @"Illinois", @"Indiana", @"Iowa", @"Kansas", @"Kentucky", @"Louisiana", @"Maine",
                @"Maryland", @"Massachusetts", @"Michigan", @"Minnesota", @"Mississippi", @"Missouri", @"Montana", @"Nebraska", @"Nevada", @"New Hampshire",
                @"New Jersey", @"New Mexico", @"New York", @"North Carolina", @"North Dakota", @"Ohio", @"Oklahoma", @"Oregon", @"Pennsylvania",@ "Rhode Island",
                @"South Carolina", @"South Dakota", @"Tennessee", @"Texas", @"Utah", @"Vermont", @"Virginia", @"Washington", @"West Virginia", @"Wisconsin", @"Wyoming", nil];

    _departments = [[NSArray alloc] initWithObjects:
                    @"Fine Produce", @"Fine Meats", @"Fine Seafood", @"Grocery & Household", @"Wine, Beer & Spirits", @"Recipes", @"Deli Prepared Foods",
                    @"Fine Bakery", @"New Items", @"Natural & Organic Foods", @"Pharmacy & Wellness", nil];


    _app = (id)[[UIApplication sharedApplication] delegate];
    _normalFont = _app._normalFont;
    _boldFont = _app._boldFont;
    
    
    _accountRequest = [[AccountRequest alloc] init];

    if(_registrationPage == NO)
    {
        [self setFooterDetails];
        _accountRequest = _app._currentAccountRequest;
    }

    UIImageView *background = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _app._viewWidth, _app._viewHeight)];
    [background setImage:[UIImage imageNamed:@"account_background_blurred"]];
    [_contentView addSubview:background];

    _numberFormatter = [[NSNumberFormatter alloc] init];
    _textColor = [UIColor colorWithRed:0.35 green:0.23 blue:.08 alpha:1.0];
    _hintColor = [UIColor colorWithRed:0.63 green:0.53 blue:.38 alpha:1.0];

    // don't use base class content area
    _accountView = [[UIView alloc] initWithFrame:CGRectMake(0, _app._headerHeight, _app._viewWidth, _app._viewHeight - _app._headerHeight - _app._footerHeight)];
    [View addSubview:_accountView];
    
    HeaderView *headerView = [[HeaderView alloc] initWithFrame:CGRectMake(0.0, 0.0, 320.0, _app._headerHeight)];
    
    [headerView setDelegate:self];
    [View addSubview:headerView];
    

    
    int editViewXOrigin = _accountView.frame.size.width * .025;
    int editViewYOrigin = 60;
    int editViewWidth = _accountView.frame.size.width * .95;
    int editViewHeight = _accountView.frame.size.height;

    int backgroundHeight = editViewHeight * .62;

    _editView = [[UIView alloc] initWithFrame:CGRectMake(editViewXOrigin, editViewYOrigin, editViewWidth, editViewHeight)];
    [_accountView addSubview:_editView];

    // background for all views
    UIImageView *editBackground = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, editViewWidth, backgroundHeight)];
    editBackground.image = [UIImage imageNamed:@"account_background"];
    [_editView addSubview:editBackground];

    int editLine1YOrigin = backgroundHeight * .37;
    int editLine2YOrigin = backgroundHeight * .54;
    int editLine3YOrigin = backgroundHeight * .71;

    int textWidth = editViewWidth * .9;
    int textXOrigin = editViewWidth * .05;
    int textHeight = backgroundHeight * .11;
    int textFieldOffset = textWidth * .02;
    _tableCellHeight = textHeight;


    //----------------------------------------------------------------------------------------------------------------------------------
    // section tabs begin
    //----------------------------------------------------------------------------------------------------------------------------------
    _activeViewImage = [UIImage imageNamed:@"account_tab_selected"];
    _inactiveViewImage = [UIImage imageNamed:@"account_tab_unselected"];

    int tabCount;

    if(_registrationPage == YES)
        tabCount = 6;
    else
        tabCount = 5;

    int tabWidth = editViewWidth * .14;
    int tabHeight = backgroundHeight * .1;
    int tabInteriorPad = editViewWidth * .01;
    int tabExteriorPad = (editViewWidth - ((tabCount * tabWidth) + ((tabCount - 1) * tabInteriorPad))) / 2;
    UIFont *labelFont = [Utility fontForFamily:_app._boldFont andHeight:tabHeight * .7];

    CGRect buttonRects[tabCount];

    for(int i = 0; i < tabCount; i++)
    {
        buttonRects[i].origin.x = tabExteriorPad + (tabWidth * i) + (tabInteriorPad * i);
        buttonRects[i].origin.y = 0;
        buttonRects[i].size.width = tabWidth;
        buttonRects[i].size.height = tabHeight;
    };

    _view1Tab = [[UIImageView alloc] initWithFrame:buttonRects[0]];
    _view1Tab.image = _activeViewImage;
    [_editView addSubview:_view1Tab];

    UILabel *view1Label = [[UILabel alloc] initWithFrame:buttonRects[0]];
    view1Label.backgroundColor = [UIColor clearColor];
    view1Label.textColor = _textColor;
    view1Label.font = labelFont;
    view1Label.textAlignment = NSTextAlignmentCenter;
    view1Label.text = @"Step1";
    [_editView addSubview:view1Label];

    _view2Tab = [[UIImageView alloc] initWithFrame:buttonRects[1]];
    _view2Tab.image = _inactiveViewImage;
    [_editView addSubview:_view2Tab];

    UILabel *view2Label = [[UILabel alloc] initWithFrame:buttonRects[1]];
    view2Label.backgroundColor = [UIColor clearColor];
    view2Label.textColor = _textColor;
    view2Label.font = labelFont;
    view2Label.textAlignment = NSTextAlignmentCenter;
    view2Label.text = @"Step2";
    [_editView addSubview:view2Label];

    _view3Tab = [[UIImageView alloc] initWithFrame:buttonRects[2]];
    _view3Tab.image = _inactiveViewImage;
    [_editView addSubview:_view3Tab];

    UILabel *view3Label = [[UILabel alloc] initWithFrame:buttonRects[2]];
    view3Label.backgroundColor = [UIColor clearColor];
    view3Label.textColor = _textColor;
    view3Label.font = labelFont;
    view3Label.textAlignment = NSTextAlignmentCenter;
    view3Label.text = @"Step3";
    [_editView addSubview:view3Label];

    _view4Tab = [[UIImageView alloc] initWithFrame:buttonRects[3]];
    _view4Tab.image = _inactiveViewImage;
    [_editView addSubview:_view4Tab];

    UILabel *view4Label = [[UILabel alloc] initWithFrame:buttonRects[3]];
    view4Label.backgroundColor = [UIColor clearColor];
    view4Label.textColor = _textColor;
    view4Label.font = labelFont;
    view4Label.textAlignment = NSTextAlignmentCenter;
    view4Label.text = @"Step4";
    [_editView addSubview:view4Label];
    
    _view5Tab = [[UIImageView alloc] initWithFrame:buttonRects[4]];
    _view5Tab.image = _inactiveViewImage;
    [_editView addSubview:_view5Tab];

    UILabel *view5Label = [[UILabel alloc] initWithFrame:buttonRects[4]];
    view5Label.backgroundColor = [UIColor clearColor];
    view5Label.textColor = _textColor;
    view5Label.font = labelFont;
    view5Label.textAlignment = NSTextAlignmentCenter;

    if(_registrationPage == YES)
        view5Label.text = @"Step5";
    else
        view5Label.text = @"Done!";

    [_editView addSubview:view5Label];

    if(_registrationPage == YES)
    {
        _view6Tab = [[UIImageView alloc] initWithFrame:buttonRects[5]];
        _view6Tab.image = _inactiveViewImage;
        [_editView addSubview:_view6Tab];

        UILabel *view6Label = [[UILabel alloc] initWithFrame:buttonRects[5]];
        view6Label.backgroundColor = [UIColor clearColor];
        view6Label.textColor = _textColor;
        view6Label.font = labelFont;
        view6Label.textAlignment = NSTextAlignmentCenter;
        view6Label.text = @"Done!";
        [_editView addSubview:view6Label];
    }
    //----------------------------------------------------------------------------------------------------------------------------------
    // section tabs end
    //----------------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------------------------------------------
    // page views common setup begin
    //----------------------------------------------------------------------------------------------------------------------------------
    CGRect editFrame = CGRectMake(0, 0, editViewWidth, editViewHeight);

    _view1 = [[UIView alloc] initWithFrame:editFrame];
    [_editView addSubview:_view1];
    _view2 = [[UIView alloc] initWithFrame:editFrame];
    _view3 = [[UIView alloc] initWithFrame:editFrame];
    _view4 = [[UIView alloc] initWithFrame:editFrame];
    _view5 = [[UIView alloc] initWithFrame:editFrame];
    _view6 = [[UIView alloc] initWithFrame:editFrame];

    // buttons setup
    int arrowButtonHeight = backgroundHeight * .12;
    int arrowButtonYOrigin = backgroundHeight * .86;

    UIImage *previousButtonImage = [UIImage imageNamed:@"account_arrow_previous"];
    UIImage *nextButtonImage = [UIImage imageNamed:@"account_arrow_next"];
    CGRect previousButtonRect = CGRectMake(editViewWidth * .05, arrowButtonYOrigin, editViewWidth * .1, arrowButtonHeight);
    CGRect nextButtonRect = CGRectMake(editViewWidth * .75, arrowButtonYOrigin, editViewWidth * .2, arrowButtonHeight);

    // common header setup
    int commonHeaderHeight = (int)(backgroundHeight * .23);
    int topLineHeight = (int)(backgroundHeight * .09);
    int bottomLinesHeight = (int)(backgroundHeight * .08);

    _commonHeader = [[UIView alloc] initWithFrame:CGRectMake(0, editViewYOrigin + tabHeight, editViewWidth, commonHeaderHeight)];

    SmartTextView *commonHeaderLine1 = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, commonHeaderHeight * .02, textWidth, topLineHeight) backGroundColor:[UIColor clearColor]];
    commonHeaderLine1.numberOfLines = 1;
    commonHeaderLine1.fontFamily = _app._boldFont;
    commonHeaderLine1.textColor = _textColor;
    commonHeaderLine1.textAlignment = NSTextAlignmentCenter;
    commonHeaderLine1.text = @"Tell us about yourself...";
    [_commonHeader addSubview:commonHeaderLine1];

    SmartTextView *commonHeaderLine2 = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, commonHeaderHeight * .37, textWidth, bottomLinesHeight) backGroundColor:[UIColor clearColor]];
    commonHeaderLine2.numberOfLines = 1;
    commonHeaderLine2.fontFamily = _app._boldFont;
    commonHeaderLine2.textColor = _textColor;
    commonHeaderLine2.textAlignment = NSTextAlignmentCenter;
    commonHeaderLine2.text = @"and we'll do our best to";
    [_commonHeader addSubview:commonHeaderLine2];

    SmartTextView *commonHeaderLine3 = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, commonHeaderHeight * .65, textWidth, bottomLinesHeight) backGroundColor:[UIColor clearColor]];
    commonHeaderLine3.numberOfLines = 1;
    commonHeaderLine3.fontFamily = _app._boldFont;
    commonHeaderLine3.textColor = _textColor;
    commonHeaderLine3.textAlignment = NSTextAlignmentCenter;
    commonHeaderLine3.text = @"provide offers you want most!";
    [_commonHeader addSubview:commonHeaderLine3];

    [_editView addSubview:_commonHeader];
    _commonHeader.hidden = YES;
    //----------------------------------------------------------------------------------------------------------------------------------
    // page views common setup end
    //----------------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------------------------------------------
    // email page(view 1) begin
    //----------------------------------------------------------------------------------------------------------------------------------
    int headerLineHeight = (int)(backgroundHeight * .08);

    UILabel *headerLine1 = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, backgroundHeight * .11, textWidth, headerLineHeight)];
    headerLine1.backgroundColor = [UIColor clearColor];
    headerLine1.font = [Utility fontForFamily:_app._boldFont andHeight:headerLineHeight];
    headerLine1.textAlignment = NSTextAlignmentCenter;
    headerLine1.textColor = _textColor;
    headerLine1.text = @"Make lists. Get Offers.";
    [_view1 addSubview:headerLine1];

    UILabel *headerLine2 = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, backgroundHeight * .18, textWidth, headerLineHeight)];
    headerLine2.backgroundColor = [UIColor clearColor];
    headerLine2.font = [Utility fontForFamily:_app._boldFont andHeight:headerLineHeight];
    headerLine2.textAlignment = NSTextAlignmentCenter;
    headerLine2.textColor = _textColor;
    headerLine2.text = @" Order on E-cart.";
    [_view1 addSubview:headerLine2];

    UILabel *headerLine3 = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, backgroundHeight * .25, textWidth, headerLineHeight)];
    headerLine3.backgroundColor = [UIColor clearColor];
    headerLine3.font = [Utility fontForFamily:_app._boldFont andHeight:headerLineHeight];
    headerLine3.textAlignment = NSTextAlignmentCenter;
    headerLine3.textColor = _textColor;
    headerLine3.text = @"Sign up for Something Extra!";
    [_view1 addSubview:headerLine3];

    UIFont *textFieldFont = [Utility fontForFamily:_app._normalFont andHeight:textHeight * .7];

    _emailTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, editLine1YOrigin, textWidth, textHeight) offset:textFieldOffset];
    _emailTextField.delegate = self;
    _emailTextField.keyboardType = UIKeyboardTypeEmailAddress;
    _emailTextField.returnKeyType = UIReturnKeyNext;
    _emailTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _emailTextField.textColor = _textColor;
    _emailTextField.font = textFieldFont;
    _emailTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _emailTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;

    if(![Utility isEmpty:_accountRequest.email])
        _emailTextField.text = _accountRequest.email;
    else
        _emailTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Enter Your Email Address" attributes:@{NSForegroundColorAttributeName:_hintColor}];

    [_view1 addSubview:_emailTextField];

    SmartTextView *passwordFormatText = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, backgroundHeight * .48, textWidth, textHeight * .5) backGroundColor:[UIColor clearColor]];
    passwordFormatText.numberOfLines = 1;
    passwordFormatText.fontFamily = _app._normalFont;
    passwordFormatText.textColor = _hintColor;
    passwordFormatText.textAlignment = NSTextAlignmentLeft;
    passwordFormatText.text = @"(minimum 6 characters, at least one uppercase letter)";
    [_view1 addSubview:passwordFormatText];

    _passwordTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, editLine2YOrigin, textWidth, textHeight) offset:textFieldOffset];
    _passwordTextField.delegate = self;
    _passwordTextField.keyboardType = UIKeyboardTypeDefault;
    _passwordTextField.returnKeyType = UIReturnKeyNext;
    _passwordTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _passwordTextField.textColor = _textColor;
    _passwordTextField.font = textFieldFont;
    _passwordTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _passwordTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    _passwordTextField.secureTextEntry = YES;

    if(_registrationPage == NO)
        _passwordTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Change Password(optional)" attributes:@{NSForegroundColorAttributeName:_hintColor}];
    else
        _passwordTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Create a Password" attributes:@{NSForegroundColorAttributeName:_hintColor}];

    [_view1 addSubview:_passwordTextField];

    _confirmPasswordTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, editLine3YOrigin, textWidth, textHeight) offset:textFieldOffset];
    _confirmPasswordTextField.delegate = self;
    _confirmPasswordTextField.keyboardType = UIKeyboardTypeDefault;
    _confirmPasswordTextField.returnKeyType = UIReturnKeyDone;
    _confirmPasswordTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _confirmPasswordTextField.textColor = _textColor;
    _confirmPasswordTextField.font = textFieldFont;
    _confirmPasswordTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _confirmPasswordTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    _confirmPasswordTextField.secureTextEntry = YES;
    _confirmPasswordTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Retype Password" attributes:@{NSForegroundColorAttributeName:_hintColor}];
    [_view1 addSubview:_confirmPasswordTextField];

    _view1NextButton = [[UIButton alloc] initWithFrame:nextButtonRect];
    [_view1NextButton setBackgroundImage:nextButtonImage forState:UIControlStateNormal];
    [_view1NextButton addTarget:self action:@selector(forwardToView2) forControlEvents:UIControlEventTouchDown];
    [_view1 addSubview:_view1NextButton];
    //----------------------------------------------------------------------------------------------------------------------------------
    // email page(view 1) end
    //----------------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------------------------------------------
    // name page(view 2) begin
    //----------------------------------------------------------------------------------------------------------------------------------
    _firstNameTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, editLine1YOrigin, textWidth, textHeight) offset:textFieldOffset];
    _firstNameTextField.delegate = self;
    _firstNameTextField.keyboardType = UIKeyboardTypeEmailAddress;
    _firstNameTextField.returnKeyType = UIReturnKeyNext;
    _firstNameTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _firstNameTextField.textColor = _textColor;
    _firstNameTextField.font = textFieldFont;
    _firstNameTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _firstNameTextField.autocapitalizationType = UITextAutocapitalizationTypeWords;

    if(![Utility isEmpty:_accountRequest.firstName])
        _firstNameTextField.text = _accountRequest.firstName;
    else
        _firstNameTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"First Name" attributes:@{NSForegroundColorAttributeName:_hintColor}];

    [_view2 addSubview:_firstNameTextField];

    _lastNameTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, editLine2YOrigin, textWidth, textHeight) offset:textFieldOffset];
    _lastNameTextField.delegate = self;
    _lastNameTextField.keyboardType = UIKeyboardTypeDefault;
    _lastNameTextField.returnKeyType = UIReturnKeyNext;
    _lastNameTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _lastNameTextField.textColor = _textColor;
    _lastNameTextField.font = textFieldFont;
    _lastNameTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _lastNameTextField.autocapitalizationType = UITextAutocapitalizationTypeWords;

    if(![Utility isEmpty:_accountRequest.lastName])
        _lastNameTextField.text = _accountRequest.lastName;
    else
        _lastNameTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Last Name" attributes:@{NSForegroundColorAttributeName:_hintColor}];

    [_view2 addSubview:_lastNameTextField];

    SmartTextView *homePhoneFormatText = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, backgroundHeight * .65, textWidth * .475, textHeight * .5) backGroundColor:[UIColor clearColor]];
    homePhoneFormatText.numberOfLines = 1;
    homePhoneFormatText.fontFamily = _app._normalFont;
    homePhoneFormatText.textColor = _hintColor;
    homePhoneFormatText.textAlignment = NSTextAlignmentLeft;
    homePhoneFormatText.text = @"(xxx-xxx-xxxx format)";
    [_view2 addSubview:homePhoneFormatText];

    _homePhoneTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, editLine3YOrigin, textWidth * .475, textHeight) offset:textFieldOffset];
    _homePhoneTextField.delegate = self;
    _homePhoneTextField.keyboardType = UIKeyboardTypeDefault;
    _homePhoneTextField.returnKeyType = UIReturnKeyNext;
    _homePhoneTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _homePhoneTextField.textColor = _textColor;
    _homePhoneTextField.font = textFieldFont;
    _homePhoneTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _homePhoneTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;

    if(![Utility isEmpty:_accountRequest.phone])
        _homePhoneTextField.text = _accountRequest.phone;
    else
        _homePhoneTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Home Phone" attributes:@{NSForegroundColorAttributeName:_hintColor}];

    [_view2 addSubview:_homePhoneTextField];

    SmartTextView *cellPhoneFormatText = [[SmartTextView alloc] initWithFrame:CGRectMake((textXOrigin * 2) + (textWidth * .475), backgroundHeight * .65, textWidth * .475, textHeight * .5) backGroundColor:[UIColor clearColor]];
    cellPhoneFormatText.numberOfLines = 1;
    cellPhoneFormatText.fontFamily = _app._normalFont;
    cellPhoneFormatText.textColor = _hintColor;
    cellPhoneFormatText.textAlignment = NSTextAlignmentLeft;
    cellPhoneFormatText.text = @"(xxx-xxx-xxxx format)";
    [_view2 addSubview:cellPhoneFormatText];

    _cellPhoneTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake((textXOrigin * 2) + (textWidth * .475), editLine3YOrigin, textWidth * .475, textHeight) offset:textFieldOffset];
    _cellPhoneTextField.delegate = self;
    _cellPhoneTextField.keyboardType = UIKeyboardTypeDefault;
    _cellPhoneTextField.returnKeyType = UIReturnKeyDone;
    _cellPhoneTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _cellPhoneTextField.textColor = _textColor;
    _cellPhoneTextField.font = textFieldFont;
    _cellPhoneTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _cellPhoneTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;

    if(![Utility isEmpty:_accountRequest.mobilePhone])
        _cellPhoneTextField.text = _accountRequest.mobilePhone;
    else
        _cellPhoneTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Mobile Phone" attributes:@{NSForegroundColorAttributeName:_hintColor}];

    [_view2 addSubview:_cellPhoneTextField];

    _view2PreviousButton = [[UIButton alloc] initWithFrame:previousButtonRect];
    [_view2PreviousButton setBackgroundImage:previousButtonImage forState:UIControlStateNormal];
    [_view2PreviousButton addTarget:self action:@selector(backwardToView1) forControlEvents:UIControlEventTouchDown];
    [_view2 addSubview:_view2PreviousButton];

    _view2NextButton = [[UIButton alloc] initWithFrame:nextButtonRect];
    [_view2NextButton setBackgroundImage:nextButtonImage forState:UIControlStateNormal];
    [_view2NextButton addTarget:self action:@selector(forwardToView3) forControlEvents:UIControlEventTouchDown];
    [_view2 addSubview:_view2NextButton];

    _view2.hidden = YES;
    [_editView addSubview:_view2];
    //----------------------------------------------------------------------------------------------------------------------------------
    // name page(view 2) end
    //----------------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------------------------------------------
    // address page(view 3) begin
    //----------------------------------------------------------------------------------------------------------------------------------
    _addressTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, editLine1YOrigin, textWidth, textHeight) offset:textFieldOffset];
    _addressTextField.delegate = self;
    _addressTextField.keyboardType = UIKeyboardTypeEmailAddress;
    _addressTextField.returnKeyType = UIReturnKeyNext;
    _addressTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _addressTextField.textColor = _textColor;
    _addressTextField.font = textFieldFont;
    _addressTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _addressTextField.autocapitalizationType = UITextAutocapitalizationTypeWords;

    if(![Utility isEmpty:_accountRequest.address])
        _addressTextField.text = _accountRequest.address;
    else
        _addressTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Mailing Address (for rewards)" attributes:@{NSForegroundColorAttributeName:_hintColor}];

    [_view3 addSubview:_addressTextField];

    _cityTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, editLine2YOrigin, textWidth, textHeight) offset:textFieldOffset];
    _cityTextField.delegate = self;
    _cityTextField.keyboardType = UIKeyboardTypeDefault;
    _cityTextField.returnKeyType = UIReturnKeyNext;
    _cityTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _cityTextField.textColor = _textColor;
    _cityTextField.font = textFieldFont;
    _cityTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _cityTextField.autocapitalizationType = UITextAutocapitalizationTypeWords;

    if(![Utility isEmpty:_accountRequest.city])
        _cityTextField.text = _accountRequest.city;
    else
        _cityTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"City" attributes:@{NSForegroundColorAttributeName:_hintColor}];

    [_view3 addSubview:_cityTextField];

    SmartTextView *zipcodeFormatText = [[SmartTextView alloc] initWithFrame:CGRectMake(editViewWidth * .625, backgroundHeight * .65, editViewWidth * .325, textHeight * .5) backGroundColor:[UIColor clearColor]];
    zipcodeFormatText.numberOfLines = 1;
    zipcodeFormatText.fontFamily = _app._normalFont;
    zipcodeFormatText.textColor = _hintColor;
    zipcodeFormatText.textAlignment = NSTextAlignmentLeft;
    zipcodeFormatText.text = @"(5 digits)";
    [_view3 addSubview:zipcodeFormatText];

    _zipcodeTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(editViewWidth * .625, editLine3YOrigin, editViewWidth * .325, textHeight) offset:textFieldOffset];
    _zipcodeTextField.delegate = self;
    _zipcodeTextField.keyboardType = UIKeyboardTypeDefault;
    _zipcodeTextField.returnKeyType = UIReturnKeyDone;
    _zipcodeTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _zipcodeTextField.textColor = _textColor;
    _zipcodeTextField.font = textFieldFont;
    _zipcodeTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _zipcodeTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;

    if(![Utility isEmpty:_accountRequest.zip])
        _zipcodeTextField.text = _accountRequest.zip;
    else
        _zipcodeTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Zip Code" attributes:@{NSForegroundColorAttributeName:_hintColor}];
    
    [_view3 addSubview:_zipcodeTextField];

    _view3PreviousButton = [[UIButton alloc] initWithFrame:previousButtonRect];
    [_view3PreviousButton setBackgroundImage:previousButtonImage forState:UIControlStateNormal];
    [_view3PreviousButton addTarget:self action:@selector(backwardToView2) forControlEvents:UIControlEventTouchDown];
    [_view3 addSubview:_view3PreviousButton];

    _view3NextButton = [[UIButton alloc] initWithFrame:nextButtonRect];
    [_view3NextButton setBackgroundImage:nextButtonImage forState:UIControlStateNormal];
    [_view3NextButton addTarget:self action:@selector(forwardToView4) forControlEvents:UIControlEventTouchDown];
    [_view3 addSubview:_view3NextButton];
    
    // add the state stuff last so it's list covers other fields when exposed
    int stateButtonWidth = (int)(editViewWidth * .55);
    int stateButtonSize = textHeight;

    UIImageView *stateView = [[UIImageView alloc] initWithFrame:CGRectMake(textXOrigin, editLine3YOrigin, stateButtonWidth, textHeight)];
    stateView.image = [UIImage imageNamed:@"account_cell_medium"];
    [_view3 addSubview:stateView];

    _stateButton = [[UIButton alloc] initWithFrame:CGRectMake(textXOrigin + stateButtonWidth - stateButtonSize, editLine3YOrigin, stateButtonSize, stateButtonSize)];
    [_stateButton setBackgroundImage:[UIImage imageNamed:@"account_updown_button"] forState:UIControlStateNormal];
    [_stateButton addTarget:self action:@selector(chooseState) forControlEvents:UIControlEventTouchDown];
    [_view3 addSubview:_stateButton];

    _stateText = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin + (textWidth * .02), editLine3YOrigin + textHeight * .15, stateButtonWidth - stateButtonSize, textHeight * .7) backGroundColor:[UIColor clearColor]];
    _stateText.numberOfLines = 1;
    _stateText.fontFamily = _app._normalFont;
    _stateText.textColor = _hintColor;
    _stateText.textAlignment = NSTextAlignmentLeft;

    if(![Utility isEmpty:_accountRequest.state])
    {
        _stateText.textColor = _textColor;
        _stateText.text = _accountRequest.state;
    }
    else
    {
        _stateText.text = STATE_DEFAULT_TEXT;
    }

    [_view3 addSubview:_stateText];

    _stateTable = [[UITableView alloc] initWithFrame:CGRectMake(textXOrigin, editLine3YOrigin + textHeight, stateButtonWidth, textHeight * 6)];
    _stateTable.dataSource = self;
    _stateTable.delegate = self;
    _stateTable.backgroundColor = [UIColor clearColor];
    _stateTable.separatorStyle = UITableViewCellSeparatorStyleNone;
    _stateTable.hidden = YES;
    [_view3 addSubview:_stateTable];

    _view3.hidden = YES;
    [_editView addSubview:_view3];
    //----------------------------------------------------------------------------------------------------------------------------------
    // address page(view 3) end
    //----------------------------------------------------------------------------------------------------------------------------------

    
    //----------------------------------------------------------------------------------------------------------------------------------
    // preference page(view 4) begin
    //----------------------------------------------------------------------------------------------------------------------------------
    UIImage *preferenceBackground = [UIImage imageNamed:@"account_cell_wide"];
    UIImage *preferenceButton = [UIImage imageNamed:@"account_updown_button"];
    int preferenceButtonSize = textHeight;
    int preferenceTextWidth = textWidth - preferenceButtonSize;
    int storeYOrigin = backgroundHeight * .35;
    int departmentYOrigin;

    if(_registrationPage == YES)
        departmentYOrigin = backgroundHeight * .49;
    else
        departmentYOrigin = backgroundHeight * .42;

    if(_registrationPage == YES)
    {
        UIImageView *storeView = [[UIImageView alloc] initWithFrame:CGRectMake(textXOrigin, storeYOrigin, textWidth, textHeight)];
        storeView.image = preferenceBackground;
        [_view4 addSubview:storeView];
        
        _storeButton = [[UIButton alloc] initWithFrame:CGRectMake(textXOrigin + preferenceTextWidth, storeYOrigin, preferenceButtonSize, preferenceButtonSize)];
        [_storeButton setBackgroundImage:preferenceButton forState:UIControlStateNormal];
        [_storeButton addTarget:self action:@selector(chooseStore) forControlEvents:UIControlEventTouchDown];
        [_view4 addSubview:_storeButton];

        _storeText = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin + (textWidth * .02), storeYOrigin + textHeight * .2, preferenceTextWidth, textHeight * .6) backGroundColor:[UIColor clearColor]];
        _storeText.numberOfLines = 1;
        _storeText.fontFamily = _app._normalFont;
        _storeText.textColor = _hintColor;
        _storeText.textAlignment = NSTextAlignmentLeft;
        _storeText.text = STORE_DEFAULT_TEXT;
        [_view4 addSubview:_storeText];
    }

    int checkBoxSize = (int)(textHeight * .8);
    int checkBoxXOrigin = textXOrigin * 1.5;
    int choiceTextXOrigin = checkBoxXOrigin + (int)(checkBoxSize * 1.2);
    int choiceTextWidth = textWidth - choiceTextXOrigin;

    _checkedBitmap = [UIImage imageNamed:@"account_checked_box"];
    _uncheckedBitmap = [UIImage imageNamed:@"account_unchecked_box"];

    _emailCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(checkBoxXOrigin, backgroundHeight * .63, checkBoxSize, checkBoxSize)];
    [_emailCheckBox setBackgroundImage:_uncheckedBitmap forState:UIControlStateNormal];
    [_emailCheckBox setBackgroundImage:_checkedBitmap forState:UIControlStateSelected];
    [_emailCheckBox addTarget:self action:@selector(toggleEmailCheckBox) forControlEvents:UIControlEventTouchDown];

    if(![Utility isEmpty:_accountRequest.sendEmailsFlag] && (_accountRequest.sendEmailsFlag == [NSNumber numberWithBool:YES]))
        [_emailCheckBox setSelected:YES];
    else
        [_emailCheckBox setSelected:NO];
    
    [_view4 addSubview:_emailCheckBox];

    SmartTextView *emailChoiceText = [[SmartTextView alloc] initWithFrame:CGRectMake(choiceTextXOrigin, backgroundHeight * .62, choiceTextWidth, textHeight) backGroundColor:[UIColor clearColor]];
    emailChoiceText.numberOfLines = 2;
    emailChoiceText.fontFamily = _app._boldFont;
    emailChoiceText.textColor = _textColor;
    emailChoiceText.textAlignment = NSTextAlignmentLeft;
    emailChoiceText.text = @"When my new offers are ready, please email me!";
    [_view4 addSubview:emailChoiceText];

    _textCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(checkBoxXOrigin, backgroundHeight * .75, checkBoxSize, checkBoxSize)];
    [_textCheckBox setBackgroundImage:_uncheckedBitmap forState:UIControlStateNormal];
    [_textCheckBox setBackgroundImage:_checkedBitmap forState:UIControlStateSelected];
    [_textCheckBox addTarget:self action:@selector(toggleTextCheckBox) forControlEvents:UIControlEventTouchDown];

    if(![Utility isEmpty:_accountRequest.sendTextsFlag] && (_accountRequest.sendTextsFlag == [NSNumber numberWithBool:YES]))
        [_textCheckBox setSelected:YES];
    else
        [_textCheckBox setSelected:NO];

    [_view4 addSubview:_textCheckBox];

    SmartTextView *textChoiceText = [[SmartTextView alloc] initWithFrame:CGRectMake(choiceTextXOrigin, backgroundHeight * .74, choiceTextWidth, textHeight) backGroundColor:[UIColor clearColor]];
    textChoiceText.numberOfLines = 2;
    textChoiceText.fontFamily = _app._boldFont;
    textChoiceText.textColor = _textColor;
    textChoiceText.textAlignment = NSTextAlignmentLeft;
    textChoiceText.text = @"When there's a really hot offer for me, text me on my phone! Standard rates may apply.";
    [_view4 addSubview:textChoiceText];

    _view4PreviousButton = [[UIButton alloc] initWithFrame:previousButtonRect];
    [_view4PreviousButton setBackgroundImage:previousButtonImage forState:UIControlStateNormal];
    [_view4PreviousButton addTarget:self action:@selector(backwardToView3) forControlEvents:UIControlEventTouchDown];
    [_view4 addSubview:_view4PreviousButton];

    _view4NextButton = [[UIButton alloc] initWithFrame:nextButtonRect];
    [_view4NextButton setBackgroundImage:nextButtonImage forState:UIControlStateNormal];
    [_view4NextButton addTarget:self action:@selector(forwardToView5) forControlEvents:UIControlEventTouchDown];
    [_view4 addSubview:_view4NextButton];

    // add the department stuff last so it's list covers other fields when exposed
    UIImageView *departmentView = [[UIImageView alloc] initWithFrame:CGRectMake(textXOrigin, departmentYOrigin, textWidth, textHeight)];
    departmentView.image = preferenceBackground;
    [_view4 addSubview:departmentView];

    _departmentButton = [[UIButton alloc] initWithFrame:CGRectMake(textXOrigin + preferenceTextWidth, departmentYOrigin, preferenceButtonSize, preferenceButtonSize)];
    [_departmentButton setBackgroundImage:preferenceButton forState:UIControlStateNormal];
    [_departmentButton addTarget:self action:@selector(chooseDepartment) forControlEvents:UIControlEventTouchDown];
    [_view4 addSubview:_departmentButton];

    _departmentText = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin + (textWidth * .02), departmentYOrigin + textHeight * .2, preferenceTextWidth, textHeight * .6) backGroundColor:[UIColor clearColor]];
    _departmentText.numberOfLines = 1;
    _departmentText.fontFamily = _app._normalFont;
    _departmentText.textColor = _hintColor;
    _departmentText.textAlignment = NSTextAlignmentLeft;

    if(![Utility isEmpty:_accountRequest.favoriteDept])
    {
        _departmentText.textColor = _textColor;
        _departmentText.text = _accountRequest.favoriteDept;
    }
    else
    {
        _departmentText.text = DEPARTMENT_DEFAULT_TEXT;
    }

    [_view4 addSubview:_departmentText];

    _departmentTable = [[UITableView alloc] initWithFrame:CGRectMake(textXOrigin, departmentYOrigin + textHeight, textWidth, textHeight * 6)];
    _departmentTable.dataSource = self;
    _departmentTable.delegate = self;
    _departmentTable.backgroundColor = [UIColor clearColor];
    _departmentTable.separatorStyle = UITableViewCellSeparatorStyleNone;
    _departmentTable.hidden = YES;
    [_view4 addSubview:_departmentTable];

    _view4.hidden = YES;
    [_editView addSubview:_view4];
    //----------------------------------------------------------------------------------------------------------------------------------
    // preference page(view 4) end
    //----------------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------------------------------------------
    // something extra page(view 5) begin
    //----------------------------------------------------------------------------------------------------------------------------------
    int page5HeaderHeight = (int)(backgroundHeight * .14);
    int lineHeight = (int)(page5HeaderHeight * .55);
    _page5Header = [[UIView alloc] initWithFrame:CGRectMake(0, editViewYOrigin + (tabHeight * 1.1), editViewWidth, page5HeaderHeight)];

    SmartTextView *page5HeaderLine1 = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, page5HeaderHeight * .02, textWidth, lineHeight) backGroundColor:[UIColor clearColor]];
    page5HeaderLine1.numberOfLines = 1;
    page5HeaderLine1.fontFamily = _app._boldFont;
    page5HeaderLine1.textColor = _textColor;
    page5HeaderLine1.textAlignment = NSTextAlignmentCenter;
    page5HeaderLine1.text = @"Use your Something Extra account";
    [_page5Header addSubview:page5HeaderLine1];

    SmartTextView *page5HeaderLine2 = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, page5HeaderHeight * .50, textWidth, lineHeight) backGroundColor:[UIColor clearColor]];
    page5HeaderLine2.numberOfLines = 1;
    page5HeaderLine2.fontFamily = _app._boldFont;
    page5HeaderLine2.textColor = _textColor;
    page5HeaderLine2.textAlignment = NSTextAlignmentCenter;
    page5HeaderLine2.text = @"every time you shop.";
    [_page5Header addSubview:page5HeaderLine2];

    _page5Header.hidden = YES;
    [_editView addSubview:_page5Header];

    _loyaltyTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, backgroundHeight * .28, textWidth, textHeight) offset:textFieldOffset];
    _loyaltyTextField.delegate = self;
    _loyaltyTextField.keyboardType = UIKeyboardTypeDefault;
    _loyaltyTextField.returnKeyType = UIReturnKeyNext;
    _loyaltyTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _loyaltyTextField.textColor = _textColor;
    _loyaltyTextField.font = textFieldFont;
    _loyaltyTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _loyaltyTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    _loyaltyTextField.secureTextEntry = YES;

    if(_registrationPage == NO)
        _loyaltyTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Change 10-digit Loyalty Number(optional)" attributes:@{NSForegroundColorAttributeName:_hintColor}];
    else
        _loyaltyTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Create a 10-digit Loyalty Number" attributes:@{NSForegroundColorAttributeName:_hintColor}];

    [_view5 addSubview:_loyaltyTextField];

    SmartTextView *loyaltyFormatText = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, backgroundHeight * .39, textWidth, textHeight * .5) backGroundColor:[UIColor clearColor]];
    loyaltyFormatText.numberOfLines = 1;
    loyaltyFormatText.fontFamily = _app._normalFont;
    loyaltyFormatText.textColor = _hintColor;
    loyaltyFormatText.textAlignment = NSTextAlignmentLeft;
    loyaltyFormatText.text = @"Many customers like to use their phone number.";
    [_view5 addSubview:loyaltyFormatText];

    _loyaltyConfirmTextField = [[OffsetTextField alloc] initWithFrame:CGRectMake(textXOrigin, backgroundHeight * .45, textWidth, textHeight) offset:textFieldOffset];
    _loyaltyConfirmTextField.delegate = self;
    _loyaltyConfirmTextField.keyboardType = UIKeyboardTypeDefault;
    _loyaltyConfirmTextField.returnKeyType = UIReturnKeyDone;
    _loyaltyConfirmTextField.background = [UIImage imageNamed:@"account_cell_wide"];
    _loyaltyConfirmTextField.textColor = _textColor;
    _loyaltyConfirmTextField.font = textFieldFont;
    _loyaltyConfirmTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _loyaltyConfirmTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    _loyaltyConfirmTextField.secureTextEntry = YES;
    _loyaltyConfirmTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"Retype Loyalty Number" attributes:@{NSForegroundColorAttributeName:_hintColor}];
    [_view5 addSubview:_loyaltyConfirmTextField];

    if(_registrationPage == YES)
    {
        _cardCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(checkBoxXOrigin, backgroundHeight * .63, checkBoxSize, checkBoxSize)];
        [_cardCheckBox setBackgroundImage:_uncheckedBitmap forState:UIControlStateNormal];
        [_cardCheckBox setBackgroundImage:_checkedBitmap forState:UIControlStateSelected];
        [_cardCheckBox addTarget:self action:@selector(toggleCardCheckBox) forControlEvents:UIControlEventTouchDown];

        if(![Utility isEmpty:_accountRequest.issueCardFlag] && (_accountRequest.issueCardFlag == [NSNumber numberWithBool:YES]))
            [_cardCheckBox setSelected:YES];
        else
            [_cardCheckBox setSelected:NO];

        [_view5 addSubview:_cardCheckBox];

        SmartTextView *cardChoiceText = [[SmartTextView alloc] initWithFrame:CGRectMake(choiceTextXOrigin, backgroundHeight * .62, choiceTextWidth, textHeight) backGroundColor:[UIColor clearColor]];
        cardChoiceText.numberOfLines = 2;
        cardChoiceText.fontFamily = _app._boldFont;
        cardChoiceText.textColor = _textColor;
        cardChoiceText.textAlignment = NSTextAlignmentLeft;
        cardChoiceText.text = @"I'd like to request a Something Extra rewards card.";
        [_view5 addSubview:cardChoiceText];

        _cardLessCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(checkBoxXOrigin, backgroundHeight * .75, checkBoxSize, checkBoxSize)];
        [_cardLessCheckBox setBackgroundImage:_uncheckedBitmap forState:UIControlStateNormal];
        [_cardLessCheckBox setBackgroundImage:_checkedBitmap forState:UIControlStateSelected];
        [_cardLessCheckBox addTarget:self action:@selector(toggleCardlessCheckBox) forControlEvents:UIControlEventTouchDown];

        if(![Utility isEmpty:_accountRequest.issueCardFlag] && (_accountRequest.issueCardFlag == [NSNumber numberWithBool:YES]))
            [_cardLessCheckBox setSelected:NO];
        else
            [_cardLessCheckBox setSelected:YES];

        [_view5 addSubview:_cardLessCheckBox];

        SmartTextView *cardLessChoiceText = [[SmartTextView alloc] initWithFrame:CGRectMake(choiceTextXOrigin, backgroundHeight * .74, choiceTextWidth, textHeight) backGroundColor:[UIColor clearColor]];
        cardLessChoiceText.numberOfLines = 2;
        cardLessChoiceText.fontFamily = _app._boldFont;
        cardLessChoiceText.textColor = _textColor;
        cardLessChoiceText.textAlignment = NSTextAlignmentLeft;
        cardLessChoiceText.text = @"No thanks, I'll just shop with my Loyalty Number.";
        [_view5 addSubview:cardLessChoiceText];
    }
    
    _view5PreviousButton = [[UIButton alloc] initWithFrame:previousButtonRect];
    [_view5PreviousButton setBackgroundImage:previousButtonImage forState:UIControlStateNormal];
    [_view5PreviousButton addTarget:self action:@selector(backwardToView4) forControlEvents:UIControlEventTouchDown];
    [_view5 addSubview:_view5PreviousButton];

    if(_registrationPage == YES)
    {
        _view5NextButton = [[UIButton alloc] initWithFrame:nextButtonRect];
        [_view5NextButton setBackgroundImage:nextButtonImage forState:UIControlStateNormal];
        [_view5NextButton addTarget:self action:@selector(forwardToView6) forControlEvents:UIControlEventTouchDown];
        [_view5 addSubview:_view5NextButton];
    }
    else
    {
        int submitButtonWidth = (int)(editViewWidth * .35);
        UIButton *submitButton = [[UIButton alloc] initWithFrame:CGRectMake(editViewWidth - textXOrigin - submitButtonWidth, arrowButtonYOrigin, submitButtonWidth, arrowButtonHeight)];
        [submitButton setBackgroundImage:[UIImage imageNamed:@"account_submit_button"] forState:UIControlStateNormal];
        [submitButton setBackgroundColor:[UIColor clearColor]];
        [submitButton.titleLabel setFont:[Utility fontForFamily:_app._boldFont andHeight:arrowButtonHeight * .6]];
        [submitButton setTitleColor:_textColor forState:UIControlStateNormal];
        [submitButton setTitle:@"SUBMIT" forState:UIControlStateNormal];
        [submitButton addTarget:self action:@selector(changeAccount) forControlEvents:UIControlEventTouchDown];
        [_view5 addSubview:submitButton];
    }

    _view5.hidden = YES;
    [_editView addSubview:_view5];
    //----------------------------------------------------------------------------------------------------------------------------------
    // something extra page(view 5) end
    //----------------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------------------------------------------
    // terms and conditions page(view6) begin
    //----------------------------------------------------------------------------------------------------------------------------------
    if(_registrationPage == YES)
    {
        int disclosureBitmapHeight = (int)(backgroundHeight * .49);

        UIImageView *disclosureBackground = [[UIImageView alloc] initWithFrame:CGRectMake(textXOrigin, backgroundHeight * .13, textWidth, disclosureBitmapHeight)];
        disclosureBackground.image = [UIImage imageNamed:@"account_cell_wide"];
        [_view6 addSubview:disclosureBackground];

        NSString *text = [NSString stringWithFormat:@"%@%@%@",
                          @"Welcome to Something Extra! We hope our Something Extra Program makes your life easier, healthier and happier. We know ",
                          @"it would be easier not to have terms and conditions to read, but then you might miss some of the nifty features of ",
                          @"Something Extra. We plan to change and improve Something Extra over time, so please come back and check this page frequently for any changes."];


        SmartTextView *disclosureText = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin + textWidth * .03, backgroundHeight * .15, textWidth * .94, backgroundHeight * .45) backGroundColor:[UIColor clearColor]];

        if(_app._deviceType == IPHONE_5)
            disclosureText.numberOfLines = 9;
        else
            disclosureText.numberOfLines = 8;

        disclosureText.fontFamily = _app._normalFont;
        disclosureText.textColor = _textColor;
        disclosureText.textAlignment = NSTextAlignmentLeft;
        disclosureText.text = text;
        [_view6 addSubview:disclosureText];

        _agreeCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(checkBoxXOrigin, backgroundHeight * .69, checkBoxSize, checkBoxSize)];
        [_agreeCheckBox setBackgroundImage:_uncheckedBitmap forState:UIControlStateNormal];
        [_agreeCheckBox setBackgroundImage:_checkedBitmap forState:UIControlStateSelected];
        [_agreeCheckBox addTarget:self action:@selector(toggleAgreeCheckBox) forControlEvents:UIControlEventTouchDown];
        [_view6 addSubview:_agreeCheckBox];

        SmartTextView *agreeText = [[SmartTextView alloc] initWithFrame:CGRectMake(choiceTextXOrigin, backgroundHeight * .68, choiceTextWidth, textHeight) backGroundColor:[UIColor clearColor]];
        agreeText.numberOfLines = 2;
        agreeText.fontFamily = _app._normalFont;
        agreeText.textColor = _textColor;
        agreeText.textAlignment = NSTextAlignmentLeft;
        agreeText.text = @"I agree to to the Terms and Conditions of this program.";
        [_view6 addSubview:agreeText];

        int agreeButtonWidth = (int)(editViewWidth * .35);
        UIButton *submitButton = [[UIButton alloc] initWithFrame:CGRectMake(editViewWidth - textXOrigin - agreeButtonWidth, arrowButtonYOrigin, agreeButtonWidth, arrowButtonHeight)];
        [submitButton setBackgroundImage:[UIImage imageNamed:@"account_submit_button"] forState:UIControlStateNormal];
        [submitButton setBackgroundColor:[UIColor clearColor]];
        [submitButton.titleLabel setFont:[Utility fontForFamily:_app._boldFont andHeight:arrowButtonHeight * .6]];
        [submitButton setTitleColor:_textColor forState:UIControlStateNormal];
        [submitButton setTitle:@"SIGN ME UP" forState:UIControlStateNormal];
        [submitButton addTarget:self action:@selector(createAccount) forControlEvents:UIControlEventTouchDown];
        [_view6 addSubview:submitButton];

        _view6PreviousButton = [[UIButton alloc] initWithFrame:previousButtonRect];
        [_view6PreviousButton setBackgroundImage:previousButtonImage forState:UIControlStateNormal];
        [_view6PreviousButton addTarget:self action:@selector(backwardToView5) forControlEvents:UIControlEventTouchDown];
        [_view6 addSubview:_view6PreviousButton];
        
        _view6.hidden = YES;
        [_editView addSubview:_view6];
    }
    //----------------------------------------------------------------------------------------------------------------------------------
    // terms and conditions page(view6) end
    //----------------------------------------------------------------------------------------------------------------------------------
}
-(void)backButtonClicked
{
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];

    if(_app._locateForAccount == YES)
    {
        _app._locateForAccount = NO;

        if(_app._storeForAccount != nil)
        {
            _accountRequest.storeNumber = _app._storeForAccount.storeNumber;
            _storeText.textColor = _textColor;
            _storeText.text = [NSString stringWithFormat:@"%@ %@", _app._storeForAccount.chain, _app._storeForAccount.address];
            [_storeText setNeedsDisplay];
        }
    }
}


- (void)chooseSalutation
{
    [_firstNameTextField resignFirstResponder];
    [_lastNameTextField resignFirstResponder];
    [_homePhoneTextField resignFirstResponder];
    [_cellPhoneTextField resignFirstResponder];
    //_salutationTable.hidden = NO;
}


- (void)chooseState
{
    [_addressTextField resignFirstResponder];
    [_cityTextField resignFirstResponder];
    [_zipcodeTextField resignFirstResponder];
    _stateTable.hidden = NO;
}


- (void)chooseDepartment
{
    _departmentTable.hidden = NO;
}


- (void)chooseStore
{
    _departmentTable.hidden = YES;

    if(_app._allStoresList.count == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Sorry, the store locator is not available at this time."]];
        [dialog show];
        return;
    }

    _app._locateForAccount = YES;
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:1.0];
    [UIView setAnimationTransition:UIViewAnimationTransitionFlipFromRight forView:[self.view superview] cache:YES];
    [UIView commitAnimations];
    StoreLocatorScreenVC *storeLocatorScreenVC = [[StoreLocatorScreenVC alloc] init];
    [self presentViewController:storeLocatorScreenVC animated:NO completion:nil];
}


- (void)changeAccount
{
    if([self validateView5] == NO)
        return;

    [self sendRequest];
}


- (void)createAccount
{
    if([self validateView6] == NO)
        return;

    [self sendRequest];
}


- (void)toggleAgreeCheckBox
{
    if(_agreeCheckBox.isSelected == YES)
        [_agreeCheckBox setSelected:NO];
    else
        [_agreeCheckBox setSelected:YES];
}


- (void)toggleEmailCheckBox
{
    if(_emailCheckBox.isSelected == YES)
        [_emailCheckBox setSelected:NO];
    else
        [_emailCheckBox setSelected:YES];
}


- (void)toggleTextCheckBox
{
    if(_textCheckBox.isSelected == YES)
        [_textCheckBox setSelected:NO];
    else
        [_textCheckBox setSelected:YES];
}


- (void)toggleCardCheckBox
{
    if(_cardCheckBox.isSelected == YES)
    {
        [_cardCheckBox setSelected:NO];
        [_cardLessCheckBox setSelected:YES];
    }
    else
    {
        [_cardLessCheckBox setSelected:NO];
        [_cardCheckBox setSelected:YES];
    }
}


- (void)toggleCardlessCheckBox
{
    if(_cardLessCheckBox.isSelected == YES)
    {
        [_cardLessCheckBox setSelected:NO];
        [_cardCheckBox setSelected:YES];
    }
    else
    {
        [_cardCheckBox setSelected:NO];
        [_cardLessCheckBox setSelected:YES];
    }
}


- (void)backwardToView1
{
    _commonHeader.hidden = YES;
    _view1.hidden = NO;
    _view1Tab.image = _activeViewImage;
    _view2.hidden = YES;
    _view2Tab.image = _inactiveViewImage;
}


- (BOOL)validateView1
{
    _accountRequest.email = _emailTextField.text;
    _accountRequest.password = _passwordTextField.text;
    NSString *confirmPassword = _confirmPasswordTextField.text;


    if(_accountRequest.email.length == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Email address can't be blank"];
        [dialog show];
        return NO;
    }

    if([Utility validateEmail:_accountRequest.email] == false)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Email Address is invalid"];
        [dialog show];
        return NO;
    }

    if(_registrationPage == NO && _accountRequest.password.length == 0 && confirmPassword.length == 0)
        return YES;

    if(_accountRequest.password.length == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Password can't be blank"];
        [dialog show];
        return NO;
    }

    if(confirmPassword.length == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Confirm password can't be blank"];
        [dialog show];
        return NO;
    }

    if(_accountRequest.password.length < 6 || confirmPassword.length < 6 || [[_accountRequest.password componentsSeparatedByCharactersInSet:[NSCharacterSet uppercaseLetterCharacterSet]] count] - 1 == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Passwords must be at least 6 characters and contain at least one uppercase letter"];
        [dialog show];
        return NO;
    }

    if(![confirmPassword isEqualToString:_accountRequest.password])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Passwords do not match"];
        [dialog show];
        return NO;
    }

    return YES;
}


- (void)forwardToView2
{
    if([self validateView1] == NO)
        return;

    _commonHeader.hidden = NO;
    _view2.hidden = NO;
    _view2Tab.image = _activeViewImage;
    _view1.hidden = YES;
    _view1Tab.image = _inactiveViewImage;
}


- (void)backwardToView2
{
    _view2.hidden = NO;
    _view2Tab.image = _activeViewImage;
    _view3.hidden = YES;
    _view3Tab.image = _inactiveViewImage;
}


- (BOOL)validateView2
{
    _accountRequest.firstName = _firstNameTextField.text;
    _accountRequest.lastName = _lastNameTextField.text;
    //_accountRequest.prefix = _salutationText.text;
    _accountRequest.phone = _homePhoneTextField.text;
    _accountRequest.mobilePhone = _cellPhoneTextField.text;

    if([Utility isEmpty:_accountRequest.firstName])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"First Name can't be blank."];
        [dialog show];
        return NO;
    }

    if([Utility isEmpty:_accountRequest.lastName])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Last Name can't be blank."];
        [dialog show];
        return NO;
    }

    if([Utility isEmpty:_accountRequest.phone] && [Utility isEmpty:_accountRequest.mobilePhone])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Home Phone or Cell phone must be provided."];
        [dialog show];
        return NO;
    }

    if(![Utility isEmpty:_accountRequest.phone])
    {
        if(_accountRequest.phone.length < 12 ||
           ![[_accountRequest.phone substringWithRange:NSMakeRange(3, 1)] isEqualToString:@"-"] ||
           ![[_accountRequest.phone substringWithRange:NSMakeRange(7, 1)] isEqualToString:@"-"] ||
           [_numberFormatter numberFromString:[_accountRequest.phone substringWithRange:NSMakeRange(0, 3)]] == nil ||
           [_numberFormatter numberFromString:[_accountRequest.phone substringWithRange:NSMakeRange(4, 3)]] == nil ||
           [_numberFormatter numberFromString:[_accountRequest.phone substringWithRange:NSMakeRange(8, 4)]] == nil)
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Home Phone should be in xxx-xxx-xxxx format."];
            [dialog show];
            return NO;
        }
    }

    if(![Utility isEmpty:_accountRequest.mobilePhone])
    {
        if(_accountRequest.mobilePhone.length < 12 ||
           ![[_accountRequest.mobilePhone substringWithRange:NSMakeRange(3, 1)] isEqualToString:@"-"] ||
           ![[_accountRequest.mobilePhone substringWithRange:NSMakeRange(7, 1)] isEqualToString:@"-"] ||
           [_numberFormatter numberFromString:[_accountRequest.mobilePhone substringWithRange:NSMakeRange(0, 3)]] == nil ||
           [_numberFormatter numberFromString:[_accountRequest.mobilePhone substringWithRange:NSMakeRange(4, 3)]] == nil ||
           [_numberFormatter numberFromString:[_accountRequest.mobilePhone substringWithRange:NSMakeRange(8, 4)]] == nil)
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Cell Phone should be in xxx-xxx-xxxx format."];
            [dialog show];
            return NO;
        }
    }

    return YES;
}


- (void)forwardToView3
{
    if([self validateView2] == NO)
        return;

    _view3.hidden = NO;
    _view3Tab.image = _activeViewImage;
    _view2.hidden = YES;
    _view2Tab.image = _inactiveViewImage;
}


- (void)backwardToView3
{
    _view3.hidden = NO;
    _view3Tab.image = _activeViewImage;
    _view4.hidden = YES;
    _view4Tab.image = _inactiveViewImage;
}


- (BOOL)validateView3
{
    _accountRequest.address = _addressTextField.text;
    _accountRequest.city = _cityTextField.text;
    _accountRequest.state = _stateText.text;
    _accountRequest.zip = _zipcodeTextField.text;

    if(_accountRequest.address.length == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Mailing Address can't be blank."];
        [dialog show];
        return NO;
    }

    if(_accountRequest.city.length == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"City can't be blank."];
        [dialog show];
        return NO;
    }

    if([_accountRequest.state isEqualToString:STATE_DEFAULT_TEXT])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"State can't be blank."];
        [dialog show];
        return NO;
    }

    if(_accountRequest.zip.length == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Zip Code can't be blank."];
        [dialog show];
        return NO;
    }

    if(_accountRequest.zip.length != 5 ||
       [_numberFormatter numberFromString:[_accountRequest.zip substringWithRange:NSMakeRange(0, 5)]] == nil)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Zip Code should be a five digit number."];
        [dialog show];
        return NO;
    }

    return YES;
}


- (void)forwardToView4
{
    if([self validateView3] == NO)
        return;

    _view4.hidden = NO;
    _view4Tab.image = _activeViewImage;
    _view3.hidden = YES;
    _view3Tab.image = _inactiveViewImage;
}


- (void)backwardToView4
{
    _page5Header.hidden = YES;
    _commonHeader.hidden = NO;
    _view4.hidden = NO;
    _view4Tab.image = _activeViewImage;
    _view5.hidden = YES;
    _view5Tab.image = _inactiveViewImage;
}


- (BOOL)validateView4
{
    _accountRequest.favoriteDept = _departmentText.text;

    if(_registrationPage == YES)
    {
        if([_storeText.text isEqualToString:STORE_DEFAULT_TEXT])
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Please choose a favorite location."];
            [dialog show];
            return NO;
        }
    }

    if([_accountRequest.favoriteDept isEqualToString:DEPARTMENT_DEFAULT_TEXT])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Please choose a favorite department."];
        [dialog show];
        return NO;
    }

    return YES;
}


- (void)forwardToView5
{
    if([self validateView4] == NO)
        return;

    _commonHeader.hidden = YES;
    _page5Header.hidden = NO;
    _view5.hidden = NO;
    _view5Tab.image = _activeViewImage;
    _view4.hidden = YES;
    _view4Tab.image = _inactiveViewImage;
}


- (void)backwardToView5
{
    _page5Header.hidden = NO;
    _view5.hidden = NO;
    _view5Tab.image = _activeViewImage;
    _view6.hidden = YES;
    _view6Tab.image = _inactiveViewImage;
}


- (BOOL)validateView5
{
    if(_registrationPage == YES)
    {
        _accountRequest.loyaltyNumber = _loyaltyTextField.text;
        NSString *loyaltyConfirmNumber = _loyaltyConfirmTextField.text;

        if(_accountRequest.loyaltyNumber.length == 0)
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Loyalty Number can't be blank"];
            [dialog show];
            return NO;
        }

        if(loyaltyConfirmNumber.length == 0)
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Confirm Loyalty Number can't be blank"];
            [dialog show];
            return NO;
        }

        if(_accountRequest.loyaltyNumber.length != 10 || [_numberFormatter numberFromString:_accountRequest.loyaltyNumber] == nil)
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Loyalty Numbers must contain 10 digits"];
            [dialog show];
            return NO;
        }

        if(![loyaltyConfirmNumber isEqualToString:_accountRequest.loyaltyNumber])
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Loyalty Numbers do not match"];
            [dialog show];
            return NO;
        }
    }
    
    return YES;
}


- (void)forwardToView6
{
    if([self validateView5] == NO)
        return;

    _page5Header.hidden = YES;
    _view6.hidden = NO;
    _view6Tab.image = _activeViewImage;
    _view5.hidden = YES;
    _view5Tab.image = _inactiveViewImage;
}


- (BOOL)validateView6
{
    if(_agreeCheckBox.isSelected == NO)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"You must agree to the Terms and Conditions in order to register."];
        [dialog show];
        return NO;
    }

    return true;
}


- (void)sendRequest
{
    NSString *urlString;
    NSString *auth;

    if(_registrationPage == YES)
    {
        urlString = ACCOUNT_REGISTRATION_URL;
        auth = nil;
        _dialogText = @"Creating Account...";
    }
    else
    {
        urlString = ACCOUNT_UPDATE_URL;
        _dialogText = @"Updating Account...";
        Login *login = [_app getLogin];
        auth = login.authKey;
        _accountRequest.crmNumber = login.crmNumber;
        _accountRequest.accountId = login.accountId;
    }

    _accountRequest.issueCardFlag = [NSNumber numberWithBool:_cardCheckBox.isSelected];
    _accountRequest.termsAcceptedFlag = [NSNumber numberWithBool:_agreeCheckBox.isSelected];
    _accountRequest.sendEmailsFlag = [NSNumber numberWithBool:_emailCheckBox.isSelected];
    _accountRequest.sendTextsFlag = [NSNumber numberWithBool:_textCheckBox.isSelected];
    _accountRequest.issueCardFlag = [NSNumber numberWithBool:_cardCheckBox.isSelected];

    _progressDialog = [[ProgressDialog alloc] initWithView:View message:_dialogText];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"Login"];
    [_service execute:urlString authorization:auth requestObject:_accountRequest requestType:POST]; // response handled by handleAccountServiceResponse method below
}


- (void)handleAccountServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];

    if(status == 200) // service success
    {
        if(_registrationPage == NO)
            _dialogText = @"Account Updated";
        else
            _dialogText = @"Account Created";

        _textDialog = [[TextDialog alloc] initWithView:View title:@"Request Succeeded" message:_dialogText responder:self callback:@selector(removeView)];
        [_textDialog show];
    }
    else // service failure
    {
        if(status == 422) // backend or internet unavailable error
        {
            WebServiceError *error = [_service getError];

            NSString *titleText;

            if(_registrationPage == NO)
                titleText = @"Account Update Failed";
            else
                titleText = @"Account Create Failed";

            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:titleText message:error.errorMessage];
            [dialog show];
        }
        else if(status == 408) // timeout error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
            [dialog show];
        }
        else // http error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:COMMON_ERROR_MSG]];
            [dialog show];
        }
    }

    _service = nil;
}


- (void)removeView
{
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [_textDialog close];
    [self.presentingViewController dismissViewControllerAnimated:NO completion:nil];
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// text view delegate begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark UITextField Delegate
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    if(textField == _firstNameTextField || textField == _lastNameTextField || textField == _homePhoneTextField || textField == _cellPhoneTextField)
        ; //_salutationTable.hidden = YES;
    else if(textField == _addressTextField || textField == _cityTextField || textField == _zipcodeTextField)
        _stateTable.hidden = YES;

    return YES;

}


- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if(textField == _emailTextField)
    {
        [_emailTextField resignFirstResponder];
        [_passwordTextField becomeFirstResponder];
    }
    else if(textField == _passwordTextField)
    {
        [_passwordTextField resignFirstResponder];
        [_confirmPasswordTextField becomeFirstResponder];
    }
    else if(textField == _confirmPasswordTextField)
    {
        [_confirmPasswordTextField resignFirstResponder];
    }
    else if(textField == _firstNameTextField)
    {
        [_firstNameTextField resignFirstResponder];
        [_lastNameTextField becomeFirstResponder];
    }
    else if(textField == _lastNameTextField)
    {
        [_lastNameTextField resignFirstResponder];
        [_homePhoneTextField becomeFirstResponder];
    }
    else if(textField == _homePhoneTextField)
    {
        [_homePhoneTextField resignFirstResponder];
        [_cellPhoneTextField becomeFirstResponder];
    }
    else if(textField == _cellPhoneTextField)
    {
        [_cellPhoneTextField resignFirstResponder];
    }
    else if(textField == _addressTextField)
    {
        [_addressTextField resignFirstResponder];
        [_cityTextField becomeFirstResponder];
    }
    else if(textField == _cityTextField)
    {
        [_cityTextField resignFirstResponder];
        [_zipcodeTextField becomeFirstResponder];
    }
    else if(textField == _zipcodeTextField)
    {
        [_zipcodeTextField resignFirstResponder];
    }
    else if(textField == _loyaltyTextField)
    {
        [_loyaltyTextField resignFirstResponder];
        [_loyaltyConfirmTextField becomeFirstResponder];
    }
    else if(textField == _loyaltyConfirmTextField)
    {
        [_loyaltyConfirmTextField resignFirstResponder];
    }

    return YES;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// text view delegate end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// table view delegate begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - UITableView
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(tableView == _stateTable)
    {
        return _states.count;
    }
    else if(tableView == _departmentTable)
    {
        return _departments.count;
    }

    return 0;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"AccountTableItem";

    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];

    if (cell == nil)
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];

    cell.backgroundColor = [UIColor clearColor];
    UIImageView *background = [[UIImageView alloc] initWithFrame:cell.frame];
    background.image = [UIImage imageNamed:@"account_cell_medium"];
    [cell setBackgroundView:background];
    cell.textLabel.font = [Utility fontForFamily:_app._normalFont andHeight:_tableCellHeight * .7];
    cell.textLabel.textColor = _textColor;

    if(tableView == _stateTable)
    {
        cell.textLabel.text = [_states objectAtIndex:indexPath.row];
        return cell;
    }
    else if(tableView == _departmentTable)
    {
        cell.textLabel.text = [_departments objectAtIndex:indexPath.row];
        return cell;
    }
    else
        return nil;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath;
{
    return _tableCellHeight;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView == _stateTable)
    {
        _stateTable.hidden = YES;
        _stateText.textColor = _textColor;
        _stateText.text = [_states objectAtIndex:indexPath.row];
        [_stateText setNeedsDisplay];
    }
    else if(tableView == _departmentTable)
    {
        _departmentTable.hidden = YES;
        _departmentText.textColor = _textColor;
        _departmentText.text = [_departments objectAtIndex:indexPath.row];
        [_departmentText setNeedsDisplay];
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// table view delegate end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



#pragma mark WebServiceListener Delegate
- (void)onServiceResponse:(id)responseObject
{
    [_progressDialog dismiss];

    if([responseObject isKindOfClass:[Login class]])
        [self handleAccountServiceResponse:responseObject];
}


@end
