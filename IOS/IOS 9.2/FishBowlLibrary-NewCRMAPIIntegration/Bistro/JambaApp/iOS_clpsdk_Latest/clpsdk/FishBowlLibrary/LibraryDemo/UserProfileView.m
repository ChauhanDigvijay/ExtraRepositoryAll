//
//  UserProfileView.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 31/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "UserProfileView.h"
#import "ModelClass.h"
#import "ApiClasses.h"
#import "OfferDetailView.h"
#import "StoreSearchView.h"

@interface UserProfileView ()<UITextFieldDelegate,StoreSearch>
{
    ModelClass  * obj;
    ApiClasses * apiCall;
    UITextField *textFieldDynamic;
    BOOL isEdit;
    BOOL isCheckEmail;
    BOOL isCheckSms;
    NSString * genderSelection;
    NSString * emailSelect;
    NSString * smsSelection;
    NSDictionary * dic;
    NSString * storeNameValue;
    NSString * storeNumberValue;
    NSString * storeNumberString;

}
@property (weak, nonatomic) IBOutlet UIView *datePickerView;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *sendBtn;
@property (weak, nonatomic) IBOutlet UIImageView *headerImage;
@property (weak, nonatomic) IBOutlet UIScrollView *mainScrollView;
@property (weak, nonatomic) IBOutlet UIButton *editBtn;
@property (weak, nonatomic) IBOutlet UIDatePicker *datePicker;
@property (weak, nonatomic) IBOutlet UIImageView *companyImageLogo;
@property (weak, nonatomic) IBOutlet UIButton *changePasswordBtn;
@property (weak, nonatomic) IBOutlet UIButton *saveBtn;
@property (weak, nonatomic) IBOutlet UILabel *updateProfile;

@end

@implementation UserProfileView

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    obj=[ModelClass sharedManager];
    [self.datePickerView setHidden:YES];

   // NSString * str = [obj reterieveuserDefaultData:@"mobileSetting"];
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    // button background color
   self.saveBtn.backgroundColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
   self.editBtn.backgroundColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
   self.updateProfile.textColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    

    // background image
    
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:
                   str2];
    [self.backgroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
//    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
//    NSURL *url3 = [NSURL URLWithString:
//                   str3];
//    [self.headerImage sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    
    NSString * str = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    NSURL *url = [NSURL URLWithString:
                  str];
    [self.companyImageLogo sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
  
    // button corner radius
    self.saveBtn.layer.masksToBounds = YES;
    self.saveBtn.layer.cornerRadius = self.saveBtn.frame.size.height/2.0;;
    self.saveBtn.layer.borderWidth = 1.0;
    self.saveBtn.layer.borderColor = [[UIColor clearColor] CGColor];
    
    [self profileRecord];

}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
}


// profile

-(void)profileRecord
{
    // fetch profile data
    
    NSUserDefaults *def = [NSUserDefaults standardUserDefaults];
    NSData *data = [def objectForKey:@"MyData"];
    
    if(data!=nil)
    {
        NSDictionary *retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
        dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
        NSLog(@"userProfile------%@",dic);
    }
    NSArray * arr ;
    NSArray * arr1;
    
    if(dic.count!=0)
    {
     arr = dic.allKeys;
     arr1 = dic.allValues;
     NSLog(@"all values and all keys------%@ %@",arr ,arr1);
    }
    
   // NSMutableArray * arrAddFields = [NSMutableArray new];

    NSMutableArray * arrfields = [NSMutableArray new];
    NSMutableArray * arrKeys = [NSMutableArray new];
    

        if([arr containsObject:@"firstName"]&&[dic valueForKey:@"firstName"]!= ( NSString *)[NSNull null])
        {
            [arrfields addObject:[dic valueForKey:@"firstName"]];
            [arrKeys addObject:@"firstName"];
        }
        
        if([arr containsObject:@"lastName"]&&[dic valueForKey:@"lastName"]!= ( NSString *) [ NSNull null ])
        {
            [arrfields addObject:[dic valueForKey:@"lastName"]];
            [arrKeys addObject:@"lastName"];
        }
        
        if([arr containsObject:@"emailID"]&&[dic valueForKey:@"emailID"]!= ( NSString *) [ NSNull null ])
        {
            [arrfields addObject:[dic valueForKey:@"emailID"]];
            [arrKeys addObject:@"emailID"];
        }
        
        if([arr containsObject:@"cellPhone"]&&[dic valueForKey:@"cellPhone"]!= ( NSString *) [ NSNull null ])
        {
            [arrfields addObject:[dic valueForKey:@"cellPhone"]];
            [arrKeys addObject:@"cellPhone"];
        }
        
        if([arr containsObject:@"dateOfBirth"]&&[dic valueForKey:@"dateOfBirth"]!= ( NSString *) [ NSNull null ])
        {
            [arrfields addObject:[dic valueForKey:@"dateOfBirth"]];
            [arrKeys addObject:@"dateOfBirth"];
        }
        
        if([arr containsObject:@"customerGender"]&&[dic valueForKey:@"customerGender"]!= ( NSString *) [ NSNull null ])
        {
             [arrfields addObject:[dic valueForKey:@"customerGender"]];
             [arrKeys addObject:@"customerGender"];
        }
        
        if([arr containsObject:@"addressLine1"]&&[dic valueForKey:@"addressLine1"]!= ( NSString *) [ NSNull null ])
        {
            [arrfields addObject:[dic valueForKey:@"addressLine1"]];
            [arrKeys addObject:@"addressLine1"];
        }
        
        if([arr containsObject:@"addressCity"]&&[dic valueForKey:@"addressCity"]!= ( NSString *) [ NSNull null ])
        {
             [arrfields addObject:[dic valueForKey:@"addressCity"]];
             [arrKeys addObject:@"addressCity"];
        }
        
        if([arr containsObject:@"addressState"]&&[dic valueForKey:@"addressState"]!= ( NSString *) [ NSNull null ])
        {
            [arrfields addObject:[dic valueForKey:@"addressState"]];
            [arrKeys addObject:@"addressState"];
        }
        
        if([arr containsObject:@"addressZip"]&&[dic valueForKey:@"addressZip"]!= ( NSString *) [ NSNull null ])
        {
            [arrfields addObject:[dic valueForKey:@"addressZip"]];
            [arrKeys addObject:@"addressZip"];
        }
        
        if([arr containsObject:@"homeStoreID"]&&[dic valueForKey:@"homeStoreID"]!= ( NSString *) [ NSNull null ])
        {
            [arrfields addObject:[dic valueForKey:@"homeStoreID"]];
            [arrKeys addObject:@"homeStoreID"];
        }
        
        if([arr containsObject:@"smsOpted"]&&[dic valueForKey:@"smsOpted"]!= (NSString *) [NSNull null])
        {
            [arrfields addObject:[dic valueForKey:@"smsOpted"]];
            [arrKeys addObject:@"smsOpted"];
        }
        
        if([arr containsObject:@"emailOpted"]&&[dic valueForKey:@"emailOpted"]!= (NSString *)[ NSNull null ])
        {
            [arrfields addObject:[dic valueForKey:@"emailOpted"]];
            [arrKeys addObject:@"emailOpted"];
        }
    
    
    NSLog(@"arrfields and all values------%@",arrfields);
    NSLog(@"arrKeys and all values------%@",arrKeys);

    self.mainScrollView.backgroundColor = [UIColor clearColor];
    
    int y=112 ;
    
    self.mainScrollView.contentSize = CGSizeMake(self.mainScrollView.frame.size.width, 40*arrfields.count+120+117);
    
    if(arr1.count>0)
    {
        for(int i = 0; i<[arrfields count]; i++)
        {
            CGRect screenRect = [[UIScreen mainScreen] bounds];
            CGFloat screenWidth = screenRect.size.width;
            CGFloat screenHeight = screenRect.size.height;
            
            if(screenWidth == 320 && screenHeight == 480)
            {
                textFieldDynamic  = [[UITextField alloc] initWithFrame:CGRectMake(20, y, 280, 40)];
            }
            else if (screenWidth == 320 && screenHeight == 568)
            {
                textFieldDynamic  = [[UITextField alloc] initWithFrame:CGRectMake(20,y,280,40)];
            }
            else if (screenWidth == 375 && screenHeight == 667)
            {
                textFieldDynamic  = [[UITextField alloc] initWithFrame:CGRectMake(20, y, 335, 40)];
            }
            else if (screenWidth == 414 && screenHeight == 736)
            {
                textFieldDynamic  = [[UITextField alloc] initWithFrame:CGRectMake(20, y, 374, 40)];
            }
            textFieldDynamic.borderStyle = UITextBorderStyleNone;
            //textFieldDynamic.font = [UIFont systemFontOfSize:13];
            textFieldDynamic.font = [UIFont fontWithName:@"Avant Garde XLight" size:13];
            textFieldDynamic.backgroundColor = [UIColor whiteColor];
            textFieldDynamic.alpha = 0.8f;
           
            if(arrfields == (id)[NSNull null])
            {
                
            }
            else
            {
                if ([[arrfields objectAtIndex:i] integerValue] == 1)
                {
                    
                    if ([[arrKeys objectAtIndex:i] isEqualToString:@"homeStoreID"])
                    {
                        textFieldDynamic.placeholder = @"favourite Store";
                        textFieldDynamic.tag = 108;
                        
                        
                        UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
                        UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                        
                        if (screenWidth == 375 && screenHeight == 667)
                        {
                            male1.frame = CGRectMake(0, 0, 320, 40);
                            male.frame = CGRectMake(280, 10, 20, 20);
                        }
                        else if (screenWidth == 320 && screenHeight == 568)
                        {
                            male1.frame = CGRectMake(0, 0, 300, 40);
                            male.frame = CGRectMake(220, 10, 20, 20);
                        }
                        else if (screenWidth == 320 && screenHeight == 480)
                        {
                            male1.frame = CGRectMake(0, 0, 300, 40);
                            male.frame = CGRectMake(220, 10, 20, 20);
                        }
                        else if (screenWidth == 414 && screenHeight == 736)
                        {
                            male1.frame = CGRectMake(0, 0, 340, 40);
                            male.frame = CGRectMake(320, 10, 20, 20);
                        }
                        
                        male1.backgroundColor = [UIColor clearColor];
                        [male1 addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                        male1.tag =9001;
                        [textFieldDynamic addSubview:male1];
                        
                        
                        [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
                        [male addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                         [textFieldDynamic addSubview:male];

                    }
                    
                    if([[arrKeys objectAtIndex:i]isEqualToString:@"smsOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                            textFieldDynamic.text = @"smsOpted";
                            textFieldDynamic.placeholder = @"smsOpted";
                            UIFont* boldFont = [UIFont boldSystemFontOfSize:[UIFont systemFontSize]];
                            [textFieldDynamic setFont:boldFont];
                        }
                        if([[arrKeys objectAtIndex:i]isEqualToString:@"emailOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                            textFieldDynamic.placeholder = @"emailOpted";
                            textFieldDynamic.text = @"emailOpted";
                            UIFont* boldFont = [UIFont boldSystemFontOfSize:[UIFont systemFontSize]];
                            [textFieldDynamic setFont:boldFont];
                        }
                }
                else
                {
                    
                    if ([[NSString stringWithFormat:@"%@",[arrfields objectAtIndex:i]] isEqualToString:@"0"])
                    {
                        
                        NSLog(@"textFieldDynamic.placeholder is place %@",textFieldDynamic.placeholder);

                        if([[arrKeys objectAtIndex:i]isEqualToString:@"smsOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                            textFieldDynamic.text = @"smsOpted";
                            textFieldDynamic.placeholder = @"smsOpted";
                        }
                        if([[arrKeys objectAtIndex:i]isEqualToString:@"emailOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                            textFieldDynamic.placeholder = @"emailOpted";
                            textFieldDynamic.text = @"emailOpted";
                        }
                    }
                    else
                    {
                        textFieldDynamic.placeholder = [NSString stringWithFormat:@"%@",[arrKeys objectAtIndex:i]];
                        NSLog(@"textFieldDynamic.placeholder is %@",textFieldDynamic.placeholder);
                        
                        textFieldDynamic.text = [NSString stringWithFormat:@"%@",[arrfields objectAtIndex:i]];
                    }
                }
            }
            
           if([textFieldDynamic.placeholder isEqualToString:@"cellPhone"] && textFieldDynamic.placeholder.length!=0)
             {
                textFieldDynamic.tag = 109;
                textFieldDynamic.keyboardType = UIKeyboardTypeNumberPad;
             }
            else if([textFieldDynamic.placeholder isEqualToString:@"emailID"]&& textFieldDynamic.placeholder.length!=0)
             {
                textFieldDynamic.tag = 101;
                textFieldDynamic.keyboardType = UIKeyboardTypeEmailAddress;
             }
            else if([textFieldDynamic.placeholder isEqualToString:@"lastName"]&& textFieldDynamic.placeholder.length!=0)
              {
                textFieldDynamic.tag = 100;
              }
            else if([textFieldDynamic.placeholder isEqualToString:@"addressState"]&& textFieldDynamic.placeholder.length!=0)
               {
                textFieldDynamic.tag = 102;
               }
            else if([textFieldDynamic.placeholder isEqualToString:@"dateOfBirth"]&& textFieldDynamic.placeholder.length!=0)
              {
                UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                male.frame = CGRectMake(0, 0, 240, 30);
                [male addTarget:self action:@selector(btnDOB_Action:) forControlEvents:UIControlEventTouchUpInside];
                male.tag =4005;
                [textFieldDynamic addSubview:male];
                textFieldDynamic.tag = 103;
              }
            else if([textFieldDynamic.placeholder isEqualToString:@"firstName"]&& textFieldDynamic.placeholder.length!=0)
               {
                textFieldDynamic.tag = 104;
               }
            else if([textFieldDynamic.placeholder isEqualToString:@"addressLine1"]&& textFieldDynamic.placeholder.length!=0)
              {
                textFieldDynamic.tag = 105;
              }
            else if([textFieldDynamic.placeholder isEqualToString:@"smsOpted"]&& textFieldDynamic.placeholder.length!=0)
              {
                UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
                male1.backgroundColor = [UIColor clearColor];
                [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
                [textFieldDynamic addSubview:male1];
                
                UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox"] forState:UIControlStateNormal];
                [male addTarget:self action:@selector(btnSMSOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
                male.tag =4001;
                [textFieldDynamic addSubview:male];
                UIFont* boldFont = [UIFont boldSystemFontOfSize:[UIFont systemFontSize]];
                [textFieldDynamic setFont:boldFont];
                
                textFieldDynamic.tag = 106;
                if (screenWidth == 375 && screenHeight == 667)
                {
                    male1.frame = CGRectMake(0, 0, 320, 40);
                    male.frame = CGRectMake(280, 10, 20, 20);
                }
              else if (screenWidth == 320 && screenHeight == 568)
                {
                    male1.frame = CGRectMake(0, 0, 300, 40);
                    male.frame = CGRectMake(220, 10, 20, 20);
                }
              else if (screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 300, 40);
                  male.frame = CGRectMake(220, 10, 20, 20);
              }
              else if (screenWidth == 414 && screenHeight == 736)
              {
                  male1.frame = CGRectMake(0, 0, 340, 40);
                  male.frame = CGRectMake(320, 10, 20, 20);
              }
            }
            
             else if([textFieldDynamic.placeholder isEqualToString:@"customerGender"]&& textFieldDynamic.placeholder.length!=0)
               {
                UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
                male1.backgroundColor = [UIColor clearColor];
                [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
                [textFieldDynamic addSubview:male1];
                
                UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                [male setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
                [male addTarget:self action:@selector(btnmale_Action:) forControlEvents:UIControlEventTouchUpInside];
                male.tag = 1001;
                male.clipsToBounds = YES;
                [male bringSubviewToFront:self.view];
                
                UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(175, 12, 50, 16)];
                
                if (screenWidth == 375 && screenHeight == 667)
                {
                    label.frame = CGRectMake(175, 12, 50, 16);
                }
                else if (screenWidth == 320 && screenHeight == 568)
                {
                    label.frame = CGRectMake(155, 12, 50, 16);
                }
                else if (screenWidth == 320 && screenHeight == 480)
                {
                    label.frame = CGRectMake(155, 12, 50, 16);
                }
                else if (screenWidth == 414 && screenHeight == 736)
                {
                    label.frame = CGRectMake(205, 12, 50, 16);
                }
                label.backgroundColor = [UIColor clearColor];
                label.textColor = [UIColor blackColor];
                label.font = [UIFont fontWithName:@"Helvetica" size:13.0];
                label.text = @"Male";
                [textFieldDynamic addSubview:label];
                
                UIButton *female = [UIButton buttonWithType:UIButtonTypeCustom];
                
                [female setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
                [female addTarget:self action:@selector(btnFemale_Action:) forControlEvents:UIControlEventTouchUpInside];
                female.clipsToBounds = YES;
                female.tag = 2001;
                [female bringSubviewToFront:self.view];
                
                if (screenWidth == 375 && screenHeight == 667)
                {
                   label = [[UILabel alloc] initWithFrame:CGRectMake(245, 12, 50, 16)];
                }
                else if (screenWidth == 320 && screenHeight == 568)
                {
                    label = [[UILabel alloc] initWithFrame:CGRectMake(225, 12, 50, 16)];
                }
                else if (screenWidth == 320 && screenHeight == 480)
                {
                    label = [[UILabel alloc] initWithFrame:CGRectMake(225, 12, 50, 16)];
                }
                else if (screenWidth == 414 && screenHeight == 736)
                {
                    label = [[UILabel alloc] initWithFrame:CGRectMake(275, 12, 50, 16)];
                }
                
                label.backgroundColor = [UIColor clearColor];
                label.font = [UIFont fontWithName:@"Helvetica" size:13.0];
                label.textColor = [UIColor blackColor];
                label.text = @"Female";
                [textFieldDynamic addSubview:label];
                
                [textFieldDynamic addSubview:male];
                [textFieldDynamic addSubview:female];
                
                    if (screenWidth == 375 && screenHeight == 667)
                    {
                        male1.frame  =  CGRectMake(0, 0, 320, 40);
                        male.frame   =  CGRectMake(150, 10, 20, 20);
                        female.frame =  CGRectMake(220, 10, 20, 20);
                    }
                    else if (screenWidth == 320 && screenHeight == 568)
                    {
                        male1.frame  =  CGRectMake(0, 0, 300, 40);
                        male.frame   =  CGRectMake(130, 10, 20, 20);
                       female.frame =  CGRectMake(200, 10, 20, 20);
                    }
                    else if (screenWidth == 320 && screenHeight == 480)
                    {
                        male1.frame  =  CGRectMake(0, 0, 300, 40);
                        male.frame   =  CGRectMake(130, 10, 20, 20);
                        female.frame =  CGRectMake(200, 10, 20, 20);
                    }
                    else if (screenWidth == 414 && screenHeight == 736)
                    {
                        male1.frame  =  CGRectMake(0, 0, 320, 40);
                        male.frame   =  CGRectMake(180, 10, 20, 20);
                        female.frame =  CGRectMake(250, 10, 20, 20);
                    }
                
                textFieldDynamic.tag = 107;
                UIFont* boldFont = [UIFont boldSystemFontOfSize:[UIFont systemFontSize]];
                [textFieldDynamic setFont:boldFont];
            }
            

          else if([textFieldDynamic.placeholder isEqualToString:@"homeStoreID"])
               {
                   
                   NSLog(@"homeStore Value not empty -------");
                  
                   //textFieldDynamic.placeholder = @"FavouritStore";
                   
                   UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
  
                   //male1.frame = CGRectMake(0, 0, 300, 30);
                 
                   UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                  // male.frame = CGRectMake(250, 10, 18, 18);
                   
                   if (screenWidth == 375 && screenHeight == 667)
                   {
                       male1.frame = CGRectMake(0, 0, 320, 40);
                       male.frame = CGRectMake(280, 10, 20, 20);
                   }
                   else if (screenWidth == 320 && screenHeight == 568)
                   {
                       male1.frame = CGRectMake(0, 0, 300, 40);
                       male.frame = CGRectMake(220, 10, 20, 20);
                   }
                   else if (screenWidth == 320 && screenHeight == 480)
                   {
                       male1.frame = CGRectMake(0, 0, 300, 40);
                       male.frame = CGRectMake(220, 10, 20, 20);
                   }
                   else if (screenWidth == 414 && screenHeight == 736)
                   {
                       male1.frame = CGRectMake(0, 0, 340, 40);
                       male.frame = CGRectMake(320, 10, 20, 20);
                   }
                   
                   male1.backgroundColor = [UIColor clearColor];
                   [male1 addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                   male1.tag =9001;
                   [textFieldDynamic addSubview:male1];
                   
                   
                   [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
                   [male addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                   [textFieldDynamic addSubview:male];
                    textFieldDynamic.tag = 108;
                   
               }
             if([textFieldDynamic.placeholder isEqualToString:@"addressZip"]&& textFieldDynamic.placeholder.length!=0)
             {
                textFieldDynamic.tag = 110;
             }
             if([textFieldDynamic.placeholder isEqualToString:@"addressCity"]&& textFieldDynamic.placeholder.length!=0)
               {
                textFieldDynamic.tag = 111;
               }
            if([textFieldDynamic.placeholder isEqualToString:@"emailOpted"]&& textFieldDynamic.placeholder.length!=0)
              {
                UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
             
                male1.backgroundColor = [UIColor clearColor];
                [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
                [textFieldDynamic addSubview:male1];
                
                textFieldDynamic.tag = 112;
                
                UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
           
                [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox"] forState:UIControlStateNormal];
                [male addTarget:self action:@selector(EmailOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
                male.tag =3001;
                [textFieldDynamic addSubview:male];
                
                if (screenWidth == 375 && screenHeight == 667)
                {
                      male1.frame = CGRectMake(0, 0, 320, 40);
                      male.frame = CGRectMake(280, 10, 20, 20);
                }
                else if (screenWidth == 320 && screenHeight == 568)
                {
                    male1.frame = CGRectMake(0, 0, 300, 40);
                    male.frame = CGRectMake(220, 10, 20, 20);
                }
                else if (screenWidth == 320 && screenHeight == 480)
                {
                    male1.frame = CGRectMake(0, 0, 300, 40);
                    male.frame = CGRectMake(220, 10, 20, 20);
                }
                else if (screenWidth == 414 && screenHeight == 736)
                {
                    male1.frame = CGRectMake(0, 0, 340, 40);
                    male.frame = CGRectMake(320, 10, 20, 20);
                }
                UIFont* boldFont = [UIFont boldSystemFontOfSize:[UIFont systemFontSize]];
                [textFieldDynamic setFont:boldFont];
            }
                
             if([arrKeys containsObject:@"password"])
            {
                textFieldDynamic.tag = 113;
            }
            else
            {
                textFieldDynamic.keyboardType = UIKeyboardTypeDefault;
            }
            
            textFieldDynamic.returnKeyType = UIKeyboardTypeDefault;
            textFieldDynamic.autocorrectionType = UITextAutocorrectionTypeNo;
            textFieldDynamic.delegate = self;
            [obj addLeftPaddingToTextField:textFieldDynamic];
            [self addCornerRadius:textFieldDynamic];
            [self.mainScrollView addSubview:textFieldDynamic];
            textFieldDynamic.autocapitalizationType = UITextAutocapitalizationTypeNone;
            y = y+40+10;
            
        }
    }
    
    if([dic valueForKey:@"customerGender"] != (NSString *)[NSNull null])
     {
    if([[dic valueForKey:@"customerGender"]isEqualToString:@"Male"])
      {
        [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
        [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
        UITextField * gender = (UITextField *)[self.view viewWithTag:107];
        gender.text = @"Gender";
        genderSelection = @"Male";
      }
     else
      {
        [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
        [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
        UITextField * gender = (UITextField *)[self.view viewWithTag:107];
        gender.text = @"Gender";
        genderSelection = @"Female";
      }
    }
    
    if([[dic valueForKey:@"smsOpted"]boolValue]==1)
    {
        [(UIButton *)[self.view viewWithTag:4001]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:106];
       emailOpt.text = @"smsOpted";
        smsSelection = @"true";
        isCheckSms = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:4001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:106];
        emailOpt.text = @"smsOpted";
        smsSelection = @"false";
        isCheckSms = NO;
    }
    
    
    if([[dic valueForKey:@"emailOpted"]boolValue]==1)
    {
        [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailOpt.text = @"emailOpted";
        emailSelect = @"true";
        isCheckEmail = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailOpt.text = @"emailOpted";
        emailSelect = @"false";
        isCheckEmail = NO;

    }
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenWidth = screenRect.size.width;
    CGFloat screenHeight = screenRect.size.height;
    
    
    if (screenWidth == 320 && screenHeight == 480)
    {
        self.changePasswordBtn.frame = CGRectMake(self.changePasswordBtn.frame.origin.x,40*arrfields.count+230,  self.changePasswordBtn.frame.size.width,  self.changePasswordBtn.frame.size.height);
        
        self.saveBtn.frame = CGRectMake(self.saveBtn.frame.origin.x, self.changePasswordBtn.frame.origin.y+ self.changePasswordBtn.frame.size.height+10, self.saveBtn.frame.size.width, self.saveBtn.frame.size.height);
        
        
        self.mainScrollView.contentSize = CGSizeMake(self.mainScrollView.frame.size.width, 40*arrfields.count+310);
    }
    
    
    else  if (screenWidth == 320 && screenHeight == 568)
    {
        self.changePasswordBtn.frame = CGRectMake(self.changePasswordBtn.frame.origin.x,40*arrfields.count+115,  self.changePasswordBtn.frame.size.width,  self.changePasswordBtn.frame.size.height);
        
        self.saveBtn.frame = CGRectMake(self.saveBtn.frame.origin.x, self.changePasswordBtn.frame.origin.y+ self.changePasswordBtn.frame.size.height+10, self.saveBtn.frame.size.width, self.saveBtn.frame.size.height);
        
        
        self.mainScrollView.contentSize = CGSizeMake(self.mainScrollView.frame.size.width, 40*arrfields.count+314);
    }
    
    else if (screenWidth == 375 && screenHeight == 667)
    {
        if(arrfields.count>10)
        {
        self.changePasswordBtn.frame = CGRectMake(self.changePasswordBtn.frame.origin.x,40*arrfields.count+20,  self.changePasswordBtn.frame.size.width,  self.changePasswordBtn.frame.size.height);
        
        self.saveBtn.frame = CGRectMake(self.saveBtn.frame.origin.x, self.changePasswordBtn.frame.origin.y+ self.changePasswordBtn.frame.size.height+5, self.saveBtn.frame.size.width, self.saveBtn.frame.size.height);
        }
        else
        {
            self.changePasswordBtn.frame = CGRectMake(self.changePasswordBtn.frame.origin.x,40*arrfields.count+45,  self.changePasswordBtn.frame.size.width,  self.changePasswordBtn.frame.size.height);
            
            self.saveBtn.frame = CGRectMake(self.saveBtn.frame.origin.x, self.changePasswordBtn.frame.origin.y+ self.changePasswordBtn.frame.size.height+5, self.saveBtn.frame.size.width, self.saveBtn.frame.size.height);
        }
        
        self.mainScrollView.contentSize = CGSizeMake(self.mainScrollView.frame.size.width, 40*arrfields.count+320);
    }
    
    else if (screenWidth == 414 && screenHeight == 736)
    {
        
        if(arrfields.count>11)
        {
            self.changePasswordBtn.frame = CGRectMake(self.changePasswordBtn.frame.origin.x,40*arrfields.count-18,  self.changePasswordBtn.frame.size.width,  self.changePasswordBtn.frame.size.height);
            
            self.saveBtn.frame = CGRectMake(self.saveBtn.frame.origin.x, self.changePasswordBtn.frame.origin.y+ self.changePasswordBtn.frame.size.height, self.saveBtn.frame.size.width, self.saveBtn.frame.size.height);
        }
        else
        {
            self.changePasswordBtn.frame = CGRectMake(self.changePasswordBtn.frame.origin.x,40*arrfields.count+15,  self.changePasswordBtn.frame.size.width,  self.changePasswordBtn.frame.size.height);
            
            self.saveBtn.frame = CGRectMake(self.saveBtn.frame.origin.x, self.changePasswordBtn.frame.origin.y+ self.changePasswordBtn.frame.size.height+5, self.saveBtn.frame.size.width, self.saveBtn.frame.size.height);
        }
     
        self.mainScrollView.contentSize = CGSizeMake(self.mainScrollView.frame.size.width, 40*arrfields.count+342);
    }
    
    
    
    UITextField * phoneNumber = (UITextField *)[self.view viewWithTag:109];
    UIToolbar *keyboardDoneButtonView = [[UIToolbar alloc] init];
    [keyboardDoneButtonView sizeToFit];
    UIBarButtonItem *flexBarButton = [[UIBarButtonItem alloc]
                                      initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                      target:nil action:nil];
    
    UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"style:UIBarButtonItemStylePlain target:self
                                                                  action:@selector(doneClicked:)];
    
    keyboardDoneButtonView.items = @[flexBarButton, doneButton];
    phoneNumber.inputAccessoryView = keyboardDoneButtonView;
    

    
    NSInteger  str1 = [[dic valueForKey:@"homeStoreID"] integerValue];
    
    if(str1  == 0)
    {
        NSLog(@"if block store==%ld",(long)str1);
        [self favouritStoreName:str1];
    }
    else
    {
        NSLog(@"else block store==%ld",(long)str1);
        [self favouritStoreName:str1];
    }
    
}


// favouritStore code
-(void)favouritStoreName:(NSInteger )str
{
    NSArray * arrStoreNumber =  [[NSUserDefaults standardUserDefaults]valueForKey:@"storesKey"];
    
    NSArray * str1 = [arrStoreNumber filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(storeID == %ld)",(long)str]];

    if(str1.count!=0)
    {
    NSString *strFavStore =  [[str1 objectAtIndex:0]valueForKey:@"storeName"];
    NSLog(@"SDK demo store name=%@",[[str1 objectAtIndex:0]valueForKey:@"storeName"]);
    UITextField  *favouritStore11 = (UITextField *)[self.mainScrollView viewWithTag:108];
    favouritStore11.text = strFavStore;
    }

}


// favourit store button action

-(void)FavouritBtn_Action:(id)sender
{
    
    [self.datePickerView setHidden:YES];
    
    
    StoreSearchView * obj1 = [[StoreSearchView alloc]initWithNibName:@"StoreSearchView" bundle:nil];
    obj1.delegate = self;
    [self.navigationController pushViewController:obj1 animated:YES];
    
    
    [self resignKeyboard];
}

-(void)StoreSearch:(NSString *)storeNumber and:(NSString *)StoreName
{
    storeNameValue = StoreName;
    UITextField * favouritStore = (UITextField *)[self.view viewWithTag:108];
    favouritStore.text = storeNameValue;
    
    storeNumberValue = storeNumber;
    
    [[NSUserDefaults standardUserDefaults]setValue:storeNumberValue forKey:@"storeNumberValue"];
    
    if([storeNumberValue intValue]==0)
    {
        storeNumberString = storeNumber;
        
    [[NSUserDefaults standardUserDefaults]setValue:storeNumberString forKey:@"storeNumberString"];

        NSLog(@" StoreNumber--------%@",storeNumber);
    }
    else
    {
        NSLog(@" StoreNumber--------%d",[storeNumberValue intValue]);
    }
    
    NSLog(@"storeNane And StoreNumber--------%@ %@",storeNameValue,storeNumberValue);
    
}



// resign button action
-(void)btnresign_Action:(id)sender
{
    [self resignKeyboard];
    [self.datePickerView setHidden:YES];
}



// male button action
-(void)btnmale_Action:(id)sender
{
    [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
    [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
     UITextField * gender = (UITextField *)[self.view viewWithTag:107];
    gender.text = @"Gender";
    genderSelection = @"Male";
     [self resignKeyboard];
}


// female button action
-(void)btnFemale_Action:(id)sender
{
    [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
    [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
     UITextField * gender = (UITextField *)[self.view viewWithTag:107];
    gender.text = @"Gender";
    genderSelection = @"Female";
    
    [self resignKeyboard];
}

// Dob  resign keyboard button action
-(void)btnDOB_Action:(id)sender
{
    [self resignKeyboard];
    [self.datePickerView setHidden:NO];
}


// email button action
-(void)EmailOptIn_Action:(id)sender
{
    if(isCheckEmail == NO)
    {
        [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailOpt.text = @"emailOptIn";
        emailSelect = @"true";
        isCheckEmail = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailOpt.text = @"emailOptIn";
        emailSelect = @"false";
        isCheckEmail = NO;

    }
     [self resignKeyboard];
}

// sms button action
-(void)btnSMSOptIn_Action:(id)sender
{
    if(isCheckSms == NO)
    {
        [(UIButton *)[self.view viewWithTag:4001]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * smsOption = (UITextField *)[self.view viewWithTag:106];
        smsOption.text = @"smsOptIn";
        smsSelection = @"true";
        isCheckSms = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:4001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * smsOption = (UITextField *)[self.view viewWithTag:106];
        smsOption.text = @"smsOptIn";
        smsSelection = @"false";
        isCheckSms = NO;

        
    }
    // keyboard resign method call
    [self resignKeyboard];
}




// numeric key pad next button action

- (IBAction)doneClicked:(id)sender
{
    NSLog(@"Done Clicked.");
    [self.view endEditing:YES];
 
}


// text field corner radius

-(void)addCornerRadius:(UITextField*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 0.5;
    textField.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
    
}

// view corner radius
-(void)addCornerRadiusView:(UIView*)view
{
    view.layer.masksToBounds = YES;
    view.layer.cornerRadius = 2.0;
    view.layer.borderWidth = 0.5;
    view.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

#pragma mark - logout button action

- (IBAction)logOutBtn_Action:(id)sender
{
    
//    if([obj checkNetworkConnection])
//    {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert!"
                                      message:@"Do you want to Logout."
                                      preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
        {
                //[self logOutApi];
            [self.delegate logOutProfileAction];
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"login"];
            
        }];
        
        [alert addAction:cancel1];
        UIAlertAction* noButton = [UIAlertAction
                                   actionWithTitle:@"NO"
                                   style:UIAlertActionStyleDefault
                                   handler:^(UIAlertAction * action)
                                   {
                                       //Handel your yes please button action here
                                       [alert dismissViewControllerAnimated:YES completion:nil];
                                       
                                   }];
        [alert addAction:noButton];
        
        [self presentViewController:alert animated:YES completion:nil];
//    }
//    
//    else
//    {
//        [self alertViewDelegate:@"Please check your network connection"];
//        
//    }
  
}

// log out api
-(void)logOutApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSMutableDictionary * dict = [NSMutableDictionary new];
    
    [dict setValue:@"Application" forKey:@"mobile"];
    
    [apiCall logoutAPI:dict url:@"/member/logout" withTarget:self withSelector:@selector(logout:)];
    apiCall = nil;
}

-(void)logout:(id)response
{
    [obj removeLoadingView:self.view];
    if(response!=nil)
    {
        if([[response valueForKey:@"successFlag"]integerValue]==1)
        {
            [self.delegate logOutProfileAction];
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"login"];

            //[self alertViewDelegateIndex:[response valueForKey:@"message"]];
            //[[NSUserDefaults standardUserDefaults]removeObjectForKey:@"mobileSetting"];
        }
        else
        {
            [self alertViewDelegate:[response valueForKey:@"message"]];
            
        }
        NSLog(@"response------%@", response);
    }
    else
    {
        [self alertViewDelegate:@"Internal Server error. Please Contact Administrator"];
    }
    
}


// alert view method

-(void)alertViewDelegateIndex:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                                                              [self.delegate logOutProfileAction];
                                                          }];
    [alertController addAction:defaultAction];
    [self presentViewController:alertController animated:YES completion:nil];
}




# pragma mark - back button action method

- (IBAction)updateProfileback_Action:(id)sender
{
    [self.delegate updateProfileBackAction];
}


# pragma mark - edit profile method Action

- (IBAction)editBtn_Action:(id)sender
{
   
        [self.editBtn setTitle:@"Save" forState:UIControlStateNormal];
        
         [(UITextField *)[self.view viewWithTag:100]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:101]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:102]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:103]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:104]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:105]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:106]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:107]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:108]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:109]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:110]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:111]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:112]setUserInteractionEnabled:YES];
         [(UITextField *)[self.view viewWithTag:113]setUserInteractionEnabled:YES];
         [(UIButton *)[self.view viewWithTag:1001]setUserInteractionEnabled:YES];
         [(UIButton *)[self.view viewWithTag:2001]setUserInteractionEnabled:YES];
         [(UIButton *)[self.view viewWithTag:3001]setUserInteractionEnabled:YES];
         [(UIButton *)[self.view viewWithTag:4001]setUserInteractionEnabled:YES];
        
    
        if([obj checkNetworkConnection])
        {
            // call signIN Api
            [self updateProfileApi];
        }
        
        else
        {
            [self alertViewDelegate:@"Please check your network connection"];
            
        }

}


-(void)updateProfileApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    
    UITextField * lastname = (UITextField *)[self.view viewWithTag:100];
    UITextField * email  =  (UITextField *)[self.view viewWithTag:101];
    UITextField * state  =  (UITextField *)[self.view viewWithTag:102];
    UITextField * dob  =  (UITextField *)[self.view viewWithTag:103];
    UITextField * firstName = (UITextField *)[self.view viewWithTag:104];
    UITextField * address = (UITextField *)[self.view viewWithTag:105];
   // UITextField * smsOption = (UITextField *)[self.view viewWithTag:106];
  // UITextField * gender = (UITextField *)[self.view viewWithTag:107];
    UITextField * favouritStore = (UITextField *)[self.view viewWithTag:108];
    UITextField * phoneNumber = (UITextField *)[self.view viewWithTag:109];
    UITextField * zipCode = (UITextField *)[self.view viewWithTag:110];
    UITextField * city = (UITextField *)[self.view viewWithTag:111];
   // UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
  //UITextField * password = (UITextField *)[self.view viewWithTag:113];
    
    
  
    [dict setValue:address.text forKey:@"addressStreet"];//addressStreet

    [dict setValue:lastname.text forKey:@"lastName"];//lastName
    
    [dict setValue:email.text forKey:@"email"];
    
    [dict setValue:dob.text forKey:@"dob"];//dob
    
    [dict setValue:state.text forKey:@"addressState"];//addressState
 
    [dict setValue:smsSelection forKey:@"smsOptIn"];//smsOptIn
    
    [dict setValue:genderSelection forKey:@"gender"];//gender
    
    [dict setValue:emailSelect forKey:@"emailOptIn"];//emailOptIn
    
    [dict setValue:favouritStore.text forKey:@"favoriteStore"];//favoriteStore
  
    [dict setValue:firstName.text forKey:@"firstName"];//firstName
    
    NSLog(@"firstName value------%@",firstName.text);
    
    [dict setValue:city.text forKey:@"addressCity"];//addressCity
    
    [dict setValue:phoneNumber.text forKey:@"phone"];
    
    [dict setValue:zipCode.text forKey:@"addressZipCode"];//addressZipCode
    
    NSString * strNumber = [[NSUserDefaults standardUserDefaults]valueForKey:@"storeNumberValue"];
    
    if(strNumber.length!=0)
    {
        if([strNumber intValue]==0)
        {
            NSString * strNumerString = [[NSUserDefaults standardUserDefaults]valueForKey:@"storeNumberString"];
            
            [dict setValue:strNumerString forKey:@"storeCode"];
            [dict setValue:strNumerString forKey:@"favoriteStore"];

            
        }
        else
        {
            
             [dict setValue:[NSString stringWithFormat:@"%d",[strNumber intValue]] forKey:@"storeCode"];
             [dict setValue:[NSString stringWithFormat:@"%d",[strNumber intValue]] forKey:@"favoriteStore"];
        }
    }
    else
    {
        [dict setValue:@"474" forKey:@"storeCode"];
        [dict setValue:@"474" forKey:@"favoriteStore"];
    }
    
    
    
    NSLog(@"dic print is %@",dict.description);
    
    [apiCall updateProfile:dict url:@"/member/update" withTarget:self withSelector:@selector(updateProfile:)];
    dict = nil;
    apiCall = nil;
}


// update profile api
-(void)updateProfile:(id)response
{
    //[obj removeLoadingView:self.view];
    
    if(response != nil)
    {
        NSLog(@"update response------%@",response);
        
        if([[response valueForKey:@"successFlag"]integerValue]==1)
        {
            //[self.delegate updateProfileAction];
     // [[NSNotificationCenter defaultCenter] postNotificationName:@"pushNotification" object:nil];
            
            [self getMemberApi];
            
           
          
        }
        else
        {
            [obj removeLoadingView:self.view];
            [self alertViewDelegate:[response valueForKey:@"message"]];
        }
    }
}



- (void)getMemberApi
{
    //[obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall getMember:nil url:@"/member/getMember" withTarget:self withSelector:@selector(getMember:)];
    apiCall = nil;
}

// api response

-(void)getMember:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"member response value------%@",response);
    if(response != nil)
    {
        NSUserDefaults *def = [NSUserDefaults standardUserDefaults];
        [def setObject:[NSKeyedArchiver archivedDataWithRootObject:response] forKey:@"MyData"];
        [def synchronize];
        
        [self alertViewDelegateIndex1:@"Member updated Successfully"];

    }
}



# pragma mark - alert view with delegate

-(void)alertViewDelegateIndex1:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                                                        [self.delegate updateProfileBackAction];
                                                          }];
    
    
    [alertController addAction:defaultAction];
    [self presentViewController:alertController animated:YES completion:nil];
}


# pragma mark - alert view method

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:ok];
    
    [self presentViewController:alertController animated:YES completion:nil];
}


// change Password button
- (IBAction)changePasswordBtn_Action:(id)sender
{
    [self.delegate changePasswordAction];
}


# pragma mark - TextField Delegate Method

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.datePickerView setHidden:YES];
    
    if(textField.tag == 100)
    {
        // [(UITextField*)[self.view viewWithTag:101]becomeFirstResponder];
    }
    
    //    else if (textField.tag == 101)
    //    {
    //        //[(UITextField*)[self.view viewWithTag:102]becomeFirstResponder];
    //    }
    //    else if (textField.tag == 102)
    //    {
    //       // [(UITextField*)[self.view viewWithTag:103]becomeFirstResponder];
    //    }
    //    else if (textField.tag == 103)
    //    {
    //
    //        // [(UITextField*)[self.view viewWithTag:104]becomeFirstResponder];
    //    }
    //    else if (textField.tag == 104)
    //    {
    //        //[(UITextField*)[self.view viewWithTag:105]becomeFirstResponder];
    //    }
    //    else if (textField.tag == 105)
    //    {
    //       // [(UITextField*)[self.view viewWithTag:106]resignFirstResponder];
    //
    //    }
    //    else if (textField.tag == 106)
    //    {
    //        [textField resignFirstResponder];
    //
    //        //[(UITextField*)[self.view viewWithTag:107]becomeFirstResponder];
    //    }
    //    else if (textField.tag == 107)
    //    {
    //        [textField resignFirstResponder];
    //
    //        //[(UITextField*)[self.view viewWithTag:108]becomeFirstResponder];
    //    }
    //    else if (textField.tag == 108)
    //    {
    //        //[(UITextField*)[self.view viewWithTag:109]becomeFirstResponder];
    //    }
    //    else if (textField.tag == 109)
    //    {
    //       // [(UITextField*)[self.view viewWithTag:110]becomeFirstResponder];
    //
    //    }
    //    else if (textField.tag == 110)
    //    {
    //        //[(UITextField*)[self.view viewWithTag:111]becomeFirstResponder];
    //    }
    
    else if (textField.tag == 111)
    {
        [(UITextField*)[self.view viewWithTag:111]resignFirstResponder];
    }
    else if (textField.tag == 112)
    {
        //[textField resignFirstResponder];
        
        [(UITextField*)[self.view viewWithTag:112]resignFirstResponder];
    }
    else if (textField.tag == 113)
    {
        //[textField resignFirstResponder];
        
        [(UITextField*)[self.view viewWithTag:113]resignFirstResponder];
    }
    //    else
    //    {
    //        [textField resignFirstResponder];
    //    }
    
    [textField resignFirstResponder];
    return YES;
}



// textfield method
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    NSLog(@"textfield tag-------%ld",(long)textField.tag);
    
    UITextField * phoneNumber = (UITextField *)[self.view viewWithTag:109];
    
    if(textField.tag == 109)
    {
        int length = (int)[self getLength:textField.text];
        
        if(length == 10)
        {
            if(range.length == 0)
                
                return NO;
        }
        
        if(length == 3)
        {
            NSString *num = [self formatNumber:textField.text];
            phoneNumber.text = [NSString stringWithFormat:@"%@-",num];
            
            if(range.length > 0)
                phoneNumber.text = [NSString stringWithFormat:@"%@",[num substringToIndex:3]];
        }
        else if(length == 6)
        {
            NSString *num = [self formatNumber:textField.text];
            
            phoneNumber.text = [NSString stringWithFormat:@"%@-%@-",[num  substringToIndex:3],[num substringFromIndex:3]];
            
            if(range.length > 0)
                phoneNumber.text = [NSString stringWithFormat:@"%@-%@",[num substringToIndex:3],[num substringFromIndex:3]];
        }
    }
    
    return YES;
}

- (NSString *)formatNumber:(NSString *)mobileNumber
{
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"(" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@")" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@" " withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"-" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"+" withString:@""];
    
    int length = (int)[mobileNumber length];
    if(length > 10)
    {
        mobileNumber = [mobileNumber substringFromIndex: length-10];
    }
    return mobileNumber;
}

- (int)getLength:(NSString *)mobileNumber
{
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"(" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@")" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@" " withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"-" withString:@""];
    mobileNumber = [mobileNumber stringByReplacingOccurrencesOfString:@"+" withString:@""];
    
    int length = (int)[mobileNumber length];
    return length;
}




-(void)textFieldDidBeginEditing:(UITextField *)textField {
    
    
    if ([textField.placeholder isEqualToString:@"dateOfBirth"] || textField.tag ==103)
    {
        
        [textField resignFirstResponder];
        [self.datePickerView setHidden:NO];
       
        
    }
    else if ([textField.placeholder isEqualToString:@"customerGender"] || textField.tag ==107)
    {
        [textField resignFirstResponder];
        [self.datePickerView setHidden:YES];
       
        
        
    }
    else if ([textField.placeholder isEqualToString:@"emailOpted"] || textField.tag ==112)
    {
        
        [textField resignFirstResponder];
        [self.datePickerView setHidden:YES];
        
    }
    else if ([textField.placeholder isEqualToString:@"smsOpted"] || textField.tag ==106)
    {
     
        [self resignKeyboard];
        [self.datePickerView setHidden:YES];
    }
    else
    {
        [self.datePickerView setHidden:YES];
    }
}


# pragma mark - keyboard resigning

-(void)resignKeyboard
{
    [(UITextField *)[self.view viewWithTag:100]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:101]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:102]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:104]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:105]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:106]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:103]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:108]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:109]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:110]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:111]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:112]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:113]resignFirstResponder];
}


#pragma mark - date picker action method

- (IBAction)datePicker_Action:(id)sender
{
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"yyyy-MM-dd"];
    NSString *date = [dateFormat stringFromDate:self.datePicker.date];
    NSDate *todayDate = [NSDate date];
    [self.datePicker setMaximumDate: todayDate];
    UITextField * dob  =  (UITextField *)[self.view viewWithTag:103];
    dob.text = [NSString stringWithFormat:@"%@",date];
}

- (IBAction)doneBtn_Action:(id)sender
{
    [self.datePickerView setHidden:YES];
}


@end
