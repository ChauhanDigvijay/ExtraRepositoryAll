//
//  SignUpViewController.m
//  Raley's
//
//  Created by VT01 on 02/06/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "SignUpViewController.h"
#import "HeaderView.h"
#import "Utility.h"
#import "TextDialog.h"
#import "StoreLocatorScreenVC.h"

#define navigationbarheight 44.0f

@interface SignUpViewController ()
{
    int yAxis;
    NSNumberFormatter *_numberFormatter;
    
    BOOL shop_Offer_flag;
    BOOL email_trigger_flag;
    BOOL Text_msg_trigger_flag;
    BOOL terms_selection_flag;
    
}
@end

@implementation SignUpViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        
        
    }
    return self;
}

- (void)viewDidLoad
{
    _app = (id)[[UIApplication sharedApplication] delegate];
    [bgscroll_view setScrollEnabled:YES];
    _accountRequest = [[AccountRequest alloc]init];
    _numberFormatter = [[NSNumberFormatter alloc] init];
    
    _states = [[NSArray alloc] initWithObjects:
               @"Alabama", @"Alaska", @"Arizona", @"Arkansas", @"California", @"Colorado", @"Connecticut", @"Delaware", @"DC", @"Florida",
               @"Georgia", @"Hawaii", @"Idaho", @"Illinois", @"Indiana", @"Iowa", @"Kansas", @"Kentucky", @"Louisiana", @"Maine",
               @"Maryland", @"Massachusetts", @"Michigan", @"Minnesota", @"Mississippi", @"Missouri", @"Montana", @"Nebraska", @"Nevada", @"New Hampshire",
               @"New Jersey", @"New Mexico", @"New York", @"North Carolina", @"North Dakota", @"Ohio", @"Oklahoma", @"Oregon", @"Pennsylvania",@ "Rhode Island",
               @"South Carolina", @"South Dakota", @"Tennessee", @"Texas", @"Utah", @"Vermont", @"Virginia", @"Washington", @"West Virginia", @"Wisconsin", @"Wyoming", nil];
    
    _departments = [[NSArray alloc] initWithObjects:
                    @"Fine Produce", @"Fine Meats", @"Fine Seafood", @"Grocery & Household", @"Wine, Beer & Spirits", @"Recipes", @"Deli Prepared Foods",
                    @"Fine Bakery", @"New Items", @"Natural & Organic Foods", @"Pharmacy & Wellness", nil];
    
    //    NSString * booleanString = (__registrationPage) ? @"True" : @"False";
    //
    //    UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"Boolean check" message:booleanString delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    //    [alertView show];
    
    //    self.automaticallyAdjustsScrollViewInsets = NO;
    
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        yAxis = 20;
    else
    { yAxis = 0;
        CGRect frame = header.frame;
        frame.origin.y -= 20;
        [header setFrame:frame];
    }
    
    [self applyPaddingForTextField:first_name];
    [self applyPaddingForTextField:last_name];
    [self applyPaddingForTextField:emailid];
    [self applyPaddingForTextField:password];
    [self applyPaddingForTextField:re_password];
    
    [self applyTextFieldDelegate:first_name];
    [self applyTextFieldDelegate:last_name];
    [self applyTextFieldDelegate:emailid];
    [self applyTextFieldDelegate:password];
    
    [next.layer setCornerRadius:5.0f];
    
    [self applyPaddingForTextField:mobile];
    [self applyPaddingForTextField:home];
    [self applyPaddingForTextField:str_address];
    [self applyPaddingForTextField:city];
    [self applyPaddingForTextField:state];
    [self applyPaddingForTextField:zip];
    
    [self applyTextFieldDelegate:mobile];
    [self applyTextFieldDelegate:home];
    [self applyTextFieldDelegate:str_address];
    [self applyTextFieldDelegate:city];
    [self applyTextFieldDelegate:state];
    [self applyTextFieldDelegate:zip];
    
    loyaltyNO = NO;
    
    [self applyPaddingForTextField:loyaltyNum];
    [self applyTextFieldDelegate:loyaltyNum];
    
    
    [loyaltyNum.layer setCornerRadius:5.0f];
    
    // Custom initialization
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(sukeyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(sukeyboardWillHide:) name:UIKeyboardWillHideNotification object:nil];
    
    
    [bgscroll_view setClipsToBounds:NO];
    
    // first_name.returnKeyType = UIReturnKeyDone;
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self
                                   action:@selector(dismissKeyboard)];
    
    [self.view addGestureRecognizer:tap];
    
    [Terms_bg_view setHidden:NO];
    CGRect frame=All_other_static_views.frame;
    frame.origin.y=frame.origin.y-8;
    [All_other_static_views setFrame:frame];
    
    // applying values
    
    if(!__registrationPage){
        
        _accountRequest =_app._currentAccountRequest;
        
        [first_name setText: _app._currentAccountRequest.firstName];
        [last_name setText: _app._currentAccountRequest.lastName];
        [emailid setText: _app._currentAccountRequest.email];
        [password setText: _app._currentAccountRequest.password];
        
        [mobile setText: _app._currentAccountRequest.mobilePhone];
        [home setText: _app._currentAccountRequest.phone];
        [str_address setText: _app._currentAccountRequest.address];
        [city setText: _app._currentAccountRequest.city];
        [state setText:[self getStateNameByCode:_app._currentAccountRequest.state]];
        [zip setText: _app._currentAccountRequest.zip];
        [loyaltyNum setText:_app._currentAccountRequest.loyaltyNumber];
        [tellus_abt_fav_department_txt setText:_app._currentAccountRequest.favoriteDept];
        
        CGRect frame=All_other_static_views.frame;
        frame.origin.y=frame.origin.y+8;
        [All_other_static_views setFrame:frame];
        
        if(_app._allStoresList.count>0){
            Store *store;
            for(NSString *key in [_app._allStoresList allKeys])
            {
                store = [_app._allStoresList objectForKey:key];
                if(store!=nil)
                {
                    if(store.storeNumber==_accountRequest.storeNumber){
                        tellus_abt_fav_Location_txt.text = [NSString stringWithFormat:@"%@ %@", store.chain, store.address];
                        [tellus_abt_fav_Location_txt setNeedsDisplay];
                        break;
                    }
                }
            }
            
        }
        
        Login *login = [_app getLogin];
        [password setText:login.password];
        
        [password setTextColor:[UIColor lightGrayColor]];
        
        terms_selection_flag=_accountRequest.termsAcceptedFlag.boolValue;
        shop_Offer_flag=_accountRequest.issueCardFlag.boolValue;
        email_trigger_flag=_accountRequest.sendEmailsFlag.boolValue;
        Text_msg_trigger_flag=_accountRequest.sendTextsFlag.boolValue;
        [PointsBalance_val_lbl setText:login.pointsBalance];

        
        if(_accountRequest.issueCardFlag.boolValue==TRUE)
        {
            [loyalty_options_number setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
            [loyalty_options_extra_option setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
            
        }
        else
        {
            [loyalty_options_number setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
            [loyalty_options_extra_option setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
        }
        
        if(_accountRequest.sendEmailsFlag.boolValue==TRUE)
        {
            [tellus_abt_email_me setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
        }
        else
        {
            [tellus_abt_email_me setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
        }
        
        if(_accountRequest.sendTextsFlag.boolValue==TRUE)
        {
            [tellus_abt_message_me setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
        }
        else
        {
            [tellus_abt_message_me setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
            
        }
        
        
        
        [next setTitle:@"Update" forState:UIControlStateNormal];
        [acc_title setText:@"My Account"];
        
        [Terms_bg_view setHidden:YES];
        
    }
    else
    {
        //Checkbox Default state:
        
        terms_selection_flag=false;
        shop_Offer_flag=true;
        email_trigger_flag=true;
        Text_msg_trigger_flag=true;
        
        [loyalty_options_extra_option setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
        
        
        [loyalty_options_number setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
        
        [tellus_abt_email_me setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
        
        [tellus_abt_message_me setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
        
        [terms_and_condition_option setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
    }
    
    //[state addTarget:self action:@selector(chooseStatePicker) forControlEvents:UIControlEventTouchDown];
    
    // hide the radio button
    
    if(!__registrationPage){
        
        //    IBOutlet UIView  *PointsBalance_bg_view;
        //    IBOutlet UILabel *PointsBalance_txt_lbl;
        //    IBOutlet UILabel *PointsBalance_val_lbl;
        
        [PointsBalance_bg_view setHidden:NO];
        
        CGFloat height = loyalty_text.frame.origin.y;
        
        CGRect frame1 = loyaltyNum.frame;
        // CGFloat height1 = bounds1.origin.y;
        //bounds1.origin.y = height1 - height + 10;
        frame1.origin.y = height;
        [loyaltyNum setFrame:frame1];
        
        CGRect temp = PointsBalance_bg_view.frame;
        temp.origin.y = loyaltyNum.frame.origin.y + loyaltyNum.frame.size.height + 8;
        [PointsBalance_bg_view setFrame:temp];
        
        
        CGRect frame2 = bg_mobile_view.frame;
        frame2.origin.y = PointsBalance_bg_view.frame.origin.y + PointsBalance_bg_view.frame.size.height + 8;
        [bg_mobile_view setFrame:frame2];
        
        loyalty_text.hidden= YES;
        loyalty_button.hidden = YES;
        
        CGRect new_frame=All_other_static_views.frame; // bottom of all controls moving to up positions
        new_frame.origin.y=new_frame.origin.y-(loyalty_text.frame.size.height+20) + 18; // 18 Gaps
        [All_other_static_views setFrame:new_frame];
        
    }
    else{
        
        [PointsBalance_bg_view setHidden:YES];
        
        
        CGRect frame2 = bg_mobile_view.frame;
        frame2.origin.y = loyaltyNum.frame.origin.y + loyaltyNum.frame.size.height + 8;
        [bg_mobile_view setFrame:frame2];
        
        
        CGRect new_frame=All_other_static_views.frame; // bottom of all controls moving to up positions
        new_frame.origin.y=(new_frame.origin.y-(loyalty_text.frame.size.height+20)) + PointsBalance_bg_view.frame.size.height + 18; // 18 Gaps
        [All_other_static_views setFrame:new_frame];
        
    }

    
    CGFloat _customHeight;
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){
        _customHeight = 20;
    }
    else{
        _customHeight = 0;
    }
    // stupid logic for picker view
    if(_app._deviceType == IPHONE_5){
        _bg_picker_view = [[UIView alloc]initWithFrame:CGRectMake(0, header.frame.size.height+_customHeight, self.view.frame.size.width, self.view.frame.size.height+navigationbarheight+_customHeight)];
    }
    else{
        _bg_picker_view = [[UIView alloc]initWithFrame:CGRectMake(0, header.frame.size.height+_customHeight, self.view.frame.size.width, self.view.frame.size.height - (navigationbarheight * 0.79))];
        
        
    }
    [_bg_picker_view setUserInteractionEnabled:YES];
    [_bg_picker_view setBackgroundColor:[UIColor colorWithRed:0 green:0 blue:0 alpha:0.3]];
    UITapGestureRecognizer *single_tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(dismissPickerView)];
    [_bg_picker_view addGestureRecognizer:single_tap];
    [_bg_picker_view setHidden:YES];
    
    [self.view addSubview:_bg_picker_view];
    
    [self chooseStatePicker];
    [self chooseDepartmentPicker];
    
    [bgscroll_view setShowsHorizontalScrollIndicator:NO];
    [bgscroll_view setShowsVerticalScrollIndicator:NO];
    [bgscroll_view setContentSize:bg_View.frame.size];
    
    [self Corners_and_Text_fields_Styles];
    
    
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}


-(void)Corners_and_Text_fields_Styles
{
    
    //rounded corners
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:first_name.bounds
                                                   byRoundingCorners:(UIRectCornerTopLeft)
                                                         cornerRadii:CGSizeMake(5.0, 0.0)];
    
    CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.view.bounds;
    maskLayer.path = maskPath.CGPath;
    first_name.layer.mask = maskLayer;
    
    
    maskPath = [UIBezierPath bezierPathWithRoundedRect:last_name.bounds
                                     byRoundingCorners:(UIRectCornerTopRight )
                                           cornerRadii:CGSizeMake(5.0, 0.0)];
    maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.view.bounds;
    maskLayer.path = maskPath.CGPath;
    last_name.layer.mask = maskLayer;
    
    
    maskPath = [UIBezierPath bezierPathWithRoundedRect:re_password.bounds
                                     byRoundingCorners:(UIRectCornerBottomLeft | UIRectCornerBottomRight)
                                           cornerRadii:CGSizeMake(5.0, 5.0)];
    maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.view.bounds;
    maskLayer.path = maskPath.CGPath;
    re_password.layer.mask = maskLayer;
    
    
    
    maskPath = [UIBezierPath bezierPathWithRoundedRect:mobile.bounds
                                     byRoundingCorners:(UIRectCornerTopLeft)
                                           cornerRadii:CGSizeMake(5.0, 0.0)];
    maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.view.bounds;
    maskLayer.path = maskPath.CGPath;
    mobile.layer.mask = maskLayer;
    
    
    maskPath = [UIBezierPath bezierPathWithRoundedRect:home.bounds
                                     byRoundingCorners:(UIRectCornerTopRight)
                                           cornerRadii:CGSizeMake(5.0, 0.0)];
    maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.view.bounds;
    maskLayer.path = maskPath.CGPath;
    home.layer.mask = maskLayer;
    
    
    
    
    maskPath = [UIBezierPath bezierPathWithRoundedRect:state.bounds
                                     byRoundingCorners:(UIRectCornerBottomLeft)
                                           cornerRadii:CGSizeMake(5.0, 0.0)];
    maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.view.bounds;
    maskLayer.path = maskPath.CGPath;
    state.layer.mask = maskLayer;
    
    
    maskPath = [UIBezierPath bezierPathWithRoundedRect:zip.bounds
                                     byRoundingCorners:(UIRectCornerBottomRight)
                                           cornerRadii:CGSizeMake(5.0, 0.0)];
    maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.view.bounds;
    maskLayer.path = maskPath.CGPath;
    zip.layer.mask = maskLayer;
    
    
    [name_bgview.layer setShadowColor:[UIColor darkGrayColor].CGColor];
    [name_bgview.layer setShadowOpacity:0.6];
    [name_bgview.layer setShadowRadius:0.3];
    [name_bgview.layer setShadowOffset:CGSizeMake(0.0,0.6)];
    [name_bgview.layer setCornerRadius:5.0f];
    
    
    [bg_mobile_view.layer setShadowColor:[UIColor darkGrayColor].CGColor];
    [bg_mobile_view.layer setShadowOpacity:0.6];
    [bg_mobile_view.layer setShadowRadius:0.3];
    [bg_mobile_view.layer setShadowOffset:CGSizeMake(0.0,0.6)];
    [bg_mobile_view.layer setCornerRadius:5.0f];
    
    [loyalty_options_bg_view.layer setShadowColor:[UIColor darkGrayColor].CGColor];
    [loyalty_options_bg_view.layer setShadowOpacity:0.6];
    [loyalty_options_bg_view.layer setShadowRadius:0.3];
    [loyalty_options_bg_view.layer setShadowOffset:CGSizeMake(0.0,0.6)];
    [loyalty_options_bg_view.layer setCornerRadius:5.0f];
    
    [PointsBalance_bg_view.layer setShadowColor:[UIColor darkGrayColor].CGColor];
    [PointsBalance_bg_view.layer setShadowOpacity:0.6];
    [PointsBalance_bg_view.layer setShadowRadius:0.3];
    [PointsBalance_bg_view.layer setShadowOffset:CGSizeMake(0.0,0.6)];
    [PointsBalance_bg_view.layer setCornerRadius:5.0f];
    
    [tellus_abt_bg_view.layer setShadowColor:[UIColor darkGrayColor].CGColor];
    [tellus_abt_bg_view.layer setShadowOpacity:0.6];
    [tellus_abt_bg_view.layer setShadowRadius:0.3];
    [tellus_abt_bg_view.layer setShadowOffset:CGSizeMake(0.0,0.6)];
    [tellus_abt_bg_view.layer setCornerRadius:5.0f];
    
    [Terms_bg_view.layer setShadowColor:[UIColor darkGrayColor].CGColor];
    [Terms_bg_view.layer setShadowOpacity:0.6];
    [Terms_bg_view.layer setShadowRadius:0.3];
    [Terms_bg_view.layer setShadowOffset:CGSizeMake(0.0,0.6)];
    [Terms_bg_view.layer setCornerRadius:5.0f];
    
    
    [tellus_abt_fav_Location_txt.layer setBorderWidth:0.7f];
    [tellus_abt_fav_Location_txt.layer setCornerRadius:5.0f];
    [tellus_abt_fav_Location_txt.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    
    
    [tellus_abt_fav_department_txt.layer setBorderWidth:0.7f];
    [tellus_abt_fav_department_txt.layer setCornerRadius:5.0f];
    [tellus_abt_fav_department_txt.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    
    UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, tellus_abt_fav_department_txt.frame.size.height)];
    leftView.backgroundColor = tellus_abt_fav_department_txt.backgroundColor;
    tellus_abt_fav_department_txt.leftView = leftView;
    tellus_abt_fav_department_txt.leftViewMode = UITextFieldViewModeAlways;
    
    
    leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, tellus_abt_fav_Location_txt.frame.size.height)];
    leftView.backgroundColor = tellus_abt_fav_Location_txt.backgroundColor;
    tellus_abt_fav_Location_txt.leftView = leftView;
    tellus_abt_fav_Location_txt.leftViewMode = UITextFieldViewModeAlways;
    
}


-(IBAction)Terms_and_conditions:(id)sender
{
    Termsconditions *products = [[Termsconditions alloc]initWithNibName:@"Termsconditions" bundle:nil];
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
    [self presentViewController:products animated:NO completion:nil];
    
}


-(IBAction)Change_Check_status:(id)sender
{
    int index=(int)[sender tag];
    
    UIButton *btn = (UIButton*)sender;
    
    switch (index) {
        case 5:{
            if(shop_Offer_flag==true)
            {
                [loyalty_options_number setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
                [loyalty_options_extra_option setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
                
                shop_Offer_flag=false;
            }else{
                [loyalty_options_number setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
                [loyalty_options_extra_option setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
                
                shop_Offer_flag=true;
            }
            
            break;
        }
        case 6:{
            if(shop_Offer_flag==false)
            {//
                [loyalty_options_number setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
                [loyalty_options_extra_option setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
                
                shop_Offer_flag=true;
                
            }else{
                [loyalty_options_number setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
                [loyalty_options_extra_option setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
                
                shop_Offer_flag=false;
            }
            break;
        }
        case 7:{
            
            if(email_trigger_flag==false)
            {//
                [tellus_abt_email_me setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
                email_trigger_flag=true;
            }
            else
            {
                [tellus_abt_email_me setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
                email_trigger_flag=false;
            }
            
            break;
        }
        case 8:{
            
            if(Text_msg_trigger_flag==false)
            {//
                [tellus_abt_message_me setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
                Text_msg_trigger_flag=true;
            }
            else
            {
                [tellus_abt_message_me setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
                Text_msg_trigger_flag=false;
            }
            
            break;
        }
            
        case 9:{
            
            if(terms_selection_flag==false)
            {//
                [btn setImage:[UIImage imageNamed:@"product_checked_box.png"] forState:UIControlStateNormal];
                terms_selection_flag=true;
            }
            else
            {
                [btn setImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
                terms_selection_flag=false;
            }
            
            break;
        }
        default:
            break;
    }
    
    
    
    
}


-(void)showPicker:(UIPickerView*)picker{
    @try {
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
        if([picker isEqual:_statePicker]){
            if([state.text isEqualToString:@""]){
                [picker selectRow:0 inComponent:0 animated:YES];
            }
            else{
                int position = [self getStatePickerScrollPosition:state.text];
                [picker selectRow:position inComponent:0 animated:YES];
            }
        }
        
    }
    @catch (NSException *exception) {
    }
}

-(void)hidePicker:(UIPickerView*)picker{
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


-(int)getStatePickerScrollPosition:(NSString*)StateName{
    @try {
        // NSString *StateName = [self getStateNameByCode:StateCode];
        for(int i=0;i< [_states count];i++){
            if([[_states objectAtIndex:i] isEqualToString:StateName]){
                return i;
            }
        }
    }
    @catch (NSException *exception) {
        
    }
    return 0;
}


- (IBAction)chooseStore
{
    //    tel.hidden = YES;
    
    if(_app._allStoresList.count == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Server Error" message:[NSString stringWithFormat:@"Sorry, the store locator is not available at this time."]];
        
        [dialog show];
        return;
    }
    
    _app._locateForAccount = YES;
    StoreLocatorScreenVC *storeLocatorScreenVC = [[StoreLocatorScreenVC alloc] init];
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
    [self presentViewController:storeLocatorScreenVC animated:NO completion:nil];
}


- (void)chooseStatePicker
{
    
    if(_statePicker==nil){
        CGRect frame = CGRectZero;
        frame.size.height=200;
        frame.origin.x = 0;
        if(_app._deviceType == IPHONE_5){
            frame.origin.y = _bg_picker_view.frame.size.height - frame.size.height;
        }
        else{
            // frame.origin.y = _bg_picker_view.frame.size.height - (frame.size.height * 8);
            frame.origin.y = -2000;
        }
        frame.size.width = self.view.frame.size.width;
        _statePicker = [[UIPickerView alloc]initWithFrame:frame];
        [_bg_picker_view addSubview:_statePicker];
        [_statePicker bringSubviewToFront:bgscroll_view];
        [_statePicker setDataSource:self];
        [_statePicker setDelegate:self];
        [_statePicker setBackgroundColor:[UIColor whiteColor]];
        [self hidePicker:_statePicker];
    }else{
        [self showPicker:_statePicker];
    }
}

-(IBAction)choosedeptPicker:(id)sender{
    [self chooseDepartmentPicker];
}

- (void)chooseDepartmentPicker
{
    [self.view endEditing:YES];
    if(_DepartmentPicker==nil){
        CGRect frame = CGRectZero;
        frame.size.height=200;
        frame.origin.x = 0;
        if(_app._deviceType == IPHONE_5){
            frame.origin.y = _bg_picker_view.frame.size.height - frame.size.height;
        }
        else{
            // frame.origin.y = _bg_picker_view.frame.size.height - (frame.size.height * 8);
            frame.origin.y = -2000;
        }
        frame.size.width = self.view.frame.size.width;
        _DepartmentPicker = [[UIPickerView alloc]initWithFrame:frame];
        [_bg_picker_view addSubview:_DepartmentPicker];
        [_DepartmentPicker bringSubviewToFront:bgscroll_view];
        [_DepartmentPicker setDataSource:self];
        [_DepartmentPicker setDelegate:self];
        [_DepartmentPicker setBackgroundColor:[UIColor whiteColor]];
        [self hidePicker:_DepartmentPicker];
    }else{
        [self showPicker:_DepartmentPicker];
    }
}

-(void)dismissKeyboard {
    if([re_password resignFirstResponder]){
        if(![password.text isEqualToString:re_password.text])
        {
            [password resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Passwords don't match. Please re-try."];
            [dialog show];
            [password setText:@""];
            [re_password setText:@""];
        }
    }
    
    [self.view endEditing:YES];
}

-(void)applyPaddingForTextField:(UITextField*)textfield{
    CGFloat padding = 10;
    
    UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, padding, textfield.frame.size.height)];
    leftView.backgroundColor = textfield.backgroundColor;
    textfield.leftView = leftView;
    textfield.leftViewMode = UITextFieldViewModeAlways;
    
    textfield.rightView = leftView;
    textfield.rightViewMode = UITextFieldViewModeUnlessEditing;
}

-(void)applyTextFieldDelegate:(UITextField*)textfield{
    [textfield setDelegate:self];
}


-(void)applyCornderRadius:(UIView*)view{
    [view.layer setCornerRadius:5.0f];
    [view setClipsToBounds:YES];
    
}

-(void)viewDidAppear:(BOOL)animated
{
    if(_app._locateForAccount == YES)
    {
        _app._locateForAccount = NO;
        if(_app._storeForAccount != nil)
        {
            _accountRequest.storeNumber = _app._storeForAccount.storeNumber;
            tellus_abt_fav_Location_txt.text = [NSString stringWithFormat:@"%@ %@", _app._storeForAccount.chain, _app._storeForAccount.address];
            [tellus_abt_fav_Location_txt setNeedsDisplay];
        }
    }
}

-(void)applyViewBottomShadow:(UIView*)view{
    
    //    CAGradientLayer *shadow = [CAGradientLayer layer];
    //    shadow.frame = CGRectMake(0, view.frame.size.height, view.frame.size.width, 10);
    //    shadow.startPoint = CGPointMake(1.0, 0.5);
    //    shadow.endPoint = CGPointMake(0, 0.5);
    //    shadow.colors = [NSArray arrayWithObjects:(id)[[UIColor colorWithWhite:0.0 alpha:0.4f] CGColor], (id)[[UIColor clearColor] CGColor], nil];
    //    [view.layer addSublayer:shadow];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)dismissview:(id)sender{
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [self dismissViewControllerAnimated:NO completion:nil];
}

-(void)dismissPickerView{
    [self hidePicker:_statePicker];
    [self hidePicker:_DepartmentPicker];
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// text view delegate begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - UITextField Delegate
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    if(textField ==password)
    {
        [password setText:@""];
        [password setTextColor:[UIColor blackColor]];
    }
    if(textField == state)
    {
        [state resignFirstResponder];
        [self.view endEditing:YES];
        [self chooseStatePicker];
        return NO;
        
    }
    else if (textField==tellus_abt_fav_Location_txt){
        [tellus_abt_fav_Location_txt resignFirstResponder];
        [self.view endEditing:YES];
        [self chooseStore];
        return NO;
    }
    else if (textField==tellus_abt_fav_department_txt){
        [tellus_abt_fav_department_txt resignFirstResponder];
        [self.view endEditing:YES];
        [self chooseDepartmentPicker];
        return NO;
    }
    
    else{
        return YES;
    }
    
}


- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if(textField.returnKeyType==UIReturnKeyDone){
        [textField resignFirstResponder];
        return YES;
    }
    NSInteger nexttag = textField.tag + 1;
    UIResponder *responder = [self.view viewWithTag:nexttag];
    if(responder){
        [responder becomeFirstResponder];
    }else{
        [textField resignFirstResponder];
    }
    return YES;
}

-(BOOL)textFieldShouldEndEditing:(UITextField *)textField
{
    if(textField==re_password){
        if(![password.text isEqualToString:re_password.text])
        {
            [re_password resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Passwords don't match.\nPlease re-enter."];
            [dialog show];
        }
        password.text=@"";
        re_password.text=@"";
        return NO;
    }
    return YES;
    
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// text view delegate end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

#pragma mark - Function

-(IBAction)have_loyalty:(id)sender{
    //product_checked_box.png
    //product_unchecked_box.png
    
//    CGRect new_frame=All_other_static_views.frame;
//    CGFloat new_y=0.0f;
//    
    UIButton *btn = (UIButton*)sender;
    if(!loyaltyNO){
        loyaltyNO = YES;
        [btn setImage:[UIImage imageNamed:@"product_checked_box"] forState:UIControlStateNormal];
        [loyaltyNum setPlaceholder:@"Enter your Something Extra or Loyalty Number"];
//        [UIView animateWithDuration:0.5 animations:^{
//            [bg_mobile_view setAlpha:0.0];
//        }completion:^(BOOL finished){
//            [bg_mobile_view setHidden:YES];
//            
//        }];
//        
//        new_y=new_frame.origin.y-120;
//        new_frame.origin.y=new_y;
//        
//        [bgscroll_view setContentSize:CGSizeMake(bg_View.frame.size.width, bg_View.frame.size.height-80)];
        
        
        //        All_other_static_views
        
    }else{
        loyaltyNO = NO;
        [btn setImage:[UIImage imageNamed:@"product_unchecked_box"] forState:UIControlStateNormal];
        [loyaltyNum setPlaceholder:@"Create a 8-12 digits Loyalty Number"];
//        [UIView animateWithDuration:0.5 animations:^{
//            [bg_mobile_view setHidden:NO];
//            [bg_mobile_view setAlpha:1.0];
//        }completion:^(BOOL finished){
//            
//        }];
//        
//        new_y=new_frame.origin.y+120;
//        new_frame.origin.y=new_y;
//        
//        [bgscroll_view setContentSize:CGSizeMake(bg_View.frame.size.width, bg_View.frame.size.height+80)];
//        
    }
    
//    [All_other_static_views setFrame:new_frame];
    
    //  [bg_View setFrame:CGRectMake(bg_View.frame.origin.x, bg_View.frame.origin.y, bg_View.frame.size.width, bg_View.frame.size.height-new_y)];
    
    //All_other_static_views
    
    
    
    [self applyPaddingForTextField:loyaltyNum];
}

-(BOOL)isEmpty:(NSString*)str{
    str = [str stringByReplacingOccurrencesOfString:@" " withString:@""];
    if(str.length==0){
        return YES;
    }else{
        return NO;
    }
}

-(BOOL)validateAllFields{
    BOOL status = NO;
    if([self isEmpty:first_name.text]){
        [first_name resignFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"First Name can't be blank."];
        [dialog show];
        return status;
    }else if ([self isEmpty:last_name.text]){
        [last_name resignFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Last Name can't be blank."];
        [dialog show];
        return status;
    }else if ([self isEmpty:emailid.text]){
        [emailid resignFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Email address can't be blank"];
        [dialog show];
        return status;
    }
    else if (![Utility validateEmail:emailid.text]){
        [emailid resignFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Email Address is invalid"];
        [dialog show];
        return status;
    }
    else if ([self isEmpty:password.text]){
        [password resignFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Password can't be blank"];
        [dialog show];
        return status;
    }
    else if(password.text.length < 6 || password.text.length < 6 || [[password.text componentsSeparatedByCharactersInSet:[NSCharacterSet uppercaseLetterCharacterSet]] count] - 1 == 0)
    {
        [password resignFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Passwords must be at least 6 characters and contain at least one uppercase letter"];
        [dialog show];
        [password setText:@""];
        [re_password setText:@""];
        return status;
    }
    else if(![password.text isEqualToString:re_password.text])
    {
        [password resignFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Passwords don't match. Please re-try."];
        [dialog show];
        [password setText:@""];
        [re_password setText:@""];
        return status;
    }
    
    // LOYALTY NUMBER
    if ([self isEmpty:loyaltyNum.text]){
        [loyaltyNum resignFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Loyalty Number can't be blank"];
        [dialog show];
        return status;
    }else if(loyaltyNum.text.length < 8 ||loyaltyNum.text.length > 12 || [_numberFormatter numberFromString:loyaltyNum.text] == nil)
    {
        [loyaltyNum resignFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Loyalty Numbers must contain 8 to 12 digits"];
        [dialog show];
        return status;
    }
    //
    
//    if(!loyaltyNO){
    
        if ([self isEmpty:mobile.text]){
            [mobile resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Cell phone must be provided."];
            [dialog show];
            return status;
        }
    // Cell phone number should not be less than 10 digit
        //else if (mobile.text.length<10){
        else if ([self calculateNumberLength:mobile.text]!=10){
            [mobile resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Cell phone is Invalid."];
            [dialog show];
            return status;
        }
        else if ([self isEmpty:home.text]){
            [home resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Home phone must be provided."];
            [dialog show];
            return status;
        }
    // Home phone number should not be less than 10 digit
        //else if (home.text.length<10){
        else if ([self calculateNumberLength:home.text]!=10){
            
            [home resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Home phone is Invalid."];
            [dialog show];
            return status;
        }
        else if ([self isEmpty:str_address.text]){
            [str_address resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Address can't be blank"];
            [dialog show];
            return status;
        }
        else if ([self isEmpty:city.text]){
            [city resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"City can't be blank"];
            [dialog show];
            return status;
        }
        else if ([self isEmpty:state.text]){
            [state resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"State can't be blank"];
            [dialog show];
            return status;
        }
        else if ([self isEmpty:zip.text]){
            [zip resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Zip can't be blank"];
            [dialog show];
            return status;
        }else if(zip.text.length != 5 ||
                 [_numberFormatter numberFromString:[zip.text substringWithRange:NSMakeRange(0, 5)]] == nil)
        {
            [zip resignFirstResponder];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Zip Code should be a five digit number."];
            [dialog show];
            return status;
        }
//    }
    
    
    if([self isEmpty:tellus_abt_fav_Location_txt.text])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Please choose a favorite location."];
        [dialog show];
        return NO;
    }
    
    
    if([self isEmpty:tellus_abt_fav_department_txt.text])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"Please choose a favorite department."];
        [dialog show];
        return NO;
    }
    
    
    if(__registrationPage == YES)
    {
        if([self validateTAC] == NO){
            return NO;
        }
    }
    
    status = YES;
    
    return status;
}

-(int)calculateNumberLength:(NSString*)contactNumber{
    
    NSRange range = [contactNumber rangeOfString:@"-"];
    if (range.location == NSNotFound) {
       return (int)contactNumber.length;
    } else {
        return (int)contactNumber.length - 2;
    }
    
}

- (BOOL)validateTAC // Terms and conditions selections
{
    if(terms_selection_flag == false)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Input Error" message:@"You must agree to the Terms and Conditions in order to register."];
        [dialog show];
        return NO;
    }
    
    return true;
}


-(NSString *)getStateNameByCode:(NSString*)state_name{
    NSString *code = @"";
    
    if([state_name isEqualToString:@"AL"]){
        code = @"Alabama";
    }else if([state_name isEqualToString:@"AK"]){
        code = @"Alaska";
    }else if([state_name isEqualToString:@"AZ"]){
        code = @"Arizona";
    }else if([state_name isEqualToString:@"AR"]){
        code = @"Arkansas";
    }else if([state_name isEqualToString:@"CA"]){
        code = @"California";
    }else if([state_name isEqualToString:@"CO"]){
        code = @"Colorado";
    }else if([state_name isEqualToString:@"CT"]){
        code = @"Connecticut";
    }else if([state_name isEqualToString:@"DE"]){
        code = @"Delaware";
    }else if([state_name isEqualToString:@"FL"]){
        code = @"Florida";
    }else if([state_name isEqualToString:@"GA"]){
        code = @"Georgia";
    }else if([state_name isEqualToString:@"HI"]){
        code = @"Hawaii";
    }else if([state_name isEqualToString:@"ID"]){
        code = @"Idaho";
    }else if([state_name isEqualToString:@"IL"]){
        code = @"Illinois";
    }else if([state_name isEqualToString:@"IN"]){
        code = @"Indiana";
    }else if([state_name isEqualToString:@"IA"]){
        code = @"Iowa";
    }else if([state_name isEqualToString:@"KS"]){
        code = @"Kansas";
    }else if([state_name isEqualToString:@"KY"]){
        code = @"Kentucky";
    }else if([state_name isEqualToString:@"LA"]){
        code = @"Louisiana";
    }else if([state_name isEqualToString:@"ME"]){
        code = @"Maine";
    }else if([state_name isEqualToString:@"MD"]){
        code = @"Maryland";
    }else if([state_name isEqualToString:@"MA"]){
        code = @"Massachusetts";
    }else if([state_name isEqualToString:@"MI"]){
        code = @"Michigan";
    }else if([state_name isEqualToString:@"MN"]){
        code = @"Minnesota";
    }else if([state_name isEqualToString:@"MS"]){
        code = @"Mississippi";
    }else if([state_name isEqualToString:@"MO"]){
        code = @"Missouri";
    }else if([state_name isEqualToString:@"MT"]){
        code = @"Montana";
    }else if([state_name isEqualToString:@"NE"]){
        code = @"Nebraska";
    }else if([state_name isEqualToString:@"NV"]){
        code = @"Nevada";
    }else if([state_name isEqualToString:@"NH"]){
        code = @"New Hampshire";
    }else if([state_name isEqualToString:@"NJ"]){
        code = @"New Jersey";
    }else if([state_name isEqualToString:@"NM"]){
        code = @"New Mexico";
    }else if([state_name isEqualToString:@"NY"]){
        code = @"New York";
    }else if([state_name isEqualToString:@"NC"]){
        code = @"North Carolina";
    }else if([state_name isEqualToString:@"ND"]){
        code = @"North Dakota";
    }else if([state_name isEqualToString:@"OH"]){
        code = @"Ohio";
    }else if([state_name isEqualToString:@"OK"]){
        code = @"Oklahoma";
    }else if([state_name isEqualToString:@"OR"]){
        code = @"Oregon";
    }else if([state_name isEqualToString:@"PA"]){
        code = @"Pennsylvania";
    }else if([state_name isEqualToString:@"RI"]){
        code = @"Rhode Island";
    }else if([state_name isEqualToString:@"SC"]){
        code = @"South Carolina";
    }else if([state_name isEqualToString:@"SD"]){
        code = @"South Dakota";
    }else if([state_name isEqualToString:@"TN"]){
        code = @"Tennessee";
    }else if([state_name isEqualToString:@"TX"]){
        code = @"Texas";
    }else if([state_name isEqualToString:@"UT"]){
        code = @"Utah";
    }else if([state_name isEqualToString:@"VT"]){
        code = @"Vermont";
    }else if([state_name isEqualToString:@"VA"]){
        code = @"Virginia";
    }else if([state_name isEqualToString:@"WA"]){
        code = @"Washington";
    }else if([state_name isEqualToString:@"WV"]){
        code = @"West Virginia";
    }else if([state_name isEqualToString:@"WI"]){
        code = @"Wisconsin";
    }else if([state_name isEqualToString:@"WY"]){
        code = @"Wyoming";
    }
    
    
    //    }else if([state_name isEqualToString:@""]){
    //        code = @"";
    
    return code;
}


-(NSString *)getStateCode:(NSString*)state_name{
    NSString *code = @"";
    
    if([state_name isEqualToString:@"Alabama"]){
        code = @"AL";
    }else if([state_name isEqualToString:@"Alaska"]){
        code = @"AK";
    }else if([state_name isEqualToString:@"Arizona"]){
        code = @"AZ";
    }else if([state_name isEqualToString:@"Arkansas"]){
        code = @"AR";
    }else if([state_name isEqualToString:@"California"]){
        code = @"CA";
    }else if([state_name isEqualToString:@"Colorado"]){
        code = @"CO";
    }else if([state_name isEqualToString:@"Connecticut"]){
        code = @"CT";
    }else if([state_name isEqualToString:@"Delaware"]){
        code = @"DE";
    }else if([state_name isEqualToString:@"Florida"]){
        code = @"FL";
    }else if([state_name isEqualToString:@"Georgia"]){
        code = @"GA";
    }else if([state_name isEqualToString:@"Hawaii"]){
        code = @"HI";
    }else if([state_name isEqualToString:@"Idaho"]){
        code = @"ID";
    }else if([state_name isEqualToString:@"Illinois"]){
        code = @"IL";
    }else if([state_name isEqualToString:@"Indiana"]){
        code = @"IN";
    }else if([state_name isEqualToString:@"Iowa"]){
        code = @"IA";
    }else if([state_name isEqualToString:@"Kansas"]){
        code = @"KS";
    }else if([state_name isEqualToString:@"Kentucky"]){
        code = @"KY";
    }else if([state_name isEqualToString:@"Louisiana"]){
        code = @"LA";
    }else if([state_name isEqualToString:@"Maine"]){
        code = @"ME";
    }else if([state_name isEqualToString:@"Maryland"]){
        code = @"MD";
    }else if([state_name isEqualToString:@"Massachusetts"]){
        code = @"MA";
    }else if([state_name isEqualToString:@"Michigan"]){
        code = @"MI";
    }else if([state_name isEqualToString:@"Minnesota"]){
        code = @"MN";
    }else if([state_name isEqualToString:@"Mississippi"]){
        code = @"MS";
    }else if([state_name isEqualToString:@"Missouri"]){
        code = @"MO";
    }else if([state_name isEqualToString:@"Montana"]){
        code = @"MT";
    }else if([state_name isEqualToString:@"Nebraska"]){
        code = @"NE";
    }else if([state_name isEqualToString:@"Nevada"]){
        code = @"NV";
    }else if([state_name isEqualToString:@"New Hampshire"]){
        code = @"NH";
    }else if([state_name isEqualToString:@"New Jersey"]){
        code = @"NJ";
    }else if([state_name isEqualToString:@"New Mexico"]){
        code = @"NM";
    }else if([state_name isEqualToString:@"New York"]){
        code = @"NY";
    }else if([state_name isEqualToString:@"North Carolina"]){
        code = @"NC";
    }else if([state_name isEqualToString:@"North Dakota"]){
        code = @"ND";
    }else if([state_name isEqualToString:@"Ohio"]){
        code = @"OH";
    }else if([state_name isEqualToString:@"Oklahoma"]){
        code = @"OK";
    }else if([state_name isEqualToString:@"Oregon"]){
        code = @"OR";
    }else if([state_name isEqualToString:@"Pennsylvania"]){
        code = @"PA";
    }else if([state_name isEqualToString:@"Rhode Island"]){
        code = @"RI";
    }else if([state_name isEqualToString:@"South Carolina"]){
        code = @"SC";
    }else if([state_name isEqualToString:@"South Dakota"]){
        code = @"SD";
    }else if([state_name isEqualToString:@"Tennessee"]){
        code = @"TN";
    }else if([state_name isEqualToString:@"Texas"]){
        code = @"TX";
    }else if([state_name isEqualToString:@"Utah"]){
        code = @"UT";
    }else if([state_name isEqualToString:@"Vermont"]){
        code = @"VT";
    }else if([state_name isEqualToString:@"Virginia"]){
        code = @"VA";
    }else if([state_name isEqualToString:@"Washington"]){
        code = @"WA";
    }else if([state_name isEqualToString:@"West Virginia"]){
        code = @"WV";
    }else if([state_name isEqualToString:@"Wisconsin"]){
        code = @"WI";
    }else if([state_name isEqualToString:@"Wyoming"]){
        code = @"WY";
    }
    
    
    //    }else if([state_name isEqualToString:@""]){
    //        code = @"";
    
    return code;
}


-(void)saveMyAccount{
    
//     _app._myStoreRefreshFlag= true;
    
    PersistentData *persistentdata = [_app getPersistentData];
    persistentdata.account = _accountRequest;
    persistentdata._login.storeNumber = _accountRequest.storeNumber;// bug fix
    [_app storePersistentData];
}

-(IBAction)createAccount:(id)sender{
    [self dismissPickerView];
    [self.view endEditing:YES];
    @try {
        if([self validateAllFields]){
            // Create
            _accountRequest.firstName = first_name.text;
            _accountRequest.lastName = last_name.text;
            _accountRequest.email = emailid.text;
            _accountRequest.password = password.text;
            _accountRequest.mobilePhone = mobile.text;
            _accountRequest.phone = home.text;
            _accountRequest.city = city.text;
            
            //            _accountRequest.state = state.text;
            //             _accountRequest.address = @"";
            NSString *country_code = [self getStateCode:state.text];
            _accountRequest.state = country_code;
            _accountRequest.address = str_address.text;
            
            _accountRequest.zip = zip.text;
            _accountRequest.loyaltyNumber = loyaltyNum.text;
            
            NSString *urlString;
            NSString *auth;
            
            NSString          *_dialogText;
            
            if(__registrationPage == YES)
            {
                urlString = ACCOUNT_REGISTRATION_URL;
                auth = nil;
                _dialogText = @"Creating Account...";
                
#ifdef CLP_ANALYTICS
                NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
                params=[[NSMutableDictionary alloc]init];
                [params setObject:@"Create_Account" forKey:@"event"];
                [params setValue:@"CLICK_EVENT" forKey:@"event_name"];
                [params setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
                [_app._clpSDK updateAppEvent:params];
#endif
            }
            else
            {
                urlString = ACCOUNT_UPDATE_URL;
                _dialogText = @"Updating Account...";
                Login *login = [_app getLogin];
                auth = login.authKey;
                _accountRequest.crmNumber = login.crmNumber;
                _accountRequest.accountId = login.accountId;
                
#ifdef CLP_ANALYTICS
                NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
                params=[[NSMutableDictionary alloc]init];
                [params setObject:@"Update_Account" forKey:@"event"];
                [params setValue:@"CLICK_EVENT" forKey:@"event_name"];
                [params setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
                [_app._clpSDK updateAppEvent:params];
#endif
            }
            
            _accountRequest.termsAcceptedFlag = [NSNumber numberWithBool:terms_selection_flag];
            _accountRequest.sendEmailsFlag = [NSNumber numberWithBool:email_trigger_flag];
            _accountRequest.sendTextsFlag = [NSNumber numberWithBool:Text_msg_trigger_flag];
            _accountRequest.issueCardFlag = [NSNumber numberWithBool:shop_Offer_flag];
            
            //            _accountRequest.storeNumber = 114;
            //            _accountRequest.favoriteDept = @"Natural & Organic Foods";
            _accountRequest.favoriteDept = tellus_abt_fav_department_txt.text;
            // _accountRequest.address = @"";
            _accountRequest.prefix = @"";
            
            _progressDialog = [[ProgressDialog alloc] initWithView:self.view message:_dialogText];
            [_progressDialog show];
            
            _service = [[WebService alloc]initWithListener:self responseClassName:@"Login"];
            [_service execute:urlString authorization:auth requestObject:_accountRequest requestType:POST]; // response handled by handleAccountServiceResponse method below
            
        }
    }
    @catch (NSException *exception) {
        
    }
}


- (void)handleAccountServiceResponse:(id)responseObject
{
    
    int status = [_service getHttpStatusCode];
    
    if(status == 200) // service success
    {
        NSString *_dialogText;
        if(__registrationPage == NO)
            _dialogText = @"Account Updated.";
        else
            _dialogText = @"Account Created.";
        
        _textDialog = [[TextDialog alloc] initWithView:self.view title:@"Request Succeeded" message:_dialogText responder:self callback:@selector(removeView)];
        [_textDialog show];
        
        [self saveMyAccount];
    }
    else // service failure
    {
        if(status == 422) // backend or internet unavailable error
        {
            WebServiceError *error = [_service getError];
            
            NSString *titleText;
            
            //            if(_registrationPage == NO)
            //                titleText = @"Account Update Failed";
            //            else
            titleText = @"Account Create Failed";
            
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:titleText message:error.errorMessage];
            [dialog show];
        }
        else if(status == 408) // timeout error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
            [dialog show];
        }
        else // http error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Server Error" message:COMMON_ERROR_MSG];
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


#pragma mark WebServiceListener Delegate
- (void)onServiceResponse:(id)responseObject
{
    [_progressDialog dismiss];
    
    if([responseObject isKindOfClass:[Login class]])
        [self handleAccountServiceResponse:responseObject];
}


#pragma mark - Keyboard

-(void)sukeyboardWillShow:(NSNotification*)sender{
    CGSize size = [[[sender userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey]CGRectValue].size;
    NSTimeInterval interval = [[[sender userInfo] objectForKey:UIKeyboardAnimationDurationUserInfoKey]doubleValue];
    [UIView animateWithDuration:interval animations:^{
        [bgscroll_view setContentInset:UIEdgeInsetsMake(0, 0, size.height, 0)];
        [bgscroll_view setScrollIndicatorInsets:UIEdgeInsetsMake(0, 0, size.height, 0)];
    }];
}

-(void)sukeyboardWillHide:(NSNotification*)sender{
    NSTimeInterval interval = [[[sender userInfo] objectForKey:UIKeyboardAnimationDurationUserInfoKey]doubleValue];
    [UIView animateWithDuration:interval animations:^{
        [bgscroll_view setContentInset:UIEdgeInsetsZero];
        [bgscroll_view setScrollIndicatorInsets:UIEdgeInsetsZero];
    }];
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// picker view delegate begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

# pragma mark - Pickerview Delegate

-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    if(pickerView==_statePicker){
        return 1;
    }
    else if (pickerView==_DepartmentPicker)
    {
        return 1;
    }
    else{
        return 0;
    }
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    if(pickerView==_statePicker){
        return _states.count;
    }
    else if (pickerView==_DepartmentPicker)
    {
        return _departments.count;
    }
    else{
        return 0;
    }
}

-(NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    
    if(pickerView==_statePicker){
        return(NSString*)[_states objectAtIndex:row];
    }
    else if (pickerView==_DepartmentPicker)
    {
        return(NSString*)[_departments objectAtIndex:row];
    }
    else{
        return nil;
    }
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    if(pickerView==_statePicker){
        state.text = [_states objectAtIndex:row];
        //        [self hidePicker:pickerView];
    }
    else if (pickerView==_DepartmentPicker)
    {
        tellus_abt_fav_department_txt.text=[_departments objectAtIndex:row];
        //        [self hidePicker:pickerView];
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// picker view delegate end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


@end
