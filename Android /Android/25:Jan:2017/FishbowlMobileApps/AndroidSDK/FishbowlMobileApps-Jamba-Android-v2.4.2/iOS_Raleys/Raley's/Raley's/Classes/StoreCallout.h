//
//  StoreCallout.h
//  Raley's
//
//  Created by Billy Lewis on 9/25/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "Store.h"
#import "StoreLocatorScreenDelegate.h"

@interface StoreCallout : UIView
{
    AppDelegate  *_app;
    Store        *_store;
}

@property (nonatomic, assign) id<StoreLocatorScreenDelegate> _storeLocatorScreenDelegate;
@property (nonatomic, strong)UIButton *_myStoreButton;
@property (nonatomic, strong)UILabel *_myStoreLabel;

- (id)initWithFrame:(CGRect)frame andStore:(Store *)store;


@end
