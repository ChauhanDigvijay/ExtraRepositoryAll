//
//  StoreTableCell.m
//  Raley's
//
//  Created by Bill Lewis on 10/9/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "StoreTableCell.h"
#import "Utility.h"
#import "Logging.h"

#define font_size17 17
#define font_size13 13
#define font_size10 10

@implementation StoreTableCell

@synthesize _storeLocatorScreenDelegate;

- (id)initWithStore:(Store *)store :(int)width :(int)height
{
    if (self = [super initWithStyle:UITableViewCellStyleDefault reuseIdentifier:STORE_TABLE_CELL_ID])
    {
        _app = (id)[[UIApplication sharedApplication] delegate];
        _store = store;
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        self.contentView.backgroundColor = [UIColor colorWithRed:255.0 green:255.0 blue:255.0 alpha:0.60];
        Login *login = [_app getLogin];

      /*
        NSString *sample = [[NSString alloc]initWithString:_store.chain];
        LogInfo(@"Chain: %@",sample);
        
        if(([_store.chain caseInsensitiveCompare:@"Raley's"] != NSOrderedSame) && ([_store.chain caseInsensitiveCompare:@"Bel Air"] != NSOrderedSame) && ([_store.chain caseInsensitiveCompare:@"Nob Hill Foods"] != NSOrderedSame) )
        {
            NSString *sample = [[NSString alloc]initWithString:_store.chain];
            NSLog(@"Chain: %@",sample);
        }*/
        
        //Raley's
        //Bel Air
//        int textXOrigin = width * .2;
        int textWidth = width * .6;
        int new_Text_X=10.0f;

        //UILabel *chainLabel = [[UILabel alloc] initWithFrame:CGRectMake(textXOrigin, height * .0, textWidth, height * .24)];
       // UILabel *chainLabel = [[UILabel alloc] initWithFrame:CGRectMake(new_Text_X, height * .0, textWidth, height * .24)];
         //UILabel *chainLabel = [[UILabel alloc] initWithFrame:CGRectMake(new_Text_X, height * .2, width * .22, height * .59)];
        
        //chainLabel.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"raley_logo"]];
       
        UIImageView *chainLabel;
//        if( _app._deviceType == IPHONE_5){
//
//       // chainLabel = [[UIImageView alloc]initWithFrame:CGRectMake(new_Text_X, height * .2, width * .27, height * .64)];
////       chainLabel = [[UIImageView alloc]initWithFrame:CGRectMake(new_Text_X, height * .2, width * .20, height * .64)];
//        
//        chainLabel = [[UIImageView alloc]initWithFrame:CGRectMake(new_Text_X, height * .1, width * .13, height * .84)];
//        }
//        else{
//          chainLabel = [[UIImageView alloc]initWithFrame:CGRectMake(new_Text_X, height * .2, width * .13, height * .64)];
//
//        }
        
        if( _app._deviceType == IPHONE_5){
            
            // chainLabel = [[UIImageView alloc]initWithFrame:CGRectMake(new_Text_X, height * .2, width * .27, height * .64)];
            chainLabel = [[UIImageView alloc]initWithFrame:CGRectMake(new_Text_X, height * .1, width * .13, height * .84)]; // width 20  15 height 64  09
        }
        else{
            chainLabel = [[UIImageView alloc]initWithFrame:CGRectMake(new_Text_X, height * .2, width * .09, height * .64)]; // width 13   height 64
            
        }
        
       // chainLabel.backgroundColor = [UIColor greenColor];
        //chainLabel.layer.masksToBounds = YES;
        //chainLabel.layer.cornerRadius = 4.0;
        //chainLabel.layer.borderWidth = 0.0;
        //chainLabel.layer.borderColor = [[UIColor clearColor] CGColor];
        
        
        //if([_store.chain isEqualToString:@"Raley's"]) Bel Air
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
        
        
        
       /* chainLabel.font = [Utility fontForFamily:_app._normalFont andHeight:height * .26];
        chainLabel.textAlignment = NSTextAlignmentCenter;
        chainLabel.numberOfLines = 0;
        chainLabel.backgroundColor = [UIColor clearColor];
        chainLabel.textColor = [UIColor redColor];
        chainLabel.text = _store.chain; */
       // [chainLabel setBackgroundColor:[UIColor redColor]];
        [self addSubview:chainLabel];

        //UILabel *addressLabel = [[UILabel alloc] initWithFrame:CGRectMake(new_Text_X * 2, height * .4, textWidth * 0.3, height * .20)];
         UILabel *addressLabel = [[UILabel alloc] initWithFrame:CGRectMake(new_Text_X * 6.5, height * .15, textWidth * 1.2, height * .50)]; // 0.2 width
        if(store.storeNumber == login.storeNumber){
            addressLabel.textColor = [UIColor blackColor]; // blackColor
        }else{
            addressLabel.textColor = [UIColor darkGrayColor]; // blackColor
        }
        addressLabel.font = [Utility fontForFamily:_app._normalFont andHeight:font_size17]; //  height * .25
        addressLabel.textAlignment = NSTextAlignmentLeft;
        addressLabel.numberOfLines = 0;
        addressLabel.backgroundColor = [UIColor clearColor];
        addressLabel.text = _store.address;
       // [addressLabel setBackgroundColor:[UIColor greenColor]];
        [self addSubview:addressLabel];

        //UILabel *cityStateZipLabel = [[UILabel alloc] initWithFrame:CGRectMake(new_Text_X * 2, height * .44, textWidth * 0.3, height * .20)];
        UILabel *cityStateZipLabel = [[UILabel alloc] initWithFrame:CGRectMake(new_Text_X * 6.5, height * .56, textWidth * 0.8, height * .40)];
        if(store.storeNumber == login.storeNumber){
            cityStateZipLabel.textColor = [UIColor blackColor]; // blackColor
        }
        else{
            cityStateZipLabel.textColor = [UIColor darkGrayColor]; // blackColor
        }
        cityStateZipLabel.font = [Utility fontForFamily:_app._normalFont andHeight:font_size13]; // height * .22
        cityStateZipLabel.textAlignment = NSTextAlignmentLeft;
        cityStateZipLabel.backgroundColor = [UIColor clearColor];
        cityStateZipLabel.text = [NSString stringWithFormat:@"%@ %@ %@", _store.city, _store.state, _store.zip];
        //[cityStateZipLabel setBackgroundColor:[UIColor blueColor]];
        [self addSubview:cityStateZipLabel];

        //UILabel *ecartLabel = [[UILabel alloc] initWithFrame:CGRectMake(width * .79, height * .24, width * .20, height * .20)];
       /* UILabel *ecartLabel = [[UILabel alloc] initWithFrame:CGRectMake(width * .74, height * .24, width * .20, height * .20)];
        ecartLabel.font = [Utility fontForFamily:_app._normalFont andHeight:height * .20];
        ecartLabel.textAlignment = NSTextAlignmentRight;
        ecartLabel.backgroundColor = [UIColor clearColor];
        ecartLabel.textColor = [UIColor blackColor];
        ecartLabel.text = [NSString stringWithFormat:@"Ecart: %@", _store.ecart];
        [ecartLabel setBackgroundColor:[UIColor grayColor]];
        [self addSubview:ecartLabel]; */

       // UILabel *distanceLabel = [[UILabel alloc] initWithFrame:CGRectMake(width * .74, height * .44, width * .20, height * .20)];
        UILabel *distanceLabel = [[UILabel alloc] initWithFrame:CGRectMake(width * .67, height * .67, width * .30, height * .40)];
        distanceLabel.font = [Utility fontForFamily:_app._normalFont andHeight:font_size10]; // height * .20
        distanceLabel.textAlignment = NSTextAlignmentRight;
        distanceLabel.backgroundColor = [UIColor clearColor];
        distanceLabel.textColor = [UIColor redColor];
        distanceLabel.text = [NSString stringWithFormat:@"%.1f mi.", _store._distanceFromLocation / METERS_PER_MILE];
        //distanceLabel.text=store.ecart;
        // [distanceLabel setBackgroundColor:[UIColor brownColor]];
        [self addSubview:distanceLabel];
        
        
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
        

        if(store.storeNumber == login.storeNumber)
        {
//            UILabel *myStoreLabel = [[UILabel alloc] initWithFrame:CGRectMake(width * .3, height * .66, width * .4, height * .28)];
  
           // UILabel *myStoreLabel = [[UILabel alloc] initWithFrame:CGRectMake(width * .14, height * .66, width * .45, height * .28)];
            UILabel *myStoreLabel = [[UILabel alloc] initWithFrame:CGRectMake(width * .84, height * .20, width * .14, height * .50)]; //.38  .53
            
            [myStoreLabel setBackgroundColor:[UIColor redColor]];
            
            if( _app._deviceType == IPHONE_5){

                myStoreLabel.font = [Utility fontForFamily:_app._normalFont andHeight:font_size10]; //height * .20
            }
            else{
                myStoreLabel.font = [Utility fontForFamily:_app._normalFont andHeight:font_size10]; //height * .22

            }
            myStoreLabel.textAlignment = NSTextAlignmentCenter;
            myStoreLabel.numberOfLines = 2;
            
            [myStoreLabel setBackgroundColor:[UIColor colorWithRed:(35/255.f) green:(156/255.f) blue:(92/255.f) alpha:1.0]]; // clearColor
           // myStoreLabel.backgroundColor = [UIColor greenColor]; // clearColor
            myStoreLabel.textColor = [UIColor whiteColor];
            NSString *my_store_title_str;
            my_store_title_str = [NSString stringWithFormat:@"%@\n%@", @"My", @"Store"];   // Note the \n will give you a new line
            myStoreLabel.text = my_store_title_str; //This Is My Store
            [myStoreLabel setLineBreakMode:NSLineBreakByWordWrapping];
            [myStoreLabel setTextAlignment:NSTextAlignmentCenter];
            
            myStoreLabel.layer.masksToBounds = YES;
            myStoreLabel.layer.cornerRadius = 3.0;
//            myStoreLabel.layer.borderWidth = 1.0;
//            myStoreLabel.layer.borderColor = [[UIColor clearColor] CGColor];
            
            
            [self addSubview:myStoreLabel];
        }
        else
        {
            //UIButton *myStoreButton = [[UIButton alloc] initWithFrame:CGRectMake(width * .135, height * .66, width * .45, height * .28)];
            UIButton *myStoreButton = [[UIButton alloc] initWithFrame:CGRectMake(width * .84, height * .20, width * .1492, height * .55)];
            
//            [myStoreButton setBackgroundImage:[UIImage imageNamed:@"button_red_plain"] forState:UIControlStateNormal];
            [myStoreButton setTitleColor:[UIColor colorWithRed:51.0f/255.0f green:51.0f/255.0f blue:51.0f/255.0f alpha:1.0f] forState:UIControlStateNormal];
            myStoreButton.titleLabel.numberOfLines = 2;
            [myStoreButton.titleLabel setTextAlignment:NSTextAlignmentCenter];
            if( _app._deviceType == IPHONE_5){

            myStoreButton.titleLabel.font = [Utility fontForFamily:_app._normalFont andHeight:font_size10]; //height * .20
            }
            else{
                myStoreButton.titleLabel.font = [Utility fontForFamily:_app._normalFont andHeight:font_size10]; //height * .22
            }
            [myStoreButton setTitleColor:[UIColor whiteColor] forState:UIControlStateHighlighted];
            [myStoreButton addTarget:self action:@selector(myStoreButtonPressed) forControlEvents:UIControlEventTouchUpInside];
            
            NSString *title_str;
            title_str = [NSString stringWithFormat:@"%@\n%@", @"Make", @"my store"];   // Note the \n will give you a new line
            [myStoreButton.titleLabel setLineBreakMode:NSLineBreakByWordWrapping];
            [myStoreButton.titleLabel setTextAlignment:NSTextAlignmentCenter];
            [myStoreButton setTitle:title_str forState:UIControlStateNormal]; //Make This My Store
            //[myStoreButton setTitle:@"Choose   Store" forState:UIControlStateNormal]; //Make This My Store

            myStoreButton.layer.masksToBounds = YES;
            myStoreButton.layer.cornerRadius = 2.5;
            [myStoreButton.layer setBorderWidth:1.0f];
            [myStoreButton.layer setBorderColor:[UIColor lightGrayColor].CGColor];
            
            if(_app._deviceType==IPHONE_5){
                 [myStoreButton setContentEdgeInsets:UIEdgeInsetsMake(2.5, 0, 0, 0)];
            }
            else{
                 [myStoreButton setContentEdgeInsets:UIEdgeInsetsMake(1.8, 0, 0, 0)];
            }
//            [myStoreButton setBackgroundColor:[UIColor colorWithRed:208.0f/255.0f green:208.0f/255.0f blue:208.0f/255.0f alpha:1.0f]]; // darker gray color
            [myStoreButton setBackgroundColor:[UIColor whiteColor]];
            
            [self addSubview:myStoreButton];
        }
    }
    
    return self;
}


- (void)myStoreButtonPressed
{
    LogInfo(@"accept button pressed for store %d, %@, %@", _store.storeNumber, _store.chain, _store.city);
    [_storeLocatorScreenDelegate changeUserStore:_store];
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
