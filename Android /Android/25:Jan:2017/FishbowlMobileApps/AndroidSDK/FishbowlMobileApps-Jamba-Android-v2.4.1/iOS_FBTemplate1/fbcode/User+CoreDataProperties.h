//
//  User+CoreDataProperties.h
//  iOS_FBTemplate1
//
//  Created by HARSH on 28/12/15.
//  Copyright © 2015 HARSH. All rights reserved.
//
//  Choose "Create NSManagedObject Subclass…" from the Core Data editor menu
//  to delete and recreate this implementation file for your updated model.
//

#import "User.h"

NS_ASSUME_NONNULL_BEGIN

@interface User (CoreDataProperties)

@property (nullable, nonatomic, retain) NSString *user_name;
@property (nullable, nonatomic, retain) NSString *email_address;
@property (nullable, nonatomic, retain) NSString *password;
@property (nullable, nonatomic, retain) NSString *phone_num;

@end

NS_ASSUME_NONNULL_END
