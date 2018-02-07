//
//  ProductCategoryButton.h
//  ProductCategoryTest
//
//  Created by Bill Lewis on 11/2/13.
//  Copyright (c) 2013 Bill Lewis. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "ProductCategory.h"

@interface ProductCategoryButton : UIButton
{
    AppDelegate  *_app;
    UIImage      *_minusImage;
    UIImage      *_plusImage;
    UIImage      *_goImage;
    UIImageView  *_expandedStateImageView;
}

@property (nonatomic, assign)BOOL _expanded;
@property (nonatomic, strong)ProductCategory *_productCategory;

- (id)initWithFrame:(CGRect)frame productCategory:productCategory;
- (void)setExpandedState:(BOOL)expanded;
- (void)toggleExpandedState;

@end
