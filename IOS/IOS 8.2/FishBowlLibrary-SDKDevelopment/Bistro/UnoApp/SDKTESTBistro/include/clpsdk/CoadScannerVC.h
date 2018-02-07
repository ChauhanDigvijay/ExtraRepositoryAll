//
//  CoadScannerVC.h
//  clpsdk
//
//  Created by Gourav Shukla on 22/09/16.
//  Copyright © 2016 clyptech. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface CoadScannerVC : UIViewController
@property (unsafe_unretained, nonatomic) IBOutlet UIImageView *qrImageCode;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *usernameLbl;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *userPhoneLbl;
@property (unsafe_unretained, nonatomic) IBOutlet UILabel *barCodeLbl;

@end
