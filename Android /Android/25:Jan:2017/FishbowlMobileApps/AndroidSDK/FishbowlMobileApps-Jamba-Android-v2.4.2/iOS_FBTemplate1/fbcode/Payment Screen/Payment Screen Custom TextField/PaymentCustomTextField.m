//
//  PaymentCustomTextField.h
//  iOS_FBTemplate1
//
//  Created by Harsh on 04/01/15.
//  Copyright (c) 2015 Harsh. All rights reserved.
//

#import "PaymentCustomTextField.h"



@implementation PaymentCustomTextField

- (void)awakeFromNib{
    [self setDelegate:self];
    [super awakeFromNib];
}

// Placeholder position
- (CGRect)textRectForBounds:(CGRect)bounds{
    return CGRectInset( bounds , 10, 2 );
}

// Text position
- (CGRect)editingRectForBounds:(CGRect)bounds{
    return CGRectInset( bounds , 10, 2 );
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

@end
