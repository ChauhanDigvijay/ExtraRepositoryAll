//
//  FAQViewViewController.m
//  clpsdk
//
//  Created by Gourav Shukla on 30/11/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "FAQViewViewController.h"
#import "SideMenuObject.h"
#import "ModelClass.h"
#import "HomeViewController.h"
#import "LoyalityView.h"
#import "MenuViewController.h"
#import "StorelocatorViewController.h"
#import "RewardsAndOfferView.h"
#import "ContactUSView.h"
#import "UserProfileView.h"



@interface FAQViewViewController ()<PushNavigation>
{
    SideMenuObject   * sideObject;
    ModelClass       * obj;
    UIViewController * myController;
    NSDictionary     * dic;
}
@property (unsafe_unretained, nonatomic) IBOutlet UIView *faqView;
@property (unsafe_unretained, nonatomic) IBOutlet UIWebView *webview;
@property (strong, nonatomic) IBOutlet UILabel *titleLable;

@end

@implementation FAQViewViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    obj=[ModelClass sharedManager];
    sideObject = [[SideMenuObject alloc]init];
    

    if ([self.strURLProgramDetails isEqualToString:@"ProgramDetails"])
    {
        self.titleLable.text = @"ProgramDetails";
        NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
        NSData *data = [currentDefaults objectForKey:@"themeSetting"];
        NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
        
        
        NSLog(@"print dic %@",dict.description);
        NSArray *arrThemeConfigSettings = [[dict valueForKey:@"themeDetails"] valueForKey:@"themeConfigSettings"];
        NSString * strTermCondition;
        NSLog(@"arrThemeConfigSettings is %@",arrThemeConfigSettings.description);
        NSLog(@"arrThemeConfigSettings.count is %lu", (unsigned long)arrThemeConfigSettings.count);
        
        for (NSDictionary *dicSetting in arrThemeConfigSettings) {
            
            NSString * strPageName = [dicSetting objectForKey:@"pageName"];
            if ([strPageName isEqualToString:@"General"]) {
                
                NSArray *arrData = [dicSetting objectForKey:@"themeCreativeSettings"];
                NSLog(@"arrData is %@",arrData.description);
                
                for (NSDictionary *DictThemeCreativeSettings in arrData) {
                    
                    NSString *strKey = [DictThemeCreativeSettings objectForKey:@"configName"];
                    if ([strKey isEqualToString:@"ProgramDetail"])
                    {
                        strTermCondition = [DictThemeCreativeSettings objectForKey:@"configValue"];
                    }
                    
                }
            }
            
        }
        
        strTermCondition = [NSString stringWithFormat:@"https://%@",strTermCondition];
        NSString   *urlString=strTermCondition;
        NSURL *url=[NSURL URLWithString:urlString];
        NSURLRequest *obj1 = [NSURLRequest requestWithURL:url];
        [self.webview loadRequest:obj1];
    }
    else
    {
        self.titleLable.text = @"FAQ";
        NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
        NSData *data = [currentDefaults objectForKey:@"themeSetting"];
        NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
        
        
        NSLog(@"print dic %@",dict.description);
        NSArray *arrThemeConfigSettings = [[dict valueForKey:@"themeDetails"] valueForKey:@"themeConfigSettings"];
        NSString * strTermCondition;
        NSLog(@"arrThemeConfigSettings is %@",arrThemeConfigSettings.description);
        NSLog(@"arrThemeConfigSettings.count is %lu", (unsigned long)arrThemeConfigSettings.count);
        
        for (NSDictionary *dicSetting in arrThemeConfigSettings) {
            
            NSString * strPageName = [dicSetting objectForKey:@"pageName"];
            if ([strPageName isEqualToString:@"General"]) {
                
                NSArray *arrData = [dicSetting objectForKey:@"themeCreativeSettings"];
                
                for (NSDictionary *DictThemeCreativeSettings in arrData) {
                    
                    NSString *strKey = [DictThemeCreativeSettings objectForKey:@"configName"];
                    if ([strKey isEqualToString:@"FAQ"])
                    {
                        strTermCondition = [DictThemeCreativeSettings objectForKey:@"configValue"];
                    }
                    
                }
            }
            
        }
        
        strTermCondition = [NSString stringWithFormat:@"https://%@",strTermCondition];
        NSString   *urlString=strTermCondition;
        NSURL *url=[NSURL URLWithString:urlString];
        NSURLRequest *obj1 = [NSURLRequest requestWithURL:url];
        [self.webview loadRequest:obj1];
    }
    
    
  
    
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}


# pragma mark - back button Action

- (IBAction)backBtn_Action:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
}


# pragma mark - Side menu button Action

- (IBAction)menuBtn_Action:(id)sender
{
    sideObject.delegate = self;
    [sideObject SideMenuAction:self.view];
}


#pragma mark -  navigation method Action
-(void)didSelectAtIndexPathRow:(NSIndexPath *)indexPath
{
    if(indexPath.row == 0)
    {
        if([self isViewControllerExist:[HomeViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
    }
    else if(indexPath.row == 1)
    {
        if([self isViewControllerExist:[LoyalityView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            LoyalityView * menuObj = [[LoyalityView alloc]initWithNibName:@"LoyalityView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 2)
    {
        if([self isViewControllerExist:[MenuViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"MenuToStore"];
            MenuViewController * menuObj = [[MenuViewController alloc]initWithNibName:@"MenuViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 3)
    {
        if([self isViewControllerExist:[StorelocatorViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"MenuToStore"];
            StorelocatorViewController * menuObj = [[StorelocatorViewController alloc]initWithNibName:@"StorelocatorViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 4)
    {
        if([self isViewControllerExist:[RewardsAndOfferView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            RewardsAndOfferView * menuObj = [[RewardsAndOfferView alloc]initWithNibName:@"RewardsAndOfferView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    
//    else if(indexPath.row == 5)
//    {
//        [sideObject removeSideNave];
//        [self alertViewDelegate:@"No Setting Available"];
//    }
    
    else if(indexPath.row == 5)
    {
        if([self isViewControllerExist:[UserProfileView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            UserProfileView * menuObj = [[UserProfileView alloc]initWithNibName:@"UserProfileView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 6)
    {
        [sideObject removeSideNave];
    }
    else if(indexPath.row == 7)
    {
        if([self isViewControllerExist:[ContactUSView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            ContactUSView * menuObj = [[ContactUSView alloc]initWithNibName:@"ContactUSView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    
    else
    {
        [sideObject removeSideNave];
    }
}


// check controller exist
-(BOOL)isViewControllerExist:(Class)isController
{
    for (UIViewController *controller in self.navigationController.viewControllers)
    {
        if ([controller isKindOfClass:isController])
        {
            myController = controller;
            return YES;
        }
    }
    return NO;
}


# pragma mark - alert view method

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:ok];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

@end
