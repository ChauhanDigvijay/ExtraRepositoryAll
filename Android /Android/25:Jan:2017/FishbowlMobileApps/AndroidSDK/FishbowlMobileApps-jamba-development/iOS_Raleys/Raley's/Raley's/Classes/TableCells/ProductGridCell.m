//
//  ProductGridCell.m
//  Raley's
//
//  Created by Bill Lewis on 10/11/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "ProductGridCell.h"
#import "ProductDetailView.h"
#import "Utility.h"
#import "Logging.h"
#import "ProductDetails.h"
#import "ListAddItemRequest.h"


#define GRIDCELL_PADDING 0.05 // In Percentage

@implementation ProductGridCell{
}

@synthesize _productImageView;
@synthesize _promoImageView;
@synthesize _background;
@synthesize _addButton;
@synthesize _promoPriceText;
@synthesize _promoPriceValue;
@synthesize _descriptionLabel;
@synthesize _extendedDisplayLabel;
@synthesize _regPriceValue;
@synthesize _product;
@synthesize _priceCell;
@synthesize _priceCellContainer;
@synthesize _background_new;
@synthesize _priceCell_new;
@synthesize _parent;
//@synthesize _extendedDisplayLabel_new;
@synthesize  loading;
@synthesize _addToList;
@synthesize _shoppingScreenDelegate;
@synthesize addType;
@synthesize productQty;
@synthesize lastSku;
@synthesize extends_detailLabl;
@synthesize category;

@synthesize see_offer_view;


- (id)initWithFrame:(CGRect)frame;
{
    if ((self = [super initWithFrame:frame]))
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        _product = _app._currentProduct;
        _login = [_app getLogin];
        int width = frame.size.width;
        int height = frame.size.height;
        int textXOrigin = width * GRIDCELL_PADDING;
        //        int textYEnd =height*.98;
        int textWidth = width - (textXOrigin * 2);
        int productImageSize = frame.size.width;
        _background_new=[[UIView alloc]initWithFrame:frame];
        //        [_background_new.layer setCornerRadius:5.0f];
        [_background_new setBackgroundColor:[UIColor whiteColor]];
        
        [self.layer setCornerRadius:5.0f];
        
        self.backgroundView = _background_new;
        
        
        //description
        _descriptionLabel = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, textXOrigin, textWidth, 28.0f)];
        _descriptionLabel.textAlignment = NSTextAlignmentCenter;
        _descriptionLabel.numberOfLines =2;
        [_descriptionLabel setLineBreakMode:NSLineBreakByWordWrapping];
        _descriptionLabel.adjustsFontSizeToFitWidth = NO;
        _descriptionLabel.lineBreakMode = NSLineBreakByTruncatingTail;
        _descriptionLabel.backgroundColor = [UIColor clearColor];
        // [_descriptionLabel.layer setBorderWidth:0.5f];
        _descriptionLabel.textColor = [UIColor colorWithRed:(29/255.0) green:(29/255.0) blue:(29/255.0) alpha:1.0];
        
        [self.contentView addSubview:_descriptionLabel];
        
        
        //extends_detailLabl
        
        //description
        extends_detailLabl = [[UILabel alloc]initWithFrame:CGRectMake(textXOrigin, textXOrigin-5, textWidth/2,13.0f)]; /* Raleys 2.1 code updated */
        extends_detailLabl.textAlignment = NSTextAlignmentLeft;
        extends_detailLabl.numberOfLines = 1;
        [extends_detailLabl setLineBreakMode:NSLineBreakByWordWrapping];
        extends_detailLabl.backgroundColor = [UIColor clearColor];
        //[extends_detailLabl.layer setBorderWidth:0.5f];
        [extends_detailLabl setText:@"Extended"];
        extends_detailLabl.textColor = [UIColor colorWithRed:(29/255.0) green:(29/255.0) blue:(29/255.0) alpha:1.0];
        
        [self.contentView addSubview:extends_detailLabl];
        
        
        /* Raleys 2.1 code updated */
        
        /* ---------------- See offer view and its content ---------------- */
        
        
        see_offer_view=[[UIView alloc]initWithFrame:CGRectMake(textXOrigin+extends_detailLabl.frame.size.width+5, textXOrigin-2, 70, 13.0f)];
        
        [see_offer_view setBackgroundColor:[UIColor clearColor]];
        
        
        UILabel *see_off_txt=[[UILabel alloc]initWithFrame:CGRectMake(13, 0, 70, 13.0f)];
        
        [see_off_txt setBackgroundColor:[UIColor clearColor]];
        
        [see_off_txt setTextColor:[UIColor colorWithRed:(204.0/255.0) green:(0.0/255.0) blue:(0.0/255.0) alpha:1.0]]; // red color
        
        [see_off_txt setText:@"See Details"];
        
        [see_off_txt setTextAlignment:NSTextAlignmentLeft];
        
        [see_off_txt setFont:[UIFont fontWithName:_app._normalFont size:font_size10-2]];
        
        //[see_offer_view addSubview:see_off_txt];//v2.
        if(_app._currentProduct.promoType > 0)
        {
            [see_offer_view setHidden:NO];
        }
        else{
            [see_offer_view setHidden:YES];
        }
        
        [self.contentView addSubview:see_offer_view];
        
        /* Raleys 2.1 code updated */
        

        
        
        //image
        _productImageView = [[UIImageView alloc] initWithFrame:CGRectMake((width - productImageSize) / 2, textXOrigin+height * .20, productImageSize, productImageSize)];
        [self.contentView addSubview:_productImageView];
        
        int priceCellWidth=width; // .75
        int priceCellHeight=30;
        int cellGap=priceCellHeight*.10;//gap between cell
        int priceCellContWidth=priceCellWidth-(cellGap*2);//cell width
        int priceCellContHeight=(priceCellHeight* .80)-cellGap*2;
        int promoImageWidth=priceCellContHeight;//image width
        int promoImageHeight=priceCellContHeight;//image height
        int regPriceValueWidth=(priceCellContWidth* .60)-cellGap*4;
        
        //textYEnd-(priceCellHeight)
        _priceCell_new=[[UIImageView alloc] initWithFrame:CGRectMake(0, 0,priceCellWidth,priceCellHeight)];
        
        _priceCell_new.backgroundColor=[UIColor blackColor];
        [_priceCell_new setImage:[UIImage imageNamed:@"new_pricecell_normal"]];
        [_priceCell_new setClipsToBounds:YES];
        [self.contentView addSubview:_priceCell_new];
        _priceCellContainer=[[UIImageView alloc] initWithFrame:CGRectMake(cellGap, (priceCellHeight-promoImageHeight)/2,priceCellContWidth,priceCellContHeight)];
        _priceCellContainer.backgroundColor = [UIColor clearColor];
        
        
        //on sale image
        CGFloat new_cellGap;
        if(_app._currentProduct.promoType > 0)
        {
            new_cellGap=cellGap*2+promoImageWidth;
        }
        else
        {
            new_cellGap=cellGap*1;
        }
        _promoImageView = [[UIImageView alloc] initWithFrame:CGRectMake(cellGap, 3, promoImageWidth, promoImageHeight)];
        [_promoImageView setImage:[UIImage imageNamed:@"sale_icon"]];
        [self._priceCellContainer addSubview:_promoImageView];
        
        //reg price
        _regPriceValue = [[UILabel alloc] initWithFrame:CGRectMake((new_cellGap),3, regPriceValueWidth, promoImageHeight) ];
        
        _regPriceValue.numberOfLines = 1;
        _regPriceValue.backgroundColor = [UIColor clearColor];
        [_regPriceValue setFont:[UIFont fontWithName:_app._normalFont size:font_size17]];
        _regPriceValue.textColor = [UIColor colorWithRed:(32/255.0) green:(35/255.0) blue:(36/255.0) alpha:1.0];
        
        // _regPriceValue.textColor = [UIColor colorWithRed:.2 green:.55 blue:.26 alpha:1.0];
        [self._priceCellContainer addSubview:_regPriceValue];
        
        
        loading = [[UIActivityIndicatorView alloc]initWithFrame:CGRectMake((frame.size.width-20)/2, (frame.size.height-20)/3, 20, 20)];
        [loading setColor:[UIColor whiteColor]];
        [loading setHidesWhenStopped:YES];
        [loading stopAnimating];
        [self insertSubview:loading aboveSubview:_productImageView];
        
        //add to list
        _addToList = [[UIButton alloc]initWithFrame:CGRectMake((cellGap*2)+(promoImageWidth+regPriceValueWidth)-4,0,45,promoImageHeight+6)];
        [_addToList setTitle:@"Add" forState:UIControlStateNormal];
        [_addToList setBackgroundColor:[UIColor colorWithRed:(204.0/255.0) green:(0.0/255.0) blue:(0.0/255.0) alpha:1.0] ];
        [_addToList.titleLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size13]];
        [_addToList setUserInteractionEnabled:YES];
        [_addToList setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_addToList.layer setCornerRadius:4.0f];
        
        BOOL found = NO;
        //check whether product was already added
        if(_app._currentShoppingList != nil && _app._currentShoppingList.productList != nil)
        {
            for(Product *product in _app._currentShoppingList.productList)
            {
                if([product.sku isEqualToString:_product.sku])
                {
                    found = YES;
                    addType=PRODUCT_MODIFY;
                    break;
                }
            }
        }
        if(found == NO)
        {
            addType=PRODUCT_ADD;
        }
        [_addToList addTarget:self action:@selector(addProduct) forControlEvents:UIControlEventTouchUpInside];
        // [_addToList addTarget:self action:@selector(addProduct) forControlEvents:UIControlEventTouchUpInside | UIControlEventTouchUpOutside];
        [self._priceCellContainer addSubview:_addToList];
        [self._priceCellContainer setUserInteractionEnabled:YES];
        
        [self._priceCell_new addSubview:_priceCellContainer];
        [self._priceCell_new setUserInteractionEnabled:YES];
        
    }
    
    UITapGestureRecognizer *singletap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(itemPressed:)];
    [singletap setNumberOfTapsRequired:1];
    [singletap setNumberOfTouchesRequired:1];
    [singletap setCancelsTouchesInView:YES];
    
    [singletap setDelegate:self];
    [self addGestureRecognizer:singletap];
    
    //    [self addGestureRecognizer:pressGesture];
    
    return self;
}
//-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
//{
//
//    UITouch *touch = [[event allTouches] anyObject];
//    CGPoint location=[touch locationInView:self];
//
//    CGFloat button_x=location.x;
//
//    CGFloat button_y=location.y;
//    if(button_x>self.frame.size.width-55.0f && button_y>self.frame.size.height-45.0f){
//        [self addProduct];
//    }
//}
//- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer
//shouldRecognizeSimultaneouslyWithGestureRecognizer:
//(UIGestureRecognizer *)otherGestureRecognizer
//
//{
//    return NO;
//}



-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch
{
    CGPoint location=[touch locationInView:gestureRecognizer.view];
    
    CGFloat height=self.frame.size.height-45.0f;
    
    if(location.y<height){
        
        return YES;
    }
    else{
        return NO;
    }
    
}

- (void)itemPressed:(UITapGestureRecognizer *)gestureRecognizer
{
    if(_service!=nil){
        return;//avoid multiple request
    }
    [UIView animateWithDuration:0.0 delay:0.1 options:UIViewAnimationOptionCurveEaseInOut animations:^{
        self.alpha = 0.75;
    }completion:^(BOOL finished){
        self.alpha=1.0;
        
        
        //    self.alpha = 1.0;
        int productDetaiType;
        BOOL found = NO;
        Product *detailProduct;
        
        if(_app._currentShoppingList != nil && _app._currentShoppingList.productList != nil)
        {
            for(Product *product in _app._currentShoppingList.productList)
            {
                if([product.sku isEqualToString:_product.sku])
                {
                    found = YES;
                    detailProduct = product;
                    productDetaiType = PRODUCT_MODIFY;//use current product details of the cell
                    break;
                }
            }
        }
        
        if(found == NO)
        {
            detailProduct = _product;//use default assigned product of the cell
            productDetaiType = PRODUCT_ADD;
        }
        
        
        ProductDetails *products = [[ProductDetails alloc]initWithNibName:@"ProductDetails" bundle:nil];
        [Utility Viewcontroller:self.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
        [_parent presentViewController:products animated:NO completion:nil];
        products._shoppingScreenDelegate = (id<ShoppingScreenDelegate>)_app._shoppingScreenVC;
        [products creating_controls:detailProduct _category:category detailType:productDetaiType _frame:_app._currentView.frame _isCurrentAcitveList:YES];
        
    }];
    //    }
    
}


- (void)addProduct
{
    if(_service!=nil){
        return;//avoid multiple request
    }
    if(_app._currentShoppingList != nil && _app._currentShoppingList.productList != nil)
    {
        for(Product *product in _app._currentShoppingList.productList)
        {
            if([product.sku isEqualToString:_product.sku])
            {
                _product = product;//use current/updated product in the shopping list
                break;
            }
        }
    }
    
    
    
    if(addType==PRODUCT_ADD){
        _app._productToAdd = _product;
        _app._productToModify = nil;
        _app._productToDelete = nil;
    }else{
        _app._productToAdd = nil;
        _app._productToModify = _product;
        _app._productToDelete = nil;
    }
    [self updateItem];
    
#ifdef CLP_ANALYTICS
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


- (void)updateItem
{
    //hide the keyboard when clicking on 'Add to List' or 'Change Quantity'
    
    if([Utility isEmpty:_app._currentShoppingList])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:_app._shoppingScreenVC.view title:@"Show Current List" message:@"No active list to add into. Goto lists and make one active."];
        [dialog show];
        return;
    }
    
    if(_service!=nil)return;
    
    NSString *listId;
    NSNumber *serverUpdateTime;
    float totalPrice;
    listId= _app._currentShoppingList.listId;
    serverUpdateTime=_app._currentShoppingList.serverUpdateTime;
    totalPrice=_app._currentShoppingList.totalPrice;
    
    ListAddItemRequest *request = [[ListAddItemRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = listId;
    request.sku = _product.sku;
    request.customerComment = _product.customerComment;
    request.appListUpdateTime = [serverUpdateTime stringValue];
    
    if(_product.approxAvgWgt > 0)//if product by weight option available
    {
        
        if(_product.weight==0 && _product.qty==0){//by default add by qty
            request.qty = _product.qty+1;
            productQty=request.qty;
            request.purchaseBy = @"E";
            
        }else if(_product.weight==0 && _product.qty!=0){//if weight is zero add by qty
            request.qty = (_product.qty+1);
            productQty=request.qty;
            request.purchaseBy = @"E";
            
        }else if(_product.weight!=0 && _product.qty==0){//if qty is zero add by weight
            request.weight = (float)(_product.weight+0.25);//v2.5 fix
            productQty=request.weight;
            request.purchaseBy = @"W";
            
        }else{
            request.qty = (_product.qty+1);//by exception add by qty
            productQty=request.qty;
            request.purchaseBy = @"E";
        }
    }
    else
    {
        request.purchaseBy = @"E";
        if(_product.qty==0){//for first time add product
            request.qty = _product.qty+1;
            productQty=request.qty;
        }
        else{
            request.qty = _product.qty+1;
            productQty=request.qty;
        }
    }
    LogInfo(@"SHOPPING_LIST: ADD: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", totalPrice]);
//    request.returnCurrentList = [[NSNumber numberWithBool:YES] stringValue];
    request.returnCurrentList = @"true";

    _progressDialog = [[ProgressDialog alloc] initWithView:_app._shoppingScreenVC.view message:@"Updating your list..."];
    [_progressDialog show];
    
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
    [_service execute:LIST_ADD_ITEM_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
}



- (void)handleListServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];
    
    
    if(status == 200) // service success
    {
        [_shoppingScreenDelegate handleListServiceResponse:responseObject httpStatusCode:status _isCurrentList:YES];
        //[self updateListCount];
        if(_app._productToAdd != nil )
        {
            addType=PRODUCT_MODIFY;
            [_progressDialog dismiss];
            _app._productToAdd = nil;
        }
        if(_app._productToModify != nil )
        {
            [_progressDialog dismiss];
            _app._productToModify = nil;
            
        }
        if(_app._productToDelete !=nil){
            _app._productToDelete =nil;
        }
        //manualy update local _product after response is success
        if(_product.approxAvgWgt > 0)
        {
            if(_product.weight==0 && _product.qty==0){
                _product.qty=productQty;
                _product.weight = 0;
            }else if(_product.weight==0 && _product.qty!=0){
                _product.qty=productQty;
                _product.weight = 0;
                
            }else if(_product.weight!=0 && _product.qty==0){
                _product.weight=productQty;
                _product.qty = 0;
            }else{
                _product.qty=productQty;
                _product.weight = 0;
            }
        }
        else
        {
            _product.qty=productQty;
            _product.weight = 0;
        }
        lastSku=_product.sku;
        [self showTick:_app._shoppingScreenVC.view];
        //[self removeFromSuperview];
    }
    else // service failure
    {
        if(status == 422) // backend error
        {
            WebServiceError *error = [_service getError];
            TextDialog *dialog = [[TextDialog alloc] initWithView:_app._shoppingScreenVC.view title:@"Update List Failed" message:error.errorMessage];
            [dialog show];
        }
        else if(status == 408) // timeout error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:_app._shoppingScreenVC.view title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
            [dialog show];
        }
        else // http error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:_app._shoppingScreenVC.view title:@"Server Error" message:COMMON_ERROR_MSG];
            [dialog show];
        }
    }
    
    _service = nil;
    _app._productToAdd = nil;
    _app._productToModify = nil;
    _app._productToDelete = nil;
}
//tick alert
-(void)showTick:(UIView*)parent{
    UIView      *img_alert_container;
    
    UIImageView *alert_image=[[UIImageView alloc]initWithFrame:CGRectMake((parent.frame.size.width-48)/2, (parent.frame.size.height-48)/2, 48, 48)];
    UIImage *aimage = [UIImage imageNamed:@"success"];
    [alert_image setImage:aimage];
    img_alert_container =[[UIView alloc]initWithFrame:parent.bounds];
    [img_alert_container setBackgroundColor:[UIColor clearColor]];
    [img_alert_container addSubview:alert_image];
    [parent addSubview:img_alert_container];
    [img_alert_container setHidden:YES];
    [img_alert_container setAlpha:0.f];
    
    [UIView animateWithDuration:0.5f delay:0.f options:UIViewAnimationOptionCurveEaseIn animations:^{
        [img_alert_container setHidden:NO];
        
        [img_alert_container setAlpha:1.f];
    } completion:^(BOOL finished) {
        [UIView animateWithDuration:1.f delay:1.f options:UIViewAnimationOptionCurveEaseInOut animations:^{
            [img_alert_container setAlpha:0.f];
            
        } completion:^(BOOL finished){
            [img_alert_container removeFromSuperview];
            
        }];
    }];
}
#pragma mark WebServiceListener Delegate
- (void)onServiceResponse:(id)responseObject
{
    if([responseObject isKindOfClass:[ShoppingList class]])
    {
        [_progressDialog dismiss];
        [self handleListServiceResponse:responseObject];
    }
}

@end
