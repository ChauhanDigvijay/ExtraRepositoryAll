//
//  ProductDetailView.m
//  Raley's
//
//  Created by Bill Lewis on 10/11/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "SmartTextView.h"
#import "ListAddItemRequest.h"
#import "ListDeleteItemRequest.h"
#import "ShoppingList.h"
#import "Logging.h"
#import "ProductDetailView.h"
#import "TextDialog.h"
#import "Utility.h"


@implementation ProductDetailView

@synthesize _shoppingScreenDelegate;

- (id)initWithFrame:(CGRect)frame product:(Product *)product detailType:(int)detailType
{
    self = [super initWithFrame:frame];

    if(self)
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        _login = [_app getLogin];
        _product = product;
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWasShown:) name:UIKeyboardDidShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillBeHidden:) name:UIKeyboardWillHideNotification object:nil];

        // the modal 'screen'
        UIView *productModalView = [[UIView alloc] initWithFrame:frame]; // full screen background makes this view modal
        productModalView.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.75];
        [self addSubview:productModalView];

        // product detail frame
        int xPad = _app._viewWidth * .08;
        int yPad = _app._viewWidth * .01;
        CGRect productDetailFrame = CGRectMake(xPad, _app._headerHeight + _app._footerHeight + yPad, _app._viewWidth - (xPad * 2), _app._viewHeight - _app._headerHeight - (_app._footerHeight * 2) - (yPad * 2));
        int width = productDetailFrame.size.width;
        int height = productDetailFrame.size.height;

        // scroll view required for displaying instruction text at the bottom of this screen
        _scrollView = [[UIScrollView alloc] initWithFrame:productDetailFrame];
        _scrollView.backgroundColor = [UIColor colorWithRed:0.9 green:0.9 blue:.9 alpha:1.0];
        _scrollView.contentSize = CGSizeMake(productDetailFrame.size.width, productDetailFrame.size.height);
        [_scrollView setScrollEnabled:YES];
        _scrollView.bounces = NO;
        [productModalView addSubview:_scrollView];

        // frame background
        UIImageView *background = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, width, height)];
        background.image = [UIImage imageNamed:@"product_detail_background"];
        [_scrollView addSubview:background];

        // cancel button
        int cancelButtonSize;

        if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
            cancelButtonSize = frame.size.width * .12;
        else
            cancelButtonSize = frame.size.width * .08;

        UIButton *cancelButton = [[UIButton alloc] initWithFrame:CGRectMake(_scrollView.frame.origin.x + _scrollView.frame.size.width - cancelButtonSize - yPad, _scrollView.frame.origin.y + yPad, cancelButtonSize, cancelButtonSize)];
        [cancelButton setBackgroundImage:[UIImage imageNamed:@"button_close"] forState:UIControlStateNormal];
        [cancelButton addTarget:self action:@selector(cancel) forControlEvents:UIControlEventTouchDown];
        [productModalView addSubview:cancelButton];
        
        // product image
        int productImageSize = height * .20;
        _productImageView = [[UIImageView alloc] initWithFrame:CGRectMake((width - productImageSize) / 2, height * .02, productImageSize, productImageSize)];
        UIImage *image = [_app getCachedImage:[_product.imagePath lastPathComponent]];

        if(image == nil)
            image = [UIImage imageNamed:@"shopping_bag"];

        [_productImageView setImage:image];
        [_scrollView addSubview:_productImageView];

        // description text
        int textXOrigin = width * .02;
        int textWidth = width * .96;
        SmartTextView *descriptionLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .23, textWidth, height * .195) backGroundColor:[UIColor clearColor]];
        descriptionLabel.numberOfLines = 3;
        descriptionLabel.fontFamily = _app._normalFont;
        descriptionLabel.textAlignment = NSTextAlignmentCenter;
        descriptionLabel.textColor = [UIColor blackColor];

        if(![Utility isEmpty:_product.brand])
            descriptionLabel.text = [NSString stringWithFormat:@"%@ %@", _product.brand, _product.description];
        else
            descriptionLabel.text = _product.description;

        [_scrollView addSubview:descriptionLabel];

        // extended display(size) text
        int textHeight = height * .055;
        SmartTextView *extendedDisplayLabel = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .425, textWidth, textHeight) backGroundColor:[UIColor clearColor]];
        extendedDisplayLabel.numberOfLines = 1;
        extendedDisplayLabel.fontFamily = _app._normalFont;
        extendedDisplayLabel.textAlignment = NSTextAlignmentCenter;
        extendedDisplayLabel.textColor = [UIColor blackColor];
        extendedDisplayLabel.text = _product.extendedDisplay;
        [_scrollView addSubview:extendedDisplayLabel];

        // regular price text
        UILabel *regPriceText = [[UILabel alloc] initWithFrame:CGRectMake(0, height * .48, width * .5, textHeight)];
        regPriceText.numberOfLines = 1;
        regPriceText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        regPriceText.textAlignment = NSTextAlignmentRight;
        regPriceText.backgroundColor = [UIColor clearColor];
        regPriceText.textColor = [UIColor blackColor];
        regPriceText.text = @"Regular Price:";
        [_scrollView addSubview:regPriceText];

        UILabel *regPriceValue = [[UILabel alloc] initWithFrame:CGRectMake(width * .51, height * .48, width * .49, textHeight)];
        regPriceValue.numberOfLines = 1;
        regPriceValue.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        regPriceValue.textAlignment = NSTextAlignmentLeft;
        regPriceValue.backgroundColor = [UIColor clearColor];
        regPriceValue.textColor = [UIColor colorWithRed:.2 green:.55 blue:.26 alpha:1.0];
        regPriceValue.text = [NSString stringWithFormat:@"$%.2f", _product.regPrice];
        [_scrollView addSubview:regPriceValue];


        // promo price text and image
        if(_product.promoType > 0)
        {
            int promoPriceYOrigin = height * .535;
            int promoImageSize = height * .10;
            int promoImagePad = width * .025;
            UIImageView *promoImageView = [[UIImageView alloc] initWithFrame:CGRectMake(promoImagePad, promoImagePad, promoImageSize, promoImageSize)];
            [promoImageView setImage:[UIImage imageNamed:@"sale_tag"]];
            [_scrollView addSubview:promoImageView];

            UILabel *promoPriceText = [[UILabel alloc] initWithFrame:CGRectMake(0, promoPriceYOrigin, width * .5, textHeight)];
            promoPriceText.numberOfLines = 1;
            promoPriceText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
            promoPriceText.textAlignment = NSTextAlignmentRight;
            promoPriceText.backgroundColor = [UIColor clearColor];
            promoPriceText.textColor = [UIColor blackColor];
            promoPriceText.text = @"Promo Price:";
            [_scrollView addSubview:promoPriceText];

            UILabel *promoPriceValue = [[UILabel alloc] initWithFrame:CGRectMake(width * .51, promoPriceYOrigin, width * .49, textHeight)];
            promoPriceValue.numberOfLines = 1;
            promoPriceValue.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
            promoPriceValue.textAlignment = NSTextAlignmentLeft;
            promoPriceValue.backgroundColor = [UIColor clearColor];
            promoPriceValue.textColor = [UIColor colorWithRed:1.0 green:0.0 blue:0.0 alpha:1.0];
            [_scrollView addSubview:promoPriceValue];

            if(_product.promoForQty > 1)
                promoPriceValue.text = [NSString stringWithFormat:@"%d for $%.2f", _product.promoForQty, _product.promoPrice];
            else
                promoPriceValue.text = [NSString stringWithFormat:@"$%.2f", _product.promoPrice];

            [_scrollView addSubview:promoPriceValue];

            SmartTextView *promoDescription = [[SmartTextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .59, textWidth, textHeight) backGroundColor:[UIColor clearColor]];
            promoDescription.numberOfLines = 1;
            promoDescription.fontFamily = _app._normalFont;
            promoDescription.textAlignment = NSTextAlignmentCenter;
            promoDescription.textColor = [UIColor blackColor];
            promoDescription.text = [NSString stringWithFormat:@"%@", _product.promoPriceText];
            [_scrollView addSubview:promoDescription];
        }

        // quantity/weight checkbox stuff
        int buttonPad = (int)(width * .025);

        if(_product.approxAvgWgt > 0)
        {
            int checkBoxYOrigin = height * .665;
            int checkBoxTextYOrigin = height * .67;
            int checkBoxHeight;
            int checkBoxTextHeight;
            int buttonPad = width * .01;

            if(_app._deviceType == IPHONE_5)
            {
                checkBoxHeight = height * .045;
                checkBoxTextHeight = height * .035;
            }
            else
            {
                checkBoxHeight = height * .05;
                checkBoxTextHeight = height * .04;
            }

            _quantityCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(width * .05, checkBoxYOrigin, checkBoxHeight, checkBoxHeight)];
            [_quantityCheckBox setBackgroundImage:[UIImage imageNamed:@"product_checked_box"] forState:UIControlStateSelected];
            [_quantityCheckBox setBackgroundImage:[UIImage imageNamed:@"product_unchecked_box"] forState:UIControlStateNormal];
            [_quantityCheckBox addTarget:self action:@selector(setPricingByQuantity) forControlEvents:UIControlEventTouchDown];
            [_scrollView addSubview:_quantityCheckBox];

            UILabel *quantityText = [[UILabel alloc] initWithFrame:CGRectMake(_quantityCheckBox.frame.origin.x + _quantityCheckBox.frame.size.width + buttonPad, checkBoxTextYOrigin, width * .2, checkBoxTextHeight)];
            quantityText.numberOfLines = 1;
            quantityText.font = [Utility fontForFamily:_app._normalFont andHeight:checkBoxTextHeight];
            quantityText.textAlignment = NSTextAlignmentLeft;
            quantityText.backgroundColor = [UIColor clearColor];
            quantityText.textColor = [UIColor blackColor];
            quantityText.text = @"by quantity";
            [_scrollView addSubview:quantityText];

            _weightCheckBox = [[UIButton alloc] initWithFrame:CGRectMake(width * .67, checkBoxYOrigin, checkBoxHeight, checkBoxHeight)];
            [_weightCheckBox setBackgroundImage:[UIImage imageNamed:@"product_checked_box"] forState:UIControlStateSelected];
            [_weightCheckBox setBackgroundImage:[UIImage imageNamed:@"product_unchecked_box"] forState:UIControlStateNormal];
            [_weightCheckBox addTarget:self action:@selector(setPricingByWeight) forControlEvents:UIControlEventTouchDown];
            [_scrollView addSubview:_weightCheckBox];

            UILabel *weightText = [[UILabel alloc] initWithFrame:CGRectMake(_weightCheckBox.frame.origin.x + _weightCheckBox.frame.size.width + buttonPad, checkBoxTextYOrigin, width * .2, checkBoxTextHeight)];
            weightText.numberOfLines = 1;
            weightText.font = [Utility fontForFamily:_app._normalFont andHeight:checkBoxTextHeight];
            weightText.textAlignment = NSTextAlignmentLeft;
            weightText.backgroundColor = [UIColor clearColor];
            weightText.textColor = [UIColor blackColor];
            weightText.text = @"by weight";
            [_scrollView addSubview:weightText];
        }

        // quantity bar stuff
        int quantityWidth = width * .7;
        int quantityXOrigin = (width - quantityWidth) / 2;
        int quantityHeight = height * .075;
        int quantityYOrigin = height * .73;

        UIImageView *quantityBackground = [[UIImageView alloc] initWithFrame:CGRectMake(quantityXOrigin, quantityYOrigin, quantityWidth, quantityHeight)];
        UIImage *backgroundImage = [UIImage imageNamed:@"product_quantity_background"];
        [quantityBackground setImage:backgroundImage];
        [_scrollView addSubview:quantityBackground];

        UIButton *plusButton = [[UIButton alloc] initWithFrame:CGRectMake(quantityXOrigin, quantityYOrigin, quantityHeight, quantityHeight)];
        [plusButton setBackgroundImage:[UIImage imageNamed:@"transparent"] forState:UIControlStateNormal];
        [plusButton addTarget:self action:@selector(addToItemCount) forControlEvents:UIControlEventTouchDown];
        [_scrollView addSubview:plusButton];

        UIButton *minusButton = [[UIButton alloc] initWithFrame:CGRectMake(quantityXOrigin + quantityWidth - quantityHeight, quantityYOrigin, quantityHeight, quantityHeight)];
        [minusButton setBackgroundImage:[UIImage imageNamed:@"transparent"] forState:UIControlStateNormal];
        [minusButton addTarget:self action:@selector(subtractFromItemCount) forControlEvents:UIControlEventTouchDown];
        [_scrollView addSubview:minusButton];

        _quantityText = [[UILabel alloc] initWithFrame:CGRectMake(0, quantityYOrigin + (quantityHeight * .1), width * .5, quantityHeight * .8)];
        _quantityText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        _quantityText.textAlignment = NSTextAlignmentRight;
        _quantityText.numberOfLines = 1;
        _quantityText.backgroundColor = [UIColor clearColor];
        _quantityText.textColor = [UIColor blackColor];
        [_scrollView addSubview:_quantityText];

        if(product.weight > 0)
            _productQuantity = (int)_product.weight;
        else
            _productQuantity = _product.qty == 0 ? 1 : _product.qty; // for products retrieved in product search the qty is 0 so set it to 1 here
        
        _quantityValue = [[UILabel alloc] initWithFrame:CGRectMake(width * .50, quantityYOrigin + (quantityHeight * .1), width * .49, quantityHeight * .8)];
        _quantityValue.font = [Utility fontForFamily:_app._boldFont andHeight:textHeight];
        _quantityValue.textAlignment = NSTextAlignmentLeft;
        _quantityValue.numberOfLines = 1;
        _quantityValue.backgroundColor = [UIColor clearColor];
        _quantityValue.textColor = [UIColor blackColor];
        _quantityValue.text = [NSString stringWithFormat:@": %d", _productQuantity];
        [_scrollView addSubview:_quantityValue];

        // total
        int totalPriceHeight = height * .07;
        _totalPriceValue = [[UILabel alloc] initWithFrame:CGRectMake(width * .35, height * .65, width * .30, totalPriceHeight)];
        _totalPriceValue.font = [Utility fontForFamily:_app._boldFont andHeight:totalPriceHeight];
        _totalPriceValue.textAlignment = NSTextAlignmentCenter;
        _totalPriceValue.numberOfLines = 1;
        _totalPriceValue.backgroundColor = [UIColor clearColor];
        _totalPriceValue.textColor = [UIColor blackColor];
        [_scrollView addSubview:_totalPriceValue];

        if(_product.weight > 0)
            [self setPricingByWeight];
        else
            [self setPricingByQuantity];

        // instructions text
        CGRect instructionsFrame = CGRectMake(textXOrigin, height * .83, textWidth, height * .06);
        UIImageView *instructionsBackground = [[UIImageView alloc] initWithFrame:instructionsFrame];
        instructionsBackground.image = [UIImage imageNamed:@"product_comment_box"];
        [_scrollView addSubview:instructionsBackground];

        CGRect instructionsTextFrame = CGRectMake(textXOrigin, height * .84, textWidth, height * .05);
        _instructionsTextField = [[OffsetTextField alloc] initWithFrame:instructionsTextFrame offset:instructionsTextFrame.size.width * .02];
        _instructionsTextField.delegate = self;
        _instructionsTextField.keyboardType = UIKeyboardTypeAlphabet;
        _instructionsTextField.returnKeyType = UIReturnKeyDone;
        _instructionsTextField.backgroundColor = [UIColor clearColor];
        _instructionsTextField.textColor = [UIColor grayColor];
        _instructionsTextField.font = [Utility fontForFamily:_app._normalFont andHeight:(instructionsTextFrame.size.height * .8)];
        _instructionsTextField.autocapitalizationType = UITextAutocapitalizationTypeSentences;

        if(![Utility isEmpty:_product.customerComment])
            _instructionsTextField.text = _product.customerComment;
        else
            _instructionsTextField.text = @"Personal Shopper instructions...";

        [_scrollView addSubview:_instructionsTextField];

        int buttonWidth = width * .475;
        int buttonHeight = height * .08;
        int buttonYOrigin = height * .91;
        buttonPad = width * .01;

        if(detailType == PRODUCT_ADD)
        {
            NSString *addText = @"Add to List";
            UIButton *addButton = [[UIButton alloc] initWithFrame:CGRectMake((width - buttonWidth) / 2, buttonYOrigin, buttonWidth, buttonHeight)];
            [addButton setBackgroundImage:[UIImage imageNamed:@"button_red_plain"] forState:UIControlStateNormal];
            [addButton addTarget:self action:@selector(addItem) forControlEvents:UIControlEventTouchDown];
            addButton.titleLabel.font = [Utility fontForSize:_app._normalFont forString:addText forSize:CGSizeMake(buttonWidth * .9, buttonHeight * .8)];
            [addButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            [addButton setTitle:addText forState:UIControlStateNormal];
            [_scrollView addSubview:addButton];
        }
        else if(detailType == PRODUCT_MODIFY)
        {
            NSString *addText = @"Change Quantity1";
            UIButton *addButton = [[UIButton alloc] initWithFrame:CGRectMake((width * .5) - buttonWidth - buttonPad, buttonYOrigin, buttonWidth, buttonHeight)];
            [addButton setBackgroundImage:[UIImage imageNamed:@"button_red_plain"] forState:UIControlStateNormal];
            [addButton addTarget:self action:@selector(modifyItem) forControlEvents:UIControlEventTouchDown];
            addButton.titleLabel.font = [Utility fontForSize:_app._normalFont forString:addText forSize:CGSizeMake(buttonWidth * .9, buttonHeight * .8)];
            [addButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            [addButton setTitle:addText forState:UIControlStateNormal];
            [_scrollView addSubview:addButton];

            NSString *deleteText = @"Delete";
            UIButton *cancelButton = [[UIButton alloc] initWithFrame:CGRectMake((width * .5) + buttonPad, buttonYOrigin, buttonWidth, buttonHeight)];
            [cancelButton setBackgroundImage:[UIImage imageNamed:@"button_red_plain"] forState:UIControlStateNormal];
            [cancelButton addTarget:self action:@selector(deleteItem) forControlEvents:UIControlEventTouchDown];
            cancelButton.titleLabel.font = [Utility fontForSize:_app._normalFont forString:addText forSize:CGSizeMake(buttonWidth * .9, buttonHeight * .8)];
            [cancelButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            [cancelButton setTitle:deleteText forState:UIControlStateNormal];
            [_scrollView addSubview:cancelButton];
        }
    }

    LogInfo(@"%@", [NSString stringWithFormat:@"SATAN: ProductDetailView: weight=%f, qty=%d, approxAvgWgt=%f", product.weight, product.qty, product.approxAvgWgt]);
    return self;
}


- (void)show
{
    [_app._currentView addSubview:self];
    self.alpha=0.0;
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:1];
    self.alpha=1.0;
    [UIView commitAnimations];
}


- (void)cancel
{
    [self removeFromSuperview];
}


- (void)setPricingByQuantity
{
    if(_product.approxAvgWgt > 0)
    {
        if(_quantityCheckBox.selected == YES)
            return;
        
        _quantityCheckBox.selected = YES;
        _weightCheckBox.selected = NO;
    }

    _quantityText.text = @"Qty";
    [self setTotalPriceText];
}


- (void)setPricingByWeight
{
    if(_product.approxAvgWgt > 0)
    {
        if(_weightCheckBox != nil && _weightCheckBox.selected == YES)
            return;

        _weightCheckBox.selected = YES;
        _quantityCheckBox.selected = NO;
    }

    _quantityText.text = [NSString stringWithFormat:@"%@s", _product.unitOfMeasure];
    [self setTotalPriceText];
}


- (void)setTotalPriceText
{
    float unitPrice;

    if(_product.promoType > 0)
        unitPrice = _product.promoPrice / _product.promoForQty;
    else
        unitPrice = _product.regPrice;

    if(_quantityCheckBox != nil && _quantityCheckBox.selected == YES)
        unitPrice *= _product.approxAvgWgt;

    _totalPriceValue.text = [NSString stringWithFormat:@"$%.2f", _productQuantity * unitPrice];
}


- (void)addToItemCount
{
    _quantityValue.text = [NSString stringWithFormat:@": %d", ++_productQuantity];
    [self setTotalPriceText];
}


- (void)subtractFromItemCount
{
    if(_productQuantity == 1)
        return;

    _quantityValue.text = [NSString stringWithFormat:@": %d", --_productQuantity];
    [self setTotalPriceText];
}


- (void)addItem
{
    _app._productToAdd = _product;
    _app._productToModify = nil;
    _app._productToDelete = nil;
    [self updateItem];
}


- (void)modifyItem
{
    _app._productToAdd = nil;
    _app._productToModify = _product;
    _app._productToDelete = nil;
    [self updateItem];
}


- (void)deleteItem
{
    _app._productToAdd = nil;
    _app._productToModify = nil;
    _app._productToDelete = _product;

    ListDeleteItemRequest *request = [[ListDeleteItemRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = _app._currentShoppingList.listId;
    request.sku = _product.sku;
    request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;

    if([_shoppingScreenDelegate shoppingListVisible] == YES)
    {
        request.returnCurrentList = [NSNumber numberWithBool:YES];
        LogInfo(@"SHOPPING_LIST: DELETE: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [_app._currentShoppingList.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", _app._currentShoppingList.totalPrice]);
    }
    else
    {
        request.returnCurrentList = [NSNumber numberWithBool:NO];
        LogInfo(@"SHOPPING_LIST: DELETE: setting returnCurrentList = NO, appListUpdateTime = %llu, totalPrice = %@", [_app._currentShoppingList.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", _app._currentShoppingList.totalPrice]);
    }

    _progressDialog = [[ProgressDialog alloc] initWithView:_app._currentView message:@"Updating your list..."];
    [_progressDialog show];

    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
    [_service execute:LIST_DELETE_ITEM_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
}


- (void)updateItem
{
    if([Utility isEmpty:_app._currentShoppingList])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:self title:@"Show Current List" message:@"No active list to add into. Goto lists and make one active."];
        [dialog show];
        return;
    }

    if(_weightCheckBox != nil && _weightCheckBox.selected == YES)
    {
        _product.weight = (float)_productQuantity;
        _product.qty = 0;
    }
    else
    {
        _product.qty = _productQuantity;
        _product.weight = 0.0f;
    }

    ListAddItemRequest *request = [[ListAddItemRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = _app._currentShoppingList.listId;
    request.sku = _product.sku;
    request.customerComment = _instructionsTextField.text;

    request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime.stringValue;

    if(_product.approxAvgWgt > 0)
    {
        if(_weightCheckBox.selected == YES)
        {
            request.purchaseBy = @"W";
            request.weight = (float)_productQuantity;
        }
        else
        {
            request.purchaseBy = @"E";
            request.qty = _productQuantity;
        }
    }
    else
    {
        request.purchaseBy = @"E";
        request.qty = _productQuantity;
    }

    if([_shoppingScreenDelegate shoppingListVisible] == YES)
    {
        LogInfo(@"SHOPPING_LIST: ADD: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [_app._currentShoppingList.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", _app._currentShoppingList.totalPrice]);
        request.returnCurrentList = [NSNumber numberWithBool:YES].stringValue;
    }
    else
    {
        LogInfo(@"SHOPPING_LIST: ADD: setting returnCurrentList = NO, appListUpdateTime = %llu, totalPrice = %@", [_app._currentShoppingList.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", _app._currentShoppingList.totalPrice]);
        request.returnCurrentList = [NSNumber numberWithBool:NO].stringValue;
    }

    _progressDialog = [[ProgressDialog alloc] initWithView:self message:@"Updating your list..."];
    [_progressDialog show];

    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
    [_service execute:LIST_ADD_ITEM_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
}


- (void)handleListServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];

    if(status == 200) // service success
    {
//        [_shoppingScreenDelegate handleListServiceResponse:responseObject httpStatusCode:status _isCurrentList:];
        [self removeFromSuperview];
    }
    else // service failure
    {
        if(status == 422) // backend error
        {
            WebServiceError *error = [_service getError];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self title:@"Update List Failed" message:error.errorMessage];
            [dialog show];
        }
        else if(status == 408) // timeout error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:self title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
            [dialog show];
        }
        else // http error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:self title:@"Server Error" message:COMMON_ERROR_MSG];
            [dialog show];
        }
    }

    _service = nil;
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
    _instructionsTextField.text = @"";
    return YES;
}


- (BOOL)textFieldShouldReturn:(UITextView *)textView
{
    _product.customerComment = _instructionsTextField.text;
    [_instructionsTextField resignFirstResponder];
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


@end
