//
//  LoginViewController.m
//  iOS_FBTemplate1
//
//  Created by HARSH on 31/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "LoginViewController.h"
#import "RegisterViewController.h"
#import "AppDelegate.h"
#import "User.h"
#import "MenuViewController.h"
#import "CustomBottomBarView.h"
#import "SignUpViewController.h"

@interface LoginViewController ()<UITextFieldDelegate>
{
    CGRect keyboardFrame;
}
@property (weak, nonatomic) IBOutlet UIView *textviewshow;
@property (weak, nonatomic) IBOutlet UITextField *emailText;
@property (weak, nonatomic) IBOutlet UITextField *passWordText;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;

@end

@implementation LoginViewController
- (IBAction)skipLogin:(id)sender
{
    
}
-(void)tap:(UIGestureRecognizer*)gr
{
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_emailText resignFirstResponder];
    [_passWordText resignFirstResponder];
    
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    //    if (textField==_phoneNumber)
    //    {
    //        NSCharacterSet *cs = [[NSCharacterSet characterSetWithCharactersInString:ACCEPTABLE_CHARECTERS] invertedSet];
    //
    //        NSString *filtered = [[string componentsSeparatedByCharactersInSet:cs] componentsJoinedByString:@""];
    //
    //
    //
    //
    //
    //
    //        return [string isEqualToString:filtered];
    //
    //        //        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"UIAlertView" message:@"Please Enter a Valid Mobile number" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
    //        //        [alert show];
    //
    //
    //
    //    }
    return YES;
    
}


- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    [self addKeyBoard];
    UITapGestureRecognizer *tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tap:)];
    [self.view addGestureRecognizer:tap];
    if(textField==_emailText)
    {
        CGPoint scrollPoint=CGPointMake(0, _textviewshow.frame.origin.y-60);
        [_scrollView setContentOffset:scrollPoint animated:YES];
    }
    if(textField==_passWordText)
    {
        CGPoint scrollPoint=CGPointMake(0,_textviewshow.frame.origin.y-40);
        [_scrollView setContentOffset:scrollPoint animated:YES];
    }
    
    
    
    return YES;
}
- (void)textFieldDidEndEditing:(UITextField *)quantityText
{
    
    
    [_scrollView setContentOffset:CGPointZero animated:YES];
    
    [_emailText resignFirstResponder];
    [_passWordText resignFirstResponder];
    
    
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    return YES;
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
    _emailText.inputAccessoryView = numberToolbar;
    _passWordText.inputAccessoryView = numberToolbar;
    
    numberToolbar.barTintColor=[UIColor colorWithRed:0.969 green:0.941 blue:0.902 alpha:1];
}

-(void)cancelNumberPad:(id)sender
{
    [self.view endEditing:YES];
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_emailText resignFirstResponder];
    [_passWordText resignFirstResponder];
}
-(void)doneWithNumberPad:(id)sender
{
    NSLog(@"Done Clicked.");
    [self.view endEditing:YES];
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_emailText resignFirstResponder];
    [_passWordText resignFirstResponder];
    
}
- (IBAction)skipClicked:(id)sender
{
    MenuViewController *tomenu=[self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
    [self.navigationController pushViewController:tomenu animated:YES];
}





- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)doneLogin:(id)sender
{
    
    
    if(_emailText.text.length == 0)
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
    
    else if(_passWordText.text.length == 0)
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
    else
    {
        AppDelegate *appDelegate =
        [[UIApplication sharedApplication] delegate];
        
        NSManagedObjectContext *context2 =
        [appDelegate managedObjectContext];
        
        
        
        
        NSFetchRequest *fetchRequest2 = [[NSFetchRequest alloc] init];
        NSEntityDescription *entity2 = [NSEntityDescription entityForName:@"User"
                                                   inManagedObjectContext:context2];
        [fetchRequest2 setEntity:entity2];
        NSError *error3;
        NSArray *Userdata = [context2 executeFetchRequest:fetchRequest2 error:&error3];
        int count=0;
        NSLog(@"orderdata==%@",Userdata);
        
        
        for (User *info in Userdata)
        {
            if([info.email_address isEqualToString:_emailText.text]&&[info.password isEqualToString:_passWordText.text])
            {
                MenuViewController *tomenu=[self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
                [self.navigationController pushViewController:tomenu animated:YES];
            }
            else
            {
                UIAlertController * alert=   [UIAlertController
                                              alertControllerWithTitle:nil
                                              message:@"Emailid and password does not match"
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
        }
        
    }
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
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    bottom1.hidden=YES;
    
    
    self.navigationController.navigationBarHidden=NO;
    self.navigationController.navigationBar.tintColor=[UIColor whiteColor];
    
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    self.navigationController.navigationBar.titleTextAttributes=@{NSForegroundColorAttributeName: [UIColor whiteColor]};
    UIButton *btnRight = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnRight setFrame:CGRectMake(0, 0,80, 44)];
    [btnRight setTitle:@"SKIP"  forState:UIControlStateNormal];
    [btnRight setTitleEdgeInsets:UIEdgeInsetsMake(0, 0.0, 0.0, -60)];
    btnRight.titleLabel.font = [UIFont fontWithName:@"Futura-Medium" size:14.0f];
    [self.navigationController.navigationBar setTitleTextAttributes:
     @{NSForegroundColorAttributeName:[UIColor whiteColor],
       NSFontAttributeName:[UIFont fontWithName:@"Futura-CondensedExtraBold" size:18.0f]}];
    [btnRight addTarget:self action:@selector(skipClicked:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *barBtnRight = [[UIBarButtonItem alloc] initWithCustomView:btnRight];
    [barBtnRight setTintColor:[UIColor whiteColor]];
    //top,left,bottom,right
    //[barBtnRight setTitleEdgeInsets:UIEdgeInsetsMake(0, 0.0, 0.0, -50)];
    self.navigationItem.rightBarButtonItem=barBtnRight;
    
    
    UIButton *btnBack = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnBack setFrame:CGRectMake(0, 0,30, 44)];
    [btnBack setImage:[UIImage imageNamed:@"BackIcon"] forState:UIControlStateNormal];
    [btnBack setImageEdgeInsets:UIEdgeInsetsMake(0, -30, 0.0, 0)];
    [btnBack addTarget:self action:@selector(backClicked:) forControlEvents:UIControlEventTouchUpInside];
    // btnBack.titleLabel.font = [UIFont fontWithName:@"HelveticaNeue-Bold" size:14.0f];
    UIBarButtonItem *barBtnBack = [[UIBarButtonItem alloc] initWithCustomView:btnBack];
    [barBtnBack setTintColor:[UIColor whiteColor]];
    
    self.navigationItem.leftBarButtonItem=barBtnBack;
    _emailText.delegate=self;
    _passWordText.delegate=self;
    
    
}
- (IBAction)goToSignUp:(id)sender
{
    RegisterViewController *toReg=[self.storyboard instantiateViewControllerWithIdentifier:@"RegisterViewController"];
    [self.navigationController pushViewController:toReg animated:YES];
}
-(void)callData
{
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
