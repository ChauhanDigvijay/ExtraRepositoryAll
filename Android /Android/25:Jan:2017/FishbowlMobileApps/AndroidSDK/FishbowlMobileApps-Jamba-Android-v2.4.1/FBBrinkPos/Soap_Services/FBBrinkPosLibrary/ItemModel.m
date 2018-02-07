//
//  ItemModel.m
//  FBBrinkPosLib
//
//  Created by Saurabh Bisht on 1/25/16.
//  Copyright © 2016 Saurabh Bisht. All rights reserved.
//

#import "ItemModel.h"



@implementation ItemModel

+ (id)sharedManager {
    static ItemModel *sharedMyManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedMyManager = [[self alloc] init];
        
    });
    return sharedMyManager;
}


- (id)init {
    if (self == [super init]) {
    }
    return self;
}
@end
