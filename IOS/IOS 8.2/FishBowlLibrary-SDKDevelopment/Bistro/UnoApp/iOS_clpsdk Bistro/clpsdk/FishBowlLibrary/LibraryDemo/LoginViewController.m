//
//  LoginViewController.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 19/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "LoginViewController.h"
#import "ApiClasses.h"
#import "ModelClass.h"
#import "clpsdk.h"
#import "ForgotPassword.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "StoreSearchView.h"
#import "SignUpViewController.h"
#import "HomeViewController.h"
#import "ForgotPassword.h"



@interface LoginViewController ()<StoreSearch>
{
    BOOL            isExist;
    ApiClasses    * apiCall;
    ModelClass    * obj;
    BOOL            isApiCorrect;
    clpsdk        * clpsdkObj;
    NSDictionary  * resultDict;
    NSString      * storeNumberValue;
    NSString      * storeNumberString;
    BOOL            isRegistration;
    clpsdk        * clpObj;
    NSString      * userName;
    
}
@property (weak, nonatomic) IBOutlet UIView      * emailView;
@property (weak, nonatomic) IBOutlet UIButton    * registerBtn;
@property (weak, nonatomic) IBOutlet UIView      * passwordView;
@property (weak, nonatomic) IBOutlet UIImageView * backgroundImage;
@property (weak, nonatomic) IBOutlet UIView      * signInView;


@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSLog(@"viewDid Load");
    
    
    isExist = NO;
    isApiCorrect = NO;
 
    [self.emailTF setValue:[UIColor blackColor]
                    forKeyPath:@"_placeholderLabel.textColor"];
    
    [self.passwordTF setValue:[UIColor blackColor]
                forKeyPath:@"_placeholderLabel.textColor"];

    if ([[NSUserDefaults standardUserDefaults]boolForKey:@"login"]==YES)
    {
        [self themeSettingsAPI];
        NSLog(@"calling theme while home");

        HomeViewController * homeObj = [[HomeViewController alloc]initWithNibName:@"HomeViewController" bundle:nil];
        [self.navigationController pushViewController:homeObj animated:NO];
    }
    else
    {
        obj=[ModelClass sharedManager];
        if([obj checkNetworkConnection])
        {
            // call mobile Setting Api
            [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"SignUp"];
            dispatch_queue_t myQueue = dispatch_queue_create("My Queue",NULL);
            dispatch_async(myQueue, ^{
                // Perform long running process
                dispatch_async(dispatch_get_main_queue(), ^{
                    // Update the UI
                  //  [self mobileSettingApi];
                   // [self getTokenApi];
                    [self themeSettingsAPI];

                    
                });
            });
        }
        else
        {
           // [self alertViewDelegate:@"Please check your network connection"];
        }
        
    }
    [obj removeLoadingView:self.view];
    //[self shadoOffect:self.signInView];
    

}






#pragma mark - getToken Api

-(void)getTokenApi
{
    //[obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    
    [dict setValue:@"testertesting@gmail.com" forKey:@"username"];
    [dict setValue:@"123456" forKey:@"password"];
    
    [apiCall getTokenApi:dict url:@"/member/login" withTarget:self withSelector:@selector(getToken:)];
    dict = nil;
    apiCall = nil;
}

-(void)getToken:(id)response
{
    NSLog(@"login response------%@",response);
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        [obj saveUserDefaultData:[response valueForKey:@"accessToken"] and:@"access_token"];
        // clp mobile setting api
        clpObj = [clpsdk sharedInstanceWithAPIKey];
    }
    else
    {
        
    }
}

// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    
}


-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    
  // button corner radius
    
//    self.signInBtn.layer.masksToBounds = YES;
//    self.signInBtn.layer.cornerRadius = self.signInBtn.frame.size.height/2.0;
//    self.signInBtn.layer.borderWidth = 1.0;
//    self.signInBtn.layer.borderColor = [[UIColor clearColor] CGColor];
    
    
    [obj removeLoadingView:self.view];
    [obj RemoveBottomView];
    obj=[ModelClass sharedManager];

    
    if([obj checkNetworkConnection])
    {
        // call mobile Setting Api

        if ([[NSUserDefaults standardUserDefaults]boolForKey:@"SignUp"]==NO)
        {
            dispatch_queue_t myQueue = dispatch_queue_create("My Queue",NULL);
            dispatch_async(myQueue, ^{
                // Perform long running process
                dispatch_async(dispatch_get_main_queue(), ^{
                    // Update the UI
            [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"SignUp"];
                  //  [self mobileSettingApi];
                    [self themeSettingsAPI];
                    
                });
            });
        }
       
    }
    
    else
    {
        //[self alertViewDelegate:@"Please check your network connection"];
    }
    
   
    
}


#pragma mark - mobile setting api

-(void)mobileSettingApi
{
    [obj addLoadingView:self.view];
    
    apiCall=[ApiClasses sharedManager];
    [apiCall mobileSettingAPI:nil url:@"/loyalty/viewLoyaltySettings" withTarget:self withSelector:@selector(mobileSettingApi:)];
    apiCall = nil;
}


#pragma mark - mobile setting api

-(void)themeSettingsAPI
{
    [obj addLoadingView:self.view];
    
    apiCall=[ApiClasses sharedManager];
    [apiCall mobileSettingAPI:nil url:@"/theme/getDefaultThemeForAllPages" withTarget:self withSelector:@selector(themeSettingApi:)];
    apiCall = nil;
}


-(void)themeSettingApi:(id)response

{
    
    [obj removeLoadingView:self.view];

    NSLog(@"theme settings response------%@",response);
    
    [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"isStoreFirstTime"];

    
    NSLog(@"Theme setting archieve srart");

    if(response!=nil)
    {
        clpObj = [clpsdk sharedInstanceWithAPIKey];
        
        dispatch_queue_t myQueue = dispatch_queue_create("My Queue",NULL);
        dispatch_async(myQueue, ^{
            // Perform long running process
            dispatch_async(dispatch_get_main_queue(), ^{
                
                //              if ([response valueForKey:@"checkInButtonColor"]!= ( NSString *)[NSNull null])
                //              {
                //                self.signInBtn.backgroundColor = [obj colorWithHexString:[response valueForKey:@"checkInButtonColor"]];
                //
                //              }
                NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
                NSData *data = [NSKeyedArchiver archivedDataWithRootObject:response];
                [currentDefaults setObject:data forKey:@"themeSetting"];
                [currentDefaults synchronize];
                NSLog(@"Theme setting archieve done");
                
                //                if ([response valueForKey:@"companyLogoImageUrl"]!= ( NSString *)[NSNull null])
                //                {
                //                NSString * str = [NSString stringWithFormat:@"http://%@",[response valueForKey:@"companyLogoImageUrl"]];
                //                NSURL *url = [NSURL URLWithString:
                //                              str];
                //                [self.companyLogoImage sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
                //                }
                
                // background image
                
                //                if ([response valueForKey:@"loginBackgroundImageUrl"]!= ( NSString *)[NSNull null])
                //                {
                //                NSString * str2 = [NSString stringWithFormat:@"http://%@",[response valueForKey:@"loginBackgroundImageUrl"]];
                //                NSURL *url2 = [NSURL URLWithString:
                //                               str2];
                //                [self.backGroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
                //                }
                
                dispatch_queue_t myQueue = dispatch_queue_create("My Queue",NULL);
                dispatch_async(myQueue, ^{
                    // Perform long running process
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self storeApi];
                    });
                });
                
                
            });
        });
    }
    

    
    

}
// api response

-(void)mobileSettingApi:(id)response
{
     [obj removeLoadingView:self.view];
     NSLog(@"mobileSettingApi response------%@",response);
     [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"isStoreFirstTime"];

    if(response!=nil)
    {
         clpObj = [clpsdk sharedInstanceWithAPIKey];
        
        dispatch_queue_t myQueue = dispatch_queue_create("My Queue",NULL);
        dispatch_async(myQueue, ^{
            // Perform long running process
            dispatch_async(dispatch_get_main_queue(), ^{
                
//              if ([response valueForKey:@"checkInButtonColor"]!= ( NSString *)[NSNull null])
//              {
//                self.signInBtn.backgroundColor = [obj colorWithHexString:[response valueForKey:@"checkInButtonColor"]];
//                  
//              }
                NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
                NSData *data = [NSKeyedArchiver archivedDataWithRootObject:response];
                [currentDefaults setObject:data forKey:@"mobileSetting"];
                
//                if ([response valueForKey:@"companyLogoImageUrl"]!= ( NSString *)[NSNull null])
//                {
//                NSString * str = [NSString stringWithFormat:@"http://%@",[response valueForKey:@"companyLogoImageUrl"]];
//                NSURL *url = [NSURL URLWithString:
//                              str];
//                [self.companyLogoImage sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
//                }
                
       // background image
                
//                if ([response valueForKey:@"loginBackgroundImageUrl"]!= ( NSString *)[NSNull null])
//                {
//                NSString * str2 = [NSString stringWithFormat:@"http://%@",[response valueForKey:@"loginBackgroundImageUrl"]];
//                NSURL *url2 = [NSURL URLWithString:
//                               str2];
//                [self.backGroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
//                }
                
                dispatch_queue_t myQueue = dispatch_queue_create("My Queue",NULL);
                dispatch_async(myQueue, ^{
                    // Perform long running process
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self storeApi];
                    });
                });
                
                
            });
        });
    }
    
    
    
}


#pragma mark - store api

-(void)storeApi
{
    if([[NSUserDefaults standardUserDefaults]valueForKey:@"isStore"] == NO)
    {
        [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"isStore"];
        [obj addLoadingView:self.view];
        apiCall=[ApiClasses sharedManager];
        [apiCall getAllStores:nil url:@"/mobile/stores/getstores" withTarget:self withSelector:@selector(storeApi:)];
        apiCall = nil;
    }
}

// store api response
-(void)storeApi:(id)response
{
    [obj removeLoadingView:self.view];
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        //[[NSUserDefaults standardUserDefaults]setValue:[response valueForKey:@"stores"] forKey:@"storesKey"];
        
        NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
        NSData *data = [NSKeyedArchiver archivedDataWithRootObject:[response valueForKey:@"stores"]];
        [currentDefaults setObject:data forKey:@"storesKey"];
  
        [[NSUserDefaults standardUserDefaults]synchronize];
    }
    else
    {
        [self alert_Action:@"Due to slow internet connection Stores are not loaded"];
        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"isStore"];
    }
    //NSLog(@"response store api ------- %@", response);
}



#pragma mark - alert method

-(void)alert_Action:(NSString *)str
{
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:str
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                                  [self storeApi];
                              }];
    
    [alert addAction:cancel1];
    [self presentViewController:alert animated:YES completion:nil];
}


// text field corner radius

-(void)addCornerRadius:(UIView*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 0.5;
    textField.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
   
}

# pragma mark - Memory warning 

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
  
}

# pragma mark - New user button Action

- (IBAction)newUserBtn_Action:(id)sender
{
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"themeSetting"];
    NSArray * arr2 = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
  // NSString * str = [obj reterieveuserDefaultData:@"mobileSetting"];
  // NSArray * arr2 = [str valueForKey:@"registrationFields"];
    
    if(arr2.count>0)
    {
      // [self.delegate newUser];
        SignUpViewController * signObj = [[SignUpViewController alloc]initWithNibName:@"SignUpViewController" bundle:nil];
        [self.navigationController pushViewController:signObj animated:YES];
    }
    else
    {
        [self alertViewDelegate:@"Internal Server error. Please Contact Administrator or not archieve properly."];
    }
}

#pragma mark - Email validation

- (BOOL)validateEmailWithString:(NSString*)email
{
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@",emailRegex];
    return [emailTest evaluateWithObject:email];
}


#pragma mark - Sign in button Ation

- (IBAction)signInBtn_Action:(id)sender
{
    NSString * newString;
    
    if(self.emailTF.text.length!=0)
    {
     newString = [self.emailTF.text substringWithRange:NSMakeRange(0, 1)];
    }
    NSString * message;
    
    if(self.emailTF.text.length == 0)
    {
        message = @"Email or PhoneNumber cannot be blank";
        [self alertViewDelegate:message];
    }
    else if(![self validateEmailWithString:self.emailTF.text] && isExist == NO && ![self checkNumbers:newString])
    {
        message = @"Please enter a valid Email ID.";
        [self alertViewDelegate:message];
    }
    
    else if(self.passwordTF.text.length == 0)
    {
        message = @"Password cannot be blank.";
        [self alertViewDelegate:message];
    }
   else
     {
         obj=[ModelClass sharedManager];

         //[self callLoginApi];
     if([obj checkNetworkConnection])
     {
           // call signIN Api
            [self callLoginApi];
     }
      else
      {
         [self alertViewDelegate:@"Please check your network connection"];
      }
   }
}


#pragma  mark - Login Api

-(void)callLoginApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    [dict setValue:self.emailTF.text forKey:@"username"];
    [dict setValue:self.passwordTF.text forKey:@"password"];
    
  [apiCall loginAPI:dict url:@"/member/login" withTarget:self withSelector:@selector(loginApi:)];
    dict = nil;
    apiCall = nil;
}

// login api response
-(void)loginApi:(id)response
{
     NSLog(@"login response------%@",response);
   
    if(response !=nil)
    {
    if([[response valueForKey:@"successFlag"]integerValue]==0)
    {
        [obj removeLoadingView:self.view];
        [self alertViewDelegate:[response valueForKey:@"message"]];
    }
    else
    {
        [obj saveUserDefaultData:[response valueForKey:@"accessToken"] and:@"access_token"];
        [self.passwordTF resignFirstResponder];
        [self getMemberApiCall];
    }
   
    }
    else
    {
        [obj removeLoadingView:self.view];
        [self alertViewDelegate:@"The request timed out"];
    }
}


#pragma  mark - getMember Api

-(void)getMemberApiCall
{
    //[obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall getMember:nil url:@"/member/getMember" withTarget:self withSelector:@selector(getMember:)];
    apiCall = nil;
}

// api response
-(void)getMember:(id)response
{
    [obj removeLoadingView:self.view];
    
     NSDictionary * dic;
     NSLog(@"member response value------%@",response);
    
    [[NSUserDefaults standardUserDefaults]setValue:[response valueForKey:@"customerID"] forKey:@"customerID"];
    
    if ([response valueForKey:@"storeCode"] != (NSString *)[NSNull null])
    {
        [[NSUserDefaults standardUserDefaults]setValue:[response valueForKey:@"storeCode"] forKey:@"storeNumberValue"];
    }
    
    
   // check user registration or login
    if(isRegistration == YES)
    {
        isRegistration = NO;
        StoreSearchView * objSearch = [[StoreSearchView alloc]initWithNibName:@"StoreSearchView" bundle:nil];
         objSearch.delegate = self;
        [self.navigationController pushViewController:objSearch animated:YES];
    }
    else
    {
        NSUserDefaults *def = [NSUserDefaults standardUserDefaults];
        [def setObject:[NSKeyedArchiver archivedDataWithRootObject:response] forKey:@"MyData"];
        [def synchronize];
        self.passwordTF.text = @"";
        self.emailTF.text = @"";
        
        NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
        NSData *data = [def1 objectForKey:@"MyData"];
        
        if(data!=nil)
        {
            NSDictionary *retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
            
//            [[NSUserDefaults standardUserDefaults]setValue:[retrievedDictionary valueForKey:@"homeStoreID"] forKey:@"storeFavID"];
            
            // for event tracking
            NSString * strFirstName = [retrievedDictionary valueForKey:@"firstName"];
            
            if([retrievedDictionary valueForKey:@"firstName"]!=(NSString *)[NSNull null])
            {
                NSString * strLastName = [retrievedDictionary valueForKey:@"lastName"];
                userName = [NSString stringWithFormat:@"%@ %@",strFirstName,strLastName];
                [obj saveUserDefaultData:userName and:@"userName"];
            }
            else
            {
                userName = [NSString stringWithFormat:@"%@",strFirstName];
                [obj saveUserDefaultData:userName and:@"userName"];
            }
    
            [self updateDeviceApi];
            NSLog(@"userProfile------%@",dic);
        }
    }
        [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"login"];
   // }
}


// update device api
#pragma mark - update device api

-(void)updateDeviceApi
{
    [obj addLoadingView:self.view];
    
    NSMutableDictionary * dict1 = [NSMutableDictionary new];
    
    // device id
    NSString *stringDeviceID = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
    stringDeviceID = [stringDeviceID stringByReplacingOccurrencesOfString:@"-" withString:@""];
    stringDeviceID = [NSString stringWithFormat:@"%@",stringDeviceID];
    
    // push token
    
    NSString * strPushToken = [[NSUserDefaults standardUserDefaults]valueForKey:@"pushToken"];
    
    // device appID
    NSString *bundleIdentifier = [[NSBundle mainBundle] bundleIdentifier];
    [dict1 setValue:bundleIdentifier forKey:@"appId"];
    
    // deviceType
    [dict1 setValue:[UIDevice currentDevice].model forKey:@"deviceType"];
    
    // deviceOSVersion
    [dict1 setValue:[UIDevice currentDevice].systemVersion forKey:@"deviceOSVersion"];
    
    //memberid
    NSString * customerID = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];
    [dict1 setValue:customerID forKey:@"memberid"];
    
    //deviceId
    [dict1 setValue:stringDeviceID forKey:@"deviceId"];
    
    //storeCode
    //[dict1 setValue:@"474" forKey:@"storeCode"];
    
    //pushToken
    if(strPushToken.length!=0)
    {
        [dict1 setValue:strPushToken forKey:@"pushToken"];
    }
    else
    {
        [dict1 setValue:@"fc17d980c180eba2994b2a3c13f024f3b0947d33077573a513b47d9814e23226" forKey:@"pushToken"];
    }
    
    NSLog(@"updateDevice description is %@",dict1.description);
    
    
    apiCall=[ApiClasses sharedManager];
    [apiCall updateDevice:dict1 url:@"/member/deviceUpdate" withTarget:self withSelector:@selector(updateDevices:)];
    apiCall = nil;
    
}

//updatedevice response

-(void)updateDevices:(id)response
{
     [obj removeLoadingView:self.view];
     NSLog(@"update Device response -------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue]==1)
    {
    // event tracking method
        
   
        
    NSMutableDictionary * eventDic = [NSMutableDictionary new];
    [eventDic setValue:@"LOGIN" forKey:@"event_name"];
    [eventDic setValue:userName forKey:@"item_name"];
    [clpObj updateAppEvent:eventDic];
    eventDic = nil;
    
    HomeViewController * homeObj = [[HomeViewController alloc]initWithNibName:@"HomeViewController" bundle:nil];
    [self.navigationController pushViewController:homeObj animated:YES];
    }
   else
   {
       [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"login"];
       [self alertViewDelegate:@"Something went wrong"];
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


# pragma mark - TextField Delegate Method

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if(textField == self.emailTF)
    {
    [self.passwordTF becomeFirstResponder];
    }
    else
    {
    [textField resignFirstResponder];
    }
      return YES;
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    NSString * newString1;
    if(self.emailTF.text.length!=0)
    {
    NSString * newString = [self.emailTF.text stringByAppendingString:string];
    newString1  = [newString substringWithRange:NSMakeRange(0, 1)];
    }

    if(textField == self.emailTF && [self checkNumber:string] && [self checkNumbers:newString1])
    {
    int length = (int)[self getLength:textField.text];
    
    if(length == 10)
    {
        if(range.length == 0)
            return NO;
    }
    
    if(length == 3)
    {
        NSString *num = [self formatNumber:textField.text];
         self.emailTF.text = [NSString stringWithFormat:@"%@-",num];
        if(range.length > 0)
            self.emailTF.text = [NSString stringWithFormat:@"%@",[num substringToIndex:3]];
    }
    else if(length == 6)
    {
        NSString *num = [self formatNumber:textField.text];
         self.emailTF.text = [NSString stringWithFormat:@"%@-%@-",[num  substringToIndex:3],[num substringFromIndex:3]];
        
        if(range.length > 0)
            self.emailTF.text = [NSString stringWithFormat:@"%@-%@",[num substringToIndex:3],[num substringFromIndex:3]];
     }
        
  }
    
    return YES;
}

- (NSString *)formatNumber:(NSString *)mobileNumber
{
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"(" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@")" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@" " withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"-" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"+" withString:@""];
    
    int length = (int)[mobileNumber length];
    if(length > 10)
    {
        mobileNumber = [mobileNumber substringFromIndex: length-10];
    }
    return mobileNumber;
}


- (int)getLength:(NSString *)mobileNumber
{
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"(" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@")" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@" " withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"-" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"+" withString:@""];
    int length = (int)[mobileNumber length];
    return length;
}


-(BOOL)checkNumber:(NSString *)str
{
    NSString *input = [str substringFromIndex:0];
    //This is the string that is going to be compared to the input string
    NSString *testString = [NSString string];
    
    NSScanner *scanner = [NSScanner scannerWithString:input];
    //This is the character set containing all digits. It is used to filter the input string
    NSCharacterSet *skips = [NSCharacterSet characterSetWithCharactersInString:@"1234567890"];
    
    //This goes through the input string and puts all the
    //characters that are digits into the new string
    [scanner scanCharactersFromSet:skips intoString:&testString];
    
    if([input length] == [testString length] && input.length !=0 )
    {
        NSLog(@"number");
        isExist = YES;
        return YES;
    }
    else
    {
        NSLog(@"not number");
        isExist = NO;
        return NO;
    }
}

-(BOOL)checkNumbers:(NSString *)str
{
    NSString *input = [str substringFromIndex:0];
    //This is the string that is going to be compared to the input string
    NSString *testString = [NSString string];
    
    NSScanner *scanner = [NSScanner scannerWithString:input];
    //This is the character set containing all digits. It is used to filter the input string
    NSCharacterSet *skips = [NSCharacterSet characterSetWithCharactersInString:@"1234567890"];
    
    //This goes through the input string and puts all the
    //characters that are digits into the new string
   BOOL check = [scanner scanCharactersFromSet:skips intoString:&testString];
  
    if (check)
    {
        isExist = YES;
        return YES;
    }
    else
    {
        isExist = NO;
        return NO;
    }
}

#pragma mark - forgotPassword button Ation
- (IBAction)forgotPasswordBtn_Action:(id)sender
{
    //[self.delegate forgotPassword];
    
    ForgotPassword * forgotObj = [[ForgotPassword alloc]initWithNibName:@"ForgotPassword" bundle:nil];
    [self.navigationController pushViewController:forgotObj animated:YES];
}

#pragma mark - FaceBook button Ation
- (IBAction)faceBookBtn_Action:(id)sender
{
    
    FBSDKLoginManager* lm = [[FBSDKLoginManager alloc] init];
    [lm logOut];
    lm.loginBehavior = FBSDKLoginBehaviorWeb;
    [lm logInWithReadPermissions:@[@"public_profile", @"email"] fromViewController:self handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
        
        // no error, so get user info
        if (!error && !result.isCancelled)
        {
            NSMutableDictionary* parameters = [NSMutableDictionary dictionary];
            [parameters setValue:@"id,name,email,first_name,last_name,picture,gender,birthday,location,hometown" forKey:@"fields"];
            
            [[[FBSDKGraphRequest alloc] initWithGraphPath:@"me" parameters:parameters]
             startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection,
                                          id result, NSError *error) {
                 NSLog(@"result value ------- %@",result);
                 [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"changePassword"];
                 resultDict = result;

                 [obj addLoadingView:self.view];
                 apiCall=[ApiClasses sharedManager];
                 NSMutableDictionary *dict = [NSMutableDictionary new];
                 
                 [dict setValue:[result valueForKey:@"last_name"] forKey:@"lastName"];
                 [dict setValue:[result valueForKey:@"first_name"] forKey:@"firstName"];
                 [dict setValue:[result valueForKey:@"email"] forKey:@"email"];
                 [dict setValue:[result valueForKey:@"gender"] forKey:@"gender"];
                 [dict setValue:[result valueForKey:@"birthday"] forKey:@"birthDate"];
                 [dict setValue:@"password" forKey:@"password"];
                 [dict setValue:@"474" forKey:@"storeCode"];
                 [dict setValue:@"true" forKey:@"emailOptIn"];
                 [dict setValue:@"true" forKey:@"pushOptIn"];
                 [dict setValue:@"true" forKey:@"smsOptIn"];
      
                 [apiCall faceBookregisterAPI:dict url:@"/member/create" withTarget:self withSelector:@selector(RegisterApi:)];
                 dict = nil;
                 apiCall = nil;
             }];
         }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"changePassword"];
            NSLog(@"something missing in facebook-------%@ and %c", error.description, result.isCancelled);
        }
   }];

}

// api response
-(void)RegisterApi:(id)response
{
    [obj removeLoadingView:self.view];
    [obj addLoadingView:self.view];
    
    if(response != nil)
    {
        NSLog(@"face book registration response------%@",response);
        
        if([[response valueForKey:@"successFlag"]integerValue]==1)
        {
            isRegistration = YES;
            [self faceBookuserLoginApi];
        }
        else if([[response valueForKey:@"message"] isEqualToString:@"Email already exists"])
        {
            isRegistration = NO;
            [self faceBookuserLoginApi];
        }
        else
        {
            [obj removeLoadingView:self.view];
            [self alertViewDelegate:[response valueForKey:@"message"]];
        }
    }
}

// store search delegate
-(void)StoreSearch:(NSString *)storeNumber and:(NSString *)StoreName
{
    storeNumberValue = storeNumber;
    
    if([storeNumberValue intValue]==0)
    {
        storeNumberString = storeNumber;
    }
    else
    {
        storeNumberString = storeNumber;
        NSLog(@" StoreNumber--------%d",[storeNumberValue intValue]);
    }
    
    // callUpdateStoreApi api
    [self callUpdateStoreApi];
}

// callUpdateStoreApi Api
-(void)callUpdateStoreApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    
    NSString * customerID = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];
    [dict setValue:customerID forKey:@"memberid"];
    if(storeNumberValue.length!=0)
    {
        if([storeNumberValue intValue]==0)
        {
            [dict setValue:storeNumberString forKey:@"storeCode"];
            [dict setValue:storeNumberString forKey:@"favoriteStore"];
        }
        else
        {
            [dict setValue:[NSString stringWithFormat:@"%d",[storeNumberValue intValue]] forKey:@"storeCode"];
            [dict setValue:[NSString stringWithFormat:@"%d",[storeNumberValue intValue]] forKey:@"favoriteStore"];
        }
    }
    else
    {
        [dict setValue:@"474" forKey:@"storeCode"];
        [dict setValue:@"474" forKey:@"favoriteStore"];
    }
    
    [apiCall favouritStorehApi:dict url:@"/member/updateStore" withTarget:self withSelector:@selector(favouritStore:)];
    apiCall = nil;
}


// favStore api response
-(void)favouritStore:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"favouritStoreh response value------%@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        [self getMemberApiCall];
    }
}

// face book login Api
-(void)faceBookuserLoginApi
{
    NSLog(@"user email and password ---------- %@",[resultDict valueForKey:@"email"]);
    
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    [dict setValue:[resultDict valueForKey:@"email"] forKey:@"username"];
    [dict setValue:@"password" forKey:@"password"];
  
    [apiCall loginAPI:dict url:@"/member/login" withTarget:self withSelector:@selector(loginApi:)];
    dict = nil;
    apiCall = nil;
}

// store ID
-(void)getStoreID:(NSString *)storeCode
{
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"storesKey"];
    NSArray * arrStoreNumber = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    NSArray *  str1 = [arrStoreNumber filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(storeNumber == %@)",storeCode]];
    
    if(str1.count!=0)
    {
         [[NSUserDefaults standardUserDefaults]setValue:[[str1 objectAtIndex:0]valueForKey:@"storeID"] forKey:@"storeFavID"];
         [[NSUserDefaults standardUserDefaults]synchronize];
    }
}

@end
