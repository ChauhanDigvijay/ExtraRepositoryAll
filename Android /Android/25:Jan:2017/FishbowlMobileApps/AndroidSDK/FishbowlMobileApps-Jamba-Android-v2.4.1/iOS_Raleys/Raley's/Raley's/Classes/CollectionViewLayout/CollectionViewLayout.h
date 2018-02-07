//
//  CollectionViewLayout.h
//  CollectionViewExample
//
//  Created by Paul Dakessian on 9/6/12.
//  Copyright (c) 2012 Paul Dakessian, CapTech Consulting. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CollectionViewLayoutDatasource <NSObject>
@required
-(CGSize)customCollectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath;

@end

@interface CollectionViewLayout : UICollectionViewFlowLayout
{
    UICollectionViewScrollDirection scrollDirection;
    CGSize contentSize;
    
    BOOL  lastCellCreated;
}
@property (nonatomic) UICollectionViewScrollDirection scrollDirection;
@property (nonatomic, retain) NSMutableDictionary        *maxHeightInSection;
@property (nonatomic) id<CollectionViewLayoutDatasource> dataSource;

-(UICollectionViewScrollDirection) scrollDirection;
-(void)resetContentSize;
-(CGFloat)getMaxHeightInSection:(NSInteger)section;

@end
