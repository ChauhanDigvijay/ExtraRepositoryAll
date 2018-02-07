//
//  MyHistoryView.m
//  clpsdk
//
//  Created by Gourav Shukla on 10/11/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "MyHistoryView.h"
#import "HistoryCell.h"
#import "ModelClass.h"
#import "ApiClasses.h"

@interface MyHistoryView ()<UITableViewDelegate,UITableViewDataSource>
{
    ModelClass        * obj;
    ApiClasses        * apiCall;
    NSMutableArray    * arrayLoyality;
}

@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tblHistory;
@property (weak, nonatomic) IBOutlet UIView *headerView;
@property (weak, nonatomic) IBOutlet UIView *topView;
@property (weak, nonatomic) IBOutlet UIImageView *imageLine;


@end

@implementation MyHistoryView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    obj=[ModelClass sharedManager];
    
    
    // header view
    self.tblHistory.tableHeaderView = self.headerView;
    
    // shado view
    [self shadoOffect:self.topView];
    
    self.imageLine.backgroundColor =[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:.3];
    
    // loyalityActivity points call papi
    [self loyaltyActivityPoints];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
}

#pragma mark - loyalityActivity Points Api

-(void)loyaltyActivityPoints
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSString * str = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    [dict setValue:str forKey:@"memberId"];
    [dict setValue:@"POINT_RULE" forKey:@"activityType"];
    [dict setValue:@"0" forKey:@"startIndex"];
    [dict setValue:@"10" forKey:@"count"];

    [apiCall LoyaltyActivityApi:dict url:@"/loyalty/getActivity" withTarget:self withSelector:@selector(getLoyalityCount:)];
    apiCall = nil;
}

// loyalityActivity response
-(void)getLoyalityCount:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"activity api response -------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 0)
    {
        [self alertViewDelegate:@"No activit found"];
    }
    else
    {
        arrayLoyality = [response valueForKey:@"loyaltyActivityList"];
        [self.tblHistory reloadData];
        NSLog(@"array loyality response -------- %@",arrayLoyality);
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


#pragma mark - TableView Delegate And DataSources

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 35;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [arrayLoyality count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
        static NSString *CellIdentifier = @"HistoryCell";
        HistoryCell *cell = (HistoryCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil)
        {
            cell = [[[NSBundle mainBundle] loadNibNamed:CellIdentifier owner:self options:nil] objectAtIndex:0];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    // activityType
    if([[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"activityType"]!=(NSString *)[NSNull null])
    {
        cell.rewardEarnLbl.text = [[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"activityType"];
    }
    
    
    NSNumber * checkNumber = [[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"checkNumber"];
    
    // checkNumber
    if([[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"checkNumber"]!=(NSString *)[NSNull null])
    {
         cell.descriptionLbl2.text = [checkNumber stringValue];
    }
    
    // description
    if([[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"description"]!=(NSString *)[NSNull null])
    {
         cell.descriptionLbl1.text = [[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"description"];
    }
    
    // Date And Time
    if([[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"created"]!=(NSString *)[NSNull null])
    {
        NSLog(@"date is %@",[[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"created"]);
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss.S"];
        NSDate *date = [formatter dateFromString:[[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"created"]];
        [formatter setDateFormat:@"MM/dd/yyyy"];
        NSString *strdate = [formatter stringFromDate:date];
        NSLog(@"strdate is %@",strdate);
        
        
        //Optionally for time zone conversions
        //    [formatter setTimeZone:[NSTimeZone timeZoneWithName:@"..."]];
        
        cell.dateLbl.text = strdate;
    }
    
    NSNumber * pointsEarned = [[arrayLoyality objectAtIndex:indexPath.row]valueForKey:@"pointsEarned"];
    
    // pointsEarned
    if(pointsEarned)
    {
        cell.pointsLbl.text = [pointsEarned stringValue];
    }
    
        return cell;
}



#pragma mark - back button action

- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}


@end
