//
//  MyRewardsTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 6/11/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import SwiftyJSON


let CellIdentifier: String = "RewardTableViewCell"


class MyRewardsTableViewController: UITableViewController {
    var rewardSummary: RewardSummary?
    var OfferSummary : FishbowlOfferSummary?
    
    fileprivate var rewards: [Reward] = []
    fileprivate var offers: [FishbowlOffer] = []
    fileprivate var  offer : FishbowlOffer?
    
    var refreshOffer : Bool = false
    
    var refreshRewardAndOfferError:NSError = NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"There was a problem retrieving loyalty rewards/offers. Please try again later."])
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 60
        tableView.rowHeight = UITableViewAutomaticDimension
        
        //after launching the screen call the get gift card API
        let triggerTime = (Int64(NSEC_PER_SEC) * 0)
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + Double(triggerTime) / Double(NSEC_PER_SEC), execute: { () -> Void in
            self.showOffersList()
        })
        
        self.refreshControl?.addTarget(self, action: #selector(MyRewardsTableViewController.fbAndSpendGoRewardsAndOffers), for: .valueChanged)
        
        NotificationCenter.default.addObserver(self, selector: #selector(MyRewardsTableViewController.showOffersList), name: NSNotification.Name(rawValue: JambaNotification.ReloadRewardsAndOfferList.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(MyRewardsTableViewController.fbAndSpendGoRewardsAndOffers), name: NSNotification.Name(rawValue: JambaNotification.RefreshMyRewardViewWillAppear.rawValue), object: nil)
        
        
        if refreshOffer{
            fbAndSpendGoRewardsAndOffers()
        }
        
    }
    
    @objc func showOffersList() {
        if OfferService.sharedInstance.offersSummary != nil {
            self.OfferSummary = OfferService.sharedInstance.offersSummary!
        }
        
        self.rewardSummary = RewardService.sharedInstance.rewardSummary
        if let summary =  self.rewardSummary
        {
            let rewardsCount : [Reward] = summary.rewards
            self.rewards = rewardsCount.filter { $0.type == "offer" || $0.type == "reward" }
        }
        self.offers = OfferService.sharedInstance.offersSummary?.offerList ?? []
        self.tableView.reloadData()
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        tableView.reloadData() // For autolayout cleanup
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
       configureNavigationBar(.lightBlue)
    }
    
    
    @IBAction func closeRewadScreen(_ sender: AnyObject) {
        self.dismissModalController()
        //
    }
    // MARK: TableView datasource
    
    override func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor =   UIColor(hex: Constants.jambsRewardsTitleColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerBold, size: 20)
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    func calculateHeightForConfiguredSizingCell(_ sizingCell: UITableViewCell) -> CGFloat {
        sizingCell.layoutIfNeeded()
        let size: CGSize = sizingCell.contentView.systemLayoutSizeFitting(UILayoutFittingCompressedSize)
        return size.height
    }
    
    override func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        switch (section) {
        case 0:
            return 0
        case 1:
            return 40
        case 2:
            return 40
        default:
            return 0
        }
    }
    
    override func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        
        let  headerCell = tableView.dequeueReusableCell(withIdentifier: "TableHeaderCell") as! TableHeaderCell
        
        switch (section) {
        case 0:
            return nil
        case 1:
            headerCell.nameLabel.text = "Rewards";
            headerCell.badgeIcon.isHidden = false
            headerCell.badgeCount.isHidden = false
            headerCell.badgeCount.text = "\(rewards.count)"
        case 2:
            headerCell.nameLabel.text = "Offers";
            headerCell.badgeIcon.isHidden = false
            headerCell.badgeCount.isHidden = false
            headerCell.badgeCount.text = "\(offers.count)"
        default:
            return nil
        }
        
        return headerCell
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section{
        case 0:
            return 1
        case 1:
            return max(rewards.count, 1)
        default:
            return max(offers.count + 1, 2)     //+1 for offer instruction
        }
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if indexPath.section == 0
        {
            
            return tableView.dequeueReusableCell(withIdentifier: "RedeemInstructionsTableViewCell")!
        }
            
        else if indexPath.section == 1 {
            
            if rewards.count > 0 {
                
                let reward = rewards[indexPath.row]
                let cell = tableView.dequeueReusableCell(withIdentifier: "RewardTableViewCell") as! RewardTableViewCell
                cell.nameLabel.text = reward.name
                cell.descLabel.text = reward.desc
                cell.quantityLabel.isHidden = true
                cell.imgArrow.isHidden = true
                return cell
            }
            else {
                
                return tableView.dequeueReusableCell(withIdentifier: "NoRewardsTableViewCell")!
            }
        }
        else  {
            
            if offers.count > 0 {
                //Add offer instruction as the first cell
                if indexPath.row == 0
                {
                    let cell = tableView.dequeueReusableCell(withIdentifier: "OfferInstructionsTableViewCell")!
                    cell.layoutMargins = UIEdgeInsets.zero
                    return cell
                } else {
                    let offer = offers[indexPath.row - 1]
                    let cell = tableView.dequeueReusableCell(withIdentifier: "RewardTableViewCell") as! RewardTableViewCell
                    cell.nameLabel.text = offer.offerTitle
                    cell.nameLabel.text = String(cell.nameLabel.text!.characters.prefix(1)).uppercased() + String(cell.nameLabel.text!.characters.dropFirst())
                    cell.descLabel.text = offer.desc
                    cell.quantityLabel.isHidden = false
                    cell.imgArrow.isHidden = false
                    if offer.offerValidity.characters.count >  0
                    {
                        cell.quantityLabel.text = offer.offerValidity.convertStringToDate(offer.offerValidity)
                    }
                    else
                    {
                        cell.quantityLabel.text = "Never Expires"
                    }
                    
                    return cell
                }
            } else {
                if indexPath.row == 0
                {
                    let cell = tableView.dequeueReusableCell(withIdentifier: "OfferInstructionsTableViewCell")!
                    cell.layoutMargins = UIEdgeInsets.zero
                    return cell
                    
                } else {
                    return tableView.dequeueReusableCell(withIdentifier: "NoOfferTableViewCell")!
                }
            }
        }
        
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if indexPath.section == 2 {
            if offers.count > 0 {
                //skip the first cell because that is offer instruction
                if indexPath.row == 0 {
                    return
                }
                let index = indexPath.row - 1
                offer = offers[index]
                if offer!.channalId == 6
                {
                } else {
                    var offerId = ""
                    var offerTitle = ""
                    if offer?.id != nil{
                        offerId = "\(offer!.id)"
                    }
                    if offer?.offerTitle != nil{
                        offerTitle = "\(offer!.offerTitle)"
                    }
                    // Fishbowl Event
                    let offerName = "\(offerTitle)"
                    FishbowlApiClassService.sharedInstance.submitMobileAppEvent(offerId, item_name: offerName, event_name: "OPEN_APP_OFFER")
                    performSegue(withIdentifier: "OfferDetail", sender: self)
                }
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "OfferDetail"
        {
            let offerDetailViewController  : OfferDetailViewController = (segue.destination as? OfferDetailViewController)!
            offerDetailViewController.offerDetail = offer
        }
    }
    
    func daysFrom(_ date:Date) -> Int{
        let todaydate = Date()
        return (Calendar.current as NSCalendar).components(.day, from: todaydate, to: date, options: []).day!
    }
    
    
    func showRewardsAndOffers(_ notification: NSNotification){
        let dict = notification.userInfo! as Dictionary<AnyHashable, Any>
        rewardSummary = dict["rewards"] as! RewardSummary!
        OfferSummary = dict["offers"] as! FishbowlOfferSummary!
        self.showOffersList()
    }
    
    @objc func fbAndSpendGoRewardsAndOffers(){
        // End when pull to refresh
        self.refreshControl?.endRefreshing()
        SVProgressHUD.show(withStatus: "Loading...")
        SVProgressHUD.setDefaultMaskType(.clear)
        let group = DispatchGroup()
        group.enter()
        RewardService.loadRewards { (rewards, error) -> Void in
            if error != nil {
                group.leave()
                return
            }
            RewardService.sharedInstance.updateRewardSummary(rewards)
            group.leave()
        }
        group.enter()
        // Fishbowl Credential validation
        FishbowlApiClassService.sharedInstance.validateFishbowlCredentials { (status) in
            if !status{
                group.leave()
                return
            }else{
                FishbowlApiClassService.sharedInstance.getFishbowlOffer{ (response, error) in
                    
                    if error != nil{
                        group.leave()
                        return
                    }
                    let offers = FishbowlOfferSummary(json: JSON(response!))
                    OfferService.sharedInstance.updateOffers(offers);
                    group.leave()
                }
            }
        }
        group.notify(queue: .main) {
            UIApplication.inMainThread {
                SVProgressHUD.dismiss()
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.RefreshRewardsAndOffers.rawValue), object: self)
                self.showOffersList()
            }
        }
    }
}
