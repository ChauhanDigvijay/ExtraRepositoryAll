//
//  ViewController.m
//  taco2
//
//  Created by HARSH on 17/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "MenuViewController.h"
#import "MenuTableViewCell.h"
#import "CardViewController.h"
#import "OrderPageViewController.h"
#import "MenuRightTableViewCell.h"
#import "MainViewController.h"
#import "CustomBottomBarView.h"
#import "MainViewController.h"
#import "OrederViewController.h"
#import "AFHTTPRequestOperationManager.h"
#import "UIImageView+AFNetworking.h"
#import "LeftmenuCustomView.h"
#import "LeftmenuTableViewCell.h"
#import "AppDelegate.h"
#import "User.h"
#import "UIImage+animatedGIF.h"
#import "StorelocatorViewController.h"
#import "OrderhistoryViewController.h"
#import "ViewItemViewController.h"
#import "SignUpViewController.h"
@interface MenuViewController ()<UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate,UITextFieldDelegate>
{
    AppDelegate *sharedAppDel;
    NSMutableArray *category;
    NSArray *pizzas;
    NSArray *TAKENBAKE;
    NSArray *RUSTICAFLATBREADS;
    NSArray *SALADS;
    NSArray *SANDWICHES;
    NSArray *PASTAS;
    NSArray *SHAREABLES;
    NSMutableArray *finalArray;
    NSMutableArray *images;
    UIImageView *imageView;
    CGFloat labelHeight;
    
    NSArray *leftMenuarray;
    NSArray *leftmenuImageArray;
    BOOL zpizzaCardClicked;
    BOOL isServer;
    NSMutableArray *itemCost;
    NSMutableArray *itemDesc;
    NSMutableArray *menuCatId;
    NSMutableArray *menuItemCatId;
    NSArray *myCatMenuItem;
    
    
    
    BOOL clickedRightMenu;
    NSString *userstr;
    BOOL showuserName;
    CGRect frame2;
    
    UITextField* textSearch;
    BOOL isSearch;
    //NSDictionary *mydict1;
    NSArray *filteredArr;
    UIView* view;
    
}
@property (nonatomic, strong) NSArray *searchResult;
@property (weak, nonatomic) IBOutlet UIView *searchView;

@property (weak, nonatomic) IBOutlet UITableView *searchTable;
@property (weak, nonatomic) IBOutlet UIImageView *loadingImage;

@property (weak, nonatomic) IBOutlet UITableView *tableViewMain;
@property (weak, nonatomic) IBOutlet UITableView *tabView2;
@property (strong, nonatomic) IBOutlet UILabel *showWeAreOfferingLabel;


@end

@implementation MenuViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    sharedAppDel = (AppDelegate*)[UIApplication sharedApplication].delegate;
    [sharedAppDel enableGesture];
    
    // Do any additional setup after loading the view, typically from a nib.
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    //    bottom1.bottomDelegate=self;
    bottom1.hidden=NO;
}


- (IBAction)backClicked:(id)sender
{
    
    BOOL isfromMenuToPizza=[[[NSUserDefaults standardUserDefaults]valueForKey:@"fromMenuToPizza"]boolValue];
    if(isfromMenuToPizza==YES)
    {
        [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"fromMenuToPizza"];
        [self.navigationController popViewControllerAnimated:YES];
    }
    else
    {
        SignUpViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"SignUpViewController"];
        
        [self.navigationController pushViewController:add animated:NO];
    }
}







- (IBAction)showMenu:(id)sender
{
    
    CardViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"ZpizzaCardViewController"];
    
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1];
    self.navigationController.navigationBar.tintColor=[UIColor whiteColor];
    [self.navigationController pushViewController:add animated:NO];
}


- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    _searchView.hidden=NO;
    
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
    
    
    
    return YES;
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    NSString *strText=textSearch.text;
    [self searchBar1:strText];
    return YES;
}
#pragma mark Search
-(void)searchBar1:(NSString *)searchString
{
    
    NSPredicate *pred =[NSPredicate predicateWithFormat:@"SELF contains[c] %@", searchString];
    filteredArr = [finalArray filteredArrayUsingPredicate:pred];
    
    if(filteredArr.count==0)
    {
        [_searchTable reloadData];
        return;
    }
    
    [_searchTable reloadData];
}


-(void)cancelNumberPad:(id)sender
{
    [self.view endEditing:YES];
    [textSearch resignFirstResponder];
    _searchView.hidden = YES;
    _searchTable.hidden = YES;
    view.hidden=YES;
    self.navigationItem.titleView=nil;
}
-(void)doneWithNumberPad:(id)sender
{
    NSLog(@"Done Clicked.");
    [self.view endEditing:YES];
    [textSearch resignFirstResponder];
    _searchView.hidden = YES;
    _searchTable.hidden = YES;
    view.hidden=YES;
    self.navigationItem.titleView=nil;
}
- (void)textFieldDidEndEditing:(UITextField *)quantityText
{
    
    [textSearch resignFirstResponder];
    _searchView.hidden = YES;
    _searchTable.hidden = YES;
    view.hidden=YES;
    self.navigationItem.titleView=nil;
    
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    return YES;
}
- (IBAction)searchClicked:(id)sender
{
    UIButton *selectedButton = (UIButton *)sender;
    
    //If checked, uncheck and visa versa
    [selectedButton setSelected:![selectedButton isSelected]];
    
    if([selectedButton isSelected])
    {
        CGRect someRect = CGRectMake(0, 0, 200, 30);
        textSearch = [[UITextField alloc] initWithFrame:someRect];
        
        _searchTable.tag=5555;
        textSearch.borderStyle = UITextBorderStyleNone;
        textSearch.font = [UIFont systemFontOfSize:15];
        textSearch.placeholder = @"enter text";
        textSearch.autocorrectionType = UITextAutocorrectionTypeNo;
        textSearch.keyboardType = UIKeyboardTypeDefault;
        textSearch.returnKeyType = UIReturnKeyDone;
        textSearch.clearButtonMode = UITextFieldViewModeWhileEditing;
        textSearch.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
        view = [[UIView alloc] initWithFrame:someRect];
        view.backgroundColor=[UIColor whiteColor];
        [view addSubview:textSearch];
        
        self.navigationItem.titleView = view;
        textSearch.delegate=self;
        NSLog(@"searchclicked");
        
        _searchTable.delegate=self;
        _searchTable.dataSource=self;
        _searchView.hidden = NO;
        _searchTable.hidden = NO;
        view.hidden=NO;
        
    }
    else
    {
        //remove btn.tag from self.tapCollection
        _searchView.hidden = YES;
        _searchTable.hidden = YES;
        view.hidden=YES;
        self.navigationItem.titleView=nil;
        
    }
    [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"isSearch"];
    
}

-(void)viewWillAppear:(BOOL)animated
{
    
    
    [super viewWillAppear:NO];
    [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"selectedaddress"];
    _searchView.hidden=YES;
    if([UIScreen mainScreen].bounds.size.width==320)
    {
        _showWeAreOfferingLabel.numberOfLines=3;
        _showWeAreOfferingLabel.font=[UIFont fontWithName:@"Futura-CondensedExtraBold" size:23.0f];
        
        
    }
    
    _showWeAreOfferingLabel.hidden=YES;
    //    _showWeAreOfferingLabel.numberOfLines=3;
    //    _showWeAreOfferingLabel.adjustsFontSizeToFitWidth=YES;
    
    UIButton *btnBack = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnBack setFrame:CGRectMake(0, 0,30, 44)];
    [btnBack setImage:[UIImage imageNamed:@"BackIcon"] forState:UIControlStateNormal];
    [btnBack setImageEdgeInsets:UIEdgeInsetsMake(0, -30, 0.0, 0)];
    [btnBack addTarget:self action:@selector(backClicked:) forControlEvents:UIControlEventTouchUpInside];
    // btnBack.titleLabel.font = [UIFont fontWithName:@"HelveticaNeue-Bold" size:14.0f];
    UIBarButtonItem *barBtnBack = [[UIBarButtonItem alloc] initWithCustomView:btnBack];
    [barBtnBack setTintColor:[UIColor whiteColor]];
    
    self.navigationItem.leftBarButtonItem=barBtnBack;
    
    
    //    UIButton *btnRight = [UIButton buttonWithType:UIButtonTypeCustom];
    //    [btnRight setFrame:CGRectMake(0, 0,25, 25)];
    //    UIImage *buttonImage = [UIImage imageNamed:@"Search_icon_40x40.png"]   ;
    //    [btnRight setImage:buttonImage forState:UIControlStateNormal];
    //
    //    [btnRight setImageEdgeInsets:UIEdgeInsetsMake(0, 0, 0.0, 0)];
    //    [btnRight addTarget:self action:@selector(searchClicked:) forControlEvents:UIControlEventTouchUpInside];
    //    UIBarButtonItem *barBtnRight = [[UIBarButtonItem alloc] initWithCustomView:btnRight];
    //    [barBtnRight setTintColor:[UIColor whiteColor]];
    //    self.navigationItem.rightBarButtonItem=barBtnRight;
    
    
    
    
    _loadingImage.hidden=NO;
    _tableViewMain.hidden=YES;
    _tabView2.hidden=YES;
    NSURL *url = [[NSBundle mainBundle] URLForResource:@"loading_pizza" withExtension:@"gif"];
    self.loadingImage.image = [UIImage animatedImageWithAnimatedGIFURL:url];
    [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"RIGHTBUTTON"];
    
    self.navigationController.navigationBarHidden=NO;
    self.navigationItem.hidesBackButton=YES;
    self.navigationController.navigationBar.tintColor=[UIColor whiteColor];
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    self.navigationItem.title=@"MENU";
    
    [self.navigationController.navigationBar setTitleTextAttributes:
     @{NSForegroundColorAttributeName:[UIColor whiteColor],
       NSFontAttributeName:[UIFont fontWithName:@"Futura-CondensedExtraBold" size:18.0f]}];
    
    
    CustomBottomBarView *bottom1=[CustomBottomBarView sharedInst];
    
    bottom1.hidden=NO;
    
    
    
    [self callMenuApi];
    
    
}
-(void)callMenuApi
{
    
    NSString *stringUrl=@"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menu";
    NSLog(@" string url ====%@",stringUrl);
    NSString *encoded=[stringUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    manager.requestSerializer =requestSerializer;
    
    [manager GET:encoded parameters:nil success:^(AFHTTPRequestOperation *operation, id jsonMenu)
     
     
     {
         NSLog(@"JSON:==%@",jsonMenu);
         category=[[NSMutableArray alloc]init];
         images=[[NSMutableArray alloc]init];
         
         menuCatId=[[NSMutableArray alloc]init];
         NSLog(@"jsonMenu\n%@",jsonMenu);
         
         NSArray *myCat=[jsonMenu objectForKey:@"categories"];
         for (int i=0; i<[myCat count]; i++)
         {
             NSDictionary *mydict1;
             mydict1=[myCat objectAtIndex:i];
             [category addObject:[mydict1 valueForKey:@"category"]];
             [images addObject:[mydict1 valueForKey:@"imageurl"]];
             [menuCatId addObject:[mydict1 valueForKey:@"categoryid"]];
         }
         
         //         dispatch_async(dispatch_get_main_queue(), ^{
         [self.tableViewMain reloadData];
         //         });
         
         [self performSelectorOnMainThread:@selector(callMenuItemApi) withObject:nil waitUntilDone:YES];
         //         [self callMenuItemApi];
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
-(void)reloadTableView :(UITableView *)tabView2
{
    [tabView2 reloadData];
}
-(void)callMenuItemApi
{
    
    NSString *stringUrl=@"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menuitems";
    NSLog(@" string url ====%@",stringUrl);
    NSString *encoded=[stringUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    manager.requestSerializer =requestSerializer;
    
    [manager GET:encoded parameters:nil success:^(AFHTTPRequestOperation *operation, id jsonMenuItems)
     
     
     {
         NSLog(@"JSON:==%@",jsonMenuItems);
         
         _loadingImage.hidden=YES;
         _showWeAreOfferingLabel.hidden=NO;
         _tableViewMain.hidden=NO;
         _tabView2.hidden=NO;
         finalArray=[[NSMutableArray alloc]init];
         itemCost=[[NSMutableArray alloc]init];
         itemDesc=[[NSMutableArray alloc]init];
         menuItemCatId=[[NSMutableArray alloc]init];
         
         
         
         myCatMenuItem=[jsonMenuItems objectForKey:@"categories"];
         for (int i=0; i<[myCatMenuItem count]; i++)
         {
             NSDictionary *mydict1;
             mydict1=[myCatMenuItem objectAtIndex:i];
             [finalArray addObject:[mydict1 valueForKey:@"iname"]];
             [itemCost addObject:[mydict1 valueForKey:@"icost"]];
             [itemDesc addObject:[mydict1 valueForKey:@"idesc"]];
             [menuItemCatId addObject:[mydict1 valueForKey:@"categoryID"]];
         }
         
         leftMenuarray=[NSArray arrayWithObjects:@"Home",@"Store Locator",@"My Order",@"Order History", nil];
         leftmenuImageArray=[NSArray arrayWithObjects:@"add02.png",@"add02.png",@"add02.png",@"add02.png", nil];
         
         
         //         dispatch_async(dispatch_get_main_queue(), ^{
         [self.tabView2 reloadData];
         //         });
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


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    
    if (tableView.tag == 5000)
    {
        
        return 50;
        
    }
    else if (tableView.tag == 5555)
    {
        
        return 0;
        
    }
    else
    {
        return 0;
    }
}

- (nullable UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view;
    CGFloat heightSection = 0.0;
    //
    
    if (tableView.tag == 5000)
    {
        
        heightSection = 50;
        view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.tabView2.frame.size.width, heightSection)];
        
        UILabel *lbl = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, view.frame.size.width, view.frame.size.height-4)];
        
        lbl.text = [NSString stringWithFormat:@"%@",[category objectAtIndex:section]];
        lbl.backgroundColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
        lbl.font = [UIFont fontWithName:@"HelveticaNeue-Bold" size:14];
        lbl.textColor=[UIColor whiteColor];
        [view addSubview:lbl];
        
        view.backgroundColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    }
    
    else if (tableView.tag == 5555)
    {
        
        heightSection=0;
        
    }
    else
    {
        heightSection=0;
    }
    
    return view;
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView.tag==5000)
    {
        return 44;
    }
    else if(tableView.tag==5555)
    {
        return 44;
    }
    else
    {
        return 138;
    }
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (tableView.tag == 5000)
    {
        return [menuCatId count];
        
    }
    else if(tableView.tag==5555)
    {
        return 1;
    }
    else
    {
        return [images count];
    }
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(tableView.tag==5000)
    {
        
        int count=0;
        NSString *catidfind=[NSString stringWithFormat:@"%@",[menuCatId objectAtIndex:section]];
        for(int i=0;i<[menuItemCatId count];i++)
        {
            if([catidfind isEqualToString:[NSString stringWithFormat:@"%@",[menuItemCatId objectAtIndex:i]]])
            {
                count++;
            }
            else
            {
                
            }
        }
        
        return count;
        
        
    }
    else if(tableView.tag==5555)
    {
        return [filteredArr count];
    }
    else
    {
        return 1;
    }
    
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView.tag==5000)
    {
        
        MenuRightTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MenuRightTableViewCell"];
        if (cell == nil)
        {
            NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"MenuRightTableViewCell" owner:self options:nil];
            cell = [array objectAtIndex:0];
        }
        
        NSInteger v=indexPath.section;
        NSLog(@"section==%ld",(long)v);
        
        cell.separatorInset = UIEdgeInsetsMake(0.0f, 10.0f, 0.0f, 0.0f);
        for (int j=0; j<[menuCatId count]; j++)
        {
            if (indexPath.section==j)
            {
                
                NSString *myelement=[menuCatId objectAtIndex:j];
                NSPredicate *pred=[NSPredicate predicateWithFormat:@"(categoryID=%@)",myelement]; // if you need case sensitive search avoid '[c]' in the predicate
                
                NSArray *results = [myCatMenuItem filteredArrayUsingPredicate:pred];
                
                
                cell.nemeRight.text=[NSString stringWithFormat:@"%@",[[results objectAtIndex:indexPath.row] valueForKey:@"iname"]];
                
                
                
            }
        }
        
        
        
        
        return cell;
    }
    else if(tableView.tag==5555)
    {
        MenuRightTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MenuRightTableViewCell"];
        if (cell == nil)
        {
            NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"MenuRightTableViewCell" owner:self options:nil];
            cell = [array objectAtIndex:0];
        }
        
        //        NSInteger v=indexPath.section;
        //        NSLog(@"section==%ld",(long)v);
        //
        //        cell.separatorInset = UIEdgeInsetsMake(0.0f, 10.0f, 0.0f, 0.0f);
        //        for (int j=0; j<[menuCatId count]; j++)
        //        {
        //            if (indexPath.section==j)
        //            {
        //
        //                NSString *myelement=[menuCatId objectAtIndex:j];
        //                NSPredicate *pred=[NSPredicate predicateWithFormat:@"(categoryID=%@)",myelement]; // if you need case sensitive search avoid '[c]' in the predicate
        //
        //                NSArray *results = [myCatMenuItem filteredArrayUsingPredicate:pred];
        //
        //
        //                cell.nemeRight.text=[NSString stringWithFormat:@"%@",[[results objectAtIndex:indexPath.row] valueForKey:@"iname"]];
        //
        //
        //
        //            }
        //        }
        
        
        cell.nemeRight.text=[filteredArr objectAtIndex:indexPath.row];
        
        return cell;
    }
    else
    {
        MenuTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MenuTableViewCell"];
        if (cell == nil)
        {
            //            NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"MenuTableViewCell" owner:self options:nil];
            //            cell = [array objectAtIndex:0];
        }
        
        
        
        cell.contentView.backgroundColor=[UIColor whiteColor];
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        
        //        cell.contentView.layer.borderWidth=.5f;
        //        cell.contentView.layer.borderColor=[UIColor lightGrayColor].CGColor;
        //        cell.contentView.layer.borderWidth=.5f;
        //        cell.contentView.layer.borderColor=(__bridge CGColorRef _Nullable)([UIColor blackColor]);
        
        NSURL *myURL=[NSURL URLWithString:[images objectAtIndex:indexPath.section]];
        NSURLRequest *myurl=[NSURLRequest requestWithURL:myURL];
        
        [cell.imagemenu setImageWithURLRequest:myurl
                              placeholderImage:nil
                                       success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image)
         {
             cell.imagemenu.alpha = 0.0;
             cell.imagemenu.image = image;
             [UIView animateWithDuration:0.25
                              animations:^
              {
                  cell.imagemenu.alpha = 1.0;
              }];
         }
                                       failure:^(NSURLRequest *operation, NSHTTPURLResponse *response,NSError *error)
         {
             //  cell.imagemenu.image=[UIImage imageNamed:@"Zpizza_image-1"];
             
         }];
        cell.namemenu.text = [category objectAtIndex:indexPath.section];
        
        return cell;
    }
    
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (tableView.tag == 5000)
    {
        
        
        
        
        
        
        
        
        
        
        for (int j=0; j<[menuCatId count]; j++)
        {
            if (indexPath.section==j)
            {
                
                NSString *myelement=[menuCatId objectAtIndex:j];
                NSPredicate *pred=[NSPredicate predicateWithFormat:@"(categoryID=%@)",myelement]; // if you need case sensitive search avoid '[c]' in the predicate
                
                NSArray *results = [myCatMenuItem filteredArrayUsingPredicate:pred];
                NSUserDefaults *orderClicked=[NSUserDefaults standardUserDefaults];
                [orderClicked setValue:@"YES" forKey:@"orderClicked"];
                [orderClicked synchronize];
                NSUserDefaults *selectedItem=[NSUserDefaults standardUserDefaults];
                [selectedItem setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"iname"] forKey:@"selected"];
                [selectedItem synchronize];
                NSUserDefaults *costSelectedItem=[NSUserDefaults standardUserDefaults];
                [costSelectedItem setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"icost"] forKey:@"costSelectedItem"];
                [costSelectedItem synchronize];
                
                NSUserDefaults *descSelectedItem=[NSUserDefaults standardUserDefaults];
                [descSelectedItem setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"idesc"] forKey:@"descSelectedItem"];
                
                [descSelectedItem synchronize];
                NSUserDefaults *Selecteditemid=[NSUserDefaults standardUserDefaults];
                [Selecteditemid setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"itemid"] forKey:@"Selecteditemid"];
                
                [Selecteditemid synchronize];
                NSUserDefaults *imageurlSelecteditem=[NSUserDefaults standardUserDefaults];
                [imageurlSelecteditem setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"imageurl"] forKey:@"imageurlSelecteditem"];
                
                [imageurlSelecteditem synchronize];
                
                
                
                
            }
            
            
            
            
            
            
        }
        ViewItemViewController *toItemView = [self.storyboard instantiateViewControllerWithIdentifier:@"ViewItemViewController"];
        
        //        add.navigationController.navigationBarHidden = NO;
        //        add.navigationItem.hidesBackButton = YES;
        [self.navigationController pushViewController:toItemView animated:YES];
        
        //return;
    }
    else if (tableView.tag == 5555)
    {
        
        
        for (int j=0; j<[menuCatId count]; j++)
        {
            if (indexPath.section==j)
            {
                
                NSString *myelement=[menuCatId objectAtIndex:j];
                NSPredicate *pred=[NSPredicate predicateWithFormat:@"(categoryID=%@)",myelement]; // if you need case sensitive search avoid '[c]' in the predicate
                
                NSArray *results = [myCatMenuItem filteredArrayUsingPredicate:pred];
                NSUserDefaults *orderClicked=[NSUserDefaults standardUserDefaults];
                [orderClicked setValue:@"YES" forKey:@"orderClicked"];
                [orderClicked synchronize];
                NSUserDefaults *selectedItem=[NSUserDefaults standardUserDefaults];
                [selectedItem setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"iname"] forKey:@"selected"];
                [selectedItem synchronize];
                NSUserDefaults *costSelectedItem=[NSUserDefaults standardUserDefaults];
                [costSelectedItem setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"icost"] forKey:@"costSelectedItem"];
                [costSelectedItem synchronize];
                
                NSUserDefaults *descSelectedItem=[NSUserDefaults standardUserDefaults];
                [descSelectedItem setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"idesc"] forKey:@"descSelectedItem"];
                
                [descSelectedItem synchronize];
                NSUserDefaults *Selecteditemid=[NSUserDefaults standardUserDefaults];
                [Selecteditemid setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"itemid"] forKey:@"Selecteditemid"];
                
                [Selecteditemid synchronize];
                NSUserDefaults *imageurlSelecteditem=[NSUserDefaults standardUserDefaults];
                [imageurlSelecteditem setObject:[[results objectAtIndex:indexPath.row] valueForKey:@"imageurl"] forKey:@"imageurlSelecteditem"];
                
                [imageurlSelecteditem synchronize];
                
                
                
                
            }
            
            
            
            
            
            
        }
        ViewItemViewController *toItemView = [self.storyboard instantiateViewControllerWithIdentifier:@"ViewItemViewController"];
        
        //        add.navigationController.navigationBarHidden = NO;
        //        add.navigationItem.hidesBackButton = YES;
        [self.navigationController pushViewController:toItemView animated:YES];
        
        //return;
    }
    
    
    else
    {
        //Set the background color so we can actually see the views, the first will be grey, the second, black.
        
        //  [self scrollViewDidScroll:_tableViewMain index:indexPath];
        
        [self.tabView2 reloadData];
        CGRect sectionRect = [self.tabView2 rectForSection:indexPath.section];
        sectionRect.size.height = self.tabView2.frame.size.height;
        [self.tabView2 scrollRectToVisible:sectionRect animated:YES];
    }
    
    
}
//- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
//{
//
//
//
//        if (scrollView == self.tabView2)
//        {
//
//        }
//        else
//        {
//
//
//            NSIndexPath *visibleIndexPath = nil;
//            NSArray *indexPaths = [self.tableViewMain indexPathsForVisibleRows];
//            if (indexPaths.count)
//            {
//                for (NSIndexPath *indexPath in indexPaths)
//                {
//                    visibleIndexPath = indexPath;
//                }
//
//                if (visibleIndexPath)
//                {
//                    if (visibleIndexPath.section == 4 || visibleIndexPath.section == 3 || visibleIndexPath.section == 2)
//                    {
//                        CGRect sectionRect = [self.tabView2 rectForSection:visibleIndexPath.section];
//                        sectionRect.size.height = self.tabView2.frame.size.height;
//                        [self.tabView2 scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:0] atScrollPosition:UITableViewScrollPositionTop animated:YES];
//                    }
//                    else if (visibleIndexPath.section == 5)
//                    {
//                        CGRect sectionRect = [self.tabView2 rectForSection:visibleIndexPath.section];
//                        sectionRect.size.height = self.tabView2.frame.size.height;
//                        [self.tabView2 scrollToRowAtIndexPath:visibleIndexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
//                    }
//                    else
//                    {
//                        CGRect sectionRect = [self.tabView2 rectForSection:visibleIndexPath.section];
//                        sectionRect.size.height = self.tabView2.frame.size.height;
//                        [self.tabView2 scrollToRowAtIndexPath:visibleIndexPath atScrollPosition:UITableViewScrollPositionMiddle animated:YES];
//                    }
//                }
//            }
//        }
//
//
//
//}
//
//-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
//{
//    if (scrollView == self.tabView2)
//    {
//
//    }
//    else
//    {
//        NSIndexPath *visibleIndexPath = nil;
//        NSArray *indexPaths = [self.tableViewMain indexPathsForVisibleRows];
//        if (indexPaths.count)
//        {
//            for (NSIndexPath *indexPath in indexPaths)
//            {
//                visibleIndexPath = indexPath;
//            }
//
//            if (visibleIndexPath)
//            {
//                if (visibleIndexPath.section == 4 || visibleIndexPath.section == 3 || visibleIndexPath.section == 2)
//                {
//                    CGRect sectionRect = [self.tabView2 rectForSection:visibleIndexPath.section];
//                    sectionRect.size.height = self.tabView2.frame.size.height;
//                    [self.tabView2 scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:0] atScrollPosition:UITableViewScrollPositionTop animated:YES];
//                }
//                else if (visibleIndexPath.section == 5)
//                {
//                    CGRect sectionRect = [self.tabView2 rectForSection:visibleIndexPath.section];
//                    sectionRect.size.height = self.tabView2.frame.size.height;
//                    [self.tabView2 scrollToRowAtIndexPath:visibleIndexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
//                }
//                else
//                {
//                    CGRect sectionRect = [self.tabView2 rectForSection:visibleIndexPath.section];
//                    sectionRect.size.height = self.tabView2.frame.size.height;
//                    [self.tabView2 scrollToRowAtIndexPath:visibleIndexPath atScrollPosition:UITableViewScrollPositionMiddle animated:YES];
//                }
//            }
//        }
//    }
//
//}

//- (void)scrollViewDidScroll:(UIScrollView *)scrollView
//{
//    if (scrollView == self.tabView2)
//    {
//
//    }
//    else
//    {
//
//
//        CGPoint translation = [scrollView.panGestureRecognizer translationInView:scrollView.superview];
//
//        if(translation.y > 0)
//        {
//            NSLog(@"down");
//            NSArray *indexPaths = [self.tableViewMain indexPathsForVisibleRows];
//            if (indexPaths.count)
//            {
//
//
//                NSIndexPath *index1=[indexPaths objectAtIndex:0];
//
//                CGRect sectionRect = [self.tabView2 rectForSection:index1.section];
//                sectionRect.size.height = self.tabView2.frame.size.height;
//                [self.tabView2 scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:index1.row inSection:index1.section] atScrollPosition:UITableViewScrollPositionTop animated:YES];
//            }
//
//            // react to dragging down
//        } else
//        {
//
//            NSLog(@"up");
//            NSArray *indexPaths = [self.tableViewMain indexPathsForVisibleRows];
//            if (indexPaths.count)
//            {
//
//
//                NSIndexPath *index1=[indexPaths lastObject];
//
//                CGRect sectionRect = [self.tabView2 rectForSection:index1.section];
//                sectionRect.size.height = self.tabView2.frame.size.height;
//                [self.tabView2 scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:index1.row inSection:index1.section] atScrollPosition:UITableViewScrollPositionTop animated:YES];
//            }
//
//            // react to dragging up
//        }
//      //  NSIndexPath *visibleIndexPath = nil;
//            }
//}

//- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
//{

//Your code here

// }
//- (void)scrollViewDidScroll:(UIScrollView *)scrollView
//{
//    if (scrollView == self.tabView2)
//    {
//
//    }
//    else
//    {
//
//
//        CGPoint translation = [scrollView.panGestureRecognizer translationInView:scrollView.superview];
//
//        if(translation.y > 0)
//        {
//            NSLog(@"down");
//            // react to dragging down
//        } else
//        {
//
//            NSLog(@"up");
//            // react to dragging up
//        }
//        NSIndexPath *visibleIndexPath = nil;
//        NSArray *indexPaths = [self.tableViewMain indexPathsForVisibleRows];
//        if (indexPaths.count)
//        {
//            for (NSIndexPath *indexPath in indexPaths)
//            {
//                visibleIndexPath = indexPath;
//            }
//
//            if (visibleIndexPath)
//            {
//                if (visibleIndexPath.section == 4 || visibleIndexPath.section == 3 || visibleIndexPath.section == 2)
//                {
//                    CGRect sectionRect = [self.tabView2 rectForSection:visibleIndexPath.section];
//                    sectionRect.size.height = self.tabView2.frame.size.height;
//                    [self.tabView2 scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:0] atScrollPosition:UITableViewScrollPositionTop animated:YES];
//                }
//                else if (visibleIndexPath.section == 5)
//                {
//                    CGRect sectionRect = [self.tabView2 rectForSection:visibleIndexPath.section];
//                    sectionRect.size.height = self.tabView2.frame.size.height;
//                    [self.tabView2 scrollToRowAtIndexPath:visibleIndexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
//                }
//                else
//                {
//                    CGRect sectionRect = [self.tabView2 rectForSection:visibleIndexPath.section];
//                    sectionRect.size.height = self.tabView2.frame.size.height;
//                    [self.tabView2 scrollToRowAtIndexPath:visibleIndexPath atScrollPosition:UITableViewScrollPositionMiddle animated:YES];
//                }
//            }
//        }
//    }
//}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (IBAction)backToSignup:(id)sender
{
    MainViewController *backToMain=[self.storyboard instantiateViewControllerWithIdentifier:@"MainViewController"];
    [self.navigationController pushViewController:backToMain animated:NO];
    // [self.navigationController popViewControllerAnimated:YES];
}
- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation
{
    return UIInterfaceOrientationPortrait;
}
- (BOOL)shouldAutorotate
{
    return false;
}
- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
{
    return false;
}

@end
