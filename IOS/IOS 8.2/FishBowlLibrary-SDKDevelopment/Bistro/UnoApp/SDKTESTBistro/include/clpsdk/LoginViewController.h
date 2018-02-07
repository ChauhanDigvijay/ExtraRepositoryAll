//
//  LoginViewController.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 19/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol NewUserAction <NSObject>

// custom delegate method

-(void)newUser;
-(void)signIn;
-(void)forgotPassword;

@end


@interface LoginViewController : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *emailTF;
@property (weak, nonatomic) IBOutlet UITextField *passwordTF;
@property (weak, nonatomic) IBOutlet UIButton *signInBtn;

@property(weak,nonatomic)id<NewUserAction>delegate;

@end
