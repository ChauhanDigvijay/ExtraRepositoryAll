//
//  GiftCardListViewController.swift
//  JambaJuice
//
//  Created by vThink on 9/13/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
import InCommSDK

class GiftCardListViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    @IBOutlet weak var tableView            :   UITableView!
    @IBOutlet weak var emptyGiftCardView    :   UIView!
    @IBOutlet weak var placeOrderButton     :   UIButton!
    
    var userGiftCards: [InCommUserGiftCard]?{
        get{
            // Get user gift card from shared instance
            return GiftCardCreationService.sharedInstance.inCommUserGiftCards
        }
    }
    var selectedGiftCard: InCommUserGiftCard?
    
    // MARK: - View did load
    override func viewDidLoad() {
        emptyGiftCardView.hidden = true
        tableView.hidden = true
        getUserGiftCards()
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    // MARK: - Did receive memory warning
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - Table view delegate
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let giftCards = userGiftCards{
            return giftCards.count
        }
        return 0
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("GiftCardTableViewCell", forIndexPath: indexPath) as! GiftCardTableViewCell
        let cellGiftCard = userGiftCards![indexPath.row]
        if selectedGiftCard != nil{
            if cellGiftCard.cardId == selectedGiftCard!.cardId{
                cell.setData(cellGiftCard, selectedGiftCard: cellGiftCard)
            }
            else{
                cell.setData(cellGiftCard, selectedGiftCard: nil)
            }
        }
        else{
            cell.setData(cellGiftCard, selectedGiftCard: nil)
        }
        return cell
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
        if selectedGiftCard?.cardId == userGiftCards![indexPath.row].cardId{
            selectedGiftCard = nil
            tableView.reloadData()
        }
        else{
            selectedGiftCard = userGiftCards![indexPath.row]
            tableView.reloadData()
        }
    }
    
    // MARK: - Get user gift cards
    func getUserGiftCards(){
        SVProgressHUD.showWithStatus("Getting gift cards...",maskType: . Clear)
        GiftCardCreationService.sharedInstance.getAllGiftCards { (userGiftCards, error) in
            SVProgressHUD.dismiss()
            if error != nil{
                self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed{
                        self.getUserGiftCards()
                    }
                    else{
                        self.popViewController()
                    }
                })
            }
            else{
                self.validateGiftCards()
            }
        }
    }
    
    // MARK: - Validate gift cards
    func validateGiftCards(){
        if let giftcards = userGiftCards{
            if giftcards.count == 0{
                emptyGiftCardView.hidden = false
                tableView.hidden = true
                placeOrderButton.hidden = true
                return
            }
            else{
                emptyGiftCardView.hidden = true
                tableView.hidden = false
                placeOrderButton.hidden = false
                tableView.reloadData()
                return
            }
        }
        else{
            getUserGiftCards()
        }
    }
    
    // MARK: - Place order
    @IBAction func placeOrder(sender:UIButton){
        if selectedGiftCard == nil{
            self.presentOkAlert("Alert", message: "Please select any one gift card")
        }
        else{
            sender.enabled = false
            SVProgressHUD.showWithStatus("Processing order...", maskType: .Clear)
            BasketService.placeOrderWithGiftCard(selectedGiftCard!, callback: { (orderStatus, error) in
                SVProgressHUD.dismiss()
                sender.enabled = true
                if error != nil{
                    self.presentError(error)
                    return
                }
                else{
                    (self.navigationController as! BasketNavigationController).dismiss()
                    
                    GiftCardCreationService.sharedInstance.resetInCommUserGiftCards()
                    
                    // TODO: Home Controller should listen to this to make get recent orders call (no need to pass order status here)
                    NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.OrderPlaced.rawValue, object: self, userInfo: nil)
                    
                    BasketService.lastOrderStatus = orderStatus!
                    let vc = UIViewController.instantiate("OrderConfirmationVC", storyboard: "Main") as! OrderConfirmationViewController
                    self.view.window!.addSubview(vc.view)
                }
            })
        }
    }
    
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}
