//
//  ShoppingScreenVC
//  Raley's
//
//  Created by Billy Lewis on 9/30/13.
//  Copyright (c) 2013 Raleys. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "BaseScreenVC.h"
#import "ListsModalView.h"
#import "Login.h"
#import "Product.h"
#import "ProductCategory.h"
#import "ProductCategoryButton.h"
#import "ShoppingMenu.h"
#import "ShoppingScreenDelegate.h"
#import "EcartScreenVC.h"
#import "AccountScreenVC.h"
#import "StoreLocatorScreenVC.h"
#import "WebService.h"
#import "ZBarSDK.h"
//#import "CollectionViewLayout.h"
#import "ShoppingListCell.h"
#import "ShoppingDetailsCell.h"
#import "ShoppingListCell.h"
#import "ShoppingListEditCell.h"
#import "ShoppingListAddNewCells.h"
#import <ZXingObjC.h>


#define PRODUCT_CATEGORIES 0
#define PROMO_CATEGORIES 1


@interface ShoppingScreenVC : BaseScreenVC <UITableViewDataSource, UITableViewDelegate, UICollectionViewDataSource, UICollectionViewDelegate, UITextFieldDelegate, ShoppingScreenDelegate, WebServiceListener, ZBarReaderViewDelegate,UIScrollViewDelegate,ZXCaptureDelegate,ShoppingDetailsCellDelegate>
{
    int       _scrollViewYOffset;
    int       _selectedButtonYOrigin;
    int       _restoreContentHeight;
    int       _categoryButtonHeight;
    int       _scannerBackgroundWidth;
    int       _scannerBackgroundHeight;
    int       _listNameTableCellHeight;
    int       _personalizedOffersListIndexPath;
    int       _extraFriendzyOffersListIndexPath;
    int       _moreForYouOffersListIndexPath;
    int       _acceptedOffersListIndexPath;
    int       _currentProductPage;
    int       _productPageCount;
    int       _productsInCategoryCount;
    int       _categoryType;
    int       _currentProductIndex;
    BOOL      _offersShown;
    BOOL      _validatingEcartList;
    CGRect    _readerFrame;
    CGRect    _menuVisibleFrame;
    CGRect    _menuHiddenFrame;
    CGRect    _childViewVisibleFrame;
    CGRect    _childViewHiddenFrame;
    CGPoint   _restorePoint;
    NSString  *_barCode;
    NSString  *_currentCategoryKey;
    NSString  *_currentCategoryName;
    NSString  *_currentSearchText;
    NSArray                     *_shoppingListNameArray;
    NSMutableArray                     *_currentPageProductList;
    NSMutableArray              *_offerListsArray;
    NSMutableArray              *_productByAisleArray;
    NSMutableArray              *_multiplePageProductList;
    UIColor                     *_categoryButtonTextColor;
    UILabel                     *_scannerStatusLabel;
    UILabel                     *_productHeaderLabel;
    UILabel                     *_productCountTopLabel;
    UILabel                     *_productCountBottomLabel;
    UIButton                    *_moreButton;
    UIButton                    *_ecartButton;
    UIButton                    *_productsButton;
    UIButton                    *_listsButton;
    UIButton                    *_offersButton;
    UIButton                    *_forwardButton;
    UIButton                    *_reverseButton;
    UIView                      *_scannerView;
    UIView                      *_ecartView;
    UIImage                     *_categoryButtonImage;
    UIImageView                 *_searchTextBackground;
    UIView                      *_menuView;
    UIView                      *_offersView;
    UIView                      *_productView;
    UIView                      *_shoppingListView;
    UIView                      *_categoryView;
    UIView                      *product_page_search_view;
    UIScrollView                *_categoryMenu;
    UITextField                 *_searchTextField;
    UITableView                 *_shoppingListNameTable;
    UICollectionView            *_offerGrid;
//    CollectionViewLayout  *_offerGridLayout;
    UICollectionViewFlowLayout  *_offerGridLayout;
    UICollectionView            *_productGrid;
//    CollectionViewLayout  *_productGridLayout;
    UICollectionViewFlowLayout  *_productGridLayout;
    UICollectionView            *_shoppingListGrid;
    UICollectionViewFlowLayout  *_shoppingListGridLayout;
    UITapGestureRecognizer      *_tapRecognizer;
    OffsetTextField             *_listNameText;
    ListsModalView         *_listsModalView;
    Login                  *_login;
    ProductCategory        *_currentProductCategory;
    ProductCategoryButton  *_selectedButton;
    ShoppingMenu           *_productMenu;
    ShoppingMenu           *_offerMenu;
    ShoppingMenu           *_listMenu;
    ShoppingMenu           *_mapMenu;
    WebService             *_service;
    ZBarReaderView         *_reader;
    
    UIView      *img_alert_container;
    UIImageView *alert_image;
    
    UIImageView *overlay_imgview;
    int current_screen_index_value;
    
    UITextField *globalMainSearch;
    
    int addType;
}

//- (void)acceptOffer:(Offer *)offer;

@end
