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
                                       @"client_id":@"201969E1BFD242E189FE7B6297B1B5A5",
                                       @"Content-Type":contentType,
                                       @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB7",
                                       
}];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    

    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
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




#pragma mark - Register Api

-(void)registerAPI:(NSDictionary *)dictValue url:(NSString *)baseURl withTarget:(id)tempTarget withSelector:(SEL)tempSelector
{
    m_callBackSelector=tempSelector;
    m_callBackTarget=tempTarget;
    
    // 1
    
    NSString * baseurl = [NSString stringWithFormat:@"%@%@",baseURL,baseURl];
    
    NSURL *url = [NSURL URLWithString:baseurl];
    
    NSLog(@"url-----%@ %@",url,dictValue);
    
    //@"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    
    // Parse requires HTTP headers for authentication. Set them before creating your NSURLSession
    
    [config setHTTPAdditionalHeaders:@{@"Application":applicationType,
                                       @"client_id":clientID,
                                       @"Content-Type":contentType,
                                       @"response_type":@"token",
                                       @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
                                       @"response_type":@"code",
                                       @"scope":@"READ",
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
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
                                                }];
    
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
                                       @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
                                       @"access_token" :str,
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
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
                                       @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
                                       @"access_token" :str,
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"PUT";
    
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
                                                    @"tenantid" :@"1173",
                                                    }];
    
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
                                       @"client_secret":@"C65A0DC0F28C469FB7376F972DEFBCB8",
                                       @"access_token" :str,
                                       @"tenantid" :@"1173",
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"PUT";
    
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
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :@"1173",
                                                    @"client_id": @"201969E1BFD242E189FE7B6297B1B5A5",
                                                    @"access_token" : str,

                                                    }];
    
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

// getPromocode api

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
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :@"1173",
                                                    @"client_id": @"201969E1BFD242E189FE7B6297B1B5A5",
                                                    @"access_token" : str,
                                                    }];
    
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


// getOffer by offerID

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
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :@"1173",
                                                    @"client_id": @"201969E1BFD242E189FE7B6297B1B5A5",
                                                    @"access_token" : str,
                                                    }];
    
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


// PassOpen api method

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
    
    [defaultConfigObject setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                                    @"Content-Type":contentType,
                                                    @"tenantid" :@"1173",
                                                    @"client_id": @"201969E1BFD242E189FE7B6297B1B5A5",
                                                    @"access_token" : str,
                                                    @"tenantName": @"fishbowl",
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




// getAllStores api method


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
                                                    @"tenantid" :@"1173",
                                                    }];
    
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




// updateDevice api method

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
    
    [config setHTTPAdditionalHeaders:@{@"Application":@"mobilesdk",
                                       @"Content-Type":contentType,
                                       @"tenantid" :@"1173",
                                       @"client_id": @"201969E1BFD242E189FE7B6297B1B5A5",
                                       @"access_token" : str,
                                       }];
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config];
    
    
    NSLog(@"session-----%@ %@",session,config);
    
    // 2
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    request.HTTPMethod = @"POST";
    
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
                         NSLog(@"error description --------%@", error.description);
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



@end
