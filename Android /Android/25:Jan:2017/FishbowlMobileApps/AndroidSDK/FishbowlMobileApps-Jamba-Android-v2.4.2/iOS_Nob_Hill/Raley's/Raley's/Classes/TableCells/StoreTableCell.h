//
//  StoreTableCell.h
//  Raley's
//
//  Created by Bill Lewis on 10/9/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "Store.h"
#import "StoreLocatorScreenDelegate.h"

#define STORE_TABLE_CELL_ID @"StoreTableCell"


@interface StoreTableCell : UITableViewCell
{
    AppDelegate  *_app;
    Store        *_store;
}

@property (nonatomic, assign) id<StoreLocatorScreenDelegate> _storeLocatorScreenDelegate;

- (id)initWithStore:(Store *)store :(int)width :(int)height;


@end
