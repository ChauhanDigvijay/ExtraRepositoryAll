//
//  SubMenuCollectionViewCell.h
//  clpsdk
//
//  Created by surendra pathak on 20/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SubMenuCollectionViewCell : UICollectionViewCell
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imgSubMenuItem;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblPriceSubItem;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblNameSubItem;

@end
