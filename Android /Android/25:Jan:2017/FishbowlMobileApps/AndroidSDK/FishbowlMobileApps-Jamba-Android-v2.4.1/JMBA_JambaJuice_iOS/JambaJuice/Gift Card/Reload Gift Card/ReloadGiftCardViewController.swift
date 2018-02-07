//
//  ManageGiftCardViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 09/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import AlamofireImage
import SVProgressHUD



class ReloadGiftCardViewController: UIViewController,UITableViewDelegate,UITableViewDataSource, AmountSelectionViewControllerDelegate,  PurchaserDetailsViewControllerDelegate,
AddPaymentViewControllerDelegate, PaymentViewControllerPassObjectDelegate,PaySafeWebViewDelegate {
    
    //field name
    enum fieldName : String {
        case giftCardPreview    = "Gift Card Preview"
        case number             = "Number"
        case currentBalance     = "Current Balance"
        case selectReloadAmount = "Select Reload Amount"
        case paymentAccount     = "Payment Account"
        case purchaserDetails   = "Purchaser Details"
    }
    
    //segue identifiers
    enum SegueIdentifiers : String {
        case showPaymentListVC      = "ShowPaymentListVC"
        case AmountSelectionVC      = "ShowAmountSelectionVC"
        case showPurchaserDetailsVC = "ShowPurchaserDetailsVC"
    }
    
    @IBOutlet weak var tableView:UITableView!
    
    fileprivate var addPaymentViewController    : AddPaymentViewController?
    var reloadGiftCardDetails               : [AddNewGiftCardDataModel] = []        //array for cell details
    var userPaymentDetails                  : InCommUserPaymentAccountDetails?      //retain payment details
    var giftCardBrand                       : InCommBrand?                          //brand details
    var initialAmountINString               : String = ""                           //initial amount format
    var inCommUserGiftCardId                : Int32!
    var creditCardTypeImageUrl              : String?
    var operationDoneByUser                 :Bool = false
    
    // Vesta web session
    var paySafeWebView:PaySafeWebView = PaySafeWebView(frame: CGRect.zero)
    var inCommOrgId:String?
    var inCommSessionId:String?
    
    var inCommUserCard: InCommUserGiftCard?{
        get{
            return GiftCardCreationService.sharedInstance.getUserGiftCard(inCommUserGiftCardId)!
        }
    }
    
    override func viewDidLoad() {
        operationDoneByUser = false     //initially didn't any operation
        giftCardBrand = InCommGiftCardBrandDetails.sharedInstance.brand
        initializeObject()
        createVestaWebSession { (vestaWebSession, error) in
            if error == nil{
                self.startWebSession()
            }
        }
        super.viewDidLoad()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == SegueIdentifiers.showPaymentListVC.rawValue {
            let targetController =  segue.destination as! PaymentListViewController
            targetController.headerTitle = GiftCardAppConstants.addNewPaymentTitle
            
            //remove "$" from card amount
            var cardAmount = reloadGiftCardDetails[fieldName.selectReloadAmount.hashValue].fieldRightSideValue
            if (cardAmount.contains("$")) {
                cardAmount = String(cardAmount.characters.dropFirst())
            }
            
            targetController.cardAmount = Double(cardAmount)!
            targetController.enableSavePayment = true
            targetController.usePaymentList = true
            targetController.userNewlyAddedCreditCard = GiftCardCreationService.sharedInstance.paymentDetails
            targetController.userSelectedCard = GiftCardCreationService.sharedInstance.paymentDetailsId
            targetController.userSavedPaymentDetails = GiftCardCreationService.sharedInstance.savePaymentDetails
            targetController.addPaymentDetailDelegate = self
            targetController.paymentDetailDelegate = self
            
        } else if segue.identifier == SegueIdentifiers.AmountSelectionVC.rawValue {
            let targetController = segue.destination as! AmountSelectionViewController
            targetController.userSelectedAmount =  GiftCardCreationService.sharedInstance.cardAmount
            targetController.delegate = self
            
        } else if segue.identifier == SegueIdentifiers.showPurchaserDetailsVC.rawValue {
            let targetController = segue.destination as! PurchaserDetailsViewController
            targetController.purchaserInformationFromPayment = GiftCardCreationService.sharedInstance.purchaserInformationFromPayment
            targetController.newPaymentDetails = GiftCardCreationService.sharedInstance.paymentDetails
            targetController.existingPayment = GiftCardCreationService.sharedInstance.existingPaymentDetails
            targetController.purchaserDetail = GiftCardCreationService.sharedInstance.purchaserDetails
            targetController.delegate = self
        }
    }
    
    //MARK: - IBAction methods
    //close screen - before closing ask confirmation
    @IBAction func close(){
        if operationDoneByUser {
            discardChangesConfirmationAlert()
        } else {
            GiftCardCreationService.sharedInstance.resetSharedInstanceValues()
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    //MARK: Reload gift card amount
    @IBAction func reloadGiftCardAmount(){
        if !reloadGiftCardValidation(){
            return
        }
        else{
            self.submitReloadOrder()
        }
    }
    
    
    // MARK: - Table view delegates
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return reloadGiftCardDetails.count;
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if (reloadGiftCardDetails[indexPath.row].fieldName == fieldName.giftCardPreview.rawValue) {
            var width = tableView.frame.width - GiftCardAppConstants.tableViewGiftCardLeadingAndTrailingSpace  //table view width - (leading & trailing space)
            
            //checking for ipad
            if !GiftCardAppConstants.isiPhone {
                width = tableView.frame.width / GiftCardAppConstants.GiftCardRatioWidth
            }
            return (width * CGFloat(GiftCardAppConstants.GiftCardRatioHeight)) + GiftCardAppConstants.tableViewGiftCardTopAndsBottomSpace
        } else {
            return GiftCardAppConstants.tableViewCellHeight
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (reloadGiftCardDetails[indexPath.row].fieldName == fieldName.giftCardPreview.rawValue) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ReloadGiftCardImagePreviewCell", for: indexPath) as! ReloadGiftCardImagePreviewCell
            
            cell.setCellData(inCommUserCard!.imageUrl)
            
            return cell
        } else  {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ReloadGiftCardTableViewCell", for: indexPath) as! ReloadGiftCardTableViewCell
            
            var showNavigationIcon = true
            if (reloadGiftCardDetails[indexPath.row].fieldName == fieldName.number.rawValue || reloadGiftCardDetails[indexPath.row].fieldName == fieldName.currentBalance.rawValue) {
                showNavigationIcon = false
            }
            
            cell.setCellData(reloadGiftCardDetails[indexPath.row].fieldName,  field_value: reloadGiftCardDetails[indexPath.row].fieldRightSideValue,  imageUrl: reloadGiftCardDetails[indexPath.row].fieldCreditCardTypeImage, showNavigationIcon: showNavigationIcon)
            
            if (reloadGiftCardDetails[indexPath.row].fieldName == fieldName.currentBalance.rawValue) {
                cell.setCellBackgroundlightColor()
            }
            
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if (reloadGiftCardDetails[indexPath.row].fieldName == fieldName.selectReloadAmount.rawValue) {
            performSegue(withIdentifier: SegueIdentifiers.AmountSelectionVC.rawValue, sender: nil);
        } else if (reloadGiftCardDetails[indexPath.row].fieldName == fieldName.paymentAccount.rawValue) {
            performSegue(withIdentifier: SegueIdentifiers.showPaymentListVC.rawValue, sender: nil);
        } else if (reloadGiftCardDetails[indexPath.row].fieldName == fieldName.purchaserDetails.rawValue) {
            if (GiftCardCreationService.sharedInstance.paymentDetails == nil && GiftCardCreationService.sharedInstance.paymentDetailsId == nil) {
                self.presentOkAlert("Error", message: "Please enter payment details")
                return
            }
            performSegue(withIdentifier: SegueIdentifiers.showPurchaserDetailsVC.rawValue, sender: nil);
        }
    }
    
    //MARK: - Amount selection screen delegate
    func selectedAmount(_ value:String) {
        operationDoneByUser = true
        //calculate total summary
        let amountString:String = String(value.characters.dropFirst())
        reloadGiftCardDetails[fieldName.selectReloadAmount.hashValue].fieldRightSideValue = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,Double(amountString)!)
        let amount = Double(amountString)!
        
        reloadGiftCardDetails[fieldName.selectReloadAmount.hashValue].fieldRightSideValue = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,Double(amountString)!)
        
        //update card amount in corresponding cell
        let indexPath = IndexPath(row: fieldName.selectReloadAmount.hashValue, section: 0)
        let cell = tableView.cellForRow(at: indexPath)
        if let cellType = cell as? ReloadGiftCardTableViewCell {
            cellType.value.text = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,Double(amountString)!)
        }
        
        //update in shared instance
        if (GiftCardCreationService.sharedInstance.paymentDetails != nil || GiftCardCreationService.sharedInstance.paymentDetailsId != nil) {
            GiftCardCreationService.sharedInstance.updatePaymentDetailsAmount(amount)
            
        }
        GiftCardCreationService.sharedInstance.setCardAmout(reloadGiftCardDetails[fieldName.selectReloadAmount.hashValue].fieldRightSideValue)
    }
    
    // MARK: - Purchaser information delegates
    func purchaserDetailSaved(_ purchaserDetails:InCommOrderPurchaser,purchaserDetailsFromPayment:Bool) {
        //update purchaser details value
        reloadGiftCardDetails[fieldName.purchaserDetails.hashValue].fieldRightSideValue = purchaserDetails.emailAddress
        let indexPath = IndexPath(row: fieldName.purchaserDetails.hashValue, section: 0)
        let cell = tableView.cellForRow(at: indexPath)
        if let cellType = cell as? ReloadGiftCardTableViewCell {
            cellType.value.text = purchaserDetails.emailAddress
        }
        
        //update shared Instance & recipient values
        GiftCardCreationService.sharedInstance.setPurchaserDetails(purchaserDetails)
        GiftCardCreationService.sharedInstance.setPurchaserInformationFromPayment(purchaserDetailsFromPayment)
        
        //update recipient Details
        if var recipientDetail = GiftCardCreationService.sharedInstance.recipientDetails {
            if ((GiftCardCreationService.sharedInstance.recipientSelf) != nil && GiftCardCreationService.sharedInstance.recipientSelf) {
                recipientDetail.firstName = purchaserDetails.firstName
                recipientDetail.lastName = purchaserDetails.lastName
                recipientDetail.emailAddress = purchaserDetails.emailAddress
                
                GiftCardCreationService.sharedInstance.setReceipientDetails(recipientDetail)
            }
        }
    }
    
    //MARK: - PaymentDelegate
    func paymentAdded(_ addedPaymentDetails:InCommSubmitPayment?, savePayment:InCommUserPaymentAccountDetails?, paymentDetailWithId: InCommSubmitPaymentWithId?, creditCardDetail: InCommUserPaymentAccount?, creditCardImage:String, cardLastDigits:String) {
        operationDoneByUser = true
        popToRootViewController()
        //reset values
        if (addedPaymentDetails != nil) {
            GiftCardCreationService.sharedInstance.setPaymentDetails(addedPaymentDetails!)
            
            if savePayment != nil {
                GiftCardCreationService.sharedInstance.setSavePaymentDetails(savePayment!)
            }
            
            //if there is any existing payment choosen clear it
            GiftCardCreationService.sharedInstance.resetPaymentDetailsId()
            
            //update purchaser information
            if var purhaserDetail = GiftCardCreationService.sharedInstance.purchaserDetails {
                if ((GiftCardCreationService.sharedInstance.purchaserInformationFromPayment) != nil && GiftCardCreationService.sharedInstance.purchaserInformationFromPayment) {
                    purhaserDetail.firstName = addedPaymentDetails!.firstName!
                    purhaserDetail.lastName = addedPaymentDetails!.lastName!
                    purhaserDetail.country = addedPaymentDetails!.country!
                    
                    GiftCardCreationService.sharedInstance.setPurchaserDetails(purhaserDetail)
                }
                
                //update recipient Details
                if var recipientDetail = GiftCardCreationService.sharedInstance.recipientDetails {
                    if ((GiftCardCreationService.sharedInstance.recipientSelf) != nil && GiftCardCreationService.sharedInstance.recipientSelf) {
                        recipientDetail.firstName = addedPaymentDetails!.firstName!
                        recipientDetail.lastName = addedPaymentDetails!.lastName!
                        recipientDetail.emailAddress = purhaserDetail.emailAddress
                        
                        GiftCardCreationService.sharedInstance.setReceipientDetails(recipientDetail)
                    }
                }
            }
        }
        
        if (paymentDetailWithId != nil) {
            GiftCardCreationService.sharedInstance.setPaymentDetailsId(paymentDetailWithId!)
            GiftCardCreationService.sharedInstance.setexistingPaymentDetails(creditCardDetail!)
            
            //clear user entered manual entries
            GiftCardCreationService.sharedInstance.resetPaymentDetails()
            
            //update purchaser information
            if var purhaserDetail = GiftCardCreationService.sharedInstance.purchaserDetails {
                if ((GiftCardCreationService.sharedInstance.purchaserInformationFromPayment) != nil && GiftCardCreationService.sharedInstance.purchaserInformationFromPayment) {
                    purhaserDetail.firstName = creditCardDetail!.firstName
                    purhaserDetail.lastName = creditCardDetail!.lastName
                    purhaserDetail.country = creditCardDetail!.country
                    
                    GiftCardCreationService.sharedInstance.setPurchaserDetails(purhaserDetail)
                }
                
                //update recipient Details
                if var recipientDetail = GiftCardCreationService.sharedInstance.recipientDetails {
                    if ((GiftCardCreationService.sharedInstance.recipientSelf) != nil && GiftCardCreationService.sharedInstance.recipientSelf) {
                        recipientDetail.firstName = creditCardDetail!.firstName
                        recipientDetail.lastName = creditCardDetail!.lastName
                        recipientDetail.emailAddress = purhaserDetail.emailAddress
                        
                        GiftCardCreationService.sharedInstance.setReceipientDetails(recipientDetail)
                    }
                }
            }
        }
        
        
        //get total summary amount
        let indexPath = IndexPath(row: fieldName.paymentAccount.hashValue, section: 0)
        
        //update value in payment detail cell
        let cell = tableView.cellForRow(at: indexPath)
        if let cellType = cell as? ReloadGiftCardTableViewCell {
            cellType.value.isHidden = false
            cellType.creditCardTypeImage.isHidden = false
            cellType.fieldRightSideValueTrailingSpace.constant = GiftCardAppConstants.paymentValueWithCreditCardImage
            
            //update value in local array
            cellType.value.text = cardLastDigits
            reloadGiftCardDetails[fieldName.paymentAccount.hashValue].fieldRightSideValue = cardLastDigits
            reloadGiftCardDetails[fieldName.paymentAccount.hashValue].fieldCreditCardTypeImage = creditCardImage
            
            //update/show credit card image
            
            if let url = URL(string: creditCardImage){
                cellType.creditCardTypeImage.layer.cornerRadius = GiftCardAppConstants.enableCornerRadius
                cellType.creditCardTypeImage.layer.masksToBounds = true
                cellType.creditCardTypeImage.af_setImage(withURL: url, placeholderImage: GiftCardAppConstants.jambaGiftCardDefaultImage, filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                    cellType.creditCardTypeImage.image = image.value
                    cellType.creditCardTypeImage.layer.cornerRadius = GiftCardAppConstants.disableCornerRadius
                    cellType.creditCardTypeImage.layer.masksToBounds = false
                    SVProgressHUD.dismiss()
                })
            }
        }
    }
    
    
    // MARK: - Table field initialization
    func initializeObject() {
        //dynamically get minimum value from brand details & convert as a amount
        initialAmountINString = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,giftCardBrand!.variableAmountDenominationMinimumValue!);
        GiftCardCreationService.sharedInstance.setCardAmout(self.initialAmountINString)
        
        var data:AddNewGiftCardDataModel = AddNewGiftCardDataModel()
        data.fieldName = fieldName.giftCardPreview.rawValue
        data.fieldImage = GiftCardAppConstants.jambaGiftCardDefaultImage
        reloadGiftCardDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = fieldName.number.rawValue
        data.fieldRightSideValue = (inCommUserCard?.cardNumber)!
        reloadGiftCardDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = fieldName.currentBalance.rawValue
        data.fieldRightSideValue = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint, (inCommUserCard?.balance)!)
        reloadGiftCardDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = fieldName.selectReloadAmount.rawValue
        data.fieldRightSideValue = initialAmountINString
        reloadGiftCardDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = fieldName.paymentAccount.rawValue
        data.fieldRightSideValue = ""
        reloadGiftCardDetails.append(data)
        
        data = AddNewGiftCardDataModel()
        data.fieldName = fieldName.purchaserDetails.rawValue
        data.fieldRightSideValue = ""
        reloadGiftCardDetails.append(data)
        
        tableView.reloadData()
    }
    
    //MARK: - Reload gift card valdiation
    func reloadGiftCardValidation() -> Bool{
        if (GiftCardCreationService.sharedInstance.paymentDetails == nil && GiftCardCreationService.sharedInstance.paymentDetailsId == nil) {
            self.presentOkAlert("Alert", message: "Please select payment details")
            return false
        } else if (GiftCardCreationService.sharedInstance.purchaserDetails == nil) {
            self.presentOkAlert("Alert", message: "Please enter purchaser details")
            return false
        }
        else if giftCardBrand != nil{
            let amount = String(GiftCardCreationService.sharedInstance.cardAmount.characters.dropFirst())
            let doubleAmount = Double(amount)!
            let balance      = inCommUserCard!.balance
            let totalAmount = doubleAmount + balance
            let maximumVariableAmount = giftCardBrand!.variableAmountDenominationMaximumValue!
            if  totalAmount  > maximumVariableAmount{
                self.presentOkAlert("Alert", message: "Reloading selected amount exceeds the maximum allowable balance of $\(String(format: "%.2f",maximumVariableAmount)) .")
                return false
            }
        }
        return true
    }
    
    //MARK: - Submit relaod order
    func submitReloadOrder(){
        SVProgressHUD.show(withStatus: "Reloading Order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        if inCommSessionId == nil || inCommOrgId == nil{
             // Getting incomm vesta web session id and org id
            createVestaWebSession{ (vestaWebSession, error) in
                if error != nil{
                    SVProgressHUD.dismiss()
                    self.presentConfirmation("Failure", message:"Unable to proceed please Retry", buttonTitle: "Retry", callback: { (confirmed) in
                        if confirmed{
                            self.submitReloadOrder()
                        }
                        else{
                            return
                        }
                    })
                }
                else{
                    self.startWebSession()
                    self.reloadOrder()
                }
            }
        }
        else{
            reloadOrder()
        }
    }
    
    
    // MARK: Submit reload Order
    func reloadOrder(){
        // Proceeding the reload order
        let amount = String(GiftCardCreationService.sharedInstance.cardAmount.characters.dropFirst())
        var paymentDetails =  GiftCardCreationService.sharedInstance.paymentDetails
        var paymentWithId = GiftCardCreationService.sharedInstance.paymentDetailsId
        if paymentDetails != nil{
            paymentDetails!.vestaOrgId = inCommOrgId
            paymentDetails!.vestaWebSessionId = inCommSessionId
        }
        else{
            paymentWithId?.vestaOrgId = inCommOrgId!
            paymentWithId?.vestaWebSessionId = inCommSessionId!
        }
        
        let reloadOrder:InCommReloadOrder =   InCommReloadOrder.init(cardId: inCommUserCard!.cardId, cardPin: inCommUserCard!.cardPin, brandId: InCommGiftCardBrandDetails.sharedInstance.brandID, amount: Double(amount)!, purchaser: GiftCardCreationService.sharedInstance.purchaserDetails, payment: paymentDetails, paymentWithId: paymentWithId)

        GiftCardCreationService.sharedInstance.submitReloadOrder(reloadOrder) { (order, error) in
            if let error =  error {
                SVProgressHUD.dismiss()
                self.presentConfirmation("Failure", message:error.localizedDescription , buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed{
                        self.submitReloadOrder()
                    }
                    else{
                        return
                    }
                })
            } else {
                //track clyp analytics event for reloaded the gift card amount
                //clpAnalyticsService.sharedInstance.clpTrackScreenView("GiftCardReload");
                FishbowlApiClassService.sharedInstance.submitMobileAppEvent("GIFT_CARD_RELOAD")
                if GiftCardCreationService.sharedInstance.savePaymentDetails != nil{
                    self.savePayment()
                }
                else{
                    self.completeWithRefresh()
                }
            }
        }
    }
    
    //MARK: - Save payment
    func savePayment(){
        SVProgressHUD.show(withStatus: "Associating Payment Account...")
        SVProgressHUD.setDefaultMaskType(.clear)
        GiftCardCreationService.sharedInstance.associatePaymentDetailsToUser(GiftCardCreationService.sharedInstance.savePaymentDetails) { (inCommUserPaymentAccount, error) in
            if let error = error{
                SVProgressHUD.dismiss()
                self.presentConfirmation("Failure", message:error.localizedDescription , buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed{
                        self.savePayment()
                    }
                    else{
                        self.presentConfirmation("Confirm", message: "Payment not associated to your account. \n Please confirm to exit?", buttonTitle: "OK", callback: { (confirmed) in
                            if confirmed{
                                self.completeWithResetValues()
                            }
                            else{
                                self.savePayment()
                            }
                        })
                    }
                })
            }
            else{
                self.completeWithRefresh()
            }
        }
    }
    
    //MARK:  -Complete with refresh
    func completeWithRefresh(){
        SVProgressHUD.dismiss()
        self.presentOkAlert("Success", message: "Gift card reloaded successfully", callback: {
            GiftCardCreationService.sharedInstance.resetSharedInstanceValues()
           // NotificationCenter.default.post(GiftCardAppConstants.giftCardBalanceReloaded.rawv, object: nil)
                NotificationCenter.default.post(name: Notification.Name(rawValue: GiftCardAppConstants.giftCardBalanceReloaded), object: nil);
      
           // self.delegate?.refreshBalanceAmount()
           // self.dismiss(animated: true, completion: nil)
        })
    }
    
    
    //MARK: - Complete with reset values
    func completeWithResetValues(){
        GiftCardCreationService.sharedInstance.resetSharedInstanceValues()
        self.dismiss(animated: true, completion: nil)
    }
    
    
    // MARK: - Present confirmation
    func presentConfirmationWithYesOrNo(_ title: String, message: String, callback: @escaping (_ confirmed: Bool) -> Void){
        let okAction = UIAlertAction(title: "Yes", style: .default) { action in
            callback(true)
            
        }
        let cancelAction = UIAlertAction(title: "No", style: .cancel) { action in
            callback(false)
        }
        presentAlert(title, message: message, actions: okAction, cancelAction)
    }
    
    // MARK: - Discard changes confirmation alert
    func discardChangesConfirmationAlert(){
        self.presentConfirmationWithYesOrNo("Attention", message: "Do you wish to cancel current gift card order?") { (confirmed) in
            if (confirmed) {
                //clear all values store in shared instance
                GiftCardCreationService.sharedInstance.resetSharedInstanceValues()
                self.dismiss(animated: true, completion: nil)
            }
        }
    }
    
    // MARK: - Create and start web session
    func createVestaWebSession(_ callback:@escaping InCommVestaWebSessionCallback){
        GiftCardCreationService.sharedInstance.createVestWebSession { (vestaWebSession, error) in
            if error != nil{
                self.presentError(error)
                return callback(nil, error)
            }
            else{
                self.inCommOrgId = vestaWebSession?.vestaOrgId
                self.inCommSessionId = vestaWebSession?.vestaWebSessionId
                return callback(vestaWebSession, nil)
            }
        }
    }
    
    func startWebSession(){
        self.paySafeWebView.delegate = self
        self.paySafeWebView.start()
    }
    
    // MARK: - Pay safe webview delegate
    func getDetails(_ webview: PaySafeWebView) -> (orgId: String, sessionId: String){
        return (orgId: inCommOrgId!, sessionId: inCommSessionId!)
    }
}
