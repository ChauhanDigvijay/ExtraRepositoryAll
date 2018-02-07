//
//  ProgressDialog.h
//  LibiOS
//
//  Created by Bill Lewis on 9/30/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ModalDialog.h"
#import "SmartTextView.h"

@interface ProgressDialog : ModalDialog
{
    UIView         *_parentView;
    UILabel     *_messageText;
    UIImageView *spinnerBottom;
}

- (id)initWithView:(UIView *)view message:(NSString *)message;
- (void)changeMessage:(NSString *)message;
- (void)show;
- (void)dismiss;
-(void)showTextMessage;

@end
