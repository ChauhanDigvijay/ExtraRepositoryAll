//
//  MainViewController.h
//  taco2
//
//  Created by HARSH on 17/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MainViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIButton *facebooklogin;
@property (weak, nonatomic) IBOutlet UIButton *emaillogin;
- (IBAction)signIN:(id)sender;

@end
