//
//  Utility.m
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "Utility.h"
#import "Reachability.h"
#import "Logging.h"
#import <ImageIO/ImageIO.h>


@implementation Utility


+ (BOOL)isEmpty:(id)object
{
    return object == nil || [object isKindOfClass:[NSNull class]] ||
           ([object respondsToSelector:@selector(length)] && [(NSData *)object length] == 0) ||
           ([object respondsToSelector:@selector(count)] && [(NSArray *)object count] == 0);
}


+ (BOOL)isNetworkAvailable
{
	Reachability *reachability = [Reachability reachabilityForInternetConnection];
	[reachability startNotifier];
	NetworkStatus netStatus = [reachability currentReachabilityStatus];
    
	if(netStatus == NotReachable) 
		return NO;
	else
		return YES;
    
}


+ (void)logAvailableFonts
{
    for(NSString *familyName in [UIFont familyNames])
    {
        for(NSString *fontName in [UIFont fontNamesForFamilyName:familyName])
            LogInfo(@"%@", fontName);
    }
}


+ (UIFont *)fontForFamily:(NSString *)family andHeight:(int)height
{
    int maxFontSize = 200;
    CGSize measuredSize;

    for(int i = 1; i < maxFontSize; i++)
    {
        measuredSize = [@"Tg" sizeWithFont:[UIFont fontWithName:family size:i]];  // covers max height above and below baseline

        if(measuredSize.height >= height)
            return [UIFont fontWithName:family size:i - 1];
    }

    return [UIFont fontWithName:family size:maxFontSize];
}


+ (UIFont *)fontForSize:(NSString *)fontFamily forString:(NSString *)string forSize:(CGSize)size
{
    int maxFontSize = 200;
    CGSize measuredSize;
    
    for(int i = 1; i < maxFontSize; i++)
    {
        measuredSize = [string sizeWithFont:[UIFont fontWithName:fontFamily size:i]];
        
        if(measuredSize.width >= size.width || measuredSize.height >= size.height)
            return [UIFont fontWithName:fontFamily size:i - 1];
    }
    
    return [UIFont fontWithName:fontFamily size:maxFontSize];
}


+ (NSArray *)wrapText:(NSString *)text forFont:(UIFont *)font forWidth:(int)width
{
    NSMutableArray *textLines = [[NSMutableArray alloc] init];
    NSRange range;
    CGSize size;
    NSString *subString = nil;
    int startIndex = 0;
    int lastValidIndex = 0;
    int index = 0;
    
    while(YES)
    {
        if(index < text.length)
        {
            if([text characterAtIndex:index] == ' ' || index == text.length - 1)
            {
                range = NSMakeRange(startIndex, index - startIndex);
                subString = [text substringWithRange:range];
                size = [subString sizeWithFont:font];
                
                if(size.width < width)
                {
                    lastValidIndex = index;
                }
                else
                {
                    if(lastValidIndex > startIndex)
                    {
                        range = NSMakeRange(startIndex, lastValidIndex - startIndex);
                        index = startIndex = lastValidIndex + 1;
                    }
                    else
                    {
                        range = NSMakeRange(startIndex, index - lastValidIndex);
                        lastValidIndex = startIndex = index;
                    }
                    
                    subString = [text substringWithRange:range];
                    [textLines addObject:subString];
                }
                
                if(index == text.length - 1)
                {
                    if(startIndex < index)
                    {
                        range = NSMakeRange(startIndex, text.length - startIndex);
                        subString = [text substringWithRange:range];
                        [textLines addObject:subString];
                    }
                }
            }
            
            index++;
        }
        else // end of text
        {
            break;
        }
        
    }
    
    NSArray *textArray = [NSArray arrayWithArray:textLines];
    return textArray;
}


+ (BOOL)validateEmail:(NSString *)email
{
    NSString *emailRegex = @"(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex]; 
    return [emailTest evaluateWithObject:email]; 
}


+ (void)logError:(NSError *)error
{
    if(![Utility isEmpty:error])
        LogError(@"code:%ld, domain:%@, localizedDescription:%@, localizedFailureReason:%@", (long)[error code], [error domain], [error localizedDescription], [error localizedFailureReason]);
}


+(CGSize)get_image_width_and_height:(NSString*)path
{
    if(path==nil){
        path = [[NSBundle mainBundle]pathForResource:@"shopping_bag" ofType:@"png"];
//        return CGSizeZero;
    }
    CGSize img_size;
    CGFloat width = 0.0f, height = 0.0f;
    NSURL *imageFileURL=imageFileURL=[NSURL fileURLWithPath:path];
    CGImageSourceRef imageSource = CGImageSourceCreateWithURL((__bridge CFURLRef)imageFileURL, NULL);
    if (imageSource == NULL) {
        // Error loading image
        img_size.width=width;
        img_size.height=height;
        return img_size;
    }
    
    CFDictionaryRef imageProperties = CGImageSourceCopyPropertiesAtIndex(imageSource, 0, NULL);
    if (imageProperties != NULL) {
        CFNumberRef widthNum  = CFDictionaryGetValue(imageProperties, kCGImagePropertyPixelWidth);
        if (widthNum != NULL) {
            CFNumberGetValue(widthNum, kCFNumberCGFloatType, &width);
        }
        CFNumberRef heightNum = CFDictionaryGetValue(imageProperties, kCGImagePropertyPixelHeight);
        if (heightNum != NULL) {
            CFNumberGetValue(heightNum, kCFNumberCGFloatType, &height);
        }
        CFRelease(imageProperties);
    }
    //  NSLog(@"Image dimensions: %.0f x %.0f px", width, height);
    img_size.width=width;
    
    img_size.height=height;

    return img_size;
}


+(void)Viewcontroller:(CALayer*)window_layer _AnimationStyle:(ViewCATransitions)Style
{
    
    CATransition *transition = [CATransition animation];
    transition.duration = 0.5;
    transition.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
    transition.type = kCATransitionPush;
    
    if(Style==ViewCATransitionsLEFT){
        transition.subtype = kCATransitionFromLeft;
    }
    else if(Style==ViewCATransitionsRIGHT){
        transition.subtype =kCATransitionFromRight;
    }
    else if(Style==ViewCATransitionsNONE){
        return;
    }
    [window_layer addAnimation:transition forKey:nil];
}


+(CGSize)getUILabelFontSizeBasedOnText_width:(CGFloat)width _fontname:(NSString*)font _fontsize:(CGFloat)fsize _text:(NSString*)text{
    CGSize size = CGSizeMake(width, 0);
    //    if([Utility isEmpty:text]){
    if(text.length<=0){
        return size;
    }
    CGSize constrainedSize = CGSizeMake(size.width, 99999);
    NSDictionary *attributesDictionary = [NSDictionary dictionaryWithObjectsAndKeys:
                                          [UIFont fontWithName:font size:fsize], NSFontAttributeName,
                                          nil];
    NSMutableAttributedString *string = [[NSMutableAttributedString alloc] initWithString:text attributes:attributesDictionary];
    CGRect requiredHeight = [string boundingRectWithSize:constrainedSize options:NSStringDrawingUsesLineFragmentOrigin context:nil];
    if (requiredHeight.size.width > size.width) {
        requiredHeight = CGRectMake(0,0, size.width, requiredHeight.size.height);
    }
    size.height = ceil(requiredHeight.size.height);//+10); // plus 10 for top and bottom gap of label
    return size;
}


+(void)updateChildOriginY_Parent:(id)parent _Child:(id)child _Gap:(CGFloat)gap{
    CGRect frame = CGRectZero;
    UIView *pview = (UIView*)parent;
    UIView *cview = (UIView*)child;
    
    frame = cview.frame;
    
    frame.origin.y = pview.frame.origin.y + pview.frame.size.height + gap;
    [cview setFrame:frame];
}




@end
