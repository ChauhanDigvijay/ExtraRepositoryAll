//
//  XmlParser.m
//  XmlParser
//
//  Created by Saurabh Bisht on 1/25/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

#import "XmlParser.h"
#import "ServiceClient.h"
#import "ItemModel.h"


@interface XmlParser(){
    NSArray*tagsArray;
    ItemModel* model;
}

@end
@implementation XmlParser

@synthesize theDataArray;

- (id)initWithData:(NSData *)data{
    self = [super init];
    if (self) {
        dataToParse = [data copy];
        lastString = [[NSMutableString alloc] init];
        model  = [ItemModel sharedManager];
        //itemsDictionary = [[NSMutableArray alloc] init];
        tagsArray = [[NSArray alloc] initWithObjects:@"ItemGroup",@"Id",@"Item",@"GetItemsResult", nil];
        URL = nil;
    }
    return self;
}
- (id)initWithURL:(NSURL *)url{
    self = [super init];
    if (self) {
        dataToParse = nil;
        URL = [url copy];
    }
    return self;
}



-(void)startParser{
    ////Error --
    if (dataToParse == nil && URL == nil) {
        NSLog(@"nil parameters..");
        return;
    }
    
     NSLog(@"Parser started ...");
    
   // NSString *tagsPath = [[NSBundle mainBundle] pathForResource:@"SYXmlParserTags" ofType:@"plist"];
    tags = tagsArray;//[[NSArray alloc]initWithContentsOfFile:tagsPath];

    
    ////InitWithData --
    if (dataToParse != nil && URL == nil) {
        theDataArray = [[NSMutableArray alloc]init];
        parser = [[NSXMLParser alloc]initWithData:dataToParse];
        [parser setDelegate:self];
        [parser parse];
    }
    
    ////initWithURL	--
    if (URL != nil && dataToParse == nil) {
        theDataArray = [[NSMutableArray alloc]init];
        
        
        if ([NSData dataWithContentsOfURL:URL] == nil) {
            NSLog(@"Connection lost...");
           //connection lost
            theDataArray = nil;
        }
        else {
            parser = [[NSXMLParser alloc]initWithData:[NSData dataWithContentsOfURL:URL]];
            [parser setDelegate:self];
            [parser parse];
            
            
        }
    }
}

- (void)excludeKeys {
    NSArray* keys = [NSArray arrayWithObjects:
                     @"AvailableSelectDays",
                     @"int",
                     @"IsDeleted",
                     @"BrandAllocations",
                     @"RevenueCenterId",
                     @"Cost",
                     @"Active",
                     @"AskName",
                     @"VideoGroupId",
                     @"PLU",
                     @"SortPriority",
                     @"End",
                     @"UnitName",
                     @"UnitPrecision",
                     @"AvailableThursday",
                     @"AvailableSelectDates",
                     @"AvailableTuesday",
                     @"IsGiftCard",
                     @"IsQuantityCounted",
                     @"KitchenName",
                     @"TareId",
                     @"AvailableSunday",
                     @"AvailableFriday",
                     @"PrinterGroupId",
                     @"AvailableSaturday",
                     @"ModifierWeight",
                     @"PriceLevel",
                     @"AvailableMonday",
                     @"LastEditedTime",
                     @"Start",
                     @"TaxIds",
                     @"AvailableWednesday",nil];
    [item removeObjectsForKeys:keys];
     NSLog(@"Selecting Keys ...");
}

-(void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName attributes:(NSDictionary *)attributeDict{
    currentElement = elementName;
    currentString = [[NSMutableString alloc]init];
  
    if(![currentElement isEqual:@"b:int"])lastKeyElement = currentElement;
    
    if([currentElement  isEqual: @"a:IngredientItemIds"] || [currentElement  isEqual: @"a:ModifierGroupIds"]){
        self.dictTempDataStorage = [[NSMutableArray alloc]init];
      
    }
    
    
    for (int i=0; i<[tags count]; i++) {
        
        if ([[elementName
              stringByReplacingOccurrencesOfString:@"a:" withString:@""] isEqualToString:[tags objectAtIndex:i]]) {
            
            NSError *error;
            if(item!=nil && ([currentElement  isEqual: @"a:ItemGroup"] || [currentElement  isEqual: @"a:Item"]) && [item count] > 1){
                
                if([model itemsArray]!=nil){
                    if ([[model itemsArray] containsObject:[item objectForKey:@"Id"]]) {
                        [self excludeKeys];
                        NSLog(@"Adding item - %@ ...", [item objectForKey:@"Id"]);
                        [theDataArray addObject:[item copy]];
                        [self convertToJson:error];
                        [item removeAllObjects];
                    }
                }
                else{
                    [self excludeKeys];
                    [theDataArray addObject:[item copy]];
                    [self convertToJson:error];
                    [item removeAllObjects];
                }
                
            }
            
            item = [[NSMutableDictionary alloc]init];
            groupItems = [[NSMutableDictionary alloc]init];
            
        }
    }
}





//// CDATA Block --
-(void)parser:(NSXMLParser *)parser foundCDATA:(NSData *)CDATABlock{
    
    @try {
        currentString = nil;
        currentParsedData = [[NSString alloc]initWithData:CDATABlock encoding:NSUTF8StringEncoding];
        [item setObject:currentParsedData forKey:currentElement];
        
    }
    @catch (NSException * e) {
        NSLog(@"PARSER EXCEPTION foundCDATA: %@",[e description]);
    }
    @finally {
        
    }
}

- (NSMutableString*)commaSeperatedStringFromDictionary:(NSMutableArray*)dict{
    NSMutableString *large_CSV_String = [[NSMutableString alloc] init];
    for(NSString *aKey in dict){
        [large_CSV_String appendFormat:@",%@",aKey];
    }
    return large_CSV_String;
}

- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string{
    
    if ([currentElement isEqualToString:@"b:int"] ) {
        if (![string isEqualToString:@"\n"]) {
            [currentString appendString:string];
            lastKeyTempElement = lastKeyElement;
        }
    }
    else{
        
        if(self.dictTempDataStorage !=nil && lastKeyTempElement!=nil){
            [item setObject:[[self.dictTempDataStorage valueForKey:@"description"] componentsJoinedByString:@","] forKey:lastKeyTempElement];
            [self.dictTempDataStorage removeAllObjects];
            lastKeyElement = nil;
            lastKeyTempElement=nil;
        }
        
        [currentString appendString:string];
        
    }
}




- (void)convertToJson:(NSError *)error {
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:item
                                                       options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                         error:&error];
    
    if (! jsonData) {
        NSLog(@"Got an error: %@", error);
    } else {
        NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        NSLog(@"JsonData : %@", jsonString);
        [item removeAllObjects];
    }
}

-(void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName{
    for (int i=0; i<[tags count]; i++) {
        if ([elementName isEqualToString:[tags objectAtIndex:i]]) {
           // [theDataArray addObject:item];
            
        }
    }
    
    if ([elementName isEqualToString:@"b:int"]){
        [self.dictTempDataStorage addObject:[NSString stringWithString:currentString]];
    }
    
    if(currentString){
        
        //        ItemAttributes* itm = [ItemAttributes sharedManager];
        //        [itm setServiceType:@"ISettingsWebService"];
        //   [itm setMethodName:@"GetItemGroups"];
        
        if (![elementName isEqualToString:@"b:int"]){
            if(lastElement == currentElement){
                [currentString appendString:[NSString stringWithFormat:@",%@", lastString]];
            }
            
            lastElement = currentElement;
            lastString = currentString;
            [item setObject:[currentString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] forKey:[[currentElement stringByReplacingOccurrencesOfString:@"a:" withString:@""] stringByReplacingOccurrencesOfString:@"b:" withString:@""]];
            currentString = nil;
            
        }
        
    }
    
}

-(void)parserDidStartDocument:(NSXMLParser *)parser{
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
}

- (void)parserDidEndDocument:(NSXMLParser *)parser{
    NSLog(@"Parsing finished...");
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
}

- (void)parser:(NSXMLParser *)parser parseErrorOccurred:(NSError *)parseError{
    [UIApplication sharedApplication].networkActivityIndicatorVisible =NO;
    NSLog(@"Parser Error:%@",[parseError description]);
}

@end
