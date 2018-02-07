//
//  LeftmenuCustomView.h
//  taco2
//
//  Created by HARSH on 07/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol LeftMenudelegate <NSObject>
@optional
// Delegate optional methods

-(void)saveClicked;


@end
@interface LeftmenuCustomView : UIView
@property (weak, nonatomic) IBOutlet UILabel *userName;
+(LeftmenuCustomView*)sharedInstRightMenu;
+(LeftmenuCustomView*)leftmenuCustomView;

@property (weak, nonatomic) IBOutlet UIView *signUpView;
@property (weak, nonatomic) IBOutlet UILabel *signInSignUpLabel;


@property (nonatomic, assign) id<LeftMenudelegate>leftMenuDelegate;
@property (strong, nonatomic) IBOutlet UITableView *tblViewRightMenu;

@end
