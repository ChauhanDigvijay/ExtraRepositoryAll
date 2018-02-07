//
//  Product.h
//  Raley's
//
//  Created by Bill Lewis on 10/9/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"

@interface Product : JsonObject <NSCoding>

@property (nonatomic, strong)NSString *sku;
@property (nonatomic, strong)NSString *upc;
@property (nonatomic, strong)NSString *brand;
@property (nonatomic, assign)int      storeNumber;
@property (nonatomic, strong)NSString *description;
@property (nonatomic, strong)NSString *extendedDisplay;
@property (nonatomic, assign)int      packSize;
@property (nonatomic, strong)NSString *unitOfMeasure;
@property (nonatomic, assign)int      regForQty;
@property (nonatomic, assign)float    regPrice;
@property (nonatomic, assign)float    salesTaxRate;
@property (nonatomic, strong)NSString *promoPriceText;
@property (nonatomic, assign)float    promoPrice;
@property (nonatomic, assign)int      promoForQty;
@property (nonatomic, assign)int      promoType;
@property (nonatomic, strong)NSString *promoStart;
@property (nonatomic, strong)NSString *promoEnd;
@property (nonatomic, assign)int      aisleNumber;
@property (nonatomic, strong)NSString *aisleSide;
@property (nonatomic, strong)NSString *imagePath;
@property (nonatomic, assign)int      qty;
@property (nonatomic, assign)float    weight;
@property (nonatomic, strong)NSString *sellByCode;
@property (nonatomic, strong)NSString *customerComment;
@property (nonatomic, assign)float    approxAvgWgt;
@property (nonatomic, strong)NSString *mainCategory;
@property (nonatomic, assign)float    crvAmount;
@property (nonatomic, strong)NSArray  *productList;

@property (nonatomic, strong)NSString *ingredients;


@end
