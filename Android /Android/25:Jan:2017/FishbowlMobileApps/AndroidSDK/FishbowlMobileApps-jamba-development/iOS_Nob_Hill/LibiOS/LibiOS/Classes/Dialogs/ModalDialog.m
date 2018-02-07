//
//  ModalDialog.m
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ModalDialog.h"


@implementation ModalDialog

- (id)initWithView:(UIView *)view :(UIImage *)background
{
    CGRect rect = view.frame; // view frame origin.y is the status bar height not 0
    rect.origin.y = 0;
    UIView *adjustedView = [[UIView alloc] initWithFrame:rect];

    self = [super initWithFrame:adjustedView.frame];
    
    if (self)
    {
//        self.backgroundColor = [UIColor colorWithWhite:0.0 alpha:.4];
        self.backgroundColor=[UIColor clearColor];
        CGFloat dialogXOrigin = (self.bounds.size.width - background.size.width) / 2;
        CGFloat dialogYOrigin = ((self.bounds.size.height - background.size.height) / 2);
        _dialogView = [[UIImageView alloc] initWithImage:background];
        _dialogView.frame = CGRectMake(dialogXOrigin, dialogYOrigin, background.size.width, background.size.height);
        [self applyCornerRadius];
        //_dialogView.alpha = .9;
        [self addSubview:_dialogView];
    }
    
    return self;
}

-(void)applyCornerRadius{
    [_dialogView.layer setCornerRadius:5.0f];
    [_dialogView setClipsToBounds:YES];
}

- (id)initWithView:(UIView *)view :(int)width :(int)height :(UIImage *)background
{
    CGRect rect = view.frame; // view frame origin.y is the status bar height not 0
    rect.origin.y = 0;
    UIView *adjustedView = [[UIView alloc] initWithFrame:rect];
    
    self = [super initWithFrame:adjustedView.frame];
    
    if (self)
    {
//        self.backgroundColor = [UIColor colorWithWhite:0.0 alpha:.4];
        self.backgroundColor=[UIColor clearColor];
        CGFloat dialogXOrigin = (self.bounds.size.width - width) / 2;
        CGFloat dialogYOrigin = (self.bounds.size.height - height) / 2;
        _dialogView = [[UIImageView alloc] initWithImage:background];
        _dialogView.frame = CGRectMake(dialogXOrigin, dialogYOrigin, width, height);
        [self applyCornerRadius];
        [self addSubview:_dialogView];
    }
    
    return self;
}


- (CGRect)getFrame
{
    return _dialogView.frame;
}

@end
