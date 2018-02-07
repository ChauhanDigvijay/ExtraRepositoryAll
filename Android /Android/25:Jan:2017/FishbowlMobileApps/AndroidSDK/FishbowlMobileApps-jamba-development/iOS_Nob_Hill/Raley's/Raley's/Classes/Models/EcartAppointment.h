//
//  EcartAppointment.h
//  Raley's
//
//  Created by Bill Lewis on 3/18/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface EcartAppointment : JsonObject

@property (nonatomic, assign)int      storeNumber;
@property (nonatomic, strong)NSString *appointmentDate;
@property (nonatomic, strong)NSArray  *appointmentTimeList;

@end
