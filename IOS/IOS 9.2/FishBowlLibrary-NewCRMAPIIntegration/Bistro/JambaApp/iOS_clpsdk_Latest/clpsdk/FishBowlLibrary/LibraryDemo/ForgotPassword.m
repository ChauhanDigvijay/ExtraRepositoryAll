//
//  ForgotPassword.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 28/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "ForgotPassword.h"
#import "ModelClass.h"

@interface ForgotPassword ()
{
    ModelClass  * obj;
    

}
@property (weak, nonatomic) IBOutlet UIImageView *companyImage;
@property (unsafe_unretained, nonatomic) IBOutlet UITextField *emailTF;

@end

@implementation ForgotPassword

- (void)viewDidLoad {
    [super viewDidLoad];
    //[self.emailTF becomeFirstResponder];
    
    // data fetch from mobilesetting
    obj=[ModelClass sharedManager];
   // NSString * str = [obj reterieveuserDefaultData:@"mobileSetting"];
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    
    // button background color
    self.sendBtn.backgroundColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:
                   str2];
    [self.backgroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    // company image
    
    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginLeftSideImageUrl"]];
    NSURL *url3 = [NSURL URLWithString:
                   str3];
    [self.companyImage sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    
}

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
    [self.delegate forgotPasswordBackButtonAction];
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
   // [self.delegate sendEmailAction];
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
