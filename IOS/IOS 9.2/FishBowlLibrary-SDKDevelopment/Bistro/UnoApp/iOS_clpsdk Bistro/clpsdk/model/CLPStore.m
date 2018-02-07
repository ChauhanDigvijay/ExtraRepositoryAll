//
//  Store.m
//  clpsdk
//
//  Created by VT02 on 1/7/15.
//  Copyright (c) 2015 com.clp. All rights reserved.
//

#import "CLPStore.h"

@implementation CLPStore

 - (void)encodeWithCoder:(NSCoder *)encoder {
    //Encode properties, other class variables, etc
    [encoder encodeObject:self.storeName forKey:@"storeName"];
    [encoder encodeObject:self.contactPerson forKey:@"contactPerson"];
    [encoder encodeObject:self.address forKey:@"address"];
    [encoder encodeObject:[NSString stringWithFormat:@"%i",self.storeNumber] forKey:@"storeNumber"];
     [encoder encodeObject:[NSString stringWithFormat:@"%i",self.companyID] forKey:@"companyID"];
     [encoder encodeObject:[NSString stringWithFormat:@"%i",self.storeID] forKey:@"storeID"];

    [encoder encodeObject:self.city forKey:@"city"];
    [encoder encodeObject:self.state forKey:@"state"];
    [encoder encodeObject:self.zip forKey:@"zip"];
    [encoder encodeObject:self.country forKey:@"country"];
    [encoder encodeObject:self.phone forKey:@"phone"];
    [encoder encodeObject:self.mobile forKey:@"mobile"];
    
    [encoder encodeObject:self.email forKey:@"email"];
    [encoder encodeObject:self.latitude forKey:@"latitude"];
    [encoder encodeObject:self.longitude forKey:@"longitude"];
    [encoder encodeObject:self.enableGeoConquest forKey:@"enableGeoConquest"];
    [encoder encodeObject:self.createDate forKey:@"createDate"];
    [encoder encodeObject:self.updateDate forKey:@"updateDate"];
}

- (id)initWithCoder:(NSCoder *)decoder {
    if((self = [super init])) {
        //decode properties, other class vars
        self.storeName = [decoder decodeObjectForKey:@"storeName"];
        self.contactPerson = [decoder decodeObjectForKey:@"contactPerson"];
        self.storeID = [[decoder decodeObjectForKey:@"storeID"] intValue];
        self.companyID = [[decoder decodeObjectForKey:@"companyID"] intValue];
        self.storeNumber = [[decoder decodeObjectForKey:@"storeNumber"] intValue];
        self.address = [decoder decodeObjectForKey:@"address"];
        self.city = [decoder decodeObjectForKey:@"city"];
        self.state = [decoder decodeObjectForKey:@"state"];
        self.zip = [decoder decodeObjectForKey:@"zip"];
        self.country = [decoder decodeObjectForKey:@"country"];
        self.phone = [decoder decodeObjectForKey:@"phone"];
        self.mobile = [decoder decodeObjectForKey:@"mobile"];
        
        self.email = [decoder decodeObjectForKey:@"email"];
        self.latitude = [decoder decodeObjectForKey:@"latitude"];
        self.longitude = [decoder decodeObjectForKey:@"longitude"];
        self.enableGeoConquest = [decoder decodeObjectForKey:@"enableGeoConquest"];
        self.createDate = [decoder decodeObjectForKey:@"createDate"];
        self.updateDate = [decoder decodeObjectForKey:@"updateDate"];
    }
    return self;
}

@end
