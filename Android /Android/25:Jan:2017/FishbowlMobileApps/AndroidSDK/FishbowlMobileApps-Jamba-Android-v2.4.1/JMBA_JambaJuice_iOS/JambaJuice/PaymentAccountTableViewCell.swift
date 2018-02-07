//
//  PaymentAccountTableViewCell.swift
//  JambaJuice
//
//  Created by VT010 on 7/12/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit

enum CreditCardTypeString :String {
    case AmericanExpress = "Amex"
    case Visa = "Visa"
    case MasterCard = "Mastercard"
    case Discover = "Discover"
}

class PaymentAccountTableViewCell: UITableViewCell {
    @IBOutlet weak var creditCardTypeImageView: UIImageView!
    @IBOutlet weak var creditCardCVVNumber: UILabel!
    @IBOutlet weak var checkImageView: UIImageView!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func getCreditCardImageName(creditCardTypeName:String) -> String {
        switch creditCardTypeName {
        case CreditCardTypeString.AmericanExpress.rawValue:
            return "american_exp_card"
        case CreditCardTypeString.Visa.rawValue:
            return "visa_card"
        case CreditCardTypeString.MasterCard.rawValue:
            return "master_card_card"
        case CreditCardTypeString.Discover.rawValue:
            return "discover_card"
        default:
            return ""
        }
    }

}
