//
//  OrderPageViewController.h
//  taco2
//
//  Created by HARSH on 02/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol editDelegate <NSObject>

-(void)ProductEdited;

@end

@interface OrderPageViewController : UIViewController



@property (strong, nonatomic) IBOutlet UILabel *noItemOutlet;

@property (strong, nonatomic) IBOutlet UIView *totalView;
@property (strong, nonatomic) IBOutlet UILabel *totalCost;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *viewHeight;
- (IBAction)addMore:(id)sender;



@property (assign, nonatomic)  BOOL isEditDone;
@property (nonatomic,strong) NSString *selectedItemName;
@property (nonatomic,strong) NSString *selectedItemICost;
@property (nonatomic,strong) NSString *selectedItemDesc;
@property (nonatomic,strong) NSString *selectedItemQuantity;
@property (nonatomic,strong) NSString *selectedItemTotalPrice;
@property (nonatomic,strong) NSString *selectedItemId;
@property (nonatomic,strong) NSString *strSelectAddress;
@property (nonatomic,strong) NSString *strSelectCity;
@property (nonatomic,strong) NSString *selectedItemSize;
@property (weak, nonatomic) id <editDelegate> delegate;

@end
