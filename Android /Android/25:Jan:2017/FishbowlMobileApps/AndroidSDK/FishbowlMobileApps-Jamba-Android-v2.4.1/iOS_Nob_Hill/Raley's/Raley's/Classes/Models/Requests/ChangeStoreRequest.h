//
//  ChangeStoreRequest.h
//  Raley's
//
//  Created by Bill Lewis on 12/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface ChangeStoreRequest : JsonObject

@property (nonatomic, strong)NSString *accountId;
@property (nonatomic, assign)int      storeNumber;

@end
