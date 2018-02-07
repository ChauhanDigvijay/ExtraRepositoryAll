//
//  itemViewController.m
//  pageView
//
//  Created by Gourav Shukla on 16/08/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "itemViewController.h"
#import "ApiClasses.h"
#import "OrderPageViewController.h"
#import "ModelClass.h"
#import "ShowStoreViewController.h"
#import "SideMenuObject.h"
#import "MenuViewController.h"
#import "HomeViewController.h"
#import "UserProfileView.h"
#import "RewardsAndOfferView.h"
#import "LoyalityView.h"
#import "StorelocatorViewController.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "clpsdk.h"
#import "FAQViewViewController.h"
#import "ContactUSView.h"
#import "ApiClasses.h"



#define ACCEPTABLE_CHARECTERS @"0123456789"
@interface itemViewController ()<UITextFieldDelegate,UIPickerViewDelegate,editDelegate,PushNavigation>
{
    NSString                    * strFinalCost;
    NSArray                     * sizeArray;
    NSArray                     * sizeA;
    ModelClass                  * obj;
    SideMenuObject              * sideObject;
    UIViewController            * myController;
    clpsdk                      * clpObj;
    ApiClasses                  * apiCall;
}
@property (weak, nonatomic) IBOutlet UIImageView              *imgBG;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgTop;
@property (weak, nonatomic) IBOutlet UILabel                  *lblQuantity;
@property (weak, nonatomic) IBOutlet UILabel                  *lblItemPrice;

@property (weak, nonatomic) IBOutlet UILabel                  *lblShowSubMenuItemName;
@property (weak, nonatomic) IBOutlet UIButton                 *regulerCrustOutlet;
@property (weak, nonatomic) IBOutlet UIButton                 *wheatCrustOutlet;
@property (weak, nonatomic) IBOutlet UIButton                 *largeCrustOutlet;
@property (weak, nonatomic) IBOutlet UIButton                 *wholeLGOutlet;
@property (weak, nonatomic) IBOutlet UIButton                 *firstHalfOutlet;
@property (weak, nonatomic) IBOutlet UIButton                 *secondHalfOutlet;
@end

@implementation itemViewController
@synthesize selectedItemImageUrl,selectedIndex,selectedItemName,selectedItemICost,selectedItemDesc;


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidLoad
{
    
    [super viewDidLoad];
    obj=[ModelClass sharedManager];
    sideObject = [[SideMenuObject alloc]init];
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    //    NSString * str1 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    //    NSURL *url = [NSURL URLWithString:str1];
    //    [self.imgLogo sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // background image
//    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
//    NSURL *url2 = [NSURL URLWithString:str2];
//    [self.imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // header image
    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
    NSURL *url3 = [NSURL URLWithString:str3];
    [self.imgTop sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self.imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    [self ProductDetailApi];
    
}


-(void)ProductDetailApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    ///menu/subCategory?storeId=647&familyId=2&categoryId=2
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strStoreID = [dic valueForKey:@"homeStoreID"];
    NSLog(@"Store id---------%@",strStoreID);
    
    NSString * str = [NSString stringWithFormat:@"/menu/product?storeId=%@&categoryId=%ld&subCategoryId=%ld&productId=%ld",strStoreID,(long)self.categoryID,(long)self.subProductID,(long)self.productID];
    
    [apiCall ProductAttributes:nil url:str withTarget:self withSelector:@selector(getProductDetailApi:)];
    
    apiCall = nil;
}


-(void)getProductDetailApi:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"SubCategory response --------- %@",response);
    
    if ([[response valueForKey:@"successFlag"]integerValue]==1)
    {
 
        selectedItemICost = [[[response valueForKey:@"productDetails"]valueForKey:@"productPrice"]stringValue];
        _lblItemPrice.text=selectedItemICost;
        
    }
}


- (IBAction)addToOrder:(id)sender
{
    
    NSLog(@"addToOrder Button Action");
    
    int quantity3=[_lblQuantity.text intValue];
    
    if(quantity3 != 0)
    
    {
    OrderPageViewController * orderPageObj = [[OrderPageViewController alloc]initWithNibName:@"OrderPageViewController" bundle:nil];
    
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"isFromrderComplete"];
    orderPageObj.delegate = self;
    orderPageObj.isEditDone = isFromOrderEdit;
        
     NSLog(@"strFinalCost==%@",strFinalCost);
        
    if (selectedItemName.length!=0 && isFromOrderEdit == NO)
    {
        NSMutableDictionary *itemDict = [[NSMutableDictionary alloc]init];
        [itemDict setValue:selectedItemName forKey:@"iname"];
        [itemDict setValue:selectedItemICost forKey:@"icost"];
        
        int quantity2=[_lblQuantity.text intValue];
        NSLog(@"quantity2==%d",quantity2);
        NSLog(@"selected item cost ------ %@",selectedItemICost);
        
        if(selectedItemICost.length!=0 && selectedItemICost !=(NSString *)[NSNull null])
        {
            NSString *myValu=[NSString stringWithString:selectedItemICost];
            float  intCost1=[myValu floatValue];
            float totalCost1=intCost1*quantity2;
            strFinalCost=[NSString stringWithFormat:@"%.02f",totalCost1];
            orderPageObj.selectedItemTotalPrice=strFinalCost;
             [itemDict setValue:strFinalCost forKey:@"iPrice"];
        }
        
        [itemDict setValue:selectedItemDesc forKey:@"idesc"];
        [itemDict setValue:_lblQuantity.text forKey:@"iQuantity"];
       
        [itemDict setValue:_selectedItemId forKey:@"itemid"];
        [itemDict setValue:@"medium" forKey:@"iSize"];
        
        NSLog(@"obj.itemDictArray==%@",obj.itemDictArray);

        [obj.itemDictArray addObject:itemDict];
          NSLog(@"itemDict==%@",itemDict);
        NSLog(@"obj.itemDictArray==%@",obj.itemDictArray);
    }
    
    if (self.isFromMenu==YES)
    {
        self.isFromMenu=NO;
    }
    else
    {
        if(isFromOrderEdit == YES)
        {
             NSDictionary *DictEdit = [obj.itemDictArray objectAtIndex:obj.indexGlobal];
            [DictEdit setValue:_lblQuantity.text forKey:@"iQuantity"];
            [DictEdit setValue:@"medium" forKey:@"iSize"];
            
            int quantity2=[_lblQuantity.text intValue];
            NSLog(@"quantity2==%d",quantity2);
            NSLog(@"selected item cost ------ %@",selectedItemICost);

            NSString *myValu=[NSString stringWithString:selectedItemICost];
            float  intCost1=[myValu floatValue];
            float totalCost1=intCost1*quantity2;
            strFinalCost=[NSString stringWithFormat:@"%.02f",totalCost1];
            orderPageObj.selectedItemTotalPrice=strFinalCost;
            
             [DictEdit setValue:strFinalCost forKey:@"iPrice"];
            NSLog(@"obj.itemDictArray==%@",obj.itemDictArray);
            [obj.itemDictArray replaceObjectAtIndex:obj.indexGlobal withObject:DictEdit];
        }
    }
    [self.navigationController pushViewController:orderPageObj animated:YES];
    }
    else
    {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert!"
                                      message:@"Quantity may not be 0"
                                      preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                                  {
   
                                  }];
        
        [alert addAction:cancel1];
        
        [self presentViewController:alert animated:YES completion:nil];
    }
    NSLog(@"itemArray==%@",obj.itemDictArray);
}

-(void)ProductEdited;
{
    isFromOrderEdit = YES;
}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
  
    
    sizeArray=[NSArray arrayWithObjects:@"SMALL",@"MEDIUM",@"LARGE",@"EXTRA LARGE",nil];
    sizeA=[NSArray arrayWithObjects:@"10",@"12",@"14",@"18",nil];
    
    NSString *string = [selectedItemImageUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    NSURL *url = [NSURL URLWithString:string];
    
   [_viewitemImage sd_setImageWithURL:url placeholderImage:nil];
    
    
    if(selectedItemName.length!=0)
    {
       _viewItemName.text=selectedItemName;
    }
    
    if(selectedItemDesc.length!=0)
    {
       _viewItemDesc.text=selectedItemDesc;
    }
    
    if(selectedItemName.length!=0)
    {
      _lblShowSubMenuItemName.text=selectedItemName;
    }
  
    _lblItemPrice.text=selectedItemICost;
    _lblQuantity.layer.borderWidth=1;
    _lblQuantity.layer.borderColor=[UIColor lightGrayColor].CGColor;
    
    //NSLog(@"view item name ------ %@",_viewItemName.text);
}



- (IBAction)regulerCrustClicked:(id)sender
{
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_wheatCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_largeCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
}
- (IBAction)wheatCrustClicked:(id)sender
{
    
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_wheatCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_largeCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    
}
- (IBAction)largeCrustClicked:(id)sender
{
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_wheatCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_largeCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
}
- (IBAction)wholeLGClicked:(id)sender
{
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_firstHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_secondHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
}
- (IBAction)firstHalfClicked:(id)sender
{
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_firstHalfOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_secondHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
}
- (IBAction)secondHalfClicked:(id)sender
{
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_firstHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_secondHalfOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
}



# pragma mark - back button action


- (IBAction)tapBack:(id)sender
{
[self.navigationController popViewControllerAnimated:true];
}

# pragma mark - Quantity decrement method


- (IBAction)tapDecrementQT:(UIButton *)sender
{
    
    int quantity=[_lblQuantity.text intValue];
    if (quantity<=1) {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert!"
                                      message:@"Quantity may not be less then 0"
                                      preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                                  {
                                      
                                  }];
        
        [alert addAction:cancel1];
        
        [self presentViewController:alert animated:YES completion:nil];

    }
    else
    {
    quantity-=1;
    _lblQuantity.text=[NSString stringWithFormat:@"%d",quantity];
    }
}

# pragma mark - Quantity increment method

- (IBAction)tapIncrementQT:(UIButton *)sender
{
int quantity=[_lblQuantity.text intValue];
    if (quantity>=10) {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert!"
                                      message:@"Quantity may not be greater then 10"
                                      preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                                  {
                                      
                                  }];
        
        [alert addAction:cancel1];
        
        [self presentViewController:alert animated:YES completion:nil];
        
    }
    else
    {
    quantity+=1;
    _lblQuantity.text=[NSString stringWithFormat:@"%d",quantity];
    }
    
}


# pragma mark - sideMenu method

- (IBAction)menuBtn_Action:(id)sender
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
    else if(indexPath.row == 6)
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
    else if(indexPath.row == 7)
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
    else if(indexPath.row == 8)
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


# pragma mark - right Swipe method

- (IBAction)rightSwipeGesture:(id)sender
{
    if(self.itemActegoryID-1 >=0)
    {
    
    NSString *string = [[[self.subCategoryArray objectAtIndex:--self.itemActegoryID]valueForKey:@"imageurl"] stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    NSURL *url = [NSURL URLWithString:string];
    [_viewitemImage sd_setImageWithURL:url placeholderImage:nil];
    
    _lblShowSubMenuItemName.text = [[self.subCategoryArray objectAtIndex:self.itemActegoryID]valueForKey:@"iname"];
    selectedItemName = _lblShowSubMenuItemName.text;
        
        // clp mobile setting api
        clpObj = [clpsdk sharedInstanceWithAPIKey];
        
        // event tracking method
        NSMutableDictionary * eventDic = [NSMutableDictionary new];
        [eventDic setValue:@"SUB_CATEGORY_CLICK" forKey:@"event_name"];
        [eventDic setValue:_lblShowSubMenuItemName.text forKey:@"item_name"]; // item name
        [clpObj updateAppEvent:eventDic];
        eventDic = nil;
        
        
       _viewItemDesc.text = [[self.subCategoryArray objectAtIndex:self.itemActegoryID]valueForKey:@"idesc"];
        selectedItemDesc = _viewItemDesc.text;
        
    _selectedItemId=[[self.subCategoryArray objectAtIndex:self.itemActegoryID]valueForKey:@"itemid"];
    
    _lblItemPrice.text=[[self.subCategoryArray objectAtIndex:self.itemActegoryID]valueForKey:@"icost"];
    selectedItemICost = _lblItemPrice.text;
        
    }
    
}

# pragma mark - right left method

- (IBAction)leftSwipeGesture:(id)sender
{
    
    if(self.subCategoryArray.count > self.itemActegoryID+1)
    {
    
    NSString *string = [[[self.subCategoryArray objectAtIndex:++self.itemActegoryID]valueForKey:@"imageurl"] stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    NSURL *url = [NSURL URLWithString:string];
    [_viewitemImage sd_setImageWithURL:url placeholderImage:nil];
    
    _lblShowSubMenuItemName.text = [[self.subCategoryArray objectAtIndex:self.itemActegoryID]valueForKey:@"iname"];
        selectedItemName = _lblShowSubMenuItemName.text;
        
        // clp mobile setting api
        clpObj = [clpsdk sharedInstanceWithAPIKey];
        
        // event tracking method
        NSMutableDictionary * eventDic = [NSMutableDictionary new];
        [eventDic setValue:@"SUB_CATEGORY_CLICK" forKey:@"event_name"];
        [eventDic setValue:_lblShowSubMenuItemName.text forKey:@"item_name"]; // item name
        [clpObj updateAppEvent:eventDic];
        eventDic = nil;
        
     _viewItemDesc.text = [[self.subCategoryArray objectAtIndex:self.itemActegoryID]valueForKey:@"idesc"];
      selectedItemDesc = _viewItemDesc.text;
        
    _selectedItemId=[[self.subCategoryArray objectAtIndex:self.itemActegoryID]valueForKey:@"itemid"];
    
    _lblItemPrice.text=[[self.subCategoryArray objectAtIndex:self.itemActegoryID]valueForKey:@"icost"];
    selectedItemICost = _lblItemPrice.text;
    }
}


@end
