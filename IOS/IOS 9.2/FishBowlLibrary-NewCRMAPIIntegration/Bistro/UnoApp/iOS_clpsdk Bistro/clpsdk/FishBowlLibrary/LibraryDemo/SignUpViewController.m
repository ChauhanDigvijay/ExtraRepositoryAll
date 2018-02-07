//
//  SignUpViewController.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 19/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "SignUpViewController.h"
#import "ApiClasses.h"
#import "ModelClass.h"
#import "clpsdk.h"
#import "CLPStore.h"
#import "StoreSearchView.h"
#import "StateSearchView.m"
#import "TermsAndConditionsViewController.h"
#import "PrivacyPolicyView.h"
#import "SignUpBonusView.h"
#import "Constant.h"
#import "ThemeFieldType.h"

@interface SignUpViewController ()<UITextFieldDelegate,clpSdkDelegate,UIGestureRecognizerDelegate,StoreSearch,stateSearch,SelectBonus,ThemeFieldDelegate>
{
    __weak IBOutlet UIButton    * btnTerm;
    __weak IBOutlet UIButton    * btnPrivacy;
    NSMutableArray              * arr2;
    ApiClasses                  * apiCall;
    ModelClass                  * obj;
    NSMutableArray              * mobileSetting;
    BOOL                          isCheckEmail;
    BOOL                          isCheckSms;
    NSString                    * genderSelection;
    NSString                    * emailSelect;
    NSString                    * smsSelection;
    NSString                    * pushSelection;
    NSMutableArray              * arrMandatoryFields;
    NSMutableArray              * arrAddFields;
    BOOL                          isEmailVerify;
    BOOL                          isPushOpton;
    clpsdk                      * clpsdkObj;
    NSString                    * storeCode;
    BOOL                          isFavouritSrore;
    NSString                    * storeNameValue;
    NSString                    * storeNumberValue;
    NSString                    * storeNumberString;
    BOOL                          isAgree;
    NSMutableDictionary         * dictIsMadatory;
    NSString                    * ruleID;
    NSString                    * rewardRules;
    NSMutableArray              * arrCustomTag;
    ThemeFieldType              * themeFieldType;
    NSArray * resultArray;
    NSMutableArray *arrKeyName;
    NSMutableDictionary *dictCheck;
    
}
@property (weak, nonatomic) IBOutlet UIImageView   * companyLogoImage;
@property (weak, nonatomic) IBOutlet UIButton      * doneBtn;
@property (weak, nonatomic) IBOutlet UILabel       * signUPLoyality;
@property (weak, nonatomic) IBOutlet UIDatePicker  * datePicker;
@property (weak, nonatomic) IBOutlet UIView        * datePickerView;
@property (weak, nonatomic) IBOutlet UILabel       * lblAgreeTerms;
@property (weak, nonatomic) IBOutlet UIButton      * btnAgree;
@property (weak, nonatomic) IBOutlet UIView        * registerView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *TopConstaintScrolVW;

@end


@implementation SignUpViewController


- (NSString *)withLowerCasedFirstChar:(NSString *)strSting  {

        return [NSString stringWithFormat:@"%@%@",[[strSting substringToIndex:1] lowercaseString],[strSting substringFromIndex:1]];
}


- (void)viewDidLoad {
    [super viewDidLoad];

    clpsdkObj = [[clpsdk alloc]init];
    dictCheck = [[NSMutableDictionary alloc]init];

    clpsdkObj.delegate = self;
    arrCustomTag = [[NSMutableArray alloc]init];
    themeFieldType = [[ThemeFieldType alloc]init];
    themeFieldType.themeFieldDelegate = self;
    
    
    [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"SignUp"];
    
    mobileSetting = [[NSMutableArray alloc]init];
    
    isCheckSms = NO;
    isCheckEmail = NO;
    isEmailVerify = NO;
    isPushOpton = NO;
    
    [self.datePickerView setHidden:YES];

    
    self.mainViewOL.layer.masksToBounds = YES;
    self.mainViewOL.layer.cornerRadius  = 4.0;
    self.mainViewOL.layer.borderWidth   = 1.0;
    self.mainViewOL.layer.borderColor   = [[UIColor clearColor] CGColor];
    
    
    
   // self.textFieldScroll.backgroundColor = [UIColor clearColor];
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenWidth = screenRect.size.width;
    CGFloat screenHeight = screenRect.size.height;
    
    // data fetch from mobilesetting
     obj=[ModelClass sharedManager];
    
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSLog(@"Anarchieve start");
    NSLog(@" theme setting is %@", [currentDefaults objectForKey:@"themeSetting"]);
    NSData *data  = [currentDefaults objectForKey:@"themeSetting"];
    NSLog(@"data is %@",data.description);
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    NSLog(@"themeSetting is %@",dict.description);
     NSLog(@"str value------%@",[[dict valueForKey:@"themeDetails"] valueForKey:@"profilefield"]);
    
      arr2 = [NSMutableArray new];

      arr2 = [[dict valueForKey:@"themeDetails"] valueForKey:@"profilefield"];
    
    
    NSSortDescriptor* sortOrder = [NSSortDescriptor sortDescriptorWithKey:@"configDisplaySeq" ascending: YES];
    
 
    resultArray  = [arr2 sortedArrayUsingDescriptors:[NSArray arrayWithObject: sortOrder]];
    
    NSLog(@"array field values ---------- %@",resultArray);
    
    for (int i= 0 ; i<[resultArray count]; i++)
    {
       if ( [[[resultArray objectAtIndex:i] valueForKey:@"pageName"] isEqualToString:@"Registration"])
       {
           arr2 = [[resultArray objectAtIndex:i] valueForKey:@"fields"];
       }
    }
    
     sortOrder = [NSSortDescriptor sortDescriptorWithKey:@"displaySeq" ascending: YES];

     resultArray  = [arr2 sortedArrayUsingDescriptors:[NSArray arrayWithObject: sortOrder]];

    
//    resultArray = arr2;
    NSLog(@"resultArray is %@",resultArray.description);
    
    
    // text fields
    
        arrAddFields       = [NSMutableArray new];
        arrMandatoryFields = [NSMutableArray new];
        dictIsMadatory     = [NSMutableDictionary new];
        arrKeyName =               [NSMutableArray new];
    
    [self.view bringSubviewToFront:self.getStartedBtn];


    if(resultArray.count!=0)
    {
        for(int i= 0 ; i<[resultArray count]; i++)
        {

            [arrAddFields addObject:[[resultArray objectAtIndex:i]valueForKey:@"displayName"]];
            [arrKeyName addObject:[[resultArray objectAtIndex:i]valueForKey:@"name"]];

       }
    }
    
    [currentDefaults setValue:arrKeyName forKey:@"FieldArray"];

    
    
    int y= 10;

   if(arrAddFields.count>0)
   {
    for(int i = 0; i<[arrAddFields count]; i++)
    {
        UITextField * textField;
        UIImageView * imageLine;
        UILabel     * textLabel;
       
          if(screenWidth == 320 && screenHeight == 480)
          {
            textLabel  = [[UILabel alloc] initWithFrame:CGRectMake(33, y, 260, 21)];
            textField  = [[UITextField alloc] initWithFrame:CGRectMake(18, y+22, 276, 30)];
            imageLine  = [[UIImageView alloc] initWithFrame:CGRectMake(30, y+31+22, 261,1)];
          }
          else if (screenWidth == 320 && screenHeight == 568)
          {
            textLabel  = [[UILabel alloc] initWithFrame:CGRectMake(33, y, 260, 21)];
            textField  = [[UITextField alloc] initWithFrame:CGRectMake(17, y+22, 276, 30)];
            imageLine  = [[UIImageView alloc] initWithFrame:CGRectMake(30, y+31+22, 261,1)];
          }
          else if (screenWidth == 375 && screenHeight == 667)
          {
            textLabel  = [[UILabel alloc] initWithFrame:CGRectMake(38, y, 305, 21)];
            textField  = [[UITextField alloc] initWithFrame:CGRectMake(22, y+23, 321, 36)];
            imageLine  = [[UIImageView alloc] initWithFrame:CGRectMake(35, y+36+22, 307,1)];
          }
          else if (screenWidth == 414 && screenHeight == 736)
          {
            textLabel  = [[UILabel alloc] initWithFrame:CGRectMake(53, y, 314, 21)];
            textField  = [[UITextField alloc] initWithFrame:CGRectMake(37, y+22, 331, 33)];
            imageLine  = [[UIImageView alloc] initWithFrame:CGRectMake(50, y+33+22, 316,1)];
          }
        
        imageLine.alpha = 0.5f;
        imageLine.backgroundColor = [UIColor colorWithRed:202.0/255.0 green:202.0/255.0 blue:202.0/255.0 alpha:1.0];
        
            textField.borderStyle = UITextBorderStyleNone;
            textLabel.text = [[arrAddFields objectAtIndex:i]uppercaseString];
            textLabel.font = [UIFont fontWithName:@"Proxima Nova" size:12];
            textLabel.textColor = [UIColor colorWithRed:202.0/255.0 green:202.0/255.0 blue:202.0/255.0 alpha:1.0];
            textField.tag = 300 + i;
        
            textField.backgroundColor = [UIColor clearColor];
            textField.alpha = 0.8f;
            textField.placeholder = [arrAddFields objectAtIndex:i];
            textField.userInteractionEnabled = YES;
            textField.font = [UIFont fontWithName:@"Proxima Nova" size:12];

            [textField setValue:[UIColor blackColor]
                    forKeyPath:@"_placeholderLabel.textColor"];
        
        
        NSString *strKey = [arrKeyName objectAtIndex:i];
        
         if([strKey isEqualToString:@"FirstName"]) //FirstName
        {
            
        }
        
         else if([strKey isEqualToString:@"LastName"]) // LastName
        {
            
        }
        
         else if([strKey isEqualToString:@"EmailAddress"]) // EmailAddress
         {
             textField.keyboardType = UIKeyboardTypeEmailAddress;
         }

        else if([strKey isEqualToString:@"PhoneNumber"]) // PhoneNumber
            {
                
                UIToolbar *keyboardDoneButtonView = [[UIToolbar alloc] init];
                [keyboardDoneButtonView sizeToFit];
                UIBarButtonItem *flexBarButton = [[UIBarButtonItem alloc]
                                                  initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                                  target:nil action:nil];
                
                UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"style:UIBarButtonItemStylePlain target:self
                                                                              action:@selector(doneClicked:)];
                
                keyboardDoneButtonView.items = @[flexBarButton, doneButton];
                textField.inputAccessoryView = keyboardDoneButtonView;
                
                textField.keyboardType = UIKeyboardTypeNumberPad;
            }
        
        else if([strKey isEqualToString:@"Password"]) // Password
        {
            textField.secureTextEntry = YES;
            
            
        }
        
        else if([strKey isEqualToString:@"FavoriteStore"]) // FavouritStore
        {
            textLabel.text = [@"Favourite Store" uppercaseString];
            
            UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
            male1.backgroundColor = [UIColor clearColor];
            [male1 addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
            male1.tag =textField.tag;
            
            UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
            [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
            male.tag =textField.tag;
            [male addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
            
            if(screenWidth == 320 && screenHeight == 480)
            {
                male1.frame = CGRectMake(0, 0, 280, 30);
                male.frame = CGRectMake(230, 5, 20, 20);
            }
            else if (screenWidth == 320 && screenHeight == 568)
            {
                male1.frame = CGRectMake(0, 0, 280, 30);
                male.frame = CGRectMake(257, 5, 20, 20);
            }
            else if (screenWidth == 375 && screenHeight == 667)
            {
                male1.frame = CGRectMake(0, 0, 335, 35);
                male.frame = CGRectMake(340, 8, 20, 20);
            }
            else if (screenWidth == 414 && screenHeight == 736)
            {
                male1.frame = CGRectMake(0, 0, 354, 33);
                male.frame = CGRectMake(330, 5, 20, 20);
            }
            
            male.translatesAutoresizingMaskIntoConstraints = YES;
            [textField addSubview:male1];
            [textField addSubview:male];
            
            isFavouritSrore = YES;
        }
        
        
        else if([strKey isEqualToString:@"State"]) // State
        {
            
            textLabel.text = [textField.placeholder uppercaseString];

            UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
            male1.backgroundColor = [UIColor clearColor];
            [male1 addTarget:self action:@selector(FavouritStateBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
            male1.tag =textField.tag;
            [textField addSubview:male1];
            
            UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
            [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
            
            [male addTarget:self action:@selector(FavouritStateBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
            [textField addSubview:male];
            male.tag = textField.tag;
            
            if(screenWidth == 320 && screenHeight == 480)
            {
                male1.frame = CGRectMake(0, 0, 280, 30);
                male.frame = CGRectMake(230, 5, 20, 20);
            }
            else if (screenWidth == 320 && screenHeight == 568)
            {
                male1.frame = CGRectMake(0, 0, 280, 30);
                male.frame = CGRectMake(257, 5, 20, 20);
            }
            else if (screenWidth == 375 && screenHeight == 667)
            {
                male1.frame = CGRectMake(0, 0, 335, 35);
                male.frame = CGRectMake(330, 8, 20, 20);
            }
            else if (screenWidth == 414 && screenHeight == 736)
            {
                male1.frame = CGRectMake(0, 0, 354, 33);
                male.frame = CGRectMake(330, 5, 20, 20);
            }
        }
        else if([strKey isEqualToString:@"Country"]) // Country
        {
            
            textLabel.text = [textField.placeholder uppercaseString];

            UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
            male1.backgroundColor = [UIColor clearColor];
            [male1 addTarget:self action:@selector(FavouritCountryBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
            male1.tag =textField.tag;
            [textField addSubview:male1];
            
            UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
            [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
            male1.tag =textField.tag;
            [male addTarget:self action:@selector(FavouritCountryBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
            [textField addSubview:male];
            
            
            if(screenWidth == 320 && screenHeight == 480)
            {
                male1.frame = CGRectMake(0, 0, 280, 30);
                male.frame = CGRectMake(230, 5, 20, 20);
            }
            else if (screenWidth == 320 && screenHeight == 568)
            {
                male1.frame = CGRectMake(0, 0, 280, 30);
                male.frame = CGRectMake(257, 5, 20, 20);
            }
            else if (screenWidth == 375 && screenHeight == 667)
            {
                male1.frame = CGRectMake(0, 0, 335, 35);
                male.frame = CGRectMake(330, 8, 20, 20);
            }
            else if (screenWidth == 414 && screenHeight == 736)
            {
                male1.frame = CGRectMake(0, 0, 354, 33);
                male.frame = CGRectMake(330, 5, 20, 20);
            }
        }
        else if([strKey isEqualToString:@"DOB"]) // Date Of Birth
        {
            
            textLabel.text = [textField.placeholder uppercaseString];

            
            UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
            
            
            if(screenWidth == 320 && screenHeight == 480)
            {
                male.frame = CGRectMake(0, 0, 280, 30);
            }
            else if (screenWidth == 320 && screenHeight == 568)
            {
                male.frame = CGRectMake(0, 0, 280, 30);
            }
            else if (screenWidth == 375 && screenHeight == 667)
            {
                male.frame = CGRectMake(0, 0, 335, 35);
            }
            else if (screenWidth == 414 && screenHeight == 736)
            {
                male.frame = CGRectMake(0, 0, 354, 33);
            }
            
            
            [male addTarget:self action:@selector(btnDOB_Action:) forControlEvents:UIControlEventTouchUpInside];
            male.tag =textField.tag;
            [textField addSubview:male];
            
                UIToolbar *keyboardDoneButtonView = [[UIToolbar alloc] init];
                [keyboardDoneButtonView sizeToFit];
                UIBarButtonItem *flexBarButton = [[UIBarButtonItem alloc]
                                                  initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                                  target:nil action:nil];
            
            self.doneBtn.tag = textField.tag;
                
                UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"style:UIBarButtonItemStylePlain target:self
                                                                              action:@selector(DatePickerDoneClicked:withValues:)];
                
                keyboardDoneButtonView.items = @[flexBarButton, doneButton];
                textField.inputAccessoryView = keyboardDoneButtonView;
            }
            
        
        
        else if([strKey isEqualToString:@"Address"]) //Address
        {
            textLabel.text = [textField.placeholder uppercaseString];

        }
        
        
        else if([strKey isEqualToString:@"Gender"]) // gender
        {
            
            
            textLabel.text = [textField.placeholder uppercaseString];
            textField.textColor = [UIColor blackColor];
            
            UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
            male1.frame = CGRectMake(0, 0, 335, 33);
            male1.backgroundColor = [UIColor clearColor];
            [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
            [textField addSubview:male1];
            
            UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
            male.frame = CGRectMake(100, 5, 20, 20);
            [male setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
            [male addTarget:self action:@selector(btnmale_Action:) forControlEvents:UIControlEventTouchUpInside];
            male.tag = 1001;
            male.clipsToBounds = YES;
            [male bringSubviewToFront:self.view];
            
            UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(125, 8, 50, 16)];
            label.backgroundColor = [UIColor clearColor];
            label.textColor = [UIColor blackColor];
            label.font = [UIFont fontWithName:@"Proxima Nova" size:13.0];
            label.text = @"Male";
            [textField addSubview:label];
            
            UIButton *female = [UIButton buttonWithType:UIButtonTypeCustom];
            female.frame = CGRectMake(170, 5, 20, 20);
            [female setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
            [female addTarget:self action:@selector(btnFemale_Action:) forControlEvents:UIControlEventTouchUpInside];
            female.clipsToBounds = YES;
            female.tag = 2001;
            [female bringSubviewToFront:self.view];
            
            label = [[UILabel alloc] initWithFrame:CGRectMake(195, 8, 70, 16)];
            label.backgroundColor = [UIColor clearColor];
            label.font = [UIFont fontWithName:@"Proxima Nova" size:13.0];
            label.textColor = [UIColor blackColor];
            label.text = @"Female";
            [textField addSubview:label];
            
            genderSelection = @"M";
            
            [textField addSubview:male];
            [textField addSubview:female];
            
        }
        else if([strKey isEqualToString:@"ZipCode"]) // ZipCode
        {
            UIToolbar *keyboardDoneButtonView = [[UIToolbar alloc] init];
            [keyboardDoneButtonView sizeToFit];
            UIBarButtonItem *flexBarButton = [[UIBarButtonItem alloc]
                                              initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                              target:nil action:nil];
            
            UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"style:UIBarButtonItemStylePlain target:self
                                                                          action:@selector(doneClicked:)];
            
            keyboardDoneButtonView.items = @[flexBarButton, doneButton];
            textField.inputAccessoryView = keyboardDoneButtonView;

            textField.keyboardType = UIKeyboardTypeNumberPad;
        }
        else if([strKey isEqualToString:@"City"]) // City
        {
            textLabel.text = [textField.placeholder uppercaseString];

        }

        else if([strKey isEqualToString:@"SMSOptIn"])  //smsOption
          {
              textLabel.text = [@"SMS Opt-In" uppercaseString];
              textField.textColor = [UIColor blackColor];

              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
             
              UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
              
              id isDefault = [[resultArray objectAtIndex:i]valueForKey:@"defaultValue"];
              
              if([isDefault isEqualToString:@"true"])
              {
                  [male setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
                  smsSelection = @"true";
                  isCheckSms = YES;
              }
              else
              {
             
            [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
                  smsSelection = @"false";
                  isCheckSms = NO;

              }
            [male addTarget:self action:@selector(btnSMSOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
            male.tag =6000;
    
              
              if(screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(225, 0, 25, 25);
              }
              else if (screenWidth == 320 && screenHeight == 568)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 0, 25, 25);
              }
              else if (screenWidth == 375 && screenHeight == 667)
              {
                    male1.frame = CGRectMake(0, 0, 335, 35);
                    male.frame = CGRectMake(325,0, 25, 25);
              }
              else if (screenWidth == 414 && screenHeight == 736)
              {
                  male1.frame = CGRectMake(0, 0, 354, 33);
                  male.frame = CGRectMake(325, 0, 25, 25);
              }
              
            [textField addSubview:male1];
            [textField addSubview:male];

          }
       
          else if([strKey isEqualToString:@"EmailOptIn"]) // EmailOption
          {
              textLabel.text = [@"Email Opt-In" uppercaseString];
              textField.textColor = [UIColor blackColor];
              
              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
             
              
              UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
              id isDefault = [[resultArray objectAtIndex:i]valueForKey:@"defaultValue"];
              
              if([isDefault isEqualToString:@"true"])
              {
                  [male setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
                  emailSelect = @"true";
                  isCheckEmail = YES;
              }
              else
              {
                  
                  [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
                  emailSelect = @"false";
                  isCheckEmail = NO;
                  
              }
              
              [male addTarget:self action:@selector(EmailOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
              male.tag =3001;
              
              
              if(screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(225, 0, 25, 25);
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
                  male.frame = CGRectMake(325, 0, 25, 25);
              }
 
              [textField addSubview:male1];
              [textField addSubview:male];
          }
        
          else if([strKey isEqualToString:@"pushOptIn"]) //PushOptin
          {
              
              textLabel.text = [@"push Opt-In" uppercaseString];
              textField.textColor = [UIColor blackColor];
              
              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
              
              
              UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
              id isDefault = [[resultArray objectAtIndex:i]valueForKey:@"defaultValue"];
              
              if([isDefault isEqualToString:@"true"])
              {
                  [male setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
                  pushSelection = @"true";
                  isPushOpton = YES;
              }
              else
              {
                  
                  [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
                  pushSelection = @"false";
                  isPushOpton = NO;
                  
              }
              [male addTarget:self action:@selector(pushOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
              male.tag =3006;
              
              
              if(screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(225, 0, 25, 25);
              }
              else if (screenWidth == 320 && screenHeight == 568)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 0, 25, 25);
              }
              else if (screenWidth == 375 && screenHeight == 667)
              {
                  male1.frame = CGRectMake(0, 0, 335, 35);
                  male.frame = CGRectMake(325, 0, 25, 25);
              }
              else if (screenWidth == 414 && screenHeight == 736)
              {
                  male1.frame = CGRectMake(0, 0, 354, 33);
                  male.frame = CGRectMake(325, 0, 25, 25);
              }
                 [male layoutIfNeeded];
                 [textField addSubview:male1];
                 [textField addSubview:male];
          }
          else if([strKey isEqualToString:@"Bonus"]) // Bonus
          {
              textLabel.text = [textField.placeholder uppercaseString];
              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(BonusBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
              male1.tag =textField.tag;
              [textField addSubview:male1];
              
              UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
              [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
              male.tag = textField.tag;
              [male addTarget:self action:@selector(BonusBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
              [textField addSubview:male];
              
              
              if(screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(230, 5, 20, 20);
              }
              else if (screenWidth == 320 && screenHeight == 568)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(257, 5, 20, 20);
              }
              else if (screenWidth == 375 && screenHeight == 667)
              {
                  male1.frame = CGRectMake(0, 0, 335, 35);
                  male.frame = CGRectMake(330, 8, 20, 20);
              }
              else if (screenWidth == 414 && screenHeight == 736)
              {
                  male1.frame = CGRectMake(0, 0, 354, 33);
                  male.frame = CGRectMake(330, 5, 20, 20);
              }
          }
   
    else
    {
        
        if ([[[resultArray objectAtIndex:i]valueForKey:@"customField"]boolValue] == 1)
        {
            
            NSMutableArray *arrCustomValues = [[resultArray objectAtIndex:i]valueForKey:@"optionList"];
            NSInteger intmaxLenth =   [[[resultArray objectAtIndex:i]valueForKey:@"maximumLength"] integerValue];
            if(intmaxLenth==0)
            {
                textField.userInteractionEnabled =  NO;
            }
            else
            {
                textField.userInteractionEnabled =  YES;

            }

            if([arrCustomValues class] == [NSNull class])
            {
                
                textField = [themeFieldType getFieldType:[[resultArray objectAtIndex:i]valueForKey:@"profileFieldType"] withTextFieldTag:textField withIndex:i withArrCusValues:nil withDefaultValue:[[resultArray objectAtIndex:i]valueForKey:@"defaultValue"]];
                
            }
            else
            {
                
                if(arrCustomValues.count> 0)
                {
                    textField = [themeFieldType getFieldType:[[resultArray objectAtIndex:i]valueForKey:@"profileFieldType"] withTextFieldTag:textField withIndex:i withArrCusValues:resultArray withDefaultValue:[[resultArray objectAtIndex:i]valueForKey:@"defaultValue"]];
                    
                }
                
            }
            
        }
    
    }
        
        
        textField.returnKeyType = UIReturnKeyDone;
        textField.autocorrectionType = UITextAutocorrectionTypeNo;
        textField.autocapitalizationType = UITextAutocapitalizationTypeNone;
        textField.delegate = self;
        
        [obj addLeftPaddingToTextField:textField];
        [self.textFieldScroll addSubview:textLabel];
        [textField addSubview:imageLine];
        [self.textFieldScroll addSubview:imageLine];
        [self.textFieldScroll addSubview:textField];
        
        y = y+30+5+40;
    }
       
   }
    
    self.TopConstaintScrolVW.constant = 75*arrAddFields.count;
    
    [self getTokenApi];
    
}


#pragma mark - getToken Api

-(void)getTokenApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];

    [dict setValue:clientID forKey:@"clientId"];
    [dict setValue:ClientSecret forKey:@"clientSecret"];
    [dict setValue:[apiCall deviceID] forKey:@"deviceId"];
    [dict setValue:TanentID forKey:@"tenantId"];
    
    [apiCall getTokenApi:dict url:@"/mobile/getToken" withTarget:self withSelector:@selector(getToken:)];
    dict = nil;
    apiCall = nil;
}

-(void)getToken:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"login response------%@",response);
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        [obj saveUserDefaultData:[response valueForKey:@"message"] and:@"access_token"];
    }
    else
    {
        [self alertViewDelegate:[response valueForKey:@"message"]];
    }
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    [obj RemoveBottomView];
}


-(void)DropDownBtn_Action:(NSMutableArray *)arrMutable withTxtFieldTag:(NSInteger)intTagValue
{
    [self.datePickerView setHidden:YES];
    
    StoreSearchView * obj1 = [[StoreSearchView alloc]initWithNibName:@"StoreSearchView" bundle:nil];
    obj1.delegate = self;
    obj1.customField = 1;
    obj1.tagValue = intTagValue;
    obj1.arrCategory = arrMutable;
    [self.navigationController pushViewController:obj1 animated:YES];
    [self resignKeyboard];
}

// favourit store button action
-(void)FavouritBtn_Action:(UIButton *)sender
{
    [self.datePickerView setHidden:YES];
    
    StoreSearchView * obj1 = [[StoreSearchView alloc]initWithNibName:@"StoreSearchView" bundle:nil];
    obj1.delegate = self;
    obj1.tagValue = sender.tag;
    [self.navigationController pushViewController:obj1 animated:YES];
    [self resignKeyboard];
}


# pragma mark - State button action

-(void)FavouritStateBtn_Action:(UIButton *)sender
{
    [self.datePickerView setHidden:YES];
    
    StateSearchView * obj1 = [[StateSearchView alloc]initWithNibName:@"StateSearchView" bundle:nil];
    obj1.delegate = self;
    obj1.intTag = sender.tag;
    obj1.titleLabel = @"State";
    [self.navigationController pushViewController:obj1 animated:YES];
    [self resignKeyboard];
}

# pragma mark - SignUpBonus button action

-(void)BonusBtn_Action:(UIButton *)sender
{
    [self.datePickerView setHidden:YES];
    
    SignUpBonusView * obj1 = [[SignUpBonusView alloc]initWithNibName:@"SignUpBonusView" bundle:nil];
    obj1.delegate = self;
    obj1.intTag = sender.tag;
    [self.navigationController pushViewController:obj1 animated:YES];
    [self resignKeyboard];
}

-(void)Bonus:(NSString *)ruleId andReward :(NSString *)rewardRule andDescription :(NSString *)Description withTxtTag:(NSInteger)intTxtTag;
{
    NSLog(@"reward id and rewardRule -------- %@ %@ %@",ruleId ,rewardRule,Description);
    
    UITextField * bonus = (UITextField *)[self.view viewWithTag:intTxtTag];
    bonus.text = Description;
    
    ruleID      = ruleId;
    rewardRules = rewardRule;
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

# pragma mark - Country search delegate

-(void)countrySearch:(NSString *)CountryName and :(NSString *)CountryCode WithTextTag:(NSInteger)intTxtTag;
{
    UITextField * favouritCountry = (UITextField *)[self.view viewWithTag:intTxtTag];
    favouritCountry.text = CountryName;
    NSLog(@"country Code ------- %@", CountryCode);
     [[NSUserDefaults standardUserDefaults]setValue:CountryCode forKey:@"CountryCode"];
     [[NSUserDefaults standardUserDefaults]synchronize];
}


# pragma mark - Store search delegate

-(void)StoreSearch:(NSString *)storeNumber and :(NSString *)StoreName withTag:(NSInteger)intTag
{
    storeNameValue = StoreName;
    UITextField * favouritStore = (UITextField *)[self.view viewWithTag:intTag];
    NSLog(@"favouritStoretag is %ld",(long)intTag);
    NSLog(@"favouritStore is %@",favouritStore.description);
    favouritStore.text = storeNameValue;
    
    storeNumberValue = storeNumber;
    
    if([storeNumberValue intValue]==0)
    {
        storeNumberString = storeNumber;
        NSLog(@" StoreNumber--------%@",storeNumber);
    }
    else
    {
        NSLog(@" StoreNumber--------%d",[storeNumberValue intValue]);
    }
     [[NSUserDefaults standardUserDefaults]setValue:StoreName forKey:@"FavouritStoreName"];
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


# pragma mark - Country button action

-(void)FavouritCountryBtn_Action:(UIButton *)sender
{
    [self.datePickerView setHidden:YES];
    
    StateSearchView * obj1 = [[StateSearchView alloc]initWithNibName:@"StateSearchView" bundle:nil];
    obj1.delegate = self;
    obj1.intTag = sender.tag;
    obj1.titleLabel = @"Country";
    [self.navigationController pushViewController:obj1 animated:YES];
    
    [(UIButton *)[self.view viewWithTag:9001]setHidden:NO];
    [(UIButton *)[self.view viewWithTag:8001]setHidden:NO];
    
    [self resignKeyboard];
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    
}


// resign button action
-(void)btnresign_Action:(id)sender
{
    NSLog(@"button table");
    [self resignKeyboard];
    [self.datePickerView setHidden:YES];
}


// Dob  resign keyboard button action
-(void)btnDOB_Action:(UIButton *)sender
{
    [self resignKeyboard];
    [self.datePickerView setHidden:NO];
}


// male button action
-(void)btnmale_Action:(id)sender
{
    [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
    [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
     genderSelection = @"M";
     [self resignKeyboard];
}


// female button action
-(void)btnFemale_Action:(id)sender
{
    [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
    [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
      genderSelection = @"F";
    [self resignKeyboard];
}


// email button action
-(void)EmailOptIn_Action:(id)sender
{
    if(isCheckEmail == NO)
    {
       [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        emailSelect = @"true";
        isCheckEmail = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
         emailSelect = @"false";
         isCheckEmail = NO;
    }
   [self resignKeyboard];
}

-(void)onDatePickerValueChanged:(id)sender;
{
    UIDatePicker *datePicker = (UIDatePicker *)sender;
    NSDate * date = datePicker.date;
    NSDateFormatter * formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"MM/dd/YYYY"]; // yyyy-MM-dd'T'HH:mm:ssZ
    NSString *strDate = [formatter stringFromDate:date];
    NSLog(@"strDate is %@",strDate);
    formatter = nil;
    UITextField *txtField = (UITextField *)[self.view viewWithTag:datePicker.tag-9000];
    txtField.text = strDate;
    
    
}

-(void)btnCustomCheck_Action:(UIButton *)sender
{
    
    
    UITextField *txtTemp = (UITextField *)[self.view viewWithTag:sender.tag-4000];
    NSLog(@"txtTemp.text is %@",txtTemp.description);
    
    if(sender.selected == YES)
    {
        NSLog(@"sender is no %i",sender.selected);
        sender.selected = NO;
        [sender setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        [dictCheck setValue:@"false" forKey:[NSString stringWithFormat:@"%ld",(long)txtTemp.tag]];

    }
    else
    {
        NSLog(@"sender is yes %i",sender.selected);

        sender.selected = YES;
        [sender setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        [dictCheck setValue:@"true" forKey:[NSString stringWithFormat:@"%ld",(long)txtTemp.tag]];

    }
}

// sms button action
-(void)btnSMSOptIn_Action:(id)sender
{
    
    UIButton *btTemp = (UIButton *)sender;
    
    if(isCheckSms == NO)
    {
    [(UIButton *)[self.view viewWithTag:btTemp.tag]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        smsSelection = @"true";
        isCheckSms = YES;
    }
 else
   {
    [(UIButton *)[self.view viewWithTag:btTemp.tag]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
       smsSelection = @"false";
       isCheckSms = NO;
   }
    [self resignKeyboard];
}

// email button action
-(void)btnEmailVerify:(id)sender
{
    if(isEmailVerify == NO)
    {
        [(UIButton *)[self.view viewWithTag:3005]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        isEmailVerify = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3005]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        isEmailVerify = NO;
    }
    [self resignKeyboard];
}


// email button action
-(void)pushOptIn_Action:(id)sender
{
    if(isPushOpton == NO)
    {
        [(UIButton *)[self.view viewWithTag:3006]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        pushSelection = @"true";
        isPushOpton = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3006]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        pushSelection = @"false";
        isPushOpton = NO;
    }
    [self resignKeyboard];
}



// add textfield corner radius
-(void)addCornerRadius1:(UITextField*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 0.5;
    textField.layer.borderColor = [[UIColor colorWithRed:75.0/255.0f green:75.0/255.0f blue:75.0/255.0f alpha:.3] CGColor];
}


// numeric key pad next button action
- (IBAction)doneClicked:(id)sender
{
    NSLog(@"Done Clicked.");
    
    [self.view endEditing:YES];

}

-(void)DatePickerDoneClicked:(UIDatePicker *)sender withValues:(NSString *)strValue;
{
    
    if([strValue isEqualToString:@""])
    {
        NSDate * date = self.datePicker.date;
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


# pragma mark - Memory method

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}



// view corner radius
-(void)addCornerRadius:(UIView*)view
{
    view.layer.masksToBounds = YES;
    view.layer.cornerRadius = 2.0;
    view.layer.borderWidth = 0.5;
    view.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
}


#pragma mark - Email validation

- (BOOL)validateEmailWithString:(NSString*)email
{
    NSString *emailRegex = @"[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:email];
}


# pragma mark - GetStartButton Action

- (IBAction)getStartedBtn_Action:(id)sender
{
    NSLog(@"tap on button");
    
       NSString * message;

    
    for (int i = 0; i<resultArray.count; i++) {
        
        
      NSInteger intRequired =   [[[resultArray objectAtIndex:i]valueForKey:@"required"] integerValue];
        
        if(intRequired== 1)
        {
            UITextField *txtField = (UITextField *)[self.textFieldScroll viewWithTag:300+i];
            if ([txtField.text isEqualToString:@""])
            {
                       message = [NSString stringWithFormat:@"Please enter %@. It is Mandatory",[[resultArray objectAtIndex:i]valueForKey:@"displayName"]];

                       [self alertViewDelegate:message];
                return;
            }
        }
        
        
    }
    
    [self postApiData];

    
//
//
//    
////    
//    UITextField * firstName      = (UITextField *)[self.view viewWithTag:300];
//    UITextField * lastname         = (UITextField *)[self.view viewWithTag:301];
//    UITextField * email         = (UITextField *)[self.view viewWithTag:302];
//    UITextField * phoneNumber   = (UITextField *)[self.view viewWithTag:303];
//    UITextField * password = (UITextField *)[self.view viewWithTag:304];
//    UITextField * favouritStore = (UITextField *)[self.view viewWithTag:305];
//
//    UITextField * dob           = (UITextField *)[self.view viewWithTag:103];
//    UITextField * address       = (UITextField *)[self.view viewWithTag:105];
////    //UITextField * smsOption     = (UITextField *)[self.view viewWithTag:106];
////  //  UITextField * gender        = (UITextField *)[self.view viewWithTag:107];
//   UITextField * zipCode       = (UITextField *)[self.view viewWithTag:110];
//   UITextField * city          = (UITextField *)[self.view viewWithTag:111];
//  //  UITextField * emailOpt      = (UITextField *)[self.view viewWithTag:112];
//    UITextField * country      = (UITextField *)[self.view viewWithTag:116];
//    UITextField * bonus      = (UITextField *)[self.view viewWithTag:117];
//
//    
//    NSLog(@"email is %@",email.text);
//   NSString * message;
//    
//if([[dictIsMadatory valueForKey:@"FirstName"]integerValue] == 1 && firstName.text.length == 0)
//    {
//        message = @"First Name cannot be blank";
//        [self alertViewDelegate:message];
//    }
// else if([[dictIsMadatory valueForKey:@"LastName"]integerValue] == 1 && lastname.text.length == 0)
//    {
//        message = @"Last Name cannot be blank";
//        [self alertViewDelegate:message];
//    }
//  else if ([[dictIsMadatory valueForKey:@"EmailAddress"]integerValue] == 1 && email.text.length == 0)
//    {
//        message = @"Email cannot be blank";
//        [self alertViewDelegate:message];
//    }
//  else  if(![self validateEmailWithString:email.text])
//    {
//        message = @"Please enter a valid Email ID.";
//        [self alertViewDelegate:message];
//    }
//  else if ([[dictIsMadatory valueForKey:@"PhoneNumber"]integerValue] == 1 && phoneNumber.text.length == 0)
//    {
//        message = @"PhoneNumber cannot be blank.";
//        [self alertViewDelegate:message];
//    }
//   else if([[dictIsMadatory valueForKey:@"Password"]integerValue] == 1 && password.text.length == 0)
//    {
//        message = @"Password cannot be blank";
//       [self alertViewDelegate:message];
//    }
//   else if ([[dictIsMadatory valueForKey:@"Address"]integerValue] == 1 && address.text.length == 0)
//    {
//        message = @"Address cannot be blank.";
//        [self alertViewDelegate:message];
//    }
//   else if ([[dictIsMadatory valueForKey:@"City"]integerValue] == 1 && city.text.length == 0)
//    {
//        message = @"City cannot be blank.";
//        [self alertViewDelegate:message];
//    }
//   else  if ([[dictIsMadatory valueForKey:@"ZipCode"]integerValue] == 1 && zipCode.text.length == 0)
//    {
//        message = @"ZipCode cannot be blank.";
//        [self alertViewDelegate:message];
//    }
//   else if ([[dictIsMadatory valueForKey:@"Country"]integerValue] == 1 && country.text.length == 0)
//    {
//        message = @"Country cannot be blank.";
//        [self alertViewDelegate:message];
//    }
//   else if ([[dictIsMadatory valueForKey:@"State"]integerValue] == 1 && state.text.length == 0)
//    {
//        message = @"State cannot be blank.";
//        [self alertViewDelegate:message];
//    }
//   else if ([[dictIsMadatory valueForKey:@"FavoriteStore"]integerValue] == 1 && favouritStore.text.length == 0)
//   {
//        message = @"Store cannot be blank.";
//        [self alertViewDelegate:message];
//   }
//   else if ([[dictIsMadatory valueForKey:@"DOB"]integerValue] == 1 && dob.text.length == 0)
//   {
//       message = @"DOB cannot be blank.";
//       [self alertViewDelegate:message];
//   }
//   else if ([[dictIsMadatory valueForKey:@"EmailOptIn"]integerValue] == 1 && [emailSelect isEqualToString:@"false"])
//   {
//       message = @"Select email option";
//       [self alertViewDelegate:message];
//   }
//   else if ([[dictIsMadatory valueForKey:@"SMSOptIn"]integerValue] == 1 && [smsSelection isEqualToString:@"false"])
//   {
//       message = @"Select SMS option";
//       [self alertViewDelegate:message];
//   }
//   else if ([[dictIsMadatory valueForKey:@"pushOptIn"]integerValue] == 1 && [pushSelection isEqualToString:@"false"])
//   {
//       message = @"Select push option";
//       [self alertViewDelegate:message];
//   }
//   else if ([[dictIsMadatory valueForKey:@"Bonus"]integerValue] == 1 && bonus.text.length == 0)
//   {
//       message = @"Select bonus point";
//       [self alertViewDelegate:message];
//   }
//    else
//    {
//        if([obj checkNetworkConnection])
//        {
//            // call mobile Setting Api
//            [self postApiData];
//        }
//        else
//        {
//            [self alertViewDelegate:@"Please check your network connection"];
//        }
//
//    }
}


#pragma mark - Register Api

-(void)postApiData
{
    [obj addLoadingView:self.view];
    UITextField *state;
    
    apiCall=[ApiClasses sharedManager];
    NSMutableDictionary *dict = [NSMutableDictionary new];
    
    for (int i = 0; i<arrKeyName.count; i++) {
        
        NSString *strTemp =  [arrKeyName objectAtIndex:i];
        UITextField *txtField =  (UITextField *)[self.view viewWithTag:300+i];
     
        NSLog(@"strTemp is %@",strTemp);
        
        if ([strTemp isEqualToString:@"FirstName"])
        {
            [dict setValue:txtField.text forKey:@"firstName"];
            
        }
        else if ([strTemp isEqualToString:@"LastName"])
        {
            [dict setValue:txtField.text forKey:@"lastName"];
            
        }
        else if ([strTemp isEqualToString:@"EmailAddress"])
        {
            [dict setValue:txtField.text forKey:@"email"];
            
        }
        else if ([strTemp isEqualToString:@"PhoneNumber"])
        {
            [dict setValue:txtField.text forKey:@"phone"];
            
        }
        else if ([strTemp isEqualToString:@"Password"])
        {
            [dict setValue:txtField.text forKey:@"password"];
            
        }
        else if ([strTemp isEqualToString:@"FavoriteStore"])
        {
            [dict setValue:txtField.text forKey:@"favoriteStore"];
            
        }
        else if ([strTemp isEqualToString:@"State"])
        {
            [dict setValue:txtField.text forKey:@"addressStreet"];
            
        }
        else if ([strTemp isEqualToString:@"Country"])
        {
            [dict setValue:txtField.text forKey:@"addressCountry"];
            
        }
        
        else if ([strTemp isEqualToString:@"City"])
        {
            [dict setValue:txtField.text forKey:@"addressCity"];
            
        }
        
        else if ([strTemp isEqualToString:@"DOB"])
        {
            [dict setValue:txtField.text forKey:@"birthDate"];
            
        }
        else if ([strTemp isEqualToString:@"Address"])
        {
            [dict setValue:txtField.text forKey:@"addressCity"];
            
        }
        else if ([strTemp isEqualToString:@"Gender"])
        {
            [dict setValue:genderSelection forKey:@"gender"];
            
        }
        else if ([strTemp isEqualToString:@"ZipCode"])
        {
            [dict setValue:txtField.text forKey:@"addressZipCode"];
            
        }
        else if ([strTemp isEqualToString:@"SMSOptIn"])
        {
            [dict setValue:smsSelection forKey:@"smsOptIn"];
            
        }
        else if ([strTemp isEqualToString:@"EmailOptIn"])
        {
            [dict setValue:emailSelect forKey:@"emailOptIn"];
            
        }
        else if ([strTemp isEqualToString:@"pushOptIn"])
        {
            [dict setValue:pushSelection forKey:@"pushOptIn"];
            
        }
        else if ([strTemp isEqualToString:@"State"])
        {
            state = txtField;
        }
        else if ([strTemp isEqualToString:@"Bonus"])
        {
            
            if(rewardRules.length!=0)
            {
                NSLog(@"rewards rule value ------- %@",rewardRules);
                
                NSString * checkBool;
                if ([rewardRules integerValue] == 0)
                {
                    checkBool = @"false";
                }
                else
                {
                    checkBool = @"true";
                }
                
                [dict setValue:checkBool forKey:@"rewardRule"];
                [dict setValue:ruleID forKey:@"ruleId"];
            }

        }
        
        else
        {
            
            [dict setValue:txtField.text forKey:[[[resultArray objectAtIndex:i] valueForKey:@"databaseName"] lowercaseString]];
            NSLog(@"dictCheck is %@",dictCheck.description);
            
            if ([[NSString stringWithFormat:@"%@", [[resultArray objectAtIndex:i]valueForKey:@"profileFieldType"]] isEqualToString:@"checkbox"])
            {
               
                if([[dictCheck valueForKey:[NSString stringWithFormat:@"%ld",(long)txtField.tag]] isEqualToString:@""] || [dictCheck valueForKey:[NSString stringWithFormat:@"%ld",(long)txtField.tag]] == (NSString*)[NSNull null]  || dictCheck.count == 0)
                    {
                        NSLog(@"profileFieldType is %@",@"one");

                        UIButton *btnTemp = (UIButton *)[self.view viewWithTag:txtField.tag+4000];
                        if(btnTemp.selected == YES)
                        {
                            [dict setValue:@"true" forKey:[[[resultArray objectAtIndex:i] valueForKey:@"databaseName"] lowercaseString]];

                        }
                        else
                        {
                            [dict setValue:@"false" forKey:[[[resultArray objectAtIndex:i] valueForKey:@"databaseName"] lowercaseString]];
                        }
                    }
                    else
                    {
                        NSLog(@"profileFieldType is %@",@"0");

                        [dict setValue:[dictCheck valueForKey:[NSString stringWithFormat:@"%ld",(long)txtField.tag]] forKey:[[[resultArray objectAtIndex:i] valueForKey:@"databaseName"] lowercaseString]];

                    }
            }
            
            
        }
        
    }
    
    NSString * stateCode = [[NSUserDefaults standardUserDefaults]valueForKey:@"StateCode"];
    
    if(stateCode.length!=0)
    {
        [dict setValue:stateCode forKey:@"addressState"];
    }
    else
    {
        [dict setValue:state.text forKey:@"addressState"];
    }

    
    //device appID
    NSString *bundleIdentifier = [[NSBundle mainBundle] bundleIdentifier];
    [dict setValue:bundleIdentifier forKey:@"appId"];

    
    //deviceType
    [dict setValue:[UIDevice currentDevice].model forKey:@"deviceType"];
    
    //deviceOSVersion
    [dict setValue:[UIDevice currentDevice].systemVersion forKey:@"deviceOSVersion"];
    
    
     //PushToken
    NSString *strToken = [[NSUserDefaults standardUserDefaults]valueForKey:@"pushToken"];
    
    if (strToken.length!=0)
    {
          [dict setValue:strToken forKey:@"pushToken"];
    }
    else
    {
        [dict setValue:@"fc17d980c180eba2994b2a3c13f024f3b0947d33077573a513b47d9814e23226" forKey:@"pushToken"];
    }
    
    //DeviceID
    NSString *strDeviceID = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
    strDeviceID = [strDeviceID stringByReplacingOccurrencesOfString:@"-" withString:@""];
    strDeviceID = [NSString stringWithFormat:@"%@",strDeviceID];
    
    
    if(strDeviceID.length!=0)
  {
      [dict setValue:strDeviceID forKey:@"deviceId"];
  }
  else
  {
      [dict setValue:@"FAD187A162244A1EA2823A519226360A" forKey:@"deviceId"];
  }
    
    if(storeNumberValue.length!=0)
    {
        if([storeNumberValue intValue]==0)
        {
             [dict setValue:storeNumberString forKey:@"storeCode"];
             [dict setValue:storeNumberString forKey:@"favoriteStore"];
        }
        else
        {
            
         [dict setValue:[NSString stringWithFormat:@"%d",[storeNumberValue intValue]] forKey:@"storeCode"];
        [dict setValue:[NSString stringWithFormat:@"%d",[storeNumberValue intValue]] forKey:@"favoriteStore"];
        }
    }
    else
    {
         [dict setValue:@"474" forKey:@"storeCode"];
         [dict setValue:@"474" forKey:@"favoriteStore"];
    }
    
    // current date and time
    NSDateFormatter *dateFormatter=[[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ss"];
    [dict setValue:[dateFormatter stringFromDate:[NSDate date]] forKey:@"eventDateTime"];
    
    
    NSLog(@"print Dictionary %@", dict.description);
  
    [apiCall registerAPI:dict url:@"/member/create" withTarget:self withSelector:@selector(RegisterApi:)];
    dict = nil;
    apiCall = nil;
}


// api response

-(void)RegisterApi:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"registration response ---------- %@",response);
    
    if(response != nil)
    {
    NSLog(@"response------%@",response);
        
    if([[response valueForKey:@"successFlag"]integerValue]==1)
    {
         [self storeNameAndAddress];
        // event tracking method
        
        clpsdkObj = [clpsdk sharedInstanceWithAPIKey];
        UITextField * firstName = (UITextField *)[self.view viewWithTag:104];
        NSMutableDictionary * eventDic = [NSMutableDictionary new];
        [eventDic setValue:@"SIGN_UP_COMPLETE" forKey:@"event_name"];
        [eventDic setValue:firstName.text forKey:@"item_name"];
        [clpsdkObj updateAppEvent:eventDic];
        eventDic = nil;
        
        
        [self alertViewDelegateIndex:[response valueForKey:@"message"]];
    }
    else
    {
        [self alertViewDelegate:[response valueForKey:@"message"]];
    }
    }
}

// favourit store address and store name value
-(void)storeNameAndAddress
{
    // BottomView * bottomObj = [BottomView sharedBottomView];
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
        
    [[NSUserDefaults standardUserDefaults]setValue:[[str1 objectAtIndex:0]valueForKey:@"phone"] forKey:@"phoneNumber"];
        NSLog(@"SDK demo store name=%@",[[str1 objectAtIndex:0]valueForKey:@"address"]);
        
    }
    obj.bottomObj.lblStoreName.text = storeName;
}




// alert view method

-(void)alertViewDelegateIndex:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                                                              //[self.delegate startBtnAction];
                [self.navigationController popViewControllerAnimated:YES];
                                                          }];
    
    [alertController addAction:defaultAction];
    [self presentViewController:alertController animated:YES completion:nil];
}




#pragma mark - Alert View Method

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:ok];
    
    [self presentViewController:alertController animated:YES completion:nil];
}



# pragma mark - termsConditionsBtn Action

- (IBAction)termsConditionsBtn_Action:(id)sender
{
    TermsAndConditionsViewController * termsObj = [[TermsAndConditionsViewController alloc]initWithNibName:@"TermsAndConditionsViewController" bundle:nil];
    [self.navigationController pushViewController:termsObj animated:YES];
}


# pragma mark - PrivacyPolicy Action

- (IBAction)privacyPolicyBtn_Action:(id)sender
{
    PrivacyPolicyView * termsObj = [[PrivacyPolicyView alloc]initWithNibName:@"PrivacyPolicyView" bundle:nil];
    [self.navigationController pushViewController:termsObj animated:YES];
}


# pragma mark - Back Button Action

- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

# pragma mark - TextField Delegate Method

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    
    [self.datePickerView setHidden:YES];
    
    if(textField.tag == 100)
    {
        
    }
    else if (textField.tag == 111)
    {
        [(UITextField*)[self.view viewWithTag:111]resignFirstResponder];
    }
    else if (textField.tag == 112)
    {
        [(UITextField*)[self.view viewWithTag:112]resignFirstResponder];
    }
    else if (textField.tag == 113)
    {
        [(UITextField*)[self.view viewWithTag:113]resignFirstResponder];
    }
 
    [textField resignFirstResponder];
    return YES;
}


- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    
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


- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    if ([textField.placeholder isEqualToString:@"DOB"] || textField.tag ==103)
    {
        [textField resignFirstResponder];
        [self.datePickerView setHidden:NO];
    }
    return YES;
}


-(void)textFieldDidBeginEditing:(UITextField *)textField {
    
    if ([textField.placeholder isEqualToString:@"DOB"] || textField.tag ==103)
    {
        [textField resignFirstResponder];
        [self.datePickerView setHidden:NO];
    }
    else if ([textField.placeholder isEqualToString:@"Gender"] || textField.tag ==107)
    {
         [textField resignFirstResponder];
         [self.datePickerView setHidden:YES];

    }
    else if ([textField.placeholder isEqualToString:@"EmailOptIn"] || textField.tag ==112)
    {
        [textField resignFirstResponder];
        [self.datePickerView setHidden:YES];

    }
    else if ([textField.placeholder isEqualToString:@"I would like to receive text messages with offers and updates!"] || textField.tag ==106)
    {
        [self resignKeyboard];
        [self.datePickerView setHidden:YES];
    }
    else
    {
        [self.datePickerView setHidden:YES];
    }
}


#pragma mark - resign KeyBoard

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
    [(UITextField *)[self.view viewWithTag:114]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:115]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:116]resignFirstResponder];
    [(UITextField *)[self.view viewWithTag:117]resignFirstResponder];
}


#pragma  mark - date picker

- (IBAction)datePicker_Action:(id)sender
{
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"MM/dd/yyyy"];
    NSString *date = [dateFormat stringFromDate:self.datePicker.date];
    NSDate *todayDate = [NSDate date];
    [self.datePicker setMaximumDate: todayDate];
    UITextField * dob  =  (UITextField *)[self.view viewWithTag:103];
    dob.text = [NSString stringWithFormat:@"%@",date];
}

- (IBAction)pickerDoneButton_Action:(UIButton *)sender
{
    [self.datePickerView setHidden:YES];
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"MM/dd/yyyy"];
    NSString *date = [dateFormat stringFromDate:self.datePicker.date];
    NSDate *todayDate = [NSDate date];
    [self.datePicker setMaximumDate: todayDate];
    UITextField * dob  =  (UITextField *)[self.view viewWithTag:sender.tag];
    dob.text = [NSString stringWithFormat:@"%@",date];
    
    
    
}

#pragma mark - touch begun method

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.view endEditing:YES];
}

- (IBAction)btnAgree_Action:(id)sender
{
    if(isAgree == NO)
    {
        [self.btnAgree setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        [self.getStartedBtn setUserInteractionEnabled:YES];
        isAgree = YES;
    }
    else
    {
       [self.btnAgree setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        [self.getStartedBtn setUserInteractionEnabled:NO];
        isAgree = NO;
    }
}



@end
