//
//  StoreLocatorScreenVC.h
//  Raley's
//
//  Created by Billy Lewis on 9/24/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "ChangeStoreRequest.h"
#import "BaseScreenVC.h"
#import "StoreCallout.h"
#import "Store.h"
#import "StoreLocatorScreenDelegate.h"
#import "WebService.h"

@interface StoreLocatorScreenVC : BaseScreenVC <MKMapViewDelegate, UITableViewDataSource, UITableViewDelegate, WebServiceListener, StoreLocatorScreenDelegate,UIScrollViewDelegate>
{
    int          _storeTableWidth;
    int          _storeTableCellHeight;
    double       _mapArea;
    UIView       *_locatorView;
    UITableView  *_storeTable;
    UIButton     *_mapViewButton;
    UIButton     *_listViewButton;
    MKMapView    *_mapView;
    NSArray      *_sortedStoresList;
    ChangeStoreRequest  *_request;
    StoreCallout        *_storeCallout;
    Store               *_currentStore;
    WebService          *_service;
    UIView  *_mapViewHolder;
    UIView  *_listViewHolder;
    UIView                      *_menuView;
    CGRect    _menuVisibleFrame;
    CGRect    _menuHiddenFrame;
    UIButton                    *_moreButton;
    
    UIImageView *map_overlay_imgview;
    int map_current_screen_index_value;
}


@end
