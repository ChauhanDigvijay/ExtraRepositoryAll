//
//  RegisterViewController.m
//  iOS_FBTemplate1
//
//  Created by Vishnu Dayal on 27/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "RegisterViewController.h"
#import "User.h"
#import "AppDelegate.h"
#include "MenuViewController.h"
#import "CustomBottomBarView.h"
#import "SignUpViewController.h"
#define ACCEPTABLE_CHARECTERS @"0123456789"
#define MAX_LENGTH 10
@interface RegisterViewController ()<UITextFieldDelegate>
{
    CGRect framename;
    CGRect frameEmail;
    CGRect passWord;
    CGRect phoneNumber;
    CGRect framenamelabel;
    CGRect frameEmaillabel;
    CGRect passWordlabel;
    CGRect phoneNumberlabel;
    CGRect resetframeEmail;
    CGRect resetpassWord;
    CGRect resetphoneNumber;
    
    
}
@property (weak, nonatomic) IBOutlet UILabel *passwordlabel;
@property (weak, nonatomic) IBOutlet UILabel *phoneLebel;
@property (weak, nonatomic) IBOutlet UILabel *namelabel;
@property (weak, nonatomic) IBOutlet UILabel *emailLebel;
@property (weak, nonatomic) IBOutlet UITextField *userName;
@property (weak, nonatomic) IBOutlet UITextField *emailAddress;
@property (weak, nonatomic) IBOutlet UITextField *passWord;
@property (weak, nonatomic) IBOutlet UITextField *phoneNumber;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;

@end

@implementation RegisterViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    _emailAddress.delegate=self;
    _passWord.delegate=self;
    _phoneNumber.delegate=self;
    _userName.delegate=self;
    
    // Do any additional setup after loading the view.
}
-(void)viewWillDisappear:(BOOL)animated
{
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    
    bottom1.hidden=NO;
}



- (IBAction)backClicked:(id)sender
{
    SignUpViewController *toSignUp = [self.storyboard instantiateViewControllerWithIdentifier:@"SignUpViewController"];
    
    [self.navigationController pushViewController:toSignUp animated:NO];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    self.navigationController.navigationBarHidden=NO;
    self.navigationItem.hidesBackButton=YES;
    
    self.navigationController.navigationBar.tintColor=[UIColor whiteColor];
    self.navigationController.navigationBar.barTintColor=self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    self.navigationItem.title=@"New User Registration";
    
    
    [self.navigationController.navigationBar setTitleTextAttributes:
     @{NSForegroundColorAttributeName:[UIColor whiteColor],
       NSFontAttributeName:[UIFont fontWithName:@"Futura-CondensedExtraBold" size:18.0f]}];
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    bottom1.hidden=YES;
    
    
    
    
    UIButton *btnBack = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnBack setFrame:CGRectMake(0, 0,30, 44)];
    [btnBack setImage:[UIImage imageNamed:@"BackIcon"] forState:UIControlStateNormal];
    [btnBack setImageEdgeInsets:UIEdgeInsetsMake(0, -30, 0.0, 0)];
    [btnBack addTarget:self action:@selector(backClicked:) forControlEvents:UIControlEventTouchUpInside];
    // btnBack.titleLabel.font = [UIFont fontWithName:@"HelveticaNeue-Bold" size:14.0f];
    UIBarButtonItem *barBtnBack = [[UIBarButtonItem alloc] initWithCustomView:btnBack];
    [barBtnBack setTintColor:[UIColor whiteColor]];
    
    self.navigationItem.leftBarButtonItem=barBtnBack;
    
}
-(void)tap:(UIGestureRecognizer*)gr
{
    
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_userName resignFirstResponder];
    [_emailAddress resignFirstResponder];
    [_passWord resignFirstResponder];
    [_phoneNumber resignFirstResponder];
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    if(textField==_phoneNumber)
    {
        
        
        if (![self isNumeric:_phoneNumber.text] )
        {
            
            return NO;
            
        }
        
        else
            
        {
            NSUInteger newLength = (([_phoneNumber.text length] + [string length])-range.length);
            
            if(newLength > MAX_LENGTH)
            {
                
                return NO;
            }
            else
            {
                return YES;
            }
        }
        
    }
    if(textField==_userName)
    {
        NSCharacterSet *myCharSet = [NSCharacterSet characterSetWithCharactersInString:@"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "];
        for (int i = 0; i < [string length]; i++) {
            unichar c = [string characterAtIndex:i];
            if (![myCharSet characterIsMember:c])
            {
                
                
                UIAlertController * alert=   [UIAlertController
                                              alertControllerWithTitle:@"Error"
                                              message:@"Enter only alphabets"
                                              preferredStyle:UIAlertControllerStyleAlert];
                UIAlertAction* yesButton = [UIAlertAction
                                            actionWithTitle:@"OK"
                                            style:UIAlertActionStyleDefault
                                            handler:^(UIAlertAction * action)
                                            {
                                                //Handel your yes please button action here
                                                [alert dismissViewControllerAnimated:YES completion:nil];
                                                
                                            }];
                [alert addAction:yesButton];
                
                [self presentViewController:alert animated:YES completion:nil];
                
                return NO;
            }
            
        }
    }
    
    
    return YES;
}
-(BOOL)isNumeric:(NSString*)inputString
{
    NSCharacterSet *charcter =[[NSCharacterSet characterSetWithCharactersInString:@"0123456789"] invertedSet];
    NSString *filtered;
    filtered = [[inputString componentsSeparatedByCharactersInSet:charcter] componentsJoinedByString:@""];
    return [inputString isEqualToString:filtered];
}

- (BOOL)validateEmailWithString:(NSString*)checkString
{
    BOOL stricterFilter = NO;
    NSString *stricterFilterString = @"[A-Z0-9a-z\\._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}";
    NSString *laxString = @".+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2}[A-Za-z]*";
    NSString *emailRegex = stricterFilter ? stricterFilterString : laxString;
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:checkString];
}




- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    UITapGestureRecognizer *tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tap:)];
    [self.view addGestureRecognizer:tap];
    [self addKeyBoard];
    if(textField==_phoneNumber)
    {
        CGPoint scrollPoint=CGPointMake(0, _phoneNumber.frame.origin.y+80);
        [_scrollView setContentOffset:scrollPoint animated:YES];
    }
    if(textField==_userName)
    {
        CGPoint scrollPoint=CGPointMake(0, _userName.frame.origin.y);
        [_scrollView setContentOffset:scrollPoint animated:YES];
    }
    if(textField==_emailAddress)
    {
        
        CGPoint scrollPoint=CGPointMake(0, _emailAddress.frame.origin.y+30);
        [_scrollView setContentOffset:scrollPoint animated:YES];
    }
    if(textField==_passWord)
    {
        CGPoint scrollPoint=CGPointMake(0, _passWord.frame.origin.y+50);
        [_scrollView setContentOffset:scrollPoint animated:YES];
    }
    
    
    
    
    return YES;
}
- (void)textFieldDidEndEditing:(UITextField *)textField
{
    
    
    
    if(textField==_emailAddress)
    {
        
        //Email Address is valid.
        [_scrollView setContentOffset:CGPointZero animated:YES];
        [_emailAddress resignFirstResponder];
    }
    if(textField==_userName)
    {
        [_scrollView setContentOffset:CGPointZero animated:YES];
        [_userName resignFirstResponder];
    }
    
    if(textField==_passWord)
    {
        [_scrollView setContentOffset:CGPointZero animated:YES];
        [_passWord resignFirstResponder];
    }
    if(textField==_phoneNumber)
    {
        [_scrollView setContentOffset:CGPointZero animated:YES];
        [_phoneNumber resignFirstResponder];
    }
    
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    return YES;
}
-(void)saveUser
{
    AppDelegate *appDelegate =
    [[UIApplication sharedApplication] delegate];
    
    NSManagedObjectContext *context =
    [appDelegate managedObjectContext];
    
    BOOL entityExists = NO;
    NSString *strItemid=_emailAddress.text;
    
    NSFetchRequest *fetchRequest1 = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity1 = [NSEntityDescription entityForName:@"User"inManagedObjectContext:context];
    [fetchRequest1 setEntity:entity1];
    NSError *error1;
    NSArray  *fetchedObjects1 = [context executeFetchRequest:fetchRequest1 error:&error1];
    for (User *info in fetchedObjects1)
    {
        if([info.email_address isEqualToString:strItemid])
            
        {
            
            entityExists = YES;
            break;
            
        }
    }
    
    if(!entityExists)
    {
        
        
        
        User *addUser = [NSEntityDescription
                         insertNewObjectForEntityForName:@"User"
                         inManagedObjectContext:context];
        addUser.user_name=_userName.text;
        addUser.password=_passWord.text;
        addUser.phone_num=_phoneNumber.text;
        addUser.email_address=_emailAddress.text;
        
        NSError *error1;
        if (![context save:&error1])
        {
            NSLog(@"Whoops, couldn't save: %@", [error1 localizedDescription]);
        }    NSLog(@"savetag==%@",addUser.description);
        
        
        NSLog(@"context==%@",context);
    }
    
    
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"User"
                                              inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    NSError *error;
    NSArray *fetchedObjects = [context executeFetchRequest:fetchRequest error:&error];
    int count=0;
    
    for (User *info in fetchedObjects)
    {
        NSLog(@"email_address: %@", info.email_address);
        NSLog(@"user_name: %@", info.user_name);
        NSLog(@"password: %@", info.password);
        NSLog(@"phone_num: %@", info.phone_num);
        NSLog(@"count==%i",++count);
    }
    
    
}

- (IBAction)signUpAndSave:(id)sender
{
    
    NSString *userStr = [_userName.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    NSString *passWordlStr = [_passWord.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    NSString *phoneStr = [_phoneNumber.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    if(userStr.length == 0)
    {
        // textField is empty
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Enter user name"
                                      message:nil
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
        
    }
    
    else if(_emailAddress.text.length == 0)
    {
        // textField is empty
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Enter email address"
                                      message:nil
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
        
    }
    else if(![self validateEmailWithString:_emailAddress.text])
    {
        //Email Address is invalid.
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:nil
                                      message:@"Enter valid email address"
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
        
    }
    else if(passWordlStr.length == 0)
    {
        // textField is empty
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Enter password"
                                      message:nil
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
        
    }
    
    
    else if(phoneStr.length == 0)
    {
        // textField is empty
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Enter phone number"
                                      message:nil
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
        
    }
    else if(phoneStr.length<10)
    {
        // textField is empty
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Enter 10 digit phone number"
                                      message:nil
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
        
    }
    else
    {
        
        //Email Address is valid.
        [self saveUser];
        MenuViewController *toMenu=[self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
        [[NSUserDefaults standardUserDefaults] setObject:@"YES" forKey:@"firstTimeLogin"];
        [self.navigationController pushViewController:toMenu animated:YES];
        
    }
    
}
-(void)addKeyBoard
{
    UIToolbar* numberToolbar = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 50)];
    numberToolbar.barStyle = UIBarStyleBlackTranslucent;
    numberToolbar.items = [NSArray arrayWithObjects:
                           [[UIBarButtonItem alloc]initWithTitle:@"Cancel" style:UIBarButtonItemStyleBordered target:self action:@selector(cancelNumberPad:)],
                           [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil],
                           [[UIBarButtonItem alloc]initWithTitle:@"Done" style:UIBarButtonItemStyleDone target:self action:@selector(doneWithNumberPad:)],
                           nil];
    [numberToolbar sizeToFit];
    _userName.inputAccessoryView = numberToolbar;
    _passWord.inputAccessoryView = numberToolbar;
    _emailAddress.inputAccessoryView = numberToolbar;
    _phoneNumber.inputAccessoryView = numberToolbar;
    numberToolbar.barTintColor=[UIColor colorWithRed:0.969 green:0.941 blue:0.902 alpha:1];
}

-(void)cancelNumberPad:(id)sender
{
    [self.view endEditing:YES];
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_userName resignFirstResponder];
    [_emailAddress resignFirstResponder];
    [_passWord resignFirstResponder];
    [_phoneNumber resignFirstResponder];
}
-(void)doneWithNumberPad:(id)sender
{
    NSLog(@"Done Clicked.");
    [self.view endEditing:YES];
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_userName resignFirstResponder];
    [_emailAddress resignFirstResponder];
    [_passWord resignFirstResponder];
    [_phoneNumber resignFirstResponder];
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
