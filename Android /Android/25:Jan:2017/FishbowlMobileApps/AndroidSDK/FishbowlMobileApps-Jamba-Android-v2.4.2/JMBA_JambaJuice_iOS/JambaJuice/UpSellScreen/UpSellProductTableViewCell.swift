//
//  UpSellProductTableViewCell.swift
//  JambaJuice
//
//  Created by VT010 on 10/21/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit
import OloSDK

protocol UpSellProductTableViewCellDelegate: class {
    func showProductQuantityPickerView(_ selectedUpSellItem:SelecttedUpSellItem)
}

class UpSellProductTableViewCell: UITableViewCell {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var selectedImageView: UIImageView!
    @IBOutlet weak var quantityTextBox: UITextField!
    @IBOutlet weak var quatityButton: UIButton!
    @IBOutlet weak var quatityButtonLeadingConstraint: NSLayoutConstraint!
    weak var delegate  : UpSellProductTableViewCellDelegate?
    
    var upSellItem:SelecttedUpSellItem!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    
    //show quantity selection field with animation
    func showQuantityField() {
        selectedImageView.isHidden = false
        quantityTextBox.isHidden = false
        quatityButton.isHidden = false
        updatePriceLabel(String(upSellItem.selectedQuantity))
        UIView.animate(withDuration: 0.35, animations: {
            self.quatityButtonLeadingConstraint.constant = 0
            self.layoutIfNeeded()
        })
        
    }
    
    //hide quantity selection field with animation
    func hideQuantityField() {
        UIView.animate(withDuration: 0.35, animations: {
            self.quatityButtonLeadingConstraint.constant = -46
            self.layoutIfNeeded()
        }, completion: { (success) in
            self.selectedImageView.isHidden = true
            self.quantityTextBox.isHidden = true
            self.quatityButton.isHidden = true
            let priceArray = self.priceLabel.text!.characters.split{$0 == "$"}.map(String.init)
            if priceArray.count > 0 {
                self.priceLabel.text = "$\(priceArray[priceArray.count - 1])"
            }
        })
    }
    
    //show quantity selection field without animation
    func showQuantityFieldWithoutAnimation() {
        selectedImageView.isHidden = false
        quantityTextBox.isHidden = false
        quatityButton.isHidden = false
        updatePriceLabel(String(upSellItem.selectedQuantity))
        quatityButtonLeadingConstraint.constant = 0
    }
    
    //hide quantity selection field without animation
    func hideQuantityFieldWithoutAnimation() {
        self.quatityButtonLeadingConstraint.constant = -46
        self.selectedImageView.isHidden = true
        self.quantityTextBox.isHidden = true
        self.quatityButton.isHidden = true
        let priceArray = self.priceLabel.text!.characters.split{$0 == "$"}.map(String.init)
        if priceArray.count > 0 {
            self.priceLabel.text = "$\(priceArray[priceArray.count - 1])"
        }
    }
    
    //update price label with exact quantity
    func updatePriceLabel(_ value:String) {
        var priceArray = priceLabel.text!.characters.split{$0 == "$"}.map(String.init)
        if priceArray.count > 0 {
            let price: String = priceArray[priceArray.count - 1]
            priceLabel.text = "\(value) x $\(price)"
            quantityTextBox.text = value
        }
    }
    
    //show the quantity picker for product modifier options
    @IBAction func showPicker(_ sender:UIButton) {
        delegate?.showProductQuantityPickerView(upSellItem)
    }
    
    func setData(_ indexPathRow:Int,upSellItem:SelecttedUpSellItem){
        self.upSellItem = upSellItem
        nameLabel.text = upSellItem.name
        priceLabel.text = upSellItem.cost
    }
}
