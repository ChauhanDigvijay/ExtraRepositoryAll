//
//  UpSellProductsViewController.swift
//  JambaJuice
//
//  Created by VT010 on 10/21/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit
import OloSDK
import Parse
import SVProgressHUD


class UpSellProductsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource,UpSellProductTableViewCellDelegate, ProductDetailPickerViewControllerDelegate{
    
    @IBOutlet weak var productsTableView:UITableView!
    @IBOutlet weak var pickerView:UIView!
    
    var upSellItems:[SelecttedUpSellItem] = []
    var selectedUpsellItems:[SelecttedUpSellItem] = []
    var currentSelectedUpSellItem:SelecttedUpSellItem?
    var currentPickerViewSelectedUpSellItem:SelecttedUpSellItem?
    var pickerViewController    : ProductDetailPickerViewController?

    override func viewDidLoad() {
        if let upSellGroup = BasketService.sharedBasket?.upSellGroups.first{
            let upSellProductItems = upSellGroup.items
            for upSellProductItem in upSellProductItems{
                upSellItems.append(SelecttedUpSellItem.init(oloUpSellItem: upSellProductItem))
            }
        }
        updateScreen()
        pickerView.isHidden = true
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        StatusBarStyleManager.pushStyle(.default, viewController: self)
    }
        
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func close(){
        self.dismiss(animated: true, completion: nil)
    }
    
    //MARK: TableView Delegate
    
    func numberOfSections(in tableView: UITableView) -> Int {
       return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0{
            return 1
        }else{
        return upSellItems.count
        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0{
           return 234
        }
        else{
            return 68
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 1{
        let cell = tableView.dequeueReusableCell(withIdentifier: "UpSellProductTableViewCell", for: indexPath) as! UpSellProductTableViewCell
        let upSellItem = upSellItems[indexPath.row]
        cell.setData(indexPath.row, upSellItem: upSellItem)
        if checkIteemSelectedOrNot(upsellItem: upSellItem) == true{
            cell.showQuantityField()
        }else{
            cell.hideQuantityField()
        }
        cell.delegate = self
        return cell
        }else{
             let cell = tableView.dequeueReusableCell(withIdentifier: "UpSellProductImagesCollectionTableViewCell", for: indexPath) as! UpSellProductImagesCollectionTableViewCell
            cell.setTimer()
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if let cell = tableView.cellForRow(at: indexPath) as? UpSellProductTableViewCell{
            if checkIteemSelectedOrNot(upsellItem: cell.upSellItem) == true{
                if let index = selectedUpsellItems.index(where: {$0.id == cell.upSellItem.id}){
                    selectedUpsellItems.remove(at: index)
                }
                if let index = upSellItems.index(where: {$0.id == cell.upSellItem.id}){
                    upSellItems[index].updateQuantity(value: String(cell.upSellItem.minquantity))
                }
            }else{
                if (totalSelectedProductsQuantity() >= Constants.oloItemLimit){
                    presentOkAlert("Too Many Items", message: "The maximum number of items allowed per order is 10")
                    return
                }
                currentSelectedUpSellItem = cell.upSellItem
                selectedUpsellItems.append(cell.upSellItem)
            }
        }
        tableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, didEndDisplaying cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if let cell = tableView.cellForRow(at: indexPath) as? UpSellProductImagesCollectionTableViewCell{
            cell.timer = nil
            cell.timer?.invalidate()
        }
    }
    
    //MARK: - UpSellProduct TableViewCell PickerView Deleagate
    func showProductQuantityPickerView(_ selectedUpSellItem: SelecttedUpSellItem) {
        if (Int(Constants.oloItemLimit) - (totalSelectedProductsQuantity() - selectedUpSellItem.selectedQuantity)) == 1{
            return
        }
        currentPickerViewSelectedUpSellItem = selectedUpSellItem
        pickerViewController?.setMaxQuantity(Int(Int(Constants.oloItemLimit) - totalSelectedProductsQuantity() + selectedUpSellItem.selectedQuantity))
        pickerViewController?.picker.reloadAllComponents()
        pickerView.isHidden = false
        pickerViewController?.selectedValue1 = String(selectedUpSellItem.selectedQuantity)
        pickerViewController?.retainSelectedValue()
        pickerViewController?.pickerHeaderTitleLabel.text = "Select Quantity"
        pickerViewController?.showPicker()
    }
    
    //MARK: - PickerView Deleagate
    func pickerValueChanged(_ value: String, index: Int) {
        if currentPickerViewSelectedUpSellItem != nil{
            if let index = selectedUpsellItems.index(where: {$0.id == currentPickerViewSelectedUpSellItem!.id}){
                selectedUpsellItems[index].updateQuantity(value: value)
            }
            if let index = upSellItems.index(where: {$0.id == currentPickerViewSelectedUpSellItem!.id}){
                upSellItems[index].updateQuantity(value: value)
            }
        }
    }
    
    func closepickerScreen() {
        pickerView.isHidden = true
        productsTableView.reloadData()
    }
    
    //MARK: - Segue Method
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "ProductDetailPickerViewController"{
            pickerViewController = segue.destination as? ProductDetailPickerViewController
            pickerViewController?.delegate = self
        }
    }
    
    //MARK: - Get Toatl Quantity
    func totalSelectedProductsQuantity() -> Int{
        var totalQuanitySelected = 0
        for product in (BasketService.sharedBasket?.products)!{
            totalQuanitySelected = totalQuanitySelected + product.quantity
        }
        for product in self.selectedUpsellItems{
            totalQuanitySelected = totalQuanitySelected + product.selectedQuantity
        }
        return totalQuanitySelected
    }
    
    //MARK: - Selected Product Validation
    func checkIteemSelectedOrNot(upsellItem:SelecttedUpSellItem) ->Bool{
        var result:Bool = false
        
        for selectedUpSellItem in selectedUpsellItems {
            if selectedUpSellItem.id == upsellItem.id{
                result = true
                break
            }
        }
        return result
    }

 
    
    //MARK: - Update screen based on upsell product images
    func updateScreen(){
        let upSellImages = UpSellProductsService.sharedInstance.upSellProductImages
        if upSellImages.count == 0 {
            UpSellProductsService.sharedInstance.getUpSellProductsImages(callback: { (upSellProducts, upSellConfig) in
                // Need not to handle callback
                self.productsTableView.reloadData()
            })
        }
    }
    
    @IBAction func addToBasket(){
        if selectedUpsellItems.count == 0{
            presentOkAlert("Invalid Quantity", message: "Please add at least one product")
        }else{
            var oloUpSellRequestItems:OloUpSellRequestItems = OloUpSellRequestItems.init()
            for selectedItem in selectedUpsellItems{
                var oloUpSellItem:OloUpSellRequestItem = OloUpSellRequestItem.init()
                oloUpSellItem.id = selectedItem.id
                oloUpSellItem.quantity = Int64(selectedItem.selectedQuantity)
                oloUpSellRequestItems.items.append(oloUpSellItem)
            }
            SVProgressHUD.show(withStatus: "Adding...")
            SVProgressHUD.setDefaultMaskType(.clear)
            BasketService.addUpSellProducts(oloUpSellRequestItems: oloUpSellRequestItems, callback: { (basket, error) in
                SVProgressHUD.dismiss()
                if error != nil{
                    self.presentError(error)
                }else{
                    // Update no thanks option
                    BasketService.sharedBasket?.updateNoThanksOption(enable: false)
                    self.close()
                }
            })
        }
    }
    
    @IBAction func noThanks(){
        BasketService.sharedBasket?.updateNoThanksOption(enable: true)
        self.dismiss(animated: true) {
            NotificationCenter.default.post(name: NSNotification.Name(rawValue: JambaNotification.ProceedOrder.rawValue), object: nil)
        }
    }
    
    deinit{
        NotificationCenter.default.removeObserver(self)
        StatusBarStyleManager.popStyle(self)
    }
}
