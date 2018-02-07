//
//  MenuTableViewCell.h
//  clpsdk
//
//  Created by surendra pathak on 16/08/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MenuTableViewCell : UITableViewCell
@property (unsafe_unretained, nonatomic) IBOutlet UIActivityIndicatorView *activityIndicator;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgCellBG;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblNameMenu;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblCount;

@end
