//
//  OrderhistoryViewController.m
//  iOS_FBTemplate1
//
//  Created by HARSH on 21/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "OrderhistoryViewController.h"
#import "OrderHistoryTableViewCell.h"
#import "AppDelegate.h"
#import "OrderHeaderTable.h"
#import "OrderPageViewController.h"
#import "CustomBottomBarView.h"
#import "MenuViewController.h"
@interface OrderhistoryViewController ()
{
    NSArray *orderTableData;
    
    NSMutableArray *itemOrderDateArray;
    NSMutableArray *itemOrderStatusArray;
    NSMutableArray *itemorderStoreIdArray;
    NSMutableArray *itemorderTimeArray;
    NSMutableArray *itemOrderTotalPriceArray;
    NSMutableArray *itemOrderNumberArray;
    NSMutableArray *itemOrderSRNumber;
}
@property (weak, nonatomic) IBOutlet UITableView *historyTableView;

@end

@implementation OrderhistoryViewController



-(void)callOrderHeaderTable
{
    
    AppDelegate *appDelegate =
    [[UIApplication sharedApplication] delegate];
    
    NSManagedObjectContext *context2 =
    [appDelegate managedObjectContext];
    
    
    
    
    NSFetchRequest *fetchRequest2 = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity2 = [NSEntityDescription entityForName:@"OrderHeaderTable"
                                               inManagedObjectContext:context2];
    [fetchRequest2 setEntity:entity2];
    NSError *error3;
    orderTableData = [context2 executeFetchRequest:fetchRequest2 error:&error3];
    int count=0;
    NSLog(@"orderdata==%@",orderTableData);
    itemOrderDateArray=[[NSMutableArray alloc]init];
    itemOrderStatusArray=[[NSMutableArray alloc]init];
    itemorderStoreIdArray=[[NSMutableArray alloc]init];
    itemorderTimeArray=[[NSMutableArray alloc]init];
    itemOrderTotalPriceArray=[[NSMutableArray alloc]init];
    itemOrderNumberArray=[[NSMutableArray alloc]init];
    itemOrderSRNumber=[[NSMutableArray alloc]init];
    for (OrderHeaderTable *info in orderTableData)
    {
        
        
        NSLog(@"itemOrderDate: %@", info.itemOrderDate);
        [itemOrderDateArray addObject:info.itemOrderDate];
        
        NSLog(@"itemOrderNumber: %@", info.itemOrderNumber);
        [itemOrderNumberArray addObject:info.itemOrderNumber];
        
        NSLog(@"itemOrderStatus: %@", info.itemOrderStatus);
        [itemOrderStatusArray addObject:info.itemOrderStatus];
        
        NSLog(@"itemorderStoreId: %@", info.itemorderStoreId);
        [itemorderStoreIdArray addObject:info.itemorderStoreId];
        
        NSLog(@"itemorderStoreId: %@", info.itemorderTime);
        [itemorderTimeArray addObject:info.itemorderTime];
        
        NSLog(@"itemOrderTotalPrice: %@", info.itemOrderTotalPrice);
        [itemOrderTotalPriceArray addObject:info.itemOrderTotalPrice];
        NSLog(@"count==%i",++count);
    }
    
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    //    bottom1.bottomDelegate=self;
    bottom1.hidden=NO;
    self.navigationItem.title=@"ORDER HISTORY";
    self.navigationController.navigationBar.titleTextAttributes=@{NSForegroundColorAttributeName: [UIColor whiteColor]};
   
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    self.navigationController.navigationBar.titleTextAttributes=@{NSForegroundColorAttributeName: [UIColor whiteColor]};
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.hidesBackButton = NO;
    
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)backClicked:(id)sender
{
    
    MenuViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"comeFromHistory"];
    [self.navigationController pushViewController:add animated:NO];
}
-(void)viewWillAppear:(BOOL)animated

{
    [super viewWillAppear:YES];
     [[NSUserDefaults standardUserDefaults]setValue:@"0" forKey:@"orderCount"];
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    bottom1.orderHistoryCount.layer.borderWidth=.5f;
    
    bottom1.orderHistoryCount.layer.borderColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1].CGColor;
    
    bottom1.orderHistoryCount.layer.cornerRadius = bottom1.orderHistoryCount.frame.size.width / 2;
    bottom1.orderHistoryCount.clipsToBounds = YES;
    //    bottom1.bottomDelegate=self;
    bottom1.hidden=NO;
    self.navigationItem.title=@"ORDER HISTORY";
    
    self.navigationItem.hidesBackButton=YES;;
    self.navigationController.navigationBar.tintColor=[UIColor whiteColor];
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    [self.navigationController.navigationBar setTitleTextAttributes:
     @{NSForegroundColorAttributeName:[UIColor whiteColor],
       NSFontAttributeName:[UIFont fontWithName:@"Futura-CondensedExtraBold" size:18.0f]}];
    
    UIButton *btnBack = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnBack setFrame:CGRectMake(0, 0,30, 44)];
    [btnBack setImage:[UIImage imageNamed:@"BackIcon"] forState:UIControlStateNormal];
    [btnBack setImageEdgeInsets:UIEdgeInsetsMake(0, -30, 0.0, 0)];
    [btnBack addTarget:self action:@selector(backClicked:) forControlEvents:UIControlEventTouchUpInside];
    // btnBack.titleLabel.font = [UIFont fontWithName:@"HelveticaNeue-Bold" size:14.0f];
    UIBarButtonItem *barBtnBack = [[UIBarButtonItem alloc] initWithCustomView:btnBack];
    [barBtnBack setTintColor:[UIColor whiteColor]];
    
    self.navigationItem.leftBarButtonItem=barBtnBack;
    [self callOrderHeaderTable];
    int countNumber= [itemOrderNumberArray count];
    for (int i=1; i<=countNumber; i++)
    {
        [itemOrderSRNumber addObject:[NSString stringWithFormat:@"%d",i]];
    }
    [_historyTableView reloadData];
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 120.0f;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 40.0f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"OrderHistoryTableViewCell" owner:self options:nil];
    UIView *view = [array objectAtIndex:1];
    view.backgroundColor = [UIColor colorWithRed:0.969 green:0.941 blue:0.902 alpha:1]; /*#f7f0e6*/
    return view;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    int countNumber= [itemOrderNumberArray count];
    [[NSUserDefaults standardUserDefaults]setValue:[NSString stringWithFormat:@"%ld",(long)countNumber] forKey:@"historyCount"];
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    bottom1.orderHistoryCount.layer.borderWidth=.5f;
    bottom1.orderHistoryCount.layer.borderColor=[UIColor lightGrayColor].CGColor;
    bottom1.orderHistoryCount.layer.cornerRadius = bottom1.orderHistoryCount.frame.size.width / 2;
    bottom1.orderHistoryCount.clipsToBounds = YES;
    
    bottom1.orderHistoryCount.text=[NSString stringWithFormat:@"%d",countNumber];
    
    return [itemOrderNumberArray count];
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    OrderHistoryTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"OrderHistoryTableViewCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"OrderHistoryTableViewCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
    if(indexPath.row%2 == 0)
    {
        
        cell.backgroundColor=[UIColor clearColor];
    }
    else
    {
        cell.backgroundColor = [UIColor colorWithRed:0.969 green:0.941 blue:0.902 alpha:1]; /*#f7f0e6*/
        
        
    }
    
//    cell.contentView.layer.borderWidth=.5f;
//    cell.contentView.layer.borderColor=[UIColor colorWithRed:0.486 green:0.471 blue:0.498 alpha:1].CGColor;
    cell.orderNum.text=[itemOrderNumberArray objectAtIndex:indexPath.row];
    cell.orderDate.text=[itemOrderDateArray objectAtIndex:indexPath.row];
    cell.ordertime.text=[itemorderTimeArray objectAtIndex:indexPath.row];
    cell.orderStatus.text=[itemOrderStatusArray objectAtIndex:indexPath.row];
    cell.orderTotalprice.text=[itemOrderTotalPriceArray objectAtIndex:indexPath.row];
    cell.lblOrderSerialNumber.text=[itemOrderSRNumber objectAtIndex:indexPath.row];
    
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    OrderPageViewController *order=[self.storyboard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
    NSString *strorderNum=[itemOrderNumberArray objectAtIndex:indexPath.row];
    [[NSUserDefaults standardUserDefaults]setObject:strorderNum forKey:@"orderNumber"];
    [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"comeFromHistory"];
    [self.navigationController pushViewController:order animated:NO];
    
    
}
-(void)viewWillDisappear:(BOOL)animated
{
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    //    bottom1.bottomDelegate=self;
    bottom1.hidden=NO;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
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
