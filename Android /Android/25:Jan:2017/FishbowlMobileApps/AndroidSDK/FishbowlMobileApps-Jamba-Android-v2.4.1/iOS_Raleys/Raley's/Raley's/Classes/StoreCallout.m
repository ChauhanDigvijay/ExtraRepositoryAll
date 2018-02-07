//
//  StoreCallout.m
//  Raley's
//
//  Created by Billy Lewis on 9/25/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "Login.h"
#import "StoreCallout.h"
#import "Store.h"
#import "Utility.h"
#import "Logging.h"

#define font_size17 17
#define font_size13 13
#define font_size10 10

@implementation StoreCallout

@synthesize _storeLocatorScreenDelegate;
@synthesize _myStoreButton;
@synthesize _myStoreLabel;

- (id)initWithFrame:(CGRect)frame andStore:(Store *)store
{
    self = [super initWithFrame:frame];
    
    if(self)
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        _store = store;
        Login *login = [_app getLogin];

        UIImageView *background = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        background.image = [UIImage imageNamed:@"map_dialog1"];
        [self addSubview:background];
       /*
        int titleHeight = frame.size.height * .16;
        UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(frame.size.height * .02, 0, frame.size.width, titleHeight)];
        titleLabel.backgroundColor = [UIColor clearColor];
        titleLabel.textAlignment = NSTextAlignmentCenter;
        titleLabel.font = [Utility fontForFamily:_app._boldFont andHeight:titleHeight];
        titleLabel.textColor = [UIColor blackColor];
        titleLabel.text = store.chain;
        [self addSubview:titleLabel];*/
        
        UIImageView *chainLabel ;
        if( _app._deviceType == IPHONE_5){
            chainLabel = [[UIImageView alloc]initWithFrame:CGRectMake(frame.size.width * 0.03, frame.size.height * 0.123, frame.size.width * .15625, frame.size.height  * .50)];
        }
        else{
            chainLabel = [[UIImageView alloc]initWithFrame:CGRectMake(frame.size.width * 0.02, frame.size.height * 0.125, frame.size.width * .15, frame.size.height  * .53)];
        }
        
        if([_store.chain caseInsensitiveCompare:@"Raley's"] == NSOrderedSame)
        {
            [chainLabel setImage:[UIImage imageNamed:@"raley_logo"]];
        }
        else if([_store.chain caseInsensitiveCompare:@"Bel Air"] == NSOrderedSame)
        {
            [chainLabel setImage:[UIImage imageNamed:@"bel_logo"]];
        }
        else if([_store.chain caseInsensitiveCompare:@"Nob Hill Foods"] == NSOrderedSame)
        {
            [chainLabel setImage:[UIImage imageNamed:@"nob_logo"]];
        }
        
       // [chainLabel setImage:[UIImage imageNamed:@"raley_logo"]];
        [self addSubview:chainLabel];
        
        // Ecart
        if(store.ecart.boolValue){
            UIImageView *ecart = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"ecart_button"]];
            [ecart setUserInteractionEnabled:NO];
            CGRect frame = chainLabel.frame;
            frame.origin.x = frame.origin.x + frame.size.width - 10; // +frame.size.width;
            frame.size.width = 20;
            frame.size.height = 9;
            [ecart setFrame:frame];
            [self addSubview:ecart];
        }
        //
        
        
        int textHeight ;
        if( _app._deviceType == IPHONE_5){
         textHeight = frame.size.height * .47;  // 27
        }
        else{
        textHeight = frame.size.height * .25;
        }
        //UIFont *addresstextFont = [Utility fontForFamily:_app._normalFont andHeight:font_size13]; //textHeight
        //UIFont *citytextFont = [Utility fontForFamily:_app._normalFont andHeight:font_size10]; //textHeight * 0.9
        UIFont *addresstextFont = [UIFont fontWithName:_app._normalFont size:10.5];
        UIFont *citytextFont = [UIFont fontWithName:_app._normalFont size:9];
        
        
        UILabel *addressLabel;
        if( _app._deviceType == IPHONE_5){
            addressLabel = [[UILabel alloc] initWithFrame:CGRectMake(frame.size.width * .24, frame.size.height * .07, frame.size.width * 0.6, textHeight)];
        }
        else{
           addressLabel = [[UILabel alloc] initWithFrame:CGRectMake(frame.size.width * .21, frame.size.height * .15, frame.size.width * 0.65, textHeight)];
        }
        
        addressLabel.backgroundColor = [UIColor clearColor]; //clearColor
        addressLabel.textAlignment = NSTextAlignmentLeft; //NSTextAlignmentCenter
        addressLabel.font = addresstextFont;
        addressLabel.textColor = [UIColor whiteColor]; //blackColor
        addressLabel.text = [NSString stringWithFormat:@"%@", store.address];
     //   addressLabel.text = [NSString stringWithFormat:@"%@", @"123456789012345678901234567890"];

        addressLabel.adjustsFontSizeToFitWidth = NO;
        addressLabel.lineBreakMode = NSLineBreakByTruncatingTail;
        [self addSubview:addressLabel];
        
        UILabel *cityLabel;
        
        if( _app._deviceType == IPHONE_5){
            cityLabel = [[UILabel alloc] initWithFrame:CGRectMake(frame.size.width * .24, frame.size.height * .27, frame.size.width * 0.6, textHeight)];
        }
        else{
            cityLabel = [[UILabel alloc] initWithFrame:CGRectMake(frame.size.width * .21, frame.size.height * .37, frame.size.width * 0.7, textHeight)];
        }
        
        cityLabel.backgroundColor = [UIColor clearColor];
        cityLabel.textAlignment = NSTextAlignmentLeft; //NSTextAlignmentCenter
        cityLabel.font = citytextFont;
        cityLabel.textColor = [UIColor whiteColor]; //blackColor
        cityLabel.text = [NSString stringWithFormat:@"%@ %@ %@", store.city, store.state, store.zip];
        [self addSubview:cityLabel];
        
        UIButton *makebutton,*makebutton1;
        //UIImageView *makebutton;
        
        CGFloat button_width=28.0;
        CGFloat button_height=28.0;
        
        if( _app._deviceType == IPHONE_5){
//            makebutton = [[UIButton alloc]initWithFrame:CGRectMake(frame.size.width * 0.85, frame.size.height * 0.23, frame.size.width * .1, frame.size.height  * .30)];
//            makebutton1 = [[UIButton alloc]initWithFrame:CGRectMake(frame.size.width * 0.85, frame.size.height * 0.23, frame.size.width * .1, frame.size.height  * .30)];

            makebutton = [[UIButton alloc]initWithFrame:CGRectMake(frame.size.width * 0.83, frame.size.height * 0.16, button_width, button_height)];
            makebutton1 = [[UIButton alloc]initWithFrame:CGRectMake(frame.size.width * 0.83, frame.size.height * 0.16, button_width, button_height)];
        }
        else{
            makebutton = [[UIButton alloc]initWithFrame:CGRectMake(frame.size.width * 0.86, frame.size.height * 0.16, button_width, button_height)];
            makebutton1 = [[UIButton alloc]initWithFrame:CGRectMake(frame.size.width * 0.87, frame.size.height * 0.16, button_width, button_height)];
        }
        if(_store.storeNumber == login.storeNumber){
            //[makebutton setImage:[UIImage imageNamed:@"map_forward_button"]];
            [makebutton setBackgroundImage:[UIImage imageNamed:@"product_checked_box"] forState:UIControlStateNormal]; //map_mystore_button
            [makebutton removeTarget:self action:@selector(myStoreButtonPressed) forControlEvents:UIControlEventTouchUpInside];
            [self addSubview:makebutton];
        }
        else{
            [makebutton1 setBackgroundImage:[UIImage imageNamed:@"map_forward_button"] forState:UIControlStateNormal];
            [makebutton1 addTarget:self action:@selector(myStoreButtonPressed) forControlEvents:UIControlEventTouchUpInside];
            [self addSubview:makebutton1];
        }

        //[self addSubview:makebutton];
        
      /*  UILabel *ecartLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, frame.size.height * .51, frame.size.width, textHeight)];
        ecartLabel.backgroundColor = [UIColor clearColor];
        ecartLabel.textAlignment = NSTextAlignmentCenter;
        ecartLabel.font = citytextFont;
        ecartLabel.textColor = [UIColor blackColor];
        ecartLabel.text = [NSString stringWithFormat:@"Ecart Available: %@", store.ecart];
        [self addSubview:ecartLabel];

       // _myStoreLabel = [[UILabel alloc] initWithFrame:CGRectMake(frame.size.width * .1, frame.size.height * .65, frame.size.width * .8, frame.size.height * .14)];
        _myStoreLabel = [[UILabel alloc] initWithFrame:CGRectMake(frame.size.width * .1, frame.size.height * .65, frame.size.width * .8, frame.size.height * .14)];
        _myStoreLabel.backgroundColor = [UIColor clearColor];
        _myStoreLabel.textAlignment = NSTextAlignmentCenter;
        _myStoreLabel.font = [Utility fontForFamily:_app._boldFont andHeight:frame.size.height * .14];
        _myStoreLabel.textColor = [UIColor whiteColor];
        _myStoreLabel.text = [NSString stringWithFormat:@"This Is My Store!"];
        [self addSubview:_myStoreLabel];

        _myStoreButton = [[UIButton alloc] initWithFrame:CGRectMake(frame.size.width * .1, frame.size.height * .65, frame.size.width * .8, frame.size.height * .16)];
        [_myStoreButton setBackgroundImage:[UIImage imageNamed:@"button_red_plain"] forState:UIControlStateNormal];
        _myStoreButton.titleLabel.font = [Utility fontForFamily:_app._normalFont andHeight:frame.size.height * .12];
        [_myStoreButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_myStoreButton addTarget:self action:@selector(myStoreButtonPressed) forControlEvents:UIControlEventTouchUpInside];
        [_myStoreButton setTitle:@"Make This My Store" forState:UIControlStateNormal];
        [self addSubview:_myStoreButton];

        if(_store.storeNumber == login.storeNumber)
            _myStoreButton.hidden = YES;
        else
            _myStoreLabel.hidden = NO; */
}

    return self;
}


- (void)myStoreButtonPressed
{
    [_storeLocatorScreenDelegate changeUserStore:_store];
}

@end
