//
//  MenuTableViewCell.h
//  taco2
//
//  Created by HARSH on 19/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MenuTableViewCell : UITableViewCell
+(MenuTableViewCell*)menuTableViewCell;
@property (weak, nonatomic) IBOutlet UILabel *namemenu;
@property (weak, nonatomic) IBOutlet UIImageView *imagemenu;

@end
