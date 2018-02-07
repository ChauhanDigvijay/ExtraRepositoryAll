//
//  ShoppingMenu.m
//  Raley's
//
//  Created by Bill Lewis on 11/23/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ShoppingMenu.h"
#import "Utility.h"
#import "Logging.h"

@implementation ShoppingMenu

@synthesize _menuButton;

- (id)initWithFrame:(CGRect)frame parentView:(UIView *)parentView itemHeight:(int)itemHeight items:(NSArray *)items responder:(id)responder
{
    CGFloat i_width = 1.5; // Increased Width
    CGFloat font_size = 12;
    
    _app = (id)[[UIApplication sharedApplication] delegate];
    _parentView = parentView;
    itemHeight =itemHeight;
    int topItemHeight = frame.size.height;
    int baselineHeight = topItemHeight * .05;
    int itemsFrameHeight = topItemHeight + (int)([items count] * itemHeight) + baselineHeight;
    int yOrigin = 0;
    _itemsVisibleFrame = CGRectMake(frame.origin.x, frame.origin.y , frame.size.width * i_width , itemsFrameHeight);
    _itemsHiddenFrame = CGRectMake(frame.origin.x, frame.origin.y - itemsFrameHeight, frame.size.width * i_width, itemsFrameHeight);
    
    //    _itemsVisibleFrame = CGRectMake(frame.origin.x, frame.origin.y - itemsFrameHeight, frame.size.width, itemsFrameHeight);
    //    _itemsHiddenFrame = CGRectMake(frame.origin.x, frame.origin.y , frame.size.width, itemsFrameHeight);
    
   self = [super initWithFrame:frame];
    
    
    if(self)
    {
        _itemsView = [[UIView alloc] initWithFrame:_itemsHiddenFrame];
        UIButton *item;
        
        // filler behind the menu button
        UIImageView *dummy = [[UIImageView alloc] initWithFrame:CGRectMake(0, yOrigin, frame.size.width * i_width, topItemHeight)];
        dummy.image = [UIImage imageNamed:@"more_dropdown"];
        [_itemsView addSubview:dummy];
        yOrigin += topItemHeight;
        //        yOrigin += (topItemHeight);
        
        for(int i = 0; i < [items count]; i++)
        {
            NSString *string = [items objectAtIndex:i];
            NSArray *array = [string componentsSeparatedByCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@":"]];
            NSString *key = [array objectAtIndex:0];
            NSString *selectorName = [array objectAtIndex:1];
            
            item = [[UIButton alloc] initWithFrame:CGRectMake(0, yOrigin, frame.size.width * i_width, itemHeight)];
            
            if(i == 0)
            {
                [item setBackgroundImage:[UIImage imageNamed:@"drop_menu_top_unselected"] forState:UIControlStateNormal];
                [item setBackgroundImage:[UIImage imageNamed:@"drop_menu_top_selected"] forState:UIControlStateSelected];
            }
            else
            {
                [item setBackgroundImage:[UIImage imageNamed:@"drop_menu_unselected"] forState:UIControlStateNormal];
                [item setBackgroundImage:[UIImage imageNamed:@"drop_menu_selected"] forState:UIControlStateSelected];
            }
            
            [item addTarget:responder action:NSSelectorFromString(selectorName) forControlEvents:UIControlEventTouchUpInside];
            item.titleLabel.font = [Utility fontForSize:_app._normalFont forString:@"Accepted Offers" forSize:CGSizeMake(frame.size.width * .9, itemHeight * .8)];
            [item setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            [item.titleLabel setFont:[UIFont systemFontOfSize:font_size]];
            [item setTitle:key forState:UIControlStateNormal];
            [_itemsView addSubview:item];
            
            yOrigin += itemHeight;
        }
        
        UIImageView *baselineImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, yOrigin, frame.size.width * i_width, baselineHeight)];
        [baselineImage setImage:[UIImage imageNamed:@"drop_menu_baseline"]];
        [_itemsView addSubview:baselineImage];
        
        [_parentView addSubview:_itemsView];
        _itemsView.hidden = YES;
        
        _menuButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        [self addSubview:_menuButton];
    }
    
    return self;

}

-(void)UpdateMenuItems:(CGRect)frame parentView:(UIView *)parentView itemHeight:(int)itemHeight items:(NSArray *)items responder:(id)responder
{
    
    
    for(UIView *view in _itemsView.subviews){
        [view removeFromSuperview];
    }
    
    CGFloat i_width = 1.5; // Increased Width
    CGFloat font_size = 12;
    
    _app = (id)[[UIApplication sharedApplication] delegate];
    _parentView = parentView;
    itemHeight =itemHeight;
    int topItemHeight = frame.size.height;
    int baselineHeight = topItemHeight * .05;
    int itemsFrameHeight = topItemHeight + (int)([items count] * itemHeight) + baselineHeight;
    int yOrigin = 0;
    _itemsVisibleFrame = CGRectMake(frame.origin.x, frame.origin.y , frame.size.width * i_width , itemsFrameHeight);
    _itemsHiddenFrame = CGRectMake(frame.origin.x, frame.origin.y - itemsFrameHeight, frame.size.width * i_width, itemsFrameHeight);
    
        UIButton *item;
        yOrigin += topItemHeight;
    
        for(int i = 0; i < [items count]; i++)
        {
            NSString *string = [items objectAtIndex:i];
            NSArray *array = [string componentsSeparatedByCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@":"]];
            NSString *key = [array objectAtIndex:0];
            NSString *selectorName = [array objectAtIndex:1];
            
            item = [[UIButton alloc] initWithFrame:CGRectMake(0, yOrigin, frame.size.width * i_width, itemHeight)];
            
            if(i == 0)
            {
                [item setBackgroundImage:[UIImage imageNamed:@"drop_menu_top_unselected"] forState:UIControlStateNormal];
                [item setBackgroundImage:[UIImage imageNamed:@"drop_menu_top_selected"] forState:UIControlStateSelected];
            }
            else
            {
                [item setBackgroundImage:[UIImage imageNamed:@"drop_menu_unselected"] forState:UIControlStateNormal];
                [item setBackgroundImage:[UIImage imageNamed:@"drop_menu_selected"] forState:UIControlStateSelected];
            }
            
            [item addTarget:responder action:NSSelectorFromString(selectorName) forControlEvents:UIControlEventTouchUpInside];
            item.titleLabel.font = [Utility fontForSize:_app._normalFont forString:@"Accepted Offers" forSize:CGSizeMake(frame.size.width * .9, itemHeight * .8)];
            [item setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            [item.titleLabel setFont:[UIFont systemFontOfSize:font_size]];
            [item setTitle:key forState:UIControlStateNormal];
            [_itemsView addSubview:item];
            
            yOrigin += itemHeight;
        }
        
        UIImageView *baselineImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, yOrigin, frame.size.width * i_width, baselineHeight)];
        [baselineImage setImage:[UIImage imageNamed:@"drop_menu_baseline"]];
        [_itemsView addSubview:baselineImage];
   
}


- (void)setSelectedImage:(UIImage *)image
{
    [_menuButton setBackgroundImage:image forState:UIControlStateHighlighted];
}


- (void)menuButtonPressed
{
    if(_itemsView.frame.origin.y == _itemsHiddenFrame.origin.y)
    {
        _itemsView.hidden = NO;
        [self showItems];
    }
    else
    {
        [self hideItems];
        _itemsView.hidden = YES;
    }
}


- (void)showItems
{
    if(_itemsView.frame.origin.y == _itemsVisibleFrame.origin.y) // already visible
        return;

    _itemsView.hidden = NO;
    [UIView animateWithDuration:0.33f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut | UIViewAnimationOptionAllowUserInteraction
                     animations:^{ [_itemsView setFrame:_itemsVisibleFrame]; }
                     completion:^(BOOL finished){ _itemsView.hidden = NO; }];
}


- (void)hideItems
{
    if(_itemsView.frame.origin.y == _itemsHiddenFrame.origin.y) // already hidden
        return;

    [UIView animateWithDuration:0.33f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut | UIViewAnimationOptionAllowUserInteraction
                     animations:^{ [_itemsView setFrame:_itemsHiddenFrame]; }
                     completion:^(BOOL finished){ _itemsView.hidden = YES; }];
}


- (BOOL)hidden
{
    if(_itemsView.frame.origin.y == _itemsHiddenFrame.origin.y) // hidden
        return YES;
    else
        return NO;
}


@end
