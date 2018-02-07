//
//  MyRewardsTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD


let CellIdentifier: String = "RewardTableViewCell"


class MyRewardsTableViewController: UITableViewController,clpSdkDelegate {

    @IBOutlet weak var customNav: UINavigationItem!

    var rewardSummary: RewardSummary?
    var OfferSummary : ClpOfferSummary?
    
    private var rewards: [Reward] = []
    private var offers: [ClpOffer] = []
    
    private var  offer : ClpOffer?
    var isOffers : Bool!;
    

    override func viewDidLoad() {
        super.viewDidLoad()
        
        
              NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(MyRewardsTableViewController.updateStuff), name: "appDidBecomeActive", object: nil)
        
        configureNavigationBar(.LightBlue)
        
        
        self.tableView.registerClass(UITableViewCell.self, forCellReuseIdentifier: "BasketRewardTableViewCell")
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 60
        tableView.rowHeight = UITableViewAutomaticDimension
        
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
        
        if appdelegate?.isPushOpen == true
        {
            let defaults = NSUserDefaults.standardUserDefaults()
            if let customerId = defaults.stringForKey("Newmemberid")
            {
                SVProgressHUD.showWithStatus("Loading offers...", maskType: .Clear)

                ClpOfferService.getClpOffers(customerId){ (offers : ClpOfferSummary?, error) -> Void in
                    
                    SVProgressHUD.dismiss()
                    if error != nil {
                        log.warning("WARNING: \(error?.localizedDescription)")
                        return
                    }
                    self.OfferSummary = nil
                    self.OfferSummary = offers
                    
                    if let summary =  self.rewardSummary
                    {
                        if let rewardsCount : [Reward] = summary.rewards
                        {
                    
                            self.rewards = rewardsCount.filter { $0.type == "offer" || $0.type == "reward" } ?? []
                        }
                    }

                    self.offers = self.OfferSummary?.offerList ?? []
                    self.tableView.reloadData()
 
                }
                
            }
        }
        
        else
        {
        // Display only rewards of type "offer"
            
            
            self.offers = self.OfferSummary?.offerList ?? []
            
            
            
            if let summary =  self.rewardSummary
            {
                if let rewardsCount : [Reward] = summary.rewards
                {
                    self.rewards = (rewardsCount)
                    self.rewards = rewardsCount.filter { $0.type == "offer" || $0.type == "reward" } ?? []
                }
            }
          
                    self.tableView.reloadData()
        }
        
    }
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        tableView.reloadData() // For autolayout cleanup
    }
    
    
    @IBAction func closeRewadScreen(sender: AnyObject) {

        self.dismissModalController()

//                
    }
    // MARK: TableView datasource
    
    override func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor =   UIColor(hex: Constants.jambsRewardsTitleColor)
        headerView?.textLabel?.font = UIFont(name: Constants.archerBold, size: 20)
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        
        if clpAnalyticsService.sharedInstance.clpsdkobj?.isInAPPOffer == true
        {
         return 3
        }
        else
        {
            return 2
        }
    }
//    
//      override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat
//    
//    {
//        var cell: RewardTableViewCell? = nil
//        
//        var token: dispatch_once_t = 0
//        dispatch_once(&token) { () -> Void in
//            print("Called once")
//            cell  = tableView.dequeueReusableCellWithIdentifier(CellIdentifier) as? RewardTableViewCell
//        }
//
//        return calculateHeightForConfiguredSizingCell(cell!)
//
//    }

    
    func calculateHeightForConfiguredSizingCell(sizingCell: UITableViewCell) -> CGFloat {
        
        
        sizingCell.layoutIfNeeded()
        let size: CGSize = sizingCell.contentView.systemLayoutSizeFittingSize(UILayoutFittingCompressedSize)
        return size.height
    }

    
    override func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        switch section{
        case 0:
            return ""
        case 1:
            return "Rewards"
        default:
            return "Offers"
        }
        
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section{
        case 0:
                return 1
        case 1:
            return max(rewards.count, 1)
        default:
            return max(offers.count, 1)
        }
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        if indexPath.section == 0
        {
            
                return tableView.dequeueReusableCellWithIdentifier("RedeemInstructionsTableViewCell")!
        }
        
        else if indexPath.section == 1 {
            
                    if rewards.count > 0 {

                let reward = rewards[indexPath.row]
                let cell = tableView.dequeueReusableCellWithIdentifier("RewardTableViewCell") as! RewardTableViewCell
                cell.nameLabel.text = reward.name
                cell.descLabel.text = reward.desc
                cell.quantityLabel.hidden = true
                cell.imgArrow.hidden = true
                return cell
                }
           else {
                return tableView.dequeueReusableCellWithIdentifier("NoRewardsTableViewCell")!
                    }
        }
        else  {
            
            if offers.count > 0 {
                let offer = offers[indexPath.row]
                let cell = tableView.dequeueReusableCellWithIdentifier("RewardTableViewCell") as! RewardTableViewCell
                cell.nameLabel.text = offer.offerTitle
                 cell.nameLabel.text = String(cell.nameLabel.text!.characters.prefix(1)).uppercaseString + String(cell.nameLabel.text!.characters.dropFirst())
                cell.descLabel.text = offer.desc
                cell.quantityLabel.hidden = false
                if offer.offerValidity.characters.count >  0
                {
                    cell.quantityLabel.text = self.convertStringToDate(offer.offerValidity)
                }
                else
                {
                    cell.quantityLabel.text = "Never Expires"
                }
              
                return cell
            } else {
                return tableView.dequeueReusableCellWithIdentifier("NoOfferTableViewCell")!
            }
        }
    
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath)
    {
        if indexPath.section == 2 {
            
    
            
            if offers.count > 0 {
                
                 offer = offers[indexPath.row]
                
                if offer!.channalId == 6
                {
                    
                    self.openDynamicPass(String(offer!.id))
                    
                    if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                    {
                    productName = self.offers[indexPath.row].offerTitle
                    productID =   Int64(self.offers[indexPath.row].id)
                    isAppEvent = true
                    clpAnalyticsService.sharedInstance.clpTrackScreenPassView("PASS_CLICKED");
                    }

                    
                }
                
                else
                {
                    if (clpAnalyticsService.sharedInstance.clpsdkobj?.isTriggerAppEvent == true)
                    {
                    productName = self.offers[indexPath.row].offerTitle
                    productID =   Int64(self.offers[indexPath.row].id)
                    isAppEvent = true
                    
                    clpAnalyticsService.sharedInstance.clpTrackScreenView("OPEN_APP_OFFER")
                    }

                    performSegueWithIdentifier("OfferDetail", sender: self)
                }
                
                
            }
            
            

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
        if let customerId = defaults.stringForKey("Newmemberid")
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
            request.setValue(clpAnalyticsService.sharedInstance.clpsdkobj?.client_ID, forHTTPHeaderField: "client_id")
            //request.setValue("C65A0DC0F28C469FB7376F972DEFBCB8", forHTTPHeaderField: "client_secret")
            request.setValue("fishbowl", forHTTPHeaderField: "tenantName")
            
            request.HTTPMethod = "POST"
        
                do {
            
                    let params = ["memberid":customerId, "campaignId":offerId, "deviceName":"\(deviceType)"] as Dictionary<String, String>
                    
                    request.HTTPBody = try! NSJSONSerialization.dataWithJSONObject(params, options: [])
                    
        let task = session.dataTaskWithRequest(request, completionHandler: { (respnosedata: NSData?, response: NSURLResponse?, error: NSError?) -> Void in
            let statusCode = (response as! NSHTTPURLResponse).statusCode
            
            self.view.userInteractionEnabled = true


            if (error == nil && statusCode == 200) {
                // Success
                
//                clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer));
                
                clpAnalyticsService.sharedInstance.clpsdkobj = clpsdk.sharedInstanceWithAPIKey(AppConstants.CLPheaderKey, withBaseURL: AppConstants.URL.AppPointingURL(intPointingServer),withClientID:AppConstants.URL.AppPointingClientID(intPointingServer));

                
                if respnosedata?.length > 0
                {
                    clpAnalyticsService.sharedInstance.clpsdkobj!.openPassbookAndShowwithData(respnosedata)
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
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        
        if segue.identifier == "OfferDetail"
        {
            let offerDetailViewController  : OfferDetailViewController = (segue.destinationViewController as? OfferDetailViewController)!
            offerDetailViewController.offerDetail = offer
            

        }

    }
    
    
    func updateStuff()
    {
        let appdelegate = UIApplication.sharedApplication().delegate as? AppDelegate
        
        if appdelegate?.isPushOpen == true
        {
            
            self.dismissViewControllerAnimated(true, completion: {() -> Void in
            
                SignedInMainViewController.sharedInstance().startOrderButtonClicked()

            })
            
        }
    }
    
    
    override func viewWillAppear(animated: Bool) {
        
        self.updateStuff()
        
    }
    
    
    
    
    
    func clpClosePassbook(strError : String) {
        
        SVProgressHUD.dismiss()
        if strError.characters.count>0 {
            self.presentOkAlert("Error", message: strError)
        }
        
    }
    func clpOpenPassbook() {
        
    }
    
    
    func clpPushDataBinding(strOfferTitle: String, withId strOfferId: String)
    {
        
    }
    func clpResponseFail(error: NSError!) {

    }
    
    
    func daysFrom(date:NSDate) -> Int{
        
        let todaydate = NSDate()
        return NSCalendar.currentCalendar().components(.Day, fromDate: todaydate, toDate: date, options: []).day
    }
    
    func convertStringToDate(strDate:String) -> String{
        
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat="yyyy-MM-dd HH:mm:ss";
        let date = dateFormatter.dateFromString(strDate)
        
        let number = self.daysFrom(date!)
        
        return "Expires in \(number) days"
        
    
    }
    

    
    // Convert date to string
    func convertDateToString(date:NSDate)-> String{
        let dateFormatter = NSDateFormatter()
        dateFormatter.locale = NSLocale.currentLocale()
        dateFormatter.dateFormat="yyyy-MM-dd HH:mm:ss.SSS";
        print(date);
        let string = dateFormatter.stringFromDate(date)
        print(string)
        return string;
    }
    
    
}
