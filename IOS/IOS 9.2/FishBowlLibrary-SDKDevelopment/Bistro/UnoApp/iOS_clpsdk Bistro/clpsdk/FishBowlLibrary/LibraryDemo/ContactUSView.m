//
//  ContactUSView.m
//  clpsdk
//
//  Created by Gourav Shukla on 30/11/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "ContactUSView.h"
#import "SideMenuObject.h"
#import "ModelClass.h"
#import "HomeViewController.h"
#import "LoyalityView.h"
#import "MenuViewController.h"
#import "StorelocatorViewController.h"
#import "RewardsAndOfferView.h"
#import "UserProfileView.h"
#import "FAQViewViewController.h"
#import "ContactUsCell.h"
#import "ApiClasses.h"
#import "MenuViewController.h"



@interface ContactUSView ()<PushNavigation,UITextViewDelegate>
{
    SideMenuObject   * sideObject;
    ModelClass       * obj;
    UIViewController * myController;
    NSDictionary     * dic;
    NSString         * stringDefaultComment;
    NSMutableArray   * arrayMessage;
    NSMutableArray   * arrayArea;
    NSMutableArray   * arrayMessageID;
    NSMutableArray   * arrayAreaID;
    ApiClasses       * apiCall;
    NSString         * areaID;
    NSString         * messageID;
    BOOL               isAreaType;
    BOOL               isMessageType;
    NSArray * arrStoreNumber;
}
@property (unsafe_unretained, nonatomic) IBOutlet UITextView *messageTextLb;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton   *typeOfMessageBtnOL;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton   *areaBtnOL;
@property (weak, nonatomic) IBOutlet UIScrollView            *scrollview;
@property (weak, nonatomic) IBOutlet UITextField             *writeSubjectTf;
@property (weak, nonatomic) IBOutlet UITableView             *typeOfMessageTbl;
@property (weak, nonatomic) IBOutlet UITableView             *areaTbl;

@end

@implementation ContactUSView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    obj=[ModelClass sharedManager];
    sideObject = [[SideMenuObject alloc]init];
    
    // corner radius
    [self addCornerRadius:self.messageTextLb];
    
    // textView place holder
    stringDefaultComment = @"Please write your message here";
    self.messageTextLb.text = stringDefaultComment;
    
    isAreaType    = YES;
    isMessageType = YES;
    
    // tbl hidden
    [self.areaTbl setHidden:YES];
    [self.typeOfMessageTbl setHidden:YES];
    
    // api call method
    [self getLoyaltyMessageType];
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"storesKey"];
    arrStoreNumber = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSLog(@"arrStoreNumber is %@",arrStoreNumber.description);
    
    
}


// corner radius
-(void)addCornerRadius:(UITextView*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 0.5;
    textField.layer.borderColor = [[UIColor colorWithRed:128.0/255.0f green:128.0/255.0f blue:128.0/255.0f alpha:.3] CGColor];
    
}


#pragma mark - getLoyaltyMessageType Api
-(void)getLoyaltyMessageType
{
    obj = [ModelClass sharedManager];
    [obj addLoadingView:self.view];
    
    apiCall=[ApiClasses sharedManager];
    [apiCall getLoyaltyMessageType:nil url:@"/loyalty/getLoyaltyMessageType" withTarget:self withSelector:@selector(getLoyaltyMessageType:)];
    apiCall = nil;
}

// api response
-(void)getLoyaltyMessageType:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"getLoyaltyMessageType ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        arrayMessage = [response valueForKey:@"loyaltyMessageType"];
        arrayMessageID = [response valueForKey:@"id"];
        [self.typeOfMessageTbl reloadData];
      //  [self getLoyaltyAreaType];
    }
    else
    {
        [self alertViewDelegate:@"No Area Found"];
    }
    
    // api call method
    
}



#pragma mark - getLoyaltyMessageType Api
-(void)getLoyaltyAreaType
{
     obj = [ModelClass sharedManager];
    [obj addLoadingView:self.view];
    
    apiCall=[ApiClasses sharedManager];
    [apiCall getLoyaltyAreaType:nil url:@"/loyalty/getLoyaltyAreaType" withTarget:self withSelector:@selector(getLoyaltyAreaType:)];
    apiCall = nil;
}

// api response
-(void)getLoyaltyAreaType:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"getLoyaltyAreaType ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        arrayArea = [response valueForKey:@"loyaltyAreaType"];
        arrayAreaID = [response valueForKey:@"id"];
        [self.areaTbl reloadData];
    }
    else
    {
        [self alertViewDelegate:@"No Message Found"];
    }
}



#pragma mark - textview Delegate Method

 -(void)textViewDidEndEditing:(UITextView *)textView
{
    
    if ([self.messageTextLb.text isEqualToString:@""]) {
        self.messageTextLb.text=stringDefaultComment;
    }
    
}
 -(void)textViewDidBeginEditing:(UITextView *)textView {
    
    if ([self.messageTextLb.text isEqualToString:stringDefaultComment]) {
        self.messageTextLb.text=@"";
    }
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    
    if ([text isEqualToString:@"\n"]){
        [textView resignFirstResponder];
        return YES;
    }
    else
        return YES;
}

#pragma mark - textField Delegate Method

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField becomeFirstResponder];
    return YES;
}


#pragma mark - didReceiveMemory Method

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

#pragma mark - Side menu button Action
- (IBAction)sideMenuBtn_Action:(id)sender {
    
    sideObject.delegate = self;
    [sideObject SideMenuAction:self.view];
}


#pragma mark -  navigation method Action
-(void)didSelectAtIndexPathRow:(NSIndexPath *)indexPath
{
    if(indexPath.row == 0)
    {
        if([self isViewControllerExist:[HomeViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
    }
    else if(indexPath.row == 1)
    {
        if([self isViewControllerExist:[LoyalityView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            LoyalityView * menuObj = [[LoyalityView alloc]initWithNibName:@"LoyalityView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 2)
    {
        if([self isViewControllerExist:[MenuViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"MenuToStore"];
            MenuViewController * menuObj = [[MenuViewController alloc]initWithNibName:@"MenuViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 3)
    {
        if([self isViewControllerExist:[StorelocatorViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults]setBool:YES forKey:@"MenuToStore"];
            StorelocatorViewController * menuObj = [[StorelocatorViewController alloc]initWithNibName:@"StorelocatorViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 4)
    {
        if([self isViewControllerExist:[RewardsAndOfferView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            RewardsAndOfferView * menuObj = [[RewardsAndOfferView alloc]initWithNibName:@"RewardsAndOfferView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    
//    else if(indexPath.row == 5)
//    {
//        [sideObject removeSideNave];
//        [self alertViewDelegate:@"No Setting Available"];
//    }
    
    else if(indexPath.row == 5)
    {
        if([self isViewControllerExist:[UserProfileView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            UserProfileView * menuObj = [[UserProfileView alloc]initWithNibName:@"UserProfileView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 6)
    {
        if([self isViewControllerExist:[FAQViewViewController class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            FAQViewViewController * menuObj = [[FAQViewViewController alloc]initWithNibName:@"FAQViewViewController" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
    }
    else if(indexPath.row == 7)
    {
         [sideObject removeSideNave];
    }
    
    else
    {
        [sideObject removeSideNave];
    }
}


// check controller exist
-(BOOL)isViewControllerExist:(Class)isController
{
    for (UIViewController *controller in self.navigationController.viewControllers)
    {
        if ([controller isKindOfClass:isController])
        {
            myController = controller;
            return YES;
        }
    }
    return NO;
}



# pragma mark - alert view method

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    
    [alertController addAction:ok];
    
    [self presentViewController:alertController animated:YES completion:nil];
}



#pragma mark - TableView Delegate And DataSources

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView == self.areaTbl)
    {
        return 35;
    }
    else
    {
        return 35;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(tableView == self.areaTbl)
    {
        return [arrStoreNumber count];
    }
    else
    {
        return [arrayMessage count];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if(tableView == self.areaTbl)
    {
        static NSString *CellIdentifier = @"ContactUsCell";
        ContactUsCell *cell = (ContactUsCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil)
        {
            cell = [[[NSBundle mainBundle] loadNibNamed:CellIdentifier owner:self options:nil] objectAtIndex:0];
        }
        cell.textLbl.text = [[arrStoreNumber objectAtIndex:indexPath.row]valueForKey:@"storeName"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        //cell.contentView.backgroundColor = [UIColor lightGrayColor];
        
        return cell;
    }
    else
    {
        static NSString *CellIdentifier = @"ContactUsCell";
        ContactUsCell *cell = (ContactUsCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil)
        {
            cell = [[[NSBundle mainBundle] loadNibNamed:CellIdentifier owner:self options:nil] objectAtIndex:0];
        }
        cell.textLbl.text = [[arrayMessage objectAtIndex:indexPath.row]valueForKey:@"messageType"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
       // cell.contentView.backgroundColor = [UIColor lightGrayColor];
        return cell;
    }
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView == self.areaTbl)
    {
        NSString * areaText = [[arrStoreNumber objectAtIndex:indexPath.row]valueForKey:@"storeName"];
        [self.areaBtnOL setTitle:areaText forState:UIControlStateNormal];
        areaID = areaText; // [[arrStoreNumber objectAtIndex:indexPath.row]valueForKey:@"id"];
        [self.areaTbl setHidden:YES];
    }
    else
    {
        NSString * messageText = [[arrayMessage objectAtIndex:indexPath.row]valueForKey:@"messageType"];
        [self.typeOfMessageBtnOL setTitle:messageText forState:UIControlStateNormal];
        messageID =  messageText; //[[arrayMessage objectAtIndex:indexPath.row]valueForKey:@"id"];
        [self.typeOfMessageTbl setHidden:YES];
    }

}

#pragma mark - Back button Action
- (IBAction)backBtn_Action:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - typeOfMessage button Action
- (IBAction)typeOfMessageBtn_Action:(id)sender
{
    
    if(isMessageType == YES)
    {
    [self.typeOfMessageTbl setHidden:NO];
    [self.typeOfMessageTbl reloadData];
        isMessageType = NO;
    }
    else
    {
        [self.typeOfMessageTbl setHidden:YES];
         isMessageType = YES;
    }
}


#pragma mark - areaBtn button Action
- (IBAction)areaBtn_Action:(id)sender
{
    if(isAreaType == YES)
    {
        [self.areaTbl setHidden:NO];
        [self.areaTbl reloadData];
        isAreaType = NO;
    }
    else
    {
        [self.areaTbl setHidden:YES];
        isAreaType = YES;
    }
  
}



#pragma mark - resetBtn button Action
- (IBAction)resetBtn_Action:(id)sender {
    self.messageTextLb.text = @"";
    self.writeSubjectTf.text = @"";
    [self.areaBtnOL setTitle:@"Select Option" forState:UIControlStateNormal];
    [self.typeOfMessageBtnOL setTitle:@"Select Option" forState:UIControlStateNormal];
}


#pragma mark - message api

-(void)messageApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSMutableDictionary * dict = [NSMutableDictionary new];
    [dict setValue:areaID forKey:@"areaType"];
    [dict setValue:messageID forKey:@"messageType"];
    [dict setValue:self.writeSubjectTf.text forKey:@"subject"];
    [dict setValue:self.messageTextLb.text forKey:@"description"];
    NSString * customerID = [[NSUserDefaults standardUserDefaults]valueForKey:@"customerID"];
    [dict setValue:customerID forKey:@"memberId"];
    
    [apiCall messages:dict url:@"/loyalty/messages" withTarget:self withSelector:@selector(messages:)];
    apiCall = nil;
}

// api response
-(void)messages:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"getLoyaltyAreaType ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        [self alertViewDelegateIndex:@"Message sent successfully"];

    }
    else
    {
        [self alertViewDelegateIndex:[response valueForKey:@"message"]];
    }
}

// alert view method

-(void)alertViewDelegateIndex:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                                                              
                                                              // [self.delegate changePasswordApi];
                                                              [self.navigationController popViewControllerAnimated:YES];
                                                          }];
    [alertController addAction:defaultAction];
    [self presentViewController:alertController animated:YES completion:nil];
}


#pragma mark - submitBtn button Action

- (IBAction)submitBtn_Action:(id)sender {
    
    if(self.writeSubjectTf.text.length == 0)
    {
        [self alertViewDelegate:@"Please enter subject"];
    }
    else if(self.areaBtnOL.titleLabel.text.length ==0)
    {
        [self alertViewDelegate:@"Select type of message"];
    }
    else if(self.typeOfMessageBtnOL.titleLabel.text.length ==0)
    {
        [self alertViewDelegate:@"Select area"];
    }
    else if(self.messageTextLb.text.length ==0)
    {
        [self alertViewDelegate:@"Enter some message"];
    }
    else
    {
        [self messageApi];
    }
    
}

@end
