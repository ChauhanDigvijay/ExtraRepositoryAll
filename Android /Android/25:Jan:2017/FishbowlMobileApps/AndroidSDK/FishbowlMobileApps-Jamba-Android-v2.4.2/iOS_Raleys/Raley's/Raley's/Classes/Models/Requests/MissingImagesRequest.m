//
//  MissingImagesRequest.m
//  Raley's
//
//  Created by Bill Lewis on 3/12/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "MissingImagesRequest.h"

@implementation MissingImagesRequest

@synthesize imageUrlList;


- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"imageUrlList"])
        return @"NSString";
    else
        return nil;
}

@end
