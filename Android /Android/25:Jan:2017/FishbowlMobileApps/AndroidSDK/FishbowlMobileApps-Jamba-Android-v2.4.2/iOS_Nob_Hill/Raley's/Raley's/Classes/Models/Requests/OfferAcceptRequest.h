//
//  OfferAcceptRequest.h
//  Raley's
//
//  Created by Bill Lewis on 3/11/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface OfferAcceptRequest : JsonObject

@property (nonatomic, strong)NSString *crmNumber;
@property (nonatomic, strong)NSString *acceptGroup;
// New
@property (nonatomic, strong)NSString *offerId;

@property (nonatomic, strong)NSString *endDate;
@property (nonatomic, strong)NSString *promoCode;
@property (nonatomic, strong)NSString *title;
@property (nonatomic, readwrite) BOOL dynamicOffer;


@end
