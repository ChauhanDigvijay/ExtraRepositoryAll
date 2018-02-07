//
//  ProductCategoryButton.m
//  ProductCategoryTest
//
//  Created by Bill Lewis on 11/2/13.
//  Copyright (c) 2013 Bill Lewis. All rights reserved.
//

#import "ProductCategoryButton.h"
#import "Utility.h"
#import "Logging.h"

@implementation ProductCategoryButton

@synthesize _expanded;
@synthesize _productCategory;


- (id)initWithFrame:(CGRect)frame productCategory:productCategory
{
    self = [super initWithFrame:frame];
    
    if(self)
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        _expanded = NO;
        _productCategory = productCategory;
        //        _minusImage = [UIImage imageNamed:@"circled_minus"];
        //        _plusImage = [UIImage imageNamed:@"circled_plus"];
        _minusImage = [UIImage imageNamed:@"down_button"]; // down_button
        _plusImage = [UIImage imageNamed:@"forward_button"];
        
        //        _goImage = [UIImage imageNamed:@"button_go"];
        //        _goImage = [UIImage imageNamed:@"forward_button"];
        //self.backgroundColor = [UIColor clearColor];
        //        [self setBackgroundImage:[UIImage imageNamed:@"bar_white_gradient"] forState:UIControlStateNormal];
        [self setBackgroundImage:[UIImage imageNamed:@"cat_list_cell"] forState:UIControlStateNormal];
        
        //        [self setBackgroundImage:[UIImage imageNamed:@"bar_gray_gradient"] forState:UIControlStateHighlighted];
        [self setBackgroundImage:[UIImage imageNamed:@"bar_white_gradient"] forState:UIControlStateHighlighted];
        [self setBackgroundColor:[UIColor whiteColor]];
        int inset = _productCategory.level == 1 ? (frame.size.width * .01) : (frame.size.width * .01) + ((_productCategory.level - 1) * (frame.size.width * .04));
        int imageSize = frame.size.height * .4;
        int imageYOrigin = (frame.size.height - imageSize) / 2;
        int textHeight = frame.size.height * .4;
        int textYOrigin = (frame.size.height - textHeight) / 2;
        int textPad = frame.size.width * .01;
        int textXOrigin = inset + imageSize + textPad;
        int textWidth = frame.size.width - textXOrigin - (imageSize+(frame.size.width*0.1));
        NSString *categoryCount = @"";
        
        inset = frame.size.width * 0.9;
        
        _expandedStateImageView = [[UIImageView alloc] initWithFrame:CGRectMake(inset, imageYOrigin, imageSize, imageSize)];
        
        if(_productCategory.subCategoryList.count > 0)
            [_expandedStateImageView setImage:_plusImage];
        else
            [_expandedStateImageView setImage:_goImage];
        
        [self addSubview:_expandedStateImageView];
        
        //        [_expandedStateImageView setImage:[UIImage imageNamed:@"forward_button"]];
        
        if(_productCategory.subCategoryList.count > 0)
            categoryCount = [NSString stringWithFormat:@"(%lu)", (unsigned long)_productCategory.subCategoryList.count];
        
        UILabel *categoryNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, textYOrigin, textWidth, textHeight)];
        categoryNameLabel.backgroundColor = [UIColor clearColor];
//        categoryNameLabel.font = [Utility fontForFamily:_app._normalFont andHeight:17.0];
        [categoryNameLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size13]];
        categoryNameLabel.textAlignment = NSTextAlignmentLeft;
        categoryNameLabel.textColor = [UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0];
        categoryNameLabel.text = [NSString stringWithFormat:@"%@ %@", _productCategory.name, categoryCount];
        [self addSubview:categoryNameLabel];
        
        //        UILabel *bottomLine = [[UILabel alloc] initWithFrame:CGRectMake(0, frame.size.height - 1, frame.size.width, 1)];
        //        bottomLine.backgroundColor = [UIColor colorWithRed:.85 green:0.0 blue:0.0 alpha:1.00];
        //        [bottomLine setBackgroundColor:[UIColor lightGrayColor]];
        //        [self addSubview:bottomLine];
    }
    
    return self;
}


- (void)setExpandedState:(BOOL)expanded
{
    _expanded = expanded;
    
    if(_expanded == YES){
        //        int imageSize = self.frame.size.height * .4;
        //        CGRect frame = _expandedStateImageView.frame;
        //        frame.size.width = imageSize;
        //        frame.size.height = imageSize/1.5;
        //        [_expandedStateImageView setFrame:frame];
        [_expandedStateImageView setImage:_minusImage];
    }
    else{
        //        int imageSize = self.frame.size.height * .4;
        //        CGRect frame = _expandedStateImageView.frame;
        //        frame.size.width = imageSize/1.5;
        //        frame.size.height = imageSize;
        //        [_expandedStateImageView setFrame:frame];
        [_expandedStateImageView setImage:_plusImage];
    }
}


- (void)toggleExpandedState
{
    if(_expanded == YES)
        _expanded = NO;
    else
        _expanded = YES;
}


/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect
 {
 // Drawing code
 }
 */

@end
