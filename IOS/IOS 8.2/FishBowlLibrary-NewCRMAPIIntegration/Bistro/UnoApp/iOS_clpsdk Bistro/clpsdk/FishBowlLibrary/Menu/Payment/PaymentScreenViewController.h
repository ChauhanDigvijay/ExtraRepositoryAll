//
//  PaymentScreenViewController.h
//  iOS_FBTemplate1
//
//  Created by HARSH   on 12/30/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PaymentCustomTextField.h"
#import "PaymentCustomPickerPopupView.h"
#import "PaymentCustomDatePickerPopUpView.h"
#import "PaymentUILabel.h"

@interface PaymentScreenViewController : UIViewController<UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout,PaymentCustomPickerPopupViewDelegate,PaymentCustomDatePickerPopUpViewDelegate>{
    
    // Payment screen main views
    
    __weak IBOutlet UIButton *searchOutlet;
    IBOutlet UIView *_headerView;
    IBOutlet UIScrollView *_contentScrollView;
    IBOutlet UILabel *pleaseProvideInfoView;
    IBOutlet UIView *_contentView;
    IBOutlet UIImageView *_backgroundImageView;
    
    IBOutlet UIView *_signInfoView;
    IBOutlet UIView *_customerInfoView;
    IBOutlet UIView *_passwordInfoView;
    IBOutlet UIView *_phonNumberInfoView;
    IBOutlet UIView *_creditCardInfoView;
    IBOutlet UIView *_deliveryAddressChoiceView;
    IBOutlet UIView *_deleiveryAddressInfoView;
    IBOutlet UIView *_submitOrderView;
    
    // Payment method collection view
    IBOutlet UICollectionView *_paymentMethodTypeCollectionView;
    IBOutlet UICollectionViewFlowLayout *_paymentMethodTypeCollectionViewFlowLayout;
    
    
    // Labels
    IBOutlet UILabel *_signInInfoLabel;
    IBOutlet UILabel *_payAmountValueLabel;
    IBOutlet UILabel *_deliveryAddressValueLabel;
    
    // Signed user textfields
    IBOutlet   PaymentCustomTextField *_nameTextField;
    IBOutlet   PaymentCustomTextField *_emailAddressTextField;
    IBOutlet   PaymentCustomTextField *_primaryPhoneNumberTextField;
    IBOutlet   PaymentCustomTextField *_newPhoneNumberTextField;
    
    // Guest user textfields
    IBOutlet   PaymentCustomTextField *_passwordTextField;
    IBOutlet   PaymentCustomTextField *_paswordPhoneNumberTextField;
    
    
    // Credit card text fields
    IBOutlet   PaymentCustomTextField *_paymentMethodTypeTextField;
    IBOutlet   PaymentCustomTextField *_cardNumberTextField;
    IBOutlet   PaymentCustomTextField *_nameOnCardTextField;
    IBOutlet   PaymentCustomTextField *_expirationDateTextField;
    IBOutlet   PaymentCustomTextField *_cvvTextField;
    
    // Deleivery order text fields
    IBOutlet  PaymentCustomTextField *_typeTextField;
    IBOutlet  PaymentCustomTextField *_addressTextField;
    IBOutlet  PaymentCustomTextField *_address1TextField;
    IBOutlet  PaymentCustomTextField  *_countryTextField;
    IBOutlet  PaymentCustomTextField *_cityTextField;
    IBOutlet  PaymentCustomTextField *_stateTextField;
    IBOutlet  PaymentCustomTextField *_zipCodeTextField;
    
    
    // Phone number choice radio button
    IBOutlet   UIButton *_phoneNumberRadioButton;
    IBOutlet   UIButton *_newPhoneNumberRadioButton;
    
    // Delivery address choice radio button
    IBOutlet UIButton *_primaryAddressRadioButton;
    IBOutlet UIButton *_newAddressRadioButton;
    
    
    
    
    
    // Credit card tap button
    IBOutlet UIView             *_paymentMethodTypeView;
    IBOutlet PaymentUILabel     *_paymentMethodTypeLabel;
    
    IBOutlet UIView             *_deliveryCountryView;
    IBOutlet PaymentUILabel     *_deliveryCountryLabel;
    
    IBOutlet UIView             *_deliveryStateView;
    IBOutlet PaymentUILabel     *_deliveryStateLabel;
    
    IBOutlet UIView             *_deliveryAddressTypeView;
    IBOutlet PaymentUILabel     *_deliveryAddressTypeLabel;
    
    IBOutlet UIView             *_creditCardExpireDateView;
    IBOutlet PaymentUILabel     *_creditCardExpireDateLabel;
    
    
    //delivery address view outlet
    
    IBOutlet UILabel *_pleaseSelectAddressLabel;
    IBOutlet UIView *_primaryAddressView;
    
    IBOutlet UIView *_newAddressView;
    IBOutlet UIView *_addressBottomLine;
    
    //enter online delivery address label not sign in
    IBOutlet UILabel *lpleseOnLIneLabelToEnterAddessNotSignIn;
}
@property (nonatomic,strong) NSString *strSelectAddress;
@property (nonatomic,strong) NSString *strSelectCity;
@end
