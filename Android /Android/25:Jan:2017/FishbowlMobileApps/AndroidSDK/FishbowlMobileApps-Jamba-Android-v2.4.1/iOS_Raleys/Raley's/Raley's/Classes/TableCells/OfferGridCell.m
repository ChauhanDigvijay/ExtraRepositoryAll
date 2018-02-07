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
@synthesize accepted_offer_img;

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
        _consumerTitleLabel = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, height * .21, textWidth, 28.0f)];
        _consumerTitleLabel.numberOfLines = 2;
        _consumerTitleLabel.textColor = [UIColor blackColor];
        _consumerTitleLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_consumerTitleLabel];

//        _consumerDescriptionLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .47, textWidth, height * .24) backGroundColor:[UIColor clearColor]];
        _consumerDescriptionLabel = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, height * .47, textWidth, height * .12)]; //.24
        _consumerDescriptionLabel.numberOfLines = 2;
        _consumerDescriptionLabel.textColor = [UIColor blackColor];
        _consumerDescriptionLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_consumerDescriptionLabel];

//        _offerLimitLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .70, textWidth, height * .08) backGroundColor:[UIColor clearColor]];
        _offerLimitLabel = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, height * .70, textWidth, height * .08)];
        _offerLimitLabel.numberOfLines = 1;
        _offerLimitLabel.textColor = [UIColor blackColor];
        _offerLimitLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_offerLimitLabel];

//        _endDateLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .77, textWidth, height * .08) backGroundColor:[UIColor clearColor]];
        _endDateLabel = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, height * .77, textWidth, height * .08)];
        _endDateLabel.numberOfLines = 1;
        _endDateLabel.textColor = [UIColor blackColor];
        _endDateLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:_endDateLabel];

        int acceptedHeight = height * .13;
        int acceptedWidth = width * .85; //0.8
        int acceptedXOrigin = width * .08; //0.1
        
        UIView *border_top=[[UIView alloc]init];
        [border_top setBackgroundColor:[UIColor lightGrayColor]];
        [border_top setAlpha:0.8];
       // [self addSubview:border_top];
//
//        accepted_offer_img=[[UIImageView alloc]initWithFrame:CGRectMake(acceptedXOrigin, height * .85 + (acceptedHeight * .1),25,25)];
//        accepted_offer_img.image=[UIImage imageNamed:@"product_checked_box.png"];
//        
//        [self addSubview:accepted_offer_img];
        
//        UIImageView *labelBackground = [[UIImageView alloc] initWithFrame:CGRectMake(acceptedXOrigin, height * .85, acceptedWidth, acceptedHeight)];
//        labelBackground.image = [UIImage imageNamed:@"offer_container_button"];
//        [self addSubview:labelBackground];

//        _acceptedOfferLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(acceptedXOrigin, height * .85 + (acceptedHeight * .1), acceptedWidth, acceptedHeight * .8) backGroundColor:[UIColor clearColor]];
        _acceptedOfferLabel = [[UIImageView alloc]initWithFrame:CGRectMake(acceptedXOrigin, height * .888 + (acceptedHeight * .1), acceptedWidth, 24)];
        [_acceptedOfferLabel.layer setCornerRadius:3.0f];
        _acceptedOfferLabel.clipsToBounds=YES;
//        _acceptedOfferLabel.numberOfLines = 0;
//        _acceptedOfferLabel.textColor = [UIColor whiteColor];
//        _acceptedOfferLabel.textAlignment = NSTextAlignmentCenter;
        [border_top setFrame:CGRectMake(0, height * .88 + (acceptedHeight * .1), width, 1)];
        
        
        //[_acceptedOfferLabel addSubview:border_top];
        
        [self addSubview:_acceptedOfferLabel];
//        [self insertSubview:border_top aboveSubview:_acceptedOfferLabel];
        
//        [_acceptedOfferLabel setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"offer_container_button"]]];
//        [_acceptedOfferLabel setBackgroundColor:[UIColor colorWithRed:187.0/255.0 green:0.0 blue:0.0 alpha:0.8]];
//        [_acceptedOfferLabel setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1]];
//        [_acceptedOfferLabel.layer setCornerRadius:3.0];
//        [_acceptedOfferLabel setClipsToBounds:YES];

        //_acceptOfferButton = [[UIButton alloc] initWithFrame:CGRectMake(acceptedXOrigin, height * .85, acceptedWidth, acceptedHeight)];
        _acceptOfferButton = [[UIButton alloc] initWithFrame:CGRectMake(acceptedXOrigin, height * .85 + (acceptedHeight * .1), acceptedWidth, 24)];
        [_acceptOfferButton setBackgroundColor:[UIColor colorWithRed:(204.0/255.0) green:(0.0/255.0) blue:(0.0/255.0) alpha:1.0] ];
        //       [_acceptOfferButton setBackgroundImage:[UIImage imageNamed:@"accept_this_offer.png"] forState:UIControlStateNormal];
        _acceptOfferButton.titleLabel.font =[UIFont fontWithName:_app._normalFont size:font_size13];
        [_acceptOfferButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_acceptOfferButton setTitle:@"Accept This Offer" forState:UIControlStateNormal];
        [_acceptOfferButton addTarget:self action:@selector(acceptButtonPressed) forControlEvents:UIControlEventTouchUpInside];
        [_acceptOfferButton.layer setCornerRadius:4];

        [_acceptOfferButton setClipsToBounds:YES];
        [self addSubview:_acceptOfferButton];
        
        UITapGestureRecognizer *single_tap  = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(show_detail)];
        [single_tap setNumberOfTapsRequired:1];
        [single_tap setNumberOfTouchesRequired:1];
        [self addGestureRecognizer:single_tap];
        
        
    }

    return self;
}

-(void)changeAcceptOfferButton{
    
    //    [_acceptOfferButton setTitle:@"" forState:UIControlStateNormal];
    //    [_acceptOfferButton setBackgroundImage:[UIImage imageNamed:@"offer_accepted.png"] forState:UIControlStateNormal];
    //    CGRect frame = _acceptOfferButton.frame;
    //    frame.size.width +=10;
    //    [_acceptOfferButton setFrame:frame];
    
    [_acceptOfferButton setHidden:YES];
    [_acceptedOfferLabel setHidden:NO];
    CGRect mainframe = [self frame];
    CGRect frame = _acceptedOfferLabel.frame;
    frame.origin.y = mainframe.size.height - 30;
    frame.size.height = 21;
    //     frame.size.height += 5;
    frame.size.width += 10;
    frame.origin.x -=10;
    [_acceptedOfferLabel setFrame:frame];
    // [_acceptedOfferLabel setBackgroundColor:[UIColor blueColor]];
    
    [_acceptedOfferLabel setImage:[UIImage imageNamed:@"offer_accepted.png"]];
    
    // change the values
    
    _offer._acceptedOffer = YES;
    _offer._acceptableOffer = NO;
}

- (void)acceptButtonPressed
{
    [self changeAcceptOfferButton];
    [_shoppingScreenDelegate acceptOffer:_offer];
}

-(void)show_detail{
    @try {
        OfferDetailViewController *offer_detail = [[OfferDetailViewController alloc]initWithNibName:@"OfferDetailViewController" _offer:_offer _enddate:_endDateLabel.text];
        [offer_detail setDelegate:self];
        offer_detail.shoppingDelegate=(id<ShoppingScreenDelegate>)_app._shoppingScreenVC;
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
   // [_shoppingScreenDelegate acceptOffer:_offer];
}

@end
