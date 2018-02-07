//
//  REST.m
//  clp register
//
//  Created by VT001 on 20/02/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import "REST.h"
#import "AFNetworking.h"
#import "Company.h"
//#define BASE_URL @"http://dev.clpcloud.com" //Demo
//#define BASE_URL @"http://10.0.0.13:8080/Clyptechs" //localtest
//http://10.0.0.13:8080/Clyptechs/clpapi/mobile/ipadcustomer
#define BASE_URL @"http://demo.clpdemo.com" //Demo
//#define BASE_URL @"https://mipueblo.clyptechs.com"
//#define BASE_URL @"https://milagros.clyptechs.com"
//#define BASE_URL @"http://10.0.0.5:8080"
#define CLP_API_KEY @"4a27499469885f476b14c5cbe97f7995"
//#define CLP_API_KEY @"59b0642b74a76cc58017f1c711152bdf" //Demo1 - Milagrow
//#define CLP_API_KEY @"4a27499469885f476b14c5cbe97f7995" //Demo2 - MiPeublo

@implementation REST

#pragma mark - Company REST
-(void)getCompanyDetails:(void(^)(Company *response))onCompletion{
    @try {
        NSString *string = @"{\"message\":\"[\\\"{\\\\\\\"langcode\\\\\\\":\\\\\\\"en\\\\\\\",\\\\\\\"displaymessage\\\\\\\":\\\\\\\"Get 5% off on selected products and coupon\\\\\\\"}\\\",\\\"{\\\\\\\"langcode\\\\\\\":\\\\\\\"sp\\\\\\\",\\\\\\\"displaymessage\\\\\\\":\\\\\\\"Get 5% off on selected productica and couponnss\\\\\\\"}\\\"]\"}";
        

        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        manager.requestSerializer = [AFJSONRequestSerializer serializer];
        manager.responseSerializer = [AFJSONResponseSerializer serializer];
        
        NSString *url = [NSString stringWithFormat:@"%@/clpapi/auth/getCustomData",BASE_URL];
        NSMutableDictionary *inputs = [[NSMutableDictionary alloc]init];
        [inputs setValue:BASE_URL forKey:@"siteURL"];        
        [inputs setValue:[NSNumber numberWithBool:YES] forKey:@"isIpad"];
        [manager POST:url parameters:inputs success:^(AFHTTPRequestOperation *operation, id responseObject) {
            dispatch_async(dispatch_get_main_queue(), ^{
                NSMutableDictionary *sample = (NSMutableDictionary*)responseObject;
                
                NSString *string = [sample valueForKey:@"initialMessage"];
                string = [string stringByReplacingOccurrencesOfString:@"\\" withString:@""];
                string = [string stringByReplacingOccurrencesOfString:@"[\"" withString:@"["];
                string = [string stringByReplacingOccurrencesOfString:@"]\"" withString:@"]"];
                string = [string stringByReplacingOccurrencesOfString:@"\"{" withString:@"{"];
                string = [string stringByReplacingOccurrencesOfString:@"}\"" withString:@"}"];
                string = [string stringByReplacingOccurrencesOfString:@"\"[" withString:@"["];
                
                Company *com = [[Company alloc]init];
                com.companyLogo = [sample valueForKey:@"companyLogo"];
                com.companyBackGround = [sample valueForKey:@"companyBackGround"];
                com.companyShortName = [sample valueForKey:@"companyShortName"];
                com.initialMessage = string;
                
                //Company *com = [[Company alloc]initWithDictionary:responseObject error:nil];
                
                /// temp fix
                if(com && com.companyLogo && com.companyLogo.length>0){
                    if(![com.companyLogo hasPrefix:@"http"]){
                        com.companyLogo = [NSString stringWithFormat:@"http:%@",com.companyLogo];
                    }
                }
                if(com && com.companyBackGround && com.companyBackGround.length>0){
                    if(![com.companyBackGround hasPrefix:@"http"]){
                        com.companyBackGround = [NSString stringWithFormat:@"http:%@",com.companyBackGround];
                    }
                }
                ///
                
                onCompletion(com);
            });
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"%@",error);
            dispatch_async(dispatch_get_main_queue(), ^{
                Company *com = [[Company alloc]init];
                onCompletion(com);
            });
        }];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception);
        dispatch_async(dispatch_get_main_queue(), ^{
            Company *com = [[Company alloc]init];
            onCompletion(com);
        });
    }
}

-(NSString*)parseJSONManual:(NSString*)string{
    NSError *error;
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:[string dataUsingEncoding:NSUTF8StringEncoding]
                                                         options:NSJSONReadingAllowFragments error:&error];
    
    string = [json valueForKey:@"initialMessage"];
    
    json = [NSJSONSerialization JSONObjectWithData:[string dataUsingEncoding:NSUTF8StringEncoding]
                                           options:NSJSONReadingAllowFragments error:&error];
    
    string = [json valueForKey:@"message"];
    NSArray *message = [NSJSONSerialization JSONObjectWithData:[string dataUsingEncoding:NSUTF8StringEncoding]
                                                       options:NSJSONReadingAllowFragments error:&error];
    
    for (NSString *jsonstring in message) {
        json = [NSJSONSerialization JSONObjectWithData:[jsonstring dataUsingEncoding:NSUTF8StringEncoding]
                                               options:NSJSONReadingAllowFragments error:&error];
        NSLog(@"%@",[json valueForKey:@"displaymessage"]);
        NSLog(@"%@",[json valueForKey:@"langcode"]);
        
    }
    return string;
}

# pragma mark - Customer REST
-(void)registerCustomerInfo:(Customer *)info completion:(void(^)(NSMutableDictionary *message))onCompletion{
    @try {
        NSMutableDictionary *inputs = [[NSMutableDictionary alloc]initWithDictionary:[info toDictionary]];
        AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
        manager.requestSerializer = [AFJSONRequestSerializer serializer];
        [manager.requestSerializer setValue:CLP_API_KEY forHTTPHeaderField:@"CLP-API-KEY"];
        
        NSString *url = [NSString stringWithFormat:@"%@/clpapi/mobile/ipadcustomer",BASE_URL];
        [manager POST:url parameters:inputs success:^(AFHTTPRequestOperation *operation, id responseObject) {
            dispatch_async(dispatch_get_main_queue(), ^{
                if ([responseObject isKindOfClass:[NSDictionary class]]) {
                    NSMutableDictionary *response=(NSMutableDictionary*)responseObject;
//                    BOOL successFlag=[[response valueForKey:@"successFlag"] boolValue];                    
//                    if(successFlag){
//                        onCompletion(NSLocalizedString([response valueForKey:@"message"],nil));
//                    }else{
//                        onCompletion(NSLocalizedString([response valueForKey:@"message"],nil));
//                    }
                    onCompletion(response);
                }
            });
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            NSLog(@"%@",error);
            dispatch_async(dispatch_get_main_queue(), ^{
                NSString *errorMsg = NSLocalizedString(@"There is a problem in registration server. Please try again later.",nil);
                NSMutableDictionary *dict = [NSMutableDictionary new];
                [dict setValue:errorMsg forKey:@"message"];
                onCompletion(dict);
            });
        }];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception);
        dispatch_async(dispatch_get_main_queue(), ^{
            onCompletion(NSLocalizedString(@"Registration failed",nil));
        });
    }
}
@end