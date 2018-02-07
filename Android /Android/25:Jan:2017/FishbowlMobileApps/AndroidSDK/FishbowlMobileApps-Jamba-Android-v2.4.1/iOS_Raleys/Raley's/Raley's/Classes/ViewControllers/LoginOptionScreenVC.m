//
//  LoginOptionScreenVC.m
//  Raley's
//
//  Created by Samar Gupta on 5/20/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "LoginOptionScreenVC.h"
#import "ShoppingScreenVC.h"
#import "WebService.h"
#import "TextDialog.h"
#import "Login.h"
#import "LoginRequest.h"
#import "Logging.h"
#import "Utility.h"
#import "ProductDetails.h"
#import "Raleys-Prefix.pch"
#import "SignUpViewController.h"

#define Retina4 ([UIScreen mainScreen].bounds.size.height == 568.f)

@interface LoginOptionScreenVC ()
{
    AppDelegate *_app;
    UIView      *View;
    
    UIImageView *logo_img;
    
    NSMutableArray *imageArray;
    NSInteger image;
    UIImageView *background;
    
    UIScrollView *Image_container;
    
    
    UIView *flip_view;
    
    
    // Set One
    UIButton *fbButton;
    UIButton *mailButton;
    UILabel *login_lbl;
    UIButton *regButton;
    
    
    // Set Two
    
    UITextField *email_id;
    UITextField *pwd;
    
    UIButton *login_button;
    UIButton *forgot_pwd;
    
    NSTimer *timer;
    UIScrollView *_scrollView;
    
    NSMutableArray *Highlights;
    UILabel *highlight_text;
    
    UIImageView *bottom_logo_small;
    
    //    UIButton *back_Button;
    
    
}
@end

@implementation LoginOptionScreenVC

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
    [super viewDidLoad];
    _app = (id)[[UIApplication sharedApplication] delegate];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWasShown:) name:UIKeyboardDidShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillBeHidden:) name:UIKeyboardWillHideNotification object:nil];
    
    // last part of the fix the status bar overlap problem on iOS7, also see AppDelegate:didFinishLaunchingWithOptions
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
    {
        View = [[UIView alloc] initWithFrame:CGRectMake(0, 20, self.view.frame.size.width, self.view.frame.size.height - 20)];
    }
    else{
        View = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    }
    
    // scroll view
    _scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    _scrollView.delegate = self;
    _scrollView.contentSize = CGSizeMake(_app._viewWidth,  _app._viewHeight);
    [_scrollView setScrollEnabled:YES];
    _scrollView.bounces = NO;
    
    
    UITapGestureRecognizer *dismiss_tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(login_dismiss_keyboard)];
    [dismiss_tap setNumberOfTapsRequired:1];
    [dismiss_tap setNumberOfTouchesRequired:1];
    [_scrollView addGestureRecognizer:dismiss_tap];
    
    [self.view addSubview:_scrollView];
    
    [_scrollView addSubview:View];
    
    
    Image_container=[[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, _app._viewWidth, _app._viewHeight)];
    [Image_container setBackgroundColor:[UIColor clearColor]];
    Image_container.delegate = self;
    
    Image_container.contentSize = CGSizeMake(_app._viewWidth+80.0,_app._viewHeight+40.0);
    [Image_container setShowsHorizontalScrollIndicator:NO];
    [Image_container setShowsVerticalScrollIndicator:NO];
    [Image_container setUserInteractionEnabled:NO];
    
    // Do any additional setup after loading the view.
    background = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _app._viewWidth+80.0, _app._viewHeight+40.0)];
    [background setImage:[UIImage imageNamed:@"bg_loginoption"]];
    
    [Image_container addSubview:background];
    
    //logo_img=[[UIImageView alloc]initWithFrame:CGRectMake(_app._viewWidth * .21,_app._viewHeight * .06,186,78)];
    logo_img=[[UIImageView alloc]initWithFrame:CGRectMake(_app._viewWidth * .165,_app._viewHeight * .06,210,85)];
    [logo_img setImage:[UIImage imageNamed:@"raleys_logo.png"]];
    
    bottom_logo_small=[[UIImageView alloc]initWithFrame:CGRectMake(70,_app._viewHeight * .94,180,24)];
    [bottom_logo_small setImage:[UIImage imageNamed:@"raleys_trilogo_white.png"]];
    
    
    highlight_text=[[UILabel alloc]initWithFrame:CGRectMake(10,_app._viewHeight * .28,300,50.0)];
    [highlight_text setText:@"For food. For family. For you."];
    [highlight_text setNumberOfLines:4];
    [highlight_text setTextAlignment:NSTextAlignmentCenter];
    [highlight_text setFont:[UIFont fontWithName:_app._loginFont size:34.0f]];
    [highlight_text setTextColor:[UIColor whiteColor]];
    [highlight_text setAlpha:0.8];
    [highlight_text setBackgroundColor:[UIColor clearColor]];
    
    [View addSubview:Image_container];
    
    
    // int buttonXOrigin = _app._viewWidth * .07;
    
    // UIButton *fbButton = [[UIButton alloc] initWithFrame:CGRectMake(buttonXOrigin, _app._viewHeight * .70, 275.0, 47.0)];
    //    fbButton = [[UIButton alloc] initWithFrame:CGRectMake(27,42,265,45)];
    [fbButton setBackgroundImage:[UIImage imageNamed:@"facebook_button@2x.png"] forState:UIControlStateNormal];
    [fbButton addTarget:self action:@selector(fbButtonPressed) forControlEvents:UIControlEventTouchDown];
    //    [View addSubview:fbButton];
    
    //UIButton *mailButton = [[UIButton alloc] initWithFrame:CGRectMake(buttonXOrigin, _app._viewHeight * .82, 275.0, 47.0)];
    //  mailButton = [[UIButton alloc] initWithFrame:CGRectMake(27,93,265,45)];
    mailButton =[[UIButton alloc] initWithFrame:CGRectMake(27,42,265,45)];
    [mailButton setBackgroundImage:[UIImage imageNamed:@"email_button@2x.png"] forState:UIControlStateNormal];
    [mailButton addTarget:self action:@selector(mailIDLogin) forControlEvents:UIControlEventTouchDown];
    [mailButton.layer setCornerRadius:5.0f];
    [mailButton.layer setBorderWidth:0.7f];
    [mailButton.layer setBorderColor:[UIColor whiteColor].CGColor];
    mailButton.clipsToBounds=YES;
    
    //    [View addSubview:mailButton];
    
    
    // UILabel *login_lbl=[[UILabel alloc]initWithFrame:CGRectMake(_app._viewWidth * .08, _app._viewHeight * .93,165,30)];
    //login_lbl=[[UILabel alloc]initWithFrame:CGRectMake(27,145,160,30)];
    login_lbl=[[UILabel alloc]initWithFrame:CGRectMake(27,173,160,30)];
    [login_lbl setText:@"Don’t have an account?"];
    [login_lbl setTextAlignment:NSTextAlignmentLeft];
    [login_lbl setTextColor:[UIColor whiteColor]];
    [login_lbl setFont:[UIFont fontWithName:_app._normalFont size:font_size13]];
    [login_lbl setBackgroundColor:[UIColor clearColor]];
    
    //UIButton *regButton = [[UIButton alloc] initWithFrame:CGRectMake(_app._viewWidth * .60, _app._viewHeight * .93, 100.0, 30.0)];
    regButton = [[UIButton alloc] initWithFrame:CGRectMake(194,173,100,30)];
    [regButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [regButton setTitle:@"Register now" forState:UIControlStateNormal];
    [regButton.titleLabel setFont:[UIFont fontWithName:_app._boldFont size:font_size13]];
    
    [regButton setBackgroundColor:[UIColor clearColor]];
    [regButton addTarget:self action:@selector(regBtnClicked) forControlEvents:UIControlEventTouchDown];
    
    
    if(!Retina4)
    {
        flip_view=[[UIView alloc]initWithFrame:CGRectMake(0,_app._viewHeight * .48,320,220)];
    }
    else
    {
        flip_view=[[UIView alloc]initWithFrame:CGRectMake(0,_app._viewHeight * .54,320,220)];
    }
    
    
    //    //UIButton *regButton = [[UIButton alloc] initWithFrame:CGRectMake(_app._viewWidth * .60, _app._viewHeight * .93, 100.0, 30.0)];
    //     back_Button = [[UIButton alloc] initWithFrame:CGRectMake(flip_view.frame.size.width-(25*2),20,23,23)];
    //    [back_Button setBackgroundImage:[UIImage imageNamed:@"login_close"] forState:UIControlStateNormal];
    //    [back_Button addTarget:self action:@selector(reset_login:) forControlEvents:UIControlEventTouchUpInside];
    //
    //    [back_Button setHidden:YES];
    [flip_view setBackgroundColor:[UIColor clearColor]];
    
    //    [flip_view addSubview:back_Button];
    
    //    [flip_view addSubview:fbButton];
    //    [flip_view addSubview:mailButton];
    [flip_view addSubview:login_lbl];
    [flip_view addSubview:regButton];
    
    [self Login_controls];
    
    [View insertSubview:flip_view aboveSubview:background];
    
    
    
    [View insertSubview:logo_img aboveSubview:background];
    [View insertSubview:highlight_text aboveSubview:background];
    [View insertSubview:bottom_logo_small aboveSubview:background];
    
    
    [self imageAnimation];
    [self performTransition];
}

-(void)viewWillAppear:(BOOL)animated{
   // [self login_dismiss_keyboard];
}


- (void)imageAnimation {
    
    //take an array of image , you can take many image as you think for variation
    imageArray =[NSMutableArray new];
    [imageArray addObject:[UIImage imageNamed:@"fresh.png"]]; // dublicate index for loading at first // Pending Images
    [imageArray addObject:[UIImage imageNamed:@"fresh.png"]];
    [imageArray addObject:[UIImage imageNamed:@"desert.png"]];
    [imageArray addObject:[UIImage imageNamed:@"soup.png"]];
    [imageArray addObject:[UIImage imageNamed:@"juice.png"]];
    [imageArray addObject:[UIImage imageNamed:@"bg_loginoption.png"]];
    
    Highlights=[NSMutableArray new];
    [Highlights addObject:@""];// dublicate index for loading at first
    [Highlights addObject:@"Everyday Fresh"];
    [Highlights addObject:@"Fine Desserts"];
    [Highlights addObject:@"Inspired Meals"];
    [Highlights addObject:@"Local & Organic "];
    [Highlights addObject:@"For food. For family. For you."];
    
    
    image=0;
    //UIImageView *editBackground
    [background setImage:[imageArray objectAtIndex:image]];
    image++;
    
    // Set the timer for image transition fired
    timer=[NSTimer scheduledTimerWithTimeInterval:2.0 target:self selector:@selector(performTransition) userInfo:nil repeats:YES];
}
//perform image transition
-(void)performTransition
{
    
    [Image_container setContentOffset:CGPointMake(0, 0)]; // Scrollview reset
    
    CGRect text_frame=highlight_text.frame;
    CGRect logo_frame=logo_img.frame;
    CGPoint moving_size;
    if(image != 5) // animation right to left first 4 images
    {
        text_frame.size.height=60.0f;
        // text_frame.origin.y=(_app._viewHeight/2)-(text_frame.size.height/2);
        text_frame.origin.y=_app._viewHeight * .22;
        // logo_frame.origin.y=_app._viewHeight * .20;
        logo_frame.origin.y=_app._viewHeight * .06;
        moving_size=CGPointMake(80, 0);// Scrollview scrolling to top
        
        [highlight_text setFont:[UIFont fontWithName:_app._loginFont size:34.0f]];
        
    }
    else // animation bottom to top last image
    {
        text_frame.origin.y=_app._viewHeight * .22;
        text_frame.size.height=60.0f;
        logo_frame.origin.y=_app._viewHeight * .06;
        moving_size=CGPointMake(0, 40);// Scrollview scrolling to top
        //[highlight_text setFont:[UIFont fontWithName:_app._loginFont size:25.0f]];
        [highlight_text setFont:[UIFont fontWithName:_app._normalFont size:19.0f]]; //25.0f
    }
    
    [highlight_text setFrame:text_frame];
    [logo_img setFrame:logo_frame];
    
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:2.0]; // scroll to top animation duration
    [UIView setAnimationDelay:0.01];
    [UIView setAnimationCurve:UIViewAnimationCurveEaseIn];
    [Image_container setContentOffset:moving_size];
    [UIView commitAnimations];
    
    [background setImage:[imageArray objectAtIndex:image]];
    
    CATransition *transition = [CATransition animation];
    transition.duration = 0.5; // fade animation duration
    transition.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
    transition.type = kCATransitionFade;
    transition.delegate = self;
    
    [Image_container.layer addAnimation:transition forKey:nil];
    [highlight_text.layer addAnimation:transition forKey:nil];
    [highlight_text setText:[NSString stringWithFormat:@"%@",[Highlights objectAtIndex:image]]];
    
    image++;
    
    // [self Login_controls:image];
    
    if(image == [imageArray count]){
        image=0;
        [timer invalidate];
    }
}

-(void)Login_controls
{
    
    // Set Two
    email_id=[[UITextField alloc]initWithFrame:CGRectMake(27,21,265,45)];
    [email_id setTextAlignment:NSTextAlignmentLeft];
    [email_id setTextColor:[UIColor blackColor]];
    [email_id setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
    [email_id setBackground:[UIImage imageNamed:@"categories_bar_new.png"]];
    [email_id setPlaceholder:@"Email"];
    email_id.delegate = self;
    email_id.keyboardType = UIKeyboardTypeEmailAddress;
    email_id.returnKeyType = UIReturnKeyNext;
    email_id.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    email_id.autocapitalizationType = UITextAutocapitalizationTypeNone;
    email_id.alpha=0.7;
    email_id.autocorrectionType = UITextAutocorrectionTypeNo;  // ios 8 fixes to stopping defrault suggestions texts.
    
    [email_id.layer setCornerRadius:5.0f];
    [email_id.layer setBorderWidth:1.0f];
    [email_id.layer setBorderColor:[UIColor whiteColor].CGColor];
    email_id.clipsToBounds=YES;
    
    UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, email_id.frame.size.height)];
    leftView.backgroundColor = email_id.backgroundColor;
    email_id.leftView = leftView;
    email_id.leftViewMode = UITextFieldViewModeAlways;
    
    pwd=[[UITextField alloc]initWithFrame:CGRectMake(27,68,265,45)];
    [pwd setTextAlignment:NSTextAlignmentLeft];
    [pwd setTextColor:[UIColor blackColor]];
    [pwd setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
    [pwd setBackground:[UIImage imageNamed:@"categories_bar_new.png"]];
    [pwd setSecureTextEntry:YES];
    [pwd setPlaceholder:@"Password"];
    pwd.delegate = self;
    pwd.keyboardType = UIKeyboardTypeDefault;
    pwd.returnKeyType = UIReturnKeyDone;
    pwd.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    pwd.autocapitalizationType = UITextAutocapitalizationTypeNone;
    pwd.alpha=0.7;
    [pwd.layer setCornerRadius:5.0f];
    [pwd.layer setBorderWidth:1.0f];
    [pwd.layer setBorderColor:[UIColor whiteColor].CGColor];
    pwd.clipsToBounds=YES;
    
    
    leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, pwd.frame.size.height)];
    leftView.backgroundColor = pwd.backgroundColor;
    pwd.leftView = leftView;
    pwd.leftViewMode = UITextFieldViewModeAlways;
    
    
    login_button=[[UIButton alloc]initWithFrame:CGRectMake(27,122,265,45)];
    [login_button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [login_button setTitle:@"Login" forState:UIControlStateNormal];
    [login_button.titleLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size20]];
    [login_button setBackgroundColor:[UIColor clearColor]];
    [login_button setBackgroundImage:[UIImage imageNamed:@"login_button_red_plain.png"] forState:UIControlStateNormal];
    [login_button addTarget:self action:@selector(loginButtonPressed) forControlEvents:UIControlEventTouchDown];
    login_button.alpha=0.7;
    [login_button.layer setCornerRadius:5.0f];
    [login_button.layer setBorderWidth:1.0f];
    [login_button.layer setBorderColor:[UIColor whiteColor].CGColor];
    login_button.clipsToBounds=YES;
    
    
    //    forgot_pwd=[[UIButton alloc]initWithFrame:CGRectMake(22,210,275,22)];
    //    [forgot_pwd setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    //    [forgot_pwd setTitle:@"Forgot your password?" forState:UIControlStateNormal];
    //    [forgot_pwd.titleLabel setFont:[UIFont fontWithName:_app._boldFont size:font_size13]];
    //    [forgot_pwd setBackgroundColor:[UIColor clearColor]];
    //    [forgot_pwd.titleLabel setTextAlignment:NSTextAlignmentCenter];
    
    [flip_view addSubview:email_id];
    [flip_view addSubview:pwd];
    [flip_view addSubview:login_button];
    //    [flip_view addSubview:forgot_pwd];
    
}


-(IBAction)reset_login:(id)sender
{
    [email_id resignFirstResponder];
    [pwd resignFirstResponder];
    
    //    [back_Button setHidden:YES];
    
    [email_id setHidden:YES];
    [pwd setHidden:YES];
    [login_button setHidden:YES];
    [forgot_pwd setHidden:YES];
    
    [fbButton setHidden:NO];
    [mailButton setHidden:NO];
    [login_lbl setHidden:NO];
    [regButton setHidden:NO];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)mailIDLogin
{
    //   [timer invalidate];
    
    // [self Login_controls];
    
    //    [back_Button setHidden:NO];
    
    [email_id setHidden:NO];
    [pwd setHidden:NO];
    [login_button setHidden:NO];
    [forgot_pwd setHidden:NO];
    
    //    [fbButton setHidden:YES];
    //    [mailButton setHidden:YES];
    //    [login_lbl setHidden:YES];
    //    [regButton setHidden:YES];
    
}
-(void)fbButtonPressed
{
    
}

-(void)regBtnClicked
{
    if(_activeField!=nil){ // ios 8 fixes. Resetting _scrollview frame when back to login screen from sign up screen.
         [self login_dismiss_keyboard];
    }
    
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
    SignUpViewController *viewctrl = [[SignUpViewController alloc]initWithNibName:@"SignUpViewController" bundle:nil];
    viewctrl._registrationPage = YES;
    [self presentViewController:viewctrl animated:NO completion:nil];
    
#ifdef CLP_ANALYTICS
    NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
    //new
    [params setObject:@"Register_Now" forKey:@"event"];
    [params setValue:@"CLICK_EVENT" forKey:@"event_name"];
    [params setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
    [_app._clpSDK updateAppEvent:params];
#endif
    //    AccountScreenVC *accountScreenVC = [[AccountScreenVC alloc] init];
    //    accountScreenVC._registrationPage = YES;
    //    [self presentViewController:accountScreenVC animated:YES completion:nil];
}


- (void)loginButtonPressed
{
    [email_id resignFirstResponder];
    [pwd resignFirstResponder];
    
    
    if([self fieldsValid])
    {
        LoginRequest *request = [[LoginRequest alloc] init];
        request.email = _email;
        request.password = _password;
        request.platform = @"iOS";
        request.platformVersion = [[UIDevice currentDevice] systemVersion];
        
        [Raleys shared].password = request.password;
        
        _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Validating credentials..."];
        //        _progressDialog=[[ProgressDialog alloc]init];
        [_progressDialog show];
        
        if(_service==NULL){
            _service = [[WebService alloc]initWithListener:self responseClassName:@"Login"];
            [_service execute:LOGIN_URL authorization:nil requestObject:request requestType:POST]; // response handled by handleLoginServiceResponse method below
            
        }
    }
    
#ifdef CLP_ANALYTICS
    NSMutableDictionary *params=[[NSMutableDictionary alloc]init];
    [params setObject:[NSDate date] forKey:@"time"];
    [params setObject:@"Login" forKey:@"Event_Name"];
    [params setValue:@"Login" forKey:@"event_name"];
    [params setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
    [_app._clpSDK updateAppEvent:params];
#endif
    
}


- (void)handleLoginServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];
    
    if(status == 200) // service success
    {
        _service = nil;
        Login *login = (Login *)responseObject;
        
        if(login == nil || login.accountId == nil || login.crmNumber == nil)
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:@"Unable to parse data returned from server."];
            [dialog show];
            return;
        }
        
        login.password = [Raleys shared].password;
        [_app storeLogin:login];
        [_app getAvailableOffers];
        
        
        NSString *_Existing_user=login.accountId;
        
        if(![[NSUserDefaults standardUserDefaults] valueForKey:login.accountId]){
            
//            [[NSUserDefaults standardUserDefaults] setValue:login.login forKey:login.accountId];
            
            [[NSUserDefaults standardUserDefaults] setBool:FALSE forKey:_Existing_user];
            
            [[NSUserDefaults standardUserDefaults] setBool:FALSE forKey:@"_isListsPage"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            
            [[NSUserDefaults standardUserDefaults] setBool:FALSE forKey:@"_isMapListPage"];
            [[NSUserDefaults standardUserDefaults] synchronize];
        }
        else{
            [[NSUserDefaults standardUserDefaults] setBool:TRUE forKey:@"_isListsPage"];
            [[NSUserDefaults standardUserDefaults] synchronize];
            
            [[NSUserDefaults standardUserDefaults] setBool:TRUE forKey:@"_isMapListPage"];
            [[NSUserDefaults standardUserDefaults] synchronize];
        }
        
        
        [self loginSuccess];
        
    }
    else // service failure
    {
        if(status == 422) // backend or internet unavailable error
        {
            WebServiceError *error = [_service getError];
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Login Failed" message:error.errorMessage];
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
        
        _service = nil;
        return;
    }
}


-(void)loginSuccess{
    
    
    ShoppingScreenVC *shoppingScreenVC = [[ShoppingScreenVC alloc] init];
    _app._shoppingScreenVC = shoppingScreenVC;
    
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
    [self presentViewController:shoppingScreenVC animated:NO completion:nil];
    
    
#ifdef CLP_ANALYTICS
        //Analytics
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        //new
        [data setValue:@"SignedIn" forKey:@"event_name"];
        [data setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
        [_app._clpSDK updateAppEvent:data];
    
#endif
    
}


- (BOOL)fieldsValid
{
    [pwd resignFirstResponder];
    [email_id resignFirstResponder];
    NSString *errorText = nil;
    
    if(email_id.text == nil || [email_id.text isEqualToString:@""])
    {
        errorText = @"Email is mandatory";
        _useEmailResponder = YES;
        _usePasswordResponder = NO;
    }
    else if([Utility validateEmail:email_id.text] == NO)
    {
        errorText = @"Email format is invalid";
        _useEmailResponder = YES;
        _usePasswordResponder = NO;
    }
    else if(pwd.text == nil || [pwd.text isEqualToString:@""])
    {
        errorText = @"Password is mandatory";
        _usePasswordResponder = YES;
        _useEmailResponder = NO;
    }
    
    if(errorText != nil)
    {
        _editErrorDialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:errorText responder:self callback:@selector(returnToEdit)];
        [_editErrorDialog show];
        return NO;
    }
    
    return YES;
}


- (void)returnToEdit
{
    [_editErrorDialog close];
    
    if(_useEmailResponder == YES)
        [email_id becomeFirstResponder];
    else if(_usePasswordResponder == YES)
        [pwd becomeFirstResponder];
}


// UIKeyboardDidShowNotification handler
- (void)keyboardWasShown:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
    CGSize keyboardSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    [ _scrollView setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height-keyboardSize.height)];
    
    [self performSelector:@selector(scrollToTop:) withObject:aNotification afterDelay:0.1];
}

-(void)scrollToTop:(NSNotification*)aNotification{
    NSDictionary* info = [aNotification userInfo];
    CGSize keyboardSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    CGPoint scrollPoint = _scrollView.contentOffset;
    scrollPoint.y = keyboardSize.height;
    [_scrollView setContentOffset:scrollPoint animated:YES];
}

// UIKeyboardWillHideNotification handler
- (void)keyboardWillBeHidden:(NSNotification*)aNotification
{
    NSTimeInterval interval = [[[aNotification userInfo]objectForKey:UIKeyboardAnimationDurationUserInfoKey]doubleValue];
    
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:(interval/2)]; // scroll to top animation duration
    [self login_dismiss_keyboard];
    
    [UIView commitAnimations];
}


-(void)login_dismiss_keyboard
{
    _activeField = nil;
    [pwd resignFirstResponder];
    [email_id resignFirstResponder];
    [ _scrollView setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
}
-(void)backButtonClicked
{
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [self.presentingViewController dismissViewControllerAnimated:NO completion:nil];
    
}

#pragma mark UITextField Delegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if(textField == email_id)
    {
        [email_id resignFirstResponder];
        
        [pwd becomeFirstResponder];
    }
    else // _passwordTextField
    {
        [pwd resignFirstResponder];
        //[self loginButtonPressed];
    }
    
    return YES;
}



- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    _activeField = textField;
}


- (void)textFieldDidEndEditing:(UITextField *)textField
{
    _activeField = nil;
    
    if(textField == email_id)
    {
        _email = email_id.text;
        [email_id resignFirstResponder];
    }
    else // _passwordTextField
    {
        _password = pwd.text;
        [pwd resignFirstResponder];
    }
}


#pragma mark WebServiceListener Delegate
- (void)onServiceResponse:(id)responseObject
{
    [_progressDialog dismiss];
    
    if([responseObject isKindOfClass:[Login class]])
        [self handleLoginServiceResponse:responseObject];
}

@end
