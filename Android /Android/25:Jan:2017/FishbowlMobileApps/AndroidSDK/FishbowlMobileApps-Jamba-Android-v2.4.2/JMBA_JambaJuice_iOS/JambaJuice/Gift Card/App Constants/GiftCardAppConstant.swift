//
//  AppConstant.swift
//  JambaGiftCard
//
//  Created by vThink on 19/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import Foundation
import CoreGraphics
import InCommSDK

struct GiftCardAppConstants {
    
    //MARK: - JAMBA URL's
    static let jambaGiftCardTermsUrl = "http://www.jambajuice.com/company-info/JambaCardTermsandConditions"
    static let jambaGiftCardFAQUrl = "https://premium.vcdelivery.com/V2/jambajuice/GiftCardFAQ.aspx"
    
    // Error Code
    //MARK: - Jamba gift card error code for invalid user
    static let errorCodeInvalidUser = 401000
    static let cardAlreadyProvisionedError = 80
    
    //MARK: Jamba gift card delete error code for already send request
    static let unableToFindUserPaymentAccount = 113
    //MARK: Jamba gift card auto reload delted already
    static let unableToGetAutoReloadInformation = 13
    
    //MARK: Custom error code
    static let autoReloadConfigDeleteError = 1
    static let creditCardDeleteError = 0
    
    //MARK: Genral Error
    static let generalError:NSError = NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Unable to proceed please Retry"])
//    static let generalError:NSError = NSError.init(coder: "Unable to proceed please Retry")
    
    //MARK: - NotificationCenter
    static let giftCardNameChanged = "Gift Card Name changed"
    static let existingGiftCardAdded = "Existing Gift Card Added"
    static let refreshGiftCardHomePage = "Refresh Gift Card Home Page"
    
    static let giftCardBalanceReloaded = "giftCardBalanceReloaded"
    static let giftCardRemoved         = "giftCardRemoved"
    
    //MARK: - Screens Button Text
    static let addNewPaymentTitle = "Add New Card"
    static let billingDetailTitle = "Billing Details"
    
    //MARK: - Default Country Code
    static let giftCardPaymentDefaultCountryCode = "US"
    
    //MARK: - No Funds Collected
    static let noFundsColloectedOrderPaymentMethodType = "NoFundsCollected"
    
    //MARK: - Incomm token expired or invalid user id
    static let incommTokenExpirationOrInvalidUserId = 401
    
    //MARK: - Screen Height & Spaces
    //gift card
    static let isiPhone = (UIDevice.current.userInterfaceIdiom == .phone)
    static let GiftCardRatioWidth:CGFloat = isiPhone ? 1.15 : 2.0
    static let GiftCardRatioHeight:CGFloat = 0.632
    static let giftCardLeadingOrTrailingSpace:CGFloat = isiPhone ? 10 : 30
    static let giftCardLeadingAndTrailingSpace:CGFloat = (giftCardLeadingOrTrailingSpace*2) //leading = 15 + trailing = 15
    static let giftCardTopAndBottomSpace:CGFloat = isiPhone ? 25 : ((1-GiftCardRatioHeight)/2) //top = 10 + Bottom = 10
    
    //MARK: - Corner Radius
    static let enableCornerRadius:CGFloat = 8.0
    static let disableCornerRadius:CGFloat = 8.0
    
    //MARK: - Spaces for the app
    //pickr view
    static let pickerViewHeight:CGFloat = 256
    static let animationDuration:Double = 0.5
    
    //table view
    static let tableViewGiftCardTopAndsBottomSpace:CGFloat = 30
    static let tableViewGiftCardLeadingAndTrailingSpace:CGFloat = 30
    static let tableViewLeadingSpace:CGFloat = 15
    static let tableViewTrailingSpace:CGFloat = 15
    static let tableViewCellHeight:CGFloat = 60
    static let tableViewSectionHeight:CGFloat = 30
    
    //disable table view cell alpha value
    static let tableViewCellDeactiveState:CGFloat = 0.5
    static let tableViewCellActiveState:CGFloat = 1.0
    
    //tableview - payment list
    static let paymentValueWithCreditCardImage:CGFloat = 102
    static let tableViewValueWithNavigationIcon:CGFloat = 37
    
    //button
    static let buttonHeight:CGFloat = 50
    
    //label
    static let labelSingleLineHeight:CGFloat = 27
    static let labelDoubleLineHeight:CGFloat = 50
    
    //MARK: - Default images
    static let backButtonImageWhenPresented:UIImage = UIImage(named: "down-button")!
    static let backButtonImageWhenPushed:UIImage = UIImage(named: "back-button")!
    static let pushScreenButon:UIImage = UIImage(named: "disclosure-indicator")!
    
    //auto reload screen static images
    static let radioButtonDisabled:UIImage = UIImage(named: "radio-button-disabled")!
    static let radioButtonEnabled:UIImage = UIImage(named: "tickWithGreenBG")!
    
    //default image for gift card
    static let jambaGiftCardDefaultImage:UIImage = UIImage(named: "product-placeholder")!
    
    //default background for credit card
    static let jambaCreditCardDefaultBackgroundColor:UIColor = UIColor(red: 235/255, green: 241/255, blue: 246/255, alpha: 1.0)
    
    //MARK: - App format
    //date formation
    static let ShortDateFormat = "MM/dd/yyyy"
    static let LongDateFormat = "MM/dd/yyyy 'at' h:mm a"
    static let AMSymbol = "AM"
    static let PMSymbol = "PM"
    static let timeZone = "UTC"
    static let dateToSendTime = " at 1:00 AM"
    
    //Amount Display format
    static let amountWithTwoDecimalPoint = "$%.2f"
    static let amountWithZeroDecimalPoint = "$%.0f"
    
    //Mark: - Gift Card Maximim digit
    static let giftCardMaximumNumberOfDigits:Int = 20
    
    //MARK: - Jamba Default Card Name
    static let jambaDefaultCard = "jambaDefaultCard"
    
    //MARK: - Screen mode/operation
    //payment screen mode
    enum PaymentScreenMode : String {
        case ViewOnly = "view"
        case ViewWithTrashOperation = "With trash operation"
        case ViewWithoutTrashOperation = "Without trash operation"
    }
    
    //MARK: - Gift card promo order
    static let GiftCardPromoOrderAlertOptionKey = "GiftCardPromoAlertOption"
    enum GiftCardPromoOrderAlertOptionValue : Int{
        case notRightNow  = 0
        case notInterested = 1
    }
    
    //MARK: - Gift card alert show
    static let GiftCardPromoOrderAlertViewKey = "GiftCardPromoOrderAlertView"
    enum GiftCardPromOrderAlertViewShow: Int{
        case show = 0
        case hide = 1
    }
    
    
    // MARK: Date to Send
    static func getDateToSend(_ dateToSend:Date) -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = GiftCardAppConstants.ShortDateFormat
        let selectedDate = dateToSend
        let now = Date()
        let calendar = Calendar(identifier: Calendar.Identifier.gregorian)
        let components = (calendar as NSCalendar).components(.day, from: selectedDate, to: now, options: .wrapComponents)
        let days = components.day
        var dateToSendText = dateFormatter.string(from: selectedDate)
        if days == 0 {//The day only becomes 1 after 24h irrespective of day
            if calendar.isDateInToday(selectedDate) {
                dateToSendText = "Today"
                return dateToSendText
            }
            else if calendar.isDateInTomorrow(selectedDate) {
                dateToSendText = "Tomorrow"
                return dateToSendText
            }
            else {//Case of yesterday with less then 24h diff
                return dateToSendText
            }
        }
        return dateToSendText
    }
    
    // MARK: Name validation
    static func nameValidation(_ name:String) -> Bool{
        let regexText = NSPredicate(format: "SELF MATCHES %@", "^[a-zA-Z]+$")
        if regexText.evaluate(with: name){
            if name.length > 20{
                return false
            }else{
                return true
            }
        }else{
            return false
        }
    }
}
