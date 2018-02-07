//
//  MissingImagesRequest.h
//  Raley's
//
//  Created by Bill Lewis on 3/12/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface MissingImagesRequest : JsonObject

@property (nonatomic, strong)NSArray *imageUrlList;

@end
