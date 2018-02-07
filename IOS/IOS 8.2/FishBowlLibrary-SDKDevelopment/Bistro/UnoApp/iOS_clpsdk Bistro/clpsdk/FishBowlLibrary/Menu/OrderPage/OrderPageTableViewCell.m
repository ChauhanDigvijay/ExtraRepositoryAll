//
//  OrderPageTableViewCell.m
//  taco2
//
//  Created by HARSH on 02/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import "OrderPageTableViewCell.h"
#import "OrderPageViewController.h"
@implementation OrderPageTableViewCell
@synthesize quantityText,something;
+(OrderPageTableViewCell*)orderPageTableViewCell
{
    
    OrderPageTableViewCell *customPopup = [[[NSBundle mainBundle]loadNibNamed:@"OrderPageTableViewCell" owner:self options:nil]lastObject];
    if(customPopup != nil || [customPopup isKindOfClass:[OrderPageTableViewCell class]]){
        return customPopup;
    }else{
        return nil;
    }
}
- (void)awakeFromNib {

    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (IBAction)doneEditing:(id)sender
{
//    if([quantityText.text isEqual:@""])
//    {
//        NSLog(@"%@",quantityText.text);
//    }
//    
//    else
//    {
//        int index_path=[_orderCounting.text intValue];
//        int myindex=index_path-1;
//        NSLog(@"%d",myindex);
//        NSString *strindex=[NSString stringWithFormat:@"%d",myindex];
//        NSUserDefaults *myIndex=[NSUserDefaults standardUserDefaults];
//        [myIndex setValue:strindex forKey:@"myindex"];
//    [myIndex synchronize];
//        
//   NSLog(@"%@",quantityText.text);
//        NSString *myValu=[NSString stringWithString:_singleItemCost.text];
//        myValu=[myValu substringFromIndex:1];
//        NSLog(@"%@",myValu);
//        float  intCost1=[myValu floatValue];
//        int quantity2=[quantityText.text intValue];
//        NSString *strquantity=[NSString stringWithFormat:@"%d",quantity2];
//        NSInteger totalCost1=intCost1*quantity2;
//        _price.text=[NSString stringWithFormat:@"$%ld.00",(long)totalCost1];
//        
//        NSUserDefaults *finalCostSingleItem=[NSUserDefaults standardUserDefaults];
//        [finalCostSingleItem setValue:[NSString stringWithFormat:@"$%ld.00",(long)totalCost1] forKey:@"finalCostSingleItem"];
//        [finalCostSingleItem synchronize];
//        
//        NSUserDefaults *quantityValue=[NSUserDefaults standardUserDefaults];
//        [quantityValue setValue:strquantity forKey:@"Quantity"];
//        [quantityValue synchronize];
////    something = [[OrderPageViewController alloc] init];
////    [something somethingWithParam];
//    
//       
//        [[NSUserDefaults standardUserDefaults]setValue:@"YES" forKey:@"EDITCLICKED"];
//    [quantityText resignFirstResponder];
//    }
}

- (IBAction)editOrderAction:(id)sender
{
    if (self.orderpagecellDelegate != nil || [self.orderpagecellDelegate performSelector:@selector(editOrderClicked)])
    {
        [self.orderpagecellDelegate editOrderClicked:sender];
    }
}

- (IBAction)removeOrderAction:(id)sender
{
    if (self.orderpagecellDelegate != nil || [self.orderpagecellDelegate performSelector:@selector(removeOrderClicked)])
    {
        [self.orderpagecellDelegate removeOrderClicked:sender];
    }
}
@end
