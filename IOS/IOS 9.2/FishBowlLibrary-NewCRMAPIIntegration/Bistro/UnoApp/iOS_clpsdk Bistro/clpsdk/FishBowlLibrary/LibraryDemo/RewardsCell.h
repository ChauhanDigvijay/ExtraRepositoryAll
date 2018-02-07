//
//  RewardsCell.h
//  clpsdk
//
//  Created by Gourav Shukla on 21/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RewardsCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *lblTitleTbl;
@property (weak, nonatomic) IBOutlet UILabel *lblDescriptionTbl;
@property (weak, nonatomic) IBOutlet UILabel *lblExpireDateTbl;

@end
