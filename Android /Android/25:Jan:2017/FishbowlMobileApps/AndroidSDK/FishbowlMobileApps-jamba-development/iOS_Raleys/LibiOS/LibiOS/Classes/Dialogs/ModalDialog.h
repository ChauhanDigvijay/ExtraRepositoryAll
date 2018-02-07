//
//  ModalDialog.h
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//


#import <UIKit/UIKit.h>


@interface ModalDialog : UIView
{
    UIImageView *_dialogView;    
}

- (id)initWithView:(UIView *)view :(UIImage *)background;
- (id)initWithView:(UIView *)view :(int)width :(int)height :(UIImage *)background;
- (CGRect)getFrame;

@end
