//
//  collectionCell.h
//  pageView
//
//  Created by Gourav Shukla on 22/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface collectionCell : UICollectionViewCell

@property (weak, nonatomic) IBOutlet UIImageView *img;
@property (weak, nonatomic) IBOutlet UIView *cellView;
@property (weak, nonatomic) IBOutlet UILabel *descriptionLbl;
@property (weak, nonatomic) IBOutlet UILabel *expireDate;
@property (weak, nonatomic) IBOutlet UILabel *campineTitle;
@property (weak, nonatomic) IBOutlet UIButton *promocodeBtn;
@property (weak, nonatomic) IBOutlet UIButton *sendViaSmsBtn_Action;

@end
