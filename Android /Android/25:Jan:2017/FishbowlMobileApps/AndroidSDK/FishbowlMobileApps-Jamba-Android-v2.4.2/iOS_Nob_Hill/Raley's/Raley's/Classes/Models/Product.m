//
//  Product.m
//  Raley's
//
//  Created by Bill Lewis on 10/9/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "Product.h"

@implementation Product

@synthesize sku;
@synthesize upc;
@synthesize brand;
@synthesize storeNumber;
@synthesize description;
@synthesize extendedDisplay;
@synthesize packSize;
@synthesize unitOfMeasure;
@synthesize regForQty;
@synthesize regPrice;
@synthesize salesTaxRate;
@synthesize promoPriceText;
@synthesize promoPrice;
@synthesize promoForQty;
@synthesize promoType;
@synthesize promoStart;
@synthesize promoEnd;
@synthesize aisleNumber;
@synthesize aisleSide;
@synthesize imagePath;
@synthesize qty;
@synthesize weight;
@synthesize sellByCode;
@synthesize customerComment;
@synthesize approxAvgWgt;
@synthesize mainCategory;
@synthesize crvAmount;
@synthesize productList;

@synthesize ingredients;

- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"productList"])
        return @"Product";
    else
        return nil;
}


#pragma mark NSCoding Protocol
- (void)encodeWithCoder:(NSCoder *)encoder;
{
    [encoder encodeObject:self.sku forKey:@"sku"];
    [encoder encodeObject:self.upc forKey:@"upc"];
    [encoder encodeObject:self.brand forKey:@"brand"];
    [encoder encodeInt:self.storeNumber forKey:@"storeNumber"];
    [encoder encodeObject:self.description forKey:@"description"];
    [encoder encodeObject:self.extendedDisplay forKey:@"extendedDisplay"];
    [encoder encodeInteger:self.packSize forKey:@"packSize"];
    [encoder encodeObject:self.unitOfMeasure forKey:@"unitOfMeasure"];
    [encoder encodeInteger:self.regForQty forKey:@"regForQty"];
    [encoder encodeFloat:self.regPrice forKey:@"regPrice"];
    [encoder encodeFloat:self.salesTaxRate forKey:@"salesTaxRate"];
    [encoder encodeObject:self.promoPriceText forKey:@"promoPriceText"];
    [encoder encodeFloat:self.promoPrice forKey:@"promoPrice"];
    [encoder encodeInteger:self.promoForQty forKey:@"promoForQty"];
    [encoder encodeInteger:self.promoType forKey:@"promoType"];
    [encoder encodeObject:self.promoStart forKey:@"promoStart"];
    [encoder encodeObject:self.promoEnd forKey:@"promoEnd"];
    [encoder encodeInteger:self.aisleNumber forKey:@"aisleNumber"];
    [encoder encodeObject:self.aisleSide forKey:@"aisleSide"];
    [encoder encodeObject:self.imagePath forKey:@"imagePath"];
    [encoder encodeInteger:self.qty forKey:@"qty"];
    [encoder encodeFloat:self.weight forKey:@"weight"];
    [encoder encodeObject:self.sellByCode forKey:@"sellByCode"];
    [encoder encodeObject:self.customerComment forKey:@"customerComment"];
    [encoder encodeFloat:self.approxAvgWgt forKey:@"approxAvgWgt"];
    [encoder encodeObject:self.mainCategory forKey:@"mainCategory"];
    [encoder encodeFloat:self.crvAmount forKey:@"crvAmount"];
    [encoder encodeObject:self.productList forKey:@"productList"];
    
    [encoder encodeObject:self.ingredients forKey:@"ingredients"];
}


- (id)initWithCoder:(NSCoder *)decoder;
{
    self = [super init];

    if(self == nil)
        return nil;

    self.sku = [decoder decodeObjectForKey:@"sku"];
    self.upc = [decoder decodeObjectForKey:@"upc"];
    self.brand = [decoder decodeObjectForKey:@"brand"];
    self.storeNumber = [decoder decodeIntForKey:@"storeNumber"];
    self.description = [decoder decodeObjectForKey:@"description"];
    self.extendedDisplay = [decoder decodeObjectForKey:@"extendedDisplay"];
    self.packSize = (int)[decoder decodeIntegerForKey:@"packSize"];
    self.unitOfMeasure = [decoder decodeObjectForKey:@"unitOfMeasure"];
    self.regForQty = (int)[decoder decodeIntegerForKey:@"regForQty"];
    self.regPrice = [decoder decodeFloatForKey:@"regPrice"];
    self.salesTaxRate = [decoder decodeFloatForKey:@"salesTaxRate"];
    self.promoPriceText = [decoder decodeObjectForKey:@"promoPriceText"];
    self.promoPrice = [decoder decodeFloatForKey:@"promoPrice"];
    self.promoForQty = (int)[decoder decodeIntegerForKey:@"promoForQty"];
    self.promoType = (int)[decoder decodeIntegerForKey:@"promoType"];
    self.promoStart = [decoder decodeObjectForKey:@"promoStart"];
    self.promoEnd = [decoder decodeObjectForKey:@"promoEnd"];
    self.aisleNumber = (int)[decoder decodeIntegerForKey:@"aisleNumber"];
    self.aisleSide = [decoder decodeObjectForKey:@"aisleSide"];
    self.imagePath = [decoder decodeObjectForKey:@"imagePath"];
    self.qty = (int)[decoder decodeIntegerForKey:@"qty"];
    self.weight = [decoder decodeFloatForKey:@"weight"];
    self.sellByCode = [decoder decodeObjectForKey:@"sellByCode"];
    self.customerComment = [decoder decodeObjectForKey:@"customerComment"];
    self.approxAvgWgt = [decoder decodeFloatForKey:@"approxAvgWgt"];
    self.mainCategory = [decoder decodeObjectForKey:@"mainCategory"];
    self.crvAmount = [decoder decodeFloatForKey:@"crvAmount"];
    self.productList = [decoder decodeObjectForKey:@"productList"];
    
    self.ingredients=[decoder decodeObjectForKey:@"ingredients"];

    return self;
}


@end
