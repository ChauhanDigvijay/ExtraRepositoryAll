//
//  BasketItemTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 26/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import MGSwipeTableCell
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}

// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l > r
  default:
    return rhs < lhs
  }
}


class BasketItemTableViewCell: MGSwipeTableCell {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    @IBOutlet weak var rightLabel: UILabel!
    @IBOutlet weak var badgeLabel: UILabel!
    @IBOutlet weak var seeMoreButton: UIButton!
    @IBOutlet weak var descriptionLabelHeight: NSLayoutConstraint!
    
    //default no of lines 3 default height for single line is 16
    static let defaultNoOfLines = 3;
    static let defaultLineHeight = 16;
    
    //label width = Total screen width(UIScreen.mainScreen().bounds.width) - label left space (leading space = 33) & right space(trailing space = 81) (115)
    static let descriptionLabelWidth = 114
    
    //expand/collapse state
    enum SeeMore :Int {
        case kSeeMoreNone = -1
        case kSeeMoreExpand = 0
        case kSeeMoreCollapse = 1
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        rightButtons = []
        delegate = nil
    }
    
    func update(_ product: BasketProduct,state: Int) {
        nameLabel.text = product.name.capitalized
        
        //Since Olo at times returns option's with same name, we filter them out.
                let optionNamesAlreadyIncludedInDescription = NSMutableArray()
        
        var basketChoiceInText:[String] = []
                for basketChoice in product.choices {
                    if !basketChoice.name.lowercased().hasPrefix("click here to") {//Filter out click here to...
                        //Check if we have already included a choice with same name
                        //If we haven't return the choice name and include it in the set for future.
                        if !optionNamesAlreadyIncludedInDescription.contains(basketChoice.name.uppercased()) {
                            optionNamesAlreadyIncludedInDescription.add(basketChoice.name.uppercased())
                            if basketChoice.quantity != nil && basketChoice.quantity > 1 {
                                basketChoiceInText.append("\(basketChoice.quantity!)x \(basketChoice.name.uppercased())")
                            } else {
                                basketChoiceInText.append("\(basketChoice.name.uppercased())")
                            }
                            
                        } else if (!basketChoice.isSizeModifier()){
                            optionNamesAlreadyIncludedInDescription.add(basketChoice.name.uppercased())
                            
                            if basketChoice.quantity != nil && basketChoice.quantity > 1 {
                                basketChoiceInText.append("\(basketChoice.quantity!)x \(basketChoice.name.uppercased())")
                            } else {
                                basketChoiceInText.append("\(basketChoice.name.uppercased())")
                            }
                            
                        }
                    }
                }
        
                descriptionLabel.text = basketChoiceInText.joined(separator: ", ")
        
        if (state == SeeMore.kSeeMoreExpand.rawValue) {
            let noOfLines:Int = BasketItemTableViewCell.lines(BasketItemTableViewCell.descriptionLabelWidth,label: descriptionLabel);
            descriptionLabel.numberOfLines = noOfLines;
            descriptionLabelHeight.constant = CGFloat(noOfLines * BasketItemTableViewCell.defaultLineHeight);  //16 is the height for each line. so, if there are n lines then it needs n*16 height space
            seeMoreButton.setTitle("See Less", for: UIControlState())
            seeMoreButton.isHidden = false
        } else if (state == SeeMore.kSeeMoreCollapse.rawValue){
            descriptionLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines;
            descriptionLabelHeight.constant = CGFloat(BasketItemTableViewCell.defaultLineHeight * BasketItemTableViewCell.defaultNoOfLines)
            seeMoreButton.setTitle("See More", for: UIControlState())
            seeMoreButton.isHidden = false
        }
        let noOfLines:Int = BasketItemTableViewCell.lines(BasketItemTableViewCell.descriptionLabelWidth,label: descriptionLabel);
        if (noOfLines <= BasketItemTableViewCell.defaultNoOfLines) {
            descriptionLabel.numberOfLines = noOfLines;
            descriptionLabelHeight.constant = CGFloat(noOfLines * BasketItemTableViewCell.defaultLineHeight);
            seeMoreButton.isHidden = true
        }
        //
        if product.quantity > 0 {
            badgeLabel.isHidden = false
            badgeLabel.text = "\(product.quantity)"
        }
        else {
            badgeLabel.isHidden = true
        }
        let costString = String(format: "$%.2f",  product.totalCost)
        rightLabel.text = costString
        //configure right buttons
        let deleteButton = MGSwipeButton(title: "Delete", backgroundColor: UIColor(hex: Constants.jambaGarnetColor))
        rightButtons = [deleteButton]
        //This is needed for label to take proper dimensions
        layoutIfNeeded()
    }
    
    static func lines(_ width:Int,label:UILabel) -> Int {
        label.numberOfLines = 0;
        var lineCount = 0;
        let widthFloat:CGFloat=CGFloat(width)
        let textSize = CGSize(width: UIScreen.main.bounds.width - widthFloat, height: CGFloat(Float.infinity));
        let rHeight = lroundf(Float(label.sizeThatFits(textSize).height))
        let charSize = lroundf(Float(label.font.lineHeight));
        lineCount = rHeight/charSize
        // less than 3 means hide expand/coolapse button
        return lineCount;
    }
    
}
