//
//  TermsAndConditionsViewController.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 26/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "TermsAndConditionsViewController.h"
#import "ModelClass.h"

@interface TermsAndConditionsViewController ()
{
    ModelClass  * obj;

}
@property (weak, nonatomic) IBOutlet UIWebView *webview;
@property (weak, nonatomic) IBOutlet UIImageView *headerImage;
@property (weak, nonatomic) IBOutlet UIImageView *companyLogoImage;

@end

@implementation TermsAndConditionsViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // data fetch from mobilesetting
    obj=[ModelClass sharedManager];
   // NSString * str = [obj reterieveuserDefaultData:@"mobileSetting"];

   // http://d7be05wqyprgy.cloudfront.net/201969E1BFD242E189FE7B6297B1B5A6/loyalty/TermsAndConditions/c0e6ebaf86ee48b8b871de61687958f1.html
    
  //  NSLog(@"str-----%@",[str valueForKey:@"termsAndConditions"]);
    
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    
    
    NSString * strUrl = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"termsAndConditionsUrl"]];
    
    NSString   *urlString=strUrl;
    NSURL *url=[NSURL URLWithString:urlString];
    NSURLRequest *obj1=[NSURLRequest requestWithURL:url];
    [self.webview loadRequest:obj1];
    
    
    
    //[self.webview loadHTMLString:strUrl baseURL:nil];
    
    self.webview.backgroundColor = [UIColor clearColor];
    [self.webview setOpaque:NO];
    
    
    // header image
//    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
//    NSURL *url3 = [NSURL URLWithString:
//                   str3];
//    
//    [self.headerImage sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:
                   str2];
    [self.backgroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    
    NSString * str = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    NSURL *url1 = [NSURL URLWithString:
                   str];
    [self.companyLogoImage sd_setImageWithURL:url1 placeholderImage:[UIImage imageNamed:@"nil"]];
    
}



#pragma mark - memory method

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}
- (IBAction)termsBackAction:(id)sender
{
    [self.delegate termsBack];
}



@end
