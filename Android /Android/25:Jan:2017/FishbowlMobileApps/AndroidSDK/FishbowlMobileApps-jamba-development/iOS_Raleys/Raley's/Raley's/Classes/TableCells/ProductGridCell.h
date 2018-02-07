//
//  ProductGridCell.h
//  Raley's
//
//  Created by Bill Lewis on 10/11/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "Product.h"
#import "SmartTextView.h"
#import "ProgressDialog.h"
#import "ShoppingScreenVC.h"
#import "ShoppingScreenDelegate.h"
#import "ProductCategory.h"

#define PRODUCT_GRID_CELL_IDENTIFIER @"ProductGridCell"

@interface ProductGridCell : UICollectionViewCell <UIGestureRecognizerDelegate,WebServiceListener>
{
    AppDelegate     *_app;
    ProgressDialog  *_progressDialog;
    WebService          *_service;
    Login *_login ;

}

@property (nonatomic, strong)UIImageView    *_productImageView;
@property (nonatomic, strong)UIImageView    *_promoImageView;
@property (nonatomic, strong)UIImageView    *_background;
@property (nonatomic, strong)UIButton       *_addButton;
@property (nonatomic, strong)UILabel        *_regPriceValue;
@property (nonatomic, strong)UILabel        *_promoPriceText;
@property (nonatomic, strong)UILabel        *_promoPriceValue;
@property (nonatomic, strong)UIImageView        *_priceCell;
@property (nonatomic, strong)UIView         *_priceCellContainer;

@property (nonatomic, strong)UIView       *_background_new;
@property (nonatomic, strong)UIImageView        *_priceCell_new;


@property (nonatomic, strong) ProductCategory   *category;

//@property (nonatomic, strong)SmartTextView  *_descriptionLabel;
@property (nonatomic, strong)UILabel  *_descriptionLabel;
@property (nonatomic, strong)UILabel  *extends_detailLabl;
@property (nonatomic, strong)SmartTextView  *_extendedDisplayLabel;
@property (nonatomic, strong)Product        *_product;

@property (nonatomic, retain) ShoppingScreenVC *_parent;

//@property (nonatomic, strong) UILabel                   *_extendedDisplayLabel_new;
@property (nonatomic, strong) NSIndexPath               *indexpath;
@property (nonatomic, strong) UIActivityIndicatorView   *loading;
@property (nonatomic, strong)UIButton       *_addToList;
@property (nonatomic, assign) id<ShoppingScreenDelegate> _shoppingScreenDelegate;
@property(nonatomic,readwrite)int addType;
//@property(nonatomic,readwrite)Product *addProduct;
@property(nonatomic,readwrite)int productQty;
@property(nonatomic,readwrite)NSString *lastSku;


@property(nonatomic, strong)UIView *see_offer_view;


//

@end
