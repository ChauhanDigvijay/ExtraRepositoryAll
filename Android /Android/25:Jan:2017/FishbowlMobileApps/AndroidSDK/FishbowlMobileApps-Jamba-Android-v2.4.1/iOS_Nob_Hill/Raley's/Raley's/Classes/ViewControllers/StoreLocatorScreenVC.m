//
//  StoreLocatorScreenVC.m
//  Raley's
//
//  Created by Billy Lewis on 9/24/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//


#import "ChangeStoreRequest.h"
#import "ListRequest.h"
#import "Login.h"
#import "StoreLocatorScreenVC.h"
#import "MapAnnotation.h"
#import "MapAnnotationView.h"
#import "StoreCallout.h"
#import "StoreTableCell.h"
#import "Utility.h"
#import "Logging.h"
#import "SignUpViewController.h"

#define DEFAULT_MAP_AREA 10.0
#define Retina4 ([UIScreen mainScreen].bounds.size.height == 568.f)

@interface StoreLocatorScreenVC (){
    UIImageView *headerImageView;
    BOOL firstLocationUpdate;
}
@end

@implementation StoreLocatorScreenVC


- (void)viewDidLoad
{
    [super viewDidLoad];
    firstLocationUpdate = NO;
     _contentView.frame = CGRectMake(0, _app._headerHeight, _app._viewWidth, _app._viewHeight - _app._headerHeight - _app._headerHeight);
    
    
    
	// Do any additional setup after loading the view.
    _backButton.hidden = NO;

    // navigation bar buttons
    int buttonCount = 2;
    int buttonWidth = _app._viewWidth / buttonCount;
    int lastButtonPad = _app._viewWidth - (buttonWidth * buttonCount);
    int buttonIndex = 0;
//    UIFont *navigationBarFont = [Utility fontForFamily:_normalFont andHeight:_navigationBarHeight * .5];
    
    
    
    int buttonPad = _app._viewWidth * .02;
   
    
    /*UIImageView *headerImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 320, 35.0)];
    [headerImageView setImage:[UIImage imageNamed:@"header"]];
    [View addSubview:headerImageView];*/
    
    int itemHeight = _app._headerHeight;
    int baselineHeight = itemHeight * .05;
    
    /*  -- My code  */
    
    
    // more menu
    int menuButtonIndex = 0;
    UIFont *menuButtonFont = [Utility fontForSize:_normalFont forString:@"Accepted Offers" forSize:CGSizeMake(_menuViewWidth * .9, _menuButtonHeight * .8)];
    
    UIButton *accountButton = [[UIButton alloc] initWithFrame:CGRectMake(0, _menuButtonHeight * menuButtonIndex++, _menuViewWidth, _menuButtonHeight)];
    [accountButton setBackgroundImage:[UIImage imageNamed:@"drop_menu_top_unselected"] forState:UIControlStateNormal];
    [accountButton setBackgroundImage:[UIImage imageNamed:@"drop_menu_top_selected"] forState:UIControlStateSelected];
    accountButton.titleLabel.font = menuButtonFont;
    [accountButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [accountButton addTarget:self action:@selector(accountButtonPressed) forControlEvents:UIControlEventTouchDown];
    [accountButton setTitle:@"My Account" forState:UIControlStateNormal];
    
//    UIButton *storeLocatorButton = [[UIButton alloc] initWithFrame:CGRectMake(0, _menuButtonHeight * menuButtonIndex++, _menuViewWidth, _menuButtonHeight)];
//    [storeLocatorButton setBackgroundImage:[UIImage imageNamed:@"drop_menu_unselected"] forState:UIControlStateNormal];
//    [storeLocatorButton setBackgroundImage:[UIImage imageNamed:@"drop_menu_selected"] forState:UIControlStateSelected];
//    storeLocatorButton.titleLabel.font = menuButtonFont;
//    [storeLocatorButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//    [storeLocatorButton addTarget:self action:@selector(storeLocatorButtonPressed) forControlEvents:UIControlEventTouchDown];
//    [storeLocatorButton setTitle:@"Store Locator" forState:UIControlStateNormal];
    
//    UIButton *helpInstruction = [[UIButton alloc] initWithFrame:CGRectMake(0, _menuButtonHeight * menuButtonIndex++, _menuViewWidth, _menuButtonHeight)];
//    [helpInstruction setBackgroundImage:[UIImage imageNamed:@"drop_menu_unselected"] forState:UIControlStateNormal];
//    [helpInstruction setBackgroundImage:[UIImage imageNamed:@"drop_menu_selected"] forState:UIControlStateSelected];
//    helpInstruction.titleLabel.font = menuButtonFont;
//    [helpInstruction setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
//    [helpInstruction addTarget:self action:@selector(Show_map_overlay:) forControlEvents:UIControlEventTouchDown];
//    [helpInstruction setTitle:@"Help" forState:UIControlStateNormal];
    
    UIButton *signOutButton = [[UIButton alloc] initWithFrame:CGRectMake(0, _menuButtonHeight * menuButtonIndex++, _menuViewWidth, _menuButtonHeight)];
    [signOutButton setBackgroundImage:[UIImage imageNamed:@"drop_menu_unselected"] forState:UIControlStateNormal];
    [signOutButton setBackgroundImage:[UIImage imageNamed:@"drop_menu_selected"] forState:UIControlStateSelected];
    signOutButton.titleLabel.font = menuButtonFont;
    [signOutButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [signOutButton addTarget:self action:@selector(signOutButtonPressed) forControlEvents:UIControlEventTouchDown];
    [signOutButton setTitle:@"Sign Out" forState:UIControlStateNormal];
    
    UIImageView *baselineImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, _menuButtonHeight * menuButtonIndex, _menuViewWidth, baselineHeight)];
    [baselineImage setImage:[UIImage imageNamed:@"drop_menu_baseline"]];
    
    _menuVisibleFrame = CGRectMake(_contentViewWidth - _menuViewWidth, _app._headerHeight, _menuViewWidth, (_menuButtonHeight * menuButtonIndex) + baselineHeight);
    _menuHiddenFrame = CGRectMake(_contentViewWidth - _menuViewWidth, -(_menuButtonHeight * menuButtonIndex), _menuViewWidth, (_menuButtonHeight * menuButtonIndex) + baselineHeight);
    _menuView = [[UIView alloc] initWithFrame:_menuHiddenFrame];
    [_menuView addSubview:accountButton];
    //[_menuView addSubview:helpInstruction];
    [_menuView addSubview:signOutButton];
    [_menuView addSubview:baselineImage];
    [View addSubview:_menuView];
    
    // more menu end
    
    [self addHeader];
    
//    int moreButtonSize = _app._headerHeight * .6;
    int moreButtonWidth = _app._headerHeight;
    int moreButtonHeight = _app._headerHeight;
    _moreButton = [[UIButton alloc] initWithFrame:CGRectMake(_app._viewWidth - moreButtonWidth - buttonPad, (_app._headerHeight - moreButtonHeight) / 2, moreButtonWidth, moreButtonHeight)];
    [_moreButton setBackgroundImage:[UIImage imageNamed:@"more_button"] forState:UIControlStateNormal];
    [_moreButton setBackgroundImage:[UIImage imageNamed:@"more_button_selected"] forState:UIControlStateSelected];
    [_moreButton addTarget:self action:@selector(moreButtonPressed) forControlEvents:UIControlEventTouchDown];
    
//    _app = (id)[[UIApplication sharedApplication] delegate];
    if([_app isLoggedIn])
    {
        [View addSubview:_moreButton];
    }
    
    // stupid hack to make iOS7 status bar non-translucent
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
    {
        UIView *statusBarBackground = [[UIView alloc] initWithFrame:CGRectMake(0, 0, _app._viewWidth, 20)];
        statusBarBackground.backgroundColor = [UIColor blackColor];
        [self.view addSubview:statusBarBackground];
    }

    
    
   // _mapViewHolder = [[UIView alloc]initWithFrame:CGRectMake(buttonWidth * buttonIndex++, 0, buttonWidth, _navigationBarHeight)];
    
    

    
    _mapViewButton = [[UIButton alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, 0, buttonWidth, _navigationBarHeight * 0.7)];
    [_mapViewButton setBackgroundImage:[UIImage imageNamed:@"locator_tab_left_n"] forState:UIControlStateNormal];
    [_mapViewButton setBackgroundImage:[UIImage imageNamed:@"locator_tab_left"] forState:UIControlStateSelected];
    _mapViewButton.adjustsImageWhenHighlighted = NO;
//    [_mapViewButton setAlpha:0.8];
    //[_mapViewButton setTitleColor:[UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:.6] forState:UIControlStateNormal];
   /* [_mapViewButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [_mapViewButton setTitleColor:[UIColor whiteColor] forState:UIControlStateHighlighted];
    [_mapViewButton setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    _mapViewButton.titleLabel.font = navigationBarFont;
    _mapViewButton.titleLabel.textAlignment = NSTextAlignmentLeft;
    [_mapViewButton setTitleEdgeInsets:UIEdgeInsetsMake(0.0, -70.0, 0.0, 0.0)];
    [_mapViewButton setTitle:@"Map" forState:UIControlStateNormal]; */
    [_mapViewButton addTarget:self action:@selector(mapButtonPressed) forControlEvents:UIControlEventTouchDown];
   // [_contentView addSubview:_mapViewButton];
    _mapViewButton.selected = YES;
    
    _listViewButton = [[UIButton alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, 0, buttonWidth + lastButtonPad, _navigationBarHeight * 0.7)];
    [_listViewButton setBackgroundImage:[UIImage imageNamed:@"locator_tab_right_n"] forState:UIControlStateNormal];
    [_listViewButton setBackgroundImage:[UIImage imageNamed:@"locator_tab_right"] forState:UIControlStateSelected];
    _listViewButton.adjustsImageWhenHighlighted = NO;
//    [_mapViewButton setAlpha:0.8];
    //[_listViewButton setTitleColor:[UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:.6] forState:UIControlStateNormal];
   /* [_listViewButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [_listViewButton setTitleColor:[UIColor whiteColor] forState:UIControlStateHighlighted];
    [_listViewButton setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    _listViewButton.titleLabel.font = navigationBarFont;
    [_listViewButton setTitleEdgeInsets:UIEdgeInsetsMake(0.0, -60.0, 0.0, 0.0)];
    [_listViewButton setTitle:@"List" forState:UIControlStateNormal];*/
    [_listViewButton addTarget:self action:@selector(listButtonPressed) forControlEvents:UIControlEventTouchDown];
  //  [_contentView addSubview:_listViewButton];
    
   // _mapView = [[MKMapView alloc] initWithFrame:CGRectMake(0,_navigationBarHeight, _contentViewWidth, _contentViewHeight)];
    //_mapView = [[MKMapView alloc] initWithFrame:CGRectMake(0, ( _navigationBarHeight - (_navigationBarHeight * 0.3)), _contentViewWidth, (_contentViewHeight+ (_navigationBarHeight * 0.3)))];
    
//      _mapView = [[MKMapView alloc] initWithFrame:CGRectMake(0, 0, _contentViewWidth, (_contentViewHeight))];
    
    _mapView = [[MKMapView alloc] initWithFrame:CGRectMake(0, 0, _contentViewWidth, (_contentViewHeight+ (_navigationBarHeight * 0.3))+( _navigationBarHeight - (_navigationBarHeight * 0.3)))];
    
    [_mapView setBackgroundColor:[UIColor blackColor]];
    
    _mapView.delegate = self;
    _mapView.showsUserLocation = YES;
    
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(hideMoreMenu)];
    [singleTap setCancelsTouchesInView:NO];
    [_mapView addGestureRecognizer:singleTap];
    

    CLLocation *currentLocation;
    currentLocation = [[CLLocation alloc] initWithLatitude:_app._currentLocation.latitude longitude:_app._currentLocation.longitude];
    _sortedStoresList = [_app getNearestStores:currentLocation];
    
    if([[_mapView userLocation]location]){
        currentLocation = [[_mapView userLocation]location];
    }
    [self updateMapZoom:currentLocation];
    
    CLLocationCoordinate2D coordinate;

	if(_app._allStoresList.count > 0)
    {
        for(Store *store in _sortedStoresList)
        {
			coordinate.latitude = [store.latitude doubleValue];
			coordinate.longitude = [store.longitude doubleValue];
			MapAnnotation *annotation = [[MapAnnotation alloc] initWithCoordinate:coordinate andStore:store];
			annotation._coordinate = coordinate;
			annotation._locationTitle = [NSString stringWithFormat:@"%@, %@", store.address, store.city];
			[_mapView addAnnotation:annotation];
		}
	}

    [_contentView addSubview:_mapView];

    [_contentView addSubview:_mapViewButton];
    [_contentView addSubview:_listViewButton];

    _storeTableWidth = _contentViewWidth;
    _storeTableCellHeight = _contentViewHeight * .10; //0.18
    //_storeTable = [[UITableView alloc] initWithFrame:CGRectMake(0, _navigationBarHeight, _contentViewWidth, _contentViewHeight)];
    _storeTable = [[UITableView alloc] initWithFrame:CGRectMake(0, ( _navigationBarHeight - (_navigationBarHeight * 0.3)), _contentViewWidth, (_contentViewHeight+ (_navigationBarHeight *0.2)))];
    _storeTable.backgroundColor = [UIColor clearColor];
    _storeTable.dataSource = self;
    _storeTable.delegate = self;
    _storeTable.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
    _storeTable.hidden = YES;
    [_contentView addSubview:_storeTable];
    
    UITapGestureRecognizer *storeSingleTap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(hideMoreMenu)];
    [storeSingleTap setCancelsTouchesInView:NO];
    [_storeTable addGestureRecognizer:storeSingleTap];
    

    [self setFooterDetails];
    
//    if(_app._myStoreRefreshFlag){
//        _app._myStoreRefreshFlag=false;
//    }

}

-(void)updateMapZoom:(CLLocation*)currentLocation{
    //    _sortedStoresList = [_app getNearestStores:currentLocation];
    //    Store *closestStore = [_sortedStoresList objectAtIndex:0];
    [self moveActiveStoreListToTop];//move the active store to top of the list
    
    //    double milesAway = closestStore._distanceFromLocation / METERS_PER_MILE;
    
    // default map area will be 10 miles, if nearest store is more than one quarter that distance set the radius to quadruple the nearest store distance
    //    if(milesAway > (DEFAULT_MAP_AREA / 4))
    //        _mapArea = milesAway * 4;
    //    else
    //        _mapArea = DEFAULT_MAP_AREA;
    _mapArea = 50;
    _mapArea = _mapArea * METERS_PER_MILE;
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(currentLocation.coordinate, _mapArea , _mapArea );
    region.center = currentLocation.coordinate;
    
    @try {
        [_mapView setRegion:region animated:YES];
    }
    @catch (NSException *exception) {
        region.span.longitudeDelta = 0.15f;
        region.span.latitudeDelta = 0.15f;
        [_mapView setRegion:region animated:YES];
    }
    
    [_mapView setCenterCoordinate:currentLocation.coordinate animated:YES];
}

-(void)viewDidAppear:(BOOL)animated{
    
    [self moveActiveStoreListToTop];
    [_storeTable reloadData];
    /*
    if(_app._myStoreRefreshFlag){
         _app._myStoreRefreshFlag=false;
        Login *login = [_app getLogin];
        login.storeNumber = _request.storeNumber;
        [_app storeLogin:login];
        [self moveActiveStoreListToTop];
        [_storeTable reloadData];
        
        id userLocation = [_mapView userLocation];
        
        //clear and re-set map points
        [_mapView removeAnnotations:_mapView.annotations];
        
        if(userLocation!=nil){
            [_mapView addAnnotation:userLocation];
        }
        
        CLLocationCoordinate2D coordinate;
        if(_app._allStoresList.count > 0)
        {
            for(Store *store in _sortedStoresList)
            {
                coordinate.latitude = [store.latitude doubleValue];
                coordinate.longitude = [store.longitude doubleValue];
                MapAnnotation *annotation = [[MapAnnotation alloc] initWithCoordinate:coordinate andStore:store];
                annotation._coordinate = coordinate;
                annotation._locationTitle = [NSString stringWithFormat:@"%@, %@", store.address, store.city];
                [_mapView addAnnotation:annotation];
            }
        }
    }*/
}
-(void)moveActiveStoreListToTop{
    NSMutableArray *temp=[[NSMutableArray alloc]init];
    Login *login = [_app getLogin];
    
    for(int i=0;i<_sortedStoresList.count;i++){
        Store *objStore= (Store *)[_sortedStoresList objectAtIndex:i];
        if(objStore.storeNumber==login.storeNumber){
            [temp insertObject:objStore atIndex:0];
        }
        else{
            [temp addObject:objStore];
        }
    }
    _sortedStoresList=[NSArray arrayWithArray:temp];
}
-(void)addHeader
{
    // screen header
    headerImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _app._viewWidth, _app._headerHeight)];
    [headerImageView setImage:[UIImage imageNamed:@"header"]];
    [View addSubview:headerImageView];
    
    // back button
    UIButton *backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    //    [backButton setFrame:CGRectMake(8.0, 2.0, 18.0, 31.0)];
    //    [backButton setBackgroundImage:[UIImage imageNamed:@"back_button_white"] forState:UIControlStateNormal];
    [backButton setFrame:CGRectMake(0.0, 2.0, 70.0, 40.0)];
    [backButton setImage:[UIImage imageNamed:@"back_button_white_small"] forState:UIControlStateNormal];
    backButton.contentEdgeInsets = UIEdgeInsetsMake(5, 5, 5, 35);
    //    backButton.contentEdgeInsets = UIEdgeInsetsMake(2.5, 0.0, 0.0, 0.0);
    [backButton addTarget:self action:@selector(backButtonPressed) forControlEvents:UIControlEventTouchUpInside];
    [View addSubview:backButton];
}

-(void)Overlay_for_first_time // Displayed When screen appeared at first time
{
    if(map_current_screen_index_value == 1)
    {
        if(![[NSUserDefaults standardUserDefaults] boolForKey:@"_isMapListPage"]) // false default value
        {
             map_current_screen_index_value=1;
            [self Overlay_screens:1];
            [[NSUserDefaults standardUserDefaults] setBool:TRUE forKey:@"_isMapListPage"]; // change to true for do not apper again
            [[NSUserDefaults standardUserDefaults] synchronize];
        }
    }
}

-(void)Show_map_overlay:(id)sender
{
    if(map_current_screen_index_value == 1){
        map_current_screen_index_value=1;
        [self Overlay_screens:1];
    }
    [self hideMoreMenu];
}

-(void)Overlay_screens:(int)current_screen_index
{
    UIImage *img;
    NSMutableArray *img_array;
    if(Retina4){
        img_array=[[NSMutableArray alloc]initWithObjects:
                   [UIImage imageNamed:@"map_my_store_overlay-586"],nil];
        
    }
    else{
        img_array=[[NSMutableArray alloc]initWithObjects:
                   [UIImage imageNamed:@"map_my_store_overlay"],nil];
    }
    
    switch (map_current_screen_index_value) {
        case 1:
            img=[img_array objectAtIndex:0];
            break;
            
        default:
            [self remove_overlay];
            return;
            break;
            
    }
    
    CGFloat y_val=0.0;
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){
        y_val=0.0f;
    }
    else{
        y_val=-20.0f;
    }
    map_overlay_imgview=[[UIImageView alloc]initWithFrame:CGRectMake(0,y_val, _app._viewWidth, _app._viewHeight+20.0f)];
    map_overlay_imgview.userInteractionEnabled=YES;
    map_overlay_imgview.image=img;
    
    UITapGestureRecognizer *dismiss_overlay=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(remove_overlay)];
    dismiss_overlay.numberOfTouchesRequired=1;
    dismiss_overlay.numberOfTapsRequired=1;
    
    [map_overlay_imgview addGestureRecognizer:dismiss_overlay];
    
    [self.view addSubview:map_overlay_imgview];
}

-(void)remove_overlay
{
    [map_overlay_imgview removeFromSuperview];
}


-(void)backButtonPressed
{
    //if ([_app respondsToSelector:@selector(backButtonClicked)])
    //{
        [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
        [self.presentingViewController dismissViewControllerAnimated:NO completion:nil];
    //}
}

- (void)moreButtonPressed
{
    if(_menuView.frame.origin.y != _menuVisibleFrame.origin.y)
    {
        [self hideMoreMenu];
        [self showMoreMenu];
    }
    else
    {
        [self hideMoreMenu];
    }
}

- (void)showMoreMenu
{
    [UIView animateWithDuration:0.33f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut | UIViewAnimationOptionAllowUserInteraction
                     animations:^{ [_menuView setFrame:_menuVisibleFrame]; }
                     completion:^(BOOL finished){ }];
}


- (void)hideMoreMenu
{
    if(_menuView.frame.origin.y == _menuVisibleFrame.origin.y) // already visible
        
        [UIView animateWithDuration:0.33f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut | UIViewAnimationOptionAllowUserInteraction
                         animations:^{ [_menuView setFrame:_menuHiddenFrame]; }
                         completion:^(BOOL finished){
                           
                         }];
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)mapButtonPressed
{
    [self hideMoreMenu];
    _mapViewButton.selected = YES;
    _listViewButton.selected = NO;
    _storeTable.hidden = YES;
    _mapView.hidden = NO;
 
    map_current_screen_index_value=0;
    [self Overlay_for_first_time];
}


- (void)listButtonPressed
{
    [self hideMoreMenu];
    _listViewButton.selected = YES;
    _mapViewButton.selected = NO;
    _mapView.hidden = YES;
    _storeTable.hidden = NO;
    
    map_current_screen_index_value=1;
    [self Overlay_for_first_time];
}

- (void)accountButtonPressed
{
    [self hideMoreMenu];
    Login *_login = [_app getLogin];
    AccountRequest *request = [[AccountRequest alloc] init];
    request.accountId = _login.accountId;
    
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving account data..."];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"AccountRequest"];
    [_service execute:ACCOUNT_GET_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleAccountServiceResponse method below
}


- (void)handleAccountServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];
    
    if(status == 200) // service success
    {
        _app._currentAccountRequest = (AccountRequest *)responseObject;
        
        if(_app._currentAccountRequest == nil)
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:@"Unable to parse data returned from server."];
            [dialog show];
            return;
        }
        
        [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
        //        AccountScreenVC *accountScreenVC = [[AccountScreenVC alloc] init];
        //        accountScreenVC._registrationPage = NO;
        //        [self presentViewController:accountScreenVC animated:NO completion:nil];
        [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
        SignUpViewController *viewctrl = [[SignUpViewController alloc]initWithNibName:@"SignUpViewController" bundle:nil];
        [self presentViewController:viewctrl animated:NO completion:nil];
    }
    else // service failure
    {
        if(status == 422) // backend or internet unavailable error
        {
            WebServiceError *error = [_service getError];
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Get Account Failed" message:error.errorMessage];
            [dialog show];
        }
        else if(status == 408) // timeout error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
            [dialog show];
        }
        else // http error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:COMMON_ERROR_MSG]];
            [dialog show];
        }
    }
    
    _service = nil;
}


- (void)signOutButtonPressed
{
    [self hideMoreMenu];
    _textDialog = [[TextDialog alloc] initWithView:View title:@"Sign Out" message:@"Are you sure you want sign out?" responder:self leftCallback:@selector(signOutYes) rightCallback:@selector(signOutNo)];
    [_textDialog show];
}


- (void)signOutYes
{
    [_textDialog close];
    [_app logout];
}


- (void)signOutNo
{
    [_textDialog close];
}

#pragma mark - Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if ([tableView respondsToSelector:@selector(setSeparatorInset:)]) {
        [tableView setSeparatorInset:UIEdgeInsetsZero];
    }
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _sortedStoresList.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    StoreTableCell *cell = [tableView dequeueReusableCellWithIdentifier:@"MyIdentifier"];
    
    if(cell == nil)
    {
        cell = [[StoreTableCell alloc] initWithStore:(Store *)[_sortedStoresList objectAtIndex:indexPath.row] :_storeTableWidth :_storeTableCellHeight];
        cell._storeLocatorScreenDelegate = self;
    }
    
    return cell;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath;
{
    return _storeTableCellHeight;
}


#pragma mark - Table view delegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    LogInfo(@"Store item %ld selected", (long)indexPath.row);
    [self hideMoreMenu];
    if(tableView==_storeTable){
        
        [self changeUserStore:(Store *)[_sortedStoresList objectAtIndex:indexPath.row]];
    }
}


#pragma mark - MKMapViewDelegate Methods

-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation{
    if(!firstLocationUpdate){
        [self updateMapZoom:userLocation.location];
    }else{
        [mapView setCenterCoordinate:userLocation.location.coordinate animated:YES];
    }
    firstLocationUpdate = YES;
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation
{
    if([annotation isKindOfClass:[MKUserLocation class]])
        return nil;

    static NSString *identifier = @"myAnnotation";
    MapAnnotationView * annotationView = (MapAnnotationView *)[_mapView dequeueReusableAnnotationViewWithIdentifier:identifier];

    if(!annotationView)
        annotationView = [[MapAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:identifier];
    else
        annotationView.annotation = annotation;

    Store *store = [(MapAnnotation *)annotation _store];
    Login *login = [_app getLogin];

    NSString *pinImage;

    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        if([store.chain isEqualToString:@"Raley's"])
            pinImage = @"pin_red_small";
        else if([store.chain isEqualToString:@"Bel Air"])
            pinImage = @"pin_blue_small";
        else if([store.chain isEqualToString:@"Nob Hill Foods"])
            pinImage = @"pin_gold_small";
        if(store.storeNumber==login.storeNumber){
            pinImage=@"pin_green_small";
        }
    }
    else
    {
        if([store.chain isEqualToString:@"Raley's"])
            pinImage = @"pin_red_large";
        else if([store.chain isEqualToString:@"Bel Air"])
            pinImage = @"pin_blue_large";
        else if([store.chain isEqualToString:@"Nob Hill Foods"])
            pinImage = @"pin_gold_large";
        if(store.storeNumber==login.storeNumber){
            pinImage=@"pin_green_large";
        }
    }

    annotationView.image = [UIImage imageNamed:pinImage];
    //LogInfo(@"annotation width = %d, height = %d", (int)annotationView.frame.size.width, (int)annotationView.frame.size.width);
    return annotationView;
}


- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view
{
    if(![view.annotation isKindOfClass:[MKUserLocation class]])
    {
        int calloutWidth = _app._viewWidth * .5;
        Store *store = [(MapAnnotation *)[view annotation] _store];

        // center the map on the selected pin
//        MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(_app._currentLocation, _mapArea * METERS_PER_MILE , _mapArea * METERS_PER_MILE * 0.12342342);
        
        MKCoordinateRegion region = _mapView.region;
        CLLocationCoordinate2D newLocation;
        newLocation.latitude = [store.latitude doubleValue];
        newLocation.longitude = [store.longitude doubleValue];
        region.center = newLocation;
        
       // view.centerOffset = CGPointMake(10, -20);
        
        @try {
            [_mapView setRegion:region animated:YES];
        }
        @catch (NSException *exception) {
            
        }

        int calloutHeight = _app._viewHeight * .20;
      //  _storeCallout = [[StoreCallout alloc] initWithFrame:CGRectMake((view.frame.size.width * .5) - (calloutWidth / 2), -calloutHeight, calloutWidth, calloutHeight) andStore:store];
        if( _app._deviceType == IPHONE_5){

        _storeCallout = [[StoreCallout alloc] initWithFrame:CGRectMake(( view.frame.size.width * 1.2) - (calloutWidth / 2), -calloutHeight*0.6, calloutWidth * 1.3	, calloutHeight*0.6) andStore:store];
        }
        else{
            _storeCallout = [[StoreCallout alloc] initWithFrame:CGRectMake(( view.frame.size.width * 1.05) - (calloutWidth / 2), - calloutHeight * 0.6, calloutWidth * 1.40, calloutHeight*0.6) andStore:store];

        }
        _storeCallout._storeLocatorScreenDelegate = self;
        
        [view addSubview:_storeCallout];
        
#ifdef CLP_ANALYTICS
        if(![Utility isEmpty:store]){
            // Analytics
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:[NSString stringWithFormat:@"%d",store.storeNumber] forKey:@"SM_StoreNumber"];
            [data setValue:store.address forKey:@"SM_StoreAddress"];
            [data setValue:store.latitude forKey:@"SM_StoreLatitude"];
            [data setValue:store.longitude forKey:@"SM_StoreLongitude"];
            //new
            [data setObject:@"Store Selected" forKey:@"event"];
            [data setValue:@"CLICK_EVENT" forKey:@"event_name"];
            [data setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
            [_app._clpSDK updateAppEvent:data];
        }
#endif
    }
}


-(void)mapView:(MKMapView *)mapView didDeselectAnnotationView:(MKAnnotationView *)view
{
    for(UIView *subview in view.subviews)
        [subview removeFromSuperview];
}


#pragma mark StoreLocatorScreen Delegate
- (void)changeUserStore:(Store *)store
{
    Login *login = [_app getLogin];
    if(login.storeNumber ==store.storeNumber)
    {
        return;
    }
    
    if(_app._locateForAccount == YES)
    {
        _app._storeForAccount = store;
        [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
        [self.presentingViewController dismissViewControllerAnimated:NO completion:nil];
        return;
    }
    
    // avoid multiple alert
    if(_textDialog!=nil)return;
    
    _currentStore = store;
    _textDialog = [[TextDialog alloc] initWithView:View title:@"My Store" message:[NSString stringWithFormat:@"Change My Store To %@ at %@ %@, %@", store.chain, store.address, store.city, store.state] responder:self leftCallback:@selector(changeStoreYes) rightCallback:@selector(changeStoreNo)];
    [_textDialog show];

}


- (void)changeStoreNo
{
    [_textDialog close];
    _textDialog = nil;
}


- (void)changeStoreYes
{
    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"ChangeStoreFlag"];
    [[NSUserDefaults standardUserDefaults] synchronize];
    [_textDialog close];
    _textDialog = nil;
    Login *login = [_app getLogin];
    _request = [[ChangeStoreRequest alloc] init];
    _request.accountId = login.accountId;
    _request.storeNumber = _currentStore.storeNumber;

    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Changing store..."];
    [_progressDialog show];

    _service = [[WebService alloc]initWithListener:self responseClassName:@"ChangeStoreRequest"];
    [_service execute:CHANGE_STORE_URL authorization:login.authKey requestObject:_request requestType:POST]; // response handled by handleChangeStoreServiceResponse method below
    
#ifdef CLP_ANALYTICS
    //Change Store - Analytics
    NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
    [data setValue:_request.accountId forKey:@"Store_AccountID"];
    [data setValue:[NSString stringWithFormat:@"%d",_request.storeNumber] forKey:@"Store_storeNumber"];
    [data setValue:@"Favorite Store Selected" forKey:@"event_name"];
    [data setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
    [_app._clpSDK updateAppEvent:data];
#endif
}


- (void)handleChangeStoreServiceResponse:(id)responseObject
{
    _storeCallout._myStoreButton.hidden = YES;
    _storeCallout._myStoreLabel.hidden = NO;
    int status = [_service getHttpStatusCode];

    if(status == 200) // service success
    {
        _service = nil;
        Login *login = [_app getLogin];
        login.storeNumber = _request.storeNumber;
        [_app storeLogin:login];
        [self moveActiveStoreListToTop];
        [_storeTable reloadData];
        
        id userLocation = [_mapView userLocation];
        
        //clear and re-set map points
        [_mapView removeAnnotations:_mapView.annotations];
        
        if(userLocation!=nil){
            [_mapView addAnnotation:userLocation];
        }
        
        CLLocationCoordinate2D coordinate;
        if(_app._allStoresList.count > 0)
        {
            for(Store *store in _sortedStoresList)
            {
                coordinate.latitude = [store.latitude doubleValue];
                coordinate.longitude = [store.longitude doubleValue];
                MapAnnotation *annotation = [[MapAnnotation alloc] initWithCoordinate:coordinate andStore:store];
                annotation._coordinate = coordinate;
                annotation._locationTitle = [NSString stringWithFormat:@"%@, %@", store.address, store.city];
                [_mapView addAnnotation:annotation];
            }
        }

        if(![Utility isEmpty:_app._currentShoppingList])
        {
            ListRequest *request = [[ListRequest alloc] init];
            request.accountId = login.accountId;
            request.listId = _app._currentShoppingList.listId;
            request.returnCurrentList = [NSNumber numberWithBool:YES];
            request.appListUpdateTime = [NSNumber numberWithLong:0];
            LogInfo(@"SHOPPING_LIST: handleChangeStoreServiceResponse: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [_app._currentShoppingList.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", _app._currentShoppingList.totalPrice]);

            _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Updating current list..."];
            [_progressDialog show];

            _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
            [_service execute:LIST_GET_BY_ID_URL authorization:login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
        }
    }
    else // service failure
    {
        if(status == 422) // backend or internet unavailable error
        {
            WebServiceError *error = [_service getError];
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Change Store Failed" message:error.errorMessage];
            [dialog show];
        }
        else if(status == 408) // timeout error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
            [dialog show];
        }
        else // http error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:COMMON_ERROR_MSG]];
            [dialog show];
        }

        _service = nil;
    }
}


- (void)handleListServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];

    if(status == 200) // service success
    {
        _service = nil;
        ShoppingList *response = (ShoppingList *)responseObject;

        if(response == nil)
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:@"Unable to parse data returned from server."];
            [dialog show];
            return;
        }
        
        _app._currentShoppingList = (ShoppingList *)responseObject;
        [self setFooterDetails];
    }
    else // service failure
    {
        if(status == 422) // backend error
        {
            WebServiceError *error = [_service getError];
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"List Retrieve Failed" message:error.errorMessage];
            [dialog show];
        }
        else if(status == 408) // timeout error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
            [dialog show];
        }
        else // http error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:COMMON_ERROR_MSG]];
            [dialog show];
        }

        _service = nil;
    }
}


#pragma mark WebServiceListener Delegate
- (void)onServiceResponse:(id)responseObject
{
    [_progressDialog dismiss];

    if([responseObject isKindOfClass:[ChangeStoreRequest class]])
        [self handleChangeStoreServiceResponse:responseObject];
    if([responseObject isKindOfClass:[ShoppingList class]])
        [self handleListServiceResponse:responseObject];
    if([responseObject isKindOfClass:[AccountRequest class]])
    {
        [self handleAccountServiceResponse:responseObject];
    }
}

# pragma mark Scroll Delegate

-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView{
    [self hideMoreMenu];
}

# pragma mark Touch Delegate
-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [self hideMoreMenu];
}

@end
