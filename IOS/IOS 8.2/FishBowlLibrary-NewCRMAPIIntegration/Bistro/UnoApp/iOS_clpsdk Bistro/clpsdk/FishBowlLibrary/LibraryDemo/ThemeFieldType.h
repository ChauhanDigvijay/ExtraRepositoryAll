//
//  ThemeFieldType.h
//  clpsdk
//
//  Created by Puneet  on 6/2/17.
//  Copyright © 2017 clyptech. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@protocol ThemeFieldDelegate <NSObject>

@optional

// Delegate optional methods

-(void)btnmale_Action:(id)sender;
-(void)btnFemale_Action:(id)sender;
-(void)btnresign_Action:(id)sender;
-(void)btnCustomCheck_Action:(UIButton *)sender;
-(void)onDatePickerValueChanged:(id)sender;
-(void)DatePickerDoneClicked:(UIDatePicker *)sender withValues:(NSString *)strValue;
-(void)DropDownBtn_Action:(NSMutableArray *)arrMutable withTxtFieldTag:(NSInteger)intTagValue;



@end


@interface ThemeFieldType : NSObject

@property (nonatomic, assign) id<ThemeFieldDelegate>themeFieldDelegate;

-(UITextField * )getFieldType:(NSString *)strProfileFieldType withTextFieldTag:(UITextField *)txtField withIndex:(NSInteger)intIndex withArrCusValues:(NSArray *)arrCusValues withDefaultValue:(id)isDefault;



@end
