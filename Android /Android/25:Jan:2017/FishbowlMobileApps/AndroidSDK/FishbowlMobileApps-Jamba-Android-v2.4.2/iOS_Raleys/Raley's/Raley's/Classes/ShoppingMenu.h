//
//  ShoppingMenu.h
//  Raley's
//
//  Created by Bill Lewis on 11/23/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@interface ShoppingMenu : UIView
{
    AppDelegate  *_app;
    CGRect  _itemsVisibleFrame;
    CGRect  _itemsHiddenFrame;
    UIView  *_parentView;
    UIView  *_itemsView;
}

@property (nonatomic, strong)UIButton  *_menuButton;

- (id)initWithFrame:(CGRect)frame parentView:(UIView *)parentView itemHeight:(int)itemHeight items:(NSArray *)items responder:(id)responder;
-(void)UpdateMenuItems:(CGRect)frame parentView:(UIView *)parentView itemHeight:(int)itemHeight items:(NSArray *)items responder:(id)responder;
- (void)setSelectedImage:(UIImage *)image;
- (void)menuButtonPressed;
- (void)showItems;
- (void)hideItems;
- (BOOL)hidden;



@end
