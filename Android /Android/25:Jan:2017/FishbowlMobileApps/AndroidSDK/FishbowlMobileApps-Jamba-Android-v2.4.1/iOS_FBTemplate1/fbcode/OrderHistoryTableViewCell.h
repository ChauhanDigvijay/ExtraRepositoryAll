//
//  OrderHistoryTableViewCell.h
//  iOS_FBTemplate1
//
//  Created by HARSH on 21/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OrderHistoryTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *orderStatus;
@property (weak, nonatomic) IBOutlet UILabel *orderDate;
+(OrderHistoryTableViewCell*)OrderHistoryTableViewCell;
@property (weak, nonatomic) IBOutlet UILabel *orderNum;
@property (weak, nonatomic) IBOutlet UILabel *orderTotalprice;
@property (strong, nonatomic) IBOutlet UILabel *lblOrderSerialNumber;
@property (weak, nonatomic) IBOutlet UILabel *ordertime;
@end
