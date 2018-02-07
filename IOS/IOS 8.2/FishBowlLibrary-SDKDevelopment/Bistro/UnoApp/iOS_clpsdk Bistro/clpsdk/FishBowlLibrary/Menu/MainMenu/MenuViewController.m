//
//  MenuViewController.m
//  clpsdk
//
//  Created by surendra pathak on 16/08/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "MenuViewController.h"
#import "MenuTableViewCell.h"
#import "AFHTTPRequestOperationManager.h"
#import "UIImageView+AFNetworking.h"
#import "ApiClasses.h"
#import "ModelClass.h"
#import "clpsdk.h"
#import "SubMenuViewController.h"
#import "HomeViewController.h"
#import "BottomView.h"
#import "SideMenuObject.h"
#import "HomeViewController.h"
#import "LoyalityView.h"
#import "RewardsAndOfferView.h"
#import "StorelocatorViewController.h"
#import "UserProfileView.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "clpsdk.h"
#import "FAQViewViewController.h"
#import "ContactUSView.h"


@interface MenuViewController ()<PushNavigation>
{
    NSMutableArray   * leftCatArray;
    NSMutableArray   * subMenuItemArray;
    NSMutableArray   * countArray;
    ModelClass       * obj;
    SideMenuObject   * sideObject;
    UIViewController * myController;
    clpsdk           * clpObj;
    ApiClasses       * apiCall;
    NSMutableArray   * arrayMenuImage;
}

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView * imgBG;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView * imgBGTop;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView * imgLogo;
@property (unsafe_unretained, nonatomic) IBOutlet UITableView * tableView;
@property (weak, nonatomic) IBOutlet UIView                   * view2;
@property (weak, nonatomic) IBOutlet UIView                   * menuView;

@end

@implementation MenuViewController

- (void)viewDidLoad {
    [super viewDidLoad];
     obj=[ModelClass sharedManager];
     sideObject = [[SideMenuObject alloc]init];
  // obj.itemDictArray=nil;
     [obj.itemDictArray removeAllObjects];

    
//    [obj AddBottomView];
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSString * str1 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    NSURL *url = [NSURL URLWithString:str1];
    [self.imgLogo sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self.imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // header image
    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
    NSURL *url3 = [NSURL URLWithString:str3];
    [self.imgBGTop sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    
 
   // [self callMenuApi];
    
    [self callMenu];
    
    // Do any additional setup after loading the view from its nib.
    
    //[self shadoOffect:self.menuView];
    
    [self eventTrack];
}


-(void)eventTrack
{
    // clp mobile setting api
    clpObj = [clpsdk sharedInstanceWithAPIKey];
    
    // event tracking method
    NSMutableDictionary * eventDic = [NSMutableDictionary new];
    NSString * userName = [[NSUserDefaults standardUserDefaults]valueForKey:@"userName"];
    [eventDic setValue:@"MENU_CLICK" forKey:@"event_name"];
    [eventDic setValue:userName forKey:@"item_name"];
    [clpObj updateAppEvent:eventDic];
    eventDic = nil;
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



#pragma  mark - back button Action
- (IBAction)tapBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
//    NSInteger numberOfViewControllers = self.navigationController.viewControllers.count;
//    
//    for (int i=0; i<numberOfViewControllers; i++) {
//        
//        UIViewController *controller = (UIViewController*)[self.navigationController.viewControllers objectAtIndex:i];
//        
//        if ([controller isKindOfClass:[HomeViewController class]])
//        {
//            [self.navigationController popToViewController:controller animated:YES];
//        }
//        
//    }
    
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
     _tableView.hidden=NO;
//      objBottomView=[[BottomView alloc]init];
//     [objBottomView sharedBottomCustomView];
//    
//    objBottomView.frame = CGRectMake(0, self.view.frame.size.height-55, self.view.frame.size.width, 55);
}

#pragma  mark - Menu Api

-(void)callMenu
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strStoreID = [dic valueForKey:@"homeStoreID"];
    NSLog(@"Store id---------%@",strStoreID);
    NSString * str = [NSString stringWithFormat:@"/menu/category?storeId=%@",strStoreID];
    [apiCall menuApi:nil url:str withTarget:self withSelector:@selector(getMenuRespomse:)];
    apiCall = nil;
}


-(void)getMenuRespomse:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"menu response --------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        leftCatArray=[[NSMutableArray alloc]init];
        arrayMenuImage = [[NSMutableArray alloc]initWithObjects:[UIImage imageNamed:@"Large-Ham2.jpg"],[UIImage imageNamed:@"Ham_Classic1.jpg"], nil];
        leftCatArray=[response objectForKey:@"productCategoryList"];
        [self.tableView reloadData];
    }
    else
    {
        
    }
}


//-(void)callMenuApi
//{
//    
//   https://stg-hbh.fishbowlcloud.com/clpapi/menu/category?storeId=104
//    
//  [obj addLoadingView:self.view];
//    NSString *stringUrl=@"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menu/104";
//    NSLog(@" string url ====%@",stringUrl);
//    NSString * encoded = [stringUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
//    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
//    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
//    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
//    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
//    manager.requestSerializer =requestSerializer;
//    
//    [manager GET:encoded parameters:nil success:^(AFHTTPRequestOperation *operation, id jsonMenu)
//     {
//         NSLog(@"JSON:==%@",jsonMenu);
//         leftCatArray=[[NSMutableArray alloc]init];
//         NSLog(@"jsonMenu\n%@",jsonMenu);
//         leftCatArray=[jsonMenu objectForKey:@"categories"];
//         
//         // [self.tableViewMain reloadData];
//         _tableView.hidden=NO;
//         [self performSelectorOnMainThread:@selector(callMenuItemApi) withObject:nil waitUntilDone:YES];
//        
//         [self callMenuItemApi];
//     }
//         failure:^(AFHTTPRequestOperation *operation,NSError *error)
//     {
//          [obj removeLoadingView:self.view];
//         NSLog(@"error.description==%@",error.description);
//         
//         UIAlertController * alert=   [UIAlertController
//                                       alertControllerWithTitle:@"Failure"
//                                       message:[error localizedDescription]
//                                       preferredStyle:UIAlertControllerStyleAlert];
//         UIAlertAction* yesButton = [UIAlertAction
//                                     actionWithTitle:@"OK"
//                                     style:UIAlertActionStyleDefault
//                                     handler:^(UIAlertAction * action)
//                                     {
//                                         //Handel your yes please button action here
//                                         [alert dismissViewControllerAnimated:YES completion:nil];
//                                         
//                                     }];
//         [alert addAction:yesButton];
//         
//         [self presentViewController:alert animated:YES completion:nil];
//     }];
//}

#pragma  mark - Menu item Api

-(void)callMenuItem
{
//    [obj addLoadingView:self.view];
//    apiCall=[ApiClasses sharedManager];
//    
//    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
//    NSData *data = [def1 objectForKey:@"MyData"];
//    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
//    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
//    NSString * strStoreID = [dic valueForKey:@"homeStoreID"];
//    NSLog(@"Store id---------%@",strStoreID);
//    NSString * str = [NSString stringWithFormat:@"/menu/menuDrawer?storeId=%@&categoryId=%@",strStoreID];
//    [apiCall menuApi:nil url:str withTarget:self withSelector:@selector(getMenuRespomse:)];
//    apiCall = nil;
}


-(void)callMenuItemApi
{
//https://stg-hbh.fishbowlcloud.com/clpapi/menu/menuDrawer?storeId=647&categoryId=1
  
    NSString *stringUrl=@"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menuitems/104";
    NSLog(@" string url ====%@",stringUrl);
    NSString * encoded = [stringUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    manager.requestSerializer =requestSerializer;
    
    [manager GET:encoded parameters:nil success:^(AFHTTPRequestOperation *operation, id jsonMenuItems)
     {
          [obj removeLoadingView:self.view];
         NSLog(@"JSON:==%@",jsonMenuItems);
         subMenuItemArray=[[NSMutableArray alloc]init];
         countArray=[[NSMutableArray alloc]init];
         subMenuItemArray=[jsonMenuItems objectForKey:@"categories"];
         
         for (int i =0; i<leftCatArray.count; i++)
         {
             int count1=0;
             int leftCatId=[[[leftCatArray objectAtIndex:i]valueForKey:@"categoryid"] intValue];
             for (int j =0; j<subMenuItemArray.count; j++)
             {
                 int subMenuCatId=[[[subMenuItemArray objectAtIndex:j
                                     ]valueForKey:@"categoryID"]intValue];
                 if (leftCatId == subMenuCatId )
                 {
                     count1=count1+1;
                 }
             }
             [countArray addObject:[NSString stringWithFormat:@"%d",count1]];
         }
         
         NSLog(@"countArray==%@",countArray);
         [self.tableView reloadData];
         
         //         });
     }
         failure:^(AFHTTPRequestOperation *operation,NSError *error)
     {
         
         [obj removeLoadingView:self.view];
         
         UIAlertController * alert=   [UIAlertController
                                       alertControllerWithTitle:@"Failure"
                                       message:[error localizedDescription]
                                       preferredStyle:UIAlertControllerStyleAlert];
         
         UIAlertAction* yesButton = [UIAlertAction
                                     actionWithTitle:@"OK"
                                     style:UIAlertActionStyleDefault
                                     handler:^(UIAlertAction * action)
                                     {
                                         //[obj removeLoadingView:self.view];
                                         //Handel your yes please button action here
                                         [alert dismissViewControllerAnimated:YES completion:nil];
                                         
                                     }];
         [alert addAction:yesButton];
         
         [self presentViewController:alert animated:YES completion:nil];
     }];
}


#pragma  mark - table view datasource and delegate method


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 121;
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return leftCatArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    MenuTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MenuTableViewCell"];
    if (cell == nil)
    {
    NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"MenuTableViewCell" owner:self options:nil];
    cell = [array objectAtIndex:0];
    }
    
    cell.imgCellBG.image = [arrayMenuImage objectAtIndex:indexPath.row];
 
    if([[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"productCategoryImageUrl"]!=(NSString *)[NSNull null])
    {
    NSString *imgUrl=[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"productCategoryImageUrl"];
    NSString *string = [imgUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        NSURL *url = [NSURL URLWithString:string];
        NSLog(@"URL is %@",url);
    [cell.imgCellBG sd_setImageWithURL:url placeholderImage:nil];
    }
    
    cell.hidden = NO;
    cell.imgCellBG.layer.masksToBounds = NO;
    cell.imgCellBG.layer.shadowOffset = CGSizeMake(0,0);
    cell.imgCellBG.layer.shadowRadius = 5;
    cell.imgCellBG.layer.shadowOpacity = 0.5;
    

    cell.lblNameMenu.text=[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"productCategoryName"];
   // cell.lblCount.layer.cornerRadius = 15.0f;
    //[cell.lblCount setClipsToBounds:YES];
    
   // cell.lblCount.text=[countArray objectAtIndex:indexPath.row];
    return cell;
}

#pragma  mark - table view delegate


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    SubMenuViewController * SubMenuObj = [[SubMenuViewController alloc]initWithNibName:@"SubMenuViewController" bundle:nil];
    int strCatIdMain=[[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"productCategoryId"]intValue];
    SubMenuObj.mainCatId=strCatIdMain;
    
    SubMenuObj.subItemArray=subMenuItemArray;
    
    SubMenuObj.staticMenuImage = [arrayMenuImage objectAtIndex:indexPath.row];
    
   if([[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"productCategoryImageUrl"]!=(NSString *)[NSNull null])
    {
    SubMenuObj.imageUrlStr=[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"productCategoryImageUrl"];
    }
    
    SubMenuObj.mainMenuItemNamelStr=[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"productCategoryName"];
    
    // clp mobile setting api
    clpObj = [clpsdk sharedInstanceWithAPIKey];
    
    // event tracking method
    NSMutableDictionary * eventDic = [NSMutableDictionary new];
    [eventDic setValue:@"CATEGORY_CLICK" forKey:@"event_name"];
    [eventDic setValue:[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"category"] forKey:@"item_name"];
    [clpObj updateAppEvent:eventDic];
    eventDic = nil;
    
    SubMenuObj.categorydescMenu=[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"categorydesc"];
    
    SubMenuObj.categoryArray = leftCatArray;
    SubMenuObj.categoryIndexID = indexPath.row;
    
     [self.navigationController pushViewController:SubMenuObj animated:YES];
}


# pragma mark - sideMenu method

- (IBAction)sideMenu_Action:(id)sender
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
        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"MenuToStore"];
     [sideObject removeSideNave];
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


# pragma mark - alert method

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
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


// logout button Action
-(void)logOutBt_Action
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:@"Do you want to Logout."
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                                  FBSDKLoginManager *loginManager = [[FBSDKLoginManager alloc] init];
                                  [loginManager logOut];
                                  [obj RemoveBottomView];
                                  [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"changePassword"];
                                  
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
