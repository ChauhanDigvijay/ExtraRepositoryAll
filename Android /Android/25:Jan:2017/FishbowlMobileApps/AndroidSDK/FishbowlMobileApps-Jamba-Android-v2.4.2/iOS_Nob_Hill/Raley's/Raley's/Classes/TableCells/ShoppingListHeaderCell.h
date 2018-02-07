//
//  ShoppingListHeaderCell.h
//  Raley's
//
//  Created by Bill Lewis on 12/5/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

#define SHOPPINGLIST_HEADER_CELL_IDENTIFIER @"ShoppingListHeaderCell"

@interface ShoppingListHeaderCell : UICollectionReusableView
{
    AppDelegate *_app;
}


@property (nonatomic, strong)UILabel *_aisleNumber;

@end
