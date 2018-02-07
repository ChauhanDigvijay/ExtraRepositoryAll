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




@interface LoginViewController ()
{
    BOOL isExist;
    ApiClasses  * apiCall;
    ModelClass  * obj;
    BOOL isApiCorrect;
    clpsdk * clpsdkObj;
}
@property (weak, nonatomic) IBOutlet UIView *emailView;
@property (weak, nonatomic) IBOutlet UIButton *registerBtn;

@property (weak, nonatomic) IBOutlet UIView *passwordView;
@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    isExist = NO;
    isApiCorrect = NO;
    
    
    [self.emailTF setValue:[UIColor whiteColor]
                    forKeyPath:@"_placeholderLabel.textColor"];
    
    [self.passwordTF setValue:[UIColor whiteColor]
                forKeyPath:@"_placeholderLabel.textColor"];
    
    if ([[NSUserDefaults standardUserDefaults]boolForKey:@"login"]==YES)
    {
        [self.delegate signIn];
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
                    [self mobileSettingApi];
                });
            });
        }
        else
        {
            [self alertViewDelegate:@"Please check your network connection"];
        }
    }
    [obj removeLoadingView:self.view];
  
    
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    
    [obj removeLoadingView:self.view];
    
    // textfield corner radius
    
    [self addCornerRadius:self.emailView];
    [self addCornerRadius:self.passwordView];
    
    // button corner radius
    
    self.signInBtn.layer.masksToBounds = YES;
    self.signInBtn.layer.cornerRadius = self.signInBtn.frame.size.height/2.0;
    self.signInBtn.layer.borderWidth = 1.0;
    self.signInBtn.layer.borderColor = [[UIColor clearColor] CGColor];
    
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
            [self mobileSettingApi];
                });
            });
        }
       
    }
    else
    {
        [self alertViewDelegate:@"Please check your network connection"];
    }
}


// mobile setting api

-(void)mobileSettingApi
{
    [obj addLoadingView:self.view];
    
    apiCall=[ApiClasses sharedManager];
    [apiCall mobileSettingAPI:nil url:@"/loyalty/viewLoyaltySettings" withTarget:self withSelector:@selector(mobileSettingApi:)];
    apiCall = nil;
}

// api response

-(void)mobileSettingApi:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"mobileSettingApi response------%@",response);
  [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"isStoreFirstTime"];

   
    
    if(response!=nil)
    {
        dispatch_queue_t myQueue = dispatch_queue_create("My Queue",NULL);
        dispatch_async(myQueue, ^{
            // Perform long running process
            dispatch_async(dispatch_get_main_queue(), ^{
                
              if ([response valueForKey:@"checkInButtonColor"]!= ( NSString *)[NSNull null])
              {
                self.signInBtn.backgroundColor = [obj colorWithHexString:[response valueForKey:@"checkInButtonColor"]];
                  
                  [self.registerBtn setTitleColor:[obj colorWithHexString:[response valueForKey:@"checkInButtonColor"]] forState:UIControlStateNormal];
              }
                
                NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
                NSData *data = [NSKeyedArchiver archivedDataWithRootObject:response];
                [currentDefaults setObject:data forKey:@"mobileSetting"];
                
                if ([response valueForKey:@"companyLogoImageUrl"]!= ( NSString *)[NSNull null])
                {
                NSString * str = [NSString stringWithFormat:@"http://%@",[response valueForKey:@"companyLogoImageUrl"]];
                NSURL *url = [NSURL URLWithString:
                              str];
                [self.companyLogoImage sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
                }
                
                // top right side
                
                if ([response valueForKey:@"loginHeaderImageUrl"]!= ( NSString *)[NSNull null])
                {
                NSString * str1 = [NSString stringWithFormat:@"http://%@",[response valueForKey:@"loginHeaderImageUrl"]];
                NSURL *url1 = [NSURL URLWithString:
                               str1];
                [self.topRightImage sd_setImageWithURL:url1 placeholderImage:[UIImage imageNamed:@"nil"]];
                }
                
                // background image
                
                if ([response valueForKey:@"loginBackgroundImageUrl"]!= ( NSString *)[NSNull null])
                {
                NSString * str2 = [NSString stringWithFormat:@"http://%@",[response valueForKey:@"loginBackgroundImageUrl"]];
                NSURL *url2 = [NSURL URLWithString:
                               str2];
                [self.backGroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
                }
                
                // company image
                
                if ([response valueForKey:@"loginLeftSideImageUrl"]!= ( NSString *)[NSNull null])
                {
                NSString * str5 = [NSString stringWithFormat:@"http://%@",[response valueForKey:@"loginLeftSideImageUrl"]];
                NSURL *url5 = [NSURL URLWithString:str5];
                    
                    
        [self.companyImage sd_setImageWithURL:url5 placeholderImage:[UIImage imageNamed:@"nil"]];
                }
                
                // bottom image
                
                if ([response valueForKey:@"loginFooterImageUrl"]!= ( NSString *)[NSNull null])
                {
                NSString * str3 = [NSString stringWithFormat:@"https://%@",[response valueForKey:@"loginFooterImageUrl"]];
                    
                NSURL *url3 = [NSURL URLWithString:
                               str3];
                [self.bottomImage sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
                }
                
                // company Name
                if ([response valueForKey:@"companyName"]!= ( NSString *)[NSNull null])
                {
                self.companyNameLbl.text = [response valueForKey:@"companyName"];
                }
            });
        });
    }
    
   
    
    dispatch_queue_t myQueue = dispatch_queue_create("My Queue",NULL);
    dispatch_async(myQueue, ^{
        // Perform long running process
        dispatch_async(dispatch_get_main_queue(), ^{
            [self storeApi];
            
        });
    });
    
}



// store Api
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


-(void)storeApi:(id)response
{
    [obj removeLoadingView:self.view];
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        [[NSUserDefaults standardUserDefaults]setValue:[response valueForKey:@"stores"] forKey:@"storesKey"];
        [[NSUserDefaults standardUserDefaults]synchronize];
    }
    else
    {
        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"isStore"];
  
    }
    
    NSLog(@"response store api ------- %@", response);
    
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
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSArray * arr2 = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
  // NSString * str = [obj reterieveuserDefaultData:@"mobileSetting"];
  // NSArray * arr2 = [str valueForKey:@"registrationFields"];
    
    if(arr2.count>0)
    {
       [self.delegate newUser];
    }
    else
    {
        [self alertViewDelegate:@"Internal Server error. Please Contact Administrator"];
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
    
//    else if(isExist == YES || [self checkNumbers:newString])
//    {
//        message = @"Please enter a valid MobileNumber.";
//        [self alertViewDelegate:message];
//       
//    }
    
    else if(self.passwordTF.text.length == 0)
    {
        message = @"Password cannot be blank.";
        [self alertViewDelegate:message];
    }
    
 else
   {
       if([obj checkNetworkConnection])
       {
           // call signIN Api
            [self postApiData];
       }
       
       else
       {
           [self alertViewDelegate:@"Please check your network connection"];
       }

   }
}
// login api method
-(void)postApiData
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
        
        //clpsdk * obj1 = [[clpsdk alloc]init];
       // [obj1 updateDeviceInSDK];
        
        // [self.delegate signIn];
        //[[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"login"];
        
        [self updateProfile];
    }
   
    }
    else
    {
         [obj removeLoadingView:self.view];
         [self alertViewDelegate:@"The request timed out"];
    }
}


// update profile api
-(void)updateProfile
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
    

         NSUserDefaults *def = [NSUserDefaults standardUserDefaults];
        [def setObject:[NSKeyedArchiver archivedDataWithRootObject:response] forKey:@"MyData"];
        [def synchronize];
        self.passwordTF.text = @"";
        self.emailTF.text = @"";
    
     NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
     NSData *data = [def1 objectForKey:@"MyData"];
    
    if(data!=nil)
    {
//        NSDictionary *retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
//        dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
//        
//        NSString * str = [dic valueForKey:@"customerID"];
    
      //[NSTimer scheduledTimerWithTimeInterval:10.0 target:self selector:@selector(updateDeviceApi) userInfo:nil repeats:NO];
         [self updateDeviceApi];
         //[self.delegate signIn];
        NSLog(@"userProfile------%@",dic);
        
    }
        [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"login"];
   // }
}





// update device api
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
    [dict1 setValue:@"com.mybristo.fishbowl" forKey:@"appId"];
    //[dict1 setValue:@"com.olo.jambajuiceapp" forKey:@"appId"];
    
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
    [self.delegate signIn];

    NSLog(@"update device rseponse------%@", response);
   
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
        //NSLog(@"number");
       // NSLog(@"check=====%i",check);
        isExist = YES;
        return YES;
    }
    else
    {
       // NSLog(@"nonumber");
       // NSLog(@"check=====%i",check);
        isExist = NO;
        return NO;
    }
}

#pragma mark - forgotPassword button Ation

- (IBAction)forgotPasswordBtn_Action:(id)sender
{
    [self.delegate forgotPassword];
}

@end
