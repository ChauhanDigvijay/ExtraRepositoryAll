//
//  PaymentListTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 11/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import Haneke

class PaymentListTableViewCell: UITableViewCell {
    @IBOutlet weak var creditCardType               : UIImageView!
    @IBOutlet weak var creditCardNumber             : UILabel!
    @IBOutlet weak var creditCardSelectedView       : UIView!
    @IBOutlet weak var creditCardSelectedShadeView  : UIView!
    
    func setCellData(creditCardDetail:InCommUserPaymentAccount,imageUrl:String, selectedCreditCardId:Int32) {
        creditCardNumber.text = "xxxx xxxx xxxx \(creditCardDetail.creditCardNumber)"
        if let url = NSURL(string: (imageUrl)){
            creditCardType.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            creditCardType.layer.masksToBounds = true
            creditCardType.hnk_setImageFromURL(url, placeholder: GiftCardAppConstants.jambaGiftCardDefaultImage, success:{(image) in
                self.creditCardType.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.creditCardType.layer.masksToBounds = false
                self.creditCardType.image = image
            })
        }
        //check for the selected card
        if (creditCardDetail.id == selectedCreditCardId) {
            creditCardSelectedView.hidden = false
            creditCardSelectedShadeView.hidden = false
        } else {
            creditCardSelectedView.hidden = true
            creditCardSelectedShadeView.hidden = true
        }
    }
}
