//
//  Downloader.m
//  clp register
//
//  Created by VT001 on 20/02/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import "Downloader.h"
#import "AFNetworking.h"
@implementation Downloader

-(void)downloadImage:(NSString *)httpurl completion:(void(^)(NSString *filePath))onCompletion{
    @try {
        if(!httpurl || httpurl.length==0){
            onCompletion(nil);
            return;
        }
        NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
        AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
        
        NSURL *URL = [NSURL URLWithString:[httpurl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
        NSURLRequest *request = [NSURLRequest requestWithURL:URL];
        
        NSURLSessionDownloadTask *downloadTask = [manager downloadTaskWithRequest:request progress:nil destination:^NSURL *(NSURL *targetPath, NSURLResponse *response) {
            NSURL *documentsDirectoryURL = [[NSFileManager defaultManager] URLForDirectory:NSDocumentDirectory inDomain:NSUserDomainMask appropriateForURL:nil create:NO error:nil];
            return [documentsDirectoryURL URLByAppendingPathComponent:[response suggestedFilename]];
        } completionHandler:^(NSURLResponse *response, NSURL *filePath, NSError *error) {
            NSLog(@"File downloaded to: %@", filePath);
            dispatch_async(dispatch_get_main_queue(), ^{
                if(filePath){
                    onCompletion([filePath path]);
                }else{
                    onCompletion(nil);
                }
            });
        }];
        [downloadTask resume];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception);
        dispatch_async(dispatch_get_main_queue(), ^{
            onCompletion(nil);
        });
    }
}

@end
