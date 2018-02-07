//
//  StateSearchView.m
//  clpsdk
//
//  Created by Gourav Shukla on 16/10/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import "StateSearchView.h"
#import "SearchCell.h"
#import "ApiClasses.h"
#import "ModelClass.h"

@interface StateSearchView ()
{
    ApiClasses     * apiCall;
    ModelClass     * obj;
    NSArray        * SearchArray;
    NSMutableArray * arrCategory;
    NSArray        * arrCountry;
    NSMutableArray * arrStateCode;
}
@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tblSearch;
@property (unsafe_unretained, nonatomic) IBOutlet UIView *stateView;
@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tblCategory;
@property (unsafe_unretained, nonatomic) IBOutlet UITextField *tfSearch;
@property (weak, nonatomic) IBOutlet UILabel *lblTitleLabel;
@end

@implementation StateSearchView

- (void)viewDidLoad {
    [super viewDidLoad];
    
     obj=[ModelClass sharedManager];
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
    
    [self shadoOffect:self.stateView];
    
    
    if([self.titleLabel isEqualToString:@"State"])
    {
        self.tfSearch.placeholder = @"SearchState";
        self.lblTitleLabel.text = self.titleLabel;
        // state api
        [self stateApi];
    }
    else
    {
        self.tfSearch.placeholder = @"SearchCountry";
        self.lblTitleLabel.text = self.titleLabel;
        // state api
        [self countryApi];
    }
}


// shado view
-(void)shadoOffect:(UIView *)shadoView
{
    shadoView.layer.masksToBounds = NO;
    shadoView.layer.shadowOffset = CGSizeMake(0, 0);
    shadoView.layer.shadowRadius = 2;
    shadoView.layer.shadowOpacity = 0.5;
    
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}


#pragma mark - State Api
-(void)stateApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall stateSearch:nil url:@"/states" withTarget:self withSelector:@selector(stateApi:)];
    apiCall = nil;
}


// state api response
-(void)stateApi:(id)response
{
    [obj removeLoadingView:self.view];
    
     NSLog(@"state List response ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        arrCategory  = [NSMutableArray new];
        arrStateCode = [NSMutableArray new];
       [[NSUserDefaults standardUserDefaults]setValue:[response valueForKey:@"stateList"] forKey:@"stateSearch"];
        
       // NSString * countryCode = [[NSUserDefaults standardUserDefaults]valueForKey:@"CountryCode"];
        
//        if(countryCode.length !=0)
//        {
//            NSArray * arr = [response valueForKey:@"stateList"];
//            
//            NSLog(@"country code name ------- %@", countryCode);
//            
//            for(int j =0; j< [arr count]; j++)
//            {
//                if([[[arr objectAtIndex:j]valueForKey:@"countryCode"] isEqualToString:countryCode])
//                {
//                    [arrCategory addObject:[[arr objectAtIndex:j]valueForKey:@"stateName"]];
//                    [arrStateCode addObject:[[arr objectAtIndex:j]valueForKey:@"stateCode"]];
//                }
//            }
//             [self.tblSearch reloadData];
//            
//            if(arrCategory.count == 0)
//            {
//                [self.delegate stateSearch:@""];
//                [self alert_Action:@"No states available for this country"];
////                [[NSUserDefaults standardUserDefaults]removeObjectForKey:@"StateCode"];
////                arrCategory  = [[response valueForKey:@"stateList"]valueForKey:@"stateName"];
////                arrStateCode = [[response valueForKey:@"stateList"]valueForKey:@"stateCode"];
//            }
//            
//        }
//        else
//        {
          arrCategory  = [[response valueForKey:@"stateList"]valueForKey:@"stateName"];
          arrStateCode = [[response valueForKey:@"stateList"]valueForKey:@"stateCode"];
          [self.tblSearch reloadData];
           NSLog(@"arrayCategory name ------- %@",arrCategory);
       // }
    }
    else
    {
        [self alert_Action:@"Something went wrong"];
    }
}

#pragma mark - Country Api
-(void)countryApi
{
    [obj addLoadingView:self.view];
    apiCall=[ApiClasses sharedManager];
    [apiCall countryApi:nil url:@"/states/getAllCountry" withTarget:self withSelector:@selector(CountryApi:)];
    apiCall = nil;
}

// state api response
-(void)CountryApi:(id)response
{
    [obj removeLoadingView:self.view];
    
    NSLog(@"Country List response ------- %@",response);
    
    if([[response valueForKey:@"successFlag"]intValue] == 1)
    {
        arrCategory  = [NSMutableArray new];
        arrCategory  = [[response valueForKey:@"countryList"]valueForKey:@"countryName"];
        arrCountry   = [[response valueForKey:@"countryList"]valueForKey:@"countryCode"];
        [self.tblSearch reloadData];
       // NSLog(@"arrayCategory name ------- %@",arrCategory);
       // [[NSUserDefaults standardUserDefaults]setValue:[response valueForKey:@"stateList"] forKey:@"stateSearch"];
    }
    else
    {
        [self alert_Action:@"Something went wrong"];
    }
}

// alert Action
-(void)alert_Action:(NSString *)str
{
    
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Alert!"
                                  message:str
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *cancel1 = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:^(UIAlertAction * action)
                              {
            [self.navigationController popViewControllerAnimated:YES];
                              }];
    
    [alert addAction:cancel1];
    [self presentViewController:alert animated:YES completion:nil];
}


// back button Action
- (IBAction)backBtn_Action:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
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
        return [arrCategory count];
    }
    else
    {
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
        cell.lblCategory.text = [arrCategory objectAtIndex:indexPath.row];
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
    
    
    NSLog(@"indexPath is %@",[arrCategory objectAtIndex:indexPath.row]);
    
    if([self.titleLabel isEqualToString:@"State"])
    {
        NSLog(@"self.titleLabel is %@",self.titleLabel);

    if(tableView == self.tblSearch)
    {
        NSString * str = [arrCategory objectAtIndex:indexPath.row];
        self.tfSearch.text = str;
        
        NSString * stateCode = [arrStateCode objectAtIndex:indexPath.row];
        [[NSUserDefaults standardUserDefaults]setValue:stateCode forKey:@"StateCode"];
        
        [self.delegate stateSearch:str withTag:self.intTag];
    }
    else
    {
        [self.tfSearch resignFirstResponder];
        [self.tblSearch setHidden:YES];
        [self.tblCategory setHidden:YES];
        NSString * str = [SearchArray objectAtIndex:indexPath.row];
        self.tfSearch.text = str;
        
        NSString * stateCode = [arrStateCode objectAtIndex:indexPath.row];
        [[NSUserDefaults standardUserDefaults]setValue:stateCode forKey:@"StateCode"];
        
        [self.delegate stateSearch:str withTag:self.intTag];
    }
    }
    else
    {
        
        NSLog(@"self.titleLabel is %@",self.titleLabel);

        if(tableView == self.tblSearch)
        {
            NSString * str = [arrCategory objectAtIndex:indexPath.row];
            self.tfSearch.text = str;
            
            NSString * str1 = [arrCountry objectAtIndex:indexPath.row];
            
            NSLog(@"arrCountry is %@",arrCountry.description);
            NSLog(@"str1 is %@",str1);
            
            if(arrCountry.count!=0)
            {
                [self.delegate countrySearch:str and:str1 WithTextTag:self.intTag];
        }
        }
        else
        {
            [self.tfSearch resignFirstResponder];
            [self.tblSearch setHidden:YES];
            [self.tblCategory setHidden:YES];
            NSLog(@"SearchArray is %@",SearchArray.description);

            NSString * str = [SearchArray objectAtIndex:indexPath.row];
            self.tfSearch.text = str;
            
            NSLog(@"str is %@",str);
            
            NSString * str1 = [arrCountry objectAtIndex:indexPath.row];
            NSLog(@"str1 is %@",str1);

            NSLog(@"arrCountry is %@",arrCountry.description);

            
            if(arrCountry.count!=0)
            {
                [self.delegate countrySearch:str and:str1 WithTextTag:self.intTag];
            }
        }
        
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
        
        SearchArray = [arrCategory filteredArrayUsingPredicate:resultPredicate];
    }
    
    if(stringText.length==0)
    {
        if(arrCategory.count>0)
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


@end
