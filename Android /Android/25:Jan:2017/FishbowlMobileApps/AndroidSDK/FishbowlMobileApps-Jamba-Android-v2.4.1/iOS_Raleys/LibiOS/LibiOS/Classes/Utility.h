//
//  Utility.h
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_OPTIONS(NSUInteger,ViewCATransitions)
{
    ViewCATransitionsNONE=0,
    ViewCATransitionsLEFT=1,
    ViewCATransitionsRIGHT=2
};
@interface Utility : NSObject

+ (BOOL)isEmpty:(id)object;
+ (BOOL)isNetworkAvailable;
+ (void)logAvailableFonts;
+ (UIFont *)fontForFamily:(NSString *)family andHeight:(int)height;
+ (UIFont *)fontForSize:(NSString *)fontFamily forString:(NSString *)string forSize:(CGSize)size;
+ (NSArray *)wrapText:(NSString *)text forFont:(UIFont *)font forWidth:(int)width;
+ (BOOL)validateEmail:(NSString *)email;
+ (void)logError:(NSError *)error;
+ (CGSize)get_image_width_and_height:(NSString*)path;
+(void)Viewcontroller:(CALayer*)window_layer _AnimationStyle:(ViewCATransitions)Style;
+(CGSize)getUILabelFontSizeBasedOnText_width:(CGFloat)width _fontname:(NSString*)font _fontsize:(CGFloat)fsize _text:(NSString*)text;

+(void)updateChildOriginY_Parent:(id)parent _Child:(id)child _Gap:(CGFloat)gap;
@end
