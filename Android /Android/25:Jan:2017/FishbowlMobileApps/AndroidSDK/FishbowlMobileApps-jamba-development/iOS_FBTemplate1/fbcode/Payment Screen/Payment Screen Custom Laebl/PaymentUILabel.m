//
//  PaymentUILabel.m
//  
//
//  Created by Harsh on 04/01/15.
//  Copyright (c) 2015 Harsh. All rights reserved.
//

#import "PaymentUILabel.h"

@implementation PaymentUILabel

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (void)drawTextInRect:(CGRect)rect {
    UIEdgeInsets insets = {0, 10, 0, 10};

    
    [super drawTextInRect:UIEdgeInsetsInsetRect(rect, insets)];
}

@end
