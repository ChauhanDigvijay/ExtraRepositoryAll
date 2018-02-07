//
//  CollectionViewLayout.m
//  CollectionViewExample
//
//  Created by Paul Dakessian on 9/6/12.
//  Copyright (c) 2012 Paul Dakessian, CapTech Consulting. All rights reserved.
//

#import "CollectionViewLayout.h"
#define kNavigationBarHeight 44.0f
#define OFFER_GRID_HEADER_HEIGHT 35
#define PADDING 10
#define COLUMNS 2

@implementation CollectionViewLayout
@synthesize maxHeightInSection;
@synthesize dataSource;

-(id)init
{
    self = [super init];
    if (self) {
        self.scrollDirection = UICollectionViewScrollDirectionVertical;
        contentSize = CGSizeZero;
    }
    return self;
}

- (NSArray *)layoutAttributesForElementsInRect:(CGRect)rect {
    rect.size.height *= 1.5;
    NSArray* arr = [super layoutAttributesForElementsInRect:rect];
    for (UICollectionViewLayoutAttributes* atts in arr) {
        if (nil == atts.representedElementKind) {
            NSIndexPath* ip = atts.indexPath;
            atts.frame = [self layoutAttributesForItemAtIndexPath:ip].frame;
        }
    }
    return arr;
}

-(void)resetContentSize{
    contentSize = CGSizeZero;
    maxHeightInSection = nil;
    lastCellCreated = NO;
}

//-(void)updateContentSize:(CGRect)f{
//    CGFloat newHeight = f.origin.y + f.size.height;
//    if(newHeight>contentSize.height){
//        contentSize.height = newHeight;
//        contentSize.height+=PADDING;
//    }
//}

-(void)updateMaxHeightInSection:(NSInteger)section _Frame:(CGRect)frame{
    if(maxHeightInSection==nil){
        maxHeightInSection = [[NSMutableDictionary alloc]init];
    }
    NSString *key = [NSString stringWithFormat:@"%d",(int)section];
    if([maxHeightInSection valueForKey:key]==nil){
        NSNumber *value = [NSNumber numberWithDouble:(frame.origin.y+frame.size.height+PADDING)];
        [maxHeightInSection setValue:value forKey:key];
    }else{
        NSNumber *value = [maxHeightInSection valueForKey:key];
        NSNumber *new_value = [NSNumber numberWithDouble:(frame.origin.y+frame.size.height+PADDING)];
        if(new_value.doubleValue>value.doubleValue){
            [maxHeightInSection setValue:new_value forKey:key];
        }
    }
}

-(CGFloat)getMaxHeightInSection:(NSInteger)section{
    NSString *key = [NSString stringWithFormat:@"%d",(int)--section];
    if([maxHeightInSection valueForKey:key]!=nil){
        NSNumber *value = [maxHeightInSection valueForKey:key];
        return value.floatValue;
    }
    return 0;
}

- (void)prepareLayout {
    @autoreleasepool {
        NSIndexPath *indexPath;
        NSInteger numSections = [self.collectionView numberOfSections];
        CGFloat maxHeight = 0;
        int maxCount=0;
        for(NSInteger section = 0; section < numSections; section++){
            NSInteger numItems = [self.collectionView numberOfItemsInSection:section];
            for(NSInteger item = 0; item < numItems; item++){
                maxCount++;
                indexPath = [NSIndexPath indexPathForItem:item inSection:section];
                
                CGSize size = [dataSource customCollectionView:self.collectionView layout:self sizeForItemAtIndexPath:indexPath];
                maxHeight += size.height;
            }
        }
        
        
        // section
        if(numSections>0){
            maxHeight += ((numSections*self.headerReferenceSize.height));// + (numSections*PADDING) - PADDING);
        }
        
        // cell gap
        maxHeight += ((maxCount/COLUMNS) *PADDING*COLUMNS);
        
        
        contentSize.height = maxHeight + PADDING;
    }
}


-(CGSize)collectionViewContentSize{
    contentSize.width = self.collectionView.frame.size.width;
    if(contentSize.height<self.collectionView.frame.size.height){
        contentSize.height = self.collectionView.frame.size.height+1;
    }
//    else{
//        NSInteger numberofSection = [self.collectionView numberOfSections];
//        CGFloat maxHeight = [self getMaxHeightInSection:numberofSection];
//        if(maxHeight>contentSize.height){
//            contentSize.height = maxHeight;
//        }
//    }
    return contentSize;
}

-(CGFloat)getAllRecordsHeightInCollectionview{
    NSInteger numberofSection = [self.collectionView numberOfSections];
    NSInteger record = 0;
    for (NSInteger i=0; i<numberofSection; i++) {
        record += [self.collectionView numberOfItemsInSection:i];
    }
    return record*self.collectionView.frame.size.height;
}
//
//-(CGFloat)getAllSectionMaxHeight{
//    CGFloat maxHeight = 0.0;
//    
//    return maxHeight;
//}


-(void)updateContentSize:(UICollectionViewLayoutAttributes*)atts{
    return;
    CGRect f = atts.frame;
    CGFloat newHeight = f.origin.y + f.size.height;
//    NSInteger numberofSection = [self.collectionView numberOfSections]-1;
//    NSInteger numberofRows = [self.collectionView numberOfItemsInSection:numberofSection]-1;
    
    CGFloat maxHeight = [self getMaxHeightInSection:atts.indexPath.section];
    if(newHeight>maxHeight){
        contentSize.height = newHeight;
    }else{
        contentSize.height = maxHeight;
    }
    contentSize.height += PADDING;
//    if(atts.indexPath.section==numberofSection && atts.indexPath.row==numberofRows){
//        newHeight = 0;
//        newHeight = [self getMaxHeightInSection:numberofSection +1];
//        contentSize.height = newHeight;
//        contentSize.height += PADDING;
//        lastCellCreated = YES;
//    }else{
//        if(!lastCellCreated){
//        contentSize.height = [self getAllRecordsHeightInCollectionview]; // self.collectionView.frame.size.height * (numberofSection+1);
//        }
//    }
    
    self.collectionView.showsHorizontalScrollIndicator = NO;
    self.collectionView.showsVerticalScrollIndicator = YES;
}



- (UICollectionViewLayoutAttributes *)layoutAttributesForItemAtIndexPath:(NSIndexPath *)indexPath {
    UICollectionViewLayoutAttributes* atts =
    [super layoutAttributesForItemAtIndexPath:indexPath];
    
    int column = COLUMNS;
    
    CGSize size = self.headerReferenceSize;
//    int numberofSection = (int)[self.collectionView numberOfSections];
    
    if(indexPath.section==0){
        if ((indexPath.item+1) <= column){ // degenerate case 1, first item of section
            CGRect f = atts.frame;
            if(indexPath.item>0){
                NSIndexPath* prev =
                [NSIndexPath indexPathForItem:indexPath.item-1 inSection:indexPath.section];
                f.origin.y = [self layoutAttributesForItemAtIndexPath:prev].frame.origin.y;
            }
            else{
                f.origin.y = PADDING + size.height;
            }
            
            atts.frame = f;
            [self updateContentSize:atts];
            [self updateMaxHeightInSection:indexPath.section _Frame:atts.frame];
            return atts;
        }
        
        NSIndexPath* ipPrev =
        [NSIndexPath indexPathForItem:indexPath.item-column inSection:indexPath.section];
        
        CGRect fPrev = [self layoutAttributesForItemAtIndexPath:ipPrev].frame;
        CGFloat rightPrev = fPrev.origin.y + fPrev.size.height + PADDING;
        
        CGRect f = atts.frame;
        f.origin.y = rightPrev;
        atts.frame = f;
        [self updateMaxHeightInSection:indexPath.section _Frame:atts.frame];
        [self updateContentSize:atts];
        return atts;
        
    }else{
        if ((indexPath.item+1) <= column){ // degenerate case 1, first item of section
            CGRect f = atts.frame;
            f.origin.y = [self getMaxHeightInSection:indexPath.section] + size.height + PADDING;
            atts.frame = f;

            [self updateMaxHeightInSection:indexPath.section _Frame:atts.frame];
            [self updateContentSize:atts];
            return atts;
        }
        
        NSIndexPath* ipPrev =
        [NSIndexPath indexPathForItem:indexPath.item-column inSection:indexPath.section];
        
        CGRect fPrev = [self layoutAttributesForItemAtIndexPath:ipPrev].frame;
        CGFloat rightPrev = fPrev.origin.y + fPrev.size.height + PADDING;
        
        CGRect f = atts.frame;
        f.origin.y = rightPrev;
        atts.frame = f;

        [self updateMaxHeightInSection:indexPath.section _Frame:atts.frame];
        [self updateContentSize:atts];
        return atts;
    }
}
-(BOOL)shouldInvalidateLayoutForBoundsChange:(CGRect)newBounds{
    return YES;
}


@end