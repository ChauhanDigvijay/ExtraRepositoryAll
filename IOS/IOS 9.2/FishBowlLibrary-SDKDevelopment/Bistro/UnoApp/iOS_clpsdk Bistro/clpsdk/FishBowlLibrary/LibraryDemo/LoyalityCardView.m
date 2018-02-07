//
//  LoyalityCardView.m
//  clpsdk
//
//  Created by Gourav Shukla on 10/11/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "LoyalityCardView.h"
#import "ModelClass.h"


@interface LoyalityCardView ()
{
    ModelClass  * obj;
}

@property (unsafe_unretained, nonatomic) IBOutlet UIView *emailWalletView;
@property (unsafe_unretained, nonatomic) IBOutlet UIView *loyaltyCardView;
@property (unsafe_unretained, nonatomic) IBOutlet UIView *loyaltyNumberView;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *userName;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *barCodeImage;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *loyalityNumber;
@property (weak, nonatomic) IBOutlet UIImageView *lineImage;
@property (weak, nonatomic) IBOutlet UIImageView *loyalityLineImage;

@end

@implementation LoyalityCardView

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    obj=[ModelClass sharedManager];
    
    // shado radius
    [self addCornerRadius:self.emailWalletView];
    [self addCornerRadius:self.loyaltyCardView];
    [self addCornerRadius:self.loyaltyNumberView];
    
    
    self.lineImage.backgroundColor =[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:.3];
    self.loyalityLineImage.backgroundColor =[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:.3];
    
    
  // userProfile
    [self userProfile];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}

// shado view CornerRadius
-(void)addCornerRadius:(UIView*)textField
{
    textField.layer.masksToBounds = YES;
    textField.layer.cornerRadius = 2.0;
    textField.layer.borderWidth = 1.0;
    textField.layer.borderColor = [[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:.3] CGColor];
}



#pragma mark - userProfile

-(void)userProfile
{
    NSUserDefaults *def1 = [NSUserDefaults standardUserDefaults];
    NSData *data = [def1 objectForKey:@"MyData"];
    NSDictionary * retrievedDictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    NSDictionary * dic = [[NSDictionary alloc] initWithDictionary:retrievedDictionary];
    NSString * strCustomerFirstName = [dic valueForKey:@"firstName"];
    NSString * strCustomerLastName = [dic valueForKey:@"lastName"];
    NSString * strLoyalityNo = [dic valueForKey:@"loyalityNo"];
    
    
    if(strCustomerFirstName!=(NSString *)[NSNull null])
    {
        if(strCustomerLastName != (NSString *)[NSNull null])
        {
            self.userName.text = [NSString stringWithFormat:@"%@ %@",strCustomerFirstName ,strCustomerLastName];
        }
        else
        {
            self.userName.text = strCustomerFirstName;
        }
    }

    // qr code
    if(strLoyalityNo != (NSString *)[NSNull null])
    {
        self.loyalityNumber.text = strLoyalityNo;
        
        if(self.loyalityNumber.text.length!=0)
        {
            self.barCodeImage.image = [obj quickResponseImageForString:self.loyalityNumber.text withDimension:182];
        }
        
    }
    else
    {
         self.barCodeImage.image = [obj quickResponseImageForString:@"32344567"withDimension:182];
    }
}


#pragma mark - Back Button Action

- (IBAction)crossBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

@end
