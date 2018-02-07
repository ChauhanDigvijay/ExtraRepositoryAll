//
//  MenuTableViewCell.h
//  taco2
//
//  Created by HARSH on 19/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MenuRightTableViewCell : UITableViewCell
+(MenuRightTableViewCell*)menuRightTableViewCell;

@property (weak, nonatomic) IBOutlet UILabel *nemeRight;
@property (weak, nonatomic) IBOutlet UILabel *plusSign;
@property (weak, nonatomic) IBOutlet UILabel *bottomLine;

@end
