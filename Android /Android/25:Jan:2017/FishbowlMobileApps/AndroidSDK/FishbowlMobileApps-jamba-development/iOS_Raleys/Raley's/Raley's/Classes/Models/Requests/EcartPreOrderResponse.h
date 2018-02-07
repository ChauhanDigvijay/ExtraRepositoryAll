//
//  EcartPreOrderResponse.h
//  Raley's
//
//  Created by Bill Lewis on 3/18/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "JsonObject.h"
#import "EcartAppointment.h"

@interface EcartPreOrderResponse : JsonObject

@property (nonatomic, strong)NSString *accountId;
@property (nonatomic, strong)NSString *listId;
@property (nonatomic, assign)int   sePoints;
@property (nonatomic, assign)float productPrice;
@property (nonatomic, assign)float salesTax;
@property (nonatomic, assign)float crv;
@property (nonatomic, assign)float fees;
@property (nonatomic, assign)float totalPrice;
@property (nonatomic, strong)NSArray *appointmentList;

@end
