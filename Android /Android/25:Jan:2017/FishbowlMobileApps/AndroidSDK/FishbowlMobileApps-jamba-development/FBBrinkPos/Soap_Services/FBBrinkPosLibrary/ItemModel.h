//
//  ItemModel.h
//  FBBrinkPosLib
//
//  Created by Saurabh Bisht on 1/25/16.
//  Copyright © 2016 Saurabh Bisht. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ItemModel : NSObject
+ (id)sharedManager;
@property(nonatomic,strong)NSString*serviceType;
@property(nonatomic,strong)NSString*methodName;
@property(nonatomic,strong)NSArray* itemsArray;
@property(nonatomic,strong)NSMutableArray *itemsDictionary;
@end
