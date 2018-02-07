//
//  REST.h
//  clp register
//
//  Created by VT001 on 20/02/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Company.h"
#import "Customer.h"

@interface REST : NSObject
-(void)getCompanyDetails:(void(^)(Company *response))onCompletion;
-(void)registerCustomerInfo:(Customer *)info completion:(void(^)(NSMutableDictionary *message))onCompletion;
@end