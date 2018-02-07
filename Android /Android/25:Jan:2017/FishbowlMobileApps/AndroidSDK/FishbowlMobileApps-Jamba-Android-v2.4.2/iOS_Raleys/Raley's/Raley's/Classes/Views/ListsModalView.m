//
//  ListsModalView.m
//  Raley's
//
//  Created by Bill Lewis on 12/11/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ListsModalView.h"
#import "Utility.h"

@implementation ListsModalView

@synthesize _contentView;

- (id)initWithFrame:(CGRect)frame contentWidth:(int)width contentHeight:(int)height adjustedTop:(int)adjustedTop
{
    self = [super initWithFrame:frame];

    if(self)
    {
        self.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.8];

        if(adjustedTop == 0)
            _contentView = [[UIImageView alloc] initWithFrame:CGRectMake((frame.size.width - width) / 2, (frame.size.height - height) / 2, width, height)];
        else
            _contentView = [[UIImageView alloc] initWithFrame:CGRectMake((frame.size.width - width) / 2, adjustedTop, width, height)];

        _contentView.image = [UIImage imageNamed:@"list_dialog_background"];
        [self addSubview:_contentView];

        int listIconWidth = width * .25;
        UIImageView *listIcon = [[UIImageView alloc] initWithFrame:CGRectMake((_contentView.frame.size.width - listIconWidth) / 2, _contentView.frame.size.height * .1, listIconWidth, height * .35)];
        listIcon.image = [UIImage imageNamed:@"list_dialog_icon"];
        [_contentView addSubview:listIcon];

        int cancelButtonSize;

        if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
            cancelButtonSize = frame.size.width * .10;
        else
            cancelButtonSize = frame.size.width * .08;        

        UIButton *cancelButton = [[UIButton alloc] initWithFrame:CGRectMake(_contentView.frame.origin.x + _contentView.frame.size.width - cancelButtonSize, _contentView.frame.origin.y, cancelButtonSize, cancelButtonSize)];
        [cancelButton setBackgroundImage:[UIImage imageNamed:@"button_close"] forState:UIControlStateNormal];
        [cancelButton addTarget:self action:@selector(cancel) forControlEvents:UIControlEventTouchDown];
        [self addSubview:cancelButton];
    }

    return self;
}




- (void)cancel
{
    [self removeFromSuperview];
}


@end
