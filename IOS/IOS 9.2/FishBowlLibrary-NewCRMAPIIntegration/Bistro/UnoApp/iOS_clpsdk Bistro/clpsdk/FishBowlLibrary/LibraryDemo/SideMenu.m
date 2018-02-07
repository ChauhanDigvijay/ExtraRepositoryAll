//
//  SideMenu.m
//  pageControle
//
//  Created by Gourav Shukla on 15/09/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "SideMenu.h"
#import "SideMenuCell.h"
#import "MenuViewController.h"

@implementation SideMenu
@synthesize  arr;



+(SideMenu *)sharedSideMenu
{
    static SideMenu *sharedMyManager = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedMyManager = [SideMenu sharedSideLeftCustomView];
        
    });
    return sharedMyManager;
}

-(void)initializeData{
    

arr = [[NSMutableArray alloc]initWithObjects:@"Home",@"My Loyalty",@"Menu",@"Location",@"Rewards and Offers",@"My Profile",@"FAQ",@"Contact Us",nil];
    
arrImage = [[NSMutableArray alloc]initWithObjects:[UIImage imageNamed:@"home@3x.png"],[UIImage imageNamed:@"reward@3x.png"],[UIImage imageNamed:@"menu1@3x.png"],[UIImage imageNamed:@"maplocation@3x.png"],[UIImage imageNamed:@"gift@3x.png"],[UIImage imageNamed:@"profile@3x.png"],[UIImage imageNamed:@"faq@3x.png"],[UIImage imageNamed:@"ContactUS@3x.png"],nil];
}

+(SideMenu *)sharedSideLeftCustomView
{
    SideMenu * sideObj = [[[NSBundle mainBundle]loadNibNamed:@"SideMenu" owner:self options:nil]lastObject];
     sideObj.frame = CGRectMake(sideObj.frame.size.width, 0, sideObj.frame.size.width, sideObj.frame.size.height);
    if(sideObj != nil || [sideObj isKindOfClass:[SideMenu class]]){
       
   return sideObj;
    }else{
        return nil;
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
     return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenWidth = screenRect.size.width;
    CGFloat screenHeight = screenRect.size.height;
    if(screenWidth == 320 && screenHeight == 480)
    {
       return 55;
    }
    else
    {
       return 65;
       
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
        return [arr count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"SideMenuCell";
    
    SideMenuCell *cell = [tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"SideMenuCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    cell.lblText.text  = [arr objectAtIndex:indexPath.row];
    cell.imgView.image = [arrImage objectAtIndex:indexPath.row];
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.rightMenuDelegate didSelectAtIndexPath:indexPath];
    NSLog(@"table view class cell -------- %@",[arr objectAtIndex:indexPath.row]);
    
}

- (IBAction)signOutBtn_Action:(id)sender
{
    [self.rightMenuDelegate logout_Action];
}


@end
