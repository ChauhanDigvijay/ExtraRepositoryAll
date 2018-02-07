//
//  ShoppingListCell.h
//  Raley's
//
//  Created by VT02 on 5/28/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ShoppingListCell : UITableViewCell
@property (nonatomic, retain) IBOutlet UILabel *_listName;
@property (nonatomic, retain) IBOutlet UIView *_cellContentView;

@property (nonatomic, retain) IBOutlet UIButton *_activate,*_delete,*_tickMark;

@property (nonatomic, retain) IBOutlet UIScrollView *_cellScrollView;

@end
