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
@property (nonatomic)  int mainCatId;
@property (nonatomic,strong)NSString *imageUrlStr;
@property (nonatomic,strong)NSString *mainMenuItemNamelStr;
@end
