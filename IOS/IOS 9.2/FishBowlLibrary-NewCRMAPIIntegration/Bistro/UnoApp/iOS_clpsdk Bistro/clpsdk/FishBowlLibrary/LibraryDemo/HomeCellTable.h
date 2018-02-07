//
//  HomeCellTable.h
//  clpsdk
//
//  Created by Gourav Shukla on 12/08/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeCellTable : UITableViewCell
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *imageOfferTbl;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *lblTitleTbl;
@property (weak, nonatomic) IBOutlet UILabel *lblDescriptionTbl;
@property (weak, nonatomic) IBOutlet UILabel *lblExpireDateTbl;

@end
