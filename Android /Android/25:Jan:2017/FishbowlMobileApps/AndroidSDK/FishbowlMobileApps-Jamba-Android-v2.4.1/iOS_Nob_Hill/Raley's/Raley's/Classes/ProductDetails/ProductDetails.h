//
//  ProductDetails.h
//  Raley's
//
//  Created by trg02 on 5/24/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "SmartTextView.h"
#import "Product.h"
#import "OffsetTextField.h"
#import "ProgressDialog.h"
#import "ShoppingScreenDelegate.h"
#import "WebService.h"
#import "HeaderView.h"

#define PRODUCT_ADD 1
#define PRODUCT_MODIFY 2
#define PRODUCT_DELETE 3
#define DEFAULT_PRODUCT_COMMENTS @"Personal Shopper instructions"
@interface ProductDetails : UIViewController<UITextFieldDelegate, WebServiceListener,HeaderViewDelegate>
{
    
    AppDelegate         *_app;
    Login               *_login;
    Product             *_product;
    OffsetTextField     *_instructionsTextField;
    ProgressDialog      *_progressDialog;
    WebService          *_service;
    float                 _productQuantity;
    
     IBOutlet   UILabel             *quantityText;

    IBOutlet  UILabel      *weightText;
    IBOutlet  UILabel *_totalPriceValue;

   IBOutlet  UIButton      *_quantityCheckBox;
   IBOutlet  UIButton      *_weightCheckBox;
        IBOutlet UIImageView   *_productImageView;
    IBOutlet UIView   *_quantityTextbg;
    IBOutlet UIView   *_quantityValuesbg;

    IBOutlet UIImageView   *promoImageView;
    IBOutlet UIButton   *plusButton;
    IBOutlet UIButton   *minusButton;
        IBOutlet UIView     *_instructionsTextFieldbg;

    UILabel *_quantityText,*_quantityValue;
    
    IBOutlet  UIButton      *addButton;
    IBOutlet  UIButton      *cancelButton;

    

    
    IBOutlet UIView  *descriptionbg;
    IBOutlet UIView  *extendedDisplayLabelbg;
    IBOutlet UIView  *regPriceTextbg;
    
    IBOutlet UILabel *mylistcount;


   IBOutlet UIView *container;
    
    
    
    // New control declartions
    
    UIScrollView  *_scrollView;

    UIView *product_radius_view;
    UIImageView *product_image;
    UILabel *product_description;
    UILabel *product_type;
    UILabel *product_normal_price;
    
    UIImageView *Total_price_icon;
    UILabel *product_total_price;
    
    UIButton *quality_option_button;
    UIButton *weight_option_button;
    
    UIButton *Ingredients_pop_button;
    
    UIView *Ingredients_pop_view;
    UIView *details_subview;
    NSString *ingredients_str;
    HeaderView *headerView;

}

@property (nonatomic, assign) id<ShoppingScreenDelegate> _shoppingScreenDelegate;
//- (id)initWithFrame:(CGRect)frame product:(Product *)product detailType:(int)detailType;
-(void)creating_controls:(Product *)product _category:(ProductCategory*)category detailType:(int)detailType _frame:(CGRect)frame _isCurrentAcitveList:(BOOL)_isActive;
-(IBAction)backButtonClicked;
@end





