//
//  ListStoredDeliveryAddress.swift
//  JambaJuice
//
//  Created by VT016 on 13/03/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import UIKit
import OloSDK
import SVProgressHUD

class ListStoredDeliveryAddressViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var noRecordView:UIView!
    
    var savedAddress:[OloDeliveryAddress] = []
    
    var selectedDeliveryAddress:String = ""
    
    override func viewDidLoad() {
        selectedDeliveryAddress = (BasketService.sharedBasket!.deliveryAddress!.id)
        tableView.delegate = self
        getSavedAddress()
    }
    
    //MARK:- Table View
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return savedAddress.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ListStoredDeliveryAddress") as! ListStoredDeliveryAddressTableViewCell
        
        cell.addressLabel.text = "\(savedAddress[indexPath.row].streetaddress)\n\(savedAddress[indexPath.row].city), \(savedAddress[indexPath.row].zipcode)"
        
        //retain selected info
        if selectedDeliveryAddress == savedAddress[indexPath.row].id {
            cell.isCellSelected.image = UIImage(named: "option_selected")!
        } else {
            cell.isCellSelected.image = UIImage(named: "radio-button-disabled")!
        }
        
        cell.viewButton.tag = indexPath.row
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if let cell = tableView.cellForRow(at: indexPath) as? ListStoredDeliveryAddressTableViewCell {
            cell.isCellSelected.image = UIImage(named: "option_selected")!
            saveSelectedAddress(indexPath.row)
            selectedDeliveryAddress = savedAddress[indexPath.row].id
            tableView.reloadData()
        }
    }
    
    //MARK:- IBAction
    @IBAction func viewParticularAddress(_ sender:UIButton) {
        self.performSegue(withIdentifier: "StoredeliveryAddressVC", sender: sender)
    }
    
    @IBAction func addNewAddress() {
        self.performSegue(withIdentifier: "StoredeliveryAddressVC", sender: nil)
    }
    
    //MARK:- Prepare for Segue
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "StoredeliveryAddressVC" {
            let vc = segue.destination as! EnterDeliveryAddressViewController
            if let index = sender as? UIButton {
                vc.deliveryAddress = savedAddress[index.tag]
                vc.isView = true
            }
         }
    }
    
    
    //MARK:- API Call
    func getSavedAddress() {
        self.noRecordView.isHidden = true
        SVProgressHUD.show(withStatus: "Loading...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.getUserSavedDeliverAddress{ (deliverAddress, error) in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
            } else {
                self.savedAddress = deliverAddress
                if self.savedAddress.count > 0 {
//                    self.noRecordView.hidden = true
                    self.tableView.reloadData()
                } else {
                    self.noRecordView.isHidden = false
                }
            }
        }
    }
    
    func saveSelectedAddress(_ index:Int) {
        SVProgressHUD.show(withStatus: "Loading...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.addDeliveryAddress(savedAddress[index], callback: { (basket, error) in
            SVProgressHUD.dismiss()
            if basket != nil{
                let action = UIAlertAction(title: "OK", style: .default, handler: { (confirmed) in
                   NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.OrderdeliveryModeChanged.rawValue), object: nil)
                    _ = self.navigationController?.popToRootViewController(animated: true)
                })
                self.presentAlert("Message", message: "Estimated delivery time is \(Int(basket!.leadTimeEstimateMinutes )) mins.", actions: action)

            }else if error != nil{
                self.presentOkAlert("Error", message: error!.getErrorMessage())
            }else {
                self.presentOkAlert("Error", message: Constants.genericErrorMessage)
            }
        })
    }
}

extension NSError {
    //get the error message (if technical description is availa)
    func getErrorMessage() -> String {
        let error = self
        if error.userInfo["TechnicalDescription"] != nil && error.userInfo["TechnicalDescription"] as? String != nil {
            return error.userInfo["TechnicalDescription"] as? String ?? ""
        } else {
            return error.localizedDescription
        }
    }
}
