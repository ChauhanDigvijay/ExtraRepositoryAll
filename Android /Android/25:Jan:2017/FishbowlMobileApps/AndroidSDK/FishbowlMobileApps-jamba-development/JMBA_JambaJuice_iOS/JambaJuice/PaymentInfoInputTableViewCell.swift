//
//  PaymentInfoInputTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 02/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class PaymentInfoInputTableViewCell: UITableViewCell {
    
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var creditCardtype: UIImageView!
    @IBOutlet weak var textFieldTrailingSpace:NSLayoutConstraint!
    @IBOutlet weak var cameraButton:UIButton!
   
    
    enum CreditCardType :String {
        case AmericanExpress = "3"
        case Visa = "4"
        case MasterCard = "5"
        case Discover = "6"
    }
    
    func setData(placeHolderText:String,value:String) {
        textField.placeholder = placeHolderText
        textField.text = value
        creditCardtype.hidden = true
       cameraButton.hidden = true
        textFieldTrailingSpace.constant = 33            //trailing space(33)
        if placeHolderText == "Card Number" {
            cameraButton.hidden = false
            if value.length>0 {
                let charIndex = value.startIndex.advancedBy(0)
                creditCardtype.image = UIImage(named: getCreditCardImageName(String(value[charIndex])))
                creditCardtype.hidden = false
              
                textFieldTrailingSpace.constant = 100    //trailing space(33) + credit card image width(55) +buffer(7)
            }
        }
    }
    
    func setCreditCardImage(value:String) {
        if value.length>0 {
            let charIndex = value.startIndex.advancedBy(0)
            creditCardtype.image = UIImage(named: getCreditCardImageName(String(value[charIndex])))
            creditCardtype.hidden = false
            
            textFieldTrailingSpace.constant = 100    //trailing space(33) + credit card image width(55) +buffer(7)
        } else {
            creditCardtype.hidden = true
            
            textFieldTrailingSpace.constant = 33    //trailing space(33)
        }
    }
    
    func getCreditCardImageName(cardNumber:String) -> String {
        switch cardNumber {
        case CreditCardType.AmericanExpress.rawValue:
            return "american_exp_card"
        case CreditCardType.Visa.rawValue:
            return "visa_card"
        case CreditCardType.MasterCard.rawValue:
            return "master_card_card"
        case CreditCardType.Discover.rawValue:
            return "discover_card"
        default:
            return ""
        }
    }
}