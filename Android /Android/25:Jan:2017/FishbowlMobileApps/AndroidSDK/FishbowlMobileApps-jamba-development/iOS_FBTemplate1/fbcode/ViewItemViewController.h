//
//  ViewItemViewController.h
//  iOS_FBTemplate1
//
//  Created by HARSH on 08/01/16.
//  Copyright © 2016 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewItemViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *viewitemImage;
@property (weak, nonatomic) IBOutlet UILabel *viewItemName;
@property (weak, nonatomic) IBOutlet UILabel *viewItemDesc;
@property (weak, nonatomic) IBOutlet UIView *sizeQuantityView;
@property (weak, nonatomic) IBOutlet UIView *ingredientsview;
@property (weak, nonatomic) IBOutlet UIView *extratoppingView;
@property (weak, nonatomic) IBOutlet UITextField *selectSize;
@property (weak, nonatomic) IBOutlet UITextField *selectQuantity;
@property (strong, nonatomic) IBOutlet UIView *viewMainSub;
@property (strong, nonatomic) IBOutlet UIScrollView *scrollView;

@end
