//
//  OffsetTextField.m
//  LibiOS
//
//  Created by Bill Lewis on 2/4/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "OffsetTextField.h"

@implementation OffsetTextField

- (id)initWithFrame:(CGRect)frame offset:(int)offset
{
    self = [super initWithFrame:frame];

    if(self)
    {
        _offset = offset;
    }

    return self;
}


// placeholder offset
- (CGRect)textRectForBounds:(CGRect)bounds
{
    return CGRectInset(bounds, _offset , 0 );
}


// text offset
- (CGRect)editingRectForBounds:(CGRect)bounds
{
    return CGRectInset(bounds, _offset , 0 );
}


@end
