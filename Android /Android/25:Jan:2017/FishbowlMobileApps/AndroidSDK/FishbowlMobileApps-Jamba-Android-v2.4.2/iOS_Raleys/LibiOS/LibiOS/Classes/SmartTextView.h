//
//  SmartTextView.h
//  LibiOS
//
//  Created by Bill Lewis on 10/24/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>


// Custom class that will vertically center single or multiple lines of text in a view.  The view's
// font will be sized so that it uses the entire height of the line.  Line height will be determined
// by the view height divided by the number of lines specified.  If the text is too long to fit
// in the specified view size the last line will be truncated.  If the text does not fill all the
// lines specified, the lines that are used will be centered vertically in the view.  This is a
// feature missing in iOS.  Text can be center or left justified.

@interface SmartTextView : UIView
{
    CGRect  _frame;
}

@property (nonatomic, assign)int numberOfLines;
@property (nonatomic, assign)UITextAlignment textAlignment;
@property (nonatomic, strong)NSString *text;
@property (nonatomic, strong)NSString *fontFamily;
@property (nonatomic, strong)UIColor *textColor;
@property (nonatomic, readwrite)CGFloat fontsize;


- (id)initWithFrame:(CGRect)frame backGroundColor:(UIColor *)backgroundColor;

@end
