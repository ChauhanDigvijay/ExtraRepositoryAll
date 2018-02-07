//
//  OfferDetailView.m
//  clpsdk
//
//  Created by Gourav Shukla on 30/07/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "OfferDetailView.h"
#import "ModelClass.h"
#import "Code39.h"
#import "ApiClasses.h"
#import "HomeViewController.h"

@interface OfferDetailView ()
{
    ModelClass  * obj;
    ApiClasses * apiCall;

}

@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *backgroundImage;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *headerImage;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *logoImage;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *qrImage;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *qrLblValue;
@property (weak, nonatomic) IBOutlet UILabel *titleLbl;
@property (weak, nonatomic) IBOutlet UILabel *descriptionLbl;
@property (weak, nonatomic) IBOutlet UILabel *expireLbl;
@property (weak, nonatomic) IBOutlet UIImageView *offerImage;
@property (weak, nonatomic) IBOutlet UIImageView *companyLogoImage;

void freeRawData(void *info, const void *data, size_t size);


@end

@implementation OfferDetailView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    obj=[ModelClass sharedManager];
    
    // mobilesetting
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self.backgroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // header image
//NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
//    NSURL *url3 = [NSURL URLWithString:str3];
//    [self.headerImage sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    // promo logo image
    if(self.loginImageURl.length!=0)
    {
    NSString * str4 = [NSString stringWithFormat:@"http://%@",self.loginImageURl];
    NSURL *url4 = [NSURL URLWithString:str4];
    [self.logoImage sd_setImageWithURL:url4 placeholderImage:[UIImage imageNamed:@"nil"]];
    }
    
// Barcode generator code
    
//    self.qrImage.image = [Code39 code39ImageFromString:self.promoCodevalue Width:160 Height:150];
//    self.qrImage.autoresizesSubviews = NO;
//[self.qrImage setAutoresizingMask:UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth];
    
    
    NSString * str = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    NSURL *url1 = [NSURL URLWithString:
                   str];
    [self.companyLogoImage sd_setImageWithURL:url1 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    // Qrcoe generator
    
    if(self.promoCodevalue.length!=0)
    {
   self.qrImage.image = [self quickResponseImageForString:self.promoCodevalue withDimension:182];
        
    // promocode value show
    self.qrLblValue.text = self.promoCodevalue;
    }
    
    // offerTitle
    if(self.offersTitle.length!=0)
    {
    self.titleLbl.text = self.offersTitle;
    }
    
    // offer description
    if(self.offersDescription.length!=0)
    {
    self.descriptionLbl.text = self.offersDescription;
    }
    
    // offer expireDate
    if(self.offerExpireDate.length!=0)
    {
    self.expireLbl.text = self.offerExpireDate;
    }
    
    // promo logo image
    
    if(self.offerImageIcon.length!=0)
    {
    NSString * str5 = [NSString stringWithFormat:@"http://%@",self.offerImageIcon];
    NSURL *url5 = [NSURL URLWithString:str5];
    [self.offerImage sd_setImageWithURL:url5 placeholderImage:[UIImage imageNamed:@"greatOffer.png"]];
        self.offerImageIcon =nil;
    }
    else
    {
        self.offerImage.image = [UIImage imageNamed:@"greatOffer.png"];
    }
    
  
   //NSUserDefaults.standardUserDefaults().setBool(true, forKey: "push")
    
 
    if([[NSUserDefaults standardUserDefaults]boolForKey:@"push"] == YES)
    {
        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"push"];
        [self pushReceived];
    }
  
}

//push notification service

-(void)pushReceived
{
   
    NSString * promoCode = [[NSUserDefaults standardUserDefaults]valueForKey:@"promocode"];
    
    if(promoCode.length!=0)
    {
        dispatch_async( dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            dispatch_async( dispatch_get_main_queue(), ^{
                
                self.qrImage.image = [self quickResponseImageForString:promoCode withDimension:182];
                self.qrLblValue.text = promoCode;
            });
        });
    }
    else
     {
        [self alertViewDelegate:@"No promocode available"];
     }
    
    NSString *offerID = [[NSUserDefaults standardUserDefaults]valueForKey:@"offerID"];

    if(offerID.length!=0)
    {
        [self getOffersFormOfferID:offerID];
    }
}




//-(void)pushReceived:(NSNotification *)userInfo
//{
//    NSLog(@"userInfo-------%@",[userInfo userInfo]);
//    
//    NSString *promoCode = [userInfo.userInfo objectForKey:@"pc"];
//    
//    if(promoCode.length!=0)
//    {
//        
//        dispatch_async( dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
//            dispatch_async( dispatch_get_main_queue(), ^{
//                
//                self.qrImage.image = [self quickResponseImageForString:promoCode withDimension:182];
//            });
//        });
//        
//        
//    }
//    else
//    {
//    }
//    
//    NSString *offerID = [userInfo.userInfo objectForKey:@"offerid"];
//
//    if(offerID.length!=0)
//    {
//        dispatch_async( dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
//            dispatch_async( dispatch_get_main_queue(), ^{
//                
//                [self getOffersFormOfferID:offerID];
//            });
//        });
//  
//    }

    
//    NSString *campaignDescription = [[userInfo.userInfo objectForKey:@"aps"]objectForKey:@"campaignDescription"];
//    
//    if(campaignDescription.length!=0)
//    {
//        self.descriptionLbl.text = campaignDescription;
//    }
//    
//
//    NSString *validityEndDateTime = [userInfo.userInfo objectForKey:@"validityEndDateTime"];
//    
//    if(validityEndDateTime.length!=0)
//    {
//        NSString *endStr = validityEndDateTime;
//        NSArray *myArray = [endStr componentsSeparatedByString:@" "];
//        NSString *end = [myArray objectAtIndex:0];
//        
//        NSDate *currentDate=[NSDate date];
//        NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
//        [currentF setDateFormat:@"yyyy-MM-dd"];
//        NSString *strCurrentDate1=[currentF stringFromDate:currentDate];   NSDateFormatter *f = [[NSDateFormatter alloc] init];
//        [f setDateFormat:@"yyyy-MM-dd"];
//        NSDate *startDate = [f dateFromString:strCurrentDate1];
//        NSDate *endDate = [f dateFromString:end];
//        
//        NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
//        NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
//                                                            fromDate:startDate
//                                                              toDate:endDate
//                                                             options:NSCalendarWrapComponents];
//        
//        NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
//        self.expireLbl.text = totalDays;
//    }
//    
//
//    NSString *campaignTitle = [[userInfo.userInfo objectForKey:@"aps"]objectForKey:@"alert"];
//    
//    if(campaignTitle.length!=0)
//    {
//        self.titleLbl.text = campaignTitle;
//    }
//
//    NSString *passCustomStripUrl = [userInfo.userInfo objectForKey:@"passCustomStripUrl"];
//    
//    if(passCustomStripUrl.length!=0)
//    {
//        NSString * str5 = [NSString stringWithFormat:@"http://%@",passCustomStripUrl];
//        NSURL *url5 = [NSURL URLWithString:str5];
//        [self.offerImage sd_setImageWithURL:url5 placeholderImage:[UIImage imageNamed:@"greatOffer.png"]];
//    }
//    
//    
//    NSString *customLogoURL = [userInfo.userInfo objectForKey:@"customLogoURL"];
//
//    if(customLogoURL.length!=0)
//    {
//        NSString * str4 = [NSString stringWithFormat:@"http://%@",customLogoURL];
//        NSURL *url4 = [NSURL URLWithString:str4];
//        [self.logoImage sd_setImageWithURL:url4 placeholderImage:[UIImage imageNamed:@"nil"]];
//    }
    
 
//}



-(void)getOffersFormOfferID:(NSString *)offerID
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];

        NSString * str = [NSString stringWithFormat:@"/mobile/getOfferByOfferId/%@",offerID];
        [apiCall getuserOffer:nil url:str withTarget:self withSelector:@selector(getOffers:)];
        apiCall = nil;
}



-(void)getOffers:(id)response
{
      [obj removeLoadingView:self.view];
    NSLog(@"response offer from offer id---------%@",response);
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        
        NSArray * arr = (NSArray *)[response valueForKey:@"inAppOfferList"];
        
        self.titleLbl.text = [[arr objectAtIndex:0]valueForKey:@"campaignTitle"];
        
        self.descriptionLbl.text = [[arr objectAtIndex:0]valueForKey:@"campaignDescription"];
        
            NSString *validityEndDateTime = [[arr objectAtIndex:0]valueForKey:@"validityEndDateTime"];
        
            if(validityEndDateTime.length!=0)
            {
                NSString *endStr = validityEndDateTime;
                NSArray *myArray = [endStr componentsSeparatedByString:@" "];
                NSString *end = [myArray objectAtIndex:0];
        
                NSDate *currentDate=[NSDate date];
                NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
                [currentF setDateFormat:@"yyyy-MM-dd"];
                NSString *strCurrentDate1=[currentF stringFromDate:currentDate];   NSDateFormatter *f = [[NSDateFormatter alloc] init];
                [f setDateFormat:@"yyyy-MM-dd"];
                NSDate *startDate = [f dateFromString:strCurrentDate1];
                NSDate *endDate = [f dateFromString:end];
        
                NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                                    fromDate:startDate
                                                                      toDate:endDate
                                                                     options:NSCalendarWrapComponents];
        
                NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
                self.expireLbl.text = totalDays;
            }
    }
    
}

//# pragma mark - Offers Api
//
//-(void)getCustomerOffers
//{
//    [obj addLoadingView:self.view];
//    apiCall=[ApiClasses sharedManager];
//    
//    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
//    NSData *data = [def1 objectForKey:@"MyData"];
//    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
//    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
//    NSString * strCustomerID = [dic valueForKey:@"customerID"];
//    NSLog(@"customer id---------%@",strCustomerID);
//    //@"16900720"
//    NSString * str = [NSString stringWithFormat:@"/mobile/getoffers/%@/0",strCustomerID];
//    
//    [apiCall getOffers:nil url:str withTarget:self withSelector:@selector(getOffers:)];
//    apiCall = nil;
//}
//
//// getoffers api response
//
//-(void)getOffers:(id)response
//{
//    [obj removeLoadingView:self.view];
//    NSLog(@"get offers response---------%@",response);
//    
//    if([[response valueForKey:@"successFlag"]intValue] == 1)
//    {
//       // NSArray * arr = (NSArray *)[response valueForKey:@"inAppOfferList"];
//
//    }
//    
//}




// get promocode api
-(void)getPromocode :(NSString *)campignID
{
    [obj addLoadingView:self.view];
    
    if([obj checkNetworkConnection])
    {
        apiCall=[ApiClasses sharedManager];
        
        // customer id
        NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
        NSData *data = [def1 objectForKey:@"MyData"];
        NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
        NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
        NSString * strCustomerID = [dic valueForKey:@"customerID"];
        
        NSLog(@"customer id---------%@",strCustomerID);
       // @"16900720"
    NSString * str = [NSString stringWithFormat:@"/mobile/getPromo/%@/%@/NO",strCustomerID,campignID];
        
        // get offer api
        [apiCall getPromocode:nil url:str withTarget:self withSelector:@selector(getPromocodeResponseApi:)];
        
        apiCall = nil;
    }
    else
    {
        [self alertViewDelegate:@"Please check your network connection"];
    }

}



// promocode api response
-(void)getPromocodeResponseApi:(id)response
{
    [obj removeLoadingView:self.view];
    
    if([[response valueForKey:@"successFlag"]intValue]==1 && ![[response valueForKey:@"promoCode"]isEqualToString:@"DEFAULT"])
    {
         self.qrImage.image = [self quickResponseImageForString:[response valueForKey:@"promoCode"] withDimension:182];
    }
    else
    {
        [self alertViewDelegate:@"No promocode available"];
    }
    
    NSLog(@"promocode response ----- %@", response);
}


// alert view method
-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
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


// memory warning
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}



// cross button action
- (IBAction)crossButton_Action:(id)sender
{
    
//    NSInteger numberOfViewControllers = self.navigationController.viewControllers.count;
//    
//    for (int i=0; i<numberOfViewControllers; i++)
//    {
//        UIViewController *controller = (UIViewController*)[self.navigationController.viewControllers objectAtIndex:i];
//        
//        if ([controller isKindOfClass:[HomeViewController class]])
//        {
//            [self.navigationController popToViewController:controller animated:YES];
//        }
//    }
    
    [self.navigationController popViewControllerAnimated:YES];
    
    //[self.navigationController popToRootViewControllerAnimated:YES];

}


// remove notification observer
-(void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];

}

@end