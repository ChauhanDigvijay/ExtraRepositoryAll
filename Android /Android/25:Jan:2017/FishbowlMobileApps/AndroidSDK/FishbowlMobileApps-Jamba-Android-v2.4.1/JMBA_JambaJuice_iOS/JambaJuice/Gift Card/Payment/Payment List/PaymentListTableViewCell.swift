//
//  PaymentListTableViewCell.swift
//  JambaGiftCard
//
//  Created by vThink on 11/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import AlamofireImage

class PaymentListTableViewCell: UITableViewCell {
    @IBOutlet weak var creditCardType               : UIImageView!
    @IBOutlet weak var creditCardNumber             : UILabel!
    @IBOutlet weak var creditCardSelectedView       : UIView!
    @IBOutlet weak var creditCardSelectedShadeView  : UIView!
    
    func setCellData(_ creditCardDetail:InCommUserPaymentAccount,imageUrl:String, selectedCreditCardId:Int32) {
        creditCardNumber.text = "xxxx xxxx xxxx \(creditCardDetail.creditCardNumber)"
        if let url = URL(string: (imageUrl)){
            creditCardType.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
            creditCardType.layer.masksToBounds = true
            
            creditCardType.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                self.creditCardType.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                self.creditCardType.layer.masksToBounds = false
                self.creditCardType.image = image.value
            })
        }
        //check for the selected card
        if (creditCardDetail.id == selectedCreditCardId) {
            creditCardSelectedView.isHidden = false
            creditCardSelectedShadeView.isHidden = false
        } else {
            creditCardSelectedView.isHidden = true
            creditCardSelectedShadeView.isHidden = true
        }
    }
}
