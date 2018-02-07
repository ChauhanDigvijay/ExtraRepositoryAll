//
//  MapAnnotation.m
//  Raley's
//
//  Created by Billy Lewis on 9/24/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//


#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "Store.h"

@interface MapAnnotation : NSObject <MKAnnotation> 

@property (nonatomic, strong) NSString *_locationTitle;
@property (nonatomic, assign) CLLocationCoordinate2D _coordinate;
@property (nonatomic, strong) Store *_store;

- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate andStore:(Store *)store;

@end
