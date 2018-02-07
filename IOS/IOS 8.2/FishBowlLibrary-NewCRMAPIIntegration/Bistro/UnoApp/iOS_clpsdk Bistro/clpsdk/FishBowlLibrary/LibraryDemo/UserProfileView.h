//
//  UserProfileView.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ChangePassword <NSObject>

// custom delegate method

-(void)changePasswordAction;
-(void)updateProfileBackAction;
-(void)logOutProfileAction;
-(void)updateProfileAction;


@end

@interface UserProfileView : UIViewController

@property (unsafe_unretained, nonatomic) IBOutlet UIView *changePasswordView;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *backgroundImage;
@property (weak,nonatomic) id<ChangePassword> delegate;

@end
