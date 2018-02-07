//
//  OffsetTextField.h
//  LibiOS
//
//  Created by Bill Lewis on 2/4/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OffsetTextField : UITextField
{
    int  _offset;
}


- (id)initWithFrame:(CGRect)frame offset:(int)offset;


@end
