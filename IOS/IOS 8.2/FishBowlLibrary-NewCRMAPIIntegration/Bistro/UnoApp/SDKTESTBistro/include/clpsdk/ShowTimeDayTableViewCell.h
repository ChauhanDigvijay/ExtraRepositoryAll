//
//  ShowTimeDayTableViewCell.h
//  clpsdk
//
//  Created by surendra pathak on 04/10/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ShowTimeDayTableViewCell : UITableViewCell
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblWeekDay;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblStartTime;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblEndTime;

@end
