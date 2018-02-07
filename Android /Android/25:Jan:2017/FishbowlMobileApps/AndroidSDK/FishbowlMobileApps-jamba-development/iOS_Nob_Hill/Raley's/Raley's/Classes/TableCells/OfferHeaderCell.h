//
//  OfferHeaderCell.h
//  Raley's
//
//  Created by Bill Lewis on 11/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

#define OFFER_HEADER_CELL_IDENTIFIER @"OfferHeaderCell"

@interface OfferHeaderCell : UICollectionReusableView
{
    AppDelegate *_app;
}

@property (nonatomic, strong)UILabel *_categoryLabel;

@end
