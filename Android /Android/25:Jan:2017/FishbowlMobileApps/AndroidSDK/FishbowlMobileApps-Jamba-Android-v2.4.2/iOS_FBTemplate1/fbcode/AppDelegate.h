//
//  AppDelegate.h
//  taco2
//
//  Created by HARSH on 17/11/15.
//  Copyright © 2015 HARSH. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "clpsdk.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate,clpSdkDelegate>{
    clpsdk *_clpSDK;

}

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;
-(void)disableGesture;
-(void)enableGesture;
@end

