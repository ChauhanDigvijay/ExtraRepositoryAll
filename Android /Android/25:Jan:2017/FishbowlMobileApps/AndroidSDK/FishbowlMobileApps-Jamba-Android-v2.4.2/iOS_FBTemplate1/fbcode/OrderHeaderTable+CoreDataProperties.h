//
//  OrderHeaderTable+CoreDataProperties.h
//  iOS_FBTemplate1
//
//  Created by HARSH on 19/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//
//  Choose "Create NSManagedObject Subclass…" from the Core Data editor menu
//  to delete and recreate this implementation file for your updated model.
//

#import "OrderHeaderTable.h"

NS_ASSUME_NONNULL_BEGIN

@interface OrderHeaderTable (CoreDataProperties)

@property (nullable, nonatomic, retain) NSString *itemOrderNumber;
@property (nullable, nonatomic, retain) NSString *itemOrderDate;
@property (nullable, nonatomic, retain) NSString *itemorderTime;
@property (nullable, nonatomic, retain) NSString *itemOrderTotalPrice;
@property (nullable, nonatomic, retain) NSString *itemOrderStatus;
@property (nullable, nonatomic, retain) NSString *itemorderStoreId;

@end

NS_ASSUME_NONNULL_END
