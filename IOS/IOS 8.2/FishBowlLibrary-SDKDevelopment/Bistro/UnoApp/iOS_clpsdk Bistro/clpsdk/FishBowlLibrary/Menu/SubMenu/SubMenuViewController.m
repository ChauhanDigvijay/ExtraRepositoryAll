//
//  SubMenuViewController.m
//  clpsdk
//
//  Created by surendra pathak on 16/08/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "SubMenuViewController.h"
#import "ApiClasses.h"
#import "itemViewController.h"
#import "SubMenuCollectionViewCell.h"
#import "BottomView.h"
#import "ShowStoreViewController.h"
#import "SideMenuObject.h"
#import "MenuViewController.h"
#import "HomeViewController.h"
#import "UserProfileView.h"
#import "RewardsAndOfferView.h"
#import "ModelClass.h"
#import "LoyalityView.h"
#import "StorelocatorViewController.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "clpsdk.h"
#import "FAQViewViewController.h"
#import "ContactUSView.h"
#import "TableCell.h"
#import "SubMenuCell.h"
#import "MenuSubItemViewController.h"


@interface SubMenuViewController ()<PushNavigation,UITableViewDelegate,UITableViewDataSource,UICollectionViewDelegate,UICollectionViewDataSource>
{
    NSMutableArray              * selectedItemArray;
    BottomView                  * objBottomView;
    SideMenuObject              * sideObject;
    UIViewController            * myController;
    ModelClass                  * obj;
    NSInteger                     strCatIdMain;
    clpsdk                      * clpObj;
    ApiClasses                  * apiCall;
    NSArray                     * arraySubCategory;
    BOOL                          isTableExpand;
    NSIndexPath                 * selectIndex;
    NSInteger                     countArray;
    NSMutableArray              * arrayCollectionImage;
    NSMutableArray              * arrayDictionary;
    int                           k;
    CGFloat                       picDimension;
    NSIndexPath                 * indexPathRow;
    
}

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgBG;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgItem;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblNameItem;
@property (weak, nonatomic) IBOutlet UILabel *lblCatgoryDesc;
@property (weak, nonatomic) IBOutlet UIView *subMenuView;
@property (weak, nonatomic) IBOutlet UITableView *tblCategory;
@property (assign)BOOL isOpen;
@property (nonatomic,strong)NSIndexPath *selectIndex;

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgTop;
@end

@implementation SubMenuViewController
@synthesize subItemArray,mainCatId,imageUrlStr,mainMenuItemNamelStr,isOpen,selectIndex;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    obj=[ModelClass sharedManager];
    sideObject = [[SideMenuObject alloc]init];
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    strCatIdMain = self.categoryIndexID;
    
//    NSString * str1 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
//    NSURL *url = [NSURL URLWithString:str1];
//    [self.imgLogo sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self.imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // header image
    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
    NSURL *url3 = [NSURL URLWithString:str3];
    [self.imgTop sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    // custom collection view class
    
    [self.collectionView registerNib:[UINib nibWithNibName:@"SubMenuCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"SubMenuCollectionViewCell"];
    self.collectionView.backgroundColor = [UIColor clearColor];
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenWidth = screenRect.size.width;
    CGFloat screenHeight = screenRect.size.height;
    
    if(screenWidth == 320 && screenHeight == 480)
    {
        [flowLayout setItemSize:CGSizeMake(130, 130)];
    }
    else if (screenWidth == 320 && screenHeight == 568)
    {
        [flowLayout setItemSize:CGSizeMake(100, 100)];
    }
    else if (screenWidth == 375 && screenHeight == 667)
    {
        [flowLayout setItemSize:CGSizeMake(100, 100)];
    }
    else if (screenWidth == 414 && screenHeight == 736)
    {
        [flowLayout setItemSize:CGSizeMake(100, 100)];
    }
    
      [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];

    
    arrayDictionary = [NSMutableArray new];
    
    
    [self.collectionView setCollectionViewLayout:flowLayout];
    [self.collectionView setAllowsSelection:YES];
  
    selectedItemArray=[[NSMutableArray alloc]init];
    arrayCollectionImage = [NSMutableArray new];
    
    k = 0;
   // categories menu
    _lblNameItem.text=mainMenuItemNamelStr;
    _lblCatgoryDesc.text=_categorydescMenu;
   
    NSString *string = [imageUrlStr stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    NSURL *url = [NSURL URLWithString:string];
    
    [_imgItem sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    self.imgItem.image = self.staticMenuImage;
    
   // [self subMenuApi];
 
    [self subMenuCategory];
}


-(void)subMenuCategory
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
    NSString * str = [NSString stringWithFormat:@"/menu/subCategory?storeId=%@&categoryId=%d",strStoreID,self.mainCatId];
    [apiCall SubCategory:nil url:str withTarget:self withSelector:@selector(getSubCategory:)];
    apiCall = nil;
}


-(void)getSubCategory:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"SubCategory response --------- %@",response);
    
    if ([[response valueForKey:@"successFlag"]integerValue]==1)
    {
        arraySubCategory = [NSMutableArray new];
        arraySubCategory = [response valueForKey:@"productSubCategoryList"];
        [self.tblCategory reloadData];
        
//       for(int i =0 ; i< arraySubCategory.count ; i++)
//        {
//            [selectedItemArray addObject:@"0"];
//            [arrayDictionary addObject:@"0"];
//            
//        }
//        int subCategoryID = [[[arraySubCategory objectAtIndex:0]valueForKey:@"productSubCategoryId"]intValue];
//        [self subMenuApi:subCategoryID];
    }
    
}


# pragma mark - subMenu Api

-(void)subMenuApi:(int)strID
{
  //  /menu/menuDrawer?storeId=647&familyId=2&categoryId=2&subCategoryId=2
  //[obj addLoadingView:self.view];
    
        apiCall=[ApiClasses sharedManager];
    
        NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
        NSData *data = [def1 objectForKey:@"MyData"];
        NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
        NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
        NSString * strStoreID = [dic valueForKey:@"homeStoreID"];
        NSLog(@"Store id---------%@",strStoreID);
        NSString * str = [NSString stringWithFormat:@"/menu/menuDrawer?storeId=%@&categoryId=%d&subCategoryId=%d",strStoreID,self.mainCatId,strID];
    
        [apiCall ProductList:nil url:str withTarget:self withSelector:@selector(getProductList:)];
        apiCall = nil;
}


-(void)getProductList:(id)response
{
    //NSLog(@"menu response --------- %@",response);
    [obj removeLoadingView:self.view];

  if ([[response valueForKey:@"successFlag"]integerValue]==1)
    {
    NSLog(@"k response --------- %d",k);
        
        
    [selectedItemArray replaceObjectAtIndex:k withObject:[response valueForKey:@"productList"]];
        
        k++;
        
        if(arraySubCategory.count-1 >= k)
        {
            [obj addLoadingView:self.view];
            NSLog(@"k response --------- %d",k);
            int subCategoryID = [[[arraySubCategory objectAtIndex:k]valueForKey:@"productSubCategoryId"]intValue];
            [self subMenuApi:subCategoryID];
        }
        
//        if (indexPathRow.row == 0)
//        {
//            if ([indexPathRow isEqual:self.selectIndex])
//            {
//                self.isOpen = NO;
//                [self didSelectCellRowFirstDo:NO nextDo:NO];
//                self.selectIndex = nil;
//            }
//            else
//            {
//                arrayCollectionImage = [NSMutableArray new];
//                [arrayCollectionImage addObjectsFromArray:[selectedItemArray objectAtIndex:indexPathRow.section]];
//                NSLog(@"arrayCollectionImage ============ %@",arrayCollectionImage);
//                
//                if (!self.selectIndex)
//                {
//                    self.selectIndex = indexPathRow;
//                    [self didSelectCellRowFirstDo:YES nextDo:NO];
//                }
//                else
//                {
//                    [self didSelectCellRowFirstDo:NO nextDo:YES];
//                }
//            }
//        }
//        [self.tblCategory deselectRowAtIndexPath:indexPathRow animated:YES];
    }
    NSLog(@"menu selectedItemArray response --------- %@",selectedItemArray);
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


// viewwill appear
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
}


- (IBAction)tapBack:(id)sender
{
[self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - TableView DataSource And Delegate methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
//    if (self.isOpen && self.selectIndex.section == indexPath.section && indexPath.row!=0)
//    {
//        if(arrayCollectionImage.count%2 == 0)
//        {
//            return 100 * arrayCollectionImage.count/2+5;
//        }
//        else
//        {
//           return 100 * arrayCollectionImage.count/2+80;
//        }
//    }
//    else
//    {
           return 35;
    //}
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
//        if (self.isOpen)
//        {
//            if (self.selectIndex.section == section)
//            {
//                return 2;
//            }
//        }
    return [arraySubCategory count];
}



-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
//    if (self.isOpen && self.selectIndex.section == indexPath.section&&indexPath.row!=0)
//    {
//        static NSString *CellIdentifier = @"SubMenuCell";
//        SubMenuCell *cell = (SubMenuCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
//        if (cell == nil)
//        {
//            cell = [[[NSBundle mainBundle] loadNibNamed:CellIdentifier owner:self options:nil] objectAtIndex:0];
//        }
//        cell.selectionStyle = UITableViewCellSelectionStyleNone;
//        cell.collectionview.delegate   = self;
//        cell.collectionview.dataSource = self;
//        return cell;
//    }
//    else
//    {
        static NSString *CellIdentifier = @"TableCell";
        TableCell *cell = (TableCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        if (cell == nil)
        {
            cell = [[[NSBundle mainBundle] loadNibNamed:CellIdentifier owner:self options:nil] objectAtIndex:0];
        }
        cell.categoryName.text = [[arraySubCategory objectAtIndex:indexPath.row]valueForKey:@"productSubCategoryName"];
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.categoryName.layer.cornerRadius = 2;
        cell.categoryName.layer.masksToBounds = YES;
        cell.categoryName.layer.borderWidth = 1.0;
        cell.categoryName.layer.borderColor = [UIColor lightGrayColor].CGColor;
        return cell;
    //}
   
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    MenuSubItemViewController * SubMenuObj = [[MenuSubItemViewController alloc]initWithNibName:@"MenuSubItemViewController" bundle:nil];
    int subCategoryID = [[[arraySubCategory objectAtIndex:indexPath.row]valueForKey:@"productSubCategoryId"]intValue];
    SubMenuObj.mainCatIdItem=mainCatId;
    SubMenuObj.subCatID=subCategoryID;
    SubMenuObj.strItemNAme=[[arraySubCategory objectAtIndex:indexPath.row]valueForKey:@"productSubCategoryName"];
    [self.navigationController pushViewController:SubMenuObj animated:YES];

//    if (indexPath.row == 0)
//    {
//        if ([indexPath isEqual:self.selectIndex])
//        {
//            self.isOpen = NO;
//            [self didSelectCellRowFirstDo:NO nextDo:NO];
//            self.selectIndex = nil;
//        }
//        else
//        {
//            arrayCollectionImage = [NSMutableArray new];
//            [arrayCollectionImage addObjectsFromArray:[selectedItemArray objectAtIndex:indexPath.section]];
//            NSLog(@"arrayCollectionImage ============ %@",arrayCollectionImage);
//        
//            if (!self.selectIndex)
//                {
//                self.selectIndex = indexPath;
//                [self didSelectCellRowFirstDo:YES nextDo:NO];
//                }
//                else
//                {
//                [self didSelectCellRowFirstDo:NO nextDo:YES];
//                }
//            }
//        }
//     [tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)didSelectCellRowFirstDo:(BOOL)firstDoInsert nextDo:(BOOL)nextDoInsert
{
    
    self.isOpen = firstDoInsert;
    
    [self.tblCategory beginUpdates];
    
    NSInteger section           = self.selectIndex.section;
    
    NSMutableArray* rowToInsert = [[NSMutableArray alloc] init];

    NSIndexPath* indexPathToInsert = [NSIndexPath indexPathForRow:1 inSection:section];
    [rowToInsert addObject:indexPathToInsert];
    
    if (firstDoInsert)
    {
        [self.tblCategory insertRowsAtIndexPaths:rowToInsert withRowAnimation:UITableViewRowAnimationTop];
    }
    else
    {
        NSIndexPath* indexPathToInsert = [NSIndexPath indexPathForRow:1 inSection:section];
        [rowToInsert addObject:indexPathToInsert];
        
        [self.tblCategory deleteRowsAtIndexPaths:rowToInsert withRowAnimation:UITableViewRowAnimationTop];
    }
  
    [self.tblCategory endUpdates];
    
    if (nextDoInsert)
    {
        self.isOpen = YES;
        self.selectIndex = [self.tblCategory indexPathForSelectedRow];
        [self didSelectCellRowFirstDo:YES nextDo:NO];
    }
    
    if (self.isOpen)
    {
        [self.tblCategory scrollToNearestSelectedRowAtScrollPosition:UITableViewScrollPositionTop animated:YES];
    }
    else
    {
        
    }
}

#pragma mark - CollectionView methods

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
     return arrayCollectionImage.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    SubMenuCollectionViewCell *cell=[collectionView dequeueReusableCellWithReuseIdentifier:@"SubMenuCollectionViewCell" forIndexPath:indexPath];
    
    
    if([[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productImageUrl"]!=(NSString*)[NSNull null])
    {
    NSString *imgUrl=[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productImageUrl"];
   // NSString *string = [imgUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
         NSURL *url = [NSURL URLWithString:imgUrl];
        [cell.imgSubMenuItem sd_setImageWithURL:url placeholderImage:nil];
    }
    
    cell.hidden = NO;
 
    cell.layer.masksToBounds = NO;
    cell.layer.shadowOffset = CGSizeMake(0, 0);
    cell.layer.shadowRadius = 4;
    cell.layer.shadowOpacity = 0.5;
  
//    self.collectionView.layer.masksToBounds = NO;
//    self.collectionView.layer.shadowOffset = CGSizeMake(0, 0);
//    self.collectionView.layer.shadowRadius = 2;
//    self.collectionView.layer.shadowOpacity = 0.5;
    
      cell.contentView.layer.cornerRadius = 2.0f;
      cell.contentView.layer.borderWidth = 2.0f;
      cell.contentView.layer.borderColor = [UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:0.3].CGColor;
      cell.contentView.layer.masksToBounds = YES;

    
    
    //cell.imgSubMenuItem.image=[UIImage imageNamed:[offerImageArray objectAtIndex:indexPath.row]];
    
    
//    NSString * strCost = [NSString stringWithFormat:@"$ %@",[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"icost"]];
//    
//    cell.lblPriceSubItem.text=strCost;
    
    cell.lblNameSubItem.text=[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productName"];
    
    return cell;
}


- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
 
    SubMenuCollectionViewCell *cell = (SubMenuCollectionViewCell *)[collectionView cellForItemAtIndexPath:indexPath];
    // Set the index once user taps on a cell
    
    // Set the selection here so that selection of cell is shown to ur user immediately
    cell.layer.borderWidth = .5f;
    cell.layer.borderColor = [[UIColor grayColor] CGColor];
    itemViewController * itemViewObj = [[itemViewController alloc]initWithNibName:@"itemViewController" bundle:nil];
    
    
    if([[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productImageUrl"]!=(NSString *)[NSNull null])
    {
        itemViewObj.selectedItemImageUrl=[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productImageUrl"];
    }
    
    if([[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"productName"]!=(NSString *)[NSNull null])
    {
            itemViewObj.selectedItemName=[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productName"];
    }

    
    // clp mobile setting api
    clpObj = [clpsdk sharedInstanceWithAPIKey];
    
    // event tracking method
    NSMutableDictionary * eventDic = [NSMutableDictionary new];
    [eventDic setValue:@"SUB_CATEGORY_CLICK" forKey:@"event_name"];
    [eventDic setValue:[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productName"] forKey:@"item_name"]; // item name
    [clpObj updateAppEvent:eventDic];
    eventDic = nil;
  
    //itemViewObj.selectedItemICost=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"icost"];
    
    
    if([[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productLongDescription"]!=(NSString *)[NSNull null])
    {
      itemViewObj.selectedItemDesc=[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productLongDescription"];
    }
   
   // itemViewObj.selectedItemId=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"itemid"];
    
    itemViewObj.selectedmainMenuNameItem =  [[self.categoryArray objectAtIndex:strCatIdMain]valueForKey:@"productCategoryName"];
    
    if(subItemArray.count>0)
    {
    itemViewObj.subCategoryArray = selectedItemArray;
    }
    
    itemViewObj.itemActegoryID = indexPath.row;
    
    [self.navigationController pushViewController:itemViewObj animated:YES];
}


- (void)collectionView:(UICollectionView *)collectionView didDeselectItemAtIndexPath:(NSIndexPath *)indexPath
{
    SubMenuCollectionViewCell *cell = (SubMenuCollectionViewCell *)[collectionView cellForItemAtIndexPath:indexPath];
    // Set the index once user taps on a cell
    
    // Set the selection here so that selection of cell is shown to ur user immediately
    cell.layer.borderWidth = 0.0;
    cell.layer.borderColor = [[UIColor clearColor] CGColor];
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


# pragma mark - Gesture Swipe Right

- (IBAction)gestureRight:(id)sender
{
    
    NSLog(@"card id --------- %ld",(long)strCatIdMain);
    
    if(strCatIdMain-1 >=0)
    {
    NSString *string = [[[self.categoryArray objectAtIndex:--strCatIdMain]valueForKey:@"imageurl"] stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    
    NSURL *url = [NSURL URLWithString:string];
    [_imgItem sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    _lblNameItem.text=[[self.categoryArray objectAtIndex:strCatIdMain]valueForKey:@"category"];
    
    _lblCatgoryDesc.text=[[self.categoryArray objectAtIndex:strCatIdMain]valueForKey:@"categorydesc"];
        
        // clp mobile setting api
        clpObj = [clpsdk sharedInstanceWithAPIKey];
        
        // event tracking method
        NSMutableDictionary * eventDic = [NSMutableDictionary new];
        [eventDic setValue:@"CATEGORY_CLICK" forKey:@"event_name"];
        [eventDic setValue:_lblNameItem.text forKey:@"item_name"];
        [clpObj updateAppEvent:eventDic];
        eventDic = nil;
        
    
    NSString * strCatID = [[self.categoryArray objectAtIndex:strCatIdMain]valueForKey:@"categoryid"];
    
        if(selectedItemArray.count>0)
        {
            [selectedItemArray removeAllObjects];
        }

        if(subItemArray.count>0)
        {
    for (int i=0; i<subItemArray.count; i++)
    {
        int subMenuCatId=[[[subItemArray objectAtIndex:i
                            ]valueForKey:@"categoryID"]intValue];
        if ([strCatID intValue] == subMenuCatId)
        {
            [selectedItemArray addObject:[subItemArray objectAtIndex:i]];
        }
    }
    [self.collectionView reloadData];
    }
  }
}


# pragma mark - Gesture Swipe Left

- (IBAction)gestureLeft:(id)sender
{
    NSLog(@"card id --------- %ld",(long)strCatIdMain);
    

    if(self.categoryArray.count > strCatIdMain+1)
    {
    NSString *string = [[[self.categoryArray objectAtIndex:++strCatIdMain]valueForKey:@"imageurl"]
                        stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    
    NSURL *url = [NSURL URLWithString:string];
    
    [_imgItem sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    _lblNameItem.text=[[self.categoryArray objectAtIndex:strCatIdMain]valueForKey:@"category"];
_lblCatgoryDesc.text=[[self.categoryArray objectAtIndex:strCatIdMain]valueForKey:@"categorydesc"];
  
        // clp mobile setting api
        clpObj = [clpsdk sharedInstanceWithAPIKey];
        
        // event tracking method
        NSMutableDictionary * eventDic = [NSMutableDictionary new];
        [eventDic setValue:@"CATEGORY_CLICK" forKey:@"event_name"];
        [eventDic setValue:_lblNameItem.text forKey:@"item_name"];
        [clpObj updateAppEvent:eventDic];
        eventDic = nil;
        
    NSString * strCatID = [[self.categoryArray objectAtIndex:strCatIdMain]valueForKey:@"categoryid"];
    
        if(selectedItemArray.count>0)
        {
            [selectedItemArray removeAllObjects];
        }
  
        if(subItemArray.count>0)
        {
    for (int i=0; i<subItemArray.count; i++)
    {
        int subMenuCatId=[[[subItemArray objectAtIndex:i
                            ]valueForKey:@"categoryID"]intValue];
        
        if ([strCatID intValue] == subMenuCatId)
        {
            [selectedItemArray addObject:[subItemArray objectAtIndex:i]];
        }
    }
    [self.collectionView reloadData];
    }
  }
    
}

@end
