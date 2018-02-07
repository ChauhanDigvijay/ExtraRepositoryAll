

//  OrderPageViewController.m
//  taco2
//
//  Created by HARSH on 02/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "OrderPageViewController.h"
#import "OrderPageTableViewCell.h"
#import "MenuViewController.h"
#import "OrederViewController.h"
#import "AFHTTPRequestOperationManager.h"
#import "AppDelegate.h"
#import "OrderHistory.h"
#import "OrderHeaderTable.h"
#import "StorelocatorViewController.h"
#import "OrderhistoryViewController.h"
#import "PaymentScreenViewController.h"
#import "CustomBottomBarView.h"
#import "ViewItemViewController.h"

@interface OrderPageViewController ()<UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate,UITextViewDelegate,OrderPageCellDelegate>
{
    NSString *itemselected;
    NSString *itemCost;
    NSString *itemDesc;
    NSString *itemId1;
    NSString *itemImageUrl1;
    BOOL notselected;
    BOOL editClicked;
    NSString *orderNumber;
    BOOL comefromHistory;
    OrderPageTableViewCell *orderCell;
    BOOL confirmedClicked;
    BOOL confromOrderComplete;
    BOOL fromMENUOrder;
    BOOL fromStore;
    BOOL fromItemView;
    
    
    BOOL orderToOrder;
    
    
    NSString *itemSize;
    
    
}
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (strong, nonatomic) IBOutlet UILabel *totalPrice;
@property (strong, nonatomic) IBOutlet UILabel *showSalesTax;
@property (strong, nonatomic) IBOutlet UILabel *subTotal;
@property (strong, nonatomic) IBOutlet UILabel *showServiceCharge;
@property (strong, nonatomic) IBOutlet UILabel *deliveryLabel;
@property (strong, nonatomic) IBOutlet UILabel *showDelivery;


@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tableHeight;
@property (weak, nonatomic) IBOutlet UIView *headerView;

@property (weak, nonatomic) IBOutlet UILabel *showpricesLabel;

@property (weak, nonatomic) IBOutlet UILabel *showAddress;
@property (weak, nonatomic) IBOutlet UILabel *shoeCity;

@property (weak, nonatomic) IBOutlet UIView *participationView;
@property (weak, nonatomic) IBOutlet UITableView *orderTableView;
@property (weak, nonatomic) IBOutlet UIButton *backOutlet1;
@property (weak, nonatomic) IBOutlet UIButton *addMoreOutlet;
@property (weak, nonatomic) IBOutlet UIButton *continueoutlet;
@property (weak, nonatomic) IBOutlet UIImageView *bannerOutlet;
@property (weak, nonatomic) IBOutlet UIView *addressViewOutlet;
@property (weak, nonatomic) IBOutlet UIButton *orderConfirmedOutlet;
- (IBAction)orderConfirmedAction:(id)sender;

@end

@implementation OrderPageViewController

static NSMutableArray *additem;
static  NSMutableArray *costitem;
static  NSMutableArray *Descitem;
static  NSMutableArray *itemId;
static  NSMutableArray *itemImageUrl;
static  NSMutableArray *sizeArray;
NSMutableArray *priceArray;
NSMutableArray *countitem;

static NSMutableArray *quantityArray;
- (void)somethingWithParam
{
    NSLog(@"my method");
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"EDITCLICKED"];
    NSUserDefaults *myIndex=[NSUserDefaults standardUserDefaults];
    NSString *strindex=[myIndex objectForKey:@"myindex"];
    [myIndex synchronize];
    [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"myindex"];
    NSUserDefaults *quantityValue=[NSUserDefaults standardUserDefaults];
    
    
    NSString *strquantity=[quantityValue objectForKey:@"Editquantity"];
    [quantityValue synchronize];
    [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"Editquantity"];
    int index_path=[strindex intValue];
    [quantityArray replaceObjectAtIndex:index_path withObject:[NSString stringWithFormat:@"%@",strquantity]];
    //[quantityArray insertObject:[NSString stringWithFormat:@"%@",strquantity]atIndex:index_path];
    NSUserDefaults *finalCostSingleItem=[NSUserDefaults standardUserDefaults];
    NSString *finalcostPrice=[finalCostSingleItem objectForKey:@"finalCostSingleItem"];
    [finalCostSingleItem synchronize];
    [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"finalCostSingleItem"];
    //  [priceArray insertObject:finalcostPrice atIndex:index_path];
    [priceArray replaceObjectAtIndex:index_path withObject:finalcostPrice];
    
    int count = [priceArray count];
    long double sum = 0;
    for (int i = 0; i < count; i++)
    {
        NSString *myValu=[priceArray objectAtIndex:i];
        myValu=[myValu substringFromIndex:1];
        NSLog(@"%@",myValu);
        float finalvalue=[myValu floatValue];
        sum += finalvalue;
    }
    _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
    int serviceCharge=10;
    _showServiceCharge.text=[NSString stringWithFormat:@"$ %.2d",serviceCharge];
    _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
    long double subTotal=sum+serviceCharge;
    _subTotal.text=[NSString stringWithFormat:@"$ %.2Lf",subTotal];
    long double  salestax;
    long double findSalestax=0.08;
    salestax=findSalestax*subTotal;
    
    _showSalesTax.text=[NSString stringWithFormat:@"$ %.2Lf",salestax];
    long double mytotal=salestax+subTotal;
    _totalPrice.text=[NSString stringWithFormat:@"$ %.2Lf",mytotal];    NSString *strTextcost =[NSString stringWithFormat:@"$ %.2Lf",sum];
    // OrderPageViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
    //  add.totalCost.text=strTextcost;
    //    [[add totalCost]setText:strTextcost];
    _totalCost.text=strTextcost;
    
}

-(void)fromorderHistory
{
    
    NSString *strOrderNum=[[NSUserDefaults standardUserDefaults]valueForKey:@"orderNumber"];
    
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"comeFromHistory"];
    
    AppDelegate *appDelegate =
    [[UIApplication sharedApplication] delegate];
    
    NSManagedObjectContext *context =
    [appDelegate managedObjectContext];
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"OrderHistory" inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    
    
    
    NSPredicate *p=[NSPredicate predicateWithFormat:@"itemOrderNumber==%@",strOrderNum];
    [fetchRequest setPredicate:p];
    //... add sorts if you want them
    NSError *fetchError;
    NSArray *fetchedProducts2=[context executeFetchRequest:fetchRequest error:&fetchError];
    additem=[[NSMutableArray alloc]init];
    costitem=[[NSMutableArray alloc]init];
    Descitem=[[NSMutableArray alloc]init];
    itemId=[[NSMutableArray alloc]init];
    itemImageUrl=[[NSMutableArray alloc]init];
    quantityArray=[[NSMutableArray alloc]init];
    priceArray=[[NSMutableArray alloc]init];
    countitem=[[NSMutableArray alloc]init];
    sizeArray=[[NSMutableArray alloc]init];
    for (OrderHistory *info in fetchedProducts2)
    {
        NSLog(@"%@",info.itemOrderNumber);
        [additem addObject:info.itemName];
        [costitem addObject:info.itemCost];
        [Descitem addObject:info.itemDesc];
        [itemImageUrl addObject:info.itemImageurl];
        [countitem addObject:info.itemCount];
        [priceArray addObject:info.itemTotalCost];
        [quantityArray addObject:info.itemQuantity];
        [itemId addObject:info.itemID];
        [sizeArray addObject:info.itemSize];
        
    }
    //[uniqueArray removeObject:additem];
    
    int count = [priceArray count];
    long double sum = 0;
    for (int i = 0; i < count; i++)
    {
        NSString *myValu=[priceArray objectAtIndex:i];
        myValu=[myValu substringFromIndex:1];
        NSLog(@"%@",myValu);
        float finalvalue=[myValu floatValue];
        sum += finalvalue;
    }
    _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
    int serviceCharge=10;
    _showServiceCharge.text=[NSString stringWithFormat:@"$ %.2d",serviceCharge];
    _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
    long double subTotal=sum+serviceCharge;
    _subTotal.text=[NSString stringWithFormat:@"$ %.2Lf",subTotal];
    long double  salestax;
    long double findSalestax=0.08;
    salestax=findSalestax*subTotal;
    
    _showSalesTax.text=[NSString stringWithFormat:@"$ %.2Lf",salestax];
    long double mytotal=salestax+subTotal;
    _totalPrice.text=[NSString stringWithFormat:@"$ %.2Lf",mytotal];    if([additem count]>0)
    {
        _noItemOutlet.hidden=YES;
        _tableBottomLineView.hidden=NO;
        _totalView.hidden=NO;
    }
    else
    {
        _noItemOutlet.hidden=NO;
        _tableBottomLineView.hidden=YES;
        _totalView.hidden=YES;
    }
}
-(void)editOrderClicked:(id)sender
{
    
    
    [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"EDITORDERCLICKED"];
    NSUserDefaults *userDefaultsArray = [NSUserDefaults standardUserDefaults];
    [userDefaultsArray setObject:additem forKey:@"additem"];
    [userDefaultsArray setObject:costitem forKey:@"costitem"];
    [userDefaultsArray setObject:Descitem forKey:@"Descitem"];
    [userDefaultsArray setObject:itemImageUrl forKey:@"itemImageUrl"];
    [userDefaultsArray setObject:quantityArray forKey:@"quantityArray"];
    [userDefaultsArray setObject:priceArray forKey:@"priceArray"];
    [userDefaultsArray setObject:itemId forKey:@"itemId"];
    [userDefaultsArray setObject:countitem forKey:@"countitem"];
    [userDefaultsArray setObject:sizeArray forKey:@"sizeArray"];
    [userDefaultsArray synchronize];
    NSLog(@"edit clicked");
    NSLog(@"sender : %@",sender);
    UIButton *btn = (UIButton *)sender;
    CGPoint point = [btn convertPoint:btn.bounds.origin toView:_orderTableView];
    NSIndexPath *indexPath = [_orderTableView indexPathForRowAtPoint:point];
    if (indexPath)
    {
        NSLog(@"indexPath : %ld",(long)indexPath.row);
        int i=(long)indexPath.row;
        NSUserDefaults *defaultsValues=[NSUserDefaults standardUserDefaults];
        [defaultsValues setValue:[additem objectAtIndex:i] forKey:@"editAddItem"];
        [defaultsValues setValue:[itemImageUrl objectAtIndex:i] forKey:@"editItemImageUrl"];
        [defaultsValues setValue:[Descitem objectAtIndex:i] forKey:@"editDescitem"];
        [defaultsValues setValue:[quantityArray objectAtIndex:i] forKey:@"editquantityArray"];
        [defaultsValues setValue:[NSString stringWithFormat:@"%d",i] forKey:@"editIndex"];
        [defaultsValues synchronize];
        ViewItemViewController *toItemView=[self.storyboard instantiateViewControllerWithIdentifier:@"ViewItemViewController"];
        [self.navigationController pushViewController:toItemView animated:YES];
    }
    else
    {
        
    }
    
    
    
}
-(void)removeOrderClicked:(id)sender
{
    
     [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"EDITORDERCLICKED"];
    
    NSLog(@"sender : %@",sender);
    UIButton *btn = (UIButton *)sender;
    CGPoint point = [btn convertPoint:btn.bounds.origin toView:_orderTableView];
    NSIndexPath *indexPath = [_orderTableView indexPathForRowAtPoint:point];
    if (indexPath)
    {
        NSLog(@"indexPath : %ld",(long)indexPath.row);
        int i=(long)indexPath.row;
        [costitem removeObjectAtIndex:i];
        [sizeArray removeObjectAtIndex:i];
        [Descitem removeObjectAtIndex:i];
        [quantityArray removeObjectAtIndex:i];
        
        [priceArray removeObjectAtIndex:i];
        [additem removeObjectAtIndex:i];
        [countitem removeObjectAtIndex:i];
        countitem=nil;
        countitem=[[NSMutableArray alloc]init];
        for (int j=0; j<[additem count]; j++)
        {
            
            [countitem insertObject:[NSString stringWithFormat:@"%d",j+1] atIndex:j];
        }
        
        int count = [priceArray count];
        long double sum = 0;
        for (int i = 0; i < count; i++)
        {
            NSString *myValu=[priceArray objectAtIndex:i];
            myValu=[myValu substringFromIndex:1];
            NSLog(@"%@",myValu);
            float finalvalue=[myValu floatValue];
            sum += finalvalue;
        }
        _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
        int serviceCharge=10;
        _showServiceCharge.text=[NSString stringWithFormat:@"$ %.2d",serviceCharge];
        _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
        long double subTotal=sum+serviceCharge;
        _subTotal.text=[NSString stringWithFormat:@"$ %.2Lf",subTotal];
        long double  salestax;
        long double findSalestax=0.08;
        salestax=findSalestax*subTotal;
        
        _showSalesTax.text=[NSString stringWithFormat:@"$ %.2Lf",salestax];
        long double mytotal=salestax+subTotal;
        _totalPrice.text=[NSString stringWithFormat:@"$ %.2Lf",mytotal];
        [_orderTableView reloadData];
        if([additem count]>0)
        {
            _noItemOutlet.hidden=YES;
            _tableBottomLineView.hidden=NO;
            _totalView.hidden=NO;
            
        }
        else
        {
            NSInteger countNumber= [additem count];
            [[NSUserDefaults standardUserDefaults]setValue:[NSString stringWithFormat:@"%ld",(long)countNumber] forKey:@"orderCount"];
            _noItemOutlet.hidden=NO;
            _tableBottomLineView.hidden=YES;
            _totalView.hidden=YES;
            
        }
    }
    else
    {
        
    }
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    int small,medium,large,extralarge;
    small=10;
    medium=12;
    large=14;
    extralarge=18;
    _addressViewOutlet.hidden=YES;
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    int countorder=(long)[bottom1.orderCount.text intValue];
    NSLog(@"%d",countorder);
    if (countorder==0)
    {
        _noItemOutlet.hidden=NO;
        
        _tableBottomLineView.hidden=YES;
        _totalView.hidden=YES;
        
    }
    else
    {
        _noItemOutlet.hidden=YES;
        _tableBottomLineView.hidden=NO;
        _totalView.hidden=NO;
    }
    
    
    //
    //
    comefromHistory=NO;
    _viewHeight.constant = [UIScreen mainScreen].bounds.size.height - 104;
    comefromHistory= [[[NSUserDefaults standardUserDefaults]valueForKey:@"comeFromHistory"]boolValue];
    if(comefromHistory==YES)
    {
        [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"comeFromHistory"];
        [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"comeFromHistory"];
        [self fromorderHistory];
    }
    else
    {
        int len=[additem count];
        if (len==0)
        {
            //            editClicked=NO;
            additem=[[NSMutableArray alloc]init];
            costitem=[[NSMutableArray alloc]init];
            Descitem=[[NSMutableArray alloc]init];
            itemId=[[NSMutableArray alloc]init];
            itemImageUrl=[[NSMutableArray alloc]init];
            quantityArray=[[NSMutableArray alloc]init];
            priceArray=[[NSMutableArray alloc]init];
            countitem=[[NSMutableArray alloc]init];
            sizeArray=[[NSMutableArray alloc]init];
            _noItemOutlet.hidden=NO;
            
            _tableBottomLineView.hidden=YES;
            _totalView.hidden=YES;
            //            [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"EDITCLICKED"];
        }
        else
        {
            _noItemOutlet.hidden=YES;
            _tableBottomLineView.hidden=NO;
            _totalView.hidden=NO;
        }
        orderToOrder=NO;
        orderToOrder=[[[NSUserDefaults standardUserDefaults]valueForKey:@"orderToOrder"]boolValue];
        
        if(orderToOrder==YES)
        {
            [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"orderToOrder"];
            [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"orderToOrder"];
            int count = [priceArray count];
            long double sum = 0;
            for (int i = 0; i < count; i++)
            {
                NSString *myValu=[priceArray objectAtIndex:i];
                myValu=[myValu substringFromIndex:1];
                NSLog(@"%@",myValu);
                float finalvalue=[myValu floatValue];
                sum += finalvalue;
            }
            
            if([additem count]>0)
            {
                _noItemOutlet.hidden=YES;
                _tableBottomLineView.hidden=NO;
                _totalView.hidden=NO;
            }
            else
            {
                _noItemOutlet.hidden=NO;
                _tableBottomLineView.hidden=YES;
                _totalView.hidden=YES;
            }
            
            _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
            int serviceCharge=10;
            _showServiceCharge.text=[NSString stringWithFormat:@"$ %.2d",serviceCharge];
            _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
            long double subTotal=sum+serviceCharge;
            _subTotal.text=[NSString stringWithFormat:@"$ %.2Lf",subTotal];
            long double  salestax;
            long double findSalestax=0.08;
            salestax=findSalestax*subTotal;
            
            _showSalesTax.text=[NSString stringWithFormat:@"$ %.2Lf",salestax];
            long double mytotal=salestax+subTotal;
            _totalPrice.text=[NSString stringWithFormat:@"$ %.2Lf",mytotal];
        }
        else
        {
            NSUserDefaults *costSelectedItem=[NSUserDefaults standardUserDefaults];
            itemCost=[costSelectedItem objectForKey:@"costSelectedItem"];
            [costSelectedItem synchronize];
            
            NSUserDefaults *sizeItem1=[NSUserDefaults standardUserDefaults];
            itemSize=[sizeItem1 valueForKey:@"selectSize"];
            [sizeItem1 synchronize];
            
            
            NSUserDefaults *descSelectedItem=[NSUserDefaults standardUserDefaults];
            itemDesc=[descSelectedItem objectForKey:@"descSelectedItem"];
            [descSelectedItem synchronize];
            //
            //        NSUserDefaults *removeItemCost=[NSUserDefaults standardUserDefaults];
            //        [removeItemCost objectForKey:@"costSelectedItem"];
            //        [removeItemCost synchronize];
            //        NSUserDefaults *removeItemdesc=[NSUserDefaults standardUserDefaults];
            //        [removeItemdesc objectForKey:@"descSelectedItem"];
            //        [removeItemdesc synchronize];
            
            NSUserDefaults *Selecteditemid=[NSUserDefaults standardUserDefaults];
            itemId1=[Selecteditemid objectForKey:@"Selecteditemid"];
            [Selecteditemid synchronize];
            
            //        NSUserDefaults *removeitemid=[NSUserDefaults standardUserDefaults];
            //        [removeitemid objectForKey:@"Selecteditemid"];
            //        [removeitemid synchronize];
            
            NSUserDefaults *imageurlSelecteditem=[NSUserDefaults standardUserDefaults];
            itemImageUrl1=[imageurlSelecteditem objectForKey:@"imageurlSelecteditem"];
            [imageurlSelecteditem synchronize];
            
            //        NSUserDefaults *removeItemimageurl=[NSUserDefaults standardUserDefaults];
            //        [removeItemimageurl objectForKey:@"imageurlSelecteditem"];
            //        [removeItemimageurl synchronize];
            
            NSUserDefaults *selectedItem=[NSUserDefaults standardUserDefaults];
            itemselected=[selectedItem objectForKey:@"selected"];
            
            [selectedItem synchronize];
            if ([additem containsObject:itemselected])
            {
                
                fromStore=[[[NSUserDefaults standardUserDefaults]valueForKey:@"fromStore"]boolValue];
                if(fromStore==YES)
                {
                    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"fromStore"];
                    int count = [priceArray count];
                    long double sum = 0;
                    for (int i = 0; i < count; i++)
                    {
                        NSString *myValu=[priceArray objectAtIndex:i];
                        myValu=[myValu substringFromIndex:1];
                        NSLog(@"%@",myValu);
                        float finalvalue=[myValu floatValue];
                        sum += finalvalue;
                    }
                    _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                    int serviceCharge=10;
                    _showServiceCharge.text=[NSString stringWithFormat:@"$ %.2d",serviceCharge];
                    _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                    long double subTotal=sum+serviceCharge;
                    _subTotal.text=[NSString stringWithFormat:@"$ %.2Lf",subTotal];
                    long double  salestax;
                    long double findSalestax=0.08;
                    salestax=findSalestax*subTotal;
                    
                    _showSalesTax.text=[NSString stringWithFormat:@"$ %.2Lf",salestax];
                    long double mytotal=salestax+subTotal;
                    _totalPrice.text=[NSString stringWithFormat:@"$ %.2Lf",mytotal];
                }
                else
                {
                    if(editClicked==YES)
                    {
                        
                    }
                    else
                    {
                        confirmedClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"ORDERCONFIRMED"]boolValue];
                        if(confirmedClicked==YES)
                        {
                            [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"ORDERCONFIRMED"];
                            _addressViewOutlet.hidden=NO;
                            _participationView.hidden=NO;
                            
                            
                            _shoeCity.text=[[NSUserDefaults standardUserDefaults]valueForKey:@"selectedCity"];
                            _showAddress.text=[[NSUserDefaults standardUserDefaults]valueForKey:@"selectedaddress"];
                            int count = [priceArray count];
                            long double sum = 0;
                            for (int i = 0; i < count; i++)
                            {
                                NSString *myValu=[priceArray objectAtIndex:i];
                                myValu=[myValu substringFromIndex:1];
                                NSLog(@"%@",myValu);
                                float finalvalue=[myValu floatValue];
                                sum += finalvalue;
                            }
                            _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                            int serviceCharge=10;
                            _showServiceCharge.text=[NSString stringWithFormat:@"$ %.2d",serviceCharge];
                            _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                            long double subTotal=sum+serviceCharge;
                            _subTotal.text=[NSString stringWithFormat:@"$ %.2Lf",subTotal];
                            long double  salestax;
                            long double findSalestax=0.08;
                            salestax=findSalestax*subTotal;
                            
                            _showSalesTax.text=[NSString stringWithFormat:@"$ %.2Lf",salestax];
                            long double mytotal=salestax+subTotal;
                            _totalPrice.text=[NSString stringWithFormat:@"$ %.2Lf",mytotal];
                        }
                        else
                        {
                            
                            //edityes
                            
                            BOOL iseditorderClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"EDITORDERCLICKED"]boolValue];
                            if(iseditorderClicked==YES)
                            {
                                additem=[[NSMutableArray alloc]init];
                                costitem=[[NSMutableArray alloc]init];
                                Descitem=[[NSMutableArray alloc]init];
                                itemId=[[NSMutableArray alloc]init];
                                itemImageUrl=[[NSMutableArray alloc]init];
                                quantityArray=[[NSMutableArray alloc]init];
                                priceArray=[[NSMutableArray alloc]init];
                                countitem=[[NSMutableArray alloc]init];
                                sizeArray=[[NSMutableArray alloc]init];
                                NSInteger editIndex=[[[NSUserDefaults standardUserDefaults]valueForKey:@"editIndex"]integerValue];
                                additem=[[[NSUserDefaults standardUserDefaults]valueForKey:@"additem"] mutableCopy];
                                costitem=[[[NSUserDefaults standardUserDefaults]valueForKey:@"costitem"] mutableCopy];
                                Descitem=[[[NSUserDefaults standardUserDefaults]valueForKey:@"Descitem"] mutableCopy];
                                itemImageUrl=[[[NSUserDefaults standardUserDefaults]valueForKey:@"itemImageUrl"] mutableCopy];
                               NSMutableArray *Qt=[[NSMutableArray alloc]init];
                                Qt=[[NSUserDefaults standardUserDefaults]valueForKey:@"quantityArray"];
                                quantityArray=[Qt mutableCopy];
                                
                                priceArray=[[[NSUserDefaults standardUserDefaults]valueForKey:@"priceArray"] mutableCopy];
                                itemId=[[[NSUserDefaults standardUserDefaults]valueForKey:@"itemId"] mutableCopy];
                                countitem=[[[NSUserDefaults standardUserDefaults]valueForKey:@"countitem"] mutableCopy];
                                sizeArray=[[[NSUserDefaults standardUserDefaults]valueForKey:@"sizeArray"] mutableCopy];
                                NSString *strQU=[[[NSUserDefaults standardUserDefaults]valueForKey:@"updatedQuantity"] mutableCopy];
                                [quantityArray replaceObjectAtIndex:editIndex withObject:strQU];
                                int countpriceindex=[additem count]-1;
                                
                                NSString *pricecal=[NSString stringWithFormat:@"$%.02f",[[costitem objectAtIndex:countpriceindex] floatValue]*[[quantityArray objectAtIndex:countpriceindex]intValue]];
                                
                                [priceArray replaceObjectAtIndex:countpriceindex withObject:pricecal];
                                int count = [priceArray count];
                                long double sum = 0;
                                for (int i = 0; i < count; i++)
                                {
                                    NSString *myValu=[priceArray objectAtIndex:i];
                                    myValu=[myValu substringFromIndex:1];
                                    NSLog(@"%@",myValu);
                                    float finalvalue=[myValu floatValue];
                                    sum += finalvalue;
                                }
                                _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                                int serviceCharge=10;
                                _showServiceCharge.text=[NSString stringWithFormat:@"$ %.2d",serviceCharge];
                                _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                                long double subTotal=sum+serviceCharge;
                                _subTotal.text=[NSString stringWithFormat:@"$ %.2Lf",subTotal];
                                long double  salestax;
                                long double findSalestax=0.08;
                                salestax=findSalestax*subTotal;
                                
                                _showSalesTax.text=[NSString stringWithFormat:@"$ %.2Lf",salestax];
                                long double mytotal=salestax+subTotal;
                                _totalPrice.text=[NSString stringWithFormat:@"$ %.2Lf",mytotal];
                            }
                            else
                            {
                            NSLog(@"Bingo!");
                            notselected=YES;
                            NSUInteger index = [additem indexOfObject:itemselected];
                            NSString *strquantityIndex=[quantityArray objectAtIndex:index];
                            float value=[strquantityIndex floatValue];
                            value=value+1;
                            NSString *coststr=[costitem objectAtIndex:index];
                            float value2=[coststr floatValue];
                            float finalcost=value*value2;
                            NSString *finalCostStr=[NSString stringWithFormat:@"$%.02f",finalcost];
                            [priceArray replaceObjectAtIndex:index withObject:finalCostStr];
                            NSString *finalQuantity=[NSString stringWithFormat:@"%d",(int)value];
                            [quantityArray replaceObjectAtIndex:index withObject:finalQuantity];
                            [additem replaceObjectAtIndex:index withObject:itemselected];
                            [costitem replaceObjectAtIndex:index withObject:itemCost];
                            [Descitem replaceObjectAtIndex:index withObject:itemDesc];
                            [itemId replaceObjectAtIndex:index withObject:itemId1];
                            [itemImageUrl replaceObjectAtIndex:index withObject:itemImageUrl1];
                            [sizeArray replaceObjectAtIndex:index withObject:itemSize];
                            int countpriceindex=[additem count]-1;
                            
                            NSString *pricecal=[NSString stringWithFormat:@"$%.02f",[[costitem objectAtIndex:countpriceindex] floatValue]*[[quantityArray objectAtIndex:countpriceindex]intValue]];
                            
                            [priceArray replaceObjectAtIndex:countpriceindex withObject:pricecal];
                            int count = [priceArray count];
                            long double sum = 0;
                            for (int i = 0; i < count; i++)
                            {
                                NSString *myValu=[priceArray objectAtIndex:i];
                                myValu=[myValu substringFromIndex:1];
                                NSLog(@"%@",myValu);
                                float finalvalue=[myValu floatValue];
                                sum += finalvalue;
                            }
                            _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                            int serviceCharge=10;
                            _showServiceCharge.text=[NSString stringWithFormat:@"$ %.2d",serviceCharge];
                            _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                            long double subTotal=sum+serviceCharge;
                            _subTotal.text=[NSString stringWithFormat:@"$ %.2Lf",subTotal];
                            long double  salestax;
                            long double findSalestax=0.08;
                            salestax=findSalestax*subTotal;
                            
                            _showSalesTax.text=[NSString stringWithFormat:@"$ %.2Lf",salestax];
                            long double mytotal=salestax+subTotal;
                            _totalPrice.text=[NSString stringWithFormat:@"$ %.2Lf",mytotal];
                            
                            
                        }
                    }
                    }
                }
                
            }
            else
            {
                if(editClicked==YES)
                {
                    
                }
                else
                {
                    
                    [additem addObject:itemselected];
                    
                    NSString *editQuantity=[[NSUserDefaults standardUserDefaults]valueForKey:@"Editquantity"];
                    [quantityArray addObject:editQuantity];
                    [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"Editquantity"];
                    [costitem addObject:itemCost];
                    [Descitem addObject:itemDesc];
                    [itemId addObject:itemId1];
                    [itemImageUrl addObject:itemImageUrl1];
                    [sizeArray addObject:itemSize];
                    NSInteger contitem=[additem count];
                    for (int i=1;i<=contitem;i++)
                    {
                        NSString *item=[NSString stringWithFormat:@"%i",i];
                        [countitem insertObject:item atIndex:i-1];
                    }
                    //                    NSUserDefaults *removeItem=[NSUserDefaults standardUserDefaults];
                    //                    [removeItem objectForKey:@"selected"];
                    //                    [removeItem synchronize];
                    int countpriceindex=[additem count]-1;
                    
                    NSString *pricecal=[NSString stringWithFormat:@"$%.02f",[[costitem objectAtIndex:countpriceindex] floatValue]*[[quantityArray objectAtIndex:countpriceindex]intValue]];
                    
                    [priceArray insertObject:pricecal atIndex:countpriceindex];
                    int count = [priceArray count];
                    long double sum = 0;
                    for (int i = 0; i < count; i++)
                    {
                        NSString *myValu=[priceArray objectAtIndex:i];
                        myValu=[myValu substringFromIndex:1];
                        NSLog(@"%@",myValu);
                        float finalvalue=[myValu floatValue];
                        sum += finalvalue;
                    }
                    _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                    int serviceCharge=10;
                    _showServiceCharge.text=[NSString stringWithFormat:@"$ %.2d",serviceCharge];
                    _totalCost.text=[NSString stringWithFormat:@"$ %.2Lf",sum];
                    long double subTotal=sum+serviceCharge;
                    _subTotal.text=[NSString stringWithFormat:@"$ %.2Lf",subTotal];
                    long double  salestax;
                    long double findSalestax=0.08;
                    salestax=findSalestax*subTotal;
                    
                    _showSalesTax.text=[NSString stringWithFormat:@"$ %.2Lf",salestax];
                    long double mytotal=salestax+subTotal;
                    _totalPrice.text=[NSString stringWithFormat:@"$ %.2Lf",mytotal];                }
            }
        }
        if([additem count]>0)
        {
            _noItemOutlet.hidden=YES;
            _tableBottomLineView.hidden=NO;
            _totalView.hidden=NO;
        }
        else
        {
            _noItemOutlet.hidden=NO;
            _tableBottomLineView.hidden=YES;
            _totalView.hidden=YES;
        }
        
    }
}

- (IBAction)backClicked:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"EDITORDERCLICKED"];

    MenuViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
    
    
    
    [self.navigationController pushViewController:add animated:NO];
    
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    _deliveryLabel.hidden=YES;
    _showDelivery.hidden=YES;
    _continueoutlet.hidden=YES;
    _headerView.layer.borderWidth=0.5f;
    _headerView.layer.borderColor=[UIColor blackColor].CGColor;
    
    
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    bottom1.orderHistoryCount.hidden=NO;
    bottom1.orderCount.hidden=NO;
    
    
    bottom1.orderCount.layer.borderWidth=.5f;
    
    bottom1.orderCount.layer.borderColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1].CGColor;
    
    bottom1.orderCount.layer.cornerRadius = bottom1.orderCount.frame.size.width / 2;
    bottom1.orderCount.clipsToBounds = YES;
    
    bottom1.hidden=NO;
    
    
    
    
    
    
    
    UIButton *btnLeft = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnLeft setFrame:CGRectMake(0, 0,90, 44)];
    
    
    UIImage *buttonImage = [UIImage imageNamed:@"BackIcon"]   ;
    [btnLeft setImage:buttonImage forState:UIControlStateNormal];
    [btnLeft setTitle:@"ADD MORE"  forState:UIControlStateNormal];
    btnLeft.titleLabel.font = [UIFont fontWithName:@"Futura-Medium" size:12.0f];
    
    [btnLeft setImageEdgeInsets:UIEdgeInsetsMake(0,-20, 0.0, 0)];
    [btnLeft addTarget:self action:@selector(backClicked:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *barBtnLeft = [[UIBarButtonItem alloc] initWithCustomView:btnLeft];
    
    [barBtnLeft setTintColor:[UIColor whiteColor]];
    self.navigationItem.leftBarButtonItem=barBtnLeft;
    
    //    UIImage *buttonImage = [[UIImage imageNamed:@"BackIcon"]
    //                            resizableImageWithCapInsets:UIEdgeInsetsMake(0, 0.0, 0.0, 0)];
    //
    //    //create the button and assign the image
    //    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    //    [button setBackgroundImage:buttonImage forState:UIControlStateNormal];
    //
    //    UILabel *lbl = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 90, 44)];
    //    lbl.text = @"ADD MORE";
    //    lbl.textColor=[UIColor whiteColor];
    //    lbl.backgroundColor = [UIColor clearColor];
    //    //lbl.font = [UIFont fontWithName:@"Helvetica" size:12.0];
    //    lbl.numberOfLines = 0;
    //
    //
    //    lbl.font = [UIFont fontWithName:@"Futura-Medium" size:12.0f];
    //
    //    [button addSubview:lbl];
    //
    //    //set the frame of the button to the size of the image (see note below)
    //    button.frame = CGRectMake(0, 0, 100, 44);
    //    [button addTarget:self action:@selector(backClicked:) forControlEvents:UIControlEventTouchUpInside];
    //
    //    //create a UIBarButtonItem with the button as a custom view
    //    UIBarButtonItem *customBarItem = [[UIBarButtonItem alloc] initWithCustomView:button];
    //    self.navigationItem.leftBarButtonItem = customBarItem;
    //add more button
    
    //    UIButton *btnLeft = [UIButton buttonWithType:UIButtonTypeCustom];
    //    [btnLeft setFrame:CGRectMake(0, 0,90, 44)];
    //    [btnLeft setTitle:@"ADD MORE"  forState:UIControlStateNormal];
    //     btnLeft.titleLabel.font = [UIFont fontWithName:@"Futura-Medium" size:12.0f];
    //    [btnLeft setTitleEdgeInsets:UIEdgeInsetsMake(0,-32, 0.0, 0)];
    //    [btnLeft addTarget:self action:@selector(backClicked:) forControlEvents:UIControlEventTouchUpInside];
    //    UIBarButtonItem *barBtnLeft = [[UIBarButtonItem alloc] initWithCustomView:btnLeft];
    //
    //    [barBtnLeft setTintColor:[UIColor whiteColor]];
    //    self.navigationItem.leftBarButtonItem=barBtnLeft;
    //
    UIButton *btnRight = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnRight setFrame:CGRectMake(0, 0,80, 44)];
    [btnRight setTitle:@"CONTINUE"  forState:UIControlStateNormal];
    btnRight.titleLabel.font = [UIFont fontWithName:@"Futura-Medium" size:12.0f];
    [btnRight setTitleEdgeInsets:UIEdgeInsetsMake(0, 0.0, 0.0, -25)];
    [btnRight addTarget:self action:@selector(continueClicked:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *barBtnRight = [[UIBarButtonItem alloc] initWithCustomView:btnRight];
    [barBtnRight setTintColor:[UIColor whiteColor]];
    self.navigationItem.rightBarButtonItem=barBtnRight;
    
    self.navigationItem.hidesBackButton=YES;
    self.navigationController.navigationBarHidden=NO;
    self.navigationController.navigationBar.tintColor=[UIColor whiteColor];
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    self.navigationItem.title=@"MY ORDER";
    
    [self.navigationController.navigationBar setTitleTextAttributes:
     @{NSForegroundColorAttributeName:[UIColor whiteColor],
       NSFontAttributeName:[UIFont fontWithName:@"Futura-CondensedExtraBold" size:18.0f]}];
    //        _addressViewOutlet.layer.borderWidth=0.5f;
    //    _addressViewOutlet.layer.borderColor=[UIColor blackColor].CGColor;
    
    //    if (120*(additem.count)+250 < ([UIScreen mainScreen].bounds.size.height - 104))
    //    {
    //
    //    }
    //    else
    //    {
    //        _viewHeight.constant = 104*(additem.count)+250;
    //        _tableHeight.constant=104*(additem.count);
    //    }
    
    if (104*(additem.count) <_tableHeight.constant)
    {
        
    }
    else
    {
        _viewHeight.constant = 104*(additem.count)+270;
        _tableHeight.constant=104*(additem.count);
    }
}
- (IBAction)continueClicked:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"EDITORDERCLICKED"];

    confromOrderComplete= [[[NSUserDefaults standardUserDefaults]valueForKey:@"orderComplete"]boolValue];
    NSString *isAddress=[NSString stringWithFormat:@"%@",[[NSUserDefaults standardUserDefaults]valueForKey:@"selectedaddress"]];
    if(![isAddress  isEqualToString:@"(null)"])
    {
        [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"orderComplete"];
        if([additem count]>0)
        {
            
            [self callapi];
        }
        else
        {
            UIAlertController * alert=   [UIAlertController
                                          alertControllerWithTitle:@"No Item Available"
                                          message:@"Click Back To Add Item"
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
    else
    {
        if([additem count]>0)
        {
            
            StorelocatorViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"StorelocatorViewController"];
            NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
            [userDefaults setObject:additem forKey:@"additem"];
            [userDefaults setObject:costitem forKey:@"costitem"];
            [userDefaults setObject:Descitem forKey:@"Descitem"];
            [userDefaults setObject:itemImageUrl forKey:@"itemImageUrl"];
            [userDefaults setObject:quantityArray forKey:@"quantityArray"];
            [userDefaults setObject:priceArray forKey:@"priceArray"];
            [userDefaults setObject:itemId forKey:@"itemId"];
            [userDefaults setObject:countitem forKey:@"countitem"];
            [userDefaults setObject:sizeArray forKey:@"sizeArray"];
            [userDefaults synchronize];
            
            
            
            [self.navigationController pushViewController:add animated:YES];
        }
        else
        {
            UIAlertController * alert=   [UIAlertController
                                          alertControllerWithTitle:@"No Item Available"
                                          message:@"Click Back To Add Item"
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
    
}

#pragma mark -------- UITableView Delegates -------------

//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    return 88;
//}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    NSInteger countNumber= [additem count];
    [[NSUserDefaults standardUserDefaults]setValue:[NSString stringWithFormat:@"%ld",(long)countNumber] forKey:@"orderCount"];
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    bottom1.orderCount.layer.borderWidth=.5f;
    bottom1.orderCount.layer.borderColor=[UIColor lightGrayColor].CGColor;
    bottom1.orderCount.layer.cornerRadius = bottom1.orderCount.frame.size.width / 2;
    bottom1.orderCount.clipsToBounds = YES;
    bottom1.orderCount.text=[NSString stringWithFormat:@"%ld",(long)countNumber];
    
    return countNumber;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    OrderPageTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"OrderPageTableViewCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"OrderPageTableViewCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
    cell.singleItemCost.text=[NSString stringWithFormat:@"$%@",[costitem objectAtIndex:indexPath.row]];
    
    cell.itemDesc.text=[NSString stringWithFormat:@"%@",[Descitem objectAtIndex:indexPath.row]];
    cell.quantityText.text=[quantityArray objectAtIndex:indexPath.row];
    
    cell.price.text=[priceArray objectAtIndex:indexPath.row];
    
    cell.quantityText.delegate = self;
    cell.quantityText.textAlignment = NSTextAlignmentCenter;
    
    cell.orderCounting.layer.borderWidth=.5f;
    cell.orderCounting.layer.borderColor=[UIColor colorWithRed:0.486 green:0.471 blue:0.498 alpha:1].CGColor;
    cell.itemOrderNmae.textColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    cell.itemOrderNmae.text=[additem objectAtIndex:indexPath.row];
    
    cell.orderCounting.text=[countitem objectAtIndex:indexPath.row];
    cell.itemOrderNmae.numberOfLines = 0;
    cell.itemSize.text=[sizeArray objectAtIndex:indexPath.row];
    cell.itemOrderNmae.adjustsFontSizeToFitWidth = true;
    cell.orderpagecellDelegate=self;
    
    
    if(indexPath.row%2 == 0)
    {
        
        cell.backgroundColor=[UIColor clearColor];
    }
    else
    {
        cell.backgroundColor = [UIColor colorWithRed:0.969 green:0.941 blue:0.902 alpha:1]; /*#f7f0e6*/
        
        
    }
    
    
    
    return cell;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */



- (IBAction)addMore:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"EDITORDERCLICKED"];

    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"orderComplete"];
    additem=nil;
    costitem=nil;
    priceArray=nil;
    
    quantityArray=nil;
    countitem=nil;
    Descitem=nil;
    
    MenuViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
    NSInteger countNumber= [additem count];
    [[NSUserDefaults standardUserDefaults]setValue:[NSString stringWithFormat:@"%ld",(long)countNumber] forKey:@"orderCount"];
    
    [self.navigationController pushViewController:add animated:NO];
    
    
    
}



-(void)callapi
{
    
    NSDictionary *param=[NSDictionary dictionaryWithObjectsAndKeys:@"-1",@"orderNumber",@"123",@"customerId",@"yes",@"customerName",@"8",@"companyID", nil];
    NSLog(@"param==%@",param);
    
    
    NSString *stringUrl=@"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/createorder";
    NSLog(@" string url ====%@",stringUrl);
    NSString *encoded=[stringUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    manager.requestSerializer =requestSerializer;
    
    [manager POST:encoded parameters:param success:^(AFHTTPRequestOperation *operation, id responseObject)
     
     
     {
         NSLog(@"JSON:==%@",responseObject);
         NSString *orderNumberstr=[responseObject objectForKey:@"message"];
         orderNumber = [[orderNumberstr componentsSeparatedByCharactersInSet:
                         [[NSCharacterSet characterSetWithCharactersInString:@"0123456789"]
                          invertedSet]]
                        componentsJoinedByString:@""];
         NSLog(@"orderNumber==%@",orderNumber);
         [self saveOrderHistory];
         
     }
          failure:^(AFHTTPRequestOperation *operation,NSError *error)
     {
         
         UIAlertController * alert=   [UIAlertController
                                       alertControllerWithTitle:@"Failure"
                                       message:[error localizedDescription]
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
     }];
    
    
}
-(void)saveOrderHeaderTable
{
    
    AppDelegate *appDelegate =
    [[UIApplication sharedApplication] delegate];
    
    NSManagedObjectContext *context2 =
    [appDelegate managedObjectContext];
    NSDate* now = [NSDate date];
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    NSDateComponents *dateComponents = [gregorian components:(NSHourCalendarUnit  | NSMinuteCalendarUnit | NSSecondCalendarUnit) fromDate:now];
    NSInteger hour = [dateComponents hour];
    NSInteger minute = [dateComponents minute];
    NSInteger second = [dateComponents second];
    NSString *timeString = [NSString stringWithFormat:@"%02ld:%02ld:%02ld", (long)hour, (long)minute, (long)second];
    NSLog(@"current time==%@", timeString);
    
    NSCalendar *calendar = [[NSCalendar alloc]
                            initWithCalendarIdentifier:NSGregorianCalendar];
    NSCalendarUnit units = NSYearCalendarUnit | NSMonthCalendarUnit | NSDayCalendarUnit;
    NSDateComponents *components = [calendar components:units fromDate:now];
    
    NSLog(@"Day: %ld", (long)[components day]);
    NSLog(@"Month: %ld", (long)[components month]);
    NSLog(@"Year: %ld", (long)[components year]);
    NSString *currentDate=[NSString stringWithFormat:@"%ld/%ld/%ld",(long)[components day],(long)[components month],(long)[components year]];
    NSLog(@"current date==%@",currentDate);
    OrderHeaderTable *addHeaderData = [NSEntityDescription
                                       insertNewObjectForEntityForName:@"OrderHeaderTable"
                                       inManagedObjectContext:context2];
    addHeaderData.itemOrderDate=currentDate;
    addHeaderData.itemOrderStatus=@"Processing";
    //addHeaderData.itemorderStoreId=@"12";
    addHeaderData.itemorderStoreId=[[NSUserDefaults standardUserDefaults]valueForKey:@"storeID"];
    // addHeaderData.itemorderTime=timeString;
    addHeaderData.itemorderTime= [NSString stringWithFormat:@"%@",[[NSUserDefaults standardUserDefaults]valueForKey:@"selectedCity"]];
    [[NSUserDefaults standardUserDefaults]valueForKey:@"selectedCity"];
    addHeaderData.itemOrderTotalPrice=[NSString stringWithFormat:@"%@",_totalCost.text];
    //
    addHeaderData.itemOrderNumber=orderNumber;
    
    NSError *error2;
    if (![context2 save:&error2])
    {
        NSLog(@"Whoops, couldn't save: %@", [error2 localizedDescription]);
    }
    //NSLog(@"savetag==%@",addOrder.description);
    
    
    NSLog(@"context==%@",context2);
    
    
    
    NSFetchRequest *fetchRequest2 = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity2 = [NSEntityDescription entityForName:@"OrderHeaderTable"
                                               inManagedObjectContext:context2];
    [fetchRequest2 setEntity:entity2];
    NSError *error3;
    NSArray *fetchedObjects2 = [context2 executeFetchRequest:fetchRequest2 error:&error3];
    int count=0;
    
    for (OrderHeaderTable *info in fetchedObjects2)
    {
        NSLog(@"itemOrderDate: %@", info.itemOrderDate);
        NSLog(@"itemOrderNumber: %@", info.itemOrderNumber);
        NSLog(@"itemOrderStatus: %@", info.itemOrderStatus);
        NSLog(@"itemorderStoreId: %@", info.itemorderStoreId);
        NSLog(@"itemorderStoreId: %@", info.itemorderTime);
        NSLog(@"itemOrderTotalPrice: %@", info.itemOrderTotalPrice);
        
        NSLog(@"count==%i",++count);
    }
    
    
}
-(void)saveOrderHistory
{
    [self saveOrderHeaderTable];
    AppDelegate *appDelegate =
    [[UIApplication sharedApplication] delegate];
    
    NSManagedObjectContext *context =
    [appDelegate managedObjectContext];
    
    
    
    
    
    
    for(int i=0;i<[costitem count];i++)
    {
        
        
        
        
        OrderHistory *addOrder = [NSEntityDescription
                                  insertNewObjectForEntityForName:@"OrderHistory"
                                  inManagedObjectContext:context];
        addOrder.itemName=[additem objectAtIndex:i];
        addOrder.itemCost=[costitem objectAtIndex:i];
        addOrder.itemDesc=[Descitem objectAtIndex:i];
        addOrder.itemImageurl=[itemImageUrl objectAtIndex:i];
        addOrder.itemQuantity=[quantityArray objectAtIndex:i];
        addOrder.itemTotalCost=[priceArray objectAtIndex:i];
        addOrder.itemID=[NSString stringWithFormat:@"%@",[itemId objectAtIndex:i]];
        addOrder.itemOrderNumber=orderNumber;
        addOrder.itemCount=[countitem objectAtIndex:i];
        addOrder.itemSize=[sizeArray objectAtIndex:i];
        NSError *error;
        if (![context save:&error])
        {
            NSLog(@"Whoops, couldn't save: %@", [error localizedDescription]);
        }    NSLog(@"savetag==%@",addOrder.description);
        
        
        NSLog(@"context==%@",context);
    }
    
    
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"OrderHistory"
                                              inManagedObjectContext:context];
    [fetchRequest setEntity:entity];
    NSError *error;
    NSArray *fetchedObjects1 = [context executeFetchRequest:fetchRequest error:&error];
    int count=0;
    
    for (OrderHistory *info in fetchedObjects1)
    {
        NSLog(@"itemOrderNumber: %@", info.itemOrderNumber);
        NSLog(@"itemName: %@", info.itemName);
        NSLog(@"itemCost: %@", info.itemCost);
        NSLog(@"itemDesc: %@", info.itemDesc);
        NSLog(@"itemQuantity: %@", info.itemQuantity);
        NSLog(@"itemTotalCost: %@", info.itemTotalCost);
        NSLog(@"itemID: %@", info.itemID);
        NSLog(@"itemImageurl: %@", info.itemImageurl);
        NSLog(@"itemSize: %@", info.itemSize);
        NSLog(@"count==%i",++count);
    }
    PaymentScreenViewController *psCtrller = [[PaymentScreenViewController alloc]initWithNibName:@"PaymentScreenViewController" bundle:nil];
    [[NSUserDefaults standardUserDefaults]setObject:_totalCost.text forKey:@"totalcost"];
    //[[NSUserDefaults standardUserDefaults]setObject:_showAddress.text forKey:@"showAddress"];
    
    [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"selectedaddress"];
    [self.navigationController pushViewController:psCtrller animated:YES];
    //    OrderhistoryViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"OrderhistoryViewController"];
    //
    //
    //    [self.navigationController pushViewController:add animated:YES];
    
}
- (IBAction)continueOrder:(id)sender
{
    
    //    if([additem count]>0)
    //    {
    //
    //        StorelocatorViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"StorelocatorViewController"];
    //        NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    //        [userDefaults setObject:additem forKey:@"additem"];
    //        [userDefaults setObject:costitem forKey:@"costitem"];
    //        [userDefaults setObject:Descitem forKey:@"Descitem"];
    //        [userDefaults setObject:itemImageUrl forKey:@"itemImageUrl"];
    //        [userDefaults setObject:quantityArray forKey:@"quantityArray"];
    //        [userDefaults setObject:priceArray forKey:@"priceArray"];
    //        [userDefaults setObject:itemId forKey:@"itemId"];
    //        [userDefaults setObject:countitem forKey:@"countitem"];
    //        [userDefaults synchronize];
    //
    //
    //
    //        [self.navigationController pushViewController:add animated:YES];
    //    }
    //    else
    //    {
    //        UIAlertController * alert=   [UIAlertController
    //                                      alertControllerWithTitle:@"No Item Available"
    //                                      message:@"Click Back To Add Item"
    //                                      preferredStyle:UIAlertControllerStyleAlert];
    //        UIAlertAction* yesButton = [UIAlertAction
    //                                    actionWithTitle:@"OK"
    //                                    style:UIAlertActionStyleDefault
    //                                    handler:^(UIAlertAction * action)
    //                                    {
    //                                        //Handel your yes please button action here
    //                                        [alert dismissViewControllerAnimated:YES completion:nil];
    //
    //                                    }];
    //        [alert addAction:yesButton];
    //
    //        [self presentViewController:alert animated:YES completion:nil];    }
    
    
}

#pragma mark -------- UITextField Delegates -------------



- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    
    UIToolbar* numberToolbar = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 50)];
    numberToolbar.barStyle = UIBarStyleBlackTranslucent;
    numberToolbar.items = [NSArray arrayWithObjects:
                           [[UIBarButtonItem alloc]initWithTitle:@"Cancel" style:UIBarButtonItemStyleBordered target:self action:@selector(cancelNumberPad:)],
                           [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil],
                           [[UIBarButtonItem alloc]initWithTitle:@"Done" style:UIBarButtonItemStyleDone target:self action:@selector(doneWithNumberPad:)],
                           nil];
    [numberToolbar sizeToFit];
    textField.inputAccessoryView = numberToolbar;
    numberToolbar.barTintColor=[UIColor colorWithRed:0.969 green:0.941 blue:0.902 alpha:1]; /*#f7f0e6*/
    CGPoint scrollPoint=CGPointMake(0, textField.frame.origin.y+120);
    
    [_scrollView setContentOffset:scrollPoint animated:YES];
    
    
    return YES;
}
-(void)cancelNumberPad:(id)sender
{
    [self.view endEditing:YES];
}
-(void)doneWithNumberPad:(id)sender
{
    NSLog(@"Done Clicked.");
    [self.view endEditing:YES];
}
- (void)textFieldDidEndEditing:(UITextField *)quantityText
{
    CGPoint point = [quantityText convertPoint:quantityText.bounds.origin toView:self.orderTableView];
    
    NSIndexPath *indexPath = [self.orderTableView indexPathForRowAtPoint:point];
    if (indexPath)
    {
        OrderPageTableViewCell *cell = (OrderPageTableViewCell*)[self.orderTableView cellForRowAtIndexPath:indexPath];
        
        
        if([quantityText.text isEqual:@""])
        {
            NSLog(@"%@",quantityText.text);
        }
        
        else
        {
            int index_path=[cell.orderCounting.text intValue];
            int myindex=index_path-1;
            NSLog(@"%d",myindex);
            NSString *strindex=[NSString stringWithFormat:@"%d",myindex];
            NSUserDefaults *myIndex=[NSUserDefaults standardUserDefaults];
            [myIndex setValue:strindex forKey:@"myindex"];
            [myIndex synchronize];
            
            NSLog(@"%@",quantityText.text);
            NSString *myValu=[NSString stringWithString:cell.singleItemCost.text];
            myValu=[myValu substringFromIndex:1];
            NSLog(@"%@",myValu);
            float  intCost1=[myValu floatValue];
            int quantity2=[quantityText.text intValue];
            NSString *strquantity=[NSString stringWithFormat:@"%d",quantity2];
            
            float totalCost1=intCost1*quantity2;
            cell.price.text=[NSString stringWithFormat:@"$%.02f",totalCost1];
            
            NSUserDefaults *finalCostSingleItem=[NSUserDefaults standardUserDefaults];
            [finalCostSingleItem setValue:[NSString stringWithFormat:@"$%.02f",totalCost1] forKey:@"finalCostSingleItem"];
            [finalCostSingleItem synchronize];
            [quantityArray replaceObjectAtIndex:myindex withObject:strquantity];
            NSUserDefaults *quantityValue=[NSUserDefaults standardUserDefaults];
            [quantityValue setValue:strquantity forKey:@"Editquantity"];
            [quantityValue synchronize];
            //    something = [[OrderPageViewController alloc] init];
            [self somethingWithParam];
            
            
            [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"EDITCLICKED"];
            [quantityText resignFirstResponder];
        }
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    return YES;
}

- (IBAction)orderConfirmedAction:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"EDITORDERCLICKED"];

    
    additem=nil;
    costitem=nil;
    priceArray=nil;
    
    quantityArray=nil;
    countitem=nil;
    Descitem=nil;
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"orderComplete"];
    
    MenuViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
    
    NSInteger countNumber= [additem count];
    [[NSUserDefaults standardUserDefaults]setValue:[NSString stringWithFormat:@"%ld",(long)countNumber] forKey:@"orderCount"];
    [self.navigationController pushViewController:add animated:NO];
    
}
@end
