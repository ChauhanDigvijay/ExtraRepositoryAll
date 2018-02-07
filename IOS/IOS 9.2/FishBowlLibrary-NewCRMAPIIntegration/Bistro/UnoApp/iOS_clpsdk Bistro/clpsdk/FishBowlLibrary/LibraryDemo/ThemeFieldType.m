//
//  ThemeFieldType.m
//  clpsdk
//
//  Created by Puneet  on 6/2/17.
//  Copyright © 2017 clyptech. All rights reserved.
//

#import "ThemeFieldType.h"

@implementation ThemeFieldType

UIDatePicker *datePicker;
NSMutableArray *arrValues;



- (instancetype)init
{
    self = [super init];
    if (self) {
        
        arrValues = [[NSMutableArray alloc]init];
    }
    return self;
}


-(UITextField * )getFieldType:(NSString *)strProfileFieldType withTextFieldTag:(UITextField *)txtField withIndex:(NSInteger)intIndex withArrCusValues:(NSArray *)arrCusValues withDefaultValue:(id)isDefault
{
    
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenWidth = screenRect.size.width;
    CGFloat screenHeight = screenRect.size.height;
    

    if ([strProfileFieldType isEqualToString:@"text"])
    {
        
        NSString *strDefaultValue = (NSString*)isDefault;
        if(strDefaultValue != (NSString*)[NSNull null] && ![strDefaultValue isEqualToString:@""])
        {
            txtField.text = strDefaultValue;
        }
        NSLog(@"ohh yeahh Text");

    }
   
    else if ([strProfileFieldType isEqualToString:@"Legend"])
    {
        NSLog(@"ohh yeahh Legend");

    }
    else if ([strProfileFieldType isEqualToString:@"Textarea"])
    {
        NSLog(@"ohh yeahh TextArea");
        
        NSString *strDefaultValue = (NSString*)isDefault;
        txtField.text = strDefaultValue;

    }
    else if ([strProfileFieldType isEqualToString:@"dropdown"])
    {
        NSLog(@"ohh yeahh dropdown");
        [arrValues removeAllObjects];
        [arrValues addObjectsFromArray:arrCusValues];
        UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
        male1.backgroundColor = [UIColor clearColor];
        [male1 addTarget:self action:@selector(DropDownBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
        male1.tag = txtField.tag;
        [txtField addSubview:male1];
        NSLog(@"txtField Drop is %ld",(long)txtField.tag);
        
        UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];
        [male setBackgroundImage:[UIImage imageNamed:@"rightArrow"] forState:UIControlStateNormal];
    
        [male addTarget:self action:@selector(DropDownBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
        [txtField addSubview:male];
        male.tag = txtField.tag;
        
        
        NSLog(@"custom txtField tag is %ld",(long)txtField.tag);
        
        
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
    else if ([strProfileFieldType isEqualToString:@"checkbox"])
    {
        
        NSLog(@"ohh yeahh checkbox");
        
        NSLog(@"TextField is %@",txtField.placeholder);

        
        UIButton *male1 = [UIButton buttonWithType:UIButtonTypeCustom];
        
        male1.backgroundColor = [UIColor clearColor];
        [male1 addTarget:self action:@selector(btnresign_Action:) forControlEvents:UIControlEventTouchUpInside];
//        male1.tag =4001+ intIndex;
        
        

        UIButton *male = [UIButton buttonWithType:UIButtonTypeCustom];

            if([isDefault isEqualToString:@"true"])
             {
                 [male setBackgroundImage:[UIImage imageNamed:@"checked_checkbox.png"] forState:UIControlStateNormal];
                 NSLog(@"valies 2 is %ld",(long)[[[arrCusValues objectAtIndex:intIndex]valueForKey:@"defaultValue"] integerValue]);
                 male.selected = YES;



             }
            else
            {
                [male setBackgroundImage:[UIImage imageNamed:@"unchecked_checkbox.png"] forState:UIControlStateNormal];
                male.selected = NO;
                
                NSLog(@"valies 3 is %ld",(long)[[[arrCusValues objectAtIndex:intIndex]valueForKey:@"defaultValue"] integerValue]);

        }
        
        [male addTarget:self action:@selector(btnCustomCheck_Action:) forControlEvents:UIControlEventTouchUpInside];
//        male.tag =4001+ intIndex;
        male.tag = 4000+txtField.tag;
        
        
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
        
        
        [txtField addSubview:male1];
        [txtField addSubview:male];

        
    }
    else if ([strProfileFieldType isEqualToString:@"date"])
    {
        
        NSString *strDefaultValue = (NSString*)isDefault;
        NSLog(@"strDefaultValue is %@",strDefaultValue);
        NSDateFormatter * formatter = [[NSDateFormatter alloc]init];
        [formatter setDateFormat:@"mm/dd/yyyy"]; // yyyy-MM-dd'T'HH:mm:ssZ
        NSDate *date = [formatter dateFromString:strDefaultValue];
        [formatter setDateFormat:@"mm/dd/yyyy"];
        strDefaultValue = [formatter stringFromDate:date];
        NSLog(@"strDefaultValue is %@",strDefaultValue);
       // NSLog(@"date is %@",date);
        

        
        datePicker = [[UIDatePicker alloc] initWithFrame:CGRectZero];
        [datePicker setDatePickerMode:UIDatePickerModeDate];
        datePicker.tag = 9000+ txtField.tag;
        [datePicker addTarget:self action:@selector(onDatePickerValueChanged:) forControlEvents:UIControlEventValueChanged];
        if(strDefaultValue != nil)
        {
            txtField.text = strDefaultValue;
            datePicker.date = date;
        }
        
        txtField.inputView = datePicker;

//        txtField.tag = 9100 + intIndex;
        
        UIToolbar *keyboardDoneButtonView = [[UIToolbar alloc] init];
        [keyboardDoneButtonView sizeToFit];
        UIBarButtonItem *flexBarButton = [[UIBarButtonItem alloc]
                                          initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                          target:nil action:nil];
        
        UIBarButtonItem *doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"style:UIBarButtonItemStylePlain target:self
                                                                      action:@selector(DatePickerDoneClicked:)];
        doneButton.tag = 9200 + intIndex;
        
        keyboardDoneButtonView.items = @[flexBarButton, doneButton];
        txtField.inputAccessoryView = keyboardDoneButtonView;
    }
    
    
    return txtField;

}

-(void)DropDownBtn_Action:(UIButton *)sender
{
    
    NSLog(@"sender.tag is %ld",(long)sender.tag);
    NSLog(@" dic is %@",[arrValues objectAtIndex:sender.tag-300-1]);
    
    NSMutableArray *arrCustomValues = [[arrValues objectAtIndex:sender.tag-300]valueForKey:@"optionList"];
    
    NSMutableArray *arrSend = [[NSMutableArray alloc]init];
    
    
    for (NSDictionary *dictValues in arrCustomValues) {
        
        NSString *strValues = [dictValues valueForKey:@"displayName"];
        if([[dictValues valueForKey:@"active"] integerValue] == 1) {
            [arrSend addObject:strValues];
        }
    }
    
    NSLog(@"arrSend is %@",arrSend.description);


    if (self.themeFieldDelegate != nil || [self.themeFieldDelegate performSelector:@selector(btnresign_Action:)])
    {
        NSLog(@"sender.tag+1000 is %ld",(long)sender.tag-1000);

        [self.themeFieldDelegate DropDownBtn_Action:arrSend withTxtFieldTag:sender.tag];
    }
}

-(void)DatePickerDoneClicked:(id)sender
{
    
//    UIDatePicker *datePicker = (UIDatePicker *)sender;
    NSDate * date = datePicker.date;
    NSDateFormatter * formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"MM/dd/YYYY"]; // yyyy-MM-dd'T'HH:mm:ssZ
    NSString *strDate = [formatter stringFromDate:date];
    NSLog(@"strDate is %@",strDate);
    formatter = nil;

    
    if (self.themeFieldDelegate != nil || [self.themeFieldDelegate performSelector:@selector(DatePickerDoneClicked:withValues:)])
    {
        [self.themeFieldDelegate DatePickerDoneClicked:datePicker withValues:strDate];
    }
    
//    [datePicker removeFromSuperview];
    
    
    //[datePicker removeFromSuperview];
  //  datePicker = nil;
}

-(void)onDatePickerValueChanged:(id)sender
{
    if (self.themeFieldDelegate != nil || [self.themeFieldDelegate performSelector:@selector(onDatePickerValueChanged:)])
    {
        [self.themeFieldDelegate onDatePickerValueChanged:sender];
    }
}

-(void)btnCustomCheck_Action:(UIButton *)sender
{
    
    if (self.themeFieldDelegate != nil || [self.themeFieldDelegate performSelector:@selector(btnCustomCheck_Action:)])
    {
        [self.themeFieldDelegate btnCustomCheck_Action:sender];
    }
}

-(void)btnresign_Action:(id)sender
{
    if (self.themeFieldDelegate != nil || [self.themeFieldDelegate performSelector:@selector(btnresign_Action:)])
    {
        [self.themeFieldDelegate btnresign_Action:sender];
    }
}


- (void)btnmale_Action:(id)sender
{
    if (self.themeFieldDelegate != nil || [self.themeFieldDelegate performSelector:@selector(btnmale_Action:)])
    {
        [self.themeFieldDelegate btnmale_Action:sender];
    }
}

- (void)btnFemale_Action:(id)sender
{
    if (self.themeFieldDelegate != nil || [self.themeFieldDelegate performSelector:@selector(btnFemale_Action:)])
    {
        [self.themeFieldDelegate btnFemale_Action:sender];
    }
}





@end
