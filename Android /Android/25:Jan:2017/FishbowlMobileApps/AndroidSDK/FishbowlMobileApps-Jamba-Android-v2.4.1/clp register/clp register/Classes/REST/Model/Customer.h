//
//  Customer.h
//  clp register
//
//  Created by VT001 on 20/02/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JSONModel.h"
@interface Customer : JSONModel
@property(nonatomic,strong) NSString *emailID;
@property(nonatomic,strong) NSString *cellPhone;
@property(nonatomic,strong) NSString *firstName;
@property(nonatomic,strong) NSString *addressLine1;
@property(nonatomic,strong) NSString *customerTenantID;
@end