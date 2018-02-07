//
// PaymentCustomDatePickerPopUpView.h
//  iOS_FBTemplate1
//
//  Created by Harsh on 04/01/15.
//  Copyright (c) 2015 Harsh. All rights reserved.
//

#import <UIKit/UIKit.h>

@class PaymentCustomDatePickerPopUpView;
@protocol PaymentCustomDatePickerPopUpViewDelegate <NSObject>
@optional
- (void)dismissedPopup:(PaymentCustomDatePickerPopUpView*)popup withDate:(NSString*)date;
@end

@interface PaymentCustomDatePickerPopUpView : UIView


@property (nonatomic, weak) IBOutlet UILabel        *dateTitleLabel;
@property (nonatomic, weak) IBOutlet UILabel        *seletedDateLabel;
@property (nonatomic, weak) IBOutlet UIDatePicker   *datePicker;
@property (nonatomic, weak) IBOutlet UIView         *popupView;
@property (nonatomic, weak) IBOutlet UIButton       *doneButton;

@property (nonatomic, weak) id <PaymentCustomDatePickerPopUpViewDelegate> delegate;

+ (PaymentCustomDatePickerPopUpView*)showInView:(UIView *)aView withTitle:(NSString*)title withSelectedDate:(NSString*)selDate animated:(BOOL)animated;

@end