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

@interface SignUpViewController ()<UITextFieldDelegate,clpSdkDelegate,UIGestureRecognizerDelegate,StoreSearch>
{
    __weak IBOutlet UIButton *btnTerm;
    __weak IBOutlet UIButton *btnPrivacy;
    NSArray * arr2;
    ApiClasses  * apiCall;
    ModelClass  * obj;
    NSMutableArray * mobileSetting;
    BOOL isCheckEmail;
    BOOL isCheckSms;
    NSString * genderSelection;
    NSString * emailSelect;
    NSString * smsSelection;
    NSMutableArray * arrMandatoryFields;
    NSMutableArray * arrAddFields;
    BOOL isEmailVerify;
    BOOL isPushOpton;
    clpsdk * clpsdkObj;
    NSString * storeCode;
    BOOL  isFavouritSrore;
    NSString * storeNameValue;
    NSString * storeNumberValue;
    NSString * storeNumberString;
    
}
@property (weak, nonatomic) IBOutlet UIImageView *companyLogoImage;

@property (weak, nonatomic) IBOutlet UIButton *doneBtn;
@property (weak, nonatomic) IBOutlet UILabel *signUPLoyality;
@property (weak, nonatomic) IBOutlet UIDatePicker *datePicker;
@property (weak, nonatomic) IBOutlet UIView *datePickerView;

@end

@implementation SignUpViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    clpsdkObj = [[clpsdk alloc]init];
    clpsdkObj.delegate = self;
    

    [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"SignUp"];
    
    mobileSetting = [[NSMutableArray alloc]init];
    
    isCheckSms = NO;
    isCheckEmail = NO;
    isEmailVerify = NO;
    isPushOpton = NO;
    
    [self.datePickerView setHidden:YES];

 // button corner radius
     self.getStartedBtn.layer.masksToBounds = YES;
     self.getStartedBtn.layer.cornerRadius = self.getStartedBtn.frame.size.height/2.0;;
     self.getStartedBtn.layer.borderWidth = 1.0;
     self.getStartedBtn.layer.borderColor = [[UIColor clearColor] CGColor];
    
   // main view corner radius
    self.mainViewOL.layer.masksToBounds = YES;
    self.mainViewOL.layer.cornerRadius = 4.0;
    self.mainViewOL.layer.borderWidth = 1.0;
    self.mainViewOL.layer.borderColor = [[UIColor clearColor] CGColor];


    // data fetch from mobilesetting
     obj=[ModelClass sharedManager];
    
     //NSString * str = [obj reterieveuserDefaultData:@"mobileSetting"];
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    
     NSLog(@"str value------%@",[dict valueForKey:@"registrationFields"]);
    
      arr2 = [dict valueForKey:@"registrationFields"];

    // text fields
    
       arrAddFields = [NSMutableArray new];
      arrMandatoryFields = [NSMutableArray new];

    
    if(arr2.count!=0)
    {
        for(int i= 0 ; i<[arr2 count]; i++)
        {
           if([[[arr2 objectAtIndex:i]valueForKey:@"visible"]integerValue] == 1)
           {
               [arrAddFields addObject:[[arr2 objectAtIndex:i]valueForKey:@"field"]];
               [arrMandatoryFields addObject:[[arr2 objectAtIndex:i]valueForKey:@"mandatory"]];
            }
        }
    }
    
//    FirstName,
//    EmailAddress,
//    PhoneNumber,
//    Password,
//    DOB,
//    Gender,
//    Address,
//    State,
//    City,
//    ZipCode,
//    FavoriteStore,
//    EmailOptIn,
//    SMSOptIn
    
    self.textFieldScroll.backgroundColor = [UIColor clearColor];
    
    int y= 147;
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenWidth = screenRect.size.width;
    CGFloat screenHeight = screenRect.size.height;
    
   if(arrAddFields.count>0)
   {
    for(int i = 0; i<[arrAddFields count]; i++)
    {
        UITextField *textField;
       
        
          if(screenWidth == 320 && screenHeight == 480)
          {
            textField  = [[UITextField alloc] initWithFrame:CGRectMake(20, y, 280, 30)];
          }
          else if (screenWidth == 320 && screenHeight == 568)
          {
              textField  = [[UITextField alloc] initWithFrame:CGRectMake(20, y, 280, 30)];
          }
          else if (screenWidth == 375 && screenHeight == 667)
          {
              textField  = [[UITextField alloc] initWithFrame:CGRectMake(20, y, 335, 36)];
          }
          else if (screenWidth == 414 && screenHeight == 736)
          {
              textField  = [[UITextField alloc] initWithFrame:CGRectMake(30, y, 354, 33)];
          }
            
            textField.borderStyle = UITextBorderStyleNone;
            //textField.font = [UIFont systemFontOfSize:13];
            textField.font = [UIFont fontWithName:@"Avant Garde XLight" size:13];
            textField.backgroundColor = [UIColor whiteColor];
            textField.alpha = 0.8f;
            textField.placeholder = [arrAddFields objectAtIndex:i];
            textField.userInteractionEnabled = YES;
        
        [textField setValue:[UIColor darkGrayColor]
                    forKeyPath:@"_placeholderLabel.textColor"];

            if([textField.placeholder isEqualToString:@"PhoneNumber"])
            {
                textField.tag = 109;
                textField.keyboardType = UIKeyboardTypeNumberPad;
            }
          else if([textField.placeholder isEqualToString:@"EmailAddress"])
            {
                textField.tag = 101;
                textField.keyboardType = UIKeyboardTypeEmailAddress;
            }
          else if([textField.placeholder isEqualToString:@"LastName"])
          {
              textField.tag = 100;
          }
          else if([textField.placeholder isEqualToString:@"State"])
          {
              textField.tag = 102;
          }
          else if([textField.placeholder isEqualToString:@"DOB"])
          {
              textField.tag = 103;
              
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
              male.tag =4005;
              [textField addSubview:male];
              
          }
          else if([textField.placeholder isEqualToString:@"FirstName"])
          {
              textField.tag = 104;
          }
          else if([textField.placeholder isEqualToString:@"Address"])
          {
              textField.tag = 105;
          }
          else if([textField.placeholder isEqualToString:@"SMSOptIn"])
          {
              textField.tag = 106;
              
              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
              
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
              [textField addSubview:male1];
              
              UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
             
            [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox"] forState:UIControlStateNormal];
            [male addTarget:self action:@selector(btnSMSOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
            male.tag =4001;
              smsSelection = @"false";
              
              
              if(screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 320 && screenHeight == 568)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 375 && screenHeight == 667)
              {
                    male1.frame = CGRectMake(0, 0, 335, 35);
                    male.frame = CGRectMake(300,8, 20, 20);
              }
              else if (screenWidth == 414 && screenHeight == 736)
              {
                  male1.frame = CGRectMake(0, 0, 354, 33);
                  male.frame = CGRectMake(300, 5, 20, 20);
              }
              
            [textField addSubview:male];

          }
          else if([textField.placeholder isEqualToString:@"Gender"])
          {
              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
              male1.frame = CGRectMake(0, 0, 335, 33);
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
              [textField addSubview:male1];
              
              
              //textField.userInteractionEnabled = NO;
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
              label.font = [UIFont fontWithName:@"Helvetica" size:13.0];
              label.text = @"Male";
              [textField addSubview:label];
              
              UIButton *female = [UIButton buttonWithType:UIButtonTypeCustom];
              female.frame = CGRectMake(170, 5, 20, 20);
              [female setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
              [female addTarget:self action:@selector(btnFemale_Action:) forControlEvents:UIControlEventTouchUpInside];
              female.clipsToBounds = YES;
               female.tag = 2001;
              [female bringSubviewToFront:self.view];
              
               label = [[UILabel alloc] initWithFrame:CGRectMake(195, 8, 50, 16)];
              label.backgroundColor = [UIColor clearColor];
              label.font = [UIFont fontWithName:@"Helvetica" size:13.0];
              label.textColor = [UIColor blackColor];
              label.text = @"Female";
              [textField addSubview:label];
              
              genderSelection = @"Male";
   
              [textField addSubview:male];
              [textField addSubview:female];
              
              textField.tag = 107;
          }
          else if([textField.placeholder isEqualToString:@"FavoriteStore"])
          {
              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
              //male1.frame = CGRectMake(0, 0, 335, 32);
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
              male1.tag =7001;
              [textField addSubview:male1];
              
              UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
             // male.frame = CGRectMake(300, 8, 15, 15);
              [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
              
            [male addTarget:self action:@selector(FavouritBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
            [textField addSubview:male];
              
              
              
              if(screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 320 && screenHeight == 568)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 375 && screenHeight == 667)
              {
                  male1.frame = CGRectMake(0, 0, 335, 35);
                  male.frame = CGRectMake(300, 8, 20, 20);
              }
              else if (screenWidth == 414 && screenHeight == 736)
              {
                  male1.frame = CGRectMake(0, 0, 354, 33);
                  male.frame = CGRectMake(300, 5, 20, 20);
              }
   
              isFavouritSrore = YES;
              textField.tag = 108;
          }
          else if([textField.placeholder isEqualToString:@"ZipCode"])
          {
              textField.tag = 110;
          }
          else if([textField.placeholder isEqualToString:@"City"])
          {
              textField.tag = 111;
          }
          else if([textField.placeholder isEqualToString:@"EmailOptIn"])
          {
              textField.tag = 112;
              
              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
            //  male1.frame = CGRectMake(0, 0, 335, 33);
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
              [textField addSubview:male1];
              
              UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
              //male.frame = CGRectMake(300, 5, 20, 20);
              [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox"] forState:UIControlStateNormal];
            [male addTarget:self action:@selector(EmailOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
              male.tag =3001;
              
              
              if(screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 320 && screenHeight == 568)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 375 && screenHeight == 667)
              {
                  male1.frame = CGRectMake(0, 0, 335, 35);
                  male.frame = CGRectMake(300, 8, 20, 20);
              }
              else if (screenWidth == 414 && screenHeight == 736)
              {
                  male1.frame = CGRectMake(0, 0, 354, 33);
                  male.frame = CGRectMake(300, 5, 20, 20);
              }
              
              
              
              
              emailSelect = @"false";
              
              [textField addSubview:male];
              
          }
          else if([textField.placeholder isEqualToString:@"Password"])
          {
              textField.tag = 113;
              textField.secureTextEntry = YES;

          }
          else if([textField.placeholder isEqualToString:@"EmailVerify"])
          {
              textField.tag = 114;
              
              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
             // male1.frame = CGRectMake(0, 0, 335, 33);
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
              [textField addSubview:male1];
              
              UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
             // male.frame = CGRectMake(300, 5, 20, 20);
              [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox"] forState:UIControlStateNormal];
              [male addTarget:self action:@selector(btnEmailVerify:) forControlEvents:UIControlEventTouchUpInside];
              male.tag =3005;
            //  isEmailVerify = @"false";
              
              if(screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 320 && screenHeight == 568)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 375 && screenHeight == 667)
              {
                  male1.frame = CGRectMake(0, 0, 335, 35);
                  male.frame = CGRectMake(300, 8, 20, 20);
              }
              else if (screenWidth == 414 && screenHeight == 736)
              {
                  male1.frame = CGRectMake(0, 0, 354, 33);
                  male.frame = CGRectMake(300, 5, 20, 20);
              }//
              
              
              
              
              [textField addSubview:male];
   
          }
          else if([textField.placeholder isEqualToString:@"pushOptIn"])
          {
              textField.tag = 115;
              
              UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
              //male1.frame = CGRectMake(0, 0, 335, 33);
              male1.backgroundColor = [UIColor clearColor];
              [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
              [textField addSubview:male1];
              
              UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
              //male.frame = CGRectMake(300, 5, 20, 20);
              [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox"] forState:UIControlStateNormal];
              [male addTarget:self action:@selector(pushOptIn_Action:) forControlEvents:UIControlEventTouchUpInside];
              male.tag =3006;
              //isPushOpton = @"false";
              
              if(screenWidth == 320 && screenHeight == 480)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 320 && screenHeight == 568)
              {
                  male1.frame = CGRectMake(0, 0, 280, 30);
                  male.frame = CGRectMake(245, 5, 20, 20);
              }
              else if (screenWidth == 375 && screenHeight == 667)
              {
                  male1.frame = CGRectMake(0, 0, 335, 35);
                  male.frame = CGRectMake(300, 8, 20, 20);
              }
              else if (screenWidth == 414 && screenHeight == 736)
              {
                  male1.frame = CGRectMake(0, 0, 354, 33);
                  male.frame = CGRectMake(300, 5, 20, 20);
              }
              
              
              
              [textField addSubview:male];
          }
        
            else
            {
            textField.keyboardType = UIKeyboardTypeDefault;

            }
            
            textField.returnKeyType = UIReturnKeyDefault;
           
            textField.autocorrectionType = UITextAutocorrectionTypeNo;
            textField.autocapitalizationType = UITextAutocapitalizationTypeNone;

            textField.delegate = self;
            [obj addLeftPaddingToTextField:textField];
            [self addCornerRadius1:textField];
            [self.textFieldScroll addSubview:textField];
            y = y+30+15;
    }
       
   }

     UITextField * smsOption = (UITextField *)[self.view viewWithTag:106];
    
    if([smsOption.placeholder isEqualToString:@"SMSOptIn"])
    {
        smsOption.text = @"SMSOptIn";
        smsOption.textColor = [UIColor blackColor];

    }

     UITextField * gender = (UITextField *)[self.view viewWithTag:107];
    
    if([gender.placeholder isEqualToString:@"Gender"])
    {
        gender.text = @"Gender";
        gender.textColor = [UIColor blackColor];

    }

    UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
    
    if([emailOpt.placeholder isEqualToString:@"EmailOptIn"])
    {
        emailOpt.text = @"EmailOptIn";
        emailOpt.textColor = [UIColor blackColor];

    }
    
    
    UITextField * EmailVerify = (UITextField *)[self.view viewWithTag:114];
    
    if([EmailVerify.placeholder isEqualToString:@"EmailVerify"])
    {
        EmailVerify.text = @"EmailVerify";
        EmailVerify.textColor = [UIColor blackColor];
    }
    
    UITextField * pushOptIn = (UITextField *)[self.view viewWithTag:115];
    
    if([pushOptIn.placeholder isEqualToString:@"pushOptIn"])
    {
        pushOptIn.text = @"pushOptIn";
        pushOptIn.textColor = [UIColor blackColor];

    }
 
  
    

    if (screenWidth == 320 && screenHeight == 480)
    {
        btnTerm.frame = CGRectMake(btnTerm.frame.origin.x-27,33*arrAddFields.count+235, btnTerm.frame.size.width, btnTerm.frame.size.height);
        
        btnPrivacy.frame = CGRectMake(btnPrivacy.frame.origin.x+50,33*arrAddFields.count+235, btnPrivacy.frame.size.width, btnPrivacy.frame.size.height);
        
        self.getStartedBtn.frame = CGRectMake(self.getStartedBtn.frame.origin.x,btnTerm.frame.origin.y+btnTerm.frame.size.height+10, self.getStartedBtn.frame.size.width, self.getStartedBtn.frame.size.height);
        
        
        self.textFieldScroll.contentSize = CGSizeMake(self.mainViewOL.frame.size.width, 33*arrAddFields.count+310);
    }
    

  else  if (screenWidth == 320 && screenHeight == 568)
    {
        btnTerm.frame = CGRectMake(btnTerm.frame.origin.x-27,33*arrAddFields.count+149, btnTerm.frame.size.width, btnTerm.frame.size.height);
        btnPrivacy.frame = CGRectMake(btnPrivacy.frame.origin.x+50,33*arrAddFields.count+149, btnPrivacy.frame.size.width, btnPrivacy.frame.size.height);
        
        self.getStartedBtn.frame = CGRectMake(self.getStartedBtn.frame.origin.x,btnTerm.frame.origin.y+btnTerm.frame.size.height+8, self.getStartedBtn.frame.size.width, self.getStartedBtn.frame.size.height);
        
        
        self.textFieldScroll.contentSize = CGSizeMake(self.mainViewOL.frame.size.width, 33*arrAddFields.count+315);
    }
    
     else if (screenWidth == 375 && screenHeight == 667)
    {
        btnTerm.frame = CGRectMake(btnTerm.frame.origin.x-27,34*arrAddFields.count+78, btnTerm.frame.size.width, btnTerm.frame.size.height);
        
        btnPrivacy.frame = CGRectMake(btnPrivacy.frame.origin.x+50,34*arrAddFields.count+78, btnPrivacy.frame.size.width, btnPrivacy.frame.size.height);
        
        self.getStartedBtn.frame = CGRectMake(self.getStartedBtn.frame.origin.x,btnTerm.frame.origin.y+btnTerm.frame.size.height, self.getStartedBtn.frame.size.width, self.getStartedBtn.frame.size.height);
        self.textFieldScroll.contentSize = CGSizeMake(self.mainViewOL.frame.size.width, 34*arrAddFields.count+315);
    }
    
     else if (screenWidth == 414 && screenHeight == 736)
     {
         btnTerm.frame = CGRectMake(btnTerm.frame.origin.x-30,33*arrAddFields.count+54, btnTerm.frame.size.width, btnTerm.frame.size.height);
         btnPrivacy.frame = CGRectMake(btnPrivacy.frame.origin.x+50,33*arrAddFields.count+54, btnPrivacy.frame.size.width, btnPrivacy.frame.size.height);
         
         self.getStartedBtn.frame = CGRectMake(self.getStartedBtn.frame.origin.x,btnTerm.frame.origin.y+btnTerm.frame.size.height+7, self.getStartedBtn.frame.size.width, self.getStartedBtn.frame.size.height);
         self.textFieldScroll.contentSize = CGSizeMake(self.mainViewOL.frame.size.width, 33*arrAddFields.count+320);
     }
    

    // button background color
    self.getStartedBtn.backgroundColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    self.signUPLoyality.textColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
      [btnTerm setTitleColor:[obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]] forState:UIControlStateNormal];
    
    [btnPrivacy setTitleColor:[obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]] forState:UIControlStateNormal];
    
    [self.doneBtn setTitleColor:[obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]] forState:UIControlStateNormal];
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:
                   str2];
    [self.backgroundImg sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    //self.backgroundImg.image = [UIImage imageNamed:@"dashboard.png"];
    
    
    NSString * str = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    NSURL *url = [NSURL URLWithString:
                  str];
    [self.companyLogoImage sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
 
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
    
    
//    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(singleTapGestureCaptured:)];
//    singleTap.delegate=self;
//    [self.textFieldScroll addGestureRecognizer:singleTap];
    
    
   // [clpsdkObj  getALLStoreList];
    
}


// text field corner radius

//-(void)addCornerRadius:(UIView*)textField
//{
//    textField.layer.masksToBounds = YES;
//    textField.layer.cornerRadius = 2.0;
//    textField.layer.borderWidth = 0.5;
//    textField.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
//    
//}



//- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
//{
//    if ([touch.view isDescendantOfView:self.tblStore])
//    {
//        return NO;
//    }
//    
//    return YES;
//}
//
//- (void)singleTapGestureCaptured:(UITapGestureRecognizer *)gesture
//{
//    if (!self.tblStore.hidden) {
//        self.tblStore.hidden=YES;
//    }
//}




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
    
    if([storeNumberValue intValue]==0)
    {
        storeNumberString = storeNumber;
        
        NSLog(@" StoreNumber--------%@",storeNumber);
    }
    else
    {
        NSLog(@" StoreNumber--------%d",[storeNumberValue intValue]);
    }
    
     NSLog(@"storeNane And StoreNumber--------%@ %@",storeNameValue,storeNumberValue);
    
    
//    [[NSUserDefaults standardUserDefaults]setValue:storeNameValue forKey:@"storeNameD"];
//    [[NSUserDefaults standardUserDefaults]synchronize];
    
    
}



//
//// store list from store Api
//
//-(void)storeListResponseSucceed:(NSArray *)arryStore
//{
//    NSLog(@"array value------%@",arryStore);
//    
//    arrStoreName = [NSMutableArray new];
//    arrStoreCode = [NSMutableArray new];
//    
//    
//    if ((arryStore != nil) && (arryStore.count > 0))
//    {
//        for (CLPStore *store in arryStore)
//        {
//            [arrStoreName addObject:store.storeName];
//            [arrStoreCode addObject:[NSString stringWithFormat:@"%d",store.storeNumber]];
//        }
//    }
//    
//    NSLog(@"array value------%@ %@",arrStoreName , arrStoreCode);
//}


// resign button action
-(void)btnresign_Action:(id)sender
{
    NSLog(@"button table");
    [self resignKeyboard];
    [self.datePickerView setHidden:YES];
}



// Dob  resign keyboard button action
-(void)btnDOB_Action:(id)sender
{
    [self resignKeyboard];
    [self.datePickerView setHidden:NO];
}


// male button action
-(void)btnmale_Action:(id)sender
{
    [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
    [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
   // UITextField * gender = (UITextField *)[self.view viewWithTag:107];
     genderSelection = @"Male";
     [self resignKeyboard];
}


// female button action
-(void)btnFemale_Action:(id)sender
{
    [(UIButton *)[self.view viewWithTag:1001]setBackgroundImage:[UIImage imageNamed:@"radioUnSel"] forState:UIControlStateNormal];
    [(UIButton *)[self.view viewWithTag:2001]setBackgroundImage:[UIImage imageNamed:@"radioSel"] forState:UIControlStateNormal];
   // UITextField * gender = (UITextField *)[self.view viewWithTag:107];
      genderSelection = @"Female";
   [self resignKeyboard];
}


// email button action
-(void)EmailOptIn_Action:(id)sender
{
    if(isCheckEmail == NO)
    {
       [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
        //UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailSelect = @"true";
        isCheckEmail = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
       // UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
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
        //UITextField * smsOption = (UITextField *)[self.view viewWithTag:106];
        smsSelection = @"true";
        isCheckSms = YES;
    }
 else
   {
    [(UIButton *)[self.view viewWithTag:4001]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
       //UITextField * smsOption = (UITextField *)[self.view viewWithTag:106];
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
        //UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailSelect = @"true";
        isEmailVerify = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3005]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        // UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailSelect = @"false";
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
        //UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailSelect = @"true";
        isPushOpton = YES;
    }
    else
    {
        [(UIButton *)[self.view viewWithTag:3006]setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
        // UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
        emailSelect = @"false";
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
    
//    UITextField * phoneNumber = (UITextField *)[self.view viewWithTag:110];
//    [phoneNumber becomeFirstResponder];
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
    
    
    UITextField * lastname      = (UITextField *)[self.view viewWithTag:100];
    UITextField * email         = (UITextField *)[self.view viewWithTag:101];
   // UITextField * state         = (UITextField *)[self.view viewWithTag:102];
    //UITextField * dob           = (UITextField *)[self.view viewWithTag:103];
    UITextField * firstName     = (UITextField *)[self.view viewWithTag:104];
    UITextField * address       = (UITextField *)[self.view viewWithTag:105];
    //UITextField * smsOption     = (UITextField *)[self.view viewWithTag:106];
  //  UITextField * gender        = (UITextField *)[self.view viewWithTag:107];
  //  UITextField * favouritStore = (UITextField *)[self.view viewWithTag:108];
    UITextField * phoneNumber   = (UITextField *)[self.view viewWithTag:109];
    UITextField * zipCode       = (UITextField *)[self.view viewWithTag:110];
    UITextField * city          = (UITextField *)[self.view viewWithTag:111];
  //  UITextField * emailOpt      = (UITextField *)[self.view viewWithTag:112];
    
    
    
    //    FirstName,
    //    EmailAddress,
    //    PhoneNumber,
    //    Password,
    //    DOB,
    //    Gender,
    //    Address,
    //    State,
    //    City,
    //    ZipCode,
    //    FavoriteStore,
    //    EmailOptIn,
    //    SMSOptIn
    //    LastName
 
   NSString * message;
    
 //lastname.text = @"xyz";
    
    
    if(lastname.text.length == 0 && [self isMandatory:@"LastName"])
    {
        message = @"Last Name cannot be blank";
        [self alertViewDelegate:message];
    }
 else if(firstName.text.length == 0 && [self isMandatory:@"FirstName"])
    {
        message = @"First Name cannot be blank";
        [self alertViewDelegate:message];
    }
    else if (email.text.length == 0 && [self isMandatory:@"EmailAddress"])
    {
        message = @"Email cannot be blank";
        [self alertViewDelegate:message];
    }
    
    else if(![self validateEmailWithString:email.text])
    {
        message = @"Please enter a valid Email ID.";
        [self alertViewDelegate:message];
        
    }
    else if (phoneNumber.text.length == 0 && [self isMandatory:@"PhoneNumber"])
    {
        message = @"PhoneNumber cannot be blank.";
        [self alertViewDelegate:message];
    }
    
    else if (address.text.length == 0 && [self isMandatory:@"Address"])
    {
        message = @"Address cannot be blank.";
        [self alertViewDelegate:message];
    }
    
    else if (city.text.length == 0 && [self isMandatory:@"City"])
    {
        message = @"City cannot be blank.";
        [self alertViewDelegate:message];
    }
    
    else if (zipCode.text.length == 0 && [self isMandatory:@"ZipCode"])
    {
        message = @"ZipCode cannot be blank.";
        [self alertViewDelegate:message];
    }
    
    else
    {
        if([obj checkNetworkConnection])
        {
            // call mobile Setting Api
            [self postApiData];
        }
        
        else
        {
            [self alertViewDelegate:@"Please check your network connection"];
            
        }

    }
}


// register Api

-(void)postApiData
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
    //UITextField * smsOption = (UITextField *)[self.view viewWithTag:106];
    //UITextField * gender = (UITextField *)[self.view viewWithTag:107];
    UITextField * favouritStore = (UITextField *)[self.view viewWithTag:108];
    UITextField * phoneNumber = (UITextField *)[self.view viewWithTag:109];
    UITextField * zipCode = (UITextField *)[self.view viewWithTag:110];
    UITextField * city = (UITextField *)[self.view viewWithTag:111];
  //  UITextField * emailOpt = (UITextField *)[self.view viewWithTag:112];
    UITextField * password = (UITextField *)[self.view viewWithTag:113];

 
    [dict setValue:lastname.text forKey:@"lastName"];
    
    
    [dict setValue:email.text forKey:@"email"];
    
//    if(dob.text.length!=0)
//    {
       [dict setValue:dob.text forKey:@"birthDate"];
    //}
    
//    if(state.text.length!=0)
//    {
        [dict setValue:state.text forKey:@"addressState"];
    //}
    
//    if(smsOption.text.length!=0)
//    {
        [dict setValue:smsSelection forKey:@"smsOptIn"];
   // }
    
//    if(gender.text.length!=0)
//    {
        [dict setValue:genderSelection forKey:@"gender"];
   // }
    
//    if(emailOpt.text.length!=0)
//    {
        [dict setValue:emailSelect forKey:@"emailOptIn"];
   // }
    
//    if(favouritStore.text.length!=0)
//    {
        [dict setValue:favouritStore.text forKey:@"favoriteStore"];
    //}
    
    
    [dict setValue:firstName.text forKey:@"firstName"];
    [dict setValue:address.text forKey:@"addressCity"];
    [dict setValue:phoneNumber.text forKey:@"phone"];
    [dict setValue:zipCode.text forKey:@"addressZipCode"];
    [dict setValue:city.text forKey:@"addressCity"];
    [dict setValue:address.text forKey:@"addressStreet"];
    
    if(password.text.length != 0)
    {
    [dict setValue: password.text forKey:@"password"];
    }
    else
    {
    [dict setValue:@"gaurav" forKey:@"password"];
    }
    
//    NSString *string = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
//    
//    string = [string stringByReplacingOccurrencesOfString:@"-" withString:@""];
//    
//    string = [NSString stringWithFormat:@"%@",string];
    
    NSString *string = [[NSUserDefaults standardUserDefaults]valueForKey:@"pushToken"];
    
    if(string.length!=0)
  {
      [dict setValue:string forKey:@"deviceId"];
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
    
 
    [apiCall registerAPI:dict url:@"/member/create" withTarget:self withSelector:@selector(RegisterApi:)];
    dict = nil;
    apiCall = nil;
}


-(BOOL)isMandatory:(NSString*)textfieldName
{
    for(int i =0;i<[arr2 count]; i++)
    {
       if([[[arr2 objectAtIndex:i]valueForKey:@"visible"]integerValue] == 1)
       {
           if([arrAddFields containsObject:textfieldName])
           {
               if([[[arr2 objectAtIndex:i]valueForKey:@"mandatory"]integerValue]!=0)
               {
                   return YES;
               }
           }
       }
        
    }
    return NO;
}


// api response

-(void)RegisterApi:(id)response
{
    [obj removeLoadingView:self.view];
    
    if(response != nil)
    {
    NSLog(@"response------%@",response);
        
        
    if([[response valueForKey:@"successFlag"]integerValue]==1)
    {
        [self alertViewDelegateIndex:[response valueForKey:@"message"]];
        
    }
    else
    {
        [self alertViewDelegate:[response valueForKey:@"message"]];

    }
    }
}


// alert view method

-(void)alertViewDelegateIndex:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                                                              [self.delegate startBtnAction];
                                                          }];
    
    [alertController addAction:defaultAction];
    [self presentViewController:alertController animated:YES completion:nil];
}




// alert view method

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
    [self.delegate termsAndConditionAction:@"termsCondition"];
}


# pragma mark - PrivacyPolicy Action

- (IBAction)privacyPolicyBtn_Action:(id)sender
{
    [self.delegate privacyPolicyAction:@"privacyPolicy"];

}


# pragma mark - Back Button Action

- (IBAction)backBtn_Action:(id)sender
{
    [self.delegate backBtnAction];
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
    else if ([textField.placeholder isEqualToString:@"SMSOptIn"] || textField.tag ==106)
    {
                [self resignKeyboard];
                [self.datePickerView setHidden:YES];
    }
    else
    {
        [self.datePickerView setHidden:YES];

    }
}

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
}


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

- (IBAction)pickerDoneButton_Action:(id)sender
{
    [self.datePickerView setHidden:YES];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.view endEditing:YES];
}



@end
