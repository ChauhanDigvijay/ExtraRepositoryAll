//
//  RewardsAndOfferView.m
//  clpsdk
//
//  Created by Gourav Shukla on 19/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "RewardsAndOfferView.h"
#import "ModelClass.h"
#import "SideMenuObject.h"
#import "HomeViewController.h"
#import "LoyalityView.h"
#import "MenuViewController.h"
#import "RewardsCell.h"
#import "UserProfileView.h"
#import "StorelocatorViewController.h"
#import "ApiClasses.h"
#import "OfferDetailView.h"
#import "clpsdk.h"
#import "HtmlOffersAndRewards.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import "FAQViewViewController.h"
#import "ContactUSView.h"


@interface RewardsAndOfferView ()<PushNavigation,UITableViewDataSource,UITableViewDelegate>
{
        ModelClass       * obj;
        NSDictionary     * dict;
        SideMenuObject   * sideObject;
        UIViewController * myController;
        ApiClasses       * apiCall;
        NSMutableArray   * arrOffers;
        NSMutableArray   * arrRewards;
        NSMutableArray   * arrRewardsAndOffer;
        BOOL                isReward;
        NSString         * offerTitle;
        NSString         * offerDescription;
        NSString         * expireDate;
        NSString         * offerImageURl;
        clpsdk           * clpObj;
    
}
@property (weak, nonatomic) IBOutlet UILabel     *totalNumberOfferLabel;
@property (weak, nonatomic) IBOutlet UILabel     *totalNumberRewardsLabel;
@property (weak, nonatomic) IBOutlet UIImageView *backgroundImage;
@property (weak, nonatomic) IBOutlet UIView      *rewardsAndOfferView;
@property (weak, nonatomic) IBOutlet UILabel     *alertMessageLbl;


@end

@implementation RewardsAndOfferView

- (void)viewDidLoad {
    [super viewDidLoad];
    
     obj=[ModelClass sharedManager];
     sideObject         = [[SideMenuObject alloc]init];
     arrOffers          = [[NSMutableArray alloc]init];
     arrRewards         = [[NSMutableArray alloc]init];
     arrRewardsAndOffer = [[NSMutableArray alloc]init];
    
    self.tblOffers.delegate = self;
    self.tblOffers.dataSource = self;
    
    
    isReward = YES;
    
    // offer api 
   // [self getCustomerOffersCount];
      [self getRewardsCount];
      [self shadoOffect:self.rewardsAndOfferView];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    // set bottomview frame
    [obj setBottomframe];
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds =  NO;
    shadoView.layer.shadowOffset  =  CGSizeMake(0, 0);
    shadoView.layer.shadowRadius  =  4;
    shadoView.layer.shadowOpacity =  0.5;
}


#pragma mark - Side Menu Action

- (IBAction)sideMenuBtn_Action:(id)sender
{
     sideObject.delegate = self;
     [sideObject SideMenuAction:self.view];
}

#pragma mark - Table View DataSource

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 73;
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(isReward == YES)
    {
        return [arrRewards count];
    }
    else
    {
        return [arrOffers count];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    RewardsCell *cell = [tableView dequeueReusableCellWithIdentifier:@"RewardsCell"];
    if (cell == nil)
    {
        NSArray *array = [[NSBundle mainBundle]loadNibNamed:@"RewardsCell" owner:self options:nil];
        cell = [array objectAtIndex:0];
    }
    
    if(isReward == YES)
    {
        // campine descripttion
        if([[arrRewards objectAtIndex:indexPath.row] valueForKey:@"campaignTitle"]!=(NSString*)[NSNull null])
        {
            cell.lblTitleTbl.text = [[arrRewards objectAtIndex:indexPath.row] valueForKey:@"campaignTitle"];
        }
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        // campine descripttion
        if([[arrRewards objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"]!=(NSString*)[NSNull null])
        {
            cell.lblDescriptionTbl.text = [[arrRewards objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"];
        }
        
        // offer expire date
        if([[arrRewards objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
        {
            NSString *endStr = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
            
            if(endStr.length!=0)
            {
                NSArray *myArray = [endStr componentsSeparatedByString:@" "];
                NSString *end = [myArray objectAtIndex:0];
                
                NSDate *currentDate=[NSDate date];
                NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
                [currentF setDateFormat:@"yyyy-MM-dd"];
                
                NSString *strCurrentDate1=[currentF stringFromDate:currentDate];
                NSDateFormatter *f = [[NSDateFormatter alloc] init];
                
                [f setDateFormat:@"yyyy-MM-dd"];
                
                NSDate *startDate = [f dateFromString:strCurrentDate1];
                NSDate *endDate = [f dateFromString:end];
                
                NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                                    fromDate:startDate
                                                                      toDate:endDate
                                                                     options:NSCalendarWrapComponents];
                NSString *totalDays;
                
                if(components == 0)
                {
                     cell.lblExpireDateTbl.text = @"Never Expire";
                }
                else
                {
                    totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
                    cell.lblExpireDateTbl.text = totalDays;
                }
            }
            else
            {
                    cell.lblExpireDateTbl.text = @"Never Expires";
            }
        }
    }
    
    else
        
    {
    
    // campine descripttion
    if([[arrOffers objectAtIndex:indexPath.row] valueForKey:@"campaignTitle"]!=(NSString*)[NSNull null])
    {
        cell.lblTitleTbl.text = [[arrOffers objectAtIndex:indexPath.row] valueForKey:@"campaignTitle"];
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    

    // campine descripttion
    if([[arrOffers objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"]!=(NSString*)[NSNull null])
    {
        cell.lblDescriptionTbl.text = [[arrOffers objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"];
    }
   
    // offer expire date
    if([[arrOffers objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
    {
        NSString *endStr = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
        
       if(endStr.length!=0)
       {
           NSArray *myArray = [endStr componentsSeparatedByString:@" "];
           NSString *end = [myArray objectAtIndex:0];
           
           NSDate *currentDate=[NSDate date];
           NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
           [currentF setDateFormat:@"yyyy-MM-dd"];
           
           NSString *strCurrentDate1=[currentF stringFromDate:currentDate];
           NSDateFormatter *f = [[NSDateFormatter alloc] init];
           
           [f setDateFormat:@"yyyy-MM-dd"];
           
           NSDate *startDate = [f dateFromString:strCurrentDate1];
           NSDate *endDate = [f dateFromString:end];
           
           NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
           NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                               fromDate:startDate
                                                                 toDate:endDate
                                                                options:NSCalendarWrapComponents];
           NSString *totalDays;
           
           if(components == 0)
           {
               cell.lblExpireDateTbl.text = @"Never Expire";
           }
           else
           {
               totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
               cell.lblExpireDateTbl.text = totalDays;
           }
       }
      else
      {
          cell.lblExpireDateTbl.text = @"Never Expires";
      }
    }
 }
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(isReward == YES)
    {
        if(arrRewards.count!=0)
        {
            if([obj checkNetworkConnection])
            {
                apiCall=[ApiClasses sharedManager];
                
                // customer id
                NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
                NSData *data = [def1 objectForKey:@"MyData"];
                NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
                NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
                NSString * strCustomerID = [dic valueForKey:@"customerID"];
                NSLog(@"customer id---------%@",strCustomerID);
                
                if (arrRewards.count>0)
                {
                    
                    NSString * strChannelID = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"channelTypeID"];
                    
                    if([strChannelID intValue] == 6)
                    {
                        // offerId / campineId
                        NSString * strCampineID = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"campaignId"];
                        // @"16900720"
                        
                        NSString * str = [NSString stringWithFormat:@"/mobile/getPass"];
                        NSMutableDictionary * dicValue = [[NSMutableDictionary alloc]init];
                        [dicValue setValue:strCustomerID forKey:@"customerID"];
                        [dicValue setValue:strCampineID forKey:@"campaignId"];
                        
                        // get offer api
                        [obj addLoadingView:self.view];
                        
                        [apiCall PassOpen:dicValue url:str withTarget:self withSelector:@selector(getPass:)];
                        apiCall = nil;
                        
                    }
                    else  if([strChannelID intValue] == 5)
                    {
                        if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            offerObj.rewardsAndOfferTitle = @"Rewards";
                            offerObj.htmlBody = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        else if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"htmlBody"]!=nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            offerObj.htmlBody = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"htmlBody"];
                            offerObj.bodyType = @"Html";
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        else
                        {
                            
                            if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"promotionCode"]!= nil)
                            {
                            OfferDetailView * offerObj = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
                            
                            offerObj.promoCodevalue    = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"promotionCode"];
                            offerObj.offersTitle       = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"campaignTitle"];
                            offerObj.offersDescription  = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"campaignDescription"];
                            if([[arrRewards objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
                            {
                                
                                NSString *endStr = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
                                
                                if(endStr.length!=0)
                                {
                                    NSArray *myArray = [endStr componentsSeparatedByString:@" "];
                                    NSString *end = [myArray objectAtIndex:0];
                                    
                                    NSDate *currentDate=[NSDate date];
                                    NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
                                    [currentF setDateFormat:@"yyyy-MM-dd"];
                                    NSString *strCurrentDate1=[currentF stringFromDate:currentDate];   NSDateFormatter *f = [[NSDateFormatter alloc] init];
                                    [f setDateFormat:@"yyyy-MM-dd"];
                                    NSDate *startDate = [f dateFromString:strCurrentDate1];
                                    NSDate *endDate = [f dateFromString:end];
                                    
                                    NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                                    NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                                                        fromDate:startDate
                                                                                          toDate:endDate
                                                                                         options:NSCalendarWrapComponents];
                                    
                                    NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
                                    expireDate = totalDays;
                                }
                                
                            }
                            
                            if(expireDate.length!=0)
                            {
                                offerObj.offerExpireDate   = expireDate;
                            }
                            else
                            {
                                offerObj.offerExpireDate   = @"never expire";
                            }
                            [self.navigationController pushViewController:offerObj animated:YES];
                            }
                            else
                            {
                                [self alertViewDelegate:@"Promocode not available"];
                            }
                        }
                    }
                    else  if([strChannelID intValue] == 7)
                    {
                        if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            offerObj.rewardsAndOfferTitle = @"Rewards";
                            offerObj.htmlBody = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        else
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            offerObj.htmlBody = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"htmlBody"];
                            offerObj.bodyType = @"Html";
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                    }
                    else  if([strChannelID intValue] == 1)
                    {
                        
                        
                if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
                        {
                        HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                        offerObj.rewardsAndOfferTitle = @"Rewards";
                         offerObj.htmlBody = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
                    [self.navigationController pushViewController:offerObj animated:YES];
                        }
                      else
                      {
                          NSString * campaignId = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"campaignId"];
                          
                          NSString * url = [NSString stringWithFormat:@"/mobile/getEmailOfferDetail/%@",campaignId];
                          
                          
                          [obj addLoadingView:self.view];
                          apiCall=[ApiClasses sharedManager];
                          [apiCall emailApi:nil url:url withTarget:self withSelector:@selector(emailApi:)];
                          apiCall = nil;
                          
                      }
                        
                        
                       
                        
                       // [self apiEmail];
                        
                        
//                        if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
//                        {
//                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
//                            offerObj.rewardsAndOfferTitle = @"Rewards";
//                            offerObj.htmlBody = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
//                            [self.navigationController pushViewController:offerObj animated:YES];
//                        }
//                        else
//                        {
//                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
//                            
//                            offerObj.bodyType = @"Html";
//                            
//                            offerObj.htmlBody = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"htmlBody"];
//                            
//                            offerObj.rewardsAndOfferTitle = @"Rewards";
//                            
//                            [self.navigationController pushViewController:offerObj animated:YES];
//                        }
                    }
                    else  if([strChannelID intValue] == 3)
                    {
                        if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            offerObj.rewardsAndOfferTitle = @"Rewards";
                            
                            offerObj.htmlBody = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        
                        else if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"notificationContent"]!=nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            NSString * strUrl = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"notificationContent"];
                            
                            if([strUrl containsString:@"http"])
                            {
                                NSArray * arr =  [strUrl componentsSeparatedByString:@"http"];
                                
                                NSString * url = [arr objectAtIndex:1];
                                NSArray * arr2 = [url componentsSeparatedByString:@" "];
                                
                                NSString  * urlString = [arr2 objectAtIndex:0];
                                
                                offerObj.bodyType = @"notificationContent";
                                offerObj.htmlBody = [NSString stringWithFormat:@"http%@",urlString];
                                offerObj.rewardsAndOfferTitle = @"Rewards";
                                [self.navigationController pushViewController:offerObj animated:YES];
                            }
                        }
                        else
                        {
                            if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"promotionCode"]!=nil)
                            {
                            
                            OfferDetailView * offerObj = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
                            
                            offerObj.promoCodevalue    = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"promotionCode"];
                            
                            offerObj.offersTitle       = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"campaignTitle"];
                            
                            offerObj.offersDescription  = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"campaignDescription"];
                            
                            if([[arrRewards objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
                            {
                                
                                NSString *endStr = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
                                
                                if(endStr.length!=0)
                                {
                                    NSArray *myArray = [endStr componentsSeparatedByString:@" "];
                                    NSString *end = [myArray objectAtIndex:0];
                                    
                                    NSDate *currentDate=[NSDate date];
                                    NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
                                    [currentF setDateFormat:@"yyyy-MM-dd"];
                                    NSString *strCurrentDate1=[currentF stringFromDate:currentDate];   NSDateFormatter *f = [[NSDateFormatter alloc] init];
                                    [f setDateFormat:@"yyyy-MM-dd"];
                                    NSDate *startDate = [f dateFromString:strCurrentDate1];
                                    NSDate *endDate = [f dateFromString:end];
                                    
                                    NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                                    NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                                                        fromDate:startDate
                                                                                          toDate:endDate
                                                                                         options:NSCalendarWrapComponents];
                                    
                                    NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
                                    expireDate = totalDays;
                                }
                                
                            }
                            
                            if(expireDate.length!=0)
                            {
                                offerObj.offerExpireDate   = expireDate;
                            }
                            else
                            {
                                offerObj.offerExpireDate   = @"never expire";
                            }
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                            }
                            else
                            {
                                [self alertViewDelegate:@"Promocode not available"];
                            }
                        }
                    }
                    else
                    {
                        if([[arrRewards objectAtIndex:indexPath.row]valueForKey:@"promotionCode"]!=nil)
                        {
                        
                        OfferDetailView * offerObj = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
                        offerObj.promoCodevalue    = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"promotionCode"];
                        offerObj.offersTitle       = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"campaignTitle"];
                        offerObj.offersDescription  = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"campaignDescription"];
                        
                        if([[arrRewards objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
                        {
                            NSString *endStr = [[arrRewards objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
                            
                            if(endStr.length!=0)
                            {
                                NSArray *myArray = [endStr componentsSeparatedByString:@" "];
                                NSString *end = [myArray objectAtIndex:0];
                                
                                NSDate *currentDate=[NSDate date];
                                NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
                                [currentF setDateFormat:@"yyyy-MM-dd"];
                                NSString *strCurrentDate1=[currentF stringFromDate:currentDate];   NSDateFormatter *f = [[NSDateFormatter alloc] init];
                                [f setDateFormat:@"yyyy-MM-dd"];
                                NSDate *startDate = [f dateFromString:strCurrentDate1];
                                NSDate *endDate = [f dateFromString:end];
                                
                                NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                                NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                                                    fromDate:startDate
                                                                                      toDate:endDate
                                                                                     options:NSCalendarWrapComponents];
                                
                                NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
                                expireDate = totalDays;
                            }
                            
                        }
                        
                        if(expireDate.length!=0)
                        {
                            offerObj.offerExpireDate   = expireDate;
                        }
                        else
                        {
                            offerObj.offerExpireDate   = @"never expire";
                        }
                        
                        [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        else
                        {
                            [self alertViewDelegate:@"Promocode not available"];
                        }
                        
                    }
                    
                }
            }
        }
    }
    else
    {
        if(arrOffers.count!=0)
        {
            if([obj checkNetworkConnection])
            {
                apiCall=[ApiClasses sharedManager];
                
                // customer id
                NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
                NSData *data = [def1 objectForKey:@"MyData"];
                NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
                NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
                NSString * strCustomerID = [dic valueForKey:@"customerID"];
                
                NSLog(@"customer id---------%@",strCustomerID);
                
                if (arrOffers.count>0)
                {
                    NSString * strChannelID = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"channelTypeID"];
                    
                   // NSString * strPmOfferlID = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"isPMOffer"];
                    
                    if([strChannelID intValue] == 6)
                    {
                        // offerId / campineId
                        NSString * strCampineID = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignId"];
                        // @"16900720"
                        
                        NSString * str = [NSString stringWithFormat:@"/mobile/getPass"];
                        NSMutableDictionary * dicValue = [[NSMutableDictionary alloc]init];
                        [dicValue setValue:strCustomerID forKey:@"customerID"];
                        [dicValue setValue:strCampineID forKey:@"campaignId"];
                        
                        // get offer api
                        
                        [obj addLoadingView:self.view];
                        
                        [apiCall PassOpen:dicValue url:str withTarget:self withSelector:@selector(getPass:)];
                        apiCall = nil;
                    }
                    else if([strChannelID intValue] == 1)
                    {
                        
                        
                    if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
                        {
                                                    HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                        
                                                    offerObj.rewardsAndOfferTitle = @"Offers";
                        
                                                    offerObj.htmlBody = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
                        
                                                    [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        
                        else
                        {
                        
                        NSString * campaignId = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignId"];
                        
                        NSString * url = [NSString stringWithFormat:@"/mobile/getEmailOfferDetail/%@",campaignId];
                       
                        
                        [obj addLoadingView:self.view];
                        apiCall=[ApiClasses sharedManager];
                        [apiCall emailApi:nil url:url withTarget:self withSelector:@selector(emailApi:)];
                        apiCall = nil;
                        }
                        
                       // [self apiEmail];
//                        if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
//                        {
//                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
//                            
//                            offerObj.rewardsAndOfferTitle = @"Offers";
//                            
//                            offerObj.htmlBody = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
//                            
//                            [self.navigationController pushViewController:offerObj animated:YES];
//                        }
//                        else
//                        {
//                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
//                            
//                            offerObj.htmlBody = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"htmlBody"];
//                            offerObj.rewardsAndOfferTitle = @"Offers";
//                            offerObj.bodyType = @"Html";
//                            
//                            [self.navigationController pushViewController:offerObj animated:YES];
//                        }
                    }
                    else if([strChannelID intValue] == 7)
                    {
                        if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            offerObj.rewardsAndOfferTitle = @"Offers";
                            
                            offerObj.htmlBody = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        else
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            offerObj.bodyType = @"Html";
                            offerObj.htmlBody = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"htmlBody"];
                            offerObj.rewardsAndOfferTitle = @"Offers";
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                    }
                    else  if([strChannelID intValue] == 5)
                    {
                        // check couponURL
                        if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            offerObj.rewardsAndOfferTitle = @"Offers";
                            
                            offerObj.htmlBody = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                     else if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"notificationContent"]!= nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            offerObj.bodyType = @"notificationContent";
                            
                            NSString * str = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"notificationContent"];
                            
                            
                            if([str containsString:@"http"])
                            {
                                NSArray * arr1 = [str componentsSeparatedByString:@"http"];
                                
                                NSString * url = [arr1 objectAtIndex:1];
                                
                                NSArray * arr2 = [url componentsSeparatedByString:@" "];
                                
                                NSLog(@"str value ------- %@",arr2);
                                
                                offerObj.htmlBody = [NSString stringWithFormat:@"http%@",[arr2 objectAtIndex:0]];
                                
                                offerObj.rewardsAndOfferTitle = @"Offers";
                                
                                [self.navigationController pushViewController:offerObj animated:YES];
                            }
                            else
                            {
                                if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"promotionCode"]!=nil)
                                {
                                    OfferDetailView * offerObj = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
                                    
                                    offerObj.promoCodevalue    = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"promotionCode"];
                                    offerObj.offersTitle       = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignTitle"];
                                    offerObj.offersDescription  = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignDescription"];
                                    
                                    if([[arrOffers objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
                                    {
                                        
                                        NSString *endStr = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
                                        
                                        if(endStr.length!=0)
                                        {
                                            NSArray *myArray = [endStr componentsSeparatedByString:@" "];
                                            NSString *end = [myArray objectAtIndex:0];
                                            
                                            NSDate *currentDate=[NSDate date];
                                            NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
                                            [currentF setDateFormat:@"yyyy-MM-dd"];
                                            NSString *strCurrentDate1=[currentF stringFromDate:currentDate];   NSDateFormatter *f = [[NSDateFormatter alloc] init];
                                            [f setDateFormat:@"yyyy-MM-dd"];
                                            NSDate *startDate = [f dateFromString:strCurrentDate1];
                                            NSDate *endDate = [f dateFromString:end];
                                            
                                            NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                                            NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                                                                fromDate:startDate
                                                                                                  toDate:endDate
                                                                                                 options:NSCalendarWrapComponents];
                                            
                                            NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
                                            expireDate = totalDays;
                                        }
                                        
                                    }
                                    
                                    if(expireDate.length!=0)
                                    {
                                        offerObj.offerExpireDate   = expireDate;
                                    }
                                    else
                                    {
                                        offerObj.offerExpireDate   = @"never expire";
                                    }
                                    [self.navigationController pushViewController:offerObj animated:YES];
                                    
                                }
                                else
                                {
                                    [self alertViewDelegate:@"Promocode not available"];
                                }

                            }
                        }
                        else
                        {
                            
                            if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"promotionCode"]!=nil)
                            {
                                OfferDetailView * offerObj = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
                                
                                offerObj.promoCodevalue    = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"promotionCode"];
                                offerObj.offersTitle       = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignTitle"];
                                offerObj.offersDescription  = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignDescription"];
                                
                                if([[arrOffers objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
                                {
                                    
                                    NSString *endStr = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
                                    
                                    if(endStr.length!=0)
                                    {
                                        NSArray *myArray = [endStr componentsSeparatedByString:@" "];
                                        NSString *end = [myArray objectAtIndex:0];
                                        
                                        NSDate *currentDate=[NSDate date];
                                        NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
                                        [currentF setDateFormat:@"yyyy-MM-dd"];
                                        NSString *strCurrentDate1=[currentF stringFromDate:currentDate];   NSDateFormatter *f = [[NSDateFormatter alloc] init];
                                        [f setDateFormat:@"yyyy-MM-dd"];
                                        NSDate *startDate = [f dateFromString:strCurrentDate1];
                                        NSDate *endDate = [f dateFromString:end];
                                        
                                        NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                                        NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                                                            fromDate:startDate
                                                                                              toDate:endDate
                                                                                             options:NSCalendarWrapComponents];
                                        
                                        NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
                                        expireDate = totalDays;
                                    }
                                    
                                }
                                
                                if(expireDate.length!=0)
                                {
                                    offerObj.offerExpireDate   = expireDate;
                                }
                                else
                                {
                                    offerObj.offerExpireDate   = @"never expire";
                                }
                                [self.navigationController pushViewController:offerObj animated:YES];

                            }
                            else
                            {
                                [self alertViewDelegate:@"Promocode not available"];
                            }
                            
                    }
                        
                    }
                    
                    else  if([strChannelID intValue] == 3)
                    {
                        
                        if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"]!= nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            offerObj.rewardsAndOfferTitle = @"Offers";
                            
                            offerObj.htmlBody = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"couponURL"];
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        // check notificationContent
                        else if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"notificationContent"]!= nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            offerObj.bodyType = @"notificationContent";
                            
                            NSString * str = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"notificationContent"];
                            
                            
                            if([str containsString:@"http"])
                            {
                                NSArray * arr1 = [str componentsSeparatedByString:@"http"];
                                
                                NSString * url = [arr1 objectAtIndex:1];
                                
                                NSArray * arr2 = [url componentsSeparatedByString:@" "];
                                
                                NSLog(@"str value ------- %@",arr2);
                                
                                offerObj.htmlBody = [NSString stringWithFormat:@"http%@",[arr2 objectAtIndex:0]];
                                
                                offerObj.rewardsAndOfferTitle = @"Offers";
                                
                                [self.navigationController pushViewController:offerObj animated:YES];
                            }
                            else
                            {
                                
                            }
                        }
                        // check htmlBody
                        else if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"htmlBody"]!= nil)
                        {
                            HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
                            
                            offerObj.bodyType = @"Html";
                            
                            offerObj.htmlBody = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"notificationContent"];
                            
                            offerObj.rewardsAndOfferTitle = @"Offers";
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        
                        else if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"promotionCode"]!= nil)
                        {
                            
                            OfferDetailView * offerObj = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
                            
                            offerObj.promoCodevalue    = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"promotionCode"];
                            offerObj.offersTitle       = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignTitle"];
                            offerObj.offersDescription  = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignDescription"];
                            if([[arrRewards objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
                            {
                                
                                NSString *endStr = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
                                
                                if(endStr.length!=0)
                                {
                                    NSArray *myArray = [endStr componentsSeparatedByString:@" "];
                                    NSString *end = [myArray objectAtIndex:0];
                                    
                                    NSDate *currentDate=[NSDate date];
                                    NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
                                    [currentF setDateFormat:@"yyyy-MM-dd"];
                                    NSString *strCurrentDate1=[currentF stringFromDate:currentDate];   NSDateFormatter *f = [[NSDateFormatter alloc] init];
                                    [f setDateFormat:@"yyyy-MM-dd"];
                                    NSDate *startDate = [f dateFromString:strCurrentDate1];
                                    NSDate *endDate = [f dateFromString:end];
                                    
                                    NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                                    NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                                                        fromDate:startDate
                                                                                          toDate:endDate
                                                                                         options:NSCalendarWrapComponents];
                                    
                                    NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
                                    expireDate = totalDays;
                                }
                                
                            }
                            
                            if(expireDate.length!=0)
                            {
                                offerObj.offerExpireDate   = expireDate;
                            }
                            else
                            {
                                offerObj.offerExpireDate   = @"never expire";
                            }
                            
                            [self.navigationController pushViewController:offerObj animated:YES];
                            
                        }
                        else
                        {
                            [self alertViewDelegate:@"Promocode not available"];
                        }
                        
                    }
                    else
                    {
                        
                        if([[arrOffers objectAtIndex:indexPath.row]valueForKey:@"promotionCode"]!=nil)
                        {
                            OfferDetailView * offerObj = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
                            
                            offerObj.promoCodevalue    = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"promotionCode"];
                            offerObj.offersTitle       = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignTitle"];
                            offerObj.offersDescription  = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"campaignDescription"];
                            
                            if([[arrOffers objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
                            {
                                
                                NSString *endStr = [[arrOffers objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
                                
                                if(endStr.length!=0)
                                {
                                    NSArray *myArray = [endStr componentsSeparatedByString:@" "];
                                    NSString *end = [myArray objectAtIndex:0];
                                    
                                    NSDate *currentDate=[NSDate date];
                                    NSDateFormatter *currentF = [[NSDateFormatter alloc] init];
                                    [currentF setDateFormat:@"yyyy-MM-dd"];
                                    NSString *strCurrentDate1=[currentF stringFromDate:currentDate];   NSDateFormatter *f = [[NSDateFormatter alloc] init];
                                    [f setDateFormat:@"yyyy-MM-dd"];
                                    NSDate *startDate = [f dateFromString:strCurrentDate1];
                                    NSDate *endDate = [f dateFromString:end];
                                    
                                    NSCalendar *gregorianCalendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
                                    NSDateComponents *components = [gregorianCalendar components:NSCalendarUnitDay
                                                                                        fromDate:startDate
                                                                                          toDate:endDate
                                                                                         options:NSCalendarWrapComponents];
                                    
                                    NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
                                    expireDate = totalDays;
                                }
                                
                            }
                            
                            if(expireDate.length!=0)
                            {
                                offerObj.offerExpireDate   = expireDate;
                            }
                            else
                            {
                                offerObj.offerExpireDate   = @"never expire";
                            }
                            
                        [self.navigationController pushViewController:offerObj animated:YES];
                        }
                        else
                        {
                            [self alertViewDelegate:@"Promocode not available"];
                        }
                    }
                    
                }
            }
        }
        
    }
    
}


-(void)apiEmail
{
    
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall emailApi:nil url:@"/mobile/getEmailOfferDetail/4807" withTarget:self withSelector:@selector(emailApi:)];
    apiCall = nil;
}


-(void)emailApi:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"getLoyaltyMessageType ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]integerValue] == 1)
    {
        HtmlOffersAndRewards * offerObj = [[HtmlOffersAndRewards alloc]initWithNibName:@"HtmlOffersAndRewards" bundle:nil];
        
        offerObj.bodyType = @"Html";
        
        offerObj.htmlBody = [response valueForKey:@"preview"];
        
        offerObj.rewardsAndOfferTitle = @"Offers";
        
        [self.navigationController pushViewController:offerObj animated:YES];
     
    }
    else
    {
        [self alertViewDelegate:@"No preview available"];
    }
    
}



-(void)getPass:(id)response
{
    [obj removeLoadingView:self.view];
    [obj RemoveBottomView];
    NSLog(@"response pass success---------%@",response);
    
    clpsdk * obj22 = [[clpsdk alloc]init];
    [obj22 openPassbookAndShowwithData:response];
    
}


// promocode api response

-(void)getPromocodeResponse:(id)response
{
    [obj removeLoadingView:self.view];
    
    if([[response valueForKey:@"successFlag"]intValue]==1 && ![[response valueForKey:@"promoCode"]isEqualToString:@"DEFAULT"])
    {
        OfferDetailView * offerObj = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
        
        //offerObj.loginImageURl     = imgPromoLogo;
        offerObj.promoCodevalue    = [response valueForKey:@"promoCode"];
        offerObj.offersTitle       = offerTitle;
        offerObj.offersDescription = offerDescription;
        offerObj.offerExpireDate   = expireDate;
        offerObj.offerImageIcon    = offerImageURl;
        [self.navigationController pushViewController:offerObj animated:YES];
    }
    else
    {
        [self alertViewDelegate:@"No promocode available"];
    }
    
    NSLog(@"promocode response ----- %@", response);
}

// alert view method

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
}


# pragma mark - Offers Api

-(void)getCustomerOffersCount
{
    //[obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerID = [dic valueForKey:@"customerID"];
    NSLog(@"customer id---------%@",strCustomerID);
    NSString * str = [NSString stringWithFormat:@"/mobile/getoffers/%@/0",strCustomerID];
    
    [apiCall getOffers:nil url:str withTarget:self withSelector:@selector(getOffersCount:)];
    apiCall = nil;
}

// getoffers api response

-(void)getOffersCount:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"get offers response---------%@",response);
    
    if(arrOffers.count>0)
    {
        [arrOffers removeAllObjects];
       
    }
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        NSArray * arr = (NSArray *)[response valueForKey:@"inAppOfferList"];
        
        [arrOffers addObjectsFromArray:arr];
        
        if(arrOffers.count!=0)
        {
        self.totalNumberOfferLabel.text = [NSString stringWithFormat:@"%lu",(unsigned long)arrOffers.count];
            
            //[self.tblOffers reloadData];
        }
        else
        {
            self.totalNumberOfferLabel.text = [NSString stringWithFormat:@"%lu",(unsigned long)0];
        }
    }
    else
    {
        
        self.totalNumberOfferLabel.text = [NSString stringWithFormat:@"%lu",(unsigned long)0];
    }
    

}


-(void)getRewardsCount
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerID = [dic valueForKey:@"customerID"];
    NSLog(@"customer id---------%@",strCustomerID);
    NSString * str = [NSString stringWithFormat:@"/mobile/getrewards/%@/0",strCustomerID];
    
    [apiCall getRewards:nil url:str withTarget:self withSelector:@selector(getRewardsCount:)];
    apiCall = nil;
}

// getoffers api response

-(void)getRewardsCount:(id)response
{
    //[obj removeLoadingView:self.view];
    
    NSLog(@"get getrewards response%@",response);
    
    if(arrRewards.count>0)
    {
        [arrRewards removeAllObjects];
    }
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        NSArray * arr = (NSArray *)[response valueForKey:@"inAppOfferList"];
        
        [arrRewards addObjectsFromArray:arr];
   
       
  
        if(arrRewards.count!=0)
        {
            [self.tblOffers setHidden:NO];
            [self.tblOffers reloadData];
            self.totalNumberRewardsLabel.text = [NSString stringWithFormat:@"%lu",(unsigned long)arrRewards.count];
        }
        else
        {
            [self.tblOffers setHidden:YES];
            self.totalNumberRewardsLabel.text = [NSString stringWithFormat:@"%lu",(unsigned long)0];
            
        }
    }
    else
    {
        [self.tblOffers setHidden:YES];
        self.totalNumberRewardsLabel.text = [NSString stringWithFormat:@"%lu",(unsigned long)0];
        self.alertMessageLbl.text = @"No Rewards Available";
        //[self alertViewDelegate:@"No Rewards Available"];
    }
    
    [self getCustomerOffersCount];
}


// delegate method

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
        [sideObject removeSideNave];
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
        if([self isViewControllerExist:[ContactUSView class]])
        {
            [self.navigationController popToViewController:myController animated:NO];
        }
        else
        {
            ContactUSView * menuObj = [[ContactUSView alloc]initWithNibName:@"ContactUSView" bundle:nil];
            [self.navigationController pushViewController:menuObj animated:YES];
        }
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

#pragma mark - Rewards and Offer Action

- (IBAction)rewardsAndOfferBtn_Action:(id)sender {
    
    UIButton * btn  = (UIButton *)sender;
    if(btn.tag == 1)
    {
        [(UIView*)[self.view viewWithTag:11]setBackgroundColor:[UIColor colorWithRed:177.0/255.0 green:0.0/255.0 blue:3.0/255 alpha:1.0]];
        [(UIView*)[self.view viewWithTag:22]setBackgroundColor:[UIColor colorWithRed:208.0/255.0 green:26.0/255.0 blue:42.0/255 alpha:1.0]];

        isReward = YES;
   
        if(arrRewards.count == 0)
         {
              [self.tblOffers setHidden:YES];
              self.alertMessageLbl.text = @"No Rewards Available";
              //[self alertViewDelegate:@"No Rewards Available"];
         }
        else
         {
              [self.tblOffers setHidden:NO];
              [self.tblOffers reloadData];
         }
      }
    else
    {
        [(UIView*)[self.view viewWithTag:11]setBackgroundColor:[UIColor colorWithRed:208.0/255.0 green:26.0/255.0 blue:42.0/255 alpha:1.0]];
        [(UIView*)[self.view viewWithTag:22]setBackgroundColor:[UIColor colorWithRed:177.0/255.0 green:0.0/255.0 blue:3.0/255 alpha:1.0]];
 
        isReward = NO;
        
        if(arrOffers.count == 0)
        {
               [self.tblOffers setHidden:YES];
              // [self alertViewDelegate:@"No Offers Available"];
               self.alertMessageLbl.text = @"No Offers Available";
        }
        else
        {
               [self.tblOffers setHidden:NO];
               [self.tblOffers reloadData];
        }
    }
}

#pragma mark - BackButto Action

- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - Memory Warning

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}


// logout button Action
-(void)logOutBt_Action
{
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:@"Do you want to Logout."
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                                  FBSDKLoginManager *loginManager = [[FBSDKLoginManager alloc] init];
                                  [loginManager logOut];
                                  [obj RemoveBottomView];
                                  [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"changePassword"];
                                  
                                  // clp mobile setting api
                                  clpObj = [clpsdk sharedInstanceWithAPIKey];
                                  
                                  // event tracking method
                                  NSMutableDictionary * eventDic = [NSMutableDictionary new];
                                  NSString * userName = [[NSUserDefaults standardUserDefaults]valueForKey:@"userName"];
                                  [eventDic setValue:@"LOGOUT" forKey:@"event_name"];
                                  [eventDic setValue:userName forKey:@"item_name"];
                                  [clpObj updateAppEvent:eventDic];
                                  eventDic = nil;
                                  
                                  [self.navigationController popToRootViewControllerAnimated:YES];
                                  [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"login"];
                              }];
    
    [alert addAction:cancel1];
    UIAlertAction* noButton = [UIAlertAction
                               actionWithTitle:@"NO"
                               style:UIAlertActionStyleDefault
                               handler:^(UIAlertAction * action)
                               {
                                   //Handel your yes please button action here
                                   [alert dismissViewControllerAnimated:YES completion:nil];
                                   
                               }];
    
    [alert addAction:noButton];
    [self presentViewController:alert animated:YES completion:nil];
    
}

@end
