//
//  ShoppingScreenDelegate.h
//  Raley's
//
//  Created by Bill Lewis on 12/6/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol ShoppingScreenDelegate <NSObject>

- (BOOL)shoppingListVisible;
- (void)handleListServiceResponse:(id)responseObject httpStatusCode:(int)status _isCurrentList:(BOOL)currentList;
- (void)getCurrentShoppingList:(NSString *)listId;
- (void)acceptOffer:(Offer *)offer;
- (void) RemoveOffers_DetailpageSelected;
-(BOOL)isWebServiceRunning;

@end
