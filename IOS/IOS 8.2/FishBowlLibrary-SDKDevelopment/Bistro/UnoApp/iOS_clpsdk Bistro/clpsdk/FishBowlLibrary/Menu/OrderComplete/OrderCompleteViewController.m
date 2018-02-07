//
//  OrderCompleteViewController.m
//  iOS_FBTemplate1
//
//  Created by HARSH on 30/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "OrderCompleteViewController.h"
#import "OrderPageViewController.h"
#import "StorelocatorViewController.h"
#import "PaymentCustomDatePickerPopUpView.h"
#import "PaymentScreenViewController.h"
//#import "Header.h"
#define kDateFormat        @"MM/dd/yyyy"
#define kTimeFormat        @"h:mm a"
#import "ModelClass.h"
@interface OrderCompleteViewController ()<UITextFieldDelegate,UIPickerViewDataSource,UIPickerViewDelegate,PaymentCustomDatePickerPopUpViewDelegate>
{
    BOOL                       orderClicked;
    NSMutableArray            *monthsArray;
    NSMutableArray            *yearsArray;
    NSMutableArray            *dayArray;
    NSString                  *daystr;
    NSString                  *monthstr;
    NSString                  *yearstr;
    NSString                  *strdate;
    NSMutableArray            *hourArray;
    NSMutableArray            *minArray;
    NSMutableArray            *secArray;
    NSString                  *hourstr;
    NSString                  *minstr;
    NSString                  *secstr;
    NSString                  *strTime;
    
    PaymentCustomDatePickerPopUpView *_creditCardExpireDatePickerPopupView;
    
    BOOL                       dateClicked;
    BOOL                       timeClicked;
    NSDate                    *myDate;
    ModelClass                *obj;
    UIViewController          *myController;
    
}
@property (weak, nonatomic) IBOutlet UIImageView        *imgBG;
@property (weak, nonatomic) IBOutlet UIButton           *pickUpDeliveryOutlet;

@property (weak, nonatomic) IBOutlet UIButton           *onlineDeliveryOutlet;
@property (strong, nonatomic) IBOutlet UILabel          *dateOutlet;

@property (strong, nonatomic) IBOutlet UILabel          *showTime;

@property (weak, nonatomic) IBOutlet UIButton           *continueOutlet;
@property (weak, nonatomic) IBOutlet UIView             *timeView;
@property (weak, nonatomic) IBOutlet UILabel            *cityOutlet;
@property (weak, nonatomic) IBOutlet UILabel            *addressOutlet;
@property (weak, nonatomic) IBOutlet UIImageView        *radioOnlinedeliveryOrderImage;
- (IBAction)radioOnlineDeliveryOrderButton:(id)sender;
@property (weak, nonatomic) IBOutlet UIImageView        *radioOnlinePick_upOrderImage;
@property (weak, nonatomic) IBOutlet UIView             *orderCompleteView;


@end

@implementation OrderCompleteViewController


#pragma mark - Creditcard Expire Date dismiss
- (void)dismissedPopup:(PaymentCustomDatePickerPopUpView*)popup withDate:(NSString*)date
{
    
    
    dateClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"showDate"]boolValue];
    if(dateClicked==YES)
    {
        
        _dateOutlet.text=[NSString stringWithFormat:@" %@",date];
        [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showDate"];
        [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showTime"];
        myDate=[self convertStringToDate:_dateOutlet.text];
    }
    timeClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"showTime"]boolValue];
    if(timeClicked==YES)
    {
        [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showDate"];
        [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showTime"];
       
        _showTime.text=[NSString stringWithFormat:@" %@",date];;
    }
    
   // [self shadoOffect:self.orderCompleteView];
    
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    
}


#pragma mark - Min max date
-(NSDate*)minMaxDate:(NSDate*)date day:(int)day{
    
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    
    NSDateComponents *comps = [[NSDateComponents alloc] init];
    
    [comps setDay:day];
    
    NSDate *resultDate = [gregorian dateByAddingComponents:comps toDate:date  options:0];
    
    NSLog(@"resultDate-----%@",resultDate);
    return resultDate;
}

#pragma mark - Convert Date to String
-(NSString*)convertDateToString:(NSDate*)date{
    NSDateFormatter *format = [NSDateFormatter new];
    [format setDateFormat:kDateFormat];
    return [format stringFromDate:date];
}
#pragma mark - Convert String to date
- (NSDate*)convertStringToDate:(NSString*)dateStr{
    NSDateFormatter *format = [NSDateFormatter new];
    [format setDateFormat:kDateFormat];
    return [format dateFromString:dateStr];
}
#pragma mark - Convert Time to String
-(NSString*)convertTimeToString:(NSDate*)date{
    NSDateFormatter *format = [NSDateFormatter new];
    
    [format setDateFormat:kTimeFormat];
    return [format stringFromDate:date];
}
#pragma mark - Convert String to Time
- (NSDate*)convertStringToTime:(NSString*)dateStr{
    NSDateFormatter *format = [NSDateFormatter new];
    [format setDateFormat:kTimeFormat];
    return [format dateFromString:dateStr];
}


#pragma mark - Change Location Method

- (IBAction)changeLocationAction:(id)sender
{
   // [self.navigationController popViewControllerAnimated:YES];
    
    if([self isViewControllerExist:[StorelocatorViewController class]])
    {
        [self.navigationController popToViewController:myController animated:NO];
    }
    else
    {
       // [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"MenuToStore"];
        StorelocatorViewController * menuObj = [[StorelocatorViewController alloc]initWithNibName:@"StorelocatorViewController" bundle:nil];
        [self.navigationController pushViewController:menuObj animated:YES];
    }
}


#pragma mark - ViewControllerExist Method

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


- (void)viewDidLoad
{
    [super viewDidLoad];
     obj=[ModelClass sharedManager];

    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self.imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
   
   // _onlineDeliveryOutlet.backgroundColor=[obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    // Do any additional setup after loading the view.
}
- (IBAction)backClicked:(id)sender
{
    
    // [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"fromStore"];

[self.navigationController popViewControllerAnimated:YES];
    
   
}
- (IBAction)dateClicked:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"showDate"];
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showTime"];
    
    NSDate *defaultDate=[self minMaxDate:[NSDate date] day:0];
    NSString *defaultDateString=[self convertDateToString:defaultDate];
    
    _creditCardExpireDatePickerPopupView = [PaymentCustomDatePickerPopUpView showInView:self.view withTitle:@"Choose the date" withSelectedDate:defaultDateString animated:YES];
    [_creditCardExpireDatePickerPopupView setDelegate:self];
    _creditCardExpireDatePickerPopupView.datePicker.minimumDate=[self minMaxDate:[NSDate date] day:0];
}
- (IBAction)timeClicked:(id)sender
{
//    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showDate"];
//    [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"showTime"];
//    
//    NSDate *defaultDate=[self minMaxDate:[NSDate date] day:0];
//    NSString *defaultTimeString=[self convertTimeToString:defaultDate];
//    _creditCardExpireDatePickerPopupView = [PaymentCustomDatePickerPopUpView showInView:self.view withTitle:@"Choose the Time" withSelectedDate:defaultTimeString animated:YES];
//    [_creditCardExpireDatePickerPopupView setDelegate:self];
//    _creditCardExpireDatePickerPopupView.datePicker.minimumDate=[self minMaxDate:[NSDate date] day:0];
}
- (IBAction)selectDateButton:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"showDate"];
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showTime"];
    
    NSDate *defaultDate=[self minMaxDate:[NSDate date] day:0];
    NSString *defaultDateString=[self convertDateToString:defaultDate];
    
    _creditCardExpireDatePickerPopupView = [PaymentCustomDatePickerPopUpView showInView:self.view withTitle:@"Choose the date" withSelectedDate:defaultDateString animated:YES];
    [_creditCardExpireDatePickerPopupView setDelegate:self];
    _creditCardExpireDatePickerPopupView.datePicker.minimumDate=[self minMaxDate:[NSDate date] day:0];
    myDate=[self convertStringToDate:_dateOutlet.text];
}

- (IBAction)selectTimeButton:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showDate"];
    [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"showTime"];
    
    NSDate *defaultDate=[self minMaxDate:[NSDate date] day:0];
    
    NSString *defaultTimeString=[self convertTimeToString:defaultDate];
    _creditCardExpireDatePickerPopupView = [PaymentCustomDatePickerPopUpView showInView:self.view withTitle:@"Choose the Time" withSelectedDate:defaultTimeString animated:YES];
    [_creditCardExpireDatePickerPopupView setDelegate:self];
    
    if ([myDate compare:[self minMaxDate:[NSDate date] day:0]] == NSOrderedDescending) {
        NSLog(@"date1 is later than date2");
    }
    
    else {
        _creditCardExpireDatePickerPopupView.datePicker.minimumDate=[self minMaxDate:[NSDate date] day:0];
        NSLog(@"dates are the same");
    }
  
}



- (IBAction)continueClicked:(id)sender
{
    BOOL fromMenuToStore=[[[NSUserDefaults standardUserDefaults]valueForKey:@"MenuToStore"]boolValue];
    if (fromMenuToStore==YES)
    {
        PaymentScreenViewController *toPayment=[[PaymentScreenViewController alloc]initWithNibName:@"PaymentScreenViewController" bundle:nil];
        [self.navigationController pushViewController:toPayment animated:YES];
    }
    else
    {
  OrderPageViewController * SubMenuObj = [[OrderPageViewController alloc]initWithNibName:@"OrderPageViewController" bundle:nil];
    
    SubMenuObj.strSelectAddress=_strSelectAddress;
    SubMenuObj.strSelectCity=_strSelectCity;
     [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"isFromrderComplete"];
      [self.navigationController pushViewController:SubMenuObj animated:NO];
    }
}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
   
    
        [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"isOnlineDelivery"];
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"isPickUpOrder"];
    NSUserDefaults *userDefaults=[NSUserDefaults standardUserDefaults];
     _cityOutlet.text=[userDefaults valueForKey:@"city"];
    _addressOutlet.text=[userDefaults valueForKey:@"address"];
    [userDefaults synchronize];
//    _cityOutlet.text=_strSelectCity;
//   
//    _addressOutlet.text=_strSelectAddress;
    
     _addressOutlet.adjustsFontSizeToFitWidth=YES;
    monthsArray=[[NSMutableArray alloc]init];
    
    hourArray=[[NSMutableArray alloc]initWithObjects:@"01",@"02",@"03",@"04",@"05",@"06",@"07",@"08",@"09",@"10",@"11",@"12",@"13",@"14",@"15",@"16",@"17",@"18",@"19",@"20",@"21",@"22",@"23",@"24",nil];
    minArray=[[NSMutableArray alloc]initWithObjects:@"01",@"02",@"03",@"04",@"05",@"06",@"07",@"08",@"09",@"10",@"11",@"12",@"13",@"14",@"15",@"16",@"17",@"18",@"19",@"20",@"21",@"22",@"23",@"24",@"25",@"26",@"27",@"28",@"29",@"30",@"31",@"32",@"33",@"34",@"35",@"36",@"37",@"38",@"39",@"40",@"41",@"42",@"43",@"44",@"45",@"46",@"47",@"48",@"49",@"50",@"51",@"52",@"53",@"54",@"55",@"56",@"57",@"58",@"59",@"60",nil];
    secArray=[[NSMutableArray alloc]initWithObjects:@"01",@"02",@"03",@"04",@"05",@"06",@"07",@"08",@"09",@"10",@"11",@"12",@"13",@"14",@"15",@"16",@"17",@"18",@"19",@"20",@"21",@"22",@"23",@"24",@"25",@"26",@"27",@"28",@"29",@"30",@"31",@"32",@"33",@"34",@"35",@"36",@"37",@"38",@"39",@"40",@"41",@"42",@"43",@"44",@"45",@"46",@"47",@"48",@"49",@"50",@"51",@"52",@"53",@"54",@"55",@"56",@"57",@"58",@"59",@"60",nil];
    dayArray=[[NSMutableArray alloc]initWithObjects:@"01",@"02",@"03",@"04",@"05",@"06",@"07",@"08",@"09",@"10",@"11",@"12",@"13",@"14",@"15",@"16",@"17",@"18",@"19",@"20",@"21",@"22",@"23",@"24",@"25",@"26",@"27",@"28",@"29",@"30",@"31",nil];
    monthsArray=[[NSMutableArray alloc]initWithObjects:@"Jan",@"Feb",@"Mar",@"Apr",@"May",@"Jun",@"Jul",@"Aug",@"Sep",@"Oct",@"Nov",@"Dec",nil];
    NSDate *defaultTIME=[self minMaxDate:[NSDate date] day:0];
    NSString *defaultTimeString=[self convertTimeToString:defaultTIME];
    _showTime.text=[NSString stringWithFormat:@" %@",defaultTimeString];;
    NSDate *defaultDate=[self minMaxDate:[NSDate date] day:0];
    NSString *defaultDateString=[self convertDateToString:defaultDate];
    _dateOutlet.text=[NSString stringWithFormat:@" %@",defaultDateString];
    
    
    _dateOutlet.layer.borderWidth=.5f;
    _dateOutlet.layer.borderColor=[UIColor lightGrayColor].CGColor;
    _showTime.layer.borderWidth=.5f;
    _showTime.layer.borderColor=[UIColor lightGrayColor].CGColor;
    self.navigationController.navigationBarHidden=YES;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)continueToOrderPage:(id)sender
{
    
//    OrderPageViewController *order=[self.storyboard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
//    [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"orderComplete"];
//    [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"ORDERCONFIRMED"];
//    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
//    [userDefaults removeObjectForKey:@"additem"];
//    [userDefaults removeObjectForKey:@"costitem"];
//    [userDefaults removeObjectForKey:@"Descitem"];
//    [userDefaults removeObjectForKey:@"itemImageUrl"];
//    [userDefaults removeObjectForKey:@"quantityArray"];
//    [userDefaults removeObjectForKey:@"priceArray"];
//    [userDefaults removeObjectForKey:@"itemId"];
//    [userDefaults removeObjectForKey:@"countitem"];
//    [userDefaults synchronize];
//    NSUserDefaults *selectedCity=[NSUserDefaults standardUserDefaults];
//    [selectedCity setObject:_cityOutlet.text forKey:@"selectedCity"];
//    [selectedCity synchronize];
//    NSUserDefaults *selectedaddress=[NSUserDefaults standardUserDefaults];
//    [selectedaddress setObject:_addressOutlet.text forKey:@"selectedaddress"];
//    [selectedaddress synchronize];
//    
//    [self.navigationController pushViewController:order animated:NO];
    
    
    
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

- (IBAction)radioOnlineDeliveryOrderButton:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"isOnlineDelivery"];
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"isPickUpOrder"];
   

    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
   
    _onlineDeliveryOutlet.backgroundColor=[obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    _pickUpDeliveryOutlet.backgroundColor=[UIColor lightGrayColor]; /*#aaaaaa*/
    
    
//    [_radioOnlinedeliveryOrderImage setImage:[UIImage imageNamed:@"radio-enabled.png"]];
//    [_radioOnlinePick_upOrderImage setImage:[UIImage imageNamed:@"radio-disabled.png"]];
//    [_onlineDeliveryOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
//    [_pickUpDeliveryOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    
    
}
- (IBAction)radioOnlinePick_UpOrderButton:(id)sender
{
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"isOnlineDelivery"];
    [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"isPickUpOrder"];
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    _pickUpDeliveryOutlet.backgroundColor= [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    _onlineDeliveryOutlet.backgroundColor=[UIColor lightGrayColor]; /*#ec2026*/
//    [_onlineDeliveryOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
//    [_pickUpDeliveryOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
}
@end
