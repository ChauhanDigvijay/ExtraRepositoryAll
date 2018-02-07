//
//  StateSearchView.h
//  clpsdk
//
//  Created by Gourav Shukla on 16/10/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol stateSearch <NSObject>

// custom delegate method

// State Search
-(void)stateSearch:(NSString *)StateName withTag:(NSInteger)intTag;

// CountryName Search
-(void)countrySearch:(NSString *)CountryName and :(NSString *)CountryCode WithTextTag:(NSInteger)intTxtTag;



@end

@interface StateSearchView : UIViewController

@property (strong,nonatomic) NSString * titleLabel;
@property (assign,nonatomic) NSInteger intTag;

@property (weak,nonatomic) id<stateSearch> delegate;


@end
