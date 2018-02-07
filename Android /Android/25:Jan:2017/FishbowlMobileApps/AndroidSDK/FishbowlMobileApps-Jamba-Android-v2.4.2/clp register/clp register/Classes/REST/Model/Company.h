//
//  Company.h
//  clp register
//
//  Created by VT001 on 20/02/15.
//  Copyright (c) 2015 clyptech. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JSONModel.h"

@interface Company : JSONModel
@property (nonatomic, strong) NSString *companyBackGround;
@property (nonatomic, strong) NSString *companyLogo;
@property (nonatomic, strong) NSString *companyShortName;
@property (nonatomic, strong) NSString *message;
@property (nonatomic, assign) BOOL successFlag;
@property (nonatomic, strong) NSString *initialMessage;
@end

//        {
//            companyBackGround = "http://d10wbtjzs2ix9b.cloudfront.net/dev.clyptechs/dev/companydata/Screen Shot 2015-02-19 at 7.38.27 AM.png";
//            companyLogo = "http://d10wbtjzs2ix9b.cloudfront.net/dev.clyptechs/dev/companydata/Screen Shot 2015-02-19 at 7.40.09 AM.png";
//            companyShortName = "";
//            message = success;
//            successFlag = 1;
//            type = company;
//        }
