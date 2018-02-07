//
//  CoadScannerVC.m
//  clpsdk
//
//  Created by Gourav Shukla on 22/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "CoadScannerVC.h"
#import "ModelClass.h"



@interface CoadScannerVC ()
{
     ModelClass  * obj;
}
@property (weak, nonatomic) IBOutlet UILabel *userNameLbl;
@property (weak, nonatomic) IBOutlet UILabel *mobileNumberLbl;
@property (weak, nonatomic) IBOutlet UIView *coadScannerView;

@end

@implementation CoadScannerVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Qrcoe generator
    
     obj=[ModelClass sharedManager];
   
    [self userProfile];
    [self shadoOffect:self.coadScannerView];
    
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
    
}



#pragma mark - userProfile method

-(void)userProfile
{
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerFirstName = [dic valueForKey:@"firstName"];
    NSString * strCustomerLastName = [dic valueForKey:@"lastName"];
    NSString * strCustomermobile = [dic valueForKey:@"cellPhone"];
    NSString * strLoyalityNo = [dic valueForKey:@"loyalityNo"];
    
    
    if(strCustomerFirstName != (NSString *)[NSNull null])
    {
        if(strCustomerLastName != (NSString *)[NSNull null])
        {
            self.userNameLbl.text = [NSString stringWithFormat:@"%@ %@",strCustomerFirstName ,strCustomerLastName];
        }
        else
        {
            self.userNameLbl.text = strCustomerFirstName;
        }
    }
    
    if(strCustomermobile != (NSString *)[NSNull null])
    {
        self.mobileNumberLbl.text = strCustomermobile;
    }
    
    // qr code
    if(strLoyalityNo != (NSString *)[NSNull null])
    {
      self.barCodeLbl.text = strLoyalityNo;
    
    if(self.barCodeLbl.text.length!=0)
    {
        //self.barCodeLbl.text
        self.qrImageCode.image = [obj quickResponseImageForString:self.barCodeLbl.text withDimension:182];
    }
    
   }
}


#pragma mark - memory method

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


#pragma mark - Back Button Action

- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}






@end
