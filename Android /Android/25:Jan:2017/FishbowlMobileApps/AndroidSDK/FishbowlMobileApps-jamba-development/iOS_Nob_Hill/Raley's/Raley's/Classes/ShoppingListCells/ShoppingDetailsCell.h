//
//  ShoppingDetailsCell.h
//  Raley's
//
//  Created by VT02 on 5/30/14.
//  Copyright (c) 2014 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol ShoppingDetailsCellDelegate<NSObject>
@required
-(void)deleteProduct:(NSIndexPath*)indexPath;
-(void)MoveToActive:(NSIndexPath *)currentIndex;

@end
@interface ShoppingDetailsCell : UITableViewCell
@property(nonatomic,retain) IBOutlet UILabel *_productName,*_totalItemPrice;
@property(nonatomic,retain) IBOutlet UIImageView *_productImage;
@property(nonatomic,retain) IBOutlet UIScrollView *_scrollView;
@property(nonatomic,retain) IBOutlet UIView *_contentView;
@property(nonatomic,retain) IBOutlet UIButton *_deletebtn;
@property(nonatomic,retain) IBOutlet UIButton *_Move_to_activebtn;
@property(nonatomic,retain) id <ShoppingDetailsCellDelegate> delegate;

-(void)setDeleteButtonEvent:(NSIndexPath*)indexPath;
-(void)SetMove_to_Active:(NSIndexPath*)currentIndex;
@end
