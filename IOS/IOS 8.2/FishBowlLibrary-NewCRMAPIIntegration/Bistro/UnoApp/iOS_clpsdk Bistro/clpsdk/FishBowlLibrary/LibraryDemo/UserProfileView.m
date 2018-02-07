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
#import "SideMenuObject.h"
#import "MenuViewController.h"
#import "LoyalityView.h"
#import "RewardsAndOfferView.h"
#import "HomeViewController.h"
#import "ChangePasswordView.h"
#import "BottomView.h"
#import <CoreLocation/CoreLocation.h>
#import "StorelocatorViewController.h"
#import "StateSearchView.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "clpsdk.h"
#import "FAQViewViewController.h"
#import "ContactUSView.h"
#import "ThemeFieldType.h"




@interface UserProfileView ()<UITextFieldDelegate,StoreSearch,PushNavigation,stateSearch,ThemeFieldDelegate>
{
    
    ModelClass       * obj;
    ApiClasses       * apiCall;
    UITextField      * textFieldDynamic;
    BOOL               isEdit;
    BOOL               isCheckEmail;
    BOOL               isCheckSms;
    BOOL               isCheckPush;
    NSString         * genderSelection;
    NSString         * emailSelect;
    NSString         * smsSelection;
    NSString         * pushSelection;
    NSDictionary     * dic;
    NSString         * storeNameValue;
    NSString         * storeNumberValue;
    NSString         * storeNumberString;
    SideMenuObject   * sideObject;
    UIViewController * myController;
    clpsdk           * clpObj;
    NSMutableArray * arrKeys;
    NSMutableArray * arrDynamicFields;
    NSMutableArray *arrDisplayField;
    ThemeFieldType    * themeFieldType;
    UIDatePicker        *datePicker;
    NSArray * resultArray;
    NSMutableDictionary *dictCheck;



}

@property (unsafe_unretained, nonatomic) IBOutlet UIButton * sendBtn;
@property (weak, nonatomic) IBOutlet UIImageView           * headerImage;
@property (weak, nonatomic) IBOutlet UIScrollView          * mainScrollView;
@property (weak, nonatomic) IBOutlet UIButton              * editBtn;
@property (weak, nonatomic) IBOutlet UIImageView           * companyImageLogo;
@property (weak, nonatomic) IBOutlet UIButton              * changePasswordBtn;
@property (weak, nonatomic) IBOutlet UIButton              * saveBtn;
@property (weak, nonatomic) IBOutlet UILabel               * updateProfile;
@property (weak, nonatomic) IBOutlet UIView                * myProfileView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *viewScrolTopConstraint;


@end

@implementation UserProfileView

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    obj=[ModelClass sharedManager];
    sideObject = [[SideMenuObject alloc]init];
    arrKeys = [NSMutableArray new];
    arrDynamicFields =  [NSMutableArray new];
    arrDisplayField = [NSMutableArray new];
    themeFieldType = [[ThemeFieldType alloc]init];
    themeFieldType.themeFieldDelegate = self;
    dictCheck = [[NSMutableDictionary alloc]init];
    

    
    if([[NSUserDefaults standardUserDefaults]boolForKey:@"changePassword"] == YES)
    {
        [self.changePasswordBtn setHidden:YES];
    }
    else
    {
        [self.changePasswordBtn setHidden:NO];
    }

    [self shadoOffect:self.myProfileView];
  
    [self getMemberApiCall];
    
}



-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
}


// profile

-(void)profileRecord
{
    // fetch profile data
    
    NSMutableArray *arr2;
    NSMutableArray *arrDatabaseName;
    

    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSLog(@"Anarchieve start");
    NSLog(@" theme setting is %@", [currentDefaults objectForKey:@"themeSetting"]);
    NSData *dataThemeSetting  = [currentDefaults objectForKey:@"themeSetting"];
    NSLog(@"data is %@",dataThemeSetting.description);
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:dataThemeSetting];
    
    NSLog(@"themeSetting is %@",dict.description);
    NSLog(@"str value------%@",[[dict valueForKey:@"themeDetails"] valueForKey:@"profilefield"]);
    
    arr2 = [NSMutableArray new];
    
    arr2 = [[dict valueForKey:@"themeDetails"] valueForKey:@"profilefield"];
    
    arrDatabaseName = [NSMutableArray new];
    
    
    
    NSSortDescriptor* sortOrder = [NSSortDescriptor sortDescriptorWithKey:@"configDisplaySeq" ascending: YES];
    
    
    resultArray  = [arr2 sortedArrayUsingDescriptors:[NSArray arrayWithObject: sortOrder]];
    
    
    NSMutableArray *arrDisplay = [NSMutableArray new];
    
    
    for (int i= 0 ; i<[resultArray count]; i++)
    {
        if ( [[[resultArray objectAtIndex:i] valueForKey:@"pageName"] isEqualToString:@"Registration"])
        {
            
            arr2 = [[resultArray objectAtIndex:i] valueForKey:@"fields"];
            [arrDisplay addObjectsFromArray:arr2];
            NSLog(@"arrDisplay.count is %lu",(unsigned long)arrDisplay.count);

            
            for (int p = 0; p<arr2.count; p++) {
                
            if ([[[arr2 objectAtIndex:p] valueForKey:@"displayName"] isEqualToString:@"Password"] || [[[arr2 objectAtIndex:p] valueForKey:@"displayName"] isEqualToString:@"Bonus"])
            {
//                [arrDisplay removeObjectAtIndex:p];
                [arrDatabaseName addObject:@"0"];
                
                NSPredicate *PassPredicate = [NSPredicate predicateWithFormat:@"SELF.displayName contains[cd] %@",@"Password"];
                NSPredicate *BonusPredicate = [NSPredicate predicateWithFormat:@"SELF.displayName contains[cd] %@",@"Bonus"];
                NSArray *subPredicates = [NSArray arrayWithObjects:PassPredicate, BonusPredicate, nil];
                NSPredicate *orPredicate = [NSCompoundPredicate orPredicateWithSubpredicates:subPredicates];
                
                NSArray *arr = [arr2 filteredArrayUsingPredicate:orPredicate];
                NSLog(@"HERE %@",arr);
                
                [arrDisplay removeObjectsInArray:arr];

            }
                else
                {
                    NSLog(@"Do nothing");
                    NSString *strDatabaseName =  [[arr2 objectAtIndex:p] valueForKey:@"databaseName"];
                    if(strDatabaseName != (NSString *)[NSNull null])
                    {
                        [arrDatabaseName addObject:[strDatabaseName lowercaseString]];
                    }
                    else{
                        [arrDatabaseName addObject:@"0"];

                    }
                    
                    
                }
                
                [arrKeys addObject:[[arr2 objectAtIndex:p]valueForKey:@"name"]];
                [arrDisplayField addObject:[[arr2 objectAtIndex:p]valueForKey:@"displayName"]];
                
            }
            
            
            
            if([arrDisplayField containsObject:@"Password"])
            {
                [arrDisplayField removeObject:@"Password"];
            }
            if([arrDisplayField containsObject:@"Bonus"])
            {
                [arrDisplayField removeObject:@"Bonus"];
            }
        }
    }
    
    NSLog(@"arrDisplay.count is %@",arrDisplay.description);

    
    arr2 = arrDisplay;
    
    sortOrder = [NSSortDescriptor sortDescriptorWithKey:@"displaySeq" ascending: YES];
    
    resultArray  = [arr2 sortedArrayUsingDescriptors:[NSArray arrayWithObject: sortOrder]];
    
    NSLog(@"array field values ---------- %@",resultArray);
    NSLog(@"arrDatabaseName values ---------- %@",arrDatabaseName);


    
    
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
    

    NSMutableArray * arrfields = [NSMutableArray new];
    
    
//    NSLog(@"array is %@", [[NSUserDefaults standardUserDefaults] valueForKey:@"FieldArray"]);
    
    
//    [arrKeys addObjectsFromArray:[[NSUserDefaults standardUserDefaults] valueForKey:@"FieldArray"]];
    
    NSLog(@"arrKeys before is %@",arrKeys.description);
    
    if (arrKeys.count == 0)
    {
       
        if(resultArray.count!=0)
        {
            for(int i= 0 ; i<[resultArray count]; i++)
            {
               
                [arrKeys addObject:[[resultArray objectAtIndex:i]valueForKey:@"name"]];
                [arrDisplayField addObject:[[resultArray objectAtIndex:i]valueForKey:@"displayName"]];
            }
        }
        
        [currentDefaults setValue:arrKeys forKey:@"FieldArray"];
        
    }
    
    NSLog(@"arrKeys After is %@",arrKeys.description);

    
    int customFields =1;
    
    for (int i =0 ; i < arrKeys.count; i++) {
        
        NSString *strKey = [arrKeys objectAtIndex:i];
        if ([strKey isEqualToString:@"FirstName"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"firstName"];
        }
        else if ([strKey isEqualToString:@"LastName"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"lastName"];

            
        }
        else if ([strKey isEqualToString:@"EmailAddress"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"emailID"];

            
        }
        else if ([strKey isEqualToString:@"PhoneNumber"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"cellPhone"];
            
           
            
        }
        else if ([strKey isEqualToString:@"EmailOptIn"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"emailOpted"];

            
        }
        else if ([strKey isEqualToString:@"SMSOptIn"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"smsOpted"];

            
        }
        else if ([strKey isEqualToString:@"pushOptIn"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"pushOpted"];

            
        }
        
        else if ([strKey isEqualToString:@"Password"])
        {
            
        }
        else if ([strKey isEqualToString:@"Bonus"])
        {
            
        }
        else if ([strKey isEqualToString:@"FavoriteStore"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"homeStoreID"];

            
        }
        else if ([strKey isEqualToString:@"City"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"addressCity"];

            
        }
        else if ([strKey isEqualToString:@"ZipCode"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"addressZip"];
        

            
        }
        else if ([strKey isEqualToString:@"Address"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"addressCity"];

            
        }
        else if ([strKey isEqualToString:@"DOB"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"dateOfBirth"];

            
        }
        else if ([strKey isEqualToString:@"State"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"addressState"];

            
        }
        else if ([strKey isEqualToString:@"Country"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"addressCountry"];

            
        }
        else if ([strKey isEqualToString:@"Gender"])
        {
            [arrKeys replaceObjectAtIndex:i withObject:@"customerGender"];

            
        }
        else
        {
            
            [arrKeys replaceObjectAtIndex:i withObject:[arrDatabaseName objectAtIndex:i]];

        }
        
    }
    
    customFields = 1;
    
    
    [arrKeys removeObject:@"Password"];
    [arrKeys removeObject:@"Bonus"];
    
    NSLog(@"arrkey after change %@",arrKeys.description);
    
    
    for (int p = 0; p<arrKeys.count; p++) {
        
        
//        if([dic valueForKey:[arrKeys objectAtIndex:p]]!= ( NSString *)[NSNull null])
//        {
            NSLog(@"[arrKeys objectAtIndex:p] is %@",[arrKeys objectAtIndex:p]);
            if([[NSString stringWithFormat:@"%@",[arrKeys objectAtIndex:p]] containsString:@"Custom"])
            {
                if([dic objectForKey:[[arrKeys objectAtIndex:p] lowercaseString]] != (NSString *)[NSNull null])
                {
                 [arrfields addObject:[dic objectForKey:[[arrKeys objectAtIndex:p] lowercaseString]]];
                }
                else{
                    [arrfields addObject:@""];
                }
            }
            else
            {
                if([dic objectForKey:[arrKeys objectAtIndex:p]] != (NSString *)[NSNull null])
                {
                [arrfields addObject:[dic objectForKey:[arrKeys objectAtIndex:p]]];
                }
                else
                {
                    [arrfields addObject:@""];

                }
            }
            
            
//        }

    }
    
    NSLog(@"arrfields and all values------%@",arrfields);
    NSLog(@"arrKeys and all values------%@",arrKeys);
    

   // self.mainScrollView.backgroundColor = [UIColor clearColor];
    
    int y=10 ;
    
    self.mainScrollView.contentSize = CGSizeMake(self.mainScrollView.frame.size.width, 40*arrfields.count+120+117);
    
    if(arrKeys.count>0)
    {
        for(int i = 0; i<[arrfields count]; i++)
        {
            CGRect screenRect = [[UIScreen mainScreen] bounds];
            CGFloat screenWidth = screenRect.size.width;
            CGFloat screenHeight = screenRect.size.height;
            UIImageView * imageLine;
            UILabel     * textLabel;
            
            if(screenWidth == 320 && screenHeight == 480)
            {
                textFieldDynamic  = [[UITextField alloc] initWithFrame:CGRectMake(18, y+22, 276, 30)];
                textLabel  = [[UILabel alloc] initWithFrame:CGRectMake(33, y, 260, 21)];
                imageLine  = [[UIImageView alloc] initWithFrame:CGRectMake(30, y+31+22, 261,1)];
            }
            else if (screenWidth == 320 && screenHeight == 568)
            {
                textFieldDynamic  = [[UITextField alloc] initWithFrame:CGRectMake(18, y+22, 276, 30)];
                textLabel  = [[UILabel alloc] initWithFrame:CGRectMake(33, y, 260, 21)];
                imageLine  = [[UIImageView alloc] initWithFrame:CGRectMake(30, y+31+22, 261,1)];
            }
            else if (screenWidth == 375 && screenHeight == 667)
            {
                textFieldDynamic  = [[UITextField alloc] initWithFrame:CGRectMake(22, y+23, 321, 36)];
                textLabel  = [[UILabel alloc] initWithFrame:CGRectMake(38, y, 305, 21)];
                imageLine  = [[UIImageView alloc] initWithFrame:CGRectMake(35, y+36+22, 307,1)];
            }
            else if (screenWidth == 414 && screenHeight == 736)
            {
                textFieldDynamic  = [[UITextField alloc] initWithFrame:CGRectMake(37, y+22, 333, 33)];
                textLabel  = [[UILabel alloc] initWithFrame:CGRectMake(53, y, 314, 21)];
                imageLine  = [[UIImageView alloc] initWithFrame:CGRectMake(50, y+33+22, 316,1)];
            }
            textFieldDynamic.borderStyle = UITextBorderStyleNone;
            textFieldDynamic.font = [UIFont fontWithName:@"Proxima Nova" size:12];
            textFieldDynamic.backgroundColor = [UIColor whiteColor];
            textFieldDynamic.alpha = 0.8f;
            textFieldDynamic.tag = 300 + i;
            textFieldDynamic.returnKeyType  = UIReturnKeyDone;
            textFieldDynamic.placeholder = [arrDisplayField objectAtIndex:i];
            NSString *strKey = [arrKeys objectAtIndex:i];

            
            textLabel.text = [NSString stringWithFormat:@"%@",[arrDisplayField objectAtIndex:i]];
            textLabel.font = [UIFont fontWithName:@"Proxima Nova" size:12];
            textLabel.textColor = [UIColor colorWithRed:202.0/255.0 green:202.0/255.0 blue:202.0/255.0 alpha:1.0];
            
            imageLine.alpha = 0.5f;
            imageLine.backgroundColor = [UIColor colorWithRed:202.0/255.0 green:202.0/255.0 blue:202.0/255.0 alpha:1.0];
           
            if(arrfields == (id)[NSNull null])
            {
                
            }
            else
            {
                if ([[arrfields objectAtIndex:i] integerValue] == 1)
                {
                    
                       if([[arrKeys objectAtIndex:i]isEqualToString:@"smsOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                            
                        }
                    
                        if([[arrKeys objectAtIndex:i]isEqualToString:@"emailOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                        }
                    
                      if([[arrKeys objectAtIndex:i]isEqualToString:@"pushOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                            
                        }
                    
                    
                }
                else if ([[NSString stringWithFormat:@"%@",[arrfields objectAtIndex:i]] isEqualToString:@"0"])
                    {
                        
                        NSLog(@"textFieldDynamic.placeholder is place %@",textFieldDynamic.placeholder);

                        if([[arrKeys objectAtIndex:i]isEqualToString:@"smsOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                            
                        }
                        
                        if([[arrKeys objectAtIndex:i]isEqualToString:@"emailOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                        }
                        
                        if([[arrKeys objectAtIndex:i]isEqualToString:@"pushOpted"]&& [[arrKeys objectAtIndex:i] length] !=0)
                        {
                        }
                        
                    }
                    else
                    {
                        NSLog(@"textFieldDynamic.placeholder is %@",textFieldDynamic.placeholder);
                        
                        if([arrfields objectAtIndex:i] == (NSString *)[NSNull null])
                        {
                            textFieldDynamic.text = @"";
                        }
                        else
                        {
                            

                        textFieldDynamic.text = [NSString stringWithFormat:@"%@",[arrfields objectAtIndex:i]];
                            NSLog(@"value of placeholder %@ is %@", textFieldDynamic.placeholder,[arrfields objectAtIndex:i]);
                        }
                    }
            }
            
            
            //Condition according to Fields.
            
            
            
            
            if([strKey isEqualToString:@"cellPhone"] && strKey.length!=0)
             {
                textFieldDynamic.keyboardType = UIKeyboardTypeNumberPad;
                 if([textFieldDynamic.text isEqualToString:@""])
                 {
                     textFieldDynamic.userInteractionEnabled = YES;
                 }
                 else
                 {
                     textFieldDynamic.userInteractionEnabled = NO;
                     
                 }
                 
                 if(textFieldDynamic.text.length>0 && textFieldDynamic.text.length == 10)
                 {
                     NSString *num = [self formatNumber:textFieldDynamic.text];
                 NSString *str1 = [num substringToIndex:3];
                 NSString *str2 = [num substringFromIndex:3];
                 str2 = [str2 substringWithRange:NSMakeRange(0, 3)];
                 NSLog(@"str2 is %@",str2);
                 NSString *str3 = [num substringFromIndex:6];
                 NSLog(@"str3 is %@",str3);
                 str3 = [str3 substringWithRange:NSMakeRange(0, 4)];
                 NSLog(@"str3 is %@",str3);

                 NSLog(@"textFieldDynamic is %@%@%@",str1,str2,str3);

                 textFieldDynamic.text = [NSString stringWithFormat:@"%@-%@-%@",str1,str2,str3];
                 }
                 
                 UIToolbar *keyboardDoneButtonView = [[UIToolbar alloc] init];
                 [keyboardDoneButtonView sizeToFit];
                 UIBarButtonItem *flexBarButton = [[UIBarButtonItem alloc]
                                                   initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                                   target:nil action:nil];
                 
                 UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"style:UIBarButtonItemStylePlain target:self
                                                                               action:@selector(doneClicked:)];
                 
                 keyboardDoneButtonView.items = @[flexBarButton, doneButton];
                 textFieldDynamic.inputAccessoryView = keyboardDoneButtonView;

             }
            
            else if([strKey isEqualToString:@"emailID"]&& strKey.length!=0)
             {
                textFieldDynamic.keyboardType = UIKeyboardTypeEmailAddress;
                 if([textFieldDynamic.text isEqualToString:@""])
                 {
                     textFieldDynamic.userInteractionEnabled = YES;
                 }
                 else
                 {
                     textFieldDynamic.userInteractionEnabled = NO;

                 }
                 
             }
            else if([strKey isEqualToString:@"lastName"]&& strKey.length!=0)
              {
                  
              }
            
            else if([strKey isEqualToString:@"addressCountry"]&& strKey.length!=0)//country
            {
                
                UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
                UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                
                if (screenWidth == 375 && screenHeight == 667)
                {
                    male1.frame = CGRectMake(0, 0, 350, 40);
                    male.frame = CGRectMake(320, 7, 25, 25);
                }
                else if (screenWidth == 320 && screenHeight == 568)
                {
                    male1.frame = CGRectMake(0, 0, 300, 40);
                    male.frame = CGRectMake(257, 7, 25, 25);
                }
                else if (screenWidth == 320 && screenHeight == 480)
                {
                    male1.frame = CGRectMake(0, 0, 300, 40);
                    male.frame = CGRectMake(225, 7, 25, 25);
                }
                else if (screenWidth == 414 && screenHeight == 736)
                {
                    male1.frame = CGRectMake(0, 0, 360, 40);
                    male.frame = CGRectMake(300, 7, 25, 25);
                }
                
                male1.backgroundColor = [UIColor clearColor];
                [male1 addTarget:self action:@selector(FavouritCountryBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                male1.tag =textFieldDynamic.tag;
                [textFieldDynamic addSubview:male1];
                
                [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
                [male addTarget:self action:@selector(FavouritCountryBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                [textFieldDynamic addSubview:male];
            }
            
            
            
            else if([strKey isEqualToString:@"addressState"]&& strKey.length!=0)
               {
                   
                   
                   if([dic valueForKey:@"addressState"]!=(NSString *)[NSNull null])
                   {
                       if([[dic valueForKey:@"addressState"]integerValue])
                       {
                          textFieldDynamic.text =  [self StateNameWithStateID:[[dic valueForKey:@"addressState"] integerValue]];
                           NSLog(@"integerValue");
                       }
                       else
                       {
                           NSString *strdata = [self StateNameWithStateCode:[dic valueForKey:@"addressState"]];
                           textFieldDynamic.text = strdata;
                           NSLog(@"strdata is %@",strdata);

                       }
                   }

                   
                   
                   UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
                   UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                  
                   if (screenWidth == 375 && screenHeight == 667)
                   {
                       male1.frame = CGRectMake(0, 0, 350, 40);
                       male.frame = CGRectMake(320, 7, 25, 25);
                   }
                   else if (screenWidth == 320 && screenHeight == 568)
                   {
                       male1.frame = CGRectMake(0, 0, 300, 40);
                       male.frame = CGRectMake(257, 7, 25, 25);
                   }
                   else if (screenWidth == 320 && screenHeight == 480)
                   {
                       male1.frame = CGRectMake(0, 0, 300, 40);
                       male.frame = CGRectMake(225, 7, 25, 25);
                   }
                   else if (screenWidth == 414 && screenHeight == 736)
                   {
                       male1.frame = CGRectMake(0, 0, 360, 40);
                       male.frame = CGRectMake(300, 7, 25, 25);
                   }
                   
                   male1.backgroundColor = [UIColor clearColor];
                   [male1 addTarget:self action:@selector(FavouritStateBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                   male1.tag =textFieldDynamic.tag;
                   [textFieldDynamic addSubview:male1];
                   
                   [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
                   male.tag = 8001;
                   [male addTarget:self action:@selector(FavouritStateBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                   [textFieldDynamic addSubview:male];
               }
            
            else if([strKey isEqualToString:@"dateOfBirth"]&& strKey.length!=0)
              {
                  
                  
                  datePicker = [[UIDatePicker alloc] initWithFrame:CGRectZero];
                  [datePicker setDatePickerMode:UIDatePickerModeDate];
                  datePicker.tag = 9000+ textFieldDynamic.tag;
                  [datePicker addTarget:self action:@selector(onDatePickerValueChanged:) forControlEvents:UIControlEventValueChanged];
                  
                  NSString *strDefaultValue = textFieldDynamic.text;
                  NSLog(@"strDefaultValue is %@",strDefaultValue);
                  NSDate *date;
                  NSDateFormatter * formatter = [[NSDateFormatter alloc]init];
                  [formatter setDateFormat:@"yyyy/MM/dd"];
                  if([strDefaultValue isEqualToString:@""])
                  {
                      date = [NSDate date];

                  }
                  else
                  {
                      date = [formatter dateFromString:strDefaultValue];
                  }
                  [formatter setDateFormat:@"MM/dd/yyyy"];
                  strDefaultValue = [formatter stringFromDate:date];
                  textFieldDynamic.text = strDefaultValue;
                  NSLog(@"strDefaultValue is %@",strDefaultValue);
                  // NSLog(@"date is %@",date);

                  
                  datePicker.date = date;
                  
                  textFieldDynamic.inputView = datePicker;
                  
                  
                  UIToolbar *keyboardDoneButtonView = [[UIToolbar alloc] init];
                  [keyboardDoneButtonView sizeToFit];
                  UIBarButtonItem *flexBarButton = [[UIBarButtonItem alloc]
                                                    initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                                    target:nil action:nil];
                  
                  UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"style:UIBarButtonItemStylePlain target:self
                                                                                action:@selector(DatePickerDoneClicked:)];
                  doneButton.tag = 9200 + i;
                  
                  keyboardDoneButtonView.items = @[flexBarButton, doneButton];
                  textFieldDynamic.inputAccessoryView = keyboardDoneButtonView;
                  
                  
              }
            else if([strKey isEqualToString:@"firstName"]&& strKey.length!=0)
               {
               }
            else if([strKey isEqualToString:@"addressCity"]&& strKey.length!=0)
              {
              }
            else if([strKey isEqualToString:@"smsOpted"]&& strKey.length!=0)
              {
                  
                  textLabel.text = @"SMS Opt-In";

                UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
                male1.backgroundColor = [UIColor clearColor];
                [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
                [textFieldDynamic addSubview:male1];
                
                UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox"] forState:UIControlStateNormal];
                [male addTarget:self action:@selector(btnSMSOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
                male.tag =4001;
                [textFieldDynamic addSubview:male];
                  
                  
                  if([[dic valueForKey:@"smsOpted"]boolValue]==1)
                  {
                      [(UIButton *)[textFieldDynamic viewWithTag:4001]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
                      smsSelection = @"true";
                      isCheckSms = YES;
                  }
                  else
                  {
                      [(UIButton *)[textFieldDynamic viewWithTag:4001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
                      smsSelection = @"false";
                      isCheckSms = NO;
                  }
                  
                  if(screenWidth == 320 && screenHeight == 480)
                  {
                      male1.frame = CGRectMake(0, 0, 280, 30);
                      male.frame = CGRectMake(220, 0, 25, 25);
                  }
                  else if (screenWidth == 320 && screenHeight == 568)
                  {
                      male1.frame = CGRectMake(0, 0, 280, 30);
                      male.frame = CGRectMake(245, 0, 25, 25);
                  }
                  else if (screenWidth == 375 && screenHeight == 667)
                  {
                      male1.frame = CGRectMake(0, 0, 335, 35);
                      male.frame = CGRectMake(290,0, 25, 25);
                  }
                  else if (screenWidth == 414 && screenHeight == 736)
                  {
                      male1.frame = CGRectMake(0, 0, 354, 33);
                      male.frame = CGRectMake(290, 0, 25, 25);
                  }
            }
            
             else if([strKey isEqualToString:@"customerGender"]&& strKey.length!=0)
               {
                   
                UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
                male1.backgroundColor = [UIColor clearColor];
//                   textFieldDynamic.text = @"Gender";
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
                    label.frame = CGRectMake(130, 12, 50, 16);
                }
                else if (screenWidth == 320 && screenHeight == 480)
                {
                    label.frame = CGRectMake(155, 12, 50, 16);
                }
                else if (screenWidth == 414 && screenHeight == 736)
                {
                    label.frame = CGRectMake(185, 12, 50, 16);
                }
                label.backgroundColor = [UIColor clearColor];
                label.textColor = [UIColor blackColor];
                label.font = [UIFont fontWithName:@"Proxima Nova" size:13.0];
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
                    label = [[UILabel alloc] initWithFrame:CGRectMake(248, 12, 60, 16)];
                }
                else if (screenWidth == 320 && screenHeight == 568)
                {
                    label = [[UILabel alloc] initWithFrame:CGRectMake(208, 12, 60, 16)];
                }
                else if (screenWidth == 320 && screenHeight == 480)
                {
                    label = [[UILabel alloc] initWithFrame:CGRectMake(228, 12, 60, 16)];
                }
                else if (screenWidth == 414 && screenHeight == 736)
                {
                    label = [[UILabel alloc] initWithFrame:CGRectMake(258, 12, 60, 16)];
                }
                
                label.backgroundColor = [UIColor clearColor];
                label.font = [UIFont fontWithName:@"Proxima Nova" size:12.0];
                label.textColor = [UIColor blackColor];
                label.text = @"Female";
                [textFieldDynamic addSubview:label];
                
                [textFieldDynamic addSubview:male];
                [textFieldDynamic addSubview:female];
                
                    if (screenWidth == 375 && screenHeight == 667)
                    {
                         male1.frame  =  CGRectMake(0, 0, 320, 40);
                         male.frame   =  CGRectMake(150, 7, 20, 20);
                         female.frame =  CGRectMake(220, 7, 20, 20);
                    }
                    else if (screenWidth == 320 && screenHeight == 568)
                    {
                         male1.frame  =  CGRectMake(0, 0, 300, 40);
                         male.frame   =  CGRectMake(105, 7, 20, 20);
                         female.frame =  CGRectMake(185, 7, 20, 20);
                    }
                    else if (screenWidth == 320 && screenHeight == 480)
                    {
                         male1.frame  =  CGRectMake(0, 0, 300, 40);
                         male.frame   =  CGRectMake(130, 7, 20, 20);
                         female.frame =  CGRectMake(200, 7, 20, 20);
                    }
                    else if (screenWidth == 414 && screenHeight == 736)
                    {
                         male1.frame  =  CGRectMake(0, 0, 320, 40);
                         male.frame   =  CGRectMake(160, 7, 20, 20);
                         female.frame =  CGRectMake(230, 7, 20, 20);
                    }
                   
                   
                   if([dic valueForKey:@"customerGender"] != (NSString *)[NSNull null])
                   {
                       if([[dic valueForKey:@"customerGender"]isEqualToString:@"Male"] || [[dic valueForKey:@"customerGender"]isEqualToString:@"M"])
                       {
                           [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
                           [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
                           genderSelection = @"M";
                       }
                       else
                       {
                           [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
                           [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
                           genderSelection = @"F";
                       }
                   }
                   
                   
                
            }
            
          else if([strKey isEqualToString:@"homeStoreID"])
               {
                   
                   NSInteger  str1 = [[dic valueForKey:@"homeStoreID"] integerValue];
                   
                   if(str1  == 0)
                   {
                      textFieldDynamic.text =  [self favouritStoreName:str1];
                   }
                   else
                   {
                       textFieldDynamic.text = [self favouritStoreName:str1];

                   }
                  
                   UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
  
                   UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                   
                   if (screenWidth == 375 && screenHeight == 667)
                   {
                       male1.frame = CGRectMake(0, 0, 350, 40);
                       male.frame = CGRectMake(320, 7, 20, 20);
                   }
                   else if (screenWidth == 320 && screenHeight == 568)
                   {
                       male1.frame = CGRectMake(0, 0, 300, 40);
                       male.frame = CGRectMake(257, 7, 20, 20);
                   }
                   else if (screenWidth == 320 && screenHeight == 480)
                   {
                       male1.frame = CGRectMake(0, 0, 300, 40);
                       male.frame = CGRectMake(225, 7, 20, 20);
                   }
                   else if (screenWidth == 414 && screenHeight == 736)
                   {
                       male1.frame = CGRectMake(0, 0, 360, 40);
                       male.frame = CGRectMake(300, 7, 20, 20);
                   }
   
                   male1.backgroundColor = [UIColor clearColor];
                   [male1 addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                   male1.tag =textFieldDynamic.tag;
                   [textFieldDynamic addSubview:male1];
                   
                   
                   [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
                   [male addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
                   [textFieldDynamic addSubview:male];
                   
               }
             else if([strKey isEqualToString:@"addressZip"]&& strKey.length!=0)
             {
                 textFieldDynamic.keyboardType = UIKeyboardTypeNumberPad;
                 
                 UIToolbar *keyboardDoneButtonView = [[UIToolbar alloc] init];
                 [keyboardDoneButtonView sizeToFit];
                 UIBarButtonItem *flexBarButton = [[UIBarButtonItem alloc]
                                                   initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                                   target:nil action:nil];
                 
                 UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"style:UIBarButtonItemStylePlain target:self
                                                                               action:@selector(doneClicked:)];
                 
                 keyboardDoneButtonView.items = @[flexBarButton, doneButton];
                 textFieldDynamic.inputAccessoryView = keyboardDoneButtonView;

                 
                 
             }
             else if([strKey isEqualToString:@"addressCity"]&& strKey.length!=0)
               {
               }
            else if([strKey isEqualToString:@"emailOpted"]&& strKey.length!=0)
              {
                  
                  textLabel.text = @"Email Opt-In";

                UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
             
                male1.backgroundColor = [UIColor clearColor];
                [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
                [textFieldDynamic addSubview:male1];
                  
                
                UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
           
                [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
                [male addTarget:self action:@selector(EmailOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
                male.tag =3001;
                [textFieldDynamic addSubview:male];
                  
                  
                  if([[dic valueForKey:@"emailOpted"]boolValue]==1)
                  {
                      [(UIButton *)[textFieldDynamic viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
                      emailSelect = @"true";
                      isCheckEmail = YES;
                  }
                  else
                  {
                      [(UIButton *)[textFieldDynamic viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
                      emailSelect = @"false";
                      isCheckEmail = NO;
                      
                  }
                  
                  
                
                  if(screenWidth == 320 && screenHeight == 480)
                  {
                      male1.frame = CGRectMake(0, 0, 280, 30);
                      male.frame = CGRectMake(220, 0, 25, 25);
                  }
                  else if (screenWidth == 320 && screenHeight == 568)
                  {
                      male1.frame = CGRectMake(0, 0, 280, 30);
                      male.frame = CGRectMake(245, 0, 25, 25);
                  }
                  else if (screenWidth == 375 && screenHeight == 667)
                  {
                      male1.frame = CGRectMake(0, 0, 335, 35);
                      male.frame = CGRectMake(335, 0, 25, 25);
                  }
                  else if (screenWidth == 414 && screenHeight == 736)
                  {
                      male1.frame = CGRectMake(0, 0, 354, 33);
                      male.frame = CGRectMake(290, 0, 25, 25);
                  }
                  
            }
            else if([strKey isEqualToString:@"pushOpted"]&& strKey.length!=0)
            {
                
                textLabel.text = @"Push Opt-In";

    
                UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
                
                male1.backgroundColor = [UIColor clearColor];
                [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
                [textFieldDynamic addSubview:male1];
                
                UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
                
                [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
                [male addTarget:self action:@selector(pushOpted_Action:) forControlEvents:UIControlEventTouchUpInside];
                male.tag =3005;
                [textFieldDynamic addSubview:male];
                
                
                if([[dic valueForKey:@"pushOpted"]boolValue]==1)
                {
                    [(UIButton *)[textFieldDynamic viewWithTag:3005]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
                    
                    pushSelection = @"true";
                    isCheckPush = YES;
                }
                else
                {
                    [(UIButton *)[textFieldDynamic viewWithTag:3005]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
                    
                    pushSelection = @"false";
                    isCheckPush = NO;
                    
                }
                
                if(screenWidth == 320 && screenHeight == 480)
                {
                    male1.frame = CGRectMake(0, 0, 280, 30);
                    male.frame = CGRectMake(220, 0, 25, 25);
                }
                else if (screenWidth == 320 && screenHeight == 568)
                {
                    male1.frame = CGRectMake(0, 0, 280, 30);
                    male.frame = CGRectMake(245, 0, 25, 25);
                }
                else if (screenWidth == 375 && screenHeight == 667)
                {
                    male1.frame = CGRectMake(0, 0, 335, 35);
                    male.frame = CGRectMake(335, 0, 25, 25);
                }
                else if (screenWidth == 414 && screenHeight == 736)
                {
                    male1.frame = CGRectMake(0, 0, 354, 33);
                    male.frame = CGRectMake(295, 0, 25, 25);
                }
                
            }
            
             else if([arrKeys containsObject:@"password"])
            {
            }
            
        else
        {
            
            NSMutableArray *arrCustomValues = [[resultArray objectAtIndex:i]valueForKey:@"optionList"];
            
            NSLog(@"Index is %ld",(long)i);
            NSLog(@"OptionList is %@",[[resultArray objectAtIndex:i]valueForKey:@"optionList"]);
            NSLog(@"profileFieldType is %@",[NSString stringWithFormat:@"%@", [[resultArray objectAtIndex:i]valueForKey:@"profileFieldType"]]);

            
            NSString *strDefault;

            if([arrCustomValues class] == [NSNull class])
            {
                
                if([textFieldDynamic.text isEqualToString:@""])
                {
                    strDefault = [[resultArray objectAtIndex:i]valueForKey:@"defaultValue"];
                }
                else
                {
                    strDefault = textFieldDynamic.text;
                }
                
                
                NSInteger intmaxLenth =   [[[resultArray objectAtIndex:i]valueForKey:@"maximumLength"] integerValue];
                if(intmaxLenth==0)
                {
                    textFieldDynamic.userInteractionEnabled =  NO;
                }
                else
                {
                    textFieldDynamic.userInteractionEnabled =  YES;
                    
                }
                
                
                textFieldDynamic = [themeFieldType getFieldType:[[resultArray objectAtIndex:i]valueForKey:@"profileFieldType"] withTextFieldTag:textFieldDynamic withIndex:i withArrCusValues:nil withDefaultValue: strDefault];
                if ([[NSString stringWithFormat:@"%@", [[resultArray objectAtIndex:i]valueForKey:@"profileFieldType"]] isEqualToString:@"checkbox"])
                {
                    textFieldDynamic.text =  @"";

                }

                
            }
            else
            {
                
                if(arrCustomValues.count> 0)
                {
                    
                    if([textFieldDynamic.text isEqualToString:@""])
                    {
                        strDefault = [[resultArray objectAtIndex:i]valueForKey:@"defaultValue"];
                    }
                    else
                    {
                        strDefault = textFieldDynamic.text;
                    }
                    
                    
                    textFieldDynamic = [themeFieldType getFieldType:[[resultArray objectAtIndex:i]valueForKey:@"profileFieldType"] withTextFieldTag:textFieldDynamic withIndex:i withArrCusValues:resultArray withDefaultValue:strDefault];
                    
                    if ([[NSString stringWithFormat:@"%@", [[resultArray objectAtIndex:i]valueForKey:@"profileFieldType"]] isEqualToString:@"checkbox"])
                    {
                        textFieldDynamic.text =  @"";
                        
                    }
                    
                }
                
            }
            
            customFields = customFields + 1;
            
            }
            
            
            textFieldDynamic.autocorrectionType = UITextAutocorrectionTypeNo;
            textFieldDynamic.delegate = self;
            [obj addLeftPaddingToTextField:textFieldDynamic];
            [self.mainScrollView addSubview:textFieldDynamic];
            textFieldDynamic.autocapitalizationType = UITextAutocapitalizationTypeNone;
            
            [self.mainScrollView addSubview:textLabel];
            [textFieldDynamic addSubview:imageLine];
            [self.mainScrollView addSubview:imageLine];
            y = y+40+22;
        }
        
    }

    self.viewScrolTopConstraint.constant = 62*arrfields.count;


}



#pragma  mark - getMember Api

-(void)getMemberApiCall
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall getMember:nil url:@"/member/getMember" withTarget:self withSelector:@selector(getMember:)];
    apiCall = nil;
}


-(void)getMember:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"member response value------%@",response);
    
    NSUserDefaults *def = [NSUserDefaults standardUserDefaults];
    [def setObject:[NSKeyedArchiver archivedDataWithRootObject:response] forKey:@"MyData"];
    [def synchronize];
    
    // call state Api
    [self stateApi];
    
    // call profile method
    [self profileRecord];
    
}


// state search Api
-(void)stateApi
{
    if([[NSUserDefaults standardUserDefaults]boolForKey:@"stateFirst"] == NO)
    {
        [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"stateFirst"];
        
        [obj addLoadingView:self.view];
        apiCall=[ApiClasses sharedManager];
        [apiCall stateSearch:nil url:@"/states" withTarget:self withSelector:@selector(stateApi:)];
        apiCall = nil;
    }
}


// state api response
-(void)stateApi:(id)response
{
    [obj removeLoadingView:self.view];
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        [[NSUserDefaults standardUserDefaults]setValue:[response valueForKey:@"stateList"] forKey:@"stateSearch"];
    }
    else
    {
        [self alertViewDelegate:@"States not loaded"];
    }
    
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
}





-(void)DropDownBtn_Action:(NSMutableArray *)arrMutable withTxtFieldTag:(NSInteger)intTagValue
{
//    [datePicker setHidden:YES];
    
    StoreSearchView * obj1 = [[StoreSearchView alloc]initWithNibName:@"StoreSearchView" bundle:nil];
    obj1.delegate = self;
    obj1.customField = 1;
    obj1.tagValue = intTagValue;
    obj1.arrCategory = arrMutable;
    [self.navigationController pushViewController:obj1 animated:YES];
    [self resignKeyboard];
}


-(void)customSearch:(NSString *)strValue withTextFieldTag:(NSInteger)intTag;
{
    NSLog(@" strValue--------%@",strValue);
    NSLog(@" intTag--------%ld",(long)intTag);
    NSLog(@"self.view %@",self.view);
    
    UITextField * txtCustom = (UITextField *)[self.view viewWithTag:intTag];
    
    NSLog(@"txtCustom is %@",txtCustom.description);
    txtCustom.text = strValue;
    
}


-(void)DatePickerDoneClicked:(UIDatePicker *)sender withValues:(NSString *)strValue;
{
    
    if([strValue isEqualToString:@""])
    {
        //    UIDatePicker *datePicker = (UIDatePicker *)sender;
        NSDate * date = datePicker.date;
        NSDateFormatter * formatter = [[NSDateFormatter alloc]init];
        [formatter setDateFormat:@"MM/dd/YYYY"]; // yyyy-MM-dd'T'HH:mm:ssZ
        NSString *strDate = [formatter stringFromDate:date];
        NSLog(@"strDate is %@",strDate);
        strValue = strDate;
        formatter = nil;
        
    }
    
    NSLog(@"sender tag is %ld",(long)sender.tag);
    UITextField *txtField = (UITextField *)[self.view viewWithTag:sender.tag+100];
    NSLog(@"txtField is %@",txtField.description);
    
    txtField.text = strValue;
    
    [self.view endEditing:YES];
    
    
}

-(void)onDatePickerValueChanged:(id)sender;
{
    UIDatePicker *tempdatePicker = (UIDatePicker *)sender;
    NSDate * date = tempdatePicker.date;
    NSDateFormatter * formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"MM/dd/YYYY"]; // yyyy-MM-dd'T'HH:mm:ssZ
    NSString *strDate = [formatter stringFromDate:date];
    NSLog(@"strDate is %@",strDate);
    formatter = nil;
    UITextField *txtField = (UITextField *)[self.view viewWithTag:tempdatePicker.tag-9000];
    txtField.text = strDate;
    
    
}


-(void)btnCustomCheck_Action:(UIButton *)sender
{
    
    UITextField *txtTemp = (UITextField *)[self.view viewWithTag:sender.tag-4000];
    
    if(sender.selected == YES)
    {
        NSLog(@"sender is yes %i",sender.selected);
        sender.selected = NO;
        [sender setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        [dictCheck setValue:@"false" forKey:[NSString stringWithFormat:@"%ld",(long)txtTemp.tag]];
    }
    else
    {
        NSLog(@"sender is no %i",sender.selected);
        
        sender.selected = YES;
        [sender setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        [dictCheck setValue:@"true" forKey:[NSString stringWithFormat:@"%ld",(long)txtTemp.tag]];
        
    }
}


# pragma  mark - State Search

-(NSString *)StateNameWithStateCode:(NSString *)str
{
    NSString *strFavStore;
    
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSArray * arrStateSearch = [currentDefaults objectForKey:@"stateSearch"];
    
    NSArray * str1 = [arrStateSearch filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(stateCode == %@)",str]];
    
    if(str1.count!=0)
    {
        strFavStore =  [[str1 objectAtIndex:0]valueForKey:@"stateName"];

    }
    else
    {
        strFavStore = @"";
    }
    
    return strFavStore;
    
}



# pragma  mark - State Search

-(NSString *)StateNameWithStateID:(NSInteger )str
{
    
    NSString *strFavStore;
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSArray * arrStateSearch = [currentDefaults objectForKey:@"stateSearch"];
    
    NSArray * str1 = [arrStateSearch filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(stateID == %ld)",(long)str]];
    
    if(str1.count!=0)
    {
        strFavStore =  [[str1 objectAtIndex:0]valueForKey:@"stateName"];
    }
    else
    {
        strFavStore = @"";
    }
    
    return  strFavStore;
    
}



# pragma mark - favouritStore code

-(NSString *)favouritStoreName:(NSInteger )str
{
    
    NSString *strFavStore;
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"storesKey"];
    NSArray * arrStoreNumber = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    NSArray * str1 = [arrStoreNumber filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(storeID == %ld)",(long)str]];

    if(str1.count!=0)
    {
    strFavStore =  [[str1 objectAtIndex:0]valueForKey:@"storeName"];
    NSLog(@"SDK demo store name=%@",strFavStore);
        
    }
    
    return  strFavStore;
}



# pragma mark - favourit store button action

-(void)FavouritBtn_Action:(UIButton *)sender
{
    
    StoreSearchView * obj1 = [[StoreSearchView alloc]initWithNibName:@"StoreSearchView" bundle:nil];
    obj1.delegate = self;
    obj1.tagValue = sender.tag;
    [self.navigationController pushViewController:obj1 animated:YES];
    
    [self resignKeyboard];
}



# pragma mark - State button action

-(void)FavouritStateBtn_Action:(UIButton *)sender
{
    
    StateSearchView * obj1 = [[StateSearchView alloc]initWithNibName:@"StateSearchView" bundle:nil];
    obj1.delegate = self;
    obj1.intTag = sender.tag;
    obj1.titleLabel = @"State";
    [self.navigationController pushViewController:obj1 animated:YES];
    
    [self resignKeyboard];
}

# pragma mark - Country button action

-(void)FavouritCountryBtn_Action:(UIButton *)sender
{
    
    StateSearchView * obj1 = [[StateSearchView alloc]initWithNibName:@"StateSearchView" bundle:nil];
    obj1.delegate = self;
    obj1.intTag = sender.tag;
    obj1.titleLabel = @"Country";
    [self.navigationController pushViewController:obj1 animated:YES];
    [(UIButton *)[self.view viewWithTag:1002]setHidden:YES];
    [(UIButton *)[self.view viewWithTag:8001]setHidden:YES];
    
    [self resignKeyboard];
}



# pragma mark - State search delegate

-(void)stateSearch:(NSString *)StateName withTag:(NSInteger)intTag
{
    
    if(StateName.length!=0)
    {
        [(UIButton *)[self.view viewWithTag:9001]setHidden:NO];
        [(UIButton *)[self.view viewWithTag:8001]setHidden:NO];
        UITextField * favouritState = (UITextField *)[self.view viewWithTag:intTag];
        favouritState.text = StateName;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:9001]setHidden:YES];
        [(UIButton *)[self.view viewWithTag:8001]setHidden:YES];
    }
}



# pragma mark - store delegate method

-(void)StoreSearch:(NSString *)storeNumber and :(NSString *)StoreName withTag:(NSInteger)intTag
{
    storeNameValue = StoreName;
    UITextField * favouritStore = (UITextField *)[self.view viewWithTag:intTag];
    favouritStore.text = storeNameValue;
    NSLog(@"intTag is %ld",(long)intTag);
    
     storeNumberValue = storeNumber;
    
    [[NSUserDefaults standardUserDefaults]setValue:storeNumberValue forKey:@"storeNumberValue"];
    
    if([storeNumberValue intValue]==0)
    {
        storeNumberString = storeNumber;
       [[NSUserDefaults standardUserDefaults]setValue:storeNumberString forKey:@"storeNumberString"];
        
    }
    else
    {
        NSLog(@" StoreNumber--------%d",[storeNumberValue intValue]);
    }
    
   [[NSUserDefaults standardUserDefaults]setValue:StoreName forKey:@"FavouritStoreName"];
    
}


# pragma mark - Country search delegate

-(void)countrySearch:(NSString *)CountryName and :(NSString *)CountryCode WithTextTag:(NSInteger)intTxtTag;
{
    UITextField * favouritCountry = (UITextField *)[self.view viewWithTag:intTxtTag];
    favouritCountry.text = CountryName;
    [[NSUserDefaults standardUserDefaults]setValue:CountryCode forKey:@"CountryCode"];
    [[NSUserDefaults standardUserDefaults]synchronize];
}




// resign button action
-(void)btnresign_Action:(id)sender
{
    [self resignKeyboard];
}



// male button action
-(void)btnmale_Action:(id)sender
{
    [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
    [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
     UITextField * gender = (UITextField *)[self.view viewWithTag:107];
    gender.text = @"Gender";
    genderSelection = @"M";
     [self resignKeyboard];
}


// female button action
-(void)btnFemale_Action:(id)sender
{
    [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
    [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
     UITextField * gender = (UITextField *)[self.view viewWithTag:107];
    gender.text = @"Gender";
    genderSelection = @"F";
    
    [self resignKeyboard];
}



// email button action
-(void)EmailOptIn_Action:(id)sender
{
    if(isCheckEmail == NO)
    {
        [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailOpt.text = @"email OptIn";
        emailSelect = @"true";
        isCheckEmail = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailOpt.text = @"email OptIn";
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
        smsOption.text = @"sms OptIn";
        smsSelection = @"true";
        isCheckSms = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:4001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * smsOption = (UITextField *)[self.view viewWithTag:106];
        smsOption.text = @"sms OptIn";
        smsSelection = @"false";
        isCheckSms = NO;

        
    }
    // keyboard resign method call
    [self resignKeyboard];
}




// email button action
-(void)pushOpted_Action:(id)sender
{
    if(isCheckPush == NO)
    {
        [(UIButton *)[self.view viewWithTag:3005]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:114];
        emailOpt.text = @"push OptIn";
        pushSelection = @"true";
        isCheckPush = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3005]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        UITextField * emailOpt = (UITextField *)[self.view viewWithTag:114];
        emailOpt.text = @"push OptIn";
        pushSelection = @"false";
        isCheckPush = NO;
        
    }
    [self resignKeyboard];
}


//
# pragma mark - numeric key pad next button action

- (IBAction)doneClicked:(id)sender
{
    [self.view endEditing:YES];
 
}

- (void)DatePickerDoneClicked:(id)sender
{
    [self.view endEditing:YES];
    
}



// text field corner radius

# pragma mark - textfield corner radius

-(void)addCornerRadius:(UITextField*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 0.5;
    textField.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
    
}

# pragma mark - view corner radius

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
           // [self.delegate logOutProfileAction];
            [self.navigationController popToRootViewControllerAnimated:YES];
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


# pragma mark - logout Api

-(void)logOutApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSMutableDictionary * dict = [NSMutableDictionary new];
    
    [dict setValue:@"Application" forKey:@"mobile"];
    
    [apiCall logoutAPI:dict url:@"/member/logout" withTarget:self withSelector:@selector(logout:)];
    apiCall = nil;
}

// logout response

-(void)logout:(id)response
{
    [obj removeLoadingView:self.view];
    if(response!=nil)
    {
        if([[response valueForKey:@"successFlag"]integerValue]==1)
        {
            [self.navigationController popToRootViewControllerAnimated:YES];
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"login"];

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

# pragma mark - alert view method

-(void)alertViewDelegateIndex:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                                                              [self.navigationController popToRootViewControllerAnimated:YES];
                                                             // [self.delegate logOutProfileAction];
                                                          }];
    [alertController addAction:defaultAction];
    [self presentViewController:alertController animated:YES completion:nil];
}




# pragma mark - back button action method

- (IBAction)updateProfileback_Action:(id)sender
{
    //[self.delegate updateProfileBackAction];
    [self.navigationController popViewControllerAnimated:YES];
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
         [(UIButton *)[self.view viewWithTag:3005]setUserInteractionEnabled:YES];
    
    for (int i =0; i<arrDynamicFields.count; i++) {
        
        UITextField * textCustom = (UITextField *)[self.view viewWithTag:[[arrDynamicFields objectAtIndex:i] integerValue]];
        textCustom.userInteractionEnabled = YES;
        
    }
    
    
        if([obj checkNetworkConnection])
        {
            
            
            NSString * message;
            
            
            for (int i = 0; i<resultArray.count; i++) {
                
                
                NSInteger intRequired =   [[[resultArray objectAtIndex:i]valueForKey:@"required"] integerValue];
                
                if(intRequired== 1)
                {
                    UITextField *txtField = (UITextField *)[self.mainScrollView viewWithTag:300+i];
                    if ([txtField.text isEqualToString:@""])
                    {
                        message = [NSString stringWithFormat:@"Please enter %@. It is Mandatory",[[resultArray objectAtIndex:i]valueForKey:@"displayName"]];
                        
                        [self alertViewDelegate:message];
                        return;
                    }
                }
                
                
            }
            
            // call signIN Api
            [self updateProfileApi];
        }
        
        else
        {
            [self alertViewDelegate:@"Please check your network connection"];
            
        }

}

# pragma mark - updateProfileApi

-(void)updateProfileApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    
    
    int p = 1;
    for (int i = 0; i<arrKeys.count; i++) {
        
        NSString *strTemp =  [arrKeys objectAtIndex:i];
        UITextField *txtField =  (UITextField *)[self.view viewWithTag:300+i];
        
        
        
        if ([strTemp isEqualToString:@"firstName"])
        {
            [dict setValue:txtField.text forKey:@"firstName"];
            
        }
        else if ([strTemp isEqualToString:@"lastName"])
        {
            [dict setValue:txtField.text forKey:@"lastName"];
            
        }
        else if ([strTemp isEqualToString:@"emailID"])
        {
            [dict setValue:txtField.text forKey:@"email"];
            
        }
        else if ([strTemp isEqualToString:@"cellPhone"])
        {
            [dict setValue:txtField.text forKey:@"phone"];
            
        }
   
        else if ([strTemp isEqualToString:@"homeStoreID"])
        {
            
            NSString * strNumber = [[NSUserDefaults standardUserDefaults]valueForKey:@"storeNumberValue"];
            
            NSLog(@"store number is in update profile ------ %@",strNumber);
            
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
            

            
        }
        else if ([strTemp isEqualToString:@"addressState"])
        {
            
            // state code
            
            NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
            NSArray * arrStateSearch = [currentDefaults objectForKey:@"stateSearch"];
            
            NSArray * str1 = [arrStateSearch filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(stateName == %@)",txtField.text]];
            
            NSLog(@"str1 is %@",str1);
            NSLog(@"%@",arrStateSearch.description);
            
            NSString *strFavState;
            if(str1.count!=0)
            {
                strFavState =  [[str1 objectAtIndex:0]valueForKey:@"stateCode"];
                NSLog(@"strFavState is %@",strFavState);
                [dict setValue:strFavState forKey:@"addressState"];
            }
            
            
        }
        else if ([strTemp isEqualToString:@"addressCountry"])
        {
            [dict setValue:txtField.text forKey:@"addressCountry"];
            
        }
        
        else if ([strTemp isEqualToString:@"addressCity"])
        {
            [dict setValue:txtField.text forKey:@"addressCity"];
            
        }
        
        else if ([strTemp isEqualToString:@"addressCity"])
        {
            [dict setValue:txtField.text forKey:@"addressStreet"];
            
        }
        
        
        else if ([strTemp isEqualToString:@"dateOfBirth"])
        {
            [dict setValue:txtField.text forKey:@"birthDate"];
            
        }
     
        else if ([strTemp isEqualToString:@"customerGender"])
        {
            [dict setValue:genderSelection forKey:@"gender"];
            
        }
        else if ([strTemp isEqualToString:@"addressZip"])
        {
            [dict setValue:txtField.text forKey:@"addressZipCode"];
            
        }
        else if ([strTemp isEqualToString:@"smsOpted"])
        {
            [dict setValue:smsSelection forKey:@"smsOptIn"];
            
        }
        else if ([strTemp isEqualToString:@"emailOpted"])
        {
            [dict setValue:emailSelect forKey:@"emailOptIn"];
            
        }
        else if ([strTemp isEqualToString:@"pushOpted"])
        {
            [dict setValue:pushSelection forKey:@"pushOptIn"];
            
        }
        
        else
        {
            
            NSLog(@"strTemp is %@",strTemp);
            NSLog(@"txtField.text is %@",txtField.text);
            [dict setValue:txtField.text forKey:[strTemp lowercaseString]];
            
            
            
            
            if ([[NSString stringWithFormat:@"%@", [[resultArray objectAtIndex:i]valueForKey:@"profileFieldType"]] isEqualToString:@"checkbox"])
            {
                
                if([[dictCheck valueForKey:[NSString stringWithFormat:@"%ld",(long)txtField.tag]] isEqualToString:@""] || [dictCheck valueForKey:[NSString stringWithFormat:@"%ld",(long)txtField.tag]] == (NSString*)[NSNull null]  || dictCheck.count == 0)
                {
                    NSLog(@"profileFieldType is %@",@"one");
                    
                    UIButton *btnTemp = (UIButton *)[self.view viewWithTag:txtField.tag+4000];
                    if(btnTemp.selected == YES)
                    {
                        [dict setValue:@"true" forKey:[strTemp lowercaseString]];
                        
                    }
                    else
                    {
                        [dict setValue:@"false" forKey:[strTemp lowercaseString]];
                    }
                }
                else
                {
                    NSLog(@"profileFieldType is %@",@"0");
                    
                    [dict setValue:[dictCheck valueForKey:[NSString stringWithFormat:@"%ld",(long)txtField.tag]] forKey:[strTemp lowercaseString]];
                    
                }
            }
            
            
            
            
            
            
            
            
            
            
            
            p = p+1;
            
        }
        
    }
    
    NSLog(@"dic print is %@",dict.description);
    
    [apiCall updateProfile:dict url:@"/member/update" withTarget:self withSelector:@selector(updateProfile:)];
    dict = nil;
    apiCall = nil;
}


// update profile api


-(void)updateProfile:(id)response
{
    
    if(response != nil)
    {
        NSLog(@"update response------%@",response);
        
        if([[response valueForKey:@"successFlag"]integerValue]==1)
        {
            // clp mobile setting api
            clpObj = [clpsdk sharedInstanceWithAPIKey];
            
            // event tracking method
            NSMutableDictionary * eventDic = [NSMutableDictionary new];
            NSString * userName = [[NSUserDefaults standardUserDefaults]valueForKey:@"userName"];
            [eventDic setValue:@"UPDATE_PROFILE" forKey:@"event_name"];
            [eventDic setValue:userName forKey:@"item_name"];
            [clpObj updateAppEvent:eventDic];
            eventDic = nil;
            
            
            [self storeNameAndAddress];
            // call getmember api
            [self getMemberApi];
           
        }
        else
        {
            [obj removeLoadingView:self.view];
            [self alertViewDelegate:[response valueForKey:@"message"]];
        }
    }
}

# pragma mark - favourit store address and store name value

-(void)storeNameAndAddress
{
    NSString * storeName = [[NSUserDefaults standardUserDefaults]valueForKey:@"FavouritStoreName"];
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"storesKey"];
    NSArray * arrStoreNumber = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    NSString * strNumber = [[NSUserDefaults standardUserDefaults]valueForKey:@"storeNumberValue"];
    NSArray * str1;
    if(strNumber.length!=0)
    {
        if([strNumber intValue]==0)
        {
            NSString * strNumerString = [[NSUserDefaults standardUserDefaults]valueForKey:@"storeNumberString"];
            
            
            str1 = [arrStoreNumber filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(storeNumber == %@)",strNumerString]];
            
        }
        else
        {
             str1 = [arrStoreNumber filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(storeNumber == %@)",strNumber]];
            
        }
    }
  
    
    NSString *strAddress;
    if(str1.count!=0)
    {
        strAddress =  [[str1 objectAtIndex:0]valueForKey:@"address"];
         obj.bottomObj.lblStoreAddress.text = strAddress;
        NSLog(@"SDK demo store name=%@",[[str1 objectAtIndex:0]valueForKey:@"address"]);
        
        [[NSUserDefaults standardUserDefaults]setValue:[[str1 objectAtIndex:0]valueForKey:@"phone"] forKey:@"phoneNumber"];
        
     obj.bottomObj.lblStoreName.text = storeName;
    
    
    NSString *strLat  =[[str1 objectAtIndex:0]valueForKey:@"latitude"];
    NSString *strLong =[[str1 objectAtIndex:0]valueForKey:@"longitude"];
    
    CLLocation *location1 = [[CLLocation alloc] initWithLatitude: 37.3462302 longitude:-121.9417057];
    CLLocation *location2 = [[CLLocation alloc] initWithLatitude:[strLat floatValue] longitude:[strLong floatValue]];
    
    NSLog(@"Distance i meters: %f", [location1 distanceFromLocation:location2]/1000);
    float miles = [location1 distanceFromLocation:location2]/1000 * 0.6213;
    obj.bottomObj.lblDistance.text = [NSString stringWithFormat:@"%.0fmi",miles];
    }
    
    
}


# pragma mark - getMember Api

- (void)getMemberApi
{
    //[obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall getMember:nil url:@"/member/getMember" withTarget:self withSelector:@selector(getMemberUpdateProfile1:)];
    apiCall = nil;
}

// api response

-(void)getMemberUpdateProfile1:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"member response value------%@",response);
    if(response != nil)
    {
    
         NSUserDefaults *def = [NSUserDefaults standardUserDefaults];
        [def setObject:[NSKeyedArchiver archivedDataWithRootObject:response] forKey:@"MyData"];
        [def synchronize];
        
        [self storeNameAndAddress];
        
        [self alertViewDelegateIndex1:@"Member updated Successfully"];
    }
}


# pragma mark - alert view with delegate

-(void)alertViewDelegateIndex1:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                    if([self isViewControllerExist:[HomeViewController class]])
                {
                [self.navigationController popToViewController:myController animated:NO];
                }
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


#pragma mark -  Change password button action

- (IBAction)changePasswordBtn_Action:(id)sender
{
    
    ChangePasswordView * passwordObj = [[ChangePasswordView alloc]initWithNibName:@"ChangePasswordView" bundle:nil];
    [self.navigationController pushViewController:passwordObj animated:YES];
}


# pragma mark - TextField Delegate Method

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    
     if (textField.tag == 111)
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
    
    
    if([textField.placeholder isEqualToString:@"Mobile Phone"])
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
            textField.text = [NSString stringWithFormat:@"%@-",num];
            
            if(range.length > 0)
                textField.text = [NSString stringWithFormat:@"%@",[num substringToIndex:3]];
        }
        else if(length == 6)
        {
            NSString *num = [self formatNumber:textField.text];
            
            textField.text = [NSString stringWithFormat:@"%@-%@-",[num  substringToIndex:3],[num substringFromIndex:3]];
            
            if(range.length > 0)
                textField.text = [NSString stringWithFormat:@"%@-%@",[num substringToIndex:3],[num substringFromIndex:3]];
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
    
    
    if ([textField.placeholder isEqualToString:@"dateOfBirth"])
    {
        [textField resignFirstResponder];
    }
    else if ([textField.placeholder isEqualToString:@"Gender"])
    {
        [textField resignFirstResponder];
    }
    else if ([textField.placeholder isEqualToString:@"emailOpted"])
    {
        
        [textField resignFirstResponder];
        
    }
    else if ([textField.placeholder isEqualToString:@"smsOpted"])
    {
        [textField resignFirstResponder];
    }
    else
    {
        
    }
}


# pragma mark - keyboard resigning

-(void)resignKeyboard
{
    [(UITextField *)[self.view viewWithTag:300]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:301]resignFirstResponder];
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
    NSString *date = [dateFormat stringFromDate:datePicker.date];
    NSDate *todayDate = [NSDate date];
    [datePicker setMaximumDate: todayDate];
    UITextField * dob  =  (UITextField *)[self.view viewWithTag:103];
    dob.text = [NSString stringWithFormat:@"%@",date];
}

// done button Action

# pragma mark - done button Action

- (IBAction)doneBtn_Action:(UIButton *)sender
{
    
    [datePicker setHidden:YES];
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"MM/dd/yyyy"];
    NSString *date = [dateFormat stringFromDate:datePicker.date];
    NSDate *todayDate = [NSDate date];
    [datePicker setMaximumDate: todayDate];
    UITextField * dob  =  (UITextField *)[self.view viewWithTag:sender.tag];
    dob.text = [NSString stringWithFormat:@"%@",date];
    
}


# pragma mark - menu button Action

- (IBAction)menuBtn_Action:(id)sender
{
    sideObject.delegate = self;
    [sideObject SideMenuAction:self.view];
}





#pragma mark -  navigation method Action
-(void)didSelectAtIndexPathRow:(NSIndexPath *)indexPath
{
    if(indexPath.row == 0)
    {
        if([self isViewControllerExist:[HomeViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
    }
    else if(indexPath.row == 1)
    {
        if([self isViewControllerExist:[LoyalityView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            LoyalityView * menuObj = [[LoyalityView alloc]initWithNibName:@"LoyalityView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 2)
    {
        if([self isViewControllerExist:[MenuViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"MenuToStore"];
            MenuViewController * menuObj = [[MenuViewController alloc]initWithNibName:@"MenuViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 3)
    {
        if([self isViewControllerExist:[StorelocatorViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"MenuToStore"];
            StorelocatorViewController * menuObj = [[StorelocatorViewController alloc]initWithNibName:@"StorelocatorViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 4)
    {
        if([self isViewControllerExist:[RewardsAndOfferView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            RewardsAndOfferView * menuObj = [[RewardsAndOfferView alloc]initWithNibName:@"RewardsAndOfferView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    

    
    else if(indexPath.row == 5)
    {
       [sideObject removeSideNave];
    }
    else if(indexPath.row == 6)
    {
        if([self isViewControllerExist:[FAQViewViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            FAQViewViewController * menuObj = [[FAQViewViewController alloc]initWithNibName:@"FAQViewViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 7)
    {
        if([self isViewControllerExist:[ContactUSView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            ContactUSView * menuObj = [[ContactUSView alloc]initWithNibName:@"ContactUSView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }

    else
    {
        [sideObject removeSideNave];
    }
}

// check controller exist
-(BOOL)isViewControllerExist:(Class)isController
{
    for (UIViewController *controller in self.navigationController.viewControllers)
    {
        if ([controller isKindOfClass:isController])
        {
            myController = controller;
            return YES;
        }
    }
    return NO;
}


#pragma mark -  back button Action

- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

// logout button Action
-(void)logOutBt_Action
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:@"Do you want to Logout."
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                                  FBSDKLoginManager *loginManager = [[FBSDKLoginManager alloc] init];
                                  [loginManager logOut];
                                  [obj RemoveBottomView];
                                  [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"changePassword"];
                                  
                                  // clp mobile setting api
                                  clpObj = [clpsdk sharedInstanceWithAPIKey];
                                  
                                  // event tracking method
                                  NSMutableDictionary * eventDic = [NSMutableDictionary new];
                                  NSString * userName = [[NSUserDefaults standardUserDefaults]valueForKey:@"userName"];
                                  [eventDic setValue:@"LOGOUT" forKey:@"event_name"];
                                  [eventDic setValue:userName forKey:@"item_name"];
                                  [clpObj updateAppEvent:eventDic];
                                  eventDic = nil;
                                  
                                  [self.navigationController popToRootViewControllerAnimated:YES];
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
}


@end
