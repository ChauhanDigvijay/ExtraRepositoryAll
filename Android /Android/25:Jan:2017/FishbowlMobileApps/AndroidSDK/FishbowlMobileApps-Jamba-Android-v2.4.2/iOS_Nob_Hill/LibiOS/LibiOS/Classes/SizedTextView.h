//
//  SizedTextView.h
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>


typedef enum
{
	JUSTIFICATION_NONE = 0,
	JUSTIFICATION_LEFT = 1,
	JUSTIFICATION_CENTER = 2,
	JUSTIFICATION_CENTER_BOTTOM = 3
}TextJustification;


@interface SizedTextView : UIView
{
    UIColor *_textColor;
    UIFont *_font;
    NSArray *_textStrings;
    TextJustification _justification;
}


- (id)initWithFrame:(CGRect)frame :(UIColor *)backgroundColor :(UIColor *)textColor :(UIFont *)font :(NSArray *)textStrings :(TextJustification)justification;

@end
