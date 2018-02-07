//
//  LeftmenuCustomView.m
//  taco2
//
//  Created by HARSH on 07/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "LeftmenuCustomView.h"

@implementation LeftmenuCustomView


+(LeftmenuCustomView*)sharedInstRightMenu
{
    static LeftmenuCustomView *sharedInstRight = nil;
    static dispatch_once_t once;
    dispatch_once(&once, ^ {
        //    sharedInst = [[[NSBundle mainBundle]loadNibNamed:@"CustomBottomBarView" owner:self options:nil]lastObject];
        sharedInstRight = [LeftmenuCustomView leftmenuCustomView];
    });
    return sharedInstRight;
}
+(LeftmenuCustomView*)leftmenuCustomView
{
//    LeftmenuCustomView *customPopup = [[[NSBundle mainBundle]loadNibNamed:@"LeftmenuCustomView" owner:self options:nil]lastObject];
//    if(customPopup != nil || [customPopup isKindOfClass:[LeftmenuCustomView class]]){
//        return customPopup;
//    }else{
//        return nil;
//    }
    
    
    
    
    
    LeftmenuCustomView *customPopup = [[[NSBundle mainBundle]loadNibNamed:@"LeftmenuCustomView" owner:self options:nil]lastObject];
    if(customPopup != nil || [customPopup isKindOfClass:[LeftmenuCustomView class]]){
        return customPopup;
    }else{
        return nil;
    }
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
