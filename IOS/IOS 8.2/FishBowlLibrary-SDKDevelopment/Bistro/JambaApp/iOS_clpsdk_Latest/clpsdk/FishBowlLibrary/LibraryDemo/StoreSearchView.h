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

-(void)StoreSearch:(NSString *)storeNumber and :(NSString *)StoreName;


@end

@interface StoreSearchView : UIViewController
@property (weak,nonatomic) id<StoreSearch> delegate;

@end
