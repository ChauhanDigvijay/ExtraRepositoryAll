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
    func existingGiftCardAdded(inCommCardId:Int32)
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
        tableView.separatorStyle = UITableViewCellSeparatorStyle.None
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
    @IBAction func addCardButtonPressed(sender:UIButton){
        SVProgressHUD.showWithStatus("Adding GiftCard...",maskType: .Clear)
        GiftCardCreationService.sharedInstance.associateGiftCardToUser(inCommCard.cardNumber, cardPin: inCommCard.cardPin) { (inCommUserGiftCard, error) in
            SVProgressHUD.dismiss()
            if(error != nil){
                self.presentError(error)
            } else {
                //track clyp analytics event for adding existing gift card to user account
                clpAnalyticsService.sharedInstance.clpTrackScreenView("GiftCardAdded");
                self.presentOkAlert("Success", message: "New card added to account successfully", callback: {
                    self.navigationController?.dismissViewControllerAnimated(true, completion: {
                        self.delegate?.existingGiftCardAdded(inCommUserGiftCard!.cardId)
                    })
                })
            }
        }
    }
    
    // MARK: Tableview delegate
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tableViewCellTitles.count + 1
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if indexPath.row == 0{
            let cell = tableView.dequeueReusableCellWithIdentifier(TableViewCellIdentifier.GiftCardImage.rawValue, forIndexPath: indexPath) as! GiftCardImageTableViewCell
            cell.setGiftCardImage(inCommCard.imageUrl)
            return cell
        }
        else{
            let cell = tableView.dequeueReusableCellWithIdentifier(TableViewCellIdentifier.GiftCardDetail.rawValue, forIndexPath: indexPath) as! GiftCardDetailTableViewCell
            cell.setData(tableViewCellTitles[indexPath.row-1], cellValue: tableViewCellValues[indexPath.row-1])
            return cell
        }
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
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
