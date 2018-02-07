//
//  JsonObject.h
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "objc/runtime.h"

@interface JsonObject : NSObject

// should be implemented by subclasses using NSArray types
- (NSString *) getComponentTypeForArray:(NSString *)propertyName;

// next two methods allow for different property name for a json key
- (NSString *) getPropertyNameForJsonKey:(NSString *)jsonKey;
- (NSString *) getJsonKeyForPropertyName:(NSString *)propertyName;

+ (JsonObject *) objectForJSON:(NSString *)inputJSON;
+ (JsonObject *) objectForDictionary:(NSDictionary *)inputDict;

+ (NSMutableDictionary *) getPropertiesAndTypesForClassName:(const char *)className;
- (NSDictionary *)objectToDictionary;
- (NSString *)objectToJson;

@end