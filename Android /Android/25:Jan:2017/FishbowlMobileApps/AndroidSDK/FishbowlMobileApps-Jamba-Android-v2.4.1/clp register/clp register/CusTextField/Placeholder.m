//
//  Placeholder.m
//  clp register
//
//  Created by VT002 on 3/10/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import "Placeholder.h"

@implementation Placeholder

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (void)drawPlaceholderInRect:(CGRect)rect
{
    // Placeholder text color, the same like default
    UIColor *placeholderColor = [UIColor colorWithWhite:0.55 alpha:1];
    [placeholderColor setFill];
    
    // Get the size of placeholder text. We will use height to calculate frame Y position
//    CGSize size = [self.placeholder sizeWithFont:self.font];
    
    CGSize size = [self.placeholder sizeWithAttributes:
                       @{NSFontAttributeName:
                             self.font}];
    
    // Vertically centered frame
    CGRect placeholderRect =  CGRectMake(rect.origin.x, (rect.size.height - size.height)/2, rect.size.width, size.height);
    
    // Check if OS version is 7.0+ and draw placeholder a bit differently
  //  if ([[UIDevice currentDevice] systemVersion].floatValue >= 7.0) {
        
        NSMutableParagraphStyle *style = [[NSMutableParagraphStyle alloc] init];
        style.lineBreakMode = NSLineBreakByTruncatingTail;
        style.alignment = self.textAlignment;
        NSDictionary *attr = [NSDictionary dictionaryWithObjectsAndKeys:style,NSParagraphStyleAttributeName, self.font, NSFontAttributeName, placeholderColor, NSForegroundColorAttributeName, nil];
        
        [self.placeholder drawInRect:placeholderRect withAttributes:attr];
        
        
//    } else {
//        [self.placeholder drawInRect:placeholderRect
//                            withFont:self.font
//                       lineBreakMode:NSLineBreakByTruncatingTail
//                           alignment:self.textAlignment];
//    }
    
}

@end
