//
//  PrivacyPolicyView.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol PrivacyPolicy <NSObject>

-(void)PrivacyBack;

@end

@interface PrivacyPolicyView : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *backgroundImage;

@property (weak, nonatomic) IBOutlet UIWebView *webview;

@property (weak,nonatomic)id <PrivacyPolicy> delegate;


@end
