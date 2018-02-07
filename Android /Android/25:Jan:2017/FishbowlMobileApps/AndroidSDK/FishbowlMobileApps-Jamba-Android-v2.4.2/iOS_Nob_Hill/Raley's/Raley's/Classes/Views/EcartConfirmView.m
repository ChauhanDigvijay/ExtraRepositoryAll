//
//  EcartConfirmView.m
//  Raley's
//
//  Created by Bill Lewis on 3/19/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "SmartTextView.h"
#import "ListAddItemRequest.h"
#import "ListDeleteItemRequest.h"
#import "ShoppingList.h"
#import "Logging.h"
#import "EcartConfirmView.h"
#import "TextDialog.h"
#import "Utility.h"

@implementation EcartConfirmView

@synthesize _ecartScreenDelegate;

- (id)initWithFrame:(CGRect)frame view:(UIView *)parentView preOrderResponse:(EcartPreOrderResponse *)preOrderResponse orderRequest:(EcartOrderRequest *)orderRequest
{
    self = [super initWithFrame:frame];

    if(self)
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        _parentView = parentView;
        _preOrderResponse = preOrderResponse;
        _orderRequest = orderRequest;
        _login = [_app getLogin];
//        _textColor = [UIColor colorWithRed:0.35 green:0.23 blue:.08 alpha:1.0];
        _textColor=[UIColor darkGrayColor];
        _headingColor=[UIColor colorWithRed:(187.0/255.0) green:(32.0/255.0) blue:(1.0/255.0) alpha:1.0];

        UIView *ecartModalView = [[UIView alloc] initWithFrame:frame]; // full screen background makes this view modal
      //  ecartModalView.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.75];
          ecartModalView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3];
        [self addSubview:ecartModalView];

        int detailFramePad = _app._viewWidth * .08;
        CGRect ecartDetailFrame;
        ecartDetailFrame = CGRectMake(detailFramePad, _app._headerHeight + (detailFramePad/2), _app._viewWidth - (detailFramePad * 2), _app._viewHeight - _app._headerHeight - _app._footerHeight - ((detailFramePad/2) * 2));

        UIView *ecartDetailView = [[UIView alloc] initWithFrame:ecartDetailFrame];
        [ecartModalView addSubview:ecartDetailView];

        int width = ecartDetailFrame.size.width;
        int height = ecartDetailFrame.size.height;
        int textWidth = width * .9;
        int textXOrigin = (width - textWidth) / 2;
        int splitTextWidth = textWidth / 2;
        int splitTextRightXOrigin = width * .51;
        int headingHeight = height * .045;
        int textHeight = height * .04;

        UIImageView *background = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, width, height)];
        background.image =nil;
        background.backgroundColor = [UIColor whiteColor];
        [background.layer setCornerRadius:2.5f];
        [ecartDetailView addSubview:background];

        UILabel *headerText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .01, textWidth, height * .06)];
//        headerText.font = [Utility fontForFamily:_app._normalFont andHeight:height * .07];
        headerText.font=[UIFont fontWithName:_app._normalFont size:font_size17];
                         
        headerText.textAlignment = NSTextAlignmentCenter;
        headerText.numberOfLines = 1;
        headerText.backgroundColor = [UIColor clearColor];
        headerText.textColor = _headingColor;
        headerText.text = @"Your Pickup Information:";
        [ecartDetailView addSubview:headerText];

        NSString *storeName;
        NSString *storeAddress;
        NSString *storeCityStateZip;
        NSString *storePhone;

        if(![Utility isEmpty:_app._allStoresList])
        {
            Store *store = [_app._allStoresList objectForKey:[NSString stringWithFormat:@"%d", _login.storeNumber]];
            storeName = store.chain;
            storeAddress = store.address;
            storeCityStateZip = [NSString stringWithFormat:@"%@, %@, %@", store.city, store.state, store.zip];
            storePhone = store.groceryPhoneNo;
        }
        else // this shouldn't happen, but just in case
        {
            storeName = [NSString stringWithFormat:@"%d", _login.storeNumber];
            storeAddress = @"";
            storeCityStateZip = @"";
            storePhone = @"";
        }
        
        UILabel *orderingAtText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .07, textWidth, headingHeight)];
//        orderingAtText.font = [Utility fontForFamily:_app._boldFont andHeight:headingHeight];
        orderingAtText.font=[UIFont fontWithName:_app._boldFont size:font_size13];
        orderingAtText.textAlignment = NSTextAlignmentCenter;
        orderingAtText.numberOfLines = 1;
        orderingAtText.backgroundColor = [UIColor clearColor];
        orderingAtText.textColor = _textColor;
        orderingAtText.text = [NSString stringWithFormat:@"You are ordering at: %@", storeName];
        [ecartDetailView addSubview:orderingAtText];

        UILabel *addressText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .115, textWidth, textHeight)];
//        addressText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        addressText.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        addressText.textAlignment = NSTextAlignmentCenter;
        addressText.numberOfLines = 1;
        addressText.backgroundColor = [UIColor clearColor];
        addressText.textColor = _textColor;
        addressText.text = storeAddress;
        [ecartDetailView addSubview:addressText];

        UILabel *cityStateZipText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .155, textWidth, textHeight)];
//        cityStateZipText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        cityStateZipText.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        cityStateZipText.textAlignment = NSTextAlignmentCenter;
        cityStateZipText.numberOfLines = 1;
        cityStateZipText.backgroundColor = [UIColor clearColor];
        cityStateZipText.textColor = _textColor;
        cityStateZipText.text = storeCityStateZip;
        [ecartDetailView addSubview:cityStateZipText];

        UILabel *phoneText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .195, textWidth, textHeight)];
//        phoneText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        phoneText.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        phoneText.textAlignment = NSTextAlignmentCenter;
        phoneText.numberOfLines = 1;
        phoneText.backgroundColor = [UIColor clearColor];
        phoneText.textColor = _textColor;
        phoneText.text = storePhone;
        [ecartDetailView addSubview:phoneText];

        UILabel *substitutionHeaderText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .245, textWidth, headingHeight)];
//        substitutionHeaderText.font = [Utility fontForFamily:_app._boldFont andHeight:headingHeight];
        substitutionHeaderText.font=[UIFont fontWithName:_app._boldFont size:font_size13];
        substitutionHeaderText.textAlignment = NSTextAlignmentCenter;
        substitutionHeaderText.numberOfLines = 1;
        substitutionHeaderText.backgroundColor = [UIColor clearColor];
        substitutionHeaderText.textColor = _textColor;
        substitutionHeaderText.text = @"Substitutions:";
        [ecartDetailView addSubview:substitutionHeaderText];

        UILabel *substitutionText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .29, textWidth, textHeight)];
//        substitutionText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        substitutionText.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        substitutionText.textAlignment = NSTextAlignmentCenter;
        substitutionText.numberOfLines = 1;
        substitutionText.backgroundColor = [UIColor clearColor];
        substitutionText.textColor = _textColor;
        substitutionText.text = _orderRequest.substitutionPreferenceName;
        [ecartDetailView addSubview:substitutionText];

        UILabel *bagPreferenceHeaderText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .34, textWidth, headingHeight)];
//        bagPreferenceHeaderText.font = [Utility fontForFamily:_app._boldFont andHeight:headingHeight];
        bagPreferenceHeaderText.font=[UIFont fontWithName:_app._boldFont size:font_size13];
        bagPreferenceHeaderText.textAlignment = NSTextAlignmentCenter;
        bagPreferenceHeaderText.numberOfLines = 1;
        bagPreferenceHeaderText.backgroundColor = [UIColor clearColor];
        bagPreferenceHeaderText.textColor = _textColor;
        bagPreferenceHeaderText.text = @"Bag Preference:";
        [ecartDetailView addSubview:bagPreferenceHeaderText];

        UILabel *bagPreferenceText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .385, textWidth, textHeight)];
//        bagPreferenceText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        bagPreferenceText.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        bagPreferenceText.textAlignment = NSTextAlignmentCenter;
        bagPreferenceText.numberOfLines = 1;
        bagPreferenceText.backgroundColor = [UIColor clearColor];
        bagPreferenceText.textColor = _textColor;
        bagPreferenceText.text = _orderRequest.bagPreferenceName;
        [ecartDetailView addSubview:bagPreferenceText];
        
        UILabel *pickupHeaderText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .435, textWidth, headingHeight)];
//        pickupHeaderText.font = [Utility fontForFamily:_app._boldFont andHeight:headingHeight];
        pickupHeaderText.font=[UIFont fontWithName:_app._boldFont size:font_size13];
        pickupHeaderText.textAlignment = NSTextAlignmentCenter;
        pickupHeaderText.numberOfLines = 1;
        pickupHeaderText.backgroundColor = [UIColor clearColor];
        pickupHeaderText.textColor = _textColor;
        pickupHeaderText.text = @"Pickup Date and Time:";
        [ecartDetailView addSubview:pickupHeaderText];

        UILabel *pickupText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .48, textWidth, textHeight)];
//        pickupText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        pickupText.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        pickupText.textAlignment = NSTextAlignmentCenter;
        pickupText.numberOfLines = 1;
        pickupText.backgroundColor = [UIColor clearColor];
        pickupText.textColor = _textColor;
        pickupText.text = [NSString stringWithFormat:@"%@  %@", _orderRequest.appointmentDay, _orderRequest.appointmentTime];
        [ecartDetailView addSubview:pickupText];
        
        UILabel *instructionHeaderText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .53, textWidth, headingHeight)];
        instructionHeaderText.font = [Utility fontForFamily:_app._boldFont andHeight:headingHeight];
        instructionHeaderText.font=[UIFont fontWithName:_app._boldFont size:font_size13];
        instructionHeaderText.textAlignment = NSTextAlignmentCenter;
        instructionHeaderText.numberOfLines = 1;
        instructionHeaderText.backgroundColor = [UIColor clearColor];
        instructionHeaderText.textColor = _textColor;
        instructionHeaderText.text = @"Special Instructions:";
        [ecartDetailView addSubview:instructionHeaderText];

        UITextView *instructionText = [[UITextView alloc] initWithFrame:CGRectMake(textXOrigin, height * .575, textWidth, textHeight * 2)];
        instructionText.editable = NO;
        instructionText.backgroundColor = [UIColor clearColor];
        instructionText.textColor = _textColor;
//        instructionText.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        instructionText.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        instructionText.text = _orderRequest.instructions;
        [ecartDetailView addSubview:instructionText];

        UILabel *orderTotalText = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .665, textWidth, headingHeight)];
//        orderTotalText.font = [Utility fontForFamily:_app._boldFont andHeight:headingHeight];
        orderTotalText.font=[UIFont fontWithName:_app._boldFont size:font_size13];
        orderTotalText.textAlignment = NSTextAlignmentCenter;
        orderTotalText.numberOfLines = 1;
        orderTotalText.backgroundColor = [UIColor clearColor];
        orderTotalText.textColor = _headingColor;
        orderTotalText.text = @"Your Order Totals:";
        [ecartDetailView addSubview:orderTotalText];

        int sePointsYOrigin = height * .71;
        UILabel *sePointsPrompt = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, sePointsYOrigin, splitTextWidth, textHeight)];
//        sePointsPrompt.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        sePointsPrompt.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        sePointsPrompt.textAlignment = NSTextAlignmentRight;
        sePointsPrompt.numberOfLines = 1;
        sePointsPrompt.backgroundColor = [UIColor clearColor];
        sePointsPrompt.textColor = _textColor;
        sePointsPrompt.text = @"Estimated Points:";
        [ecartDetailView addSubview:sePointsPrompt];

        UILabel *sePointsText = [[UILabel alloc] initWithFrame:CGRectMake(splitTextRightXOrigin, sePointsYOrigin, splitTextWidth, textHeight)];
        sePointsText.font = [Utility fontForFamily:@"Courier-Bold" andHeight:textHeight];
        sePointsText.font=[UIFont fontWithName:@"Courier-Bold" size:font_size13];
        sePointsText.textAlignment = NSTextAlignmentLeft;
        sePointsText.numberOfLines = 1;
        sePointsText.backgroundColor = [UIColor clearColor];
        sePointsText.textColor = _textColor;
        sePointsText.text = [NSString stringWithFormat:@"%d", _app._currentEcartPreOrderResponse.sePoints];
        [ecartDetailView addSubview:sePointsText];

        int subtotalYOrigin = height * .75;
        UILabel *subtotalPrompt = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, subtotalYOrigin, splitTextWidth, textHeight)];
//        subtotalPrompt.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        subtotalPrompt.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        subtotalPrompt.textAlignment = NSTextAlignmentRight;
        subtotalPrompt.numberOfLines = 1;
        subtotalPrompt.backgroundColor = [UIColor clearColor];
        subtotalPrompt.textColor = _textColor;
        subtotalPrompt.text = @"Subtotal $:";
        [ecartDetailView addSubview:subtotalPrompt];

        UILabel *subtotalText = [[UILabel alloc] initWithFrame:CGRectMake(splitTextRightXOrigin, subtotalYOrigin, splitTextWidth, textHeight)];
//        subtotalText.font = [Utility fontForFamily:@"Courier-Bold" andHeight:textHeight];
        subtotalText.font=[UIFont fontWithName:@"Courier-Bold" size:font_size13];
        subtotalText.textAlignment = NSTextAlignmentLeft;
        subtotalText.numberOfLines = 1;
        subtotalText.backgroundColor = [UIColor clearColor];
        subtotalText.textColor = _textColor;
        subtotalText.text = [NSString stringWithFormat:@"%7.2f", _app._currentEcartPreOrderResponse.productPrice];
        [ecartDetailView addSubview:subtotalText];
        
        int taxYOrigin = height * .79;
        UILabel *taxPrompt = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, taxYOrigin, splitTextWidth, textHeight)];
//        taxPrompt.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        taxPrompt.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        taxPrompt.textAlignment = NSTextAlignmentRight;
        taxPrompt.numberOfLines = 1;
        taxPrompt.backgroundColor = [UIColor clearColor];
        taxPrompt.textColor = _textColor;
        taxPrompt.text = @"Tax + CRV $:";
        [ecartDetailView addSubview:taxPrompt];

        UILabel *taxText = [[UILabel alloc] initWithFrame:CGRectMake(splitTextRightXOrigin, taxYOrigin, splitTextWidth, textHeight)];
//        taxText.font = [Utility fontForFamily:@"Courier-Bold" andHeight:textHeight];
        taxText.font=[UIFont fontWithName:@"Courier-Bold" size:font_size13];
        taxText.textAlignment = NSTextAlignmentLeft;
        taxText.numberOfLines = 1;
        taxText.backgroundColor = [UIColor clearColor];
        taxText.textColor = _textColor;
        taxText.text = [NSString stringWithFormat:@"%7.2f", _app._currentEcartPreOrderResponse.salesTax + _app._currentEcartPreOrderResponse.crv];
        [ecartDetailView addSubview:taxText];

        int serviceFeeYOrigin = height * .83;
        UILabel *serviceFeePrompt = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, serviceFeeYOrigin, splitTextWidth, textHeight)];
//        serviceFeePrompt.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        serviceFeePrompt.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        serviceFeePrompt.textAlignment = NSTextAlignmentRight;
        serviceFeePrompt.numberOfLines = 1;
        serviceFeePrompt.backgroundColor = [UIColor clearColor];
        serviceFeePrompt.textColor = _textColor;
        serviceFeePrompt.text = @"Service Fee $:";
        [ecartDetailView addSubview:serviceFeePrompt];

        UILabel *serviceFeeText = [[UILabel alloc] initWithFrame:CGRectMake(splitTextRightXOrigin, serviceFeeYOrigin, splitTextWidth, textHeight)];
//        serviceFeeText.font = [Utility fontForFamily:@"Courier-Bold" andHeight:textHeight];
        serviceFeeText.font=[UIFont fontWithName:@"Courier-Bold" size:font_size13];
        serviceFeeText.textAlignment = NSTextAlignmentLeft;
        serviceFeeText.numberOfLines = 1;
        serviceFeeText.backgroundColor = [UIColor clearColor];
        serviceFeeText.textColor = _textColor;
        serviceFeeText.text = [NSString stringWithFormat:@"%7.2f", _app._currentEcartPreOrderResponse.fees];
        [ecartDetailView addSubview:serviceFeeText];
        
        int totalPriceYOrigin = height * .87;
        UILabel *totalPricePrompt = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, totalPriceYOrigin, splitTextWidth, textHeight)];
//        totalPricePrompt.font = [Utility fontForFamily:_app._normalFont andHeight:textHeight];
        totalPricePrompt.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        totalPricePrompt.textAlignment = NSTextAlignmentRight;
        totalPricePrompt.numberOfLines = 1;
        totalPricePrompt.backgroundColor = [UIColor clearColor];
        totalPricePrompt.textColor = _textColor;
        totalPricePrompt.text = @"Estimated Total $:";
        [ecartDetailView addSubview:totalPricePrompt];

        UILabel *totalPriceText = [[UILabel alloc] initWithFrame:CGRectMake(splitTextRightXOrigin, totalPriceYOrigin, splitTextWidth, textHeight)];
//        totalPriceText.font = [Utility fontForFamily:@"Courier-Bold" andHeight:textHeight];
        totalPriceText.font=[UIFont fontWithName:@"Courier-Bold" size:font_size13];
        totalPriceText.textAlignment = NSTextAlignmentLeft;
        totalPriceText.numberOfLines = 1;
        totalPriceText.backgroundColor = [UIColor clearColor];
        totalPriceText.textColor = _textColor;
        totalPriceText.text = [NSString stringWithFormat:@"%7.2f", _app._currentEcartPreOrderResponse.totalPrice + _app._currentEcartPreOrderResponse.crv];
        [ecartDetailView addSubview:totalPriceText];

        int buttonWidth = width * .45;
        int buttonHeight = height * .07;
        int buttonYOrigin = height * .92;
        int buttonPad = width * .02;

        NSString *addText = @"Submit Order";
        UIButton *addButton = [[UIButton alloc] initWithFrame:CGRectMake((width * .5) - buttonWidth - buttonPad, buttonYOrigin, buttonWidth, buttonHeight)];
        [addButton setBackgroundImage:[UIImage imageNamed:@"button_red_plain"] forState:UIControlStateNormal];
        [addButton addTarget:self action:@selector(placeEcartOrder) forControlEvents:UIControlEventTouchDown];
       // addButton.titleLabel.font = [Utility fontForSize:_app._normalFont forString:addText forSize:CGSizeMake(buttonWidth * .9, buttonHeight * .7)];
        addButton.titleLabel.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        [addButton.layer setCornerRadius:5.0f];
        addButton.clipsToBounds=YES;
        [addButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [addButton setTitle:addText forState:UIControlStateNormal];
        [ecartDetailView addSubview:addButton];

        NSString *deleteText = @"Cancel Order";
        UIButton *cancelButton = [[UIButton alloc] initWithFrame:CGRectMake((width * .5) + buttonPad, buttonYOrigin, buttonWidth, buttonHeight)];
        [cancelButton setBackgroundImage:[UIImage imageNamed:@"button_red_plain"] forState:UIControlStateNormal];
        [cancelButton addTarget:self action:@selector(cancelRequest) forControlEvents:UIControlEventTouchDown];
      //  cancelButton.titleLabel.font = [Utility fontForSize:_app._normalFont forString:addText forSize:CGSizeMake(buttonWidth * .9, buttonHeight * .7)];
        cancelButton.titleLabel.font=[UIFont fontWithName:_app._normalFont size:font_size13];
        [cancelButton.layer setCornerRadius:5.0f];
        cancelButton.clipsToBounds=YES;
        [cancelButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [cancelButton setTitle:deleteText forState:UIControlStateNormal];
        [ecartDetailView addSubview:cancelButton];
    }

    return self;
}


- (void)show
{
    [_parentView addSubview:self];
    self.alpha=0.0;
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:1];
    self.alpha=1.0;
    [UIView commitAnimations];
}


- (void)cancelRequest
{
    [_ecartScreenDelegate showSubmitButton];
    [self removeFromSuperview];
}


- (void)placeEcartOrder
{
    _progressDialog = [[ProgressDialog alloc] initWithView:self message:@"Submitting your Ecart Order..."];
    [_progressDialog show];

    _service = [[WebService alloc]initWithListener:self responseClassName:@"EcartOrderRequest"];
    [_service execute:ECART_ORDER_URL authorization:_login.authKey requestObject:_orderRequest requestType:POST]; // response handled by handleEcartOrderServiceResponse method below
    
#ifdef CLP_ANALYTICS
    if(_orderRequest){
        @try {                                                                //Product added to list
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"ListSubmitted" forKey:@"event_name"];
            [data setValue:_orderRequest.accountId forKey:@"Ecart_AccountID"];
            [data setValue:_orderRequest.listId forKey:@"Ecart_ListID"];
            [data setValue:_orderRequest.appointmentDay forKey:@"Ecart_AppointmentDay"];
            [data setValue:_orderRequest.appointmentTime forKey:@"Ecart_appointmentTime"];
            [data setValue:[NSString stringWithFormat:@"%d", _orderRequest.storeNumber] forKey:@"store"];
            [data setValue:_orderRequest.bagPreferenceName forKey:@"bag_preference"];
            [data setValue:_orderRequest.instructions forKey:@"instructions"];
            [data setValue:[NSString stringWithFormat:@"%f",_app._currentEcartPreOrderResponse.totalPrice] forKey:@"price"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
    }
#endif
}


- (void)handleEcartOrderServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];

    if(status == 200) // service success
    {
        [self removeFromSuperview];
        [_ecartScreenDelegate handleEcartOrderServiceResponse:responseObject];
    }
    else // service failure
    {
        if(status == 422) // backend error
        {
            WebServiceError *error = [_service getError];
            TextDialog *dialog = [[TextDialog alloc] initWithView:self title:@"Ecart Order Failed" message:error.errorMessage];
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


#pragma mark WebServiceListener Delegate
- (void)onServiceResponse:(id)responseObject
{
    if([responseObject isKindOfClass:[EcartOrderRequest class]])
    {
        [_progressDialog dismiss];
        [self handleEcartOrderServiceResponse:responseObject];
    }
}


@end
