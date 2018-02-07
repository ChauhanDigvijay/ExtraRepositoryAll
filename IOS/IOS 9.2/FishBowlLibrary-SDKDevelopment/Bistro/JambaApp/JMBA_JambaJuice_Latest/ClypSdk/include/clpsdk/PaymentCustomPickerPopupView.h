//
//  PaymentCustomPickerPopupView.h
//  iOS_FBTemplate1
//
//  Created by Harsh on 04/01/15.
//  Copyright (c) 2015 Harsh. All rights reserved.
//

#import <UIKit/UIKit.h>

@class PaymentCustomPickerPopupView;
@protocol PaymentCustomPickerPopupViewDelegate <NSObject>
@optional
- (void)dismissedPopup:(NSString*)selectedDropDownValue selectedDropDownID:(NSString *)selectedDropDownID;
@end

@interface PaymentCustomPickerPopupView : UIView<UIPickerViewDataSource,UIPickerViewDelegate>
@property (nonatomic, weak) IBOutlet UILabel    *dropDownTitleLabel;
@property (nonatomic, weak) IBOutlet UILabel    *selectedDropDownValueLabel;
@property (nonatomic, weak) IBOutlet UIPickerView   *dropDownPickerView;
@property (nonatomic, weak) IBOutlet UIView     *popupView;
@property (nonatomic, weak) IBOutlet UIButton   *doneButton;

@property (nonatomic,retain) NSString *_selectedDropDownID;

@property(nonatomic,retain) NSMutableArray *_dropDownMutableArray;

@property (nonatomic, weak) id <PaymentCustomPickerPopupViewDelegate> delegate;


+(PaymentCustomPickerPopupView*)showInView:(UIView *)aView dropDownMutableArray:(NSMutableArray *)dropDownMutableArray  withTitle:(NSString*)title  animated:(BOOL)animated;
@end
