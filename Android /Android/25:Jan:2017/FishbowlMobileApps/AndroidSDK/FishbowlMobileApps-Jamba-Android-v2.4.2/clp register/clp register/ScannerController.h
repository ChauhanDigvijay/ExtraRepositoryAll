//
//  ScannerController.h
//  clp register
//
//  Created by VT001 on 15/06/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZBarSDK.h"
#import <ZXingObjC.h>

@protocol ScannerDelegate <NSObject>
-(void)setTenantID:(NSString*)tenantID;
@end

@interface ScannerController : UIViewController<ZXCaptureDelegate>
{
    UIView                      *_scannerView;
    CGRect    _childViewHiddenFrame;
    int _contentViewWidth;
    int _contentViewHeight;
    int _scannerBackgroundWidth;
    int _scannerBackgroundHeight;
    int _navigationBarHeight;
    CGRect scanAreaRect;
    UIView *scanArea;
    UILabel *_scannerStatusLabel;
    BOOL scannerStart;
    NSDate *scanner_wait;
    CGRect _childViewVisibleFrame;
    NSString *_barCode;
    UIButton *backbutton;

}
@property(nonatomic,readwrite)NSString *_tenantID;
@property(nonatomic,readwrite)NSString *_motionImage;
@property(nonatomic,retain)UIImage *_logoImage;
@property(nonatomic,assign) id<ScannerDelegate> delegate;
@end
