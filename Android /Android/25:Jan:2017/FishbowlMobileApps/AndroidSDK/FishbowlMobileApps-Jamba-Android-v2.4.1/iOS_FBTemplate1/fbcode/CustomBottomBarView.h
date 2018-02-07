//
//  CustomBottomBarView.h
//  FBTemplate2
//
//  Created by HARSH on 10/12/15.
//  Copyright © 2015 Fishbowl. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol BottomDelegate <NSObject>
@optional
// Delegate optional methods

-(void)menuShow;
-(void)orderPage;
-(void)orderHistory;
-(void)offerPage;


@end

@interface CustomBottomBarView : UIView


@property (strong, nonatomic) IBOutlet NSLayoutConstraint *leadingConstraintsOfMainMenuButton;

@property (assign, nonatomic) NSInteger tagOfSelectedItem;
@property (weak, nonatomic) IBOutlet UILabel *orderHistoryCount;
@property (weak, nonatomic) IBOutlet UILabel *orderCount;

@property (weak, nonatomic) IBOutlet UILabel *offerCount;
@property (strong, nonatomic) IBOutlet UIButton *orderPageOutlet;
@property (strong, nonatomic) IBOutlet UIButton *offerOutlet;
@property (weak, nonatomic) IBOutlet UIButton *menuButtonOutlet;
- (IBAction)offerAction:(id)sender;
- (IBAction)orderHistoryAction:(id)sender;
- (IBAction)menuShowAction:(id)sender;

@property (weak, nonatomic) IBOutlet UIButton *orderHistoryOutlet;

- (IBAction)orderPageAction:(id)sender;
+(CustomBottomBarView*)sharedInst;
+(CustomBottomBarView*)customBottomBarView;
@property (nonatomic, assign) id<BottomDelegate>bottomDelegate;
@end
