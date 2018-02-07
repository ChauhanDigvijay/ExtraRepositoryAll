//
//  ProductRequest.h
//  Raley's
//
//  Created by Bill Lewis on 10/31/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface ProductRequest : JsonObject

@property (nonatomic, assign)int      storeNumber;
@property (nonatomic, strong)NSString *upc;

@end
