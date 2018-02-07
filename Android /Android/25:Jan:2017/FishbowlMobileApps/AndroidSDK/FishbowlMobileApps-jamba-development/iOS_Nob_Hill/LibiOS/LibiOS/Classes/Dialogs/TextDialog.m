//
//  TextDialog.m
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "TextDialog.h"
#import "SizedTextView.h"
#import "UIImage+scaleToSize.h"
#import "SmartTextView.h"
#import "Utility.h"
#import "Logging.h"

#define button_bg_height 45
#define button_margin 8
#define button_height button_bg_height-(2*button_margin)
#define button_background_color [UIColor colorWithRed:193.0/255.0 green:192.0/255.0 blue:192.0/255.0 alpha:1]
#define button_Text_color [UIColor colorWithRed:(32/255.0) green:(35/255.0) blue:(36/255.0) alpha:1.0]
#define alert_text_size 13.0f
#define alert_text_width 0.92 // in percentage of total width
#define alert_message_min_height 100 //120
#define alert_message_max_height 250
#define alert_message_margin 10
#define alert_message_num_line 12  // Depends on "alert_text_size" and "alert_message_max_height"

#define alert_button_font_size 13.0f

@implementation TextDialog

- (id)initWithView:(UIView *)view title:(NSString *)title message:(NSString *)message
{
      _buttonCount = 1;
    _title = title;
    _message = message;
    [self buildView:view];
    self = [super initWithView:view :_sizedBackground];
    
    if(self)
        [self addComponents];

    return self;
}

-(NSString*)getMessage{
    return _message;
}

- (id)initWithView:(UIView *)view title:(NSString *)title message:(NSString *)message responder:(id)responder callback:(SEL)singleCallback
{
    _buttonCount = 1;
    _title = title;
    _message = message;
    _responder = responder;
    _singleCallback = singleCallback;
    [self buildView:view];
    self = [super initWithView:view :_sizedBackground];

    if(self)
        [self addComponents];

    return self;
}


- (id)initWithView:(UIView *)view title:(NSString *)title message:(NSString *)message responder:(id)responder leftCallback:(SEL)leftCallback rightCallback:(SEL)rightCallback
{
    _buttonCount = 2;
    _title = title;
    _message = message;
    _responder = responder;
    _leftCallback = leftCallback;
    _rightCallback = rightCallback;
    [self buildView:view];
    self = [super initWithView:view :_sizedBackground];

    if(self)
        [self addComponents];
    return self;
}


- (void)buildView:(UIView *)view
{
    _parentView = view;
    
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        _titleFont = [UIFont fontWithName:kFontBold size:14];
        _messageFont = [UIFont fontWithName:kFontRegular size:14];
    }
    else
    {
        _titleFont = [UIFont fontWithName:kFontBold size:28];
        _messageFont = [UIFont fontWithName:kFontRegular size:28];
    }
    
//    UIImage *background = [UIImage imageNamed:@"dialog_background"];
    backgroundWidth = view.bounds.size.width * 0.75;// .9;
    backgroundHeight = backgroundWidth * .6; // default to 10:6 aspect ratio
    
    // message height
    CGSize size = [self getUILabelFontSizeBasedOnText_width:backgroundWidth*alert_text_width _fontname:kFontRegular _fontsize:alert_text_size _text:_message];
    
    backgroundHeight = size.height + (2*alert_message_margin);
    
    _textWidth = backgroundWidth * .85;
    _textXPadding = backgroundWidth * .075;
    _buttonHeight = button_height; // backgroundHeight * .17; // .14

    if(_buttonCount == 1)
        _buttonWidth = backgroundWidth * .5;
    else
        _buttonWidth = backgroundWidth * .35;

//    _buttonImage = [[UIImage imageNamed:@"button_red_plain"] scaleToSize:CGSizeMake(_buttonWidth, _buttonHeight)];

//    _buttonImage = [[UIImage imageNamed:@"account_tab_unselected"] scaleToSize:CGSizeMake(_buttonWidth, _buttonHeight)];
//    
//     _buttonImage = [[UIImage imageNamed:@"drop_menu_top_selected"] scaleToSize:CGSizeMake(_buttonWidth, _buttonHeight)];

//    _buttonImage = [self buttonBGImageSize:CGSizeMake(_buttonWidth, _buttonHeight)];
    
    //
    _textYPadding = (int)(backgroundHeight * .050);
    _sizedBackground = [self imageWithSize:CGSizeMake(backgroundWidth, backgroundHeight+button_bg_height)]; // [background scaleToSize:CGSizeMake(backgroundWidth, backgroundHeight)];
}

- (UIImage *)imageWithSize:(CGSize)size
{
    UIColor *color = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.7];
    CGRect rect = CGRectMake(0.0f, 0.0f, size.width, size.height-button_bg_height);
    UIGraphicsBeginImageContext(size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    color = [UIColor colorWithRed:0 green:0 blue:0 alpha:1];
    rect = CGRectMake(0.0f, size.height-button_bg_height, size.width, button_bg_height);
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    
    return image;
}

//- (UIImage *)buttonBGImageSize:(CGSize)size
//{
//    UIColor *color = [UIColor colorWithRed:193.0/255.0 green:192.0/255.0 blue:192.0/255.0 alpha:1];
//    CGRect rect = CGRectMake(0.0f, 0.0f, size.width, size.height);
//    UIGraphicsBeginImageContext(size);
//    CGContextRef context = UIGraphicsGetCurrentContext();
//    
//    CGContextSetFillColorWithColor(context, [color CGColor]);
//    CGContextFillRect(context, rect);
//    
//    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
//    UIGraphicsEndImageContext();
//    
//    
//    return image;
//}


-(CGSize)getUILabelFontSizeBasedOnText_width:(CGFloat)width _fontname:(NSString*)font _fontsize:(CGFloat)fsize _text:(NSString*)text{

    CGSize size = CGSizeMake(width, alert_message_min_height);
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
    
    if(size.height<alert_message_min_height){
        size.height = alert_message_min_height;
    }else if (size.height>alert_message_max_height){
        size.height = alert_message_max_height;
    }
    return size;
}


- (void)addComponents
{
    CGRect frame = [self getFrame]; // frame size is the background size
    _buttonYOrigin = frame.origin.y +backgroundHeight + button_margin; //frame.origin.y + (frame.size.height * .79); // .80
    
//    SmartTextView *titleLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(frame.origin.x, frame.origin.y + (frame.size.height * .05), frame.size.width, frame.size.height * .15) backGroundColor:[UIColor clearColor]];

//    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(frame.origin.x, frame.origin.y + (frame.size.height * .32), frame.size.width-10, frame.size.height * .15)];
//    titleLabel.numberOfLines = 7;
//    titleLabel.textColor = [UIColor whiteColor];
//    [titleLabel setFont:[UIFont fontWithName:kFontBold size:13]];
//    titleLabel.text = _title;
//    titleLabel.textAlignment = NSTextAlignmentCenter;
//    [self addSubview:titleLabel];
    
//    SmartTextView *textView = [[SmartTextView alloc] initWithFrame:CGRectMake(frame.origin.x + (frame.size.width * .04), frame.origin.y + (frame.size.height * .36), frame.size.width * .92, frame.size.height * .45) backGroundColor:[UIColor clearColor]];
    
    
    
    frame = CGRectMake(frame.origin.x + (frame.size.width * .04), frame.origin.y+alert_message_margin, frame.size.width * alert_text_width, frame.size.height * .60);
    
    CGSize size = [self getUILabelFontSizeBasedOnText_width:frame.size.width _fontname:kFontRegular _fontsize:alert_text_size _text:_message];
    frame.size.height = size.height;
    
    UILabel *textview_lbl=[[UILabel alloc]initWithFrame:frame];
    
    textview_lbl.numberOfLines = alert_message_num_line;
    [textview_lbl setFont:[UIFont fontWithName:kFontRegular size:alert_text_size]];
    textview_lbl.lineBreakMode = NSLineBreakByTruncatingTail;
    textview_lbl.textColor = [UIColor whiteColor];
    textview_lbl.text = _message;
    textview_lbl.textAlignment = NSTextAlignmentCenter;
    [textview_lbl setBackgroundColor:[UIColor clearColor]];
    [self addSubview:textview_lbl];
    
    if(_buttonCount == 1)
        [self addSingleButton:frame];
    else
        [self addDoubleButtons:frame];
}


- (void)addSingleButton:(CGRect)frame
{
    _okButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_okButton setFrame:CGRectMake((frame.origin.x + (frame.size.width - _buttonWidth) / 2), _buttonYOrigin, _buttonWidth, _buttonHeight)];
    _okButton.titleLabel.font = [UIFont fontWithName:kFontBold size:alert_button_font_size];
    [_okButton setBackgroundColor:button_background_color];
    [_okButton setTitleColor:button_Text_color forState:UIControlStateNormal];

    [_okButton setTitle:@"OK" forState:UIControlStateNormal];

    if(_responder == nil || _singleCallback == nil)
        [_okButton addTarget:self action:@selector(close) forControlEvents:UIControlEventTouchDown];
    else
        [_okButton addTarget:_responder action:_singleCallback forControlEvents:UIControlEventTouchDown];

    [_okButton.layer setCornerRadius:5.0f];
    [_okButton setClipsToBounds:YES];
    [self addSubview:_okButton];
}


- (void)addDoubleButtons:(CGRect)frame
{
//    int buttonSpacing = (frame.size.width - (_buttonWidth * 2)) / 3;

        int buttonSpacing = (backgroundWidth - (_buttonWidth * 2)) / 3;
    
    _leftButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_leftButton setFrame:CGRectMake(frame.origin.x + buttonSpacing, _buttonYOrigin, _buttonWidth * 0.7, _buttonHeight)];
    _leftButton.titleLabel.font = [UIFont fontWithName:kFontBold size:alert_button_font_size];
    [_leftButton setBackgroundColor:button_background_color];
    [_leftButton setTitleColor:button_Text_color forState:UIControlStateNormal];
    [_leftButton setTitle:@"Yes" forState:UIControlStateNormal];
    [_leftButton addTarget:_responder action:_leftCallback forControlEvents:UIControlEventTouchDown];
    [self addSubview:_leftButton];

    _rightButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_rightButton setFrame:CGRectMake(frame.origin.x + _buttonWidth + (buttonSpacing * 2), _buttonYOrigin, _buttonWidth * 0.7, _buttonHeight)];
    _rightButton.titleLabel.font = [UIFont fontWithName:kFontBold size:alert_button_font_size];
    [_rightButton setBackgroundColor:button_background_color];
    [_rightButton setTitleColor:button_Text_color forState:UIControlStateNormal];
    [_rightButton setTitle:@"No" forState:UIControlStateNormal];
    [_rightButton addTarget:_responder action:_rightCallback forControlEvents:UIControlEventTouchDown];
    
    [_leftButton.layer setCornerRadius:5.0f];
    [_rightButton.layer setCornerRadius:5.0f];
    [_leftButton setClipsToBounds:YES];
    [_rightButton setClipsToBounds:YES];
    [self addSubview:_rightButton];
}


- (void)show
{
    [_parentView addSubview:self];
}


- (void)close
{
    [self removeFromSuperview];
}


@end
