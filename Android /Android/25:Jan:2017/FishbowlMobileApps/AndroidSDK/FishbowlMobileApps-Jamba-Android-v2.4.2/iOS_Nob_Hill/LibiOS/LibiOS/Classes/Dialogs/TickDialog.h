//
//  TickDialog.h
//  LibiOS
//
//  Created by Bill Lewis on 9/30/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ModalDialog.h"
#import "SmartTextView.h"

@interface TickDialog : ModalDialog
{
    UIView         *_parentView;
    SmartTextView  *_messageText;
    UIImageView *spinnerBottom;
}

- (id)initWithView:(UIView *)view message:(NSString *)message;
- (id)initWithView:(UIView *)view;

- (void)changeMessage:(NSString *)message;
- (void)show;
- (void)dismiss;

@end