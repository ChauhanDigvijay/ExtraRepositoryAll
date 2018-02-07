//
//  OrderHistory+CoreDataProperties.h
//  iOS_FBTemplate1
//
//  Created by HARSH on 18/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//
//  Choose "Create NSManagedObject Subclass…" from the Core Data editor menu
//  to delete and recreate this implementation file for your updated model.
//

#import "OrderHistory.h"

NS_ASSUME_NONNULL_BEGIN

@interface OrderHistory (CoreDataProperties)

@property (nullable, nonatomic, retain) NSString *itemName;
@property (nullable, nonatomic, retain) NSString *itemCost;
@property (nullable, nonatomic, retain) NSString *itemTotalCost;
@property (nullable, nonatomic, retain) NSString *itemQuantity;
@property (nullable, nonatomic, retain) NSString *itemDesc;
@property (nullable, nonatomic, retain) NSString *itemImageurl;
@property (nullable, nonatomic, retain) NSString *itemID;
@property (nullable, nonatomic, retain) NSString *itemOrderNumber;
@property (nullable, nonatomic, retain) NSString *itemCount;
@property (nullable, nonatomic, retain) NSString *itemSize;
@end

NS_ASSUME_NONNULL_END
