//
//  JsonObject.h
//  LibiOS
//
//  Created by Bill Lewis on 9/19/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "JsonObject.h"
#import "SBJson.h"
#import "Logging.h"

@implementation JsonObject

const char *property_getTypeString(objc_property_t property);

- (id) init
{
    if (self = [super init])
    {
    }
    
    return self;
}


// should be implemented by subclasses using NSArray types
- (NSString *)getComponentTypeForArray:(NSString *)propertyName
{
    return nil;
}


// should be implemented by subclasses that will have a different propertyName for a json key
- (NSString *)getJsonKeyForPropertyName:(NSString *)propertyName
{
    return nil;
}


// should be implemented by subclasses that will have a different propertyName for a json key
- (NSString *)getPropertyNameForJsonKey:(NSString *)jsonKey
{
    return nil;
}


+ (id)objectForJSON:(NSString *)inputJSON
{
    @synchronized(inputJSON)
    {
        SBJsonParser *parser = [[SBJsonParser alloc] init];
        NSDictionary *jDict = [parser objectWithString:inputJSON error:nil];
        return [self objectForDictionary:jDict];
    }
}


const char *property_getTypeString(objc_property_t property)
{
    const char *attrs = property_getAttributes(property);
    
    if(attrs == NULL)
        return(NULL);
    
    static char buffer[256];
    const char *e = strchr(attrs, ',');
    
    if(e == NULL)
        return(NULL);
    
    int len = (int)(e - attrs);
    memcpy(buffer, attrs, len);
    buffer[len] = '\0';
    return(buffer);
}


+ (BOOL)hasPropertyNamed:(NSString *)name
{
    return(class_getProperty(self, [name UTF8String]) != NULL);
}


+ (BOOL)hasPropertyForKey:(NSString *)key
{
    if([self hasPropertyNamed:key])
        return (YES);
    
    return NO;
}


+ (char *)typeOfPropertyNamed:(const char *)rawPropType
{
    int k = 0;
    char *parsedPropertyType = malloc(sizeof(char) * 16);
    
    if(*rawPropType == 'T')
        rawPropType++;
    else
        rawPropType = NULL;
    
    if(rawPropType == NULL)
    {
        free(parsedPropertyType);
        return NULL;
    }

    if(*rawPropType == '@')
    {
        rawPropType += 2;
        
        for(; *rawPropType != '\"';)
        {
            parsedPropertyType[k++] = *rawPropType;
            rawPropType++;
        }
        
        parsedPropertyType[k] = '\0';
        return parsedPropertyType;
    }
    else if(*rawPropType == 'i')
    {
        parsedPropertyType = strcpy(parsedPropertyType,"NSInteger\0");
        return parsedPropertyType;
    }
    else if(*rawPropType == 'd')
    {
        parsedPropertyType = strcpy(parsedPropertyType,"double\0");
        return parsedPropertyType;
    }
    else if(*rawPropType == 'f')
    {
        parsedPropertyType = strcpy(parsedPropertyType,"float\0");
        return parsedPropertyType;
    }
    else if(*rawPropType == 'c')
    {
        parsedPropertyType = strcpy(parsedPropertyType,"BOOL\0");
        return parsedPropertyType;
    }
    
    free(parsedPropertyType);
    return(NULL);
}


+ (NSMutableDictionary *)getPropertiesAndTypesForClassName:(const char *)className
{
    NSMutableDictionary * dict = [[NSMutableDictionary alloc] init];
    id class = objc_getClass(className);

    @synchronized(class)
    {
        if([class superclass] != [NSObject class])
        {
            dict = [JsonObject getPropertiesAndTypesForClassName:class_getName([class superclass])];
        }

        unsigned int outCount, i;
        objc_property_t *properties = class_copyPropertyList(class, &outCount);
        const char *objCType;

        for(i = 0; i < outCount; i++)
        {
            @try
            {
                objc_property_t property = properties[i];
                const char *propName = property_getName(property);
                const char *rawPropType = property_getTypeString(property);
                objCType = [self typeOfPropertyNamed:rawPropType];
                NSString *propertyName = [NSString stringWithCString:propName encoding:NSUTF8StringEncoding];

                if(objCType == NULL)
                {
                    NSLog(@"Invalid property type for propertyName %@. Skip ", propertyName);
                }
                else
                {
                    NSString *propertyType = [NSString stringWithCString:objCType encoding:NSUTF8StringEncoding];
                    [dict setValue:propertyType forKey:propertyName];
                }

                if(objCType != NULL)
                    free((char *)objCType);
            }
            @catch (NSException *exception)
            {
                if(objCType != NULL)
                    free((char *)objCType);
                
                LogError(@"Caught exception in getPropertiesAndTypesForClassName, exception:%@",[exception description]);
            }
        }
        
        free(properties);
        return dict;
    } // end @synchronized
}


+(BOOL)isPropertyTypeArray:(NSString *)propertyType
{
    if([propertyType isEqualToString:@"NSArray"] || [propertyType isEqualToString:@"NSMutableArray"])
        return YES;
    else
        return NO;
}


+(BOOL)isPropertyTypeBasic:(NSString *)propertyType 
{
    if([propertyType isEqualToString:@"NSString"] || [propertyType isEqualToString:@"NSNumber"] || [propertyType isEqualToString:@"NSInteger"] || [propertyType isEqualToString:@"int"] || [propertyType isEqualToString:@"float"] || [propertyType isEqualToString:@"double"] || [propertyType isEqualToString:@"BOOL"])        
        return YES;
    else
        return NO;
}

    
+(id)objectForPropertyKey:(NSString *)propertyType
{
    @synchronized(propertyType)
    {
        id kvcObject = [[NSClassFromString(propertyType) alloc] init];
        return kvcObject;
    }
}


+ (NSArray *)arrayForType:(NSString *)componentType withJSONArray:(NSArray *)jArray
{
    @synchronized(jArray)
    {
        if([componentType isEqualToString:@"NSString"] || [componentType isEqualToString:@"NSNumber"])
            return [NSArray arrayWithArray:jArray];

        NSMutableArray *resultArray = [[NSMutableArray alloc] init];

        for(NSDictionary *item in jArray)
        {
            Class childClass = NSClassFromString(componentType);
            id child = [childClass objectForDictionary:item];
            [resultArray addObject:child];
        }
        
        return resultArray;
    }
}


+ (id)objectForDictionary:(NSDictionary *)inputDict
{
    @synchronized(inputDict)
    {
        if([inputDict isKindOfClass:[NSNull class]])
            return nil;

        const char *className = class_getName([self class]);
        NSDictionary *propertyDict = [self getPropertiesAndTypesForClassName:className];
        NSArray *propertyKeys = [propertyDict allKeys];

        // create our object
        id jsonObject = [[NSClassFromString([NSString stringWithCString:className encoding:NSUTF8StringEncoding]) alloc] init];
        
        @try
        {
            for(NSString __strong *key in [inputDict allKeys])
            {
                id propertyValue = [inputDict objectForKey:key];

                if(![propertyKeys containsObject:key])
                    key = [jsonObject getPropertyNameForJsonKey:key];

                if(key)
                {
                    NSString *propType = [propertyDict objectForKey:key];

                    if(propType == nil) // ignore any invalid property types used by the client object
                        continue;

                    if([JsonObject isPropertyTypeArray:propType])
                    {
                        NSString *componentType = [jsonObject getComponentTypeForArray:key];
                        NSArray  *jArray = (NSArray *)propertyValue;

                        if(jArray != (id)[NSNull null])
                        {
                            // if the object has specified a type, create objects of that type. else set the array as such.
                            if([componentType length] > 1)
                            {
                                NSArray *componentArray = [JsonObject arrayForType:componentType withJSONArray:jArray];
                                [jsonObject setValue:componentArray forKey:key];
                            }
                            else
                            {
                                [jsonObject setValue:jArray forKey:key];
                            }
                        }
                    }
                    else if([JsonObject isPropertyTypeBasic:propType])
                    {
                        if(propertyValue != [NSNull null])
                            [jsonObject setValue:propertyValue forKey:key];
                    }
                    else
                    {
                        Class childClass = NSClassFromString(propType);

                        // if the component is not any primitive type or array create a custom object of it and pass the dictionary to it.
                        if([childClass isSubclassOfClass:[JsonObject class]])
                        {
                            id jsonChild = [childClass objectForDictionary:propertyValue];
                            [jsonObject setValue:jsonChild forKey:key];
                        }
                        else
                        {
                            [jsonObject setValue:propertyValue forKey:key];
                        }
                    }
                }        
            }
        }
        @catch(NSException* exception)
        {
            LogError(@"No keys found in Json dictionary for %s", className);
        }

        return jsonObject;
    } // end @synchronized
}


- (NSString *)customKeyForPropertyName:(NSString *)propertyName
{
    NSString *customJsonKey = [self getJsonKeyForPropertyName:propertyName];

    if(customJsonKey == nil)
        return propertyName;
    else
        return customJsonKey;
}


- (NSDictionary *)objectToDictionary
{
    const char *className = class_getName([self class]);
    NSDictionary *propertyDict = [JsonObject getPropertiesAndTypesForClassName:className];

    @synchronized(propertyDict)
    {
        NSMutableDictionary *resultDict = [[NSMutableDictionary alloc] init];

        for(NSString *currentProperty in propertyDict)
        {
            NSString *propType = [propertyDict objectForKey:currentProperty];

            if(propType == nil)
                continue;

            if([JsonObject isPropertyTypeArray:propType])
            {
                NSArray *objArray = [self valueForKey:currentProperty];

                if(objArray != nil && objArray != NULL && objArray.count > 0)
                {
                    id firstObject = [objArray objectAtIndex:0];

                    if([firstObject isKindOfClass:[NSString class]] || [firstObject isKindOfClass:[NSNumber class]])
                    {
                        [resultDict setValue:objArray forKey:[self customKeyForPropertyName:currentProperty]];
                    }
                    else
                    {
                        NSMutableArray *customObjArray = [[NSMutableArray alloc] init];

                        for(id arrayObj in objArray)
                        {
                            if([arrayObj isKindOfClass:[JsonObject class]])
                            {
                                NSDictionary * childDict = [arrayObj objectToDictionary];
                                [customObjArray addObject:childDict];
                            }
                        }

                        [resultDict setValue:customObjArray forKey:[self customKeyForPropertyName:currentProperty]];
                    }
                }
                else
                {
                    NSArray *emptyArray = [[NSArray alloc] init];
                    [resultDict setValue:emptyArray forKey:[self customKeyForPropertyName:currentProperty]];
                }
            }
            else if ([JsonObject isPropertyTypeBasic:propType])
            {
                id basicValue = [self valueForKey:currentProperty];

                if(basicValue != nil)
                    [resultDict setValue:basicValue forKey:[self customKeyForPropertyName:currentProperty]];
            }
            else
            {
                id jsonChild = [self valueForKey:currentProperty];

                if(jsonChild == nil)
                    jsonChild = [[JsonObject alloc] init];

                if([jsonChild isKindOfClass:[JsonObject class]])
                {
                    NSDictionary *childDict = [jsonChild objectToDictionary];
                    [resultDict setValue:childDict forKey:[self customKeyForPropertyName:currentProperty]];
                }
            }
        }
        
        return resultDict;
    } // end @synchronized
}


- (NSString *)objectToJson
{
    return [[self objectToDictionary] JSONRepresentation];
}
@end
