//
//  HistoryCell.h
//  clpsdk
//
//  Created by Gourav Shukla on 10/11/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HistoryCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *dateLbl;
@property (weak, nonatomic) IBOutlet UILabel *rewardEarnLbl;
@property (weak, nonatomic) IBOutlet UILabel *descriptionLbl1;
@property (weak, nonatomic) IBOutlet UILabel *descriptionLbl2;
@property (weak, nonatomic) IBOutlet UILabel *pointsLbl;

@end
