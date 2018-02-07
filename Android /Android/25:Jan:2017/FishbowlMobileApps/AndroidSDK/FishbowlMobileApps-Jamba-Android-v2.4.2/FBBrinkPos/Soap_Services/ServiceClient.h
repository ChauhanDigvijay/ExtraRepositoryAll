//
//  ServiceClient.h
//  ServiceClient
//
//  Created by Saurabh Bisht on 1/25/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//
#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>


#pragma mark protocol
@protocol serviceClientDelegate
-(void)callBackWithData:(NSMutableArray *)_data;
@end
#pragma end



@interface ServiceClient : NSObject<NSXMLParserDelegate>{
    id <NSObject, serviceClientDelegate > delegate;
}
@property (retain) id <NSObject, serviceClientDelegate > delegate;

-(void)callSoapServiceWithParameters__functionName:(NSString*)___functionName tags:(NSMutableArray*)___tags vars:(NSMutableArray*)___vars wsdlURL:(NSString*)___url service:(NSString*)___serviceName;
-(void)callSoapServiceWithoutParameters__functionName:(NSString*)___functionName wsdlURL:(NSString*)___url;

@end
