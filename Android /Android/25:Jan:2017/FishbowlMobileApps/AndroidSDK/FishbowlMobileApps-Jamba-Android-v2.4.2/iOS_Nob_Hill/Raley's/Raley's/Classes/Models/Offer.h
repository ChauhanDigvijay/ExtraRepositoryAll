//
//  Offer.h
//  Raley's
//
//  Created by Billy Lewis on 10/3/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface Offer : JsonObject

@property (nonatomic, strong)NSString *offerType;
@property (nonatomic, strong)NSString *offerProductImage;
@property (nonatomic, strong)NSString *consumerDesc;
@property (nonatomic, strong)NSString *consumerDetails;
@property (nonatomic, strong)NSString *offerSize;
@property (nonatomic, strong)NSString *offerLimit;
@property (nonatomic, strong)NSString *offerId;
@property (nonatomic, strong)NSString *offeridLive;
@property (nonatomic, strong)NSString *offerProductImageFile;
@property (nonatomic, assign)float    offerPrice;
@property (nonatomic, strong)NSString *offerLogoImageFile;
@property (nonatomic, strong)NSString *acceptGroup;
@property (nonatomic, strong)NSString *consumerTitle;
@property (nonatomic, strong)NSString *offerOrderNumber;
@property (nonatomic, strong)NSString *offerListType;
@property (nonatomic, strong)NSString *startDate;
@property (nonatomic, strong)NSString *offerProductImageAlt;
@property (nonatomic, strong)NSString *candidateGroup;
@property (nonatomic, strong)NSString *offerLogoImageAlt;
@property (nonatomic, strong)NSString *offerLogoImage;
@property (nonatomic, strong)NSString *active;
@property (nonatomic, strong)NSString *hideAccept;
@property (nonatomic, strong)NSString *endDate;
@property (nonatomic, strong)NSArray  *offerList;
@property (nonatomic, assign)BOOL     _acceptedOffer; // not used by json
@property (nonatomic, assign)BOOL     _acceptableOffer; // not used by json
@property (nonatomic, strong)NSString *promoCode;
@property (nonatomic, readwrite) BOOL dynamicOffer;

@end
