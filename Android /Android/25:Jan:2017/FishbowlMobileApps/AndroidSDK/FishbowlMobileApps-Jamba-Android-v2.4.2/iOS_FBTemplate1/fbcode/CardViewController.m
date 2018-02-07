//
//  slideMenuViewController.m
//  ChallengerApp
//
//  Created by Alobha Technologies on 01/06/15.
//  Copyright (c) 2015 Alobha Technologies. All rights reserved.
//


#import "CardViewController.h"
#import "CardTableViewCell.h"
#import "OrederViewController.h"
#import "MenuViewController.h"
#import "OrderPageViewController.h"
#import "OrderhistoryViewController.h"
@interface CardViewController ()
{
    NSArray *cardName;
    BOOL orderClicked;
}



@end

@implementation CardViewController
- (IBAction)eGiftAction:(id)sender {
}
- (IBAction)locateStoreAction:(id)sender {
}
- (IBAction)menuActin:(id)sender
{
    MenuViewController *order=[self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
    self.navigationController.navigationBarHidden=YES;
    self.navigationController.navigationItem.hidesBackButton=YES;
    [self.navigationController pushViewController:order animated:NO];
}
- (IBAction)exploreAction:(id)sender {
}
- (IBAction)offersAction:(id)sender {
}

- (IBAction)orderHistoryAction:(id)sender
{
    OrderhistoryViewController *tohistotyview=[self.storyboard instantiateViewControllerWithIdentifier:@"OrderhistoryViewController"];
    [self.navigationController pushViewController:tohistotyview animated:NO];
}
- (IBAction)myOrderAction:(id)sender {
    orderClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"orderClicked"]boolValue];
    if(orderClicked==YES)
    {
    OrderPageViewController *order=[self.storyboard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
        [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"comeFromHistory"];
    [self.navigationController pushViewController:order animated:NO];
    }
    else
    {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"No Order Available"
                                      message:@"Please click on  Menu to make order"
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

- (void)viewDidLoad {
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.hidesBackButton = NO;
  
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1];

    [super viewDidLoad];
    cardName=[NSArray arrayWithObjects:@"E-GIFT",@"LOCATE A STORE",@"MENU",@"EXPLORE",@"OFFERS",@"NUTRITION CALCULATER", nil];
 }
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
  
    
    self.navigationController.navigationBarHidden = NO;
    self.navigationItem.hidesBackButton = NO;
    
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1];
    self.navigationItem.title=@"MENU";
    self.navigationController.navigationBar.titleTextAttributes=@{NSForegroundColorAttributeName: [UIColor whiteColor]};
    self.navigationController.navigationBar.tintColor=[UIColor whiteColor];;
  
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    self.navigationController.navigationBarHidden = YES;
    self.navigationItem.hidesBackButton = YES;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}




-(NSInteger)tableView: (UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [cardName count];
}


-(UITableViewCell *)tableView : (UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CardTableViewCell *Cell = [tableView dequeueReusableCellWithIdentifier:@"SliderCell"];
    
        Cell.selectionStyle = UITableViewCellSelectionStyleNone;
    Cell.cardNames.textColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1];
    Cell.cardNames.text=[cardName objectAtIndex:indexPath.row];
    return Cell ;
}


-(void)tableView :(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath

{
    if (indexPath.row == 0)
    {
     
    }
    
    else if (indexPath.row == 1)
    {
 
    }
    
    else if (indexPath.row == 2)
    {
  
    }
    
    else if (indexPath.row == 3)
    {
    }
    else
    {
        
    }

    
}




- (IBAction)fblogin:(id)sender
{
    OrederViewController *order=[self.storyboard instantiateViewControllerWithIdentifier:@"OrederViewController"];
    [self.navigationController pushViewController:order animated:NO];
}

- (IBAction)emailLogin:(id)sender {
}
@end
