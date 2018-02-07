//
//  MainViewController.m
//  taco2
//
//  Created by HARSH on 17/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "MainViewController.h"
#import "MenuViewController.h"
#import "RegisterViewController.h"
#import "LoginViewController.h"
#import "AppDelegate.h"
#import "CustomBottomBarView.h"
#import "LeftmenuCustomView.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "User.h"
@interface MainViewController ()<UIActionSheetDelegate,FBSDKLoginButtonDelegate>
{
    AppDelegate *sharedAppDel;
    NSString *userid;
    NSString *userNmae;
}
@property (strong, nonatomic) IBOutlet UILabel *loginLabel;
@property (strong, nonatomic) IBOutlet UIButton *skipLoginOutlet;

@property (weak, nonatomic) IBOutlet UILabel *registerOutlet;
@property (weak, nonatomic) IBOutlet UILabel *connectOutlet;
@property (weak, nonatomic) IBOutlet FBSDKLoginButton *loginButton;

@end

@implementation MainViewController
- (IBAction)register1:(id)sender
{
    RegisterViewController *toReg=[self.storyboard instantiateViewControllerWithIdentifier:@"RegisterViewController"];
    [self.navigationController pushViewController:toReg animated:YES];
}
- (IBAction)skipAction:(id)sender
{
    MenuViewController *toMenu=[self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
    [self.navigationController pushViewController:toMenu animated:NO];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    _loginButton.readPermissions =
    @[@"public_profile", @"email", @"user_friends"];
    _skipLoginOutlet.titleLabel.minimumFontSize = 8;
    
    _skipLoginOutlet.titleLabel.adjustsFontSizeToFitWidth = YES;
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"isServer"];
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    //    bottom1.bottomDelegate=self;
    bottom1.hidden=YES;
    
    
    // [_facebooklogin setBackgroundColor:[UIColor colorWithRed:0.231 green:0.349 blue:0.596 alpha:1]];
    //[_emaillogin setBackgroundColor:[UIColor colorWithRed:0.518 green:0.067 blue:0.725 alpha:1]];
    //  [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillDisappear:(BOOL)animated
{
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    //    bottom1.bottomDelegate=self;
    bottom1.hidden=NO;
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    sharedAppDel = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [sharedAppDel disableGesture];
    
    if([UIScreen mainScreen].bounds.size.width==320)
    {
        _loginLabel.font=[UIFont fontWithName:@"Futura-Medium" size:10.0f];
        _registerOutlet.font=[UIFont fontWithName:@"Futura-Medium" size:10.0f];
        _connectOutlet.font=[UIFont fontWithName:@"Futura-Medium" size:10.0f];
        
    }
    if ([UIScreen mainScreen].bounds.size.width==375)
    {
        _loginLabel.font=[UIFont fontWithName:@"Futura-Medium" size:12.0f];
        _registerOutlet.font=[UIFont fontWithName:@"Futura-Medium" size:12.0f];
        _connectOutlet.font=[UIFont fontWithName:@"Futura-Medium" size:12.0f];
    }
    self.navigationController.navigationBarHidden=YES;
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    bottom1.hidden=YES;
   LeftmenuCustomView *rightmenu=[LeftmenuCustomView sharedInstRightMenu];
    rightmenu.hidden=YES;
    
    
    [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"RIGHTBUTTON"];
  
    
    bottom1.menuButtonOutlet.selected = NO;
    bottom1.orderHistoryOutlet.hidden=NO;
    bottom1.orderHistoryCount.hidden=NO;
    [rightmenu setHidden:YES];
    rightmenu.hidden = YES;
    rightmenu.tblViewRightMenu.hidden = YES;
    bottom1.orderCount.hidden=YES;
    [rightmenu removeFromSuperview];
    
    //rightmenu=nil;
    bottom1.leadingConstraintsOfMainMenuButton.constant = [UIScreen mainScreen].bounds.size.width - 93.5;
//    _loginLabel.minimumFontSize = 9;
//    _loginLabel.adjustsFontSizeToFitWidth = YES;
    
}

#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
//- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
//    // Get the new view controller using [segue destinationViewController].
//    [segue destinationViewController];
////     Pass the selected object to the new view controller.
//}

- (IBAction)fbLogin:(id)sender {
    [FBSDKLoginButton class];
    [FBSDKAccessToken currentAccessToken] ;
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
    
    
    [login
     logInWithReadPermissions: @[@"public_profile", @"email", @"user_friends"]
     fromViewController:self
     handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
         if (error) {
             NSLog(@"Process error");
         } else if (result.isCancelled) {
             NSLog(@"Cancelled");
         } else {
             MenuViewController *toMenu=[self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
             [self.navigationController pushViewController:toMenu animated:NO];
             
             NSLog(@"Logged in");
             [self fetchUserInfo];
             _loginButton.delegate=self;
             
         }
     }];
}
- (void)loginButton:(FBSDKLoginButton *)loginButton didCompleteWithResult:(FBSDKLoginManagerLoginResult *)result error:(NSError *)error
{
    if(!error)
    {
        NSLog(@"You've Logged in");
        
        NSLog(@"%@", result);
    }
    //I want to go to anotherViewController after they log in
    
    
    
}
-(void)fetchUserInfo {
    [[[FBSDKGraphRequest alloc] initWithGraphPath:@"me" parameters:nil]
     startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
         if (!error) {
             NSLog(@"fetched user:%@", result);
            
             userid=[result objectForKey:@"id"];
             userNmae=[result objectForKey:@"name"];
             [self saveUser];
         }
     }];
}
-(void)saveUser
{
    [[NSUserDefaults standardUserDefaults] setObject:@"YES" forKey:@"firstTimeLogin"];
    AppDelegate *appDelegate =
    [[UIApplication sharedApplication] delegate];
    
    NSManagedObjectContext *context =
    [appDelegate managedObjectContext];
    
    BOOL entityExists = NO;
    NSString *strItemid=userid;
    
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
        addUser.user_name=userNmae;
      
        
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
- (IBAction)signIN:(id)sender
{
    LoginViewController *toViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"LoginViewController"];
    
    [self.navigationController pushViewController:toViewController animated:YES];
}


@end
