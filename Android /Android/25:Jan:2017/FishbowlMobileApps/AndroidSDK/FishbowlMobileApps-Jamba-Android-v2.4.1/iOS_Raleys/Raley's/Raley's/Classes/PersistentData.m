//
//  PersistentData.m
//  Raley's
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "PersistentData.h"

@implementation PersistentData

@synthesize _currentShoppingListId;
@synthesize _lastOfferUpdateTime;
@synthesize _lastProductCategoryCacheTime;
@synthesize _lastPromoCategoryCacheTime;
@synthesize _lastMissingImageUpdateTime;
@synthesize _login;
@synthesize _raleys;
@synthesize _homeStoreLocation;
@synthesize _storeList;


#pragma mark NSCoding Protocol
- (void)encodeWithCoder:(NSCoder *)encoder;
{
    [encoder encodeObject:[self _currentShoppingListId] forKey:@"_currentShoppingListId"];
    [encoder encodeInt64:[self _lastOfferUpdateTime] forKey:@"_lastOfferUpdateTime"];
    [encoder encodeInt64:[self _lastProductCategoryCacheTime] forKey:@"_lastProductCategoryCacheTime"];
    [encoder encodeInt64:[self _lastPromoCategoryCacheTime] forKey:@"_lastPromoCategoryCacheTime"];
    [encoder encodeInt64:[self _lastMissingImageUpdateTime] forKey:@"_lastMissingImageUpdateTime"];
    [encoder encodeObject:[self _login] forKey:@"_login"];
    [encoder encodeObject:[self _raleys] forKey:@"_raleys"];
    [encoder encodeObject:[self _homeStoreLocation] forKey:@"_homeStoreLocation"];
    [encoder encodeObject:[self _storeList] forKey:@"_storeList"];
    [encoder encodeObject:[self account] forKey:@"account"];
    
}

- (id)initWithCoder:(NSCoder *)decoder;
{
    self = [super init];

    if(self == nil)
        return nil;
    
    self._currentShoppingListId = [decoder decodeObjectForKey:@"_currentShoppingListId"];
    self._lastOfferUpdateTime = [decoder decodeInt64ForKey:@"_lastOfferUpdateTime"];
    self._lastProductCategoryCacheTime = [decoder decodeInt64ForKey:@"_lastProductCategoryCacheTime"];
    self._lastPromoCategoryCacheTime = [decoder decodeInt64ForKey:@"_lastPromoCategoryCacheTime"];
    self._lastMissingImageUpdateTime = [decoder decodeInt64ForKey:@"_lastMissingImageUpdateTime"];
    self._login = [decoder decodeObjectForKey:@"_login"];
    self._raleys = [decoder decodeObjectForKey:@"_raleys"];
    self._homeStoreLocation = [decoder decodeObjectForKey:@"_homeStoreLocation"];
    self._storeList = [decoder decodeObjectForKey:@"_storeList"];
    self.account = [decoder decodeObjectForKey:@"account"];

    return self;
}

@end
