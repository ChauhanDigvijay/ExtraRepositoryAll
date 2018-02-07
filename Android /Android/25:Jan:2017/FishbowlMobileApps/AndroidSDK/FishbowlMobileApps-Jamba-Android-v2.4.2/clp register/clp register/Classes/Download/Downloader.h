//
//  Downloader.h
//  clp register
//
//  Created by VT001 on 20/02/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface Downloader : NSObject
-(void)downloadImage:(NSString *)httpurl completion:(void(^)(NSString *filePath))onCompletion;
@end
