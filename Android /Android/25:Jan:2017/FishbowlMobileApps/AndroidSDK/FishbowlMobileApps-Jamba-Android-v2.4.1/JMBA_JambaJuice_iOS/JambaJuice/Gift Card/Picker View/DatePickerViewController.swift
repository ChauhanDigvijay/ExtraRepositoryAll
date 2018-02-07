//
//  PayViaGiftCardViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 11/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

protocol DatePickerViewControllerDelegate: class {
    func datePickerValueChanged(_ value:String)
    func closeDatePickerScreen()
}

class DatePickerViewController: UIViewController {
    @IBOutlet weak var picker                           : UIDatePicker!
    @IBOutlet weak var pickerHeaderTitleLabel           : UILabel!
    @IBOutlet weak var pickerBottomSpace                : NSLayoutConstraint!
    @IBOutlet weak var pickerBackgroundViewBottomSpace  : NSLayoutConstraint!
    @IBOutlet weak var backgroundView                   : UIView!
    weak var delegate                                   : DatePickerViewControllerDelegate?
    let dateFormatter                                   = DateFormatter()
    
    
    override func viewDidLoad() {
        //update date format
        dateFormatter.dateFormat = GiftCardAppConstants.ShortDateFormat
        
        pickerBottomSpace.constant = -1 * GiftCardAppConstants.pickerViewHeight
        pickerBackgroundViewBottomSpace.constant = -1 * GiftCardAppConstants.pickerViewHeight
        backgroundView.backgroundColor = UIColor.clear
        let date = Date()
        picker.minimumDate = date
        super.viewDidLoad()
    }
    
    //MARK: - IBAction method
    @IBAction func datePickerSelectionDone() {
        let selectedDate = picker.date
        delegate?.datePickerValueChanged(dateFormatter.string(from: selectedDate))
        closePicker()
    }
    
    @IBAction func closePicker() {
        self.view.layoutIfNeeded()
        UIView.animate(withDuration: GiftCardAppConstants.animationDuration, animations: {
            self.pickerBottomSpace.constant = -1 * GiftCardAppConstants.pickerViewHeight
            self.pickerBackgroundViewBottomSpace.constant = -1 * GiftCardAppConstants.pickerViewHeight
            self.backgroundView.backgroundColor = UIColor.clear
            self.view.layoutIfNeeded()
            }, completion: {
                (value: Bool) in
                self.delegate?.closeDatePickerScreen()
        })
    }
    
    //MARK: - Show picker animation
    func showPicker() {
        self.view.layoutIfNeeded()
        UIView.animate(withDuration: GiftCardAppConstants.animationDuration, animations: {
            self.pickerBottomSpace.constant = 0
            self.pickerBackgroundViewBottomSpace.constant = 0
            self.backgroundView.backgroundColor = UIColor(red: 0/255, green: 0/255, blue: 0/255, alpha: 0.2)
            self.view.layoutIfNeeded()
        })
    }
    
    //MARK: - Retain selected value
    func retainSelectedValue(_ dateInString:String) {
        if dateInString == "" {
            return
        }
        let selectedDate = dateFormatter.date(from: dateInString)!
        picker.setDate(selectedDate, animated: true)
    }
}
