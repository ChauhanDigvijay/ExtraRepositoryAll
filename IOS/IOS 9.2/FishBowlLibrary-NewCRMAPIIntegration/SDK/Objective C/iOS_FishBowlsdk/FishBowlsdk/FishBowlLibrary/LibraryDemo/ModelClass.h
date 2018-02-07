//
//  ModelClass.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>


@interface ModelClass : NSObject<UIGestureRecognizerDelegate>


+ (id)sharedManager;


// ====================================== UserDefault Method ===================================== //

# pragma mark - save methods
-(void)saveUserDefaultData:(NSString *)value and:(NSString *)key;

# pragma mark - reterieve methods
-(NSString *)reterieveuserDefaultData:(NSString *)keyName;

// ====================================== hexa to rgb ========================================== //

# pragma mark - color code_hexa to rgb
-(UIColor *)colorWithHexString:(NSString *)stringToConvert;


// ====================================== textfield padding =======================================//

# pragma mark - textfield padding method
-(void)addLeftPaddingToTextField:(UITextField*)textField;


//====================================== network connection ======================================//

# pragma mark - check network connection method
-(BOOL)checkNetworkConnection;





@end
