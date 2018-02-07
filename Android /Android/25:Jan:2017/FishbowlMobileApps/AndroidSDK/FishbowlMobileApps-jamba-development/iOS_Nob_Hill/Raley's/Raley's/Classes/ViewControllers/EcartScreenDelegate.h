//
//  EcartScreenDelegate.h
//  Raley's
//
//  Created by Bill Lewis on 3/19/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol EcartScreenDelegate <NSObject>

- (void)handleEcartOrderServiceResponse:(id)responseObject;
- (void)showSubmitButton;

@end
