//
//  ExistingGiftCardDetailsViewController.swift
//
//  Created by vThink on 6/8/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import HDK
import SVProgressHUD

// MARK: - Delegate declaration
protocol ExistingGiftCardDetailsViewControllerDelegate:class{
    func existingGiftCardAdded(_ inCommCardId:Int32)
}

// MARK: - Enum declaration
// Table view cell identifier
enum TableViewCellIdentifier:String{
    case GiftCardImage  =   "GiftCardImageTableViewCell"
    case GiftCardDetail =   "GiftCardDetailTableViewCell"
}

// Table view cell constants
let cellSpaceForWidth                 :  CGFloat    =   25
let cellSpaceForHeight                :  CGFloat    =   10
let giftCardDetailTableViewCellHeight :  CGFloat    =   60

class ExistingGiftCardDetailsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    @IBOutlet weak var tableView:UITableView!
    var inCommCard: InCommCard!
    // Table view cell titles and values
    let tableViewCellTitles:[String] = ["Name", "Number", "Balance"]
    var tableViewCellValues:[String] = []
    var delegate:ExistingGiftCardDetailsViewControllerDelegate?
    
    // MARK: - View did load
    override func viewDidLoad() {
        tableView.separatorStyle = UITableViewCellSeparatorStyle.none
        tableView.allowsSelection = false
        tableViewCellValues.append(inCommCard.cardName)
        tableViewCellValues.append(inCommCard.cardNumber)
        tableViewCellValues.append(String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,inCommCard.balance))
        super.viewDidLoad()
    }
    
    // MARK: - Did receive memory warning
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    // MARK: - Add card button pressed
    @IBAction func addCardButtonPressed(_ sender:UIButton){
        SVProgressHUD.show(withStatus: "Adding GiftCard...")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService.sharedInstance.associateGiftCardToUser(inCommCard.cardNumber, cardPin: inCommCard.cardPin) { (inCommUserGiftCard, error) in
            SVProgressHUD.dismiss()
            if(error != nil){
                self.presentError(error)
            } else {
                //track clyp analytics event for adding existing gift card to user account
                //clpAnalyticsService.sharedInstance.clpTrackScreenView("GiftCardAdded");
                FishbowlApiClassService.sharedInstance.submitMobileAppEvent("GIFT_CARD_ADDED")
                self.presentOkAlert("Success", message: "New card added to account successfully", callback: {
                    self.navigationController?.dismiss(animated: true, completion: {
                        self.delegate?.existingGiftCardAdded(inCommUserGiftCard!.cardId)
                    })
                })
            }
        }
    }
    
    // MARK: Tableview delegate
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableViewCellTitles.count + 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0{
            let cell = tableView.dequeueReusableCell(withIdentifier: TableViewCellIdentifier.GiftCardImage.rawValue, for: indexPath) as! GiftCardImageTableViewCell
            cell.setGiftCardImage(inCommCard.imageUrl)
            return cell
        }
        else{
            let cell = tableView.dequeueReusableCell(withIdentifier: TableViewCellIdentifier.GiftCardDetail.rawValue, for: indexPath) as! GiftCardDetailTableViewCell
            cell.setData(tableViewCellTitles[indexPath.row-1], cellValue: tableViewCellValues[indexPath.row-1])
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0{
            var tableViewWidth = tableView.frame.size.width - cellSpaceForWidth
            if !GiftCardAppConstants.isiPhone{
                tableViewWidth = tableView.frame.size.width /  GiftCardAppConstants.GiftCardRatioWidth
            }
            return (tableViewWidth * GiftCardAppConstants.GiftCardRatioHeight) + GiftCardAppConstants.tableViewGiftCardTopAndsBottomSpace
        }
        else{
            return giftCardDetailTableViewCellHeight
        }
    }
}
