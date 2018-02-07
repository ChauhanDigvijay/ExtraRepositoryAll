//
//  OrderPageViewController.h
//  taco2
//
//  Created by HARSH on 02/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OrderPageViewController : UIViewController
@property (strong, nonatomic) IBOutlet UILabel *tableBottomLineView;
@property (strong, nonatomic) IBOutlet UILabel *noItemOutlet;

@property (strong, nonatomic) IBOutlet UIView *totalView;
@property (strong, nonatomic) IBOutlet UILabel *totalCost;
@property (weak, nonatomic) IBOutlet UIButton *backOutlet;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *viewHeight;
- (IBAction)backactio:(id)sender;

- (IBAction)addMore:(id)sender;

- (IBAction)continueOrder:(id)sender;
@property (weak, nonatomic) IBOutlet UIImageView *tablBackGround;
- (void)somethingWithParam;

@end
