//
//  MyLoyalityView.m
//  clpsdk
//
//  Created by Gourav Shukla on 07/02/17.
//  Copyright © 2017 clyptech. All rights reserved.
//

#import "MyLoyalityView.h"
#import "PointBankCell.h"
#import "SideMenuObject.h"
#import "HomeViewController.h"
#import "MenuViewController.h"
#import "StorelocatorViewController.h"
#import "RewardsAndOfferView.h"
#import "UserProfileView.h"
#import "FAQViewViewController.h"
#import "ContactUSView.h"
#import "ApiClasses.h"
#import "ModelClass.h"
#import "PointBankCell.h"
#import "clpsdk.h"
#import "Constant.h"
#import "HtmlOffersAndRewards.h"


@interface MyLoyalityView ()<PushNavigation,UITableViewDelegate,UITableViewDataSource>
{
    SideMenuObject    * sideObject;
    UIViewController  * myController;
    ApiClasses        * apiCall;
    ModelClass        * obj;
    NSMutableArray    * arrRewards;
    NSString          * earnedPoints;
    int                 count;
    clpsdk            * clpObj;
}
@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tblView;
@property (weak, nonatomic) IBOutlet UIView *footerView;
@property (weak, nonatomic) IBOutlet UILabel *earnedPointsLbl;
@property (weak, nonatomic) IBOutlet UILabel *pointToNextRewardLbl;
@property (weak, nonatomic) IBOutlet UILabel *userNameLbl;
@property (unsafe_unretained, nonatomic) IBOutlet UIView *pointsView;

@end

@implementation MyLoyalityView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    obj=[ModelClass sharedManager];
    sideObject = [[SideMenuObject alloc]init];
   
    [self userProfile];
   // [self loyalityApi];
    
    
}


// viewWill Appear 
-(void)viewWillAppear:(BOOL)animated
{
      count = 0;
      [super viewWillAppear:YES];
      [self loyalityApi];
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
   
    if(strCustomerFirstName != (NSString *)[NSNull null])
    {
        if(strCustomerLastName != (NSString *)[NSNull null])
        {
            self.userNameLbl.text = [NSString stringWithFormat:@"%@ %@",strCustomerFirstName ,strCustomerLastName];
        }
        else
        {
            self.userNameLbl.text = strCustomerFirstName;
        }
    }
}


#pragma mark - getRewardoffer Api

-(void)getRewardofferApi
{
    //[obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];

    NSString * str = [NSString stringWithFormat:@"/loyaltyw/getallrewardoffer"];
    
    [apiCall getallrewardoffer:nil url:str withTarget:self withSelector:@selector(getRewardoffer:)];
    apiCall = nil;
}

// getoffers api response

-(void)getRewardoffer:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"get getrewards response%@",response);
    
    arrRewards = [NSMutableArray new];
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        NSArray * arr = (NSArray *)[response valueForKey:@"loyaltyPointRedemptionList"];
        
       // for(int j=0; j<arr.count; j++)
       // {
           // if([[[arr objectAtIndex:j]valueForKey:@"enabled"]intValue]==1)
           // {
                NSSortDescriptor* sortOrder = [NSSortDescriptor sortDescriptorWithKey:@"loyaltyPoints" ascending: YES];
                
                NSArray * resultArray  = [arr sortedArrayUsingDescriptors:[NSArray arrayWithObject: sortOrder]];
        
 
        NSArray *filtered = [resultArray filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(enabled == %d)", 1]];

        NSLog(@"filtered array value -------- %@",filtered);
        
        
        NSLog(@"filtered array count value -------- %lu",(unsigned long)filtered.count);
   
      [arrRewards addObjectsFromArray:filtered];
 
                for(int i=0 ; i<arrRewards.count; i++)
                {
                    NSNumber  * strPoints = [[arrRewards objectAtIndex:i]valueForKey:@"loyaltyPoints"];
                    NSString * points = [NSString stringWithFormat:@"%@ PTS",[strPoints stringValue]];
                    if([points integerValue] <= [earnedPoints integerValue])
                    {
                        count++;
                    }
                        
               }
        
        
        NSLog(@"count value is --------- %d",count);
        
         self.tblView.tableFooterView = self.footerView;
         [self.tblView reloadData];
    }
    else
    {
        [self alertViewDelegate:@"No Rewards Available"];
    }
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
    //[obj removeLoadingView:self.view];
    NSLog(@"loyality api response ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
            NSNumber  *nextReward = [response valueForKey:@"pointsToNextReward"];
          NSString * nextPoint = [NSString stringWithFormat:@"You need %@ more pts for the next offer",[nextReward stringValue]];
        self.pointToNextRewardLbl.text = nextPoint;
        
            //self.progressBar.maxValue = [strMax floatValue];
            
            NSNumber * rewardsValue = [response valueForKey:@"earnedPoints"];
            earnedPoints = [NSString stringWithFormat:@"%@ Pts Available",[rewardsValue stringValue]];
            self.earnedPointsLbl.text = earnedPoints;
     }
    
    [self getRewardofferApi];
}


#pragma mark - Table View DataSource

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 80;
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [arrRewards count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    PointBankCell *cell = [tableView dequeueReusableCellWithIdentifier:@"PointBankCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"PointBankCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    if([[[arrRewards objectAtIndex:indexPath.row]valueForKey:@"fishBowlPromotion"]valueForKey:@"publicname"]!=(NSString *)[NSNull null])
    {
      cell.offerNameLbl.text = [[[arrRewards objectAtIndex:indexPath.row]valueForKey:@"fishBowlPromotion"]valueForKey:@"publicname"];
    }

    
    NSNumber  * strPoints = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"loyaltyPoints"];
    NSString * points = [NSString stringWithFormat:@"%@ PTS",[strPoints stringValue]];
    
   
    
    
    cell.pointBankLbl.text = points;
    
    cell.pointImg.layer.cornerRadius = cell.pointImg.frame.size.width / 2;
    cell.pointImg.clipsToBounds = YES;
    
    cell.pointBankLbl.layer.cornerRadius = 8;
    cell.pointBankLbl.clipsToBounds = YES;
    
    [cell.reedemBtnLbl addTarget:self action:@selector(reedemBtnAction:) forControlEvents:UIControlEventTouchUpInside];
    cell.reedemBtnLbl.tag = indexPath.row;
    
    if([points integerValue] <= [earnedPoints integerValue])
    {
         cell.reedemBtnLbl.backgroundColor = [UIColor colorWithRed:232.0f/255 green:36.0f/255 blue:52.0f/255 alpha:1.0f];
         cell.lineImg.backgroundColor = [UIColor colorWithRed:250.0f/255 green:213.0f/255 blue:30.0f/255 alpha:1.0f];
         cell.reedemBtnLbl.userInteractionEnabled = YES;
    }
    else
    {
         cell.reedemBtnLbl.backgroundColor = [UIColor colorWithRed:138.0f/255 green:138.0f/255 blue:138.0f/255 alpha:1.0f];
         cell.lineImg.backgroundColor = [UIColor colorWithRed:138.0f/255 green:138.0f/255 blue:138.0f/255 alpha:1.0f];
         cell.reedemBtnLbl.userInteractionEnabled = NO;
    }

    if(indexPath.row == count-1)
    {
         cell.pointImg.hidden = NO;
    }
    else
    {
         cell.pointImg.hidden = YES;
    }
    return cell;
}


#pragma mark -  reedemBtn Action

-(void)reedemBtnAction:(UIButton *)sender
{
    // use offer Api
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSString * customerID = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];
    
   // NSString * memberID = [[arrRewards objectAtIndex:[sender tag]]valueForKey:@"memberId"];
   
     NSString * offerId = [[arrRewards objectAtIndex:[sender tag]]valueForKey:@"offerId"];
    
     NSString * claimPoints = [[arrRewards objectAtIndex:[sender tag]]valueForKey:@"loyaltyPoints"];
    
    NSMutableDictionary *dict = [NSMutableDictionary new];
    
    [dict setValue:customerID forKey:@"memberId"];
    [dict setValue:offerId forKey:@"offerId"];
    [dict setValue:claimPoints forKey:@"claimPoints"];
    [dict setValue:TanentID forKey:@"tenantId"];
    
    [apiCall useOffer:dict url:@"/loyalty/useOffer" withTarget:self withSelector:@selector(useOffer:)];
    
    dict = nil;
    apiCall = nil;
    
}

-(void)useOffer:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"loyality api response ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
         HtmlOffersAndRewards * ObjUrl = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
        ObjUrl.bodyType = @"MyPoint";
        ObjUrl.htmlBody = [response valueForKey:@"message"];
        [self.navigationController pushViewController:ObjUrl animated:YES];
    }
    else
    {
        [obj removeLoadingView:self.view];
      [self alertViewDelegate:@"No Rewards Available"];
    }
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


#pragma mark -  back Btn Action

- (IBAction)backBtn_Action:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark -  menuBtn Action

- (IBAction)menuBtn_Action:(id)sender{
    
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


#pragma mark - AlertView Action

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
}

#pragma mark - profileBtn Action

- (IBAction)profileBtn_Action:(id)sender {
    
    UserProfileView * menuObj = [[UserProfileView alloc]initWithNibName:@"UserProfileView" bundle:nil];
    [self.navigationController pushViewController:menuObj animated:YES];
}

#pragma mark - LoyaltyCard Action

- (IBAction)LoyaltyBtn_Action:(id)sender
{
    apiCall=[ApiClasses sharedManager];
    [obj addLoadingView:self.view];
    
    NSString * customerID = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];
    NSString * str = [NSString stringWithFormat:@"/mobile/getLoyaltyCard/%@",customerID];
    
    // get LoyaltyCardApi api
    
    [apiCall LoyaltyCardApi:nil url:str withTarget:self withSelector:@selector(ViewLoyalty:)];
    apiCall = nil;
}

// loyality card response
-(void)ViewLoyalty:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"ViewLoyalty response---------%@",response);
    
    clpsdk * obj22 = [[clpsdk alloc]init];
    [obj22 openPassbookAndShowwithData:response];
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



@end
