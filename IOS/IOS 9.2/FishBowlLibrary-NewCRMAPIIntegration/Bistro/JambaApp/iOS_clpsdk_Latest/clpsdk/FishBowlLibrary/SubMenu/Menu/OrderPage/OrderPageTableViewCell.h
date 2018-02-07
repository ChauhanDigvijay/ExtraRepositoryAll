//
//  OrderPageTableViewCell.h
//  taco2
//
//  Created by HARSH on 02/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OrderPageViewController.h"
@protocol OrderPageCellDelegate <NSObject>

@optional

// Delegate optional methods

-(void)removeOrderClicked:(id)sender;
-(void)editOrderClicked:(id)sender;

@end
@interface OrderPageTableViewCell : UITableViewCell<UITextFieldDelegate>
{
    OrderPageViewController *something;
}
@property (nonatomic,strong) OrderPageViewController *something;
@property (strong, nonatomic) IBOutlet UILabel *itemSize;

@property (weak, nonatomic) IBOutlet UILabel *orderCounting;
- (IBAction)editOrderAction:(id)sender;
- (IBAction)removeOrderAction:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *singleItemCost;

@property (weak, nonatomic) IBOutlet UITextField *quantityText;

- (IBAction)doneEditing:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *itemOrderNmae;

@property (weak, nonatomic) IBOutlet UILabel *price;
@property (weak, nonatomic) IBOutlet UILabel *itemDesc;

+(OrderPageTableViewCell*)orderPageTableViewCell;
@property (nonatomic, assign) id<OrderPageCellDelegate>orderpagecellDelegate;
@end
