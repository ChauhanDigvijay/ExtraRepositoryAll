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


class OfferDetailViewController: UIViewController {
    
    let isFromBasket : Bool = false
    @IBOutlet weak var lblwidthpromo: NSLayoutConstraint!
    
    @IBOutlet weak var lblDecHeight: NSLayoutConstraint!
    @IBOutlet weak var lblOfferTitle: UILabel!
    
    
    @IBOutlet weak var lblStoreAddress: UILabel!
    
    @IBOutlet weak var lblOfferDesc: UILabel!
    
    @IBOutlet weak var lblExpires: UILabel!
    
    @IBOutlet weak var imgQRCode: UIImageView!
    
    @IBOutlet weak var btnApplyNow: UIButton!
    
    @IBOutlet weak var lblPromoCdode: UILabel!
    
    var offerDetail: ClpOffer?
    
    var offerSummary : ClpOfferSummary?
    
    var qrcodeImage: CIImage!
    
    
    @IBAction func btnCloseClicked(sender: AnyObject) {
        
        self.dismissModalController()
        
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(OfferDetailViewController.updateStuff), name: "appDidBecomeActive", object: nil)
        
        configureNavigationBar(.Orange)
        
        
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
        
        
        if appdelegate?.isReachable == true
        {
            
            if (offerDetail == nil)
            {
                ClpOfferService.getClpOffer((appdelegate?.offerId)!){ (offers : ClpOfferSummary?, error) -> Void in
                    if error != nil {
                        log.warning("WARNING: \(error?.localizedDescription)")
                        return
                    }
                    print("joe** response is here \(offers)")
                    self.offerSummary = offers
                    self.offerDetail = self.offerSummary!.offerList[0]
                    
                    
                    
                    if self.offerDetail?.offerTitle.characters.count > 0
                    {
                        self.lblOfferTitle.text = self.offerDetail?.offerTitle
                    }
                    if UserService.sharedUser?.favoriteStore?.name.characters.count > 0
                    {
                        
                        var myMutableString = NSMutableAttributedString()
                        
                        self.lblStoreAddress.text =  "Available Store: \((UserService.sharedUser?.favoriteStore?.name)!) \((UserService.sharedUser?.favoriteStore?.street)!) \((UserService.sharedUser?.favoriteStore?.city)!) \((UserService.sharedUser?.favoriteStore?.state)!) \((UserService.sharedUser?.favoriteStore?.zip)!)"
                        
                        
                        myMutableString = NSMutableAttributedString(string: self.lblStoreAddress.text!, attributes: [NSFontAttributeName:UIFont(name: "Helvetica", size: 13.0)!])
                        
                        myMutableString.addAttribute(NSForegroundColorAttributeName, value: UIColor(hex: Constants.jambaPinkColor), range: NSRange(location:0,length:16))
                        
                        // set label Attribute
                        
                        self.lblStoreAddress.attributedText = myMutableString
                    }
                    if self.offerDetail?.desc.characters.count > 0
                    {
                        self.lblOfferDesc.text = self.offerDetail?.desc
                    }
                    else
                    {
                        self.lblOfferDesc.text = ""
                        self.lblDecHeight.constant = 0;
                    }
                    
                    if self.offerDetail?.offerValidity.characters.count > 0
                    {
                        self.lblExpires.text = self.offerDetail?.offerValidity.convertStringToDate((self.offerDetail?.offerValidity)!)
                    }
                    else
                    {
                        self.lblExpires.text = "Never Expires"
                    }
                    
                    
                    let defaults = NSUserDefaults.standardUserDefaults()
                    if let customerId = defaults.stringForKey("Newmemberid")
                    {
                        SVProgressHUD.showWithStatus("Getting Promocode...", maskType: .Clear)
                        
                        ClpOfferService.getPromoCode(customerId ,offerId: (self.offerDetail?.isPMoffer)! == true ?(self.offerDetail?.pmPromotionID)!:String(self.offerDetail!.id),isPMIntegrated :(self.offerDetail?.isPMoffer)!){ (promoJson : clppromoSummary?, error) -> Void in
                            
                            SVProgressHUD.dismiss()
                            
                            if error != nil {
                                log.warning("WARNING: \(error?.localizedDescription)")
                                self.presentError(error)
                                
                                return
                            }
                            print("joe** response is here \(promoJson)")
                            if let promo = promoJson?.promoCode
                            {
                                if promo.isEmpty
                                {
                                    self.presentOkAlert("Alert", message: "No promocode avaialble for this offer")
                                    self.lblPromoCdode.hidden = true
                                    
                                }
                                else
                                {
                                    print(promo)
                                    
                                    self.lblPromoCdode.text = "Promo code : \(promo)";
                                    
                                    if promo.characters.count>0
                                    {
                                        let data = promo.dataUsingEncoding(NSISOLatin1StringEncoding, allowLossyConversion: false)
                                        
                                        let filter = CIFilter(name: "CIQRCodeGenerator")// // CICode128BarcodeGenerator
                                        
                                        filter!.setValue(data, forKey: "inputMessage")
                                        filter!.setValue("Q", forKey: "inputCorrectionLevel")
                                        
                                        self.qrcodeImage = filter!.outputImage
                                        
                                        self.displayQRCodeImage()
                                    }
                                    else
                                    {
                                        return
                                    }
                                }
                                
                            }
                        }
                        
                    }
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                }
            }
                
            else
            {
                
                if offerDetail?.offerTitle.characters.count > 0
                {
                    lblOfferTitle.text = offerDetail?.offerTitle
                }
                if UserService.sharedUser?.favoriteStore?.name.characters.count > 0
                {
                    
                    var myMutableString = NSMutableAttributedString()
                    
                    lblStoreAddress.text =  "Available Store: \((UserService.sharedUser?.favoriteStore?.name)!) \((UserService.sharedUser?.favoriteStore?.street)!) \((UserService.sharedUser?.favoriteStore?.city)!) \((UserService.sharedUser?.favoriteStore?.state)!) \((UserService.sharedUser?.favoriteStore?.zip)!)"
                    
                    
                    myMutableString = NSMutableAttributedString(string: lblStoreAddress.text!, attributes: [NSFontAttributeName:UIFont(name: "Helvetica", size: 13.0)!])
                    
                    myMutableString.addAttribute(NSForegroundColorAttributeName, value: UIColor(hex: Constants.jambaPinkColor), range: NSRange(location:0,length:16))
                    
                    // set label Attribute
                    
                    lblStoreAddress.attributedText = myMutableString
                }
                if offerDetail?.desc.characters.count > 0
                {
                    lblOfferDesc.text = offerDetail?.desc
                }
                else
                {
                    lblOfferDesc.text = ""
                    lblDecHeight.constant = 0;
                }
                if offerDetail?.offerValidity.characters.count >  0
                {
                    
                    lblExpires.text = offerDetail?.offerValidity.convertStringToDate((offerDetail?.offerValidity)!)
                }
                else
                {
                    lblExpires.text = "Never Expires"
                }
                
                let defaults = NSUserDefaults.standardUserDefaults()
                if let customerId = defaults.stringForKey("Newmemberid")
                {
                    SVProgressHUD.showWithStatus("Getting Promocode...", maskType: .Clear)
                    
                    ClpOfferService.getPromoCode(customerId ,offerId: (offerDetail?.isPMoffer)! == true ?(offerDetail?.pmPromotionID)!:String(offerDetail!.id),isPMIntegrated :(offerDetail?.isPMoffer)!){ (promoJson : clppromoSummary?, error) -> Void in
                        
                        SVProgressHUD.dismiss()
                        
                        if error != nil {
                            log.warning("WARNING: \(error?.localizedDescription)")
                            self.presentError(error)
                            
                            return
                        }
                        
                        let jsonobject: clppromoSummary = promoJson!
                        let StatusCode : Int = jsonobject.StatusCode
                        
                        if (StatusCode == 401)
                        {
                            clpAnalyticsService.sharedInstance.clpsdkobj?.getRefrshedToken()
                                ClpOfferService.getPromoCode(customerId ,offerId: (self.offerDetail?.isPMoffer)! == true ?(self.offerDetail?.pmPromotionID)!:String(self.offerDetail!.id),isPMIntegrated :(self.offerDetail?.isPMoffer)!){ (promoJson : clppromoSummary?, error) -> Void in
                                    SVProgressHUD.dismiss()

                                    if error != nil {
                                        log.warning("WARNING: \(error?.localizedDescription)")
                                        self.presentError(error)
                                        
                                        return
                                    }
                                    
                                    print("joe** response is here \(promoJson)")
                                    if let promo = promoJson?.promoCode
                                    {
                                        if promo.isEmpty
                                        {
                                            self.presentOkAlert("Alert", message: "No promocode avaialble for this offer")
                                            self.lblPromoCdode.hidden = true
                                            
                                        }
                                        else
                                        {
                                            print(promo)
                                            
                                            self.lblPromoCdode.text = "Promo code : \(promo)";
                                            self.lblPromoCdode.hidden = false

                                            
                                            //                        promo = "jj2off"
                                            if promo.characters.count>0
                                            {
                                                let data = promo.dataUsingEncoding(NSISOLatin1StringEncoding, allowLossyConversion: false)
                                                
                                                let filter = CIFilter(name: "CIQRCodeGenerator")// // CICode128BarcodeGenerator
                                                
                                                filter!.setValue(data, forKey: "inputMessage")
                                                filter!.setValue("Q", forKey: "inputCorrectionLevel")
                                                
                                                self.qrcodeImage = filter!.outputImage
                                                
                                                self.displayQRCodeImage()
                                            }
                                            else
                                            {
                                                return
                                            }
                                        }
                                        
                                    }

                                    
                            }
                        }
                        else
                        {
                       
                        print("joe** response is here \(promoJson)")
                        if let promo = promoJson?.promoCode
                        {
                            if promo.isEmpty
                            {
                                self.presentOkAlert("Alert", message: "No promocode avaialble for this offer")
                                self.lblPromoCdode.hidden = true
                                
                            }
                            else
                            {
                                print(promo)
                                
                                self.lblPromoCdode.text = "Promo code : \(promo)";
                                
                                //                        promo = "jj2off"
                                if promo.characters.count>0
                                {
                                    let data = promo.dataUsingEncoding(NSISOLatin1StringEncoding, allowLossyConversion: false)
                                    
                                    let filter = CIFilter(name: "CIQRCodeGenerator")// // CICode128BarcodeGenerator
                                    
                                    filter!.setValue(data, forKey: "inputMessage")
                                    filter!.setValue("Q", forKey: "inputCorrectionLevel")
                                    
                                    self.qrcodeImage = filter!.outputImage
                                    
                                    self.displayQRCodeImage()
                                }
                                else
                                {
                                    return
                                }
                            }
                            
                        }
                            
                        }
                    }
                    
                }
                
                
            }
            
        }
            
        else{
            
            let alert = UIAlertController(title: "Alert", message: "Cannot connect to :", preferredStyle: UIAlertControllerStyle.Alert)
            alert.addAction(UIAlertAction(title: "Click", style: UIAlertActionStyle.Default, handler: nil))
            appdelegate?.window?.rootViewController?.presentViewController(alert, animated: true, completion: nil)
        }
        
        
        // Do any additional setup after loading the view.
    }
    
    
    
    
    func displayQRCodeImage() {
        let scaleX = imgQRCode.frame.size.width / qrcodeImage.extent.size.width
        let scaleY = imgQRCode.frame.size.height / qrcodeImage.extent.size.height
        
        let transformedImage = qrcodeImage.imageByApplyingTransform(CGAffineTransformMakeScale(scaleX, scaleY))
        
        imgQRCode.image = UIImage(CIImage: transformedImage)
    }
    
    
    
    // MARK: Private
    
    private func dismiss() {
        // (navigationController as? BasketNavigationController)?.dismiss()
        
        //        navigationController
        
        self.view.removeFromSuperview()
        
        self.navigationController?.popViewControllerAnimated(true)
        
    }
    
    
    
    override func viewWillAppear(animated: Bool) {
        
        self.updateStuff()
        
    }
    
    func updateStuff()
    {
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
        
        if appdelegate?.isPushOpen == true
        {
            NonSignedInViewController.sharedInstance().startOrderButtonClicked()
            
        }
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // Remove observer
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    //    /// Slide the navigation controller view out of screen
    //    private func animateOut(callback: BasketNavigationControllerAnimationCompleteCallback) {
    //        let superViewBounds = view.superview!.bounds
    //        let finalFrame = CGRectMake(superViewBounds.width, 0, superViewBounds.width, superViewBounds.height)
    //        UIView.animateWithDuration(Constants.basketNCAnimationTime, animations: {
    //            self.view.frame = finalFrame
    //            }, completion: { completed in
    //                callback()
    //        })
    //    }
    ////    
    //    func dismiss() {
    ////        animateOut {
    //            self.view.removeFromSuperview()
    ////        }
    //    }
    
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}