//
//  AmountSelectionViewController.swift
//  GiftCardMissingScreens
//
//  Created by vThink on 5/31/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK
import SVProgressHUD

//MARK: AmountSelectionViewController Delegate
protocol AmountSelectionViewControllerDelegate: class {
    func selectedAmount(_ value:String)
}

class AmountSelectionViewController: UIViewController,UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout,UITextFieldDelegate,UIGestureRecognizerDelegate {
    
    
    @IBOutlet weak var collectionView                   : UICollectionView!
    @IBOutlet weak var collectionViewBottomSpace        : NSLayoutConstraint!
    @IBOutlet weak var selectAmountLabelLeadingSpace    : NSLayoutConstraint!
    @IBOutlet weak var selectAmountLabelTrailingSpace   : NSLayoutConstraint!
    var selectedItem                                    : Int       = 0
    var retainAmount                                    : String    = ""
    var amountInText                                    : String    = ""
    weak var delegate                                   : AmountSelectionViewControllerDelegate?
    var giftCardBrand                                   : InCommBrand?
    var userSelectedAmount                              : String?
    
    override func viewDidLoad() {
        giftCardBrand = InCommGiftCardBrandDetails.sharedInstance.brand
        retainValues()
        
        let whiteSpace = self.collectionView.frame.size.width * 0.10
        selectAmountLabelLeadingSpace.constant = whiteSpace
        selectAmountLabelTrailingSpace.constant = whiteSpace
        
        NotificationCenter.default.addObserver(self, selector: #selector(AmountSelectionViewController.keyBoardWillShown(_:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        
        NotificationCenter.default.addObserver(self, selector:#selector(AmountSelectionViewController.keyBoardWillHidden(_:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
        
        let tap:UITapGestureRecognizer = UITapGestureRecognizer(target: self,action: #selector(AmountSelectionViewController.dismissKeyBoard))
        tap.delegate = self
        self.view.addGestureRecognizer(tap)
        
        super.viewDidLoad()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
    //MARK: - Collection view delegates
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 6
        default:
            return 1
        }
    }
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 4
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch indexPath.section {
        case 0:
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "AmountCollectionViewCell", for: indexPath) as! AmountCollectionViewCell
            
            switch indexPath.item {
            case 0:
                cell.amountLabel.text = "$5"
            case 1:
                cell.amountLabel.text = "$10"
            case 2:
                cell.amountLabel.text = "$25"
            case 3:
                cell.amountLabel.text = "$50"
            case 4:
                cell.amountLabel.text = "$75"
            case 5:
                cell.amountLabel.text = "$100"
                
            default:
                cell.amountLabel.text = "$10"
                
            }
            let amountInFullForm = cell.amountLabel.text! + ".00"
            if(retainAmount == amountInFullForm){
                cell.amountLabel.backgroundColor = UIColor(red: 248/255, green: 153/255, blue:33/255, alpha:1.0)
                cell.amountLabel.textColor = UIColor.white
                cell.amountLabel.layer.borderColor = UIColor(red: 248/255, green: 153/255, blue:33/255, alpha:1.0).cgColor
            }
            else{
                cell.amountLabel.backgroundColor = UIColor.white
                cell.amountLabel.textColor = UIColor(red: 94/255, green: 94/255, blue:94/255, alpha:1.0)
                cell.amountLabel.layer.borderColor = UIColor(red: 215/255, green: 215/255, blue:215/255, alpha:1.0).cgColor
            }
            
            return cell
        case 2:
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "AmountCustomSelectCollectionViewCell", for: indexPath) as! AmountCustomSelectCollectionViewCell
            cell.amountLabel.delegate = self
            cell.amountLabel.text = amountInText
            let description = String(format: "eGift Card Price Range " + GiftCardAppConstants.amountWithZeroDecimalPoint + " - " + GiftCardAppConstants.amountWithZeroDecimalPoint,giftCardBrand!.variableAmountDenominationMinimumValue!,giftCardBrand!.variableAmountDenominationMaximumValue!)
            cell.amountDescription.text = description
            return cell
        default:
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "SolidLine", for: indexPath)
            return cell
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch indexPath.section {
        case 0:
            let cellgap = (self.collectionView.frame.size.width * 0.20) + (self.collectionView.frame.size.height * 0.10)
            let collectionViewCellWidth = (self.collectionView.frame.size.width - cellgap)/3
            let collectionViewCellHeight = collectionViewCellWidth * 0.575
            return CGSize(width: collectionViewCellWidth, height: collectionViewCellHeight)
        case 2:
            let cellgap = (self.collectionView.frame.size.width * 0.20)
            return CGSize(width: self.collectionView.frame.size.width - cellgap, height: 90)
        default:
            return CGSize(width: self.collectionView.frame.size.width, height: 1)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        selectedItem = indexPath.item
        let cell = collectionView.cellForItem(at: indexPath)
        if let amountCell = cell as? AmountCollectionViewCell{
            amountInText = (amountCell.amountLabel.text!)
            //check amount is in range
            var amountInTextWithoutDoller = amountInText
            if amountInTextWithoutDoller.characters.contains("$") {
                amountInTextWithoutDoller = String(amountInText.characters.dropFirst())
            }
            let amount:Double = Double(amountInTextWithoutDoller)!
            if (amount < giftCardBrand?.variableAmountDenominationMinimumValue ?? 0 || amount > giftCardBrand?.variableAmountDenominationMaximumValue ?? 0) {
                showInvalidPriceRangeError()
                amountInText = ""
                selectedItem = 0
                collectionView.reloadData()
                return
            }
            delegate?.selectedAmount(String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,amount))
            popViewController()
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        let topBottom  = (self.collectionView.frame.size.height * 0.05)
        switch section {
        case 0:
            let leftRightInset = self.collectionView.frame.size.width * 0.10
            return UIEdgeInsetsMake(topBottom, leftRightInset, topBottom, leftRightInset)
        case 2:
            let leftRightInset = self.collectionView.frame.size.width * 0.10
            return UIEdgeInsetsMake(topBottom, leftRightInset, topBottom, leftRightInset)
        default:
            return UIEdgeInsets.zero
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return self.collectionView.frame.size.height * 0.05
    }
    
    //MARK: - Text field delegates
    func keyBoardWillShown(_ notification:Notification){
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        let keyboardFrame:NSValue = userInfo.value(forKey: UIKeyboardFrameEndUserInfoKey) as! NSValue
        
        let indexPath = IndexPath(row: 0, section:2);
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.collectionViewBottomSpace.constant = keyboardFrame.cgRectValue.height
            self.view.layoutIfNeeded()
        }, completion: { (completed) in
            if completed {
                self.collectionView.scrollToItem(at: indexPath, at: .top, animated: true)
            }
        }) 
    }
    
    func keyBoardWillHidden(_ notification:Notification){
        let userInfo:NSDictionary = notification.userInfo! as NSDictionary
        UIView.animate(withDuration: (userInfo.value(forKey: UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.collectionViewBottomSpace.constant = 0
            self.view.layoutIfNeeded()
        }) 
        
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        var textValue = textField.text! as NSString
        textValue = textValue.replacingCharacters(in: range, with: string) as NSString
        let textValueString = textValue as String
        let indexPath = IndexPath(item: 0, section: 2)
        let cell = collectionView.cellForItem(at: indexPath)
        if let cellType = cell as? AmountCustomSelectCollectionViewCell {
            if !isCustomAmount(textValueString) {
                cellType.amountLabel.text = amountInText
                return false
                //find $ is in text
            } else if (!(textValueString.characters.contains("$")) && textValueString.characters.count > 0){
                amountInText = "$" + textValueString
                cellType.amountLabel.text = "$"
            } else {
                amountInText = textValueString
            }
        }
        return true
    }
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        view.endEditing(true);
        if (amountInText == "") {
            selectedItem = 0
            collectionView.reloadData()
        } else {
            amountInText = amountInText.trim()
            
            let indexPath = IndexPath(item: 0, section: 2)
            let cell = collectionView.cellForItem(at: indexPath)
            //check for valid number
            if !isCustomAmount(amountInText) {
                self.presentOkAlert("Alert", message: "Please enter a valid custom amount")
                amountInText = ""
                if let cellType = cell as? AmountCustomSelectCollectionViewCell {
                    cellType.amountLabel.text = ""
                }
                selectedItem = 0
                collectionView.reloadData()
                return true
            }
            if amountInText.characters.contains("$") {
                amountInText = String(amountInText.characters.dropFirst())
            }
            
            //check whether textbox is empty
            if amountInText == "" {
                showInvalidPriceRangeError()
                return true
            }
            
            let amount:Double = Double(amountInText)!
            
            if (amount < giftCardBrand?.variableAmountDenominationMinimumValue ?? 0 || amount > giftCardBrand?.variableAmountDenominationMaximumValue ?? 0) {
                showInvalidPriceRangeError()
                amountInText = ""
                if let cellType = cell as? AmountCustomSelectCollectionViewCell {
                    cellType.amountLabel.text = ""
                }
                selectedItem = 0
                collectionView.reloadData()
                return true
            }
            
            amountInText = String(format: GiftCardAppConstants.amountWithTwoDecimalPoint,amount)
            
            if let cellType = cell as? AmountCustomSelectCollectionViewCell {
                cellType.amountLabel.text = amountInText
            }
        }
        return true
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        let indexPath = IndexPath(item: 0, section: 2)
        let cell = collectionView.cellForItem(at: indexPath)
        if let cellType = cell as? AmountCustomSelectCollectionViewCell {
            cellType.amountLabel.becomeFirstResponder()
        }
        amountInText = ""
        deselectDefaultAmount()
    }
    
    func deselectDefaultAmount() {
        for index in 0...6 {
            let indexPath = IndexPath(item: index, section: 0)
            let cell = collectionView.cellForItem(at: indexPath)
            if let cellType = cell as? AmountCollectionViewCell {
                cellType.amountLabel.backgroundColor = UIColor.white
                cellType.amountLabel.textColor = UIColor(red: 94/255, green: 94/255, blue:94/255, alpha:1.0)
                cellType.amountLabel.layer.borderColor = UIColor(red: 215/255, green: 215/255, blue:215/255, alpha:1.0).cgColor
            }
        }
    }
    
    func dismissKeyBoard(){
        self.view.endEditing(true)
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        
        if ((touch.view?.isKind(of: UILabel.self)) == true){
            return false
        }
        
        return true
    }
    
    
    //MARK: - IBAction methods
    @IBAction func continueButtonPressed(_ sender:UIButton){
        if (amountInText != "") {
            if amountInText.characters.contains("$") {
                amountInText = String(amountInText.characters.dropFirst())
            }
            let amount = Double(amountInText) ?? 0
            
            if (amount < (giftCardBrand?.variableAmountDenominationMinimumValue)! || amount > (giftCardBrand?.variableAmountDenominationMaximumValue)!) {
                showInvalidPriceRangeError()
                return
            }
            delegate?.selectedAmount(String(format: GiftCardAppConstants.amountWithTwoDecimalPoint, amount))
        }
        popViewController()
    }
    
    @IBAction func close(){
        popViewController()
    }
    
    
    //MARK: - Regex for custom amount validation
    func isCustomAmount(_ testStr:String) -> Bool {
        var amountRegEx = "[$]{0,1}[0-9]{0,3}[.]?"
        if testStr.characters.contains(".") {
            amountRegEx = "[$]{0,1}[0-9]{0,3}[.]{1}[0-9]{0,2}"
        }
        let amountTest = NSPredicate(format:"SELF MATCHES %@", amountRegEx)
        return amountTest.evaluate(with: testStr)
    }
    
    //MARK: - General
    func retainValues() {
        if (userSelectedAmount != nil || userSelectedAmount != "") {
            retainAmount = userSelectedAmount!
            if !(retainAmount == "$5.00" || retainAmount == "$10.00" || retainAmount == "$25.00" || retainAmount == "$50.00" || retainAmount == "$100.00" || retainAmount == "$75.00") {
                amountInText = retainAmount
                
            }
            collectionView.reloadData()
        }
    }
    
    func showInvalidPriceRangeError () {
        let message = String(format: "Please enter an amount between " + GiftCardAppConstants.amountWithTwoDecimalPoint + " - " + GiftCardAppConstants.amountWithTwoDecimalPoint,giftCardBrand!.variableAmountDenominationMinimumValue!,giftCardBrand!.variableAmountDenominationMaximumValue!)
        self.presentOkAlert("Alert", message: message)
    }
    
    //MARK: - deinit
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
}
