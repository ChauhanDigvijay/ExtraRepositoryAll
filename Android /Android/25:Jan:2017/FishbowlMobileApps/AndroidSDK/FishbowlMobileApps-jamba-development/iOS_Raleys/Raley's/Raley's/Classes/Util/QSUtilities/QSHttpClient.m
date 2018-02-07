//
//  QSHttpClient.m
//  NextManga
//
//  Created by Mike Ho on 3/14/11.
//  Copyright 2011 Quasidea Development, LLC. All rights reserved.
//

#import "QSUtilities.h"

@interface QSHttpClient (private) 
- (void)sendWithData:(id)objRequestContent StreamFlag:(bool)blnStreamFlag;
@end

@implementation QSHttpClient

@synthesize _strUrl;
@synthesize _strHttpMethod;
@synthesize _intTimeoutInterval;

@synthesize _intHttpStatusCode;
@synthesize _objResponseData;

@synthesize _intTag;
@synthesize _intArbitraryIdentifier;

@synthesize _objDelegate;

@synthesize cache;

#pragma mark -
#pragma mark Initializers and Housekeeping

- (QSHttpClient *)initWithUrl:(NSString *)strUrl HttpMethod:(NSString *)strHttpMethod {
	if ([self init]) {
		[self setUrl:strUrl];
		[self setHttpMethod:strHttpMethod];
		[self setTimeoutInterval:60];
		[self setCache:YES];
		
		return self;
	}
	
	return nil;
}

- (void)cleanupFromPreviousRequests {
	if (_objResponseData != nil) {
		[_objResponseData release];
		_objResponseData = nil;
	}
	
	_intHttpStatusCode = 0;
}

#pragma mark -
#pragma mark Pubic Execution Methods

- (void)sendString:(NSString *)strRequest {
	[self sendWithData:[strRequest dataUsingEncoding:NSUTF8StringEncoding] StreamFlag:false];
}

- (void)sendFile:(NSString *)strFilePath {
	_intRequestDataSize = [QSFileManager fileSize:strFilePath];
	[self sendWithData:[NSInputStream inputStreamWithFileAtPath:strFilePath] StreamFlag:true];
}

#pragma mark -
#pragma mark Private Helpers


- (void)sendWithData:(id)objRequestContent StreamFlag:(bool)blnStreamFlag {
	// Cleanup from Previous Requests (if applicable)
	[self cleanupFromPreviousRequests];
	
	// Setup the Response Data Placeholder
	_objResponseData = [[NSMutableData alloc] init];
		
	// Generate the Request
    
	NSURL * objUrl = [[NSURL alloc] initWithString:_strUrl];
	NSMutableURLRequest * objRequest = [[NSMutableURLRequest alloc] initWithURL:objUrl];

    
//    NSLog(@"_strUrl==%@",_strUrl);

//    [AFJSONRequestOperation addAcceptableContentTypes:[NSSet setWithObject:@"text/html"]];
//    NSURLRequest *request = [NSURLRequest requestWithURL:objUrl];
//    AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) {
//        NSLog(@"Pass Response = %d", [[JSON objectForKey:@"clips"] count]);
//    } failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON) {
//        NSLog(@"error==%@",error);
//        NSLog(@"Failed Response : %@", JSON);
//    }];
//    [operation start];    

	if (!self.cache) {
		objRequest.cachePolicy = NSURLRequestReloadIgnoringCacheData;
	}
	
	[objRequest setTimeoutInterval:_intTimeoutInterval];
	[objRequest setHTTPMethod:_strHttpMethod];
	
	if (blnStreamFlag) {
		[objRequest setHTTPBodyStream:objRequestContent];
	} else {
		[objRequest setHTTPBody:objRequestContent];
	}
	
	NSURLConnection * objConnection = [[NSURLConnection alloc] initWithRequest:objRequest delegate:self startImmediately:NO];
	
	if ([UIApplication sharedApplication].applicationState != UIApplicationStateBackground) {
		// Perform the Request
		[objConnection scheduleInRunLoop:[NSRunLoop currentRunLoop] forMode:NSRunLoopCommonModes];
		[objConnection start];
		[objConnection release];
	} else if (NO) {
		NSURLResponse* response = nil;
		NSError* error = nil;
		NSData* responseData = [NSURLConnection sendSynchronousRequest:objRequest returningResponse:&response error:&error];
		
		if (error) {
			[self connection:objConnection didFailWithError:error];
		} else {
			[self connection:objConnection didReceiveResponse:response];
			[self connection:objConnection didReceiveData:responseData];
			[self connectionDidFinishLoading:objConnection];
		}
		
		[response release];
		[error release];
		[responseData release];
	}
	
	// Cleanup
	[objUrl release];
	[objRequest release];
}

#pragma mark -
#pragma mark Response Getters

- (NSString *)getResponseAsString {
	return [[[NSString alloc] initWithData:_objResponseData encoding:NSUTF8StringEncoding] autorelease];
}

- (NSData *)getResponseAsRawData {
	return [NSData dataWithData:_objResponseData];
}

#pragma mark -
#pragma mark Server Connection Delegate Handler

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
	if (_objDelegate && [(NSObject *)_objDelegate respondsToSelector:@selector(httpClient:ErrorReceived:)])
		[_objDelegate httpClient:self ErrorReceived:[error localizedDescription]];
}

- (void)connection:(NSURLConnection *)connection didSendBodyData:(NSInteger)bytesWritten totalBytesWritten:(NSInteger)totalBytesWritten totalBytesExpectedToWrite:(NSInteger)totalBytesExpectedToWrite
{
	if (_objDelegate && [(NSObject *)_objDelegate respondsToSelector:@selector(httpClient:RequestProgressPercentage:)]) {
		// Calculate Completion Percentage
		CGFloat fltComplete = 0;
		if (totalBytesExpectedToWrite > 0) {
			fltComplete = (1.0 * totalBytesWritten) / (1.0 * totalBytesExpectedToWrite);
		} else if (_intRequestDataSize > 0) {
			fltComplete = (1.0 * totalBytesWritten) / (1.0 * _intRequestDataSize);
		}

		[_objDelegate httpClient:self RequestProgressPercentage:fltComplete];
	}
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{

        
	// Check Status Code
	if ((_intHttpStatusCode >= 200) && (_intHttpStatusCode < 300)) {
		// Looks good!
		if (_objDelegate && [(NSObject *)_objDelegate respondsToSelector:@selector(httpClientResponseReceived:)])
			[_objDelegate httpClientResponseReceived:self];
		
	// Oops -- an HTTP status code indicating an issue / error
	} else {
		if (_objDelegate && [(NSObject *)_objDelegate respondsToSelector:@selector(httpClient:ErrorReceived:)])
			[_objDelegate httpClient:self ErrorReceived:[NSString stringWithFormat:NSLocalizedString(@"Received error status code '%d' from the server.", nil), _intHttpStatusCode]];
	}
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{

	if (![response respondsToSelector:@selector(statusCode)]) {
		_intHttpStatusCode = 0;
	} else {
		_intHttpStatusCode = [(NSHTTPURLResponse *)response statusCode];
	}
	
	if (![response respondsToSelector:@selector(expectedContentLength)]) {
		_intResponseDataSize = 0;
	} else {
		_intResponseDataSize = [response expectedContentLength];
	}
	
	if (_objDelegate && [(NSObject *)_objDelegate respondsToSelector:@selector(httpClient:ResponseProgressPercentage:)]) {
		[_objDelegate httpClient:self ResponseProgressPercentage:0.0f];
	}
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
 
	[_objResponseData appendData:data];
	
	// Update Progress
	CGFloat fltComplete = 0;
	if (_intResponseDataSize > 0) {
		fltComplete = (1.0 * [_objResponseData length]) / (1.0 * _intResponseDataSize);
	}
	
	if (_objDelegate && [(NSObject *)_objDelegate respondsToSelector:@selector(httpClient:ResponseProgressPercentage:)]) {
		[_objDelegate httpClient:self ResponseProgressPercentage:fltComplete];
	}
}


#pragma mark -
#pragma mark Class Lifecycle

- (void)dealloc {
	[self setUrl:nil];
	[self setHttpMethod:nil];
	[_objResponseData release];
	
	[super dealloc];
}

@end