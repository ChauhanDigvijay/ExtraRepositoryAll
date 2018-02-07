//
//  MenuTableViewCell.m
//  taco2
//
//  Created by HARSH on 19/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "MenuTableViewCell.h"

@implementation MenuTableViewCell
+(MenuTableViewCell*)menuTableViewCell
{
    MenuTableViewCell *customPopup = [[[NSBundle mainBundle]loadNibNamed:@"MenuTableViewCell" owner:self options:nil]lastObject];
    if(customPopup != nil || [customPopup isKindOfClass:[MenuTableViewCell class]]){
        return customPopup;
    }else{
        return nil;
    }
}

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
