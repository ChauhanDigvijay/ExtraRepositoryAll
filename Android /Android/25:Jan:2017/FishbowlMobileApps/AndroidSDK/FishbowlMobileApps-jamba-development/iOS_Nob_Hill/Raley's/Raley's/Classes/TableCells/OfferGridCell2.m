//
//  OfferGridCell.m
//  Raley's
//
//  Created by Bill Lewis on 11/15/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "OfferGridCell.h"
#import "Logging.h"
#import "Utility.h"

#define GRIDCELL_PADDING 0.05 // In Percentage


@implementation OfferGridCell

@synthesize _shoppingScreenDelegate;
@synthesize _offerImageView;
@synthesize _acceptOfferButton;
@synthesize _consumerTitleLabel;
@synthesize _consumerDescriptionLabel;
@synthesize _offerLimitLabel;
@synthesize _endDateLabel;
@synthesize _acceptedOfferLabel;
@synthesize _offer;
@synthesize _parent;


- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];

    if(self)
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        _shoppingScreenDelegate = (id<ShoppingScreenDelegate>)_app._shoppingScreenVC;
        int width = frame.size.width;
        int height = frame.size.height;
        int textXOrigin = width * GRIDCELL_PADDING;
        int textWidth = width - (textXOrigin * 2);
        int imageSize = height * .2;

        UIImageView *background = [[UIImageView alloc] initWithFrame:frame];
//        [background setImage:[UIImage imageNamed:@"product_cell"]];
        [background setBackgroundColor:[UIColor whiteColor]];
//        background.layer.cornerRadius = 5;
        [self.layer setCornerRadius:5];
        background.clipsToBounds = YES;
        self.backgroundView = background;
        
        _offerImageView = [[UIImageView alloc] initWithFrame:CGRectMake((width - imageSize) / 2, height * .01, imageSize, imageSize)];
        [self addSubview:_offerImageView];

//        _consumerTitleLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .21, textWidth, height * .27) backGroundColor:[UIColor clearColor]];
        _consumerTitleLabel = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, height * .21, textWidth, height * .27)];
        _consumerTitleLabel.numberOfLines = 0;
        _consumerTitleLabel.textColor = [UIColor blackColor];
        _consumerTitleLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_consumerTitleLabel];

//        _consumerDescriptionLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .47, textWidth, height * .24) backGroundColor:[UIColor clearColor]];
        _consumerDescriptionLabel = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, height * .47, textWidth, height * .24)];
        _consumerDescriptionLabel.numberOfLines = 3;
        _consumerDescriptionLabel.textColor = [UIColor blackColor];
        _consumerDescriptionLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_consumerDescriptionLabel];

//        _offerLimitLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .70, textWidth, height * .08) backGroundColor:[UIColor clearColor]];
        _offerLimitLabel = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, height * .70, textWidth, height * .08)];
        _offerLimitLabel.numberOfLines = 0;
        _offerLimitLabel.textColor = [UIColor blackColor];
        _offerLimitLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_offerLimitLabel];

//        _endDateLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .77, textWidth, height * .08) backGroundColor:[UIColor clearColor]];
        _endDateLabel = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, height * .77, textWidth, height * .08)];
        _endDateLabel.numberOfLines = 0;
        _endDateLabel.textColor = [UIColor blackColor];
        _endDateLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_endDateLabel];

        int acceptedHeight = height * .13;
        int acceptedWidth = width * .8;
        int acceptedXOrigin = width * .1;

//        UIImageView *labelBackground = [[UIImageView alloc] initWithFrame:CGRectMake(acceptedXOrigin, height * .85, acceptedWidth, acceptedHeight)];
//        labelBackground.image = [UIImage imageNamed:@"offer_container_button"];
//        [self addSubview:labelBackground];

//        _acceptedOfferLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(acceptedXOrigin, height * .85 + (acceptedHeight * .1), acceptedWidth, acceptedHeight * .8) backGroundColor:[UIColor clearColor]];
        _acceptedOfferLabel = [[UILabel alloc]initWithFrame:CGRectMake(acceptedXOrigin, height * .85 + (acceptedHeight * .1), acceptedWidth, acceptedHeight * .8)];
        _acceptedOfferLabel.numberOfLines = 0;
        _acceptedOfferLabel.textColor = [UIColor whiteColor];
        _acceptedOfferLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_acceptedOfferLabel];
        
//        [_acceptedOfferLabel setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"offer_container_button"]]];
//        [_acceptedOfferLabel setBackgroundColor:[UIColor colorWithRed:187.0/255.0 green:0.0 blue:0.0 alpha:0.8]];
        [_acceptedOfferLabel setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1]];
        [_acceptedOfferLabel.layer setCornerRadius:3.0];
        [_acceptedOfferLabel setClipsToBounds:YES];

        //_acceptOfferButton = [[UIButton alloc] initWithFrame:CGRectMake(acceptedXOrigin, height * .85, acceptedWidth, acceptedHeight)];
        _acceptOfferButton = [[UIButton alloc] initWithFrame:CGRectMake(acceptedXOrigin, height * .85 + (acceptedHeight * .1), acceptedWidth, acceptedHeight * .8)];
        [_acceptOfferButton setBackgroundImage:[UIImage imageNamed:@"offer_container_button"] forState:UIControlStateNormal];
        _acceptOfferButton.titleLabel.font =[UIFont fontWithName:_app._normalFont size:font_size13];
        [_acceptOfferButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_acceptOfferButton setTitle:@"Accept This Offer" forState:UIControlStateNormal];
        [_acceptOfferButton addTarget:self action:@selector(acceptButtonPressed) forControlEvents:UIControlEventTouchUpInside];
        [_acceptOfferButton.layer setCornerRadius:3.0];
        [_acceptOfferButton setClipsToBounds:YES];
        [self addSubview:_acceptOfferButton];
        
        UITapGestureRecognizer *single_tap  = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(show_detail)];
        [single_tap setNumberOfTapsRequired:1];
        [single_tap setNumberOfTouchesRequired:1];
        [self addGestureRecognizer:single_tap];
    }

    return self;
}


- (void)acceptButtonPressed
{
    [_shoppingScreenDelegate acceptOffer:_offer];
}

-(void)show_detail{
    @try {
        OfferDetailViewController *offer_detail = [[OfferDetailViewController alloc]initWithNibName:@"OfferDetailViewController" _offer:_offer _enddate:_endDateLabel.text];
    [offer_detail setDelegate:self];
        [Utility Viewcontroller:self.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
        [_parent presentViewController:offer_detail animated:NO completion:nil];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
   
}

// Accept the offer
-(void)accept_offer{
    [_shoppingScreenDelegate acceptOffer:_offer];
}

@end
