//
//  AccountRequest.m
//  Raley's
//
//  Created by Billy Lewis on 10/6/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import "AccountRequest.h"

@implementation AccountRequest

@synthesize crmNumber;
@synthesize accountId;
@synthesize password;
@synthesize favoriteDept;
@synthesize linkSource;
@synthesize modifiedBy;
@synthesize email;
@synthesize firstName;
@synthesize lastName;
@synthesize employee;
@synthesize employeeID;
@synthesize cardStatus;
@synthesize phone;
@synthesize mobilePhone;
@synthesize address;
@synthesize city;
@synthesize state;
@synthesize zip;
@synthesize loyaltyNumber;
@synthesize middleName;
@synthesize customerStatus;
@synthesize prefix;
@synthesize suffix;
@synthesize voucherMethod;
@synthesize sendEmailsFlag;
@synthesize sendTextsFlag;
@synthesize issueCardFlag;
@synthesize termsAcceptedFlag;
@synthesize storeNumber;
@synthesize dateOfBirth;
@synthesize enrollmentDate;
@synthesize dateCreated;
@synthesize dateModified;



- (void)encodeWithCoder:(NSCoder *)encoder;
{
    [encoder encodeObject:crmNumber forKey:@"crmNumber"];
    [encoder encodeObject:accountId forKey:@"accountId"];
    [encoder encodeObject:password forKey:@"password"];
    [encoder encodeObject:favoriteDept forKey:@"favoriteDept"];
    [encoder encodeObject:linkSource forKey:@"linkSource"];
    [encoder encodeObject:modifiedBy forKey:@"modifiedBy"];
    [encoder encodeObject:email forKey:@"email"];
    [encoder encodeObject:firstName forKey:@"firstName"];
    [encoder encodeObject:lastName forKey:@"lastName"];
    [encoder encodeObject:employee forKey:@"employee"];
    [encoder encodeObject:employeeID forKey:@"employeeID"];
    [encoder encodeObject:cardStatus forKey:@"cardStatus"];
    [encoder encodeObject:phone forKey:@"phone"];
    [encoder encodeObject:mobilePhone forKey:@"mobilePhone"];
    [encoder encodeObject:address forKey:@"address"];
    [encoder encodeObject:city forKey:@"city"];
    [encoder encodeObject:state forKey:@"state"];
    [encoder encodeObject:zip forKey:@"zip"];
    [encoder encodeObject:loyaltyNumber forKey:@"loyaltyNumber"];
    [encoder encodeObject:middleName forKey:@"middleName"];
    [encoder encodeObject:customerStatus forKey:@"customerStatus"];
    [encoder encodeObject:prefix forKey:@"prefix"];
    [encoder encodeObject:suffix forKey:@"suffix"];
    [encoder encodeObject:voucherMethod forKey:@"voucherMethod"];
    [encoder encodeObject:sendEmailsFlag forKey:@"sendEmailsFlag"];
    [encoder encodeObject:sendTextsFlag forKey:@"sendTextsFlag"];
    [encoder encodeObject:issueCardFlag forKey:@"issueCardFlag"];
    [encoder encodeObject:termsAcceptedFlag forKey:@"termsAcceptedFlag"];
    [encoder encodeObject:[NSNumber numberWithInt:storeNumber] forKey:@"storeNumber"];
    [encoder encodeObject:dateOfBirth forKey:@"dateOfBirth"];
    [encoder encodeObject:enrollmentDate forKey:@"enrollmentDate"];
    [encoder encodeObject:dateCreated forKey:@"dateCreated"];
    [encoder encodeObject:dateModified forKey:@"dateModified"];
}

- (id)initWithCoder:(NSCoder *)decoder;
{
    self = [super init];
    
    if(self == nil)
        return nil;
    
    crmNumber= [decoder decodeObjectForKey:@"crmNumber"];
    accountId= [decoder decodeObjectForKey:@"accountId"];
    password= [decoder decodeObjectForKey:@"password"];
    favoriteDept= [decoder decodeObjectForKey:@"favoriteDept"];
    linkSource= [decoder decodeObjectForKey:@"linkSource"];
    modifiedBy= [decoder decodeObjectForKey:@"modifiedBy"];
    email= [decoder decodeObjectForKey:@"email"];
    firstName= [decoder decodeObjectForKey:@"firstName"];
    lastName= [decoder decodeObjectForKey:@"lastName"];
    employee= [decoder decodeObjectForKey:@"employee"];
    employeeID= [decoder decodeObjectForKey:@"employeeID"];
    cardStatus= [decoder decodeObjectForKey:@"cardStatus"];
    phone= [decoder decodeObjectForKey:@"phone"];
    mobilePhone= [decoder decodeObjectForKey:@"mobilePhone"];
    address= [decoder decodeObjectForKey:@"address"];
    city= [decoder decodeObjectForKey:@"city"];
    state= [decoder decodeObjectForKey:@"state"];
    zip= [decoder decodeObjectForKey:@"zip"];
    loyaltyNumber= [decoder decodeObjectForKey:@"loyaltyNumber"];
    middleName= [decoder decodeObjectForKey:@"middleName"];
    customerStatus= [decoder decodeObjectForKey:@"customerStatus"];
    prefix= [decoder decodeObjectForKey:@"prefix"];
    suffix= [decoder decodeObjectForKey:@"suffix"];
    voucherMethod= [decoder decodeObjectForKey:@"voucherMethod"];
    sendEmailsFlag= [decoder decodeObjectForKey:@"sendEmailsFlag"];
    sendTextsFlag= [decoder decodeObjectForKey:@"sendTextsFlag"];
    issueCardFlag= [decoder decodeObjectForKey:@"issueCardFlag"];
    termsAcceptedFlag= [decoder decodeObjectForKey:@"termsAcceptedFlag"];
    storeNumber= [[decoder decodeObjectForKey:@"storeNumber"]intValue];
    dateOfBirth= [decoder decodeObjectForKey:@"dateOfBirth"];
    enrollmentDate= [decoder decodeObjectForKey:@"enrollmentDate"];
    dateCreated= [decoder decodeObjectForKey:@"dateCreated"];
    dateModified= [decoder decodeObjectForKey:@"dateModified"];
    
    return self;
    
}


@end
