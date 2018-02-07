//
//  PaymentInfoInputTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 02/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

enum CreditCardType :String {
    case AmericanExpress = "3"
    case Visa = "4"
    case MasterCard = "5"
    case Discover = "6"
}

class PaymentInfoInputTableViewCell: UITableViewCell {
    
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var creditCardtype: UIImageView!
    @IBOutlet weak var textFieldTrailingSpace:NSLayoutConstraint!
    @IBOutlet weak var cameraButton:UIButton!
   
    
    func setData(_ placeHolderText:String,value:String) {
        textField.placeholder = placeHolderText
        textField.text = value
        creditCardtype.isHidden = true
       cameraButton.isHidden = true
        textFieldTrailingSpace.constant = 33            //trailing space(33)
        if placeHolderText == "Card Number" {
            cameraButton.isHidden = false
            if value.length>0 {
                let charIndex = value.characters.index(value.startIndex, offsetBy: 0)
                creditCardtype.image = UIImage(named: getCreditCardImageName(String(value[charIndex])))
                creditCardtype.isHidden = false
              
                textFieldTrailingSpace.constant = 100    //trailing space(33) + credit card image width(55) +buffer(7)
            }
        }
    }
    
    func setCreditCardImage(_ value:String) {
        if value.length>0 {
            let charIndex = value.characters.index(value.startIndex, offsetBy: 0)
            creditCardtype.image = UIImage(named: getCreditCardImageName(String(value[charIndex])))
            creditCardtype.isHidden = false
            
            textFieldTrailingSpace.constant = 100    //trailing space(33) + credit card image width(55) +buffer(7)
        } else {
            creditCardtype.isHidden = true
            
            textFieldTrailingSpace.constant = 33    //trailing space(33)
        }
    }
    
    func getCreditCardImageName(_ cardNumber:String) -> String {
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
