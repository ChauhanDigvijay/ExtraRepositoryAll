//
//  ProductCategory.m
//  Raley's
//
//  Created by Bill Lewis on 11/1/13.
//  Copyright (c) 2013 Bill Lewis. All rights reserved.
//

#import "ProductCategory.h"
#import "WebService.h"

@implementation ProductCategory

@synthesize productCategoryId;
@synthesize parentCategoryId;
@synthesize grandParentCategoryId;
@synthesize parentCategoryName;
@synthesize grandParentCategoryName;
@synthesize name;
@synthesize type;
@synthesize level;
@synthesize priority;
@synthesize subCategoryList;
@synthesize categoryList;

- (id)initWithName:(NSString *)categoryName categoryId:(int)categoryId categoryLevel:(int)categoryLevel
{
    self = [super init];

    if(self)
    {
        name = categoryName;
        productCategoryId = categoryId;
        level = categoryLevel;
        
        parentCategoryName=@"";
        grandParentCategoryName=@"";
    }

    return self;
}


- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    if([propertyName isEqualToString:@"categoryList"])
        return @"ProductCategory";
    if([propertyName isEqualToString:@"subCategoryList"])
        return @"ProductCategory";
    else
        return nil;
}

#pragma mark NSCoding Protocol
- (void)encodeWithCoder:(NSCoder *)encoder;
{
    [encoder encodeInt:self.productCategoryId forKey:@"productCategoryId"];
    [encoder encodeInt:self.parentCategoryId forKey:@"parentCategoryId"];
    [encoder encodeInt:self.grandParentCategoryId forKey:@"grandParentCategoryId"];
    [encoder encodeObject:self.name forKey:@"name"];
    [encoder encodeObject:self.type forKey:@"type"];
    [encoder encodeInt:self.level forKey:@"level"];
    [encoder encodeInt:self.priority forKey:@"priority"];
    [encoder encodeObject:self.subCategoryList forKey:@"subCategoryList"];
    [encoder encodeObject:self.categoryList forKey:@"categoryList"];
    
}

- (id)initWithCoder:(NSCoder *)decoder;
{
    self = [super init];

    if(self == nil)
        return nil;
    
    self.productCategoryId = [decoder decodeIntForKey:@"productCategoryId"];
    self.parentCategoryId = [decoder decodeIntForKey:@"parentCategoryId"];
    self.grandParentCategoryId = [decoder decodeIntForKey:@"grandParentCategoryId"];
    self.name = [decoder decodeObjectForKey:@"name"];
    self.type = [decoder decodeObjectForKey:@"type"];
    self.level = [decoder decodeIntForKey:@"level"];
    self.priority = [decoder decodeIntForKey:@"priority"];
    self.subCategoryList = [decoder decodeObjectForKey:@"subCategoryList"];
    self.categoryList = [decoder decodeObjectForKey:@"categoryList"];

    self.parentCategoryName = @"";
    self.grandParentCategoryName= @"";
    return self;
}

@end
