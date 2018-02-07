//
//  SignUpViewController.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 19/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol GetStartedAction <NSObject>

// custom delegate method

-(void)startBtnAction;
-(void)backBtnAction;
-(void)termsAndConditionAction:(NSString *)str;
-(void)privacyPolicyAction:(NSString *)str;

@end


@interface SignUpViewController : UIViewController

@property (weak,nonatomic) id <GetStartedAction> delegate;
@property (weak, nonatomic) IBOutlet UIButton *getStartedBtn;
@property (weak, nonatomic) IBOutlet UIView *mainViewOL;

@property (weak, nonatomic) IBOutlet UIImageView *backgroundImg;
@property (strong, nonatomic) IBOutlet UIScrollView *textFieldScroll;

@end
