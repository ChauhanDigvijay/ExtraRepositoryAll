//
//  MyOrderItemTableViewCell.swift
//  JambaJuice
//
//  Created by VT02 on 10/6/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit

class MyOrderItemTableViewCell: UITableViewCell {

    enum SeeMore :Int {
        case kSeeMoreNone = -1
        case kSeeMoreExpand = 0
        case kSeeMoreCollapse = 1
    }
    
    //  weak var delegate: MyOrderItemTableViewCellDelegate?
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var descLabel: UILabel!
    @IBOutlet weak var rightDescLabel: UILabel!
    @IBOutlet weak var optionLabel: UILabel!
    @IBOutlet weak var optionLabelHeight: NSLayoutConstraint!
    @IBOutlet weak var seeMore: UIButton!
    
    @IBOutlet weak var sizeLabel: UILabel!
    
    @IBOutlet weak var seeMoreBottomConstraint: NSLayoutConstraint!
    
    func updateCell(orderStatusProduct: OrderStatusProduct, index:Int){
        nameLabel.text = orderStatusProduct.name.capitalized
        let spInstr = orderStatusProduct.specialInstructions
        optionLabel.text = spInstr
        optionLabel.isHidden = false
        let costString = String(format: "$%.2f",  orderStatusProduct.totalCost)
        rightDescLabel.text = costString
        
        let optionNamesAlreadyIncludedInDescription = NSMutableOrderedSet()
        let optionSizeNamesAlreadyIncludedInDescription = NSMutableOrderedSet()
        for basketChoice in  orderStatusProduct.choices {
            if !basketChoice.name.lowercased().hasPrefix("click here to") {//Filter out click here to...
                //Check if we have already included a choice with same name
                //If we haven't return the choice name and include it in the set for future.
                if !optionNamesAlreadyIncludedInDescription.contains(basketChoice.name.uppercased()) || !optionSizeNamesAlreadyIncludedInDescription.contains(basketChoice.name.uppercased()) {
                    if !orderStatusProduct.isSizeModifier(name: basketChoice.name) {
                        optionNamesAlreadyIncludedInDescription.add(basketChoice.name.uppercased())
                    } else {
                        optionSizeNamesAlreadyIncludedInDescription.add(basketChoice.name.uppercased())
                    }
                }
            }
        }
        let sizeName = (optionSizeNamesAlreadyIncludedInDescription.array as! [String]).joined(separator: ", ")
        let optionName = (optionNamesAlreadyIncludedInDescription.array as! [String]).joined(separator: ", ")
        
        optionLabel.text = optionName
        sizeLabel.text = sizeName
        
        if (index == SeeMore.kSeeMoreExpand.rawValue) {
            let noOfLines:Int = BasketItemTableViewCell.lines(BasketItemTableViewCell.descriptionLabelWidth,label: optionLabel);
            optionLabel.numberOfLines = noOfLines;
            optionLabelHeight.constant = CGFloat(noOfLines * BasketItemTableViewCell.defaultLineHeight);  //16 is the height for each line. so, if there are n lines then it needs n*16 height space
            seeMore.setTitle("See Less", for: UIControlState())
            seeMore.isHidden = false
            seeMoreBottomConstraint.constant = 32
        } else if (index == SeeMore.kSeeMoreCollapse.rawValue){
            optionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines;
            optionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultLineHeight * BasketItemTableViewCell.defaultNoOfLines)
            seeMore.setTitle("See More", for: UIControlState())
            seeMore.isHidden = false
            seeMoreBottomConstraint.constant = 32
        }
        let noOfLines:Int = BasketItemTableViewCell.lines(BasketItemTableViewCell.descriptionLabelWidth,label: optionLabel);
        if (noOfLines <= BasketItemTableViewCell.defaultNoOfLines) {
            optionLabel.numberOfLines = noOfLines;
            optionLabelHeight.constant = CGFloat(noOfLines * BasketItemTableViewCell.defaultLineHeight);
            seeMore.isHidden = true
            seeMoreBottomConstraint.constant = 0

    }
}
}
