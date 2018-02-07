//
//  StoreSearchView.h
//  clpsdk
//
//  Created by Gourav Shukla on 10/08/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol StoreSearch <NSObject>


// custom delegate method

-(void)StoreSearch:(NSString *)storeNumber and :(NSString *)StoreName withTag:(NSInteger)intTag;
-(void)customSearch:(NSString *)strValue withTextFieldTag:(NSInteger)intTag;

@end

@interface StoreSearchView : UIViewController


@property (weak,nonatomic) id<StoreSearch> delegate;
@property(strong,nonatomic) NSMutableArray  * arrCategory;
@property(assign,nonatomic) NSInteger customField;
@property(assign,nonatomic) NSInteger tagValue;



@end
