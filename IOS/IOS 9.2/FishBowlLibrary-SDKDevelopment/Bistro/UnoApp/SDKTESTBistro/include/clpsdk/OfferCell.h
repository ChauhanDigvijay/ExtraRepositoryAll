//
//  OfferCell.h
//  clpsdk
//
//  Created by Gourav Shukla on 11/08/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OfferCell : UICollectionViewCell
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgOfferPic;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *offerDescriptionLbl;

@end
