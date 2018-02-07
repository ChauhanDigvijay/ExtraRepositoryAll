//
//  WebServiceError.m
//  LibiOS
//
//  Created by Bill Lewis on 10/10/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "WebServiceError.h"

@implementation WebServiceError

@synthesize errorCode;
@synthesize errorMessage;


- (id)initWithErrorCode:(int)code andMessage:(NSString *)message
{
    self = [super init];

    if(self)
    {
        errorCode = code;
        errorMessage = message;
    }

    return self;
}
@end
