//
//  PassbookLoader.h
//
//  Created by VT001 on 11/08/14.
//  Copyright (c) 2014 Ashwin Arun. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PassbookLoader : UIView

@property (nonatomic, retain) IBOutlet UIImageView  *loader;
@property (nonatomic, retain) IBOutlet UILabel      *message;
@property (nonatomic, retain) IBOutlet UIView       *bgview;

-(void) startAnimation;
@end
