//
//  SignUpBonusView.m
//  clpsdk
//
//  Created by Gourav Shukla on 29/11/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "SignUpBonusView.h"
#import "BonusCell.h"
#import "ModelClass.h"
#import "ApiClasses.h"

@interface SignUpBonusView ()
{
    ApiClasses     * apiCall;
    ModelClass     * obj;
    NSArray        * arrayBonus;
}
@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tblBouns;
@property (unsafe_unretained, nonatomic) IBOutlet UIView *BonusView;

@end

@implementation SignUpBonusView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self shadoOffect:self.BonusView];
     obj = [ModelClass sharedManager];
    
    [self signupRuleList];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 2;
    shadoView.layer.shadowOpacity = 0.5;
    
}

#pragma mark - TableView Delegate And DataSources

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
      return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
      return 44;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [arrayBonus count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
        static NSString *CellIdentifier = @"BonusCell";
        BonusCell *cell = (BonusCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil)
        {
            cell = [[[NSBundle mainBundle] loadNibNamed:CellIdentifier owner:self options:nil] objectAtIndex:0];
        }
      cell.signUpLbl.text = [[arrayBonus objectAtIndex:indexPath.row]valueForKey:@"description"];
    
      cell.signUpLbl.layer.cornerRadius = 8;
      cell.signUpLbl.layer.masksToBounds = YES;
      cell.signUpLbl.layer.borderWidth = 1.0;
      cell.signUpLbl.layer.borderColor = [UIColor lightGrayColor].CGColor;
        return cell;
}

// delegate method
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(arrayBonus.count>0)
    {
        NSString * ruleID = [NSString stringWithFormat:@"%@",[[arrayBonus objectAtIndex:indexPath.row]valueForKey:@"id"]];
        
        NSString * rewardRule = [NSString stringWithFormat:@"%@",[[arrayBonus objectAtIndex:indexPath.row]valueForKey:@"rewardRule"]];
        
        NSString * description = [NSString stringWithFormat:@"%@",[[arrayBonus objectAtIndex:indexPath.row]valueForKey:@"description"]];
        
        [self.delegate Bonus:ruleID andReward:rewardRule andDescription:description withTxtTag:self.intTag];
    }
  
   [self.navigationController popViewControllerAnimated:YES];
}


# pragma mark - Back Button Action

- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - signupRuleList Api
-(void)signupRuleList
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall signupRuleList:nil url:@"/loyalty/signupRuleList" withTarget:self withSelector:@selector(signupRuleList:)];
    apiCall = nil;
}


// signupRuleList api response
-(void)signupRuleList:(id)response
{
    [obj removeLoadingView:self.view];
    
    arrayBonus = (NSArray *)response;
    
    NSLog(@"state signupRuleList response ------- %@",response);
    NSLog(@"arrayBonus response ------- %@",arrayBonus);
    
    [self.tblBouns reloadData];
}

@end
