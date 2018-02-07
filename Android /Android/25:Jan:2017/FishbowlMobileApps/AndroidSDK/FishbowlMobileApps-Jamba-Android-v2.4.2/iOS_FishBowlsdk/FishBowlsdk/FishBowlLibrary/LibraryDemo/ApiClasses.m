//
//  ApiClasses.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 23/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "ApiClasses.h"
#import "Constant.h"

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"



@implementation ApiClasses
{
    ModelClass * obj;
}


+ (id)sharedManager
{
    static ApiClasses *sharedMyManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedMyManager = [[self alloc] init];
    });
    return sharedMyManager;
}


#pragma mark - device token

-(NSString *)deviceID
{
    obj=[ModelClass sharedManager];
    NSString *string = [obj retrieveDeviceId];
    return string;
}


 // ===================================== Jamba SDk Method ======================================= //

#pragma mark -  SDK SignUp Method
-(void)signUpSDKApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    // subURL - /member/create
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
 
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":@"mobile",
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response------%@", error.description);
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}

#pragma mark -  SDK Memer Login Method

-(void)loginSDKApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    // subURl - /member/loginSDK
    
    obj=[ModelClass sharedManager];
    NSString * strSpendoToken = [obj reterieveSpendGoToken]; // here you pass Spendgo Tokenkey to get spendgoToken
    
    NSString * strSpendgoCusterID = [obj reterieveSpendGoCustomerId];// here you pass Spendgo customerKey to get spendgoCusterID
    
    NSString * signature = [[NSUserDefaults standardUserDefaults]valueForKey:@"signature"];// here you pass Signature key to get X-Class-Signature value
    
    
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                @"client_id":clientID,
                                @"Content-Type":contentType,
                                @"X-Class-Signature":signature,
                                @"X-Class-Key":Key,
                                @"ExternalCustomerId":strSpendgoCusterID,
                                @"externalAccessToken":strSpendoToken,
                                @"SpendGoApiBaseUrl": spendgoBaseUrl
                                }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    
    NSLog(@"request ------- %@", request);
    //printf("%s", request);
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    
    if (!error) {
        // 4
        
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            
            
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    NSLog(@"json dict------%@",jsonDict);
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response.error.description------%@", error.description);
                NSLog(@"DOH");
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                    
                });
            }
        }];
        // 5
        NSLog(@"response.error.description------%@", error.description);
        [uploadTask resume];
    }
}


#pragma mark - Get Member Details by Spendgo Id

-(void)getMemberSDKApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    // subURL - member/getMemberByExternalCustomerId/{ExternalCustomerId}
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];

    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"access_token":strToken,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    NSLog(@"error description with 401 tag --------%@", error.description);
                                                                    
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}

#pragma mark - SDK Update Device Details

-(void)updateDeviceSDKApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
      // subURL - /member/deviceUpdate
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    //@"mobilesdk"
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"Content-Type":contentType,
                                       @"client_id":clientID,
                                       @"access_token" : strToken,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
    request.timeoutInterval = 30;
    
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *)response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                        NSLog(@"error description with 401 tag  --------%@", error.description);
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    NSLog(@"error description --------%@", error.description);
                    
                });
            }
            else
            {
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                
                NSLog(@"error description --------%@", error.description);
            }
        }];
        // 5
        [uploadTask resume];
    }
}







// =========================================  Basic Api =========================================//
#pragma mark - GuestUser Api

-(void)GuestUserAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    // events Api
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
  
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"tenantid":TanentID,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    
    NSLog(@"request ------- %@", request);
    //printf("%s", request);
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    
    if (!error) {
        // 4
        
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            
            
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    NSLog(@"json dict------%@",jsonDict);
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response.error.description------%@", error.description);
                NSLog(@"DOH");
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                    
                });
            }
        }];
        // 5
        NSLog(@"response.error.description------%@", error.description);
        [uploadTask resume];
    }

}

#pragma mark - Login Api


-(void)loginAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
   // @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB7"
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":ClientSecret,
                                       @"scope":@"read",
                                       @"state":@"fsdffsfdsdf3453535353",
                                       @"deviceId":[self deviceID],
}];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    

    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    
    NSLog(@"request ------- %@", request);
    //printf("%s", request);
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
 
    if (!error) {
        // 4
        
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
           

            
        if ([response respondsToSelector:@selector(statusCode)])
        {
            if ([(NSHTTPURLResponse *) response statusCode] == 401)
            {
                 dispatch_async(dispatch_get_main_queue(), ^{
                     
                // Remind the user to update the API Key
            });
        }
    }
    if (!error) {
        
            // Data was created, we leave it to you to display all of those tall tales!
        
            dispatch_async(dispatch_get_main_queue(), ^{

                
    NSError *parseJsonError = nil;
    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
            
        // call back method
                
            NSLog(@"json dict------%@",jsonDict);
                
        [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                
        jsonDict = nil;
     
           });
        }
       else
       {
           NSLog(@"response.error.description------%@", error.description);
           NSLog(@"DOH");
           
           dispatch_async(dispatch_get_main_queue(), ^{
               
               [m_callBackTarget performSelector:m_callBackSelector withObject:nil];

           });
        }
    }];
        // 5
        NSLog(@"response.error.description------%@", error.description);
        [uploadTask resume];
    }
}


#pragma mark - SIGNUP Api

-(void)signupAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    

    //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":ClientSecret,
                                       @"response_type":@"code",
                                       @"scope":@"READ",
                                       @"access_token" :strToken,
                                       @"tenantid" :TanentID,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response------%@", error.description);
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}

#pragma mark - MobileSetting Api

-(void)mobileSettingAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];

    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
   [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                   @"client_id":clientID,
                                                   @"Content-Type":contentType,
                                                   @"deviceId":[self deviceID],
                                                }];
    
     NSLog(@"url type-----%@",defaultConfigObject);
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
            if ([response respondsToSelector:@selector(statusCode)])
                    {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                    {
                    dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                            // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                    if (!error) {
                                                            
                        // Data was created, we leave it to you to display all of those tall tales!
                                                            
                    dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                        options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                 [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                else
                   {
                       
                 NSLog(@"response------%@", error.description);      
                    NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                  }
                    }];
    // 5
    [dataTask resume];
}


#pragma mark - logout Api

-(void)logoutAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    //NSLog(@"url-----%@ %@",url,dictValue);
    
     // @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"access_token" :strToken,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
 
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *)response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];

            }
        }];
        // 5
        [uploadTask resume];
    }
}


#pragma mark - changePassword Api


-(void)changePassword:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
       //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8"
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":ClientSecret,
                                       @"access_token" :strToken,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"PUT";
    request.timeoutInterval = 30;
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}


#pragma mark - getMember Api

-(void)getMember:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
 
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"access_token" :strToken,
                                                    @"tenantid" :TanentID,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                    NSLog(@"error description with 401 tag --------%@", error.description);
                   
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);      
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}



#pragma mark - update Profile Api

-(void)updateMember:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
   //  @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB9"
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"access_token" :strToken,
                                       @"tenantid" :TanentID,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"PUT";
    request.timeoutInterval = 30;
    
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
                
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}


#pragma mark - get offers Api

-(void)getOffers:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    NSLog(@"strToken is %@",urlStr);
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
   // @"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : strToken,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                    NSLog(@"error description with 401 tag   --------%@", error.description);
        
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark - get Rewards Api

-(void)getRewards:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    NSLog(@"strToken is %@",urlStr);
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : strToken,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark - get Loyality Points Api

-(void)getLoyalityPoints:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    NSLog(@"strToken is %@",urlStr);
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID ,
                                                    @"access_token" : strToken,
                                                    @"client_secret":ClientSecret,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                    }];
    // 5
    [dataTask resume];

}



#pragma mark - get PromoCode Api

-(void)getPromocode:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id": clientID,
                                                    @"access_token" : strToken,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark -  getOffer by offerID Api

-(void)getuserOffer:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : strToken,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark - PassOpen api method

-(void)PassOpen:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : strToken,
                                                    @"tenantName": @"fishbowl",
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    
    
    NSLog(@"dictValue is %@",dictValue.description);
    NSString *memberid = [dictValue objectForKey:@"customerID"];
    NSString *campaignId = [dictValue objectForKey:@"campaignId"];

    NSString *strDevice = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceType"];
    NSLog(@"strDevice is %@",strDevice);
    
    NSMutableDictionary *dicdata = [[NSMutableDictionary alloc]init];
    [dicdata setValue:memberid forKey:@"memberid"];
    [dicdata setValue:campaignId forKey:@"campaignId"];
    [dicdata setValue:strDevice forKey:@"deviceName"];
    
    
    NSError *error;
    
    NSData *postData = [NSJSONSerialization dataWithJSONObject:dicdata options:0 error:&error];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:defaultConfigObject];
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = YES;
    [request setHTTPBody:postData];

    
    NSURLSessionDataTask *postDataTask = [session dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        // Check to make sure the server didn't respond with a "Not Authorized"
        if ([response respondsToSelector:@selector(statusCode)])
        {
            if ([(NSHTTPURLResponse *) response statusCode] == 401)
            {
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    // Remind the user to update the API Key
                });
            }
        }
        if (!error) {
            
            // Data was created, we leave it to you to display all of those tall tales!
            
            if(data.length>0)
            {
                NSLog(@"pass data-------%@",data);
                
            }
            
            dispatch_async(dispatch_get_main_queue(), ^{
                
                
                
                //                                                                NSError *parseJsonError = nil;
                //                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                //                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                
                if(data.length>0)
                {                                             // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:data];
                    
                }                                        // jsonDict = nil;
                
            });
        }
        else
        {
            
            NSLog(@"response------%@", error.description);
            NSLog(@"DOH");
            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
        }
    }];
    [postDataTask resume];
    
    NSLog(@"url-----%@",urlStr);
    
}

#pragma mark -   getAllStores api method

-(void)getAllStores:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark -  updateDevice api method

-(void)updateDevice:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
   
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    //@"mobilesdk"
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"Content-Type":contentType,
                                       @"tenantid" :TanentID,
                                       @"client_id":clientID,
                                       @"access_token" : strToken,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
    request.timeoutInterval = 30;
    
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *)response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                         NSLog(@"error description with 401 tag  --------%@", error.description);
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                     NSLog(@"error description --------%@", error.description);
                    
                });
            }
            else
            {
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                
                NSLog(@"error description --------%@", error.description);
            }
        }];
        // 5
        [uploadTask resume];
    }
}


#pragma mark -  ForgotPassword api method

-(void)ForgotPasswordApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    // @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB7"
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":ClientSecret,
                                       @"tenantid" :TanentID,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    
    NSLog(@"request-----%@",request);
    
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    
    if (!error) {
        // 4
        
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
    
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    // call back method
                    
                    NSLog(@"json dict------%@",jsonDict);
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response.error.description------%@", error.description);
                NSLog(@"DOH");
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                    
                });
            }
        }];
        // 5
        NSLog(@"response.error.description------%@", error.description);
        [uploadTask resume];
    }
    
}



#pragma mark -  Store Search api method

-(void)storeSearchApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    // @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB7"
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"tenantid" :TanentID,
                                       @"access_token" : strToken,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    NSLog(@"request-----%@",request);
    
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    
    if (!error) {
        // 4
        
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
        if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    NSLog(@"json dict------%@",jsonDict);
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response.error.description------%@", error.description);
                NSLog(@"DOH");
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                    
                });
            }
        }];
        // 5
        NSLog(@"response.error.description------%@", error.description);
        [uploadTask resume];
    }
}


#pragma mark - set favourit Store api method

-(void)favouritStorehApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8"
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":ClientSecret,
                                       @"access_token" :strToken,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"PUT";
    request.timeoutInterval = 30;
    
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}


#pragma mark - getTokenApi method

-(void)getTokenApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    // subURl = /mobile/getToken
    
    
   NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
   NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
 
    // @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB7"
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Content-Type": contentType,
                                       @"Accept": contentType,
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"PUT";
    request.timeoutInterval = 30;
    
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = [[NSMutableDictionary alloc] init];
    [dictionary setValue:clientID forKey:@"clientId"];
    [dictionary setValue:ClientSecret forKey:@"clientSecret"];
    [dictionary setValue:TanentID forKey:@"tenantId"];
    [dictionary setValue:[self deviceID] forKey:@"deviceId"];

    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    
    if (!error) {
        // 4
        
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            
            
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    NSLog(@"json dict------%@",jsonDict);
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response.error.description------%@", error.description);
                NSLog(@"DOH");
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                    
                });
            }
        }];
        // 5
        NSLog(@"response.error.description------%@", error.description);
        [uploadTask resume];
    }
}



#pragma mark - faceBook Register Api

-(void)faceBookregisterAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    obj=[ModelClass sharedManager];
    
    
    //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":ClientSecret,
                                       @"response_type":@"code",
                                       @"scope":@"READ",
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response------%@", error.description);
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}


#pragma mark - State Search Api

-(void)stateSearch:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark - getPointBankOffer Api

-(void)getPointBankOffer:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : strToken,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
            NSLog(@"response------%@", error.description);
                    NSLog(@"DOH");
        [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark - Use Offer Api

-(void)useOffer:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"tenantid" :TanentID,
                                       @"client_secret":ClientSecret,
                                       @"access_token" :strToken,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    
    request.HTTPMethod = @"PUT";
    request.timeoutInterval = 30;   // timeinterval
    
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"DOH");
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}





#pragma mark - menu_Family Api

-(void)menuFamily:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    // menuFamilyUrl -> /menu/family
    // parameter -> storeId=647
    // methodType -> GET
    // baseURL/menu/family?storeId=647
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
 
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :strToken,
                                                    @"REDIS":@"false",
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}

#pragma mark -  GetCategory api method

-(void)getCategory:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    // menuCategory -> /menu/category
    // parameter -> storeId=104& familyId=1
    // methodType -> GET
    // baseURL/menu/category?storeId=104& familyId=1

    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :strToken,
                                                    @"REDIS":@"false",
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark -  Get SubCategory api method

-(void)SubCategory:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    // menuSubCategory -> /menu/subCategory
    // parameter -> storeId=647&familyId=2&categoryId=2
    // methodType -> GET
    // baseURL/menu/subCategory?storeId=647&categoryId=2
    
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :strToken,
                                                    @"REDIS":@"false",
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}

#pragma mark -  Get Product List(Menu) API

-(void)ProductList:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    // menuProductList -> /menu/menuDrawer
    // parameter -> storeId=647&familyId=2&categoryId=2&subCategoryId=2
    // methodType -> GET
    // baseURL/menu/menuDrawer?storeId=647&familyId=2&categoryId=2&subCategoryId=2
    
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :strToken,
                                                    @"REDIS":@"false",
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark -  Get Product Attributes API
-(void)ProductAttributes:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    // menuProductList -> /menu/product
    // parameter -> storeId=647&familyId=1&categoryId=0&subCategoryId=1&productId=1
    // methodType -> GET
    // baseURL/menu/product?storeId=647&categoryId=2&subCategoryId=2&productId=1
    
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :strToken,
                                                    @"REDIS":@"false",
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}

#pragma mark -  Get Category with relevant Subcategory API
-(void)categorySubCategory:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    // menuProductList -> /menu/categorySubCategory
    // parameter -> storeId=647&familyId=2
    // methodType -> GET
    // baseURL/menu/categorySubCategory?storeId=647&familyId=2
    
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :strToken,
                                                    @"REDIS":@"false",
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];

}


#pragma mark -  categoryProductList API

-(void)categoryProductList:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    // menuProductList -> /menu/categoryProductList
    // parameter -> storeId=647&familyId=2&categoryId=2
    // methodType -> GET
    // baseURL/menu/categoryProductList?storeId=647&familyId=2&categoryId=2
    
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :strToken,
                                                    @"REDIS":@"false",
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}



#pragma mark -  getallrewardofferApi API

-(void)getallrewardofferApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :strToken,
                                                    @"deviceId":[self deviceID],
                                                    }];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    NSLog(@"url-----%@",urlStr);
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    NSLog(@"error description with 401 tag   --------%@", error.description);
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark - signupRuleList Api

-(void)signupRuleList:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    
    // url - /loyalty/signupRuleList
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"client_secret":ClientSecret,
                                                    @"deviceId":[self deviceID],
                                                    @"tenantid" :TanentID,
                                                    }];
    
    NSLog(@"url type-----%@",defaultConfigObject);
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
    
}

#pragma mark - getStoreDetails method

-(void)getStoreDetails:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    // url - /mobile/stores/getStoreDetails/{storeId}
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"client_secret":ClientSecret,
                                                    @"deviceId":[self deviceID],
                                                    @"access_token" :strToken,
                                                    @"tenantid" :TanentID,
                                                    }];
    NSLog(@"url type-----%@",defaultConfigObject);
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}

#pragma mark -  redeemed API

-(void)redeemedApi:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    // subURl - redeemed/{memberId}/{offerId}
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"client_secret":ClientSecret,
                                                    @"deviceId":[self deviceID],
                                                    @"access_token" :strToken,
                                                    @"tenantid" :TanentID,
                                                    }];
    NSLog(@"url type-----%@",defaultConfigObject);
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}


#pragma mark -  redeemed API

-(void)mobileAppEvents:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    obj=[ModelClass sharedManager];
    NSString * strToken = [obj reterieveFishbowlToken];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"tenantid":TanentID,
                                       @"tenantName":Tanentname,
                                       @"access_token" :strToken,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    
    NSLog(@"request ------- %@", request);
    //printf("%s", request);
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    
    
    if (!error) {
        // 4
        
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                        
                        NSInteger intCode = [(NSHTTPURLResponse *) response statusCode];
                        
                        NSMutableDictionary *jsonDict = [[NSMutableDictionary alloc]init];
                        [jsonDict setValue:[NSString stringWithFormat:@"%li",(long)intCode] forKey:@"StatusCode"];
                        [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                        jsonDict = nil;

                    });
                }
            else if ([(NSHTTPURLResponse *) response statusCode] == 200) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    
                    // call back method
                    
                    NSLog(@"json dict------%@",jsonDict);
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response.error.description------%@", error.description);
                NSLog(@"DOH");
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                    
                });
            }
            }
        }];
        // 5
        NSLog(@"response.error.description------%@", error.description);
        [uploadTask resume];
    }
}

# pragma mark - get mobile settings
-(void)getMobileSettings:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    obj=[ModelClass sharedManager];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType
                                                    }];
    
    NSURLSession *defaultSession = [NSURLSession sessionWithConfiguration: defaultConfigObject delegate: nil delegateQueue: [NSOperationQueue mainQueue]];
    defaultConfigObject.timeoutIntervalForRequest = 30;
    
    NSLog(@"url-----%@",urlStr);
    
    
    NSURLSessionDataTask * dataTask = [defaultSession dataTaskWithURL:url
                                                    completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
                                                        // Check to make sure the server didn't respond with a "Not Authorized"
                                                        if ([response respondsToSelector:@selector(statusCode)])
                                                        {
                                                            if ([(NSHTTPURLResponse *) response statusCode] == 401)
                                                            {
                                                                dispatch_async(dispatch_get_main_queue(), ^{
                                                                    NSLog(@"error description with 401 tag --------%@", error.description);
                                                                    
                                                                    
                                                                    // Remind the user to update the API Key
                                                                });
                                                            }
                                                        }
                                                        if (!error) {
                                                            
                                                            // Data was created, we leave it to you to display all of those tall tales!
                                                            
                                                            dispatch_async(dispatch_get_main_queue(), ^{
                                                                
                                                                NSError *parseJsonError = nil;
                                                                NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                                                                         options:NSJSONReadingAllowFragments error:&parseJsonError];
                                                                
                                                                // call back method
                                                                
                                                                [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                                                                
                                                                jsonDict = nil;
                                                                
                                                            });
                                                        }
                                                        else
                                                        {
                                                            
                                                            NSLog(@"response------%@", error.description);
                                                            NSLog(@"DOH");
                                                            [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
                                                        }
                                                    }];
    // 5
    [dataTask resume];
}

# pragma mark - Gift Card API

# pragma mark - Get InCommToken API


-(void)InCommTokenAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    // subURL - mobile/incomm/token
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    obj=[ModelClass sharedManager];

    
    NSString * strToken = [obj reterieveFishbowlToken];

    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"deviceId": [self deviceID],
                                       @"access_token": strToken,
                                       @"Accept" :contentType,
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response------%@", error.description);
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}



# pragma mark - Get Order value api


-(void)OrderValueAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    // subURL - mobile/incomm/rewards
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    obj=[ModelClass sharedManager];

    
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"deviceId": [self deviceID],
                                       @"access_token": strToken,
                                       @"Accept" :contentType,
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response------%@", error.description);
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}



# pragma mark - Get Incomm Order ID API


-(void)incommOrderIDAPI:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    // subURL - /mobile/incomm/orderId
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    obj=[ModelClass sharedManager];

    
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"deviceId": [self deviceID],
                                       @"access_token": strToken,
                                       @"Accept" :contentType,
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response------%@", error.description);
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}


# pragma mark - Get Update incomm transaction


-(void)updateIncommTransaction:(NSDictionary *)dictValue url:(NSString *)subURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    // subURL - /mobile/incomm/orderId
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,subURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    obj=[ModelClass sharedManager];

    
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    NSString * strToken = [obj reterieveFishbowlToken];
    
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"deviceId": [self deviceID],
                                       @"access_token": strToken,
                                       @"Accept" :contentType,
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"PUT";
    request.timeoutInterval = 30;
    NSLog(@"request-----%@",request);
    
    // 3
    NSDictionary *dictionary = dictValue;
    NSError *error = nil;
    NSData *data = [NSJSONSerialization dataWithJSONObject:dictionary
                                                   options:kNilOptions error:&error];
    if (!error) {
        // 4
        NSURLSessionUploadTask *uploadTask = [session uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            
            // Check to make sure the server didn't respond with a "Not Authorized"
            
            if ([response respondsToSelector:@selector(statusCode)])
            {
                if ([(NSHTTPURLResponse *) response statusCode] == 401)
                {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                        // Remind the user to update the API Key
                    });
                }
            }
            if (!error) {
                
                // Data was created, we leave it to you to display all of those tall tales!
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    NSError *parseJsonError = nil;
                    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data
                                                                             options:NSJSONReadingAllowFragments error:&parseJsonError];
                    // call back method
                    
                    [m_callBackTarget performSelector:m_callBackSelector withObject:jsonDict];
                    
                    jsonDict = nil;
                    
                });
            }
            else
            {
                NSLog(@"response------%@", error.description);
                [m_callBackTarget performSelector:m_callBackSelector withObject:nil];
            }
        }];
        // 5
        [uploadTask resume];
    }
}


@end
