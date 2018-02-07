//
//  SubMenuViewController.m
//  clpsdk
//
//  Created by surendra pathak on 16/08/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "SubMenuViewController.h"
#import "ApiClasses.h"
#import "ShowItemTableViewCell.h"
#import "itemViewController.h"
@interface SubMenuViewController ()
{
    NSMutableArray *selectedItemArray;
}
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgBG;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgItem;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblNameItem;

@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tableView;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgTop;
@end

@implementation SubMenuViewController
@synthesize subItemArray,mainCatId,imageUrlStr,mainMenuItemNamelStr;

- (void)viewDidLoad {
    [super viewDidLoad];
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
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
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    _lblNameItem.text=mainMenuItemNamelStr;
    selectedItemArray=[[NSMutableArray alloc]init];
     NSLog(@"Sub item selectedArray==%@",subItemArray);
    for (int i=0; i<subItemArray.count; i++)
    {
        int subMenuCatId=[[[subItemArray objectAtIndex:i
                            ]valueForKey:@"categoryID"]intValue];
        if (mainCatId == subMenuCatId)
        {
            [selectedItemArray addObject:[subItemArray objectAtIndex:i]];
        }
    }
    NSLog(@"selectedArray==%@",selectedItemArray);
    NSString *string = [imageUrlStr stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    NSURL *url = [NSURL URLWithString:string];
    
    [_imgItem sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
}
- (IBAction)tapBack:(id)sender
{
[self.navigationController popViewControllerAnimated:YES];
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return 103;
    
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    // return leftCatArray.count;
    return selectedItemArray.count;
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    ShowItemTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ShowItemTableViewCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"ShowItemTableViewCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
    
    _tableView.tableFooterView =  [UIView new];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.lblNameMenu.text=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"iname"];
    cell.lblDescItem.text=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"idesc"];
    cell.lblCostitem.text=[NSString stringWithFormat:@"$%@",[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"icost"]];
    
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    itemViewController * itemViewObj = [[itemViewController alloc]initWithNibName:@"itemViewController" bundle:nil];
    
    itemViewObj.selectedItemImageUrl=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"imageurl"];
    itemViewObj.selectedItemName=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"iname"];
    
   itemViewObj.selectedItemICost=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"icost"];
    itemViewObj.selectedItemDesc=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"idesc"];
    itemViewObj.selectedItemId=[[selectedItemArray objectAtIndex:indexPath.row]valueForKey:@"itemid"];
    itemViewObj.selectedmainMenuNameItem=mainMenuItemNamelStr;
    [self.navigationController pushViewController:itemViewObj animated:YES];
    
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
