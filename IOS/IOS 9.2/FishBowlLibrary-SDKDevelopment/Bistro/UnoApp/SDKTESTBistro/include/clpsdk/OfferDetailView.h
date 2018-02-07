//
//  OfferDetailView.h
//  clpsdk
//
//  Created by Gourav Shukla on 30/07/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "qrencode.h"



@interface OfferDetailView : UIViewController

@property(strong,nonatomic)NSString * loginImageURl;
@property(strong,nonatomic)NSString * promoCodevalue;
@property(strong,nonatomic)NSString * offersTitle;
@property(strong,nonatomic)NSString * offersDescription;
@property(strong,nonatomic)NSString * offerExpireDate;
@property(strong,nonatomic)NSString * offerImageIcon;


//- (UIImage *)quickResponseImageForString:(NSString *)dataString withDimension:(int)imageWidth;



@end
