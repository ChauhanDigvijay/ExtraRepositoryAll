//
//  ChangePasswordView.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ChangePasswordButton <NSObject>

// custom delegate method

-(void)changePasswordApi;
-(void)changePasswordBackAction;

@end


@interface ChangePasswordView : UIViewController

@property (unsafe_unretained, nonatomic) IBOutlet UITextField *passwordNew;
@property (unsafe_unretained, nonatomic) IBOutlet UITextField *passwordConfirm;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *changePasswordBtn;
@property (weak,nonatomic) id<ChangePasswordButton> delegate;


@end
