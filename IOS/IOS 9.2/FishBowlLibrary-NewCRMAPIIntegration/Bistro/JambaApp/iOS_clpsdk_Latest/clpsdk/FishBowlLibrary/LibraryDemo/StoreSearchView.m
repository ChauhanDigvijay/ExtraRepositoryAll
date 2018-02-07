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

@interface StoreSearchView ()<UITableViewDelegate,UITableViewDataSource>
{
     ModelClass  * obj;
     NSArray * SearchArray;
     NSMutableArray * arrCategory;
     NSArray * arrStoreNumber;
    
}
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *backgroundImage;
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *headerImage;
@property (unsafe_unretained, nonatomic) IBOutlet UITextField *tfSearch;
@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tblCategory;
@property (unsafe_unretained, nonatomic) IBOutlet UITableView *tblSearch;
@property (weak, nonatomic) IBOutlet UIImageView *logoImageIcon;

@end

@implementation StoreSearchView

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
    NSData *data = [currentDefaults objectForKey:@"mobileSetting"];
    NSDictionary * dict = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    self.tfSearch.autocorrectionType = UITextAutocorrectionTypeNo;
    [self.tblCategory setHidden:YES];
    
    // header image
//    NSString * str3 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"loginHeaderImageUrl"]];
//    NSURL *url3 = [NSURL URLWithString:
//                   str3];
//    
//    [self.headerImage sd_setImageWithURL:url3 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    // background image
    NSString * str2 = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"signUpBackgroundImageUrl"]];
    NSURL *url2 = [NSURL URLWithString:
                   str2];
    [self.backgroundImage sd_setImageWithURL:url2 placeholderImage:[UIImage imageNamed:@"nil"]];
    
    
    
    NSString * str = [NSString stringWithFormat:@"http://%@",[dict valueForKey:@"companyLogoImageUrl"]];
    NSURL *url = [NSURL URLWithString:
                  str];
    [self.logoImageIcon sd_setImageWithURL:url placeholderImage:[UIImage imageNamed:@"nil"]];
    
    [self arrayCategory];
}


-(void)arrayCategory
{
     //SearchArray =  [[NSUserDefaults standardUserDefaults]valueForKey:@"storesKey"];
    
     arrCategory =  [[[NSUserDefaults standardUserDefaults]valueForKey:@"storesKey"]valueForKey:@"storeName"];
   
    
     NSLog(@"arrCategory--------%@",arrCategory);
    
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
        
//        static NSString *CellIdentifier = @"SearchCell";
//        SearchCell *cell = (SearchCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
//        
//        if (cell == nil)
//        {
//            cell = [[[NSBundle mainBundle] loadNibNamed:CellIdentifier owner:self options:nil] objectAtIndex:0];
//        }
//        cell.lblCategory.text = [SearchArray objectAtIndex:indexPath.row];
//        cell.selectionStyle = UITableViewCellSelectionStyleNone;
//        
//        return cell;
        
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
        NSString * str = [arrCategory objectAtIndex:indexPath.row];
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



-(void)SearchNumber:(NSString *)str
{
    arrStoreNumber =  [[NSUserDefaults standardUserDefaults]valueForKey:@"storesKey"];
    
    NSArray * str1 = [arrStoreNumber filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"(storeName == %@)", str]];
    
    NSLog(@"predicate Array %@",[[str1 objectAtIndex:0]valueForKey:@"storeNumber"]);
    
    NSString * storeNumbers = [[str1 objectAtIndex:0]valueForKey:@"storeNumber"];
    
    [self.delegate StoreSearch:storeNumbers and:str];
    
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
        self.tblCategory.frame = CGRectMake(10, 124+20, self.view.frame.size.width-30,self.view.frame.size.height-140);
    }
    else
    {
        self.tblCategory.frame = CGRectMake(10, 124+20, self.view.frame.size.width-30, SearchArray.count*30);
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
