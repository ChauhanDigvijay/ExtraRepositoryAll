//
//  StoreSearchView.m
//  clpsdk
//
//  Created by Gourav Shukla on 10/08/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "StoreSearchView.h"
#import "ModelClass.h"
#import "SearchCell.h"
#import "ApiClasses.h"

@interface StoreSearchView ()<UITableViewDelegate,UITableViewDataSource>
{
     ModelClass      * obj;
     NSArray         * SearchArray;
     NSArray         * arrStoreNumber;
     ApiClasses      * apiCall;
}
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView * backgroundImage;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView * headerImage;
@property (unsafe_unretained, nonatomic) IBOutlet UITextField * tfSearch;
@property (unsafe_unretained, nonatomic) IBOutlet UITableView * tblCategory;
@property (unsafe_unretained, nonatomic) IBOutlet UITableView * tblSearch;
@property (weak, nonatomic) IBOutlet UIView                   * favouritStoreView;
@property (weak, nonatomic) IBOutlet UIImageView              * logoImageIcon;
@property (weak, nonatomic) IBOutlet UILabel *lblheader;

@end

@implementation StoreSearchView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    self.tfSearch.autocorrectionType = UITextAutocorrectionTypeNo;
    [self.tblCategory setHidden:YES];
    
 
    self.tfSearch.layer.masksToBounds = YES;
    self.tfSearch.layer.cornerRadius = 2.0;
    self.tfSearch.layer.borderWidth = 0.5;
    self.tfSearch.layer.borderColor = [[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:1] CGColor];
    
    
    self.tblSearch.layer.masksToBounds = YES;
    self.tblSearch.layer.cornerRadius = 2.0;
    self.tblSearch.layer.borderWidth = 0.5;
    self.tblSearch.layer.borderColor = [[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:1] CGColor];
    
    
    self.tblCategory.layer.masksToBounds = YES;
    self.tblCategory.layer.cornerRadius = 2.0;
    self.tblCategory.layer.borderWidth = 0.5;
    self.tblCategory.layer.borderColor = [[UIColor colorWithRed:202.0/255.0f green:202.0/255.0f blue:202.0/255.0f alpha:1] CGColor];
   
    
    NSString * str = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    NSURL *url = [NSURL URLWithString:
                  str];
    [self.logoImageIcon sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    NSLog(@"self.customField is %li",(long)self.customField);
    if(self.customField == 0)
    {
    [self arrayCategory];
    }
    
    else
    {
        [self.tblCategory reloadData];
    }
    
    [self shadoOffect:self.favouritStoreView];
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 4;
    shadoView.layer.shadowOpacity = 0.5;
}

// array category
-(void)arrayCategory
{
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"storesKey"];
    NSDictionary * dictValue = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    self.arrCategory  = [dictValue valueForKey:@"storeName"];
    [self.tblSearch reloadData];
    
    NSLog(@"arrayCategory -------- %lu",(unsigned long)self.arrCategory.count);
   

    // check store available
    if(self.arrCategory.count == 0)
    {
        obj = [ModelClass sharedManager];
        [obj addLoadingView:self.view];
        
        apiCall=[ApiClasses sharedManager];
        [apiCall getAllStores:nil url:@"/mobile/stores/getstores" withTarget:self withSelector:@selector(storeApi:)];
        apiCall = nil;
    }
}

-(void)storeApi:(id)response
{
    [obj removeLoadingView:self.view];
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
        NSData *data = [NSKeyedArchiver archivedDataWithRootObject:[response valueForKey:@"stores"]];
        [currentDefaults setObject:data forKey:@"storesKey"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        NSUserDefaults *currentDefaults1 = [NSUserDefaults standardUserDefaults];
        NSData *data1 = [currentDefaults1 objectForKey:@"storesKey"];
        NSDictionary * dictValue = [NSKeyedUnarchiver unarchiveObjectWithData:data1];
        
        self.arrCategory  = [dictValue valueForKey:@"storeName"];
        
        
          [self.tblSearch reloadData];
    }
    else
    {
        //[self alert_Action:@"Due to slow internet connection Stores are not loaded"];
        
    }
    NSLog(@"response store api ------- %@", response);
}

// alert action
-(void)alert_Action:(NSString *)str
{
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:str
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
                              }];
    
    [alert addAction:cancel1];
    [self presentViewController:alert animated:YES completion:nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


#pragma mark - TableView Delegate And DataSources

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(tableView == self.tblSearch)
    {
        return 44;
    }
    else
    {
        return 30;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(tableView == self.tblSearch)
    {
        NSLog(@"self arrCategory %@",self.arrCategory.description);
        return [self.arrCategory count];
    }
    else
    {
        NSLog(@"self arrCategory %@",self.arrCategory.description);
        return [SearchArray count];
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if(tableView == self.tblSearch)
    {
        static NSString *CellIdentifier = @"SearchCell";
        SearchCell *cell = (SearchCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil)
        {
            cell = [[[NSBundle mainBundle] loadNibNamed:CellIdentifier owner:self options:nil] objectAtIndex:0];
        }
        cell.lblCategory.text = [self.arrCategory objectAtIndex:indexPath.row];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
    }
    else
    {
        NSString *CellIdentifier = @"Cell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
        
        if (cell==nil)
        {
            cell=[[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
            
        }
        cell.textLabel.text = [SearchArray objectAtIndex:indexPath.row];
        cell.textLabel.font = [UIFont systemFontOfSize:14];
        return cell;
    }
}


// delegate method

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if(tableView == self.tblSearch)
    {
        NSString * str = [self.arrCategory objectAtIndex:indexPath.row];
        self.tfSearch.text = str;
        [self SearchNumber:str];
    }
    else
    {
        [self.tfSearch resignFirstResponder];
        [self.tblSearch setHidden:YES];
        [self.tblCategory setHidden:YES];
         NSString * str = [SearchArray objectAtIndex:indexPath.row];
        self.tfSearch.text = str;
        [self SearchNumber:str];
    }
}



// search storeNumber
-(void)SearchNumber:(NSString *)str
{
    
    
    
    if(self.customField == 1)
    {
        
        [self.delegate customSearch:str withTextFieldTag:self.tagValue];
        
    }
    
    else
    {
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"storesKey"];
    arrStoreNumber = [NSKeyedUnarchiver unarchiveObjectWithData:data];
   // arrStoreNumber  = [dictValue valueForKey:@"storeName"];
    
    NSArray * str1 = [arrStoreNumber filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(storeName == %@)", str]];
    
    NSLog(@"predicate Array %@",[[str1 objectAtIndex:0]valueForKey:@"storeNumber"]);
    
    NSString * storeNumbers = [[str1 objectAtIndex:0]valueForKey:@"storeNumber"];
    
        [self.delegate StoreSearch:storeNumbers and:str withTag:self.tagValue];
        
    }
    
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark -  textField Delegate Methods

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    [self.tblSearch setHidden:YES];
    [self.tblCategory setHidden:NO];
    
    NSString *stringText = [textField.text stringByReplacingCharactersInRange:range withString:string];
    
    NSPredicate *resultPredicate = [NSPredicate predicateWithFormat:@"SELF beginswith[c] %@", stringText];
    
    if ([stringText isEqualToString:@""])
    {
        
    } else {
        
        SearchArray = [self.arrCategory filteredArrayUsingPredicate:resultPredicate];
    }
    
    if(stringText.length==0)
    {
        if(self.arrCategory.count>0)
        {
            [self.tblSearch setHidden:NO];
            [self.tblCategory setHidden:YES];
        }
        else
        {
            [self.tblSearch setHidden:NO];
            [self.tblCategory setHidden:YES];
        }
    }
    else
    {
        [self.tblSearch setHidden:YES];
        [self.tblCategory setHidden:NO];
    }
    
    
    [self.tblCategory reloadData];
    
    if(self.tblCategory.contentSize.height>self.view.frame.size.height-140)
    {
        self.tblCategory.frame = CGRectMake(10, 120, self.view.frame.size.width-20,self.view.frame.size.height-140);
    }
    else
    {
        self.tblCategory.frame = CGRectMake(10, 120, self.view.frame.size.width-20, SearchArray.count*30);
    }
    
    return YES;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}


// back button action
- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

@end
