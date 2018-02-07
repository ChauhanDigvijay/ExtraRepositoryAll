//
//  ModelClass.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UIImageView+WebCache.h"
#import <UIKit/UIKit.h>
#import "Reachability.h"

@interface ModelClass : NSObject

+ (id)sharedManager;

@property(nonatomic,retain) NSMutableArray *itemDictArray;
@property (strong,nonatomic) NSString *strQuantityGlobal;
@property (assign,nonatomic)  NSInteger indexGlobal;

// nsuser default methods
-(void)saveUserDefaultData:(NSString *)value and:(NSString *)key;
-(NSString *)reterieveuserDefaultData:(NSString *)keyName;


// color code - hexa to rgb
-(UIColor *)colorWithHexString:(NSString *)stringToConvert;


// loader view
- (void)addLoadingView:(UIView *)view;
- (void)removeLoadingView:(UIView *)view;


// textfield padding method
-(void)addLeftPaddingToTextField:(UITextField*)textField;


//check network connection method
-(BOOL)checkNetworkConnection;


//background color method
-(void)backgroundColor:(UIView *)view color : (UIColor *)color;

@end
