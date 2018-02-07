//
//  SubMenuViewController.h
//  clpsdk
//
//  Created by surendra pathak on 16/08/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SubMenuViewController : UIViewController
{
    NSMutableArray *subItemArray;
    int mainCatId;
    NSString *mainMenuItemNamelStr;
    NSString *imageUrlStr;
}
@property (nonatomic,strong) NSMutableArray *subItemArray;
@property (nonatomic,strong) NSMutableArray *categoryArray;
@property (nonatomic,assign) int mainCatId;
@property (nonatomic,assign) NSInteger categoryIndexID;
@property (nonatomic,strong) NSString *imageUrlStr;
@property (nonatomic,strong) NSString *mainMenuItemNamelStr;
@property (nonatomic,strong) NSString *categorydescMenu;
@property (nonatomic, strong) UIImage * staticMenuImage;

@end
