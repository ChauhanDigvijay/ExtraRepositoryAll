//
//  HtmlOffersAndRewards.m
//  clpsdk
//
//  Created by Gourav Shukla on 26/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "HtmlOffersAndRewards.h"
#import "MBProgressHUD.h"

@interface HtmlOffersAndRewards ()<UIWebViewDelegate, MBProgressHUDDelegate>
{
    MBProgressHUD    * HUD;
}

@property (unsafe_unretained, nonatomic) IBOutlet UIWebView *webview;
@property (weak, nonatomic) IBOutlet UILabel *rewardsLabel;
@property (weak, nonatomic) IBOutlet UIView *rewardsView;

@end

@implementation HtmlOffersAndRewards

- (void)viewDidLoad {
    [super viewDidLoad];
    
   
    self.rewardsLabel.text = self.rewardsAndOfferTitle;
    
    if([self.bodyType isEqualToString:@"Html"])
    {
        [self.webview loadHTMLString:self.htmlBody baseURL:nil];
    }
    else if([self.bodyType isEqualToString:@"MyPoint"])
    {
         self.rewardsLabel.text = @"PointBankRewards";
        
      [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"PointBankOffer"];
        
      [[NSNotificationCenter defaultCenter] postNotificationName:@"RewardsAndOffer" object:nil];
        
        dispatch_queue_t queue = dispatch_queue_create("myqueue", NULL);
        dispatch_async(queue, ^{
            // create UIwebview, other things too
            // Perform on main thread/queue
            dispatch_async(dispatch_get_main_queue(), ^{
                
                NSURL *url = [NSURL URLWithString:self.htmlBody];
                NSLog(@"url print ----- %@",url);
                NSURLRequest *urlRequest = [NSURLRequest requestWithURL:url];
                [self.webview loadRequest:urlRequest];
            });
        });
    }
    else
    {
        NSURL *url = [NSURL URLWithString:self.htmlBody];
        NSLog(@"url print ----- %@",url);
        NSURLRequest *urlRequest = [NSURLRequest requestWithURL:url];
        [self.webview loadRequest:urlRequest];
    }
    
 
    [self shadoOffect:self.rewardsView];
}


-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
   
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
   
}
- (IBAction)backButton_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)webViewDidStartLoad:(UIWebView *)webView
{
    HUD = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
    [self.navigationController.view addSubview:HUD];
    
    HUD.delegate = self;
    HUD.labelText = @"Loading";
    [HUD show:YES];
}

-(void)webViewDidFinishLoad:(UIWebView *)webView
{
    [HUD hide:YES];
}



@end
