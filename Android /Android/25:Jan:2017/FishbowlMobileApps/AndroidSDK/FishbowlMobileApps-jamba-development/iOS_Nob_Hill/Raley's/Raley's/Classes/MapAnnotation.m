//
//  MapAnnotation.m
//  Raley's
//
//  Created by Billy Lewis on 9/24/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//


#import "MapAnnotation.h"

@implementation MapAnnotation

@synthesize _coordinate;
@synthesize _locationTitle;
@synthesize _store;


- (id)initWithCoordinate:(CLLocationCoordinate2D)coordinate andStore:(Store *)store
{
    _store = store;
	_coordinate.latitude = coordinate.latitude;
    _coordinate.longitude = coordinate.longitude;
	_locationTitle = store.chain;
    return self;
}


- (CLLocationCoordinate2D)coordinate
{
	return _coordinate;
}


- (NSString *)title
{
    return _locationTitle;
}


@end
