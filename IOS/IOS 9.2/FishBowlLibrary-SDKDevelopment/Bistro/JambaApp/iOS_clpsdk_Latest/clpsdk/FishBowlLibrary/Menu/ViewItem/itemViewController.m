//
//  itemViewController.m
//  pageView
//
//  Created by Gourav Shukla on 16/08/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "itemViewController.h"
#import "ApiClasses.h"
#import "OrderPageViewController.h"
#import "ModelClass.h"
#define ACCEPTABLE_CHARECTERS @"0123456789"
@interface itemViewController ()<UITextFieldDelegate,UIPickerViewDelegate,editDelegate>
{
    NSString *strFinalCost;
   
    NSArray *sizeArray;
    NSArray *sizeA;
    ModelClass *obj;
    
}
@property (weak, nonatomic) IBOutlet UIImageView *imgBG;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgTop;

@property (weak, nonatomic) IBOutlet UILabel *lblShowSubMenuItemName;
@property (weak, nonatomic) IBOutlet UIButton *regulerCrustOutlet;
@property (weak, nonatomic) IBOutlet UIButton *wheatCrustOutlet;
@property (weak, nonatomic) IBOutlet UIButton *largeCrustOutlet;
@property (weak, nonatomic) IBOutlet UIButton *wholeLGOutlet;
@property (weak, nonatomic) IBOutlet UIButton *firstHalfOutlet;
@property (weak, nonatomic) IBOutlet UIButton *secondHalfOutlet;
@end

@implementation itemViewController
@synthesize selectedItemImageUrl,selectedIndex,selectedItemName,selectedItemICost,selectedItemDesc;


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidLoad
{
    
    [super viewDidLoad];
    obj=[ModelClass sharedManager];
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    //    NSString * str1 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    //    NSURL *url = [NSURL URLWithString:str1];
    //    [self.imgLogo sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // background image
//    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
//    NSURL *url2 = [NSURL URLWithString:str2];
//    [self.imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // header image
    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
    NSURL *url3 = [NSURL URLWithString:str3];
    [self.imgTop sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self.imgBG sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
}
- (IBAction)addToOrder:(id)sender
{
    
    int quantity3=[_selectQuantity.text intValue];
    
    if(quantity3 != 0)
    {
    OrderPageViewController * orderPageObj = [[OrderPageViewController alloc]initWithNibName:@"OrderPageViewController" bundle:nil];
    
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"isFromrderComplete"];
    orderPageObj.delegate = self;
    orderPageObj.isEditDone = isFromOrderEdit;
        
    NSLog(@"strFinalCost==%@",strFinalCost);
    if (selectedItemName.length!=0 && isFromOrderEdit == NO)
    {
        NSMutableDictionary *itemDict = [[NSMutableDictionary alloc]init];
        [itemDict setValue:selectedItemName forKey:@"iname"];
        [itemDict setValue:selectedItemICost forKey:@"icost"];
        
        int quantity2=[_selectQuantity.text intValue];
        NSLog(@"quantity2==%d",quantity2);
        NSLog(@"selected item cost ------ %@",selectedItemICost);
        
        NSString *myValu=[NSString stringWithString:selectedItemICost];
        
        float  intCost1=[myValu floatValue];
        float totalCost1=intCost1*quantity2;
        strFinalCost=[NSString stringWithFormat:@"%.02f",totalCost1];
        orderPageObj.selectedItemTotalPrice=strFinalCost;
        
        [itemDict setValue:selectedItemDesc forKey:@"idesc"];
        [itemDict setValue:_selectQuantity.text forKey:@"iQuantity"];
        [itemDict setValue:strFinalCost forKey:@"iPrice"];
        [itemDict setValue:_selectedItemId forKey:@"itemid"];
        [itemDict setValue:_lblselectSize.text forKey:@"iSize"];
        NSLog(@"obj.itemDictArray==%@",obj.itemDictArray);

        [obj.itemDictArray addObject:itemDict];
          NSLog(@"itemDict==%@",itemDict);
        NSLog(@"obj.itemDictArray==%@",obj.itemDictArray);
    }
    
    if (self.isFromMenu==YES)
    {
        self.isFromMenu=NO;
    }
    else
    {
        if(isFromOrderEdit == YES)
        {
             NSDictionary *DictEdit = [obj.itemDictArray objectAtIndex:obj.indexGlobal];
            [DictEdit setValue:_selectQuantity.text forKey:@"iQuantity"];
            [DictEdit setValue:_lblselectSize.text forKey:@"iSize"];
            
            int quantity2=[_selectQuantity.text intValue];
            NSLog(@"quantity2==%d",quantity2);
            NSLog(@"selected item cost ------ %@",selectedItemICost);

            NSString *myValu=[NSString stringWithString:selectedItemICost];
            float  intCost1=[myValu floatValue];
            float totalCost1=intCost1*quantity2;
            strFinalCost=[NSString stringWithFormat:@"%.02f",totalCost1];
            orderPageObj.selectedItemTotalPrice=strFinalCost;
            
             [DictEdit setValue:strFinalCost forKey:@"iPrice"];
            NSLog(@"obj.itemDictArray==%@",obj.itemDictArray);
            [obj.itemDictArray replaceObjectAtIndex:obj.indexGlobal withObject:DictEdit];
        }
    }
    [self.navigationController pushViewController:orderPageObj animated:YES];
    }
    else
    {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert!"
                                      message:@"Quantity may not be 0"
                                      preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                                  {
   
                                  }];
        
        [alert addAction:cancel1];
        [self presentViewController:alert animated:YES completion:nil];
    }
}



-(void)ProductEdited;
{
    isFromOrderEdit = YES;
}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    _lblselectSize.userInteractionEnabled = YES;
    UITapGestureRecognizer *tapGestureSize =[[UITapGestureRecognizer alloc]
                                             initWithTarget:self action:@selector(selectSizeMethod:)];
    [_lblselectSize addGestureRecognizer:tapGestureSize];
    
    _pickerSelectsize.hidden=YES;
    _selectQuantity.delegate=self;
    
    sizeArray=[NSArray arrayWithObjects:@"SMALL",@"MEDIUM",@"LARGE",@"EXTRA LARGE",nil];
    sizeA=[NSArray arrayWithObjects:@"10",@"12",@"14",@"18",nil];
    NSString *string = [selectedItemImageUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]];
    NSURL *url = [NSURL URLWithString:string];
    
    
    [_viewitemImage sd_setImageWithURL:url placeholderImage:nil];
    _viewItemName.text=_selectedmainMenuNameItem;
    _viewItemDesc.text=selectedItemDesc;
    _lblShowSubMenuItemName.text=selectedItemName;
}


- (void)selectSizeMethod:(UITapGestureRecognizer *)tapGesture
{
    _pickerSelectsize.hidden=NO;
    _pickerSelectsize.delegate=self;
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

// tell the picker how many rows are available for a given component
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    
    return sizeArray.count;
    
}
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    NSString * nameInRow;
    if(component==0)
    {
        nameInRow= [sizeArray objectAtIndex:row];
    }
    return nameInRow;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    _lblselectSize.text = [sizeArray objectAtIndex:row];
    pickerView.hidden = YES;
}

- (IBAction)regulerCrustClicked:(id)sender
{
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_wheatCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_largeCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
}
- (IBAction)wheatCrustClicked:(id)sender
{
    
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_wheatCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_largeCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    
}
- (IBAction)largeCrustClicked:(id)sender
{
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_wheatCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_largeCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
}
- (IBAction)wholeLGClicked:(id)sender
{
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_firstHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_secondHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
}
- (IBAction)firstHalfClicked:(id)sender
{
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_firstHalfOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_secondHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
}
- (IBAction)secondHalfClicked:(id)sender
{
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_firstHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_secondHalfOutlet setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
}
-(void)tap:(UIGestureRecognizer*)gr
{
    [_scrollView setContentOffset:CGPointZero animated:YES];
    
    [_selectQuantity resignFirstResponder];
    
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    if (textField==_selectQuantity)
    {
        NSCharacterSet *cs = [[NSCharacterSet characterSetWithCharactersInString:ACCEPTABLE_CHARECTERS] invertedSet];
        
        NSString *filtered = [[string componentsSeparatedByCharactersInSet:cs] componentsJoinedByString:@""];
        return [string isEqualToString:filtered];
    }
    return YES;
    
}

-(void)cancelNumberPad:(id)sender
{
    [self.view endEditing:YES];
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_selectQuantity resignFirstResponder];
}
-(void)doneWithNumberPad:(id)sender
{
    NSLog(@"Done Clicked.");
    [self.view endEditing:YES];
    
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_selectQuantity resignFirstResponder];
}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    UITapGestureRecognizer *tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tap:)];
    [self.view addGestureRecognizer:tap];
    
    UIToolbar* numberToolbar = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 50)];
    numberToolbar.barStyle = UIBarStyleBlackTranslucent;
    
    numberToolbar.items = [NSArray arrayWithObjects:
                           [[UIBarButtonItem alloc]initWithTitle:@"Cancel" style:UIBarButtonItemStyleBordered target:self action:@selector(cancelNumberPad:)],
                           [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil],
                           [[UIBarButtonItem alloc]initWithTitle:@"Done" style:UIBarButtonItemStyleDone target:self action:@selector(doneWithNumberPad:)],
                           nil];
    
    [numberToolbar sizeToFit];
    
    textField.inputAccessoryView = numberToolbar;
    numberToolbar.barTintColor=[UIColor colorWithRed:0.969 green:0.941 blue:0.902 alpha:1];
    if(textField==_selectQuantity)
    {
        CGPoint scrollPoint=CGPointMake(0, _sizeQuantityView.frame.origin.y-150);
        [_scrollView setContentOffset:scrollPoint animated:YES];
    }
    
    return YES;
}
- (void)textFieldDidEndEditing:(UITextField *)quantityText
{
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_selectQuantity resignFirstResponder];
}
- (IBAction)tapBack:(id)sender
{
[self.navigationController popViewControllerAnimated:true];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    return YES;
}

@end
