//
//  SubMenuCell.m
//  clpsdk
//
//  Created by Gourav Shukla on 28/12/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "SubMenuCell.h"

@implementation SubMenuCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self.collectionview registerNib:[UINib nibWithNibName:@"SubMenuCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"SubMenuCollectionViewCell"];
    self.collectionview.backgroundColor = [UIColor clearColor];
    
//    self.collectionview.delegate   = self;
//    self.collectionview.dataSource = self;
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    
    [flowLayout setItemSize:CGSizeMake(100, 100)];
    
    
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    [self.collectionview setCollectionViewLayout:flowLayout];
    [self.collectionview setAllowsSelection:YES];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
}

@end
