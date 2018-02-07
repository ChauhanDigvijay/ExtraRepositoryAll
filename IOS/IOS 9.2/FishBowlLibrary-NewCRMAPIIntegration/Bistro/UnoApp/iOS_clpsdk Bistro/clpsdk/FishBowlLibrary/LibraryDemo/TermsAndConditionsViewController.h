//
//  TermsAndConditionsViewController.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 26/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol TermsConditionDelegate <NSObject>

-(void)termsBack;

@end

@interface TermsAndConditionsViewController : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;

@property (weak, nonatomic) IBOutlet UIImageView *backgroundImage;
@property (weak,nonatomic)id <TermsConditionDelegate> delegate;

@end
