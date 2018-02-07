//
//  ShoppingListGridCellGridCell.h
//  Raley's
//
//  Created by Bill Lewis on 12/5/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "SmartTextView.h"
#import "Product.h"
#import "ProgressDialog.h"
#import "ShoppingScreenDelegate.h"
#import "TextDialog.h"

#define SHOPPINGLIST_GRID_CELL_IDENTIFIER @"ShoppingListGridCell"

@interface ShoppingListGridCell : UICollectionViewCell <UIGestureRecognizerDelegate>
{
    AppDelegate     *_app;
    ProgressDialog  *_progressDialog;
}

@property (nonatomic, strong)UIImageView    *_productImageView;
@property (nonatomic, strong)UIImageView    *_promoImageView;
@property (nonatomic, strong)UIImageView    *_background;
@property (nonatomic, strong)UILabel        *_quantity;
@property (nonatomic, strong)UILabel        *_totalPrice;
@property (nonatomic, strong)SmartTextView  *_descriptionLabel;
@property (nonatomic, strong)SmartTextView  *_extendedDisplayLabel;
@property (nonatomic, strong)Product        *_product;

@property (nonatomic, assign) id<ShoppingScreenDelegate> _shoppingScreenDelegate;

@end
