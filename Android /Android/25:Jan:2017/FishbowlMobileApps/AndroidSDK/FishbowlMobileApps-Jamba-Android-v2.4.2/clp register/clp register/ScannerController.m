//
//  ScannerController.m
//  clp register
//
//  Created by VT001 on 15/06/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import "ScannerController.h"

#define SCAN_SUCCESS_MESSAGE @"Barcode Scanned Successfully.\nTap Close to continue with registration."

@interface ScannerController ()
{
    ZXCapture *zxingCapture;
    UILabel *barcodeValue;
    
    UIScrollView *appScroller, *mainscroll;
    UIImageView *bgImage,*logoImage;
    UIView *mainView, *formContainer;
    UIButton *closeButton;
    
    NSTimer *timer;
}
@end

@implementation ScannerController
@synthesize _tenantID;
@synthesize _motionImage;
@synthesize _logoImage;

- (void)viewDidLoad {
    [self initBasicControls];
    [self initBackGroundAnimation];
    [self setFrames];
    [self showScannerView];
    _tenantID = @"";
    
    
//    [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
//    [[NSNotificationCenter defaultCenter]
//     addObserver:self selector:@selector(orientationChanged)
//     name:UIDeviceOrientationDidChangeNotification
//     object:[UIDevice currentDevice]];
    
    [super viewDidLoad];
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (void)viewDidAppear:(BOOL)animated
{
    [self showScannerPosition];
}
-(BOOL)shouldAutorotate{
    return NO;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

-(void)initBasicControls{
    appScroller = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [appScroller setBackgroundColor:[UIColor clearColor]];
    [self.view addSubview:appScroller];
    
    mainscroll = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [mainscroll setBackgroundColor:[UIColor clearColor]];
    [appScroller addSubview:mainscroll];
    
    mainView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 1224, 918)];
    [mainView setBackgroundColor:[UIColor whiteColor]];
    [mainscroll addSubview:mainView];
    
    bgImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 1224, 918)];
    [bgImage setBackgroundColor:[UIColor clearColor]];
    [mainView addSubview:bgImage];
    
    formContainer = [[UIView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [formContainer setBackgroundColor:[UIColor blackColor]];
    [appScroller addSubview:formContainer];
    
}

-(void)setFrames{
    //_childViewHiddenFrame = CGRectMake(0, 0, 0, 0);
    _navigationBarHeight = 0;
    _contentViewHeight = self.view.frame.size.height;
    _contentViewWidth = self.view.frame.size.width;
    
    _childViewVisibleFrame = CGRectMake(0,  _navigationBarHeight, _contentViewWidth, _contentViewHeight - _navigationBarHeight);
    _childViewHiddenFrame = CGRectMake(_contentViewWidth, _navigationBarHeight, _contentViewWidth, _contentViewHeight - _navigationBarHeight);
    
}

- (void)showScannerView
{
    _scannerView = [[UIView alloc] initWithFrame:_childViewHiddenFrame];
    
    [self.view setBackgroundColor:[UIColor blackColor]];
    [_scannerView setBackgroundColor:[UIColor blackColor]];
    [_scannerView setHidden:NO];
    
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        _scannerBackgroundWidth = _contentViewWidth * .9;
        _scannerBackgroundHeight = _contentViewHeight * .8;
    }
    else
    {
        _scannerBackgroundWidth = _contentViewWidth * .4;
        _scannerBackgroundHeight = _contentViewHeight * .8;
    }
    
    int scannerBackgroundXOrigin = (_contentViewWidth - _scannerBackgroundWidth) / 2;
    int scannerBackgroundYOrigin = (_contentViewHeight - _scannerBackgroundHeight) / 2 - _navigationBarHeight;
    
    
    CGRect scannerBackgroundRect = CGRectMake(scannerBackgroundXOrigin, scannerBackgroundYOrigin, _scannerBackgroundWidth, _scannerBackgroundHeight);
    
    UIView *backgroundView = [[UIView alloc] initWithFrame:scannerBackgroundRect];
    UIImageView *backgroundImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _scannerBackgroundWidth, _scannerBackgroundHeight)];
    [backgroundImageView setImage:[UIImage imageNamed:@"scan_dialog"]];
    
    
    int labelHeight = scannerBackgroundRect.size.height * .10;
    int readerBorder = _scannerBackgroundWidth * .02;
    int readerWidth = _scannerBackgroundWidth - (readerBorder * 2);
    int readerXOrigin = (_scannerBackgroundWidth - readerWidth) / 2;
    int readerHeight = _scannerBackgroundHeight - labelHeight - readerBorder;
    int readerYOrigin = labelHeight;
    
    
    
    CGFloat degreesOfRotation = 90.0;
    
//    if([UIDevice currentDevice].orientation == UIDeviceOrientationLandscapeLeft){
//        degreesOfRotation = 270.0;
//    }else{
//        degreesOfRotation = 270.0;
//    }
    CGAffineTransform rotate = CGAffineTransformMakeRotation(degreesOfRotation * M_PI/180.0);
    
    scanAreaRect=CGRectMake(readerXOrigin+25*0, readerYOrigin+35*0, readerWidth-50*0, readerHeight-50);
    scanArea=[[UIView alloc]initWithFrame:scanAreaRect];//scanning area view
    [scanArea setBackgroundColor:[UIColor colorWithRed:135.0/255.0 green:169.0/255.0 blue:107.0/255.0 alpha:0.2]];
    //scanArea.transform = rotate;
    
    
    
    //scanner title
    _scannerStatusLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 88+scannerBackgroundRect.size.height * .025, _scannerBackgroundWidth, labelHeight)];
    _scannerStatusLabel.backgroundColor = [UIColor clearColor];
    _scannerStatusLabel.textAlignment = NSTextAlignmentCenter;
    
    _scannerStatusLabel.font=[UIFont fontWithName:@"Helvetica Neue" size:30];
    [_scannerStatusLabel setTextColor:[UIColor colorWithRed:161.0f/255.0f green:208.0f/255.0f blue:49.0f/255.0f alpha:1.0]];
    _scannerStatusLabel.text = NSLocalizedString(@"Please Scan...", nil);
    
    logoImage = [[UIImageView alloc]initWithFrame:CGRectMake((_scannerBackgroundWidth - 88)/2, 0, 88, 88)];
    [logoImage setImage:_logoImage];
    [logoImage setClipsToBounds:YES];
    [logoImage.layer setCornerRadius:4.0f];
    
    CGFloat closeButtonSize = 45.0;
    closeButton = [[UIButton alloc]initWithFrame:CGRectMake((formContainer.frame.size.width - closeButtonSize)/2, (self.view.frame.size.height - closeButtonSize*2), closeButtonSize, closeButtonSize)];
    [closeButton setImage:[UIImage imageNamed:@"close"] forState:UIControlStateNormal];
    [closeButton addTarget:self action:@selector(hideScannerView) forControlEvents:UIControlEventTouchUpInside];
    
    
    //zxcapture
    zxingCapture = [ZXCapture new];
//    if (zxingCapture.hasFront) {
//        zxingCapture.camera = zxingCapture.front;
//    } else
    zxingCapture.camera = zxingCapture.back;
    
    zxingCapture.focusMode = AVCaptureFocusModeContinuousAutoFocus;
    zxingCapture.layer.frame = CGRectMake(readerXOrigin-10, readerYOrigin+50, readerWidth, readerHeight);
    zxingCapture.rotation = 180.0;
    
    zxingCapture.delegate = self;
    [zxingCapture start];//star the scanner
    scanArea.layer.borderWidth=2;
    [self scannerAreaReset];//set scan area border as clear color
    
    scannerStart=TRUE;
    
    scanner_wait = [NSDate date];
    
    CGAffineTransform captureSizeTransform = CGAffineTransformMakeScale(0, 0);
    zxingCapture.transform = rotate;
    zxingCapture.scanRect = CGRectApplyAffineTransform(scanAreaRect,captureSizeTransform);//scan area
    
    CGFloat btnWidth = 100, btnHeight = 50;
    backbutton = [[UIButton alloc]initWithFrame:CGRectMake(10 , (_scannerView.frame.size.height - btnHeight)/2, btnWidth, btnHeight)];
    [backbutton setUserInteractionEnabled:YES];
    [backbutton setClipsToBounds:NO];
    [backbutton setTitle:@"< Back" forState:UIControlStateNormal];
    [backbutton setBackgroundColor:[UIColor colorWithRed:161.0f/255.0f green:208.0f/255.0f blue:49.0f/255.0f alpha:1.0]];
    [backbutton.titleLabel setTextColor:[UIColor whiteColor]];
    [backbutton.titleLabel setFont:[UIFont fontWithName:@"Verdana-Bold" size:18.0]];
    backbutton.clipsToBounds=YES;
    [backbutton.layer setCornerRadius:4.0f];
    [backbutton addTarget:self action:@selector(hideScannerView) forControlEvents:UIControlEventTouchUpInside];
    [backbutton setHidden:YES];
    
    CGFloat barcodeValueWidth = 400;
    CGFloat barcodeValueHeight = 80;
    barcodeValue = [[UILabel alloc]initWithFrame:CGRectMake((self.view.frame.size.width - barcodeValueWidth)/2, self.view.frame.size.height * 0.75, barcodeValueWidth, barcodeValueHeight)];
    barcodeValue.text = @"";
    barcodeValue.textColor = [UIColor whiteColor];
    barcodeValue.backgroundColor = [UIColor clearColor];
    barcodeValue.font=[UIFont fontWithName:@"Verdana" size:15.0];
    barcodeValue.textAlignment = NSTextAlignmentCenter;
    barcodeValue.numberOfLines = 2;
    
    [backgroundView.layer addSublayer:zxingCapture.layer];
    [backgroundView addSubview:backgroundImageView];
    [backgroundView addSubview:logoImage];
    [backgroundView addSubview:_scannerStatusLabel];//scanner title
    [backgroundView addSubview:scanArea];//scan area
    //[backgroundView addSubview:closeButton];
    [backgroundView.layer setCornerRadius:4.0f];
    
    [_scannerView addSubview:backgroundView];
    [_scannerView addSubview:backbutton];
    [_scannerView addSubview:barcodeValue];
    [formContainer addSubview:_scannerView];
    
    [formContainer addSubview:closeButton];
    
    [UIView animateWithDuration:0.33f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut | UIViewAnimationOptionAllowUserInteraction
                     animations:^{ [_scannerView setFrame:_childViewVisibleFrame]; }
                     completion:^(BOOL finished){ }];
}

-(void)hideScannerView{


    [zxingCapture stop];
    [_scannerView removeFromSuperview];
    _scannerView = nil;
    [self.delegate setTenantID:_tenantID];
    [self dismissViewControllerAnimated:NO completion:nil];
    
    
    /*
     if(_scannerView != nil)
     {
     //        [_reader stop];
     [zxingCapture stop];
     [_scannerView removeFromSuperview];
     _scannerView = nil;
     }
     
     if(zxingCapture!=nil){
     [zxingCapture stop];
     zxingCapture = nil;
     }
     */
}

-(void)orientationChanged{
   // NSLog(@"orientationChanged called...");
    
    if([UIDevice currentDevice].orientation == UIDeviceOrientationLandscapeLeft){
         //NSLog(@"UIDeviceOrientationLandscapeLeft called...");
    }
    if([UIDevice currentDevice].orientation == UIDeviceOrientationLandscapeRight){
        //NSLog(@"UIDeviceOrientationLandscapeRight called...");
    }
    if([UIDevice currentDevice].orientation == UIDeviceOrientationLandscapeLeft || [UIDevice currentDevice].orientation == UIDeviceOrientationLandscapeRight){
        //NSLog(@"showScannerPosition called...");
        //[self showScannerPosition];
    }
}

-(void)showScannerPosition{
    if(_scannerView !=nil){
        if(zxingCapture!=nil){
            
            if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
            {
                _scannerBackgroundWidth = _contentViewWidth * .9;
                _scannerBackgroundHeight = _contentViewHeight * .8;
            }
            else
            {
                _scannerBackgroundWidth = _contentViewWidth * .6;
                _scannerBackgroundHeight = _contentViewHeight * .4;
            }
            
            int scannerBackgroundXOrigin = (_contentViewWidth - _scannerBackgroundWidth) / 2;
            int scannerBackgroundYOrigin = (_contentViewHeight - _scannerBackgroundHeight) / 2 - _navigationBarHeight;
            
            CGRect scannerBackgroundRect = CGRectMake(scannerBackgroundXOrigin, scannerBackgroundYOrigin, _scannerBackgroundWidth, _scannerBackgroundHeight);
            
            int labelHeight = scannerBackgroundRect.size.height * .10;
            int readerBorder = _scannerBackgroundWidth * .02;
            int readerWidth = _scannerBackgroundWidth - (readerBorder * 2);
            int readerXOrigin = (_scannerBackgroundWidth - readerWidth) / 2;
            int readerHeight = _scannerBackgroundHeight - labelHeight - readerBorder;
            int readerYOrigin = labelHeight;
            CGAffineTransform captureSizeTransform = CGAffineTransformMakeScale(0, 0);
            zxingCapture.layer.frame = CGRectMake(readerXOrigin-100, scannerBackgroundYOrigin -readerYOrigin, readerWidth, readerHeight);
            
            //            zxingCapture.scanRect = CGRectApplyAffineTransform(CGRectMake(readerXOrigin, readerYOrigin, readerWidth, readerHeight),captureSizeTransform);
            CGRect scanRect=CGRectMake(readerXOrigin, readerYOrigin+90, readerWidth-60, readerHeight-60);
            scanRect=CGRectMake(0, 0, 0, 0);
            
            zxingCapture.scanRect = CGRectApplyAffineTransform(scanRect,captureSizeTransform);
            zxingCapture.delegate = self;
            scannerStart=TRUE;
            scanArea.frame = CGRectMake(readerXOrigin-100+10, scannerBackgroundYOrigin -readerYOrigin+10, readerWidth-20, readerHeight-20);
            
            
            
//            CGFloat degreesOfRotation = 270.0;
//            
//            if([UIDevice currentDevice].orientation == UIDeviceOrientationLandscapeLeft){
//                //degreesOfRotation = 180.0;
//            }else{
//                degreesOfRotation = 270.0;
//            }
//            CGAffineTransform rotate = CGAffineTransformMakeRotation(degreesOfRotation * M_PI/180.0);
//            
//            scanArea.transform = rotate;
//            
//            CGAffineTransform captureSizeTransform1 = CGAffineTransformMakeScale(0, 0);
//            zxingCapture.transform = rotate;
            //zxingCapture.scanRect = CGRectApplyAffineTransform(scanAreaRect,captureSizeTransform1);//scan area
        }
    }
}

#pragma mark -  ZXing barcode

- (NSString *)barcodeFormatToString:(ZXBarcodeFormat)format {
    switch (format) {
        case kBarcodeFormatAztec:
            return @"Aztec";
            
        case kBarcodeFormatCodabar:
            return @"CODABAR";
            
        case kBarcodeFormatCode39:
            return @"Code 39";
            
        case kBarcodeFormatCode93:
            return @"Code 93";
            
        case kBarcodeFormatCode128:
            return @"Code 128";
            
        case kBarcodeFormatDataMatrix:
            return @"Data Matrix";
            
        case kBarcodeFormatEan8:
            return @"EAN-8";
            
        case kBarcodeFormatEan13:
            return @"EAN-13";
            
        case kBarcodeFormatITF:
            return @"ITF";
            
        case kBarcodeFormatPDF417:
            return @"PDF417";
            
        case kBarcodeFormatQRCode:
            return @"QR Code";
            
        case kBarcodeFormatRSS14:
            return @"RSS 14";
            
        case kBarcodeFormatRSSExpanded:
            return @"RSS Expanded";
            
        case kBarcodeFormatUPCA:
            return @"UPCA";
            
        case kBarcodeFormatUPCE:
            return @"UPCE";
            
        case kBarcodeFormatUPCEANExtension:
            return @"UPC/EAN extension";
            
        default:
            return @"Unknown";
    }
}

#pragma mark - ZXCaptureDelegate Methods

- (void)captureResult:(ZXCapture *)capture result:(ZXResult *)result {
    if (!result) return;//if no result rescan
    
    if(scannerStart){
        
        NSString *scannedFormat=[self barcodeFormatToString:result.barcodeFormat];
        if(![scannedFormat isEqualToString:@"Unknown"]){
            
            
            [self showScannerSuccess];//outline scanner area with green border for scan success
            [self performSelector:@selector(scannerAreaReset) withObject:nil afterDelay:1.0];//reset scanner area border
            _barCode = result.text;//barcode text read through camera
           
            _tenantID = _barCode;
        }
    }
}

-(void)captureFailResult:(ZXCapture *)capture result:(ZXResult *)result{
    //    if(!scannerStart || _service!=nil){
    if(!scannerStart){
        scanner_wait = [NSDate date];
        return;
    }
    int scanner_interval = -10;
    if([scanner_wait timeIntervalSinceNow]<scanner_interval){//scan timeout
        
        [self showScannerFailure];
        [self performSelector:@selector(scannerAreaReset) withObject:nil afterDelay:1.0];
        scanner_wait = [NSDate date];
    }
}
//show green boder when scanner is success
-(void)showScannerSuccess{
    scanArea.layer.borderColor=[UIColor greenColor].CGColor;
    barcodeValue.text = NSLocalizedString(SCAN_SUCCESS_MESSAGE, nil) ;
}
//show red boder when scanner is failed
-(void)showScannerFailure{
    scanArea.layer.borderColor=[UIColor redColor].CGColor;
}
//reset scanner border
-(void)scannerAreaReset{
    scanArea.layer.borderColor=[UIColor clearColor].CGColor;
}
-(void)showScannerAnimation{
    
}
-(void)stopScannerAnimation{
    
}
- (void)reScan
{
    //[_reader start];
    [zxingCapture start];
}

//-(void)adjustTitleFrame{
//    CGSize maxsize = CGSizeMake(CGFLOAT_MAX, CGFLOAT_MAX);
//    CGSize size = [_scannerStatusLabel sizeThatFits:maxsize];
//    //[_scannerStatusLabel setBackgroundColor:[UIColor redColor]];
//    
//    CGRect frame = _scannerStatusLabel.frame;
//    //if(frame.size.width < size.width){
//        frame.size.width = size.width+5;
//        //frame.origin.x = (_contentViewWidth - frame.size.width)/2;
//    frame.origin.x = 0;
//        [_scannerStatusLabel setFrame:frame];
//    //}
//}

#pragma mark - ZBarReaderView

- (void) readerView:(ZBarReaderView *)aReaderView didReadSymbols: (ZBarSymbolSet *)symbols fromImage:(UIImage *)image
{
    ZBarSymbol *s = nil;
    NSString* capturedBarcode = @"";
    
    for(s in symbols)
    {
        capturedBarcode = s.data; //Get the first barcode
        break;
    }
    
    NSLog(@"Barcode length = %lu", (unsigned long)capturedBarcode.length);
    
    if(capturedBarcode.length > 12) // barcode UPC is always 12 characters
    {
        [self reScan];
        return;
    }
    
    _barCode = capturedBarcode;
    [aReaderView stop];
    [aReaderView flushCache];
}

#pragma mark - Background Motion Image methods

-(void)initBackGroundAnimation{
    NSString *filePath = _motionImage;
    if(filePath && [[NSFileManager defaultManager]fileExistsAtPath:filePath]){
        
        [bgImage setImage:[self blur:[UIImage imageWithContentsOfFile:filePath]]];
        [formContainer setFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
        formContainer.alpha=0.72;
        [formContainer bringSubviewToFront:bgImage];
//        [self startAnimation];
        [self performSelector:@selector(startAnimation) withObject:nil afterDelay:0.01];
        timer=[NSTimer scheduledTimerWithTimeInterval:4.0 target:self selector:@selector(startAnimation) userInfo:nil repeats:YES];
        
        
    }
}

-(void)startAnimation{
    
    [mainscroll setContentOffset:CGPointMake(0, 0)]; // Scrollview reset
    
    CGPoint moving_size;
    moving_size = CGPointMake(200, 0);
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:3.0]; // scroll to top animation duration
    [UIView setAnimationDelay:0.3];
    [UIView setAnimationCurve:UIViewAnimationCurveEaseIn];
    [mainscroll setContentOffset:moving_size];
    [UIView commitAnimations];
    
    CATransition *transition = [CATransition animation];
    transition.duration = 0.5; // fade animation duration
    transition.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
    transition.type = kCATransitionFade;
    transition.delegate = self;
    
    [mainscroll.layer addAnimation:transition forKey:nil];
    
}
-(void)viewWillDisappear:(BOOL)animated{
    [timer invalidate];
}

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

@end
