//
//  WebService.h
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ASIHTTPRequest.h"
#import "JsonObject.h"
#import "WebServiceError.h"
#import "WebServiceListener.h"

#define GET @"GET"
#define POST @"POST"
#define PUT @"PUT"

#define HTTP_TIME_OUT 15
#define AUTH @"authKey"
#define CONTENT_TYPE @"Content-Type"
#define CONTENT_TYPE_VALUE @"application/json"
#define ERROR_RESPONSE_CLASS @"WebServiceError"

#define INTERNET_UNAVAILABLE @"Internet connection not available"

@interface WebService : NSObject
{
    id <WebServiceListener> _listener;
    NSString                *_responseClassName;
    ASIHTTPRequest          *_request;
    Class                   _responseObjectClass;
    WebServiceError         *_error;
    id                      _responseObject;
    int                     _httpStatusCode;
}

- (id) initWithListener:(id)listener responseClassName:(NSString *)responseClassName;
- (void)execute:(NSString *)url authorization:(NSString *)auth requestObject:(JsonObject *)requestObj requestType:(NSString *)type;
- (int)getHttpStatusCode;
- (id)getResponseObject;
- (WebServiceError *)getError;

@end
