//
//  LeftmenuTableViewCell.h
//  taco2
//
//  Created by HARSH on 07/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LeftmenuTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *leftMenuItem;
@property (weak, nonatomic) IBOutlet UIImageView *emnuitemImage;
+(LeftmenuTableViewCell*)leftmenuTableViewCell;
@end
