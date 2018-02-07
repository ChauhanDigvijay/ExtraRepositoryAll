//
//  ListCreateRequest.h
//  Raley's
//
//  Created by Bill Lewis on 12/11/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface ListCreateRequest : JsonObject

@property (nonatomic, strong)NSString *email;
@property (nonatomic, strong)NSString *accountId;
@property (nonatomic, strong)NSString *listName;

@end
