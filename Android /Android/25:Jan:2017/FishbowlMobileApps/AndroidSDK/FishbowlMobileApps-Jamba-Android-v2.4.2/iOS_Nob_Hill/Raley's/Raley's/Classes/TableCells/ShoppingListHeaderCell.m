//
//  ShoppingListHeaderCell.m
//  Raley's
//
//  Created by Bill Lewis on 12/5/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ShoppingListHeaderCell.h"
#import "Utility.h"

@implementation ShoppingListHeaderCell

@synthesize _aisleNumber;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];

    if(self)
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        UIImageView *background = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        background.image = [UIImage imageNamed:@"section_header"];
        [self addSubview:background];
        
        int labelHeight = frame.size.height * .60;
        int labelYOrigin = frame.size.height * .20;
        
        _aisleNumber = [[UILabel alloc]initWithFrame:CGRectMake(0, labelYOrigin, frame.size.width, labelHeight)];
        _aisleNumber.backgroundColor = [UIColor clearColor];
        _aisleNumber.font = [Utility fontForFamily:_app._normalFont andHeight:labelHeight];
        _aisleNumber.textAlignment = NSTextAlignmentCenter;
        _aisleNumber.textColor = [UIColor blackColor];
        [self addSubview:_aisleNumber];
    }
    return self;
}

@end
