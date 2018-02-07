//
//  SideMenu.h
//  pageControle
//
//  Created by Gourav Shukla on 15/09/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol sideMenu <NSObject>
@optional

-(void)didSelectAtIndexPath:(NSIndexPath *)indexPath;
-(void)logout_Action;


@end

@interface SideMenu : UIView <UITableViewDataSource,UITableViewDelegate,UIGestureRecognizerDelegate>
{
    NSMutableArray * arr;
    NSMutableArray * arrImage;
}

+(SideMenu *)sharedSideMenu;
+(SideMenu *)sharedSideLeftCustomView;
-(void)initializeData;

@property (weak, nonatomic) IBOutlet UITableView *tblView;
@property (nonatomic , assign) id <sideMenu> rightMenuDelegate;
@property (strong, nonatomic) IBOutlet NSMutableArray *arr;

@end
