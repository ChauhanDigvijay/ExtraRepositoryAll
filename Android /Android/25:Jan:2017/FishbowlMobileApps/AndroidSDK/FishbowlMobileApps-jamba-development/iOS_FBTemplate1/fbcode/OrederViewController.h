//
//  OrederViewController.h
//  taco2
//
//  Created by HARSH on 20/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OrederViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIButton *instoreOutlet;
@property (weak, nonatomic) IBOutlet UIButton *drivethruOutlet;
- (IBAction)instoreAction:(id)sender;
- (IBAction)drivethruAction:(id)sender;
- (IBAction)cancleAction:(id)sender;
- (IBAction)getMeThereAction:(id)sender;

@end
