//
//  ProductView.h
//  Raley's
//
//  Created by Bill Lewis on 10/11/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "SmartTextView.h"
#import "Product.h"
#import "OffsetTextField.h"
#import "ProgressDialog.h"
#import "ShoppingScreenDelegate.h"
#import "WebService.h"

#define PRODUCT_ADD 1
#define PRODUCT_MODIFY 2
#define PRODUCT_DELETE 3

@interface ProductDetailView : UIView <UITextFieldDelegate, WebServiceListener>
{
    AppDelegate         *_app;
    UIScrollView        *_scrollView;
    UIImageView         *_productImageView;
    UILabel             *_quantityValue;
    UILabel             *_totalPriceValue;
    UILabel             *_quantityText;
    UIButton            *_quantityCheckBox;
    UIButton            *_weightCheckBox;
    Login               *_login;
    Product             *_product;
    OffsetTextField     *_instructionsTextField;
    ProgressDialog      *_progressDialog;
    WebService          *_service;
    int                 _productQuantity;
}

@property (nonatomic, assign) id<ShoppingScreenDelegate> _shoppingScreenDelegate;

- (id)initWithFrame:(CGRect)frame product:(Product *)product detailType:(int)detailType;
- (void)show;

@end
