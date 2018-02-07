//
//  WebService.m
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "WebService.h"
#import "Logging.h"
#import "Utility.h"
#import "SBJson.h"

@implementation WebService


- (id) initWithListener:(id)listener responseClassName:(NSString *)responseClassName
{
    self = [super init];
    if(self){
        _listener = listener;
        _responseClassName = responseClassName;
    }
    return self;
}


- (void)execute:(NSString *)url authorization:(NSString *)auth requestObject:(JsonObject *)requestObj requestType:(NSString *)type
{
    _responseObjectClass = objc_getClass([_responseClassName cStringUsingEncoding:NSASCIIStringEncoding]);

    if([Utility isNetworkAvailable])
    {
        LogDebug(@"url = %@", url);
        NSURL *requestUrl = [NSURL URLWithString:url];
        _request = [ASIHTTPRequest requestWithURL:requestUrl];
        if(_listener){
            [_request setDelegate:self];
        }else{
            [_request setDelegate:nil];
        }
        
        [_request setTimeOutSeconds:HTTP_TIME_OUT];
        [_request setValidatesSecureCertificate:YES];
        
        if(auth != nil)
            [_request addRequestHeader:AUTH value:auth];
        
        if([type isEqualToString:GET])
        {
            [_request setRequestMethod:GET];
        }
        else if([type isEqualToString:POST])
        {
            [_request addRequestHeader:CONTENT_TYPE value:CONTENT_TYPE_VALUE];
            [_request setRequestMethod:POST];
        }
        else // PUT
        {
            [_request addRequestHeader:CONTENT_TYPE value:CONTENT_TYPE_VALUE];
            [_request setRequestMethod:PUT];
        }
        
        if(requestObj != nil)
        {
            //NSString *json = [requestObj objectToJson];
            //LogInfo("SATAN: Request Content: %@", json);
            [_request appendPostData:(NSMutableData *)[[requestObj objectToJson] dataUsingEncoding:[NSString defaultCStringEncoding]]];
        }
        
        //[ASIHTTPRequest showNetworkActivityIndicator];
        [_request startAsynchronous];
    }
    else
    {
        LogError(@"Network is not available");
        _httpStatusCode = 422;
        _error = [[WebServiceError alloc] initWithErrorCode:0 andMessage:INTERNET_UNAVAILABLE];
        _responseObject = [_responseObjectClass objectForDictionary:nil];

        if(_listener != nil && [_listener respondsToSelector:@selector(onServiceResponse:)])
            [_listener onServiceResponse:_responseObject];
    }
}


- (void)requestFinished:(ASIHTTPRequest *)request
{
    [self handleRequest:request];
}


- (void)handleRequest:(ASIHTTPRequest *)request
{
    @synchronized(self)
    {
        //[ASIHTTPRequest hideNetworkActivityIndicator];
        NSError *error = [request error];
        _httpStatusCode = request.responseStatusCode;
        LogInfo(@"httpStatusCode: %d", _httpStatusCode);
        //LogInfo(@"%@", request.responseString);

        if(![Utility isEmpty:error])
            [Utility logError:error];

        SBJsonParser *parser = [[SBJsonParser alloc] init];
        _responseObject = nil;

        NSString *response = request.responseString;
        response = [response stringByReplacingOccurrencesOfString:@"/Jpg_90/" withString:@"/Jpg_400/"];
        //LogInfo(@"JSON Response Content: %@",response);
        
//        NSDictionary *jsonContent = [parser objectWithString:request.responseString error:nil];
        NSDictionary *jsonContent = [parser objectWithString:response error:nil];
        parser = nil;

        if(_httpStatusCode == 200)
        {
            if(![Utility isEmpty:jsonContent])
            {
                
                _responseObject = [_responseObjectClass objectForDictionary:jsonContent];
            }
            else
            {
                _responseObject = [_responseObjectClass objectForDictionary:nil];
                LogError(@"JSON Exception parsing HTTP response");
            }
        }
        else if(_httpStatusCode == 422)
        {
            id errorObject = [WebServiceError.class objectForDictionary:jsonContent];
            _error = (WebServiceError *)errorObject;
        }
        else if(_httpStatusCode == 0)
        {
            _httpStatusCode = 422;
            _error = [[WebServiceError alloc] initWithErrorCode:0 andMessage:[NSString stringWithFormat:@"%@.", [error localizedDescription]]];
        }

        if(_httpStatusCode != 200)
            _responseObject = [_responseObjectClass objectForDictionary:nil];

        if(_listener != nil && [_listener respondsToSelector:@selector(onServiceResponse:)])
            [_listener onServiceResponse:_responseObject];
    } // end @synchronized
}


- (void)requestFailed:(ASIHTTPRequest *)request
{
    //[ASIHTTPRequest hideNetworkActivityIndicator];
    NSError *error = [request error];

    if([error code] == ASIRequestTimedOutErrorType)
    {
        _httpStatusCode = 408;
    }
    else
    {
        _httpStatusCode = [request responseStatusCode];

        if(![Utility isEmpty:error])
            [Utility logError:error];
    }

    LogError(@"httpStatusCode: %d", _httpStatusCode);
    _responseObject = nil;
    _responseObject = [_responseObjectClass objectForDictionary:nil];

    if(_listener != nil && [_listener respondsToSelector:@selector(onServiceResponse:)])
        [_listener onServiceResponse:_responseObject];
}


- (id)getResponseObject
{
    return _responseObject;
}


- (int)getHttpStatusCode
{
    return _httpStatusCode;
}


- (WebServiceError *)getError
{
    return _error;
}


@end
