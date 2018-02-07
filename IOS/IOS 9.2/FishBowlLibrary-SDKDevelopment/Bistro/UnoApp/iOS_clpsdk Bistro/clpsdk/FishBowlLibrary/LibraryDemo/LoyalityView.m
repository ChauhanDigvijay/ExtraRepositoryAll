//
//  LoyalityView.m
//  clpsdk
//
//  Created by Gourav Shukla on 17/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "LoyalityView.h"
#import "ModelClass.h"
#import "SideMenuObject.h"
#import "MenuViewController.h"
#import "HomeViewController.h"
#import "RewardsAndOfferView.h"
#import "CoadScannerVC.h"
#import "StorelocatorViewController.h"
#import "UserProfileView.h"
#import "ApiClasses.h"
#import "LoyalityCardView.h"
#import "MyHistoryView.h"
#import "clpsdk.h"
#import "FAQViewViewController.h"
#import "ContactUSView.h"

@interface LoyalityView ()<PushNavigation>
{
    ModelClass        * obj;
    NSDictionary      * dict;
    SideMenuObject    * sideObject;
    UIViewController  * myController;
    ApiClasses        * apiCall;
    NSArray           * arrReward;
    NSString          * strLoyalityNo;
    clpsdk            * clpObj;
}
@property (weak, nonatomic) IBOutlet UILabel      * username;
@property (weak, nonatomic) IBOutlet UIScrollView * scrollViewOL;
@property (weak, nonatomic) IBOutlet UILabel      * maxLabel;
@property (weak, nonatomic) IBOutlet UILabel      * nextRewardPointsLbl;
@property (weak, nonatomic) IBOutlet UIImageView  * backgroundImage;
@property (weak, nonatomic) IBOutlet UILabel      * rewardsLabel;
@property (weak, nonatomic) IBOutlet UIView       * myLoyalityView;

@end

@implementation LoyalityView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    sideObject = [[SideMenuObject alloc]init];
    
    // custom progressbar
    self.progressBar.backgroundColor=[UIColor clearColor];
    self.progressBar.progressColor=[UIColor colorWithRed:232.0/255.0f green:36.0f/255.0 blue:52.0f/255.0 alpha:1];
    self.progressBar.progressStrokeColor=[UIColor clearColor];
    
     obj=[ModelClass sharedManager];
    
    
    //mobile setting
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
//    // background image
//    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
//    NSURL *url2 = [NSURL URLWithString:
//                   str2];
//    [self.backgroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    
    self.scrollViewOL.contentSize = CGSizeMake(self.scrollViewOL.frame.size.width, self.scrollViewOL.frame.size.height+250);
    

    // view corner radius
//    [self addCornerRadius:self.pointsView];
//    [self addCornerRadius:self.earnView];
//    [self addCornerRadius:self.programDetailView];
    
    [self userProfile];
    [self loyalityApi];
    [self shadoOffect:self.myLoyalityView];
   
    
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    // add bottom frame 
    [obj setBottomframe];
    
}

// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
}


// userProfile
-(void)userProfile
{
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerFirstName = [dic valueForKey:@"firstName"];
    NSString * strCustomerLastName = [dic valueForKey:@"lastName"];
    strLoyalityNo = [dic valueForKey:@"loyalityNo"];
    
    if(strCustomerFirstName != (NSString *)[NSNull null])
    {
        if(strCustomerLastName != (NSString *)[NSNull null])
        {
            self.username.text = [NSString stringWithFormat:@"%@ %@",strCustomerFirstName ,strCustomerLastName];
        }
        else
        {
            self.username.text = strCustomerFirstName;
        }
    }
}

# pragma mark - View Corner Radius

-(void)addCornerRadius:(UIView*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 0.5;
    textField.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
}



#pragma mark - side menu Action

- (IBAction)sideMenuBtn_Action:(id)sender
{
     sideObject.delegate = self;
    [sideObject SideMenuAction:self.view];
}



// delegate method
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
        [sideObject removeSideNave];
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
//            [sideObject removeSideNave];
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
      if([self isViewControllerExist:[FAQViewViewController class]])
      {
          [self.navigationController popToViewController:myController animated:NO];
      }
      else
      {
          FAQViewViewController * menuObj = [[FAQViewViewController alloc]initWithNibName:@"FAQViewViewController" bundle:nil];
          [self.navigationController pushViewController:menuObj animated:YES];
      }
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

#pragma mark - Loyality Api

-(void)loyalityApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerID = [dic valueForKey:@"customerID"];
    NSLog(@"customer id---------%@",strCustomerID);
    NSString * str = [NSString stringWithFormat:@"/mobile/getPointRewardInfo/%@",strCustomerID];
    [apiCall getLoyalityPoints:nil url:str withTarget:self withSelector:@selector(getLoyalityCount:)];
     apiCall = nil;
}

// loyality response
-(void)getLoyalityCount:(id)response
{
    [obj removeLoadingView:self.view];
     NSLog(@"loyality api response ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        
        if([[response valueForKey:@"pointsToNextReward"]integerValue] == 0)
        {
            self.nextRewardPointsLbl.text = @"no more reward configured";
            NSNumber * rewardsValue = [response valueForKey:@"earnedPoints"];
            [self.progressBar setValue:[rewardsValue floatValue]];
        }
        else
        {
            NSNumber  *strMax = [response valueForKey:@"pointsToNextReward"];
            //self.progressBar.maxValue = [strMax floatValue];
            
            NSNumber * rewardsValue = [response valueForKey:@"earnedPoints"];
            [self.progressBar setValue:[rewardsValue floatValue]];
            
            float valueCheck = [strMax floatValue] + [rewardsValue floatValue];
            self.maxLabel.text = [NSString stringWithFormat:@"%.f",valueCheck];
            
            
            self.progressBar.maxValue = valueCheck;
            
            self.nextRewardPointsLbl.text = [NSString stringWithFormat:@"%.f more points before your next reward.",[strMax floatValue]];
        }
    }
    // rewards count api
    [self getRewardsCount];
}

#pragma mark - memory warning

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


#pragma mark - barCode Action

- (IBAction)barCodeBtn_Action:(id)sender
{
    
    if(strLoyalityNo != (NSString *)[NSNull null])
    {
        CoadScannerVC * codeObj = [[CoadScannerVC alloc]initWithNibName:@"CoadScannerVC" bundle:nil];
        [self.navigationController pushViewController:codeObj animated:YES];
    }
    else
    {
        [self alertViewDelegate:@"No loyaltyNumber Available"];
    }
    
    
}

#pragma mark - Back Button Action

- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}


// logout button Action

-(void)logOutBt_Action
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:@"Do you want to Logout."
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                                  //[self logOutApi];
                                   [obj RemoveBottomView];
                                  
                                  // clp mobile setting api
                                  clpObj = [clpsdk sharedInstanceWithAPIKey];
                                  
                                  // event tracking method
                                  NSMutableDictionary * eventDic = [NSMutableDictionary new];
                                  NSString * userName = [[NSUserDefaults standardUserDefaults]valueForKey:@"userName"];
                                  [eventDic setValue:@"LOGOUT" forKey:@"event_name"];
                                  [eventDic setValue:userName forKey:@"item_name"];
                                  [clpObj updateAppEvent:eventDic];
                                  eventDic = nil;
                                  
                                  [self.navigationController popToRootViewControllerAnimated:YES];
                                  [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"login"];
                                  
                              }];
    
    [alert addAction:cancel1];
    UIAlertAction* noButton = [UIAlertAction
                               actionWithTitle:@"NO"
                               style:UIAlertActionStyleDefault
                               handler:^(UIAlertAction * action)
                               {
                                   //Handel your yes please button action here
                                   [alert dismissViewControllerAnimated:YES completion:nil];
                                   
                               }];
    [alert addAction:noButton];
    
    [self presentViewController:alert animated:YES completion:nil];
}


#pragma mark - Rewards Button Action

- (IBAction)rewardsBtn_Action:(id)sender
{
    
    if(arrReward.count>0)
    {
        RewardsAndOfferView * menuObj = [[RewardsAndOfferView alloc]initWithNibName:@"RewardsAndOfferView" bundle:nil];
        [self.navigationController pushViewController:menuObj animated:YES];
    }
    else
    {
        [self alertViewDelegate:@"No Rewards Available"];
    }
    
}

#pragma mark - Rewards Count Api

-(void)getRewardsCount
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerID = [dic valueForKey:@"customerID"];
    NSLog(@"customer id---------%@",strCustomerID);
    NSString * str = [NSString stringWithFormat:@"/mobile/getrewards/%@/0",strCustomerID];
    
    [apiCall getRewards:nil url:str withTarget:self withSelector:@selector(getRewardsCount:)];
    apiCall = nil;
}

// getoffers api response

-(void)getRewardsCount:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"get offers response---------%@",response);
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        arrReward = (NSArray *)[response valueForKey:@"inAppOfferList"];
   
        if(arrReward.count!=0)
        {
            self.rewardsLabel.text = [NSString stringWithFormat:@"You have %lu Rewards Available",(unsigned long)arrReward.count];
        }
        else
        {
            self.rewardsLabel.text = [NSString stringWithFormat:@"You have %lu Rewards Available",(unsigned long)0];
        }
    }
    else
    {
        if([[response valueForKey:@"successFlag"]intValue] == 0)
        {
            if([[response valueForKey:@"message"]isEqualToString:@"Invalid access token"])
            {
                
            }
        }
        self.rewardsLabel.text = [NSString stringWithFormat:@"You have %lu Rewards Available",(unsigned long)0];
    }
}


#pragma mark - AlertView Action

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
}


#pragma mark - Profile Button Action

- (IBAction)profileBtn_Action:(id)sender
{
    UserProfileView * menuObj = [[UserProfileView alloc]initWithNibName:@"UserProfileView" bundle:nil];
    [self.navigationController pushViewController:menuObj animated:YES];
}

#pragma mark - view Loyality Btn Action

- (IBAction)viewLoyalityBtn_Action:(id)sender
{
    LoyalityCardView * loyalityObj = [[LoyalityCardView alloc]initWithNibName:@"LoyalityCardView" bundle:nil];
    [self.navigationController pushViewController:loyalityObj animated:YES];
    
//    apiCall=[ApiClasses sharedManager];
//    [obj addLoadingView:self.view];
//  
//    NSString * customerID = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];
//    NSString * str = [NSString stringWithFormat:@"/mobile/getLoyaltyCard/%@",customerID];
//    
//    // get LoyaltyCardApi api
//    
//   [apiCall LoyaltyCardApi:nil url:str withTarget:self withSelector:@selector(ViewLoyalty:)];
//    apiCall = nil;
}


// loyality card response
-(void)ViewLoyalty:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"ViewLoyalty response---------%@",response);
    
    clpsdk * obj22 = [[clpsdk alloc]init];
    [obj22 openPassbookAndShowwithData:response];
}

#pragma mark - Point History Btn Action

- (IBAction)PointHistoryBtn_Action:(id)sender
{
    MyHistoryView * historyObj = [[MyHistoryView alloc]initWithNibName:@"MyHistoryView" bundle:nil];
    [self.navigationController pushViewController:historyObj animated:YES];
}
- (IBAction)programDetailAction:(id)sender {
    
    FAQViewViewController * menuObj = [[FAQViewViewController alloc]initWithNibName:@"FAQViewViewController" bundle:nil];
    menuObj.strURLProgramDetails = @"ProgramDetails";
    [self.navigationController pushViewController:menuObj animated:YES];
    
}

@end
