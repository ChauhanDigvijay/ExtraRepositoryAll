//
//  EcartScreenVC.h
//  Raley's
//
//  Created by Billy Lewis on 3/13/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "BaseScreenVC.h"
#import "SmartTextView.h"
#import "OffsetTextField.h"
#import "EcartScreenDelegate.h"
#import "EcartOrderRequest.h"

#define STATE_DEFAULT_TEXT @"State"
#define STORE_DEFAULT_TEXT @"Select Your Favorite Location"
#define DEPARTMENT_DEFAULT_TEXT @"Select Your Favorite Department"

@interface EcartScreenVC : BaseScreenVC <UITextFieldDelegate,UITextViewDelegate, EcartScreenDelegate, UIPickerViewDataSource, UIPickerViewDelegate>
{
    int               _tableCellHeight;
    UIColor           *_textColor;
    UIColor           *_hintColor;
    UIImage           *_activeViewImage;
    UIImage           *_inactiveViewImage;
    UIImage           *_checkedBitmap;
    UIImage           *_uncheckedBitmap;
    UIButton          *_appointmentDayButton;
    UIButton          *_appointmentTimeButton;
    UIButton          *_departmentButton;
    UIButton          *_shopperDiscretionCheckBox;
    UIButton          *_differentSizeCheckBox;
    UIButton          *_similarItemCheckBox;
    UIButton          *_noSubstitutionCheckBox;
    UIButton          *_sameSizeCheckBox;
    UIButton          *_paperBagCheckBox;
    UIButton          *_reusableBagCheckBox;
    UIButton          *_submitButton;
    UITextView        *_instructionsTextView;
//    UITableView       *_appointmentDayTable;
    UIView            *_bg_picker_view;
    UIPickerView      *_appointmentDayPicker;
    UIPickerView      *_appointmentTimePicker;
    UIScrollView      *_scrollView;
    NSMutableArray    *_appointmentDays;
    NSMutableArray    *_appointmentTimes;
    NSString          *_substitutionPreference;
    NSString          *_bagPreference;
    int               _substitutionPreferenceValue;
    int               _bagPreferenceValue;
    NSString          *_appointmentDay;
    NSString          *_appointmentTime;
    UILabel           *_appointmentDayText;
    UILabel           *_appointmentTimeText;
    EcartOrderRequest *_ecartOrderRequest;
    
    UIView *bg_container;
}

@property (nonatomic, assign)BOOL _registrationPage;

- (void)handleEcartOrderServiceResponse:(id)responseObject;

@end
