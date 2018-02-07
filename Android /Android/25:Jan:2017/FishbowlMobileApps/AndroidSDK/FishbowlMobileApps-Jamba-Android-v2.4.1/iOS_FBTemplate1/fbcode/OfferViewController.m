//
//  OfferViewController.m
//  iOS_FBTemplate1
//
//  Created by surendra pathak on 09/02/16.
//  Copyright © 2016 HARSH. All rights reserved.
//

#import "OfferViewController.h"
#import "MenuViewController.h"
@interface OfferViewController ()

@end

@implementation OfferViewController




- (IBAction)backClicked:(id)sender
{
    
    BOOL isoffer=[[[NSUserDefaults standardUserDefaults]valueForKey:@"tooffer"]boolValue];
    if(isoffer==YES)
    {
        [self.navigationController popViewControllerAnimated:YES];
        [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"tooffer"];
    }
    else
    {
        MenuViewController *tomenu=[self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
        
        
        [self.navigationController pushViewController:tomenu animated:YES];
    }
    
}




- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationItem.hidesBackButton=YES;
    self.navigationItem.title=@"EXCITING OFFERS";
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    
    
    [self.navigationController.navigationBar setTitleTextAttributes:
     @{NSForegroundColorAttributeName:[UIColor whiteColor],
       NSFontAttributeName:[UIFont fontWithName:@"Futura-CondensedExtraBold" size:18.0f]}];
    
    
    UIButton *btnBack = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnBack setFrame:CGRectMake(0, 0,30, 44)];
    [btnBack setImage:[UIImage imageNamed:@"BackIcon"] forState:UIControlStateNormal];
    [btnBack setImageEdgeInsets:UIEdgeInsetsMake(0, -30, 0.0, 0)];
    [btnBack addTarget:self action:@selector(backClicked:) forControlEvents:UIControlEventTouchUpInside];
    // btnBack.titleLabel.font = [UIFont fontWithName:@"HelveticaNeue-Bold" size:14.0f];
    UIBarButtonItem *barBtnBack = [[UIBarButtonItem alloc] initWithCustomView:btnBack];
    [barBtnBack setTintColor:[UIColor whiteColor]];
    
    self.navigationItem.leftBarButtonItem=barBtnBack;
    // Do any additional setup after loading the view.
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

@end
