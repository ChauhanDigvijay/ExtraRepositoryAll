//
//  XmlParser.h
//  XmlParser
//
//  Created by Saurabh Bisht on 1/25/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface XmlParser : NSObject <NSXMLParserDelegate>{
    NSURL *URL;
    NSData *dataToParse;
    NSArray *tags;
    NSXMLParser *parser;
    NSString *currentParsedData;
    NSString *currentElement;
    NSMutableString *currentString;
    NSString *lastKeyElement;
    NSString *lastKeyTempElement;
    
    NSString *lastElement;
    NSMutableString *lastString;
    NSMutableDictionary *item;
    NSMutableDictionary *groupItems;
    NSMutableDictionary *menuItemDictionay;
    NSMutableArray *tagList;
    
   // NSMutableArray *itemsDictionary;
    
    
}
@property(nonatomic,strong)NSMutableArray *theDataArray;
@property(nonatomic,strong)NSMutableArray* dictTempDataStorage;;
- (id)initWithURL:(NSURL *)url;
- (id)initWithData:(NSData *)data;

//@property(nonatomic,retain)NSMutableArray *theDataArray;

-(void)startParser;

@end
