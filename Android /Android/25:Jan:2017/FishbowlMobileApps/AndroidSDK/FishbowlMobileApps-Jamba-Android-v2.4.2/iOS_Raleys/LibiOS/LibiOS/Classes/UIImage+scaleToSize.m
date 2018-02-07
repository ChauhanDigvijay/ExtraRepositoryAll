//
//  UIImage+scaleToSize.m
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "UIImage+scaleToSize.h"

@implementation UIImage (scaleToSize)

-(UIImage*)scaleToSize:(CGSize)size
{
    UIGraphicsBeginImageContext(size); // create a bitmap graphics context, this will also set it as the current context
    [self drawInRect:CGRectMake(0, 0, size.width, size.height)]; // draw the scaled image in the current context
    UIImage* scaledImage = UIGraphicsGetImageFromCurrentImageContext(); // create a new image from current context
    UIGraphicsEndImageContext(); // pop the current context from the stack
    return scaledImage;
}

@end
