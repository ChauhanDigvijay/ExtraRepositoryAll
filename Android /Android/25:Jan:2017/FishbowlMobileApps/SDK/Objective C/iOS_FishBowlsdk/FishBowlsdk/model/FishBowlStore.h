//
//  Store.h
//  clpsdk
//
//  Created by VT02 on 1/7/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JSONModel.h"

@interface FishBowlStore : JSONModel
@property (assign, nonatomic) int               storeID;
@property (assign, nonatomic) int               companyID;
@property (strong, nonatomic) NSString          *storeName;
@property (strong, nonatomic) NSString          *contactPerson;
@property (strong, nonatomic) NSString          *address;
@property (strong, nonatomic) NSString          *city;
@property (strong, nonatomic) NSString          *state;
@property (strong, nonatomic) NSString          *zip;
@property (strong, nonatomic) NSString          *country;
@property (strong, nonatomic) NSString          *phone;
@property (strong, nonatomic) NSString          *mobile;
@property (strong, nonatomic) NSString          *email;
@property (strong, nonatomic) NSString          *latitude;
@property (strong, nonatomic) NSString          *longitude;
@property (assign, nonatomic) float             geoFenceCorrFactor;
@property (assign, nonatomic) int               storeNumber;
@property (strong, nonatomic) NSString          *enableGeoConquest;
@property (strong, nonatomic) NSString          *createDate;
@property (strong, nonatomic) NSString          *updateDate;
@end
