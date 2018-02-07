//
//  MenuSubItemViewController.h
//  clpsdk
//
//  Created by surendra pathak on 12/01/17.
//  Copyright © 2017 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SubMenuCollectionViewCell.h"
#import "itemViewController.h"
@interface MenuSubItemViewController : UIViewController
{

    NSMutableArray *subItemArray;
    int subCatID;
    int mainCatIdItem;
    NSString *strItemNAme;
   
}
@property (nonatomic,strong) NSString *strItemNAme;

@property (nonatomic,strong) NSMutableArray *categoryArray;
@property (assign) int subCatID;
@property (nonatomic,strong) NSMutableArray *subItemArray;
  @property (nonatomic,assign) int mainCatIdItem;
@end

