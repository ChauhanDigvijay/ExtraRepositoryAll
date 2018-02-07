//
//  ShoppingScreenVC.m
//  Raley's
//
//  Created by Billy Lewis on 9/30/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#define kNavigationBarHeight 44.0f

#import "ASIHttpRequest.h"
#import "SBJsonParser.h"
#import "EcartPreOrderRequest.h"
#import "EcartPreOrderResponse.h"
#import "ShoppingScreenVC.h"
#import "StoreLocatorScreenVC.h"
#import "ShoppingListName.h"
#import "ListRequest.h"
#import "ListCreateRequest.h"
#import "ListDeleteRequest.h"
#import "ListNameRequest.h"
#import "Offer.h"
#import "OfferAcceptRequest.h"
#import "BlankHeaderCell.h"
#import "OfferHeaderCell.h"
#import "OfferGridCell.h"
#import "ProductGridCell.h"
#import "ProductRequest.h"
#import "ProductDetailView.h"
#import "ProductsForCategoryRequest.h"
#import "ProductsForCategoryResponse.h"
#import "PromoCategoriesRequest.h"
#import "ShoppingListGridCell.h"
#import "ShoppingListHeaderCell.h"
#import "ShoppingMenu.h"
#import "TextDialog.h"
#import "Utility.h"
#import "Logging.h"
#import "ListDeleteItemRequest.h"
#import "ListAddItemRequest.h"

#import "ProductDetails.h"
#import "SignUpViewController.h"

#import "TickDialog.h"
#define PRODUCT_FETCH_LIMIT 0 //200


#define GRIDCELL_GAP 5
#define GRIDCELL_PRICE_HEIGHT 30
#define GRIDCELL_PADDING 0.05 // In Percentage

#define OFFER_GRID_HEADER_HEIGHT 35
#define GRID_IMAGE_SIZE 600
#define Retina4 ([UIScreen mainScreen].bounds.size.height == 568.f)


@interface ShoppingScreenVC ()
{
    CGFloat margin_top;
    UIButton *backButton, *shoppinglist_backbtn;
    NSString *_selectedShoppingListId;
    NSInteger _selectedShoppingListIndex;
    
    UIView *_shoppingListContainer;
    BOOL serverShoppingListFlag;
    BOOL enableEditMode;
    UITableView *_shoppingProductList;
    UIView *_shoppingProductListContainer;
    UILabel *lblTitle,*lblTotal;
    UIButton *btnCheckout;
    float _shoppingProductListLastOffset;
    
    UITextField *shopping_list_add_new;
    
    
    NSOperationQueue *image_down_task;
    
    ShoppingMenu *current_active_menu;
    BOOL _isCurrentAcitveList;
    
    BOOL _isListsPage;
    BOOL currentActiveListFlag;
    ZXCapture *zxingCapture;
    TextDialog *scannerDialog;
    BOOL scannerStart;
    BOOL firstTimeShowOffer;
    UILabel *noOfferMessage;
    
    
    NSDate  *scanner_wait;
    UIView *scanArea;
    CGRect scanAreaRect;
    
    // active shopping list for checkout
    ShoppingListName *activeShoppingList;
    BOOL activeShoppingListAnimation;
    
    BOOL movetolistFlag; // Temp flag for move to list response
    
    // Offer menu items
    NSMutableArray *offerItems;
    
}
@end


@implementation ShoppingScreenVC

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _isListsPage=NO;
    firstTimeShowOffer = NO;
    _headerImageView.hidden = YES;
    _navigationBar.hidden = YES;
    _currentPageProductList = [[NSMutableArray alloc] init];
    
    _multiplePageProductList = [[NSMutableArray alloc] init];
    
    image_down_task = [[NSOperationQueue alloc]init];
    [image_down_task setMaxConcurrentOperationCount:2];
    
    
    if(_app._deviceType == IPHONE_5)
        _categoryButtonHeight = _contentViewHeight * .07;
    else if(_app._deviceType == IPHONE)
        _categoryButtonHeight = _contentViewHeight * .08;
    else // IPAD
        _categoryButtonHeight = _contentViewHeight * .06;
    
    
    _login = [_app getLogin];
    _app._offerTableWidth = _contentViewWidth;
    _app._offerTableCellHeight = _contentViewHeight * .40;
    _app._productTableWidth = _contentViewWidth;
    _app._productTableCellHeight = _app._viewHeight * .25;
    _app._shoppingListTableWidth = _contentViewWidth;
    _app._shoppingListTableCellHeight = _app._viewHeight * .25;
    _backButton.hidden = YES;
    _childViewVisibleFrame = CGRectMake(0,  _navigationBarHeight, _contentViewWidth, _contentViewHeight - _navigationBarHeight);
    _childViewHiddenFrame = CGRectMake(_contentViewWidth, _navigationBarHeight, _contentViewWidth, _contentViewHeight - _navigationBarHeight);
    _categoryButtonImage = [UIImage imageNamed:@"bar_gray_gradient"];
    _categoryButtonTextColor = [UIColor blackColor];
    
    // menu bar buttons
    int buttonCount = 4;
    int buttonWidth = _app._viewWidth / buttonCount;
    int lastButtonPad = _app._viewWidth - (buttonWidth * buttonCount);
    int buttonIndex = 0;
    int itemHeight = _app._headerHeight;
    int baselineHeight = itemHeight * .05;
    
    // product menu
    NSMutableArray *productItems = [[NSMutableArray alloc] init];
    [productItems addObject:@"Sale Products:productSaleButtonPressed"];
    [productItems addObject:@"All Products:productSearchButtonPressed"];
    [productItems addObject:@"Scan:productScanButtonPressed"];
    //[productItems addObject:@"Scan & Search:productScanButtonPressed"];
    
    margin_top = self.view.bounds.size.height-_navigationBar.frame.size.height;
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
    {
        margin_top-=20;
    }
    
    _navigationBarHeight=_navigationBarHeight-2;
    
    _productMenu = [[ShoppingMenu alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, _navigationBar.frame.origin.y, buttonWidth, _navigationBarHeight) parentView:View itemHeight:itemHeight items:productItems responder:self];
    //        _productMenu = [[ShoppingMenu alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, margin_top, buttonWidth, _navigationBarHeight) parentView:View itemHeight:itemHeight items:productItems responder:self];
    
    
    [_productMenu setSelectedImage:[UIImage imageNamed:@"tab_shop_selected"]];
    [_productMenu._menuButton addTarget:self action:@selector(productMenuButtonPressed) forControlEvents:UIControlEventTouchUpInside];
    [View addSubview:_productMenu];
    
    // back button
    backButton = [UIButton buttonWithType:UIButtonTypeCustom];
    //    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){  // IOS 7 and above
    //        [backButton setFrame:CGRectMake(4.0, 24.0, 28.0, 28.0)];
    //    }
    //    else{
    //        [backButton setFrame:CGRectMake(4.0, 4.0, 28.0, 28.0)];
    //    }
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){  // IOS 7 and above
        [backButton setFrame:CGRectMake(0, 22.0, 70.0, 40.0)]; // 25 25
    }
    else{
        [backButton setFrame:CGRectMake(0.0, 2.0, 70.0, 40.0)];
    }
    
    
    backButton.contentEdgeInsets = UIEdgeInsetsMake(5, 5, 5, 35);
    //    [backButton.layer setBorderWidth:1.0f];
    [backButton setImage:[UIImage imageNamed:@"back_button_white_small"] forState:UIControlStateNormal];
    [backButton setImage:[UIImage imageNamed:@"back_button_white_small"] forState:UIControlStateSelected];
    
    [backButton addTarget:self action:@selector(backToCategoryButtonPressed) forControlEvents:UIControlEventTouchUpInside];
    [self.view insertSubview:backButton aboveSubview:View];
    [backButton setHidden:YES];
    
    
    
    // shopping back button
    shoppinglist_backbtn = [UIButton buttonWithType:UIButtonTypeCustom];
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){  // IOS 7 and above
        [shoppinglist_backbtn setFrame:CGRectMake(0, 22.0, 70.0, 40.0)]; // 25 25
    }
    else{
        [shoppinglist_backbtn setFrame:CGRectMake(0.0, 2.0, 70.0, 40.0)];
    }
    shoppinglist_backbtn.contentEdgeInsets = UIEdgeInsetsMake(5, 5, 5, 35);
    
    [shoppinglist_backbtn setImage:[UIImage imageNamed:@"back_button_white_small"] forState:UIControlStateNormal];
    [shoppinglist_backbtn addTarget:self action:@selector(backToShoppingListName) forControlEvents:UIControlEventTouchUpInside];
    [self.view insertSubview:shoppinglist_backbtn aboveSubview:View];
    [shoppinglist_backbtn setHidden:YES];
    
    // offer menu
    offerItems = [[NSMutableArray alloc] init];
    [offerItems addObject:@"Available Offers:availableOffersButtonPressed"];
    [offerItems addObject:@"Accepted Offers:acceptedOffersButtonPressed"];
    [offerItems addObject:@"SE by Google:googleOffersButtonPressed"];
    
    _offerMenu = [[ShoppingMenu alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, _navigationBar.frame.origin.y, buttonWidth, _navigationBarHeight) parentView:View itemHeight:itemHeight items:offerItems responder:self];
    //    _offerMenu = [[ShoppingMenu alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, margin_top, buttonWidth, _navigationBarHeight) parentView:View itemHeight:itemHeight items:offerItems responder:self];
    
    [_offerMenu setSelectedImage:[UIImage imageNamed:@"tab_offers_selected"]];
    [_offerMenu._menuButton addTarget:self action:@selector(offerMenuButtonPressed) forControlEvents:UIControlEventTouchUpInside];
    [View addSubview:_offerMenu];
    
    // list menu
    NSMutableArray *listItems = [[NSMutableArray alloc] init];
    [listItems addObject:@"Show Active:showActiveList"];
    [listItems addObject:@"Set Active:setActiveListButtonPressed"];
    [listItems addObject:@"Delete Active:deleteActiveListButtonPressed"];
    [listItems addObject:@"Create New:createNewListButtonPressed"];
    
    _listMenu = [[ShoppingMenu alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, _navigationBar.frame.origin.y, buttonWidth + lastButtonPad, _navigationBarHeight) parentView:View itemHeight:itemHeight items:listItems responder:self];
    //    _listMenu = [[ShoppingMenu alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, margin_top, buttonWidth + lastButtonPad, _navigationBarHeight) parentView:View itemHeight:itemHeight items:listItems responder:self];
    
    [_listMenu setSelectedImage:[UIImage imageNamed:@"tab_lists_selected"]];
    //    [_listMenu._menuButton addTarget:self action:@selector(listMenuButtonPressed) forControlEvents:UIControlEventTouchUpInside];
    [_listMenu._menuButton addTarget:self action:@selector(setActiveList) forControlEvents:UIControlEventTouchUpInside];
    [View addSubview:_listMenu];
    
    // Map Menu
    NSMutableArray *mapItems = [[NSMutableArray alloc] init];
    //    _mapMenu = [[ShoppingMenu alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, margin_top, buttonWidth, _navigationBarHeight) parentView:View itemHeight:itemHeight items:mapItems responder:self];
    _mapMenu = [[ShoppingMenu alloc] initWithFrame:CGRectMake(buttonWidth * buttonIndex++, _navigationBar.frame.origin.y, buttonWidth, _navigationBarHeight) parentView:View itemHeight:itemHeight items:mapItems responder:self];
    [_mapMenu setSelectedImage:[UIImage imageNamed:@"tab_map_select"]];
    [_mapMenu._menuButton addTarget:self action:@selector(storeLocatorButtonPressed) forControlEvents:UIControlEventTouchUpInside];
    
    [View addSubview:_mapMenu];
    
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
    //
    //    UIButton *helpInstruction = [[UIButton alloc] initWithFrame:CGRectMake(0, _menuButtonHeight * menuButtonIndex++, _menuViewWidth, _menuButtonHeight)];
    //    [helpInstruction setBackgroundImage:[UIImage imageNamed:@"drop_menu_unselected"] forState:UIControlStateNormal];
    //    [helpInstruction setBackgroundImage:[UIImage imageNamed:@"drop_menu_selected"] forState:UIControlStateSelected];
    //    helpInstruction.titleLabel.font = menuButtonFont;
    //    [helpInstruction setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    //    [helpInstruction addTarget:self action:@selector(Show_overlay:) forControlEvents:UIControlEventTouchDown];
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
    
    // screen header and header buttons need to be created after menus so menus can slide up behind it
    UIImageView *headerImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _app._viewWidth, _app._headerHeight)];
    [headerImageView setImage:[UIImage imageNamed:@"header"]];
    
    [View addSubview:headerImageView];
    
    [View setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]];
    
    int buttonPad = _app._viewWidth * .02;
    //    int moreButtonSize = _app._headerHeight * .6;
    int moreButtonWidth = _app._headerHeight;
    int moreButtonHeight = _app._headerHeight;
    _moreButton = [[UIButton alloc] initWithFrame:CGRectMake(_app._viewWidth - moreButtonWidth - buttonPad, (_app._headerHeight - moreButtonHeight) / 2, moreButtonWidth, moreButtonHeight)];
    [_moreButton setBackgroundImage:[UIImage imageNamed:@"more_button"] forState:UIControlStateNormal];
    [_moreButton setBackgroundImage:[UIImage imageNamed:@"more_button_selected"] forState:UIControlStateSelected];
    [_moreButton addTarget:self action:@selector(moreButtonPressed) forControlEvents:UIControlEventTouchDown];
    [View addSubview:_moreButton];
    
    // stupid hack to make iOS7 status bar non-translucent
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
    {
        UIView *statusBarBackground = [[UIView alloc] initWithFrame:CGRectMake(0, 0, _app._viewWidth, 20)];
        statusBarBackground.backgroundColor = [UIColor blackColor];
        [self.view addSubview:statusBarBackground];
    }
    
    int columns;
    float productHeightRatio;
    float shoppingListHeightRatio;
    
    // product grid layout
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        columns = 2;
        productHeightRatio = 1.5;
        shoppingListHeightRatio = 1.0;
    }
    else
    {
        columns = 4;
        productHeightRatio = 1.35;
        shoppingListHeightRatio = 1.15;
    }
    
    int cellSpacing = _contentViewWidth * .03;
    int cellSize = (_contentViewWidth - (cellSpacing * (columns + 1))) / columns;
    
    _productGridLayout = [[UICollectionViewFlowLayout alloc] init];
    _productGridLayout.scrollDirection = UICollectionViewScrollDirectionVertical;
    _productGridLayout.itemSize = CGSizeMake(cellSize, cellSize * productHeightRatio);
    _productGridLayout.sectionInset = UIEdgeInsetsMake(cellSpacing, cellSpacing, cellSpacing, cellSpacing); // default 0
    _productGridLayout.minimumInteritemSpacing = cellSpacing; // default 10
    _productGridLayout.minimumLineSpacing = cellSpacing; // default 10
    //    _productGridLayout.dataSource = self;
    
    _productGrid.delegate=self;
    _productGrid.dataSource=self;
    _productGrid.showsHorizontalScrollIndicator=NO;
    _productGrid.showsVerticalScrollIndicator=NO;
    
    
    // offer grid layout
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
        columns = 2;
    else
        columns = 3;
    
    cellSpacing = _contentViewWidth * .03;
    cellSize = (_contentViewWidth - (cellSpacing * (columns + 1))) / columns;
    
    _offerGridLayout = [[UICollectionViewFlowLayout alloc] init];
    _offerGridLayout.scrollDirection = UICollectionViewScrollDirectionVertical;
    _offerGridLayout.itemSize = CGSizeMake(cellSize, cellSize * productHeightRatio);
    _offerGridLayout.sectionInset = UIEdgeInsetsMake(cellSpacing, cellSpacing, cellSpacing, cellSpacing); // default 0
    //    _offerGridLayout.dataSource = self;
    
    _offerGridLayout.minimumInteritemSpacing = cellSpacing; // default 10
    _offerGridLayout.minimumLineSpacing = cellSpacing; // default 10
    _offerGridLayout.headerReferenceSize = CGSizeMake(_contentViewWidth, OFFER_GRID_HEADER_HEIGHT);
    
    // shopping list grid layout
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
        columns = 2;
    else
        columns = 4;
    
    cellSpacing = _contentViewWidth * .01;
    cellSize = (_contentViewWidth - (cellSpacing * (columns + 1))) / columns;
    
    _shoppingListGridLayout = [[UICollectionViewFlowLayout alloc] init];
    _shoppingListGridLayout.scrollDirection = UICollectionViewScrollDirectionVertical;
    _shoppingListGridLayout.itemSize = CGSizeMake(cellSize, cellSize * shoppingListHeightRatio);
    _shoppingListGridLayout.sectionInset = UIEdgeInsetsMake(cellSpacing, cellSpacing, cellSpacing, cellSpacing); // default 0
    _shoppingListGridLayout.minimumInteritemSpacing = cellSpacing; // default 10
    _shoppingListGridLayout.minimumLineSpacing = cellSpacing; // default 10
    _shoppingListGridLayout.headerReferenceSize = CGSizeMake(_contentViewWidth, _contentViewHeight * .1);
    
    
    UIButton *showListButton = [[UIButton alloc] initWithFrame:CGRectMake(0, _app._viewHeight - _app._footerHeight, _app._viewWidth, _app._footerHeight)];
    [showListButton setBackgroundImage:[UIImage imageNamed:@"transparent"] forState:UIControlStateNormal];
    [showListButton addTarget:self action:@selector(showActiveList) forControlEvents:UIControlEventTouchDown];
    //    [View addSubview:showListButton];
    
    int ecartButtonHeight = _app._headerHeight * .7;
    _ecartButton = [[UIButton alloc] initWithFrame:CGRectMake(buttonPad, (_app._headerHeight - ecartButtonHeight) / 2, ecartButtonHeight * 2, ecartButtonHeight)];
    [_ecartButton setBackgroundImage:[UIImage imageNamed:@"ecart_button"] forState:UIControlStateNormal];
    [_ecartButton addTarget:self action:@selector(eCartButtonPressed) forControlEvents:UIControlEventTouchDown];
    _ecartButton.hidden = YES;
    //    [View addSubview:_ecartButton];
    
    // need to clear menus if user taps on bare content view
    _tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapRecognized)];
    [_contentView addGestureRecognizer:_tapRecognizer];
    serverShoppingListFlag=FALSE;
    enableEditMode=FALSE;
    _isCurrentAcitveList=false;
    //notifier for scrolling text view to view position if keyboard was opened
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:nil];
    
    //initialize _shoppingProductList scroll postion
    _shoppingProductListLastOffset=0;
    
    //tick alert box
    alert_image=[[UIImageView alloc]initWithFrame:CGRectMake((self.view.frame.size.width-48)/2, (self.view.frame.size.height-48)/2, 48, 48)];
    UIImage *aimage = [UIImage imageNamed:@"success"];
    [alert_image setImage:aimage];
    img_alert_container =[[UIView alloc]initWithFrame:self.view.bounds];
    [img_alert_container setBackgroundColor:[UIColor clearColor]];
    [img_alert_container addSubview:alert_image];
    [self.view addSubview:img_alert_container];
    [img_alert_container setHidden:YES];
    [[Raleys shared] performSelectorInBackground:@selector(userRegister) withObject:nil];
    
    //retreive currentactive list if it is empty
    currentActiveListFlag=TRUE;
    if([Utility isEmpty:_app._currentShoppingList])
    {
        NSString *currentActiveListId;
        currentActiveListId=[_app getCurrentActiveListIdForAccountId:_login.accountId];
        if(![currentActiveListId isEqualToString:@""]){
            //[self getCurrentShoppingList:currentActiveListId];
            [_app getCurrentShoppingListThread:@""];
        }
    }
    
    //block scanner unwanted call
    scannerStart=TRUE;
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void)showTick{
    [img_alert_container setAlpha:0.f];
    
    [UIView animateWithDuration:0.5f delay:0.f options:UIViewAnimationOptionCurveEaseIn animations:^{
        [img_alert_container setHidden:NO];
        
        [img_alert_container setAlpha:1.f];
    } completion:^(BOOL finished) {
        [UIView animateWithDuration:1.f delay:1.f options:UIViewAnimationOptionCurveEaseInOut animations:^{
            [img_alert_container setAlpha:0.f];
            
        } completion:^(BOOL finished){
            [img_alert_container setHidden:NO];
            
        }];
    }];
}



- (void)viewDidAppear:(BOOL)animated
{
    
    BOOL ChangeStoreFlag = [[NSUserDefaults standardUserDefaults] boolForKey:@"ChangeStoreFlag"];
    
    if(ChangeStoreFlag){
        if(current_active_menu == _listMenu){
            [_shoppingProductList reloadData];
        }
        
        if(_shoppingProductList != nil){
            activeShoppingListAnimation = NO;
            [self getShoppingList:activeShoppingList];
        }
        [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"ChangeStoreFlag"];
        [[NSUserDefaults standardUserDefaults] synchronize];
    }
    
    
    if(_app._retrievingShoppingList == YES)
        [NSThread detachNewThreadSelector:@selector(waitForShoppingListThread:) toTarget:self withObject:nil];
    
    [self setFooterDetails];
    
    if([_app offerThreadsDone] == NO)
    {
        _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Searching for your offers..."];
        [_progressDialog show];
        [NSThread detachNewThreadSelector:@selector(waitForOffersThread:) toTarget:self withObject:OFFERS_PERSONALIZED_URL];
    }
    else if(_offersShown == NO)
    {
        [self setActiveMenu:_offerMenu];
        [self showAvailableOffersView];
    }
    
    [self removeMenus];
    
    [self showScannerPosition];
    
}


- (void)tapRecognized
{
    [self removeMenus];
}


- (void)underConstruction:(NSString *)message
{
    TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Under Construction" message:message];
    [dialog show];
}


- (void)removeMenus
{
    //    [self.view endEditing:YES];
    [self hideMenus];
    //    if(_searchTextField!=nil){
    //        [_searchTextField resignFirstResponder];
    //    }
    [self.view endEditing:YES];
}


-(void)hideMenus{
    [self hideMoreMenu];
    [_offerMenu hideItems];
    [_listMenu hideItems];
    [_productMenu hideItems];
}


- (void)removeViews
{
    if(backButton!=nil){
        [backButton setHidden:YES];
    }
    if(noOfferMessage!=nil){//remove no offers message in offers screen
        [noOfferMessage removeFromSuperview];
    }
    
    if(_offersView != nil)
    {
        [_offersView removeFromSuperview];
        _offersView = nil;
        _offerGrid = nil;
    }
    
    if(_productView != nil)
    {
        [_productView removeFromSuperview];
        _productView = nil;
    }
    
    if(_shoppingListView != nil)
    {
        if(_ecartView != nil)
        {
            [_ecartView removeFromSuperview];
            _ecartView = nil;
        }
        
        [_shoppingListView removeFromSuperview];
        _shoppingListView = nil;
        if(_productByAisleArray!=nil){
            [_productByAisleArray removeAllObjects];
        }
        _productByAisleArray = nil;
        _ecartButton.hidden = YES;
    }
    
    if(_categoryView != nil)
    {
        [_categoryView removeFromSuperview];
        [_contentView addGestureRecognizer:_tapRecognizer]; // turned off by category view
        _categoryView = nil;
    }
    
    if(_scannerView != nil)
    {
        //        [_reader stop];
        [zxingCapture stop];
        [_scannerView removeFromSuperview];
        _scannerView = nil;
    }
    
    if(zxingCapture!=nil){
        [zxingCapture stop];
        zxingCapture = nil;
    }
    
    if(_shoppingListContainer!=nil){
        [_shoppingListContainer removeFromSuperview];
        _shoppingListContainer=nil;
    }
    if(_shoppingProductList!=nil){
        [_shoppingProductList removeFromSuperview];
        _shoppingProductList=nil;
    }
    if(_shoppingProductListContainer!=nil){
        [shoppinglist_backbtn setHidden:YES];
        [_shoppingProductListContainer removeFromSuperview];
        _shoppingProductListContainer=nil;
    }
    
    if(product_page_search_view!=nil){
        [product_page_search_view removeFromSuperview];
        product_page_search_view = nil;
    }
    
    //    if(shoppinglist_backbtn!=nil){
    //        [shoppinglist_backbtn setHidden:YES];
    //    }
}


- (void)setActiveMenu:(ShoppingMenu *)menu
{
    dispatch_async(dispatch_get_main_queue(), ^{
        if(menu == _productMenu)
        {
            [menu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_shop_selected"] forState:UIControlStateNormal];
            [_offerMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_offers_unselected"] forState:UIControlStateNormal];
            [_listMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_lists_unselected"] forState:UIControlStateNormal];
            [_mapMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_map_unselect"] forState:UIControlStateNormal];
            current_active_menu = menu;
        }
        else if(menu == _offerMenu)
        {
            //        // Offers
            //
            //
            //
            //        UILabel *_offersLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, menu._menuButton.frame.size.width, menu._menuButton.frame.size.height)];
            //
            //        [_offersLabel setBackgroundColor:[UIColor colorWithRed:244/255.0 green:240/255.0 blue:240/255.0 alpha:1]];
            //
            //        _offersLabel.textAlignment = NSTextAlignmentCenter;
            //
            //        _offersLabel.text = @"offers";
            //
            //        [menu._menuButton  addSubview:_offersLabel];
            //
            //
            //        // [menu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_offers_selected"] forState:UIControlStateNormal];
            //
            //
            //
            //        // Shop
            //
            //
            //
            //        UILabel *_productLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, _productMenu._menuButton.frame.size.width, _productMenu._menuButton.frame.size.height)];
            //
            //        [_productLabel setBackgroundColor:[UIColor colorWithRed:244/255.0 green:240/255.0 blue:240/255.0 alpha:1]];
            //
            //        _productLabel.textAlignment = NSTextAlignmentCenter;
            //
            //        _productLabel.text = @"shop";
            //
            //        // _productLabel.center = CGPointMake(_productMenu._menuButton.frame.size.width/2, _productMenu._menuButton.frame.size.height/2);
            //
            //        [_productMenu._menuButton addSubview:_productLabel];
            //
            //
            //
            //        // [_productMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_shop_unselected"] forState:UIControlStateNormal];
            //
            //
            //
            //        //Lists
            //
            //        UILabel *_listLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, _listMenu._menuButton.frame.size.width, _listMenu._menuButton.frame.size.height)];
            //
            //        [_listLabel setBackgroundColor:[UIColor colorWithRed:244/255.0 green:240/255.0 blue:240/255.0 alpha:1]];
            //
            //        _listLabel.textAlignment = NSTextAlignmentCenter;
            //
            //        _listLabel.text = @"list";
            //
            //        [_listMenu._menuButton addSubview:_listLabel];
            //
            //
            //
            //        //  [_listMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_lists_unselected"] forState:UIControlStateNormal];
            //
            //
            //
            //        // Map
            //
            //
            //
            //        UILabel *_menuLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, _mapMenu._menuButton.frame.size.width, _mapMenu._menuButton.frame.size.height)];
            //
            //        [_menuLabel setBackgroundColor:[UIColor colorWithRed:244/255.0 green:240/255.0 blue:240/255.0 alpha:1]];
            //
            //        _menuLabel.textAlignment = NSTextAlignmentCenter;
            //
            //        _menuLabel.text = @"map";
            //
            //        [_mapMenu._menuButton addSubview:_menuLabel];
            [menu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_offers_selected"] forState:UIControlStateNormal];
            [_productMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_shop_unselected"] forState:UIControlStateNormal];
            [_listMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_lists_unselected"] forState:UIControlStateNormal];
            [_mapMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_map_unselect"] forState:UIControlStateNormal];
            current_active_menu = menu;
            
        }
        else if(menu == _listMenu)
        {
            [menu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_lists_selected"] forState:UIControlStateNormal];
            [_productMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_shop_unselected"] forState:UIControlStateNormal];
            [_offerMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_offers_unselected"] forState:UIControlStateNormal];
            [_mapMenu._menuButton setBackgroundImage:[UIImage imageNamed:@"tab_map_unselect"] forState:UIControlStateNormal];
            current_active_menu = menu;
        }
    });
}



- (void)productMenuButtonPressed
{
    [self hideMoreMenu];
    [_offerMenu hideItems];
    [_listMenu hideItems];
    [_mapMenu hideItems];
    [_productMenu menuButtonPressed];
    [self.view endEditing:YES];
    
#ifdef CLP_ANALYTICS
    @try {                                                                //Shop tab click event
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"Shop" forKey:@"link_clicked"];
            [data setValue:@"TabClicked" forKey:@"event_name"];
            [_app._clpSDK updateAppEvent:data];
        }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
}

//-(void)updateAppEvent:(NSDictionary *)submitAppEvents{

//    @try {
        
//        NSLog(@" updateAppEvent is calling");
        
//        
//        for(NSString *key in [submitAppEvents allKeys]) {
//            NSLog(@"Inputs:  %@",[submitAppEvents objectForKey:key]);
//        }
//        
    
        
//                AFHTTPRequestOperationManager *manager=[AFHTTPRequestOperationManager manager];
//                manager.requestSerializer = [AFJSONRequestSerializer serializer];
//                [manager.requestSerializer setValue:@"5bccfcdc00b2639232feaa75ab73ba1e" forHTTPHeaderField:@"CLP-API-KEY"];
//                [manager POST:@"http://54.67.45.38/clpapi/mobile/submitappevents" parameters:submitAppEvents success:^(AFHTTPRequestOperation *operation, id responseObject) {
//                    [self showLocalNotification:@"Location Updated to platform"];
//                    NSLog(@"SUCCESS JSON: response %@", responseObject);
//                } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//                    NSLog(@"Error: %@", [error description]);
//                }];

        
//        NSURL *url = [NSURL URLWithString:@"http://54.67.45.38/clpapi/mobile/submitappevents"];
//        AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:[NSURL URLWithString:@"http://54.67.45.38/clpapi/mobile/submitappevents"]]; //API_BASE_URL]];
//        [httpClient registerHTTPOperationClass:[AFJSONRequestOperation class]];
//        
//        
//        NSURLRequest* request = [httpClient requestWithMethod:@"POST" path:nil parameters:submitAppEvents];
//        
//        AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request
//                                                                                            success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) {
//                                                                                                @try {
//                                                                                      
//                                                                                                    
//                                                                                                }
//                                                                                                @catch (NSException *exception) {
//                                                                                                }
//                                                                                            }
//                                                                                            failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON) {
//                                                                                                // Error occured
//                                                                                                
//                                                                                            }];
//        
//        [AFJSONRequestOperation addAcceptableContentTypes:[NSSet setWithObjects:@"application/json",nil]];
//        [httpClient setDefaultHeader:@"Accept" value:@"application/json"];
//
//        [operation start];
//        AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:[NSURL URLWithString:@"http://54.67.45.38/clpapi/mobile/"]];
//        [httpClient setDefaultHeader:@"CLP-API-KEY" value:@"5bccfcdc00b2639232feaa75ab73ba1e"];
//        [httpClient setDefaultHeader:@"Accept" value:@"application/json"];
        //  method1
        //        [httpClient postPath:@"submitappevents"
        //                  parameters:submitAppEvents
        //                     success:^(AFHTTPRequestOperation *operation, id responseObject) {
        //                         NSLog(@"RESPONSE: %@", responseObject);
        //
        //                     }
        //                     failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        //                         if (error)
        //                             NSLog(@"%@", [error localizedDescription]);
        //
        //                     }];
        
        //        method2
        
        //        NSURLRequest* request = [httpClient requestWithMethod:@"POST" path:@"submitappevents" parameters:submitAppEvents];
        // POST, and for GET request, you need to use |-getPath:parameters:success:failure:|
        //        AFJSONRequestOperation *operation = [AFJSONRequestOperation
        //                                             JSONRequestOperationWithRequest:request
        //                                             success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON) {
        //                                                 // ignore
        //                                                 NSLog(@"Successfully set super property %@.  Received response:\n\n%@", JSON, JSON);
        //                                             }
        //                                             failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error, id JSON) {
        //                                                 NSLog(@"Could set super property %@ because: %@", JSON, [error localizedDescription]);
        //                                                 if (JSON && [JSON objectForKey:@"error"]) {
        //                                                     NSLog(@"The server explained the failure to set the super property %@ as follows: %@", JSON, [JSON objectForKey:@"error"]);
        //                                                 }
        //                                             }];
        //        [operation start];
        

  //  }
  //  @catch (NSException *exception) {
   //     NSLog(@"Catched Error: %@", [exception description]);
   // }
    
//}

- (void)offerMenuButtonPressed
{
    [self hideMoreMenu];
    [_productMenu hideItems];
    [_listMenu hideItems];
    [_mapMenu hideItems];
    [_offerMenu menuButtonPressed];
    [self.view endEditing:YES];
    
    // Update Offer Menu
    NSMutableArray *_tempofferItems=[[NSMutableArray alloc]initWithArray:offerItems];
    if(!(_app.beaconSchedule && _app.beaconSchedule.enableGoogleOffer)){ // nil and boolean
        if(_tempofferItems.count>0){
            [_tempofferItems removeLastObject];
        }
    }
    CGRect frame = _offerMenu.frame;
    CGFloat itemHeight = _app._headerHeight;
    [_offerMenu UpdateMenuItems:frame parentView:View itemHeight:itemHeight items:_tempofferItems responder:self];
    //
    
#ifdef CLP_ANALYTICS
    @try {                                                                //Offer tab click event
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"Offer" forKey:@"link_clicked"];
        [data setValue:@"TabClicked" forKey:@"event_name"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
}


- (void)listMenuButtonPressed
{
    [self hideMoreMenu];
    [_productMenu hideItems];
    [_offerMenu hideItems];
    [_mapMenu hideItems];
    [_listMenu menuButtonPressed];
    [self.view endEditing:YES];
    
#ifdef CLP_ANALYTICS
    @try {                                                                //Lists tab click event
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"Lists" forKey:@"link_clicked"];
        [data setValue:@"TabClicked" forKey:@"event_name"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }

#endif
}


- (void)moreButtonPressed
{
    if(_menuView.frame.origin.y != _menuVisibleFrame.origin.y)
    {
        [self removeMenus];
        [self showMoreMenu];
    }
    else
    {
        [self hideMoreMenu];
    }
    [self.view endEditing:YES];
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
                         completion:^(BOOL finished){ }];
}


- (void)accountButtonPressed
{
    [self hideMoreMenu];
    AccountRequest *request = [[AccountRequest alloc] init];
    request.accountId = _login.accountId;
    Login *login = [_app getLogin];
    if(login && login.storeNumber){
        request.storeNumber = login.storeNumber;
    }
    
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving account data..."];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"AccountRequest"];
    [_service execute:ACCOUNT_GET_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleAccountServiceResponse method below
    
#ifdef CLP_ANALYTICS
    @try {                                                                //My Account menu click event
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"My Account" forKey:@"link_clicked"];
        [data setValue:@"MenuOpened" forKey:@"event_name"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
    
#endif
}


- (void)handleAccountServiceResponse:(id)responseObject
{
    @try {
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
            viewctrl._registrationPage = NO;
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
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
        
    }
}


- (void)storeLocatorButtonPressed
{
    [self hideMoreMenu];
    [self removeMenus];
    
    if(_app._allStoresList.count == 0)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Sorry, the store locator is not available at this time."]];
        [dialog show];
        return;
    }
    
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
    StoreLocatorScreenVC *storeLocatorScreenVC = [[StoreLocatorScreenVC alloc] init];
    [self presentViewController:storeLocatorScreenVC animated:NO completion:nil];
    
#ifdef CLP_ANALYTICS
    @try {                                                                //Store Locator tab click event
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"Store Locator" forKey:@"link_clicked"];
        [data setValue:@"TabClicked" forKey:@"event_name"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// more menu end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// product menu buttons begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (void)productSaleButtonPressed
{
    [_productMenu hideItems];
    
    //    if(_categoryView != nil && _categoryType == PROMO_CATEGORIES)
    //        return;
    
    [self removeViews];
    [_contentView removeGestureRecognizer:_tapRecognizer];
    [self setActiveMenu:_productMenu];
    _categoryType = PROMO_CATEGORIES;
    [self showProductsByCategoryView];
    
#ifdef CLP_ANALYTICS
    @try {                                                                //Sale Products menu click
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"Sale Products" forKey:@"link_clicked"];
        [data setValue:@"MenuOpened" forKey:@"event_name"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
}


- (void)productSearchButtonPressed
{
    [_productMenu hideItems];
    
    //    if(_categoryView != nil && _categoryType == PRODUCT_CATEGORIES)
    //        return;
    
    [self removeViews];
    [_contentView removeGestureRecognizer:_tapRecognizer];
    [self setActiveMenu:_productMenu];
    _categoryType = PRODUCT_CATEGORIES;
    [self showProductsByCategoryView];
    
#ifdef CLP_ANALYTICS
    //Analytics
    @try {                                                                //All Products menu click
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"All Products" forKey:@"link_clicked"];
        [data setValue:@"MenuOpened" forKey:@"event_name"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
}


- (void)productScanButtonPressed
{
    if([_productMenu hidden] == NO) // bottom menu  button is hidden behind the header and can be pressed when the menu is not visible...
    {
        [_productMenu hideItems];
        
        if(_scannerView != nil)
            return;
        
        [self removeViews];
        [self setActiveMenu:_productMenu];
        [self showScannerView];
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// product menu buttons end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// offer menu buttons begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (void)availableOffersButtonPressed
{
    
    [_offerMenu hideItems];
    
    // check that the offers were retrieved on startup
    if(_app._moreForYouOffersList.count == 0 || _app._extraFriendzyOffersList.count == 0)
    {
        if([Utility isNetworkAvailable] == NO)
        {
            TextDialog *networkErrorDialog = [[TextDialog alloc] initWithView:View title:INTERNET_UNAVAILABLE message:@"Please check your internet connection and press OK to continue."];
            [networkErrorDialog show];
            return;
        }
        
        [self removeViews];
        [self setActiveMenu:_offerMenu];
        _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Searching for your offers..."];
        [_progressDialog show];
        [_app getAvailableOffers];
        [NSThread detachNewThreadSelector:@selector(waitForOffersThread:) toTarget:self withObject:OFFERS_PERSONALIZED_URL];
    }
    else
    {
        [self removeViews];
        [self setActiveMenu:_offerMenu];
        [self showAvailableOffersView];
    }
#ifdef CLP_ANALYTICS
    @try {                                                                //Available Offer menu click
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"MenuOpened" forKey:@"event_name"];
        [data setValue:@"Available Offers" forKey:@"link_clicked"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
    
}


- (void)acceptedOffersButtonPressed
{
    [_offerMenu hideItems];
    
    if([Utility isNetworkAvailable] == NO)
    {
        TextDialog *networkErrorDialog = [[TextDialog alloc] initWithView:View title:INTERNET_UNAVAILABLE message:@"Please check your internet connection and press OK to continue."];
        [networkErrorDialog show];
        return;
    }
    
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving accepted offers..."];
    [_progressDialog show];
    [_app getAcceptedOffers];
    [NSThread detachNewThreadSelector:@selector(waitForOffersThread:) toTarget:self withObject:OFFERS_ACCEPTED_URL];
}


- (void)googleOffersButtonPressed
{
    if([_offerMenu hidden] == NO) // bottom menu button is hidden behind the header and can be pressed when the menu is not visible...
    {
        [_offerMenu hideItems];
        
        [self underConstruction:@"Google Offers page coming soon..."];
        //[self removeViews];
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// offer menu buttons end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// shopping list menu buttons begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (void)showActiveList
{
    @try {
        
        [self removeMenus];
        if([Utility isEmpty:_app._currentShoppingList])
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"List Error" message:@"No active list to add into. Goto lists and make one active."];
            [dialog show];
            return;
        }
        
        if(_shoppingListView != nil)
            return;
        
        _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving product data..."];
        [_progressDialog show];
        [_app getProductImages:_app._currentShoppingList.productList]; // this is a thread function so we need our own thread function to determine when it's done
        [NSThread detachNewThreadSelector:@selector(waitForProductImagesThread:) toTarget:self withObject:@"ShoppingList"];
    }
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
    }
#ifdef CLP_ANALYTICS
    @try {                                                                //Lists menu click
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"MenuOpened" forKey:@"event_name"];
        [data setValue:@"Lists" forKey:@"link_clicked"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
}


- (void)setActiveListButtonPressed
{
    [_listMenu hideItems];
    [self setActiveList];
    
#ifdef CLP_ANALYTICS
    @try {                                                                //Set Active menu click
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"MenuOpened" forKey:@"event_name"];
        [data setValue:@"List - Set Active" forKey:@"link_clicked"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
}


- (void)deleteActiveListButtonPressed
{
    [_listMenu hideItems];
    [self deleteList];
    
#ifdef CLP_ANALYTICS
    @try {                                                                //Delete List menu click
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"MenuOpened" forKey:@"event_name"];
        [data setValue:@"Delete List" forKey:@"link_clicked"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
}


- (void)createNewListButtonPressed
{
    if([_listMenu hidden] == NO) // bottom menu  button is hidden behind the header and can be pressed when the menu is not visible...
    {
        [_listMenu hideItems];
        [self createList];
        
#ifdef CLP_ANALYTICS
        @try {                                                                //Craete New List menu click
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"MenuOpened" forKey:@"event_name"];
            [data setValue:@"Create New List" forKey:@"link_clicked"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
#endif
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// shopping list menu buttons end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// products stuff begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (void)showProductsByCategoryView
{
    
    if(_service!=nil){
        return;
    }
    
    
    NSString *url;
    PromoCategoriesRequest *categoriesRequest = nil;
    
    if(_categoryType == PRODUCT_CATEGORIES)
    {
        if(![Utility isEmpty:_app._productCategories])
        {
            [self showCategoryButtons];
            return;
        }
        else
        {
            url = PRODUCT_CATEGORIES_URL;
        }
    }
    else
    {
        if(![Utility isEmpty:_app._promoCategories] && ![Utility isEmpty:_app._promoCategories.categoryList])
        {
            [self showCategoryButtons];
            return;
        }
        else
        {
            url = PROMO_CATEGORIES_URL;
            categoriesRequest = [[PromoCategoriesRequest alloc] init];
            categoriesRequest.storeNumber = _login.storeNumber;
        }
    }
    
    if(_progressDialog!=nil){
        [_progressDialog dismiss];
        _progressDialog = nil;
    }
    
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:_categoryType == PRODUCT_CATEGORIES ? @"Retrieving product categories..." : @"Retrieving sale categories..."];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ProductCategory"];
    [_service execute:url authorization:_login.authKey requestObject:categoriesRequest requestType:POST]; // response handled by handleProductCategoryServiceResponse method below
}


- (void)handleProductCategoryServiceResponse:(id)responseObject
{
    @try {
        
        int status = [_service getHttpStatusCode];
        
        if(status == 200) // service success
        {
            _service = nil;
            
            if(_categoryType == PRODUCT_CATEGORIES)
            {
                _app._productCategories = (ProductCategory *)responseObject;
                LogInfo(@"Found %lu categories", (unsigned long)_app._productCategories.categoryList.count);
            }
            else
            {
                _app._promoCategories = (ProductCategory *)responseObject;
                LogInfo(@"Found %lu categories", (unsigned long)_app._promoCategories.categoryList.count);
            }
        }
        else // service failure
        {
            if(status == 422) // backend error
            {
                WebServiceError *error = [_service getError];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Category Retrieve Failed" message:error.errorMessage];
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
            return;
        }
        
        if((![Utility isEmpty:_app._productCategories] && _app._productCategories.categoryList.count > 0) || (![Utility isEmpty:_app._promoCategories] && _app._promoCategories.categoryList.count > 0))
        {
            [self showCategoryButtons];
        }
        else
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Sorry, the product categories service is not available at this time. Please try again later."]];
            [dialog show];
        }
    }
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
        
    }
}


/*- (void)animateCategoryScrollView
 {
 [UIView animateWithDuration:0.33f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut | UIViewAnimationOptionAllowUserInteraction
 animations:^{ [_categoryMenu setFrame:_childViewVisibleFrame]; }
 completion:^(BOOL finished){ }];
 }*/


- (void)showCategoryButtons
{
    [self removeViews];
    [self setActiveMenu:_productMenu];
    
    [_contentView setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]];
    
    int searchBarHeight = _app._headerHeight;
    int categoryViewHeight = _contentViewHeight - searchBarHeight;
    
    _categoryView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, _contentViewWidth, _contentViewHeight)];
    _searchTextBackground = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _contentViewWidth, searchBarHeight)];
    // _searchTextBackground.image = [UIImage imageNamed:@"search_bar"];
    [_searchTextBackground setBackgroundColor:[UIColor colorWithRed:(206.0/255.0) green:(204.0/255.0) blue:(204.0/255.0) alpha:1.0]];
    [_categoryView addSubview:_searchTextBackground];
    
    int textFieldHeight = searchBarHeight * .8;
    //    UIFont *textFieldFont = [Utility fontForFamily:_app._normalFont andHeight:textFieldHeight * .6];
    _searchTextField = [[UITextField alloc] initWithFrame:CGRectMake(_contentViewWidth * .030, searchBarHeight * .12, _contentViewWidth * .94, textFieldHeight)];
    _searchTextField.keyboardType = UIKeyboardTypeAlphabet;
    _searchTextField.returnKeyType = UIReturnKeySearch;
    _searchTextField.delegate = self;
    _searchTextField.backgroundColor = [UIColor whiteColor];
    _searchTextField.clearButtonMode = UITextFieldViewModeUnlessEditing;
    _searchTextField.textColor = [UIColor blackColor];
    _searchTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _searchTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    _searchTextField.placeholder = @"Search";
    _searchTextField.font=[UIFont fontWithName:_app._normalFont size:font_size13];
    [_searchTextField.layer setBorderWidth:1.0f];
    [_searchTextField.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [_searchTextField.layer setCornerRadius:4.0f];
    
    //Search textfiled leftside space view
    UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, _searchTextField.frame.size.height)];
    leftView.backgroundColor = _searchTextField.backgroundColor;
    _searchTextField.leftView = leftView;
    _searchTextField.leftViewMode = UITextFieldViewModeAlways;
    [_categoryView addSubview:_searchTextField];
    globalMainSearch = _searchTextField;
    
    //    int buttonSize = searchBarHeight * .7;
    //    UIImage *searchButtonImage;
    //
    //    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    //        searchButtonImage = [UIImage imageNamed:@"search_button"];
    //    else
    //        searchButtonImage = [UIImage imageNamed:@"search_button_large"];
    //
    //    UIButton *searchButton = [[UIButton alloc] initWithFrame:CGRectMake(_contentViewWidth - (buttonSize * 1.2), searchBarHeight * .15, buttonSize, buttonSize)];
    //    /*[searchButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    //     [searchButton setTitle:@"Search" forState:UIControlStateNormal];
    //     searchButton.titleLabel.font = [Utility fontForFamily:_app._boldFont andHeight:buttonSize * .6];*/
    //    [searchButton setBackgroundImage:searchButtonImage forState:UIControlStateNormal];
    //    [searchButton addTarget:self action:@selector(searchButtonPressed) forControlEvents:UIControlEventTouchDown];
    //    [_categoryView addSubview:searchButton];
    
    _categoryMenu = [[UIScrollView alloc] initWithFrame:CGRectMake(0, searchBarHeight, _contentViewWidth, categoryViewHeight)];
    _categoryMenu.contentSize = CGSizeMake(_contentViewWidth, categoryViewHeight);
    _categoryMenu.delegate = self;
    _scrollViewYOffset = 0;
    _selectedButton = nil;
    [self showCategoryMenu];
    [_categoryView addSubview:_categoryMenu];
    _categoryMenu.delegate=self;
    [_categoryMenu setShowsHorizontalScrollIndicator:NO];
    [_categoryMenu setShowsVerticalScrollIndicator:NO];
    [_categoryMenu flashScrollIndicators];
    
    [_contentView addSubview:_categoryView];
}


- (void)initializeProductView
{
    [_multiplePageProductList removeAllObjects];
    _currentProductIndex = 0;
    _currentProductCategory = nil;
    _currentSearchText = nil;
}


- (void)Search_Bar
{
    int searchBarHeight = _app._headerHeight;
    // int categoryViewHeight = _contentViewHeight - searchBarHeight;
    
    product_page_search_view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, _contentViewWidth, 40.0f)];
    _searchTextBackground = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _contentViewWidth, searchBarHeight)];
    //    _searchTextBackground.image = [UIImage imageNamed:@"search_bar"];
    [product_page_search_view addSubview:_searchTextBackground];
    [_searchTextBackground setBackgroundColor:[UIColor colorWithRed:(206.0/255.0) green:(204.0/255.0) blue:(204.0/255.0) alpha:1.0]];
    [product_page_search_view addSubview:_searchTextBackground];
    
    int textFieldHeight = searchBarHeight * .8;
    
    _searchTextField = [[UITextField alloc] initWithFrame:CGRectMake(_contentViewWidth * .030, searchBarHeight * .12, _contentViewWidth * .94, textFieldHeight)];
    _searchTextField.keyboardType = UIKeyboardTypeAlphabet;
    _searchTextField.returnKeyType = UIReturnKeySearch;
    _searchTextField.delegate = self;
    _searchTextField.backgroundColor = [UIColor whiteColor];
    _searchTextField.textColor = [UIColor blackColor];
    _searchTextField.clearButtonMode = UITextFieldViewModeUnlessEditing;
    _searchTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _searchTextField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    _searchTextField.placeholder = @"Search";
    _searchTextField.font=[UIFont fontWithName:_app._normalFont size:font_size13];
    [_searchTextField.layer setBorderWidth:1.0f];
    [_searchTextField.layer setBorderColor:[UIColor lightGrayColor].CGColor];
    [_searchTextField.layer setCornerRadius:4.0f];
    
    //Search textfiled leftside space view
    UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, _searchTextField.frame.size.height)];
    leftView.backgroundColor = _searchTextField.backgroundColor;
    _searchTextField.leftView = leftView;
    _searchTextField.leftViewMode = UITextFieldViewModeAlways;
    
    
    [product_page_search_view addSubview:_searchTextField];
    
    //    int buttonSize = searchBarHeight * .7;
    //    UIImage *searchButtonImage;
    //
    //    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    //        searchButtonImage = [UIImage imageNamed:@"search_button"];
    //    else
    //        searchButtonImage = [UIImage imageNamed:@"search_button_large"];
    //
    //    UIButton *searchButton = [[UIButton alloc] initWithFrame:CGRectMake(_contentViewWidth - (buttonSize * 1.2), searchBarHeight * .15, buttonSize, buttonSize)];
    //
    //    [searchButton setBackgroundImage:searchButtonImage forState:UIControlStateNormal];
    //    [searchButton addTarget:self action:@selector(searchButtonPressed) forControlEvents:UIControlEventTouchDown];
    //    [product_page_search_view addSubview:searchButton];
    
    _selectedButton = nil;
    
    [_contentView addSubview:product_page_search_view];
    
    [_contentView insertSubview:product_page_search_view aboveSubview:_productView];
}


- (void)searchButtonPressed
{
    [self removeMenus];
    
    [_searchTextField resignFirstResponder];
    [self.view endEditing:YES];
    
    if([Utility isEmpty:_searchTextField.text])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:@"Search Field can not be blank."];
        [dialog show];
    }
    else
    {
        [self initializeProductView];
        _currentSearchText = _searchTextField.text;
        [self showProductsForCategory:_currentProductCategory forSearch:_currentSearchText fromIndex:_currentProductIndex];
    }
}


- (void)showCategoryMenu
{
    int yOrigin = 0;
    int level1ProductCategoryId = 0;
    int level2ProductCategoryId = 0;
    BOOL level1Expanded = NO;
    BOOL level2Expanded = NO;
    
    if(_selectedButton != nil)
    {
        if(_selectedButton._productCategory.subCategoryList.count == 0) // the lowest category level, need to display or hide the table here
        {
            [self initializeProductView];
            _currentProductCategory = _selectedButton._productCategory;
            [self showProductsForCategory:_currentProductCategory forSearch:_currentSearchText fromIndex:_currentProductIndex];
            return;
        }
        
        [_categoryMenu.subviews makeObjectsPerformSelector: @selector(removeFromSuperview)]; // remove the previous scroll view's buttons
        
        if(_selectedButton._productCategory.level == 1)
        {
            level1ProductCategoryId = _selectedButton._productCategory.productCategoryId;
            
            if(_selectedButton._expanded == YES)
                level1Expanded = YES;
        }
        else if(_selectedButton._productCategory.level == 2)
        {
            level1ProductCategoryId = _selectedButton._productCategory.parentCategoryId;
            level2ProductCategoryId = _selectedButton._productCategory.productCategoryId;
            
            if(_selectedButton._expanded == YES)
            {
                level1Expanded = YES;
                level2Expanded = YES;
            }
            else
            {
                level1Expanded = YES;
            }
        }
        else if(_selectedButton._productCategory.level == 3)
        {
            level1ProductCategoryId = _selectedButton._productCategory.grandParentCategoryId;
            level2ProductCategoryId = _selectedButton._productCategory.parentCategoryId;
            level1Expanded = YES;
            level2Expanded = YES;
        }
    }
    
    NSArray *categoryList;
    
    if(_categoryType == PRODUCT_CATEGORIES)
        categoryList = _app._productCategories.categoryList;
    else // PROMO_CATEGORIES
        categoryList = _app._promoCategories.categoryList;
    
    
    int cell_height = _categoryButtonHeight;
    cell_height = 44;
    
    // always create buttons for the first level
    for(int i = 0; i < categoryList.count; i++)
    {
        // always create level 1 buttons
        ProductCategory *level1ProductCategory = [categoryList objectAtIndex:i];
        ProductCategoryButton *button = [[ProductCategoryButton alloc] initWithFrame:CGRectMake(0, yOrigin, _app._viewWidth, cell_height) productCategory:level1ProductCategory];
        
        // save the location of this button so we can scroll it appropriately after the whole scroll view is constructed
        if(_selectedButton != nil && _selectedButton._productCategory.level == 1 && level1ProductCategory.productCategoryId == _selectedButton._productCategory.productCategoryId)
        {
            _selectedButtonYOrigin = yOrigin;
            [button setExpandedState:_selectedButton._expanded];
        }
        
        if(level1Expanded == YES && level1ProductCategory.productCategoryId == _selectedButton._productCategory.parentCategoryId)
            [button setExpandedState:YES];
        
        button.tag = level1ProductCategory.productCategoryId;
        [button addTarget:self action:@selector(categoryButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
        [_categoryMenu addSubview:button];
        yOrigin += cell_height;
        
        
        if(_selectedButton != nil && button._expanded == YES)
        {
            if(level1ProductCategoryId == level1ProductCategory.productCategoryId)
            {
                // create buttons for the second level
                if(![Utility isEmpty:level1ProductCategory.subCategoryList])
                {
                    for(int i = 0; i < level1ProductCategory.subCategoryList.count; i++)
                    {
                        ProductCategory *level2ProductCategory = [level1ProductCategory.subCategoryList objectAtIndex:i];
                        ProductCategoryButton *button = [[ProductCategoryButton alloc] initWithFrame:CGRectMake(0, yOrigin, _app._viewWidth, cell_height) productCategory:[level1ProductCategory.subCategoryList objectAtIndex:i]];
                        
                        // save the location of this button so we can scroll it appropriately after the whole scroll view is constructed
                        if(_selectedButton._productCategory.level == 2 && level2ProductCategory.productCategoryId == _selectedButton._productCategory.productCategoryId)
                        {
                            [button setExpandedState:_selectedButton._expanded];
                            _selectedButtonYOrigin = yOrigin;
                        }
                        
                        //                        level1ProductCategoryId = 10025;
                        button._productCategory.parentCategoryId = level1ProductCategoryId;//onsale fix
                        button._productCategory.parentCategoryName = level1ProductCategory.name;
                        
                        button.tag = level2ProductCategory.productCategoryId;
                        [button addTarget:self action:@selector(categoryButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
                        [_categoryMenu addSubview:button];
                        yOrigin += cell_height;
                        
                        if(level2ProductCategoryId == level2ProductCategory.productCategoryId)
                        {
                            if(![Utility isEmpty:level2ProductCategory.subCategoryList] && level2Expanded == YES)
                            {
                                for(int i = 0; i < level2ProductCategory.subCategoryList.count; i++)
                                {
                                    ProductCategory *level3ProductCategory = [level2ProductCategory.subCategoryList objectAtIndex:i];
                                    ProductCategoryButton *button = [[ProductCategoryButton alloc] initWithFrame:CGRectMake(0, yOrigin, _app._viewWidth, cell_height) productCategory:[level2ProductCategory.subCategoryList objectAtIndex:i]];
                                    button.tag = level3ProductCategory.productCategoryId;
                                    [button addTarget:self action:@selector(categoryButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
                                    [_categoryMenu addSubview:button];
                                    button._productCategory.parentCategoryId = level2ProductCategoryId;//onsale fix
                                    button._productCategory.grandParentCategoryId = level1ProductCategoryId;//onsale fix
                                    
                                    button._productCategory.grandParentCategoryName = level1ProductCategory.name;
                                    button._productCategory.parentCategoryName = level2ProductCategory.name;
                                    yOrigin += cell_height;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    _categoryMenu.contentSize = CGSizeMake(_app._viewWidth, yOrigin);
    
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(removeMenus)];
    [singleTap setCancelsTouchesInView:NO];
    [_categoryMenu addGestureRecognizer:singleTap];
    
    if(_scrollViewYOffset > _selectedButtonYOrigin)
        _scrollViewYOffset = _selectedButtonYOrigin;
    
    [_categoryMenu setContentOffset:CGPointMake(0, _scrollViewYOffset) animated:NO];
}


- (void)categoryButtonPressed:(id)sender
{
    [self removeMenus];
    _selectedButton = (ProductCategoryButton *)sender;
    [_selectedButton toggleExpandedState];
    _scrollViewYOffset = (int)_categoryMenu.contentOffset.y;
    [self showCategoryMenu];
    
    
#ifdef CLP_ANALYTICS
    if(![Utility isEmpty:_selectedButton]){
        @try {                                                                  //Product Category tree menu click
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            
            if(_selectedButton._productCategory.parentCategoryId ==0)           //If Top Level, capture as department clicked
                if(_categoryType == PROMO_CATEGORIES)
                    [data setValue:@"CategoryViewed" forKey:@"event_name"];
                else
                    [data setValue:@"DeptViewed" forKey:@"event_name"];
            else {                                                              //Else Category Click
                [data setValue:@"CategoryViewed" forKey:@"event_name"];
                [data setValue:[NSString stringWithFormat:@"%d",_selectedButton._productCategory.parentCategoryId] forKey:@"Parent_item_ID"];
                [data setValue:[NSString stringWithFormat:@"%d",_selectedButton._productCategory.grandParentCategoryId] forKey:@"Grand_Parent_item_ID"];
                [data setValue:_selectedButton._productCategory.parentCategoryName forKeyPath:@"Parent_item_Name"];
                [data setValue:_selectedButton._productCategory.grandParentCategoryName forKeyPath:@"Grand_Parent_item_Name"];
            }
            [data setValue:[NSString stringWithFormat:@"%d",_selectedButton._productCategory.productCategoryId]  forKey:@"item_id"];
            [data setValue:_selectedButton._productCategory.name forKeyPath:@"item_name"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
    }
#endif
}


- (void)showProductsForCategory:(ProductCategory *)category forSearch:(NSString *)searchText fromIndex:(int)startIndex
{
    if(_service!=nil) return;
    
    if(startIndex==0){
        _currentPageProductList = [[NSMutableArray alloc]init];
    }
    if(_productsInCategoryCount!=0 && startIndex >= _productsInCategoryCount)
    {
        return;
    }
    
    //    startIndex = 0;
    //    // first check to see if we have already downloaded the products for the page
    //    if(startIndex < _multiplePageProductList.count)
    //    {
    //        _currentProductPage = startIndex;//(startIndex / PRODUCT_FETCH_LIMIT) + 1;
    //        int productsToDisplay;
    //
    //        productsToDisplay = _multiplePageProductList.count;
    //        //        if(startIndex + PRODUCT_FETCH_LIMIT <= _multiplePageProductList.count)
    //        //            productsToDisplay = PRODUCT_FETCH_LIMIT;
    //        //        else
    //        //            productsToDisplay = _multiplePageProductList.count % PRODUCT_FETCH_LIMIT;
    //
    //
    //
    //        _currentPageProductList = [_multiplePageProductList objectsAtIndexes:[[NSIndexSet alloc] initWithIndexesInRange:NSMakeRange(startIndex, productsToDisplay)]];
    //        [_app getProductImages:_currentPageProductList]; // this is a thread function so we need our own thread function to determine when it's done
    //        [NSThread detachNewThreadSelector:@selector(waitForProductListImagesThread:) toTarget:self withObject:_currentCategoryName];
    //        return;
    //    }
    
    ProductsForCategoryRequest *request = [[ProductsForCategoryRequest alloc] init];
    request.storeNumber = [_app getStoreNumber];
    request.startIndex = startIndex;
    request.count = PRODUCT_FETCH_LIMIT;
    
    if(![Utility isEmpty:category])
    {
        _currentCategoryName = category.name;
        
        if(category.level == 1)
        {
            request.cat1Id = category.productCategoryId;
        }
        else if(category.level == 2)
        {
            request.cat1Id = category.parentCategoryId;
            request.cat2Id = category.productCategoryId;
        }
        else if(category.level == 3)
        {
            request.cat1Id = category.grandParentCategoryId;
            request.cat2Id = category.parentCategoryId;
            request.cat3Id = category.productCategoryId;
        }
        
        _currentCategoryKey = [NSString stringWithFormat:@"%d:%d:%d", request.cat1Id, request.cat2Id, request.cat3Id];
        
        
//#ifdef CLP_ANALYTICS
//        //Analytics
//        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
//        [data setValue:[NSString stringWithFormat:@"%d",request.storeNumber] forKey:@"PC_StoreNumber"];
//        [data setValue:[NSString stringWithFormat:@"%d",request.cat1Id] forKey:@"PC_Category1ID"];
//        [data setValue:[NSString stringWithFormat:@"%d",request.cat2Id] forKey:@"PC_Category2ID"];
//        [data setValue:[NSString stringWithFormat:@"%d",request.cat3Id] forKey:@"PC_Category3ID"];
//        [data setValue:[NSString stringWithFormat:@"%d",request.startIndex] forKey:@"PC_StartIndex"];
//        
//        //new
//        [data setValue:@"Category Viewed" forKey:@"event_name"];
//        [data setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
//        [_app._clpSDK updateAppEvent:data];
//#endif
        
        
    }
    else if(![Utility isEmpty:searchText])
    {
        _currentCategoryName = searchText;
        request.searchText = searchText;
        _currentCategoryKey = nil;
        
#ifdef CLP_ANALYTICS
        @try {                                                                //Search Event
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"Filter" forKey:@"event_name"];
            [data setValue:searchText forKey:@"Search_Keyword"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
#endif
        
    }
    else
    {
        LogError(@"showProductsForCategory received no parameters");
        return;
    }
    
    //    request.cat1Id = 10028;
    //    request.cat2Id = 17;
    //    request.cat3Id = 0;
    //    request.startIndex = 0;
    //    request.count = 100;
    //    request.searchText = nil;
    //    request.storeNumber = 405; // 405 515
    
    
    
    
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving product list..."];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ProductsForCategoryResponse"];
    
    NSString *url;
    // for search purpose
    if(![Utility isEmpty:searchText]){
        //url = PRODUCTS_FOR_CATEGORY_URL;
        url = PRODUCTS_BY_SEARCH_CATEGORY_URL;
    }
    else if(_categoryType == PRODUCT_CATEGORIES)
    {
        url = PRODUCTS_FOR_CATEGORY_URL;
    }else // PROMO_CATEGORIES
    {
        url = PRODUCTS_FOR_PROMO_CATEGORY_URL;
    }
    
    
    [_service execute:url authorization:_login.authKey requestObject:request requestType:POST]; // response handled by  handleProductListServiceResponse method below
}

-(void)productScrollToIndexPath:(NSIndexPath*)indexPath{
    @try {
        if(indexPath.item>=_currentPageProductList.count) return;
        
        [_productGrid scrollToItemAtIndexPath:indexPath atScrollPosition:UICollectionViewScrollPositionBottom animated:YES];
    }
    @catch (NSException *exception) {
        
    }
    [_progressDialog dismiss];
}

- (void)handleProductListServiceResponse:(id)responseObject
{
    @try {
        
        int status = [_service getHttpStatusCode];
        ProductsForCategoryResponse *response;
        
        if(status == 200) // service success
        {
            
            response = (ProductsForCategoryResponse *)responseObject;
            _productPageCount = response.productsInCategoryCount; // (response.productsInCategoryCount / PRODUCT_FETCH_LIMIT) + 1;
            _currentProductPage = _productPageCount; // (response.responseStartIndex / PRODUCT_FETCH_LIMIT) + 1;
            _productsInCategoryCount = response.productsInCategoryCount;
            [_multiplePageProductList addObjectsFromArray:_currentPageProductList];
            
            if(_currentPageProductList.count>0){
                // next search
                NSIndexPath *indexPath = [NSIndexPath indexPathForItem:_currentPageProductList.count inSection:0];
                [_currentPageProductList addObjectsFromArray:response.productList];
                [_productGrid reloadData];
                [self performSelector:@selector(productScrollToIndexPath:) withObject:indexPath afterDelay:1.0];
                _service = nil;
                return;
            }else{
                //                NSIndexPath *indexPath = [NSIndexPath indexPathForItem:0 inSection:0];
                //                [_productGrid scrollToItemAtIndexPath:indexPath atScrollPosition:UICollectionViewScrollPositionBottom animated:NO];
                
                CGRect frame = _productGrid.frame;
                frame.origin.y = 0;
                [_productGrid scrollRectToVisible:frame animated:NO];
            }
            [_currentPageProductList addObjectsFromArray:response.productList];
        }
        else // service failure
        {
            [_progressDialog dismiss];
            
            if(status == 422) // backend error
            {
                //            _categoryView.hidden = NO;
                WebServiceError *error = [_service getError];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Product Retrieve Failed" message:error.errorMessage];
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
            return;
        }
        
        [_progressDialog changeMessage:@"Retrieving product data..."];
        [_app getProductImages:response.productList]; // this is a thread function so we need our own thread function to determine when it's done
        //    [NSThread detachNewThreadSelector:@selector(waitForProductImagesThread:) toTarget:self withObject:@"ProductList"];
        [_progressDialog dismiss];
        _service = nil;
        [self performSelectorOnMainThread:@selector(showProductView) withObject:nil waitUntilDone:NO];
        
    }
    @catch (NSException *exception) {
        [_progressDialog dismiss];
        _service = nil;
        LogInfo("%@",exception.reason);
        
    }
    
}


// thread function that waits for image downloading to complete
- (void)waitForProductListImagesThread:(NSObject *)name
{
    @autoreleasepool
    {
        int count = 0;
        
        while([_app imageThreadsDone] == NO && count++ < 50)
        {
            if(!(count % 10))
                LogInfo(@"Waiting for image threads to finish %d", count / 10);
            
            [NSThread sleepForTimeInterval:.1];
        }
        
        [_progressDialog dismiss];
        [self performSelectorOnMainThread:@selector(showProductView) withObject:nil waitUntilDone:NO];
    }
}


- (void)showProductView
{
    [_searchTextField resignFirstResponder];
    _categoryView.hidden = YES;
    [backButton setHidden:NO];
    
    [_contentView setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]];
    
    if(_productView == nil)
    {
        _productView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, _contentViewWidth, _contentViewHeight )];
        [_contentView addSubview:_productView];
        
        int headerHeight = _categoryButtonHeight;
        headerHeight = 40;
        UIImageView *header = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _contentViewWidth, headerHeight)];
        header.image = [UIImage imageNamed:@"categories_bar"];
        
        [_productView addSubview:header];
        
        //  int buttonHeight = headerHeight * .8;
        // int buttonYOrigin = (headerHeight - buttonHeight) / 2;
        int buttonWidth = _contentViewWidth * .05;
        //   int buttonPad = _contentViewWidth * .01;
        
        //        UIButton *backToCategoryButton = [[UIButton alloc] initWithFrame:CGRectMake(buttonPad, buttonYOrigin, _contentViewWidth * .21, buttonHeight)];
        //        [backToCategoryButton setBackgroundImage:[UIImage imageNamed:@"categories_arrow"] forState:UIControlStateNormal];
        //        backToCategoryButton.contentEdgeInsets = UIEdgeInsetsMake(2.5, 0.0, 0.0, 0.0);
        //        backToCategoryButton.titleLabel.font = [Utility fontForFamily:_boldFont andHeight:buttonHeight * .5];
        //        [backToCategoryButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        //        [backToCategoryButton setTitle:@"Categories" forState:UIControlStateNormal];
        //  [backToCategoryButton addTarget:self action:@selector(backToCategoryButtonPressed) forControlEvents:UIControlEventTouchDown];
        //        [_productView addSubview:backToCategoryButton];
        
        //        _reverseButton = [[UIButton alloc] initWithFrame:CGRectMake(_contentViewWidth * .78, buttonYOrigin, buttonWidth, buttonHeight)];
        //        [_reverseButton setBackgroundImage:[UIImage imageNamed:@"arrow_backward_red"] forState:UIControlStateNormal];
        //        [_reverseButton setBackgroundImage:[UIImage imageNamed:@"arrow_backward_gray"] forState:UIControlStateSelected];
        //        [_reverseButton addTarget:self action:@selector(reverseButtonPressed) forControlEvents:UIControlEventTouchDown];
        //        [_productView addSubview:_reverseButton];
        
        //        _forwardButton = [[UIButton alloc] initWithFrame:CGRectMake(_contentViewWidth * .95, buttonYOrigin, buttonWidth, buttonHeight)];
        //        [_forwardButton setBackgroundImage:[UIImage imageNamed:@"arrow_forward_red"] forState:UIControlStateNormal];
        //        [_forwardButton setBackgroundImage:[UIImage imageNamed:@"arrow_forward_gray"] forState:UIControlStateSelected];
        //        [_forwardButton addTarget:self action:@selector(forwardButtonPressed) forControlEvents:UIControlEventTouchDown];
        //        [_productView addSubview:_forwardButton];
        
        _productHeaderLabel = [[UILabel alloc] initWithFrame:CGRectMake(_contentViewWidth * .22, 0, _contentViewWidth * .56, headerHeight)];
        _productHeaderLabel.backgroundColor = [UIColor clearColor];
        //        _productHeaderLabel.font = [Utility fontForFamily:_app._normalFont andHeight:headerHeight * .7];
        _productHeaderLabel.font=[UIFont fontWithName:_app._normalFont size:font_size17];
        _productHeaderLabel.textAlignment = NSTextAlignmentCenter;
        _productHeaderLabel.textColor = [UIColor blackColor];
        _productHeaderLabel.text = _currentCategoryName;
        //        [_productView addSubview:_productHeaderLabel];
        
        int countXOrigin = _productHeaderLabel.frame.origin.x+_productHeaderLabel.frame.size.width + buttonWidth;
        int countWidth = _contentViewWidth - buttonWidth - countXOrigin;
        
        _productCountTopLabel = [[UILabel alloc] initWithFrame:CGRectMake(countXOrigin, headerHeight * .1, countWidth, headerHeight * .4)];
        _productCountTopLabel.backgroundColor = [UIColor clearColor];
        _productCountTopLabel.font = [Utility fontForFamily:_app._normalFont andHeight:headerHeight * .4];
        _productCountTopLabel.textAlignment = NSTextAlignmentCenter;
        _productCountTopLabel.textColor = [UIColor blackColor];
        //        [_productView addSubview:_productCountTopLabel];
        
        _productCountBottomLabel = [[UILabel alloc] initWithFrame:CGRectMake(countXOrigin, headerHeight * .45, countWidth, headerHeight * .4)];
        _productCountBottomLabel.backgroundColor = [UIColor clearColor];
        _productCountBottomLabel.font = [Utility fontForFamily:_app._normalFont andHeight:headerHeight * .4];
        _productCountBottomLabel.textAlignment = NSTextAlignmentCenter;
        _productCountBottomLabel.textColor = [UIColor blackColor];
        //        [_productView addSubview:_productCountBottomLabel];
        
        _productGrid = [[UICollectionView alloc] initWithFrame:CGRectMake(0, headerHeight+6.0f, _app._viewWidth, _contentViewHeight - _navigationBarHeight) collectionViewLayout:_productGridLayout];
        _productGrid.backgroundColor = [UIColor clearColor];
        _productGrid.dataSource = self;
        _productGrid.delegate = self;
        [_productGrid registerClass:[ProductGridCell class] forCellWithReuseIdentifier:PRODUCT_GRID_CELL_IDENTIFIER];
        
        _productGrid.showsHorizontalScrollIndicator=NO;
        _productGrid.showsVerticalScrollIndicator=NO;
        // drop shadow
        [_productGrid.layer setShadowColor:[UIColor darkGrayColor].CGColor];
        [_productGrid.layer setShadowOpacity:0.6];
        [_productGrid.layer setShadowRadius:0.3];
        [_productGrid.layer setShadowOffset:CGSizeMake(0.0,0.6)];
        
        
        
        [_productView addSubview:_productGrid];
        
        [_contentView addSubview:_productView];
        
        [self Search_Bar];
    }
    else
    {
        [_productGrid reloadData];
    }
    
    if(_currentProductPage == 1)
        _reverseButton.hidden = YES;
    else
        _reverseButton.hidden = NO;
    
    if(_currentProductPage < _productPageCount)
        _forwardButton.hidden = NO;
    else
        _forwardButton.hidden = YES;
    
    NSString *bottomText = @"";
    NSString *topText = @"";
    
    if(_productPageCount > 1)
    {
        topText = [NSString stringWithFormat:@"%d - %d", ((_currentProductPage - 1) * PRODUCT_FETCH_LIMIT) + 1, ((_currentProductPage - 1) * PRODUCT_FETCH_LIMIT) + (int)_currentPageProductList.count];
        bottomText = [NSString stringWithFormat:@"of %d",  _productsInCategoryCount];
    }
    else
    {
        topText = [NSString stringWithFormat:@"%lu", (unsigned long)_currentPageProductList.count];
        bottomText = @"Products";
    }
    
    _productCountTopLabel.text = topText;
    _productCountBottomLabel.text = bottomText;
}


- (void)backToCategoryButtonPressed
{
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [_productView removeFromSuperview];
    [product_page_search_view removeFromSuperview];
    
    _productView = nil;
    _categoryView.hidden = NO;
    [backButton setHidden:YES];
    
    _searchTextField = globalMainSearch;
}

- (void)backToShoppingListName
{
    if([self isWebServiceRunning]){
        return;
    }
    [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsLEFT];
    [_shoppingProductListContainer removeFromSuperview];
    _shoppingListGrid = nil;
    [shoppinglist_backbtn setHidden:YES];
    [self setActiveList];
}



- (void)forwardButtonPressed
{
    _currentProductIndex = (int)_currentPageProductList.count; //PRODUCT_FETCH_LIMIT;
    [self showProductsForCategory:_currentProductCategory forSearch:_currentSearchText fromIndex:_currentProductIndex];
}



- (void)reverseButtonPressed
{
    if(_currentProductIndex > 0)
    {
        _currentProductIndex -= PRODUCT_FETCH_LIMIT;
        [self showProductsForCategory:_currentProductCategory forSearch:_currentSearchText fromIndex:_currentProductIndex];
    }
    else
    {
        [_productView removeFromSuperview];
        _productView = nil;
        _categoryView.hidden = NO;
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// products stuff end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// offers stuff begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (void)showAvailableOffersView
{
    @try {
        
        [self removeViews];
        [self setActiveMenu:_offerMenu];
        
        if(scannerDialog!=nil){
            [scannerDialog close];
        }
        
        [_contentView setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]];
        
        if(_offersShown == NO)
            _offersShown = YES;
        
        int index = 0;
        _offerListsArray = [[NSMutableArray alloc] init];
        
        // this adds a transparent header cell that goes under the menu tabs
        //    NSObject *blank = [[NSObject alloc] init];
        //    [_offerListsArray addObject:blank];
        //    index++;
        
        // set up index path locations for menu buttons
        if(_app._personalizedOffersList.count > 0)
        {
            [_offerListsArray addObject:_app._personalizedOffersList];
            _personalizedOffersListIndexPath = index++;
        }
        else
        {
            _personalizedOffersListIndexPath = -1;
        }
        
        // Extra Frendzy Offer
        BOOL showExtraFriendzyOffer = NO;
        if(_app.beaconSchedule && _app.beaconSchedule.enableExtraFrency){
            showExtraFriendzyOffer = YES;
        }
        if(_app._extraFriendzyOffersList.count > 0 && showExtraFriendzyOffer)
        {
            [_offerListsArray addObject:_app._extraFriendzyOffersList];
            _extraFriendzyOffersListIndexPath = index++;
        }
        else
        {
            _extraFriendzyOffersListIndexPath = -1;
        }
        //
        
        if(_app._moreForYouOffersList.count > 0)
        {
            [_offerListsArray addObject:_app._moreForYouOffersList];
            _moreForYouOffersListIndexPath = index++;
        }
        else
        {
            _moreForYouOffersListIndexPath = -1;
        }
        
        if(_offerListsArray.count > 0)
        {
            _offersView = [[UIView alloc] initWithFrame:CGRectMake(0, -kNavigationBarHeight, _contentViewWidth, _contentViewHeight+kNavigationBarHeight)];
            CGRect tableFrame = CGRectMake(0, kNavigationBarHeight, _contentViewWidth, _contentViewHeight);
            
            [_offerGrid removeFromSuperview];
            dispatch_async(dispatch_get_main_queue(), ^{
                @try {
                
                _offerGrid = [[UICollectionView alloc] initWithFrame:tableFrame collectionViewLayout:_offerGridLayout];
                _offerGrid.backgroundColor = [UIColor clearColor];
                _offerGrid.dataSource = self;
                _offerGrid.delegate = self;
                
                _offerGrid.showsHorizontalScrollIndicator=NO;
                _offerGrid.showsVerticalScrollIndicator=NO;
                
                [_offerGrid registerClass:[OfferGridCell class] forCellWithReuseIdentifier:OFFER_GRID_CELL_IDENTIFIER];
                [_offerGrid registerClass:[OfferHeaderCell class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:OFFER_HEADER_CELL_IDENTIFIER];
                [_offerGrid registerClass:[BlankHeaderCell class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:BLANK_HEADER_CELL_IDENTIFIER];
                [_offersView addSubview:_offerGrid];
                [_contentView addSubview:_offersView];
                    
                } @catch (NSException *exception) {
                    NSLog(@"%@",[exception reason]);
                }
            });
            
        }
        if(noOfferMessage!=nil){
            [noOfferMessage removeFromSuperview];
        }
        
        // Check first time offer is Empty or Not
        if(!firstTimeShowOffer){
            if([_app offerThreadsDone] && _offerListsArray.count==0){
                [self productSearchButtonPressed];
                //productSearchButtonPressed
            }
            firstTimeShowOffer = YES;
        }else{
            if(_offerListsArray.count==0){
                
                noOfferMessage=[[UILabel alloc]initWithFrame:CGRectMake(0, (_contentView.frame.size.height-100)/2, _contentView.frame.size.width, 50)];
                [noOfferMessage setText:@"No offers available at this time"];
                noOfferMessage.font=[UIFont fontWithName:_app._normalFont size:font_size17];
                noOfferMessage.backgroundColor=[UIColor clearColor];
                [noOfferMessage setTextColor:[UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0]];
                [noOfferMessage setTextAlignment:NSTextAlignmentCenter];
                [_contentView addSubview:noOfferMessage];
                
                //            TextDialog *txtdialog=[[TextDialog alloc]initWithView:self.view title:@"Offers" message:@"No offers at this time"];
                //            [txtdialog show];
            }
        }
        

        
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception.reason);
    }
}


- (void)showAcceptedOffersView
{
    
    [_contentView setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]];
    
    if(_offersShown == NO)
        _offersShown = YES;
    
    int index = 0;
    _offerListsArray = [[NSMutableArray alloc] init];
    
    //    // this adds a transparent header cell that goes under the menu tabs
    //    NSObject *blank = [[NSObject alloc] init];
    //    [_offerListsArray addObject:blank];
    //    index++;
    
    // set up index path locations for menu buttons
    if(_app._acceptedOffersList.count > 0)
    {
        [_offerListsArray addObject:_app._acceptedOffersList];
        _acceptedOffersListIndexPath = index++;
    }
    else
    {
        _acceptedOffersListIndexPath = -1;
    }
    
    if(noOfferMessage!=nil){
        [noOfferMessage removeFromSuperview];
    }
    
    if(_offerListsArray.count > 0)
    {
        _offersView = [[UIView alloc] initWithFrame:CGRectMake(0, -kNavigationBarHeight, _contentViewWidth, _contentViewHeight+kNavigationBarHeight)];
        CGRect tableFrame = CGRectMake(0, kNavigationBarHeight, _contentViewWidth, _contentViewHeight);
        
        [_offerGrid removeFromSuperview];
        _offerGrid = [[UICollectionView alloc] initWithFrame:tableFrame collectionViewLayout:_offerGridLayout];
        _offerGrid.backgroundColor = [UIColor clearColor];
        _offerGrid.dataSource = self;
        _offerGrid.delegate = self;
        
        _offerGrid.showsHorizontalScrollIndicator=NO;
        _offerGrid.showsVerticalScrollIndicator=NO;
        
        [_offerGrid registerClass:[OfferGridCell class] forCellWithReuseIdentifier:OFFER_GRID_CELL_IDENTIFIER];
        [_offerGrid registerClass:[OfferHeaderCell class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:OFFER_HEADER_CELL_IDENTIFIER];
        [_offerGrid registerClass:[BlankHeaderCell class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:BLANK_HEADER_CELL_IDENTIFIER];
        [_offersView addSubview:_offerGrid];
        
        [_contentView addSubview:_offersView];
    }
    else{
        
        noOfferMessage=[[UILabel alloc]initWithFrame:CGRectMake(0, (_contentView.frame.size.height-100)/2, _contentView.frame.size.width, 50)];
        [noOfferMessage setText:@"No offers accepted at this time"];
        noOfferMessage.font=[UIFont fontWithName:_app._normalFont size:font_size17];
        noOfferMessage.backgroundColor=[UIColor clearColor];
        [noOfferMessage setTextColor:[UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0]];
        [noOfferMessage setTextAlignment:NSTextAlignmentCenter];
        [_contentView addSubview:noOfferMessage];
    }
    
#ifdef CLP_ANALYTICS
    @try {                                                                //Accept Offers menu click
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"MenuOpened" forKey:@"event_name"];
        [data setValue:@"Accepted Offers" forKey:@"link_clicked"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
}


#pragma mark ShoppingScreen Delegate(for offer)

-(void)RemoveOffers_DetailpageSelected
{
    [self showAvailableOffersView];
}


-(void)acceptOffer:(Offer *)offer
{
    //[cell._acceptedOfferLabel setImage:[UIImage imageNamed:@"offer_accepted.png"]];
    
    // return;
    //[self showTick];
    OfferAcceptRequest *request = [[OfferAcceptRequest alloc] init];
    request.crmNumber = _login.crmNumber;
    request.acceptGroup = offer.acceptGroup;
    request.offerId = offer.offerId;
    request.dynamicOffer = offer.dynamicOffer;
    if(offer.endDate){
        request.endDate = offer.endDate;
    }
    if(offer.consumerTitle){
        request.title = offer.consumerTitle;
    }
    if(offer.promoCode){
        request.promoCode = offer.promoCode;
    }
    
    [self refreshLocalArrays:offer];
    
    WebService *new_offer = [[WebService alloc]initWithListener:nil responseClassName:@"OfferAcceptRequest"];
    
    [new_offer execute:OFFER_ACCEPT_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleAcceptOfferServiceResponse method below
    
    
    //[_offerGrid reloadData];
    //[self performSelectorInBackground:@selector(reloadOfferTable) withObject:nil];
    [self performSelector:@selector(reloadOfferTable) withObject:nil afterDelay:0.2];
    
    @try {
        
#ifdef CLP_ANALYTICS
        if(offer){
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setObject:offer.offerId forKey:@"Offer_ID"];
            [data setObject:offer.consumerTitle forKey:@"Offer_Name"];
            [data setValue:@"Accept Offer" forKey:@"event_name"];
            [data setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
            [_app._clpSDK updateAppEvent:data];
        }
#endif
        
    }
    @catch (NSException *exception) {
        
    }
}

-(void)reloadOfferTable{
    [self showAvailableOffersView];
    //[_offerGrid reloadData];
    
}

-(void)refreshLocalArrays:(Offer *)current_offer{
    for(int i = 0; i < _app._personalizedOffersList.count; i++)
    {
        Offer *offer = [_app._personalizedOffersList objectAtIndex:i];
        
        if([offer.acceptGroup compare:current_offer.acceptGroup] == 0)
        {
            [_app._acceptedOffersList addObject:offer];
            [_app._personalizedOffersList removeObjectAtIndex:i];
            break;
        }
    }
    
    for(int i = 0; i < _app._extraFriendzyOffersList.count; i++)
    {
        Offer *offer = [_app._extraFriendzyOffersList objectAtIndex:i];
        
        if([offer.acceptGroup compare:current_offer.acceptGroup] == 0)
        {
            [_app._acceptedOffersList addObject:offer];
            [_app._extraFriendzyOffersList removeObjectAtIndex:i];
            break;
        }
    }
    
}



- (void)handleAcceptOfferServiceResponse:(id)responseObject
{
    @try {
        
        int status = [_service getHttpStatusCode];
        
        if(status == 200) // service success
        {
            _service = nil;
            OfferAcceptRequest *response = (OfferAcceptRequest *)responseObject;
            
            for(int i = 0; i < _app._personalizedOffersList.count; i++)
            {
                Offer *offer = [_app._personalizedOffersList objectAtIndex:i];
                
                if([offer.acceptGroup compare:response.acceptGroup] == 0)
                {
                    [_app._acceptedOffersList addObject:offer];
                    [_app._personalizedOffersList removeObjectAtIndex:i];
                    break;
                }
            }
            
            for(int i = 0; i < _app._extraFriendzyOffersList.count; i++)
            {
                Offer *offer = [_app._extraFriendzyOffersList objectAtIndex:i];
                
                if([offer.acceptGroup compare:response.acceptGroup] == 0)
                {
                    [_app._acceptedOffersList addObject:offer];
                    [_app._extraFriendzyOffersList removeObjectAtIndex:i];
                    break;
                }
            }
            
            //            // clear the current view before redrawing
            //            if(_offersView != nil)
            //            {
            //                [_offersView removeFromSuperview];
            //                _offersView = nil;
            //                _offerGrid = nil;
            //            }
            //
            //           [self showTick];
            //           [self showAvailableOffersView];
        }
        else // service failure
        {
            if(status == 422) // backend error
            {
                WebServiceError *error = [_service getError];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Accept Offer Failed" message:error.errorMessage];
                [dialog show];
            }
            else if(status == 408) // timeout error
            {
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
                [dialog show];
            }
            else // http error
            {
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Accept Offer Failed"]];
                [dialog show];
            }
            
            _service = nil;
        }
    }
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
        
    }
}


- (void)waitForOffersThread:(NSObject *)urlString
{
    @autoreleasepool
    {
        int count = 0;
        
        while([_app offerThreadsDone] == NO && count++ < 11)
        {
            LogInfo(@"Waiting for offer threads to finish %d", count);
            [NSThread sleepForTimeInterval:1.0];  // keep retrying, web service timeout = 15 - splash screen delay = 4 gives us 11 seconds to wait
        }
        
        [_progressDialog dismiss];
        [self removeViews];
        [self setActiveMenu:_offerMenu];
        
        if(_offersView == nil) // on startup if requests weren't received when this screen first appeared
        {
            LogInfo(@"waitForOffersThread: Showing offers on startup");
        }
        else // offer button pressed if no offers were received on startup
        {
            LogInfo(@"waitForOffersThread: Updating offers");
            [_offersView removeFromSuperview];
            _offersView = nil;
            _offerGrid = nil;
        }
        
        if(urlString != nil && [(NSString *)urlString compare:OFFERS_PERSONALIZED_URL] == 0)
            [self performSelectorOnMainThread:@selector(showAvailableOffersView) withObject:nil waitUntilDone:NO];
        else // should be OFFERS_ACCEPTED_URL
            [self performSelectorOnMainThread:@selector(showAcceptedOffersView) withObject:nil waitUntilDone:NO];
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// offers stuff end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// shopping list stuff begin
///////////////////pp./////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (void)setEcartButton
{
    @try {
        
        if(![Utility isEmpty:_app._allStoresList])
        {
            Store *store;
            
            for(NSString *key in [_app._allStoresList allKeys])
            {
                store = [_app._allStoresList objectForKey:key];
                
                if(_login.storeNumber == store.storeNumber && [store.ecart isEqualToString:@"Yes"])
                {
                    _ecartButton.hidden = NO;
                    break;
                }
            }
        }
    }
    @catch (NSException *exception) {
        LogInfo(@"setEcartButton: %@",exception.reason);
    }
    
    
    
    
}


- (void)eCartButtonPressed
{
    @try {
        
        [self removeMenus];
        
        BOOL isEcartAvail=FALSE;
        if(![Utility isEmpty:_app._allStoresList])
        {
            Store *store;
            for(NSString *key in [_app._allStoresList allKeys])
            {
                store = [_app._allStoresList objectForKey:key];
                if(_login.storeNumber == store.storeNumber && [store.ecart isEqualToString:@"Yes"])
                {
                    isEcartAvail=TRUE;
                    break;
                }
            }
        }
        if(isEcartAvail){
            ListRequest *request = [[ListRequest alloc] init];
            request.accountId = _login.accountId;
            request.listId = _app._selectedShoppingList.listId;
            request.returnCurrentList = [NSNumber numberWithBool:YES];
            request.appListUpdateTime = _app._selectedShoppingList.serverUpdateTime;
            
            LogInfo(@"SHOPPING_LIST: getShoppingList: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [_app._selectedShoppingList.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", _app._selectedShoppingList.totalPrice]);
            
            _validatingEcartList = YES;
            _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Syncing Current List..."];
            [_progressDialog show];
            
            _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
            [_service execute:LIST_GET_BY_ID_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
            
//#ifdef CLP_ANALYTICS
//            // Track
//            if(![Utility isEmpty:request.listId]){
//                NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
//                [data setValue:@"Checkout" forKey:@"EVENT_NAME"];
//                [data setValue:request.listId  forKey:@"SHOPPING_LIST_ID"];
//                
//                //new
//                [data setObject:@"Checkout" forKey:@"event"];
//                [data setValue:@"CLICK_EVENT" forKey:@"event_name"];
//                [data setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
//                [_app._clpSDK updateAppEvent:data];
//            }
//#endif
            
        }
        else{
            TextDialog *txtDialog=[[TextDialog alloc]initWithView:self.view title:@"eCart" message:@"eCart is not available for the current selected home store"];
            [txtDialog show];
        }
    }
    @catch (NSException *exception) {
        LogInfo(@"eCartButtonPressed: %@",exception.reason);
    }
    
    
    
    
}


- (void)ecartPreOrder
{
    @try
    {
        EcartPreOrderRequest *request = [[EcartPreOrderRequest alloc] init];
        request.accountId = _login.accountId;
        request.storeNumber = _login.storeNumber;
        request.listId = _app._selectedShoppingList.listId;
        
        _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving appointments..."];
        [_progressDialog show];
        
        _service = [[WebService alloc]initWithListener:self responseClassName:@"EcartPreOrderResponse"];
        [_service execute:ECART_PREORDER_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleEcartPreOrderServiceResponse method below
    }
    @catch (NSException *exception) {
        LogInfo(@"ecartPreOrder: %@",exception.reason);
    }
    
}

- (void)handleEcartPreOrderServiceResponse:(id)responseObject
{
    
    @try
    {
        int status = [_service getHttpStatusCode];
        
        
        if(status == 200) // service success
        {
            _service = nil;
            
            _app._currentEcartPreOrderResponse = (EcartPreOrderResponse *)responseObject;
            _app._currentAppointmentList = _app._currentEcartPreOrderResponse.appointmentList;
            LogInfo(@"Found %lu appointments", (unsigned long)_app._currentAppointmentList.count);
            
            
            [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
            EcartScreenVC *eCartScreenVC = [[EcartScreenVC alloc] init];
            [self presentViewController:eCartScreenVC animated:NO completion:nil];
        }
        else // service failure
        {
            if(status == 422) // backend error
            {
                WebServiceError *error = [_service getError];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Appointment Retrieve Failed" message:error.errorMessage];
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
    @catch (NSException *exception) {
        LogInfo(@"handleEcartPreOrderServiceResponse: %@",exception.reason);
    }
}


- (void)waitForShoppingListThread:(NSObject *)urlString
{
    @autoreleasepool
    {
        int count = 0;
        
        while(_app._retrievingShoppingList == YES && count++ < 15)
        {
            LogInfo(@"SATAN: waitForShoppingListThread: %d", count);
            [NSThread sleepForTimeInterval:1.0];
        }
        
        LogInfo(@"waitForShoppingListThread: DONE, Found %lu products for list %@", (unsigned long)_app._currentShoppingList.productList.count, _app._currentShoppingList.name);
        [self performSelectorOnMainThread:@selector(setFooterDetails) withObject:nil waitUntilDone:NO];
    }
}
//show list of products in selected shopping list name
- (void)showShoppingListView
{
    @try {
        [self moveActiveShopListToTop];
        
        [self removeViews];
        
        [self setActiveMenu:_listMenu];
        [shoppinglist_backbtn setHidden:NO];
        serverShoppingListFlag=FALSE;
        //    if(_app._selectedShoppingList.productList.count > 0)
        
        
        //    {
        [self buildProductByAisleArray];
        
        
        //listview container
        CGRect containerFrame = CGRectMake(0, 0, _contentViewWidth, _contentViewHeight);
        _shoppingProductListContainer =[[UIView alloc]initWithFrame:containerFrame];
        _shoppingProductListContainer.backgroundColor=[UIColor whiteColor];
        
        //container title
        lblTitle=[[UILabel alloc]initWithFrame:CGRectMake(0, 0, _contentViewWidth, 30)];
        lblTitle.backgroundColor=[UIColor colorWithRed:46.0/255.0 green:50.0/255.0 blue:50.0/255.0 alpha:1.0];
        lblTitle.textColor =[UIColor whiteColor];
        lblTitle.font = [UIFont fontWithName:_app._normalFont size:13];
        [lblTitle setTextAlignment:NSTextAlignmentCenter];
        lblTitle.text=_app._selectedShoppingList.name;
        
        int tableHeight=_contentViewHeight-((30*2)+40);
        [_shoppingProductList removeFromSuperview];
        _shoppingProductList=[[UITableView alloc] initWithFrame:CGRectMake(0, 30, _contentViewWidth,tableHeight) style:UITableViewStyleGrouped];
        _shoppingProductList.delegate=self;
        _shoppingProductList.dataSource=self;
        if([[[UIDevice currentDevice] systemVersion] floatValue] < 7){  // IOS 6 and below
            _shoppingProductList.backgroundView=nil;
        }
        
        [_shoppingProductList registerNib:[UINib nibWithNibName:@"ShoppingDetailsCell" bundle:nil] forCellReuseIdentifier:@"ShoppingDetailsCell"];
        
        float totalLabelY=30+(tableHeight);
        lblTotal=[[UILabel alloc]initWithFrame:CGRectMake(0, totalLabelY, _contentViewWidth, 30)];
        lblTotal.backgroundColor=[UIColor colorWithRed:187.0/255.0 green:0.0/255.0 blue:0.0/255.0 alpha:1.0];
        lblTotal.textColor =[UIColor whiteColor];
        lblTotal.font = [UIFont fontWithName:_app._boldFont size:font_size13];
        [lblTotal setTextAlignment:NSTextAlignmentCenter];
        lblTotal.text=[NSString stringWithFormat:@"$%.2f", [self getShoppingListTotal]];
        float bottomButtonY=totalLabelY+30;
        //float bottomButtonWidth=_contentViewWidth/2;
        //checkout and shop button
        btnCheckout=[[UIButton alloc]initWithFrame:CGRectMake(0, bottomButtonY, _contentViewWidth, 40)];
        [btnCheckout setTitle:@"Checkout" forState:UIControlStateNormal];
        [btnCheckout.titleLabel setTextAlignment:NSTextAlignmentCenter];
        btnCheckout.titleLabel.font=[UIFont fontWithName:_app._normalFont size:font_size17];
        btnCheckout.backgroundColor=[UIColor clearColor];
        [btnCheckout setTitleColor:[UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0] forState:UIControlStateNormal];
        [btnCheckout addTarget:self action:@selector(eCartButtonPressed) forControlEvents:UIControlEventTouchUpInside];
        [btnCheckout setUserInteractionEnabled:YES];
        //disable checkout button if shopping list do not have items
        if([_app._selectedShoppingList.productList count]<=0){
            [btnCheckout setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
            [btnCheckout setTitle:@"Checkout" forState:UIControlStateNormal];
            [btnCheckout.titleLabel setTextAlignment:NSTextAlignmentCenter];
            btnCheckout.titleLabel.font=[UIFont fontWithName:_app._normalFont size:font_size17];
            btnCheckout.backgroundColor=[UIColor lightGrayColor];
            [btnCheckout setUserInteractionEnabled:NO];
        }
        
        //        if(![Utility isEmpty:_app._allStoresList])
        //        {
        //            Store *store;
        //            for(NSString *key in [_app._allStoresList allKeys])
        //            {
        //                store = [_app._allStoresList objectForKey:key];
        //                if(_login.storeNumber == store.storeNumber && [store.ecart isEqualToString:@"Yes"])
        //                {
        //                    if(_app._selectedShoppingList.productList.count > 0){
        //                        btnCheckout.backgroundColor=[UIColor clearColor];
        //                        [btnCheckout setTitleColor:[UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0] forState:UIControlStateNormal];
        //                        [btnCheckout addTarget:self action:@selector(eCartButtonPressed) forControlEvents:UIControlEventTouchUpInside];
        //                        [btnCheckout setUserInteractionEnabled:YES];
        //                    }
        //                    break;
        //                }
        //            }
        //        }
        [_shoppingProductListContainer addSubview:lblTitle];
        [_shoppingProductListContainer addSubview:_shoppingProductList];
        [_shoppingProductListContainer addSubview:lblTotal];
        [_shoppingProductListContainer addSubview:btnCheckout];
        [_contentView addSubview:_shoppingProductListContainer];
        
        UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(removeMenus)];
        [singleTap setCancelsTouchesInView:NO];
        [_shoppingProductList addGestureRecognizer:singleTap];
        
        //    }
        if(_app._selectedShoppingList.productList.count > 0){
            if(_validatingEcartList == YES)
            {
                _validatingEcartList = NO;
                [self ecartPreOrder];
            }
        }
    }
    @catch (NSException *exception) {
        LogInfo("ShoppingScreenVC - showShoppingListView : %@",exception.reason);
        
    }
    
    if(activeShoppingListAnimation){
        [Utility Viewcontroller:_contentView.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
    }
}
-(float)getShoppingListTotal{
    //total payable price
    float total=0.0;
    for(int i=0;i<_productByAisleArray.count;i++){
        NSArray *array = [_productByAisleArray objectAtIndex:i];
        for(int i=0;i<array.count;i++){
            _app._currentProduct = [array objectAtIndex:i];
            int quantity;//no of items
            float weight;//no of items
            if(_app._currentProduct.weight > 0)
                weight = _app._currentProduct.weight;
            else
                quantity = _app._currentProduct.qty;
            float unitPrice;// item unit price
            if(_app._currentProduct.promoType > 0)
                unitPrice = _app._currentProduct.promoPrice / _app._currentProduct.promoForQty;
            else
                unitPrice = _app._currentProduct.regPrice;
            if(_app._currentProduct.approxAvgWgt > 0 && _app._currentProduct.qty > 0)
                unitPrice *= _app._currentProduct.approxAvgWgt;
            
            if(_app._currentProduct.weight > 0)
                total =total + (weight * unitPrice);
            else
                total =total + (quantity * unitPrice);
            
            //total =total + (quantity * unitPrice);
        }
    }
    return total;
}




//Retrieve all lists
- (void)setActiveList
{
    
    [self removeMenus];
    
    if(_app._currentShoppingList==nil){
        NSString *currentListId=@"";
        currentListId=[_app getCurrentActiveListIdForAccountId:_login.accountId];
        if(![currentListId isEqualToString:@""]){
            currentActiveListFlag=TRUE;
            [self getCurrentShoppingList:currentListId];
            return;
        }
    }
    if(shoppinglist_backbtn!=nil)
        [shoppinglist_backbtn setHidden:YES];
    if(backButton!=nil)
        [backButton setHidden:YES];
    enableEditMode= false;
    ListRequest *request = [[ListRequest alloc] init];
    request.accountId = _login.accountId;
    request.returnCurrentList = [NSNumber numberWithBool:NO];
    request.appListUpdateTime = [NSNumber numberWithLong:0];
    
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving your lists..."];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingListName"];
    [_service execute:LIST_GET_ALL_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListNameServiceResponse method below
}

- (void)handleListNameServiceResponse:(id)responseObject
{
    @try {
        
        int status = [_service getHttpStatusCode];
        
        if(status == 200) // service success
        {
            _service = nil;
            ShoppingListName *response = (ShoppingListName *)responseObject;
            
            if(response == nil  || response.nameList == nil)
            {
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:@"Unable to parse data returned from server."];
                [dialog show];
                return;
            }
            _shoppingListNameArray = response.nameList;
            [self moveActiveShopListToTop];
            LogInfo(@"Found %lu lists", (unsigned long)_shoppingListNameArray.count);
            //        if(_shoppingListNameArray.count == 1)
            //        {
            //            ShoppingListName *listName = [_shoppingListNameArray objectAtIndex:0];
            //            [self getShoppingList:listName];
            //        }
            //        else
            //        {
            [self showChangeListView];
            //        }
        }
        else // service failure
        {
            if(status == 422) // backend error
            {
                WebServiceError *error = [_service getError];
                if(![error.errorMessage isEqual:@"No lists found"]){
                    TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"List Names Retrieve Failed" message:error.errorMessage];
                    [dialog show];
                }
                _shoppingListNameArray =[[NSArray alloc]init];
                [self showChangeListView];
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
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
        
    }
}
//show shopping list names with active/delete options
- (void)showChangeListView
{
    @try {
        [shoppinglist_backbtn setHidden:YES];
        [_contentView setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1.0]];
        //clear all previous view which was added in contentview
        [self removeViews];
        
        if(![[NSUserDefaults standardUserDefaults] boolForKey:@"_isListsPage"]) // false default value
        {
            _isListsPage=YES;
            [self Overlay_for_first_time];
            
            [[NSUserDefaults standardUserDefaults] setBool:TRUE forKey:@"_isListsPage"]; // change to true for do not apper again
            [[NSUserDefaults standardUserDefaults] synchronize];
        }
        [_shoppingListNameTable removeFromSuperview];
        [self setActiveMenu:_listMenu];
        [_contentView removeGestureRecognizer:_tapRecognizer];
        
        //shopping list container view
        _shoppingListContainer=[[UIView alloc]initWithFrame:CGRectMake(0, 0, _contentViewWidth, _contentViewHeight )];
        //[_shoppingListContainer setBackgroundColor:[UIColor blueColor]];
        _shoppingListNameTable.allowsMultipleSelectionDuringEditing=NO;
        
        //table height
        _listNameTableCellHeight = _shoppingListContainer.frame.size.height * .14;
        //listview for listing all shopping list
        CGFloat create_list_height = 50.0f; // 'Create New List' height
        
        _shoppingListNameTable = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, _shoppingListContainer.frame.size.width, _shoppingListContainer.frame.size.height-create_list_height)];
        _shoppingListNameTable.dataSource = self;
        _shoppingListNameTable.delegate = self;
        [_shoppingListNameTable setShowsHorizontalScrollIndicator:NO];
        [_shoppingListNameTable setShowsVerticalScrollIndicator:NO];
        _shoppingListNameTable.backgroundColor = [UIColor whiteColor];
        _shoppingListNameTable.separatorStyle = UITableViewCellSeparatorStyleNone;
        _shoppingListNameTable.separatorColor =[UIColor whiteColor];
        [_shoppingListContainer addSubview:_shoppingListNameTable];
        
        [_shoppingListNameTable registerNib:[UINib nibWithNibName:@"ShoppingListCell" bundle:nil] forCellReuseIdentifier:@"ShoppingListCell"];
        [_shoppingListNameTable registerNib:[UINib nibWithNibName:@"ShoppingListEditCell" bundle:nil] forCellReuseIdentifier:@"ShoppingListEditCell"];
        
        UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(checkListEditMode)];
        [singleTap setCancelsTouchesInView:NO];
        [_shoppingListNameTable addGestureRecognizer:singleTap];
        
        // creating a fixed 'Create New List'
        UIView *create_new_list_container;//= [[[NSBundle mainBundle] loadNibNamed:@"ShoppingListAddNewCells" owner:self options:nil] objectAtIndex:0];
        create_new_list_container = [[UIView alloc]initWithFrame:CGRectMake(0, _shoppingListNameTable.frame.size.height, _shoppingListContainer.frame.size.width, create_list_height)];
        [create_new_list_container setBackgroundColor:[UIColor colorWithRed:(225.0/255.0) green:(225.0/255.0) blue:(225.0/255.0) alpha:1.0]];
        //createnew event handling
        UITapGestureRecognizer *create_list_tap=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(create_new_list_fixed)];
        [create_list_tap setNumberOfTapsRequired:1];
        [create_list_tap setNumberOfTouchesRequired:1];
        [create_new_list_container addGestureRecognizer:create_list_tap];
        
        //controll container
        UIView *controllContainer=[[UIView alloc]initWithFrame:CGRectMake((_shoppingListContainer.frame.size.width-169)/2, (create_list_height-25)/2, 169, 25)];
        [controllContainer setBackgroundColor:[UIColor clearColor]];
        
        //createnew icon
        UIButton *_create_new = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 25, 25)];
        [_create_new setBackgroundImage:[UIImage imageNamed:@"add_new.png"] forState:UIControlStateNormal];
        [_create_new setUserInteractionEnabled:NO];
        //createnew label
        UILabel *create_new_label = [[UILabel alloc]initWithFrame:CGRectMake(25, 0, 144, 25)];
        [create_new_label setText:@"Create New List"];
        [create_new_label setTextAlignment:NSTextAlignmentCenter];
        create_new_label.font=[UIFont fontWithName:_app._normalFont size:15];
        create_new_label.backgroundColor=[UIColor clearColor];//font_size13
        [controllContainer addSubview:_create_new];
        [controllContainer addSubview:create_new_label];
        
        [create_new_list_container addSubview:controllContainer];
        [_shoppingListContainer addSubview:create_new_list_container];
        [_contentView addSubview:_shoppingListContainer];
    }
    @catch (NSException *exception) {
        
    }
}
-(void)create_new_list_fixed{
    if(!enableEditMode){//check edit mode enabled
        enableEditMode=TRUE;//enable edit mode
        //[self moveActiveShopListToTop];
        [_shoppingListNameTable reloadData];
        [shopping_list_add_new becomeFirstResponder];
        //[self.view endEditing:NO];
        //[_shoppingListNameTable setContentOffset:CGPointMake(0, 300)];
        
        if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){  // IOS 7 and above
            CGFloat c_height = _shoppingListNameTable.contentSize.height;
            CGFloat f_height = _shoppingListNameTable.frame.size.height;
            if(c_height - f_height > 0){
                CGPoint offset = CGPointMake(0, _shoppingListNameTable.contentSize.height -     _shoppingListNameTable.frame.size.height);
                [_shoppingListNameTable setContentOffset:offset animated:YES];
            }
        }
        else{
            
            
            CGFloat c_height    = _shoppingListNameTable.contentSize.height;
            
            CGFloat f_height    = _shoppingListNameTable.frame.size.height;
            
            CGFloat cal_height  = f_height - c_height;
            
            if(cal_height < 200){
                CGPoint offset = CGPointMake(0, _shoppingListNameTable.contentSize.height -     _shoppingListNameTable.frame.size.height);
                
                
                [_shoppingListNameTable setContentOffset:offset animated:YES];
                
                CGRect newFrame = _shoppingListNameTable.frame;
                newFrame.origin.y = -230;
                [_shoppingListNameTable setFrame:newFrame];
                
            }
            
        }
        
        //reset all swipe to delete scrolls of the list
        for(int i=0;i<[_shoppingListNameTable numberOfRowsInSection:0];i++){
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow: i inSection:0];
            ShoppingListCell *cell = (ShoppingListCell*)[_shoppingListNameTable cellForRowAtIndexPath:indexPath];
            for(UIView *subviews in cell.contentView.subviews ){
                if([subviews isKindOfClass:[UIScrollView class]]){
                    UIScrollView *sc=(UIScrollView*)subviews;
                    [sc setContentOffset:CGPointMake(0, 0) animated:NO];
                }
            }
        }
    }
    else{
        [self checkListEditMode];
    }
    
//#ifdef CLP_ANALYTICS
//    //Analytics
//    NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
//    [data setValue:@"Create New List" forKey:@"Event"];
//    //new
//    [data setObject:@"Create New List" forKey:@"event"];
//    [data setValue:@"CLICK_EVENT" forKey:@"event_name"];
//    [data setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
//    [_app._clpSDK updateAppEvent:data];
//#endif
    
}

-(void)checkListEditMode{
    [self removeMenus];
    if(enableEditMode ==TRUE){
        [shopping_list_add_new resignFirstResponder];
        _textDialog = [[TextDialog alloc] initWithView:View title:@"Create New List" message:@"Do you want to save the list?" responder:self leftCallback:@selector(yesCreateNewList) rightCallback:@selector(noCreateNewList)];
        [_textDialog show];
    }
}


- (void)deleteList
{
    if([Utility isEmpty:_app._currentShoppingList])
    {
        _textDialog = [[TextDialog alloc] initWithView:View title:@"List Error" message:@"You do not have a current list to delete."];
        [_textDialog show];
        return;
    }
    
    _textDialog = [[TextDialog alloc] initWithView:View title:@"Delete List" message:[NSString stringWithFormat:@"Are you sure you want to delete list '%@'?", _app._currentShoppingList.name] responder:self leftCallback:@selector(deleteYes) rightCallback:@selector(deleteNo)];
    [_textDialog show];
}

- (void)deleteYes
{
    [_textDialog close];
    [self deleteExistingList];
}

- (void)deleteNo
{
    [_textDialog close];
}


- (void)deleteExistingList
{
    ListDeleteRequest *request = [[ListDeleteRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = _app._currentShoppingList.listId;
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ListDeleteRequest"];
    [_service execute:LIST_DELETE_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListDeleteServiceResponse method below
}


- (void)handleListDeleteServiceResponse:(id)responseObject
{
    @try {
        
        int status = [_service getHttpStatusCode];
        
        if(status == 200) // service success
        {
            _service = nil;
            dispatch_async(dispatch_get_main_queue(), ^{
                
                if([_selectedShoppingListId isEqualToString:_app._currentShoppingList.listId]){
                    _app._currentShoppingList = nil;
                    //                [_app setCurrentActiveListId:@"" ForAccountId:_login.accountId];
                    [_app setCurrentActiveListId:@"" setName:@"" setTotalProduct:@"" ForAccountId:_login.accountId];
                    
                }
                [self setActiveList];
                
                
            });
            //        _app._currentShoppingList = nil;
            //        [_app storeShoppingListId:nil];
            //        [self setFooterDetails];
            //        [self removeViews];
            //        [self showAvailableOffersView];
            //        [self setActiveMenu:_offerMenu];
            
            
        }
        else // service failure
        {
            if(status == 422) // backend error
            {
                WebServiceError *error = [_service getError];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"List Delete Failed" message:error.errorMessage];
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
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
        
    }
}

- (void)createList
{
    CGRect frame;
    
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        frame = self.view.frame;
    else
        frame = View.frame;
    
    int dialogTop;
    
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
        dialogTop = frame.size.height * .15;
    else
        dialogTop = 0;
    
    
    _listsModalView = [[ListsModalView alloc] initWithFrame:frame contentWidth:frame.size.width * .8 contentHeight:frame.size.height * .5 adjustedTop:dialogTop];
    [View addSubview:_listsModalView];
    
    CGRect contentFrame = _listsModalView._contentView.frame;
    int textFieldHeight = contentFrame.size.height * .12;
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(contentFrame.size.width * .02, contentFrame.size.height * .22, contentFrame.size.width * .96, textFieldHeight)];
    titleLabel.backgroundColor = [UIColor clearColor];
    titleLabel.textColor = [UIColor colorWithRed:0.8 green:0.0 blue:0.0 alpha:1.0];
    titleLabel.font = [Utility fontForFamily:_app._normalFont andHeight:textFieldHeight];
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.text = @"Create a New List";
    [_listsModalView._contentView addSubview:titleLabel];
    
    _listNameText = [[OffsetTextField alloc] initWithFrame:CGRectMake(contentFrame.origin.x + contentFrame.size.width * .1, contentFrame.origin.y + contentFrame.size.height * .57, contentFrame.size.width * .8, textFieldHeight) offset:contentFrame.size.width * .02];
    _listNameText.delegate = self;
    _listNameText.keyboardType = UIKeyboardTypeAlphabet;
    _listNameText.returnKeyType = UIReturnKeyDone;
    _listNameText.background = [UIImage imageNamed:@"product_comment_box"];
    _listNameText.textColor = [UIColor blackColor];
    _listNameText.font = [Utility fontForFamily:_app._normalFont andHeight:textFieldHeight * .8];
    _listNameText.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
    _listNameText.autocapitalizationType = UITextAutocapitalizationTypeWords;
    _listNameText.placeholder = @"List Name";
    [_listsModalView addSubview:_listNameText];
    
    UIButton *addButton = [[UIButton alloc] initWithFrame:CGRectMake(contentFrame.origin.x + contentFrame.size.width * .3, contentFrame.origin.y + contentFrame.size.height * .82, contentFrame.size.width * .4, textFieldHeight)];
    [addButton setBackgroundImage:[UIImage imageNamed:@"button_red_plain"] forState:UIControlStateNormal];
    addButton.titleLabel.font = [Utility fontForFamily:_app._normalFont andHeight:textFieldHeight * .8];
    [addButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [addButton setTitle:@"Create" forState:UIControlStateNormal];
    [addButton addTarget:self action:@selector(createNewList) forControlEvents:UIControlEventTouchDown];
    [_listsModalView addSubview:addButton];
}


- (void)createNewList
{
    if([Utility isEmpty:_listNameText.text] || _listNameText.text.length < 4)
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:[NSString stringWithFormat:@"List Name must contain at least 4 characters."]];
        [dialog show];
        return;
    }
    
    ListCreateRequest *request = [[ListCreateRequest alloc] init];
    request.email = _login.email;
    request.accountId = _login.accountId;
    request.listName = _listNameText.text;
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ListCreateRequest"];
    [_service execute:LIST_CREATE_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListCreateServiceResponse method below
}


- (void)handleListCreateServiceResponse:(id)responseObject
{
    @try {
        
        [_progressDialog dismiss];
        int status = [_service getHttpStatusCode];
        
        if(status == 200) // service success
        {
            _service = nil;
            ListCreateRequest *response = (ListCreateRequest *)responseObject;
            
            if(response == nil || response.listName == nil)
            {
                //            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:@"Unable to parse data returned from server."];
                //            [dialog show];
                return;
            }
            TickDialog *tickDiag=[[TickDialog alloc] initWithView:View message:@"List created"];
            [tickDiag show];
            
            
            [UIView animateWithDuration:1.0f delay:0.5f options:UIViewAnimationOptionCurveEaseIn animations:^(void){
                [tickDiag setAlpha:1.0f];
                
            }
                             completion:^(BOOL finished){
                                 [UIView animateWithDuration:1.0f delay:1.0f options:UIViewAnimationOptionCurveEaseOut animations:^(void){
                                     [tickDiag setAlpha:0.0f];
                                     
                                 }
                                                  completion:^(BOOL finished){
                                                      [tickDiag dismiss];
                                                      [_listsModalView removeFromSuperview];
                                                      dispatch_async(dispatch_get_main_queue(), ^{
                                                          enableEditMode=FALSE;
                                                          [self setActiveList];
                                                      });
                                                  }];
                             }];
            
            //        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Request Succeeded" message:[NSString stringWithFormat:@"List %@ created", response.listName]];
            //        [dialog show];
            
            
            
        }
        else // service failure
        {
            if(status == 422) // backend error
            {
                WebServiceError *error = [_service getError];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"List Add Failed" message:error.errorMessage];
                [dialog show];
                dispatch_async(dispatch_get_main_queue(), ^{
                    enableEditMode=FALSE;
                    [self setActiveList];
                });
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
    @catch (NSException *exception) {
        LogInfo(@"Response Error: %@",[exception reason]);
    }
}


- (void)getShoppingList:(ShoppingListName *)listName
{
    activeShoppingList = listName;
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving shopping list..."];
    [_progressDialog show];
    
    ListRequest *request = [[ListRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = listName.listId;
    request.returnCurrentList = [NSNumber numberWithBool:YES];
    
    //    if([_app._currentShoppingList.listId isEqualToString:listName.listId])
    //        request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;
    //    else
    request.appListUpdateTime = [NSNumber numberWithLong:0];
    
    LogInfo(@"SHOPPING_LIST: getShoppingList: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [_app._currentShoppingList.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", _app._currentShoppingList.totalPrice]);
    
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
    [_service execute:LIST_GET_BY_ID_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
}


- (void)handleListServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];
    [self handleList:responseObject httpStatusCode:status _isCurrentAcitveList:_isCurrentAcitveList];
}


- (void)handleList:(id)responseObject httpStatusCode:(int)status _isCurrentAcitveList:(BOOL)isCurrentActiveList
{
    @try {
        
        if(status == 200) // service success
        {
            _service = nil;
            ShoppingList *response = (ShoppingList *)responseObject;
            
            if(response == nil)
            {
                [_progressDialog dismiss];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:@"Unable to parse data returned from server."];
                [dialog show];
                return;
            }
            
            //            [_app storeShoppingListId:response.listId];
            
            if(response.storeNumber != _login.storeNumber)
            {
                LogInfo("Changing login storeNumber to %d", response.storeNumber);
                //                _login.storeNumber = response.storeNumber;
                [_app storeLogin:_login];
            }
            
            LogInfo("SHOPPING_LIST: handleListServiceResponse for %@", response.listId);
            
            if(response.productList == nil)
                LogInfo("SHOPPING_LIST: response.productList is nil");
            else
                LogInfo("SHOPPING_LIST: response.productList contains %lu products", (unsigned long)response.productList.count);
            
            
            
            //            if(response.productListReturned == YES)
            if(response.productList!=nil)
            {
                LogInfo("SHOPPING_LIST: response.productListReturned == true, serverUpdateTime = %llu, totalPrice = %@", [response.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", response.totalPrice]);
                
                if(response.productList == nil)
                    LogError(@"handleListServiceResponse productList is nil when productListReturned is YES");
                else
                {
                    if(current_active_menu == _listMenu){
                        if(_isCurrentAcitveList){
                            _app._selectedShoppingList = response;
                            _app._currentShoppingList= response;
                            LogInfo(@"response: both ");
                            
                        }else{
                            _app._selectedShoppingList = response;
                            LogInfo(@"response: selected ");
                        }
                        
                    }else{
                        _app._currentShoppingList= response;
                        
                    }
                    
                }
            }
            else // list wasn't returned so check if we need to manually update the list because a product was added, deleted, or modified, or that we were just syncing prior to submitting an ecart order
            {
                
                if(current_active_menu == _productMenu || (current_active_menu == _listMenu && _shoppingProductList!=nil)){
                    LogInfo("SHOPPING_LIST: responseList.productListReturned == false, serverUpdateTime = %llu, totalPrice = %@", [response.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", response.totalPrice]);
                    if(isCurrentActiveList)
                    {
                        _app._currentShoppingList.serverUpdateTime = response.serverUpdateTime;
                        _app._currentShoppingList.name = response.name; // for footer details
                        _app._currentShoppingList.totalPrice = response.totalPrice; // for footer details
                    }else{
                        _app._selectedShoppingList.serverUpdateTime = response.serverUpdateTime;
                        _app._selectedShoppingList.name = response.name; // for footer details
                        _app._selectedShoppingList.totalPrice = response.totalPrice; // for footer details
                    }
                    
                    
                    if(_app._productToAdd != nil)
                    {
                        LogInfo("SHOPPING_LIST: Adding product: %@, qty:%d, description: %@", _app._productToAdd.upc, _app._productToAdd.qty, _app._productToAdd.description);
                        
                        if(isCurrentActiveList){
                            [_app._currentShoppingList.productList addObject:_app._productToAdd];
                        }else{
                            [_app._selectedShoppingList.productList addObject:_app._productToAdd];
                        }
                        
                        [_progressDialog dismiss];
                        
                        if(_shoppingListGrid != nil)
                        {
                            [self buildProductByAisleArray];
                            [_shoppingListGrid  reloadData];
                        }
                        if(_shoppingProductList != nil)
                        {
                            [self buildProductByAisleArray];
                            [_shoppingProductList  reloadData];
                            lblTotal.text=[NSString stringWithFormat:@"$%.2f", [self getShoppingListTotal]];
                            if([_app._selectedShoppingList.productList count]<=0){
                                [btnCheckout setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
                                [btnCheckout setTitle:@"Checkout" forState:UIControlStateNormal];
                                [btnCheckout.titleLabel setTextAlignment:NSTextAlignmentCenter];
                                btnCheckout.titleLabel.font=[UIFont fontWithName:_app._normalFont size:font_size17];
                                btnCheckout.backgroundColor=[UIColor lightGrayColor];
                                [btnCheckout setUserInteractionEnabled:NO];
                            }
                        }
                        
                        
                        
                        return;
                    }
                    
                    if(_app._productToDelete != nil)
                    {
                        LogInfo("SHOPPING_LIST: Deleting product: %@, qty:%d, description: %@", _app._productToDelete.upc, _app._productToDelete.qty, _app._productToDelete.description);
                        
                        if(isCurrentActiveList){
                            for(int i = 0; i < _app._currentShoppingList.productList.count; i++)
                            {
                                Product *product = [_app._currentShoppingList.productList objectAtIndex:i];
                                
                                if([product.upc isEqualToString:_app._productToDelete.upc])
                                {
                                    [_app._currentShoppingList.productList removeObjectAtIndex:i];
                                    break;
                                }
                            }
                        }else{
                            for(int i = 0; i < _app._selectedShoppingList.productList.count; i++)
                            {
                                Product *product = [_app._selectedShoppingList.productList objectAtIndex:i];
                                
                                if([product.upc isEqualToString:_app._productToDelete.upc])
                                {
                                    [_app._selectedShoppingList.productList removeObjectAtIndex:i];
                                    break;
                                }
                            }
                        }
                        
                        
                        [_progressDialog dismiss];
                        
                        if(_shoppingListGrid != nil)
                        {
                            [self buildProductByAisleArray];
                            [_shoppingListGrid  reloadData];
                        }
                        if(_shoppingProductList != nil)
                        {
                            [self buildProductByAisleArray];
                            [_shoppingProductList  reloadData];
                            lblTotal.text=[NSString stringWithFormat:@"$%.2f", [self getShoppingListTotal]];
                            if([_app._selectedShoppingList.productList count]<=0){
                                [btnCheckout setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
                                [btnCheckout setTitle:@"Checkout" forState:UIControlStateNormal];
                                [btnCheckout.titleLabel setTextAlignment:NSTextAlignmentCenter];
                                btnCheckout.titleLabel.font=[UIFont fontWithName:_app._normalFont size:font_size17];
                                btnCheckout.backgroundColor=[UIColor lightGrayColor];
                                [btnCheckout setUserInteractionEnabled:NO];
                            }
                        }
                        
                        _app._productToDelete = nil;
                        
                        return;
                    }
                    
                    if(_app._productToModify != nil)
                    {
                        LogInfo("SHOPPING_LIST: Modifying product: %@, qty:%d, description: %@", _app._productToModify.upc, _app._productToModify.qty, _app._productToModify.description);
                        
                        if(isCurrentActiveList){
                            for(int i = 0; i < _app._currentShoppingList.productList.count; i++)
                            {
                                Product *product = [_app._currentShoppingList.productList objectAtIndex:i];
                                
                                if([product.upc isEqualToString:_app._productToModify.upc])
                                {
                                    [_app._currentShoppingList.productList removeObjectAtIndex:i];
                                    [_app._currentShoppingList.productList addObject:_app._productToModify];
                                    break;
                                }
                            }
                        }else{
                            for(int i = 0; i < _app._selectedShoppingList.productList.count; i++)
                            {
                                Product *product = [_app._selectedShoppingList.productList objectAtIndex:i];
                                
                                if([product.upc isEqualToString:_app._productToModify.upc])
                                {
                                    [_app._selectedShoppingList.productList removeObjectAtIndex:i];
                                    [_app._selectedShoppingList.productList addObject:_app._productToModify];
                                    break;
                                }
                            }
                        }
                        
                        
                        
                        [_progressDialog dismiss];
                        
                        if(_shoppingListGrid != nil)
                        {
                            [self buildProductByAisleArray];
                            [_shoppingListGrid  reloadData];
                        }
                        if(_shoppingProductList != nil)
                        {
                            [self buildProductByAisleArray];
                            [_shoppingProductList  reloadData];
                            lblTotal.text=[NSString stringWithFormat:@"$%.2f", [self getShoppingListTotal]];
                            if([_app._selectedShoppingList.productList count]<=0){
                                [btnCheckout setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
                                [btnCheckout setTitle:@"Checkout" forState:UIControlStateNormal];
                                [btnCheckout.titleLabel setTextAlignment:NSTextAlignmentCenter];
                                btnCheckout.titleLabel.font=[UIFont fontWithName:_app._normalFont size:font_size17];
                                btnCheckout.backgroundColor=[UIColor lightGrayColor];
                                [btnCheckout setUserInteractionEnabled:NO];
                            }
                        }
                        
                        return;
                    }
                    
                    if(_validatingEcartList == YES)
                    {
                        [_progressDialog dismiss];
                        _validatingEcartList = NO;
                        [self ecartPreOrder];
                        return;
                    }
                }
                ///
            }
            
            if(current_active_menu == _listMenu){
                if(_isCurrentAcitveList){
                    
                    if(_app._currentShoppingList.productList != nil)
                    {
                        LogInfo(@"Found %lu products for list %@", (unsigned long)_app._currentShoppingList.productList.count, _app._currentShoppingList.name);
                        [_progressDialog changeMessage:@"Retrieving product data..."];
                        [_app getProductImages:_app._selectedShoppingList.productList]; // this is a thread function so we need our own thread function to determine when it's done
                        [NSThread detachNewThreadSelector:@selector(waitForProductImagesThread:) toTarget:self withObject:@"ShoppingList"];
                    }
                    else
                    {
                        [_progressDialog dismiss];
                    }
                }
                else{
                    if(_app._selectedShoppingList.productList != nil)
                    {
                        LogInfo(@"Found %lu products for list %@", (unsigned long)_app._selectedShoppingList.productList.count, _app._selectedShoppingList.name);
                        [_progressDialog changeMessage:@"Retrieving product data..."];
                        [_app getProductImages:_app._selectedShoppingList.productList]; // this is a thread function so we need our own thread function to determine when it's done
                        [NSThread detachNewThreadSelector:@selector(waitForProductImagesThread:) toTarget:self withObject:@"ShoppingList"];
                    }
                    else
                    {
                        [_progressDialog dismiss];
                    }
                }
            }
            else{//current screen is shopping screen
                if(_app._currentShoppingList.productList != nil)
                {
                    LogInfo(@"Found %lu products for list %@", (unsigned long)_app._currentShoppingList.productList.count, _app._currentShoppingList.name);
                    [_progressDialog changeMessage:@"Retrieving product data..."];
                    [_app getProductImages:_app._selectedShoppingList.productList]; // this is a thread function so we need our own thread function to determine when it's done
                    [NSThread detachNewThreadSelector:@selector(waitForProductImagesThread:) toTarget:self withObject:@"ShoppingList"];
                }
                else
                {
                    [_progressDialog dismiss];
                }
                
            }
        }
        else // service failure
        {
            [_progressDialog dismiss];
            
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
    @catch (NSException *exception) {
        LogInfo(@"Response Error: %@",[exception reason]);
    }
}
-(BOOL)isWebServiceRunning{
    if(_service==nil){
        return false;
    }
    else{
        return true;
    }
}
- (void)getCurrentShoppingList:(NSString *)listId
{
    currentActiveListFlag=TRUE;
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving shopping list..."];
    [_progressDialog show];
    
    ListRequest *request = [[ListRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = listId;
    request.returnCurrentList = [NSNumber numberWithBool:YES];
    
    //    if([_app._currentShoppingList.listId isEqualToString:listName.listId])
    //        request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;
    //    else
    request.appListUpdateTime = [NSNumber numberWithLong:0];
    
    LogInfo(@"SHOPPING_LIST: getShoppingList: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [_app._currentShoppingList.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", _app._currentShoppingList.totalPrice]);
    
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
    [_service execute:LIST_GET_BY_ID_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
}


- (void)handleCurrentListServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];
    [self handleCurrentList:responseObject httpStatusCode:status];
}


- (void)handleCurrentList:(id)responseObject httpStatusCode:(int)status
{
    
    @try {
        if(status == 200) // service success
        {
            _service = nil;
            ShoppingList *response = (ShoppingList *)responseObject;
            [_progressDialog dismiss];
            if(response == nil)
            {
                [_progressDialog dismiss];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:@"Unable to parse data returned from server."];
                [dialog show];
                return;
            }
            
            [_app storeShoppingListId:response.listId];
            
            if(response.storeNumber != _login.storeNumber)
            {
                LogInfo("Changing login storeNumber to %d", response.storeNumber);
                //                _login.storeNumber = response.storeNumber;
                [_app storeLogin:_login];
            }
            
            LogInfo("SHOPPING_LIST: handleListServiceResponse for %@", response.listId);
            
            if(response.productList == nil)
                LogInfo("SHOPPING_LIST: response.productList is nil");
            else
                LogInfo("SHOPPING_LIST: response.productList contains %lu products", (unsigned long)response.productList.count);
            
            //            if(response.productListReturned == YES)
            if(response.productList!=nil)
            {
                LogInfo("SHOPPING_LIST: response.productListReturned == true, serverUpdateTime = %llu, totalPrice = %@", [response.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", response.totalPrice]);
                
                if(response.productList == nil)
                    LogError(@"handleListServiceResponse productList is nil when productListReturned is YES");
                else
                {
                    _app._currentShoppingList= response;
                    //                    [_app setCurrentActiveListId:response.listId ForAccountId:_login.accountId];
                    [_app setCurrentActiveListId:response.listId setName:response.name setTotalProduct:[NSString stringWithFormat:@"%d",(int)response.productList.count] ForAccountId:_login.accountId];
                    
                    //[self moveActiveShopListToTop];
                    [_progressDialog dismiss];
                    if(current_active_menu == _listMenu){
                        [self setActiveList];
                    }
                }
            }
            [_progressDialog dismiss];
            
        }
        else // service failure
        {
            [_progressDialog dismiss];
            
            if(status == 422) // backend error
            {
                WebServiceError *error = [_service getError];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Current Active List Retrieve Failed" message:error.errorMessage];
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
    @catch (NSException *exception) {
        LogInfo(@"Response Error: %@",[exception reason]);
    }
}


// thread function that waits for image downloading to complete
- (void)waitForProductImagesThread:(NSString *)listType
{
    @autoreleasepool
    {
        int count = 0;
        
        while([_app imageThreadsDone] == NO && count++ < 50)
        {
            if(!(count % 10))
                LogInfo(@"Waiting for image threads to finish %d", count / 10);
            
            [NSThread sleepForTimeInterval:.1];
        }
        
        [_progressDialog dismiss];
        
        if([listType compare:@"ShoppingList"] == 0)
        {
            if(!serverShoppingListFlag){
                if(current_active_menu == _listMenu){
                [self performSelectorOnMainThread:@selector(showShoppingListView) withObject:nil waitUntilDone:YES];
                }
            }
        }
        else // @"ProductList"
            [self performSelectorOnMainThread:@selector(showProductView) withObject:nil waitUntilDone:NO];
        
    }
}


- (void)buildProductByAisleArray
{
    @try {
        if(current_active_menu == _productMenu){
            _app._selectedShoppingList=_app._currentShoppingList;
        }
        
        NSArray *sortedArray = [_app._selectedShoppingList.productList sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2)
                                {
                                    Product *firstProduct = (Product *)obj1;
                                    Product *secondProduct = (Product *)obj2;
                                    NSNumber *first = [NSNumber numberWithInt:firstProduct.aisleNumber];
                                    NSNumber *second = [NSNumber numberWithInt:secondProduct.aisleNumber];
                                    return [first compare:second]; // ascending
                                }];
        
        if(_productByAisleArray != nil)
        {
            [_productByAisleArray removeAllObjects];
            _productByAisleArray = nil;
        }
        
        _productByAisleArray = [[NSMutableArray alloc] init];
        
        // this adds a transparent header cell that goes under the menu tabs
        //    NSObject *blank = [[NSObject alloc] init];
        //    [_productByAisleArray addObject:blank];
        
        NSMutableArray *currentAisleArray = [[NSMutableArray alloc] init];
        int currentAisle = 0;
        
        for(Product *product in sortedArray)
        {
            if(currentAisle == 0)
                LogInfo(@"Aisle 0: %@", product.aisleSide);
            
            if(product.aisleNumber == currentAisle)
            {
                [currentAisleArray addObject:product];
            }
            else // next aisle
            {
                currentAisle = product.aisleNumber;
                
                if(currentAisleArray.count > 0)
                    [_productByAisleArray addObject:[NSArray arrayWithArray:currentAisleArray]];
                
                [currentAisleArray removeAllObjects];
                [currentAisleArray addObject:product];
            }
        }
        
        if(currentAisleArray.count > 0)
            [_productByAisleArray addObject:currentAisleArray];
        
    }
    @catch (NSException *exception) {
        
        LogInfo(@"buildProductByAisleArray:%@", exception.reason);
    }
}

#pragma mark ShoppingScreen Delegate(for list)
- (void)handleListServiceResponse:(id)responseObject httpStatusCode:(int)status _isCurrentList:(BOOL)currentList
{
    [self handleList:responseObject httpStatusCode:status _isCurrentAcitveList:currentList];
}


- (BOOL)shoppingListVisible
{
    if(_shoppingListView != nil)
        return YES;
    else
        return NO;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// shopping list stuff end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// scanner view begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
- (void)showScannerView
{
    [self removeMenus];
    [self removeViews];
    
    _scannerView = [[UIView alloc] initWithFrame:_childViewHiddenFrame];
    //    _scannerView.backgroundColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:0.75];
    [_contentView setBackgroundColor:[UIColor whiteColor]];
    [_scannerView setBackgroundColor:[UIColor whiteColor]];
    
    
    if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
    {
        _scannerBackgroundWidth = _contentViewWidth * .9;
        _scannerBackgroundHeight = _contentViewHeight * .8;
    }
    else
    {
        _scannerBackgroundWidth = _contentViewWidth * .6;
        _scannerBackgroundHeight = _contentViewHeight * .4;
    }
    
    int scannerBackgroundXOrigin = (_contentViewWidth - _scannerBackgroundWidth) / 2;
    int scannerBackgroundYOrigin = (_contentViewHeight - _scannerBackgroundHeight) / 2 - _navigationBarHeight;
    
    CGRect scannerBackgroundRect = CGRectMake(scannerBackgroundXOrigin, scannerBackgroundYOrigin, _scannerBackgroundWidth, _scannerBackgroundHeight);
    UIView *backgroundView = [[UIView alloc] initWithFrame:scannerBackgroundRect];
    UIImageView *backgroundImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, _scannerBackgroundWidth, _scannerBackgroundHeight)];
    [backgroundImageView setImage:[UIImage imageNamed:@"scan_dialog"]];
    //
    //    ZBarImageScanner *scanner = [ZBarImageScanner new];
    //    [scanner setSymbology:0 config:ZBAR_CFG_ENABLE to:0];
    //    [scanner setSymbology:ZBAR_EAN8 config:ZBAR_CFG_ENABLE to:0];
    //    [scanner setSymbology:ZBAR_EAN13 config:ZBAR_CFG_ENABLE to:1];
    //    [scanner setSymbology:ZBAR_UPCA config:ZBAR_CFG_ENABLE to:1];
    //    [scanner setSymbology:ZBAR_UPCE config:ZBAR_CFG_ENABLE to:0];
    //    [scanner setSymbology:ZBAR_CODE39 config:ZBAR_CFG_ENABLE to:0];
    //    [scanner setSymbology:ZBAR_ISBN13 config:ZBAR_CFG_ENABLE to:0];
    //    [scanner setSymbology:ZBAR_ISBN10 config:ZBAR_CFG_ENABLE to:0];
    //
    //    _reader = [ZBarReaderView new];
    //    [_reader initWithImageScanner:scanner];
    //    _reader.readerDelegate = self;
    //_reader.frame = CGRectMake(readerXOrigin, readerYOrigin, readerWidth, readerHeight);
    //[_reader start];
    
    int labelHeight = scannerBackgroundRect.size.height * .10;
    int readerBorder = _scannerBackgroundWidth * .02;
    int readerWidth = _scannerBackgroundWidth - (readerBorder * 2);
    int readerXOrigin = (_scannerBackgroundWidth - readerWidth) / 2;
    int readerHeight = _scannerBackgroundHeight - labelHeight - readerBorder;
    int readerYOrigin = labelHeight;
    
    scanAreaRect=CGRectMake(readerXOrigin+25, readerYOrigin+35, readerWidth-50, readerHeight-60);
    scanArea=[[UIView alloc]initWithFrame:scanAreaRect];//scanning area view
    [scanArea setBackgroundColor:[UIColor colorWithRed:135.0/255.0 green:169.0/255.0 blue:107.0/255.0 alpha:0.2]];
    
    //scanner title
    _scannerStatusLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, scannerBackgroundRect.size.height * .025, _scannerBackgroundWidth, labelHeight)];
    _scannerStatusLabel.backgroundColor = [UIColor clearColor];
    _scannerStatusLabel.textAlignment = NSTextAlignmentCenter;
    //    _scannerStatusLabel.font = [Utility fontForFamily:_app._normalFont andHeight:20];
    _scannerStatusLabel.font=[UIFont fontWithName:_app._normalFont size:font_size20];
    _scannerStatusLabel.textColor = [UIColor darkGrayColor];
    _scannerStatusLabel.text = @"Please Scan Your Item";
    
    //zxcapture
    zxingCapture = [[ZXCapture alloc] init];
    zxingCapture.camera = zxingCapture.back;
    zxingCapture.focusMode = AVCaptureFocusModeContinuousAutoFocus;
    zxingCapture.rotation = 90.0f;
    zxingCapture.layer.frame = CGRectMake(readerXOrigin, readerYOrigin, readerWidth, readerHeight);
    
    zxingCapture.delegate = self;
    [zxingCapture start];//star the scanner
    scanArea.layer.borderWidth=2;
    [self scannerAreaReset];//set scan area border as clear color
    scannerStart=TRUE;
    
    scanner_wait = [NSDate date];
    
    CGAffineTransform captureSizeTransform = CGAffineTransformMakeScale(0, 0);
    zxingCapture.scanRect = CGRectApplyAffineTransform(scanAreaRect,captureSizeTransform);//scan area
    
    
    //    [backgroundView addSubview:_reader];
    [backgroundView.layer addSublayer:zxingCapture.layer];
    
    [backgroundView addSubview:backgroundImageView];
    [backgroundView addSubview:_scannerStatusLabel];//scanner title
    [backgroundView addSubview:scanArea];//scan area
    
    [_scannerView addSubview:backgroundView];
    [_contentView addSubview:_scannerView];
    
    
    [UIView animateWithDuration:0.33f delay:0.0f options:UIViewAnimationOptionCurveEaseInOut | UIViewAnimationOptionAllowUserInteraction
                     animations:^{ [_scannerView setFrame:_childViewVisibleFrame]; }
                     completion:^(BOOL finished){ }];
    
    
#ifdef CLP_ANALYTICS
    @try {                                                                //Scan Products menu click
        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
        [data setValue:@"Scan" forKey:@"link_clicked"];
        [data setValue:@"MenuOpened" forKey:@"event_name"];
        [_app._clpSDK updateAppEvent:data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",[exception reason]);
    }
#endif
}


-(void)showScannerPosition{
    if(_scannerView !=nil){
        if(zxingCapture!=nil){
            
            if(UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
            {
                _scannerBackgroundWidth = _contentViewWidth * .9;
                _scannerBackgroundHeight = _contentViewHeight * .8;
            }
            else
            {
                _scannerBackgroundWidth = _contentViewWidth * .6;
                _scannerBackgroundHeight = _contentViewHeight * .4;
            }
            
            int scannerBackgroundXOrigin = (_contentViewWidth - _scannerBackgroundWidth) / 2;
            int scannerBackgroundYOrigin = (_contentViewHeight - _scannerBackgroundHeight) / 2 - _navigationBarHeight;
            
            CGRect scannerBackgroundRect = CGRectMake(scannerBackgroundXOrigin, scannerBackgroundYOrigin, _scannerBackgroundWidth, _scannerBackgroundHeight);
            
            int labelHeight = scannerBackgroundRect.size.height * .10;
            int readerBorder = _scannerBackgroundWidth * .02;
            int readerWidth = _scannerBackgroundWidth - (readerBorder * 2);
            int readerXOrigin = (_scannerBackgroundWidth - readerWidth) / 2;
            int readerHeight = _scannerBackgroundHeight - labelHeight - readerBorder;
            int readerYOrigin = labelHeight;
            CGAffineTransform captureSizeTransform = CGAffineTransformMakeScale(0, 0);
            zxingCapture.layer.frame = CGRectMake(readerXOrigin, readerYOrigin, readerWidth, readerHeight);
            //            zxingCapture.scanRect = CGRectApplyAffineTransform(CGRectMake(readerXOrigin, readerYOrigin, readerWidth, readerHeight),captureSizeTransform);
            CGRect scanRect=CGRectMake(readerXOrigin+30, readerYOrigin+30, readerWidth-60, readerHeight-60);
            
            zxingCapture.scanRect = CGRectApplyAffineTransform(scanRect,captureSizeTransform);
            zxingCapture.delegate = self;
            scannerStart=TRUE;
            
        }
    }
}

#pragma mark -  ZXing barcode

- (NSString *)barcodeFormatToString:(ZXBarcodeFormat)format {
    switch (format) {
        case kBarcodeFormatAztec:
            return @"Aztec";
            
        case kBarcodeFormatCodabar:
            return @"CODABAR";
            
        case kBarcodeFormatCode39:
            return @"Code 39";
            
        case kBarcodeFormatCode93:
            return @"Code 93";
            
        case kBarcodeFormatCode128:
            return @"Code 128";
            
        case kBarcodeFormatDataMatrix:
            return @"Data Matrix";
            
        case kBarcodeFormatEan8:
            return @"EAN-8";
            
        case kBarcodeFormatEan13:
            return @"EAN-13";
            
        case kBarcodeFormatITF:
            return @"ITF";
            
        case kBarcodeFormatPDF417:
            return @"PDF417";
            
        case kBarcodeFormatQRCode:
            return @"QR Code";
            
        case kBarcodeFormatRSS14:
            return @"RSS 14";
            
        case kBarcodeFormatRSSExpanded:
            return @"RSS Expanded";
            
        case kBarcodeFormatUPCA:
            return @"UPCA";
            
        case kBarcodeFormatUPCE:
            return @"UPCE";
            
        case kBarcodeFormatUPCEANExtension:
            return @"UPC/EAN extension";
            
        default:
            return @"Unknown";
    }
}

#pragma mark - ZXCaptureDelegate Methods

- (void)captureResult:(ZXCapture *)capture result:(ZXResult *)result {
    if (!result) return;//if no result rescan
    
    if (_service!=nil) return; // if web service is active
    
    if(scannerStart){
        
        
        NSString *scannedFormat=[self barcodeFormatToString:result.barcodeFormat];
        if(![scannedFormat isEqualToString:@"Unknown"])
            //if([scannedFormat isEqualToString:@"Aztec"] ||[scannedFormat isEqualToString:@"PDF417"] || [scannedFormat isEqualToString:@"QR Code"])
        {
            //dont connect server if previous response messagebox is not closed
            if(scannerDialog!=nil){
                if([scannerDialog superview]!=nil){
                    return;
                }
            }
            
            if([View.subviews containsObject:scannerDialog] || [View.subviews containsObject:_progressDialog]){
                return;
            }
            
            [self showScannerSuccess];//outline scanner area with green border for scan success
            [self performSelector:@selector(scannerAreaReset) withObject:nil afterDelay:1.0];//reset scanner area border
            
            scannerStart=FALSE;//flag to break scanner delegate loop
            _barCode = result.text;//barcode text read through camera
            
            ProductRequest *request = [[ProductRequest alloc] init];
            request.upc = _barCode;
            request.storeNumber = _login.storeNumber;
            
            _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving product data..."];
            [_progressDialog show];
            
            _service = [[WebService alloc]initWithListener:self responseClassName:@"Product"];
            [_service execute:PRODUCT_FOR_UPC_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleProductServiceResponse method below
            // Vibrate
            //AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
            
            //CLYP Event captured is in handleProductServiceResponse method.
        }
        else{
            scannerDialog = [[TextDialog alloc] initWithView:View title:@"Product Retrieve Failed" message:[NSString stringWithFormat:@"Invalid Bardcode format. %@",scannedFormat]];
            [scannerDialog show];
            [self showScannerFailure];//outline scanner area with red border for scan failur
            [self performSelector:@selector(scannerAreaReset) withObject:nil afterDelay:1.0];//reset scanner area border
        }
    }
}

-(void)captureFailResult:(ZXCapture *)capture result:(ZXResult *)result{
    if(!scannerStart || _service!=nil){
        scanner_wait = [NSDate date];
        return;
    }
    if(scannerDialog!=nil){
        if([scannerDialog superview]!=nil){
            scanner_wait = [NSDate date];
            return;
        }
    }
    
    if([View.subviews containsObject:scannerDialog] || [View.subviews containsObject:_progressDialog]){
        scanner_wait = [NSDate date];
        return;
    }
    
    int scanner_interval = -10;
    if([scanner_wait timeIntervalSinceNow]<scanner_interval){//scan timeout
        scannerDialog = [[TextDialog alloc] initWithView:View title:@"Product Retrieve Failed" message:[NSString stringWithFormat:@"Barcode not readable, please try again"]];
        [scannerDialog show];
        
        [self showScannerFailure];
        [self performSelector:@selector(scannerAreaReset) withObject:nil afterDelay:1.0];
        scanner_wait = [NSDate date];
    }
}
//show green boder when scanner is success
-(void)showScannerSuccess{
    scanArea.layer.borderColor=[UIColor greenColor].CGColor;
}
//show red boder when scanner is failed
-(void)showScannerFailure{
    scanArea.layer.borderColor=[UIColor redColor].CGColor;
}
//reset scanner border
-(void)scannerAreaReset{
    scanArea.layer.borderColor=[UIColor clearColor].CGColor;
}
-(void)showScannerAnimation{
    
}
-(void)stopScannerAnimation{
    
}
- (void)reScan
{
    //[_reader start];
    [zxingCapture start];
}



#pragma mark - ZBarReaderView
- (void) readerView:(ZBarReaderView *)aReaderView didReadSymbols: (ZBarSymbolSet *)symbols fromImage:(UIImage *)image
{
    ZBarSymbol *s = nil;
    NSString* capturedBarcode = @"";
    
    for(s in symbols)
    {
        capturedBarcode = s.data; //Get the first barcode
        break;
    }
    
    LogInfo(@"Barcode length = %lu", (unsigned long)capturedBarcode.length);
    
    if(capturedBarcode.length > 12) // barcode UPC is always 12 characters
    {
        [self reScan];
        return;
    }
    
    _barCode = capturedBarcode;
    [aReaderView stop];
    [aReaderView flushCache];
    LogInfo(@"barcode:%@", _barCode);
    
    ProductRequest *request = [[ProductRequest alloc] init];
    request.upc = _barCode;
    request.storeNumber = _login.storeNumber;
    
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Retrieving product data..."];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"Product"];
    [_service execute:PRODUCT_FOR_UPC_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleProductServiceResponse method below
}


- (void)handleProductServiceResponse:(id)responseObject
{
    @try {
        
        int status = [_service getHttpStatusCode];
        
        if(status == 200) // service success
        {
            
            _service = nil;
            
            Product *product = (Product *)responseObject;
            
            if(![Utility isEmpty:_barCode]){
                // Scan
#ifdef CLP_ANALYTICS
                if(_barCode){
                     @try {                                                                //Scan Products menu click
                        NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
                        [data setValue:@"ProductScanned" forKey:@"event_name"];
                        [data setValue:[NSString stringWithFormat:@"%d",product.storeNumber] forKey:@"store"];
                        [data setValue:_barCode forKey:@"barcode"];
                        [data setValue:product.sku forKey:@"SKU"];
                        [data setValue:product.upc forKey:@"item_id"];
                        [data setValue:product.description forKey:@"item_name"];
                        [data setValue:product.brand forKey:@"brand"];
                        [data setValue:[NSString stringWithFormat:@"%f",product.regPrice] forKey:@"price"];
                        [data setValue:[NSString stringWithFormat:@"%f",product.promoPrice]forKey:@"promo_price"];
                         if(product.weight > 0)
                             [data setValue:[NSString stringWithFormat:@"%f",product.weight] forKey:@"quantity"];
                         else
                             [data setValue:[NSString stringWithFormat:@"%d",product.qty] forKey:@"quantity"];
                        [data setValue:product.mainCategory forKey:@"category"];
                        [_app._clpSDK updateAppEvent:data];
                    }
                    @catch (NSException *exception) {
                        NSLog(@"%@",[exception reason]);
                    }
                }
#endif
            }
            
            if([Utility isEmpty:product.sku])
            {
                [_progressDialog dismiss];
                scannerDialog = [[TextDialog alloc] initWithView:View title:@"Product Retrieve Failed" message:[NSString stringWithFormat:@"Product not found."]];
                [scannerDialog show];
                return;
            }
            
            if(![Utility isEmpty:product.imagePath])
                [_app getImageSync:product.imagePath]; // stores the image data to the disk cache
            
            [_progressDialog dismiss];
            
            int productDetaiType;
            BOOL found = NO;
            Product *detailProduct;
            
            for(Product *listProduct in _app._currentShoppingList.productList)
            {
                if([listProduct.sku isEqualToString:product.sku])
                {
                    found = YES;
                    detailProduct = listProduct;
                    productDetaiType = PRODUCT_MODIFY;
                    break;
                }
            }
            
            if(found == NO)
            {
                detailProduct = product;
                productDetaiType = PRODUCT_ADD;
            }
            
            //        ProductDetailView *detailView = [[ProductDetailView alloc] initWithFrame:_app._currentView.frame product:detailProduct detailType:productDetaiType];
            //        detailView._shoppingScreenDelegate = self;
            //
            //        ProductDetails *products = [[ProductDetails alloc]initWithNibName:@"ProductDetails" bundle:nil];
            //        [self presentViewController:products animated:YES completion:nil];
            if(detailProduct!=nil){
                scannerStart=FALSE;//break scanner loop before opening product details screen
                ProductDetails *products = [[ProductDetails alloc]initWithNibName:@"ProductDetails" bundle:nil];
                [Utility Viewcontroller:self.view.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
                [self presentViewController:products animated:NO completion:nil];
                products._shoppingScreenDelegate = (id<ShoppingScreenDelegate>)_app._shoppingScreenVC;
                [products creating_controls:detailProduct _category:nil detailType:productDetaiType _frame:_app._currentView.frame _isCurrentAcitveList:YES];
            }
            //  [detailView show];
        }
        else // service failure
        {
            [_progressDialog dismiss];
            
            NSString *message=nil;
            if(status == 422) // backend error
            {
                WebServiceError *error = [_service getError];
                
                if(error.errorCode == 200)
                    scannerDialog = [[TextDialog alloc] initWithView:View title:@"Product Search Failed" message:@"We're sorry, this product was not found in our inventory."];
                else
                    scannerDialog = [[TextDialog alloc] initWithView:View title:@"Product Search Failed" message:error.errorMessage];
                
                [scannerDialog show];
            }
            else if(status == 408) // timeout error
            {
                scannerDialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
                [scannerDialog show];
            }
            else // http error
            {
                scannerDialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:[NSString stringWithFormat:COMMON_ERROR_MSG]];
                [scannerDialog show];
            }
            
            message = [scannerDialog getMessage];
            
            scannerStart=TRUE;//flag to continue scanner delegate loop
            _service = nil;
            
            if(![Utility isEmpty:_barCode]){
                // Scan
//#ifdef CLP_ANALYTICS
//                if(_barCode){
//                    NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
//                    [data setValue:_barCode forKey:@"Barcode_Value"];
//                    if(message){
//                        [data setValue:message forKey:@"Error_Message"];
//                    }
//                    
//                    //new
//                    [data setValue:@"Product Scanned" forKey:@"event_name"];
//                    [data setValue:[_app stringFromDate:[NSDate date]] forKey:@"event_time"];
//                    [_app._clpSDK updateAppEvent:data];
//                }
//#endif
            }
        }
        
        //    [self reScan];
        _barCode=@"";
        
    }
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
        
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// scanner view end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// table view delegate begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - UITableView
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if ([tableView respondsToSelector:@selector(separatorInset)]) {
        [tableView setSeparatorInset:UIEdgeInsetsZero];
    }
    if(tableView == _shoppingListNameTable)
        return 1;
    else if(tableView == _shoppingProductList){
        if(_productByAisleArray.count==0){//if product item list is emtpy show only one section header
            return 1;
        }
        else{
            return _productByAisleArray.count;
        }
    }
    else
        return 0;
    
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(tableView == _shoppingListNameTable)
    {
        if(enableEditMode){
            return _shoppingListNameArray.count+1;//2
        }
        else{
            return _shoppingListNameArray.count+0;//1
        }
    }
    else if(tableView == _shoppingProductList){
        if(_productByAisleArray.count==0){//if product item list is emtpy show only one section header. no rows.
            return 0;
        }
        else{
            NSArray *array = [_productByAisleArray objectAtIndex:section];
            return array.count;
        }
    }
    else
    {
        return 0;
    }
    
}
-(UIView*)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UILabel *lbl = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, tableView.bounds.size.width,30)];
    if(tableView == _shoppingProductList){
        lbl.font = [UIFont fontWithName:_app._normalFont size:font_size13];
        [lbl setTextAlignment:NSTextAlignmentCenter];
        lbl.textColor = [UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0];
        lbl.backgroundColor=[UIColor colorWithRed:(225.0/255.0) green:(225.0/255.0) blue:(225.0/255.0) alpha:1.0];
        [lbl setUserInteractionEnabled:NO];
        if(_productByAisleArray.count==0){ //if product item list is empty
            lbl.font = [UIFont fontWithName:_app._normalFont size:font_size17];
            lbl.textColor=[UIColor lightGrayColor];
            lbl.backgroundColor=[UIColor clearColor];
            lbl.text=@"No item added";
            return lbl;
        }
        NSArray *array = [_productByAisleArray objectAtIndex:section];
        Product *product = [array objectAtIndex:0];
        if(product.aisleNumber == 999)
            lbl.text = @"Not Available At This Store";
        else
            lbl.text =[NSString stringWithFormat:@"Products in Aisle %d", product.aisleNumber];
    }
    return lbl;
}

-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.01f;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if(tableView == _shoppingProductList){
        
        if(_productByAisleArray.count==0){
            return tableView.frame.size.height;//if product is empty
        }
        else{
            return 30;
        }
    }
    else{
        return 0;
    }
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath;
{
    if(tableView == _shoppingListNameTable)
    {
        return 54;
    }
    else if(tableView == _shoppingProductList){
        return 90;
    }
    else
        return 0;
}


//creates shopping list
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        _app._currentView = self.view; // need this to show the dialog
    else
        _app._currentView = View; // need this to show the dialog
    
    if(tableView == _shoppingListNameTable)
    {
        if(indexPath.row<_shoppingListNameArray.count){
            //create cell for showing shoppinglistname
            static NSString *simpleTableIdentifier = @"ShoppingListCell";
            ShoppingListCell *cell;
            cell= [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
            //get list name from shoppinglistname array
            ShoppingListName *listName = [_shoppingListNameArray objectAtIndex:indexPath.row];
            [tableView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
            //cell background
            [cell.contentView setBackgroundColor:[UIColor clearColor]];
            [cell.accessoryView setBackgroundColor:[UIColor clearColor]];
            [cell.backgroundView setBackgroundColor:[UIColor clearColor]];
            [cell setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"cat_list_cell"]]];
            [cell setAccessoryType:UITableViewCellAccessoryNone];
            cell._cellScrollView.delegate=self;
            //delete
            cell._delete.layer.cornerRadius=3.0;
            // [[cell._tickMark layer] setBorderWidth:1.0f];
            cell._delete.tag=indexPath.row;
            [cell._delete addTarget:self action:@selector(deleteSelectedList:) forControlEvents:UIControlEventTouchUpInside];//delete selected shoppinglist
            cell.selectionStyle = UITableViewCellSelectionStyleGray;
            NSString *name=listName.name;
            [cell._listName setTextColor:[UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0]];
            [cell._listName setText:name];//set name of the shopping list
            [cell._listName setFont:[UIFont fontWithName:@"Verdana" size:15.0f]];
            [cell._cellScrollView setContentSize:CGSizeMake(471, 54)];
            [cell._cellScrollView setBackgroundColor:[UIColor clearColor]];
            [cell._cellScrollView setTag:indexPath.row];
            NSString *currentListId=@"";
            currentListId=[_app getCurrentActiveListIdForAccountId:_login.accountId];
            
            [cell._tickMark setHidden:NO];
            
            if(![currentListId isEqualToString:@""] && [listName.listId isEqualToString:currentListId]){
                //                [cell._tickMark setBackgroundImage:[UIImage imageNamed:@"product_checked_box"] forState:UIControlStateNormal];
                
                [cell._tickMark setTitle:@"Active" forState:UIControlStateNormal];
                [cell._tickMark removeTarget:self action:@selector(activateAsCurrentList:) forControlEvents:UIControlEventTouchUpInside];
                [cell._tickMark addTarget:self action:@selector(openShoppingListByActiveButton:) forControlEvents:UIControlEventTouchUpInside];
                cell._listName.font = [UIFont boldSystemFontOfSize:15.0f];
                [cell._tickMark setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
                cell._tickMark.titleLabel.font = [UIFont systemFontOfSize:13.0];
                cell._tickMark.titleLabel.textAlignment = NSTextAlignmentCenter;
                [cell._tickMark setTitleColor:[UIColor blackColor] forState:UIControlStateHighlighted];
                [cell._tickMark setBackgroundColor:[UIColor colorWithRed:(35/255.f) green:(156/255.f) blue:(92/255.f) alpha:1.0]];
                cell._tickMark.layer.masksToBounds = YES;
                cell._tickMark.layer.cornerRadius = 3.0;
                //                [[cell._tickMark layer] setBorderWidth:1.0f];
                //                [[cell._tickMark layer] setBorderColor:[UIColor colorWithRed:204.0f/255.0f green:204.0f/255.0f blue:204.0f/255.0f alpha:1.0].CGColor];
                
            }else{
                [cell._tickMark setBackgroundImage:nil forState:UIControlStateNormal];
                NSString *title_str;
                title_str = [NSString stringWithFormat:@"%@", @"Set Active"];   // Note the \n will give you a new line
                cell._tickMark.titleLabel.numberOfLines =2;
                cell._listName.font = [UIFont systemFontOfSize:15.0f];
                [cell._tickMark setTitle:title_str forState:UIControlStateNormal];//Set Active
                [cell._tickMark setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
                
                cell._tickMark.titleLabel.font = [UIFont systemFontOfSize:13.0];
                cell._tickMark.titleLabel.textAlignment = NSTextAlignmentCenter;
                [cell._tickMark setTitleColor:[UIColor blackColor] forState:UIControlStateHighlighted];
                [cell._tickMark setBackgroundColor:[UIColor whiteColor]];
                [[cell._tickMark layer] setBorderWidth:1.0f];
                cell._tickMark.layer.masksToBounds = YES;
                cell._tickMark.layer.cornerRadius = 3.0;
                [[cell._tickMark layer] setBorderColor:[UIColor lightGrayColor].CGColor];
                cell._tickMark.tag=indexPath.row;
                [cell._tickMark addTarget:self action:@selector(activateAsCurrentList:) forControlEvents:UIControlEventTouchUpInside];//do shopping list activation process
            }
            
            
            UITapGestureRecognizer *recognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(openShoppingList:)];// getsture listner for showing products of selected shopping list
            //            recognizer.delegate=self;
            [recognizer setNumberOfTapsRequired:1];
            [recognizer setNumberOfTouchesRequired:1];
            cell._cellContentView.userInteractionEnabled = YES;
            [cell._cellContentView addGestureRecognizer:recognizer];
            return cell;
        }
        else if(enableEditMode ==TRUE && indexPath.row ==_shoppingListNameArray.count){
            //show add new listname cell
            static NSString *simpleTableIdentifier = @"ShoppingListEditCell";
            ShoppingListEditCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
            [cell._shoppingListName becomeFirstResponder];
            cell._shoppingListName.text=@"";
            cell._shoppingListName.tag=indexPath.row;
            cell._shoppingListName.delegate=self;
            shopping_list_add_new = cell._shoppingListName;
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            return cell;
        }
        else{
            return nil;
        }
    }
    else if(tableView == _shoppingProductList){//cells for showing shopping product list
        NSArray *array = [_productByAisleArray objectAtIndex:indexPath.section];
        _app._currentProduct = [array objectAtIndex:indexPath.row];
        static NSString *simpleTableIdentifier = @"ShoppingDetailsCell";
        ShoppingDetailsCell *cell;
        cell= [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        //product name
        if(![Utility isEmpty:_app._currentProduct.brand])
            cell._productName.text = [NSString stringWithFormat:@"%@ %@", _app._currentProduct.brand, _app._currentProduct.description];
        else
            cell._productName.text = _app._currentProduct.description;
        //quantity
        int quantity=0;
        float weight= 0.0f;
        if(_app._currentProduct.weight > 0){
            //quantity = (int)_app._currentProduct.weight;
            weight = _app._currentProduct.weight;//mycode
        }
        else
            quantity = _app._currentProduct.qty;
        //price per quantity
        float unitPrice;
        if(_app._currentProduct.promoType > 0)
            unitPrice = _app._currentProduct.promoPrice / _app._currentProduct.promoForQty;
        else
            unitPrice = _app._currentProduct.regPrice;
        if(_app._currentProduct.approxAvgWgt > 0 && _app._currentProduct.qty > 0)
            unitPrice *= _app._currentProduct.approxAvgWgt;
        
        
        //ShoppingListName *listName = [_shoppingListNameArray objectAtIndex:indexPath.row];
        
        CGRect but_frame=cell._deletebtn.frame;
        
        NSString *currentListId=@"";
        currentListId=[_app getCurrentActiveListIdForAccountId:_login.accountId];
        [cell._Move_to_activebtn.layer setCornerRadius:4.0f];
        [cell._Move_to_activebtn setBackgroundColor:[UIColor colorWithRed:(204.0/255.0) green:(0.0/255.0) blue:(0.0/255.0) alpha:1.0] ];
        
        if([_app._selectedShoppingList.listId isEqualToString:_app._currentShoppingList.listId]){
            // if(![currentListId isEqualToString:@""] && [listName.listId isEqualToString:currentListId]){
            
            [cell._Move_to_activebtn setHidden:YES];
            but_frame.origin.y=30.0f;
            [cell._deletebtn setFrame:but_frame];
        }
        else{
            [cell._Move_to_activebtn setHidden:NO];
            but_frame.origin.y=50.0f;
            [cell._deletebtn setFrame:but_frame];
        }
        
        NSString *new_weight=@"";
        if(_app._currentProduct.approxAvgWgt > 0)//if product by weight option available
        {
            if(_app._currentProduct.weight==0 && _app._currentProduct.qty==0){ //by default add by qty
                
                new_weight=@"Qty";
                
            }else if(_app._currentProduct.weight==0 && _app._currentProduct.qty!=0){//if weight is zero add by qty
                
                new_weight=@"Qty";
                
            }else if(_app._currentProduct.weight!=0 && _app._currentProduct.qty==0){//if qty is zero add by weight
                new_weight=@"Lbs";
                
            }else{
                
                new_weight=@"Qty";
            }
        }
        else
        {
            new_weight=@"Qty";
        }
        
        // List of products from the lists
        
        if(_app._currentProduct.weight > 0){
            cell._totalItemPrice.text= [NSString stringWithFormat:@"%@: %.2f   Price: $%.2f",new_weight,weight,weight * unitPrice];
        }
        else{
            cell._totalItemPrice.text= [NSString stringWithFormat:@"%@: %d   Price: $%.2f",new_weight,quantity,quantity * unitPrice];
        }
        
        [cell._productName setTextColor:[UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0]];
        [cell._totalItemPrice setTextColor:[UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0]];
        //product image
        UIImage *image=[_app getCachedImage:[_app._currentProduct.imagePath lastPathComponent]];
        NSString *image_name = [_app._currentProduct.imagePath lastPathComponent];
        if(image == nil){
            image = [UIImage imageNamed:@"noimage"];
        }else if([image_name isEqualToString:@"product-image-default-bag-90px.png"]){
            image = [UIImage imageNamed:@"noimage"];
        }
        
        [cell._productImage setImage:image];
        cell.backgroundView=[[UIView alloc]initWithFrame:cell.bounds];
        //cell background
        [cell.contentView setBackgroundColor:[UIColor clearColor]];
        [cell.accessoryView setBackgroundColor:[UIColor clearColor]];
        [cell.backgroundView setBackgroundColor:[UIColor clearColor]];
        [cell setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"cat_list_cell"]]];
        [tableView setSeparatorStyle:UITableViewCellSeparatorStyleSingleLine];
        
        [cell._scrollView setTag:indexPath.row];
        [cell._scrollView setContentOffset:CGPointMake(0, 0)];
        
        [cell.contentView setUserInteractionEnabled:YES];
        
        //touch gesture for details view
        UITapGestureRecognizer *detailsTapgesture=[[UITapGestureRecognizer alloc]init];
        [detailsTapgesture addTarget:self action:@selector(openShoppingProductDetails:)];
        detailsTapgesture.numberOfTapsRequired=1;
        detailsTapgesture.numberOfTouchesRequired=1;
        [cell._contentView addGestureRecognizer:detailsTapgesture];
        
        
        cell.delegate= self;
        [cell setDeleteButtonEvent:indexPath];
        [cell SetMove_to_Active:indexPath];
        return cell;
    }
    else
        return nil;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    LogInfo(@"item %ld selected", (long)indexPath.row);
    @try {
        [self removeMenus];
        if(tableView == _shoppingListNameTable)
        {
            if(!enableEditMode){//enable edit mode no enabled
                enableEditMode=TRUE;//enable edit mode
                [self moveActiveShopListToTop];
                [_shoppingListNameTable reloadData];
            }
            else{
                //                enableEditMode=FALSE;
                //
                //                [_shoppingListNameTable reloadData];
            }
            
        }
        else if(tableView == _shoppingProductList){
            NSArray *array = [_productByAisleArray objectAtIndex:indexPath.section];
            _app._currentProduct = [array objectAtIndex:indexPath.row];
            ProductDetails *products = [[ProductDetails alloc]initWithNibName:@"ProductDetails" bundle:nil];
            products._shoppingScreenDelegate = (id<ShoppingScreenDelegate>)_app._shoppingScreenVC;
            [Utility Viewcontroller:_contentView.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
            [self presentViewController:products animated:NO completion:nil];
            if(_isCurrentAcitveList){//open productdetails from current active list
                [products creating_controls:_app._currentProduct  _category:nil detailType:PRODUCT_MODIFY _frame:_app._currentView.frame _isCurrentAcitveList:YES];
            }
            else{//open productdetails from selected active list
                [products creating_controls:_app._currentProduct  _category:nil detailType:PRODUCT_MODIFY _frame:_app._currentView.frame _isCurrentAcitveList:NO];
            }
        }
    }
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// table view delegate end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// collection view delegate begin
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - UICollectionView
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    [collectionView.collectionViewLayout invalidateLayout]; // this is required for subsequent layouts where there are fewer objects in the collection
    
    if(collectionView == _productGrid){
        //        [((CollectionViewLayout*)collectionView.collectionViewLayout) resetContentSize];
        return 1;
    }
    else if(collectionView == _offerGrid){
        //        [((CollectionViewLayout*)collectionView.collectionViewLayout) resetContentSize];
        return _offerListsArray.count;
    }
    else if(collectionView == _shoppingListGrid)
        return _productByAisleArray.count;
    else
        return 0;
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    @try {
        if(collectionView == _productGrid)
        {
            return _currentPageProductList.count;
        }
        else if(collectionView == _offerGrid)
        {
            
            [_offerGrid.layer setShadowColor:[UIColor darkGrayColor].CGColor];
            [_offerGrid.layer setShadowOpacity:0.6];
            [_offerGrid.layer setShadowRadius:0.3];
            [_offerGrid.layer setShadowOffset:CGSizeMake(0.0,0.6)];
            
            //        if(section == 0)
            //            return 0;
            
            NSArray *array = [_offerListsArray objectAtIndex:section];
            return array.count;
        }
        else if(collectionView == _shoppingListGrid)
        {
            if(section == 0)
                return 0;
            
            NSArray *array = [_productByAisleArray objectAtIndex:section];
            return array.count;
        }
        else
            return 0;
    }
    @catch (NSException *exception) {
        return 0;
    }
    
}


- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section
{
    if(collectionView == _offerGrid)
    {
        return CGSizeMake(_contentViewWidth, OFFER_GRID_HEADER_HEIGHT);
        
        //        if(section == 0)
        //            return CGSizeMake(_contentViewWidth, _navigationBarHeight * 0.9);
        //        else
        //            return CGSizeMake(_contentViewWidth, _navigationBarHeight * 1.1);
    }else if(collectionView == _shoppingListGrid){
        return CGSizeMake(_contentViewWidth, _navigationBarHeight);
    }
    
    return CGSizeMake(0, 0);
}



- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    if(collectionView == _offerGrid)
    {
        //        BlankHeaderCell *blankCell = nil;
        OfferHeaderCell *headerCell = nil;
        
        if(kind == UICollectionElementKindSectionHeader)
        {
            //            if(indexPath.section == 0)
            //            {
            //                blankCell = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:BLANK_HEADER_CELL_IDENTIFIER forIndexPath:indexPath];
            //                return blankCell;
            //            }
            //            else
            //            {
            headerCell = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:OFFER_HEADER_CELL_IDENTIFIER forIndexPath:indexPath];
            NSMutableArray *array = [_offerListsArray objectAtIndex:indexPath.section];
            CGRect frame = headerCell.frame;
            //            CGFloat previous_height = [((CollectionViewLayout*)collectionView.collectionViewLayout) getMaxHeightInSection:indexPath.section];
            //            if(previous_height>0){
            //                frame.origin.y = previous_height;
            //            }
            frame.size.height = _offerGridLayout.headerReferenceSize.height ; //_navigationBarHeight;
            //                frame.origin.y=(_navigationBarHeight*(indexPath.section+1));
            [headerCell setFrame:frame];
            if(array == _app._personalizedOffersList)
                headerCell._categoryLabel.text = @"Personalized Offers";
            else if(array == _app._extraFriendzyOffersList)
                headerCell._categoryLabel.text = @"Extra Friendzy Offers";
            else if(array == _app._moreForYouOffersList)
                headerCell._categoryLabel.text = @"More For You Offers";
            else if(array == _app._acceptedOffersList)
                headerCell._categoryLabel.text = @"Accepted Offers";
            //            }
        }
        //        [headerCell setBackgroundColor:[UIColor clearColor]];
        //        [headerCell._categoryLabel setBackgroundColor:[UIColor clearColor]];
        //        [headerCell._categoryLabel setTextColor:[UIColor redColor]];
        //        CGRect frame = headerCell.frame;
        //        frame.size.height=0;
        //        [headerCell setFrame:frame];
        return headerCell;
    }
    else if(collectionView == _shoppingListGrid)
    {
        BlankHeaderCell *blankCell = nil;
        ShoppingListHeaderCell *headerCell = nil;
        
        if(kind == UICollectionElementKindSectionHeader)
        {
            if(indexPath.section == 0)
            {
                blankCell = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:BLANK_HEADER_CELL_IDENTIFIER forIndexPath:indexPath];
                return blankCell;
            }
            else
            {
                headerCell = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:SHOPPINGLIST_HEADER_CELL_IDENTIFIER forIndexPath:indexPath];
                NSArray *array = [_productByAisleArray objectAtIndex:indexPath.section];
                Product *product = [array objectAtIndex:0];
                
                if(product.aisleNumber == 999)
                    headerCell._aisleNumber.text = @"Not Available At This Store";
                else
                    headerCell._aisleNumber.text = [NSString stringWithFormat:@"Products in Aisle %d", product.aisleNumber];
            }
        }
        
        return headerCell;
    }
    else
        return nil;
}

-(void)download_image:(UICollectionViewCell*)cell{
    if([cell isKindOfClass:[ProductGridCell class]]){
        ProductGridCell *newcell = (ProductGridCell*)cell;
        UIImage *image = nil;
        image = [_app getImageSync:newcell._product.imagePath];
        dispatch_async(dispatch_get_main_queue(),
                       ^{
                           @try {
                               if(newcell!=NULL && image !=NULL)
                               {
                                   [newcell.loading stopAnimating];
                                   [newcell.loading setHidden:YES];
                                   [newcell._productImageView setImage:image];
                               }else{
                                   if(newcell!=NULL){
                                       [newcell.loading stopAnimating];
                                       [newcell.loading setHidden:YES];
                                       [newcell._productImageView setImage:[UIImage imageNamed:@"noimage"]];
                                   }
                               }
                           }
                           @catch (NSException *exception) {
                               LogInfo(@"download_image : %@",exception.reason);
                           }
                       });
    }
}


- (UICollectionViewCell *)collectionView:(UICollectionView *)cv cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    @try{
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7)
        _app._currentView = self.view; // need this to show the dialog
    else
        _app._currentView = View; // need this to show the dialog
    
    if(cv == _productGrid)
    {
        @try {
            _app._currentProduct = [_currentPageProductList objectAtIndex:indexPath.row];
        }
        @catch (NSException *exception) {
            LogInfo(@"My Search Error: %@",[exception reason]);
        }
        ProductGridCell *cell = [cv dequeueReusableCellWithReuseIdentifier:PRODUCT_GRID_CELL_IDENTIFIER forIndexPath:indexPath];
        cell._product = _app._currentProduct;
        cell.category = _currentProductCategory;
        
        NSString *extendedDetails=_app._currentProduct.extendedDisplay;
        
        if(![Utility isEmpty:_app._currentProduct.brand])
            cell._descriptionLabel.text = [NSString stringWithFormat:@"%@ %@", _app._currentProduct.brand, _app._currentProduct.description];
        else
            cell._descriptionLabel.text = [NSString stringWithFormat:@"%@",_app._currentProduct.description];
        
        
        cell.extends_detailLabl.text=[NSString stringWithFormat:@"%@",extendedDetails];
        
        //        if(![Utility isEmpty:_app._currentProduct.brand])
        //            cell._descriptionLabel.text = [NSString stringWithFormat:@"%@ %@", _app._currentProduct.brand, _app._currentProduct.description];
        //        else
        //            cell._descriptionLabel.text = [NSString stringWithFormat:@"%@",_app._currentProduct.description];
        
        [cell._descriptionLabel setNeedsDisplay];
        //        cell._extendedDisplayLabel_new.text = _app._currentProduct.extendedDisplay;
        //        [cell._extendedDisplayLabel_new setNeedsDisplay];
        cell._regPriceValue.text = [NSString stringWithFormat:@"$%.2f", _app._currentProduct.regPrice];
        [cell._regPriceValue setNeedsDisplay];
        
        NSString *lastPathComponentValue = [_app._currentProduct.imagePath lastPathComponent];
        UIImage *image = [_app getCachedImage:lastPathComponentValue];
        
        // No Image
        NSString *image_name = [cell._product.imagePath lastPathComponent];
        if([image_name isEqualToString:@"product-image-default-bag-90px.png"]){
            image = [UIImage imageNamed:@"noimage"];
        }else if ([_app._missingImagesList objectForKey:lastPathComponentValue]!=nil){
            image = [UIImage imageNamed:@"noimage"];
        }
        //
        
        cell.indexpath = indexPath;
        
        [cell._productImageView setContentMode:UIViewContentModeScaleAspectFit];
        if(image == nil){
            image = [UIImage imageNamed:@"shopping_bag"];
            //            [cell._productImageView setTag:100];
            [cell.loading startAnimating];
            [cell.loading setHidden:NO];
            NSOperation* operation = [[NSInvocationOperation alloc] initWithTarget:self
                                                                          selector:@selector(download_image:)
                                                                            object:cell];
            
            [image_down_task addOperation:operation];
            
        }else{
            [cell.loading setHidden:YES];
        }
        //        else{
        //            [cell.loading setHidden:YES];
        //            if(cell._productImageView.tag==100){
        //                [_productGrid reloadItemsAtIndexPaths:@[indexPath]];
        //            }
        //            [cell._productImageView setTag:200];
        //
        //        }
        [cell._productImageView setImage:image];
        
        CGRect frame = CGRectZero;
        //        // image
        //        NSString *path=[NSString stringWithFormat:@"%@",[_app getImageFullPath:[_app._currentProduct.imagePath lastPathComponent]]];
        //
        //        CGSize product_image_size;
        //        product_image_size=[Utility get_image_width_and_height:path];
        //
        //        CGRect frame=cell._productImageView.frame;
        //
        //        //        frame.size.width=product_image_size.width;
        //        frame.size.height=product_image_size.height;
        //
        //
        //        cell._productImageView.contentMode = UIViewContentModeScaleAspectFill;
        //        [cell._productImageView setClipsToBounds:YES];
        //
        //        [cell._productImageView setFrame:frame];
        
        
        int priceCellHeight=30;
        int cellGap=priceCellHeight*.10;
        int promoImageWidth =(priceCellHeight* .80)-cellGap*2;
        
        CGFloat new_cellGap;
        CGRect x_frame=cell._regPriceValue.frame;
        
        if( _app._currentProduct.promoType > 0)
        {
            
            new_cellGap=(cellGap*2)+promoImageWidth;
            
            [cell._promoImageView setImage:[UIImage imageNamed:@"sale_icon"]];
            [cell._priceCell setImage:[UIImage imageNamed:@"pricecell_red"]];
            cell._regPriceValue.textColor = [UIColor colorWithRed:(187.0/255.0) green:(32.0/255.0) blue:(1.0/255.0) alpha:1.0];
            cell._regPriceValue.text = [NSString stringWithFormat:@"$%.2f", _app._currentProduct.regPrice];
            //            cell._extendedDisplayLabel_new.textColor = [UIColor colorWithRed:(187.0/255.0) green:(32.0/255.0) blue:(1.0/255.0) alpha:1.0];
            
            //            cell._extendedDisplayLabel_new.textColor=[UIColor lightGrayColor];
            
            cell._promoPriceText.text = @"On Sale:";
            
            [cell.see_offer_view setHidden:NO]; /* Raleys 2.1 code updated */
            
            
            if(cell._product.promoForQty > 1)
                cell._promoPriceValue.text = [NSString stringWithFormat:@"%d for $%.2f", cell._product.promoForQty, cell._product.promoPrice];
            else
                cell._promoPriceValue.text = [NSString stringWithFormat:@"$%.2f", cell._product.promoPrice];
        }
        else
        {
            new_cellGap=cellGap;
            
            [cell.see_offer_view setHidden:YES]; /* Raleys 2.1 code updated */
            
            
            [cell._priceCell setImage:[UIImage imageNamed:@"pricecell_normal"]];
            cell._regPriceValue.textColor = [UIColor colorWithRed:(32/255.0) green:(35/255.0) blue:(36/255.0) alpha:1.0];
            //            cell._extendedDisplayLabel_new.textColor=[UIColor lightGrayColor];
            
            [cell._promoImageView setImage:nil];
            cell._promoPriceText.text = @"";
            cell._promoPriceValue.text = @"";
        }
        
        x_frame.origin.x=new_cellGap;
        [cell._regPriceValue setFrame:x_frame];
        
        [cell._addButton addTarget:self action:@selector(addItem) forControlEvents:UIControlEventTouchUpInside];
        
        
        // Realign Cell Frame
        
        // modify (override) font
        [cell._descriptionLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size10]];
        [cell._descriptionLabel setTextAlignment:NSTextAlignmentLeft];
        
        [cell.extends_detailLabl setFont:[UIFont fontWithName:_app._normalFont size:font_size10]];
        
        CGFloat c_gap = GRIDCELL_GAP;
        @try {
            _app._currentProduct = [_currentPageProductList objectAtIndex:indexPath.row];
        }
        @catch (NSException *exception) {
            LogInfo(@"My Search Error: %@",[exception reason]);
        }
        CGSize size = CGSizeZero;
        size.height = 0;
        
        
        // Product Image
        CGSize image_size =[Utility get_image_width_and_height:[_app getImageFullPath:[_app._currentProduct.imagePath lastPathComponent]]];
        // Override image size
        image_size.width = GRID_IMAGE_SIZE;
        image_size.height = GRID_IMAGE_SIZE;
        //
        
        CGFloat image_height = image_size.height;
        frame = cell._productImageView.frame;
        frame.origin.x = 0;
        frame.size.width = _productGridLayout.itemSize.width;
        if(image_size.width>_productGridLayout.itemSize.width){
            CGFloat radio = _productGridLayout.itemSize.width / image_size.width;
            image_height = image_height * radio;
        }else{
            CGFloat new_image_x = _productGridLayout.itemSize.width - image_size.width;
            new_image_x /=2;
            frame.origin.x = new_image_x;
            frame.size.width = image_size.width;
        }
        
        frame.origin.y = size.height;
        frame.size.height = image_height;
        [cell._productImageView setFrame:frame];
        
        size.height+=image_height;
        size.height+= c_gap;
        
        
        // Title
        //        CGFloat common_lbl_width =  _productGridLayout.itemSize.width - (_productGridLayout.itemSize.width * (GRIDCELL_PADDING*2));
        //        CGSize desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:font_size10 _text:cell._descriptionLabel.text];
        //        frame = cell._descriptionLabel.frame;
        //        frame.origin.y=size.height;
        //        frame.size.height = desc_size.height;
        //        [cell._descriptionLabel setFrame:frame];
        //        size.height+=desc_size.height;
        //        size.height+= c_gap;
        
        frame = cell._descriptionLabel.frame;
        frame.origin.y=size.height;
        // frame.size.height = desc_size.height;
        [cell._descriptionLabel setFrame:frame];
        size.height+=28.0f;
        size.height+= c_gap;
        
        //Extends label
        frame = cell.extends_detailLabl.frame;
        frame.origin.y=size.height;
        CGFloat copy_y=frame.origin.y;
        // frame.size.height = cell.extends_detailLabl.frame.size.height;
        [cell.extends_detailLabl setFrame:frame];
        size.height+=13.0f;
        size.height+= c_gap;
        
        /* Raleys 2.1 code updated */
        
        // See offer view
        frame=cell.see_offer_view.frame;
        frame.origin.y=copy_y;
        [cell.see_offer_view setFrame:frame];
        
        /* Raleys 2.1 code updated */
        
        
        // Price
        frame = cell._priceCellContainer.frame;
        frame.size.height = image_height;
        [cell._priceCellContainer setFrame:frame];
        
        //        [cell._productImageView setContentMode:UIViewContentModeScaleAspectFit];
        
        frame = cell._priceCell_new.frame;
        frame.origin.x = 0;
        frame.origin.y = size.height;
        frame.size.width = cell.frame.size.width;
        frame.size.height = GRIDCELL_PRICE_HEIGHT;
        [cell._priceCell_new setFrame:frame];
        size.height+=GRIDCELL_PRICE_HEIGHT;
        size.height+= c_gap;
        
        [cell._priceCell_new setClipsToBounds:YES];
        
        frame = cell.frame;
        frame.size.height = size.height;
        [cell setFrame:frame];
        
        //        LogInfo(@"Create %d %f",indexPath.row, size.height);
        
        [cell setClipsToBounds:YES];
        //
        
        
        cell._parent=self;
        cell._shoppingScreenDelegate = (id<ShoppingScreenDelegate>)_app._shoppingScreenVC;
        
        return cell;
    }
    
    else if(cv == _offerGrid)
    {
        NSMutableArray *array = [_offerListsArray objectAtIndex:indexPath.section];
        _app._currentOffer = [array objectAtIndex:indexPath.row];
        
        OfferGridCell *cell = [cv dequeueReusableCellWithReuseIdentifier:OFFER_GRID_CELL_IDENTIFIER forIndexPath:indexPath];
        cell._offer = _app._currentOffer;
        UIImage *image=[_app getCachedImage:_app._currentOffer.offerProductImageFile];
        if(image==nil){
            image=[UIImage imageNamed:@"noimage.png"];
        }
        [cell._offerImageView setImage:image];
        cell._consumerTitleLabel.text = _app._currentOffer.consumerTitle;
        [cell._consumerTitleLabel setNeedsDisplay];
        cell._consumerDescriptionLabel.text = _app._currentOffer.consumerDesc;
        [cell._consumerDescriptionLabel setNeedsDisplay];
        cell._offerLimitLabel.text = _app._currentOffer.offerLimit;
        [cell._offerLimitLabel setNeedsDisplay];
        
        NSTimeInterval timeStamp = [_app._currentOffer.endDate doubleValue] / 1000; // endDate is in milliseconds
        NSDate *date = [NSDate dateWithTimeIntervalSince1970:timeStamp];
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        [dateFormat setDateStyle:NSDateFormatterShortStyle];
        
        cell._endDateLabel.text = [NSString stringWithFormat:@"Good thru %@", [dateFormat stringFromDate:date]];
        [cell._endDateLabel setNeedsDisplay];
        
        if(_app._currentOffer._acceptableOffer == YES)
        {
            cell._acceptOfferButton.hidden = NO;
            cell._acceptedOfferLabel.hidden = YES;
        }
        else if(_app._currentOffer._acceptedOffer == YES)
        {
            cell._acceptOfferButton.hidden = YES;
            cell._acceptedOfferLabel.hidden = NO;
            cell.accepted_offer_img.hidden=NO;
            //            cell._acceptedOfferLabel.text = @"Offer Accepted";
            //            [cell._acceptedOfferLabel setTextColor:[UIColor blackColor]];
            //            [cell._acceptedOfferLabel setBackgroundColor:[UIColor colorWithRed:225.0/255.0 green:225.0/255.0 blue:225.0/255.0 alpha:1]];
            [cell._acceptedOfferLabel setImage:[UIImage imageNamed:@"offer_accepted.png"]];
            [cell._acceptedOfferLabel setNeedsDisplay];
        }
        else
        {
            cell.accepted_offer_img.hidden=YES;
            cell._acceptOfferButton.hidden = YES;
            cell._acceptedOfferLabel.hidden = NO;
            //            cell._acceptedOfferLabel.text = @"Always Available";
            //            [cell._acceptedOfferLabel setTextColor:[UIColor whiteColor]];
            [cell._acceptedOfferLabel setImage:[UIImage imageNamed:@"offer_always_available.png"]];
            
            //            [cell._acceptedOfferLabel setBackgroundColor:[UIColor colorWithRed:187.0/255.0 green:0.0/255.0 blue:0.0/255.0 alpha:1]];
            [cell._acceptedOfferLabel setNeedsDisplay];
        }
        
        // temp
        //        [cell._acceptOfferButton setHidden:NO];
        //        [cell._acceptedOfferLabel setHidden:NO];
        //        for (UIView *view in cell.subviews) {
        //            [view.layer setBorderWidth:1];
        //            [view.layer setBorderColor:[UIColor redColor].CGColor];
        //        }
        //
        
        
        // Realign Cell Frame
        
        // modify (override) font
        [cell._consumerTitleLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size10]];
        [cell._consumerDescriptionLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size10]];
        [cell._offerLimitLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size10]];
        [cell._endDateLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size10]];
        //        [cell._acceptedOfferLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size13]];
        //        [cell._acceptOfferButton.titleLabel setFont:[UIFont fontWithName:_app._normalFont size:font_size13]];
        
        [cell._consumerTitleLabel setTextColor:[UIColor colorWithRed:(51.0/255.0) green:(51.0/255.0) blue:(51.0/255.0) alpha:1.0]];
        [cell._consumerDescriptionLabel setTextColor:[UIColor darkGrayColor]];
        [cell._offerLimitLabel setTextColor:[UIColor darkGrayColor]];
        [cell._endDateLabel setTextColor:[UIColor darkGrayColor]];
        //        [cell._acceptedOfferLabel setTextColor:[UIColor whiteColor]];
        //        [cell._acceptOfferButton.titleLabel setTextColor:[UIColor whiteColor]];
        
        [cell._consumerTitleLabel setTextAlignment:NSTextAlignmentLeft];
        [cell._consumerDescriptionLabel setTextAlignment:NSTextAlignmentLeft];
        [cell._offerLimitLabel setTextAlignment:NSTextAlignmentLeft];
        [cell._endDateLabel setTextAlignment:NSTextAlignmentLeft];
        
        //        [cell._consumerTitleLabel setLineBreakMode:NSLineBreakByWordWrapping];
        //        [cell._consumerDescriptionLabel setLineBreakMode:NSLineBreakByWordWrapping];
        //        [cell._offerLimitLabel setLineBreakMode:NSLineBreakByWordWrapping];
        //        [cell._endDateLabel setLineBreakMode:NSLineBreakByWordWrapping];
        //        [cell._acceptedOfferLabel setLineBreakMode:NSLineBreakByWordWrapping];
        
        
        CGRect frame = CGRectZero;
        CGFloat c_gap = GRIDCELL_GAP;
        CGSize size = CGSizeZero;
        //        size.height = GRIDCELL_GAP;
        
        
        // Product Image
        
        
        CGSize image_size =[Utility get_image_width_and_height:[_app getImageFullPath:[_app._currentOffer.offerProductImageFile lastPathComponent]]];
        // Override image size
        image_size.width = GRID_IMAGE_SIZE;
        image_size.height = GRID_IMAGE_SIZE;
        CGFloat image_height = image_size.height;
        
        frame = cell._offerImageView.frame;
        frame.origin.x=0;
        frame.size.width = _offerGridLayout.itemSize.width;
        if(image_size.width>_offerGridLayout.itemSize.width){
            CGFloat radio = _offerGridLayout.itemSize.width / image_size.width;
            image_height = image_height * radio;
        }else{
            CGFloat new_image_x = _offerGridLayout.itemSize.width - image_size.width;
            new_image_x /=2;
            frame.origin.x = new_image_x;
            frame.size.width = image_size.width;
        }
        
        frame.origin.y = size.height;
        frame.size.height = image_height;
        [cell._offerImageView setFrame:frame];
        
        [cell._offerImageView setContentMode:UIViewContentModeScaleAspectFit];
        
        size.height+=image_height;
        size.height+= c_gap;
        
        /*
         _consumerTitleLabel
         _consumerDescriptionLabel
         _offerLimitLabel
         _endDateLabel
         _acceptedOfferLabel
         _acceptOfferButton
         */
        
        
        CGFloat common_lbl_width =  _offerGridLayout.itemSize.width - (_offerGridLayout.itemSize.width * (GRIDCELL_PADDING*2));
        
        // Consumer Title Label
        //        CGSize desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:cell._consumerTitleLabel.font.fontName _fontsize:cell._consumerTitleLabel.font.pointSize _text:cell._consumerTitleLabel.text];
        CGSize desc_size =CGSizeMake(0, 28.0f);
        frame = cell._consumerTitleLabel.frame;
        frame.origin.y=size.height;
        frame.size.height = desc_size.height;
        [cell._consumerTitleLabel setFrame:frame];
        if(desc_size.height>0){
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        
        [cell._consumerDescriptionLabel setHidden:YES];
        //        // Consumer Description Label
        //        desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:cell._consumerDescriptionLabel.font.fontName _fontsize:cell._consumerDescriptionLabel.font.pointSize _text:cell._consumerDescriptionLabel.text];
        //        frame = cell._consumerDescriptionLabel.frame;
        //        if(cell._consumerDescriptionLabel.text.length>0){
        //            desc_size.height = 40;
        //        }
        //        frame.origin.y=size.height;
        //        frame.size.height = desc_size.height;
        //        [cell._consumerDescriptionLabel setFrame:frame];
        //        if(desc_size.height>0){
        //            size.height+=desc_size.height;
        //            size.height+= c_gap;
        //        }
        
        
        // Offer Limit Label
        desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:cell._offerLimitLabel.font.fontName _fontsize:cell._offerLimitLabel.font.pointSize _text:cell._offerLimitLabel.text];
        frame = cell._offerLimitLabel.frame;
        frame.origin.y=size.height;
        frame.size.height = desc_size.height;
        [cell._offerLimitLabel setFrame:frame];
        if(desc_size.height>0){
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        
        
        // End Dated Label
        desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:cell._endDateLabel.font.fontName _fontsize:cell._endDateLabel.font.pointSize _text:cell._endDateLabel.text];
        frame = cell._endDateLabel.frame;
        frame.origin.y=size.height;
        frame.size.height = desc_size.height;
        [cell._endDateLabel setFrame:frame];
        if(desc_size.height>0){
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        
        
        if(_app._currentOffer._acceptableOffer == YES)
        {
            // Accepted Offer Button
            //            frame = cell._acceptOfferButton.frame;
            //            frame.origin.x = 0;
            //            frame.origin.y = size.height;
            //            frame.size.width = cell.frame.size.width;
            //            frame.size.height = GRIDCELL_PRICE_HEIGHT/1.2;
            //            [cell._acceptOfferButton setFrame:frame];
            //            size.height+=frame.size.height;
            //            size.height+= c_gap;
            
            //            desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:font_size10 _text:cell._acceptOfferButton.titleLabel.text];
            
            desc_size.height=24.0f;
            frame = cell._acceptOfferButton.frame;
            frame.origin.y=size.height;
            frame.size.height = desc_size.height;
            [cell._acceptOfferButton setFrame:frame];
            size.height+=desc_size.height;
            size.height+= c_gap;
            
        }
        else if(_app._currentOffer._acceptedOffer == YES)
        {
            // Accepted Offer Label
            //            desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:cell._acceptedOfferLabel.font.fontName _fontsize:cell._acceptedOfferLabel.font.pointSize _text:cell._acceptedOfferLabel.text];
            desc_size.height=18.0f;
            frame = cell._acceptedOfferLabel.frame;
            frame.origin.y=size.height;
            frame.size.height = desc_size.height;
            [cell._acceptedOfferLabel setFrame:frame];
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        else
        {
            // Accepted Offer Label
            //            desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:cell._acceptedOfferLabel.font.fontName _fontsize:cell._acceptedOfferLabel.font.pointSize _text:cell._acceptedOfferLabel.text];
            
            desc_size.height=18.0f;
            frame = cell._acceptedOfferLabel.frame;
            frame.origin.y=size.height;
            frame.size.height = desc_size.height;
            [cell._acceptedOfferLabel setFrame:frame];
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        
        
        frame = cell.frame;
        frame.size.height = size.height;
        [cell setFrame:frame];
        
        //        LogInfo(@"Create %d %f",indexPath.row, size.height);
        
        
        [cell.contentView setClipsToBounds:YES];
        [cell setClipsToBounds:YES];
        [cell.backgroundView setClipsToBounds:YES];
        //
        
        cell._parent = self;
        
        //        NSInteger number_of_section = [_offerGrid numberOfSections]-1;
        //        NSInteger number_of_item_last_section = 0;
        //        number_of_item_last_section = [_offerGrid numberOfItemsInSection:number_of_section]-1;
        //        if(number_of_section==indexPath.section && number_of_item_last_section == indexPath.row){
        //            CGRect gframe = [_offerGrid layoutAttributesForItemAtIndexPath:indexPath].frame;
        //            [_offerGrid setContentSize:CGSizeMake(_offerGrid.bounds.size.width, (gframe.origin.y + gframe.size.height))];
        //        }
        
        //        [cell setNeedsDisplay];
        
        return cell;
    }
    else if(cv == _shoppingListGrid)
    {
        NSArray *array = [_productByAisleArray objectAtIndex:indexPath.section];
        _app._currentProduct = [array objectAtIndex:indexPath.row];
        ShoppingListGridCell *cell = [cv dequeueReusableCellWithReuseIdentifier:SHOPPINGLIST_GRID_CELL_IDENTIFIER forIndexPath:indexPath];
        cell._product = _app._currentProduct;
        
        if(![Utility isEmpty:_app._currentProduct.brand])
            cell._descriptionLabel.text = [NSString stringWithFormat:@"%@ %@", _app._currentProduct.brand, _app._currentProduct.description];
        else
            cell._descriptionLabel.text = _app._currentProduct.description;
        
        [cell._descriptionLabel setNeedsDisplay];
        cell._extendedDisplayLabel.text = _app._currentProduct.extendedDisplay;
        [cell._extendedDisplayLabel setNeedsDisplay];
        
        int quantity;
        float weight;
        if(_app._currentProduct.weight > 0)
            weight = _app._currentProduct.weight;
        else
            quantity = _app._currentProduct.qty;
        
        if(_app._currentProduct.weight > 0)
            cell._quantity.text = [NSString stringWithFormat:@"Quantity: %0.2f", weight];
        else
            cell._quantity.text = [NSString stringWithFormat:@"Quantity: %d", quantity];
        [cell._quantity setNeedsDisplay];
        
        UIImage *image = [_app getCachedImage:[_app._currentProduct.imagePath lastPathComponent]];
        
        if(image == nil)
            image = [UIImage imageNamed:@"shopping_bag"];
        
        [cell._productImageView setImage:image];
        
        if(_app._currentProduct.promoType > 0)
            [cell._promoImageView setImage:[UIImage imageNamed:@"sale_tag"]];
        else
            [cell._promoImageView setImage:[UIImage imageNamed:@""]];
        
        float unitPrice;
        
        if(_app._currentProduct.promoType > 0)
            unitPrice = _app._currentProduct.promoPrice / _app._currentProduct.promoForQty;
        else
            unitPrice = _app._currentProduct.regPrice;
        
        if(_app._currentProduct.approxAvgWgt > 0 && _app._currentProduct.qty > 0)
            unitPrice *= _app._currentProduct.approxAvgWgt;
        
        if(_app._currentProduct.weight > 0)
            cell._totalPrice.text = [NSString stringWithFormat:@"$%.2f", weight * unitPrice];
        else
            cell._totalPrice.text = [NSString stringWithFormat:@"$%.2f", quantity * unitPrice];
        [cell._totalPrice setNeedsDisplay];
        
        cell._shoppingScreenDelegate = (id<ShoppingScreenDelegate>)_app._shoppingScreenVC;
        return cell;
    }
    else
    {
        return nil;
    }
    }
    @catch (NSException *exception) {
        LogInfo(@"Collectionview: %@",[exception reason]);
    }
}


- (UIEdgeInsets)collectionView:
(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
    
    if(collectionView==_productGrid){
        //        return UIEdgeInsetsMake(0, 8, 0, 8);
        return _productGridLayout.sectionInset;
    }
    else if(collectionView == _offerGrid)
    {
        return _offerGridLayout.sectionInset;
    }else if (collectionView==_shoppingListGrid){
        
        return _shoppingListGridLayout.sectionInset;
    }else{
        
        return UIEdgeInsetsMake(0, 0, 0, 0);
    }
}


-(CGSize)customCollectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    return [self collectionView:collectionView layout:collectionViewLayout sizeForItemAtIndexPath:indexPath];
}


-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if(collectionView==_productGrid){
        CGFloat c_gap = GRIDCELL_GAP;
        @try {
            _app._currentProduct = [_currentPageProductList objectAtIndex:indexPath.row];
        }
        @catch (NSException *exception) {
            LogInfo(@"My Search Error: %@",[exception reason]);
        }
        
        NSString *description;
        //        if(![Utility isEmpty:_app._currentProduct.brand]){
        //            description = [NSString stringWithFormat:@"%@ %@", _app._currentProduct.brand, _app._currentProduct.description];
        //        }
        //        else{
        //            description = _app._currentProduct.description;
        //        }
        //        NSString *extendedDetails=_app._currentProduct.extendedDisplay;
        
        if(![Utility isEmpty:_app._currentProduct.brand])
            description = [NSString stringWithFormat:@"%@ %@", _app._currentProduct.brand, _app._currentProduct.description];
        else
            description = [NSString stringWithFormat:@"%@",_app._currentProduct.description];
        
        
        
        //        CGFloat common_lbl_width =  _productGridLayout.itemSize.width - (_productGridLayout.itemSize.width * (GRIDCELL_PADDING*2));
        //
        //        CGSize size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:font_size10 _text:description];
        CGSize image_size =[Utility get_image_width_and_height:[_app getImageFullPath:[_app._currentProduct.imagePath lastPathComponent]]];
        // Override image size
        image_size.width = GRID_IMAGE_SIZE;
        image_size.height = GRID_IMAGE_SIZE;
        //
        
        CGFloat image_height = image_size.height;
        if(image_size.width>_productGridLayout.itemSize.width){
            CGFloat radio = _productGridLayout.itemSize.width / image_size.width;
            image_height = image_height * radio;
        }
        
        CGSize size=CGSizeMake(0, 28);
        size.height+=image_height;
        size.height+=GRIDCELL_PRICE_HEIGHT;
        size.height+=13.0f; // extended label
        size.height+=(c_gap*4);
        //        size.height+=image_height;
        //        size.height+=GRIDCELL_PRICE_HEIGHT;
        //        size.height+=(c_gap*3);
        //        LogInfo(@"Guess %ld %f",(long)indexPath.row, size.height);
        //        return _productGridLayout.itemSize;
        size.width = _productGridLayout.itemSize.width;
        return size;
    }
    else if(collectionView == _offerGrid)
    {
        
        NSMutableArray *array = [_offerListsArray objectAtIndex:indexPath.section];
        _app._currentOffer = [array objectAtIndex:indexPath.row];
        CGFloat c_gap = GRIDCELL_GAP;
        CGSize size = CGSizeZero;
        //        size.height = GRIDCELL_GAP;
        size.width = _offerGridLayout.itemSize.width;
        
        // Product Image
        CGSize image_size =[Utility get_image_width_and_height:[_app getImageFullPath:[_app._currentOffer.offerProductImageFile lastPathComponent]]];
        //        image_size.width = GRID_IMAGE_SIZE;
        //        image_size.height = GRID_IMAGE_SIZE;
        CGFloat image_height = image_size.height;
        
        if(image_size.width>_offerGridLayout.itemSize.width){
            CGFloat radio = _offerGridLayout.itemSize.width / image_size.width;
            image_height = image_height * radio;
        }
        
        size.height+=image_height;
        size.height+= c_gap;
        
        /*
         _consumerTitleLabel
         _consumerDescriptionLabel
         _offerLimitLabel
         _endDateLabel
         _acceptedOfferLabel
         _acceptOfferButton
         */
        
        NSTimeInterval timeStamp = [_app._currentOffer.endDate doubleValue] / 1000; // endDate is in milliseconds
        NSDate *date = [NSDate dateWithTimeIntervalSince1970:timeStamp];
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        [dateFormat setDateStyle:NSDateFormatterShortStyle];
        
        NSString *endDateLabel = [NSString stringWithFormat:@"Good thru %@", [dateFormat stringFromDate:date]];
        
        CGFloat common_lbl_width =  _offerGridLayout.itemSize.width - (_offerGridLayout.itemSize.width * (GRIDCELL_PADDING*2));
        
        // Consumer Title Label
        //        CGSize desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:(font_size10) _text:_app._currentOffer.consumerTitle];
        CGSize desc_size =CGSizeMake(0, 28.0f);
        if(desc_size.height>0){
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        
        
        // Consumer Description Label
        //        desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:GRIDCELL_FONT_SIZE _text:_app._currentOffer.consumerDesc];
        //        if(desc_size.height>0){
        ////            size.height+=desc_size.height;
        //            size.height += 40;
        //            size.height+= c_gap;
        //        }
        
        
        // Offer Limit Label
        desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:font_size10 _text:_app._currentOffer.offerLimit];
        if(desc_size.height>0){
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        
        
        // End Dated Label
        desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:font_size10 _text:endDateLabel];
        if(desc_size.height>0){
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        
        if(_app._currentOffer._acceptableOffer == YES)
        {
            //            cell._acceptOfferButton.hidden = NO;
            //            cell._acceptedOfferLabel.hidden = YES;
            //            desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:font_size13 _text:@"Accept This Offer"];
            
            desc_size.height=24.0f; // button image height
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        else if(_app._currentOffer._acceptedOffer == YES)
        {
            //            cell._acceptOfferButton.hidden = YES;
            //            cell._acceptedOfferLabel.hidden = NO;
            //            cell._acceptedOfferLabel.text = @"Offer Accepted";
            //            [cell._acceptedOfferLabel setNeedsDisplay];
            //            desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:font_size13 _text:@"Offer Accepted"];
            desc_size.height=18.0f;// button image height
            size.height+=desc_size.height;
            size.height+= c_gap;
        }
        else
        {
            //            cell._acceptOfferButton.hidden = YES;
            //            cell._acceptedOfferLabel.hidden = NO;
            //            cell._acceptedOfferLabel.text = @"Always Available";
            //            [cell._acceptedOfferLabel setNeedsDisplay];
            //            desc_size = [self getUILabelFontSizeBasedOnText_width:common_lbl_width _fontname:_app._normalFont _fontsize:font_size13 _text:@"Always Available"];
            desc_size.height=18.0f;// button image height
            size.height+=desc_size.height;
            size.height+= c_gap;
            
        }
        
        
        //        if(_app._currentOffer._acceptableOffer == YES)
        //        {
        // Accepted Offer Label
        //            desc_size = [self getUILabelFontSizeBasedOnText_width:_productGridLayout.itemSize.width _fontname:_app._normalFont _fontsize:GRIDCELL_FONT_SIZE _text:@"Offer Accepted"];
        //            size.height+=desc_size.height;
        //            size.height+= c_gap;
        //        }
        //        else if(_app._currentOffer._acceptedOffer == YES)
        //        {
        // Accepted Offer Button
        //            size.height+=GRIDCELL_PRICE_HEIGHT;
        //            size.height+= c_gap;
        //        }
        //        else
        //        {
        //            // Accepted Offer Label
        //            desc_size = [self getUILabelFontSizeBasedOnText_width:_productGridLayout.itemSize.width _fontname:_app._normalFont _fontsize:GRIDCELL_FONT_SIZE _text:@"Always Available"];
        //            size.height+=desc_size.height;
        //            size.height+= c_gap;
        //        }
        
        
        return size;
    }else if (collectionView==_shoppingListGrid){
        return _shoppingListGridLayout.itemSize;
    }else{
        return CGSizeZero;
    }
    //    return [self get_height:indexPath]    ;
}



- (void)collectionView:(UICollectionView *)collectionView didDeselectItemAtIndexPath:(NSIndexPath *)indexPath
{
    if(collectionView == _productGrid)
        LogInfo(@"product item %ld:%ld selected", (long)indexPath.section, (long)indexPath.row);
    else if(collectionView == _offerGrid)
        LogInfo(@"offer item %ld:%ld selected", (long)indexPath.section, (long)indexPath.row);
    else if(collectionView == _shoppingListGrid)
        LogInfo(@"shopping list item %ld:%ld selected", (long)indexPath.section, (long)indexPath.row);
}

//- (UIEdgeInsets)collectionView:
//
//(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
//    return UIEdgeInsetsMake(0, 10, 0, 10);
//}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// collection view delegate end
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

# pragma mark - Util

-(CGSize)getUILabelFontSizeBasedOnText_width:(CGFloat)width _fontname:(NSString*)font _fontsize:(CGFloat)fsize _text:(NSString*)text{
    @try {
        CGSize size = CGSizeMake(width, 0);
        //    if([Utility isEmpty:text]){
        if(text.length<=0){
            return size;
        }
        CGSize constrainedSize = CGSizeMake(size.width, 99999);
        NSDictionary *attributesDictionary = [NSDictionary dictionaryWithObjectsAndKeys:
                                              [UIFont fontWithName:font size:fsize], NSFontAttributeName,
                                              nil];
        NSMutableAttributedString *string = [[NSMutableAttributedString alloc] initWithString:text attributes:attributesDictionary];
        CGRect requiredHeight = [string boundingRectWithSize:constrainedSize options:NSStringDrawingUsesLineFragmentOrigin context:nil];
        if (requiredHeight.size.width > size.width) {
            requiredHeight = CGRectMake(0,0, size.width, requiredHeight.size.height);
        }
        size.height = ceil(requiredHeight.size.height);//+10); // plus 10 for top and bottom gap of label
        //    size = [text sizeWithFont:[UIFont fontWithName:font size:fsize] constrainedToSize:constrainedSize lineBreakMode:NSLineBreakByWordWrapping];
        //    size.height += 10;
        return size;
        
        
    }
    @catch (NSException *exception) {
        LogInfo(@"ShoppingScreenVC - getUILabelFontSizeBasedOnText_width: %@",exception.reason);
        
        CGSize size = CGSizeMake(width, 0);
        return size;
    }
}


#pragma mark UITextField Delegate

-(void)textFieldDidBeginEditing:(UITextField *)textField{
    [self hideMenus];
}


- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if(textField == _listNameText)
        [_listNameText resignFirstResponder];
    else if(textField == _searchTextField){
        [_searchTextField resignFirstResponder];
        [self searchButtonPressed];
    }
    else if(textField == shopping_list_add_new)
    {
        if([textField.text length]>3){
            _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Creating new list..."];
            [_progressDialog show];
            [textField resignFirstResponder];
            ListCreateRequest *request = [[ListCreateRequest alloc] init];
            request.email = _login.email;
            request.accountId = _login.accountId;
            request.listName = textField.text;
            
            _service = [[WebService alloc]initWithListener:self responseClassName:@"ListCreateRequest"];
            [_service execute:LIST_CREATE_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListCreateServiceResponse method below
        }
        else{
            //[textField becomeFirstResponder];
            [textField resignFirstResponder];
            
            TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:[NSString stringWithFormat:@"List Name must contain at least 4 characters."]];
            [dialog show];
        }
    }
    else
        [textField resignFirstResponder];
    
    return YES;
}

#pragma mark WebServiceListener Delegate
- (void)onServiceResponse:(id)responseObject
{
    @try {
        
        
        if([responseObject isKindOfClass:[ShoppingList class]])
        {
            if(!movetolistFlag){
                
                if(serverShoppingListFlag){
                    [self handleActivateListServiceResponse:responseObject];
                }
                else if(currentActiveListFlag){
                    [self handleCurrentListServiceResponse:responseObject];
                    currentActiveListFlag=FALSE;
                }
                else{
                    [self handleListServiceResponse:responseObject];
                }
            }
            else{
                [self handleMovetoListNameServiceResponse:responseObject];
            }
        }
        else if([responseObject isKindOfClass:[ListCreateRequest class]])
        {
            [_progressDialog dismiss];
            [self handleListCreateServiceResponse:responseObject];
        }
        else if([responseObject isKindOfClass:[ShoppingListName class]])
        {
            [_progressDialog dismiss];
            [self handleListNameServiceResponse:responseObject];
        }
        else if([responseObject isKindOfClass:[ListDeleteRequest class]])
        {
            [_progressDialog dismiss];
            [self handleListDeleteServiceResponse:responseObject];
        }
        else if([responseObject isKindOfClass:[ProductCategory class]])
        {
            [_progressDialog dismiss];
            [self handleProductCategoryServiceResponse:responseObject];
        }
        else if([responseObject isKindOfClass:[ProductsForCategoryResponse class]])
        {
            [self handleProductListServiceResponse:responseObject];
        }
        else if([responseObject isKindOfClass:[Product class]])
        {
            [_progressDialog dismiss];
            [self handleProductServiceResponse:responseObject];
        }
        else if([responseObject isKindOfClass:[OfferAcceptRequest class]])
        {
            [_progressDialog dismiss];
            [self handleAcceptOfferServiceResponse:responseObject];
        }
        else if([responseObject isKindOfClass:[EcartPreOrderResponse class]])
        {
            [_progressDialog dismiss];
            [self handleEcartPreOrderServiceResponse:responseObject];
        }
        else if([responseObject isKindOfClass:[AccountRequest class]])
        {
            [_progressDialog dismiss];
            [self handleAccountServiceResponse:responseObject];
        }
        
        
    }
    @catch (NSException *exception) {
        LogInfo(@"ShoppingScreenVC - onServiceResponse :%@",exception.reason);
    }
}

#pragma mark - shoppinglist delete selected item
- (void)deleteSelectedList:(UIButton*)btn
{
    if(_service!=nil){
        return;//avoid multiple request
    }
    if(enableEditMode){//check edit mode enabled and return
        return;
    }
    if(_shoppingListNameArray.count<1)
    {
        _textDialog = [[TextDialog alloc] initWithView:View title:@"List Error" message:@"You do not have a shopping list to delete."];
        [_textDialog show];
        return;
    }
    //user btn tag for getting list name and id
    _selectedShoppingListIndex=[btn tag];
    ShoppingListName *listName = [_shoppingListNameArray objectAtIndex:_selectedShoppingListIndex];
    _selectedShoppingListId=listName.listId;
    _textDialog = [[TextDialog alloc] initWithView:View title:@"Delete List" message:[NSString stringWithFormat:@"Are you sure you want to delete list '%@'?", listName.name] responder:self leftCallback:@selector(deleteSelectedYes) rightCallback:@selector(deleteSelectedNo)];
    [_textDialog show];
}


- (void)deleteSelectedYes
{
    [_textDialog close];
    [self deleteSelectedListFromServer];
}


- (void)deleteSelectedNo
{
    [_textDialog close];
}


- (void)deleteSelectedListFromServer
{
    ListDeleteRequest *request = [[ListDeleteRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = _selectedShoppingListId;
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Deleting list..."];
    [_progressDialog show];
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ListDeleteRequest"];
    [_service execute:LIST_DELETE_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListDeleteServiceResponse method below
}

//reorder the shopplist name. Active list will got to top
-(void)moveActiveShopListToTop{
    NSString *currentActiveListId;
    ShoppingListName *activeListName;
    currentActiveListId=@"";
    currentActiveListId=[_app getCurrentActiveListIdForAccountId:_login.accountId];//get current active list id from plist
    
    NSMutableArray *tempArray=[[NSMutableArray alloc]init];
    for(int i=0;i<_shoppingListNameArray.count;i++){
        ShoppingListName *listName = [_shoppingListNameArray objectAtIndex:i];
        if(![currentActiveListId isEqualToString:@""] && [currentActiveListId isEqualToString:listName.listId] ){
            activeListName =listName;
            [tempArray insertObject:listName atIndex:0];
        }
        else{
            [tempArray addObject:listName];
        }
    }
    _shoppingListNameArray =[tempArray mutableCopy];
}


#pragma activate shopping list
-(void)activateAsCurrentList:(UIButton*)btn{
    if(_service!=nil){
        return;//avoid multiple request
    }
    if(enableEditMode){//check edit mode enabled and return
        return;
    }
    ShoppingListName *listName = [_shoppingListNameArray objectAtIndex:[btn tag]];
    
    if([listName.listId isEqualToString:_app._currentShoppingList.listId] && _shoppingListView != nil)
        return;
    
    [self activateShoppingList:listName];
}

- (void)activateShoppingList:(ShoppingListName *)listName
{
    _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Activating shopping list..."];
    [_progressDialog show];
    
    ListRequest *request = [[ListRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = listName.listId;
    request.returnCurrentList = [NSNumber numberWithBool:YES];
    
    if([_app._currentShoppingList.listId isEqualToString:listName.listId])
        request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;
    else
        request.appListUpdateTime = [NSNumber numberWithLong:0];
    
    LogInfo(@"SHOPPING_LIST: getShoppingList: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [_app._currentShoppingList.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", _app._currentShoppingList.totalPrice]);
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
    serverShoppingListFlag= TRUE;
    [_service execute:LIST_GET_BY_ID_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
}


- (void)handleActivateListServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];
    [self handleActiveList:responseObject httpStatusCode:status];
}
- (void)handleActiveList:(id)responseObject httpStatusCode:(int)status
{
    @try {
        
        if(status == 200) // service success
        {
            _service = nil;
            ShoppingList *response = (ShoppingList *)responseObject;
            
            //set current active list id in plist
            
            [_app setCurrentActiveListId:response.listId setName:response.name setTotalProduct:[NSString stringWithFormat:@"%d",(int)response.productList.count] ForAccountId:_login.accountId];
            
            if(response == nil)
            {
                [_progressDialog dismiss];
                TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Server Error" message:@"Unable to parse data returned from server."];
                [dialog show];
                return;
            }
            
            [_app storeShoppingListId:response.listId];
            
            if(response.storeNumber != _login.storeNumber)
            {
                LogInfo("Changing login storeNumber to %d", response.storeNumber);
                //                _login.storeNumber = response.storeNumber;
                [_app storeLogin:_login];
            }
            
            LogInfo("SHOPPING_LIST: handleListServiceResponse for %@", response.listId);
            
            if(response.productList == nil)
                LogInfo("SHOPPING_LIST: response.productList is nil");
            else
                LogInfo("SHOPPING_LIST: response.productList contains %lu products", (unsigned long)response.productList.count);
            
            //            if(response.productListReturned == YES)
            if(response.productList!=nil)
            {
                LogInfo("SHOPPING_LIST: response.productListReturned == true, serverUpdateTime = %llu, totalPrice = %@", [response.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", response.totalPrice]);
                
                if(response.productList == nil)
                    LogError(@"handleListServiceResponse productList is nil when productListReturned is YES");
                else
                    _app._currentShoppingList = response;
            }
            else // list wasn't returned so check if we need to manually update the list because a product was added, deleted, or modified, or that we were just syncing prior to submitting an ecart order
            {
                LogInfo("SHOPPING_LIST: responseList.productListReturned == false, serverUpdateTime = %llu, totalPrice = %@", [response.serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", response.totalPrice]);
                _app._currentShoppingList.serverUpdateTime = response.serverUpdateTime;
                _app._currentShoppingList.name = response.name; // for footer details
                _app._currentShoppingList.totalPrice = response.totalPrice; // for footer details
                _app._currentShoppingList.listId = response.listId;
                
                if(_app._productToAdd != nil)
                {
                    LogInfo("SHOPPING_LIST: Adding product: %@, qty:%d, description: %@", _app._productToAdd.upc, _app._productToAdd.qty, _app._productToAdd.description);
                    [_app._currentShoppingList.productList addObject:_app._productToAdd];
                    
                    [_progressDialog dismiss];
                    [self setFooterDetails];
                    
                    if(_shoppingListGrid != nil)
                    {
                        [self buildProductByAisleArray];
                        [_shoppingListGrid  reloadData];
                    }
                    
                    _app._productToAdd = nil;
                    return;
                }
                
                if(_app._productToDelete != nil)
                {
                    LogInfo("SHOPPING_LIST: Deleting product: %@, qty:%d, description: %@", _app._productToDelete.upc, _app._productToDelete.qty, _app._productToDelete.description);
                    
                    for(int i = 0; i < _app._currentShoppingList.productList.count; i++)
                    {
                        Product *product = [_app._currentShoppingList.productList objectAtIndex:i];
                        
                        if([product.upc isEqualToString:_app._productToDelete.upc])
                        {
                            [_app._currentShoppingList.productList removeObjectAtIndex:i];
                            break;
                        }
                    }
                    
                    [_progressDialog dismiss];
                    [self setFooterDetails];
                    
                    if(_shoppingListGrid != nil)
                    {
                        [self buildProductByAisleArray];
                        [_shoppingListGrid  reloadData];
                    }
                    
                    _app._productToDelete = nil;
                    return;
                }
                
                if(_app._productToModify != nil)
                {
                    LogInfo("SHOPPING_LIST: Modifying product: %@, qty:%d, description: %@", _app._productToModify.upc, _app._productToModify.qty, _app._productToModify.description);
                    
                    for(int i = 0; i < _app._currentShoppingList.productList.count; i++)
                    {
                        Product *product = [_app._currentShoppingList.productList objectAtIndex:i];
                        
                        if([product.upc isEqualToString:_app._productToModify.upc])
                        {
                            [_app._currentShoppingList.productList removeObjectAtIndex:i];
                            [_app._currentShoppingList.productList addObject:_app._productToModify];
                            break;
                        }
                    }
                    
                    [_progressDialog dismiss];
                    [self setFooterDetails];
                    
                    if(_shoppingListGrid != nil)
                    {
                        [self buildProductByAisleArray];
                        [_shoppingListGrid  reloadData];
                    }
                    
                    _app._productToModify = nil;
                    return;
                }
                
                if(_validatingEcartList == YES)
                {
                    [_progressDialog dismiss];
                    _validatingEcartList = NO;
                    [self ecartPreOrder];
                    return;
                }
                
            }
            
            [self setFooterDetails];
            
            if(_app._currentShoppingList.productList != nil)
            {
                LogInfo(@"Found %lu products for list %@", (unsigned long)_app._currentShoppingList.productList.count, _app._currentShoppingList.name);
                [_progressDialog changeMessage:@"Retrieving product data..."];
                [_app getProductImages:_app._currentShoppingList.productList]; // this is a thread function so we need our own thread function to determine when it's done
                [NSThread detachNewThreadSelector:@selector(waitForProductImagesThread:) toTarget:self withObject:@"ShoppingList"];
            }
            else
            {
                [_progressDialog dismiss];
            }
            
            if(_progressDialog!=nil){
                [_progressDialog dismiss];
            }
            [self showTick];
            
        }
        else // service failure
        {
            [_progressDialog dismiss];
            
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
        dispatch_async(dispatch_get_main_queue(), ^{
            [self moveActiveShopListToTop];
            
            [_shoppingListNameTable reloadData];
            
        });
    }
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
        
    }
}

#pragma table cell horizontal scrollview
-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView{
    [self removeMenus];
}


-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    //reset swipe to delete
    if(_shoppingListNameTable!=nil ){
        
        if(enableEditMode ==TRUE){//reset all swipe to delete scrolls of the list
            for(int i=0;i<[_shoppingListNameTable numberOfRowsInSection:0];i++){
                NSIndexPath *indexPath = [NSIndexPath indexPathForRow: i inSection:0];
                ShoppingListCell *cell = (ShoppingListCell*)[_shoppingListNameTable cellForRowAtIndexPath:indexPath];
                for(UIView *subviews in cell.contentView.subviews ){
                    if([subviews isKindOfClass:[UIScrollView class]]){
                        UIScrollView *sc=(UIScrollView*)subviews;
                        [sc setContentOffset:CGPointMake(0, 0) animated:NO];
                    }
                }
            }
            return;
        }
        for(int i=0;i<[_shoppingListNameTable numberOfRowsInSection:0];i++){
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow: i inSection: 0];
            ShoppingListCell *cell = (ShoppingListCell*)[_shoppingListNameTable cellForRowAtIndexPath:indexPath];
            for(UIView *subviews in cell.contentView.subviews ){
                if([subviews isKindOfClass:[UIScrollView class]]){
                    UIScrollView *sc=(UIScrollView*)subviews;
                    if(sc != scrollView){
                        [sc setContentOffset:CGPointMake(0, 0) animated:NO];
                    }
                }
            }
        }
    }
    if(_shoppingProductList!=nil ){
        for(int k=0;k<[_shoppingProductList numberOfSections];k++){
            for(int i=0;i<[_shoppingProductList numberOfRowsInSection:k];i++){
                NSIndexPath *indexPath = [NSIndexPath indexPathForRow: i inSection: 0];
                ShoppingDetailsCell *cell = (ShoppingDetailsCell*)[_shoppingProductList cellForRowAtIndexPath:indexPath];
                for(UIView *subviews in cell.contentView.subviews ){
                    if([subviews isKindOfClass:[UIScrollView class]]){
                        UIScrollView *sc=(UIScrollView*)subviews;
                        if(sc != scrollView){
                            [sc setContentOffset:CGPointMake(0, 0) animated:NO];
                        }
                    }
                }
            }
        }
    }
    //hide/show shopping list title when tableview is scrolled
    if(scrollView==_shoppingProductList && scrollView.isTracking){
        int tableHeight=_contentViewHeight-((30*2)+40);
        
        //tableview is empty show title
        if(_productByAisleArray.count==0){
            [UIView beginAnimations:@"lblTitleShow" context:NULL];
            [UIView setAnimationBeginsFromCurrentState:YES];
            [UIView setAnimationCurve:UIViewAnimationCurveLinear];
            [UIView setAnimationDuration:0.2];
            lblTitle.frame=CGRectMake(0, 0, _contentViewWidth, 30);
            _shoppingProductList.frame=CGRectMake(0, 30, _contentViewWidth, tableHeight);
            [UIView commitAnimations];
            return;
        }
        //scroll up
        if(scrollView.contentOffset.y>_shoppingProductListLastOffset){
            [UIView beginAnimations:@"lblTitleHide" context:NULL];
            [UIView setAnimationBeginsFromCurrentState:YES];
            [UIView setAnimationCurve:UIViewAnimationCurveLinear];
            [UIView setAnimationDuration:0.2];
            lblTitle.frame=CGRectMake(0, -30, _contentViewWidth, 30);
            _shoppingProductList.frame=CGRectMake(0, 0, _contentViewWidth, tableHeight+30);
            
            [UIView commitAnimations];
        }
        else if(scrollView.contentOffset.y<_shoppingProductListLastOffset){
            [UIView beginAnimations:@"lblTitleShow" context:NULL];
            [UIView setAnimationBeginsFromCurrentState:YES];
            [UIView setAnimationCurve:UIViewAnimationCurveLinear];
            [UIView setAnimationDuration:0.2];
            lblTitle.frame=CGRectMake(0, 0, _contentViewWidth, 30);
            _shoppingProductList.frame=CGRectMake(0, 30, _contentViewWidth, tableHeight);
            [UIView commitAnimations];
        }
        _shoppingProductListLastOffset=scrollView.contentOffset.y;
        
    }
    
    if(scrollView==_productGrid){
        BOOL scrollEnd = (scrollView.contentOffset.y+scrollView.frame.size.height)>=scrollView.contentSize.height?true:false;
        if(scrollEnd){
            if(_currentPageProductList.count!=0){
                [self forwardButtonPressed];
            }
        }
    }
}
-(void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView{
    
}


-(void)openShoppingList:(UITapGestureRecognizer *) sender
{
    @try {
        
        if(_service!=nil){
            return;//avoid multiple request
        }
        
        [self removeMenus];
        
        BOOL isScrolled=FALSE;
        for(int i=0;i<[_shoppingListNameTable numberOfRowsInSection:0]-1;i++){
            
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow: i inSection: 0];
            
            ShoppingListCell *cell = (ShoppingListCell*)[_shoppingListNameTable cellForRowAtIndexPath:indexPath];
            for(UIView *subviews in cell.contentView.subviews ){
                if([subviews isKindOfClass:[UIScrollView class]]){
                    UIScrollView *sc=(UIScrollView*)subviews;
                    if(sc.contentOffset.x>100){
                        [sc setContentOffset:CGPointMake(0, 0) animated:YES];
                        isScrolled=TRUE;
                    }
                }
            }
        }
        if(isScrolled){
            return;
        }
        if(enableEditMode ==TRUE){
            [shopping_list_add_new resignFirstResponder];
            
            _textDialog = [[TextDialog alloc] initWithView:View title:@"Create New List" message:@"Do you want to save the list?" responder:self leftCallback:@selector(yesCreateNewList) rightCallback:@selector(noCreateNewList)];
            [_textDialog show];
            return;
        }
        enableEditMode=FALSE;
        serverShoppingListFlag=FALSE;
        currentActiveListFlag=FALSE;
        CGPoint touchLocation = [sender locationOfTouch:0 inView:_shoppingListNameTable];
        NSIndexPath *indexPath = [_shoppingListNameTable indexPathForRowAtPoint:touchLocation];
        [_listsModalView removeFromSuperview];
        ShoppingListName *listName = [_shoppingListNameArray objectAtIndex:indexPath.row];
        if([listName.listId isEqualToString:_app._currentShoppingList.listId] && _shoppingListView != nil)
            return;
        if([listName.listId isEqualToString:_app._currentShoppingList.listId]){
            _isCurrentAcitveList=TRUE;
        }
        else{
            _isCurrentAcitveList=FALSE;
        }
        activeShoppingListAnimation = YES;
        [self getShoppingList:listName];
    }
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
    }
}


-(void)openShoppingListByActiveButton:(UIButton*)btn{
    @try {
        
        if([Utility isEmpty:_app._currentShoppingList])
        {
            [self activateAsCurrentList:btn];
            return;
        }
        
        if(_service!=nil){
            return;//avoid multiple request
        }
        
        [self removeMenus];
        
        BOOL isScrolled=FALSE;
        for(int i=0;i<[_shoppingListNameTable numberOfRowsInSection:0]-1;i++){
            
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow: i inSection: 0];
            
            ShoppingListCell *cell = (ShoppingListCell*)[_shoppingListNameTable cellForRowAtIndexPath:indexPath];
            for(UIView *subviews in cell.contentView.subviews ){
                if([subviews isKindOfClass:[UIScrollView class]]){
                    UIScrollView *sc=(UIScrollView*)subviews;
                    if(sc.contentOffset.x>100){
                        [sc setContentOffset:CGPointMake(0, 0) animated:YES];
                        isScrolled=TRUE;
                    }
                }
            }
        }
        if(isScrolled){
            return;
        }
        if(enableEditMode ==TRUE){
            [shopping_list_add_new resignFirstResponder];
            
            _textDialog = [[TextDialog alloc] initWithView:View title:@"Create New List" message:@"Do you want to save the list?" responder:self leftCallback:@selector(yesCreateNewList) rightCallback:@selector(noCreateNewList)];
            [_textDialog show];
            return;
        }
        enableEditMode=FALSE;
        serverShoppingListFlag=FALSE;
        currentActiveListFlag=FALSE;
        [_listsModalView removeFromSuperview];
        ShoppingListName *listName = [_shoppingListNameArray objectAtIndex:0];
        if([listName.listId isEqualToString:_app._currentShoppingList.listId] && _shoppingListView != nil)
            return;
        if([listName.listId isEqualToString:_app._currentShoppingList.listId]){
            _isCurrentAcitveList=TRUE;
        }
        else{
            _isCurrentAcitveList=FALSE;
        }
        activeShoppingListAnimation = YES;
        [self getShoppingList:listName];
    }
    @catch (NSException *exception) {
        LogInfo("%@",exception.reason);
    }
}



-(void)openShoppingProductDetails:(UITapGestureRecognizer*)sender{
    @try {
        
        BOOL isScrolled=FALSE;
        
        for(int k=0;k<[_shoppingProductList numberOfSections];k++){
            
            for(int i=0;i<[_shoppingProductList numberOfRowsInSection:0];i++){
                
                NSIndexPath *indexPath = [NSIndexPath indexPathForRow: i inSection: 0];
                
                ShoppingDetailsCell *cell = (ShoppingDetailsCell*)[_shoppingProductList cellForRowAtIndexPath:indexPath];
                
                for(UIView *subviews in cell.contentView.subviews ){
                    
                    if([subviews isKindOfClass:[UIScrollView class]]){
                        
                        UIScrollView *sc=(UIScrollView*)subviews;
                        
                        if(sc.contentOffset.x>80){
                            
                            CGRect frame=sc.frame;
                            frame.origin.x=0;
                            //                            [sc scrollRectToVisible:frame animated:NO];
                            [sc setContentOffset:CGPointZero animated:NO];
                            
                            isScrolled=TRUE;
                            
                        }
                        
                    }
                    
                }
                
            }
            
        }
        
        if(isScrolled){
            return;
        }
        
        //get index point from touch point
        CGPoint touchpoint=[sender locationOfTouch:0 inView:_shoppingProductList];
        NSIndexPath *indexPath=[_shoppingProductList indexPathForRowAtPoint:touchpoint];
        
        NSArray *array = [_productByAisleArray objectAtIndex:indexPath.section];
        _app._currentProduct = [array objectAtIndex:indexPath.row];
        ProductDetails *products = [[ProductDetails alloc]initWithNibName:@"ProductDetails" bundle:nil];
        products._shoppingScreenDelegate = (id<ShoppingScreenDelegate>)_app._shoppingScreenVC;
        [Utility Viewcontroller:_contentView.window.layer _AnimationStyle:ViewCATransitionsRIGHT];
        [self presentViewController:products animated:NO completion:nil];
        if(_isCurrentAcitveList){//open productdetails from current active list
            [products creating_controls:_app._currentProduct _category:nil detailType:PRODUCT_MODIFY _frame:_app._currentView.frame _isCurrentAcitveList:YES];
        }
        else{//open productdetails from selected active list
            [products creating_controls:_app._currentProduct _category:nil detailType:PRODUCT_MODIFY _frame:_app._currentView.frame _isCurrentAcitveList:NO];
        }
        
    }
    @catch (NSException *exception) {
        
    }
}

-(void)MoveToActive:(NSIndexPath *)currentIndex{
    
    NSArray *array = [_productByAisleArray objectAtIndex:currentIndex.section];
    Product             *_product;
    _product = [array objectAtIndex:currentIndex.row];
    
    BOOL found = NO;
    
    //check whether product was already added
    if(_app._currentShoppingList != nil && _app._currentShoppingList.productList != nil)
    {
        for(Product *product in _app._currentShoppingList.productList)
        {
            if([product.sku isEqualToString:_product.sku])
            {
                found = YES;
                addType=PRODUCT_MODIFY;
                break;
            }
        }
    }
    if(found == NO)
    {
        addType=PRODUCT_ADD;
    }
    [self addProduct:_product];
    
}


- (void)addProduct:(Product*)_product
{
    
    if(_service!=nil){
        return;//avoid multiple request
    }
    
    BOOL isExist=false;
    // int previous_qty=0;
    
    Product *Current_projectObj;
    if(_app._currentShoppingList != nil && _app._currentShoppingList.productList != nil)
    {
        for(Product *product in _app._currentShoppingList.productList)
        {
            if([product.sku isEqualToString:_product.sku])
            {
                isExist=true;
                // _product = product;//use current/updated product in the shopping list
                //                previous_qty=product.qty;
                //                Current_projectObj=product;
                break;
            }
        }
    }
    //%@",_app._shoppingProductList
    
    NSString *current_Active_listName=@"";
    current_Active_listName=[NSString stringWithFormat:@"%@",_app._currentShoppingList.name];
    NSString *msg_text=[NSString stringWithFormat:@"This item already Exists in %@",current_Active_listName];
    
    if(isExist){
        TextDialog *dialog = [[TextDialog alloc] initWithView:_app._shoppingScreenVC.view title:@"Item Exists" message:msg_text];
        [dialog show];
        return;
    }
    else{
        if(addType==PRODUCT_ADD){
            _app._productToAdd = _product;
            _app._productToModify = nil;
            _app._productToDelete = nil;
        }else{
            _app._productToAdd = nil;
            _app._productToModify = _product;
            _app._productToDelete = nil;
        }
        _app._currentProduct=Current_projectObj;
        
        [self updateItem:_product _isProductExists:isExist];
    }
    
}


- (void)updateItem:(Product*)_new_product _isProductExists:(BOOL)Exists
{
    //hide the keyboard when clicking on 'Add to List' or 'Change Quantity'
    
    if([Utility isEmpty:_app._currentShoppingList])
    {
        TextDialog *dialog = [[TextDialog alloc] initWithView:_app._shoppingScreenVC.view title:@"Show Current List" message:@"No active list to add into. Goto lists and make one active."];
        [dialog show];
        return;
    }
    
    int productQty=1;
    
    if(_service!=nil)return;
    
    NSString *listId;
    NSNumber *serverUpdateTime;
    float totalPrice;
    listId= _app._currentShoppingList.listId;
    serverUpdateTime=_app._currentShoppingList.serverUpdateTime;
    totalPrice=_app._currentShoppingList.totalPrice;
    
    ListAddItemRequest *request = [[ListAddItemRequest alloc] init];
    request.accountId = _login.accountId;
    request.listId = listId;
    request.sku = _new_product.sku;
    request.customerComment = _new_product.customerComment;
    request.appListUpdateTime = serverUpdateTime.stringValue;
    
    if(_new_product.approxAvgWgt > 0)//if product by weight option available
    {
        if(_new_product.weight==0 && _new_product.qty==0){ //by default add by qty
            request.qty = productQty;
            productQty=request.qty;
            request.purchaseBy = @"E";
            
        }else if(_new_product.weight==0 && _new_product.qty!=0){//if weight is zero add by qty
            request.qty = productQty;
            productQty=request.qty;
            request.purchaseBy = @"E";
            
        }else if(_new_product.weight!=0 && _new_product.qty==0){//if qty is zero add by weight
            //request.weight = (float)(_new_product.weight+productQty);
            request.weight = (float)(productQty);
            //                productQty=request.weight;
            request.purchaseBy = @"W";
            
        }else{
            request.qty = (_new_product.qty+productQty);//by exception add by qty
            productQty=request.qty;
            request.purchaseBy = @"E";
        }
        
    }
    else
    {
        request.purchaseBy = @"E";
        request.qty = productQty;
        //productQty=request.qty;
    }
    
    movetolistFlag=true;
    serverShoppingListFlag=true;
    
    LogInfo(@"SHOPPING_LIST: ADD: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f", totalPrice]);
    request.returnCurrentList = [NSNumber numberWithBool:YES].stringValue;
    _progressDialog = [[ProgressDialog alloc] initWithView:self.view message:@"Updating your list..."];
    [_progressDialog show];
    
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];  //ShoppingDetailsCell
    [_service execute:LIST_ADD_ITEM_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleMovetoListNameServiceResponse method below
    
    
}




- (void)handleMovetoListNameServiceResponse:(id)responseObject
{
    int status = [_service getHttpStatusCode];
    
    if(status == 200) // service success
    {
        [self handleListServiceResponse:responseObject httpStatusCode:status _isCurrentList:YES];//already in shoppingscreenvc
        //[self updateListCount];
        if(_app._productToAdd != nil )
        {
            addType=PRODUCT_MODIFY;
            [_progressDialog dismiss];
            _app._productToAdd = nil;
        }
        if(_app._productToModify != nil )
        {
            [_progressDialog dismiss];
            _app._productToModify = nil;
            
        }
        if(_app._productToDelete !=nil){
            _app._productToDelete =nil;
        }
        
        //        NSArray *array = [_productByAisleArray objectAtIndex:movedIndex.row];
        //        _app._currentProduct = [array objectAtIndex:movedIndex.row];
        //        //manualy update local _product after response is success
        //        if(_app._currentProduct.approxAvgWgt > 0)
        //        {
        //            if(_app._currentProduct.weight==0 && _app._currentProduct.qty==0){
        //                _app._currentProduct.qty=productQty;
        //                _app._currentProduct.weight = 0;
        //            }else if(_app._currentProduct.weight==0 && _app._currentProduct.qty!=0){
        //                _app._currentProduct.qty=productQty;
        //                _app._currentProduct.weight = 0;
        //
        //            }else if(_app._currentProduct.weight!=0 && _app._currentProduct.qty==0){
        //                _app._currentProduct.weight=productQty;
        //                _app._currentProduct.qty = 0;
        //            }else{
        //                _app._currentProduct.qty=productQty;
        //                _app._currentProduct.weight = 0;
        //            }
        //        }
        //        else
        //        {
        //            _app._currentProduct.qty=productQty;
        //            _app._currentProduct.weight = 0;
        //        }
        
        // lastSku=_app._currentProduct.sku;
        [self showTick];
        //        if(movetolistFlag){
        //            [self showTick];
        //        }
        //[self removeFromSuperview];
    }
    else // service failure
    {
        if(status == 422) // backend error
        {
            WebServiceError *error = [_service getError];
            TextDialog *dialog = [[TextDialog alloc] initWithView:_app._shoppingScreenVC.view title:@"Update List Failed" message:error.errorMessage];
            [dialog show];
        }
        else if(status == 408) // timeout error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:_app._shoppingScreenVC.view title:@"Server Error" message:[NSString stringWithFormat:@"Request Timed Out"]];
            [dialog show];
        }
        else // http error
        {
            TextDialog *dialog = [[TextDialog alloc] initWithView:_app._shoppingScreenVC.view title:@"Server Error" message:COMMON_ERROR_MSG];
            [dialog show];
        }
    }
    
    _service = nil;
    _app._productToAdd = nil;
    _app._productToModify = nil;
    _app._productToDelete = nil;
    
    serverShoppingListFlag=false;
    movetolistFlag=false;
}


-(void)deleteProduct:(NSIndexPath *)indexPath{
    
    if(_service!=nil)return;
    
    NSArray *array = [_productByAisleArray objectAtIndex:indexPath.section];
    Product             *_product;
    _product = [array objectAtIndex:indexPath.row];
    
    _app._productToAdd = nil;
    _app._productToModify = nil;
    _app._productToDelete =_product;
    
    ListDeleteItemRequest *request = [[ListDeleteItemRequest alloc] init];
    request.accountId = _login.accountId;
    if(_isCurrentAcitveList){
        request.listId = _app._currentShoppingList.listId;
        request.appListUpdateTime = _app._currentShoppingList.serverUpdateTime;
    }
    else{
        request.listId = _app._selectedShoppingList.listId;
        request.appListUpdateTime = _app._selectedShoppingList.serverUpdateTime;
    }
    request.sku = _product.sku;
    
    
    NSNumber *serverUpdateTime;
    float totalPrice;
    if(_isCurrentAcitveList){
        serverUpdateTime=_app._currentShoppingList.serverUpdateTime;
        totalPrice=_app._currentShoppingList.totalPrice;
    }
    else{
        serverUpdateTime=_app._selectedShoppingList.serverUpdateTime;
        totalPrice=_app._selectedShoppingList.totalPrice;
    }
    
    request.returnCurrentList = [NSNumber numberWithBool:YES];
    LogInfo(@"SHOPPING_LIST: DELETE: setting returnCurrentList = YES, appListUpdateTime = %llu, totalPrice = %@", [serverUpdateTime longLongValue], [NSString stringWithFormat:@"$%.2f",totalPrice]);
    
    
    if(_progressDialog!=nil){
        [_progressDialog dismiss];
    }
    
    _progressDialog = [[ProgressDialog alloc] initWithView:_app._currentView message:@"Updating your list..."];
    [_progressDialog show];
    
    _service = [[WebService alloc]initWithListener:self responseClassName:@"ShoppingList"];
    [_service execute:LIST_DELETE_ITEM_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListServiceResponse method below
    
#ifdef CLP_ANALYTICS
    //Analytics
    if(_product){
        @try {                                                                //Product Removed from list
            NSMutableDictionary *data = [[NSMutableDictionary alloc]init];
            [data setValue:@"ProductRemoved" forKey:@"event_name"];
            [data setValue:_product.sku forKey:@"SKU"];
            [data setValue:_product.upc forKey:@"item_id"];
            [data setValue:_product.brand forKey:@"brand"];
            [data setValue:_product.description forKey:@"item_name"];
            [data setValue:[NSString stringWithFormat:@"%f",_product.regPrice] forKey:@"price"];
            [data setValue:[NSString stringWithFormat:@"%f",_product.promoPrice]forKey:@"promo_price"];
            [data setValue:_product.mainCategory forKey:@"category"];
            if(_product.weight > 0)
                [data setValue:[NSString stringWithFormat:@"%f",_product.weight] forKey:@"quantity"];
            else
                [data setValue:[NSString stringWithFormat:@"%d",_product.qty] forKey:@"quantity"];
            [_app._clpSDK updateAppEvent:data];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",[exception reason]);
        }
    }
#endif
}

- (void)yesCreateNewList
{
    [shopping_list_add_new resignFirstResponder];
    if([shopping_list_add_new.text length]>3){
        _progressDialog = [[ProgressDialog alloc] initWithView:View message:@"Creating new list..."];
        [_progressDialog show];
        ListCreateRequest *request = [[ListCreateRequest alloc] init];
        request.email = _login.email;
        request.accountId = _login.accountId;
        request.listName = shopping_list_add_new.text;
        
        _service = [[WebService alloc]initWithListener:self responseClassName:@"ListCreateRequest"];
        [_service execute:LIST_CREATE_URL authorization:_login.authKey requestObject:request requestType:POST]; // response handled by handleListCreateServiceResponse method below
    }
    else{
        //[textField becomeFirstResponder];
        TextDialog *dialog = [[TextDialog alloc] initWithView:View title:@"Input Error" message:[NSString stringWithFormat:@"List Name must contain at least 4 characters."]];
        [dialog show];
    }
    [_textDialog close];
}
- (void)noCreateNewList
{
    enableEditMode=FALSE;
    [_textDialog close];
    [shopping_list_add_new resignFirstResponder];
    //[_shoppingListNameTable reloadData];
    
    [self setActiveList];
    //[self viewDidAppear:NO];
}
-(void)touchShoppingListContainer{
    //    if(enableEditMode ==TRUE){
    //        [shopping_list_add_new resignFirstResponder];
    //
    //        _textDialog = [[TextDialog alloc] initWithView:View title:@"Create New List" message:@"Do you want to save the list?" responder:self leftCallback:@selector(yesCreateNewList) rightCallback:@selector(noCreateNewList)];
    //        [_textDialog show];
    //        return;
    //    }
}
#pragma mark textfield delegates

- (void)keyboardWillShow:(NSNotification *)sender
{
    [self hideMenus];
    if(current_active_menu!=nil){
        if(current_active_menu == _listMenu){
            
            if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){  // IOS 7 and above
                
                
                CGSize kbSize = [[[sender userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue].size;
                NSTimeInterval duration = [[[sender userInfo] objectForKey:UIKeyboardAnimationDurationUserInfoKey] doubleValue];
                [UIView animateWithDuration:duration animations:^{
                    UIEdgeInsets  edgeInsets = UIEdgeInsetsMake(0, 0, kbSize.height, 0);
                    [_shoppingListNameTable setContentInset:edgeInsets];
                    [_shoppingListNameTable setScrollIndicatorInsets:edgeInsets];
                }];
                
            }
            else{
                
                
                CGSize kbSize = [[[sender userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue].size;
                NSTimeInterval duration = [[[sender userInfo] objectForKey:UIKeyboardAnimationDurationUserInfoKey] doubleValue];
                [UIView animateWithDuration:duration animations:^{
                    
                    CGFloat c_height = _shoppingListNameTable.contentSize.height;
                    
                    CGFloat f_height = _shoppingListNameTable.frame.size.height;
                    
                    if(f_height - c_height < 200){
                        CGPoint offset = CGPointMake(0, _shoppingListNameTable.contentSize.height -     _shoppingListNameTable.frame.size.height);
                        
                        [_shoppingListNameTable setContentOffset:offset animated:YES];
                        
                        CGRect newFrame = _shoppingListNameTable.frame;
                        
                        newFrame.origin.y = -kbSize.height;
                        [_shoppingListNameTable setFrame:newFrame];
                        
                    }
                    
                }];
            }
        }
    }
}

- (void)keyboardWillHide:(NSNotification *)sender
{
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){  // IOS 7 and above
        NSTimeInterval duration = [[[sender userInfo] objectForKey:UIKeyboardAnimationDurationUserInfoKey] doubleValue];
        
        [UIView animateWithDuration:duration animations:^{
            UIEdgeInsets edgeInsets = UIEdgeInsetsZero;
            [_shoppingListNameTable setContentInset:edgeInsets];
            [_shoppingListNameTable setScrollIndicatorInsets:edgeInsets];
        }];
        
    }
    else{
        NSTimeInterval duration = [[[sender userInfo] objectForKey:UIKeyboardAnimationDurationUserInfoKey] doubleValue];
        
        [UIView animateWithDuration:duration animations:^{
            UIEdgeInsets edgeInsets = UIEdgeInsetsZero;
            [_shoppingListNameTable setContentInset:edgeInsets];
            [_shoppingListNameTable setScrollIndicatorInsets:edgeInsets];
            
            CGRect newFrame = _shoppingListNameTable.frame;
            
            newFrame.origin.y = 0;
            [_shoppingListNameTable setFrame:newFrame];
        }];
    }
}

# pragma mark - Touch

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [self removeMenus];
}

//-(void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event{
//    [self removeMenus];
//}
//
//-(void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event{
//    [self removeMenus];
//}
//
//-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event{
//    [self removeMenus];
//}




# pragma mark - Overlay screens
-(void)Show_overlay:(id)sender // Help button pressed
{
    if(current_active_menu == _productMenu)
    {
        current_screen_index_value=0;
        [self Overlay_screens:0];
    }
    else if(current_active_menu == _offerMenu)
    {
        current_screen_index_value=1;
        [self Overlay_screens:1];
    }
    else if(current_active_menu == _listMenu)
    {
        current_screen_index_value=2;
        [self Overlay_screens:2];
    }
    [self hideMoreMenu];
}

-(void)Overlay_for_first_time // Displayed When screen appeared at first time
{
    if(_isListsPage == YES){
        current_screen_index_value=2;
        [self Overlay_screens:2];
        _isListsPage=NO;
    }
    
}

-(void)Overlay_screens:(int)current_screen_index
{
    UIImage *img;
    NSMutableArray *img_array;
    if(Retina4){
        img_array=[[NSMutableArray alloc]initWithObjects:
                   [UIImage imageNamed:@"lists_overlay-586"],
                   [UIImage imageNamed:@"map_my_store_overlay-586"],nil];
        
    }
    else{
        img_array=[[NSMutableArray alloc]initWithObjects:
                   [UIImage imageNamed:@"lists_overlay"],
                   [UIImage imageNamed:@"map_my_store_overlay"],nil];
    }
    
    switch (current_screen_index_value) {
        case 1:
            //bg_color=[UIColor greenColor]; //Available offer page
            [self remove_overlay];
            return;
            break;
        case 2:
            // bg_color=[UIColor redColor]; // Lists page
            img=[img_array objectAtIndex:0];
            break;
        case 3:
            //bg_color=[UIColor orangeColor]; // Lists details page
            img=[img_array objectAtIndex:0];
            break;
            
        default:
            [self remove_overlay];
            return;
            //bg_color=[UIColor blackColor]; // product list page
            break;
            
    }
    
    CGFloat y_val=0.0;
    if([[[UIDevice currentDevice] systemVersion] floatValue] >= 7){
        y_val=0.0f;
    }
    else{
        y_val=-20.0f;
    }
    overlay_imgview=[[UIImageView alloc]initWithFrame:CGRectMake(0,y_val, _app._viewWidth, _app._viewHeight+20.0f)];
    //    [overlay_imgview setBackgroundColor:bg_color];
    overlay_imgview.userInteractionEnabled=YES;
    overlay_imgview.image=img;
    
    UITapGestureRecognizer *dismiss_overlay=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(remove_overlay)];
    dismiss_overlay.numberOfTouchesRequired=1;
    dismiss_overlay.numberOfTapsRequired=1;
    
    [overlay_imgview addGestureRecognizer:dismiss_overlay];
    
    [self.view addSubview:overlay_imgview];
}

-(void)remove_overlay
{
    [overlay_imgview removeFromSuperview];
}

@end
