//
//  MenuSubItemViewController.m
//  clpsdk
//
//  Created by surendra pathak on 12/01/17.
//  Copyright © 2017 clyptech. All rights reserved.
//

#import "MenuSubItemViewController.h"
#import "ShowStoreViewController.h"
#import "ModelClass.h"
#import "StorelocatorViewController.h"
#import "clpsdk.h"
#import "ApiClasses.h"
@interface MenuSubItemViewController ()
{
    NSMutableArray              * arrayCollectionImage;
    NSMutableArray              * selectedItemArray;
    clpsdk                      * clpObj;
    ApiClasses                  * apiCall;
    ModelClass                  * obj;
    NSInteger                     strCatIdMain;
}
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblItemName;
@property (unsafe_unretained, nonatomic) IBOutlet UICollectionView *collectionViewItem;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgBG;
@property (weak, nonatomic) IBOutlet UILabel *titleName;

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgTop;

@end

@implementation MenuSubItemViewController
@synthesize subCatID,subItemArray,mainCatIdItem,strItemNAme;;
- (void)viewDidLoad {
    [super viewDidLoad];
    arrayCollectionImage = [NSMutableArray new];
    selectedItemArray=[[NSMutableArray alloc]init];
   // _lblItemName.text = strItemNAme;
    _titleName.text = strItemNAme;
    obj=[ModelClass sharedManager];
 
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];

    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self.imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // header image
    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
    NSURL *url3 = [NSURL URLWithString:str3];
    [self.imgTop sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    // custom collection view class
    
    
    [self.collectionViewItem registerNib:[UINib nibWithNibName:@"SubMenuCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"SubMenuCollectionViewCell"];
    self.collectionViewItem.backgroundColor = [UIColor clearColor];
    
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
        [flowLayout setItemSize:CGSizeMake(130, 130)];
    }
    else if (screenWidth == 375 && screenHeight == 667)
    {
        [flowLayout setItemSize:CGSizeMake(130, 130)];
    }
    else if (screenWidth == 414 && screenHeight == 736)
    {
        [flowLayout setItemSize:CGSizeMake(130, 130)];
    }
    
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    [self.collectionViewItem setCollectionViewLayout:flowLayout];
    [self.collectionViewItem setAllowsSelection:YES];
   NSLog(@"subCatID---------%d",subCatID);
    [obj addLoadingView:self.view];
       [self subMenuApi:subCatID];
    // Do any additional setup after loading the view from its nib.
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
    NSString * str = [NSString stringWithFormat:@"/menu/menuDrawer?storeId=%@&categoryId=%d&subCategoryId=%d",strStoreID,self.mainCatIdItem,strID];
    
    
    NSLog(@"product detail ------------ %@",str);
    
    [apiCall ProductList:nil url:str withTarget:self withSelector:@selector(getProductList:)];
    apiCall = nil;
}


-(void)getProductList:(id)response
{
    //NSLog(@"menu response --------- %@",response);
    [obj removeLoadingView:self.view];
    
    if ([[response valueForKey:@"successFlag"]integerValue]==1)
    {
     NSLog(@"response---------%@",response);
      
        arrayCollectionImage=[response valueForKey:@"productList"];
        selectedItemArray = [response valueForKey:@"productCategory"];
     
        
        [_collectionViewItem reloadData];
        
NSLog(@"menu arrayCollectionImage response --------- %@",arrayCollectionImage);
    }
    else
    {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert"
                                      message:@"Product not found"
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
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)tapBack:(UIButton *)sender
{
    [self.navigationController popViewControllerAnimated:YES];
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
    
    itemViewObj.subProductID = subCatID;
    
   // itemViewObj.strSubMenuName =_lblItemName.text;
    if([[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productImageUrl"]!=(NSString *)[NSNull null])
    {
        itemViewObj.selectedItemImageUrl=[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productImageUrl"];
    }
    
    if([[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productName"]!=(NSString *)[NSNull null])
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
    
//   if([[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productBasePrice"]!=(NSString *)[NSNull null])
//    {
//        itemViewObj.selectedItemICost= [NSString stringWithFormat:@"%@",[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productBasePrice"]];
//    }
    
    
    int prodID = [[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"id"]intValue];
    
                  itemViewObj.productID= prodID;
    
    int catID = [[[arrayCollectionImage objectAtIndex:indexPath.row]valueForKey:@"productCategoryId"]intValue];
    
        itemViewObj.categoryID = catID;
    
    
    
    
    
    // itemViewObj.selectedItemId=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"itemid"];
    
//    itemViewObj.selectedmainMenuNameItem =  [[self.categoryArray objectAtIndex:strCatIdMain]valueForKey:@"productCategoryName"];
    
//    if(subItemArray.count>0)
//    {
//        itemViewObj.subCategoryArray = selectedItemArray;
//    }
//    
//    itemViewObj.itemActegoryID = indexPath.row;
    
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
