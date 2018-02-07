//
//  PaymentScreenViewController.m
//  iOS_FBTemplate1
//
//  Created by HARSH R on 12/30/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "PaymentScreenViewController.h"
#import "AFNetworking.h"
#import "PaymentTypeCollectionViewCell.h"
//#import "AppDelegate.h"
#import "ModelClass.h"
//#import "NonSignInView.h"
#import "HomeViewController.h"

#define kSpaceBetweenViews     10
#define kCollectionViewCellIdentifier @"PaymentTypeCollectionViewCell"
#define kDateFormat        @"MM/dd/yyyy"

enum paymentView{
    kSignedOnlineOrderView=0,
    kGuestOnlineOrderView=1,
    kSignedDeliveryOrderView=2,
    kGuestDeliveryOrderView=3
};

enum paymentPickerPopupView{
    kPaymentMethodTypePopUpPickerView=0,
    kDeliveryCountryPopupPickerView=1,
    kDeliveryStatePopupPickerView=2,
    kDeliveryAddressTypePopupPickerView=3
    
};

enum radioEnabled{
    kTrue=0,
    kFalse=1
};

@interface PaymentScreenViewController (){
    // Selected payment view
    NSInteger _paymentView;
    
    // Payment view sub frames
    CGRect _signInfoViewFrame;
    CGRect _customerInfoViewFrame;
    CGRect _passwordInfoViewFrame;
    CGRect _phonNumberInfoViewFrame;
    CGRect _creditCardInfoViewFrame;
    CGRect _deliveryAddressChoiceViewFrame;
    CGRect _deleiveryAddressInfoViewFrame;
    CGRect _submitOrderViewFrame;
    
    // Payment view content frame
    CGRect _contentViewFrame;
    
    // Payment view scroll frame
    CGRect _contentScrollViewFrame;
    
    // PaymentCustomPicker
    PaymentCustomPickerPopupView *_paymentCustomPickerPopupView;
    
    // Payment Date Picker
    PaymentCustomDatePickerPopUpView *_creditCardExpireDatePickerPopupView;
    
    
    
    // NSMutableArray
    NSMutableArray *_paymentMethodMutableArray;
    NSMutableArray *_countryMutableArray;
    NSMutableArray *_stateMutableArray;
    NSMutableArray *_deliveryAddressTypeMutableArray;
    //user info
    NSString *userName;
    NSString *userEmailId;
    NSString *userPassword;
    NSString *userPhoneNUm;
    //ispickup or isdelivery
    BOOL isPickUp;
    BOOL isOnlineDelevery;
    
    
    //plese enter label frame;
    CGRect _pleseEnterOnlineDeliveryFrame;
    ModelClass *obj;
    
}

@end

@implementation PaymentScreenViewController
-(void)viewWillDisappear:(BOOL)animated
{
 
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    //obj.itemDictArray=nil;
    [obj.itemDictArray removeAllObjects];

    [_primaryAddressRadioButton setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_newAddressRadioButton setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_phoneNumberRadioButton setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_newPhoneNumberRadioButton setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    
    pleaseProvideInfoView.hidden=YES;
    searchOutlet.hidden=NO;
    self.navigationController.navigationBarHidden=YES;
        BOOL test = [[[NSUserDefaults standardUserDefaults] objectForKey:@"firstTimeLogin"] boolValue];
    
    if (test)
    {
        _primaryAddressView.hidden=YES;
        _newAddressView.hidden=YES;
        _addressBottomLine.hidden=YES;
        _signInInfoLabel.text=[NSString stringWithFormat:@"You are signed in as %@. Not %@? Sign-out",userName,userName];
        _nameTextField.text=userName;
        _emailAddressTextField.text=userEmailId;
        _primaryPhoneNumberTextField.text=userPhoneNUm;
        if ([_primaryPhoneNumberTextField.text isEqualToString:@""]) {
            _primaryPhoneNumberTextField.text=@"enter new number";
        }
        isOnlineDelevery = [[[NSUserDefaults standardUserDefaults] objectForKey:@"isOnlineDelivery"] boolValue];
        isPickUp = [[[NSUserDefaults standardUserDefaults] objectForKey:@"isPickUpOrder"] boolValue];
        
        if (isPickUp==YES)
        {
            [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"isOnlineDelivery"];
            [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"isPickUpOrder"];
            
            [self signedOnlinePaymentOrderView];
            
            
        }
        else
        {
            
            [self signInOnlineDelivery];
            
        }
    }
    else
    {
        
        _primaryAddressView.hidden=YES;
        _newAddressView.hidden=YES;
        _addressBottomLine.hidden=YES;
        
        isOnlineDelevery = [[[NSUserDefaults standardUserDefaults] objectForKey:@"isOnlineDelivery"] boolValue];
        isPickUp = [[[NSUserDefaults standardUserDefaults] objectForKey:@"isPickUpOrder"] boolValue];
        
        if (isPickUp==YES)
        {
            
            [self guestOnlinePaymentOrderView];
            [_deliveryAddressChoiceView setHidden:YES];
        }
        else
        {
            [self guestOnlineDeliveryOrderView];
        }
        
    }
    NSString *strPrice=[[NSUserDefaults standardUserDefaults]valueForKey:@"totalcost"];
    
    _payAmountValueLabel.text=[NSString stringWithFormat:@"%@ deposite required",strPrice];
    _deliveryAddressValueLabel.text=[[NSUserDefaults standardUserDefaults]valueForKey:@"showAddress"];
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"isOnlineDelivery"];
    [[NSUserDefaults standardUserDefaults]setObject:@"NO" forKey:@"isPickUpOrder"];
    
    
    
   // [self shadoOffect:_headerView];
    
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    
}




-(void)signInOnlineDelivery
{
    _customerInfoViewFrame=CGRectMake(0,0,self.view.frame.size.width,_customerInfoView.frame.size.height);
    
    _phonNumberInfoViewFrame=CGRectMake(0,_customerInfoViewFrame.size.height+_customerInfoViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_phonNumberInfoView.frame.size.height);
    
    _deliveryAddressChoiceViewFrame=CGRectMake(0,_customerInfoViewFrame.origin.y+_customerInfoViewFrame.size.height+kSpaceBetweenViews,self.view.frame.size.width,_deliveryAddressChoiceView.frame.size.height);
    
    _deleiveryAddressInfoViewFrame=CGRectMake(0,_deliveryAddressChoiceViewFrame.size.height+_deliveryAddressChoiceViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_deleiveryAddressInfoView.frame.size.height);
    
    _creditCardInfoViewFrame=CGRectMake(0,_deleiveryAddressInfoViewFrame.size.height+_deleiveryAddressInfoViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_creditCardInfoView.frame.size.height);
    
    _submitOrderViewFrame=CGRectMake(0,_creditCardInfoViewFrame.origin.y+_creditCardInfoViewFrame.size.height+kSpaceBetweenViews+kSpaceBetweenViews, self.view.frame.size.width,_submitOrderView.frame.size.height);
    
    
    _contentViewFrame =CGRectMake(0,0,self.view.frame.size.width,_customerInfoViewFrame.size.height+_creditCardInfoViewFrame.size.height+_deliveryAddressChoiceViewFrame.size.height+_deleiveryAddressInfoViewFrame.size.height+_submitOrderViewFrame.size.height+kSpaceBetweenViews*7);
    
    [_contentView setFrame:_contentViewFrame];
    
    
    [_customerInfoView setFrame:_customerInfoViewFrame];
    [_phonNumberInfoView setFrame:_phonNumberInfoViewFrame];
    [_deliveryAddressChoiceView setFrame:_deliveryAddressChoiceViewFrame];
    [_deleiveryAddressInfoView setFrame:_deleiveryAddressInfoViewFrame];
    [_creditCardInfoView setFrame:_creditCardInfoViewFrame];
    [_submitOrderView setFrame:_submitOrderViewFrame];
    
    [_passwordInfoView setHidden:YES];
    [_signInfoView setHidden:YES];
    [_phonNumberInfoView setHidden:NO];
    [lpleseOnLIneLabelToEnterAddessNotSignIn setHidden:YES];
    [_creditCardExpireDateView setUserInteractionEnabled:YES];
    
    [_contentScrollView setContentSize:CGSizeMake(self.view.frame.size.width,_contentView.frame.size.height)];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    obj=[ModelClass sharedManager];
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:str2];
    [self->_backgroundImageView sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    _paymentView=kGuestDeliveryOrderView;
    
    //  kSignedOnlineOrderView=0,
    //  kGuestOnlineOrderView=1,
    //  kSignedDeliveryOrderView=2,
    //  kGuestDeliveryOrderView=3
    
    
    [self addTapGesture];
    [self updateFrame];
    
    [self textFieldsBorder];
    [self labelBorder];
    [self initializeArray];
    [self registerPaymentCollectionViewCell];
    
    [self setRadioButtonTags];
    
    // Notification function for the keyboard show or hide
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:nil];
    
    // update Frame
    // Do any additional setup after loading the view.
}

- (void)viewDidAppear:(BOOL)animated{
    _contentViewFrame.size.width = self.view.frame.size.width;
    [_contentView setFrame:_contentViewFrame];
    [super viewDidAppear:animated];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Keybord show hide functions
// Function for the keyboard view show notification
- (void)keyboardWillShow:(NSNotification *)notification{
    // Get the notification of the keyboard
    NSDictionary *info = [notification userInfo];
    CGRect keyBoardRect = [[info objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue];
    [_contentScrollView setContentInset:UIEdgeInsetsMake(0, 0, keyBoardRect.size.height, 0)];
    [_contentScrollView setShowsVerticalScrollIndicator:NO];
}
- (void)keyboardWillHide:(NSNotification*)notification{
    [self dismissKeyboard];
}

- (void)dismissKeyboard{
    [_contentScrollView setContentInset:UIEdgeInsetsZero];
    [self.view endEditing:YES];
}

- (void)dealloc{
    [[NSNotificationCenter defaultCenter]removeObserver:self];
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */


#pragma mark - Payment method type attachments
-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return 5;
}
-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView{
    return 1;
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    PaymentTypeCollectionViewCell *cell=[collectionView dequeueReusableCellWithReuseIdentifier:kCollectionViewCellIdentifier forIndexPath:indexPath];
    switch (indexPath.row) {
        case 0:
            [cell setImage:[UIImage imageNamed:@"Visa"]];
            break;
        case 1:
            [cell setImage:[UIImage imageNamed:@"American-Express"]];
            break;
        case 2:
            [cell setImage:[UIImage imageNamed:@"Dinners-Club"]];
            break;
        case 3:
            [cell setImage:[UIImage imageNamed:@"Discover"]];
            break;
        case 4:
            [cell setImage:[UIImage imageNamed:@"Master-Card"]];
            break;
        default:
            break;
    }
    return cell;
}
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    UIEdgeInsets insets = UIEdgeInsetsMake(0, collectionView.frame.size.width*0.05, 0, collectionView.frame.size.width*0.05);
    return insets;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section{
    return collectionView.frame.size.width*0.05;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section{
    return collectionView.frame.size.width*0.05;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    CGFloat width = (collectionView.frame.size.width-(collectionView.frame.size.width*0.05*6))/5;
    return CGSizeMake(width, width*0.5);
}

#pragma mark - Update frame
-(void)updateFrame{
    
    /*    [_backgroundImageView setFrame:CGRectMake(0,0,self.view.frame.size.width,self.view.frame.size.height)];
     [_headerView setFrame:CGRectMake(0,0,self.view.frame.size.width,64)];
     _contentScrollViewFrame=CGRectMake(0, _headerView.frame.size.height+_headerView.frame.origin.y,self.view.frame.size.width,self.view.frame.size.height-_headerView.frame.size.height);
     
     [_contentScrollView setFrame:_contentScrollViewFrame];*/
    
    
    
    switch (_paymentView) {
        case kSignedOnlineOrderView:
            [self signedOnlinePaymentOrderView];
            break;
        case kGuestOnlineOrderView:
            [self guestOnlinePaymentOrderView];
            break;
        case kSignedDeliveryOrderView:
            [self signedOnlineDeliveryOrderView];
            break;
        case kGuestDeliveryOrderView:
            [self guestOnlineDeliveryOrderView];
            break;
            
        default:
            break;
    }
    
}


#pragma mark - Signed online payment order view
-(void)signedOnlinePaymentOrderView{
    
    
    _customerInfoViewFrame=CGRectMake(0,0,self.view.frame.size.width,_customerInfoView.frame.size.height);
    
    
    
    _creditCardInfoViewFrame=CGRectMake(0,_customerInfoViewFrame.size.height+_customerInfoViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_creditCardInfoView.frame.size.height);
    
    _submitOrderViewFrame=CGRectMake(0,_creditCardInfoViewFrame.origin.y+_creditCardInfoViewFrame.size.height+kSpaceBetweenViews+kSpaceBetweenViews, self.view.frame.size.width,_submitOrderView.frame.size.height);
    
    _contentViewFrame =CGRectMake(0,0,self.view.frame.size.width,_signInfoViewFrame.size.height+_customerInfoViewFrame.size.height+_creditCardInfoViewFrame.size.height+_submitOrderViewFrame.size.height+kSpaceBetweenViews*5);
    
    [_contentView setFrame:_contentViewFrame];
    
    [_customerInfoView setFrame:_customerInfoViewFrame];
    [_phonNumberInfoView setFrame:_phonNumberInfoViewFrame];
    [_creditCardInfoView setFrame:_creditCardInfoViewFrame];
    [_submitOrderView setFrame:_submitOrderViewFrame];
    
    
    [_passwordInfoView setHidden:YES];
    [_phonNumberInfoView setHidden:YES];
    [_deliveryAddressChoiceView setHidden:YES];
    
    [_deleiveryAddressInfoView setHidden:YES];
    [lpleseOnLIneLabelToEnterAddessNotSignIn setHidden:YES];
    
    [_contentScrollView setContentSize:CGSizeMake(self.view.frame.size.width,_contentView.frame.size.height)];
}

#pragma mark - Guest online payment order view
-(void)guestOnlinePaymentOrderView{
    
    _customerInfoViewFrame=CGRectMake(0,0,self.view.frame.size.width,_customerInfoView.frame.size.height);
    
    _passwordInfoViewFrame=CGRectMake(0,_customerInfoViewFrame.size.height+_customerInfoViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_passwordInfoView.frame.size.height);
    
    _creditCardInfoViewFrame=CGRectMake(0,_passwordInfoViewFrame.size.height+_passwordInfoViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_creditCardInfoView.frame.size.height);
    
    _submitOrderViewFrame=CGRectMake(0,_creditCardInfoViewFrame.origin.y+_creditCardInfoViewFrame.size.height+kSpaceBetweenViews+kSpaceBetweenViews, self.view.frame.size.width,_submitOrderView.frame.size.height);
    
    _contentViewFrame =CGRectMake(0,0,self.view.frame.size.width,_customerInfoViewFrame.size.height+_passwordInfoViewFrame.size.height+_creditCardInfoViewFrame.size.height+_submitOrderViewFrame.size.height+kSpaceBetweenViews*5);
    
    [_contentView setFrame:_contentViewFrame];
    
    [_customerInfoView setFrame:_customerInfoViewFrame];
    [_passwordInfoView setFrame:_passwordInfoViewFrame];
    [_creditCardInfoView setFrame:_creditCardInfoViewFrame];
    [_submitOrderView setFrame:_submitOrderViewFrame];
    
    [_phonNumberInfoView setHidden:YES];
    [_deliveryAddressChoiceView setHidden:YES];
    [_deleiveryAddressInfoView setHidden:YES];
    [lpleseOnLIneLabelToEnterAddessNotSignIn setHidden:YES];
    [_contentScrollView setContentSize:CGSizeMake(self.view.frame.size.width,_contentView.frame.size.height)];
    
}

#pragma mark - Signed delivery payment order view
-(void)signedOnlineDeliveryOrderView{
    
    _customerInfoViewFrame=CGRectMake(0,0,self.view.frame.size.width,_customerInfoView.frame.size.height);
    
    _phonNumberInfoViewFrame=CGRectMake(0,_customerInfoViewFrame.size.height+_customerInfoViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_phonNumberInfoView.frame.size.height);
    
    _deliveryAddressChoiceViewFrame=CGRectMake(0,_phonNumberInfoViewFrame.origin.y+_phonNumberInfoViewFrame.size.height+kSpaceBetweenViews,self.view.frame.size.width,_deliveryAddressChoiceView.frame.size.height);
    
    _deleiveryAddressInfoViewFrame=CGRectMake(0,_deliveryAddressChoiceViewFrame.size.height+_deliveryAddressChoiceViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_deleiveryAddressInfoView.frame.size.height);
    
    _creditCardInfoViewFrame=CGRectMake(0,_deleiveryAddressInfoViewFrame.size.height+_deleiveryAddressInfoViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_creditCardInfoView.frame.size.height);
    
    _submitOrderViewFrame=CGRectMake(0,_creditCardInfoViewFrame.origin.y+_creditCardInfoViewFrame.size.height+kSpaceBetweenViews+kSpaceBetweenViews, self.view.frame.size.width,_submitOrderView.frame.size.height);
    
    _contentViewFrame =CGRectMake(0,0,self.view.frame.size.width,_signInfoViewFrame.size.height+_customerInfoViewFrame.size.height+_creditCardInfoViewFrame.size.height+_phonNumberInfoViewFrame.size.height+_deliveryAddressChoiceViewFrame.size.height+_deleiveryAddressInfoViewFrame.size.height+_submitOrderViewFrame.size.height+kSpaceBetweenViews*8);
    
    [_contentView setFrame:_contentViewFrame];
    
    
    [_customerInfoView setFrame:_customerInfoViewFrame];
    [_phonNumberInfoView setFrame:_phonNumberInfoViewFrame];
    [_deliveryAddressChoiceView setFrame:_deliveryAddressChoiceViewFrame];
    [_deleiveryAddressInfoView setFrame:_deleiveryAddressInfoViewFrame];
    [_creditCardInfoView setFrame:_creditCardInfoViewFrame];
    [_submitOrderView setFrame:_submitOrderViewFrame];
    
    
    [_passwordInfoView setHidden:YES];
    
    
    
    [_contentScrollView setContentSize:CGSizeMake(self.view.frame.size.width,_contentView.frame.size.height)];
    
}

#pragma mark - Guest delivery payment order view
-(void)guestOnlineDeliveryOrderView{
    _customerInfoViewFrame=CGRectMake(0,0,self.view.frame.size.width,_customerInfoView.frame.size.height);
    
    _passwordInfoViewFrame=CGRectMake(0,_customerInfoViewFrame.size.height+_customerInfoViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_passwordInfoView.frame.size.height);
    _pleseEnterOnlineDeliveryFrame=CGRectMake(20,_passwordInfoViewFrame.origin.y+_passwordInfoViewFrame.size.height+kSpaceBetweenViews,self.view.frame.size.width,lpleseOnLIneLabelToEnterAddessNotSignIn.frame.size.height);
    
    //    _deliveryAddressChoiceViewFrame=CGRectMake(0,_passwordInfoViewFrame.origin.y+_passwordInfoViewFrame.size.height+kSpaceBetweenViews,self.view.frame.size.width,_deliveryAddressChoiceView.frame.size.height);
    
    _deleiveryAddressInfoViewFrame=CGRectMake(0,_pleseEnterOnlineDeliveryFrame.size.height+_pleseEnterOnlineDeliveryFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_deleiveryAddressInfoView.frame.size.height);
    
    _creditCardInfoViewFrame=CGRectMake(0,_deleiveryAddressInfoViewFrame.size.height+_deleiveryAddressInfoViewFrame.origin.y+kSpaceBetweenViews,self.view.frame.size.width,_creditCardInfoView.frame.size.height);
    
    _submitOrderViewFrame=CGRectMake(0,_creditCardInfoViewFrame.origin.y+_creditCardInfoViewFrame.size.height+kSpaceBetweenViews+kSpaceBetweenViews, self.view.frame.size.width,_submitOrderView.frame.size.height);
    
    
    _contentViewFrame =CGRectMake(0,0,self.view.frame.size.width,_customerInfoViewFrame.size.height+_pleseEnterOnlineDeliveryFrame.size.height+_creditCardInfoViewFrame.size.height+_passwordInfoViewFrame.size.height+_deliveryAddressChoiceViewFrame.size.height+_deleiveryAddressInfoViewFrame.size.height+_submitOrderViewFrame.size.height+kSpaceBetweenViews*6);
    
    [_contentView setFrame:_contentViewFrame];
    
    
    [_customerInfoView setFrame:_customerInfoViewFrame];
    [_passwordInfoView setFrame:_passwordInfoViewFrame];
    
    [lpleseOnLIneLabelToEnterAddessNotSignIn setFrame:_pleseEnterOnlineDeliveryFrame];
    
    // [_deliveryAddressChoiceView setFrame:_deliveryAddressChoiceViewFrame];
    [_deleiveryAddressInfoView setFrame:_deleiveryAddressInfoViewFrame];
    [_creditCardInfoView setFrame:_creditCardInfoViewFrame];
    [_submitOrderView setFrame:_submitOrderViewFrame];
    
    
    [_signInfoView setHidden:YES];
    [_phonNumberInfoView setHidden:YES];
    [_deliveryAddressChoiceView setHidden:YES];
    [_creditCardExpireDateView setUserInteractionEnabled:YES];
    
    [_contentScrollView setContentSize:CGSizeMake(self.view.frame.size.width,_contentView.frame.size.height)];
    
}

#pragma mark - UILabelBorder
-(void)labelBorder{
    [self setBorderLabel:_paymentMethodTypeLabel];
    [self setBorderLabel:_deliveryCountryLabel];
    [self setBorderLabel:_deliveryStateLabel];
    [self setBorderLabel:_deliveryAddressTypeLabel];
    [self setBorderLabel:_creditCardExpireDateLabel];
    
}

#pragma mark - TextFields border
-(void)textFieldsBorder{
    [self setBorder:_nameTextField];
    [self setBorder:_emailAddressTextField];
    [self setBorder:_paymentMethodTypeTextField];
    [self setBorder:_cardNumberTextField];
    [self setBorder:_nameOnCardTextField];
    [self setBorder:_expirationDateTextField];
    [self setBorder:_cvvTextField];
    [self setBorder:_typeTextField];
    [self setBorder:_address1TextField];
    [self setBorder:_addressTextField];
    [self setBorder:_countryTextField];
    [self setBorder:_stateTextField];
    [self setBorder:_cityTextField];
    [self setBorder:_zipCodeTextField];
    [self setBorder:_passwordTextField];
    [self setBorder:_paswordPhoneNumberTextField];
    
}

#pragma mark - Set border
-(void)setBorder:(PaymentCustomTextField *)textField{
    [textField.layer setBorderWidth:0.5f];
    [textField.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [textField.layer setCornerRadius:2.5f];
    [textField.layer setMasksToBounds:YES];
}

#pragma mark - Set Border Four Label
-(void)setBorderLabel:(PaymentUILabel *)paymentUILabel{
    [paymentUILabel.layer setBorderWidth:0.5f];
    [paymentUILabel.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [paymentUILabel.layer setCornerRadius:2.5f];
    [paymentUILabel.layer setMasksToBounds:YES];
}

#pragma mark popup View
-(void)dismissedPopup:(NSString*)selectedDropDownValue selectedDropDownID:(NSString *)selectedDropDownID{
    switch (_paymentCustomPickerPopupView.tag) {
        case kPaymentMethodTypePopUpPickerView:
            _paymentMethodTypeLabel.text=selectedDropDownValue;
            break;
        case kDeliveryCountryPopupPickerView:
            _deliveryCountryLabel.text=selectedDropDownValue;
            break;
        case kDeliveryStatePopupPickerView:
            _deliveryStateLabel.text=selectedDropDownValue;
            break;
            
        case kDeliveryAddressTypePopupPickerView:
            _deliveryAddressTypeLabel.text=selectedDropDownValue;
            break;
            
            
        default:
            break;
    }
}



#pragma mark - Back Button Clicked
-(IBAction)backButtonClicked:(id)sender{
    
    
    NSInteger numberOfViewControllers = self.navigationController.viewControllers.count;
    
    for (int i=0; i<numberOfViewControllers; i++) {
        
        UIViewController *controller = (UIViewController*)[self.navigationController.viewControllers objectAtIndex:i];
        
        if ([controller isKindOfClass:[HomeViewController class]])
        {
            [self.navigationController popToViewController:controller animated:YES];
        }
        
    }

}

#pragma mark - Initialize Array
-(void)initializeArray{
    // Array values
    _paymentMethodMutableArray=[NSMutableArray arrayWithObjects:@"Credit Card",@"Pay Pal",@"Debit Card", nil];
    
    _countryMutableArray=[NSMutableArray arrayWithObjects:@"US",@"UK",nil];
    
    _stateMutableArray=[NSMutableArray arrayWithObjects:@"California",@"Hawali",@"Floarida", nil];
    
    _deliveryAddressTypeMutableArray=[NSMutableArray arrayWithObjects:@"Home",@"Office",nil];
    
}

#pragma mark - registerPaymentCollectionViewCell
-(void)registerPaymentCollectionViewCell{
    [_paymentMethodTypeCollectionView registerNib:[UINib nibWithNibName:kCollectionViewCellIdentifier bundle:nil] forCellWithReuseIdentifier:kCollectionViewCellIdentifier];
}

#pragma mark - Add tapgesture
-(void)addTapGesture{
    UITapGestureRecognizer *paymentMethodTypeTap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(paymentMethodTypeClicked)];
    [_paymentMethodTypeView addGestureRecognizer:paymentMethodTypeTap];
    
    UITapGestureRecognizer *deliveryCountryTap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(deliveryCountryClicked)];
    [_deliveryCountryView addGestureRecognizer:deliveryCountryTap];
    
    UITapGestureRecognizer *deliveryStateTap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(deliveryStateClicked)];
    [_deliveryStateView addGestureRecognizer:deliveryStateTap];
    
    UITapGestureRecognizer *deliveryTypeTap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(deliveryTypeClicked)];
    [_deliveryAddressTypeView addGestureRecognizer:deliveryTypeTap];
    
    UITapGestureRecognizer *creditCardExpireDateTap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(creditCardExpireDateClicked)];
    [_creditCardExpireDateView addGestureRecognizer:creditCardExpireDateTap];
    
    
}

#pragma mark - Payment Method clicked
-(void)paymentMethodTypeClicked{
    [self dismissKeyboard];
    _paymentCustomPickerPopupView=[PaymentCustomPickerPopupView showInView:self.view dropDownMutableArray:_paymentMethodMutableArray withTitle:@"Select Payment Method Types" animated:YES];
    _paymentCustomPickerPopupView.tag=kPaymentMethodTypePopUpPickerView;
    _paymentCustomPickerPopupView.delegate=self;
    
}

#pragma mark - Delivery country type clicked
-(void)deliveryCountryClicked{
    [self dismissKeyboard];
    _paymentCustomPickerPopupView=[PaymentCustomPickerPopupView showInView:self.view dropDownMutableArray:_countryMutableArray withTitle:@"Select Country" animated:YES];
    _paymentCustomPickerPopupView.tag=kDeliveryCountryPopupPickerView;
    _paymentCustomPickerPopupView.delegate=self;
    
}
-(void)deliveryStateClicked{
    [self dismissKeyboard];
    _paymentCustomPickerPopupView=[PaymentCustomPickerPopupView showInView:self.view dropDownMutableArray:_stateMutableArray withTitle:@"Select State" animated:YES];
    _paymentCustomPickerPopupView.tag=kDeliveryStatePopupPickerView;
    _paymentCustomPickerPopupView.delegate=self;
    
}

-(void)deliveryTypeClicked{
    [self dismissKeyboard];
    _paymentCustomPickerPopupView=[PaymentCustomPickerPopupView showInView:self.view dropDownMutableArray:_deliveryAddressTypeMutableArray withTitle:@"Select Type" animated:YES];
    _paymentCustomPickerPopupView.tag=kDeliveryAddressTypePopupPickerView;
    _paymentCustomPickerPopupView.delegate=self;
    
}

#pragma mark - Credit Card Expiredate
-(void)creditCardExpireDateClicked{
    [self dismissKeyboard];
    [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"showDate"];
    NSDate *defaultDate=[self minMaxDate:[NSDate date] day:0];
    NSString *defaultDateString=[self convertDateToString:defaultDate];
    
    _creditCardExpireDatePickerPopupView = [PaymentCustomDatePickerPopUpView showInView:self.view withTitle:@"Choose the expiration date" withSelectedDate:defaultDateString animated:YES];
    [_creditCardExpireDatePickerPopupView setDelegate:self];
    _creditCardExpireDatePickerPopupView.datePicker.minimumDate=[self minMaxDate:[NSDate date] day:0];
}

#pragma mark - Creditcard Expire Date dismiss
- (void)dismissedPopup:(PaymentCustomDatePickerPopUpView*)popup withDate:(NSString*)date
{
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showDate"];
    [[NSUserDefaults standardUserDefaults]setValue:@"NO" forKey:@"showTime"];
    _creditCardExpireDateLabel.text=date;
}


#pragma mark - Min max date
-(NSDate*)minMaxDate:(NSDate*)date day:(int)day{
    
    NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    
    NSDateComponents *comps = [[NSDateComponents alloc] init];
    
    [comps setDay:day];
    
    NSDate *resultDate = [gregorian dateByAddingComponents:comps toDate:date  options:0];
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

#pragma mark - Radio button tag set
// Set Radio button tags by the enable disable
-(void)setRadioButtonTags{
    _phoneNumberRadioButton.tag=kTrue;
    _primaryAddressRadioButton.tag=kTrue;
    
    _newPhoneNumberRadioButton.tag=kFalse;
    _newAddressRadioButton.tag=kFalse;
}

#pragma mark - Phone Number radio button clicked
-(IBAction)phoneNumberRadiobuttonClicked:(id)sender{
//    if(_phoneNumberRadioButton.tag==kTrue){
       [_phoneNumberRadioButton setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
//        
//        _phoneNumberRadioButton.tag=kFalse;
//        
//    }
//    else{
        [_newPhoneNumberRadioButton setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
//        _phoneNumberRadioButton.tag=kTrue;
//    }
    
}

#pragma mark - New phone number radio button clicked
-(IBAction)newPhoneNumberRadiobuttonClicked:(id)sender{
//    if(_newPhoneNumberRadioButton.tag==kTrue)
//    {
      [_newPhoneNumberRadioButton setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
//        
//        
//        _newPhoneNumberRadioButton.tag=kFalse;
//        
//    }
//    else{
       [_phoneNumberRadioButton setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
//        _newPhoneNumberRadioButton.tag=kTrue;
//    }
    
}

#pragma mark - Primary address radio button clicked
-(IBAction)primaryAddressRadiobuttonClicked:(id)sender
{
    //    if(_primaryAddressRadioButton.tag==kTrue){
    //        [_primaryAddressRadioButton setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    //        _primaryAddressRadioButton.tag=kFalse;
    //
    //    }
    //    else{
    //        [_primaryAddressRadioButton setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
    //        _primaryAddressRadioButton.tag=kTrue;
    //    }
    
    [_primaryAddressRadioButton setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    [_newAddressRadioButton setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    
    
}

#pragma mark - New address radio button clicked
-(IBAction)newAddressRadiobuttonClicked:(id)sender
{
    //    if(_newAddressRadioButton.tag==kTrue){
    //        [_newAddressRadioButton setImage:[UIImage imageNamed:@"radio-disabled.png"] forState:UIControlStateNormal];
    //
    //
    //        _newAddressRadioButton.tag=kFalse;
    //
    //    }
    //    else{
    //        [_newAddressRadioButton setImage:[UIImage imageNamed:@"radio-enabled.png"] forState:UIControlStateNormal];
    //        _newAddressRadioButton.tag=kTrue;
    //    }
    
    
    [_primaryAddressRadioButton setImage:[UIImage imageNamed:@"radio-disabled"] forState:UIControlStateNormal];
    [_newAddressRadioButton setImage:[UIImage imageNamed:@"radio-enabled"] forState:UIControlStateNormal];
    
    
}

#pragma mark - IBActo
-(IBAction)test:(id)sender{
    NSLog(@"test");
}
- (IBAction)tapPayNow:(id)sender
{
    
    
    if(_cardNumberTextField.text.length == 0)
    {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert!"
                                      message:@"Card number can not be blank"
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
    }
    else if(_nameOnCardTextField.text.length == 0)
    {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert!"
                                      message:@"Name on card cantnot be blank"
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
    }
    else if(_creditCardExpireDateLabel.text.length == 0)
    {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert!"
                                      message:@"Expiration date on card cantnot be blank"
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
    }
    else if(_cvvTextField.text.length == 0)
    {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@"Alert!"
                                      message:@"Cvv on card cantnot be blank"
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* yesButton = [UIAlertAction
                                    actionWithTitle:@"OK"
                                    style:UIAlertActionStyleDefault
                                    handler:^(UIAlertAction * action)
                                    {
                                        
                                        //Handel your yes please button action here
                                        [alert dismissViewControllerAnimated:YES completion:nil];
                                        
                                    }];
        [alert addAction:yesButton];
        
        [self presentViewController:alert animated:YES completion:nil];
    }
    
    else
    {
    
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Congratulation"
                                  message:@"Order Placed Successfully !!"
                                  preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction* yesButton = [UIAlertAction
                                actionWithTitle:@"OK"
                                style:UIAlertActionStyleDefault
                                handler:^(UIAlertAction * action)
                                {
                                    
                                    
                                
                                    
                                    NSInteger numberOfViewControllers = self.navigationController.viewControllers.count;
                                    
                                    for (int i=0; i<numberOfViewControllers; i++) {
                                        
                                        UIViewController *controller = (UIViewController*)[self.navigationController.viewControllers objectAtIndex:i];
                                        
                                        if ([controller isKindOfClass:[HomeViewController class]])
                                        {
                                            [self.navigationController popToViewController:controller animated:YES];
                                        }
                                        
                                    }
                                    
                                   
                                    //Handel your yes please button action here
                                    [alert dismissViewControllerAnimated:YES completion:nil];
                                    
                                }];
    [alert addAction:yesButton];
    
    [self presentViewController:alert animated:YES completion:nil];
        
    }
}

@end
