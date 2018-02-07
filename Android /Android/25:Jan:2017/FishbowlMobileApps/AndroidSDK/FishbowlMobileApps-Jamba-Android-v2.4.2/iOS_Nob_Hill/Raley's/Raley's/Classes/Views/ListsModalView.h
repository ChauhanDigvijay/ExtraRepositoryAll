//
//  ListsModalView.h
//  Raley's
//
//  Created by Bill Lewis on 12/11/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ListsModalView : UIView

@property (nonatomic, strong)UIImageView *_contentView;

- (id)initWithFrame:(CGRect)frame contentWidth:(int)width contentHeight:(int)height adjustedTop:(int)adjustedTop;

@end
