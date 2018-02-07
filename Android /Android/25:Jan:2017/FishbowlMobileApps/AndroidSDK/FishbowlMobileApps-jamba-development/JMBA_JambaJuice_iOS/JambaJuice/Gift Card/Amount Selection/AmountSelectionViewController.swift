//
//  AmountSelectionViewController.swift
//  GiftCardMissingScreens
//
//  Created by vThink on 5/31/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import Haneke
import InCommSDK
import SVProgressHUD

//MARK: AmountSelectionViewController Delegate
protocol AmountSelectionViewControllerDelegate: class {
    func selectedAmount(value:String)
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
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(AmountSelectionViewController.keyBoardWillShown(_:)), name: UIKeyboardWillShowNotification, object: nil)
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector:#selector(AmountSelectionViewController.keyBoardWillHidden(_:)), name: UIKeyboardWillHideNotification, object: nil)
        
        let tap:UITapGestureRecognizer = UITapGestureRecognizer(target: self,action: #selector(AmountSelectionViewController.dismissKeyBoard))
        tap.delegate = self
        self.view.addGestureRecognizer(tap)
        
        super.viewDidLoad()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
    //MARK: - Collection view delegates
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        switch section {
        case 0:
            return 6
        default:
            return 1
        }
    }
    
    func numberOfSectionsInCollectionView(collectionView: UICollectionView) -> Int {
        return 4
    }
    
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        switch indexPath.section {
        case 0:
            let cell = collectionView.dequeueReusableCellWithReuseIdentifier("AmountCollectionViewCell", forIndexPath: indexPath) as! AmountCollectionViewCell
            
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
                cell.amountLabel.textColor = UIColor.whiteColor()
                cell.amountLabel.layer.borderColor = UIColor(red: 248/255, green: 153/255, blue:33/255, alpha:1.0).CGColor
            }
            else{
                cell.amountLabel.backgroundColor = UIColor.whiteColor()
                cell.amountLabel.textColor = UIColor(red: 94/255, green: 94/255, blue:94/255, alpha:1.0)
                cell.amountLabel.layer.borderColor = UIColor(red: 215/255, green: 215/255, blue:215/255, alpha:1.0).CGColor
            }
            
            return cell
        case 2:
            let cell = collectionView.dequeueReusableCellWithReuseIdentifier("AmountCustomSelectCollectionViewCell", forIndexPath: indexPath) as! AmountCustomSelectCollectionViewCell
            cell.amountLabel.delegate = self
            cell.amountLabel.text = amountInText
            let description = String(format: "eGift Card Price Range " + GiftCardAppConstants.amountWithZeroDecimalPoint + " - " + GiftCardAppConstants.amountWithZeroDecimalPoint,giftCardBrand!.variableAmountDenominationMinimumValue!,giftCardBrand!.variableAmountDenominationMaximumValue!)
            cell.amountDescription.text = description
            return cell
        default:
            let cell = collectionView.dequeueReusableCellWithReuseIdentifier("SolidLine", forIndexPath: indexPath)
            return cell
        }
        
    }
    
    func collectionView(collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAtIndexPath indexPath: NSIndexPath) -> CGSize {
        switch indexPath.section {
        case 0:
            let cellgap = (self.collectionView.frame.size.width * 0.20) + (self.collectionView.frame.size.height * 0.10)
            let collectionViewCellWidth = (self.collectionView.frame.size.width - cellgap)/3
            let collectionViewCellHeight = collectionViewCellWidth * 0.575
            return CGSizeMake(collectionViewCellWidth, collectionViewCellHeight)
        case 2:
            let cellgap = (self.collectionView.frame.size.width * 0.20)
            return CGSizeMake(self.collectionView.frame.size.width - cellgap, 90)
        default:
            return CGSizeMake(self.collectionView.frame.size.width, 1)
        }
    }
    
    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        selectedItem = indexPath.item
        let cell = collectionView.cellForItemAtIndexPath(indexPath)
        if let amountCell = cell as! AmountCollectionViewCell?{
            amountInText = (amountCell.amountLabel.text!)
            //check amount is in range
            var amountInTextWithoutDoller = amountInText
            if amountInTextWithoutDoller.characters.contains("$") {
                amountInTextWithoutDoller = String(amountInText.characters.dropFirst())
            }
            let amount:Double = Double(amountInTextWithoutDoller)!
            if (amount < giftCardBrand?.variableAmountDenominationMinimumValue || amount > giftCardBrand?.variableAmountDenominationMaximumValue) {
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
    
    func collectionView(collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAtIndex section: Int) -> UIEdgeInsets {
        let topBottom  = (self.collectionView.frame.size.height * 0.05)
        switch section {
        case 0:
            let leftRightInset = self.collectionView.frame.size.width * 0.10
            return UIEdgeInsetsMake(topBottom, leftRightInset, topBottom, leftRightInset)
        case 2:
            let leftRightInset = self.collectionView.frame.size.width * 0.10
            return UIEdgeInsetsMake(topBottom, leftRightInset, topBottom, leftRightInset)
        default:
            return UIEdgeInsetsZero
        }
    }
    
    func collectionView(collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAtIndex section: Int) -> CGFloat {
        return self.collectionView.frame.size.height * 0.05
    }
    
    //MARK: - Text field delegates
    func keyBoardWillShown(notification:NSNotification){
        let userInfo:NSDictionary = notification.userInfo!
        let keyboardFrame:NSValue = userInfo.valueForKey(UIKeyboardFrameEndUserInfoKey) as! NSValue
        
        let indexPath = NSIndexPath(forRow: 0, inSection:2);
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue, animations: {
            self.collectionViewBottomSpace.constant = keyboardFrame.CGRectValue().height
            self.view.layoutIfNeeded()
        }) { (completed) in
            if completed {
                self.collectionView.scrollToItemAtIndexPath(indexPath, atScrollPosition: .Top, animated: true)
            }
        }
    }
    
    func keyBoardWillHidden(notification:NSNotification){
        let userInfo:NSDictionary = notification.userInfo!
        UIView.animateWithDuration((userInfo.valueForKey(UIKeyboardAnimationDurationUserInfoKey) as! NSNumber).doubleValue) {
            self.collectionViewBottomSpace.constant = 0
            self.view.layoutIfNeeded()
        }
        
    }
    
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        var textValue = textField.text! as NSString
        textValue = textValue.stringByReplacingCharactersInRange(range, withString: string)
        let textValueString = textValue as String
        let indexPath = NSIndexPath(forItem: 0, inSection: 2)
        let cell = collectionView.cellForItemAtIndexPath(indexPath)
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
    
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        view.endEditing(true);
        if (amountInText == "") {
            selectedItem = 0
            collectionView.reloadData()
        } else {
            amountInText = amountInText.trim()
            
            let indexPath = NSIndexPath(forItem: 0, inSection: 2)
            let cell = collectionView.cellForItemAtIndexPath(indexPath)
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
            
            if (amount < giftCardBrand?.variableAmountDenominationMinimumValue || amount > giftCardBrand?.variableAmountDenominationMaximumValue) {
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
    
    func textFieldDidBeginEditing(textField: UITextField) {
        let indexPath = NSIndexPath(forItem: 0, inSection: 2)
        let cell = collectionView.cellForItemAtIndexPath(indexPath)
        if let cellType = cell as? AmountCustomSelectCollectionViewCell {
            cellType.amountLabel.becomeFirstResponder()
        }
        amountInText = ""
        deselectDefaultAmount()
    }
    
    func deselectDefaultAmount() {
        for index in 0...6 {
            let indexPath = NSIndexPath(forItem: index, inSection: 0)
            let cell = collectionView.cellForItemAtIndexPath(indexPath)
            if let cellType = cell as? AmountCollectionViewCell {
                cellType.amountLabel.backgroundColor = UIColor.whiteColor()
                cellType.amountLabel.textColor = UIColor(red: 94/255, green: 94/255, blue:94/255, alpha:1.0)
                cellType.amountLabel.layer.borderColor = UIColor(red: 215/255, green: 215/255, blue:215/255, alpha:1.0).CGColor
            }
        }
    }
    
    func dismissKeyBoard(){
        self.view.endEditing(true)
    }
    
    func gestureRecognizer(gestureRecognizer: UIGestureRecognizer, shouldReceiveTouch touch: UITouch) -> Bool {
        
        if ((touch.view?.isKindOfClass(UILabel)) == true){
            return false
        }
        
        return true
    }
    
    
    //MARK: - IBAction methods
    @IBAction func continueButtonPressed(sender:UIButton){
        if (amountInText != "") {
            if amountInText.characters.contains("$") {
                amountInText = String(amountInText.characters.dropFirst())
            }
            let amount = Double(amountInText)
            
            if (amount < giftCardBrand?.variableAmountDenominationMinimumValue || amount > giftCardBrand?.variableAmountDenominationMaximumValue) {
                showInvalidPriceRangeError()
                return
            }
            delegate?.selectedAmount(String(format: GiftCardAppConstants.amountWithTwoDecimalPoint, amount!))
        }
        popViewController()
    }
    
    @IBAction func close(){
        popViewController()
    }
    
    
    //MARK: - Regex for custom amount validation
    func isCustomAmount(testStr:String) -> Bool {
        var amountRegEx = "[$]{0,1}[0-9]{0,3}[.]?"
        if testStr.characters.contains(".") {
            amountRegEx = "[$]{0,1}[0-9]{0,3}[.]{1}[0-9]{0,2}"
        }
        let amountTest = NSPredicate(format:"SELF MATCHES %@", amountRegEx)
        return amountTest.evaluateWithObject(testStr)
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
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
}
