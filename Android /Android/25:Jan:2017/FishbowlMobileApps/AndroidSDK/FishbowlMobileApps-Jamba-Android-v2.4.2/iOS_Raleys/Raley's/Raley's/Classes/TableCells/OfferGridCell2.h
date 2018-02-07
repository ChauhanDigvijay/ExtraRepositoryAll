//
//  OfferGridCell.h
//  Raley's
//
//  Created by Bill Lewis on 11/15/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "Offer.h"
#import "SmartTextView.h"
#import "ShoppingScreenDelegate.h"
#import "ProgressDialog.h"
#import "OfferDetailViewController.h"
#import "ShoppingScreenVC.h"

#define OFFER_GRID_CELL_IDENTIFIER @"OfferGridCell"

@interface OfferGridCell : UICollectionViewCell <OfferDetailDelegate>
{
    AppDelegate     *_app;
    ProgressDialog  *_progressDialog;
}

@property (nonatomic, assign) id<ShoppingScreenDelegate> _shoppingScreenDelegate;
@property (nonatomic, strong)UIImageView    *_cellBackground;
@property (nonatomic, strong)UIImageView    *_offerImageView;
@property (nonatomic, strong)UIButton       *_acceptOfferButton;
@property (nonatomic, strong)UILabel  *_consumerTitleLabel;
@property (nonatomic, strong)UILabel  *_consumerDescriptionLabel;
@property (nonatomic, strong)UILabel  *_offerLimitLabel;
@property (nonatomic, strong)UILabel  *_endDateLabel;
@property (nonatomic, strong)UILabel  *_acceptedOfferLabel;
@property (nonatomic, strong)Offer          *_offer;

@property (nonatomic, retain) ShoppingScreenVC *_parent;

@end
