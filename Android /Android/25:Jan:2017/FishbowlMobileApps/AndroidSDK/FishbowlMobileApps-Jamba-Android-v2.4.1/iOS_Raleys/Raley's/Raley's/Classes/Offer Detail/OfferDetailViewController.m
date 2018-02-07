//
//  OfferDetailViewController.m
//  Raley's
//
//  Created by VT01 on 28/05/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import "OfferDetailViewController.h"
#import "AppDelegate.h"
#import "Utility.h"
#import "OfferAcceptRequest.h"
#import "ProgressDialog.h"
#import "TextDialog.h"


#import "AppDelegate.h"
#import "Logging.h"

#define GRIDCELL_GAP 10
#define GRIDCELL_PRICE_HEIGHT 30
#define GRIDCELL_FONT_SIZE 13
#define GRIDCELL_PADDING 0.02

@interface OfferDetailViewController ()
{
    Offer *selected;
    CGFloat yAxis;
    NSString *enddate_text;
    
    UIView *product_radius_view;
    
    ProgressDialog *_progressDialog;
    WebService             *_service;
    AppDelegate     *_app;
    Login               *_login;
}
@end

@implementation OfferDetailViewController
@synthesize delegate;

- (id)initWithNibName:(NSString *)nibNameOrNil _offer:(Offer *)offer _enddate:(NSString*)ed
{
    self = [super initWithNibName:nibNameOrNil bundle:nil];
    if (self) {
        selected = offer;
        enddate_text = ed;
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    _app = (id)[[UIApplication sharedApplication] delegate];
    _login = [_app getLogin];
    
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        yAxis = 20;
    else
        yAxis = 0;
    
    HeaderView *headerView = [[HeaderView alloc] initWithFrame:CGRectMake(0.0, yAxis, self.view.bounds.size.width, _app._headerHeight)];
    [self.view setBackgroundColor:[UIColor blackColor]];
    
    [container setFrame:CGRectMake(0, yAxis + headerView.frame.size.height, self.view.bounds.size.width, self.view.bounds.size.height - yAxis - headerView.frame.size.height)];
    //    [container setBackgroundColor:[UIColor whiteColor]];
    [container setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]];
    
    int view_gap=10.0f;
    CGFloat new_app_width=self.view.frame.size.width-(view_gap*2);
    // ---------------------  Products View  ---------------------  //
    product_radius_view=[[UIView alloc]initWithFrame:CGRectMake(view_gap, headerView.frame.size.height, new_app_width ,container.contentSize.height-(view_gap*3))];

    [product_radius_view.layer setCornerRadius:5.0f];
    [product_radius_view setBackgroundColor:[UIColor whiteColor]];
    [product_radius_view.layer setShadowColor:[UIColor darkGrayColor].CGColor];
    //    [product_radius_view.layer setShadowOpacity:0.6];
    //    [product_radius_view.layer setShadowRadius:0.3];
    [product_radius_view.layer setBorderWidth:0.8];
    [product_radius_view.layer setBorderColor:[UIColor colorWithRed:215.0/255.0 green:215.0/255.0 blue:215.0/255.0 alpha:1.0].CGColor];
    [product_radius_view.layer setShadowOffset:CGSizeMake(0.0,0.7)];
    [product_radius_view setClipsToBounds:YES]; // crop the image left and right most top
    [container setClipsToBounds:YES];
    [container insertSubview:product_radius_view belowSubview:accept_offer];
    

    /*
     _consumerTitleLabel
     _consumerDescriptionLabel
     _offerLimitLabel
     _endDateLabel
     _acceptedOfferLabel
     _acceptOfferButton
     */
    

    
    
    // modify (override) font
    [htitle setFont:[UIFont fontWithName:_app._normalFont size:GRIDCELL_FONT_SIZE*1]];
    [desc setFont:[UIFont fontWithName:_app._normalFont size:GRIDCELL_FONT_SIZE]];
    [limit setFont:[UIFont fontWithName:_app._normalFont size:GRIDCELL_FONT_SIZE]];
    [enddate setFont:[UIFont fontWithName:_app._normalFont size:GRIDCELL_FONT_SIZE]];
    [accept_offer_lbl setFont:[UIFont fontWithName:_app._normalFont size:GRIDCELL_FONT_SIZE*1.3]];
//    [accept_offer.titleLabel setFont:[UIFont fontWithName:_app._normalFont size:GRIDCELL_FONT_SIZE*1.3]];
    
    // modify (override) font
    [htitle setTextAlignment:NSTextAlignmentLeft];
    [desc setTextAlignment:NSTextAlignmentLeft];
    [limit setTextAlignment:NSTextAlignmentLeft];
    [enddate setTextAlignment:NSTextAlignmentLeft];
//    [accept_offer_lbl setFont:[UIFont fontWithName:_app._normalFont size:GRIDCELL_FONT_SIZE*1.3]];
//    [accept_offer.titleLabel setFont:[UIFont fontWithName:_app._normalFont size:GRIDCELL_FONT_SIZE*1.3]];

    
    UIImage *image=[_app getCachedImage:selected.offerProductImageFile];
    if(image ==nil){
    image=[UIImage imageNamed:@"noimage.png"];
    }
    
    [imageview setImage:image];
    
    [htitle setText:selected.consumerTitle];
    [desc setText:selected.consumerDesc];
    [limit setText:selected.offerLimit];
    [enddate setText:enddate_text];
    
//    NSString *offer_lbl_text;
    if(selected._acceptableOffer == YES)
    {
        [accept_offer setHidden:NO];
        [accept_offer_lbl setHidden:YES];
        // [accept_offer setBackgroundImage:[UIImage imageNamed:@"accept_this_offer.png"] forState:UIControlStateNormal];
        
        [accept_offer setBackgroundColor:[UIColor colorWithRed:(204.0/255.0) green:(0.0/255.0) blue:(0.0/255.0) alpha:1.0] ];
        [accept_offer setTitle:@"Accept This Offer" forState:UIControlStateNormal];
        [accept_offer.layer setCornerRadius:4.0f];
        [accept_offer setBackgroundImage:nil forState:UIControlStateNormal];
        accept_offer.titleLabel.font =[UIFont fontWithName:_app._normalFont size:font_size17];
        [accept_offer setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];

    }
    else if(selected._acceptedOffer == YES)
    {
        [accept_offer setHidden:YES];
        [accept_offer_lbl setHidden:NO];
       // offer_lbl_text = @"Offer Accepted";
        //[accept_offer_lbl setTextColor:[UIColor blackColor]];
//        [accept_offer_lbl setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@""]]];
        [accept_offer_img setImage:[UIImage imageNamed:@"offer_accepted.png"]];
//        [accept_offer_lbl setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1]];

    }
    else
    {
        [accept_offer setHidden:YES];
        [accept_offer_lbl setHidden:NO];
//        [accept_offer_lbl setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@""]]];
         [accept_offer_img setImage:[UIImage imageNamed:@"offer_always_available.png"]];
//        offer_lbl_text= @"Always Available";
//        [accept_offer_lbl setBackgroundColor:[UIColor colorWithRed:187.0/255.0 green:0.0/255.0 blue:0.0/255.0 alpha:1]];
//        [accept_offer_lbl setTextColor:[UIColor whiteColor]];
    }
    
//    [accept_offer_lbl setText:offer_lbl_text];
    
//    [accept_offer_lbl setBackgroundColor:[UIColor colorWithRed:0.0 green:142.0/255.0 blue:2.0/255.0 alpha:1]];
    [accept_offer_img.layer setCornerRadius:3.0];
    
    
    CGRect frame = CGRectZero;
    CGFloat c_gap = GRIDCELL_GAP;
    CGSize size = CGSizeZero;
    size.height =  view_gap;//headerView.frame.size.height;
    
    // Product Image
    CGSize image_size=CGSizeMake(0, 0);
    if([_app getImageFullPath:[_app._currentOffer.offerProductImageFile lastPathComponent]] ==nil){
        image_size =CGSizeMake(container.frame.size.width, container.frame.size.height/2);
    }
    else{
        image_size =[Utility get_image_width_and_height:[_app getImageFullPath:[_app._currentOffer.offerProductImageFile lastPathComponent]]];
    }

    CGFloat image_height = image_size.height;
    frame = imageview.frame;
    frame.origin.x=0;
    frame.size.width = self.view.bounds.size.width;
    if(image_size.width>self.view.bounds.size.width){
        CGFloat radio = self.view.bounds.size.width / image_size.width;
        image_height = image_height * radio;
    }else{
        CGFloat new_image_x = self.view.bounds.size.width - image_size.width;
        new_image_x /=2;
        frame.origin.x = new_image_x;
        frame.size.width = image_size.width;
    }

    frame.origin.y = size.height+5;
    frame.size.height = image_height;
    [imageview setFrame:frame];

    [imageview setContentMode:UIViewContentModeScaleAspectFit];

    size.height+=image_height;
    size.height+= c_gap;

    
    CGFloat common_lbl_width = htitle.frame.size.width;//250;

    // Consumer Title Label
    CGSize desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:htitle.font.fontName _fontsize:htitle.font.pointSize _text:htitle.text];
    frame = htitle.frame;
    frame.origin.y=size.height;
    frame.size.height = desc_size.height;
    [htitle setFrame:frame];
    if(desc_size.height>0){
        size.height+=desc_size.height;
        size.height+= c_gap;
    }

    // Consumer Description Label
    desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:desc.font.fontName _fontsize:desc.font.pointSize _text:desc.text];
    frame = desc.frame;
//    desc_size.height = 40;
    frame.origin.y=size.height;
    frame.size.height = desc_size.height;
    [desc setFrame:frame];
    if(desc_size.height>0){
        size.height+=desc_size.height;
        size.height+= c_gap;
    }

    
    // Offer Limit Label
    desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:limit.font.fontName _fontsize:limit.font.pointSize _text:limit.text];
    frame = limit.frame;
    frame.origin.y=size.height;
    frame.size.height = desc_size.height;
    [limit setFrame:frame];
    if(desc_size.height>0){
        size.height+=desc_size.height;
        size.height+= c_gap;
    }
    

    // End Dated Label
    desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:enddate.font.fontName _fontsize:enddate.font.pointSize _text:enddate.text];
    frame = enddate.frame;
    frame.origin.y=size.height;
    frame.size.height = desc_size.height;
    [enddate setFrame:frame];
    if(desc_size.height>0){
        size.height+=desc_size.height;
        size.height+= c_gap;
    }

    
    if(selected._acceptableOffer == YES)
    {
        // Accepted Offer Button
        frame = accept_offer.frame;
//        frame.origin.x = 0;
        frame.origin.y = size.height;
        frame.size.width = frame.size.width;
        frame.size.height = GRIDCELL_PRICE_HEIGHT;
        [accept_offer setFrame:frame];
        size.height+=GRIDCELL_PRICE_HEIGHT;
        size.height+= c_gap;
        
    }
    else
    {
        size.height+= c_gap;
        // Accepted Offer Label
//        desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:accept_offer_lbl.font.fontName _fontsize:accept_offer_lbl.font.pointSize _text:accept_offer_lbl.text];
        frame = accept_offer_img.frame;
        frame.origin.y=size.height;
//        frame.size.height = desc_size.height;
//        [accept_offer_lbl setFrame:frame];
        [accept_offer_img setFrame:frame];
        size.height+=30; // Accept this offer image button height
        size.height+= c_gap;
    }
    

    [container setContentSize:CGSizeMake(container.frame.size.width, size.height + GRIDCELL_PRICE_HEIGHT)];
    
    frame = product_radius_view.frame;
    frame.size.height = container.contentSize.height-(view_gap*3);
    [product_radius_view setFrame:frame];

//    [accept_offer_lbl setHidden:NO];
//    [accept_offer setHidden:NO];
//    for (UIView *view in container.subviews) {
//        [view.layer setBorderWidth:1];
//        [view.layer setBorderColor:[UIColor redColor].CGColor];
//    }


    
    [headerView setDelegate:self];

    
    alert_image=[[UIImageView alloc]initWithFrame:CGRectMake((self.view.frame.size.width-48)/2, (self.view.frame.size.height-48)/2, 48, 48)];
    UIImage *aimage = [UIImage imageNamed:@"success"];
    [alert_image setImage:aimage];
    img_alert_container =[[UIView alloc]initWithFrame:self.view.bounds];
    [img_alert_container setBackgroundColor:[UIColor clearColor]];
    [img_alert_container addSubview:alert_image];
    [self.view addSubview:img_alert_container];
    [img_alert_container setHidden:YES];
    
    [self.view addSubview:headerView];
    
#ifdef CLP_ANALYTICS
    if(selected){
        @try {                                                                //Offer Opens menu click
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"OpenOffer" forKey:@"event_name"];
            [data setValue:selected.offerId forKey:@"item_id"];
            [data setValue:selected.consumerTitle forKey:@"item_name"];
            [data setValue:selected.promoCode forKey:@"promo_code"];
            [data setValue:selected.consumerDesc forKey:@"promo_title"];
            [data setValue:[NSString stringWithFormat:@"%f",selected.offerPrice] forKey:@"price"];
            [data setValue:selected.acceptGroup forKey:@"accept_group"];
            [data setValue:selected.startDate forKey:@"start_date"];
            [data setValue:selected.endDate forKey:@"end_date"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
    }
#endif
    
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

-(void)showTick{
    [img_alert_container setAlpha:0.f];
    
    [UIView animateWithDuration:0.5f delay:0.f options:UIViewAnimationOptionCurveEaseIn animations:^{
        [img_alert_container setHidden:NO];
        
        [img_alert_container setAlpha:1.0f];
    } completion:^(BOOL finished) {
        [UIView animateWithDuration:1.0f delay:1.0f options:UIViewAnimationOptionCurveEaseInOut animations:^{
            [img_alert_container setAlpha:0.f];
            
        } completion:^(BOOL finished){
            [img_alert_container setHidden:NO];
            
        }];
    }];
}



-(void)backButtonClicked{
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [self dismissViewControllerAnimated:NO completion:nil];
}


-(IBAction)accept_offer:(id)sender{
    // [delegate accept_offer];
    //    [delegate accept_offer_From_DetailPage];
    //accept_offer
    
    // Instant change the label for user acceptance
    [accept_offer setHidden:YES];
    [accept_offer_lbl setHidden:YES];
    CGRect frame = product_radius_view.frame;
    CGRect img_frame = accept_offer_img.frame;
    img_frame.origin.y = (frame.size.height - 40);
    [accept_offer_img setFrame:img_frame];
    [accept_offer_img setImage:[UIImage imageNamed:@"offer_accepted.png"]];
    //return;
    
    [self refreshLocalArrays];
    
    [self performSelector:@selector(backButtonClicked) withObject:nil afterDelay:1.0];
    [_shoppingDelegate RemoveOffers_DetailpageSelected];
    [self Detailpage_acceptOffer:selected];
    
#ifdef CLP_ANALYTICS
    if(selected){
        @try {                                                                //Offer Accepted Event
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"AcceptOffer" forKey:@"event_name"];
            [data setObject:selected.offerId forKey:@"item_id"];
            [data setObject:selected.consumerTitle forKey:@"item_name"];
            [data setObject:selected.promoCode forKey:@"promo_code"];
            [data setObject:selected.consumerDesc forKey:@"promo_title"];
            [data setObject:[NSString stringWithFormat:@"%f",selected.offerPrice] forKey:@"price"];
            [data setObject:selected.acceptGroup forKey:@"accept_group"];
            [data setObject:selected.startDate forKey:@"start_date"];
            [data setObject:selected.endDate forKey:@"end_date"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
    }
#endif
}

-(void)refreshLocalArrays{
    for(int i = 0; i < _app._personalizedOffersList.count; i++)
    {
        Offer *offer = [_app._personalizedOffersList objectAtIndex:i];
        
        if([offer.acceptGroup compare:selected.acceptGroup] == 0)
        {
            [_app._acceptedOffersList addObject:offer];
            [_app._personalizedOffersList removeObjectAtIndex:i];
            break;
        }
    }
    
    for(int i = 0; i < _app._extraFriendzyOffersList.count; i++)
    {
        Offer *offer = [_app._extraFriendzyOffersList objectAtIndex:i];
        
        if([offer.acceptGroup compare:selected.acceptGroup] == 0)
        {
            [_app._acceptedOffersList addObject:offer];
            [_app._extraFriendzyOffersList removeObjectAtIndex:i];
            break;
        }
    }
    
}



-(void)Detailpage_acceptOffer:(Offer *)offer
{
    OfferAcceptRequest *request = [[OfferAcceptRequest alloc] init];
    request.crmNumber = _login.crmNumber;
    request.acceptGroup = offer.acceptGroup;
    request.offerId = offer.offerId;
    
    //_progressDialog = [[ProgressDialog alloc] initWithView:self.view message:@"Sending accept request..."];
    //[_progressDialog show];
    
    //    _service = [[WebService alloc]initWithListener:self responseClassName:@"OfferAcceptRequest"];
    WebService *new_offer = [[WebService alloc]initWithListener:nil responseClassName:nil];
    [new_offer execute:OFFER_ACCEPT_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleAcceptOfferServiceResponse method below
}


- (void)handleAcceptOfferServiceResponse:(id)responseObject
{
    @try {
        
        int status = [_service getHttpStatusCode];
        
        if(status == 200) // service success
        {
            _service = nil;
            OfferAcceptRequest *response = (OfferAcceptRequest *)responseObject;
            
            for(int i = 0; i < _app._personalizedOffersList.count; i++)
            {
                Offer *offer = [_app._personalizedOffersList objectAtIndex:i];
                
                if([offer.acceptGroup compare:response.acceptGroup] == 0)
                {
                    [_app._acceptedOffersList addObject:offer];
                    [_app._personalizedOffersList removeObjectAtIndex:i];
                    break;
                }
            }
            
            for(int i = 0; i < _app._extraFriendzyOffersList.count; i++)
            {
                Offer *offer = [_app._extraFriendzyOffersList objectAtIndex:i];
                
                if([offer.acceptGroup compare:response.acceptGroup] == 0)
                {
                    [_app._acceptedOffersList addObject:offer];
                    [_app._extraFriendzyOffersList removeObjectAtIndex:i];
                    break;
                }
            }
//            [self showTick];
//            [self performSelector:@selector(backButtonClicked) withObject:nil afterDelay:1.0];
//            [_shoppingDelegate RemoveOffers_DetailpageSelected];
        }
        else // service failure
        {
            if(status == 422) // backend error
            {
                WebServiceError *error = [_service getError];
                TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Accept Offer Failed" message:error.errorMessage];
                [dialog show];
            }
            else if(status == 408) // timeout error
            {
                TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
                [dialog show];
            }
            else // http error
            {
                TextDialog *dialog = [[TextDialog alloc] initWithView:self.view title:@"Server Error" message:[NSString stringWithFormat:@"Accept Offer Failed"]];
                [dialog show];
            }
            
            _service = nil;
        }
    }
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
        
    }
}
- (void)waitForOffersThread:(NSObject *)urlString
{
    @autoreleasepool
    {
        int count = 0;
        
        while([_app offerThreadsDone] == NO && count++ < 11)
        {
            LogInfo(@"Waiting for offer threads to finish %d", count);
            [NSThread sleepForTimeInterval:1.0];  // keep retrying, web service timeout = 15 - splash screen delay = 4 gives us 11 seconds to wait
        }
        
        [_progressDialog dismiss];
        
        //        if(_offersView == nil) // on startup if requests weren't received when this screen first appeared
        //        {
        //            LogInfo(@"waitForOffersThread: Showing offers on startup");
        //        }
        //        else // offer button pressed if no offers were received on startup
        //        {
        //            LogInfo(@"waitForOffersThread: Updating offers");
        //            [_offersView removeFromSuperview];
        //            _offersView = nil;
        //            _offerGrid = nil;
        //        }
        
        //        if(urlString != nil && [(NSString *)urlString compare:OFFERS_PERSONALIZED_URL] == 0)
        //            [self performSelectorOnMainThread:@selector(showAvailableOffersView) withObject:nil waitUntilDone:NO];
        //        else // should be OFFERS_ACCEPTED_URL
        //          [self performSelectorOnMainThread:@selector(showAcceptedOffersView) withObject:nil waitUntilDone:NO];
    }
}

#pragma mark WebServiceListener Delegate
- (void)onServiceResponse:(id)responseObject
{
    if([responseObject isKindOfClass:[OfferAcceptRequest class]])
    {
        [_progressDialog dismiss];
        [self handleAcceptOfferServiceResponse:responseObject];
    }
}




# pragma mark - Util

-(CGSize)getUILabelFontSizeBasedOnText_width:(CGFloat)width _fontname:(NSString*)font _fontsize:(CGFloat)fsize _text:(NSString*)text{
    CGSize size = CGSizeMake(width, 0);
    if([Utility isEmpty:text]){
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
    //    size = [text sizeWithFont:[UIFont fontWithName:font size:fsize] constrainedToSize:constrainedSize lineBreakMode:NSLineBreakByWordWrapping];
    //    size.height += 10;
    return size;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
