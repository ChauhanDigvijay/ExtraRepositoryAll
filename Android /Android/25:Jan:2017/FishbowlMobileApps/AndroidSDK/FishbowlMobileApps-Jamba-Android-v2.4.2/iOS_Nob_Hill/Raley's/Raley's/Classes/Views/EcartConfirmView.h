//
//  EcartConfirmView.h
//  Raley's
//
//  Created by Bill Lewis on 3/19/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "EcartPreOrderResponse.h"
#import "EcartOrderRequest.h"
#import "EcartScreenDelegate.h"
#import "SmartTextView.h"
#import "Login.h"
#import "Product.h"
#import "ProgressDialog.h"
#import "WebService.h"

#define PRODUCT_ADD 1
#define PRODUCT_MODIFY 2
#define PRODUCT_DELETE 3

@interface EcartConfirmView : UIView <WebServiceListener>
{
    AppDelegate            *_app;
    UIColor                *_textColor;
    UIColor                *_headingColor;
    UIView                 *_parentView;
    UIImageView            *_productImageView;
    UILabel                *_quantityValue;
    UILabel                *_totalPriceValue;
    Login                  *_login;
    EcartPreOrderResponse  *_preOrderResponse;
    EcartOrderRequest      *_orderRequest;
    ProgressDialog         *_progressDialog;
    WebService             *_service;
}

@property (nonatomic, assign) id<EcartScreenDelegate> _ecartScreenDelegate;

- (id)initWithFrame:(CGRect)frame view:(UIView *)parentView preOrderResponse:(EcartPreOrderResponse *)preOrderResponse orderRequest:(EcartOrderRequest *)orderRequest;
- (void)show;

@end
