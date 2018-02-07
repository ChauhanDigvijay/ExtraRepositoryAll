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
        NSString *string = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
        string = [string stringByReplacingOccurrencesOfString:@"-" withString:@""];
        string = [NSString stringWithFormat:@"%@",string];
        return string;
}


#pragma mark - Login Api


-(void)loginAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
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
    
    
    NSLog(@"device id is-----%@ %@",url,[self deviceID]);
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval = 30;
    
    NSLog(@"request ------- %@", request.description);
    NSLog(@"config ------- %@", config);
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


#pragma mark - Register Api

-(void)registerAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];

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
                                       @"tenantid" :TanentID,
                                       @"deviceId":[self deviceID],
                                       @"access_token" :str,
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

-(void)mobileSettingAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];

    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
   [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                   @"client_id":clientID,
                                                   @"Content-Type":contentType,
                                                   @"contentType":[self deviceID],
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


# pragma mark - MobileSetting Api

-(void)ThemeSettingsAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"contentType":[self deviceID],
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



#pragma mark - logout Api

-(void)logoutAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    //NSLog(@"url-----%@ %@",url,dictValue);
    
     // @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"access_token" :str,
                                       @"deviceId":[self deviceID],
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    request.timeoutInterval =30;
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


-(void)changePassword:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
       //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8"
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":ClientSecret,
                                       @"access_token" :str,
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


#pragma mark - getMember Api

-(void)getMember:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSLog(@"Access token========%@",str);
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"access_token" :str,
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

-(void)updateProfile:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
   //  @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB9"
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"access_token" :str,
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

-(void)getOffers:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSLog(@"strToken is %@",urlStr);
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
   // @"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : str,
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


#pragma mark - get Rewards Api

-(void)getRewards:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSLog(@"strToken is %@",urlStr);
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : str,
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


#pragma mark - get Loyality Points Api

-(void)getLoyalityPoints:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSLog(@"strToken is %@",urlStr);
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID ,
                                                    @"access_token" : str,
                                                    @"client_secret":ClientSecret,
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



#pragma mark - get PromoCode Api

-(void)getPromocode:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id": clientID,
                                                    @"access_token" : str,
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

-(void)getuserOffer:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : str,
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


#pragma mark - PassOpen api method

-(void)PassOpen:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : str,
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
    request.timeoutInterval = 30;
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

-(void)getAllStores:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
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

-(void)updateDevice:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
   
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    //@"mobilesdk"
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"Content-Type":contentType,
                                       @"tenantid" :TanentID,
                                       @"client_id":clientID,
                                       @"access_token" : str,
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

-(void)ForgotPasswordApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    obj=[ModelClass sharedManager];

    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    

    
    NSLog(@"access_token is %@",str);

    // @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB7"
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":ClientSecret,
                                       @"tenantid" :TanentID,
                                       @"access_token" : str,
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

-(void)storeSearchApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    NSLog(@"access_token is %@",str);
    
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
                                       @"access_token" : str,
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

-(void)getStoreDetailApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{

    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"client_secret":ClientSecret,
                                                    @"tenantid" :TanentID,
                                                    @"access_token" : str,
                                                    @"deviceId":[self deviceID],
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



#pragma mark - set favourit Store api method

-(void)favouritStorehApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8"
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":ClientSecret,
                                       @"access_token" :str,
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

-(void)getTokenApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector;
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    // baseURl = /mobile/getToken
    
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
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



#pragma mark - faceBook Register Api

-(void)faceBookregisterAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    //obj=[ModelClass sharedManager];
    
    
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

-(void)stateSearch:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :@"TanentID",
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


#pragma mark - Country Api

-(void)countryApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
   // obj=[ModelClass sharedManager];
   // NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"deviceId":[self deviceID],
                                                   // @"access_token" :str,
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


#pragma mark - get Loyality Card

-(void)LoyaltyCardApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    NSLog(@"strToken is %@",urlStr);
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    //@"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :@"TanentID",
                                                    @"client_id":clientID,
                                                    @"access_token" : str,
                                                    @"deviceId":[self deviceID],
                                                    @"tenantName" :@"fishbowl",
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
            if (!error)
            {
                
        // Data was created, we leave it to you to display all of those tall tales!
                    
                    if(data.length>0)
                    {
                        NSLog(@"pass data-------%@",data);
                    }
                    
                    dispatch_async(dispatch_get_main_queue(), ^{
                        
                   if(data.length>0)
                {
                    // call back method
                            
                [m_callBackTarget performSelector:m_callBackSelector withObject:data];
                            
                }
                // jsonDict = nil;
                        
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


#pragma mark - LoyaltyActivity Api

-(void)LoyaltyActivityApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    // @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB7"
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"access_token" : str,
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

#pragma mark - signupRuleList Api

-(void)signupRuleList:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"client_secret":ClientSecret,
                                                    @"tenantid":TanentID,
                                                    @"deviceId":[self deviceID],
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


#pragma mark - inAppAd Api


-(void)inAppAd:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"tenantid":TanentID,
                                                    @"client_secret":ClientSecret,
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


#pragma mark - getLoyaltyMessageType Api

-(void)getLoyaltyMessageType:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
  
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"client_secret":ClientSecret,
                                                    @"tenantid":TanentID,
                                                    @"access_token" : str,
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



#pragma mark - getLoyaltyAreaType Api

-(void)getLoyaltyAreaType:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"client_secret":ClientSecret,
                                                    @"tenantid":TanentID,
                                                    @"deviceId":[self deviceID],
                                                    @"access_token" : str,
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


#pragma mark - Message Api

-(void)messages:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
     obj=[ModelClass sharedManager];
     NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    
    //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":@"mobile",
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"access_token" : str,
                                       @"tenantid":TanentID,
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


#pragma mark - getLoyaltyMessageType Api

-(void)emailApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"client_secret":ClientSecret,
                                                    @"tenantid":TanentID,
                                                    @"access_token" : str,
                                                    @"deviceId":[self deviceID],
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


#pragma mark -  Get_Category api method

-(void)menuApi:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"client_id":clientID,
                                                    @"Content-Type":contentType,
                                                    @"client_secret":ClientSecret,
                                                    @"tenantid":TanentID,
                                                    @"access_token" : str,
                                                    @"deviceId":[self deviceID],
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


#pragma mark - Get Product List(Menu) API

-(void)ProductList:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    // menuProductList -> /menu/menuDrawer
    // parameter -> storeId=647&familyId=2&categoryId=2&subCategoryId=2
    // methodType -> GET
    // baseURL/menu/menuDrawer?storeId=647&familyId=2&categoryId=2&subCategoryId=2
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :str,
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
                                                                dispatch_sync(dispatch_get_main_queue(), ^{
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

-(void)SubCategory:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    // menuSubCategory -> /menu/subCategory
    // parameter -> storeId=647&familyId=2&categoryId=2
    // methodType -> GET
    // baseURL/menu/subCategory?storeId=647&familyId=2&categoryId=2
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :str,
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

#pragma mark -  Get ProductAttributes API method
-(void)ProductAttributes:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    // menuProductList -> /menu/product
    // parameter -> storeId=647&familyId=1&categoryId=0&subCategoryId=1&productId=1
    // methodType -> GET
    // baseURL/menu/product?storeId=647&categoryId=2&subCategoryId=2&productId=1
    
    //storeId=647&categoryId=0&subCategoryId=1&productId=1
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" :str,
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


#pragma mark -  Get getallrewardoffer API method

-(void)getallrewardoffer:(NSDictionary* )dictValue url:(NSString* )baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    
   // BaseURL/loyaltyw/getallrewardoffer
    
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    // 1
    NSString *urlStr = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSLog(@"strToken is %@",urlStr);
    NSURL *url = [NSURL URLWithString:urlStr];
    NSURLSessionConfiguration *defaultConfigObject = [NSURLSessionConfiguration defaultSessionConfiguration];
    // @"mobilesdk"
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :TanentID,
                                                    @"client_id":clientID,
                                                    @"access_token" : str,
                                                    @"deviceId":[self deviceID],
                                                    @"client_secret":ClientSecret,
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



#pragma mark -  UseOffer API method
-(void)useOffer:(NSDictionary* )dictValue url:(NSString* )baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8"
    
    obj=[ModelClass sharedManager];
    NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"client_secret":ClientSecret,
                                       @"access_token" :str,
                                       @"deviceId":[self deviceID],
                                       @"tenantid" :TanentID,
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

-(void)sms:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"http://52.38.132.133/sms/registration"];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    obj=[ModelClass sharedManager];
    //NSString * str = [obj reterieveuserDefaultData:@"access_token"];
    
    //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{
                                       @"Content-Type":@"multipart/form-data"
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


@end
