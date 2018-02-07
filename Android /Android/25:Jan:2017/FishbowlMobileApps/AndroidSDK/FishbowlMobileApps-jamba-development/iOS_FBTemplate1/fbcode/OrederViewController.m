//
//  OrederViewController.m
//  taco2
//
//  Created by HARSH on 20/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "OrederViewController.h"
#import "CardViewController.h"
@interface OrederViewController ()

@end

@implementation OrederViewController

- (void)viewDidLoad {
    [super viewDidLoad];
  _instoreOutlet.backgroundColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1];
    _drivethruOutlet.backgroundColor=[UIColor colorWithRed:0.812 green:0.129 blue:0.169 alpha:1];
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

- (IBAction)instoreAction:(id)sender
{
    CardViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"ZpizzaCardViewController"];
    
    //    add.navigationController.navigationBarHidden = NO;
    //    add.navigationItem.hidesBackButton = YES;
    [self.navigationController pushViewController:add animated:YES];
}

- (IBAction)drivethruAction:(id)sender {
}

- (IBAction)cancleAction:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)getMeThereAction:(id)sender {
}
@end
