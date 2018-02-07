//
//  StoreLocatorScreenDelegate.h
//  Raley's
//
//  Created by Bill Lewis on 12/17/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Store.h"

@protocol StoreLocatorScreenDelegate <NSObject>

- (void)changeUserStore:(Store *)store;

@end
