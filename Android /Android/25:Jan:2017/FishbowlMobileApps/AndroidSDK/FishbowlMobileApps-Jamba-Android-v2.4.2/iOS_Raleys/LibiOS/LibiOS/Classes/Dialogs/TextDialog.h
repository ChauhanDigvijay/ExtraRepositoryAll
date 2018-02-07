//
//  TextDialog.h
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ModalDialog.h"

@interface TextDialog : ModalDialog
{
    UIView   *_parentView;
    UIImage  *_sizedBackground;
    UIImage  *_buttonImage;
    UIButton *_okButton;
    UIButton *_leftButton;
    UIButton *_rightButton;
    UIFont   *_titleFont;
    UIFont   *_messageFont;
    NSString *_title;
    NSString *_message;
    CGFloat  _buttonWidth;
    CGFloat  _buttonHeight;
    CGSize   _textSize;
    id       _responder;
    SEL      _singleCallback;
    SEL      _leftCallback;
    SEL      _rightCallback;
    int      _textWidth;
    int      _textXPadding;
    int      _textYPadding;
    int      _buttonCount;
    int      _buttonYOrigin;
    NSArray  *_textLines;
    
    
    int backgroundWidth;
    int backgroundHeight;
}

- (id)initWithView:(UIView *)view title:(NSString *)title message:(NSString *)message;
-(NSString*)getMessage;
- (id)initWithView:(UIView *)view title:(NSString *)title message:(NSString *)message responder:(id)responder callback:(SEL)singleCallback;
- (id)initWithView:(UIView *)view title:(NSString *)title message:(NSString *)message responder:(id)responder leftCallback:(SEL)leftCallback rightCallback:(SEL)rightCallback;
- (void)show;
- (void)close;

@end
