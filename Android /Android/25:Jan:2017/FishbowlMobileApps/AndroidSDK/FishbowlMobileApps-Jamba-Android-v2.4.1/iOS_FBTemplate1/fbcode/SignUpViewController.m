//
//  ViewController.m
//  FBTemplate2
//
//  Created by HARSH on 10/12/15.
//  Copyright © 2015 Fishbowl. All rights reserved.
//

#import "SignUpViewController.h"
#import "LoginViewController.h"
#import "RegisterViewController.h"
#import "LeftmenuCustomView.h"
#import "LeftmenuTableViewCell.h"
#import "CustomBottomBarView.h"
#import "SignUpViewController.h"
#import "MenuViewController.h"
#import "StorelocatorViewController.h"
#import "OfferViewController.h"
#import "OrderPageViewController.h"
#import "User.h"
#import "AppDelegate.h"
@interface SignUpViewController ()<LeftMenudelegate,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate,BottomDelegate>
{
    LeftmenuCustomView *customtag1;
    CALayer *TopBorder;
    NSArray *leftMenuarray;
    NSArray *leftmenuImageArray;
    BOOL menuClicked;
    CustomBottomBarView *bottombar;
    
    AppDelegate *sharedAppDel;
    NSString *userstr;

}
@property (weak, nonatomic) IBOutlet UILabel *welcomezpizza;
@property (weak, nonatomic) IBOutlet UISegmentedControl *siginInSegment;
@property (weak, nonatomic) IBOutlet UIView *subView1;
@property (weak, nonatomic) IBOutlet UIView *borderViee1;
@property (weak, nonatomic) IBOutlet UIView *borderView2;
@property (weak, nonatomic) IBOutlet UIView *mainView3;
@property (weak, nonatomic) IBOutlet UIView *borderView3;
@property (weak, nonatomic) IBOutlet UIView *border1View;

@property (weak, nonatomic) IBOutlet UIImageView *menuImageArraw;
@property (weak, nonatomic) IBOutlet UIImageView *menuIconImage;
@property (weak, nonatomic) IBOutlet UILabel *pizzaNamelabel;
@property (weak, nonatomic) IBOutlet UIImageView *storeLocatoricon;
@property (weak, nonatomic) IBOutlet UILabel *storelocatorLabel;
@property (weak, nonatomic) IBOutlet UIImageView *orderIcon;
@property (weak, nonatomic) IBOutlet UILabel *orderLabel;
@property (weak, nonatomic) IBOutlet UIImageView *offerIcon;
@property (weak, nonatomic) IBOutlet UILabel *offerLabel;


@end

@implementation SignUpViewController
-(void)joinClicked
{
    NSLog(@"join click");
}
- (IBAction)signinSignUp:(UISegmentedControl *)sender
{
    switch (sender.selectedSegmentIndex) {
        case 0:
        {
            LoginViewController *tologin=[self.storyboard instantiateViewControllerWithIdentifier:@"LoginViewController"];
            
            
            [self.navigationController pushViewController:tologin animated:YES];
        }
            break;
        case 1:
        {
            RegisterViewController *toreg=[self.storyboard instantiateViewControllerWithIdentifier:@"RegisterViewController"];
            
            
            [self.navigationController pushViewController:toreg animated:YES];
        }break;
       
            
        default:
            break;
    }

}

- (IBAction)myorderAction:(id)sender
{

    [_orderIcon setImage:[UIImage imageNamed:@"my_Menu_Order_red.png"]];
    _orderLabel.textColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    [_offerIcon setImage:[UIImage imageNamed:@"my_Menu_Offers.png"]];
    _offerLabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    [_storeLocatoricon setImage:[UIImage imageNamed:@"locater.png"]];
    _storelocatorLabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    [_menuIconImage setImage:[UIImage imageNamed:@"my_Menu.png"]];
    _pizzaNamelabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    NSInteger countNumber=[[[NSUserDefaults standardUserDefaults]valueForKey:@"orderCount"]integerValue];
    if (countNumber>=1)
    {
        OrderPageViewController *toOrder=[self.storyboard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
        
        
        [self.navigationController pushViewController:toOrder animated:YES];
    }
    else
    {
        MenuViewController *pizzamenu=[self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
        //self.navigationController.navigationBarHidden=YES;
        
        [self.navigationController pushViewController:pizzamenu animated:YES];
    }
    
    
}
- (IBAction)storelocatorAction:(id)sender
{
    
    [_storeLocatoricon setImage:[UIImage imageNamed:@"locater_red.png"]];
    _storelocatorLabel.textColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    
    [_orderIcon setImage:[UIImage imageNamed:@"my_Menu_Order.png"]];
    _orderLabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    [_offerIcon setImage:[UIImage imageNamed:@"my_Menu_Offers.png"]];
    _offerLabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    
    [_menuIconImage setImage:[UIImage imageNamed:@"my_Menu.png"]];
    _pizzaNamelabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    StorelocatorViewController *toStore=[self.storyboard instantiateViewControllerWithIdentifier:@"StorelocatorViewController"];
   
      [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"frommenuToStore"];
    [self.navigationController pushViewController:toStore animated:YES];
    
}
- (IBAction)pizzaMenuActin:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"fromMenuToPizza"];
    [_menuIconImage setImage:[UIImage imageNamed:@"my_Menu_red.png"]];
    _pizzaNamelabel.textColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    
    
    [_offerIcon setImage:[UIImage imageNamed:@"my_Menu_Offers.png"]];
    _offerLabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    [_storeLocatoricon setImage:[UIImage imageNamed:@"locater.png"]];
    _storelocatorLabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    
    [_orderIcon setImage:[UIImage imageNamed:@"my_Menu_Order.png"]];
    _orderLabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/

    MenuViewController *pizzamenu=[self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
    //self.navigationController.navigationBarHidden=YES;
    
    [self.navigationController pushViewController:pizzamenu animated:YES];
    
}
- (IBAction)myOffersAction:(id)sender
{
    [_offerIcon setImage:[UIImage imageNamed:@"my_Menu_Offers_red.png"]];
    _offerLabel.textColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    
    [_storeLocatoricon setImage:[UIImage imageNamed:@"locater.png"]];
    _storelocatorLabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    
    [_orderIcon setImage:[UIImage imageNamed:@"my_Menu_Order.png"]];
    _orderLabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    [_menuIconImage setImage:[UIImage imageNamed:@"my_Menu.png"]];
    _pizzaNamelabel.textColor=[UIColor colorWithRed:0.627 green:0.514 blue:0.361 alpha:1]; /*#a0835c*/
    OfferViewController *toOffer=[self.storyboard instantiateViewControllerWithIdentifier:@"OfferViewController"];
     [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"tooffer"];
    
    [self.navigationController pushViewController:toOffer animated:YES];
}
-(void)righrMenuClicked
{
    
    
}


//-(void)callLine
//{
//   TopBorder = [CALayer layer];
//    TopBorder.frame = CGRectMake(0.0f, _borderView3.frame.size.height,_mainView3.frame.size.width, 1.0f);
//    TopBorder.backgroundColor = [UIColor lightGrayColor].CGColor;
//}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
//    menuClicked=NO;
//    [self callLine];
//    [_borderView2.layer addSublayer:TopBorder];
//    [self callLine];
//    [_borderViee1.layer addSublayer:TopBorder];
//     [self callLine];
//    [_borderView3.layer addSublayer:TopBorder];
//     [self callLine];
//   [_border1View.layer addSublayer:TopBorder];
   
}
-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:YES];
    self.navigationController.navigationBarHidden=NO;
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    //    bottom1.bottomDelegate=self;
    bottom1.hidden=NO;
    
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:NO];
    
    sharedAppDel = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [sharedAppDel disableGesture];
    
    self.navigationController.navigationBarHidden=YES;
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    //    bottom1.bottomDelegate=self;
    bottom1.hidden=YES;
    //menuClicked=NO;
    
    
    BOOL test = [[[NSUserDefaults standardUserDefaults] objectForKey:@"firstTimeLogin"] boolValue];
    
    if (test)
    {
        [self showUser];
        _siginInSegment.hidden=YES;
        _welcomezpizza.text=[NSString stringWithFormat:@"Hi %@,Welcome  To Zpizza",userstr];
        _welcomezpizza.adjustsFontSizeToFitWidth=YES;
        _welcomezpizza.hidden=NO;

    }
    else
    {
        _siginInSegment.hidden=NO;
        _welcomezpizza.hidden=YES;
    }
    //Add bottombar
//    bottombar=[CustomBottomBarView customBottomBarView];
//    [bottombar setFrame:CGRectMake(bottombar.frame.origin.x, self.view.frame.size.height-40, self.view.frame.size.width, bottombar.frame.size.height)];
//    bottombar.bottomDelegate=self;
   
}
-(void)showUser
{
    
    AppDelegate *appDelegate =
    [[UIApplication sharedApplication] delegate];
    
    NSManagedObjectContext *context2 =
    [appDelegate managedObjectContext];
    
    
    
    
    NSFetchRequest *fetchRequest2 = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity2 = [NSEntityDescription entityForName:@"User"
                                               inManagedObjectContext:context2];
    [fetchRequest2 setEntity:entity2];
    NSError *error3;
    NSArray *Userdata = [context2 executeFetchRequest:fetchRequest2 error:&error3];
    int count=0;
    NSLog(@"orderdata==%@",Userdata);
    
    
    for (User *info in Userdata)
    {
        
        userstr=info.user_name;
        NSLog(@"count==%i",++count);
    }
    
    
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 55;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [leftMenuarray count];
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    LeftmenuTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"LeftmenuTableViewCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"LeftmenuTableViewCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
    cell.leftMenuItem.font = [UIFont fontWithName:@"Helvetica-Bold" size:13];
    cell.leftMenuItem.text=[leftMenuarray objectAtIndex:indexPath.row];
    cell.leftMenuItem.textColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1];
    cell.emnuitemImage.image=[UIImage imageNamed:[leftmenuImageArray objectAtIndex:indexPath.row]];
    cell.emnuitemImage.contentMode=UIViewContentModeScaleToFill;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 0)
    {
        
        
        
    }
    
    else if (indexPath.row == 1)
    {
        
        [customtag1 removeFromSuperview];
       
        //tabView2.hidden=NO;
    }
    
    else if (indexPath.row == 2)
    {
        
    }
    
    else if (indexPath.row == 3)
    {
//        OrderPageViewController *toViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
//        //toViewController.selectedViewController=[toViewController.viewControllers objectAtIndex:1];
//        [self.navigationController pushViewController:toViewController animated:NO];
    }
    else if (indexPath.row == 4)
        
    {
        
    }
    else if (indexPath.row == 5)
        
    {
        
    }
    else if (indexPath.row == 6)
        
    {
        
    }
    else if (indexPath.row == 7)
        
    {
     
    }
    else
    {
        
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
