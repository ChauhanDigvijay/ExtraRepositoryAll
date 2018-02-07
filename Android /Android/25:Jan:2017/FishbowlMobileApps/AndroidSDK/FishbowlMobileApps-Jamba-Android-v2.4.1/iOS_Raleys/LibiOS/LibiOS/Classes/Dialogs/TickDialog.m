//
//  TickDialog.m
//  LibiOS
//
//  Created by Bill Lewis on 9/30/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
#import "SmartTextView.h"
#import "TickDialog.h"
#import "UIImage+scaleToSize.h"
#import "Utility.h"
#import "Logging.h"


@implementation TickDialog

- (id)initWithView:(UIView *)view message:(NSString *)message
{
    _parentView = view;
    //UIImage *background = [UIImage imageNamed:@"progress_background"];
    UIImage *background = [UIImage imageNamed:@"progress_background"];
    int backgroundWidth;
    int backgroundHeight; // default to 10:6 aspect ratio
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone){
        backgroundWidth = view.bounds.size.width * .3;
        backgroundHeight = backgroundWidth * .8;
    }
    else{
        backgroundWidth = view.bounds.size.width * .4;
        backgroundHeight = backgroundWidth * .6;
    }
    
    UIImage *sizedBackground = [background scaleToSize:CGSizeMake(backgroundWidth, backgroundHeight)];
    self = [super initWithView:view :sizedBackground];
    
    //[self setAlpha:0.7];
    if(self)
    {
        CGRect frame = [self getFrame]; // frame size is the background size
        int spinnerSize = frame.size.height * .7;
        int spinnerXOrigin = frame.origin.x + ((frame.size.width - spinnerSize) / 2);
        int spinnerYOrigin = frame.origin.y + (frame.size.height * .11);
        CGRect spinnerFrame = CGRectMake(spinnerXOrigin, spinnerYOrigin, spinnerSize, spinnerSize);
        
        spinnerBottom = [[UIImageView alloc] initWithFrame:spinnerFrame];
        [spinnerBottom setImage:[UIImage imageNamed:@"success"]];
        // [spinnerBottom setImage:[UIImage imageNamed:@"loader_bg.png"]];
        
//        NSMutableArray *Image_array=[NSMutableArray new];
//        [Image_array addObject:[UIImage imageNamed:@"Loader1.png"]];
//        [Image_array addObject:[UIImage imageNamed:@"Loader2.png"]];
//        [Image_array addObject:[UIImage imageNamed:@"Loader3.png"]];
//        [Image_array addObject:[UIImage imageNamed:@"Loader4.png"]];
//        [Image_array addObject:[UIImage imageNamed:@"Loader5.png"]];
//        [Image_array addObject:[UIImage imageNamed:@"Loader6.png"]];
//        [Image_array addObject:[UIImage imageNamed:@"Loader7.png"]];
//        [Image_array addObject:[UIImage imageNamed:@"Loader8.png"]];
//        [Image_array addObject:[UIImage imageNamed:@"Loader1.png"]];
//        
//        //        UIView *bg_view=[[UIView alloc]initWithFrame:spinnerFrame];
//        //
//        spinnerBottom.animationImages=Image_array;
//        spinnerBottom.animationDuration = 0.7f;
//        spinnerBottom.animationRepeatCount = 0;
//        [spinnerBottom startAnimating];
        //
        //        [bg_view.layer setBackgroundColor:[UIColor blackColor].CGColor];
        //        [bg_view.layer setCornerRadius:12.0f];
        //        [bg_view.layer setBorderWidth:1.0f];
        //        bg_view.alpha=0.7;
        //        [bg_view.layer setBorderColor:[UIColor whiteColor].CGColor];
        //
        //        [spinnerBottom setFrame:CGRectMake(0, 0, spinnerSize, spinnerSize)];
        
        //        [bg_view addSubview:spinnerBottom];
        
        // [self addSubview:bg_view];
        
        
        //  UIImageView *spinnerTop = [[UIImageView alloc] initWithFrame:spinnerFrame];
        // [spinnerTop setImage:[UIImage imageNamed:@"loader"]];
        //        [self addSubview:spinnerTop];
        
        [self addSubview:spinnerBottom];
        
        

            _messageText = [[SmartTextView alloc] initWithFrame:CGRectMake(frame.origin.x + (frame.size.width * .05), frame.origin.y + (frame.size.height * .83), frame.size.width * .9, frame.size.height * .25) backGroundColor:[UIColor clearColor]];
            _messageText.numberOfLines = 1;
            _messageText.textColor = [UIColor blackColor];
            _messageText.fontFamily = kFontRegular;
            _messageText.text = message;
            _messageText.textAlignment = NSTextAlignmentCenter;
       [self addSubview:_messageText];
        
        //[self runSpinAnimationOnView:spinnerBottom duration:100.0 rotations:1.0 repeat:HUGE_VALF];
    }
    
    return self;
}


- (id)initWithView:(UIView *)view
{
    _parentView = view;
    //UIImage *background = [UIImage imageNamed:@"progress_background"];
    UIImage *background = [UIImage imageNamed:@"progress_background"];
    int backgroundWidth;
    int backgroundHeight; // default to 10:6 aspect ratio
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone){
        backgroundWidth = view.bounds.size.width * .3;
        backgroundHeight = backgroundWidth * .8;
    }
    else{
        backgroundWidth = view.bounds.size.width * .4;
        backgroundHeight = backgroundWidth * .6;
    }
    
    UIImage *sizedBackground = [background scaleToSize:CGSizeMake(backgroundWidth, backgroundHeight)];
    self = [super initWithView:view :sizedBackground];
    
    //[self setAlpha:0.7];
    if(self)
    {
        CGRect frame = [self getFrame]; // frame size is the background size
        int spinnerSize = frame.size.height * .7;
        int spinnerXOrigin = frame.origin.x + ((frame.size.width - spinnerSize) / 2);
        int spinnerYOrigin = frame.origin.y + (frame.size.height * .11);
        CGRect spinnerFrame = CGRectMake(spinnerXOrigin, spinnerYOrigin, spinnerSize, spinnerSize);
        
        spinnerBottom = [[UIImageView alloc] initWithFrame:spinnerFrame];
        [spinnerBottom setImage:[UIImage imageNamed:@"tick_message"]];
        // [spinnerBottom setImage:[UIImage imageNamed:@"loader_bg.png"]];
        
        //        NSMutableArray *Image_array=[NSMutableArray new];
        //        [Image_array addObject:[UIImage imageNamed:@"Loader1.png"]];
        //        [Image_array addObject:[UIImage imageNamed:@"Loader2.png"]];
        //        [Image_array addObject:[UIImage imageNamed:@"Loader3.png"]];
        //        [Image_array addObject:[UIImage imageNamed:@"Loader4.png"]];
        //        [Image_array addObject:[UIImage imageNamed:@"Loader5.png"]];
        //        [Image_array addObject:[UIImage imageNamed:@"Loader6.png"]];
        //        [Image_array addObject:[UIImage imageNamed:@"Loader7.png"]];
        //        [Image_array addObject:[UIImage imageNamed:@"Loader8.png"]];
        //        [Image_array addObject:[UIImage imageNamed:@"Loader1.png"]];
        //
        //        //        UIView *bg_view=[[UIView alloc]initWithFrame:spinnerFrame];
        //        //
        //        spinnerBottom.animationImages=Image_array;
        //        spinnerBottom.animationDuration = 0.7f;
        //        spinnerBottom.animationRepeatCount = 0;
        //        [spinnerBottom startAnimating];
        //
        //        [bg_view.layer setBackgroundColor:[UIColor blackColor].CGColor];
        //        [bg_view.layer setCornerRadius:12.0f];
        //        [bg_view.layer setBorderWidth:1.0f];
        //        bg_view.alpha=0.7;
        //        [bg_view.layer setBorderColor:[UIColor whiteColor].CGColor];
        //
        //        [spinnerBottom setFrame:CGRectMake(0, 0, spinnerSize, spinnerSize)];
        
        //        [bg_view addSubview:spinnerBottom];
        
        // [self addSubview:bg_view];
        
        
        //  UIImageView *spinnerTop = [[UIImageView alloc] initWithFrame:spinnerFrame];
        // [spinnerTop setImage:[UIImage imageNamed:@"loader"]];
        //        [self addSubview:spinnerTop];
        
        [self addSubview:spinnerBottom];
        
        
        
//        _messageText = [[SmartTextView alloc] initWithFrame:CGRectMake(frame.origin.x + (frame.size.width * .05), frame.origin.y + (frame.size.height * .83), frame.size.width * .9, frame.size.height * .25) backGroundColor:[UIColor clearColor]];
//        _messageText.numberOfLines = 1;
//        _messageText.textColor = [UIColor blackColor];
//        _messageText.fontFamily = kFontRegular;
//        _messageText.text = message;
//        _messageText.textAlignment = NSTextAlignmentCenter;
//        [self addSubview:_messageText];
        
        //[self runSpinAnimationOnView:spinnerBottom duration:100.0 rotations:1.0 repeat:HUGE_VALF];
    }
    
    return self;
}
-(void)runSpinAnimationOnView:(UIView*)view duration:(CGFloat)duration rotations:(CGFloat)rotations repeat:(float)repeat;
{
    CABasicAnimation* rotationAnimation;
    rotationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
    rotationAnimation.toValue = [NSNumber numberWithFloat: M_PI * 2.0 * rotations * duration];
    rotationAnimation.duration = duration;
    rotationAnimation.cumulative = YES;
    rotationAnimation.repeatCount = repeat;
    [view.layer addAnimation:rotationAnimation forKey:@"rotationAnimation"];
}


- (void)changeMessage:(NSString *)message
{
    _messageText.text = message;
    [_messageText setNeedsDisplay];
}


- (void)show
{
    [spinnerBottom startAnimating];
    [_parentView addSubview:self];
}


- (void)dismiss
{
    [spinnerBottom stopAnimating];
    [self removeFromSuperview];
}


@end
