//
//  ModelClass.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "ModelClass.h"
#import "MBProgressHUD.h"
#import "qrencode.h"
#import "BottomView.h"
#import "DirectionMapView.h"


@implementation ModelClass
{
    SideMenu *sideMenu;
    BOOL  menuOpen;
    UIView * sideView;
    
   
}
@synthesize bottomObj;

+ (id)sharedManager
{
    static ModelClass *sharedMyManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedMyManager = [[self alloc] init];
        sharedMyManager.itemDictArray=[[NSMutableArray alloc]init];
    });
    return sharedMyManager;
}



#pragma mark - TextField Padding

-(void)addLeftPaddingToTextField:(UITextField*)textField
{
    UIView *paddingView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 16, 15)];
    textField.leftView = paddingView;
    textField.leftViewMode = UITextFieldViewModeAlways;
}



#pragma mark - NSuserDefault Methods

-(void)saveUserDefaultData:(NSString *)value and:(NSString *)key;
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:value forKey:key];
    [defaults synchronize];
    
}

-(NSString *)reterieveuserDefaultData:(NSString *)keyName;
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *salesName = [defaults objectForKey:keyName];
    return salesName;
}


#pragma mark - Color Methods

-(UIColor *)colorWithHexString:(NSString *)stringToConvert
{
    NSString *noHashString = [stringToConvert stringByReplacingOccurrencesOfString:@"#" withString:@""]; // remove the #
    NSScanner *scanner = [NSScanner scannerWithString:noHashString];
    [scanner setCharactersToBeSkipped:[NSCharacterSet symbolCharacterSet]]; // remove + and $
    
    unsigned hex;
    if (![scanner scanHexInt:&hex]) return nil;
    int r = (hex >> 16) & 0xFF;
    int g = (hex >> 8) & 0xFF;
    int b = (hex) & 0xFF;
    
    return [UIColor colorWithRed:r / 255.0f green:g / 255.0f blue:b / 255.0f alpha:1.0f];
}

#pragma mark - Loaders

- (void)addLoadingView:(UIView *)view
{
    [MBProgressHUD showHUDAddedTo:view animated:TRUE];
}

- (void)removeLoadingView:(UIView *)view
{
    [MBProgressHUD hideHUDForView:view animated:TRUE];
}


// network chek
-(BOOL)checkNetworkConnection
{
    
    
    NSLog(@"check reachability call method");
    
    Reachability *networkReachability = [Reachability reachabilityForInternetConnection];
    NetworkStatus networkStatus = [networkReachability currentReachabilityStatus];
    if (networkStatus == NotReachable)
    {
        NSLog(@"There IS NO internet connection");
        
         return NO;
    }
    else
    {
        NSLog(@"There IS internet connection");
        return YES;
    }
}


// custom background color

-(void)backgroundColor:(UIView *)view color : (UIColor *)color
{
    UIView *newView = [[UIView alloc] initWithFrame:view.bounds];
    newView.backgroundColor = color;
}



// QR code generator Code
void freeRawData(void *info, const void *data, size_t size) {
    free((unsigned char *)data);
}

- (UIImage *)quickResponseImageForString:(NSString *)dataString withDimension:(int)imageWidth {
    
    QRcode *resultCode = QRcode_encodeString([dataString UTF8String], 0, QR_ECLEVEL_L, QR_MODE_8, 1);
    
    unsigned char *pixels = (*resultCode).data;
    int width = (*resultCode).width;
    int len = width * width;
    
    if (imageWidth < width)
        imageWidth = width;
    
    // Set bit-fiddling variables
    int bytesPerPixel = 4;
    int bitsPerPixel = 8 * bytesPerPixel;
    int bytesPerLine = bytesPerPixel * imageWidth;
    int rawDataSize = bytesPerLine * imageWidth;
    
    int pixelPerDot = imageWidth / width;
    int offset = (int)((imageWidth - pixelPerDot * width) / 2);
    
    // Allocate raw image buffer
    unsigned char *rawData = (unsigned char*)malloc(rawDataSize);
    memset(rawData, 0xFF, rawDataSize);
    
    // Fill raw image buffer with image data from QR code matrix
    int i;
    for (i = 0; i < len; i++) {
        char intensity = (pixels[i] & 1) ? 0 : 0xFF;
        
        int y = i / width;
        int x = i - (y * width);
        
        int startX = pixelPerDot * x * bytesPerPixel + (bytesPerPixel * offset);
        int startY = pixelPerDot * y + offset;
        int endX = startX + pixelPerDot * bytesPerPixel;
        int endY = startY + pixelPerDot;
        
        int my;
        for (my = startY; my < endY; my++) {
            int mx;
            for (mx = startX; mx < endX; mx += bytesPerPixel) {
                rawData[bytesPerLine * my + mx    ] = intensity;    //red
                rawData[bytesPerLine * my + mx + 1] = intensity;    //green
                rawData[bytesPerLine * my + mx + 2] = intensity;    //blue
                rawData[bytesPerLine * my + mx + 3] = 255;          //alpha
            }
        }
    }
    
    CGDataProviderRef provider = CGDataProviderCreateWithData(NULL, rawData, rawDataSize, (CGDataProviderReleaseDataCallback)&freeRawData);
    CGColorSpaceRef colorSpaceRef = CGColorSpaceCreateDeviceRGB();
    CGBitmapInfo bitmapInfo = kCGBitmapByteOrderDefault;
    CGColorRenderingIntent renderingIntent = kCGRenderingIntentDefault;
    CGImageRef imageRef = CGImageCreate(imageWidth, imageWidth, 8, bitsPerPixel, bytesPerLine, colorSpaceRef, bitmapInfo, provider, NULL, NO, renderingIntent);
    
    UIImage *quickResponseImage = [UIImage imageWithCGImage:imageRef];
    
    CGImageRelease(imageRef);
    CGColorSpaceRelease(colorSpaceRef);
    CGDataProviderRelease(provider);
    QRcode_free(resultCode);
    
    return quickResponseImage;
}


// BottomView Add
-(void)AddBottomView
{
     bottomObj = [[[NSBundle mainBundle]loadNibNamed:@"BottomView" owner:self options:nil]lastObject];
     bottomObj.layer.masksToBounds = NO;
     bottomObj.layer.shadowOffset = CGSizeMake(0,0);
     bottomObj.layer.shadowRadius = 2;
     bottomObj.layer.shadowOpacity = 0.5;
    
     bottomObj.frame = CGRectMake(0, [UIScreen mainScreen].bounds.size.height-50, [UIScreen mainScreen].bounds.size.width, 50);
    
    [[[[UIApplication sharedApplication]delegate]window]addSubview:bottomObj];
}

-(void)RemoveBottomView
{
    bottomObj.frame = CGRectMake(0, 800, [UIScreen mainScreen].bounds.size.width, 50);
}


// BottomView FrameSet
-(void)setBottomframe
{
    bottomObj.frame = CGRectMake(0, [UIScreen mainScreen].bounds.size.height-50, [UIScreen mainScreen].bounds.size.width, 50);
}






@end
