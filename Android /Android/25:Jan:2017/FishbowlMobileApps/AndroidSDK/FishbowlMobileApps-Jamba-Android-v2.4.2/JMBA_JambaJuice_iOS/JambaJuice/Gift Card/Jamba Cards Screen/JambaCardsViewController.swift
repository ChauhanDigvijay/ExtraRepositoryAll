//
//  CheckPhysicalCardBalanceViewController.swift
//  Gift Card
//
//  Created by vThink on 19/05/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK

import SVProgressHUD
import HDK



typealias getOrRefreshGiftCardsCallBack = (_ error:NSError?) -> Void

class JambaCardsViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, UICollectionViewDataSource, UIScrollViewDelegate,JambaGiftCardsCellDelegate, AddNewGiftCardViewControllerDelegate, ShowBarCodeForGiftCardViewControllerDelegate,ExistingGiftCardDetailsViewControllerDelegate {
    
    enum SegueIdentifiers:String {
        case showBarcodeScreen = "showBarCodeVC"
        case showGiftCardManageScreen = "showGiftCardManage"
        case showCardDetailScreen = "CardDetailsVC"
        case showCreateCardScreen = "createNewCard"
        case showAddExistingCardScreen = "AddExistingGiftCardSegue"
    }
    
    enum buttonPressed:Int {
        case plusButton    = 1
        case addOrBuyButton   = 2
        case emptyCardPlusButton     = 3
        case none     = 0
    }
    
    // MARK: Referencing Outlet
    @IBOutlet var carousel                              :UIView!
    @IBOutlet weak var collectionView                   :UICollectionView!
    @IBOutlet weak var pageControl                      :UIPageControl!
    @IBOutlet weak var balanceLabel                     :UILabel!
    @IBOutlet weak var balanceUpdatedTimeLabel          :UILabel!
    @IBOutlet weak var reloadButtonTopSpace             :NSLayoutConstraint!
    @IBOutlet weak var pageControlTopSpace              :NSLayoutConstraint!
    @IBOutlet weak var carouselView                     :UIView!
    @IBOutlet weak var balanceView                      :UIView!
    @IBOutlet weak var balanceHorizontalViewHeight      :NSLayoutConstraint!
  //  @IBOutlet weak var balanceUpdatedTimeLabelTopSpace  :NSLayoutConstraint!
    @IBOutlet weak var balanceTextLabelHeight           :NSLayoutConstraint!
    @IBOutlet weak var emptyCardView                    :UIView!
    
    @IBOutlet weak var addOrBuyButton : UIButton!
    @IBOutlet weak var plusButton: UIButton!
    @IBOutlet weak var emptyCardPlusButton:UIButton!
    let headerHeight = CGFloat(80.0)
    var inCommUserCards:[InCommUserGiftCard]? {
        get{
            return GiftCardCreationService.sharedInstance.inCommUserGiftCards
        }
    }
    var plusButtonPressed       :Bool = false
    var selectedCardIndex       :Int!
    var currentCardIndex        :Int  = 0
    var maxPagesInPageControl         = 10
    var totalPages                    = 0;
    var lastPages                     = 0;
    var moveCardToPosition            = false
    var plusButtonValue: buttonPressed = JambaCardsViewController.buttonPressed(rawValue: buttonPressed.none.rawValue)!
    var retryCount = 0
    
    override func viewDidLoad() {
        //ui alignment for 4s
        if self.view.frame.height == 480 {
            pageControlTopSpace.constant = -5
        } else {
            balanceHorizontalViewHeight.constant = balanceHorizontalViewHeight.constant + 20
          //  balanceUpdatedTimeLabelTopSpace.constant = 10
            reloadButtonTopSpace.constant = 10
        }
        
        //during initial load. show the empty screen
        if inCommUserCards != nil {
            showGiftCards()
        } else {
            self.emptyCardView.isHidden = false
            self.plusButton.isHidden = false
            self.balanceView.isHidden = true
            self.carousel.isHidden = true
        }
        
        self.preStatusValidationCheck(false)
        
        // Hide basket flag icon from the window
        BasketFlagViewController.sharedInstance.hideFlag()
        
        NotificationCenter.default.addObserver(self, selector: #selector(refreshGiftCardFromArray), name: NSNotification.Name(rawValue: GiftCardAppConstants.refreshGiftCardHomePage), object: nil)
        
         NotificationCenter.default.addObserver(self, selector: #selector(refreshGiftCardBalance), name: NSNotification.Name(rawValue: GiftCardAppConstants.giftCardBalanceReloaded), object: nil)
        
          NotificationCenter.default.addObserver(self, selector: #selector(jambaGiftCardRemoved), name: NSNotification.Name(rawValue: GiftCardAppConstants.giftCardRemoved), object: nil)
        
        let triggerTime = (Int64(NSEC_PER_SEC) * 0)
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double(triggerTime) / Double(NSEC_PER_SEC), execute: { () -> Void in
            self.updateFrames()
        })
        
        super.viewDidLoad()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    //MARK: - Update frames
    func updateFrames() {
        //collection view heig
        let screenSize = collectionView.bounds // UIScreen.mainScreen().bounds
        let screenshotsCollectionViewFlowLayout = collectionView.collectionViewLayout as! UICollectionViewFlowLayout
        let maxwidth = screenSize.width / GiftCardAppConstants.GiftCardRatioWidth
        let maxheight = maxwidth * GiftCardAppConstants.GiftCardRatioHeight
        
        var height = screenSize.height
        var width = height * 1.5894
        if width > maxwidth {
            width = maxwidth
            height = maxheight
        }
        screenshotsCollectionViewFlowLayout.itemSize = CGSize(width: width, height: height) //screenSize.height / 1.1
        screenshotsCollectionViewFlowLayout.minimumInteritemSpacing = GiftCardAppConstants.giftCardLeadingAndTrailingSpace
        screenshotsCollectionViewFlowLayout.minimumLineSpacing = GiftCardAppConstants.giftCardLeadingOrTrailingSpace
        let screenshotsSectionInset = ( screenSize.size.width - screenshotsCollectionViewFlowLayout.itemSize.width ) / 2  //screenSize.width / 4.0
        screenshotsCollectionViewFlowLayout.sectionInset = UIEdgeInsetsMake(0.0, screenshotsSectionInset, 0.0, screenshotsSectionInset)
    }
    
    // MARK: - IBAction methods
    @IBAction func payAtCounter(_ sender:UIButton){
        if (inCommUserCards![currentCardIndex].balance > 0) {
            performSegue(withIdentifier: SegueIdentifiers.showBarcodeScreen.rawValue, sender: nil)
        } else {
            //show alert
            presentOkAlert("Balance", message: "Gift card balance is $0. Please reload or select a different card.")
        }
    }
    
    @IBAction func close(){
        // Show the basket flag
        BasketFlagViewController.sharedInstance.showFlag()
        self.dismiss(animated: true, completion: nil)
    }
    
    // Adding a New/Existing card prompt
    @IBAction func plusButton(_ sender: UIButton){
        if sender == plusButton{
            plusButtonValue = JambaCardsViewController.buttonPressed(rawValue: buttonPressed.plusButton.rawValue)!
        }else if sender == addOrBuyButton {
            plusButtonValue = JambaCardsViewController.buttonPressed(rawValue: buttonPressed.addOrBuyButton.rawValue)!
        }else if sender == emptyCardPlusButton{
            plusButtonValue = JambaCardsViewController.buttonPressed(rawValue: buttonPressed.emptyCardPlusButton.rawValue)!
        }
        //check the brand details
        plusButtonPressed = true
        preStatusValidation()
    }
    
    //MARK: - Action sheet
    func showActionSheetAlert(){
        let alertController = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        
        let proceedAction = UIAlertAction(title: "Purchase New Card", style: .default, handler: {
            action in
            self.performSegue(withIdentifier: SegueIdentifiers.showCreateCardScreen.rawValue, sender: self)
        })
        alertController.addAction(proceedAction)
        
        let changeStoreAction = UIAlertAction(title: "Add Existing Card", style: .default, handler: {
            action in
            self.performSegue(withIdentifier: SegueIdentifiers.showAddExistingCardScreen.rawValue, sender: self)
        })
        alertController.addAction(changeStoreAction)
        
        var cancelAction = UIAlertAction()
        
        // For iPad Support validation
        if UIDevice.current.userInterfaceIdiom == .pad{
            switch plusButtonValue.rawValue{
            case buttonPressed.addOrBuyButton.rawValue:
                    alertController.popoverPresentationController?.sourceView = self.addOrBuyButton
                    alertController.popoverPresentationController?.sourceRect = self.addOrBuyButton.bounds
            break;
            case buttonPressed.plusButton.rawValue:
                    alertController.popoverPresentationController?.sourceView = self.plusButton
                    alertController.popoverPresentationController?.sourceRect = self.plusButton.bounds
                break
            case buttonPressed.emptyCardPlusButton.rawValue:
                    alertController.popoverPresentationController?.sourceView = self.emptyCardPlusButton
                    alertController.popoverPresentationController?.sourceRect = self.emptyCardPlusButton.bounds
                break
            default :
                break
            }
            cancelAction = UIAlertAction(title: "Cancel", style: .default, handler: {
                action in
                return
            })
        } else {
            cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: {
                action in
                return
            })
        }
        alertController.addAction(cancelAction)
        self.present(alertController, animated: true, completion: nil)
    }
    //MARK: - Latest balance
    @IBAction func getLatestBalanceForSpecificCard(){
        getLastBalance()
       /* SVProgressHUD.showWithStatus("Refreshing...",maskType: .Clear)
        getOrRefreshUserGiftCards { (error) in
            self.handleGetOrRefreshUserGiftCardsResponse(error)
        } */
    }
    
    // MARK: - Collection view
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if inCommUserCards == nil{
            return 0
        }
        return inCommUserCards!.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "JambaGiftCardCell", for: indexPath) as! JambaGiftCardsCell
        cell.applyCardImageAndIndex(inCommUserCards![indexPath.row].imageUrl,index: indexPath.row)
        cell.delegate = self
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        currentCardIndex = indexPath.row
        moveCardToOldPosition()
        pageControl.currentPage = pageControlCurrentPage(currentCardIndex)
    }
    
    //MARK: - Orientation
    override var shouldAutorotate : Bool {
        return false
    }
    
    override var supportedInterfaceOrientations : UIInterfaceOrientationMask {
        return UIInterfaceOrientationMask.portrait
    }
    
    //MARK: - Scroll view delegates
    //gift card scroll auto navigate to nearest card
    func scrollViewWillEndDragging(_ scrollView: UIScrollView, withVelocity velocity: CGPoint, targetContentOffset: UnsafeMutablePointer<CGPoint>) {
        let offsetX = Float(targetContentOffset.pointee.x) //+ offsetAdjustment
        let cfl = collectionView.collectionViewLayout as! UICollectionViewFlowLayout
        let contentSize = Float(cfl.itemSize.width + cfl.minimumLineSpacing)
        
        let item = round(Float(offsetX)/contentSize)
        currentCardIndex = Int(item);
        let newoffsetX = item * contentSize
        var frame:CGRect = scrollView.frame
        frame.origin = CGPoint(x: CGFloat(newoffsetX), y: scrollView.frame.origin.y)
        scrollView.scrollRectToVisible(frame, animated: true)
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let offsetX = Float(scrollView.bounds.origin.x) //+ offsetAdjustment
        let cfl = collectionView.collectionViewLayout as! UICollectionViewFlowLayout
        let contentSize = Float(cfl.itemSize.width + cfl.minimumLineSpacing)
        let item = round(Float(offsetX)/contentSize)
        currentCardIndex = Int(item);
        pageControl.currentPage = pageControlCurrentPage(currentCardIndex);
        balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommUserCards![currentCardIndex].balance)
        balanceUpdatedTimeLabel.text = getBalanceUpdatedDateAsString(inCommUserCards![currentCardIndex].refreshTime!)
        let newoffsetX = item * contentSize
        var frame:CGRect = scrollView.frame
        frame.origin = CGPoint(x: CGFloat(newoffsetX), y: scrollView.frame.origin.y)
        scrollView.scrollRectToVisible(frame, animated: true)
    }
    
    func cardDetailsButtonPressed(_ selectdCardIndex:Int!){
        selectedCardIndex = selectdCardIndex
        performSegue(withIdentifier: SegueIdentifiers.showCardDetailScreen.rawValue, sender: self)
    }
    
    //MARK: - Segue actions
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == SegueIdentifiers.showCardDetailScreen.rawValue {
            let destinationController = segue.destination as! UINavigationController
            let targetController = destinationController.topViewController as! CardDetailsViewController
            targetController.inCommUserCardId = inCommUserCards![selectedCardIndex].cardId
            targetController.screenIsPresented = true
            
        } else if segue.identifier == SegueIdentifiers.showGiftCardManageScreen.rawValue {
            let destinationController = segue.destination as! UINavigationController
            let targetController = destinationController.topViewController as! ManageGiftCardViewController
            targetController.inCommUserGiftCardId = inCommUserCards![currentCardIndex].cardId
        } else if segue.identifier == SegueIdentifiers.showBarcodeScreen.rawValue {
            let destinationController = segue.destination as! UINavigationController
            let targetController = destinationController.topViewController as! ShowBarCodeForGiftCardViewController
            targetController.inCommUserGiftCardId = inCommUserCards![currentCardIndex].cardId
            targetController.delegate = self
            
        } else if segue.identifier == SegueIdentifiers.showCreateCardScreen.rawValue {
            let destinationController = segue.destination as! UINavigationController
            let targetController = destinationController.topViewController as! AddNewGiftCardViewController
            targetController.delegate = self
        }
        else if segue.identifier == SegueIdentifiers.showAddExistingCardScreen.rawValue{
            let destinationController = segue.destination as! UINavigationController
            let targetController = destinationController.topViewController as! AddExistingGiftCardViewController
            targetController.existingCardViewControllerDelegate = self
        }
    }
    
    //MARK: - Page control methods
    // moving the page control to current page
    func pageControlCurrentPage(_ index:Int) -> Int {
        if ((index+1)>maxPagesInPageControl) {
            let curPage = maxPagesInPageControl
            return curPage
        } else {
            return Int(index%maxPagesInPageControl)
        }
    }
    
    //update UI based on jamba cards count
    func showGiftCards(){
        //if there is no gift card, then show empty gift card view
        if (self.inCommUserCards!.count  == 0) {
            self.emptyCardView.isHidden = false
            self.plusButton.isHidden = false
            self.balanceView.isHidden = true
            self.carousel.isHidden = true
            SVProgressHUD.dismiss()
            return
        }
        self.collectionView.reloadData()
        self.totalPages = self.inCommUserCards!.count
        
        if (self.totalPages > self.maxPagesInPageControl) {
            self.pageControl.numberOfPages = self.maxPagesInPageControl
            self.lastPages = self.totalPages - self.maxPagesInPageControl
        } else {
            self.pageControl.numberOfPages = self.totalPages
        }
        if (currentCardIndex >= self.inCommUserCards!.count ){
            currentCardIndex = 0
            self.moveCardToPosition = true
        }
        if (self.inCommUserCards!.count  > 0) {
            pageControl.currentPage = currentCardIndex
            self.emptyCardView.isHidden = true
             self.plusButton.isHidden = true
            self.balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,self.inCommUserCards![self.currentCardIndex].balance);
            self.balanceUpdatedTimeLabel.text = getBalanceUpdatedDateAsString(self.inCommUserCards![self.currentCardIndex].refreshTime!)
        }
        self.carouselView.isHidden = false
        self.balanceView.isHidden = false
        if (self.moveCardToPosition) {
            perform(#selector(JambaCardsViewController.moveCardToOldPosition), with: nil, afterDelay: 0.5)
        }
    }
    
    
    //move the position to the card which is already selected
    @objc func moveCardToOldPosition() {
        let cfl = collectionView.collectionViewLayout as! UICollectionViewFlowLayout
        let contentSize = Float(cfl.itemSize.width + cfl.minimumLineSpacing)
        balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommUserCards![currentCardIndex].balance)
        self.balanceUpdatedTimeLabel.text = getBalanceUpdatedDateAsString(self.inCommUserCards![currentCardIndex].refreshTime!)
        let newoffsetX = Float(currentCardIndex) * contentSize
        var frame:CGRect = collectionView.frame
        frame.origin = CGPoint(x: CGFloat(newoffsetX), y: collectionView.frame.origin.y)
        collectionView.scrollRectToVisible(frame, animated: true)
        _ = pageControlCurrentPage(currentCardIndex)
        moveCardToPosition = false
    }
    
    //MARK: - Get balance updated date time
    func getBalanceUpdatedDateAsString(_ date:Date) ->String {
        let formatter = DateFormatter()
        formatter.dateFormat = GiftCardAppConstants.LongDateFormat
        formatter.amSymbol = GiftCardAppConstants.AMSymbol
        formatter.pmSymbol = GiftCardAppConstants.PMSymbol
        
        return formatter.string(from: date)
    }
    
    //MARK: - Show bar code screen (Pay At Counter) delegate
    func amountUsedInPayAtCounter(_ inCommUserCard: InCommUserGiftCard) {
        var inCommUserGiftCard =  inCommUserCards![currentCardIndex]
        inCommUserGiftCard = inCommUserCard
        GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommUserGiftCard)
        balanceUpdatedTimeLabel.text = self.getBalanceUpdatedDateAsString(inCommUserCard.refreshTime!)
        balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommUserCard.balance);
    }
    
    //MARK: Reload screen delegate
    func refreshBalanceAmount() {
        SVProgressHUD.show(withStatus: "Loading...")
        SVProgressHUD.setDefaultMaskType(.clear)
        
        InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: inCommUserCards![currentCardIndex].cardId) { (giftCardBalance, error) in
            SVProgressHUD.dismiss()
            if(error != nil){
                if error?.code == GiftCardAppConstants.errorCodeInvalidUser{
                    InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                        if inCommUserStatus{
                            InCommUserGiftCardService.getGiftCardBalance(InCommUserConfigurationService.sharedInstance.inCommUserId, cardId: self.inCommUserCards![self.currentCardIndex].cardId) { (giftCardBalance, error) in
                                if error != nil{
                                    SVProgressHUD.dismiss()
                                    self.presentError(error)
                                    return
                                } else {
                                    //update latest information
                                    var inCommUserGiftCards = self.inCommUserCards
                                    self.balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,(giftCardBalance?.balance)!);
                                    inCommUserGiftCards![self.currentCardIndex].refreshTime = NSDate() as Date
                                    self.balanceUpdatedTimeLabel.text = self.getBalanceUpdatedDateAsString(inCommUserGiftCards![self.currentCardIndex].refreshTime!)
                                    inCommUserGiftCards![self.currentCardIndex].balance = (giftCardBalance?.balance)!
                                    GiftCardCreationService.sharedInstance.updateUserGiftCards(inCommUserGiftCards!)
                                }
                            }
                        }
                        else{
                            SVProgressHUD.dismiss()
                            self.presentError(error)
                            return
                        }
                    })
                } else {
                    SVProgressHUD.dismiss()
                    self.presentError(error)
                    return
                }
            } else {
                
                //update latest information
                var inCommUserGiftCards = self.inCommUserCards
                self.balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,(giftCardBalance?.balance)!);
                inCommUserGiftCards![self.currentCardIndex].refreshTime = NSDate() as Date
                self.balanceUpdatedTimeLabel.text = self.getBalanceUpdatedDateAsString(inCommUserGiftCards![self.currentCardIndex].refreshTime!)
                inCommUserGiftCards![self.currentCardIndex].balance = (giftCardBalance?.balance)!
                GiftCardCreationService.sharedInstance.updateUserGiftCards(inCommUserGiftCards!)
            }
        }
        moveCardToPosition = true
    }
    
    // MARK: - De alloc notification
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    // MARK: - ExistingGiftCardAdded Delegate
    func existingGiftCardAdded(_ inCommCardId: Int32) {
        SVProgressHUD.show(withStatus: "Refreshing...")
        SVProgressHUD.setDefaultMaskType(.clear)
        getOrRefreshUserGiftCards { (error) in
            if error == nil{
                self.collectionView.reloadData()
                self.plusButtonPressed = false
                self.currentCardIndex = GiftCardCreationService.sharedInstance.getUserCardIndex(inCommCardId)
                self.pageControl.currentPage = self.pageControlCurrentPage(self.currentCardIndex)
                self.moveCardToPosition = true
            }
            self.handleGetOrRefreshUserGiftCardsResponse(error)
        }
    }
    
    //MARK: - Pre status validation
    func preStatusValidation(){
        self.preStatusValidationCheck(true)
    }
    func preStatusValidationCheck(_ refresh:Bool){
        if InCommUserConfigurationService.sharedInstance.inCommUserId == nil{
            getUserId()
        }
        else if InCommGiftCardBrandDetails.sharedInstance.brand == nil{
            getInCommBrandDetails()
        }
        else if GiftCardCreationService.sharedInstance.inCommUserGiftCards == nil{
            resetGiftCardPosition()
            SVProgressHUD.show(withStatus: "Getting gift cards...")
            SVProgressHUD.setDefaultMaskType(.clear)
            self.getOrRefreshUserGiftCards({ (error) in
                self.handleGetOrRefreshUserGiftCardsResponse(error)
            })
        }
        else if plusButtonPressed{
            plusButtonPressed = false
            showActionSheetAlert()
        }
        else{
            resetGiftCardPosition()
            if (refresh==true || self.inCommUserCards?.count == 0) {
                SVProgressHUD.show(withStatus: "Refreshing...")
                SVProgressHUD.setDefaultMaskType(.clear)
                self.getOrRefreshUserGiftCards{ (error) in
                    self.handleGetOrRefreshUserGiftCardsResponse(error)
                }
            }
        }
    }
    
    //MARK: - Get user id
    func getUserId(){
        SVProgressHUD.show(withStatus: "Preloading...")
        SVProgressHUD.setDefaultMaskType(.clear)
        if GiftCardCreationService.sharedInstance.inCommAuthToken == nil{
            InCommUserConfigurationService.sharedInstance.inCommServiceConfiguration({ (inCommUserStatus, inCommError) in
                if inCommUserStatus{
                    self.getInCommUserId()
                }
                else{
                    SVProgressHUD.dismiss()
                    self.preloadDataValidationError(nil)
                }
            })
        }
        else{
            getInCommUserId()
        }
    }
    
    //MARK: - Get user id
    func getInCommUserId(){
        InCommUserConfigurationService.sharedInstance.getInCommUserId { (inCommUserId, error) in
            if error != nil{
                SVProgressHUD.dismiss()
                self.preloadDataValidationError(error)
            }
            else{
                self.getInCommBrandDetails()
            }
        }
    }
    
    //MARK: - Get incomm brand details
    func getInCommBrandDetails(){
        SVProgressHUD.show(withStatus: "Preloading...")
        SVProgressHUD.setDefaultMaskType(.clear)
        InCommGiftCardBrandDetails.sharedInstance.loadBrandDetails { (brand, error) in
            if error != nil{
                SVProgressHUD.dismiss()
                self.preloadDataValidationError(error)
            }
            else{
                SVProgressHUD.show(withStatus: "Getting gift cards...")
                SVProgressHUD.setDefaultMaskType(.clear)
                self.getOrRefreshUserGiftCards({ (error) in
                    self.handleGetOrRefreshUserGiftCardsResponse(error)
                })
            }
        }
    }
    
    // MARK: - Refresh gift cards
    @objc func refreshGiftCardFromArray() {
        balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommUserCards![currentCardIndex].balance)
        balanceUpdatedTimeLabel.text = getBalanceUpdatedDateAsString(inCommUserCards![currentCardIndex].lastModifiedDate!)
    }
    
    // MARK: Preload data validation error
    func preloadDataValidationError(_ error:NSError?){
        var errorMessage = ""
        if error == nil && retryCount == 0 {
            retryCount = 1
            self.preStatusValidation()
            return
        } else {
            if error == nil{
                errorMessage = "An Unexpected error occured while processing your request."
            }else{
                errorMessage = (error?.localizedDescription)!
            }
            self.presentConfirmation("Error", message: errorMessage, buttonTitle: "Retry") { (confirmed) in
                if confirmed{
                    self.preStatusValidation()
                }
                else{
                    return
                }
            }
        }
    }
    
    // MARK: - Handle get or refresh user gift cards response
    func handleGetOrRefreshUserGiftCardsResponse(_ error:NSError?){
        if error  == nil{
            if plusButtonPressed{
                showActionSheetAlert()
            }
             showGiftCards()
            return
        }
        self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry") {(confirmed) -> Void in
            if confirmed{
                self.preStatusValidation()
            }
            else{
                if (self.inCommUserCards != nil && self.inCommUserCards!.count > 0) {
                    self.carouselView.isHidden = false
                    self.balanceView.isHidden = false
                    self.emptyCardView.isHidden = true
                    self.plusButton.isHidden = true
                }
                else{
                    self.carouselView.isHidden = true
                    self.balanceView.isHidden = true
                    self.emptyCardView.isHidden = false
                    self.plusButton.isHidden = false
                }
            }
        }
    }
    
    // MARK: - Get/Refresh user gift cards
    func getOrRefreshUserGiftCards(_ callback:@escaping getOrRefreshGiftCardsCallBack){
        GiftCardCreationService.sharedInstance.getAllGiftCards { (userGiftCards, error) in
            SVProgressHUD.dismiss()
            return callback(error)
        }
    }
    
    // MARK: - Reset gift card position
    func resetGiftCardPosition(){
        plusButtonPressed = false
        currentCardIndex = 0
        pageControl.currentPage = 0
        moveCardToPosition = true
    }
    
    // MARK: - Removed gift card delegate
    func giftCardRemoved(){
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent("GIFT_CARD_REMOVED")
        resetGiftCardPosition()
        showGiftCards()
    }
    
    // MARK: - Get latest balance for specified card
    func getLastBalance(){
        let card = inCommUserCards![currentCardIndex]
        SVProgressHUD.show(withStatus: "Refreshing...")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService.sharedInstance.getLatestBalance(card.cardId) { (giftCardBalance, error) in
            SVProgressHUD.dismiss()
            if error != nil{
                self.presentError(error)
            }
            else{
                //update latest information
                var inCommUserGiftCard:InCommUserGiftCard = card
                inCommUserGiftCard.refreshTime = NSDate() as Date
                inCommUserGiftCard.balance = (giftCardBalance?.balance)!
                GiftCardCreationService.sharedInstance.updateUserGiftCard(inCommUserGiftCard)
                self.balanceUpdatedTimeLabel.text = self.getBalanceUpdatedDateAsString(inCommUserGiftCard.refreshTime!)
                self.balanceLabel.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommUserGiftCard.balance);
            }
        }
    }
    
    @objc func refreshGiftCardBalance(){
        self.dismiss(animated: true, completion: nil)
        self.refreshBalanceAmount()
    }
    
    @objc func jambaGiftCardRemoved(){
        self.dismiss(animated: true, completion: nil)
        self.giftCardRemoved()
    }
    
    }



