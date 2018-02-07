//
//  itemViewController.h
//  pageView
//
//  Created by Gourav Shukla on 16/08/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface itemViewController : UIViewController

{
    NSString *selectedItemImageUrl;
    NSString *selectedItemName;
    NSString *selectedItemICost;
    NSString *selectedItemDesc;
    int selectedIndex;
    BOOL isFromOrderEdit;
}
@property (weak, nonatomic) IBOutlet UIImageView *viewitemImage;
@property (weak, nonatomic) IBOutlet UILabel *viewItemName;
@property (weak, nonatomic) IBOutlet UILabel *viewItemDesc;
@property (weak, nonatomic) IBOutlet UIView *sizeQuantityView;
@property (weak, nonatomic) IBOutlet UIView *ingredientsview;
@property (weak, nonatomic) IBOutlet UIView *extratoppingView;

@property (weak, nonatomic) IBOutlet UITextField *selectQuantity;
@property (strong, nonatomic) IBOutlet UIView *viewMainSub;
@property (strong, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UIImageView *loadingImage;
@property (weak, nonatomic) IBOutlet UILabel *lblselectSize;
@property (weak, nonatomic) IBOutlet UIPickerView *pickerSelectsize;

@property (nonatomic,strong) NSString *selectedItemImageUrl;
@property (nonatomic,strong) NSString *selectedItemName;
@property (nonatomic,strong) NSString *selectedItemICost;
@property (nonatomic,strong) NSString *selectedItemDesc;
@property (nonatomic,strong) NSString *selectedItemId;
@property (nonatomic,strong) NSString *selectedmainMenuNameItem;
@property (nonatomic) int selectedIndex;
@property (nonatomic) BOOL isFromMenu;






@end
