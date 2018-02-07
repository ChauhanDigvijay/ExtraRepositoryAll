//
//  PassbookLoader.m
//
//  Created by VT001 on 11/08/14.
//  Copyright (c) 2014 Ashwin Arun. All rights reserved.
//

#import "PassbookLoader.h"

@implementation PassbookLoader

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        NSArray *bundle = [[NSBundle mainBundle] loadNibNamed:@"PassbookLoader"
                                                        owner:nil options:nil];
        for (id object in bundle) {
            if ([object isKindOfClass:[PassbookLoader class]])
                self = (PassbookLoader *)object;
        }
        assert(self != nil && "PassbookLoader can't be nil");
        self.frame = frame;
    }
    return self;
}

-(void)startAnimation{
    
    [UIView animateWithDuration:0.3 delay:0 options:UIViewAnimationOptionCurveLinear animations:^{
        [self.loader setTransform:CGAffineTransformRotate(self.loader.transform, M_PI_2)];
    }completion:^(BOOL finished){
        if (finished) {
            [self startAnimation];
        }
    }];
}

@end
