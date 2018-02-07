//
//  ItemsData.h
//  FBBrinkPosLibrary
//
//  Created by Saurabh Bisht on 1/25/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface ItemsData : NSObject
-(void)getAllGroups:(id)instance;
-(void)getAllItems:(id)instance;
-(void)getItems:(NSString*)itemIds instance:(id)instance;
@end
