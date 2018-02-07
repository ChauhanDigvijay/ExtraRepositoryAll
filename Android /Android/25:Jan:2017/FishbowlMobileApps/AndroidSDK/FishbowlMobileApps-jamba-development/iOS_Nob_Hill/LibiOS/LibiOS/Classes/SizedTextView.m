//
//  SizedTextView.m
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "SizedTextView.h"


@implementation SizedTextView


- (id)initWithFrame:(CGRect)frame :(UIColor *)backgroundColor :(UIColor *)textColor :(UIFont *)font :(NSArray *)textStrings :(TextJustification)justification
{
    self = [super initWithFrame:frame];
    
    if (self)
    {
        [self setBackgroundColor:backgroundColor];
        _textColor = textColor;
        _font = font;
        _textStrings = [[NSArray alloc] initWithArray:textStrings];
        _justification = justification;
    }
    
    return self;
}


// Only override drawRect: if you perform custom drawing. An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [_textColor CGColor]);
    NSString *text = nil;
    CGSize textSize;
    CGPoint point = CGPointMake(0.0, 0.0);
    int yPos = 0;
    
    for(int i = 0; i < _textStrings.count; i++)
    {
        text = [_textStrings objectAtIndex:i];
        textSize = [text sizeWithFont:_font];
        
        if(_textStrings.count == 1)
            point.y = (rect.size.height - textSize.height) / 2;
        else
            point.y = yPos;      
        
        if(_textStrings.count == 1 || _justification == JUSTIFICATION_CENTER)
            point.x = (rect.size.width - textSize.width) / 2;
        else
            point.x = 0.0;
        
        [text drawAtPoint:point withFont:_font];
        yPos += textSize.height;
    }
}

@end
