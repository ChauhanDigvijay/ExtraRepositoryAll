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
    int       selectedIndex;
    BOOL      isFromOrderEdit;
}
@property (weak, nonatomic) IBOutlet UIImageView *viewitemImage;
@property (weak, nonatomic) IBOutlet UILabel *viewItemName;
@property (weak, nonatomic) IBOutlet UILabel *viewItemDesc;
@property (weak, nonatomic) IBOutlet UIView *sizeQuantityView;
@property (weak, nonatomic) IBOutlet UIView *ingredientsview;
@property (weak, nonatomic) IBOutlet UIView *extratoppingView;



@property (strong, nonatomic) IBOutlet UIScrollView *scrollView;


@property (nonatomic,strong) NSString *selectedItemImageUrl;
@property (nonatomic,strong) NSString *selectedItemName;
@property (nonatomic,strong) NSString *selectedItemICost;
@property (nonatomic,strong) NSString *selectedItemDesc;
@property (nonatomic,strong) NSString *selectedItemId;
@property (nonatomic,strong) NSString *selectedmainMenuNameItem;
@property (nonatomic) int              selectedIndex;
@property (nonatomic) BOOL             isFromMenu;
@property (nonatomic,strong) NSMutableArray *subCategoryArray;
@property (nonatomic,assign) NSInteger itemActegoryID ;

@property (nonatomic,assign) NSInteger categoryID ;
@property (nonatomic,assign) NSInteger productID ;
@property (nonatomic,assign) NSInteger subProductID ;





@end
