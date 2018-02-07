//
//  OrderHistoryTableViewCell.m
//  iOS_FBTemplate1
//
//  Created by HARSH on 21/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "StorelocatorTableViewCell.h"

@implementation StorelocatorTableViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
+(StorelocatorTableViewCell*)storelocatorTableViewCell
{
    
    StorelocatorTableViewCell *customPopup = [[[NSBundle mainBundle]loadNibNamed:@"StorelocatorTableViewCell" owner:self options:nil]lastObject];
    if(customPopup != nil || [customPopup isKindOfClass:[StorelocatorTableViewCell class]]){
        return customPopup;
    }else{
        return nil;
    }
}
@end
