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


@interface MenuViewController ()
{
    NSMutableArray *leftCatArray;
    NSMutableArray *subMenuItemArray;
    NSMutableArray *countArray;
    ModelClass * obj;
   
}
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgBG;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgBGTop;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgLogo;
@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIView *view2;

@end

@implementation MenuViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    obj=[ModelClass sharedManager];
     [obj addLoadingView:self.view];
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
    [self callMenuApi];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)tapBack:(id)sender
{
 [self.navigationController popViewControllerAnimated:YES];
}





-(void)viewWillAppear:(BOOL)animated
{
    
    [super viewWillAppear:YES];
   
    _tableView.hidden=NO;
    
    
    
}
-(void)callMenuApi
{
    
    NSString *stringUrl=@"http://dev.clpcloud.com:8080/ClpfbappWS/clpfbapp/apptemplate/menu/104";
    NSLog(@" string url ====%@",stringUrl);
    NSString * encoded = [stringUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    AFJSONRequestSerializer *requestSerializer = [AFJSONRequestSerializer serializer];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    manager.requestSerializer =requestSerializer;
    
    [manager GET:encoded parameters:nil success:^(AFHTTPRequestOperation *operation, id jsonMenu)
     
     
     {
         NSLog(@"JSON:==%@",jsonMenu);
         leftCatArray=[[NSMutableArray alloc]init];
         NSLog(@"jsonMenu\n%@",jsonMenu);
         leftCatArray=[jsonMenu objectForKey:@"categories"];
         
         // [self.tableViewMain reloadData];
         _tableView.hidden=NO;
         
         [self performSelectorOnMainThread:@selector(callMenuItemApi) withObject:nil waitUntilDone:YES];
        

         [self callMenuItemApi];
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

-(void)callMenuItemApi
{
    
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






- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return 138;
    
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
    // cell.contentView.backgroundColor=[UIColor whiteColor];
    
   // cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    NSString *imgUrl=[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"imageurl"];
    
    //NSString *string = [[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"imageurl"];
    //    NSString *string = [imgUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    
    NSString *string = [imgUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    
    NSURL *url = [NSURL URLWithString:string];
    NSLog(@"URL is %@",url);
    cell.hidden = NO;
    
    [cell.imgCellBG sd_setImageWithURL:url placeholderImage:nil];
    cell.lblNameMenu.text=[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"category"];
    cell.lblCount.layer.cornerRadius = 15.0f;
    [cell.lblCount setClipsToBounds:YES];
    
    cell.lblCount.text=[countArray objectAtIndex:indexPath.row];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
   
    SubMenuViewController * SubMenuObj = [[SubMenuViewController alloc]initWithNibName:@"SubMenuViewController" bundle:nil];
    int strCatIdMain=[[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"categoryid"]intValue];
    SubMenuObj.mainCatId=strCatIdMain;
    NSLog(@"strCatIdMain is %d",strCatIdMain);
    SubMenuObj.subItemArray=subMenuItemArray;
    NSLog(@"subMenuItemArray is %@",subMenuItemArray);
    SubMenuObj.imageUrlStr=[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"imageurl"];
    NSLog(@"imageurl is %@",[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"imageurl"]);
    SubMenuObj.mainMenuItemNamelStr=[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"category"];
    NSLog(@"category is %@",[[leftCatArray objectAtIndex:indexPath.row]valueForKey:@"category"]);
     [self.navigationController pushViewController:SubMenuObj animated:YES];
    
    
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
