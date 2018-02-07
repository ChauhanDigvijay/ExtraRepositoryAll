//
//  ProductDetails.m
//  Raley's
//
//  Created by trg02 on 5/24/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "ProductDetails.h"
#import "SmartTextView.h"
#import "ListAddItemRequest.h"
#import "ListDeleteItemRequest.h"
#import "ShoppingList.h"
#import "Logging.h"
#import "ProductDetailView.h"
#import "TextDialog.h"
#import "Utility.h"
#import "Add_card.h"
#import "ProductFooter.h"


@interface ProductDetails ()
{
    Add_card    *more_details;
    BOOL        _isCurrentAcitveList;
    int         currentDetailType;
    
    UIView      *img_alert_container;
    UIImageView *alert_image;
    ProductFooter *footer;
    int footerHeight;
    UIView *footerContainer;
    
    BOOL        _isQuantityChange;
}
@end

@implementation ProductDetails
@synthesize _shoppingScreenDelegate;

- (void)viewDidLoad
{
    [super viewDidLoad];
    _isQuantityChange = false;
    
    _app = (id)[[UIApplication sharedApplication] delegate];
    // Do any additional setup after loading the view from its nib.
    
    //  [[UIApplication sharedApplication]setStatusBarHidden:YES];
    
    
    [self.view setBackgroundColor:[UIColor whiteColor]];
    
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){  // IOS 7 and above
        headerView = [[HeaderView alloc] initWithFrame:CGRectMake(0.0, 20.0, _app._viewWidth, _app._headerHeight)];
    }
    else{
        headerView = [[HeaderView alloc] initWithFrame:CGRectMake(0.0, 0.0, _app._viewWidth, _app._headerHeight)];
    }
    [headerView setDelegate:self];
    [self.view addSubview:headerView];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardShow:)
                                                 name:UIKeyboardWillShowNotification object:self.view.window];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardHide:)
                                                 name:UIKeyboardWillHideNotification object:self.view.window];
}

- (void)keyboardShow:(NSNotification *)notif{
    
    NSDictionary *info = [notif userInfo];
    NSNumber *number = [info objectForKey:UIKeyboardAnimationDurationUserInfoKey];
    CGSize keyboardSize = [[[notif userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    
    double duration = [number doubleValue];
    [UIView animateWithDuration:duration animations:^{
        CGRect frame = self.view.bounds;
        //frame.origin.y -=130;
        frame.origin.y -=keyboardSize.height;
        [self.view setFrame:frame];
        //[_scrollView setContentOffset:CGPointMake(0, -keyboardSize.height) animated:YES];
        
    } completion:^(BOOL animation){}];
    
    
}
- (void)keyboardHide:(NSNotification *)notif{
    
    NSDictionary *info = [notif userInfo];
    NSNumber *number = [info objectForKey:UIKeyboardAnimationDurationUserInfoKey];
    //CGSize keyboardSize = [[[notif userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    double duration = [number doubleValue];
    [UIView animateWithDuration:duration animations:^{
        CGRect frame = self.view.bounds;
        frame.origin.y +=0;
        //frame.origin.y +=keyboardSize.height;
        [self.view setFrame:frame];
        // [_scrollView setContentOffset:CGPointMake(0, 0) animated:YES];
        
    } completion:^(BOOL animation){}];
}

-(void)creating_controls:(Product *)product _category:(ProductCategory*)category detailType:(int)detailType _frame:(CGRect)frame _isCurrentAcitveList:(BOOL)_isActive
{
    currentDetailType = detailType;
    
    _app = (id)[[UIApplication sharedApplication] delegate];
    _login = [_app getLogin];
    _product = product;
    _isCurrentAcitveList=_isActive;
    footerHeight=50;
    
    
#ifdef CLP_ANALYTICS
    if(_product){
        @try {                                                              //View Product Event
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"ProductViewed" forKey:@"event_name"];
            [data setValue:_product.sku forKey:@"SKU"];
            [data setValue:_product.upc forKey:@"item_id"];
            [data setValue:_product.brand forKey:@"brand"];
            [data setValue:_product.description forKey:@"item_name"];
            [data setValue:[NSString stringWithFormat:@"%f",_product.regPrice] forKey:@"price"];
            [data setValue:[NSString stringWithFormat:@"%f",_product.promoPrice]forKey:@"promo_price"];
            if(_product.weight > 0)
                [data setValue:[NSString stringWithFormat:@"%f",_product.weight] forKey:@"quantity"];
            else
                [data setValue:[NSString stringWithFormat:@"%d",_product.qty] forKey:@"quantity"];
            [data setValue:_product.mainCategory forKey:@"category"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
        
//        if(category){
//            [data setValue:[NSString stringWithFormat:@"%d",category.productCategoryId] forKey:@"Category_ID"];
//            [data setValue:[NSString stringWithFormat:@"%d",category.parentCategoryId] forKey:@"Parent_Category_ID"];
//            [data setValue:[NSString stringWithFormat:@"%d",category.grandParentCategoryId] forKey:@"Grand_Parent_Category_ID"];
//            
//            [data setValue:category.name forKeyPath:@"Category_Name"];
//            [data setValue:category.parentCategoryName forKeyPath:@"Parent_Category_Name"];
//            [data setValue:category.grandParentCategoryName forKeyPath:@"Grand_Parent_Category_Name"];
//        }
    }
#endif
    
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){  // IOS 7 and above
        _scrollView=[[UIScrollView alloc]initWithFrame:CGRectMake(0, _app._headerHeight+20, _app._viewWidth,  (_app._viewHeight-footerHeight))];
    }
    else{
        _scrollView=[[UIScrollView alloc]initWithFrame:CGRectMake(0, _app._headerHeight, _app._viewWidth,  (_app._viewHeight-footerHeight))];
    }
    
    [_scrollView setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]];
    [_scrollView setShowsHorizontalScrollIndicator:NO];
    [_scrollView setShowsVerticalScrollIndicator:NO];
    
    
    UITapGestureRecognizer *dismiss_tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(dismiss_keyboard)];
    [dismiss_tap setNumberOfTapsRequired:1];
    [dismiss_tap setNumberOfTouchesRequired:1];
    [_scrollView addGestureRecognizer:dismiss_tap];
    
    int view_gap=10;
    int global_y=0;
    
    CGFloat new_app_width= _app._viewWidth-(view_gap*2);
    //    CGFloat *new_app_height;
    
    // ---------------------  Products View  ---------------------  //
    product_radius_view=[[UIView alloc]initWithFrame:CGRectMake(view_gap, view_gap, new_app_width ,_scrollView.contentSize.height-(view_gap*3))];
    [product_radius_view.layer setCornerRadius:5.0f];
    [product_radius_view setBackgroundColor:[UIColor whiteColor]];
    [product_radius_view.layer setShadowColor:[UIColor darkGrayColor].CGColor];
    //    [product_radius_view.layer setShadowOpacity:0.6];
    //    [product_radius_view.layer setShadowRadius:0.3];
    [product_radius_view.layer setBorderWidth:0.8];
    [product_radius_view.layer setBorderColor:[UIColor colorWithRed:215.0/255.0 green:215.0/255.0 blue:215.0/255.0 alpha:1.0].CGColor];
    [product_radius_view.layer setShadowOffset:CGSizeMake(0.0,0.6)];
    [product_radius_view setClipsToBounds:YES]; // crop the image left and right most top
    [_scrollView setClipsToBounds:YES];
    
    // ---------------------  Product Image  ---------------------  //
    UIImage *image = [_app getCachedImage:[_product.imagePath lastPathComponent]];
    
    NSString *path=[NSString stringWithFormat:@"%@",[_app getImageFullPath:[_product.imagePath lastPathComponent]]];
    
    CGSize product_image_size;
    NSString *image_name = [_product.imagePath lastPathComponent];
    product_image_size=[Utility get_image_width_and_height:path];
    if(image==nil || [image_name isEqualToString:@"product-image-default-bag-90px.png"]){
        product_image_size = CGSizeMake(200, 200);
        // No Image
        image = [UIImage imageNamed:@"noimage"];
        //image = [UIImage imageNamed:@"shopping_bag"];
    }
    
    global_y=0;
    CGRect img_frame = CGRectZero;
    img_frame.origin.x = 0;
    img_frame.origin.y = global_y;
    if(product_image_size.width<=product_radius_view.frame.size.width){
        CGFloat new_x = product_radius_view.frame.size.width - product_image_size.width;
        new_x = new_x / 2.0f;
        img_frame.origin.x = new_x;
        img_frame.size = product_image_size;
    }else{
        CGFloat radio = product_radius_view.frame.size.width / product_image_size.width;
        img_frame.size.width = product_radius_view.frame.size.width;
        img_frame.size.height = product_image_size.height * radio;
    }
    
    product_image=[[UIImageView alloc]initWithFrame:img_frame];
    [product_image setContentMode:UIViewContentModeScaleAspectFit];
    [product_image setImage:image];
    
    //----------------------------------------------------------//
    //Ingredients button
    //----------------------------------------------------------//
    
    ingredients_str=[NSString stringWithFormat:@"%@", _product.ingredients];
    
    if(![Utility isEmpty:_product.ingredients])
    {
        CGRect buttonframe=product_image.frame;
        buttonframe.origin.x=product_image.frame.size.width-55.0f;
        buttonframe.origin.y=product_image.frame.size.height-60.0f;
        buttonframe.size.height=50.0f;
        buttonframe.size.width=50.0f;
        
        [product_image setUserInteractionEnabled:YES];
        
        Ingredients_pop_button=[UIButton buttonWithType:UIButtonTypeCustom];
        [Ingredients_pop_button setFrame:buttonframe];
        [Ingredients_pop_button setBackgroundImage:[UIImage imageNamed:@"Ingridents.png"] forState:UIControlStateNormal];
        
        [Ingredients_pop_button addTarget:self action:@selector(show_ingrdient_popup:) forControlEvents:UIControlEventTouchUpInside];
        [product_image addSubview:Ingredients_pop_button];
        
        UITapGestureRecognizer *product_image_gesture=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(show_ingrdient_popup:)];
        
        product_image_gesture.numberOfTapsRequired=1;
        product_image_gesture.numberOfTouchesRequired=1;
        
        [product_image removeGestureRecognizer:product_image_gesture];
        [product_image addGestureRecognizer:product_image_gesture];
    }
    
    
    // ---------------------  Product Category Type  ---------------------  //
    
    global_y+= product_image.frame.size.height+(view_gap*1.5);
    //global_y+=product_image.frame.size.height+(view_gap*1.5);
    
    product_type=[[UILabel alloc]initWithFrame:CGRectMake(view_gap, global_y,product_radius_view.frame.size.width-(view_gap*2), 20.0f)];
    //[product_type setText:@"18 OZ"];
    product_type.lineBreakMode=NSLineBreakByTruncatingTail;
    product_type.numberOfLines=1;
    [product_type setTextAlignment:NSTextAlignmentLeft];
    [product_type setTextColor:[UIColor darkGrayColor]];
    [product_type setBackgroundColor:[UIColor clearColor]];
    //    [product_type setFont:[UIFont systemFontOfSize:font_size13]];
    [product_type setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
    [product_type setText:_product.extendedDisplay];
    
    
    // ---------------------  Product Actual Price  ---------------------  //
    global_y+= 0.6 * product_type.frame.size.height+(view_gap*1.5);
    //global_y+=product_type.frame.size.height;
    
    product_normal_price=[[UILabel alloc]initWithFrame:CGRectMake(view_gap, global_y,product_radius_view.frame.size.width-(view_gap*2), 30.0f)];
    product_normal_price.text = [NSString stringWithFormat:@"Regular Price : $%.2f", _product.regPrice];
    //[product_normal_price setText:@"Regular Price: $2.99 /oz"];
    [product_normal_price setTextAlignment:NSTextAlignmentLeft];
    product_normal_price.lineBreakMode=NSLineBreakByTruncatingTail;
    product_normal_price.numberOfLines=1;
    [product_normal_price setTextColor:[UIColor darkGrayColor]];
    [product_normal_price setBackgroundColor:[UIColor clearColor]];
    //    [product_normal_price setFont:[UIFont systemFontOfSize:font_size13]];
    [product_normal_price setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
    
    
    
    // ---------------------  Product price tag Icon  ---------------------  //
    global_y+= 0.9 *   product_normal_price.frame.size.height+view_gap;
    
    
    
    //    global_y+=product_normal_price.frame.size.height+(view_gap*1.3);
    Total_price_icon=[[UIImageView alloc]initWithFrame:CGRectMake(view_gap, global_y, 30, 30)];
    
    // ---------------------  Product Total Price calculated  ---------------------  //
    product_total_price=[[UILabel alloc]initWithFrame:CGRectMake((view_gap)+Total_price_icon.frame.size.width, global_y,product_radius_view.frame.size.width/2, 30.0f)];
    [product_total_price setText:@"$0.00"]; // Default text applied
    
    
    CGRect price_tag_frame;
    if(_product.promoType > 0)
    {
        [Total_price_icon setHidden:NO];
        Total_price_icon.image=[UIImage imageNamed:@"sale_icon.png"];
        
        price_tag_frame=product_total_price.frame;
        price_tag_frame.origin.x=Total_price_icon.frame.origin.x+Total_price_icon.frame.size.width+(view_gap);
        [product_total_price setFrame:price_tag_frame];
        
    }
    else
    {
        [Total_price_icon setHidden:YES];
        price_tag_frame=product_total_price.frame;
        price_tag_frame.origin.x=10;
        [product_total_price setFrame:price_tag_frame];
    }
    
    // ---------------------  Product Description  ---------------------  //
    global_y+=product_total_price.frame.size.height+view_gap;
    //    global_y+=product_image.frame.size.height+view_gap;
    
    product_description=[[UILabel alloc]initWithFrame:CGRectMake(view_gap, global_y,product_radius_view.frame.size.width-(view_gap*1.5), 50.0f)];
    //[product_description setText:@"dfsd fasdfj asf;asj falfj asfl;asjfda;sdjfaksdfajsdfkajsdkfjasdfasdfassdfadsfasdfa"];
    [product_description setTextAlignment:NSTextAlignmentLeft];
    [product_description setTextColor:[UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0]];
    
    product_description.lineBreakMode=NSLineBreakByTruncatingTail;
    product_description.numberOfLines=2;
    [product_description setBackgroundColor:[UIColor clearColor]];
    //    [product_description setFont:[UIFont systemFontOfSize:font_size13]];
    [product_description setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
    
    
    
    
    
    
    
    // View for-  Border top only
    CGSize mainViewSize = product_description.bounds.size;
    CGFloat borderWidth = 1;
    UIColor *borderColor = [UIColor lightGrayColor];
    UIView *topView = [[UIView alloc] initWithFrame:CGRectMake(0.0,0.0, mainViewSize.width, borderWidth)];
    topView.opaque = YES;
    topView.alpha=0.7f;
    topView.backgroundColor = borderColor;
    topView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleBottomMargin;
    //[product_description addSubview:topView];
    
    if(![Utility isEmpty:_product.brand])
        product_description.text = [NSString stringWithFormat:@"%@ %@", _product.brand, _product.description];
    else
        product_description.text = _product.description;
    
    
    /* Raleys 2.1 code updated starts */
    
    UILabel *Save_offers_lbl;
    UILabel *Offer_detail_description_lbl_txt;
    UILabel *Offer_detail_description_lbl_val;
    
    if(_product.promoType > 0)
    {
        
        // ---------------------  Offer_detail_description_lbl  ---------------------  //
        global_y+= 1.2 * product_description.frame.size.height;
        Offer_detail_description_lbl_txt=[[UILabel alloc]initWithFrame:CGRectMake(view_gap, global_y,(product_radius_view.frame.size.width/2.11)-(view_gap*1), 30.0f)];
        Offer_detail_description_lbl_txt.text = [NSString stringWithFormat:@"Promo Price   : "];
        //[product_normal_price setText:@"Regular Price: $2.99 /oz"];
        [Offer_detail_description_lbl_txt setTextAlignment:NSTextAlignmentLeft];
        Offer_detail_description_lbl_txt.lineBreakMode=NSLineBreakByTruncatingTail;
        Offer_detail_description_lbl_txt.numberOfLines=1;
        [Offer_detail_description_lbl_txt setTextColor:[UIColor darkGrayColor]];
        [Offer_detail_description_lbl_txt setBackgroundColor:[UIColor clearColor]];
        //        [Offer_detail_description_lbl_txt.layer setBorderWidth:0.5f];
        [Offer_detail_description_lbl_txt setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
        
        
        
        // ---------------------  Offer_detail_description_lbl Text information  ---------------------  //
        //  global_y+=product_normal_price.frame.size.height;
        Offer_detail_description_lbl_val=[[UILabel alloc]initWithFrame:CGRectMake(view_gap+Offer_detail_description_lbl_txt.frame.size.width, global_y,(product_radius_view.frame.size.width/2.1)-(view_gap*1), 30.0f)];
        Offer_detail_description_lbl_val.text = [NSString stringWithFormat:@"%d for $%.2f", _product.promoForQty,_product.promoPrice];
        //[product_normal_price setText:@"Regular Price: $2.99 /oz"];
        [Offer_detail_description_lbl_val setTextAlignment:NSTextAlignmentLeft];
        Offer_detail_description_lbl_val.lineBreakMode=NSLineBreakByTruncatingTail;
        Offer_detail_description_lbl_val.numberOfLines=1;
        [Offer_detail_description_lbl_val setTextColor:[UIColor colorWithRed:(204.0/255.0) green:(0.0/255.0) blue:(0.0/255.0) alpha:1.0]]; // red color
        // [Offer_detail_description_lbl_val.layer setBorderWidth:0.5f];
        [Offer_detail_description_lbl_val setBackgroundColor:[UIColor clearColor]];
        [Offer_detail_description_lbl_val setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
        
        double save_price=0.0;
        NSString *saving_offer_price_str=@"";
        
        if(_product.promoPriceText!=nil){
            saving_offer_price_str=_product.promoPriceText;
        }else{
            if(_product.promoForQty>0){
                save_price= (_product.regPrice-_product.promoPrice)*_product.promoForQty;
                saving_offer_price_str=[NSString stringWithFormat:@"You save $%.2f on %d",save_price,_product.promoForQty];
            }
        }
        
        // ---------------------  Save_offers_lbl  ---------------------  //
        global_y+=Offer_detail_description_lbl_val.frame.size.height;
        Save_offers_lbl=[[UILabel alloc]initWithFrame:CGRectMake(view_gap, global_y,product_radius_view.frame.size.width-(view_gap*2), 30.0f)];
        //        Save_offers_lbl.text = [NSString stringWithFormat:@"You save $%.2f on %d",save_price,_product.promoForQty];
        Save_offers_lbl.text = [NSString stringWithFormat:@"%@",saving_offer_price_str];
        
        //[product_normal_price setText:@"Regular Price: $2.99 /oz"];
        [Save_offers_lbl setTextAlignment:NSTextAlignmentLeft];
        Save_offers_lbl.lineBreakMode=NSLineBreakByTruncatingTail;
        Save_offers_lbl.numberOfLines=1;
        [Save_offers_lbl setTextColor:[UIColor darkGrayColor]];
        [Save_offers_lbl setBackgroundColor:[UIColor clearColor]];
        //    [product_normal_price setFont:[UIFont systemFontOfSize:font_size13]];
        [Save_offers_lbl setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
        
        [product_radius_view addSubview:Save_offers_lbl];
        [product_radius_view addSubview:Offer_detail_description_lbl_txt];
        [product_radius_view addSubview:Offer_detail_description_lbl_val];
        
        
    }else{
        
        [Save_offers_lbl removeFromSuperview];
        [Offer_detail_description_lbl_txt removeFromSuperview];
        [Offer_detail_description_lbl_val removeFromSuperview];
        
    }
    
    /* Raleys 2.1 code updated End here */
    
    
    //    // View for-  Border Botton only
    //    mainViewSize = product_normal_price.bounds.size;
    //    borderWidth = 1;
    //    borderColor = [UIColor lightGrayColor];
    //    topView = [[UIView alloc] initWithFrame:CGRectMake(0.0,global_y, mainViewSize.width, borderWidth)];
    //    topView.opaque = YES;
    //    topView.alpha=0.7f;
    //    topView.backgroundColor = borderColor;
    //    topView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleBottomMargin;
    //    [product_radius_view addSubview:topView];
    
    
    
    if(_product.promoType > 0)
    {
        if(_product.promoForQty > 1)
            product_total_price.text = [NSString stringWithFormat:@"%d for $%.2f", _product.promoForQty, _product.promoPrice];
        else
            product_total_price.text = [NSString stringWithFormat:@"$%.2f", _product.promoPrice];
    }
    
    if(product.weight > 0)
        _productQuantity = _product.weight;//mycode
    else
        _productQuantity = _product.qty == 0 ? 1 : _product.qty; // for products retrieved in product search the qty is 0 so set it to 1 here
    
    
    [product_total_price setTextAlignment:NSTextAlignmentLeft];
    product_total_price.lineBreakMode=NSLineBreakByTruncatingTail;
    product_total_price.numberOfLines=1;
    
    if(_product.promoType > 0)
    {
        [product_total_price setTextColor:[UIColor colorWithRed:(187.0/255.0) green:(32.0/255.0) blue:(1.0/255.0) alpha:1.0]]; // red
    }
    else{
        [product_total_price setTextColor:[UIColor colorWithRed:(32/255.0) green:(35/255.0) blue:(36/255.0) alpha:1.0]]; // dark black
    }
    
    [product_total_price setBackgroundColor:[UIColor clearColor]];
    //    [product_total_price setFont:[UIFont systemFontOfSize:font_size20]];
    [product_total_price setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
    //[product_total_price setFont:[UIFont fontWithName:@"Helvetica-Bold" size:14]];
    [product_total_price setFont:[UIFont boldSystemFontOfSize:20]];
    
    
    
    global_y+=product_description.frame.size.height+(view_gap*1.3);//product_normal_price
    
    //    global_y+=Total_price_icon.frame.size.height+(view_gap*2);
    
    
    if(_product.approxAvgWgt > 0)
    {
        // ---------------------  Product Selected by quantity / weight  ---------------------  //
        
        UIView *selection_BG=[[UIView alloc]initWithFrame:CGRectMake(5, global_y, new_app_width, 45)];
        [selection_BG setBackgroundColor:[UIColor clearColor]];
        
        UIView *first_option_BG=[[UIView alloc]initWithFrame:CGRectMake(0, 0,new_app_width/2, 45)];
        [first_option_BG setBackgroundColor:[UIColor clearColor]];
        
        
        CGFloat button_width=30;
        CGFloat desc_width=first_option_BG.frame.size.width*0.7;
        
        quality_option_button=[[UIButton alloc]initWithFrame:CGRectMake(0, 5,button_width,button_width)];
        
        [quality_option_button setBackgroundImage:[UIImage imageNamed:@"product_unchecked_box"] forState:UIControlStateNormal];
        
        [quality_option_button addTarget:self action:@selector(setPricingByQuantity:) forControlEvents:UIControlEventTouchUpInside];
        
        
        UILabel *option_button1_desc=[[UILabel alloc]initWithFrame:CGRectMake(button_width+(view_gap/2), 5, desc_width, button_width)];
        [option_button1_desc setText:@"Show by Quantity"];
        [option_button1_desc setTextAlignment:NSTextAlignmentCenter];
        [option_button1_desc setTextColor:[UIColor darkGrayColor]];
        [option_button1_desc setBackgroundColor:[UIColor clearColor]];
        //    [option_button1_desc setFont:[UIFont systemFontOfSize:font_size13]];
        [option_button1_desc setFont:[UIFont fontWithName:_app._normalFont size:11.2]];
        
        [first_option_BG addSubview:quality_option_button];
        [first_option_BG addSubview:option_button1_desc];
        
        
        UIView *second_option_BG=[[UIView alloc]initWithFrame:CGRectMake(new_app_width/2+1, 0, new_app_width/2, 45)];
        [second_option_BG setBackgroundColor:[UIColor clearColor]];
        
        
        weight_option_button=[[UIButton alloc]initWithFrame:CGRectMake(0, 5,button_width,button_width)];
        [weight_option_button setBackgroundImage:[UIImage imageNamed:@"product_unchecked_box.png"] forState:UIControlStateNormal];
        
        [weight_option_button addTarget:self action:@selector(setPricingByWeight:) forControlEvents:UIControlEventTouchUpInside];
        
        
        
        UILabel *option_button2_desc=[[UILabel alloc]initWithFrame:CGRectMake(button_width+(view_gap/2), 5, desc_width, button_width)];
        [option_button2_desc setText:@"Show by Weight"];
        [option_button2_desc setTextAlignment:NSTextAlignmentCenter];
        [option_button2_desc setTextColor:[UIColor darkGrayColor]];
        [option_button2_desc setBackgroundColor:[UIColor clearColor]];
        //    [option_button2_desc setFont:[UIFont systemFontOfSize:font_size13]];
        [option_button2_desc setFont:[UIFont fontWithName:_app._normalFont size:11.2]];
        
        
        [second_option_BG addSubview:weight_option_button];
        [second_option_BG addSubview:option_button2_desc];
        
        [selection_BG addSubview:first_option_BG];
        [selection_BG addSubview:second_option_BG];
        
        
        global_y+=selection_BG.frame.size.height+view_gap;
        [product_radius_view addSubview:selection_BG];
        
        [footer.extended_text setText:[[NSString stringWithFormat:@" %@s", _product.unitOfMeasure] lowercaseString]];
    }
    
    //footer
    footer =[[[NSBundle mainBundle] loadNibNamed:@"ProductFooter" owner:self options:nil] objectAtIndex:0];
    [footer setFrame:CGRectMake(0, 0, self.view.frame.size.width, footerHeight)];
    [footer.value_plus addTarget:self action:@selector(addToItemCount:) forControlEvents:UIControlEventTouchUpInside];
    //rounded corners
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:footer.value_plus.bounds
                                                   byRoundingCorners:(UIRectCornerTopRight | UIRectCornerBottomRight)
                                                         cornerRadii:CGSizeMake(5.0, 5.0)];
    CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.view.bounds;
    maskLayer.path = maskPath.CGPath;
    footer.value_plus.layer.mask = maskLayer;
    
    [footer.value_mius addTarget:self action:@selector(subtractFromItemCount:) forControlEvents:UIControlEventTouchUpInside];
    //rounded corners
    maskPath = [UIBezierPath bezierPathWithRoundedRect:footer.value_mius.bounds
                                     byRoundingCorners:(UIRectCornerTopLeft | UIRectCornerBottomLeft)
                                           cornerRadii:CGSizeMake(5.0, 5.0)];
    maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.view.bounds;
    maskLayer.path = maskPath.CGPath;
    footer.value_mius.layer.mask = maskLayer;
    
    [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.0f",_productQuantity] forState:UIControlStateNormal];
    // more_details=[[[Add_card alloc]initWithFrame:
    // more_details=[[[Add_card alloc]initWithFrame:
    
    //more details section
    more_details = [[[NSBundle mainBundle] loadNibNamed:@"add_card" owner:self options:nil] objectAtIndex:0];
    [more_details setFrame:CGRectMake(0, global_y, new_app_width, 100)];
    //    [more_details setBounds:CGRectMake(0, global_y, new_app_width, 160)];
    global_y+=more_details.frame.size.height+view_gap;
    [more_details setBackgroundColor:[UIColor clearColor]];
    
    [more_details.comments.layer setCornerRadius:5.0f];
    [more_details.comments.layer setBorderWidth:0.7];
    [more_details.comments.layer setBorderColor:[UIColor darkGrayColor].CGColor];
    
    UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, more_details.comments.frame.size.height)];
    leftView.backgroundColor = more_details.comments.backgroundColor;
    more_details.comments.leftView = leftView;
    more_details.comments.leftViewMode = UITextFieldViewModeAlways;
    
    [self updateListCount];
    
    //    [product_radius_view addSubview:clip_view]; // Product image containers
    [product_radius_view addSubview:product_image];
    [product_radius_view addSubview:product_description];
    [product_radius_view addSubview:product_type];
    [product_radius_view addSubview:product_normal_price];
    [product_radius_view addSubview:Total_price_icon];
    [product_radius_view addSubview:product_total_price];
    
    //    [more_details setClipsToBounds:YES];
    [product_radius_view addSubview:more_details];
    
    [_scrollView addSubview:product_radius_view];
    
    //    [_scrollView setContentSize:CGSizeMake(_scrollView.contentSize.width, global_y + (view_gap*2))]; //;*1.068)];
    
    
    if(detailType==PRODUCT_ADD){
        if(_product.approxAvgWgt > 0){
            [self setPricingByWeight:nil];
        }
        else{
            if ([_product.unitOfMeasure caseInsensitiveCompare:@"LB"]==NSOrderedSame && _product.approxAvgWgt<=0)
            {
                //weight product
                [self setPricingByWeight:nil];
            } else {
                //qty product
                [self setPricingByQuantity:nil];
            }
        }
    }
    else{
        if(_product.weight > 0){
            [self setPricingByWeight:nil];
        }
        else{
            if ([_product.unitOfMeasure caseInsensitiveCompare:@"LB"]==NSOrderedSame && _product.approxAvgWgt<=0)
            {
                //weight product
                [self setPricingByWeight:nil];
            } else {
                //qty product
                [self setPricingByQuantity:nil];
            }
        }
    }
    
    more_details.comments.delegate = self;
    
    more_details.comments.backgroundColor = [UIColor clearColor];
    more_details.comments.textColor = [UIColor grayColor];
    
    
    if(![Utility isEmpty:_product.customerComment])
        more_details.comments.text = _product.customerComment;
    else
        //more_details.comments.text = @"Personal Shopper instructions";
        more_details.comments.text = DEFAULT_PRODUCT_COMMENTS;
    
    if(detailType == PRODUCT_ADD)
    {
        //first time add
        NSString *addText = @"Add";// Add to List
        [footer.Add_to_Card setTitle:addText forState:UIControlStateNormal];
        [footer.Add_to_Card addTarget:self action:@selector(addItem:) forControlEvents:UIControlEventTouchUpInside];
        
        //rounded corners
        UIBezierPath *maskPath;
        maskPath = [UIBezierPath bezierPathWithRoundedRect:footer.Add_to_Card.bounds
                                         byRoundingCorners:(UIRectCornerAllCorners)
                                               cornerRadii:CGSizeMake(5.0, 5.0)];
        
        CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
        maskLayer.frame = self.view.bounds;
        maskLayer.path = maskPath.CGPath;
        footer.Add_to_Card.layer.mask = maskLayer;
        [cancelButton setHidden:YES];
        
    }
    else if(detailType == PRODUCT_MODIFY)
    {
        
        //  [footer.Add_to_Card setHidden:NO];
        NSString *addText = @"Update";
        [footer.Add_to_Card setTitle:addText forState:UIControlStateNormal];
        [footer.Add_to_Card addTarget:self action:@selector(modifyItem:) forControlEvents:UIControlEventTouchUpInside];
        
        //rounded corners
        UIBezierPath *maskPath;
        maskPath = [UIBezierPath bezierPathWithRoundedRect:footer.Add_to_Card.bounds
                                         byRoundingCorners:(UIRectCornerAllCorners)
                                               cornerRadii:CGSizeMake(5.0, 5.0)];
        CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
        maskLayer.frame = self.view.bounds;
        maskLayer.path = maskPath.CGPath;
        footer.Add_to_Card.layer.mask = maskLayer;
        
        // [_scrollView addSubview:addButton];
        
        //        NSString *deleteText = @"Delete";
        //        [cancelButton setBackgroundImage:[UIImage imageNamed:@"button_red_plain"] forState:UIControlStateNormal];
        //        cancelButton.titleLabel.font = [Utility fontForSize:_app._normalFont forString:addText forSize:CGSizeMake(cancelButton.frame.size.width, cancelButton.frame.size.height)];
        //        [cancelButton addTarget:self action:@selector(deleteItem:) forControlEvents:UIControlEventTouchDown];
        //        [cancelButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        //        [cancelButton setTitle:deleteText forState:UIControlStateNormal];
        //
        //        //rounded corners
        //        maskPath = [UIBezierPath bezierPathWithRoundedRect:cancelButton.bounds
        //                                         byRoundingCorners:(UIRectCornerAllCorners)
        //                                               cornerRadii:CGSizeMake(5.0, 5.0)];
        //        maskLayer = [[CAShapeLayer alloc] init];
        //        maskLayer.frame = self.view.bounds;
        //        maskLayer.path = maskPath.CGPath;
        //        cancelButton.layer.mask = maskLayer;
    }
    
    CGRect main_view_frame =product_radius_view.frame;
    //    main_view_frame.size.height=_scrollView.frame.size.height-(view_gap*2);
    main_view_frame.origin.y = view_gap;
    main_view_frame.origin.x = view_gap;
    main_view_frame.size.width = new_app_width;
    main_view_frame.size.height=global_y + (view_gap*2);
    [product_radius_view setFrame:main_view_frame];
    [self.view addSubview:_scrollView];
    
    CGSize contentsize = _scrollView.frame.size;
    contentsize.height = main_view_frame.origin.y + main_view_frame.size.height + (view_gap*4)+(footerHeight/2);
    [_scrollView setContentSize:contentsize];
    
    //footer container
    CGFloat bottom_align=(_app._viewHeight + 20) - footerHeight;
    footerContainer =[[UIView alloc]initWithFrame:CGRectMake(0, bottom_align, _app._viewWidth, footerHeight)];
    [footerContainer setBackgroundColor:[UIColor whiteColor]];
    [self.view insertSubview:footerContainer aboveSubview:_scrollView];
//    [self.view addSubview:footerContainer];
    [footerContainer addSubview:footer];
    
    //tick alert
    alert_image=[[UIImageView alloc]initWithFrame:CGRectMake((self.view.frame.size.width-48)/2, (self.view.frame.size.height-48)/2, 48, 48)];
    UIImage *aimage = [UIImage imageNamed:@"success"];
    [alert_image setImage:aimage];
    img_alert_container =[[UIView alloc]initWithFrame:self.view.bounds];
    [img_alert_container setBackgroundColor:[UIColor clearColor]];
    [img_alert_container addSubview:alert_image];
    [self.view addSubview:img_alert_container];
    [img_alert_container setHidden:YES];
    //    [self debug:self.view];
}

-(void)show_ingrdient_popup:(id)sender
{
    
    CGRect main_frame=self.view.frame;
    
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){
        main_frame.origin.y=_app._headerHeight+20;
    }
    else{
        main_frame.origin.y=_app._headerHeight;
    }
    
    main_frame.size.height=main_frame.size.height-headerView.frame.size.height;
    CGRect subview_frame=main_frame;
    subview_frame.size.height=250.0f;
    subview_frame.origin.x=0.0f;
    subview_frame.origin.y=(main_frame.size.height-subview_frame.size.height);
    
    Ingredients_pop_view=[[UIView alloc]initWithFrame:main_frame];
    
    UITapGestureRecognizer *dismiss_gesture=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(remove_View:)];
    dismiss_gesture.numberOfTapsRequired=1;
    dismiss_gesture.numberOfTouchesRequired=1;
    [Ingredients_pop_view addGestureRecognizer:dismiss_gesture];
    
    [Ingredients_pop_view setBackgroundColor:[UIColor colorWithRed:0 green:0 blue:0 alpha:0.3]];
    
    details_subview=[[UIView alloc]initWithFrame:subview_frame];
    [details_subview setBackgroundColor:[UIColor colorWithWhite:1.0f alpha:1.0]];
    [details_subview setAlpha:1.0f];
    
    UIButton  *close_button=[UIButton buttonWithType:UIButtonTypeCustom];
    [close_button setFrame:CGRectMake(5, 5, 25, 25)];
    [close_button setBackgroundImage:[UIImage imageNamed:@"close_picker_button.png"] forState:UIControlStateNormal];
    [close_button addTarget:self action:@selector(remove_View:) forControlEvents:UIControlEventTouchUpInside];
    
    int label_gap=10.0f;
    
    UILabel *detail_text=[[UILabel alloc]init];
    [detail_text setTextColor:[UIColor colorWithRed:(32/255.0) green:(35/255.0) blue:(36/255.0) alpha:1.0]];
    detail_text.numberOfLines=0;
    [detail_text setBackgroundColor:[UIColor clearColor]];
    [detail_text setFont:[UIFont fontWithName:_app._normalFont size:font_size13]];
    [detail_text setText:ingredients_str];
    if([UIDevice currentDevice].systemVersion.floatValue>=7.0){
        [detail_text setTextAlignment:NSTextAlignmentJustified];
    }
    
    NSMutableAttributedString* attrString = [[NSMutableAttributedString alloc]initWithString:detail_text.text];
    NSMutableParagraphStyle *style = [[NSMutableParagraphStyle alloc] init];
    [style setLineSpacing:8];
    [style setAlignment:NSTextAlignmentJustified];
    [attrString addAttribute:NSParagraphStyleAttributeName value:style range:NSMakeRange(0, detail_text.text.length)];
    detail_text.attributedText = attrString;
    
    
    CGSize desc_size = [self getUILabelFontSizeBasedOnText_width:subview_frame.size.width-(label_gap*2) _fontname:_app._normalFont _fontsize:font_size13 _text:detail_text.text];
    
    
    int max_value=Ingredients_pop_view.frame.size.height-150;
    UIScrollView *scroll_text;
    CGSize scroll_desc_size;
    BOOL isBigger_text=NO;
    
    
    int each_height;
    CGFloat line_height_total_spacing=0.0;
    CGFloat new_lbl_height=0.0;
    CGFloat Final_subview_height=0.0f;
    
    each_height=desc_size.height/13; // font size divided by total height
    line_height_total_spacing=each_height*8; // multiplied by line height spacing
    new_lbl_height=desc_size.height + line_height_total_spacing; // added with total height
    
    if(max_value>new_lbl_height)
    {
        desc_size.height=desc_size.height;
        Final_subview_height=new_lbl_height;
        [details_subview addSubview:detail_text];
        isBigger_text=NO;
    }
    else // label Text height exceeds than screen size reset maximum value as screen size
    {
        scroll_desc_size.width=desc_size.width;
        scroll_desc_size.height= main_frame.size.height-(main_frame.origin.y+_app._headerHeight+headerView.frame.size.height+headerView.frame.origin.y);
        
        //scrollview added
        
        scroll_text=[[UIScrollView alloc]init];
        scroll_text.showsHorizontalScrollIndicator=NO;
        scroll_text.showsVerticalScrollIndicator=NO;
        [scroll_text setBackgroundColor:[UIColor whiteColor]];
        [detail_text setBackgroundColor:[UIColor clearColor]];
        
        [scroll_text addSubview:detail_text];
        [details_subview addSubview:scroll_text];
        
        isBigger_text=YES;
        
        Final_subview_height=scroll_desc_size.height;
    }
    
    
    [detail_text setFrame:CGRectMake(label_gap, 35.0, subview_frame.size.width-(label_gap*2),new_lbl_height)];
    subview_frame.size.height=(close_button.frame.size.height+5)+(Final_subview_height+35);
    subview_frame.origin.y=(main_frame.size.height-subview_frame.size.height);
    [details_subview setFrame:subview_frame];
    
    UILabel *title_text=[[UILabel alloc]initWithFrame:CGRectMake(30, 5, details_subview.frame.size.width-60, 22.0f)];
    [title_text setTextAlignment:NSTextAlignmentCenter];
    [title_text setTextColor:[UIColor colorWithRed:(187.0/255.0) green:(32.0/255.0) blue:(1.0/255.0) alpha:1.0]]; // red
    title_text.numberOfLines=1;
    [title_text setBackgroundColor:[UIColor clearColor]];
    [title_text setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
    [title_text setText:@"Ingredients"];
    
    if(isBigger_text==YES){
        
        CGRect txt_frame= detail_text.frame;
        txt_frame.origin.y=0.0f;
        txt_frame.origin.x=0.0f;
        [detail_text setFrame:txt_frame];
    }
    
    [scroll_text setFrame:CGRectMake(label_gap, 30.0f, detail_text.frame.size.width, scroll_desc_size.height)];
    [scroll_text setContentSize:CGSizeMake(desc_size.width, detail_text.frame.size.height+20)];
    
    [details_subview addSubview:title_text];
    [details_subview addSubview:close_button];
    
    
    
    [Ingredients_pop_view addSubview:details_subview];
    
    details_subview.frame =CGRectMake(subview_frame.origin.x, self.view.frame.size.height, subview_frame.size.width, subview_frame.size.height);
    
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.4];
    [UIView setAnimationDelay:0.01];
    [UIView setAnimationCurve:UIViewAnimationCurveEaseIn];
    
    details_subview.frame =subview_frame;
    
    [UIView commitAnimations];
    [self.view addSubview:Ingredients_pop_view];
    
    
}

-(void)remove_View:(id)sender
{
    CGRect old_frame=details_subview.frame;
    
    [UIView animateWithDuration:0.5 animations:^{
        [details_subview setFrame:old_frame];
        details_subview.frame =CGRectMake(details_subview.frame.origin.x, self.view.frame.size.height, details_subview.frame.size.width, details_subview.frame.size.height);
    }completion:^(BOOL finished){
        [Ingredients_pop_view removeFromSuperview];
    }];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



-(IBAction)backButtonClicked
{
    if(_isQuantityChange==true){
        [self addItem:nil];
    }
    else{
        [self Close_Details];
    }
}
-(void)Close_Details
{
    if([_shoppingScreenDelegate isWebServiceRunning]){
        return;
    }
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [self.presentingViewController dismissViewControllerAnimated:NO completion:nil];
}


-(IBAction)setPricingByQuantity:(id)sender
{
    if(_product.approxAvgWgt > 0)
    {
        if(quality_option_button.selected == YES)
            return;
        if(sender){
            _productQuantity=(int)_productQuantity;
            if(_productQuantity==0){
                _productQuantity=1;//rest to start value
            }
        }
        quality_option_button.selected = YES;
        weight_option_button.selected = NO;
        
        [quality_option_button setBackgroundImage:[UIImage imageNamed:@"product_checked_box"] forState:UIControlStateNormal];
        [weight_option_button setBackgroundImage:[UIImage imageNamed:@"product_unchecked_box"] forState:UIControlStateNormal];
    }
    //  _quantityText.text = @"Qty";
    [footer.extended_text setText:@" Qty"];
    
    [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.0f", _productQuantity] forState:UIControlStateNormal];
    
    [self setTotalPriceText];
}


-(IBAction)setPricingByWeight:(id)sender
{
    if(_product.approxAvgWgt > 0)
    {
        if(weight_option_button != nil && weight_option_button.selected == YES)
            return;
        weight_option_button.selected = YES;
        quality_option_button.selected = NO;
        
        [weight_option_button setBackgroundImage:[UIImage imageNamed:@"product_checked_box"] forState:UIControlStateNormal];
        [quality_option_button setBackgroundImage:[UIImage imageNamed:@"product_unchecked_box"] forState:UIControlStateNormal];
        
    }
    
    //    _quantityText.text = [NSString stringWithFormat:@"%@s", _product.unitOfMeasure];
    
    [footer.extended_text setText:[[NSString stringWithFormat:@" %@s", _product.unitOfMeasure] lowercaseString]];
    [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.2f", _productQuantity] forState:UIControlStateNormal];
    
    [self setTotalPriceText];
}


- (void)setTotalPriceText
{
    float unitPrice;
    
    if(_product.promoType > 0)
        unitPrice = _product.promoPrice / _product.promoForQty;
    else
        unitPrice = _product.regPrice;
    
    if(quality_option_button != nil && quality_option_button.selected == YES)
        unitPrice *= _product.approxAvgWgt;
    
    product_total_price.text = [NSString stringWithFormat:@"$%0.2f", _productQuantity * unitPrice];
}

-(IBAction)addToItemCount:(id)sender
{
    _isQuantityChange = true;
    
    int productLimit = 100;
    
    if(_productQuantity >=productLimit){
        _productQuantity = (float)productLimit;
        return;
    }
    
    //    [_quantityValue setText:[NSString stringWithFormat:@": %d", ++_productQuantity]];
    
    [footer.value_Display.titleLabel setTextAlignment:NSTextAlignmentRight];
    
    //_productQuantity +=100;
    
    //new code
    
    if(quality_option_button == nil && quality_option_button == nil){ // default is quantity
        if ([_product.unitOfMeasure caseInsensitiveCompare:@"LB"]==NSOrderedSame && _product.approxAvgWgt<=0)
        {
            //weight product
            _productQuantity += 0.25;
            [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.2f", _productQuantity] forState:UIControlStateNormal];
        } else {
            //qty product
            _productQuantity += 1;
            [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.0f", _productQuantity] forState:UIControlStateNormal];
        }
    }
    else{
        if(quality_option_button != nil && quality_option_button.selected == YES)// Quantity
        {
            _productQuantity += 1;
            [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.0f", _productQuantity] forState:UIControlStateNormal];
        }
        else
        {
            _productQuantity += 0.25;
            [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.2f", _productQuantity] forState:UIControlStateNormal];
        }
    }
    
    [self setTotalPriceText];
}

-(IBAction)subtractFromItemCount:(id)sender
{
    _isQuantityChange = true;
    
 
    
    //    _quantityValue.text = [NSString stringWithFormat:@": %d", --_productQuantity];
    
    [footer.value_Display.titleLabel setTextAlignment:NSTextAlignmentRight];
    
    if(quality_option_button == nil && quality_option_button == nil){ // default is quantity
        if ([_product.unitOfMeasure caseInsensitiveCompare:@"LB"]==NSOrderedSame && _product.approxAvgWgt<=0)
        {
            if(_productQuantity == 0.25)
                return;
            //weight product
            _productQuantity -= 0.25;
            [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.2f", _productQuantity] forState:UIControlStateNormal];
        } else {
            if(_productQuantity == 1)
                return;
            //qty product
            _productQuantity -= 1;
            [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.0f", _productQuantity] forState:UIControlStateNormal];
        }
    }
    else{
        if(quality_option_button != nil && quality_option_button.selected == YES)// Quantity
        {
            if(_productQuantity == 1)
                return;
            _productQuantity -= 1;
            [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.0f", _productQuantity] forState:UIControlStateNormal];
        }
        else
        {
            if(_productQuantity == 0.25)
                return;
            _productQuantity -= 0.25;
            [footer.value_Display setTitle:[NSString stringWithFormat:@"%0.2f", _productQuantity] forState:UIControlStateNormal];
        }
    }
    [self setTotalPriceText];
}

- (IBAction)addItem:(id)sender
{
    if(currentDetailType==PRODUCT_MODIFY){
        [self modifyItem:nil];
        return;
    }
    _app._productToAdd = _product;
    _app._productToModify = nil;
    _app._productToDelete = nil;
    [self updateItem];
    
#ifdef CLP_ANALYTICS
    //Analytics
    if(_product){
        @try {                                                                //Product added to list
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"ProductAdded" forKey:@"event_name"];
            [data setValue:_product.sku forKey:@"SKU"];
            [data setValue:_product.upc forKey:@"item_id"];
            [data setValue:_product.brand forKey:@"brand"];
            [data setValue:_product.description forKey:@"item_name"];
            [data setValue:[NSString stringWithFormat:@"%f",_product.regPrice] forKey:@"price"];
            [data setValue:[NSString stringWithFormat:@"%f",_product.promoPrice]forKey:@"promo_price"];
            [data setValue:_product.mainCategory forKey:@"category"];
            if(_product.weight > 0)
                [data setValue:[NSString stringWithFormat:@"%f",_product.weight] forKey:@"quantity"];
            else
                [data setValue:[NSString stringWithFormat:@"%d",_product.qty] forKey:@"quantity"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
    }
#endif
}

- (IBAction)modifyItem:(id)sender
{
    _app._productToAdd = nil;
    _app._productToModify = _product;
    _app._productToDelete = nil;
    [self updateItem];
    
#ifdef CLP_ANALYTICS
    //Analytics
    if(_product){
        @try {                                                                //Product quantity modified in list
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"ProductQuantityModified" forKey:@"event_name"];
            [data setValue:_product.sku forKey:@"SKU"];
            [data setValue:_product.upc forKey:@"item_id"];
            [data setValue:_product.brand forKey:@"brand"];
            [data setValue:_product.description forKey:@"item_name"];
            [data setValue:[NSString stringWithFormat:@"%f",_product.regPrice] forKey:@"price"];
            [data setValue:[NSString stringWithFormat:@"%f",_product.promoPrice]forKey:@"promo_price"];
            [data setValue:_product.mainCategory forKey:@"category"];
            if(_product.weight > 0)
                [data setValue:[NSString stringWithFormat:@"%f",_product.weight] forKey:@"quantity"];
            else
                [data setValue:[NSString stringWithFormat:@"%d",_product.qty] forKey:@"quantity"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
    }
#endif
}


- (IBAction)deleteItem:(id)sender
{
    _app._productToAdd = nil;
    _app._productToModify = nil;
    _app._productToDelete = _product;
    
    ListDeleteItemRequest *request = [[ListDeleteItemRequest alloc] init];
    request.accountId = _login.accountId;
    if(_isCurrentAcitveList){
        request.listId = _app._currentShoppingList.listId;
        request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;
    }
    else{
        request.listId = _app._selectedShoppingList.listId;
        request.appListUpdateTime = _app._selectedShoppingList.serverUpdateTime;
    }
    request.sku = _product.sku;
    
    
    NSNumber *serverUpdateTime;
    float totalPrice;
    if(_isCurrentAcitveList){
        serverUpdateTime=_app._currentShoppingList.serverUpdateTime;
        totalPrice=_app._currentShoppingList.totalPrice;
    }
    else{
        serverUpdateTime=_app._selectedShoppingList.serverUpdateTime;
        totalPrice=_app._selectedShoppingList.totalPrice;
    }
    
    if([_shoppingScreenDelegate shoppingListVisible] == YES)
    {
        
        request.returnCurrentList = [NSNumber numberWithBool:YES];
        LogInfo(@"SHOPPING_LIST: DELETE: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f",totalPrice]);
    }
    else
    {
        request.returnCurrentList = [NSNumber numberWithBool:NO];
        LogInfo(@"SHOPPING_LIST: DELETE: setting returnCurrentList = NO, appListUpdateTime = %llu, totalPrice = %@", [serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", totalPrice]);
    }
    
    _progressDialog = [[ProgressDialog alloc] initWithView:_app._currentView message:@"Updating your list..."];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
    [_service execute:LIST_DELETE_ITEM_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
}


- (void)updateItem
{
    //hide the keyboard when clicking on 'Add to List' or 'Change Quantity'
    [self.view endEditing:YES];
    
    if(_isCurrentAcitveList){
        
        if([Utility isEmpty:_app._currentShoppingList])
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Show Current List" message:@"No active list to add into. Goto lists and make one active."];
            [dialog show];
            _isQuantityChange = false;
            return;
        }
    }
    
    if ([_product.unitOfMeasure caseInsensitiveCompare:@"LB"]==NSOrderedSame) {
        
        if (quality_option_button != nil
            && quality_option_button.selected == true)// Quantity
        {
            _product.qty = (int) _productQuantity;
            _product.weight = 0.0f;
        } else {
            _product.weight = (float) _productQuantity;
            _product.qty = 0;
        }
        
    } else {
        _product.qty = (int) _productQuantity;
        _product.weight = 0.0f;
    }
    
    NSString *listId;
    NSNumber *serverUpdateTime;
    float totalPrice;
    if(_isCurrentAcitveList){
        listId= _app._currentShoppingList.listId;
        serverUpdateTime=_app._currentShoppingList.serverUpdateTime;
        totalPrice=_app._currentShoppingList.totalPrice;
    }
    else{
        listId= _app._selectedShoppingList.listId;
        serverUpdateTime=_app._selectedShoppingList.serverUpdateTime;
        totalPrice=_app._selectedShoppingList.totalPrice;
    }
    
    ListAddItemRequest *request = [[ListAddItemRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = listId;
    request.sku = _product.sku;
    //request.customerComment = _instructionsTextField.text;
    
    if(more_details.comments.text.length>60){
        request.customerComment = [more_details.comments.text substringToIndex:60];
        _product.customerComment = [more_details.comments.text substringToIndex:60];
    }else{
        request.customerComment = more_details.comments.text;
        _product.customerComment = more_details.comments.text;
    }
    
    request.appListUpdateTime =[NSString stringWithFormat:@"%@",serverUpdateTime];
    
    if(_product.approxAvgWgt > 0)
    {
        if(weight_option_button.selected == YES)
        {
            request.purchaseBy = @"W";
            request.weight = (float)_productQuantity;
        }
        else
        {
            request.purchaseBy = @"E";
            request.qty = (int)_productQuantity;
        }
    }
    else
    {
        request.purchaseBy = @"E";
        request.qty = (int)_productQuantity;
    }
    LogInfo(@"SHOPPING_LIST: ADD: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", totalPrice]);
    
    //request.returnCurrentList = [NSNumber numberWithBool:YES];
    request.returnCurrentList = @"true";
    
    _progressDialog = [[ProgressDialog alloc] initWithView:self.view message:@"Updating your list..."];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
    [_service execute:LIST_ADD_ITEM_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
}



- (void)handleListServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];
    
    
    if(status == 200) // service success
    {
        [_shoppingScreenDelegate handleListServiceResponse:responseObject httpStatusCode:status _isCurrentList:_isCurrentAcitveList];
        [self updateListCount];
        if(_app._productToAdd != nil )
        {
            [_progressDialog dismiss];
            [self showTick];
            _app._productToAdd = nil;
            [self performSelector:@selector(Close_Details) withObject:nil afterDelay:1.0f];
            //[footer.Add_to_Card setTitle:@"Change Quantity" forState:UIControlStateNormal];
             //[footer.Add_to_Card setTitle:@"Update" forState:UIControlStateNormal];
            //[footer.Add_to_Card setHidden:YES];
            currentDetailType=PRODUCT_MODIFY;
        }
        if(_app._productToModify != nil )
        {
            [_progressDialog dismiss];
            [self showTick];
            [self performSelector:@selector(Close_Details) withObject:nil afterDelay:1.0f];
            _app._productToModify = nil;
        }
        if(_app._productToDelete !=nil){
            _app._productToDelete =nil;
            [self performSelector:@selector(Close_Details) withObject:nil afterDelay:1.0f];
        }
    }
    else // service failure
    {
        if(status == 422) // backend error
        {
            WebServiceError *error = [_service getError];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Update List Failed" message:error.errorMessage];
            [dialog show];
        }
        else if(status == 408) // timeout error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
            [dialog show];
        }
        else // http error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Server Error" message:COMMON_ERROR_MSG];
            [dialog show];
        }
    }
    _service = nil;
    _app._productToAdd = nil;
    _app._productToModify = nil;
    _app._productToDelete = nil;
}
//animated tick
-(void)showTick{
    [img_alert_container setAlpha:0.f];
    
    [UIView animateWithDuration:0.5f delay:0.f options:UIViewAnimationOptionCurveEaseIn animations:^{
        [img_alert_container setHidden:NO];
        
        [img_alert_container setAlpha:1.f];
    } completion:^(BOOL finished) {
        [UIView animateWithDuration:1.f delay:1.f options:UIViewAnimationOptionCurveEaseInOut animations:^{
            [img_alert_container setAlpha:0.f];
            
        } completion:^(BOOL finished){
            [img_alert_container setHidden:NO];
            
        }];
    }];
}

// UIKeyboardDidShowNotification handler
- (void)keyboardWasShown:(NSNotification*)aNotification
{
    NSDictionary* info = [aNotification userInfo];
    CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    UIEdgeInsets contentInsets = UIEdgeInsetsMake(0.0, 0.0, kbSize.height, 0.0);
    _scrollView.contentInset = contentInsets;
    _scrollView.scrollIndicatorInsets = contentInsets;
    
    // If active text field is hidden by keyboard, scroll it so it's visible
    CGRect rect = _scrollView.frame;
    rect.size.height -= kbSize.height;
    CGPoint origin = _instructionsTextField.frame.origin;
    origin.y -= _scrollView.contentOffset.y - _app._headerHeight;
    int scrollDistance = _instructionsTextField.frame.origin.y - rect.size.height;
    
    // adjust for the status bar overlap on iOS7
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        scrollDistance += 20;
    
    if(!CGRectContainsPoint(rect, origin))
    {
        CGPoint scrollPoint = CGPointMake(0.0, scrollDistance);
        [_scrollView setContentOffset:scrollPoint animated:YES];
    }
}


// UIKeyboardWillHideNotification handler
- (void)keyboardWillBeHidden:(NSNotification*)aNotification
{
    UIEdgeInsets contentInsets = UIEdgeInsetsZero;
    _scrollView.contentInset = contentInsets;
    _scrollView.scrollIndicatorInsets = contentInsets;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// text view delegate begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark UITextField Delegate
- (BOOL) textFieldShouldBeginEditing:(UITextView *)textView
{
    //_instructionsTextField.text = @"";
    
    if([Utility isEmpty:more_details.comments.text]){
        more_details.comments.text = @"";
    }
    else if([more_details.comments.text isEqualToString:DEFAULT_PRODUCT_COMMENTS]){
        more_details.comments.text = @"";
    }
    //more_details.comments.text = @"";
    return YES;
}


- (BOOL)textFieldShouldReturn:(UITextView *)textView
{
    [textView resignFirstResponder];
    //    _product.customerComment = _instructionsTextField.text;
    //    [_instructionsTextField resignFirstResponder];
    
    _product.customerComment = more_details.comments.text;
    [more_details.comments resignFirstResponder];
    
    return YES;
}

-(BOOL)textFieldShouldEndEditing:(UITextField *)textField{
    return YES;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// text view delegate end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark WebServiceListener Delegate
- (void)onServiceResponse:(id)responseObject
{
    if([responseObject isKindOfClass:[ShoppingList class]])
    {
        [_progressDialog dismiss];
        [self handleListServiceResponse:responseObject];
    }
}

-(void)updateListCount{
    
    if(_isCurrentAcitveList){
        if([Utility isEmpty:_app._currentShoppingList])
        {
            NSString *currentActiveListId;
            currentActiveListId=[_app getCurrentActiveListIdForAccountId:_login.accountId];
            if(![currentActiveListId isEqualToString:@""]){
                [_shoppingScreenDelegate getCurrentShoppingList:currentActiveListId];
                NSString *currentActiveListName,*currentActiveListProdTotal;
                currentActiveListName=[_app getCurrentActiveListNameForAccountId:_login.accountId];
                currentActiveListProdTotal=[_app getCurrentActiveListProdTotalForAccountId:_login.accountId];
                more_details.more_items.text=[NSString stringWithFormat:@"%@ (%@ items)",currentActiveListName,currentActiveListProdTotal];
            }
            else{
                more_details.more_items.text=@"You do not have an active list";
            }
        }else{
            more_details.more_items.text=[NSString stringWithFormat:@"%@ (%d items)",_app._currentShoppingList.name,(int)_app._currentShoppingList.productList.count];
        }
    }
    else{
        if([Utility isEmpty:_app._selectedShoppingList])
        {
            more_details.more_items.text=@"";
        }else{
            more_details.more_items.text=[NSString stringWithFormat:@"%@ (%d items)",_app._selectedShoppingList.name,(int)_app._selectedShoppingList.productList.count];
        }
    }
}


-(void)dismiss_keyboard
{
    [more_details.comments resignFirstResponder];
    
}

-(CGSize)getUILabelFontSizeBasedOnText_width:(CGFloat)width _fontname:(NSString*)font _fontsize:(CGFloat)fsize _text:(NSString*)text{
    @try {
        CGSize size = CGSizeMake(width, 0);
        //    if([Utility isEmpty:text]){
        if(text.length<=0){
            return size;
        }
        CGSize constrainedSize = CGSizeMake(size.width, 99999);
        NSDictionary *attributesDictionary = [NSDictionary dictionaryWithObjectsAndKeys:
                                              [UIFont fontWithName:font size:fsize], NSFontAttributeName,
                                              nil];
        NSMutableAttributedString *string = [[NSMutableAttributedString alloc] initWithString:text attributes:attributesDictionary];
        
        CGRect requiredHeight = [string boundingRectWithSize:constrainedSize options:NSStringDrawingUsesLineFragmentOrigin context:nil];
        if (requiredHeight.size.width > size.width) {
            requiredHeight = CGRectMake(0,0, size.width, requiredHeight.size.height);
        }
        size.height = ceil(requiredHeight.size.height);//+10); // plus 10 for top and bottom gap of label
        return size;
        
        
    }
    @catch (NSException *exception) {
        LogInfo(@"ShoppingScreenVC - getUILabelFontSizeBasedOnText_width: %@",exception.reason);
        
        CGSize size = CGSizeMake(width, 0);
        return size;
    }
}

- (int)lineCountForLabel:(UILabel *)label {
    CGSize constrain = CGSizeMake(label.bounds.size.width, 99999);
    CGSize size = [label.text sizeWithFont:label.font constrainedToSize:constrain lineBreakMode:NSLineBreakByWordWrapping];
    
    return ceil(size.height / label.font.lineHeight);
}

@end
