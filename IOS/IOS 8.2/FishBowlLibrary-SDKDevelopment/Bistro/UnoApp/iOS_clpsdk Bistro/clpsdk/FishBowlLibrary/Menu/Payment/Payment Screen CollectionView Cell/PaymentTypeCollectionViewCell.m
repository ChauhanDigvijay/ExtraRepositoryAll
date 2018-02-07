//
//  PaymentTypeCollectionViewCell.m
//  iOS_FBTemplate1
//
//  Created by Sridhar R on 1/4/16.
//  Copyright © 2016 HARSH. All rights reserved.
//

#import "PaymentTypeCollectionViewCell.h"

@implementation PaymentTypeCollectionViewCell

- (void)awakeFromNib {
    // Initialization code
    self.layer.cornerRadius = 5.0f;
    self.layer.borderWidth = 1.0f;
    self.layer.borderColor = [UIColor lightGrayColor].CGColor;
    [self setClipsToBounds:YES];
}

-(void)setImage:(UIImage *)image{
    _contentImageView.image=image;
}



@end
