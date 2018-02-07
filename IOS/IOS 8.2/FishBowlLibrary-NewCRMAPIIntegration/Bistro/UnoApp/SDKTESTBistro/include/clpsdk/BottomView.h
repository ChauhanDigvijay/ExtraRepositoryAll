//
//  BottomView.h
//  clpsdk
//
//  Created by surendra pathak on 20/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DirectionMapView.h"
@protocol bottomView <NSObject>
@optional

-(void)tapCallMethod;

@end
@interface BottomView : UIView
+(BottomView *)sharedBottomView;
+(BottomView *)sharedBottomCustomView;
@property (nonatomic , assign) id <bottomView> rightMenuDelegate;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *btnCallOutlet;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblCompanyName;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblStoreName;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblStoreAddress;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblDistance;
- (IBAction)tapCall:(UIButton *)sender;

@end
