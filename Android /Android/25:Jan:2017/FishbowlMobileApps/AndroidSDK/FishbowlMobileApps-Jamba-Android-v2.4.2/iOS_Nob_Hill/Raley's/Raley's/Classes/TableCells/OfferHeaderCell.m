//
//  OfferHeaderCell.m
//  Raley's
//
//  Created by Bill Lewis on 11/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "OfferHeaderCell.h"
#import "Utility.h"

@implementation OfferHeaderCell

@synthesize _categoryLabel;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];

    if(self)
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        UIImageView *background = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
//        background.image = [UIImage imageNamed:@"section_header"];
        [self setBackgroundColor:[UIColor whiteColor]];
        [self addSubview:background];

        int labelHeight = frame.size.height * .60;
        int labelYOrigin = frame.size.height * .20;
        
        _categoryLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, labelYOrigin, frame.size.width, labelHeight)];
        _categoryLabel.backgroundColor = [UIColor clearColor];
        _categoryLabel.font = [Utility fontForFamily:_app._normalFont andHeight:font_size17];
        _categoryLabel.textAlignment = NSTextAlignmentCenter;
        _categoryLabel.textColor = [UIColor blackColor];
        [self addSubview:_categoryLabel];
    }
    return self;
}

@end
