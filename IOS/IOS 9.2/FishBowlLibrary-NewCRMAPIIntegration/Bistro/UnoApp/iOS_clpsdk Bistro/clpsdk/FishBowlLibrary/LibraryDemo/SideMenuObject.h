//
//  SideMenuObject.h
//  clpsdk
//
//  Created by Gourav Shukla on 20/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SideMenu.h"
#import <UIKit/UIKit.h>

@protocol PushNavigation <NSObject>
@optional

-(void)didSelectAtIndexPathRow:(NSIndexPath *)indexPath;
-(void)logOutBt_Action;

@end



@interface SideMenuObject : NSObject<UIGestureRecognizerDelegate,sideMenu>

@property (assign,nonatomic) id <PushNavigation> delegate;

// sideMenu Action
-(void)SideMenuAction:(UIView*)mainView;

// remove side
-(void)removeSideNave;


@end
