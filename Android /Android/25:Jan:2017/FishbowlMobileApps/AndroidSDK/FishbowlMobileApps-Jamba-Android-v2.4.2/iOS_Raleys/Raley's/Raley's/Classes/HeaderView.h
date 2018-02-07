//
//  HeaderView.h
//  Raley's
//
//  Created by Samar Gupta on 5/21/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol HeaderViewDelegate <NSObject>

-(void)backButtonClicked;

@end

@interface HeaderView : UIView
{

}
@property(nonatomic,assign)id<HeaderViewDelegate> delegate;
@end
