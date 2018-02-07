//
//  ViewController.m
//  clp register
//
//  Created by VT001 on 20/02/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import "ViewController.h"
#import "NSBundle+Language.h"
#import "ZBarSDK.h"
#import <ZXingObjC.h>

@interface ViewController ()
{
    REST *rest;
    Company *company;
    NSString *preferredLanguage;
    UITextField *_activeField;
    ZXCapture *zxingCapture;
    NSMutableDictionary *_initialMessage;
    NSString *_bgImagePath;
}
@end

@implementation ViewController

- (void)viewDidLoad {
    rest = [[REST alloc]init];
    preferredLanguage = @"es";
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWasShown:) name:UIKeyboardDidShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillBeHidden:) name:UIKeyboardWillHideNotification object:nil];
    
    iconLoader.hidesWhenStopped=true;
    [iconLoader startAnimating];
    
    [rest getCompanyDetails:^(Company *response) {
        company = response;
        if(company.companyLogo){
            Downloader *download = [[Downloader alloc]init];
            [download downloadImage:company.companyLogo completion:^(NSString *filePath) {
                [iconLoader stopAnimating];
                if(filePath && [[NSFileManager defaultManager]fileExistsAtPath:filePath]){
                    [logoImage setImage:[UIImage imageWithContentsOfFile:filePath]];
                }
            }];
        }
        if(company.companyBackGround){
            Downloader *download = [[Downloader alloc]init];
            [download downloadImage:company.companyBackGround completion:^(NSString *filePath) {
                if(filePath && [[NSFileManager defaultManager]fileExistsAtPath:filePath]){
                    // For scannerView
                    _bgImagePath = filePath;
                    
                    [bgImage setImage:[self blur:[UIImage imageWithContentsOfFile:filePath]]];
                    [formContainer setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
                    formContainer.alpha=0.72;
                    [formContainer bringSubviewToFront:bgImage];
                    [self startAnimation];
                    [self startTimer];
                    
                    
                    
                }
            }];
        }
        
        if(company.initialMessage){
            NSError *jsonError;
            NSData *objectData = [company.initialMessage dataUsingEncoding:NSUTF8StringEncoding];
            NSMutableDictionary *json = [NSJSONSerialization JSONObjectWithData:objectData
                                                                 options:NSJSONReadingMutableContainers
                                                                   error:&jsonError];
            
            _initialMessage= json;
            
            [self applyInitialMessage];
            
        }
    }];
    

    //corner radius
    txt_cellPhone.clipsToBounds=YES;
    txt_customerName.clipsToBounds=YES;
    txt_emailID.clipsToBounds=YES;
    tv_address.clipsToBounds=YES;
    logoImage.clipsToBounds =YES;
    txt_tenantID.clipsToBounds = YES;
    
    [txt_cellPhone.layer setCornerRadius:4.0f];
    [txt_customerName.layer setCornerRadius:4.0f];
    [txt_emailID.layer setCornerRadius:4.0f];
    [tv_address.layer setCornerRadius:4.0f];
    [logoImage.layer setCornerRadius:4.0f];
    [txt_tenantID.layer setCornerRadius:4.0f];

    
    
    [txt_cellPhone.layer setBackgroundColor:[UIColor clearColor].CGColor];
    [txt_customerName.layer setBackgroundColor:[UIColor clearColor].CGColor];
    [txt_emailID.layer setBackgroundColor:[UIColor clearColor].CGColor];
    [txt_tenantID.layer setBackgroundColor:[UIColor clearColor].CGColor];
    
    [tv_address.layer setBackgroundColor:[UIColor clearColor].CGColor];

    [txt_cellPhone.layer setBorderWidth:0.8f];
    [txt_customerName.layer setBorderWidth:0.8f];
    [txt_emailID.layer setBorderWidth:0.8f];
    [txt_tenantID.layer setBorderWidth:0.8f];
    [tv_address.layer setBorderWidth:0.8f];
    
    [txt_cellPhone.layer setBorderColor:[UIColor whiteColor].CGColor];
    [txt_customerName.layer setBorderColor:[UIColor whiteColor].CGColor];
    [txt_emailID.layer setBorderColor:[UIColor whiteColor].CGColor];
    [txt_tenantID.layer setBorderColor:[UIColor whiteColor].CGColor];
    [tv_address.layer setBorderColor:[UIColor whiteColor].CGColor];
    
    UIView *lftview = [[UIView alloc]init];
    [lftview setFrame:CGRectMake(0, 0, 10, 55)];
    txt_cellPhone.leftView=lftview;
    txt_cellPhone.leftViewMode = UITextFieldViewModeAlways;
    
    UIView *lftview1 = [[UIView alloc]init];
    [lftview1 setFrame:CGRectMake(0, 0, 10, 55)];
    txt_customerName.leftView=lftview1;
    txt_customerName.leftViewMode = UITextFieldViewModeAlways;
    
    UIView *lftview2 = [[UIView alloc]init];
    [lftview2 setFrame:CGRectMake(0, 0, 10, 55)];
    txt_emailID.leftView=lftview2;
    txt_emailID.leftViewMode = UITextFieldViewModeAlways;
    
    UIView *lftview3 = [[UIView alloc]init];
    [lftview3 setFrame:CGRectMake(0, 0, 10, 55)];
    txt_tenantID.leftView=lftview3;
    txt_tenantID.leftViewMode = UITextFieldViewModeAlways;
    
    UIView *lftview4 = [[UIView alloc]init];
    [lftview4 setFrame:CGRectMake(0, 0, 10, 55)];
    tv_address.leftView=lftview4;
    tv_address.leftViewMode = UITextFieldViewModeAlways;
    
    [txt_cellPhone setDelegate:self];
    [txt_customerName setDelegate:self];
    [txt_emailID setDelegate:self];
    [txt_tenantID setDelegate:self];
    [tv_address setDelegate:self];
    
    btn_register.clipsToBounds=YES;
    [btn_register.layer setCornerRadius:4.0f];
    [loader stopAnimating];
    loader.hidesWhenStopped=true;
    
    [self refreshUiText];
    [self registerForKeyboardNotifications];
    [self setFrames];
    _barCode = [NSString new];
    [super viewDidLoad];
    
  //  [self drawPlaceholderInRect:txt_emailID.frame];
    
    // Do any additional setup after loading the view, typically from a nib.
}

-(void)applyInitialMessage{
    NSString *message = [NSString new];
    
    NSMutableArray *configSettingArray = [_initialMessage valueForKey:@"message"];
    for (NSMutableDictionary *dict in configSettingArray) {
        
        if(language_switch.on){
            NSString *configName = [dict valueForKey:@"langcode"];
            
            if([configName isEqualToString:@"en"]){
                message = [dict valueForKey:@"displaymessage"];
                break;
            }
        }else{
            NSString *configName = [dict valueForKey:@"langcode"];
            
            if([configName isEqualToString:@"sp"]){
                message = [dict valueForKey:@"displaymessage"];
                break;
            }
        }
    }
    [footer_info setText:message];
}

-(void)startTimer{
    //if(!timer){
        timer=[NSTimer scheduledTimerWithTimeInterval:4.0 target:self selector:@selector(startAnimation) userInfo:nil repeats:YES];
    //}
}

-(void)stopTimer{
    //if([timer isValid]){
        [timer invalidate];
   // }
    //timer = nil;
}

-(void)startAnimation{
    
    [mainScroll setContentOffset:CGPointMake(0, 0)]; // Scrollview reset
    
    CGPoint moving_size;
    moving_size = CGPointMake(200, 0);
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:3.0]; // scroll to top animation duration
    [UIView setAnimationDelay:0.3];
    [UIView setAnimationCurve:UIViewAnimationCurveEaseIn];
    [mainScroll setContentOffset:moving_size];
    [UIView commitAnimations];
    
    CATransition *transition = [CATransition animation];
    transition.duration = 0.5; // fade animation duration
    transition.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
    transition.type = kCATransitionFade;
    transition.delegate = self;
    
    [mainScroll.layer addAnimation:transition forKey:nil];
    
}

-(void)viewWillDisappear:(BOOL)animated{
    //[self stopTimer];
}
-(void)viewWillAppear:(BOOL)animated{
    [formContainer setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    txt_tenantID.text = _barCode;
    //[self startTimer];
}

-(IBAction)customerRegistration:(id)sender{

    [appScroller setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [timer invalidate];
    
//    if(activeTextView)
//        [activeTextView resignFirstResponder];
    if(_activeField)
        [_activeField resignFirstResponder];
    Customer *customer = [[Customer alloc]init];
//    if(txt_customerName.text==nil || txt_customerName.text.length==0 || txt_customerName.text.length>60){
//        [self showAlert:NSLocalizedString(@"Invalid Name",nil)];
//        return;
//    }else if(txt_cellPhone.text==nil || txt_cellPhone.text.length==0 || ![self isPhoneNumber:txt_cellPhone.text]){
//        [self showAlert:NSLocalizedString(@"Invalid Mobile Phone",nil)];
//        return;
//    }else if(txt_emailID.text==nil || txt_emailID.text.length==0 || txt_emailID.text.length>100){
//        [self showAlert:NSLocalizedString(@"Invalid Email ID",nil)];
//        return;
//    }else if(![self isEmail:txt_emailID.text]){
//        [self showAlert:NSLocalizedString(@"Invalid Email ID",nil)];
//        return;
//    }else if(tv_address.text==nil || tv_address.text.length>60){
//        [self showAlert:NSLocalizedString(@"Invalid Address",nil)];
//        return;
//    }
    customer.emailID=txt_emailID.text;
    customer.cellPhone=txt_cellPhone.text;
    customer.firstName=txt_customerName.text;
    customer.addressLine1=tv_address.text;
    customer.customerTenantID = txt_tenantID.text;
    [loader startAnimating];
    
    [rest registerCustomerInfo:customer completion:^(NSMutableDictionary *response) {
        [loader stopAnimating];
        
        BOOL successFlag=[[response valueForKey:@"successFlag"] boolValue];
        NSString *message = [response valueForKey:@"message"];
        if(successFlag){
            NSMutableArray *configSettingArray = [response valueForKey:@"configSetting"];
            for (NSMutableDictionary *dict in configSettingArray) {
            
            if(language_switch.on){
                NSString *configName = [dict valueForKey:@"configName"];
                
                if([configName isEqualToString:@"IPAD_ADD_CUSTOMER_SUCCESS_ENGLISH"]){
                    message = [dict valueForKey:@"configValue"];
                    break;
                }
            }else{
                NSString *configName = [dict valueForKey:@"configName"];
                
                if([configName isEqualToString:@"IPAD_ADD_CUSTOMER_SUCCESS_SPANISH"]){
                    message = [dict valueForKey:@"configValue"];
                    break;
                }
            }
          }
        
        }
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:NSLocalizedString(@"Message",nil) message:message delegate:nil cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
        [alert show];
        
        if (successFlag) {
            [txt_cellPhone setText:@""];
            [txt_customerName setText:@""];
            [txt_emailID setText:@""];
            [tv_address setText:@""];
            [txt_tenantID setText:@""];
            [self refreshUiText];
       }
        
    }];
}
-(void)showAlert:(NSString*)msg{
    UIAlertView *alert = [[UIAlertView alloc]initWithTitle:NSLocalizedString(@"Message",nil) message:msg delegate:nil cancelButtonTitle:nil otherButtonTitles:@"Ok", nil];
    [alert show];
}

- (BOOL) isEmail: (NSString *) emailText {
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:emailText];
}
-(BOOL) isPhoneNumber:(NSString *)phone{
    //    NSCharacterSet* notDigits = [[NSCharacterSet decimalDigitCharacterSet] invertedSet];
    //    if ([phone rangeOfCharacterFromSet:notDigits].location == NSNotFound && phone.length==10)
    //    {
    //        return true;
    //    }else{
    //        return false;
    //    }
    NSNumberFormatter *_numberFormatter=[[NSNumberFormatter alloc]init];
    if(phone.length==10) return true;
    if(phone.length < 12 ||
       ![[phone substringWithRange:NSMakeRange(3, 1)] isEqualToString:@"-"] ||
       ![[phone substringWithRange:NSMakeRange(7, 1)] isEqualToString:@"-"] ||
       [_numberFormatter numberFromString:[phone substringWithRange:NSMakeRange(0, 3)]] == nil ||
       [_numberFormatter numberFromString:[phone substringWithRange:NSMakeRange(4, 3)]] == nil ||
       [_numberFormatter numberFromString:[phone substringWithRange:NSMakeRange(8, 4)]] == nil){
        return false;
    }else{
        return true;
    }
}
- (void)registerForKeyboardNotifications
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasShown:)
                                                 name:UIKeyboardDidShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillBeHidden:)
                                                 name:UIKeyboardWillHideNotification object:nil];
    
}

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    _activeField = textField;
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    _activeField = nil;
}


-(BOOL)textFieldShouldReturn:(UITextField *)textField {
    if(textField == txt_customerName){
        [txt_cellPhone becomeFirstResponder];
    }else if(textField == txt_cellPhone){
        [txt_emailID becomeFirstResponder];
    } else if(textField == txt_emailID){
        [tv_address becomeFirstResponder];
    }
    //    [textField resignFirstResponder];
    return YES;
}
//-(void)textViewDidBeginEditing:(UITextView *)textView{
//    activeTextView=textView;
//}
//- (void)textViewDidEndEditing:(UITextView *)textView
//{
//    activeTextView=nil;
//}
//
//// Called when the UIKeyboardWillHideNotification is sent
//- (void)keyboardWillBeHidden:(NSNotification*)aNotification
//{
//    UIEdgeInsets contentInsets = UIEdgeInsetsZero;
//    mainScroll.contentInset = contentInsets;
//    mainScroll.scrollIndicatorInsets = contentInsets;
//    [mainScroll setContentOffset:CGPointZero animated:YES];
//}


-(IBAction)ChangeLanguage:(id)sender{
    
    if(language_switch.on){
        [self applyLanguageChanges:@"en"];
        [language_lbl setText:@"ENGLISH"];
    }else{
        [self applyLanguageChanges:@"es"];
        [language_lbl setText:@"SPANISH"];
    }
    [self applyInitialMessage];
    
}

-(void)setFrames{
    //_childViewHiddenFrame = CGRectMake(0, 0, 0, 0);
    _navigationBarHeight = 0;
    _contentViewHeight = self.view.frame.size.height;
    _contentViewWidth = self.view.frame.size.width;
    
    _childViewVisibleFrame = CGRectMake(0,  _navigationBarHeight, _contentViewWidth, _contentViewHeight - _navigationBarHeight);
    _childViewHiddenFrame = CGRectMake(_contentViewWidth, _navigationBarHeight, _contentViewWidth, _contentViewHeight - _navigationBarHeight);
    
}

# pragma mark - Language

-(IBAction)setENLanguage:(id)sender{
    [self applyLanguageChanges:@"en"];
}

-(IBAction)setESLanguage:(id)sender{
    [self applyLanguageChanges:@"es"];
}

-(void)applyLanguageChanges:(NSString*)lang{
    [NSBundle setLanguage:lang.lowercaseString];
    [self refreshUiText];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(BOOL)shouldAutorotate{
    return NO;
}

-(void)refreshUiText{
    [lbl_address setText:NSLocalizedString(@"Address", nil)];
    [lbl_cellPhone setText:NSLocalizedString(@"Mobile Phone", nil)];
    [lbl_customerName setText:NSLocalizedString(@"Name", nil)];
    [lbl_emailID setText:NSLocalizedString(@"E-Mail", nil)];
    [btn_register setTitle:NSLocalizedString(@"Register", nil) forState:UIControlStateNormal];
    
    [txt_tenantID setPlaceholder:NSLocalizedString(@"Enter Id", nil)];
    [txt_customerName setPlaceholder:NSLocalizedString(@"Enter your name", nil)];
    [txt_cellPhone setPlaceholder:NSLocalizedString(@"Enter your mobile number", nil)];
    [txt_emailID setPlaceholder:NSLocalizedString(@"Enter your Email address", nil)];
    
    [tv_address setPlaceholder:NSLocalizedString(@"Enter your address", nil)];
    
    [Title_lbl setText:NSLocalizedString(@"CUSTOMER REGISTRATION", nil)];
    
    //[footer_info setText:NSLocalizedString(@"15% off on your next purchase and free membership when you enroll in our Familia Milagros Program. Only valid once.", nil)];

}

-(IBAction)clearText:(id)sender{
    [txt_cellPhone setText:@""];
    [txt_customerName setText:@""];
    [txt_emailID setText:@""];
    [tv_address setText:@""];
    [txt_tenantID setText:@""];
}
//UI IMAGE VIEW

- (UIImage*) blur:(UIImage*)theImage
{
    // ***********If you need re-orienting (e.g. trying to blur a photo taken from the device camera front facing camera in portrait mode)
    // theImage = [self reOrientIfNeeded:theImage];
    
    // create our blurred image
    CIContext *context = [CIContext contextWithOptions:nil];
    CIImage *inputImage = [CIImage imageWithCGImage:theImage.CGImage];
    
    // setting up Gaussian Blur (we could use one of many filters offered by Core Image)
    CIFilter *filter = [CIFilter filterWithName:@"CIGaussianBlur"];
    [filter setValue:inputImage forKey:kCIInputImageKey];
    [filter setValue:[NSNumber numberWithFloat:5.0f] forKey:@"inputRadius"];
    CIImage *result = [filter valueForKey:kCIOutputImageKey];
    
    // CIGaussianBlur has a tendency to shrink the image a little,
    // this ensures it matches up exactly to the bounds of our original image
    CGImageRef cgImage = [context createCGImage:result fromRect:[inputImage extent]];
    
    UIImage *returnImage = [UIImage imageWithCGImage:cgImage];//create a UIImage for this function to "return" so that ARC can manage the memory of the blur... ARC can't manage CGImageRefs so we need to release it before this function "returns" and ends.
    CGImageRelease(cgImage);//release CGImageRef because ARC doesn't manage this on its own.
    
    return returnImage;
    
    // *************** if you need scaling
    // return [[self class] scaleIfNeeded:cgImage];
}

+(UIImage*) scaleIfNeeded:(CGImageRef)cgimg {
    bool isRetina = [[[UIDevice currentDevice] systemVersion] intValue] >= 4 && [[UIScreen mainScreen] scale] == 2.0;
    if (isRetina) {
        return [UIImage imageWithCGImage:cgimg scale:2.0 orientation:UIImageOrientationUp];
    } else {
        return [UIImage imageWithCGImage:cgimg];
    }
}

- (UIImage*) reOrientIfNeeded:(UIImage*)theImage{
    
    if (theImage.imageOrientation != UIImageOrientationUp) {
        
        CGAffineTransform reOrient = CGAffineTransformIdentity;
        switch (theImage.imageOrientation) {
            case UIImageOrientationDown:
            case UIImageOrientationDownMirrored:
                reOrient = CGAffineTransformTranslate(reOrient, theImage.size.width, theImage.size.height);
                reOrient = CGAffineTransformRotate(reOrient, M_PI);
                break;
            case UIImageOrientationLeft:
            case UIImageOrientationLeftMirrored:
                reOrient = CGAffineTransformTranslate(reOrient, theImage.size.width, 0);
                reOrient = CGAffineTransformRotate(reOrient, M_PI_2);
                break;
            case UIImageOrientationRight:
            case UIImageOrientationRightMirrored:
                reOrient = CGAffineTransformTranslate(reOrient, 0, theImage.size.height);
                reOrient = CGAffineTransformRotate(reOrient, -M_PI_2);
                break;
            case UIImageOrientationUp:
            case UIImageOrientationUpMirrored:
                break;
        }
        
        switch (theImage.imageOrientation) {
            case UIImageOrientationUpMirrored:
            case UIImageOrientationDownMirrored:
                reOrient = CGAffineTransformTranslate(reOrient, theImage.size.width, 0);
                reOrient = CGAffineTransformScale(reOrient, -1, 1);
                break;
            case UIImageOrientationLeftMirrored:
            case UIImageOrientationRightMirrored:
                reOrient = CGAffineTransformTranslate(reOrient, theImage.size.height, 0);
                reOrient = CGAffineTransformScale(reOrient, -1, 1);
                break;
            case UIImageOrientationUp:
            case UIImageOrientationDown:
            case UIImageOrientationLeft:
            case UIImageOrientationRight:
                break;
        }
        
        CGContextRef myContext = CGBitmapContextCreate(NULL, theImage.size.width, theImage.size.height, CGImageGetBitsPerComponent(theImage.CGImage), 0, CGImageGetColorSpace(theImage.CGImage), CGImageGetBitmapInfo(theImage.CGImage));
        
        CGContextConcatCTM(myContext, reOrient);
        
        switch (theImage.imageOrientation) {
            case UIImageOrientationLeft:
            case UIImageOrientationLeftMirrored:
            case UIImageOrientationRight:
            case UIImageOrientationRightMirrored:
                CGContextDrawImage(myContext, CGRectMake(0,0,theImage.size.height,theImage.size.width), theImage.CGImage);
                break;
                
            default:
                CGContextDrawImage(myContext, CGRectMake(0,0,theImage.size.width,theImage.size.height), theImage.CGImage);
                break;
        }
        
        CGImageRef CGImg = CGBitmapContextCreateImage(myContext);
        theImage = [UIImage imageWithCGImage:CGImg];
        
        CGImageRelease(CGImg);
        CGContextRelease(myContext);
    }
    
    return theImage;
}


// UIKeyboardDidShowNotification handler
- (void)keyboardWasShown:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
    CGSize keyboardSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
//    [appScroller setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height-keyboardSize.height)];
    
    [appScroller setContentSize:CGSizeMake(self.view.frame.size.width, appScroller.frame.size.height+keyboardSize.height)];

    
    [self performSelector:@selector(scrollToTop:) withObject:aNotification afterDelay:0.1];
}

-(void)scrollToTop:(NSNotification*)aNotification{
    NSDictionary* info = [aNotification userInfo];
    CGSize keyboardSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    CGPoint scrollPoint = appScroller.contentOffset;
    scrollPoint.y = keyboardSize.height/2;
//    [appScroller setContentOffset:scrollPoint animated:YES];
    
    [appScroller setContentSize:CGSizeMake(self.view.frame.size.width, appScroller.frame.size.height+keyboardSize.height)];
    
}

// UIKeyboardWillHideNotification handler
- (void)keyboardWillBeHidden:(NSNotification*)aNotification
{
    NSTimeInterval interval = [[[aNotification userInfo]objectForKey:UIKeyboardAnimationDurationUserInfoKey]doubleValue];
    
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:(interval/2)]; // scroll to top animation duration
    [self login_dismiss_keyboard];
    [UIView commitAnimations];
}


-(void)login_dismiss_keyboard
{
    _activeField = nil;
    [txt_cellPhone resignFirstResponder];
    [txt_customerName resignFirstResponder];
    [txt_emailID resignFirstResponder];
    [tv_address resignFirstResponder];
    [appScroller setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [appScroller setContentSize:CGSizeMake(self.view.frame.size.width, self.view.frame.size.height)];

}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

#pragma mark - Scanner Methods

- (IBAction)showScannerView
{
    //[self showScannerView];
    
    ScannerController *scan = [[ScannerController alloc]init];
//    scan._tenantID = _barCode;
    scan.delegate = self;
    scan._motionImage = _bgImagePath;
    scan._logoImage = logoImage.image;
    [self presentViewController:scan animated:NO completion:nil];
}

-(void)setTenantID:(NSString*)tenantID{
    txt_tenantID.text = tenantID;
    _barCode = tenantID;
}

@end
