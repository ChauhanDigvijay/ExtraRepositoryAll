//
//  ViewController.h
//  clp register
//
//  Created by VT001 on 20/02/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "REST.h"
#import "Downloader.h"
#import "Placeholder.h"
#import <ZXingObjC.h>
#import "ScannerController.h"

@class Placeholder;
@interface ViewController : UIViewController<UITextFieldDelegate,UITextViewDelegate, ZXCaptureDelegate,ScannerDelegate>
{
    IBOutlet Placeholder *txt_emailID;
    IBOutlet Placeholder *txt_cellPhone;
    IBOutlet Placeholder *txt_customerName;
//    IBOutlet UITextView *tv_address;
    IBOutlet Placeholder *tv_address;
    
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
    
    IBOutlet Placeholder *txt_tenantID;
    
    IBOutlet UIImageView *logoImage;
    IBOutlet UIImageView *bgImage;
    
    IBOutlet UILabel *lbl_emailID;
    IBOutlet UILabel *lbl_cellPhone;
    IBOutlet UILabel *lbl_customerName;
    IBOutlet UILabel *lbl_address;
    IBOutlet UIButton *btn_register;
    IBOutlet UIButton *btn_clear;
    IBOutlet UIView *formContainer;
    
    IBOutlet UIActivityIndicatorView *loader;
    IBOutlet UIActivityIndicatorView *iconLoader;

    IBOutlet UIScrollView *mainScroll;
//    UITextField *activeField;
//    UITextView *activeTextView;
    
    // newly added
    IBOutlet UISwitch *language_switch;
    IBOutlet UILabel *language_lbl;
    IBOutlet UILabel *Title_lbl;
    
    IBOutlet UILabel *footer_info;
    
    NSTimer *timer;
    
    IBOutlet UIScrollView *appScroller;

}
@end



