//
//  Raleys.m
//  Raley's
//
//  Created by VT01 on 02/06/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "Raleys.h"
#import "WebService.h"
#import "AppDelegate.h"


#import <ifaddrs.h>
#import <arpa/inet.h>


@implementation Raleys

@synthesize pushDeviceToken;
@synthesize email;
@synthesize password;
@synthesize userName;
@synthesize fullName;
@synthesize latestLocation;


+(Raleys*)shared{
    static Raleys *obj = nil;
    if(obj==nil){
        AppDelegate *app = (AppDelegate*)[UIApplication sharedApplication].delegate;
        obj = [app getRaleys];
        if(obj==nil){
            obj = [[Raleys alloc]init];
            [app storeRaleys];
        }
    }
    return obj;
}


#pragma mark NSCoding Protocol
- (void)encodeWithCoder:(NSCoder *)encoder;
{
    [encoder encodeObject:[self pushDeviceToken] forKey:@"pushDeviceToken"];
    
    [encoder encodeObject:[self email] forKey:@"email"];
    [encoder encodeObject:[self password] forKey:@"password"];
    
    [encoder encodeObject:[self userName] forKey:@"userName"];
    [encoder encodeObject:[self fullName] forKey:@"fullName"];
    
}

- (id)initWithCoder:(NSCoder *)decoder;
{
    self = [super init];
    
    if(self == nil)
        return nil;
    
    self.pushDeviceToken = [decoder decodeObjectForKey:@"pushDeviceToken"];
    
    self.email = [decoder decodeObjectForKey:@"email"];
    self.password = [decoder decodeObjectForKey:@"password"];
    
    self.userName = [decoder decodeObjectForKey:@"userName"];
    self.fullName = [decoder decodeObjectForKey:@"fullName"];
    
    return self;
}


-(id)init{
    self = [super init];
    if(self){
        //sessionKey = @"testcseapi@gmail.com";
        pushDeviceToken = @"";
        userName = @"";
        latestLocation.latitude = 38.577160;
        latestLocation.longitude = -121.495560;
    }
    return self;
}
//
//-(NSString*) authString
//{
//    @try {
//        NSMutableString* output = [NSMutableString stringWithString:@""];
//        
//        if ([secureUDID length]) {
//            [output appendFormat:@"&secure_udid=%@", secureUDID];
//        }
//            [output appendFormat:@"&lat=%.6f&long=%.6f", latestLocation.latitude, latestLocation.longitude];
//        
//        if ([pushDeviceToken length]) {
//            [output appendFormat:@"&push_token=%@", pushDeviceToken];
//        }
//        if ([sessionKey length])
//        {
//            [output appendFormat:@"&sessionKey=%@", sessionKey];
//        }
//        
//        if(![Utility isEmpty:ClpUserId]){
//            [output appendFormat:@"&userId=%@", ClpUserId];
//        }
//        
//        if(![Utility isEmpty:userName]){
//            [output appendFormat:@"&userName=%@", userName];
//        }
//        if(![Utility isEmpty:isPush]){
//            [output appendFormat:@"&isPush=%@", isPush];
//        }
//        
//        //	else if (instance->serverKnowsFacebookToken && [LoginModel isLoggedIntoFacebook])
//        //	{
//        //		[output appendFormat: @"&FB_TOKEN=%@", [FBSession activeSession].accessTokenData.accessToken];
//        //	}
//        return output;
//        
//    }
//    @catch (NSException *exception) {
//        return @"";
//    }
//}
//
//-(void)authString :(NSMutableDictionary*)data;
//{
//    @try {
//        if ([secureUDID length]) {
//            [data setValue:secureUDID forKey:@"secure_udid"];
//        }
//        
//        
//            [data setValue:[NSString stringWithFormat:@"%.6f",latestLocation.latitude] forKey:@"lat"];
//            [data setValue:[NSString stringWithFormat:@"%.6f",latestLocation.longitude] forKey:@"long"];
//      
//        if ([pushDeviceToken length]) {
//            [data setValue:pushDeviceToken forKey:@"push_token"];
//        }
//        if ([sessionKey length])
//        {
//            [data setValue:sessionKey forKey:@"sessionKey"];
//        }
//        
//        if(![Utility isEmpty:ClpUserId]){
//            [data setValue:ClpUserId forKey:@"userId"];
//        }
//        
//        if(![Utility isEmpty:userName]){
//            [data setValue:userName forKey:@"userName"];
//        }
//        
//        [data setValue:isPush forKey:@"isPush"];
//
//        //	else if (instance->serverKnowsFacebookToken && [LoginModel isLoggedIntoFacebook])
//        //	{
//        //		[output appendFormat: @"&FB_TOKEN=%@", [FBSession activeSession].accessTokenData.accessToken];
//        //	}
//        
//    }
//    @catch (NSException *exception) {
//    }
//}


#pragma mark - Registration START


-(void)getAccountDetail{
    Login *login = [((AppDelegate*)[UIApplication sharedApplication].delegate) getLogin];
    if(login==nil)return;
    
    AccountRequest *request = [[AccountRequest alloc] init];
    request.accountId = login.accountId;
    
    account_service = [[WebService alloc]initWithListener:self responseClassName:@"AccountRequest"];
    [account_service execute:ACCOUNT_GET_URL authorization:login.authKey requestObject:request requestType:POST]; // response handled by handleAccountServiceResponse method below
}

-(void)onServiceResponse:(id)responseObject{
    if([responseObject isKindOfClass:[AccountRequest class]]){
        [self handleAccountServiceResponse:responseObject];
    }
}

- (void)handleAccountServiceResponse:(id)responseObject
{
    @try {
        int status = [account_service getHttpStatusCode];
        
        if(status == 200) // service success
        {
            AppDelegate *app = ((AppDelegate*)[UIApplication sharedApplication].delegate);
            app._currentAccountRequest = (AccountRequest *)responseObject;
            
            if(app._currentAccountRequest == nil)return;
            
            [app getPersistentData].account = app._currentAccountRequest;
            [app storePersistentData];
        }
    }@catch(NSException *exception){
        
    }
    [self userLoginRegister];
}

-(void)userRegister {
    AppDelegate *app = ((AppDelegate*)[UIApplication sharedApplication].delegate);
    if([[app getPersistentData]account]==nil){
        [self getAccountDetail];
    }else{
        [self userLoginRegister];
    }
}

-(void)userLoginRegister //andDelegate:(id<LoginModelDelegate>)delegateObject
{
    @try {
        
        AppDelegate *app = ((AppDelegate*)[UIApplication sharedApplication].delegate);
        AccountRequest *account = [[app getPersistentData]account];
     
        Login *login = [((AppDelegate*)[UIApplication sharedApplication].delegate) getLogin];
        email = login.email;
        password = login.password;//[Raleys base64EncodeString:login.password];
        fullName = [NSString stringWithFormat:@"%@ %@",login.firstName, login.lastName];

        //Customer Registration - CLP
        CLPCustomer *cus=[[CLPCustomer alloc]init];
        cus.companyID=1;
        cus.firstName=account.firstName;
        cus.lastName=account.lastName;
        cus.emailID=account.email;
        cus.loginID=account.email;
        cus.loginPassword=@"password";
        cus.loyalityNo=account.loyaltyNumber;
        cus.loyalityLevel=account.loyaltyNumber;
        cus.homePhone=account.phone;
        cus.cellPhone=account.mobilePhone;
        cus.additionalPhone=account.mobilePhone;
        cus.addressLine1=account.address;
        cus.addressLine2=account.address;
        cus.addressCity= account.city;
        cus.addressState= account.state;
        cus.addressZip= account.zip;
        cus.addressCountry=account.state;
        cus.customerTenantID=account.crmNumber;
        cus.customerStatus=@"Active";
        cus.lastActivtiy=@"UPDATE";
        cus.statusCode=1;
        cus.registeredIP=[Raleys getIPAddress];
        cus.customerGender=@"";
        cus.dateOfBirth=account.dateOfBirth.stringValue;
        cus.customerAge=@""; //@"45";
        cus.homeStore=[NSString stringWithFormat:@"%d",account.storeNumber];
        cus.favoriteDepartment= account.favoriteDept;
        cus.pushOpted=@"N";
        if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0){
            if(([[UIApplication sharedApplication] isRegisteredForRemoteNotifications]) &&
               ([[[UIApplication sharedApplication] currentUserNotificationSettings] types]!=UIUserNotificationTypeNone))
                cus.pushOpted=@"Y";
        }else {
            if([[UIApplication sharedApplication] enabledRemoteNotificationTypes]!=UIRemoteNotificationTypeNone)
                cus.pushOpted=@"Y";
        }
        if(account.sendEmailsFlag.boolValue)
           cus.emailOpted=@"Y";
        else
           cus.emailOpted=@"N";
        if(account.sendTextsFlag.boolValue)
           cus.smsOpted=@"Y";
        else
           cus.smsOpted=@"N";
        cus.adOpted=@"N";
        cus.phoneOpted=@"N";
        //cus.loyalityRewards=@"10";
        cus.modifiedBy=account.firstName;
        cus.deviceID=[Raleys getDeviceID];
        cus.pushToken=pushDeviceToken;
        cus.deviceType=[[UIDevice currentDevice] model];
        cus.deviceOsVersion=[[UIDevice currentDevice] systemVersion];
        cus.deviceVendor=@"APPLE";
        cus.enabledFlag=@"Y";
        cus.createdBy=account.firstName;
        
        #ifdef CLP_ANALYTICS
        [app._clpSDK saveCustomer:cus :^(CLPCustomer *cusInfo, NSError *error) {
            
        }];
        
        //Start Location updates
        [app._clpSDK startStandardUpdate];
        #endif
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception.reason);
    }
}

+(NSString*)getDeviceID{
    if ([[UIDevice currentDevice]respondsToSelector:@selector(identifierForVendor)]) {
        return [UIDevice currentDevice].identifierForVendor.UUIDString;
    }else{
        return @"";
        //return [UIDevice currentDevice].uniqueIdentifier
        //return [[UIDevice currentDevice] performSelector:@selector(uniqueIdentifier)];
    }
}


// Get IP Address
+ (NSString *)getIPAddress {
    NSString *address = @"error";
    struct ifaddrs *interfaces = NULL;
    struct ifaddrs *temp_addr = NULL;
    int success = 0;
    // retrieve the current interfaces - returns 0 on success
    success = getifaddrs(&interfaces);
    if (success == 0) {
        // Loop through linked list of interfaces
        temp_addr = interfaces;
        while(temp_addr != NULL) {
            if(temp_addr->ifa_addr->sa_family == AF_INET) {
                // Check if interface is en0 which is the wifi connection on the iPhone
                if([[NSString stringWithUTF8String:temp_addr->ifa_name] isEqualToString:@"en0"]) {
                    // Get NSString from C String
                    address = [NSString stringWithUTF8String:inet_ntoa(((struct sockaddr_in *)temp_addr->ifa_addr)->sin_addr)];
                }
            }
            temp_addr = temp_addr->ifa_next;
        }
    }
    // Free memory
    freeifaddrs(interfaces);
    return address;
    
}

+(void)showNetworkIndicator{
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
}

+(void)hideNetworkIndicator{
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
}

+(NSString*)base64EncodeString:(NSString*)value{
    @try {
        //Encode String
        NSData *encodeData = [value dataUsingEncoding:NSUTF8StringEncoding];
        NSString *base64String;
        if ([encodeData respondsToSelector:@selector(base64EncodedStringWithOptions:)]) {
            base64String = [encodeData base64EncodedStringWithOptions:kNilOptions];  // iOS 7+
        } else {
            base64String = [encodeData base64Encoding];                              // pre iOS7
        }
//        NSLog(@"Encode String Value: %@", base64String);
        return base64String;
    }
    @catch (NSException *exception) {
        return @"";
    }
}

+(NSString*)base64DecodeString:(NSString*)value{
    @try {
        //Decode String
        NSData *data;
        if ([NSData instancesRespondToSelector:@selector(initWithBase64EncodedString:options:)]) {
            data = [[NSData alloc] initWithBase64EncodedString:value options:kNilOptions];  // iOS 7+
        } else {
            data = [[NSData alloc] initWithBase64Encoding:value];                           // pre iOS7
        }
        NSString *decodeStr=[[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding];
//        NSLog(@"Decode String Value:  %@",decodeStr);
        return decodeStr;
    }
    @catch (NSException *exception) {
        return @"";
    }
}

#pragma mark  Registration END -


@end
