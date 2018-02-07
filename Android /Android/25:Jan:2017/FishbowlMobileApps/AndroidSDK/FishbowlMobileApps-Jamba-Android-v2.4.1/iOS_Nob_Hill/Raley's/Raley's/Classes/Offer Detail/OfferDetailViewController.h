//
//  OfferDetailViewController.h
//  Raley's
//
//  Created by VT01 on 28/05/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HeaderView.h"
#import "Offer.h"
#import "WebService.h"
#import "ShoppingScreenDelegate.h"

@protocol OfferDetailDelegate <NSObject>

@required
-(void)accept_offer;
@end

@interface OfferDetailViewController : UIViewController <HeaderViewDelegate, WebServiceListener>
{
    IBOutlet UIScrollView *container;
    
    IBOutlet UILabel *htitle;
    IBOutlet UILabel *desc;
    IBOutlet UILabel *limit;
    IBOutlet UILabel *enddate;
    IBOutlet UILabel *accept_offer_lbl;
    IBOutlet UIButton *accept_offer;
    IBOutlet UIImageView *accept_offer_img;
    IBOutlet UIImageView *imageview;
    
    UIView      *img_alert_container;
    UIImageView *alert_image;
    /*
     _consumerTitleLabel
     _consumerDescriptionLabel
     _offerLimitLabel
     _endDateLabel
     _acceptedOfferLabel
     _acceptOfferButton
     */
    

}
@property(nonatomic, retain) id<OfferDetailDelegate> delegate;
@property(nonatomic, retain) id<ShoppingScreenDelegate> shoppingDelegate;


- (id)initWithNibName:(NSString *)nibNameOrNil _offer:(Offer *)offer _enddate:(NSString*)ed;
@end
