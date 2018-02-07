//
//  PointBankCell.h
//  clpsdk
//
//  Created by Gourav Shukla on 07/02/17.
//  Copyright © 2017 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PointBankCell : UITableViewCell
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *offerNameLbl;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *reedemBtnLbl;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *pointBankLbl;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *lineImg;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *pointImg;

@end
