

//  OrderPageViewController.m
//  taco2
//
//  Created by HARSH on 02/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "OrderPageViewController.h"
#import "OrderPageTableViewCell.h"
#import "MenuViewController.h"
#import "AFHTTPRequestOperationManager.h"
#import "itemViewController.h"
#import "ModelClass.h"
//#import "StorelocatorViewController.h"
//#import "OrderCompleteViewController.h"
//#import "PaymentScreenViewController.h"


@interface OrderPageViewController ()<UITableViewDataSource,UITableViewDelegate,OrderPageCellDelegate,UITextFieldDelegate>
{
    NSMutableArray *totalpriceArray;
    NSMutableDictionary *itemDict;
    ModelClass * obj;
   
    
}
@property (weak, nonatomic) IBOutlet UIButton *btnAddmoreOutlet;
@property (weak, nonatomic) IBOutlet UIButton *btnCancelOutlet;
@property (weak, nonatomic) IBOutlet UIImageView *imgBG;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgTop;
@property (weak, nonatomic) IBOutlet UIView *viewTotalPrice;
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
@property (weak, nonatomic) IBOutlet UIButton *addMoreOutlet;

@property (weak, nonatomic) IBOutlet UIView *addressViewOutlet;
@property (weak, nonatomic) IBOutlet UIButton *orderConfirmedOutlet;
- (IBAction)orderConfirmedAction:(id)sender;

@end

@implementation OrderPageViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    obj=[ModelClass sharedManager];
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
   
    
    // header image
    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
    NSURL *url3 = [NSURL URLWithString:str3];
    [self.imgTop sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self.imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    totalpriceArray=[[NSMutableArray alloc]init];
   
    _btnCancelOutlet.backgroundColor= [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    _btnAddmoreOutlet.backgroundColor= [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];

}

- (IBAction)backClicked:(id)sender
{
//    BOOL isFromStore=NO;
//    isFromStore=[[[NSUserDefaults standardUserDefaults]valueForKey:@"isFromrderComplete"]boolValue];
//    if (isFromStore)
//    {
//        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"fromSTOREToOrder"];
       [self.navigationController popViewControllerAnimated:YES];
//    }
//    else
//    {
//    NSInteger numberOfViewControllers = self.navigationController.viewControllers.count;
//    
//    for (int i=0; i<numberOfViewControllers; i++) {
//        
//        UIViewController *controller = (UIViewController*)[self.navigationController.viewControllers objectAtIndex:i];
//        
//        if ([controller isKindOfClass:[MenuViewController class]])
//        {
//            [self.navigationController popToViewController:controller animated:YES];
//        }
//        
//    }
//    
//    }


    
   
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    _deliveryLabel.hidden=YES;
    _showDelivery.hidden=YES;
   
    _headerView.layer.borderWidth=0.5f;
    _headerView.layer.borderColor=[UIColor blackColor].CGColor;
  
    
 

    
    
    _addressViewOutlet.hidden=YES;
    BOOL FromHomeToOrder=NO;
    
    FromHomeToOrder=[[[NSUserDefaults standardUserDefaults]valueForKey:@"FromHomeToOrder"]boolValue];
    if (FromHomeToOrder==YES)
    {
        
    }
    else
    {
        itemDict=[[NSMutableDictionary alloc]init];
        
        BOOL isFromorder = NO;
        if (isFromorder != [[[NSUserDefaults standardUserDefaults]valueForKey:@"isFromrderComplete"]boolValue]) {
            NSLog(@"fromOrder");
            _addressViewOutlet.hidden=NO;
            
            _showAddress.text = _strSelectAddress;
            _shoeCity.text = _strSelectCity;
        }
        else
        {
            
        }
        
        if (obj.itemDictArray.count==0)
        {
            _noItemOutlet.hidden=YES;
            //     _viewTotalPrice.hidden=YES;
        }
        else
        {
            _noItemOutlet.hidden=NO;
        }
        if (104*(obj.itemDictArray.count) <_tableHeight.constant)
        {
            
        }
        else
        {
            _viewHeight.constant = 104*(obj.itemDictArray.count)+270;
            _tableHeight.constant=104*(obj.itemDictArray.count);
        }
    }
    [self calCulatePrice];
    
}

-(void)calCulatePrice
{
    float sum = 0;
    for (int i = 0; i < obj.itemDictArray.count; i++)
    {
        sum += [[[obj.itemDictArray objectAtIndex:i]valueForKey:@"iPrice"] floatValue];
    }
    
    _totalCost.text=[NSString stringWithFormat:@"$%.2f",(float)sum];
    [[NSUserDefaults standardUserDefaults]setValue:_totalCost.text forKey:@"totalcost"];
    //totalpriceArray = nil ;
}
- (IBAction)continueClicked:(id)sender
{
    BOOL isFromorder = NO;
    if (isFromorder != [[[NSUserDefaults standardUserDefaults]valueForKey:@"isFromrderComplete"]boolValue])
    {
        if(obj.itemDictArray.count>0)
        {
            NSLog(@"fromOrder");
            NSLog(@"price==%@",_totalCost.text);
            NSString *strPrice=_totalCost.text;
            if ([strPrice isEqualToString:@"$0.00"]) {
                UIAlertController * alert=   [UIAlertController
                                              alertControllerWithTitle:@"Add Item Quantity"
                                              message:nil
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
            else
            {
//            PaymentScreenViewController *shoMenu=[[PaymentScreenViewController alloc]initWithNibName:@"PaymentScreenViewController" bundle:nil];
//            shoMenu.strSelectAddress=_strSelectAddress;
//            shoMenu.strSelectCity=_strSelectCity;
//            totalpriceArray=nil;
//            mainDelegate.itemDictArray=[[NSMutableArray alloc]init];
//            
//            [self.navigationController pushViewController:shoMenu animated:YES];
            }
        }
        else
        {
            UIAlertController * alert=   [UIAlertController
                                          alertControllerWithTitle:@"No Item Available"
                                          message:@"Click back to add item"
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
    if(obj.itemDictArray.count>0)
    {
        [self calCulatePrice];
        NSLog(@"price==%@",_totalCost.text);
        NSString *strPrice=_totalCost.text;
        if ([strPrice isEqualToString:@"$0.00"]) {
            UIAlertController * alert=   [UIAlertController
                                          alertControllerWithTitle:@"Add Item Quantity"
                                          message:nil
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
        else
        {
//        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
//        StorelocatorViewController *shoMenu=(StorelocatorViewController*)[storyBoard instantiateViewControllerWithIdentifier:@"StorelocatorViewController"];
//        [self.navigationController pushViewController:shoMenu animated:YES];
        }
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
    NSLog(@"obj.itemDictArray.count==%lu",(unsigned long)obj.itemDictArray.count);
    
    return obj.itemDictArray.count;
    
}
- (IBAction)tapClickPromoCode:(id)sender
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert"
                                  message:@"No promocode available"
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

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    OrderPageTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"OrderPageTableViewCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"OrderPageTableViewCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
 
        cell.backgroundColor=[UIColor clearColor];
 
    cell.singleItemCost.text=[NSString stringWithFormat:@"$%@",[[obj.itemDictArray objectAtIndex:indexPath.row]valueForKey:@"icost"]];
    cell.itemDesc.text=[NSString stringWithFormat:@"%@",[[obj.itemDictArray objectAtIndex:indexPath.row]valueForKey:@"idesc"]];
    int indexPath4=(int)indexPath.row;
    cell.quantityText.text=[NSString stringWithFormat:@"%@",[[obj.itemDictArray objectAtIndex:indexPath.row]valueForKey:@"iQuantity"]];
    cell.itemDesc.numberOfLines=0;
    cell.quantityText.delegate = self;
    cell.quantityText.textAlignment = NSTextAlignmentCenter;

    cell.price.text=[NSString stringWithFormat:@"$%@",[[obj.itemDictArray objectAtIndex:indexPath.row]valueForKey:@"iPrice"]];
    cell.orderCounting.layer.borderWidth=.5f;
    cell.orderCounting.layer.borderColor=[UIColor colorWithRed:0.486 green:0.471 blue:0.498 alpha:1].CGColor;
    // cell.itemOrderNmae.textColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    cell.itemOrderNmae.text=[NSString stringWithFormat:@"%@",[[obj.itemDictArray objectAtIndex:indexPath.row]valueForKey:@"iname"]];
    
    cell.orderCounting.text=[NSString stringWithFormat:@"%d",indexPath4];
    cell.itemOrderNmae.numberOfLines = 0;
   cell.itemSize.text=[NSString stringWithFormat:@"%@",[[obj.itemDictArray objectAtIndex:indexPath.row]valueForKey:@"iSize"]];
    cell.itemOrderNmae.adjustsFontSizeToFitWidth = true;
    cell.orderpagecellDelegate=self;

cell.backgroundColor=[UIColor clearColor];


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
#pragma mark -------- UITextField Delegates -------------



- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    
    CGPoint point = [textField convertPoint:textField.bounds.origin toView:self.orderTableView];
    
    NSIndexPath *indexPath = [self.orderTableView indexPathForRowAtPoint:point];
    if (indexPath)
    {
        OrderPageTableViewCell *cell = (OrderPageTableViewCell*)[self.orderTableView cellForRowAtIndexPath:indexPath];
        
        
        if([textField.text isEqual:@""])
        {
            NSLog(@"%@",textField.text);
        }
        
        else
        {
            NSDictionary *myDict=[obj.itemDictArray objectAtIndex:indexPath.row];
            [myDict setValue:cell.quantityText.text forKey:@"iQuantity"];
            
            int index_path=[cell.orderCounting.text intValue];
            int myindex=index_path-1;
            NSLog(@"%d",myindex);
            int quantity2=[textField.text intValue];
            NSLog(@"%@",textField.text);
            NSString *myValu=[NSString stringWithString:cell.singleItemCost.text];
            myValu=[myValu substringFromIndex:1];
            NSLog(@"%@",myValu);
            float  intCost1=[myValu floatValue];
            float totalCost1=intCost1*quantity2;
            cell.price.text=[NSString stringWithFormat:@"$%.2f",totalCost1];
            [myDict setValue:[NSString stringWithFormat:@"%.2f",totalCost1] forKey:@"iPrice"];
            
            [self calCulatePrice];
            
        }
    }
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
            if (obj.itemDictArray.count!=0)
            {
                NSDictionary *myDict=[obj.itemDictArray objectAtIndex:indexPath.row];
                [myDict setValue:cell.quantityText.text forKey:@"iQuantity"];
                
                int index_path=[cell.orderCounting.text intValue];
                int myindex=index_path-1;
                NSLog(@"%d",myindex);
                int quantity2=[quantityText.text intValue];
                NSLog(@"%@",quantityText.text);
                NSString *myValu=[NSString stringWithString:cell.singleItemCost.text];
                myValu=[myValu substringFromIndex:1];
                NSLog(@"%@",myValu);
                float  intCost1=[myValu floatValue];
                float totalCost1=intCost1*quantity2;
                cell.price.text=[NSString stringWithFormat:@"$%.2f",totalCost1];
                [myDict setValue:[NSString stringWithFormat:@"%.2f",totalCost1] forKey:@"iPrice"];
                
                [self calCulatePrice];
            }
          
            
        }
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    return YES;
}

- (IBAction)addMore:(id)sender
{
    NSLog(@"");
    
    NSInteger numberOfViewControllers = self.navigationController.viewControllers.count;
    
    for (int i=0; i<numberOfViewControllers; i++) {
        
        UIViewController *controller = (UIViewController*)[self.navigationController.viewControllers objectAtIndex:i];
        
        if ([controller isKindOfClass:[MenuViewController class]])
        {
            [self.navigationController popToViewController:controller animated:YES];
        }
        
    }

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
//         orderNumber = [[orderNumberstr componentsSeparatedByCharactersInSet:
//                         [[NSCharacterSet characterSetWithCharactersInString:@"0123456789"]
//                          invertedSet]]
//                        componentsJoinedByString:@""];
//         NSLog(@"orderNumber==%@",orderNumber);
//       
         
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


- (IBAction)continueOrder:(id)sender
{
    
}

#pragma mark -------- UITextField Delegates -------------



- (IBAction)orderConfirmedAction:(id)sender
{
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:@"Do you want to cancel orders."
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                                  
                                  obj.itemDictArray=nil;
                                  obj.itemDictArray=[[NSMutableArray alloc]init];
                                  totalpriceArray=nil;
                                  
                                
                                  [self.navigationController popToRootViewControllerAnimated:YES];
                                  
                                  NSLog(@"sender : %@",sender);
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

//    mainDelegate.itemDictArray=nil;
//    mainDelegate.itemDictArray=[[NSMutableArray alloc]init];
//    totalpriceArray=nil;
//    
//    
//    
//    NSInteger numberOfViewControllers = self.navigationController.viewControllers.count;
//    
//    for (int i=0; i<numberOfViewControllers; i++) {
//        
//        UIViewController *controller = (UIViewController*)[self.navigationController.viewControllers objectAtIndex:i];
//        
//        if ([controller isKindOfClass:[MenuViewController class]])
//        {
//            [self.navigationController popToViewController:controller animated:YES];
//        }
//        
//    }
}
-(void)editOrderClicked:(id)sender
{
    
    NSLog(@"edit clicked");
    NSLog(@"sender : %@",sender);
    UIButton *btn = (UIButton *)sender;
    CGPoint point = [btn convertPoint:btn.bounds.origin toView:_orderTableView];
    NSIndexPath *indexPath = [_orderTableView indexPathForRowAtPoint:point];
    
    if (indexPath)
    {
        
        BOOL isFromorder = NO;
        if (isFromorder != [[[NSUserDefaults standardUserDefaults]valueForKey:@"isFromrderComplete"]boolValue]) {
            NSLog(@"fromOrder");
            
            NSInteger numberOfViewControllers = self.navigationController.viewControllers.count;
            
            for (int i=0; i<numberOfViewControllers; i++) {
                
                UIViewController *controller = (UIViewController*)[self.navigationController.viewControllers objectAtIndex:i];
                
                if ([controller isKindOfClass:[itemViewController class]])
                {
                    [self.navigationController popToViewController:controller animated:YES];
                }
                
            }
            
        }
        else
        {
            
            
            
            
            NSString *strQuantity=[[obj.itemDictArray objectAtIndex:indexPath.row]valueForKey:@"iQuantity"];
            
            ;
            obj.strQuantityGlobal=strQuantity;
            obj.indexGlobal=indexPath.row;
            [self.delegate ProductEdited];
            
            [self.navigationController popViewControllerAnimated:YES];
            
            
        }
        
        
    }
    else
    {
        
    }
    
}
-(void)removeOrderClicked:(id)sender
{
    
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:@"Do you want to remove"
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleCancel handler:^(UIAlertAction  *action) {
        
        NSLog(@"sender : %@",sender);
        _viewTotalPrice.hidden=YES;
        UIButton *btn = (UIButton *)sender;
        CGPoint point = [btn convertPoint:btn.bounds.origin toView:_orderTableView];
        NSIndexPath *indexPath = [_orderTableView indexPathForRowAtPoint:point];
        if (indexPath)
        {
            NSLog(@"indexPath : %ld",(long)indexPath.row);
            [obj.itemDictArray removeObjectAtIndex:indexPath.row];
            [_orderTableView reloadData];
            if (obj.itemDictArray.count==0) {
                
                _noItemOutlet.hidden=NO;
                
            }
            else
            {
                _noItemOutlet.hidden=YES;
            }
            [self calCulatePrice];
        }
        else
        {
            
        }
        
        _viewHeight.constant =_viewHeight.constant-104;
        _tableHeight.constant=_tableHeight.constant-104;
        
        if (obj.itemDictArray.count==0)
        {
            NSInteger numberOfViewControllers = self.navigationController.viewControllers.count;
            
            for (int i=0; i<numberOfViewControllers; i++) {
                
                UIViewController *controller = (UIViewController*)[self.navigationController.viewControllers objectAtIndex:i];
                
                if ([controller isKindOfClass:[MenuViewController class]])
                {
                    [self.navigationController popToViewController:controller animated:YES];
                }
                
            }
            
        }
        
        NSLog(@"asdhfgjk");
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
