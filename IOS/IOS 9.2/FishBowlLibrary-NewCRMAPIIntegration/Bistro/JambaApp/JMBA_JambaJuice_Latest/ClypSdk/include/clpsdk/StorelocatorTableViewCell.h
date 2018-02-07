//
//  OrderHistoryTableViewCell.h
//  iOS_FBTemplate1
//
//  Created by HARSH on 21/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface StorelocatorTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *address;
@property (weak, nonatomic) IBOutlet UILabel *phone;
+(StorelocatorTableViewCell*)storelocatorTableViewCell;
@property (weak, nonatomic) IBOutlet UILabel *city;
@property (weak, nonatomic) IBOutlet UILabel *zipcode;
@property (weak, nonatomic) IBOutlet UILabel *state;
@end
