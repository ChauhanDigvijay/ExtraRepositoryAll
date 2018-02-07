//
//  ItemsData.m
//  FBBrinkPosLibrary
//
//  Created by Saurabh Bisht on 1/25/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

#import "ItemsData.h"
#import "ServiceClient.h"
#import "ItemModel.h"

@interface ItemsData (){
    ServiceClient* client;
    NSMutableArray *arrayTags;
    NSMutableArray *arrayVars;
    ItemModel* model;
}
@end

@implementation ItemsData

-(void)initService:(id)instance{
    arrayTags       = [[NSMutableArray alloc]init];
    [arrayTags      addObjectsFromArray:@[ATOKEN, LTOKEN]];
    arrayVars       = [[NSMutableArray alloc]init];
    [arrayVars      addObjectsFromArray:@[ ACCESSTOKEN, LOCATIONTOKEN]];
    client          = [[ServiceClient alloc] init];
    client.delegate = instance;
    model           = [ItemModel sharedManager];
}
-(void)getAllGroups:(id)instance{
    [self initService:instance];
    [client callSoapServiceWithParameters__functionName:SETTING_METHOD_GETGROUPS tags:arrayTags vars:arrayVars wsdlURL:SERVICE_SETTING_LINK service:SERVICE_SETTING];
}

-(void)getAllItems:(id)instance{
    [self initService:instance];
    client.delegate = instance;
    [client callSoapServiceWithParameters__functionName:SETTING_METHOD_GETITEMS tags:arrayTags vars:arrayVars wsdlURL:SERVICE_SETTING_LINK service:SERVICE_SETTING];
}

-(void)getItems:(NSString*)itemIds instance:(id)instance{
    [self initService:instance];
    model.itemsArray = [itemIds componentsSeparatedByString:@","];
    [client callSoapServiceWithParameters__functionName:SETTING_METHOD_GETITEMS tags:arrayTags vars:arrayVars wsdlURL:SERVICE_SETTING_LINK service:SERVICE_SETTING];
}

@end
