//
//  WebServiceError.h
//  LibiOS
//
//  Created by Bill Lewis on 10/10/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JsonObject.h"

@interface WebServiceError : JsonObject

@property (nonatomic, assign)int errorCode;
@property (nonatomic, strong)NSString *errorMessage;

- (id)initWithErrorCode:(int)code andMessage:(NSString *)message;

@end
