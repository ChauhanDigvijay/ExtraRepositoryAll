//
//  Database.h
//  clpsdk
//
//  Created by VT001 on 29/01/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CLPCustomer.h"

@interface Database: NSObject <NSCoding>

@property (nonatomic, strong) CLPCustomer *customer;

@end
