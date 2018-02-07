//
//  EcartScreenVC.m
//  Raley's
//
//  Created by Billy Lewis on 3/13/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "EcartScreenVC.h"
#import "EcartConfirmView.h"
#import "EcartAppointment.h"
#import "EcartOrderRequest.h"
#import "StoreLocatorScreenVC.h"

#import "UIImage+scaleToSize.h"
#import "Utility.h"
#import "Logging.h"

#define SUB_PREF_1 @"at Personal Shopper's discretion"
#define SUB_PREF_2 @"with different size but same brand"
#define SUB_PREF_3 @"with same size but different brand"
#define SUB_PREF_4 @"with similar item that has Extra Points"
#define SUB_PREF_5 @"Don't substitute. Omit if unavailable"
#define BAG_PREF_1 @"Use paper bags.*"
#define BAG_PREF_2 @"Use reusable bags"

#define VAL_SUB_PREF_1 1
#define VAL_SUB_PREF_2 2
#define VAL_SUB_PREF_3 3
#define VAL_SUB_PREF_4 4
#define VAL_SUB_PREF_5 5
#define VAL_BAG_PREF_1 1
#define VAL_BAG_PREF_2 2

 
#define font_size17 17
#define font_size13 13
#define font_size10 10

#define content_height 680
#define between_gap 5
#define DefaultPlaceholder @"Personal Shopper Instructions"
// 560

@implementation EcartScreenVC


- (void)viewDidLoad
{
    [super viewDidLoad];
    @try {
     

    _app = (id)[[UIApplication sharedApplication] delegate];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWasShown:) name:UIKeyboardDidShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillBeHidden:) name:UIKeyboardWillHideNotification object:nil];

    _appointmentDays = [[NSMutableArray alloc] init];
    _appointmentTimes = [[NSMutableArray alloc] init];
        

    for(EcartAppointment *appointment in _app._currentAppointmentList)
    {
        if(![Utility isEmpty:appointment.appointmentTimeList])
            [_appointmentDays addObject:appointment.appointmentDate];
    }
        

    _normalFont = _app._normalFont;
    _boldFont = _app._boldFont;
    _backButton.hidden = NO;
    
    _contentView.frame = CGRectMake(0, _app._headerHeight, _app._viewWidth, _app._viewHeight - _app._headerHeight - _app._headerHeight);
    _contentView.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0];
        
    _bg_picker_view = [[UIView alloc]init];
    [_bg_picker_view setUserInteractionEnabled:YES];
    [_bg_picker_view setBackgroundColor:[UIColor colorWithRed:0 green:0 blue:0 alpha:0.3]];
        UITapGestureRecognizer *single_tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(dismissPickerView)];
        [_bg_picker_view addGestureRecognizer:single_tap];
        [_bg_picker_view setHidden:YES];
    
    // need a scroll view for instruction text at the bottom of this screen
    _scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, _contentViewWidth, _contentViewHeight+_navigationBarHeight)];
    _scrollView.backgroundColor = [UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0];
        //colorWithRed:0.9 green:0.9 blue:.9 alpha:1.0
        [_bg_picker_view setFrame:_scrollView.frame];
    _scrollView.contentSize = CGSizeMake(_contentViewWidth,  content_height);
    [_scrollView setScrollEnabled:YES];
    [_scrollView setBounces:YES];
        [_scrollView setShowsHorizontalScrollIndicator:NO];
        [_scrollView setShowsVerticalScrollIndicator:NO];
        
        
        CGSize total_content_height=_scrollView.contentSize;
    

        CGRect bg_frame=_scrollView.frame;
        bg_frame.origin.x=10;
        bg_frame.origin.y=10;
        bg_frame.size.width=bg_frame.size.width-20;
        bg_frame.size.height=total_content_height.height-20;
        
        bg_container=[[UIView alloc]initWithFrame:bg_frame];
        [bg_container setBackgroundColor:[UIColor whiteColor]];
        
        // drop shadow
        [bg_container.layer setShadowColor:[UIColor darkGrayColor].CGColor];
        [bg_container.layer setShadowOpacity:0.6];
        [bg_container.layer setShadowRadius:0.3];
        [bg_container.layer setShadowOffset:CGSizeMake(0.0,0.6)];
        [bg_container.layer setCornerRadius:5.0f];
        
        
    [_scrollView addSubview:bg_container];
        
    [_contentView addSubview:_scrollView];
//    [_contentView insertSubview:bg_container aboveSubview:_scrollView];
    [_contentView addSubview:_bg_picker_view];

    [self setFooterDetails];
    
    [self chooseAppointmentDay];
    [self chooseAppointmentTime];

    _ecartOrderRequest = [[EcartOrderRequest alloc] init];
    _textColor = [UIColor darkGrayColor]; // [UIColor colorWithRed:0.35 green:0.23 blue:.08 alpha:1.0];
    _hintColor = [UIColor darkGrayColor]; //[UIColor colorWithRed:0.63 green:0.53 blue:.38 alpha:1.0];
    _checkedBitmap = [UIImage imageNamed:@"product_checked_box"]; // account_checked_box
    _uncheckedBitmap = [UIImage imageNamed:@"product_unchecked_box"];  // account_unchecked_box
    _substitutionPreference = SUB_PREF_1;
    _bagPreference = BAG_PREF_1;
    _substitutionPreferenceValue = VAL_SUB_PREF_1;
    _bagPreferenceValue = VAL_BAG_PREF_1;
    _appointmentDay = @"";
    _appointmentTime = @"";
//
//    int editWidth = _contentViewWidth * .95;
//    int editXOrigin = _contentViewWidth * .025;
        
    int editWidth = bg_container.frame.size.width * .95;
    int editXOrigin = bg_container.frame.size.width * .025;
        
    int checkBoxSize = content_height * .045;
    int checkBoxTextWidth = editWidth - checkBoxSize - (editXOrigin * 1); // *2
    int checkBoxTextXOrigin = checkBoxSize + (editXOrigin * 2);
    int checkBoxVerticalPad = content_height * .01;
    _tableCellHeight = content_height * .055;

    UILabel *headerText = [[UILabel alloc] initWithFrame:CGRectMake(editXOrigin, 3, editWidth, 25)];
    headerText.numberOfLines = 0;
    [headerText setFont:[UIFont fontWithName:_boldFont size:font_size13]];
    headerText.textColor = _textColor;
    headerText.textAlignment = NSTextAlignmentCenter;
    headerText.text = @"Welcome to E-cart!";
    [bg_container addSubview:headerText];

    Login *login = [_app getLogin];
    NSString *storeNameText;
    NSString *storeAddressText;

    if(![Utility isEmpty:_app._allStoresList])
    {
        Store *store = [_app._allStoresList objectForKey:[NSString stringWithFormat:@"%d", login.storeNumber]];
        storeNameText = store.chain;
        storeAddressText = [NSString stringWithFormat:@"%@, %@, %@, %@", store.address, store.city, store.state, store.zip];
    }
    else // this shouldn't happen, but just in case
    {
        storeNameText = [NSString stringWithFormat:@"%d", login.storeNumber];
        storeAddressText = [NSString stringWithFormat:@"Store %d, additional details unavailable at this time", login.storeNumber];
    }

    UILabel *locationPromptText = [[UILabel alloc] initWithFrame:CGRectMake(editXOrigin, content_height * .07, editWidth, 20)];
    [Utility updateChildOriginY_Parent:headerText _Child:locationPromptText _Gap:between_gap];
    locationPromptText.numberOfLines = 1;
    locationPromptText.textColor = _textColor;
    locationPromptText.textAlignment = NSTextAlignmentCenter;
    locationPromptText.text = [NSString stringWithFormat:@"You are ordering at: %@", storeNameText];
    [locationPromptText setFont:[UIFont fontWithName:_boldFont size:font_size13]];
    [bg_container addSubview:locationPromptText];
    
    CGSize new_size = [Utility getUILabelFontSizeBasedOnText_width:editWidth _fontname:_normalFont _fontsize:font_size13 _text:storeAddressText];
    UILabel *locationText = [[UILabel alloc] initWithFrame:CGRectMake(editXOrigin, content_height * .2, editWidth, new_size.height)];
    [Utility updateChildOriginY_Parent:locationPromptText _Child:locationText _Gap:between_gap];
    locationText.numberOfLines = 0;
    locationText.textColor = _textColor;
    locationText.textAlignment = NSTextAlignmentCenter;
    locationText.text = storeAddressText;
    [locationText setFont:[UIFont fontWithName:_normalFont size:font_size13]];
    [bg_container addSubview:locationText];

        
        //------------------------------------------------------------------------------------------
        // pickup date section begin. NOTE: add this last so it's list covers other fields when exposed
        //------------------------------------------------------------------------------------------
        int pickupDateYOrigin = 0;
        
//        UILabel *pickupDatePromptText = [[UILabel alloc] initWithFrame:CGRectMake(editXOrigin, pickupDateYOrigin, editWidth, 28)];
//        [Utility updateChildOriginY_Parent:locationText _Child:pickupDatePromptText _Gap:between_gap];
//        pickupDateYOrigin = pickupDatePromptText.frame.origin.y ;
//        pickupDatePromptText.numberOfLines = 1;
//        [pickupDatePromptText setFont:[UIFont fontWithName:_boldFont size:font_size13]];
//        pickupDatePromptText.textColor = _textColor;
//        pickupDatePromptText.textAlignment = NSTextAlignmentCenter;
//        pickupDatePromptText.text = [NSString stringWithFormat:@"Pickup date and time"];
//        [_scrollView addSubview:pickupDatePromptText];
        
//        int tablesYOrigin = pickupDateYOrigin + pickupDatePromptText.frame.size.height;
        
        pickupDateYOrigin = locationText.frame.origin.y + (between_gap*2);
        int tablesYOrigin = (pickupDateYOrigin + locationText.frame.size.height)+10;
//        int tablesTextSize = _tableCellHeight * .7;
//        int tablesTextYOrigin = tablesYOrigin + (_tableCellHeight - tablesTextSize) / 2;
        int dayWidth = editWidth * .40;
        int timeWidth = editWidth * .60;
        int timeXOrigin = dayWidth + (editXOrigin * 1.5);
//        int buttonSize = _tableCellHeight;
        
        
        int extr_width=24;
        
        _appointmentDayText = [[UILabel alloc] initWithFrame:CGRectMake(editXOrigin, tablesYOrigin, dayWidth - extr_width, 30)];
        _appointmentDayText.numberOfLines = 1;
        [_appointmentDayText setFont:[UIFont fontWithName:_normalFont size:font_size13]];
        _appointmentDayText.textColor = _hintColor;
        _appointmentDayText.textAlignment = NSTextAlignmentCenter;
        _appointmentDayText.text = @"Pickup Date";
        [_appointmentDayText.layer setCornerRadius:5.0f];
        [_appointmentDayText.layer setBorderWidth:0.7f];
//        _appointmentDay=[NSString stringWithFormat:@"%@",[_appointmentDays objectAtIndex:0]];
//        _appointmentDayText.text =_appointmentDay;
        [_appointmentDayText setUserInteractionEnabled:YES];
        [_appointmentDayText.layer setBorderColor:[UIColor darkGrayColor].CGColor];
        
        UITapGestureRecognizer *pickup_date_gesture=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(Day_label_Gesture_functions)];
        pickup_date_gesture.numberOfTapsRequired=1;
        pickup_date_gesture.numberOfTouchesRequired=1;
        [_appointmentDayText addGestureRecognizer:pickup_date_gesture];
        [bg_container addSubview:_appointmentDayText];

        
        _appointmentDayButton = [[UIButton alloc] initWithFrame:CGRectMake(editXOrigin + dayWidth - 27, tablesYOrigin, 25, 30)];
        [_appointmentDayButton setBackgroundImage:[UIImage imageNamed:@"account_updown_button"] forState:UIControlStateNormal];
        [_appointmentDayButton addTarget:self action:@selector(chooseAppointmentDay) forControlEvents:UIControlEventTouchDown];
        
        [bg_container addSubview:_appointmentDayButton];
        
        
        _appointmentTimeText = [[UILabel alloc] initWithFrame:CGRectMake(timeXOrigin, tablesYOrigin, timeWidth - extr_width, 30)];
        _appointmentTimeText.numberOfLines = 1;
        _appointmentTimeText.font = [UIFont fontWithName:_normalFont size:font_size13];
        _appointmentTimeText.textColor = _hintColor;
        _appointmentTimeText.textAlignment = NSTextAlignmentCenter;
        _appointmentTimeText.text = @"Pickup Time";
        @try {
            
            for(EcartAppointment *appointment in _app._currentAppointmentList)
            {
                if([appointment.appointmentDate compare:[_appointmentDays objectAtIndex:0]] == 0)
                {
                    [_appointmentTimes removeAllObjects];
                    
                    if(![Utility isEmpty:appointment.appointmentTimeList])
                        [_appointmentTimes addObjectsFromArray:appointment.appointmentTimeList];
                    break;
                }
            }
//            _appointmentTime=[NSString stringWithFormat:@"%@",[_appointmentTimes objectAtIndex:0]];
//            _appointmentTimeText.text =_appointmentTime;
        }
        @catch (NSException *exception) {
            NSLog(@"%@",exception.reason);
        }
        
        
        [_appointmentTimeText.layer setCornerRadius:5.0f];
        [_appointmentTimeText.layer setBorderWidth:0.7f];
        [_appointmentTimeText setUserInteractionEnabled:YES];
        [_appointmentTimeText.layer setBorderColor:[UIColor darkGrayColor].CGColor];
        
        UITapGestureRecognizer *pickup_time_gesture=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(Time_label_Gesture_functions)];
        pickup_time_gesture.numberOfTapsRequired=1;
        pickup_time_gesture.numberOfTouchesRequired=1;
        [_appointmentTimeText addGestureRecognizer:pickup_time_gesture];
        [bg_container addSubview:_appointmentTimeText];
        
        _appointmentTimeButton = [[UIButton alloc] initWithFrame:CGRectMake(timeXOrigin + timeWidth - 27, tablesYOrigin, 25, 30)];
        [_appointmentTimeButton setBackgroundImage:[UIImage imageNamed:@"account_updown_button"] forState:UIControlStateNormal];
        [_appointmentTimeButton addTarget:self action:@selector(chooseAppointmentTime) forControlEvents:UIControlEventTouchDown];
        [bg_container addSubview:_appointmentTimeButton];
        
        //------------------------------------------------------------------------------------------
        // pickup date section end
        //------------------------------------------------------------------------------------------
        
        //------------------------------------------------------------------------------------------
        // instructions section begin
        //------------------------------------------------------------------------------------------
        int instructionsYOrigin = _appointmentDayText.frame.origin.y + _appointmentDayText.frame.size.height + between_gap;
        
        
//        Text_lbl = [[UILabel alloc] initWithFrame:CGRectMake(editXOrigin+5, instructionsYOrigin+15, editWidth, 50)];
//        Text_lbl.numberOfLines = 5;
//        Text_lbl.font = [UIFont fontWithName:_normalFont size:font_size13];
//        Text_lbl.textColor = _textColor;
//        Text_lbl.textAlignment = NSTextAlignmentLeft;
//        Text_lbl.backgroundColor = [UIColor clearColor];
        
        _instructionsTextView = [[UITextView alloc] initWithFrame:CGRectMake(editXOrigin, instructionsYOrigin+10, editWidth, 50)];
        _instructionsTextView.delegate = self;
        _instructionsTextView.text=DefaultPlaceholder;
        _instructionsTextView.keyboardType = UIKeyboardTypeAlphabet;
        _instructionsTextView.returnKeyType = UIReturnKeyDone;
        _instructionsTextView.backgroundColor = [UIColor whiteColor];
        _instructionsTextView.textColor = _textColor;

        [_instructionsTextView setFont:[UIFont fontWithName:_normalFont size:font_size13]];
        [_instructionsTextView.layer setCornerRadius:5.0f];
        [_instructionsTextView.layer setBorderWidth:0.7f];
               [_instructionsTextView setFont:[UIFont fontWithName:_app._normalFont size:font_size13]];
        [_instructionsTextView.layer setBorderColor:[UIColor darkGrayColor].CGColor];
        _instructionsTextView.autocapitalizationType = UITextAutocapitalizationTypeNone;
        
//        [_instructionsTextView addSubview:Text_lbl];
        
//       UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, _instructionsTextView.frame.size.height)];
//        leftView.backgroundColor = _instructionsTextView.backgroundColor;
//        _instructionsTextView.leftView = leftView;
//        _instructionsTextView.leftViewMode = UITextFieldViewModeAlways;
        
        [bg_container addSubview:_instructionsTextView];
        //------------------------------------------------------------------------------------------
        // instructions section end
        //------------------------------------------------------------------------------------------
        
        //------------------------------------------------------------------------------------------
        // submit button
        //------------------------------------------------------------------------------------------
        
        int _submitButtonYOrigin = _instructionsTextView.frame.origin.y + _instructionsTextView.frame.size.height + between_gap;
        
        int submitButtonWidth = 100;
        int submitButtonHeight = 35;
        
        int content_view_right_left_gap=20;
        
        _submitButton = [[UIButton alloc] initWithFrame:CGRectMake((_contentViewWidth - submitButtonWidth-content_view_right_left_gap)/ 2,_submitButtonYOrigin, submitButtonWidth, submitButtonHeight)];
        [Utility updateChildOriginY_Parent:_instructionsTextView _Child:_submitButton _Gap:(between_gap*3)];
        //    [_submitButton setBackgroundImage:[UIImage imageNamed:@"account_submit_button"] forState:UIControlStateNormal];
        
        [_submitButton setBackgroundColor:[UIColor colorWithRed:187.0/255.0 green:0.0 blue:0.0 alpha:1.0]];
        //    [_submitButton.titleLabel setFont:[Utility fontForFamily:_app._boldFont andHeight:font_size17]];
        
        [_submitButton.titleLabel setFont:[UIFont fontWithName:_app._boldFont size:font_size13]];
        [_submitButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_submitButton setTitle:@"Submit" forState:UIControlStateNormal];
        [_submitButton addTarget:self action:@selector(confirmOrder) forControlEvents:UIControlEventTouchUpInside];
        [_submitButton.layer setCornerRadius:5.0f];
        [bg_container addSubview:_submitButton];

        

    //------------------------------------------------------------------------------------------
    // substitution preference section begin
    //------------------------------------------------------------------------------------------
   int substitutePreferenceYOrigin = _instructionsTextView.frame.origin.y + _instructionsTextView.frame.size.height + 10 + _submitButton.frame.size.height + between_gap*2;

    UILabel *substitutePreferencePromptText = [[UILabel alloc] initWithFrame:CGRectMake(editXOrigin, substitutePreferenceYOrigin, editWidth, 34)];
    substitutePreferencePromptText.numberOfLines = 1;
    substitutePreferencePromptText.textColor = _textColor;
    substitutePreferencePromptText.textAlignment = NSTextAlignmentCenter;
    substitutePreferencePromptText.text = [NSString stringWithFormat:@"Substitute any item"];
    [substitutePreferencePromptText setFont:[UIFont fontWithName:_boldFont size:font_size13]];
        
        // View for-  Border top only
        CGSize mainViewSize = substitutePreferencePromptText.bounds.size;
        CGFloat borderHeight = 1;
        UIColor *borderColor = [UIColor lightGrayColor];
        UIView *topView = [[UIView alloc] initWithFrame:CGRectMake(0.0,mainViewSize.height-borderHeight, mainViewSize.width, borderHeight)];
        topView.opaque = YES;
        topView.alpha=0.5f;
        topView.backgroundColor = borderColor;
        topView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleBottomMargin;
        [substitutePreferencePromptText addSubview:topView];
        
    
        
    [bg_container addSubview:substitutePreferencePromptText];
   
    // shopper discretion line
    int shopperDiscretionYOrigin = substitutePreferenceYOrigin + substitutePreferencePromptText.frame.size.height + between_gap;

    _shopperDiscretionCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(editXOrigin-5, shopperDiscretionYOrigin-5, checkBoxSize+10, checkBoxSize+10)];
    [_shopperDiscretionCheckBox setImage:_uncheckedBitmap forState:UIControlStateNormal];
    [_shopperDiscretionCheckBox setImage:_checkedBitmap forState:UIControlStateSelected];
    [_shopperDiscretionCheckBox setContentEdgeInsets:UIEdgeInsetsMake(5, 5, 5, 5)];//outer touch area
    [_shopperDiscretionCheckBox addTarget:self action:@selector(toggleShopperDiscretionCheckBox) forControlEvents:UIControlEventTouchUpInside];
    [_shopperDiscretionCheckBox setSelected:YES];
    [bg_container addSubview:_shopperDiscretionCheckBox];

    UILabel *shopperDiscretionText = [[UILabel alloc] initWithFrame:CGRectMake(checkBoxTextXOrigin, shopperDiscretionYOrigin, checkBoxTextWidth, checkBoxSize)];
    shopperDiscretionText.numberOfLines = 1;
    [shopperDiscretionText setFont:[UIFont fontWithName:_normalFont size:font_size13]];
    shopperDiscretionText.textColor = _textColor;
    shopperDiscretionText.textAlignment = NSTextAlignmentLeft;
    shopperDiscretionText.text = [NSString stringWithFormat:SUB_PREF_1];
    [bg_container addSubview:shopperDiscretionText];

    // different size line
    int differentSizeYOrigin = shopperDiscretionYOrigin + checkBoxSize + checkBoxVerticalPad +between_gap;

    _differentSizeCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(editXOrigin-5, differentSizeYOrigin-5, checkBoxSize+10, checkBoxSize+10)];
    [_differentSizeCheckBox setImage:_uncheckedBitmap forState:UIControlStateNormal];
    [_differentSizeCheckBox setImage:_checkedBitmap forState:UIControlStateSelected];
    [_differentSizeCheckBox setContentEdgeInsets:UIEdgeInsetsMake(5, 5, 5, 5)];//outer touch area
    [_differentSizeCheckBox addTarget:self action:@selector(toggleDifferentSizeCheckBox) forControlEvents:UIControlEventTouchUpInside];
    [bg_container addSubview:_differentSizeCheckBox];

    UILabel *differentSizeText = [[UILabel alloc] initWithFrame:CGRectMake(checkBoxTextXOrigin, differentSizeYOrigin, checkBoxTextWidth, checkBoxSize)];
    differentSizeText.numberOfLines = 1;
    [differentSizeText setFont:[UIFont fontWithName:_normalFont size:font_size13]];
    differentSizeText.textColor = _textColor;
    differentSizeText.textAlignment = NSTextAlignmentLeft;
    differentSizeText.text = [NSString stringWithFormat:SUB_PREF_2];
    [bg_container addSubview:differentSizeText];

    // same size line
    int sameSizeYOrigin = differentSizeYOrigin + checkBoxSize + checkBoxVerticalPad +between_gap;

    _sameSizeCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(editXOrigin-5, sameSizeYOrigin-5, checkBoxSize+10, checkBoxSize+10)];
    [_sameSizeCheckBox setImage:_uncheckedBitmap forState:UIControlStateNormal];
    [_sameSizeCheckBox setImage:_checkedBitmap forState:UIControlStateSelected];
    [_sameSizeCheckBox setContentEdgeInsets:UIEdgeInsetsMake(5, 5, 5, 5)];//outer touch area
    [_sameSizeCheckBox addTarget:self action:@selector(toggleSameSizeCheckBox) forControlEvents:UIControlEventTouchUpInside];
    [bg_container addSubview:_sameSizeCheckBox];

    UILabel *sameSizeText = [[UILabel alloc] initWithFrame:CGRectMake(checkBoxTextXOrigin, sameSizeYOrigin, checkBoxTextWidth, checkBoxSize)];
    sameSizeText.numberOfLines = 1;
    [sameSizeText setFont:[UIFont fontWithName:_normalFont size:font_size13]];
    sameSizeText.textColor = _textColor;
    sameSizeText.textAlignment = NSTextAlignmentLeft;
    sameSizeText.text = [NSString stringWithFormat:SUB_PREF_3];
    [bg_container addSubview:sameSizeText];

    // same size line
    int similarItemYOrigin = sameSizeYOrigin + checkBoxSize + checkBoxVerticalPad + between_gap;

    _similarItemCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(editXOrigin-5, similarItemYOrigin-5, checkBoxSize+10, checkBoxSize+10)];
    [_similarItemCheckBox setImage:_uncheckedBitmap forState:UIControlStateNormal];
    [_similarItemCheckBox setImage:_checkedBitmap forState:UIControlStateSelected];
    [_similarItemCheckBox setContentEdgeInsets:UIEdgeInsetsMake(5, 5, 5, 5)];//outer touch area
    [_similarItemCheckBox addTarget:self action:@selector(toggleSimilarItemCheckBox) forControlEvents:UIControlEventTouchUpInside];
    [bg_container addSubview:_similarItemCheckBox];

    UILabel *similarItemText = [[UILabel alloc] initWithFrame:CGRectMake(checkBoxTextXOrigin, similarItemYOrigin, checkBoxTextWidth+3, checkBoxSize)];
    similarItemText.numberOfLines = 1;
    [similarItemText setFont:[UIFont fontWithName:_normalFont size:font_size13]];
    similarItemText.textColor = _textColor;
    similarItemText.textAlignment = NSTextAlignmentLeft;
    similarItemText.text = [NSString stringWithFormat:SUB_PREF_4];
    [similarItemText setBackgroundColor:[UIColor clearColor]];
    [bg_container addSubview:similarItemText];

    // no substitution line
    int noSubstitutionYOrigin = similarItemYOrigin + checkBoxSize + checkBoxVerticalPad + between_gap;

    _noSubstitutionCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(editXOrigin-5, noSubstitutionYOrigin-5, checkBoxSize+10, checkBoxSize+10)];
    [_noSubstitutionCheckBox setImage:_uncheckedBitmap forState:UIControlStateNormal];
    [_noSubstitutionCheckBox setImage:_checkedBitmap forState:UIControlStateSelected];
    [_noSubstitutionCheckBox setContentEdgeInsets:UIEdgeInsetsMake(5, 5, 5, 5)];//outer touch area
    [_noSubstitutionCheckBox addTarget:self action:@selector(toggleNoSubstitutionCheckBox) forControlEvents:UIControlEventTouchUpInside];
    [bg_container addSubview:_noSubstitutionCheckBox];

    UILabel *noSubstitutionText = [[UILabel alloc] initWithFrame:CGRectMake(checkBoxTextXOrigin, noSubstitutionYOrigin, checkBoxTextWidth, checkBoxSize)];
    noSubstitutionText.numberOfLines = 1;
    [noSubstitutionText setFont:[UIFont fontWithName:_normalFont size:font_size13]];
    noSubstitutionText.textColor = _textColor;
    noSubstitutionText.textAlignment = NSTextAlignmentLeft;
    noSubstitutionText.text = [NSString stringWithFormat:SUB_PREF_5];
    [bg_container addSubview:noSubstitutionText];
    //------------------------------------------------------------------------------------------
    // substitution preference section end
    //------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------
    // bag preference section begin
    //------------------------------------------------------------------------------------------
    int bagPreferenceYOrigin = noSubstitutionText.frame.origin.y + noSubstitutionText.frame.size.height + between_gap;
    
    UILabel *bagPreferencePromptText = [[UILabel alloc] initWithFrame:CGRectMake(editXOrigin, bagPreferenceYOrigin, editWidth, 34)]; // 28
    bagPreferencePromptText.numberOfLines = 1;
    [bagPreferencePromptText setFont:[UIFont fontWithName:_boldFont size:font_size13]];
    bagPreferencePromptText.textColor = _textColor;
    bagPreferencePromptText.textAlignment = NSTextAlignmentCenter;
    bagPreferencePromptText.text = [NSString stringWithFormat:@"Specify bag preference"];
        
        
        // View for-  Border top only
        mainViewSize = bagPreferencePromptText.bounds.size;
        borderHeight = 1;
        borderColor = [UIColor lightGrayColor];
        topView = [[UIView alloc] initWithFrame:CGRectMake(0.0,mainViewSize.height-borderHeight, mainViewSize.width, borderHeight)];
        topView.opaque = YES;
        topView.alpha=0.5f;
        topView.backgroundColor = borderColor;
        topView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleBottomMargin;
        [bagPreferencePromptText addSubview:topView];
        
    [bg_container addSubview:bagPreferencePromptText];

    // paper bag line
    int paperBagYOrigin = bagPreferenceYOrigin + bagPreferencePromptText.frame.size.height + between_gap;

    _paperBagCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(editXOrigin-5, paperBagYOrigin-5, checkBoxSize+10, checkBoxSize+10)];
    [_paperBagCheckBox setImage:_uncheckedBitmap forState:UIControlStateNormal];
    [_paperBagCheckBox setImage:_checkedBitmap forState:UIControlStateSelected];
    [_paperBagCheckBox setContentEdgeInsets:UIEdgeInsetsMake(5, 5, 5, 5)];//outer touch area
    [_paperBagCheckBox addTarget:self action:@selector(togglePaperBagCheckBox) forControlEvents:UIControlEventTouchUpInside];
    [_paperBagCheckBox setSelected:YES];
    [bg_container addSubview:_paperBagCheckBox];

    UILabel *paperBagText = [[UILabel alloc] initWithFrame:CGRectMake(checkBoxTextXOrigin, paperBagYOrigin, checkBoxTextWidth, checkBoxSize)];
    paperBagText.numberOfLines = 1;
    [paperBagText setFont:[UIFont fontWithName:_normalFont size:font_size13]];
    paperBagText.textColor = _textColor;
    paperBagText.textAlignment = NSTextAlignmentLeft;
    paperBagText.text = [NSString stringWithFormat:BAG_PREF_1];
    [bg_container addSubview:paperBagText];

    // reusable bag line
    int reusableBagYOrigin = paperBagYOrigin +  + checkBoxSize + checkBoxVerticalPad + between_gap;

    _reusableBagCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(editXOrigin-5, reusableBagYOrigin-5, checkBoxSize+10, checkBoxSize+10)];
    [_reusableBagCheckBox setImage:_uncheckedBitmap forState:UIControlStateNormal];
    [_reusableBagCheckBox setImage:_checkedBitmap forState:UIControlStateSelected];
    [_reusableBagCheckBox setContentEdgeInsets:UIEdgeInsetsMake(5, 5, 5, 5)];//outer touch area
    [_reusableBagCheckBox addTarget:self action:@selector(toggleReusableBagCheckBox) forControlEvents:UIControlEventTouchUpInside];
    [bg_container addSubview:_reusableBagCheckBox];

    UILabel *reusableBagText = [[UILabel alloc] initWithFrame:CGRectMake(checkBoxTextXOrigin, reusableBagYOrigin, checkBoxTextWidth, checkBoxSize)];
    reusableBagText.numberOfLines = 1;
    [reusableBagText setFont:[UIFont fontWithName:_normalFont size:font_size13]];
    reusableBagText.textColor = _textColor;
    reusableBagText.textAlignment = NSTextAlignmentLeft;
    reusableBagText.text = [NSString stringWithFormat:BAG_PREF_2];
    [bg_container addSubview:reusableBagText];

    UILabel *bagDisclosurePromptText = [[UILabel alloc] initWithFrame:CGRectMake(editXOrigin, reusableBagYOrigin + paperBagText.frame.size.height+10, editWidth, 30)];
    bagDisclosurePromptText.numberOfLines = 2;
    [bagDisclosurePromptText setFont:[UIFont fontWithName:_normalFont size:font_size10]];
    bagDisclosurePromptText.textColor = _textColor;
    bagDisclosurePromptText.textAlignment = NSTextAlignmentLeft;
    bagDisclosurePromptText.text = [NSString stringWithFormat:@"*Based on local ordinances, you may be charged per bag."];
    [bg_container addSubview:bagDisclosurePromptText];
    //------------------------------------------------------------------------------------------
    // bag preference section end
    //------------------------------------------------------------------------------------------

//    _scrollView.contentSize = CGSizeMake(_contentViewWidth,  content_height);
//        
//         total_content_height=_scrollView.contentSize;
//        
//        
//        bg_frame=_scrollView.frame;
//        bg_frame.size.height=total_content_height.height-20;
//        
//        [bg_container setFrame:bg_frame];
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]
                                       initWithTarget:self
                                       action:@selector(dismissKeyboard)];
        
        [self.view addGestureRecognizer:tap];
    }
    @catch (NSException *exception) {
        
    }
}
-(void)dismissKeyboard {
    [self.view endEditing:YES];
}
-(void)Day_label_Gesture_functions{
    [self.view endEditing:YES];
    [self showPicker:_appointmentDayPicker];
}
-(void)Time_label_Gesture_functions{
    [self.view endEditing:YES];
    [self showPicker:_appointmentTimePicker];
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
}


- (void)showSubmitButton
{
    _submitButton.hidden = NO;
}


- (void)chooseAppointmentDay
{
    @try {
    if(_appointmentDayPicker==nil){
        CGRect frame = CGRectZero;
        frame.size.height=200;
        frame.origin.x = 0;
        frame.origin.y = _bg_picker_view.frame.size.height - frame.size.height;
        frame.size.width = _contentViewWidth;
        _appointmentDayPicker = [[UIPickerView alloc]initWithFrame:frame];
        [_bg_picker_view addSubview:_appointmentDayPicker];
        [_appointmentDayPicker bringSubviewToFront:_scrollView];
        [_appointmentDayPicker setDataSource:self];
        [_appointmentDayPicker setDelegate:self];
        [_appointmentDayPicker setBackgroundColor:[UIColor whiteColor]];
        [self hidePicker:_appointmentDayPicker];
    }else{
        [self showPicker:_appointmentDayPicker];
    }
    
    }
    @catch (NSException *exception) {
        
    }
//    if(_appointmentDayPicker.hidden == YES)
//    {
//        if(_appointmentDayPicker.hidden == NO)
//            _appointmentDayPicker.hidden = YES;
//
//        _appointmentDayPicker.hidden = NO;
//    }
//    else
//    {
//        _appointmentDayPicker.hidden = YES;
//    }

    [_instructionsTextView resignFirstResponder];
}

-(void)showPicker:(UIPickerView*)picker{
    
    @try {
        if(picker==_appointmentTimePicker){
            if(_appointmentTimes.count <= 0) // don't show an invisible list...
            {
                _textDialog= [[TextDialog alloc] initWithView:View title:@"Input Error" message:[NSString stringWithFormat:@"Please select a pickup date."]];
                [_textDialog show];
                return;
            }
//        }else if (picker==_appointmentDayPicker){
//            return;
        }
        
        if(picker==NULL)return;
        
        CGRect frame = picker.frame;
        if(frame.origin.y==_bg_picker_view.frame.size.height){
            frame.origin.y = _bg_picker_view.frame.size.height - frame.size.height;
        }
        [_bg_picker_view setHidden:NO];
        [UIView animateWithDuration:0.5 animations:^{
            [picker setFrame:frame];
        }];
        
        [picker reloadAllComponents];
    }
    @catch (NSException *exception) {
        
    }
}


-(void)hidePicker:(UIPickerView*)picker{
    @try {
        
   
    if(picker==NULL)return;
    
    CGRect frame = picker.frame;
    if(frame.origin.y!=_bg_picker_view.frame.size.height){
        frame.origin.y = _bg_picker_view.frame.size.height;
    }
    [UIView animateWithDuration:0.5 animations:^{
        [picker setFrame:frame];
    }completion:^(BOOL finished){
        [_bg_picker_view setHidden:YES];
    }];
    
    [picker reloadAllComponents];
        
    }
    @catch (NSException *exception) {
        
    }
}

- (void)chooseAppointmentTime
{
    
    @try {
    
    if(_appointmentTimePicker==nil){
        CGRect frame = CGRectZero;
        frame.size.height=200;
        frame.origin.x = 0;
        frame.origin.y = _bg_picker_view.frame.size.height - frame.size.height;
        frame.size.width = _contentViewWidth;
        _appointmentTimePicker = [[UIPickerView alloc]initWithFrame:frame];
        [_bg_picker_view addSubview:_appointmentTimePicker];
        [_appointmentTimePicker bringSubviewToFront:_scrollView];
        [_appointmentTimePicker setDataSource:self];
        [_appointmentTimePicker setDelegate:self];
        [_appointmentTimePicker setBackgroundColor:[UIColor whiteColor]];
        [self hidePicker:_appointmentTimePicker];
    }else{
        [self showPicker:_appointmentTimePicker];
    }
        
    }
    @catch (NSException *exception) {
        
    }
    
//    if(_appointmentTimeTable.hidden == YES)
//    {
//        if(_appointmentDayPicker.hidden == NO)
//            _appointmentDayPicker.hidden = YES;
//
//        [_appointmentTimeTable reloadData];
//
//        if(_appointmentTimes.count == 0) // don't show an invisible list...
//            return;
//
//        _appointmentTimeTable.hidden = NO;
//    }
//    else
//    {
//        _appointmentTimeTable.hidden = YES;
//    }

    [_instructionsTextView resignFirstResponder];
}


- (void)confirmOrder
{
    if([_appointmentDay compare:@""] == 0)
    {
        _textDialog= [[TextDialog alloc] initWithView:View title:@"Input Error" message:[NSString stringWithFormat:@"Please select a pickup date."]];
        [_textDialog show];
        return;
    }
    else if([_appointmentTime compare:@""] == 0)
    {
        _textDialog= [[TextDialog alloc] initWithView:View title:@"Input Error" message:[NSString stringWithFormat:@"Please select a pickup time."]];
        [_textDialog show];
        return;
    }

    [_instructionsTextView resignFirstResponder];
    
//    if([_instructionsTextView.text isEqualToString:DefaultPlaceholder]){
//        _instructionsTextView.text=@"";
//    }

    Login *login = [_app getLogin];
    EcartOrderRequest *request = [[EcartOrderRequest alloc] init];
    request.accountId = login.accountId;
    request.storeNumber = login.storeNumber;
    request.listId = _app._currentShoppingList.listId;
    //request.substitutionPreference = _substitutionPreference;
    //request.bagPreference = _bagPreference;
    request.appointmentDay = _appointmentDay;
    request.appointmentTime = _appointmentTime;
    request.instructions = _instructionsTextView.text;
    request.substitutionPreferenceName = _substitutionPreference;
    request.substitutionPreference = _substitutionPreferenceValue;
    request.bagPreference = _bagPreferenceValue;
    request.bagPreferenceName = _bagPreference;

    LogInfo("SATAN: Request Content: %@", [request objectToJson]);
    _submitButton.hidden = YES;

    EcartConfirmView *confirmView = [[EcartConfirmView alloc] initWithFrame:CGRectMake(0, 0, _app._viewWidth, _app._viewHeight) view:View preOrderResponse:_app._currentEcartPreOrderResponse orderRequest:request];
    confirmView._ecartScreenDelegate = self;
    [confirmView show];
}


- (void)handleEcartOrderServiceResponse:(id)responseObject
{
    EcartOrderRequest *orderResponse = (EcartOrderRequest *)responseObject;

    _textDialog = [[TextDialog alloc] initWithView:View title:@"Request Succeeded" message:[NSString stringWithFormat:@"Ecart Order submitted successfully. Your order id is %@. Please check your email for confirmation.", orderResponse.orderId] responder:self callback:@selector(removeView)];
    [_textDialog show];
}


- (void)removeView
{
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [_textDialog close];
    [self.presentingViewController dismissViewControllerAnimated:NO completion:nil];
}

- (void)toggleShopperDiscretionCheckBox
{
    [_shopperDiscretionCheckBox setSelected:YES];
    _substitutionPreference = SUB_PREF_1;
    _substitutionPreferenceValue = VAL_SUB_PREF_1;
    [_differentSizeCheckBox setSelected:NO];
    [_sameSizeCheckBox setSelected:NO];
    [_similarItemCheckBox setSelected:NO];
    [_noSubstitutionCheckBox setSelected:NO];
    [_instructionsTextView resignFirstResponder];
}


- (void)toggleDifferentSizeCheckBox
{
    [_shopperDiscretionCheckBox setSelected:NO];
    [_differentSizeCheckBox setSelected:YES];
    _substitutionPreference = SUB_PREF_2;
    _substitutionPreferenceValue = VAL_SUB_PREF_2;
    [_sameSizeCheckBox setSelected:NO];
    [_similarItemCheckBox setSelected:NO];
    [_noSubstitutionCheckBox setSelected:NO];
    [_instructionsTextView resignFirstResponder];
}


- (void)toggleSameSizeCheckBox
{
    [_shopperDiscretionCheckBox setSelected:NO];
    [_differentSizeCheckBox setSelected:NO];
    [_sameSizeCheckBox setSelected:YES];
    _substitutionPreference = SUB_PREF_3;
    _substitutionPreferenceValue = VAL_SUB_PREF_3;
    [_similarItemCheckBox setSelected:NO];
    [_noSubstitutionCheckBox setSelected:NO];
    [_instructionsTextView resignFirstResponder];
}


- (void)toggleSimilarItemCheckBox
{
    [_shopperDiscretionCheckBox setSelected:NO];
    [_differentSizeCheckBox setSelected:NO];
    [_sameSizeCheckBox setSelected:NO];
    [_similarItemCheckBox setSelected:YES];
    _substitutionPreference = SUB_PREF_4;
    _substitutionPreferenceValue = VAL_SUB_PREF_4;
    [_noSubstitutionCheckBox setSelected:NO];
    [_instructionsTextView resignFirstResponder];
}


- (void)toggleNoSubstitutionCheckBox
{
    [_shopperDiscretionCheckBox setSelected:NO];
    [_differentSizeCheckBox setSelected:NO];
    [_sameSizeCheckBox setSelected:NO];
    [_similarItemCheckBox setSelected:NO];
    [_noSubstitutionCheckBox setSelected:YES];
    _substitutionPreference = SUB_PREF_5;
    _substitutionPreferenceValue = VAL_SUB_PREF_5;
    [_instructionsTextView resignFirstResponder];
}


- (void)togglePaperBagCheckBox
{
    [_paperBagCheckBox setSelected:YES];
    _bagPreference = BAG_PREF_1;
    _bagPreferenceValue = VAL_BAG_PREF_1;
    [_reusableBagCheckBox setSelected:NO];
    [_instructionsTextView resignFirstResponder];
}


- (void)toggleReusableBagCheckBox
{
    [_paperBagCheckBox setSelected:NO];
    _bagPreference = BAG_PREF_2;
    _bagPreferenceValue = VAL_BAG_PREF_2;
    [_reusableBagCheckBox setSelected:YES];
    [_instructionsTextView resignFirstResponder];
}


// UIKeyboardDidShowNotification handler
- (void)keyboardWasShown:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
    CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;

    UIEdgeInsets contentInsets = UIEdgeInsetsMake(0.0, 0.0, kbSize.height, 0.0);
    _scrollView.contentInset = contentInsets;
    _scrollView.scrollIndicatorInsets = contentInsets;

    // If active text field is hidden by keyboard, scroll it so it's visible
    CGRect rect = _scrollView.frame;
    rect.size.height -= kbSize.height;
    CGPoint origin = _instructionsTextView.frame.origin;
    origin.y -= _scrollView.contentOffset.y - _app._headerHeight;
    int scrollDistance = _instructionsTextView.frame.origin.y - rect.size.height;

    // adjust for the status bar overlap on iOS7
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        scrollDistance += 20;

    if(!CGRectContainsPoint(rect, origin))
    {
        CGPoint scrollPoint = CGPointMake(0.0, scrollDistance);
        [_scrollView setContentOffset:scrollPoint animated:YES];
    }
}


// UIKeyboardWillHideNotification handler
- (void)keyboardWillBeHidden:(NSNotification*)aNotification
{
    UIEdgeInsets contentInsets = UIEdgeInsetsZero;
    _scrollView.contentInset = contentInsets;
    _scrollView.scrollIndicatorInsets = contentInsets;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// text view delegate begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark UITextView Delegate
//- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
//{
//    if([text isEqualToString:@"\n"])
//        [_instructionsTextView resignFirstResponder];
//
//    return YES;
//}



-(BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    if([textView.text isEqualToString:DefaultPlaceholder]){
        textView.textColor=[UIColor lightGrayColor];
    }
    else{
        textView.textColor=_textColor;
    }
        if([text isEqualToString:@"\n"])
            [_instructionsTextView resignFirstResponder];
    return YES;
}
-(BOOL)textViewShouldBeginEditing:(UITextView *)textView
{
    if([textView.text isEqualToString:DefaultPlaceholder]){
        textView.textColor=[UIColor lightGrayColor];
        textView.text=@"";
    }
    else{
        textView.textColor=_textColor;
    }
    return YES;
}
-(BOOL)textViewShouldEndEditing:(UITextView *)textView
{
    if([textView.text isEqualToString:@""]){
        textView.textColor=[UIColor lightGrayColor];
        textView.text=DefaultPlaceholder;
    }
    [textView resignFirstResponder];
    return YES;
}
-(BOOL)textFieldShouldReturn:(UITextView *)textField
{
    
    [textField resignFirstResponder];
    return YES;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// text view delegate end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

-(void)dismissPickerView{
    [self hidePicker:_appointmentDayPicker];
    [self hidePicker:_appointmentTimePicker];
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// picker view delegate begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

# pragma mark - Pickerview Delegate

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    if(pickerView==_appointmentDayPicker){
        return 1;
    }else if(pickerView==_appointmentTimePicker){
        return 1;
    }
    else{
        return 0;
    }
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    if(pickerView==_appointmentDayPicker){
        return _appointmentDays.count;
    }else if(pickerView==_appointmentTimePicker){
        return _appointmentTimes.count;
    }else{
        return 0;
    }
}

-(NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    @try {
        if(pickerView==_appointmentDayPicker){
            return (NSString*)[_appointmentDays objectAtIndex:row];
        }else if(pickerView==_appointmentTimePicker){
            return(NSString*)[_appointmentTimes objectAtIndex:row];
        }else{
            return nil;
        }
    }
    @catch (NSException *exception) {
        
    }
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    if(pickerView==_appointmentDayPicker){
        NSString *selectedDay = [_appointmentDays objectAtIndex:row];
        
        if([_appointmentDay compare:selectedDay] != 0)
        {
            // reload the appointment times when the day is selected
            _appointmentDay = selectedDay;
            _appointmentTime = @"";
            
            for(EcartAppointment *appointment in _app._currentAppointmentList)
            {
                if([appointment.appointmentDate compare:_appointmentDay] == 0)
                {
                    [_appointmentTimes removeAllObjects];
                    
                    if(![Utility isEmpty:appointment.appointmentTimeList])
                        [_appointmentTimes addObjectsFromArray:appointment.appointmentTimeList];
                    
                    break;
                }
            }
        }
        
//        _appointmentDayTable.hidden = YES;
        _appointmentDayText.textColor = _textColor;
        _appointmentDayText.text = _appointmentDay;
        [_appointmentDayText setNeedsDisplay];
        _appointmentTimeText.text = @"Time";
        [_appointmentTimeText setNeedsDisplay];
//        [self hidePicker:pickerView];
    }else if(pickerView==_appointmentTimePicker){
        if(_appointmentTimes.count <= 0) // don't show an invisible list...
        {
            _textDialog= [[TextDialog alloc] initWithView:View title:@"Input Error" message:[NSString stringWithFormat:@"Please select a pickup date."]];
            [_textDialog show];
            return;
        }
        else
        {
//            [_appointmentTimePicker setHidden:NO];
            _appointmentTime = [_appointmentTimes objectAtIndex:row];
            _appointmentTimeText.textColor = _textColor;
            _appointmentTimeText.text = [_appointmentTimes objectAtIndex:row];
            [_appointmentTimeText setNeedsDisplay];
//            [self hidePicker:pickerView];
        }
        
     
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// picker view delegate end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@end
