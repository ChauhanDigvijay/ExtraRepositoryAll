//
//  ModelClass.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "ModelClass.h"
#import "Reachability.h"



@implementation ModelClass


+ (id)sharedManager
{
    static ModelClass *sharedMyManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedMyManager = [[self alloc] init];
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

-(NSString *)reterieveSpendGoToken
{
    return [self reterieveuserDefaultData:@"spendgo_access_token"] == nil ? @"" : [self reterieveuserDefaultData:@"spendgo_access_token"];
}

-(NSString *)reterieveSpendGoCustomerId
{
    return [self reterieveuserDefaultData:@"spendgo_customer_id"]  == nil ? @"" : [self reterieveuserDefaultData:@"spendgo_customer_id"];
}

-(NSString *)reterieveFishbowlToken
{
    return [self reterieveuserDefaultData:@"fishbowl_access_token"]  == nil ? @"" : [self reterieveuserDefaultData:@"fishbowl_access_token"];
}

-(NSString *)retrieveDeviceId{
     return [self reterieveuserDefaultData:@"device_id"]  == nil ? @"" : [self reterieveuserDefaultData:@"device_id"];
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


#pragma mark - Reachability Methods

-(BOOL)checkNetworkConnection
{
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

@end
