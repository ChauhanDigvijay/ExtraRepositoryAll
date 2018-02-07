//
//  BasketNavigationTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 11/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import MGSwipeTableCell

class BasketNavigationTableViewCell: MGSwipeTableCell {
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var rightLabel: UILabel!
    @IBOutlet weak var disclosureImageView: UIImageView!
    @IBOutlet weak var disclosureWidthConstraint: NSLayoutConstraint!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        rightButtons = []
        delegate = nil
    }
    
    func updateForBasket(_ basket: Basket) {
        nameLabel.text = UserService.sharedUser == nil ? "Enter Promotion Code" : "Select Reward or Promotion"
        // Neither promotion or reward has been applied
        if basket.promotionCode == nil && basket.appliedRewards.count == 0 {
            descriptionLabel.text = "NOTHING SELECTED"
            rightLabel.text = "-$0.00"
        }
        else if basket.promotionCode != nil || basket.appliedRewards.count > 0 {
            // If promotion has been applied
            if basket.promotionCode != nil && basket.discount != 0{
                descriptionLabel.text = basket.promotionCode
                rightLabel.text = String(format: "-$%.2f",  basket.discount)
            } else if basket.discount == 0{
                descriptionLabel.text = "NOTHING SELECTED"
                rightLabel.text = "-$0.00"
            }
            // If reward has been applied
            else if basket.appliedRewards.count > 0 {
                descriptionLabel.text = basket.appliedRewards.map({ $0.name }).joined(separator: ", ")
                rightLabel.text = String(format: "-$%.2f",  basket.discount)
            }
            //configure right buttons
            let removeButton = MGSwipeButton(title: "Remove", backgroundColor: UIColor(hex: Constants.jambaGarnetColor))
            rightButtons = [removeButton]
        }
        //This is needed for labels to take proper dimensions
        layoutIfNeeded()
    }
    
}
