//
//  ChangePasswordView.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "ChangePasswordView.h"
#import "ModelClass.h"
#import "ApiClasses.h"
#import "OfferDetailView.h"
#import "clpsdk.h"

@interface ChangePasswordView ()
{
    ModelClass       * obj;
    ApiClasses       * apiCall;
    clpsdk           * clpObj;
    NSDictionary     * dicValue;
}
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView * backgroundImage;
@property (weak, nonatomic) IBOutlet UIImageView              * headerImage;
@property (weak, nonatomic) IBOutlet UIImageView              * imageLogoUrl;
@property (weak, nonatomic) IBOutlet UIView *changePasswordView;

@end

@implementation ChangePasswordView

- (void)viewDidLoad {
    [super viewDidLoad];

    
    //[self addCornerRadius:self.passwordNew];
   // [self addCornerRadius:self.passwordConfirm];
    
    
    
    [self.passwordNew setValue:[UIColor blackColor]
                forKeyPath:@"_placeholderLabel.textColor"];
    
    [self.passwordConfirm setValue:[UIColor blackColor]
                   forKeyPath:@"_placeholderLabel.textColor"];

    obj=[ModelClass sharedManager];
    
    // mobilesetting
//    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
//    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
//    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    // button background color
//    self.changePasswordBtn.backgroundColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
//    // background image
//    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
//    NSURL *url2 = [NSURL URLWithString:str2];
//    [self.backgroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
//
//    // header image
//NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
//    NSURL *url3 = [NSURL URLWithString:str3];
//    [self.headerImage sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    
//    NSString * str = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
//    NSURL *url = [NSURL URLWithString:
//                  str];
//    [self.imageLogoUrl sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    
    // button corner radius
//    
//    self.changePasswordBtn.layer.masksToBounds = YES;
//    self.changePasswordBtn.layer.cornerRadius = self.changePasswordBtn.frame.size.height/2.0;
//    self.changePasswordBtn.layer.borderWidth = 1.0;
//    self.changePasswordBtn.layer.borderColor = [[UIColor clearColor] CGColor];
    
    [self shadoOffect:self.changePasswordView];

}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    
}




-(void)addCornerRadius:(UITextField*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 0.5;
    textField.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    
    //isPushBackGround
    
// if([[NSUserDefaults standardUserDefaults]boolForKey:@"isPushBackGround"]==YES)
//    {
//        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"isPushBackGround"];
//        
//        OfferDetailView * offerObject = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
//        [self.navigationController pushViewController:offerObject animated:YES];
//    }
 
}

#pragma mark - change password button Action

- (IBAction)changePasswordButtonAction:(id)sender
{
    
    NSString * message;
    
    if(self.passwordNew.text.length == 0)
    {
        message = @"OldPassword cannot be blank.";
        [self alertViewDelegate:message];
    }
    
    if(self.passwordNew.text.length < 6 || self.passwordNew.text.length>15)
    {
        message = @"Password should be at least 6 characters and less than 15 characters";
        [self alertViewDelegate:message];
    }
    
    else if(self.passwordConfirm.text.length == 0)
    {
        message = @"NewPassword cannot be blank.";
        [self alertViewDelegate:message];
    }
    
//    else if(![self.passwordNew.text isEqualToString:self.passwordConfirm.text])
//    {
//         message = @"NewPassword and ConfirmPassword not match";
//         [self alertViewDelegate:message];
//    }
    else
    {
        if([obj checkNetworkConnection])
        {
            // call signIN Api
             [self changPasswordApi];
        }
        
        else
        {
            [self alertViewDelegate:@"Please check your network connection"];
        }
        
       
    }
    
    //[self.delegate changePasswordApi];
}


#pragma mark - change password api

-(void)changPasswordApi
{
    [obj addLoadingView:self.view];
   
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary * dict = [NSMutableDictionary new];
    
    
    NSUserDefaults *def = [NSUserDefaults standardUserDefaults];
    NSData *data = [def objectForKey:@"MyData"];
    
    if(data!=nil)
    {
        NSDictionary *retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
        dicValue = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    }
    
    if([[dicValue valueForKey:@"smsOpted"]boolValue]==1)
    {
        [dict setValue:@"true" forKey:@"smsoptin"];
    }
    else
    {
        [dict setValue:@"false" forKey:@"smsoptin"];
    }
    
    if([[dicValue valueForKey:@"emailOpted"]boolValue]==1)
    {
        [dict setValue:@"true" forKey:@"emailOptIn"];
    }
    else
    {
        [dict setValue:@"false" forKey:@"emailOptIn"];
    }
    
    if([[dicValue valueForKey:@"pushOpted"]boolValue]==1)
    {
        [dict setValue:@"true" forKey:@"pushoptin"];
    }
    else
    {
        [dict setValue:@"false" forKey:@"pushoptin"];
    }
    
   
    [dict setValue:self.passwordNew.text forKey:@"oldPassword"];
    [dict setValue:self.passwordConfirm.text forKey:@"password"];
    
    [apiCall changePassword:dict url:@"/member/changePassword" withTarget:self withSelector:@selector(changePassword:)];
    dict = nil;
    apiCall = nil;
}

// change password response

-(void)changePassword:(id)response
{
    [obj removeLoadingView:self.view];
      NSLog(@"response------%@", response);
    if(response!=nil)
    {
    
    if([[response valueForKey:@"successFlag"]integerValue]==1)
    {
        // clp mobile setting api
        clpObj = [clpsdk sharedInstanceWithAPIKey];
        
        // event tracking method
        NSMutableDictionary * eventDic = [NSMutableDictionary new];
        NSString * userName = [[NSUserDefaults standardUserDefaults]valueForKey:@"userName"];
        [eventDic setValue:@"UPDATE_PASSWORD" forKey:@"event_name"];
        [eventDic setValue:userName forKey:@"item_name"];
        [clpObj updateAppEvent:eventDic];
        eventDic = nil;
        
        [self alertViewDelegateIndex:[response valueForKey:@"message"]];
    }
    else
    {
        [self alertViewDelegate:[response valueForKey:@"message"]];
    }
        
    }
}

// alert view method

-(void)alertViewDelegateIndex:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                                                          
                           // [self.delegate changePasswordApi];
                    [self.navigationController popViewControllerAnimated:YES];
                                                          }];
    [alertController addAction:defaultAction];
    [self presentViewController:alertController animated:YES completion:nil];
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
    [textField resignFirstResponder];
    return YES;
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

# pragma mark - Back Button Action

- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}



@end
