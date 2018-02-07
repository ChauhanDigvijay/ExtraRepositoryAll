//
//  ShoppingDetailsCell.m
//  Raley's
//
//  Created by VT02 on 5/30/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "ShoppingDetailsCell.h"

@implementation ShoppingDetailsCell{
    NSIndexPath *_index;
}
@synthesize _productImage,_productName,_totalItemPrice,_scrollView,_contentView,_deletebtn;
@synthesize _Move_to_activebtn;
- (void)awakeFromNib
{
    // Initialization code
}
-(void)setDeleteButtonEvent:(NSIndexPath*)indexPath{
    _index=indexPath;
    [self._deletebtn addTarget:self action:@selector(callDelegate:) forControlEvents:UIControlEventTouchUpInside];

}
-(void)callDelegate:(UIButton*)btn{
        [self.delegate deleteProduct:_index];
}

-(void)callMovetoDelegate:(UIButton*)btn{
     [self.delegate MoveToActive:_index];
}

-(void)SetMove_to_Active:(NSIndexPath*)currentIndex
{
    _index=currentIndex;
    [self._Move_to_activebtn addTarget:self action:@selector(callMovetoDelegate:) forControlEvents:UIControlEventTouchUpInside];
}



- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
