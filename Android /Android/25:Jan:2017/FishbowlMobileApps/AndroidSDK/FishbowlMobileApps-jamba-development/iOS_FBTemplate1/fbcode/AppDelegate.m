//
//  AppDelegate.m
//  taco2
//
//  Created by HARSH on 17/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "AppDelegate.h"
#import "MenuViewController.h"
#import "MainViewController.h"
#import "Payment Screen/PaymentScreenViewController.h"
#import "CustomBottomBarView.h"
#import "OrderhistoryViewController.h"
#import "User.h"
#import "LeftmenuCustomView.h"
#import "OrderPageViewController.h"
#import "LeftmenuTableViewCell.h"
#import "StorelocatorViewController.h"
#import "OfferViewController.h"
#import "SignUpViewController.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import "ItemsData.h"

@import HockeySDK;
@interface AppDelegate ()<BottomDelegate,LeftMenudelegate,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate>
{
    NSString *_pushToken;
    NSString *userstr;
    BOOL showuserName;
    NSArray *leftMenuarray;
    NSArray *leftmenuImageArray;
    BOOL clickedRightMenu;
    LeftmenuCustomView *rightmenu;
    CGRect frame2;
    CustomBottomBarView *bottom1;
    CGRect menuframeReset;
    NSMutableArray *mydata;
}

@end

@implementation AppDelegate


//include this function
//call will come back to this with data
-(void)callBackWithData:(NSMutableArray *)_data{
    NSLog(@"%@",_data);
    mydata=[[NSMutableArray alloc]init];
    mydata=[_data mutableCopy];
}

- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
    return [[FBSDKApplicationDelegate sharedInstance] application:application
                                                          openURL:url
                                                sourceApplication:sourceApplication
                                                       annotation:annotation];
}
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    //brinkpos
    ItemsData* FB = [[ItemsData alloc] init];
    //For all groups/categories

    dispatch_sync(dispatch_queue_create("myQue1", DISPATCH_QUEUE_SERIAL), ^{
        //call method 1
        [FB getAllGroups:self];
    });
    
//    [self performSelector:@selector(methodSignatureForSelector:) withObject:nil afterDelay:10.0];
    dispatch_queue_t queue1=dispatch_queue_create("abc", DISPATCH_QUEUE_SERIAL);
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 10.0), dispatch_get_main_queue(), ^{
        dispatch_sync(dispatch_queue_create("myQue2", DISPATCH_QUEUE_SERIAL), ^{
            //call method 2
            [FB getAllItems:self];
        });
    });
    
    dispatch_sync(dispatch_queue_create("myQue", DISPATCH_QUEUE_SERIAL), ^{
        //call method 3
                for (int i=0; i<[mydata count]; i++) {
            NSDictionary *tggg=[mydata objectAtIndex:i];
            
            NSString* itemIds =[tggg objectForKey:@"items"];// @"640205767,640191925,640191924";//these you get from "items" key from getAllgroups method call.
            
            [FB getItems:itemIds instance:self];
        }
    });
    
    //================================================//
    
    //for intems in each group.category
    
    
    
    //================================================//
    
    
    //================================================//
    
    //for all items
    [FB getAllItems:self];

    
    
    
    [[FBSDKApplicationDelegate sharedInstance] application:application
                             didFinishLaunchingWithOptions:launchOptions];
    
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"comeFromHistory"];
//   _clpSDK=[clpsdk sharedInstanceWithAPIKey:@"password"];
    
    
    
    
    //   if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0)
    //    {
    [application registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge) categories:nil]];
    [application registerForRemoteNotifications];
    //    }
    //    else
    //    {
    //        [application registerForRemoteNotificationTypes:UIRemoteNotificationTypeAlert|UIRemoteNotificationTypeBadge|UIRemoteNotificationTypeSound];
    //    }
    BOOL test = [[[NSUserDefaults standardUserDefaults] objectForKey:@"firstTimeLogin"] boolValue];
    
    if (test)
    {
        
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        
        SignUpViewController *toMenuView=[storyBoard instantiateViewControllerWithIdentifier:@"SignUpViewController"];
        UINavigationController
        *navController = [[UINavigationController alloc]
                          initWithRootViewController:toMenuView];
        self.window.rootViewController =navController;
        [self callTapGesture];
    }
    else
    {
        //        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        //        MainViewController  *toViewController = [storyBoard instantiateViewControllerWithIdentifier:@"MainViewController"];
        //
        //        UINavigationController *navC = [[UINavigationController alloc] initWithRootViewController:toViewController];
        //        self.window.rootViewController=navC;
        
        
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        SignUpViewController  *toViewController = [storyBoard instantiateViewControllerWithIdentifier:@"SignUpViewController"];
        
        UINavigationController *navC = [[UINavigationController alloc] initWithRootViewController:toViewController];
        self.window.rootViewController=navC;
        
    }
    
    bottom1=[CustomBottomBarView sharedInst];
    rightmenu=[LeftmenuCustomView sharedInstRightMenu];
    bottom1.offerCount.hidden=YES;
    bottom1.orderCount.hidden=YES;
    bottom1.offerCount.hidden=YES;
    bottom1.orderHistoryCount.hidden=YES;
    
    
    
    bottom1.orderHistoryCount.layer.borderWidth=.5f;
    bottom1.orderHistoryCount.layer.borderColor=[UIColor lightGrayColor].CGColor;
    bottom1.orderHistoryCount.layer.cornerRadius = bottom1.orderHistoryCount.frame.size.width / 2;
    bottom1.orderHistoryCount.clipsToBounds = YES;
    
    bottom1.orderCount.layer.borderWidth=.5f;
    bottom1.orderCount.layer.borderColor=[UIColor lightGrayColor].CGColor;
    bottom1.orderCount.layer.cornerRadius = bottom1.orderHistoryCount.frame.size.width / 2;
    bottom1.orderCount.clipsToBounds = YES;
    
    
    
    [bottom1 setFrame:CGRectMake(0, self.window.rootViewController.view.frame.size.height-60, self.window.rootViewController.view.frame.size.width, 60)];
    bottom1.bottomDelegate=self;
    
    [self.window.rootViewController.view addSubview:bottom1];
    
    // Override point for customization after application launch.
    [NSThread sleepForTimeInterval:2];
    [UIApplication sharedApplication].statusBarHidden = YES;
    [[BITHockeyManager sharedHockeyManager] configureWithIdentifier:@"APP_IDENTIFIER"];
    // Do some additional configuration if needed here
    [[BITHockeyManager sharedHockeyManager] startManager];
    [[BITHockeyManager sharedHockeyManager].authenticator authenticateInstallation]; // This line is obsolete in the crash only build
    
    [self.window makeKeyAndVisible];
    return YES;
}
-(void)enableGesture
{
    [self callTapGesture];
}

-(void)disableGesture
{
    NSArray *arr = [self.window.rootViewController.view gestureRecognizers];
    for (UIGestureRecognizer *gesture in arr)
    {
        [self.window.rootViewController.view removeGestureRecognizer:gesture];
    }
}

-(void)callTapGesture
{
    UISwipeGestureRecognizer *swipe = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipe:)];
    [swipe setDirection:(UISwipeGestureRecognizerDirectionLeft)];
    
    swipe.delegate = self;
    [self.window.rootViewController.view addGestureRecognizer:swipe];
    
    UISwipeGestureRecognizer *swipe2 = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipe2:)];
    [swipe2 setDirection:(UISwipeGestureRecognizerDirectionRight)];
    
    swipe2.delegate = self;
    [self.window.rootViewController.view addGestureRecognizer:swipe2];
    
}
- (void)handleSwipe:(UISwipeGestureRecognizer*)recognizer
{
    [self showUser];
    NSLog(@"mydata");
    bottom1.orderCount.hidden=YES;
    bottom1.orderHistoryCount.hidden=YES;
    rightmenu.tblViewRightMenu.hidden = NO;
    //    menuframeReset=bottom1.menuButtonOutlet.frame;
    leftMenuarray=[NSArray arrayWithObjects:@"Home",@"Store Locator",@"My Order",@"Order History", nil];
    
    leftmenuImageArray=[NSArray arrayWithObjects:@"PizzaHome Icon2@1x.png",@"menu_storelocator.png",@"menu_myorder.png",@"Order_History.png", nil];
    
    clickedRightMenu=[[[NSUserDefaults standardUserDefaults]valueForKey:@"RIGHTBUTTON"]boolValue];
    showuserName = [[[NSUserDefaults standardUserDefaults] objectForKey:@"firstTimeLogin"] boolValue];
    
    [UIView animateWithDuration:0.1 animations:^{
        
        CGRect frame1;
        
        
        
        frame1=CGRectMake((self.window.rootViewController.view.frame.size.width/2), 44,self.window.rootViewController.view.frame.size.width/2,self.window.rootViewController.view.frame.size.height);
        [rightmenu setFrame:frame1];
        rightmenu.tblViewRightMenu.delegate=self;
        rightmenu.tblViewRightMenu.dataSource=self;
        [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"RIGHTBUTTON"];
        //        frame2=CGRectMake((self.window.rootViewController.view.frame.size.width/2)-bottom1.menuButtonOutlet.frame.size.width, bottom1.menuButtonOutlet.frame.origin.y,bottom1.menuButtonOutlet.frame.size.width, bottom1.menuButtonOutlet.frame.size.height);
        //        bottom1.menuButtonOutlet.frame=frame2;
        
        bottom1.leadingConstraintsOfMainMenuButton.constant = [UIScreen mainScreen].bounds.size.width - (93.5+ frame1.size.width);
        
        rightmenu.signUpView.layer.borderWidth=.5f;
        rightmenu.signUpView.layer.borderColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1].CGColor;
        bottom1.menuButtonOutlet.selected = YES;
        rightmenu.hidden=NO;
        bottom1.orderHistoryOutlet.hidden=YES;
        
        if(showuserName==YES)
        {
            rightmenu.signInSignUpLabel.text=[NSString stringWithFormat:@"Welcome %@",userstr];
            rightmenu.signInSignUpLabel.adjustsFontSizeToFitWidth=YES;
            rightmenu.userName.text=@"Enjoy the benefits of ZPIZZA.save favorites.plane an express order,and track your rewards! ";
            rightmenu.userName.numberOfLines=0;
        }
        else
        {
            rightmenu.signInSignUpLabel.text=@"Sign in or Sign Up";
            rightmenu.userName.text=@"Enjoy the benefits of ZPIZZA.save favorites.plane an express order,and track your rewards! ";
            rightmenu.userName.numberOfLines=0;
            rightmenu.userName.adjustsFontSizeToFitWidth=YES;
        }
        [self.window.rootViewController.view addSubview:rightmenu];
        
        
        
        
    } completion:^(BOOL finished)
     {
         
     }];
    
    
    
}

- (void)handleSwipe2:(UISwipeGestureRecognizer*)recognizer
{
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"againClicked"];
    [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"rightbutton"];
    
    
    [UIView animateWithDuration:0.1 animations:^{
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
    } completion:^(BOOL finished)
     {
         
     }];
}

-(void)offerPage
{
    bottom1.offerCount.hidden=YES;
    // bottom1.orderCount.hidden=YES;
    bottom1.orderCount.hidden=NO;
    bottom1.orderHistoryCount.hidden=NO;
    bottom1.orderHistoryCount.text=[[NSUserDefaults standardUserDefaults]valueForKey:@"historyCount"];
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    OfferViewController  *toViewController = [storyBoard instantiateViewControllerWithIdentifier:@"OfferViewController"];
    
    UINavigationController *navC = [[UINavigationController alloc] initWithRootViewController:toViewController];
    self.window.rootViewController=navC;
    bottom1.orderHistoryCount.layer.borderWidth=.5f;
    
    //bottom1.orderHistoryCount.layer.borderColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1].CGColor;
    bottom1.orderCount.layer.borderWidth=.5f;
    bottom1.orderCount.layer.borderColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1].CGColor;
    bottom1.orderCount.layer.cornerRadius = bottom1.orderCount.frame.size.width / 2;
    bottom1.orderCount.clipsToBounds = YES;
    bottom1.orderHistoryCount.layer.borderColor=[UIColor lightGrayColor].CGColor;
    bottom1.orderHistoryCount.layer.cornerRadius = bottom1.orderHistoryCount.frame.size.width / 2;
    bottom1.orderHistoryCount.clipsToBounds = YES;
    [self.window.rootViewController.view addSubview:bottom1];
    [self callTapGesture];
}

-(void)orderHistory
{
    bottom1.offerCount.hidden=YES;
    // bottom1.orderCount.hidden=YES;
    bottom1.orderCount.hidden=NO;
    bottom1.orderHistoryCount.hidden=NO;
    bottom1.orderHistoryCount.text=[[NSUserDefaults standardUserDefaults]valueForKey:@"historyCount"];
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    OrderhistoryViewController  *toViewController = [storyBoard instantiateViewControllerWithIdentifier:@"OrderhistoryViewController"];
    
    UINavigationController *navC = [[UINavigationController alloc] initWithRootViewController:toViewController];
    self.window.rootViewController=navC;
    bottom1.orderHistoryCount.layer.borderWidth=.5f;
    
    //bottom1.orderHistoryCount.layer.borderColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1].CGColor;
    bottom1.orderHistoryCount.layer.borderColor=[UIColor lightGrayColor].CGColor;
    bottom1.orderHistoryCount.layer.cornerRadius = bottom1.orderHistoryCount.frame.size.width / 2;
    bottom1.orderHistoryCount.clipsToBounds = YES;
    [self.window.rootViewController.view addSubview:bottom1];
    [self callTapGesture];
}
-(void)showUser
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
        
        userstr=info.user_name;
        NSLog(@"count==%i",++count);
    }
    
    
    
}
-(void)menuShow
{
    [self showUser];
    NSLog(@"mydata");
    bottom1.orderCount.hidden=YES;
    bottom1.orderHistoryCount.hidden=YES;
    
    CGRect frame1;
    //    menuframeReset=bottom1.menuButtonOutlet.frame;
    leftMenuarray=[NSArray arrayWithObjects:@"Home",@"Store Locator",@"My Order",@"Order History", nil];
    
    leftmenuImageArray=[NSArray arrayWithObjects:@"PizzaHome Icon2@1x.png",@"menu_storelocator.png",@"menu_myorder.png",@"Order_History.png", nil];
    
    clickedRightMenu=[[[NSUserDefaults standardUserDefaults]valueForKey:@"RIGHTBUTTON"]boolValue];
    showuserName = [[[NSUserDefaults standardUserDefaults] objectForKey:@"firstTimeLogin"] boolValue];
    
    if(clickedRightMenu==YES)
    {
        
        frame1=CGRectMake((self.window.rootViewController.view.frame.size.width/2), 44,self.window.rootViewController.view.frame.size.width/2,self.window.rootViewController.view.frame.size.height);
        [rightmenu setFrame:frame1];
        rightmenu.tblViewRightMenu.delegate=self;
        rightmenu.tblViewRightMenu.dataSource=self;
        [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"RIGHTBUTTON"];
        
        bottom1.leadingConstraintsOfMainMenuButton.constant = [UIScreen mainScreen].bounds.size.width - (93.5+ frame1.size.width);
        
        rightmenu.signUpView.layer.borderWidth=.5f;
        rightmenu.signUpView.layer.borderColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1].CGColor;
        bottom1.menuButtonOutlet.selected = YES;
        rightmenu.hidden=NO;
        bottom1.orderHistoryOutlet.hidden=YES;
        
        if ([UIScreen mainScreen].bounds.size.width==320)
        {
            rightmenu.userName.font=[UIFont fontWithName:@"Futura-Medium" size:8.0f];
        }
        if(showuserName==YES)
        {
            rightmenu.signInSignUpLabel.text=[NSString stringWithFormat:@"Welcome %@",userstr];
            rightmenu.signInSignUpLabel.adjustsFontSizeToFitWidth=YES;
            rightmenu.userName.text=@"Enjoy the benefits of ZPIZZA.save favorites. plane an express order,and track your rewards!";
            rightmenu.userName.numberOfLines=0;
        }
        else
        {
            rightmenu.signInSignUpLabel.text=@"Sign in or Sign Up";
            rightmenu.userName.text=@"Enjoy the benefits of ZPIZZA.save favorites.plane an express order,and track your rewards! ";
            rightmenu.userName.numberOfLines=0;
            rightmenu.userName.adjustsFontSizeToFitWidth=YES;
        }
        [self.window.rootViewController.view addSubview:rightmenu];
        
        rightmenu.tblViewRightMenu.hidden = NO;
    }
    else
    {
        
        [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"RIGHTBUTTON"];
        
        
        bottom1.menuButtonOutlet.selected = NO;
        bottom1.orderHistoryOutlet.hidden=NO;
        bottom1.orderHistoryCount.hidden=NO;
        [rightmenu setHidden:YES];
        rightmenu.hidden = YES;
        rightmenu.tblViewRightMenu.hidden = YES;
        bottom1.orderCount.hidden=YES;
        [rightmenu removeFromSuperview];
        
        // rightmenu=nil;
        bottom1.leadingConstraintsOfMainMenuButton.constant = [UIScreen mainScreen].bounds.size.width - 93.5;
    }
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return 55;;
    
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
    
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    return [leftMenuarray count];
    
    
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    LeftmenuTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"LeftmenuTableViewCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"LeftmenuTableViewCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.leftMenuItem.font = [UIFont fontWithName:@"Futura-Medium" size:12.0f];
    cell.leftMenuItem.text=[leftMenuarray objectAtIndex:indexPath.row];
    cell.leftMenuItem.textColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    [cell.emnuitemImage sizeToFit];
    cell.emnuitemImage.image=[UIImage imageNamed:[leftmenuImageArray objectAtIndex:indexPath.row]];
    return cell;
    
    
    
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if(indexPath.row==0)
    {
        [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"RIGHTBUTTON"];
        frame2=CGRectMake(bottom1.frame.size.width-(bottom1.menuButtonOutlet.frame.size.width+24), 10,bottom1.menuButtonOutlet.frame.size.width, bottom1.menuButtonOutlet.frame.size.height);
        bottom1.menuButtonOutlet.frame=frame2;
        UIImage *buttonImage = [UIImage imageNamed:@"More _Menu_grey_3x.png"];
        [bottom1.menuButtonOutlet setImage:buttonImage forState:UIControlStateNormal];
        
        bottom1.orderHistoryOutlet.hidden=NO;
        [rightmenu setHidden:YES];
        rightmenu.hidden = YES;
        rightmenu.tblViewRightMenu.hidden = YES;
        
        [rightmenu removeFromSuperview];
        // rightmenu=nil;
        
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        MenuViewController  *toViewController = [storyBoard instantiateViewControllerWithIdentifier:@"MenuViewController"];
        
        UINavigationController *navC = [[UINavigationController alloc] initWithRootViewController:toViewController];
        self.window.rootViewController=navC;
        bottom1.leadingConstraintsOfMainMenuButton.constant = [UIScreen mainScreen].bounds.size.width - 93.5;
        [self.window.rootViewController.view addSubview:bottom1];
        [self callTapGesture];
        
    }
    else if(indexPath.row==1)
    {
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        StorelocatorViewController  *toViewController = [storyBoard instantiateViewControllerWithIdentifier:@"StorelocatorViewController"];
        
        UINavigationController *navC = [[UINavigationController alloc] initWithRootViewController:toViewController];
        self.window.rootViewController=navC;
        [self.window.rootViewController.view addSubview:bottom1];
        [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"RIGHTBUTTON"];
        frame2=CGRectMake(bottom1.frame.size.width-(bottom1.menuButtonOutlet.frame.size.width+24), 10,bottom1.menuButtonOutlet.frame.size.width, bottom1.menuButtonOutlet.frame.size.height);
        bottom1.menuButtonOutlet.frame=frame2;
        
        bottom1.menuButtonOutlet.selected = NO;
        bottom1.orderHistoryOutlet.hidden=NO;
        [rightmenu setHidden:YES];
        rightmenu.hidden = YES;
        rightmenu.tblViewRightMenu.hidden = YES;
        bottom1.leadingConstraintsOfMainMenuButton.constant = [UIScreen mainScreen].bounds.size.width - 93.5;
        [rightmenu removeFromSuperview];
        //rightmenu=nil;
        
        
        [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"TOSTOREFROMMENU"];
        [self callTapGesture];
    }
    else if(indexPath.row==2)
    {
        [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"orderToOrder"];
        //        NSUserDefaults *selectedItem=[NSUserDefaults standardUserDefaults];
        //        [selectedItem removeObjectForKey:@"selected"];
        //        [selectedItem synchronize];
        //        NSUserDefaults *costSelectedItem=[NSUserDefaults standardUserDefaults];
        //        [costSelectedItem removeObjectForKey:@"costSelectedItem"];
        //        [costSelectedItem synchronize];
        //
        //        NSUserDefaults *descSelectedItem=[NSUserDefaults standardUserDefaults];
        //        [descSelectedItem removeObjectForKey:@"descSelectedItem"];
        //
        //        [descSelectedItem synchronize];
        //        NSUserDefaults *Selecteditemid=[NSUserDefaults standardUserDefaults];
        //        [Selecteditemid removeObjectForKey:@"Selecteditemid"];
        //
        //        [Selecteditemid synchronize];
        //        NSUserDefaults *imageurlSelecteditem=[NSUserDefaults standardUserDefaults];
        //        [imageurlSelecteditem removeObjectForKey:@"imageurlSelecteditem"];
        //
        //        [imageurlSelecteditem synchronize];
        //        [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"fromMENUOrder"];
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        OrderPageViewController  *toViewController = [storyBoard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
        
        int countorder=(long)[bottom1.orderCount.text intValue];
        NSLog(@"%d",countorder);
        if (countorder==0)
        {
            
            toViewController.noItemOutlet.hidden=YES;
            
            toViewController.tableBottomLineView.hidden=NO;
            toViewController.totalView.hidden=YES;
            
        }
        else
        {
            toViewController.noItemOutlet.hidden=NO;
            
            toViewController.tableBottomLineView.hidden=NO;
            toViewController.totalView.hidden=NO;
        }
        
        UINavigationController *navC = [[UINavigationController alloc] initWithRootViewController:toViewController];
        self.window.rootViewController=navC;
        [self.window.rootViewController.view addSubview:bottom1];
        [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"RIGHTBUTTON"];
        frame2=CGRectMake(bottom1.frame.size.width-(bottom1.menuButtonOutlet.frame.size.width+24), 10,bottom1.menuButtonOutlet.frame.size.width, bottom1.menuButtonOutlet.frame.size.height);
        bottom1.menuButtonOutlet.frame=frame2;
        
        bottom1.menuButtonOutlet.selected = NO;
        bottom1.orderHistoryOutlet.hidden=NO;
        [rightmenu setHidden:YES];
        rightmenu.hidden = YES;
        rightmenu.tblViewRightMenu.hidden = YES;
        
        [rightmenu removeFromSuperview];
        // rightmenu=nil;
        
        bottom1.orderCount.layer.borderWidth=.5f;
        bottom1.orderCount.layer.borderColor=[UIColor lightGrayColor].CGColor;
        bottom1.orderCount.layer.cornerRadius = bottom1.orderCount.frame.size.width / 2;
        bottom1.orderCount.clipsToBounds = YES;
        bottom1.leadingConstraintsOfMainMenuButton.constant = [UIScreen mainScreen].bounds.size.width - 93.5;
        [self callTapGesture];
    }
    else if(indexPath.row==3)
    {
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        OrderhistoryViewController  *toViewController = [storyBoard instantiateViewControllerWithIdentifier:@"OrderhistoryViewController"];
        
        UINavigationController *navC = [[UINavigationController alloc] initWithRootViewController:toViewController];
        self.window.rootViewController=navC;
        [self.window.rootViewController.view addSubview:bottom1];
        [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"RIGHTBUTTON"];
        frame2=CGRectMake(bottom1.frame.size.width-(bottom1.menuButtonOutlet.frame.size.width+24), 10,bottom1.menuButtonOutlet.frame.size.width, bottom1.menuButtonOutlet.frame.size.height);
        bottom1.menuButtonOutlet.frame=frame2;
        
        bottom1.menuButtonOutlet.selected = NO;
        bottom1.orderHistoryOutlet.hidden=NO;
        [rightmenu setHidden:YES];
        rightmenu.hidden = YES;
        rightmenu.tblViewRightMenu.hidden = YES;
        
        [rightmenu removeFromSuperview];
        //  rightmenu=nil;
        bottom1.orderHistoryCount.layer.borderWidth=.5f;
        bottom1.orderHistoryCount.layer.borderColor=[UIColor lightGrayColor].CGColor;
        
        bottom1.orderHistoryCount.layer.cornerRadius = bottom1.orderHistoryCount.frame.size.width / 2;
        bottom1.orderHistoryCount.clipsToBounds = YES;
        bottom1.leadingConstraintsOfMainMenuButton.constant = [UIScreen mainScreen].bounds.size.width - 93.5;
        [self callTapGesture];
    }
    
    
    
    
    
    
    
}

-(void)orderPage
{
    
    bottom1.offerCount.hidden=YES;
    
    //bottom1.orderHistoryCount.hidden=YES;
    bottom1.orderHistoryCount.hidden=NO;
    bottom1.orderCount.hidden=NO;
    bottom1.orderCount.text=[[NSUserDefaults standardUserDefaults]valueForKey:@"orderCount"];
    
    [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"orderToOrder"];
    NSUserDefaults *selectedItem=[NSUserDefaults standardUserDefaults];
    [selectedItem removeObjectForKey:@"selected"];
    [selectedItem synchronize];
    NSUserDefaults *costSelectedItem=[NSUserDefaults standardUserDefaults];
    [costSelectedItem removeObjectForKey:@"costSelectedItem"];
    [costSelectedItem synchronize];
    
    NSUserDefaults *descSelectedItem=[NSUserDefaults standardUserDefaults];
    [descSelectedItem removeObjectForKey:@"descSelectedItem"];
    
    [descSelectedItem synchronize];
    NSUserDefaults *Selecteditemid=[NSUserDefaults standardUserDefaults];
    [Selecteditemid removeObjectForKey:@"Selecteditemid"];
    
    [Selecteditemid synchronize];
    NSUserDefaults *imageurlSelecteditem=[NSUserDefaults standardUserDefaults];
    [imageurlSelecteditem removeObjectForKey:@"imageurlSelecteditem"];
    
    [imageurlSelecteditem synchronize];
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    OrderPageViewController  *toViewController = [storyBoard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
    
    int countorder=(long)[bottom1.orderCount.text intValue];
    NSLog(@"%d",countorder);
    if (countorder==0)
    {
        
        toViewController.noItemOutlet.hidden=YES;
        
        toViewController.tableBottomLineView.hidden=NO;
        toViewController.totalView.hidden=YES;
        
    }
    else
    {
        toViewController.noItemOutlet.hidden=NO;
        
        toViewController.tableBottomLineView.hidden=NO;
        toViewController.totalView.hidden=NO;
    }
    
    UINavigationController *navC = [[UINavigationController alloc] initWithRootViewController:toViewController];
    self.window.rootViewController=navC;
    
    bottom1.orderCount.layer.borderWidth=.5f;
    bottom1.orderCount.layer.borderColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1].CGColor;
    bottom1.orderCount.layer.cornerRadius = bottom1.orderCount.frame.size.width / 2;
    bottom1.orderCount.clipsToBounds = YES;
    [self.window.rootViewController.view addSubview:bottom1];
    [self callTapGesture];
    
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    [FBSDKAppEvents activateApp];
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    // Saves changes in the application's managed object context before the application terminates.
    [self saveContext];
}

# pragma mark - Push Notification

-(void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken{
    NSString *token = [[deviceToken description] stringByTrimmingCharactersInSet:      [NSCharacterSet characterSetWithCharactersInString:@"<>"]];
    token = [token stringByReplacingOccurrencesOfString:@" " withString:@""];
    _pushToken=token;
    [self customerRegistration];
    NSLog(@"Token: %@",token);
}

-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo{
    @try {
//        [_clpSDK setDelegate:self];
//        [_clpSDK processPushMessage:userInfo];
    }
    @catch (NSException *exception) {
        NSLog(@"didReceiveRemoteNotification : %@",exception.reason);
    }
}

-(void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error{
    NSLog(@"%@",error.description);
}
-(void)clpOpenPassbook{
    // show loader
}
-(void)clpClosePassbook{
    // dismiss loader
}

#pragma mark -


- (UIInterfaceOrientationMask)application:(UIApplication *)application supportedInterfaceOrientationsForWindow:(UIWindow *)window
{
    return UIInterfaceOrientationMaskPortrait;
}

- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation
{
    return UIInterfaceOrientationPortrait;
}
- (BOOL)shouldAutorotate
{
    return false;
}
- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
{
    return false;
}
#pragma mark - Core Data stack

@synthesize managedObjectContext = _managedObjectContext;
@synthesize managedObjectModel = _managedObjectModel;
@synthesize persistentStoreCoordinator = _persistentStoreCoordinator;

- (NSURL *)applicationDocumentsDirectory {
    // The directory the application uses to store the Core Data store file. This code uses a directory named "com.yipmessenger.taco2" in the application's documents directory.
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

- (NSManagedObjectModel *)managedObjectModel {
    // The managed object model for the application. It is a fatal error for the application not to be able to find and load its model.
    if (_managedObjectModel != nil) {
        return _managedObjectModel;
    }
    NSURL *modelURL = [[NSBundle mainBundle] URLForResource:@"iOS_FBTemplate1" withExtension:@"momd"];
    _managedObjectModel = [[NSManagedObjectModel alloc] initWithContentsOfURL:modelURL];
    return _managedObjectModel;
}

- (NSPersistentStoreCoordinator *)persistentStoreCoordinator {
    // The persistent store coordinator for the application. This implementation creates and returns a coordinator, having added the store for the application to it.
    if (_persistentStoreCoordinator != nil) {
        return _persistentStoreCoordinator;
    }
    
    // Create the coordinator and store
    
    _persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    NSURL *storeURL = [[self applicationDocumentsDirectory] URLByAppendingPathComponent:@"iOS_FBTemplate1.sqlite"];
    NSError *error = nil;
    NSString *failureReason = @"There was an error creating or loading the application's saved data.";
    if (![_persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeURL options:nil error:&error]) {
        // Report any error we got.
        NSMutableDictionary *dict = [NSMutableDictionary dictionary];
        dict[NSLocalizedDescriptionKey] = @"Failed to initialize the application's saved data";
        dict[NSLocalizedFailureReasonErrorKey] = failureReason;
        dict[NSUnderlyingErrorKey] = error;
        error = [NSError errorWithDomain:@"YOUR_ERROR_DOMAIN" code:9999 userInfo:dict];
        // Replace this with code to handle the error appropriately.
        // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
        NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
        abort();
    }
    
    return _persistentStoreCoordinator;
}


- (NSManagedObjectContext *)managedObjectContext {
    // Returns the managed object context for the application (which is already bound to the persistent store coordinator for the application.)
    if (_managedObjectContext != nil) {
        return _managedObjectContext;
    }
    
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if (!coordinator) {
        return nil;
    }
    _managedObjectContext = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSMainQueueConcurrencyType];
    [_managedObjectContext setPersistentStoreCoordinator:coordinator];
    return _managedObjectContext;
}

#pragma mark - Core Data Saving support

- (void)saveContext {
    NSManagedObjectContext *managedObjectContext = self.managedObjectContext;
    if (managedObjectContext != nil) {
        NSError *error = nil;
        if ([managedObjectContext hasChanges] && ![managedObjectContext save:&error]) {
            // Replace this implementation with code to handle the error appropriately.
            // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
            abort();
        }
    }
}


#pragma mark - Customer Registration
-(void)customerRegistration{
//    
//    CLPCustomer *cus=[[CLPCustomer alloc]init];
//    cus.companyID=1;
//    cus.firstName=@"Hitesh";
//    cus.lastName=@"Raja";
//    cus.emailID=@"testcseapi@gmail.com";
//    cus.loginID=@"testcseapi@gmail.com";
//    cus.loginPassword=@"password";
//    cus.loyalityNo=@"1";
//    cus.loyalityLevel=@"1";
//    cus.homePhone=@"1000000000";
//    cus.cellPhone=@"1000000000";
//    cus.additionalPhone=@"10000000000";
//    cus.addressLine1=@"US";
//    cus.addressLine2=@"California";
//    cus.addressCity= @"CA";
//    cus.addressState= @"CA";
//    cus.addressZip= @"98526";
//    cus.addressCountry=@"US";
//    cus.customerTenantID=@"8";
//    cus.customerStatus=@"Active";
//    cus.lastActivtiy=@"UPDATE";
//    cus.statusCode=1;
//    cus.registeredIP=@"192.168.1.14";
//    cus.customerGender=@"Male";
//    cus.dateOfBirth=@"03-09-1988";
//    cus.customerAge=@"25"; //@"45";
//    cus.homeStore=@"1";
//    cus.favoriteDepartment=@"1";
//    cus.pushOpted=@"Y";
//    
//    cus.emailOpted=@"Y";
//    cus.emailOpted=@"N";
//    
//    cus.smsOpted=@"Y";
//    
//    cus.smsOpted=@"N";
//    cus.adOpted=@"N";
//    cus.phoneOpted=@"N";
//    //cus.loyalityRewards=@"10";
//    cus.modifiedBy=@"Hitesh";
//    cus.deviceID=[self getDeviceID];
//    cus.pushToken=_pushToken;
//    cus.deviceType=[[UIDevice currentDevice] model];
//    cus.deviceOsVersion=[[UIDevice currentDevice] systemVersion];
//    cus.deviceVendor=@"APPLE";
//    cus.enabledFlag=@"Y";
//    cus.createdBy=@"Hitesh";
//    [_clpSDK saveCustomer:cus :^(CLPCustomer *cusInfo, NSError *error) {
//        NSLog(@"YEsSuccess");
//    }];
//#ifdef CLP_ANALYTICS
//    Start Location updates
//    [_clpSDK startStandardUpdate];
//#endif
//    //
}

-(NSString*)getDeviceID
{
    if ([[UIDevice currentDevice]respondsToSelector:@selector(identifierForVendor)]) {
        return [UIDevice currentDevice].identifierForVendor.UUIDString;
    }else{
        return @"";
        //return [UIDevice currentDevice].uniqueIdentifier
        //return [[UIDevice currentDevice] performSelector:@selector(uniqueIdentifier)];
    }
}

@end
