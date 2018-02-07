//
//  SmartTextView.m
//  LibiOS
//
//  Created by Bill Lewis on 10/24/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "SmartTextView.h"
#import "Utility.h"


@implementation SmartTextView

@synthesize numberOfLines;
@synthesize textAlignment;
@synthesize text;
@synthesize fontFamily;
@synthesize textColor;
@synthesize fontsize;

- (id)initWithFrame:(CGRect)frame backGroundColor:(UIColor *)backgroundColor
{
    self = [super initWithFrame:frame];

    if (self)
    {
        [self setBackgroundColor:backgroundColor];
        _frame = frame;
    }

    return self;
}


// Only override drawRect: if you perform custom drawing. An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [textColor CGColor]);
    int lineHeight = _frame.size.height / numberOfLines;
    if(fontsize>0){
        lineHeight = fontsize;
    }
    int lastLineIndex = numberOfLines - 1;
    UIFont *font = [Utility fontForFamily:fontFamily andHeight:lineHeight];
    NSMutableArray *textArray = [[NSMutableArray alloc] initWithArray:[Utility wrapText:text forFont:font forWidth:_frame.size.width * .96]]; // allow a small pad on each side
    int arrayCount = (int)textArray.count;

    if(textArray.count > numberOfLines)
    {
        NSString *lastLine = [textArray objectAtIndex:numberOfLines - 1];
        NSRange range = NSMakeRange(0, [lastLine length] - 1);
        NSString *adjustedLastLine = [NSString stringWithFormat:@"%@...", [lastLine substringWithRange:range]]; // truncate the last allowable line

        for(int i = lastLineIndex; i < arrayCount; i++) // remove the last allowable line and beyond
            [textArray removeObjectAtIndex:lastLineIndex];

        [textArray addObject:adjustedLastLine]; // put the adjusted lastLine back
    }

    int actualLines = (int)textArray.count < numberOfLines  ? (int)textArray.count : numberOfLines;
    int yPadding = actualLines < numberOfLines ? ((numberOfLines - actualLines) * lineHeight) / 2 : 0;
    CGPoint point = CGPointMake(0.0, 0.0);
    CGSize textSize;
    NSString *lineText = nil;

    for(int i = 0; i < actualLines; i++)
    {
        lineText = [textArray objectAtIndex:i];
        textSize = [lineText sizeWithFont:font];
        point.y = (i * lineHeight) + yPadding;

        if(textAlignment == NSTextAlignmentLeft)
            point.x = 0.0;
        else // UITextAlignmentCenter(default)
            point.x = (rect.size.width - textSize.width) / 2;

        [lineText drawAtPoint:point withFont:font];
    }
}


@end
