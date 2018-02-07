//
//  PrivacyPolicyView.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "PrivacyPolicyView.h"
#import "ModelClass.h"


@interface PrivacyPolicyView ()
{
    ModelClass  * obj;
    
}
@property (weak, nonatomic) IBOutlet UIImageView * headerImage;
@property (weak, nonatomic) IBOutlet UIImageView * companyLogoImage;
@property (weak, nonatomic) IBOutlet UIView *privacyView;
@end

@implementation PrivacyPolicyView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    // data fetch from mobilesetting
    obj=[ModelClass sharedManager];
    //NSString * str = [obj reterieveuserDefaultData:@"mobileSetting"];
    
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"themeSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    NSLog(@"print dic %@",dict.description);
    NSArray *arrThemeConfigSettings = [[dict valueForKey:@"themeDetails"] valueForKey:@"themeConfigSettings"];
    NSString * strTermCondition;
    NSLog(@"arrThemeConfigSettings is %@",arrThemeConfigSettings.description);
    NSLog(@"arrThemeConfigSettings.count is %lu", (unsigned long)arrThemeConfigSettings.count);
    
    for (NSDictionary *dicSetting in arrThemeConfigSettings) {
        
        NSString * strPageName = [dicSetting objectForKey:@"pageName"];
        if ([strPageName isEqualToString:@"General"]) {
            
            NSArray *arrData = [dicSetting objectForKey:@"themeCreativeSettings"];
            
            for (NSDictionary *DictThemeCreativeSettings in arrData) {
                
                NSString *strKey = [DictThemeCreativeSettings objectForKey:@"configName"];
                if ([strKey isEqualToString:@"privacyPolicy"])
                {
                    strTermCondition = [DictThemeCreativeSettings objectForKey:@"configValue"];
                }
                
            }
        }
        
    }

    
    
    
    NSString * strUrl = [NSString stringWithFormat:@"http://%@",strTermCondition];
    NSString   *urlString=strUrl;
    NSURL *url=[NSURL URLWithString:urlString];
    NSURLRequest *obj1=[NSURLRequest requestWithURL:url];
    [self.webview loadRequest:obj1];
    
//    if([[dict valueForKey:@"privacyContentUrl"] hasSuffix:@".html"])
//    {
//        NSLog(@"html");
//    NSString * strUrl = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"privacyContentUrl"]];
//        [self.webview loadHTMLString:strUrl baseURL:nil];
//    }
//    else
//    {
//         NSLog(@"url");
//        NSString * strUrl = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"privacyContentUrl"]];
//        NSString   *urlString=strUrl;
//        NSURL *url=[NSURL URLWithString:urlString];
//        NSURLRequest *obj1=[NSURLRequest requestWithURL:url];
//        [self.webview loadRequest:obj1];
//    }
   
  
    self.webview.backgroundColor = [UIColor clearColor];
    [self.webview setOpaque:NO];

    // header image
//    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
//    NSURL *url3 = [NSURL URLWithString:
//                   str3];
//    
//    [self.headerImage sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
//    NSString * str = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
//    NSURL *url1 = [NSURL URLWithString:
//                  str];
//    [self.companyLogoImage sd_setImageWithURL:url1 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    
    // background image
//    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
//    NSURL *url2 = [NSURL URLWithString:
//                   str2];
//    [self.backgroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    [self shadoOffect:self.privacyView];
    
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


#pragma mark - back button action

- (IBAction)backBtn_Action:(id)sender
{
   // [self.delegate PrivacyBack];
    [self.navigationController popViewControllerAnimated:YES];
}


@end
