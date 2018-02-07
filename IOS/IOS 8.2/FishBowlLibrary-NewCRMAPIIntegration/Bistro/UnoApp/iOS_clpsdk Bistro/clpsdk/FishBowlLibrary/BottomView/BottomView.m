//
//  BottomView.m
//  clpsdk
//
//  Created by surendra pathak on 20/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "BottomView.h"

@implementation BottomView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
+(BottomView *)sharedBottomView
{
    static BottomView *sharedMyManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedMyManager = [BottomView sharedBottomCustomView];
        
    });
    return sharedMyManager;
}
+(BottomView *)sharedBottomCustomView
{
    BottomView * bottomObj = [[[NSBundle mainBundle]loadNibNamed:@"BottomView" owner:self options:nil]lastObject];
    
    if(bottomObj != nil || [bottomObj isKindOfClass:[BottomView class]]){
        
        return bottomObj;
    }else{
        return nil;
    }
}



- (IBAction)tapCall:(UIButton *)sender
{
    NSLog(@"tapCall");
    
    NSString * phoneNumber1 = [[NSUserDefaults standardUserDefaults]valueForKey:@"phoneNumber"];
    
    NSLog(@"phoneNumber ---------- %@",phoneNumber1);
    //NSString *phoneNumber;
    NSURL *URL ;
    
    if(phoneNumber1.length!=0)
    {//"tel://900-3440-567"
        
         URL = [NSURL URLWithString:[NSString stringWithFormat:@"tel://%@",phoneNumber1]];
        [[UIApplication sharedApplication] openURL:URL];
        

    }
   NSLog(@"phoneNumber2 ---------- %@",URL);
}

- (IBAction)mapBtn_Action:(id)sender
{
    
    [[NSNotificationCenter defaultCenter]
     postNotificationName:@"TestNotification"
     object:self];
    
//    DirectionMapView * obj1 = [[DirectionMapView alloc]initWithNibName:@"DirectionMapView" bundle:nil];
  
}




@end
