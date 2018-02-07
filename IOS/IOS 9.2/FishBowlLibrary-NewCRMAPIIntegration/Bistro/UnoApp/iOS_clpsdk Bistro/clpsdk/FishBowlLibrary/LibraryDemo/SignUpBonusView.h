//
//  SignUpBonusView.h
//  clpsdk
//
//  Created by Gourav Shukla on 29/11/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

//rewardRule
//ruleId

#import <UIKit/UIKit.h>

@protocol SelectBonus <NSObject>

// custom delegate method

-(void)Bonus:(NSString *)ruleId andReward :(NSString *)rewardRule andDescription :(NSString *)Description withTxtTag:(NSInteger)intTxtTag;


@end

@interface SignUpBonusView : UIViewController
@property (weak,nonatomic) id<SelectBonus> delegate;
@property (assign,nonatomic) NSInteger intTag;

@end
