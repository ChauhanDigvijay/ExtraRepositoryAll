//
//  PayViaGiftCardViewController.swift
//  JambaGiftCard
//
//  Created by vThink on 11/08/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit

protocol PickerViewControllerDelegate: class {
    func pickerValueChanged(_ value:String,index:Int)
    func closepickerScreen()
}

class PickerViewController: UIViewController,UIPickerViewDelegate {
    @IBOutlet weak var picker                   : UIPickerView!
    @IBOutlet weak var pickerHeaderTitleLabel   : UILabel!
    @IBOutlet weak var pickerBottomSpace        : NSLayoutConstraint!
    @IBOutlet weak var backgroundView           : UIView!
    var pickerData:[[String]]                   = [[],[]];
    var noOfComponents                          = 2;
    var selectedValue1                          : String = "";
    var selectedValue2                          : String = "";
    weak var delegate                           : PickerViewControllerDelegate?
    
    override func viewDidLoad() {
        pickerBottomSpace.constant = -1 * GiftCardAppConstants.pickerViewHeight
        backgroundView.backgroundColor = UIColor.clear
        super.viewDidLoad()
    }
    
    //MARK: - Picker view methods
    func numberOfComponentsInPickerView(_ pickerView: UIPickerView) -> Int {
        return noOfComponents;
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int{
        return pickerData[component].count;
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[component][row]
    }
    
    //MARK: - Retain picker selected values
    func retainSelectedValue()  {
        if (selectedValue1 == "") {
            picker.selectRow(0, inComponent: 0, animated: true)
            picker.selectRow(0, inComponent: 1, animated: true)
            return
        }
        for index in 0...pickerData[0].count-1 {
            if (pickerData[0][index] == selectedValue1) {
                picker.selectRow(index, inComponent: 0, animated: true)
                break
            }
        }
        if (noOfComponents > 1) {
            for index in 0...pickerData[1].count-1 {
                if (pickerData[1][index] == selectedValue2) {
                    picker.selectRow(index, inComponent: 1, animated: true)
                    return
                }
            }
        }
    }
    
    //MARK: - IBAction methods
    @IBAction func closePicker() {
        self.view.layoutIfNeeded()
        UIView.animate(withDuration: GiftCardAppConstants.animationDuration, animations: {
            self.pickerBottomSpace.constant = -1 * GiftCardAppConstants.pickerViewHeight
            self.backgroundView.backgroundColor = UIColor.clear
            self.view.layoutIfNeeded()
            }, completion: {
                (value: Bool) in
                self.delegate?.closepickerScreen()
        })
    }
    
    //when user press save, pass the value
    @IBAction func savePickerValue() {
        if (noOfComponents == 1) {
            let res:String = "\(pickerData[0][picker.selectedRow(inComponent: 0)])"
            delegate?.pickerValueChanged(res,index: picker.selectedRow(inComponent: 0))
        } else if (noOfComponents == 2){
            let res:String = "\(pickerData[0][picker.selectedRow(inComponent: 0)]) - \(pickerData[1][picker.selectedRow(inComponent: 1)])"
            delegate?.pickerValueChanged(res,index: picker.selectedRow(inComponent: 0))
        }
        closePicker()
    }
    
    //MARK: - Show picker animation
    func showPicker() {
        self.view.layoutIfNeeded()
        UIView.animate(withDuration: GiftCardAppConstants.animationDuration, animations: {
            self.pickerBottomSpace.constant = 0
            self.backgroundView.backgroundColor = UIColor(red: 0/255, green: 0/255, blue: 0/255, alpha: 0.2)
            self.view.layoutIfNeeded()
        })
    }
}
