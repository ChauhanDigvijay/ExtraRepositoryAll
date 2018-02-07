//
//  OfferDetailViewController.swift
//  JambaJuice
//
//  Created by Puneet  on 4/12/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import SwiftyJSON
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
    switch (lhs, rhs) {
    case let (l?, r?):
        return l < r
    case (nil, _?):
        return true
    default:
        return false
    }
}

// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
    switch (lhs, rhs) {
    case let (l?, r?):
        return l > r
    default:
        return rhs < lhs
    }
}



class OfferDetailViewController: UIViewController {
    
    var isFromBasket : Bool = false
    var isPresented : Bool = true
    @IBOutlet weak var lblwidthpromo: NSLayoutConstraint!
    
    //  @IBOutlet weak var lblDecHeight: NSLayoutConstraint!
    @IBOutlet weak var lblOfferTitle: UILabel!
    
    
    @IBOutlet weak var lblStoreAddress: UILabel!
    
    @IBOutlet weak var lblOfferDesc: UILabel!
    
    @IBOutlet weak var lblExpires: UILabel!
    
    @IBOutlet weak var imgQRCode: UIImageView!
    @IBOutlet weak var onlineOrderNowButton: UIButton!
    @IBOutlet weak var imgQRCodeTopConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var applyButton: UIButton!
    
    @IBOutlet weak var lblPromoCdode: UILabel!
    
    @IBOutlet weak var moreInfo: UIButton!
    
    @IBOutlet weak var backgroundView:UIView!
    
    @IBOutlet weak var viewHeightContraint: NSLayoutConstraint!
    
    @IBOutlet weak var tryAgainButton:UIButton!
    
    
    
    @IBOutlet weak var onLineOfferView: UIView!
    
    @IBOutlet weak var onlineViewMoreButton: UIButton!
    
    @IBOutlet weak var onlineViewStoreAddress: UILabel!
    
    @IBOutlet weak var onlineOfferDesc: UILabel!
    @IBOutlet weak var onLineViewOffervalid: UILabel!
    
    @IBOutlet weak var onLineViewOfferTitle: UILabel!
    
    var offerDetail: FishbowlOffer?
    
    var offerSummary : FishbowlOfferSummary?
    
    var promoCode = ""
    
    var qrcodeImage: CIImage!
    
    
    @IBAction func btnCloseClicked(_ sender: AnyObject) {
        
        self.dismissModalController()
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NotificationCenter.default.addObserver(self, selector: #selector(OfferDetailViewController.fishbowlOfferDetailCallback(_:)), name: NSNotification.Name(rawValue: "offerDetail"), object: nil)
        configureNavigationBar(.orange)
        //if offer detail is avilable preload the data
        
        if OfferService.onLineInstore.onlineonly.rawValue == offerDetail!.onlineInStore {
            // Check its from basket
            if isFromBasket{
                instoreOfferview()
            }else{
                onLineOfferViewOrder()
            }
            
        } else {
            instoreOfferview()
        }
        
        // Hide apply button and all
        // Do any additional setup after loading the view.
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
        updateScreen()
        
    }
    
    
    func instoreOfferview(){
        backgroundView.isHidden = false
        onLineOfferView.isHidden = true
        
        if offerDetail != nil {
            fillBasicOfferDetails()
            self.updatePromoCodeDetails(offerDetail?.promotionCode)
        }
    }
    
    
    //fill basic offer details
    func fillBasicOfferDetails() {
        if offerDetail?.offerTitle.characters.count > 0 {
            lblOfferTitle.text = offerDetail?.offerTitle
        }
        
        var preferredStore:Store?
        
        if isFromBasket {
            preferredStore = CurrentStoreService.sharedInstance.currentStore
        } else {
            preferredStore = UserService.sharedUser?.favoriteStore
        }
        
        let storeDetails = filterStore(offerDetail!, store: preferredStore)
        print("\(storeDetails.0) \(storeDetails.1)")
        
        if storeDetails.1 > 1 {
            moreInfo.isHidden = false
            imgQRCodeTopConstraint.constant = 50
        } else {
            moreInfo.isHidden = true
            imgQRCodeTopConstraint.constant = 10
        }
        
        lblStoreAddress.text =  "\(storeDetails.0.storename!) \n \((storeDetails.0.storeAddress!))"
        if offerDetail?.notificationContent.characters.count > 0
        {
            lblOfferDesc.text = offerDetail?.notificationContent
        }
        else
        {
            if offerDetail?.desc.characters.count > 0{
                lblOfferDesc.text = offerDetail?.desc
            }else{
                lblOfferDesc.text = ""
            }
        }
        
        if offerDetail?.offerValidity.characters.count >  0
        {
            lblExpires.text = offerDetail?.offerValidity.convertStringToDate((offerDetail?.offerValidity)!)
        }
        else
        {
            lblExpires.text = "Never Expires"
        }
    }
    
    func updateScreen() {
        
        let height:CGFloat = isFromBasket ? 270 : 180   //270 apply button show
        viewHeightContraint.constant = lblOfferDesc.bounds.height + imgQRCode.bounds.height + height
        setButtonBorder(moreInfo)
        setButtonBorder(applyButton)
        setButtonBorder(tryAgainButton)
        
    }
    
    func setButtonBorder(_ button:UIButton)  {
        button.layer.masksToBounds = true
        button.layer.borderWidth = 1
        if button.title(for: UIControlState()) == "More Info" {
            button.layer.borderColor = UIColor(hex: 0xCCCCCC).cgColor
        } else if button.title(for: UIControlState()) == "Try Again" {
            button.layer.borderColor = UIColor(hex: 0xC41348).cgColor
            button.layer.cornerRadius = 5
        }else {
            button.layer.borderColor = UIColor(hex: Constants.jambaOrangeColor).cgColor
            button.layer.cornerRadius = 5
        }
    }
    
    func fishbowlOfferDetailCallback(_ notification: Notification) {
        SVProgressHUD.dismiss()
        if notification.userInfo == nil {
            self.presentOkAlert("Error", message: "Sorry something Went Wrong")
            return
        }
        if let result = notification.userInfo?["successFlag"] as? Bool {
            if result {
                if let offersDetail = notification.userInfo {
                    self.offerSummary = FishbowlOfferSummary(json: JSON(offersDetail))
                    self.offerDetail = self.offerSummary!.offerList[0]
                    if self.offerDetail?.offerTitle.characters.count > 0
                    {
                        self.lblOfferTitle.text = self.offerDetail?.offerTitle
                    }
                    
                    var preferredStore:Store?
                    
                    if isFromBasket {
                        preferredStore = CurrentStoreService.sharedInstance.currentStore
                    } else {
                        preferredStore = UserService.sharedUser?.favoriteStore
                    }
                    
                    let storeDetails = filterStore(offerDetail!, store: preferredStore)
                    print("\(storeDetails.0) \(storeDetails.1)")
                    
                    if storeDetails.1 > 1 {
                        moreInfo.isHidden = false
                        imgQRCodeTopConstraint.constant = 50
                    } else {
                        moreInfo.isHidden = true
                        imgQRCodeTopConstraint.constant = 10
                    }
                    
                    //                    if UserService.sharedUser?.favoriteStore?.name.characters.count > 0
                    //                    {
                    
                    //var myMutableString = NSMutableAttributedString()
                    
                    //                        self.lblStoreAddress.text =  "Available Store: \((UserService.sharedUser?.favoriteStore?.name)!) \((UserService.sharedUser?.favoriteStore?.street)!) \((UserService.sharedUser?.favoriteStore?.city)!) \((UserService.sharedUser?.favoriteStore?.state)!) \((UserService.sharedUser?.favoriteStore?.zip)!)"
                    
                    self.lblStoreAddress.text =  "\(storeDetails.0.storename!)\n\(storeDetails.0.storeAddress!)"
                    
                    //myMutableString = NSMutableAttributedString(string: self.lblStoreAddress.text!, attributes: [NSFontAttributeName:UIFont.init(name: "Helvetica", size: 13.0)!])
                    
                    //myMutableString.addAttribute(NSForegroundColorAttributeName, value: UIColor(hex: Constants.jambaPinkColor), range: NSRange(location:0,length:16))
                    
                    // set label Attribute
                    
                    //self.lblStoreAddress.attributedText = myMutableString
                    //                    }
                    if self.offerDetail?.notificationContent.characters.count > 0
                    {
                        
                        self.lblOfferDesc.text = self.offerDetail?.notificationContent
                    }
                    else
                    {
                        if self.offerDetail?.desc.characters.count > 0{
                            self.lblOfferDesc.text = self.offerDetail?.desc
                        }else{
                            self.lblOfferDesc.text = ""
                        }
                        
                    }
                    
                    if self.offerDetail?.offerValidity.characters.count > 0
                    {
                        self.lblExpires.text = self.offerDetail?.offerValidity.convertStringToDate((self.offerDetail?.offerValidity)!)
                    }
                    else
                    {
                        self.lblExpires.text = "Never Expires"
                    }
                    if offerDetail?.promotionCode != nil && (offerDetail?.promotionCode.isEmpty == false){
                        self.updatePromoCodeDetails(offerDetail?.promotionCode)
                    }
                }
            } else {
                self.presentOkAlert("Error", message: "Sorry something Went Wrong")
            }
        } else {
            self.presentOkAlert("Error", message: "Sorry something Went Wrong")
        }
    }
    
    
    @IBAction func tryAgainButtonPressed(){
        
    }
    
    func displayQRCodeImage() {
        let scaleX = imgQRCode.frame.size.width / qrcodeImage.extent.size.width
        let scaleY = imgQRCode.frame.size.height / qrcodeImage.extent.size.height
        
        let transformedImage = qrcodeImage.applying(CGAffineTransform(scaleX: scaleX, y: scaleY))
        
        imgQRCode.image = UIImage(ciImage: transformedImage)
        
        //update view size based on image
        self.updateScreen()
    }
    // MARK: Private
    fileprivate func dismiss() {
        
        self.view.removeFromSuperview()
        
        _ = self.navigationController?.popViewController(animated: true)
        
    }
    func onLineOfferViewOrder(){
        backgroundView.isHidden =  true
        onLineOfferView.isHidden = false
        // For online offer description
        if offerDetail?.notificationContent.characters.count > 0
        {
            onlineOfferDesc.text = offerDetail?.notificationContent
        }
        else
        {
            if offerDetail?.desc.characters.count > 0{
                onlineOfferDesc.text = offerDetail?.desc
            }else{
                onlineOfferDesc.text = ""
            }
        }
        onLineViewOfferTitle.text = offerDetail?.offerTitle
        setButtonBorder(onlineViewMoreButton)
        
        var preferredStore:Store?
        if isFromBasket {
            preferredStore = CurrentStoreService.sharedInstance.currentStore
        } else {
            preferredStore = UserService.sharedUser?.favoriteStore
        }
        
        let storeDetails = filterStore(offerDetail!, store: preferredStore!)
        print("\(storeDetails.0) \(storeDetails.1)")
        onlineViewStoreAddress.text = "\(storeDetails.0.storename!) \n \((storeDetails.0.storeAddress!))"
        
        if offerDetail?.offerValidity.characters.count >  0
        {
            
            onLineViewOffervalid.text = offerDetail?.offerValidity.convertStringToDate((offerDetail?.offerValidity)!)
        }
        else
        {
            onLineViewOffervalid.text = "Never Expires"
        }
        if storeDetails.1 > 1 {
            onlineViewMoreButton.isHidden = false
        } else {
            onlineViewMoreButton.isHidden = true
        }
        onlineViewMoreButton.layer.masksToBounds = true
        onlineViewMoreButton.layer.borderWidth = 1
        onlineViewMoreButton.layer.borderColor = UIColor(hex: 0xCCCCCC).cgColor
    }
    
    @IBAction func orderNowButtonPressed(_ sender: UIButton) {
        SignedInMainViewController.sharedInstance().onLineOfferOrder()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        tryAgainButton.isHidden = true
        if isFromBasket {
            if BasketService.sharedBasket?.promotionCode == offerDetail!.promotionCode && BasketService.sharedBasket?.discount != 0 {
                applyButton.setTitle("Remove", for: UIControlState())
            }
            else{
                applyButton.setTitle("Apply", for: UIControlState())
            }
        } else {
            applyButton.isHidden = true
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // Remove observer
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
    // Fishbowl promo code
    func updatePromoCodeDetails(_ fbPromoCode:String?){
        if fbPromoCode == nil || fbPromoCode!.isEmpty{
            self.lblPromoCdode.isHidden = true
            return
        }
        else {
            self.tryAgainButton.isHidden = true
            if isFromBasket == true{
                self.applyButton.isHidden = false
            }else{
                self.applyButton.isHidden = true
            }
            self.lblPromoCdode.isHidden = false
            self.promoCode = fbPromoCode!
            offerDetail!.promotionCode = fbPromoCode!
            
            // If promocode is numeric
            var formattedPromoCode = fbPromoCode!
            if validatePromoCode(formattedPromoCode){
                formattedPromoCode = FishbowlApiClassService.sharedInstance.promoCodeFormat(fbPromoCode!)
            }
            self.lblPromoCdode.text = "Promo code: \(formattedPromoCode)"
            
            
            
            if fbPromoCode!.characters.count > 0 {
                let data = fbPromoCode!.data(using: String.Encoding.isoLatin1, allowLossyConversion: false)
                
                let filter = CIFilter(name: "CIQRCodeGenerator")// // CICode128BarcodeGenerator
                
                filter!.setValue(data, forKey: "inputMessage")
                filter!.setValue("Q", forKey: "inputCorrectionLevel")
                
                self.qrcodeImage = filter!.outputImage
                
                self.displayQRCodeImage()
            }else{
                return
            }
        }
    }
    
    
    //get store info
    func filterStore(_ offer:FishbowlOffer, store:Store?) -> (FishbowlOfferStore,Int) {
        //display as all store
        if store == nil && offer.storeRestriction.count == 0 {
            let offerStore:FishbowlOfferStore = FishbowlOfferStore(storename: "All Stores", displayname: "", storecode: "", storeid: "", storeAddress: "")
            return (offerStore,0)
        }
        
        //get the preferred or current store detail
        if offer.storeRestriction.count > 0 {
            var fbFilteredStores:[FishbowlOfferStore] = []
            fbFilteredStores = offer.storeRestriction.filter( { $0.storecode == store!.storeCode })
            if fbFilteredStores.count > 0 {
                fbFilteredStores[0].storeAddress = store!.address
                return (fbFilteredStores[0],0)
                
                //get the First store list from Store restriction
            } else {
                let offerStore:FishbowlOfferStore = FishbowlOfferStore(storename: "Tap More Info to see stores", displayname: offer.storeRestriction[0].displayname, storecode: offer.storeRestriction[0].storecode, storeid: offer.storeRestriction[0].storeid, storeAddress: "")
                return (offerStore,2)
            }
            
            //get the preferred store/current store detail
        } else {
            let offerStore:FishbowlOfferStore = FishbowlOfferStore(storename: store!.name, displayname: store!.name, storecode: store!.storeCode, storeid: "", storeAddress: store!.address)
            return (offerStore,0)
        }
    }
    
    //MARK:- segue
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ShowOfferStoreVC" {
            let vc = segue.destination as! ShowOfferStoreViewController
            vc.offerDetail = offerDetail
        }
    }
    
    //MARK:- Button Actions
    @IBAction func applyRemovePromoCode(_ sender:UIButton) {
        if sender.title(for: UIControlState()) == "Apply" {
            if BasketService.validateBasketForRewardAndOffer(){
                self.presentConfirmationWithYesOrNo("Alert", message: "Only one coupon / reward may be applied at a time. Do you want to remove the existing coupon / reward and apply a new one?", buttonTitle: "Yes") { (confirmed) in
                    if confirmed{
                        if BasketService.validateAppliedReward(){
                            SVProgressHUD.show(withStatus: "Applying promotion code...")
                            SVProgressHUD.setDefaultMaskType(.clear)
                            BasketService.removeRewards({ (basket, error) in
                                if error != nil{
                                    SVProgressHUD.dismiss()
                                    self.presentError(error)
                                }else{
                                    self.applyPromotionCode()
                                }
                            })
                        } else if BasketService.validateAppliedOffer(){
                            SVProgressHUD.show(withStatus: "Applying promotion code...")
                            SVProgressHUD.setDefaultMaskType(.clear)
                            BasketService.removePromotionCode({ (basket, error) in
                                if error != nil{
                                    SVProgressHUD.dismiss()
                                    self.presentError(error)
                                }else{
                                    self.applyPromotionCode()
                                }
                            })
                        }
                    } else{
                        return
                    }
                }
            }
            else{
                self.applyPromotionCode()
            }
        } else if sender.titleLabel?.text == "Remove" {
            // Fishbowl Event
            var itemId = ""
            var itemName = ""
            if let offer = offerDetail{
                itemId = String(offer.id)
                itemName = offer.offerTitle
            }
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent(itemId, item_name: itemName, event_name: "REMOVE_OFFER")
            
            SVProgressHUD.show(withStatus: "Removing promotion code...")
            SVProgressHUD.setDefaultMaskType(.clear)
            BasketService.removePromotionCode({ (basket, error) in
                SVProgressHUD.dismiss()
                if error != nil {
                    self.presentError(error)
                    return
                }
                BasketService.sharedBasket?.offerId = nil
                self.popToRootViewController()
            })
        }
    }
    
    func validatePromoCode(_ promoString:String)->Bool{
        let regexText = NSPredicate(format: "SELF MATCHES %@", "^[0-9]+$")
        return regexText.evaluate(with: promoString)
    }
    
    // Validate applying promotion code
    
    func applyPromotionCode(){
        // Fishbowl Event
        var itemId = ""
        var itemName = ""
        if let offer = offerDetail{
            itemId = String(offer.id)
            itemName = offer.offerTitle
        }
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent(itemId, item_name: itemName, event_name: "APPLY_OFFER")
        
        SVProgressHUD.show(withStatus: "Applying promotion code...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.applyPromotionCode(self.promoCode) { (basket, error) -> Void in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            BasketService.sharedBasket?.offerId = (self.offerDetail!.isPMoffer) == true ?(self.offerDetail!.pmPromotionID):String(self.offerDetail!.id)
            
            self.popToRootViewController() // Close all the way to basket screen
        }
    }
    
}
