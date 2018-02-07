//
//  CustomBottomBarView.m
//  FBTemplate2
//
//  Created by HARSH on 10/12/15.
//  Copyright © 2015 Fishbowl. All rights reserved.
//

#import "CustomBottomBarView.h"

@implementation CustomBottomBarView




- (IBAction)offerAction:(UIButton*)sender
{
    
    _orderHistoryOutlet.selected=NO;
    _menuButtonOutlet.selected=NO;
    _orderPageOutlet.selected=NO;
    
    sender.selected = YES;
    if (sender.selected)
    {
        NSLog(@"%ld",(long)self.tagOfSelectedItem);
        self.tagOfSelectedItem= sender.tag;
    }
    else
    {
        
    }
    if (self.bottomDelegate != nil || [self.bottomDelegate performSelector:@selector(offerPage)])
    {
        [self.bottomDelegate offerPage];
    }
}

- (IBAction)orderHistoryAction:(UIButton*)sender
{
    
    _menuButtonOutlet.selected=NO;
    _orderPageOutlet.selected=NO;
    _offerOutlet.selected=NO;
    
    sender.selected = YES;
    if (sender.selected)
    {
        NSLog(@"%ld",(long)self.tagOfSelectedItem);
        self.tagOfSelectedItem= sender.tag;
    }
    else
    {
        
    }
    if (self.bottomDelegate != nil || [self.bottomDelegate performSelector:@selector(orderHistory)])
    {
        [self.bottomDelegate orderHistory];
    }
}

- (IBAction)menuShowAction:(UIButton*)sender
{
    _orderPageOutlet.selected=NO;
    _orderHistoryOutlet.selected=NO;
    _offerOutlet.selected=NO;
    sender.selected = !sender.selected;
    //    if (sender.selected)
    //    {
    //
    //    }
    //    else
    //    {
    switch (self.tagOfSelectedItem)
    {
        case 100:
            NSLog(@"%ld",(long)self.tagOfSelectedItem);
            _offerOutlet.selected=YES;
            break;
        case 200:
            NSLog(@"%ld",(long)self.tagOfSelectedItem);
            _orderHistoryOutlet.selected=YES;
            break;
        case 300:
            NSLog(@"%ld",(long)self.tagOfSelectedItem);
            _orderPageOutlet.selected=YES;
            break;
        default:
            break;
    }
    //    }
    if (self.bottomDelegate != nil || [self.bottomDelegate performSelector:@selector(menuShow)])
    {
        [self.bottomDelegate menuShow];
    }
}

- (IBAction)orderPageAction:(UIButton*)sender
{
    sender.selected = YES;
    _menuButtonOutlet.selected=NO;
    _orderHistoryOutlet.selected=NO;
    _offerOutlet.selected=NO;
    if (sender.selected)
    {
        NSLog(@"%ld",(long)self.tagOfSelectedItem);
        self.tagOfSelectedItem= sender.tag;
    }
    else
    {
        
    }
    
    if (self.bottomDelegate != nil || [self.bottomDelegate performSelector:@selector(orderPage)])
    {
        [self.bottomDelegate orderPage];
    }
    
}
+(CustomBottomBarView*)sharedInst
{
    static CustomBottomBarView *sharedInst = nil;
    static dispatch_once_t once;
    dispatch_once(&once, ^ {
        //    sharedInst = [[[NSBundle mainBundle]loadNibNamed:@"CustomBottomBarView" owner:self options:nil]lastObject];
        sharedInst = [CustomBottomBarView customBottomBarView];
    });
    return sharedInst;
}
+(CustomBottomBarView*)customBottomBarView
{
    CustomBottomBarView *customPopup = [[[NSBundle mainBundle]loadNibNamed:@"CustomBottomBarView" owner:self options:nil]lastObject];
    if(customPopup != nil || [customPopup isKindOfClass:[CustomBottomBarView class]]){
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
