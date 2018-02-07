//
//  Offer.m
//  Raley's
//
//  Created by Billy Lewis on 10/3/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "Offer.h"

@implementation Offer

@synthesize offerType;
@synthesize offerProductImage;
@synthesize consumerDesc;
@synthesize consumerDetails;
@synthesize offerSize;
@synthesize offerLimit;
@synthesize offerId;
@synthesize offeridLive;
@synthesize offerProductImageFile;
@synthesize offerPrice;
@synthesize offerLogoImageFile;
@synthesize acceptGroup;
@synthesize consumerTitle;
@synthesize offerOrderNumber;
@synthesize offerListType;
@synthesize startDate;
@synthesize offerProductImageAlt;
@synthesize candidateGroup;
@synthesize offerLogoImageAlt;
@synthesize offerLogoImage;
@synthesize active;
@synthesize hideAccept;
@synthesize endDate;
@synthesize offerList;
@synthesize _acceptedOffer;
@synthesize _acceptableOffer;
@synthesize promoCode;

- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"offerList"])
        return @"Offer";
    else
        return nil;
}

@end
