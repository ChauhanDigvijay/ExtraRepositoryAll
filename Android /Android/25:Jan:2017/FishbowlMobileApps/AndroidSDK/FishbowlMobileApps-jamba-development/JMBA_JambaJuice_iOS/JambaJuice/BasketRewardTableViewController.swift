//
//  BasketRewardTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import SwiftyJSON

class BasketRewardTableViewController: UITableViewController {
    
    private var rewards: [Reward] = []
    private var offers: [ClpOffer] = []
    
    var rewardSummary: RewardSummary?
    var OfferSummary : ClpOfferSummary?
    
    private var  offer : ClpOffer?
    
    var selectedOfferPromo : ClpOffer?
    
    
    
    
    private var loadComplete: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        
        //        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(BasketRewardTableViewController.showOffersList), name: "offersListUpdated", object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(BasketRewardTableViewController.fishbowlPromoCodeCallback(_:)), name: "promoCodeForOffer", object: nil)
        
        SVProgressHUD.showWithStatus("Loading rewards...", maskType: .Clear)
        BasketService.availableRewards { ( rewards, error) -> Void in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            
            if clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
            {
                self.showOffersList()
            }
            self.rewardSummary = RewardSummary()
            self.rewardSummary!.rewards = rewards;
            
            // Display only rewards of type "offer"
            //                rewards = self.rewardSummary?.rewards.filter { $0.type == "offer" || $0.type == "reward" } ?? []
            self.rewards = rewards
            self.loadComplete = true
            self.updateScreen()
        }
    }
    
    //    func LoadOffers()
    //    {
    //        // Get the offers after push notification
    //
    //        if clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
    //        {
    //            SVProgressHUD.showWithStatus("Loading offers...", maskType: .Clear)
    //            ClpApiClassService.sharedInstance.getOffers()
    ////            let defaults = NSUserDefaults.standardUserDefaults()
    ////            if let customerId = defaults.stringForKey("Newmemberid")
    ////            {
    ////                print(customerId)
    ////                SVProgressHUD.showWithStatus("Loading offers...", maskType: .Clear)
    ////
    ////                ClpOfferService.getClpOffers(customerId){ (offers : ClpOfferSummary?, error) -> Void in
    ////                    SVProgressHUD.dismiss()
    ////
    ////                    if error != nil {
    ////                        self.presentError(error)
    ////                        return
    ////                    }
    ////                    print("joe** response is here \(offers)")
    ////                    self.OfferSummary = offers
    ////
    ////                    // Display only rewards of type "offer"
    ////                    //                    rewards = self.rewardSummary?.rewards.filter { $0.type == "offer" || $0.type == "reward" } ?? []
    ////                    self.offers = self.OfferSummary?.offerList ?? []
    ////                    self.loadComplete = true
    ////
    ////                    self.updateScreen()
    ////
    ////                }
    ////
    ////            }
    //
    //        }
    //
    //    }
    
    //fishbowl offer's will be loaded into screen
    func showOffersList() {
        SVProgressHUD.dismiss()
        if OfferService.sharedInstance.offersSummary != nil {
            self.OfferSummary = OfferService.sharedInstance.offersSummary
            
            self.offers = self.OfferSummary?.offerList ?? []
            self.loadComplete = true
            
            self.updateScreen()
        }
        
    }
    
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    private func updateScreen() {
        tableView.reloadData()
    }
    
    
    // MARK: TableView datasource
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        if clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
        {
            return 3
        }
        else
        {
            return 2
        }    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat
        
    {
        switch indexPath.section{
        case 0:
            return 60
        case 1:
            if indexPath.row == 0 {
                return 40
            }
            if  clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
            {
                if offers.count == 0 {
                    return 40
                } else {
                    return 80
                }
            }
            else{
                return 40
            }
            
        case 2:
            return 60
        default:
            return 60
        }
    }
    
    
    //    override func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
    //
    //        switch section{
    //        case 0:
    //            return "Rewards"
    //
    //        case 1:
    //            return "Offers  dfhzhhiu"
    //            if  clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
    //            {
    //                return "Offers"
    //            }
    //            else
    //            {
    //                return "Promotion Code"
    //
    //            }
    //        case 2:
    //            return "Promotion Code"
    //        default:
    //            return ""
    //        }
    //
    //    }
    //
    //    override func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
    //        let headerView = view as? UITableViewHeaderFooterView
    //        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambsRewardsTitleColor)
    //        headerView?.textLabel?.font = UIFont(name: Constants.archerMedium, size: 18)
    //        headerView?.textLabel?.numberOfLines = 2
    //    }
    
    override func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let  headerCell = tableView.dequeueReusableCellWithIdentifier("BasketRewardHeaderTableViewCell") as! BasketRewardHeaderTableViewCell
        
        switch section{
        case 0:
            headerCell.headerLabel.text = "Rewards"
            
        case 1:
            if  clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
            {
                headerCell.headerLabel.text =  "Offers"
            }
            else
            {
                headerCell.headerLabel.text =  "Promotion Code"
                
            }
        case 2:
            headerCell.headerLabel.text =  "Promotion Code"
        default:
            headerCell.headerLabel.text =  ""
        }
        
        return headerCell
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        switch section{
        case 0:
            return max(rewards.count, 1)
        case 1:
            if  clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
            {
                return max(offers.count + 1, 2) //+1 for offer instruction
            }
            else{
                return 1
            }
        case 2:
            return 1
        default:
            return 1
        }
    }
    
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            if indexPath.row < rewards.count {
                let reward = rewards[indexPath.row]
                let cell = tableView.dequeueReusableCellWithIdentifier("BasketRewardTableViewCell", forIndexPath: indexPath) as! BasketRewardTableViewCell
                cell.nameLabel.text = reward.name
                cell.checkImageView.hidden = !reward.applied
                cell.selectionStyle = .Default
                return cell
            } else {
                let cell = tableView.dequeueReusableCellWithIdentifier("BasketRewardTableViewCell", forIndexPath: indexPath) as! BasketRewardTableViewCell
                cell.nameLabel.text = loadComplete ? "No rewards available" : ""
                cell.checkImageView.hidden = true
                cell.selectionStyle = .None
                return cell
            }
        }
        
        if indexPath.section == 1 {
            
            if  clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
            {
                
                if indexPath.row <= offers.count {
                    if indexPath.row == 0 {
                        let cell = tableView.dequeueReusableCellWithIdentifier("OfferInstructionTableCell")!
                        cell.layoutMargins = UIEdgeInsetsZero
                        return cell
                        
                    } else {
                        let offer = offers[indexPath.row - 1]
                        let cell = tableView.dequeueReusableCellWithIdentifier("RewardTableViewCell", forIndexPath: indexPath) as! RewardTableViewCell
                        cell.nameLabel.text = offer.offerTitle
                        cell.descLabel.text = offer.desc
                        print(offer.offerValidity)
                        
                        if offer.offerValidity.characters.count > 0
                        {
                            cell.quantityLabel.text = self.convertStringToDate(offer.offerValidity)
                        }
                        else
                        {
                            cell.quantityLabel.text = "Never Expires"
                        }
                        
                        
                        
                        cell.btnApply.addTarget(self, action: #selector(BasketRewardTableViewController.btnofferApply(_:)), forControlEvents: .TouchUpInside)
                        cell.btnApply.tag = indexPath.row - 1;
                        cell.btnApply.layer.borderColor = UIColor(hex: Constants.jambaOrangeColor).CGColor
                        cell.btnApply.layer.borderWidth = 1.0
                        if offers[indexPath.row - 1].isPMoffer {
                            if BasketService.sharedBasket?.offerId == offers[indexPath.row - 1].pmPromotionID {
                                cell.btnApply.setTitle("Remove", forState: .Normal)
                            }
                        } else {
                            if BasketService.sharedBasket?.offerId == "\(offers[indexPath.row - 1].id)" {
                                cell.btnApply.setTitle("Remove", forState: .Normal)
                            }
                        }
                        cell.selectionStyle = .Default
                        return cell
                    }
                } else {
                    if indexPath.row == 0 {
                        let cell = tableView.dequeueReusableCellWithIdentifier("OfferInstructionTableCell")!
                        cell.layoutMargins = UIEdgeInsetsZero
                        return cell
                        
                    } else {
                        let cell = tableView.dequeueReusableCellWithIdentifier("BasketRewardTableViewCell", forIndexPath: indexPath) as! BasketRewardTableViewCell
                        cell.nameLabel.text = loadComplete ? "No offers available" : ""
                        cell.checkImageView.hidden = true
                        cell.selectionStyle = .None
                        return cell
                    }
                }
            }
            else{
                
                let cell = tableView.dequeueReusableCellWithIdentifier("EnterPromotionTableViewCell")!
                return cell
            }
        }
        else {
            let cell = tableView.dequeueReusableCellWithIdentifier("EnterPromotionTableViewCell")!
            return cell
        }
    }
    
    
    func btnPassOpen(sender: UIButton)
    {
        let offer = offers[sender.tag]
        
        if offer.channalId == 6
        {
            
            //            if(intPointingServer == 2)
            //            {
            //
            //                let passURL : NSURL = NSURL(string: offer.passURL)!
            //
            //                clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer));
            //
            //                clpAnalyticsService.sharedInstance.clpsdkobj?.openPassbookAndShow(passURL)
            //            }
            //            else
            //            {
            self.openDynamicPass(String(offer.id))
            //            }
            
            
        }
        
        
    }
    
    func btnofferApply(sender: UIButton)
    {
        selectedOfferPromo = offers[sender.tag]
        SVProgressHUD.showWithStatus("Getting Promocode...", maskType: .Clear)
        
        if sender.titleLabel?.text == "Apply" {
            //            ClpApiClassService.sharedInstance.getPromoCode(selectedOfferPromo!.isPMoffer == true ? selectedOfferPromo!.pmPromotionID:String(selectedOfferPromo!.id),isPMIntegrated : selectedOfferPromo!.isPMoffer)
        } else {
            SVProgressHUD.showWithStatus("Removing promotion code...", maskType: .Clear)
            BasketService.removePromotionCode({ (basket, error) in
                SVProgressHUD.dismiss()
                if error != nil {
                    self.presentError(error)
                    return
                }
                BasketService.sharedBasket?.offerId = ""
                self.popToRootViewController()
                if self.selectedOfferPromo!.channalId == 6
                {
                    if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                    {
                        clpAnalyticsService.sharedInstance.clpTrackScreenPassView("REMOVE_COUPAN");
                    }
                    
                }
                else
                {
                    if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                    {
                        
                        clpAnalyticsService.sharedInstance.clpTrackScreenView("REMOVE_COUPAN");
                    }
                    
                }
            })
        }
        //        let defaults = NSUserDefaults.standardUserDefaults()
        //        if let customerId = defaults.stringForKey("CustomerId")
        //        {
        //            SVProgressHUD.showWithStatus("Getting Promocode...", maskType: .Clear)
        
        //            ClpOfferService.getPromoCode(customerId ,offerId: (offer.isPMoffer) == true ?(offer.pmPromotionID):String(offer.id),isPMIntegrated :(offer.isPMoffer)){ (promoJson : clppromoSummary?, error) -> Void in
        //
        //                SVProgressHUD.dismiss()
        //
        //                if error != nil {
        //                    log.warning("WARNING: \(error?.localizedDescription)")
        //                    self.presentError(error)
        //
        //                    return
        //                }
        //                print("joe** response is here \(promoJson)")
        //                if let promo = promoJson?.promoCode
        //                {
        //                    if promo.isEmpty
        //                    {
        //                        self.presentOkAlert("Alert", message: "No promocode avaialble for this offer")
        //
        //                    }
        //                    else
        //                    {
        //                        SVProgressHUD.showWithStatus("Applying promotion code...", maskType: .Clear)
        //                        BasketService.applyPromotionCode(promo) { (basket, error) -> Void in
        //                            SVProgressHUD.dismiss()
        //                            if error != nil {
        //                                self.presentError(error)
        //                                return
        //                            }
        //
        //                            BasketService.sharedBasket?.offerId = (offer.isPMoffer) == true ?(offer.pmPromotionID):String(offer.id)
        //
        //                            self.popToRootViewController() // Close all the way to basket screen
        //
        //                            productName = self.offers[sender.tag].offerTitle
        //                            productID =   Int64(self.offers[sender.tag].id)
        //                            isAppEvent = true
        //
        //                            if offer.channalId == 6
        //                            {
        //                                if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
        //                                {
        //                                    clpAnalyticsService.sharedInstance.clpTrackScreenPassView("PASS_ACCEPTED");
        //                                }
        //
        //                            }
        //                            else
        //                            {
        //                                if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
        //                                {
        //
        //                                    clpAnalyticsService.sharedInstance.clpTrackScreenView("ACCEPT_OFFER");
        //                                }
        //
        //                            }
        //                        }
        //                    }
        //
        //                }
        //            }
        
        //        }
        
        
    }
    
    func fishbowlPromoCodeCallback(notification: NSNotification) {
        if self.selectedOfferPromo == nil {
            return
        }
        if let promoDetails = notification.userInfo {
            if let result = notification.userInfo?["successFlag"] as? Bool {
                if !result {
                    if let errorMessage = notification.userInfo?["successFlag"] as? String {
                        SVProgressHUD.dismiss()
                        self.presentOkAlert("Error", message: errorMessage);
                    }
                    return
                } else {
                    
                    
                    let promoJson = clppromoSummary(json: JSON(promoDetails))
                    
                    
                    print("joe** response is here \(promoJson)")
                    
                    let promo = promoJson.promoCode
                    
                    if promo.isEmpty
                    {
                        self.presentOkAlert("Alert", message: "No promocode avaialble for this offer")
                    } else {
                        SVProgressHUD.showWithStatus("Applying promotion code...", maskType: .Clear)
                        BasketService.applyPromotionCode(promo) { (basket, error) -> Void in
                            SVProgressHUD.dismiss()
                            if error != nil {
                                self.presentError(error)
                                return
                            }
                            
                            let offer = self.selectedOfferPromo!
                            BasketService.sharedBasket?.offerId = (offer.isPMoffer) == true ?(offer.pmPromotionID):String(offer.id)
                            
                            self.popToRootViewController() // Close all the way to basket screen
                            
                            productName = self.selectedOfferPromo!.offerTitle
                            productID =   Int64(self.selectedOfferPromo!.id)
                            isAppEvent = true
                            
                            if offer.channalId == 6
                            {
                                if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                                {
                                    clpAnalyticsService.sharedInstance.clpTrackScreenPassView("PASS_ACCEPTED");
                                }
                                
                            }
                            else
                            {
                                if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                                {
                                    
                                    clpAnalyticsService.sharedInstance.clpTrackScreenView("ACCEPT_OFFER");
                                }
                                
                            }
                        }
                    }
                }
            } else {
                SVProgressHUD.dismiss()
                self.presentOkAlert("Error", message: "Something went Wrong");
            }
        } else {
            SVProgressHUD.dismiss()
            self.presentOkAlert("Error", message: "Something went Wrong");
        }
    }
    
    // MARK: TableView delegate
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if indexPath.section == 0 && indexPath.row < rewards.count {
            let reward = rewards[indexPath.row]
            self.applyReward(reward)
        }
            
        else if indexPath.section == 1
        {
            if  clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
            {
                
                if offers.count > 0
                {
                    let index = indexPath.row - 1
                    productName = self.offers[index].offerTitle
                    productID =   Int64(self.offers[index].id)
                    isAppEvent = true
                    
                    offer = offers[index]
                    if offer!.channalId == 6
                    {
                        //                if(intPointingServer == 2)
                        //                {
                        //                    clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer));
                        //                    clpAnalyticsService.sharedInstance.clpsdkobj?.openPassbookAndShow(NSURL(string: (offer?.passURL)!))
                        //                }
                        //                else
                        //                {
                        self.openDynamicPass(String(offer!.id))
                        //                }
                        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                        {
                            clpAnalyticsService.sharedInstance.clpTrackScreenPassView("PassClicked");
                        }
                        
                    } else {
                        if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                        {
                            clpAnalyticsService.sharedInstance.clpTrackScreenView("OPEN_APP_OFFER")
                        }
                        
                        performSegueWithIdentifier("OfferDetail", sender: self)
                    }
                    
                }
            }
        }
        
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
    }
    
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        
        if segue.identifier == "OfferDetail"
        {
            let offerDetailViewController  : OfferDetailViewController = (segue.destinationViewController as? OfferDetailViewController)!
            offerDetailViewController.offerDetail = offer
            
            
        }
        
    }
    
    
    func openDynamicPass(offerId:String)
    {
        
        //123/231455
        
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
        
        if appdelegate?.isReachable == true
        {
            
            SVProgressHUD.showWithStatus("Opening Pass...", maskType: .Clear)
            
            self.view.userInteractionEnabled = false
            
            let defaults = NSUserDefaults.standardUserDefaults()
            if let customerId = defaults.stringForKey("CustomerId")
            {
                let sessionConfig = NSURLSessionConfiguration.defaultSessionConfiguration()
                let session = NSURLSession(configuration: sessionConfig, delegate: nil, delegateQueue: nil)
                
                let request = NSMutableURLRequest(URL: NSURL(string: "\(AppConstants.URL.AppPointingURL(intPointingServer))clpapi/mobile/getPass")!)
                
                //        request.setValue("application/octet-stream", forHTTPHeaderField: "Content-Type")
                //        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                //        request.setValue(AppConstants.CLPheaderKey, forHTTPHeaderField: "CLP-API-KEY")
                
                request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                request.setValue("mobilesdk", forHTTPHeaderField: "Application")
                request.setValue("1173", forHTTPHeaderField: "tenantid")
                let strData : String = NSUserDefaults.standardUserDefaults().stringForKey("access_token")!
                request.setValue(strData, forHTTPHeaderField: "access_token")
                request.setValue(AppConstants.jambaClientKey, forHTTPHeaderField: "client_id")
                //request.setValue("C65A0DC0F28C469FB7376F972DEFBCB8", forHTTPHeaderField: "client_secret")
                request.setValue("fishbowl", forHTTPHeaderField: "tenantName")
                
                request.HTTPMethod = "POST"
                
                
                let json = ["customerId":customerId,"offerId":offerId,"isPMIntegrated":"false","deviceType":deviceType]
                
                
                do {
                    
                    let bodyData =  try NSJSONSerialization.dataWithJSONObject(json as! [String : String], options: .PrettyPrinted)
                    
                    request.HTTPBody = bodyData
                    
                    let task = session.dataTaskWithRequest(request, completionHandler: { (respnosedata: NSData?, response: NSURLResponse?, error: NSError?) -> Void in
                        let statusCode = (response as! NSHTTPURLResponse).statusCode
                        
                        self.view.userInteractionEnabled = true
                        
                        
                        if (error == nil && statusCode == 200) {
                            // Success
                            
                            //                            clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer),withClientID:AppConstants.jambaClientKey);
                            
                            if respnosedata?.length > 0
                            {
                                //                                clpAnalyticsService.sharedInstance.clpsdkobj!.openPassbookAndShowwithData(respnosedata)
                            }
                            dispatch_async(dispatch_get_main_queue()) {
                                SVProgressHUD.dismiss()
                            }
                            
                        }
                        else {
                            // Failure
                            dispatch_async(dispatch_get_main_queue()) {
                                SVProgressHUD.dismiss()
                            }
                        }
                    })
                    task.resume()
                    
                    
                    
                }
                catch {
                    print(error)
                }
                
            }
            
        }
            
        else
        {
            presentOkAlert("NO Internet", message: "You seems to be offline. Please check your Internet Connection.")
            
        }
        
        
        
        
    }
    
    func convertStringToDate(strDate:String) -> String{
        
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat="yyyy-MM-dd HH:mm:ss.SSS";
        let date = dateFormatter.dateFromString(strDate)
        
        var number = 0
        if let formattedDate = date {
            number = self.daysFrom(formattedDate)
        } else {
            return ""
        }
        
        return "Expires in \(number) days"
        
        
    }
    
    func daysFrom(date:NSDate) -> Int{
        
        let todaydate = NSDate()
        return NSCalendar.currentCalendar().components(.Day, fromDate: todaydate, toDate: date, options: []).day
    }
    
    
    
    private func applyReward(reward: Reward) {
        // If reward has already been applied, do nothing
        if reward.applied {
            popViewController()
            return
        }
        
        SVProgressHUD.showWithStatus("Applying reward...", maskType: .Clear)
        BasketService.applyReward(reward) { (basket, error) in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            self.popViewController()
        }
    }
    
}
