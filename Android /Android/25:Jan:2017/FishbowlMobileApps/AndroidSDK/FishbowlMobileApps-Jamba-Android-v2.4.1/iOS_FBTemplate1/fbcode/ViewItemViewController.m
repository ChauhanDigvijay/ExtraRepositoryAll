//
//  ViewItemViewController.m
//  iOS_FBTemplate1
//
//  Created by HARSH on 08/01/16.
//  Copyright © 2016 HARSH. All rights reserved.
//

#import "ViewItemViewController.h"
#import "AFHTTPRequestOperationManager.h"
#import "UIImageView+AFNetworking.h"
#import "OrderPageViewController.h"
#import "UIImage+animatedGIF.h"
#import "MenuViewController.h"
#define ACCEPTABLE_CHARECTERS @"0123456789"
@interface ViewItemViewController ()<UITextFieldDelegate,UIPickerViewDataSource,UIPickerViewDelegate>
{
    NSArray *sizeArray;
    NSArray *sizeA;
    NSString *strSize;
    CGRect keyboardFrame;
    CGRect originalSIzeQtyFrame;
    CGRect mainViewFrame;
}
@property (weak, nonatomic) IBOutlet UIImageView *loadingImage;
@property (weak, nonatomic) IBOutlet UIView *descriptionView;
@property (weak, nonatomic) IBOutlet UIPickerView *sizePicker;
@property (weak, nonatomic) IBOutlet UIView *superViewMain;
@property (weak, nonatomic) IBOutlet UILabel *selectSizeLabelText;
@property (weak, nonatomic) IBOutlet UIButton *regulerCrustOutlet;
@property (weak, nonatomic) IBOutlet UIButton *wheatCrustOutlet;
@property (weak, nonatomic) IBOutlet UIButton *largeCrustOutlet;
@property (weak, nonatomic) IBOutlet UIButton *wholeLGOutlet;
@property (weak, nonatomic) IBOutlet UIButton *firstHalfOutlet;
@property (weak, nonatomic) IBOutlet UIButton *secondHalfOutlet;

@end

@implementation ViewItemViewController
- (IBAction)regulerCrustClicked:(id)sender
{
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
    [_wheatCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    [_largeCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
}
- (IBAction)wheatCrustClicked:(id)sender
{
    
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    [_wheatCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
    [_largeCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    
}
- (IBAction)largeCrustClicked:(id)sender
{
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    [_wheatCrustOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    [_largeCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
}
- (IBAction)wholeLGClicked:(id)sender
{
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
    [_firstHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    [_secondHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
}
- (IBAction)firstHalfClicked:(id)sender
{
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    [_firstHalfOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
    [_secondHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
}
- (IBAction)secondHalfClicked:(id)sender
{
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    [_firstHalfOutlet setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    [_secondHalfOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    _scrollView.contentSize= CGSizeMake([UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height*2);
    // Do any additional setup after loading the view.
}
-(void)callEditorderafterEdit
{
    
    //[userDefaultsArray setObject:quantityArray forKey:@"quantityArray"];
 
    
    NSString *strData=_selectQuantity.text;
    [[NSUserDefaults standardUserDefaults]setObject:strData forKey:@"updatedQuantity"];
    
}
-(void)tap:(UIGestureRecognizer*)gr
{
    NSString *strText=_selectQuantity.text;
    BOOL iseditorderClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"EDITORDERCLICKED"]boolValue];
    if(iseditorderClicked==YES)
    {
        
        [self callEditorderafterEdit];
    }
    else
    {
        NSUserDefaults *userDefaultsEdit = [NSUserDefaults standardUserDefaults];
        [userDefaultsEdit setObject:strText forKey:@"Editquantity"];
        
        [userDefaultsEdit synchronize];

    }
    [_scrollView setContentOffset:CGPointZero animated:YES];
    
    [_selectQuantity resignFirstResponder];
    
}
- (IBAction)addToOrder:(id)sender
{
    OrderPageViewController *toOrderPage=[self.storyboard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
    [[NSUserDefaults standardUserDefaults]setObject:strSize forKey:@"selectSize"];
    [self.navigationController pushViewController:toOrderPage animated:YES];
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    
    if (textField==_selectQuantity)
    {
        NSCharacterSet *cs = [[NSCharacterSet characterSetWithCharactersInString:ACCEPTABLE_CHARECTERS] invertedSet];
        
        NSString *filtered = [[string componentsSeparatedByCharactersInSet:cs] componentsJoinedByString:@""];
        
        
        
        
        
        
        return [string isEqualToString:filtered];
        
        //        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"UIAlertView" message:@"Please Enter a Valid Mobile number" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
        //        [alert show];
        
        
        
    }
    return YES;
    
}

-(void)cancelNumberPad:(id)sender
{
    [self.view endEditing:YES];
    NSString *strText=_selectQuantity.text;
    BOOL iseditorderClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"EDITORDERCLICKED"]boolValue];
    if(iseditorderClicked==YES)
    {
        
        [self callEditorderafterEdit];
    }
    else
    {
    NSUserDefaults *userDefaultsEdit = [NSUserDefaults standardUserDefaults];
    [userDefaultsEdit setObject:strText forKey:@"Editquantity"];
    
    [userDefaultsEdit synchronize];
    }
    [_scrollView setContentOffset:CGPointZero animated:YES];
    [_selectQuantity resignFirstResponder];
}
-(void)doneWithNumberPad:(id)sender
{
    NSLog(@"Done Clicked.");
    [self.view endEditing:YES];
    NSString *strText=_selectQuantity.text;
    BOOL iseditorderClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"EDITORDERCLICKED"]boolValue];
    if(iseditorderClicked==YES)
    {
        
        [self callEditorderafterEdit];
    }
    else
    {
    NSUserDefaults *userDefaultsEdit = [NSUserDefaults standardUserDefaults];
    [userDefaultsEdit setObject:strText forKey:@"Editquantity"];
    
    [userDefaultsEdit synchronize];
    }
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
    
    BOOL iseditorderClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"EDITORDERCLICKED"]boolValue];
    if(iseditorderClicked==YES)
    {
        
        [self callEditorderafterEdit];
    }
    else
    {

   
    }
    
    [_scrollView setContentOffset:CGPointZero animated:YES];
    
    [_selectQuantity resignFirstResponder];
    
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    return YES;
}
- (void)selectSizeMethod:(UITapGestureRecognizer *)tapGesture
{
    _sizePicker.hidden=NO;
    
    _selectSize.delegate=self;
    
    
}
- (IBAction)backClicked:(id)sender
{
    MenuViewController *add = [self.storyboard instantiateViewControllerWithIdentifier:@"MenuViewController"];
    
    [self.navigationController pushViewController:add animated:NO];
}

-(void)viewWillAppear:(BOOL)animated
{
    self.navigationItem.hidesBackButton=YES;
    _loadingImage.hidden=NO;
    NSURL *url = [[NSBundle mainBundle] URLForResource:@"loading_pizza" withExtension:@"gif"];
    self.loadingImage.image = [UIImage animatedImageWithAnimatedGIFURL:url];
    strSize=_selectSizeLabelText.text;
    [_regulerCrustOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
    [_wholeLGOutlet setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];

    UIButton *btnRight = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnRight setFrame:CGRectMake(0, 0,110, 20)];
    [btnRight setTitle:@"ADD TO CART"  forState:UIControlStateNormal];
    btnRight.titleLabel.font = [UIFont fontWithName:@"Futura-Medium" size:12.0f];
    [btnRight setTitleEdgeInsets:UIEdgeInsetsMake(0, 0.0, 0.0, -35)];
    [btnRight addTarget:self action:@selector(addToOrder:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *barBtnRight = [[UIBarButtonItem alloc] initWithCustomView:btnRight];
    [barBtnRight setTintColor:[UIColor whiteColor]];
    self.navigationItem.rightBarButtonItem=barBtnRight;
    self.navigationController.navigationBar.barTintColor=[UIColor colorWithRed:0.925 green:0.125 blue:0.149 alpha:1]; /*#ec2026*/
    self.navigationItem.title=@"VIEW ITEM";
    
 
    [self.navigationController.navigationBar setTitleTextAttributes:
     @{NSForegroundColorAttributeName:[UIColor whiteColor],
       NSFontAttributeName:[UIFont fontWithName:@"Futura-CondensedExtraBold" size:18.0f]}];
    
    UIButton *btnBack = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnBack setFrame:CGRectMake(0, 0,30, 44)];
    [btnBack setImage:[UIImage imageNamed:@"BackIcon"] forState:UIControlStateNormal];
    [btnBack setImageEdgeInsets:UIEdgeInsetsMake(0, -30, 0.0, 0)];
    [btnBack addTarget:self action:@selector(backClicked:) forControlEvents:UIControlEventTouchUpInside];
    // btnBack.titleLabel.font = [UIFont fontWithName:@"HelveticaNeue-Bold" size:14.0f];
    UIBarButtonItem *barBtnBack = [[UIBarButtonItem alloc] initWithCustomView:btnBack];
    [barBtnBack setTintColor:[UIColor whiteColor]];
    
    self.navigationItem.leftBarButtonItem=barBtnBack;
    [super viewWillAppear:YES];
    _selectQuantity.delegate=self;
    _selectSizeLabelText.userInteractionEnabled = YES;
    UITapGestureRecognizer *tapGestureSize =[[UITapGestureRecognizer alloc]
                                             initWithTarget:self action:@selector(selectSizeMethod:)];
    [_selectSizeLabelText addGestureRecognizer:tapGestureSize];
    
    _sizePicker.hidden=YES;
    sizeArray=[NSArray arrayWithObjects:@"SMALL",@"MEDIUM",@"LARGE",@"EXTRA LARGE",nil];
    sizeA=[NSArray arrayWithObjects:@"10",@"12",@"14",@"18",nil];
    NSString *strText=_selectQuantity.text;
    //  [quantityupdateArray replaceObjectAtIndex:index1 withObject:strText];
   // [[NSUserDefaults standardUserDefaults]setObject:@"YES" forKey:@"EDITORDERCLICKED"];
    BOOL iseditorderClicked=[[[NSUserDefaults standardUserDefaults]valueForKey:@"EDITORDERCLICKED"]boolValue];
    if(iseditorderClicked==YES)
    {
        
        NSString *strItem=[[NSUserDefaults standardUserDefaults]valueForKey:@"editAddItem"];
        NSString *editItemImageUrl=[[NSUserDefaults standardUserDefaults]valueForKey:@"editItemImageUrl"];
        NSString *editDescitem=[[NSUserDefaults standardUserDefaults]valueForKey:@"editDescitem"];
        NSString *editquantityArray=[[NSUserDefaults standardUserDefaults]valueForKey:@"editquantityArray"];
        
        NSLog(@"%@",strItem);
        NSLog(@"%@",editItemImageUrl);

        NSLog(@"%@",editDescitem);

        NSLog(@"%@",editquantityArray);

       // NSLog(@"%ld",(long)editIndex);
        _viewItemName.text=strItem;
        _viewItemDesc.numberOfLines=0;
        _viewItemDesc.adjustsFontSizeToFitWidth=YES;
        _viewItemDesc.text=editDescitem;
        _selectQuantity.text=editquantityArray;
        
        
        [self loadImage:editItemImageUrl];
        
    }
    else
    {
        NSUserDefaults *userDefaultsEdit = [NSUserDefaults standardUserDefaults];
        [userDefaultsEdit setObject:strText forKey:@"Editquantity"];
        
        [userDefaultsEdit synchronize];
        //    NSUserDefaults *userDefaultsArray = [NSUserDefaults standardUserDefaults];
        //    [userDefaultsArray setObject:additem forKey:@"additem"];
        //    [userDefaultsArray setObject:costitem forKey:@"costitem"];
        //    [userDefaultsArray setObject:Descitem forKey:@"Descitem"];
        //    [userDefaultsArray setObject:itemImageUrl forKey:@"itemImageUrl"];
        //    [userDefaultsArray setObject:quantityArray forKey:@"quantityArray"];
        //    [userDefaultsArray setObject:priceArray forKey:@"priceArray"];
        //    [userDefaultsArray setObject:itemId1 forKey:@"itemId1"];
        //    [userDefaultsArray setObject:countitem forKey:@"countitem"];
        //    [userDefaultsArray synchronize];
        
        
        
        
        
        
        
        
        //[[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"fromItemView"];
        NSUserDefaults *selectedItem=[NSUserDefaults standardUserDefaults];
        _viewItemName.text=[selectedItem objectForKey:@"selected"];
        [selectedItem synchronize];
        // _viewItemName.adjustsFontSizeToFitWidth=YES;
        _viewItemDesc.numberOfLines=0;
        _viewItemDesc.adjustsFontSizeToFitWidth=YES;
        
        NSUserDefaults *descSelectedItem=[NSUserDefaults standardUserDefaults];
        _viewItemDesc.text=[descSelectedItem objectForKey:@"descSelectedItem"];
        [descSelectedItem synchronize];
        
        _selectQuantity.text=strText;
        NSUserDefaults *imageurlSelecteditem=[NSUserDefaults standardUserDefaults];
        
        NSString *strImageUrl=[imageurlSelecteditem valueForKey:@"imageurlSelecteditem"];
        [imageurlSelecteditem synchronize];
        
        [self loadImage:strImageUrl];
    }
    
    //[defaultsValues setValue:[NSString stringWithFormat:@"%d",i] forKey:@"editIndex"];
    
    
}
-(void)loadImage:(NSString *)strImageUrl
{
    NSURL *myURL=[NSURL URLWithString:strImageUrl];
    NSURLRequest *myurl=[NSURLRequest requestWithURL:myURL];
    
    [_viewitemImage setImageWithURLRequest:myurl
                          placeholderImage:nil
                                   success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image)
     {
         _loadingImage.hidden=YES;
         _viewitemImage.alpha = 0.0;
         _viewitemImage.image = image;
         _viewitemImage.contentMode = UIViewContentModeScaleToFill;
         //    [_viewitemImage sizeToFit];
         [UIView animateWithDuration:0.25
                          animations:^
          {
              _viewitemImage.alpha = 1.0;
          }];
     }
                                   failure:^(NSURLRequest *operation, NSHTTPURLResponse *response,NSError *error)
     {
         _viewitemImage.image=nil;
         
     }];

}
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

// tell the picker how many rows are available for a given component
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    NSInteger rowsInComponent;
    if(component==0)
    {
        rowsInComponent=[sizeArray count];
    }
    
    return rowsInComponent;
    
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


//- (CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component
//{
//    CGFloat componentWidth ;
//    if(component==0)
//    {
//        componentWidth = 115;
//    }
//    return componentWidth;
//}
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    
    _selectSizeLabelText.text=[NSString stringWithFormat:@"%@",[sizeArray objectAtIndex:row]];
    NSLog(@"%@",[sizeArray objectAtIndex:row]);
    strSize=_selectSizeLabelText.text;
    _sizePicker.hidden=YES;
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)addToOrderAction:(id)sender
{
    //    OrderPageViewController *toOrderPage=[self.storyboard instantiateViewControllerWithIdentifier:@"OrderPageViewController"];
    //    [self.navigationController pushViewController:toOrderPage animated:YES];
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
