//
//  MobileSetting.h
//  clpsdk
//
//  Created by VT02 on 1/2/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CLPMobileSetting : NSObject

@property (nonatomic, readwrite) long              settingId;
@property (nonatomic, readwrite) long              companyId;
@property (nonatomic, readwrite) NSString          *settingName;
@property (nonatomic, readwrite) NSString          *settingValue;
@property (nonatomic, readwrite) NSString          *status;

@end
