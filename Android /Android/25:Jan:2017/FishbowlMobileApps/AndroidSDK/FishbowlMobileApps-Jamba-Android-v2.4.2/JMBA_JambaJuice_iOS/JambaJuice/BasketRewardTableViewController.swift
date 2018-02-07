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
import HDK

class BasketRewardTableViewController: UITableViewController {
    fileprivate var rewards: [Reward] = []
    fileprivate var offers: [FishbowlOffer] = []
    fileprivate var  offer : FishbowlOffer?
    var selectedOfferPromo : FishbowlOffer?
    
    // Description label width constant
    let decriptionLabelWidth = 114
    var size:CGFloat?
    
    
    var refreshOloError:NSError?
    var refreshFishbowlError:NSError?
        
    // SpendGOReward and Fishbowl Offer Error Message
    var refreshRewardAndOfferError:NSError = NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"There was a problem retrieving loyalty rewards/offers. Please try again later."])
    //    var refreshRewardAndOfferError = NSError.init(coder: "There was a problem retrieving loyalty rewards/offers. Please try again later.")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // So that table view does not show empty cells.
        tableView.tableFooterView = UIView()
        NotificationCenter.default.addObserver(self, selector: #selector(BasketRewardTableViewController.basketRefreshed), name: NSNotification.Name(rawValue: JambaNotification.SharedBasketUpdated.rawValue), object: nil)
        availableRewardsAndOffers()
        
        // Estimated height for tableview
        tableView.estimatedRowHeight = 50
        tableView.rowHeight = UITableViewAutomaticDimension
    }
    
    
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    
    // MARK: TableView datasource
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    /*  override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
     
     {
     switch indexPath.section{
     case 0:
     return 60
     case 1:
     if indexPath.row == 0 {
     return 40
     }
     if offers.count == 0 {
     return 40
     } else {
     return 80
     }
     case 2:
     return 60
     default:
     return 60
     }
     } */
    
    override func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let  headerCell = tableView.dequeueReusableCell(withIdentifier: "BasketRewardHeaderTableViewCell") as! BasketRewardHeaderTableViewCell
        
        switch section{
        case 0:
            headerCell.headerLabel.text = "Rewards"
            
        case 1:
            headerCell.headerLabel.text =  "Offers"
        case 2:
            headerCell.headerLabel.text =  "Promotion Code"
        default:
            headerCell.headerLabel.text =  ""
        }
        
        return headerCell
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        switch section{
        case 0:
            return max(rewards.count, 1)
        case 1:
            return max(offers.count + 1, 2) //+1 for offer instruction
        case 2:
            return 1
        default:
            return 1
        }
    }
    
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            if indexPath.row < rewards.count {
                let reward = rewards[indexPath.row]
                let cell = tableView.dequeueReusableCell(withIdentifier: "BasketRewardTableViewCell", for: indexPath) as! BasketRewardTableViewCell
                cell.nameLabel.text = reward.name
                
                
                cell.rewardApplyBtn.addTarget(self, action: #selector(BasketRewardTableViewController.btnrewardApply(_:)), for: .touchUpInside)
                cell.rewardApplyBtn.tag = indexPath.row;
                cell.rewardApplyBtn.layer.borderColor = UIColor(hex: Constants.jambaOrangeColor).cgColor
                cell.rewardApplyBtn.layer.borderWidth = 1.0
                cell.rewardApplyBtn.isHidden = false
                if let basket = BasketService.sharedBasket{
                    if basket.appliedRewards.count == 0{
                        cell.rewardApplyBtn.setTitle("Apply", for: UIControlState())
                    }else{
                        cell.rewardApplyBtn.setTitle("Remove", for: UIControlState())
                        cell.selectionStyle = .default
                    }
                }
                return cell
            } else {
                let cell = tableView.dequeueReusableCell(withIdentifier: "BasketRewardTableViewCell", for: indexPath) as! BasketRewardTableViewCell
                cell.nameLabel.text = "You have no rewards"
                cell.rewardApplyBtn.isHidden = true
                cell.selectionStyle = .none
                return cell
            }
        }
        
        if indexPath.section == 1 {
            if indexPath.row <= offers.count {
                if indexPath.row == 0 {
                    let cell = tableView.dequeueReusableCell(withIdentifier: "OfferInstructionTableCell")!
                    cell.layoutMargins = UIEdgeInsets.zero
                    cell.selectionStyle = .none
                    return cell
                    
                } else {
                    //  filter basket reward
                    let offer = offers[indexPath.row - 1]
                    let cell = tableView.dequeueReusableCell(withIdentifier: "RewardTableViewCell", for: indexPath) as! RewardTableViewCell
                    
                    cell.nameLabel.text = offer.offerTitle
                    cell.nameLabel.lineBreakMode = .byWordWrapping
                    
                    cell.descLabel.text = offer.desc
                    cell.descLabel.lineBreakMode = .byWordWrapping
                    
                    print(offer.offerValidity)
                    
                    if offer.offerValidity.characters.count > 0
                    {
                        cell.quantityLabel.text = offer.offerValidity.convertStringToDate(offer.offerValidity)
                    }
                    else
                    {
                        cell.quantityLabel.text = "Never Expires"
                    }
                    cell.btnApply.addTarget(self, action: #selector(BasketRewardTableViewController.btnofferApply(_:)), for: .touchUpInside)
                    cell.btnApply.tag = indexPath.row - 1;
                    cell.btnApply.layer.borderColor = UIColor(hex: Constants.jambaOrangeColor).cgColor
                    cell.btnApply.layer.borderWidth = 1.0
                    
                    if BasketService.sharedBasket?.promotionCode == offers[indexPath.row - 1].promotionCode && BasketService.sharedBasket?.discount != 0 {
                        cell.btnApply.setTitle("Remove", for: UIControlState())
                    }
                    else{
                        cell.btnApply.setTitle("Apply", for: UIControlState())
                    }
                    return cell
                }
            } else {
                
                let cell = tableView.dequeueReusableCell(withIdentifier: "BasketRewardTableViewCell", for: indexPath) as! BasketRewardTableViewCell
                cell.nameLabel.text =  "No Offers available"
                cell.rewardApplyBtn.isHidden = true
                cell.selectionStyle = .none
                return cell
            }
        }
        else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "EnterPromotionTableViewCell")!
            return cell
        }
    }
    @objc func btnrewardApply(_ sender: UIButton){
        let reward = rewards[sender.tag]
        // Fishbowl event
        var rewardMembershipId = ""
        var rewardReference = ""
        if let rewardMemId = reward.membershipId{
            rewardMembershipId = String(rewardMemId)
        }
        if let rewardReferenceValue = reward.reference{
            rewardReference = rewardReferenceValue
        }
        if sender.titleLabel?.text == "Apply" {
            let rewardName = "REWARD_TITLE:\(reward.name);MEMBERSHIP_ID:\(rewardMembershipId);REFERENCE:\(rewardReference)"
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent("", item_name: rewardName, event_name: "APPLY_REWARD")
            // Validate reward
            self.validateAppliedRewardAndOffer(reward)
        }else{
            let rewardName = "REWARD_TITLE:\(reward.name);MEMBERSHIP_ID:\(rewardMembershipId);REFERENCE:\(rewardReference)"
            FishbowlApiClassService.sharedInstance.submitMobileAppEvent("", item_name: rewardName, event_name: "REMOVE_REWARD")
            SVProgressHUD.show(withStatus: "Removing reward...")
            SVProgressHUD.setDefaultMaskType(.clear)
            BasketService.removeRewards({ (basket, error) in
                SVProgressHUD.dismiss()
                if error != nil {
                    self.presentError(error)
                    return
                }
                self.popToRootViewController()
            })
        }
    }
    @objc func btnofferApply(_ sender: UIButton){
        selectedOfferPromo = offers[sender.tag]
        if sender.titleLabel?.text == "Apply" {
            // Validate offer and reward
            self.validateAppliedRewardAndOffer(nil)
            
        } else{
            // Fishbowl Event
            var itemId = ""
            var itemName = ""
            if let selectedOffer = selectedOfferPromo{
                itemId = String(selectedOffer.id)
                itemName = selectedOffer.offerTitle
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
    
    // MARK: TableView delegate
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.section == 1{
            if indexPath.row == 0{
                return
            }
            if offers.count > 0{
                offer = offers[indexPath.row-1]
                //show offerdetail screen to rewards storyboard
                var mainView: UIStoryboard!
                mainView = UIStoryboard(name: "Rewards", bundle: nil)
                let viewcontroller = mainView.instantiateViewController(withIdentifier: "OfferdetailVC") as! OfferDetailViewController
                viewcontroller.offerDetail = offer
                viewcontroller.isFromBasket = true
                self.navigationController?.pushViewController(viewcontroller, animated: true)
            }
        }
        
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    
    func daysFrom(_ date:Date) -> Int{
        let todaydate = Date()
        return (Calendar.current as NSCalendar).components(.day, from: todaydate, to: date, options: []).day!
    }
    
    fileprivate func applyReward(_ reward: Reward) {
        SVProgressHUD.show(withStatus: "Applying reward...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.applyReward(reward) { (basket, error) in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            self.popViewController()
        }
    }
    
    @objc func basketRefreshed(){
        self.tableView.reloadData()
    }
    
    func validateAppliedRewardAndOffer(_ reward: Reward?){
        if BasketService.validateAppliedOffer(){
            self.presentConfirmationWithYesOrNo("Alert", message: "Only one coupon / reward may be applied at a time. Do you want to remove the existing coupon / reward and apply a new one?", buttonTitle: "Yes") { (confirmed) in
                if confirmed{
                    if reward != nil{
                        SVProgressHUD.show(withStatus: "Applying reward...")
                        SVProgressHUD.setDefaultMaskType(.clear)
                    }else{
                        SVProgressHUD.show(withStatus: "Applying Promocode...")
                        SVProgressHUD.setDefaultMaskType(.clear)
                    }
                    BasketService.removePromotionCode({ (basket, error) in
                        if error != nil{
                            SVProgressHUD.dismiss()
                            self.presentError(error)
                        }else{
                            if reward != nil{
                                self.applyReward(reward!)
                            }else{
                                self.applyingPromoCode()
                            }
                        }
                    })
                } else{
                    return
                }
            }
        } else if BasketService.validateAppliedReward(){
            if ((reward != nil) && (BasketService.sharedBasket?.appliedRewards.first?.rewardId == reward!.rewardId)){
                self.popViewController()
                return
            } else{
                self.presentConfirmationWithYesOrNo("Alert", message: "Only one coupon / reward may be applied at a time. Do you want to remove the existing coupon / reward and apply a new one?", buttonTitle: "Yes") { (confirmed) in
                    if confirmed{
                        if reward != nil{
                            SVProgressHUD.show(withStatus: "Applying reward...")
                            SVProgressHUD.setDefaultMaskType(.clear)
                        }else{
                            SVProgressHUD.show(withStatus: "Applying Promocode...")
                            SVProgressHUD.setDefaultMaskType(.clear)
                        }
                        BasketService.removeRewards({ (basket, error) in
                            if error != nil{
                                SVProgressHUD.dismiss()
                                self.presentError(error)
                            }else{
                                if reward != nil{
                                    self.applyReward(reward!)
                                }else{
                                    self.applyingPromoCode()
                                }
                            }
                        })
                    } else{
                        return
                    }
                }
            }
        }else{
            if reward != nil{
                self.applyReward(reward!)
            }else{
                self.applyingPromoCode()
            }
        }
    }
    
    func applyingPromoCode(){
        if selectedOfferPromo == nil{
            return
        }
        SVProgressHUD.show(withStatus: "Applying promotion code...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.applyPromotionCode(selectedOfferPromo!.promotionCode) { (basket, error) -> Void in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
            }else{
                self.popToRootViewController() // Close all the way to basket screen
            }
        }
    }
    
    func availableRewardsAndOffers(){
        SVProgressHUD.show(withStatus: "Loading rewards/offers...")
        SVProgressHUD.setDefaultMaskType(.clear)
        let group = DispatchGroup()
        group.enter()
        BasketService.availableRewards { (rewards, error) -> Void in
            if error != nil {
                self.refreshOloError = self.refreshRewardAndOfferError
                log.warning("WARNING: \(String(describing: error?.localizedDescription))")
                group.leave()
                return
            }
            self.rewards = rewards
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
                        self.refreshFishbowlError = error
                        group.leave()
                        return
                    }
                    let offers = FishbowlOfferSummary(json: JSON(response!))
                    OfferService.sharedInstance.updateOffers(offers);
                    let rawOffers = OfferService.sharedInstance.offersSummary?.offerList ?? []
                    self.offers = self.filterdOffersForBasket(offers: rawOffers)
                    group.leave()
                }
            }
        }
        group.notify(queue: .main) {
            UIApplication.inMainThread {
                SVProgressHUD.dismiss()
                self.tableView.reloadData()
            }
        }
    }
    
    func filterdOffersForBasket(offers:[FishbowlOffer]) -> [FishbowlOffer]{
        let currentStore = CurrentStoreService.sharedInstance.currentStore
        var fbFilteredOffers:[FishbowlOffer] = []
        // Get only current store offer
        for offer in offers{
            if (offer.storeRestriction.contains(where: {Int($0.storecode ?? "0") == Int(currentStore!.storeCode)})){
                fbFilteredOffers.append(offer)
            }
            if offer.storeRestriction.count == 0{
                fbFilteredOffers.append(offer)
            }
        }
        return fbFilteredOffers.filter({$0.onlineInStore == "1" || $0.onlineInStore == "2"})
    }
}
