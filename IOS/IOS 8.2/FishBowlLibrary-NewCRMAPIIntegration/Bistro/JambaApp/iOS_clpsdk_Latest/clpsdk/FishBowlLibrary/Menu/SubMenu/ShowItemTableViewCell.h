//
//  ShowItemTableViewCell.h
//  FatzTemplate
//
//  Created by surendra pathak on 10/06/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ShowItemTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *lblNameMenu;

@property (weak, nonatomic) IBOutlet UILabel *lblDescItem;
@property (weak, nonatomic) IBOutlet UILabel *lblCostitem;

@end
