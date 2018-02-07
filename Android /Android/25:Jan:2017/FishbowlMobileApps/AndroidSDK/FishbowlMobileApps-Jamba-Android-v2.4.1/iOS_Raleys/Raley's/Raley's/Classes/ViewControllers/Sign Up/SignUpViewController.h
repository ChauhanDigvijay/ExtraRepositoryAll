//
//  SignUpViewController.h
//  Raley's
//
//  Created by VT01 on 02/06/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HeaderView.h"
#import "AccountRequest.h"
#import "ProgressDialog.h"
#import "AppDelegate.h"
#import "WebService.h"
#import "TextDialog.h"
#import "Termsconditions.h"

#define STATE_DEFAULT_TEXT @"State"
#define STORE_DEFAULT_TEXT @"Select Your Favorite Location"
#define DEPARTMENT_DEFAULT_TEXT @"Select Your Favorite Department"

@interface SignUpViewController : UIViewController <UITextFieldDelegate,UIPickerViewDataSource,UIPickerViewDelegate,WebServiceListener>{
    IBOutlet UIView *header;
    
    
    IBOutlet UITextField            *first_name;
    IBOutlet UITextField            *last_name;
    IBOutlet UITextField            *emailid;
    IBOutlet UITextField            *password;
    IBOutlet UITextField            *re_password;
    
    IBOutlet UIView                 *name_bgview;
    IBOutlet UIScrollView           *bgscroll_view;
    IBOutlet UIView                 *bg_View;
    IBOutlet UIView                 *bg_mobile_view;
    
    IBOutlet UITextField            *mobile;
    IBOutlet UITextField            *home;
    IBOutlet UITextField            *str_address;
    IBOutlet UITextField            *city;
    IBOutlet UITextField            *state;
    IBOutlet UITextField            *zip;
    
    IBOutlet UITextField            *loyaltyNum;
    
    IBOutlet UILabel                *acc_title;
    
    
    IBOutlet UIButton               *next;
    
    AccountRequest                  *_accountRequest;
    BOOL                            loyaltyNO;
    ProgressDialog                  *_progressDialog;
    WebService                      *_service;
    TextDialog                      *_textDialog;
    
    
    AppDelegate     *_app;
    IBOutlet UILabel *loyalty_text;
    IBOutlet UIButton *loyalty_button;
    
    UIView            *_bg_picker_view;
    UIPickerView      *_statePicker;
    UIPickerView      *_DepartmentPicker;
    NSArray           *_states;
    NSArray           *_departments;
    
    IBOutlet UIView *All_other_static_views;
    
    IBOutlet UIView   *loyalty_options_bg_view;
    IBOutlet UIButton *loyalty_options_extra_option;
    IBOutlet UIButton *loyalty_options_number;
    
    IBOutlet UIView   *tellus_abt_bg_view;
    
    IBOutlet UITextField *tellus_abt_fav_Location_txt;
    IBOutlet UITextField *tellus_abt_fav_department_txt;
    
    IBOutlet UIButton *tellus_abt_fav_Location;
    IBOutlet UIButton *tellus_abt_fav_department;
    
    IBOutlet UIButton *tellus_abt_email_me;
    IBOutlet UIButton *tellus_abt_message_me;
    
    IBOutlet UIView   *Terms_bg_view;
    IBOutlet UIButton *terms_and_condition_option;
    IBOutlet UIButton *terms_and_condition_details;
    
    IBOutlet UIView  *PointsBalance_bg_view;
    IBOutlet UILabel *PointsBalance_txt_lbl;
    IBOutlet UILabel *PointsBalance_val_lbl;
    
    
}
@property (nonatomic, assign)BOOL _registrationPage;
-(IBAction)choosedeptPicker:(id)sender;
@end
