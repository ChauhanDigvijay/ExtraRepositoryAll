//
//  ForgotPassword.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 28/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "ForgotPassword.h"
#import "ModelClass.h"
#import "ApiClasses.h"
#import "clpsdk.h"
#import "Constant.h"
@interface ForgotPassword ()
{
    ModelClass  * obj;
    ApiClasses  * apiCall;
    clpsdk      * clpObj;

}
@property (unsafe_unretained, nonatomic) IBOutlet UITextField * emailTF;
@property (weak, nonatomic) IBOutlet UIView *forgotPasswordView;

@end

@implementation ForgotPassword

- (void)viewDidLoad {
    [super viewDidLoad];
    //[self.emailTF becomeFirstResponder];
    
    // data fetch from mobilesetting
    
    obj=[ModelClass sharedManager];
    [obj RemoveBottomView];
    
   // NSString * str = [obj reterieveuserDefaultData:@"mobileSetting"];
    
//    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
//    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
//    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    [self.emailTF setValue:[UIColor blackColor]
                forKeyPath:@"_placeholderLabel.textColor"];
    
    
    // button background color
    //self.sendBtn.backgroundColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    // background image
//    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
//    NSURL *url2 = [NSURL URLWithString:
//                   str2];
//    [self.backgroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    [self shadoOffect:self.forgotPasswordView];
    
    [self getTokenApi];
    
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    [obj RemoveBottomView];
}

// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    
}


# pragma mark - memoryWarning Method

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}




# pragma mark - TextField Delegate Method

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
   [textField resignFirstResponder];
    return YES;
}


// back button

- (IBAction)backButton_Action:(id)sender
{
    //[self.delegate forgotPasswordBackButtonAction];
   [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - Email validation

- (BOOL)validateEmailWithString:(NSString*)email
{
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@",emailRegex];
    return [emailTest evaluateWithObject:email];
}


# pragma mark - Send Mail Method

- (IBAction)sendBtn_Action:(id)sender
{
    NSString * message;
    
    if(self.emailTF.text.length == 0)
    {
        message = @"Email cannot be blank.";
        [self alertViewDelegate:message];
    }
    
   else if(![self validateEmailWithString:self.emailTF.text] )
    {
        message = @"Please enter a valid Email ID.";
        [self alertViewDelegate:message];
        
    }
    else
    {
        [self forgotPasswordApi];
    }
}


#pragma mark - getToken Api

-(void)getTokenApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    
    [dict setValue:clientID forKey:@"clientId"];
    [dict setValue:ClientSecret forKey:@"clientSecret"];
    [dict setValue:[apiCall deviceID] forKey:@"deviceId"];
    [dict setValue:TanentID forKey:@"tenantId"];
    
    [apiCall getTokenApi:dict url:@"/mobile/getToken" withTarget:self withSelector:@selector(getToken:)];
    dict = nil;
    apiCall = nil;
}

-(void)getToken:(id)response
{
    [obj removeLoadingView:self.view];

      NSLog(@"login response------%@",response);
    
    NSLog(@"login response------%@",response);
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        [obj saveUserDefaultData:[response valueForKey:@"message"] and:@"access_token"];
        NSLog(@"[response valueForKey is %@",[response valueForKey:@"message"]);

    }
    else
    {
        [self alertViewDelegate:[response valueForKey:@"message"]];
    }
}



#pragma mark - Forgot Password Api

-(void)forgotPasswordApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    [dict setValue:self.emailTF.text forKey:@"email"];
    [apiCall ForgotPasswordApi:dict url:@"/member/forgetPassMail" withTarget:self withSelector:@selector(ForgotPasswordApi:)];
    dict = nil;
    apiCall = nil;
}


// logout api response
-(void)ForgotPasswordApi:(id)response
{
     [obj removeLoadingView:self.view];
    
      NSLog(@"forgot password response------%@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        // event tracking method
        
        clpObj = [clpsdk sharedInstanceWithAPIKey];
        
        NSMutableDictionary * eventDic = [NSMutableDictionary new];
        [eventDic setValue:@"FORGOT_PASSWORD" forKey:@"event_name"];
        [clpObj updateAppEvent:eventDic];
        eventDic = nil;
        [self alertViewDelegate:[response valueForKey:@"message"]];
    }
    else
    {
        [self alertViewDelegate:[response valueForKey:@"message"]];
    }
}

// alert view method
-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
}







@end
