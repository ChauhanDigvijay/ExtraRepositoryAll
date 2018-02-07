//
//  ShoppingListCell.m
//  Raley's
//
//  Created by VT02 on 5/28/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "ShoppingListCell.h"

@implementation ShoppingListCell
@synthesize _listName,_delete,_activate,_cellScrollView,_cellContentView,_tickMark;
- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
