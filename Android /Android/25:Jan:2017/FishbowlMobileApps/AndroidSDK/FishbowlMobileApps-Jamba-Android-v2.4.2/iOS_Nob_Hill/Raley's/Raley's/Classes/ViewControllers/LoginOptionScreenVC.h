//
//  LoginOptionScreenVC.h
//  Raley's
//
//  Created by Samar Gupta on 5/20/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "ProductDetails.h"
#import "TextDialog.h"

@class ProductDetails;

@interface LoginOptionScreenVC : UIViewController<UITextFieldDelegate,UIScrollViewDelegate>
{
    
    NSString            *_email;
    NSString            *_password;
    UITextField         *_activeField;
    WebService          *_service;
    ProgressDialog      *_progressDialog;
    TextDialog          *_editErrorDialog;
    BOOL                _useEmailResponder;
    BOOL                _usePasswordResponder;
}

@end
