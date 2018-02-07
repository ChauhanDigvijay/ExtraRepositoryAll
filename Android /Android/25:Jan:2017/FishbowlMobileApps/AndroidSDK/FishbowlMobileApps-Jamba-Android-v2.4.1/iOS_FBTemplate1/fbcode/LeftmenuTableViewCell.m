//
//  LeftmenuTableViewCell.m
//  taco2
//
//  Created by HARSH on 07/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "LeftmenuTableViewCell.h"

@implementation LeftmenuTableViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
+(LeftmenuTableViewCell*)leftmenuTableViewCell
{
    LeftmenuTableViewCell *customPopup = [[[NSBundle mainBundle]loadNibNamed:@"LeftmenuTableViewCell" owner:self options:nil]lastObject];
    if(customPopup != nil || [customPopup isKindOfClass:[LeftmenuTableViewCell class]]){
        return customPopup;
    }else{
        return nil;
    }
}
@end
