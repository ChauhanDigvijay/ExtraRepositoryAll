//
//  WebServiceListener.h
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>


@protocol WebServiceListener <NSObject>

- (void)onServiceResponse:(id)responseObject;

@end
