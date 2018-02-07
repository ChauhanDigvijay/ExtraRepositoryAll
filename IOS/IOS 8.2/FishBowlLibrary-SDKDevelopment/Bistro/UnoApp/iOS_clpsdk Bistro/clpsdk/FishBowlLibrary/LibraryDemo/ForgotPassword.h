//
//  ForgotPassword.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 28/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ForgotPasswordAction <NSObject>

-(void)forgotPasswordBackButtonAction;
//-(void)sendEmailAction;

@end

@interface ForgotPassword : UIViewController

@property (weak,nonatomic)id <ForgotPasswordAction> delegate;
@property (weak, nonatomic) IBOutlet UIImageView *backgroundImage;
@property (weak, nonatomic) IBOutlet UIButton *sendBtn;

@end
