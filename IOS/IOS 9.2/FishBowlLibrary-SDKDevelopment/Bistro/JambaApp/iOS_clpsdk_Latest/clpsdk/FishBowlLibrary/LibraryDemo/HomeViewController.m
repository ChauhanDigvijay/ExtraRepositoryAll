//
//  HomeViewController.m
//  LibraryDemo
//
//  Created by Gourav Shukla on 19/05/16.
//  Copyright © 2016 Gourav Shukla. All rights reserved.
//

#import "HomeViewController.h"
//#import "MBCircularProgressBarView.h"
//#import "MBCircularProgressBarLayer.h"
#import "collectionCell.h"
#import "ModelClass.h"
#import "ApiClasses.h"
#import "clpsdk.h"
#import "OfferDetailView.h"
#import "OfferCell.h"
#import "HomeCellTable.h"
#import "UserProfileView.h"
#import "MenuTableViewCell.h"
#import "MenuViewController.h"
@interface HomeViewController ()<UIScrollViewDelegate,UICollectionViewDataSource,UICollectionViewDelegate,UITableViewDelegate,UITableViewDataSource>
{
    NSMutableArray * arrImageBanner;
    NSMutableArray * arrOffer;
    int i;
    ModelClass * obj;
    ApiClasses * apiCall;
    BOOL IsApiSuccess;
    NSString * imgPromoLogo;
    NSString * offerTitle;
    NSString * offerDescription;
    NSString * expireDate;
    NSString * offerImageURl;
}
@property (weak, nonatomic) IBOutlet UIImageView *headerImage;
@property (weak, nonatomic) IBOutlet UILabel *totalNumbersOrderLbl;
@property (weak, nonatomic) IBOutlet UIImageView *bagroundImage;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionOfferCell;
@property (weak, nonatomic) IBOutlet UIImageView *companyLogoImage;
@property (weak, nonatomic) IBOutlet UITableView *tblView;

//@property (weak, nonatomic) IBOutlet MBCircularProgressBarView *progressBar;

@property (weak, nonatomic) IBOutlet UIImageView *imagePage;
@property (weak, nonatomic) IBOutlet UIPageControl *pageControl;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;

@property (weak, nonatomic) IBOutlet UIView *imageViewTopLogo;
@property (weak, nonatomic) IBOutlet UILabel *phoneNumberLbl;
@property (weak, nonatomic) IBOutlet UILabel *emailTF;

@end

static NSString * const kCellReuseIdentifier = @"customCell";
static NSString * const kOfferCellReuseIdentifier = @"OfferCell";


@implementation HomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"SignUp"];

    
    // notification call
//[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(pushNotificationReceived) name:@"pushNotification" object:nil];
    
    
      obj=[ModelClass sharedManager];
      arrImageBanner = [[NSMutableArray alloc]init];
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    [self.imagePage setHidden:YES];
    
    //button corner radius
    self.specialOfferBtn.layer.masksToBounds = YES;
    self.specialOfferBtn.layer.cornerRadius = 2.0;
    self.specialOfferBtn.layer.borderWidth = 1.0;
    self.specialOfferBtn.layer.borderColor = [[UIColor clearColor] CGColor];
    
    self.reedemrewardsBtn.layer.masksToBounds = YES;
    self.reedemrewardsBtn.layer.cornerRadius = 2.0;
    self.reedemrewardsBtn.layer.borderWidth = 1.0;
    self.reedemrewardsBtn.layer.borderColor = [[UIColor clearColor] CGColor];
    
    
    // custom progressbar
//    self.progressBar.backgroundColor=[UIColor clearColor];
//    self.progressBar.progressColor=[UIColor colorWithRed:0.886 green:0 blue:0.133 alpha:1];
//    self.progressBar.progressStrokeColor=[UIColor clearColor];
    
    // image array
    
   
    
//    self.imageViewTopLogo.layer.masksToBounds = YES;
//    self.imageViewTopLogo.layer.cornerRadius = 2.0;
//    self.imageViewTopLogo.layer.borderWidth = 2.0;
//    self.imageViewTopLogo.layer.borderColor = [[UIColor colorWithRed:252.0/255.0f green:228.0/255.0f blue:189.0/255.0f alpha:1] CGColor];
    
 
    NSString * str4 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginLeftSideImageUrl"]];
    NSURL *url4 = [NSURL URLWithString:
                   str4];
    [self.imagePage sd_setImageWithURL:url4 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    //self.imagePage.image = [arrImageBanner objectAtIndex:0];
        
    i=0;
    
    // timer method call
    //[self changeBannerAutomatically];
    
    // custom collection view class
    [self.collectionView registerNib:[UINib nibWithNibName:@"collectionCell" bundle:nil] forCellWithReuseIdentifier:kCellReuseIdentifier];
    self.collectionView.backgroundColor = [UIColor clearColor];
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    [flowLayout setItemSize:CGSizeMake(self.collectionView.frame.size.width, self.collectionView.frame.size.height)];

    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionHorizontal];
    
    [self.collectionView setCollectionViewLayout:flowLayout];
    [self.collectionView setAllowsSelection:YES];
    
  
    
    // offer collection view class
    [self.collectionOfferCell registerNib:[UINib nibWithNibName:@"OfferCell" bundle:nil] forCellWithReuseIdentifier:kOfferCellReuseIdentifier];
    self.collectionOfferCell.backgroundColor = [UIColor clearColor];
    
    UICollectionViewFlowLayout *flowLayout1 = [[UICollectionViewFlowLayout alloc] init];
    [flowLayout1 setItemSize:CGSizeMake(self.collectionOfferCell.frame.size.width, self.collectionOfferCell.frame.size.height)];
    
    [flowLayout1 setScrollDirection:UICollectionViewScrollDirectionVertical];
    [flowLayout1 setScrollDirection:UICollectionViewScrollDirectionHorizontal];
    
    [self.collectionOfferCell setCollectionViewLayout:flowLayout1];
    [self.collectionOfferCell setAllowsSelection:YES];
    
    //self.pageControlView.numberOfPages=0;
    
    
  

    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:
                   str2];
    [self.bagroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
  
//    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
//    NSURL *url3 = [NSURL URLWithString:
//                   str3];
//    [self.headerImage sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    // button background color
    self.reedemrewardsBtn.backgroundColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    self.specialOfferBtn.backgroundColor = [obj colorWithHexString:[dict valueForKey:@"checkInButtonColor"]];
    
    
    NSString * str = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    NSURL *url = [NSURL URLWithString:
                  str];
    [self.companyLogoImage sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
   

    if([obj checkNetworkConnection])
    {
       
       
    }
    else
    {
        [self alertViewDelegate:@"Please check your network connection"];
    }
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(pushBackground) name:@"appDidBecomeActive" object:nil];
    
    
    
    dispatch_async( dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        dispatch_async( dispatch_get_main_queue(), ^{
            
            [self getCustomerOffersCount];
            
        });
    });
    
}

-(void)pushBackground
{
    if([[NSUserDefaults standardUserDefaults]boolForKey:@"isPushBackGround"]==YES)
    {
        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"isPushBackGround"];
        
        OfferDetailView * offerObject = [[OfferDetailView alloc]initWithNibName:@"OfferDetailView" bundle:nil];
        [self.navigationController pushViewController:offerObject animated:YES];
    }
}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    
    //
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    
    if(data!=nil)
    {
        NSDictionary *retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
        NSDictionary *  dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
        NSString * str = [dic valueForKey:@"firstName"];
        self.customerName.text = str;
        self.phoneNumberLbl.text = [dic valueForKey:@"cellPhone"];
        self.emailTF.text = [dic valueForKey:@"emailID"];
    }
    
    if([[NSUserDefaults standardUserDefaults]boolForKey:@"pushPassCome"] == YES)
    {
        [[NSUserDefaults standardUserDefaults]setBool:NO forKey:@"pushPassCome"];
        
        dispatch_async( dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            dispatch_async( dispatch_get_main_queue(), ^{
                
                [self getCustomerOffersCount];
                
            });
        });
       
    }
    
    
    
}



# pragma mark - Offers Api

-(void)getCustomerOffersCount
{
    [obj addLoadingView:self.view];
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
    
    if(arrImageBanner.count>0)
    {
        [arrImageBanner removeAllObjects];
    }
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        NSArray * arr = (NSArray *)[response valueForKey:@"inAppOfferList"];
        
        [arrImageBanner addObjectsFromArray:arr];
        
        if(arrImageBanner.count!=0)
        {
            self.totalNumbersOrderLbl.text = [NSString stringWithFormat:@"%lu",(unsigned long)arrImageBanner.count];
            
            [self.tblView reloadData];
        }
        else
        {
            self.totalNumbersOrderLbl.text = [NSString stringWithFormat:@"%lu",(unsigned long)0];
        }
    }
    else
    {
       self.totalNumbersOrderLbl.text = [NSString stringWithFormat:@"%lu",(unsigned long)0];
    }
}



// notification  method call

-(void)pushNotificationReceived
{
    
    NSLog(@"pushNotificationReceived call method");
    
    if([obj checkNetworkConnection])
    {
        
        dispatch_async( dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            dispatch_async( dispatch_get_main_queue(), ^{
                
                // call signIN Api
                [self updateProfile];
            });
        });
      
    }
    else
    {
        [self alertViewDelegate:@"Please check your network connection"];
    }
}

// alert view method

-(void)alertViewDelegate:(NSString *)str
{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Alert!" message:str preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil];
    [alertController addAction:ok];
    [self presentViewController:alertController animated:YES completion:nil];
}


// getMember api

-(void)updateProfile
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall getMember:nil url:@"/member/getMember" withTarget:self withSelector:@selector(getMember:)];
    apiCall = nil;
}

// api response

-(void)getMember:(id)response
{
    [obj removeLoadingView:self.view];
 
    NSLog(@"member response value------%@",response);
    if(response != nil)
    {
          NSUserDefaults *def = [NSUserDefaults standardUserDefaults];
         [def setObject:[NSKeyedArchiver archivedDataWithRootObject:response] forKey:@"MyData"];
         [def synchronize];
        
         self.customerName.text = [response valueForKey:@"firstName"];
         NSLog(@"lastname-----%@",[response valueForKey:@"firstName"]);
        
         IsApiSuccess = YES;
    }
}


# pragma mark - Timer method

-(void)changeBannerAutomatically
{
    [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(slideChange:) userInfo:nil repeats:YES];
}


# pragma mark - Page Controller

- (IBAction)slideChange:(id)sender
{
    if (i<arrImageBanner.count)
    {
        self.imagePage.image = [arrImageBanner objectAtIndex:i];
        self.pageControl.currentPage=i;
        i++;
        if (i==arrImageBanner.count)
        {
            i=0;
        }
    }
    
}

# pragma mark - Gesture Methods

- (IBAction)gestureLeft:(id)sender
{
   // left
//    NSInteger p=self.pageControl.currentPage;
//    if (p==arrImageBanner.count-1)
//    {
//        self.imagePage.image = [arrImageBanner objectAtIndex:0];
//        self.pageControl.currentPage=0;
//    }
//    else
//    {
//        self.imagePage.image = [arrImageBanner objectAtIndex:p+1];
//        self.pageControl.currentPage=p+1;
//    }
    
}

- (IBAction)gestureRight:(id)sender
{
   // right
//    long p=self.pageControl.currentPage;
//    if (p==0)
//    {
//        NSInteger n=arrImageBanner.count;
//        self.imagePage.image = [arrImageBanner objectAtIndex:n-1];
//        self.pageControl.currentPage=n-1;
//    }
//    else
//    {
//        self.imagePage.image = [arrImageBanner objectAtIndex:p-1];
//        self.pageControl.currentPage=p-1;
//    }
    
}

# pragma mark - Special Offer Button Action

- (IBAction)specialOfferBtn_Action:(id)sender
{
    if([obj checkNetworkConnection])
    {
        dispatch_async( dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            dispatch_async( dispatch_get_main_queue(), ^{
                
                [self getCustomerOffers];
                
            });
        });
    }
    else
    {
        [self alertViewDelegate:@"Please check your network connection"];
    }
}

# pragma mark - Offers Api

-(void)getCustomerOffers
{
     [obj addLoadingView:self.view];
      apiCall=[ApiClasses sharedManager];
    
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerID = [dic valueForKey:@"customerID"];
    NSLog(@"customer id---------%@",strCustomerID);
    
    NSString * str = [NSString stringWithFormat:@"/mobile/getoffers/%@/0",strCustomerID];
    
    [apiCall getOffers:nil url:str withTarget:self withSelector:@selector(getOffers:)];
     apiCall = nil;
}

// getoffers api response

-(void)getOffers:(id)response
{
    [obj removeLoadingView:self.view];
    NSLog(@"get offers response---------%@",response);
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        NSArray * arr = (NSArray *)[response valueForKey:@"inAppOfferList"];
        
        if(arrImageBanner.count>0)
        {
            [arrImageBanner removeAllObjects];
        }
        
        [arrImageBanner addObjectsFromArray:arr];
        
        if(arrImageBanner.count!=0)
        {
        self.totalNumbersOrderLbl.text = [NSString stringWithFormat:@"%lu",(unsigned long)arrImageBanner.count];
        [self.collectionView reloadData];
        }
        else
        {
           self.totalNumbersOrderLbl.text = [NSString stringWithFormat:@"%lu",(unsigned long)0];
        }
        // number of pages
       // self.pageControlView.numberOfPages=arr.count;
        // add background view
        self.offerView.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.6];
        self.offerView.frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
        [self.view addSubview:self.offerView];
    }
   else
    {
        [self alertViewDelegate:@"No offers available"];
    }
  
}

#pragma mark- Collection View DataSource

-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    // arrary count
    return [arrImageBanner count];
}

- (UICollectionViewCell*)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if(collectionView == self.collectionView)
    {
    collectionCell *cell = (collectionCell*)[collectionView dequeueReusableCellWithReuseIdentifier:kCellReuseIdentifier forIndexPath:indexPath];
    
    // cell view corner radius
    cell.cellView.layer.cornerRadius=4;
    cell.cellView.layer.borderWidth=1;
    cell.cellView.layer.borderColor = [[UIColor clearColor] CGColor];
    
  
    // cell image corner radius
    cell.img.layer.cornerRadius=4;
    cell.img.layer.borderWidth=1;
    cell.img.layer.borderColor = [[UIColor clearColor] CGColor];
    
    // offer image
    NSString * strImage = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"passCustomStripUrl"];
    
    if(strImage.length!=0)
    {
        NSString * strhttp = [NSString stringWithFormat:@"https://%@",strImage];
        [cell.img sd_setImageWithURL:[NSURL URLWithString:strhttp] placeholderImage:[UIImage imageNamed:@"greatOffer.png"]];
    }
    else
    {
         cell.img.image = [UIImage imageNamed:@"greatOffer.png"];
    }
    
    
    // campine descripttion
    if([[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"]!=(NSString*)[NSNull null])
    {
        cell.descriptionLbl.text = [[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"];
    }
    
    
    // campine title
    if([[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"campaignTitle"]!=(NSString*)[NSNull null])
    {
    cell.campineTitle.text = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"campaignTitle"];
    }
    
    // offer expire date
    if([[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
    {
        NSString *endStr = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
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
        cell.expireDate.text = totalDays;
    }
    
    // promocode button method
    
    [cell.promocodeBtn addTarget:self action:@selector(getPromocode:) forControlEvents:UIControlEventTouchUpInside];
        
    cell.promocodeBtn.tag = indexPath.row;
    
    [cell.sendViaSmsBtn_Action addTarget:self action:@selector(sendViaSmsBtn_Action:) forControlEvents:UIControlEventTouchUpInside];
    cell.promocodeBtn.tag = indexPath.row;
    cell.sendViaSmsBtn_Action.tag = indexPath.row;
    

  // number of offers
    //self.pageControlView.currentPage = indexPath.row;
    return cell;
    }
    else
    {
        OfferCell *cell = (OfferCell*)[collectionView dequeueReusableCellWithReuseIdentifier:kOfferCellReuseIdentifier forIndexPath:indexPath];
        
 // cell image corner radius
        
        // cell image corner radius
        cell.imgOfferPic.layer.cornerRadius=4;
        cell.imgOfferPic.layer.borderWidth=1;
        cell.imgOfferPic.layer.borderColor = [[UIColor clearColor] CGColor];
        
        
        // offer image
        NSString * strImage = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"passCustomStripUrl"];
        
        if(strImage.length!=0)
        {
            NSString * strhttp = [NSString stringWithFormat:@"https://%@",strImage];
            [cell.imgOfferPic sd_setImageWithURL:[NSURL URLWithString:strhttp] placeholderImage:[UIImage imageNamed:@"greatOffer.png"]];
        }
        else
        {
            cell.imgOfferPic.image = [UIImage imageNamed:@"greatOffer.png"];
        }
        // campine descripttion
        if([[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"]!=(NSString*)[NSNull null])
        {
            cell.offerDescriptionLbl.text = [[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"];
        }
        
        return cell;

    }
        
}


- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if(collectionView == self.collectionOfferCell)
    {
        return self.collectionOfferCell.frame.size;
    }
    else
    {
        return self.collectionView.frame.size;
    }
}


#pragma mark- Collection View Delegate

- (void)collectionView:(UICollectionView*)collectionView didSelectItemAtIndexPath:(NSIndexPath*)indexPath
{
    if(collectionView == self.collectionOfferCell)
    {
        if([obj checkNetworkConnection])
        {
            dispatch_async( dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
                dispatch_async( dispatch_get_main_queue(), ^{
                    
                   // [self getCustomerOffers];
                    
                });
            });
        }
        else
        {
            [self alertViewDelegate:@"Please check your network connection"];
        }
    }
    else
    {
        
    }
}

// scroll view delegate method

-(void)scrollViewWillEndDragging:(UIScrollView *)scrollView withVelocity:(CGPoint)velocity targetContentOffset:(inout CGPoint *)targetContentOffset {
    
    if(scrollView == self.collectionView)
    {
        *targetContentOffset = scrollView.contentOffset; // set acceleration to 0.0
        float pageWidth = (float)self.collectionOfferCell.bounds.size.width;
        int minSpace = 10;
        
        int cellToSwipe = (scrollView.contentOffset.x)/(pageWidth + minSpace) + 0.5; // cell width + min spacing for lines
        if (cellToSwipe < 0) {
            cellToSwipe = 0;
        }
        
       // self.pageControlView.currentPage = cellToSwipe;
        [self.collectionOfferCell scrollToItemAtIndexPath:[NSIndexPath indexPathForRow:cellToSwipe inSection:0] atScrollPosition:UICollectionViewScrollPositionLeft animated:YES];
    }
//    else
//    {
//        *targetContentOffset = scrollView.contentOffset; // set acceleration to 0.0
//        float pageWidth = (float)self.collectionView.bounds.size.width;
//        int minSpace = 10;
//        
//        int cellToSwipe = (scrollView.contentOffset.x)/(pageWidth + minSpace) + 0.5; // cell width + min spacing for lines
//        if (cellToSwipe < 0) {
//            cellToSwipe = 0;
//        }
//        
//       // self.pageControlView.currentPage = cellToSwipe;
//        [self.collectionView scrollToItemAtIndexPath:[NSIndexPath indexPathForRow:cellToSwipe inSection:0] atScrollPosition:UICollectionViewScrollPositionLeft animated:YES];
//    }
}


// table view data source and delegate method


#pragma mark - TableView Delegate And DataSources

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
         return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
        return 110;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
        return [arrImageBanner count];
   
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
        static NSString *CellIdentifier = @"HomeCellTable";
    
        HomeCellTable *cell = (HomeCellTable*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil)
        {
            cell = [[[NSBundle mainBundle] loadNibNamed:CellIdentifier owner:self options:nil] objectAtIndex:0];
        }
    
    cell.contentView.backgroundColor = [UIColor clearColor];
    
    // campine descripttion
    if([[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"]!=(NSString*)[NSNull null])
    {
        
        cell.lblTitleTbl.text = [[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"campaignDescription"];
    }
    
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    NSString * strImage = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"passCustomStripUrl"];
    
    if(strImage.length!=0)
    {
        NSString * strhttp = [NSString stringWithFormat:@"https://%@",strImage];
        [cell.imageOfferTbl sd_setImageWithURL:[NSURL URLWithString:strhttp] placeholderImage:[UIImage imageNamed:@"greatOffer.png"]];
    }
    else
    {
        cell.imageOfferTbl.image = [UIImage imageNamed:@"greatOffer.png"];
    }
    
    // campine descripttion
    if([[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"campaignTitle"]!=(NSString*)[NSNull null])
    {
        cell.lblDescriptionTbl.text = [[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"campaignTitle"];
    }
    
    // offer expire date
    if([[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
    {
        NSString *endStr = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
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
        
        NSString *totalDays=[NSString stringWithFormat:@"Expires in %ld days",(long)[components day]];
        cell.lblExpireDateTbl.text = totalDays;
    }
    
    
        return cell;
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    [obj addLoadingView:self.view];
    
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
        
        if (arrImageBanner.count>0)
        {
        
        NSString * strChannelID = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"channelID"];
        
        if([strChannelID intValue] == 6)
        {
            // offerId / campineId
            NSString * strCampineID = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"campaignId"];
            // @"16900720"
            
            NSString * str = [NSString stringWithFormat:@"/mobile/getPass"];
            NSMutableDictionary * dicValue = [[NSMutableDictionary alloc]init];
            [dicValue setValue:strCustomerID forKey:@"customerID"];
            [dicValue setValue:strCampineID forKey:@"campaignId"];
        
            // get offer api
            
            [apiCall PassOpen:dicValue url:str withTarget:self withSelector:@selector(getPass:)];
            apiCall = nil;
        }
        else
        {
            // promo logo image
            NSString * logourl = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"customLogoURL"];
            if(logourl.length!=0)
            {
                imgPromoLogo = logourl;
            }
            
            // offerTitle
            NSString * titleOffer = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"campaignTitle"];
            if(titleOffer.length!=0)
            {
                offerTitle  = titleOffer;
            }
            
            // offerDescription
            NSString * descriptionOffer = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"campaignDescription"];
            
            if(descriptionOffer.length!=0)
            {
                offerDescription  = descriptionOffer;
            }
            
            // offer expire date
            
            if([[arrImageBanner objectAtIndex:indexPath.row] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
            {
                
                NSString *endStr = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"validityEndDateTime"];
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
            
            
            // offer imageURL
            NSString * offerImage = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"passCustomStripUrl"];
            if(offerImage.length!=0)
            {
                offerImageURl = offerImage;
            }
            
            // offerId / campineId
            NSString * strCampineID = [[arrImageBanner objectAtIndex:indexPath.row]valueForKey:@"campaignId"];
            // @"16900720"
            
            NSString * str = [NSString stringWithFormat:@"/mobile/getPromo/%@/%@/NO",strCustomerID,strCampineID];
            
            // get offer api
            [apiCall getPromocode:nil url:str withTarget:self withSelector:@selector(getPromocodeResponse:)];
            apiCall = nil;
        }
        }
    }
    else
    {
        [self alertViewDelegate:@"Please check your network connection"];
    }

}



# pragma mark - sendViaSmsBtn Button Action

-(void)sendViaSmsBtn_Action:(UIButton *)sender
{
    [self alertViewDelegate:@"No sms functionality available"];
}




# pragma mark - getPromocode Button Action

-(void)getPromocode:(UIButton *)sender
{
    [obj addLoadingView:self.view];
    
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
        
        NSString * strChannelID = [[arrImageBanner objectAtIndex:[sender tag]]valueForKey:@"channelID"];
        
        if([strChannelID intValue] == 6)
        {
            // offerId / campineId
            NSString * strCampineID = [[arrImageBanner objectAtIndex:[sender tag]]valueForKey:@"campaignId"];
            // @"16900720"
            
            NSString * str = [NSString stringWithFormat:@"/mobile/getPass"];
            
            NSMutableDictionary * dicValue = [[NSMutableDictionary alloc]init];
            [dicValue setValue:strCustomerID forKey:@"customerID"];
            [dicValue setValue:strCampineID forKey:@"campaignId"];
            
            // get offer api
            [apiCall PassOpen:dicValue url:str withTarget:self withSelector:@selector(getPass:)];
            apiCall = nil;
            
        }
        else
        {
            // promo logo image
            NSString * logourl = [[arrImageBanner objectAtIndex:[sender tag]]valueForKey:@"customLogoURL"];
            if(logourl.length!=0)
            {
                imgPromoLogo = logourl;
            }
            
            // offerTitle
            NSString * titleOffer = [[arrImageBanner objectAtIndex:[sender tag]]valueForKey:@"campaignTitle"];
            if(titleOffer.length!=0)
            {
                offerTitle  = titleOffer;
            }
            
            // offerDescription
            NSString * descriptionOffer = [[arrImageBanner objectAtIndex:[sender tag]]valueForKey:@"campaignDescription"];
            
            if(descriptionOffer.length!=0)
            {
                offerDescription  = descriptionOffer;
            }
            
            // offer expire date
            
            if([[arrImageBanner objectAtIndex:[sender tag]] valueForKey:@"validityEndDateTime"]!=(NSString*)[NSNull null])
            {
                
                NSString *endStr = [[arrImageBanner objectAtIndex:[sender tag]]valueForKey:@"validityEndDateTime"];
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
            
            
            // offer imageURL
            NSString * offerImage = [[arrImageBanner objectAtIndex:[sender tag]]valueForKey:@"passCustomStripUrl"];
            if(offerImage.length!=0)
            {
                offerImageURl = offerImage;
            }
            
            // offerId / campineId
            NSString * strCampineID = [[arrImageBanner objectAtIndex:[sender tag]]valueForKey:@"campaignId"];
            // @"16900720"
            
            NSString * str = [NSString stringWithFormat:@"/mobile/getPromo/%@/%@/NO",strCustomerID,strCampineID];
            
            // get offer api
            [apiCall getPromocode:nil url:str withTarget:self withSelector:@selector(getPromocodeResponse:)];
            apiCall = nil;
        }
    }
    else
    {
        [self alertViewDelegate:@"Please check your network connection"];
    }
    
}


// Promo api response

-(void)getPass:(id)response
{
    [obj removeLoadingView:self.view];
    
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
        
        offerObj.loginImageURl     = imgPromoLogo;
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


# pragma mark - Reedem Button Action

- (IBAction)redeemBtn_Action:(id)sender
{
    [self alertViewDelegate:@"No rewards available"];
}

# pragma mark - logOutBtn Action

- (IBAction)logOutBtn_Action:(id)sender
{
//    if(IsApiSuccess == YES)
//    {
    [self.delegate logout];
   // }
//    else
//    {
//    [self alertViewDelegate:@"Invalid access token"];
//    }
}


//-(void)pushOffers:(NSDictionary *)userinfo
//{
//     clpsdk * clpObj = [[clpsdk alloc]init];
//    [clpObj processPushMessage:userinfo];
//}


# pragma mark - Cross Button Action

- (IBAction)crossBtn_Action:(id)sender
{
    [self.offerView removeFromSuperview];
}


# pragma mark - memory method

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}



- (IBAction)logOut_Action:(id)sender
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:@"Do you want to Logout."
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                                  //[self logOutApi];
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




- (IBAction)MenuAction:(id)sender
{
    MenuViewController * menuObj = [[MenuViewController alloc]initWithNibName:@"MenuViewController" bundle:nil];

    
    [self.navigationController pushViewController:menuObj animated:YES];

}



// dealloc method

-(void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


@end
