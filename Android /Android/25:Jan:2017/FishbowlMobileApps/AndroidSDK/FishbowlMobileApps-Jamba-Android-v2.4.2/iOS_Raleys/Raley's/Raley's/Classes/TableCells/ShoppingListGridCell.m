//
//  ShoppingListGridCell.m
//  Raley's
//
//  Created by Bill Lewis on 12/5/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ListAddItemRequest.h"
#import "ListDeleteItemRequest.h"
#import "ShoppingListGridCell.h"
#import "ProductDetailView.h"
#import "Utility.h"
#import "Logging.h"

@implementation ShoppingListGridCell

@synthesize _productImageView;
@synthesize _promoImageView;
@synthesize _background;
@synthesize _quantity;
@synthesize _totalPrice;
@synthesize _descriptionLabel;
@synthesize _extendedDisplayLabel;
@synthesize _product;
@synthesize _shoppingScreenDelegate;


- (id)initWithFrame:(CGRect)frame;
{
    if ((self = [super initWithFrame:frame]))
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        _product = _app._currentProduct;
        int width = frame.size.width;
        int height = frame.size.height;
        int textXOrigin = width * .03;
        int textWidth = width - (textXOrigin * 2);
        int textHalfWidth = (width - (textXOrigin * 2)) / 2;
        int productImageSize = height * .35;
        int promoImageSize = height * .15;
        int promoImagePad = width * .02;

        _background = [[UIImageView alloc] initWithFrame:frame];
        [_background setImage:[UIImage imageNamed:@"product_cell"]];
        self.backgroundView = _background;

        _promoImageView = [[UIImageView alloc] initWithFrame:CGRectMake(promoImagePad, promoImagePad, promoImageSize, promoImageSize)];
        [_promoImageView setImage:[UIImage imageNamed:@"sale_tag"]];
        [self.contentView addSubview:_promoImageView];

        _productImageView = [[UIImageView alloc] initWithFrame:CGRectMake((width - productImageSize) / 2, height * .03, productImageSize, productImageSize)];
        [self.contentView addSubview:_productImageView];

        _descriptionLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .40, textWidth, height * .40) backGroundColor:[UIColor clearColor]];
        _descriptionLabel.fontFamily = _app._normalFont;
        _descriptionLabel.textAlignment = NSTextAlignmentCenter;
        _descriptionLabel.numberOfLines = 4;
        _descriptionLabel.textColor = [UIColor blackColor];
        [self.contentView addSubview:_descriptionLabel];

        int extendedHeight = height * .09;
        _extendedDisplayLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .82, textHalfWidth, extendedHeight) backGroundColor:[UIColor clearColor]];
        _extendedDisplayLabel.fontFamily = _app._normalFont;
        _extendedDisplayLabel.textAlignment = NSTextAlignmentLeft;
        _extendedDisplayLabel.numberOfLines = 1;
        _extendedDisplayLabel.textColor = [UIColor blackColor];
        [self.contentView addSubview:_extendedDisplayLabel];

        int quantityHeight = height * .095;

        _quantity = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .89, textHalfWidth, quantityHeight)];
        _quantity.font = [Utility fontForFamily:_app._normalFont andHeight:quantityHeight];
        _quantity.textAlignment = NSTextAlignmentLeft;
        _quantity.numberOfLines = 1;
        _quantity.backgroundColor = [UIColor clearColor];
        _quantity.textColor = [UIColor blackColor];
        [self.contentView addSubview:_quantity];

        int priceHeight = height * .14;

        _totalPrice = [[UILabel alloc] initWithFrame:CGRectMake(width * .51, height * .86, width * .47, priceHeight)];
        _totalPrice.font = [Utility fontForFamily:_app._normalFont andHeight:priceHeight];
        _totalPrice.textAlignment = NSTextAlignmentRight;
        _totalPrice.numberOfLines = 1;
        _totalPrice.backgroundColor = [UIColor clearColor];

        if(_app._currentProduct.promoType > 0)
            _totalPrice.textColor = [UIColor colorWithRed:1.0 green:0.0 blue:0.0 alpha:1.0];
        else
            _totalPrice.textColor = [UIColor colorWithRed:.2 green:.55 blue:.26 alpha:1.0];

        [self.contentView addSubview:_totalPrice];
    }

    UILongPressGestureRecognizer *pressGesture = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(itemPressed:)];
    pressGesture.minimumPressDuration = .15;
    [pressGesture setDelegate:self];
    [self addGestureRecognizer:pressGesture];

    return self;
}


- (void)itemPressed:(UILongPressGestureRecognizer *)gestureRecognizer
{
    if(gestureRecognizer.state == UIGestureRecognizerStateBegan)
    {
        self.alpha = .75;
    }
    else if(gestureRecognizer.state == UIGestureRecognizerStateEnded)
    {
        self.alpha = 1.0;
        ProductDetailView *detailView = [[ProductDetailView alloc] initWithFrame:_app._currentView.frame product:_product detailType:PRODUCT_MODIFY];
        detailView._shoppingScreenDelegate = (id<ShoppingScreenDelegate>)_app._shoppingScreenVC;
        [detailView show];
    }
}
@end
