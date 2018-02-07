//
//  MapAnnotationView.m
//  Raley's
//
//  Created by Bill Lewis on 12/18/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "MapAnnotationView.h"

@implementation MapAnnotationView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}


- (UIView*)hitTest:(CGPoint)point withEvent:(UIEvent*)event
{
    UIView* hitView = [super hitTest:point withEvent:event];

    if(hitView != nil)
        [self.superview bringSubviewToFront:self];

    return hitView;
}


- (BOOL)pointInside:(CGPoint)point withEvent:(UIEvent*)event
{
    CGRect rect = self.bounds;
    BOOL isInside = CGRectContainsPoint(rect, point);

    if(!isInside)
    {
        for (UIView *view in self.subviews)
        {
            isInside = CGRectContainsPoint(view.frame, point);

            if(isInside)
                break;
        }
    }

    return isInside;
}


@end
