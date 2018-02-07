//
//  PaymentCustomPickerPopupView.m
//  iOS_FBTemplate1
//
//  Created by Harsh on 04/01/15.
//  Copyright (c) 2015 Harsh. All rights reserved.
//

#import "PaymentCustomPickerPopupView.h"

//#define TEXT_HIGHLIGHT_COLOR [UIColor colorWithRed:43.0f/255.0f green:222.0f/255.0f blue:115.0f/255.0f alpha:1.0f]
//#define TEXT_NORMAL_COLOR [UIColor colorWithRed:43.0f/255.0f green:222.0f/255.0f blue:115.0f/255.0f alpha:1.0f]
// Define the font name
//#define APP_FONT_NAME @"Helvetica Neue"

@implementation PaymentCustomPickerPopupView
@synthesize _dropDownMutableArray;
@synthesize _selectedDropDownID;

- (void)awakeFromNib
{
    
    self.backgroundColor=[[UIColor blackColor] colorWithAlphaComponent:.6];
    self.popupView.layer.cornerRadius = 5;
    self.popupView.layer.shadowOpacity = 0.8;
    self.popupView.layer.shadowOffset = CGSizeMake(0.0f, 0.0f);
    
  //  self.selectedDropDownValueLabel.textColor = TEXT_HIGHLIGHT_COLOR;
  //  [self.doneButton setTitleColor:TEXT_NORMAL_COLOR forState:UIControlStateNormal];
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




+(PaymentCustomPickerPopupView*)showInView:(UIView *)aView dropDownMutableArray:(NSMutableArray *)dropDownMutableArray  withTitle:(NSString*)title  animated:(BOOL)animated{
    PaymentCustomPickerPopupView *popup = [[[NSBundle mainBundle]loadNibNamed:@"PaymentCustomPickerPopupView" owner:nil options:nil]firstObject];
    [aView addSubview:popup];
    popup.frame = aView.frame;
    popup.dropDownTitleLabel.text = title;
    popup._dropDownMutableArray=dropDownMutableArray;
[popup.dropDownPickerView reloadAllComponents];
    
    
    popup.selectedDropDownValueLabel.text=[popup._dropDownMutableArray objectAtIndex:0];
    if (animated) {
        [popup showAnimate];
    }
    return popup;
}

- (IBAction)closePopupView:(id)sender{
    [self removeAnimate];
    if(self.delegate){
        [self.delegate dismissedPopup:_selectedDropDownValueLabel.text selectedDropDownID:_selectedDropDownID];
    }
}



#pragma mark - picker view delegate
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    return 1;
}
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    return _dropDownMutableArray.count;
}


-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
      _selectedDropDownValueLabel.text=[_dropDownMutableArray objectAtIndex:row];
    _selectedDropDownID=[NSString stringWithFormat:@"%ld",(long)row];
}

- (UIView*)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view{
    UILabel *pickerLabel = (UILabel*)view;
    if(pickerLabel==nil){
        pickerLabel = [UILabel new];
      //  [pickerLabel setFont:[UIFont fontWithName:APP_FONT_NAME size:16]];
       // [pickerLabel setTextColor:TEXT_NORMAL_COLOR];
        [pickerLabel setTextAlignment:NSTextAlignmentCenter];
    }
    [pickerLabel setText:[_dropDownMutableArray objectAtIndex:row]];
    return pickerLabel;
}

#pragma mark - Dealloc
- (void)dealloc{
    _dropDownPickerView = nil;
    _selectedDropDownValueLabel = nil;
    _dropDownTitleLabel = nil;
    _popupView = nil;
    _delegate = nil;
    _doneButton = nil;
}

@end
