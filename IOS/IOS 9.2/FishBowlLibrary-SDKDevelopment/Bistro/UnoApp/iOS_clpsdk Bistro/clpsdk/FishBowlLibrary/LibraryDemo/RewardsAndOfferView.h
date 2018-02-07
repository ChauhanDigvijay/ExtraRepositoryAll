//
//  RewardsAndOfferView.h
//  clpsdk
//
//  Created by Gourav Shukla on 19/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RewardsAndOfferView : UIViewController
@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tblRewards;

@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tblOffers;
@end
