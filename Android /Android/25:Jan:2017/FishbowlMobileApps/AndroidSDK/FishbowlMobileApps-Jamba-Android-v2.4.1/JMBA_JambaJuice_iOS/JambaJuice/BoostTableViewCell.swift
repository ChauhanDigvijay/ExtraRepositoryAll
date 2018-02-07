//
//  BoostTableViewCell.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/1/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol BoostTableViewCellDelegate: class {
    func showProductQuantityPickerView(_ index:Int, selectedValue:String, parentModifier: StoreMenuProductModifier, option: StoreMenuProductModifierOption)
}

class BoostTableViewCell: UITableViewCell {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var priceLabel: UILabel!
    @IBOutlet weak var selectedImageView: UIImageView!
    @IBOutlet weak var quantityTextBox: UITextField!
    @IBOutlet weak var quatityButton: UIButton!
    @IBOutlet weak var quatityButtonLeadingConstraint: NSLayoutConstraint!
    weak var delegate  : BoostTableViewCellDelegate?
    
    var option: StoreMenuProductModifierOption!
    var parentModifier: StoreMenuProductModifier!
    
    //For ease of reference
    var modifierIndex: Int!
    var optionIndex: Int!
    
    //show quantity selection field with animation
    func showQuantityField() {
        selectedImageView.isHidden = false
        quantityTextBox.isHidden = false
        quatityButton.isHidden = false
        updatePriceLabel("1")
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
        updatePriceLabel("1")
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
        delegate?.showProductQuantityPickerView(sender.tag,selectedValue: quantityTextBox.text!, parentModifier: parentModifier, option: option)
    }
    
}
