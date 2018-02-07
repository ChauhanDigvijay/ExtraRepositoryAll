//
//  EnterDeliveryAddressViewController.swift
//  JambaJuice
//
//  Created by VT016 on 10/03/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import UIKit
import OloSDK
import SVProgressHUD

enum AddressFieldsLengths:Int{
    case streetAddress = 128
    case buildingAddress = 64
    case cityNameAndInstructions = 32
    case zipCode = 7
}

class EnterDeliveryAddressViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate {
    
    @IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var saveButton:UIButton!
    @IBOutlet weak var saveButtonBottomSpace:NSLayoutConstraint!
    var keyboardHeight:CGFloat = 0
    var isView:Bool = false
    
    //fileds name
    var fieldName:[String] = ["Street Address ", "Building Name/ Suite / Apt ","City","Zip Code","Contact Number", "Other Instructions (cross street, buzzer, #, ....)"]
    
    var deliveryAddress:OloDeliveryAddress = OloDeliveryAddress(id: "", building: "", streetaddress: "", city: "", zipcode: "", phonenumber: "", specialinstructions: "")
    
    override func viewDidLoad() {
        
        //keyboared did show notification
        NotificationCenter.default.addObserver(self, selector: #selector(EnterDeliveryAddressViewController.keyboardDidShow(_:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(EnterDeliveryAddressViewController.keyboardWillDismiss(_:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        
        
        if deliveryAddress.id == "" && UserService.sharedUser != nil {
            deliveryAddress.phonenumber = UserService.sharedUser!.phoneNumber.formatPhoneNumber()
        }
        
        if isView {
            saveButton.setTitle("Delete", for: UIControlState())
        } else {
            self.title = "New Address"
        }
    }
    
    //MARK:- Table View
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if isView {
            return fieldName.count - 1
        }
        return fieldName.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "EnterDeliveryAddressTableViewCell") as! EnterDeliveryAddressTableViewCell
        
        //skip mobile number if the record only view
        var index = indexPath.row
        if isView && indexPath.row == fieldName.count - 2 {
            index = indexPath.row + 1
        }
        
        cell.textField.tag = indexPath.row
        
        if index == 1 || index == 5 {
            cell.textField.attributedPlaceholder = NSAttributedString(string: fieldName[index],
                                                                      attributes: [NSAttributedStringKey.foregroundColor: UIColor(hex: Constants.jambaGrayColor)])
            cell.textField.textColor = UIColor(hex: Constants.jambaMediumGrayColor)
        } else {
            cell.textField.attributedPlaceholder = NSAttributedString(string: fieldName[index],
                                                                      attributes: [NSAttributedStringKey.foregroundColor: UIColor(hex: Constants.jambaDarkGrayColor)])
            cell.textField.textColor = UIColor(hex: Constants.jambaMediumGrayColor)
            
            if index == 4 || index == 3{
                cell.textField.keyboardType = .numberPad
                let ViewForDoneButtonOnKeyboard = UIToolbar()
                ViewForDoneButtonOnKeyboard.sizeToFit()
                let btnDoneOnKeyboard = UIBarButtonItem(title: "Done", style: .done, target: self, action: #selector(self.doneBtnfromKeyboardClicked))
                let flexSpace = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.flexibleSpace, target: nil, action: nil)
                ViewForDoneButtonOnKeyboard.items = [flexSpace,btnDoneOnKeyboard]
                
                cell.textField.inputAccessoryView = ViewForDoneButtonOnKeyboard
            }
            
        }
        if isView {
            cell.textField.isEnabled = false
        }
        cell.textField.text = getCellValue(index)
        return cell
    }
    
    //MARK:- IBAction
    func textFieldDidBeginEditing(_ textField: UITextField) {
        if let cell = tableView.cellForRow(at: IndexPath(row: textField.tag, section: 0)) as? EnterDeliveryAddressTableViewCell {
            if textField.tag < fieldName.count - 1 {
                cell.textField.returnKeyType = .next
            } else {
                cell.textField.returnKeyType = .done
            }
        }
    }
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        let textValue:String = (textField.text! as NSString).replacingCharacters(in: range, with: string)
        saveCellValue(textField.tag, value: textValue )
        if textField.tag == 0{
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: AddressFieldsLengths.streetAddress.rawValue)
        }
        if textField.tag == 1{
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: AddressFieldsLengths.buildingAddress.rawValue)
        }
        if textField.tag == 2{
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: AddressFieldsLengths.cityNameAndInstructions.rawValue)
        }
        if textField.tag == 3{
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: AddressFieldsLengths.zipCode.rawValue)
        }
        if  textField.tag == 4 {
            textField.text = textValue.trim().formatPhoneNumber()
            return false
        }
        if textField.tag == 5{
            return textField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: AddressFieldsLengths.cityNameAndInstructions.rawValue)
        }
        return true
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if let cell = tableView.cellForRow(at: IndexPath(row: textField.tag + 1, section: 0)) as? EnterDeliveryAddressTableViewCell {
            if textField.tag < fieldName.count - 1 {
                cell.textField.becomeFirstResponder()
            }
        }else{
            textField.resignFirstResponder()
        }
        return true
    }
    
    //MARK:- IB Action
    @IBAction func saveValues(_ sender: UITextField) {
        saveCellValue(sender.tag, value: sender.text ?? "")
        
        if sender.tag == 4 {
            sender.text = sender.text!.trim().formatPhoneNumber()
        }
    }
    
    //textfield begin editing
    @objc(textFieldDidEndEditing:) @IBAction func textFieldDidEndEditing(_ sender: UITextField) {
        if let cell = tableView.cellForRow(at: IndexPath(row: sender.tag, section: 0)) as? EnterDeliveryAddressTableViewCell {
            if sender.tag < fieldName.count - 1 {
                cell.textField.returnKeyType = .next
            } else {
                cell.textField.returnKeyType = .done
            }
        }
    }
    
    @IBAction func applydeliveryAddress() {
        //remove Address
        if isView {
            SVProgressHUD.show(withStatus: "Removing Address...")
            SVProgressHUD.setDefaultMaskType(.clear)
            BasketService.removeDeliveryAddress(deliveryAddress.id, callback: { (error) in
                SVProgressHUD.dismiss()
                if error != nil {
                    self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry", callback: { (success) in
                        if success {
                            
                            self.applydeliveryAddress()
                            
                        }
                    })
                } else {
                    //if the deleting delivery address is selected in basket Then switch the basket mode to pickup
                    if BasketService.sharedBasket!.deliveryAddress != nil && self.deliveryAddress.id == BasketService.sharedBasket!.deliveryAddress!.id {
                        self.switchBasketMode(deliveryMode.pickup.rawValue)
                    } else {
                        _ = self.navigationController?.popToRootViewController(animated: true)
                    }
                }
            })
        } else {
            //Save Address
            if validateDeliveryAddress() {
                deliveryAddress.id = "0"
                SVProgressHUD.show(withStatus: "Saving Address...")
                SVProgressHUD.setDefaultMaskType(.clear)
                BasketService.addDeliveryAddress(deliveryAddress, callback: { (basket, error) in
                    SVProgressHUD.dismiss()
                    if basket != nil{
                        let action = UIAlertAction(title: "OK", style: .default, handler: { (confirmed) in
                            NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.OrderdeliveryModeChanged.rawValue), object: nil)
                            _ = self.navigationController?.popToRootViewController(animated: true)
                        })
                        self.presentAlert("Message", message: "Estimated delivery time is \(Int(basket!.leadTimeEstimateMinutes )) mins.", actions: action)
                    }else if error != nil{
                        self.presentError(error)
                    }else {
                        self.presentOkAlert("Error", message: Constants.genericErrorMessage)
                    }
                })
            }
        }
    }
    
    //UpdateBasket
    func switchBasketMode(_ mode:String) {
        SVProgressHUD.show(withStatus: "Removing Address...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.changeDeliveryMode(mode) { (basket, error) in
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentConfirmation("Error", message: "Please try again", buttonTitle: "Retry", callback: { (success) in
                    if success {
                        self.applydeliveryAddress()
                    }
                })
            } else {
                BasketService.sharedBasket!.selectedPickupTime = nil
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.OrderdeliveryModeChanged.rawValue), object: nil)
                _ = self.navigationController?.popToRootViewController(animated: true)
            }
        }
    }
    
    //MARK:- Validation
    func validateDeliveryAddress() -> Bool {
        if deliveryAddress.streetaddress.isEmpty {
            presentOkAlert("Address required", message: "Please enter address.", callback: {
                self.focusTextField(0)
            })
            return false
        }
        if deliveryAddress.city.isEmpty {
            presentOkAlert("City required", message: "Please enter city.", callback: {
                self.focusTextField(2)
            })
            return false
        }
        if deliveryAddress.zipcode.isZipCode() == false {
            presentOkAlert("Zip Code required", message: "Please enter valid zip code.", callback: {
                self.focusTextField(3)
            })
            return false
        }
        if deliveryAddress.phonenumber.isTenDigitPhone() == false {
            presentOkAlert("Contact Number required", message: "Please enter valid contact number") {
                self.focusTextField(4)
            }
            return false
        }
        return true
    }
    
    func focusTextField(_ index:Int) {
        if let cell = tableView.cellForRow(at: IndexPath(row: index, section: 0)) as? EnterDeliveryAddressTableViewCell {
            cell.textField.becomeFirstResponder()
        }
    }
    
    //MARK:- Get delivery Address values
    func getCellValue(_ index: Int) -> String {
        switch index {
        case 0:
            return deliveryAddress.streetaddress
        case 1:
            return deliveryAddress.building
        case 2:
            return deliveryAddress.city
        case 3:
            return deliveryAddress.zipcode
        case 4:
            return deliveryAddress.phonenumber
        case 5:
            return deliveryAddress.specialinstructions
        default:
            return ""
        }
    }
    
    //MARK:- Save delivery Address
    func saveCellValue(_ index: Int, value: String) {
        switch index {
        case 0:
            deliveryAddress.streetaddress = value.trim()
        case 1:
            deliveryAddress.building = value.trim()
        case 2:
            deliveryAddress.city = value.trim()
        case 3:
            deliveryAddress.zipcode = (value.trim())
        case 4:
            deliveryAddress.phonenumber = value.trim().formatPhoneNumber()
        case 5:
            deliveryAddress.specialinstructions = value.trim()
        default: break
            
        }
    }
    
    //MARK:- Keyboard Did show
    //when showing keyboard, the Apply automatically scroll up
    @objc func keyboardDidShow(_ notification: Notification) {
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        let keyboardFrame:NSValue = userInfo.value(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
        let keyboardRectangle = keyboardFrame.cgRectValue
        self.keyboardHeight = keyboardRectangle.height
        
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.saveButtonBottomSpace.constant = self.keyboardHeight
            self.view.layoutIfNeeded()
        }, completion: { (completed) in
            
        }) 
    }
    
    //dismiss keyboard
    @objc func keyboardWillDismiss(_ notification: Notification) {
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.saveButtonBottomSpace.constant = 0
            self.view.layoutIfNeeded()
        }) 
    }
    
    @IBAction func doneBtnfromKeyboardClicked (sender: Any) {
        print("Done Button Clicked.")
        //Hide Keyboard by endEditing or Anything you want.
        self.view.endEditing(true)
    }
    
}

extension String {
    func formatPhoneNumber() -> String {
        var formattedNumber = self
        formattedNumber = formattedNumber.stringByRemovingNonNumericCharacters()
        
        if formattedNumber.characters.count >= 10 {     //check maximum limit
            let index = formattedNumber.index(formattedNumber.startIndex, offsetBy: 10)
            formattedNumber = String(formattedNumber[..<index])
        }
        
        //format mobile number
        if (formattedNumber.characters
            .count > 0 ) {
            var formattedString = "";
            for index in 0..<formattedNumber.characters
                .count {
                if (index % 3==0 && (index > 0 && index < 8)) {
                    let charIndex = formattedNumber.index(formattedNumber.startIndex, offsetBy: index)
                    formattedString += "-" + String(formattedNumber[charIndex])
                } else {
                    let charIndex = formattedNumber.index(formattedNumber.startIndex, offsetBy: index)
                    formattedString += String(formattedNumber[charIndex])
                }
            }
            return formattedString
        }
        return formattedNumber
    }
}
