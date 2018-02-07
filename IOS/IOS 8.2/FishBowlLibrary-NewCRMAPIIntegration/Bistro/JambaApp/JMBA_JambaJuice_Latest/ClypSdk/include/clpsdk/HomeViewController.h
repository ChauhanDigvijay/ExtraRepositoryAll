//
//  HomeViewController.h
//  LibraryDemo
//
//  Created by Gourav Shukla on 19/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>
IB_DESIGNABLE

@protocol LogOutAction <NSObject>

// custom delegate method
-(void)logout;


@end

@interface HomeViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIPageControl *pageControlView;
@property (weak, nonatomic) IBOutlet UIImageView *componyLogoImage;

@property (weak,nonatomic) id <LogOutAction> delegate;
@property (weak, nonatomic) IBOutlet UIButton *reedemrewardsBtn;
@property (weak, nonatomic) IBOutlet UIButton *specialOfferBtn;
@property (strong, nonatomic) IBOutlet UIView *offerView;
@property (weak, nonatomic) IBOutlet UILabel *customerName;

@end
