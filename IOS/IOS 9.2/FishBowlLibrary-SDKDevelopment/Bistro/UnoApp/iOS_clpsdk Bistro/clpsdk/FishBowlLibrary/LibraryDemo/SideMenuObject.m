//
//  SideMenuObject.m
//  clpsdk
//
//  Created by Gourav Shukla on 20/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "SideMenuObject.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>

@implementation SideMenuObject
{
        SideMenu *sideMenu;
        BOOL  menuOpen;
        UIView * sideView;
}


// sideMenu Action

-(void)SideMenuAction:(UIView*)mainView
{
     sideMenu = [SideMenu sharedSideMenu];
     sideMenu.rightMenuDelegate = self;
    [sideMenu initializeData];
    [mainView addSubview:sideMenu];
    sideMenu.hidden = NO;
    
    [self MenuAction:mainView];
    
    // swipe gesture
    UISwipeGestureRecognizer *rightSwipeGesture = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeGesture:)];
    UISwipeGestureRecognizer *leftSwipeGesture = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeGesture:)];
    rightSwipeGesture.direction = UISwipeGestureRecognizerDirectionRight;
    leftSwipeGesture.direction = UISwipeGestureRecognizerDirectionLeft;
    
    [sideMenu addGestureRecognizer:rightSwipeGesture];
    [sideMenu addGestureRecognizer:leftSwipeGesture];
    
    rightSwipeGesture.numberOfTouchesRequired =1;
    leftSwipeGesture.numberOfTouchesRequired =1;
    
}

// side menu button

- (void)MenuAction:(UIView *)mainView
{
    
    sideMenu.layer.masksToBounds = NO;
    sideMenu.layer.shadowOffset = CGSizeMake(0,0);
    sideMenu.layer.shadowRadius = 2;
    sideMenu.layer.shadowOpacity = 0.5;
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenWidth = screenRect.size.width;
    CGFloat screenHeight = screenRect.size.height;
    sideView = mainView;
    CGRect  frame;
    frame = mainView.frame;
    [UIView setAnimationDelegate:self];
    [UIView setAnimationDidStopSelector:@selector(messageSlideFinished)];
    [UIView beginAnimations:@"slideMenu" context:(__bridge void *)(mainView)];
    [[[UIApplication sharedApplication]delegate]window].backgroundColor = [UIColor whiteColor];
    
    if(menuOpen)
    {
        frame.origin.x = 0;
        menuOpen = NO;
        sideMenu.hidden=YES;
        NSLog(@"first time");
    }
    else
    {
        
        if(screenWidth == 320 && screenHeight == 480)
        {
            frame.origin.x = -230;
            sideMenu.frame = CGRectMake(90,0,sideMenu.frame.size.width, mainView.frame.size.height);
        }
        else  if(screenWidth == 320 && screenHeight == 568)
        {
            frame.origin.x = -230;
            sideMenu.frame = CGRectMake(90,0,sideMenu.frame.size.width, mainView.frame.size.height);
        }
        else  if(screenWidth == 375 && screenHeight == 667)
        {
            frame.origin.x = -250;
            sideMenu.frame = CGRectMake(125,0,sideMenu.frame.size.width, mainView.frame.size.height);
        }
        else  if(screenWidth == 414 && screenHeight == 736)
        {
            frame.origin.x = -270;
            sideMenu.frame = CGRectMake(144,0,sideMenu.frame.size.width+20, mainView.frame.size.height);
        }
        [[[[UIApplication sharedApplication]delegate]window]addSubview:sideMenu];
        
        menuOpen = YES;
        sideMenu.hidden=NO;
        NSLog(@"second time");
    }
    mainView.frame = frame;
    [UIView commitAnimations];
}


-(void) messageSlideFinished
{
    NSLog(@"stop animation");
}


// delegate methods side menu
-(void)didSelectAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"selection time");
    sideMenu.hidden = YES;
    menuOpen = NO;
    NSLog(@"indexPath value -------- %ld",(long)indexPath.row);
    [self.delegate didSelectAtIndexPathRow:indexPath];
}


-(void)handleSwipeGesture:(UISwipeGestureRecognizer *)leftSwipe
{
    if (leftSwipe.direction == UISwipeGestureRecognizerDirectionLeft)
    {
        NSLog(@"swipeGesture left");
    }
    else
    {
        menuOpen = YES;
        [self MenuAction:sideView];
        NSLog(@"swipeGesture Right");
    }
}

-(void)removeSideNave
{
   // menuOpen = YES;
    CGRect  frame;
    frame = sideView.frame;
    [UIView setAnimationDelegate:self];
    [UIView setAnimationDidStopSelector:@selector(messageSlideFinished)];
    [UIView beginAnimations:@"slideMenu" context:(__bridge void *)(sideView)];
    frame.origin.x = 0;
    sideView.frame = frame;
    [UIView commitAnimations];
    sideMenu.hidden=YES;
}


// logout button Action

-(void)logout_Action
{
    [self removeSideNave];
     menuOpen = NO;
    [self.delegate logOutBt_Action];
}



@end
