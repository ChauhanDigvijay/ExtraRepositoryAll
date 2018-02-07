//
//  PaymentCustomDatePickerPopUpView.m
//  iOS_FBTemplate1
//
//  Created by Harsh on 04/01/15.
//  Copyright (c) 2015 Harsh. All rights reserved.
//

#import "PaymentCustomDatePickerPopUpView.h"

#define kDateFormat @"MM/dd/yyyy"
#define kTimeFormat        @"h:mm a"

#define TEXT_HIGHLIGHT_COLOR [UIColor colorWithRed:43.0f/255.0f green:222.0f/255.0f blue:115.0f/255.0f alpha:1.0f]
#define TEXT_NORMAL_COLOR [UIColor colorWithRed:43.0f/255.0f green:222.0f/255.0f blue:115.0f/255.0f alpha:1.0f]


@implementation PaymentCustomDatePickerPopUpView
{
    BOOL checkTime;
    BOOL checkDate;
}
/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect {
 // Drawing code
 }
 */

- (void)awakeFromNib
{
    self.backgroundColor=[[UIColor blackColor] colorWithAlphaComponent:.6];
    self.popupView.layer.cornerRadius = 5;
    self.popupView.layer.shadowOpacity = 0.8;
    self.popupView.layer.shadowOffset = CGSizeMake(0.0f, 0.0f);
    
    self.seletedDateLabel.textColor = TEXT_HIGHLIGHT_COLOR;
    checkDate=[[[NSUserDefaults standardUserDefaults]valueForKey:@"showDate"]boolValue];
    if(checkDate==YES)
    {
        _datePicker.datePickerMode = UIDatePickerModeDate;
    }
    checkTime=[[[NSUserDefaults standardUserDefaults]valueForKey:@"showTime"]boolValue];
    if(checkTime==YES)
    {
        
        
        _datePicker.datePickerMode = UIDatePickerModeTime;
    }
    [self.doneButton setTitleColor:TEXT_NORMAL_COLOR forState:UIControlStateNormal];
}

- (void)showAnimate
{
    self.transform = CGAffineTransformMakeScale(1.3, 1.3);
    self.alpha = 0;
    [UIView animateWithDuration:.25 animations:^{
        self.alpha = 1;
        self.transform = CGAffineTransformMakeScale(1, 1);
    }];
    
}

- (void)removeAnimate
{
    [UIView animateWithDuration:.25 animations:^{
        self.transform = CGAffineTransformMakeScale(1.3, 1.3);
        self.alpha = 0.0;
    } completion:^(BOOL finished) {
        if (finished) {
            [self removeFromSuperview];
        }
    }];
}

+ (PaymentCustomDatePickerPopUpView*)showInView:(UIView *)aView withTitle:(NSString*)title withSelectedDate:(NSString*)selDate animated:(BOOL)animated
{
    
    BOOL checkTime;
    BOOL checkDate;
    
    PaymentCustomDatePickerPopUpView *popup = [[[NSBundle mainBundle]loadNibNamed:@"PaymentCustomDatePickerPopUpView" owner:nil options:nil]firstObject];
    //dispatch_async(dispatch_get_main_queue(), ^{
    [aView addSubview:popup];
    popup.frame = aView.frame;
    popup.dateTitleLabel.text = title;
    
    
    checkTime=[[[NSUserDefaults standardUserDefaults]valueForKey:@"showTime"]boolValue];
    if(checkTime==YES)
    {
        
        
        NSDate *selectedTime = [self convertStringToTime:selDate];
        if(selectedTime)
        {
            [popup.datePicker setDate:selectedTime];
        }
    }
    checkDate=[[[NSUserDefaults standardUserDefaults]valueForKey:@"showDate"]boolValue];
    if(checkDate==YES)
    {
        
        NSDate *selectedDate = [self convertStringToDate:selDate];
        if(selectedDate){
            [popup.datePicker setDate:selectedDate];
        }
    }
    
    if (animated) {
        [popup showAnimate];
    }
    
    popup.seletedDateLabel.text = selDate;
    
    
    //});
    return popup;
}

#pragma mark - Action
- (IBAction)choosenDate:(UIDatePicker*)sender
{
    checkDate=[[[NSUserDefaults standardUserDefaults]valueForKey:@"showDate"]boolValue];
    if(checkDate==YES)
    {
        self.seletedDateLabel.text = [self convertDateToString:sender.date];
    }
    checkTime=[[[NSUserDefaults standardUserDefaults]valueForKey:@"showTime"]boolValue];
    if(checkTime==YES)
    {
        self.seletedDateLabel.text = [self convertTimeToString:sender.date];
    }
    
}

- (IBAction)closePopupView:(id)sender{
    [self removeAnimate];
    
    if(self.delegate){
        [self.delegate dismissedPopup:self withDate:self.seletedDateLabel.text];
    }
}


#pragma mark - Convert date to string
- (NSString*)convertDateToString:(NSDate*)date{
    NSDateFormatter *format = [NSDateFormatter new];
    [format setDateFormat:kDateFormat];
    return [format stringFromDate:date];
}

#pragma mark - Convert string to date
+ (NSDate*)convertStringToDate:(NSString*)dateStr{
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
+ (NSDate*)convertStringToTime:(NSString*)dateStr{
    NSDateFormatter *format = [NSDateFormatter new];
    [format setDateFormat:kTimeFormat];
    return [format dateFromString:dateStr];
}


#pragma mark - Dealloc
- (void)dealloc{
    _datePicker = nil;
    _seletedDateLabel = nil;
    _dateTitleLabel = nil;
    _popupView = nil;
    _delegate = nil;
    _doneButton = nil;
}
@end

