//
//  LoyalityView.h
//  clpsdk
//
//  Created by Gourav Shukla on 17/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MBCircularProgressBarView.h"
#import "MBCircularProgressBarLayer.h"

@interface LoyalityView : UIViewController
@property (unsafe_unretained, nonatomic) IBOutlet MBCircularProgressBarView *progressBar;
@property (weak, nonatomic) IBOutlet UIView *pointsView;
@property (weak, nonatomic) IBOutlet UIView *earnView;
@property (weak, nonatomic) IBOutlet UIView *programDetailView;
@property (weak, nonatomic) IBOutlet UILabel *maxPointsLbl;

@end
