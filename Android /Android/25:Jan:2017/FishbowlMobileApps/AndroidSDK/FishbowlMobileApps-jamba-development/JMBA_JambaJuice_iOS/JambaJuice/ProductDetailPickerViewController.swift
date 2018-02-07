//
//  ProductDetailPickerViewController.swift
//  JambaJuice
//
//  Created by VT016 on 19/11/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit

protocol ProductDetailPickerViewControllerDelegate: class {
    func pickerValueChanged(value:String,index:Int)
    func closepickerScreen()
}

class ProductDetailPickerViewController: UIViewController,UIPickerViewDelegate {
    @IBOutlet weak var picker                   : UIPickerView!
    @IBOutlet weak var pickerHeaderTitleLabel   : UILabel!
    @IBOutlet weak var pickerBottomSpace        : NSLayoutConstraint!
    @IBOutlet weak var backgroundView           : UIView!
    var pickerData:[String]                        = ["1","2","3","4","5","6","7","8","9","10"];
    var noOfComponents                          = 2;
    var selectedValue1                          : String = "";
    var selectedValue2                          : String = "";
    weak var delegate                           : ProductDetailPickerViewControllerDelegate?
    let pickerViewHeight:CGFloat                = 256
    
    override func viewDidLoad() {
        pickerBottomSpace.constant = -pickerViewHeight
        backgroundView.backgroundColor = UIColor.clearColor()
        super.viewDidLoad()
    }
    
    //MARK: - Picker view methods
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 1;
    }
    
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int{
        return pickerData.count;
    }
    
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[row]
    }
    
    //MARK: - Retain picker selected values
    func retainSelectedValue()  {
        if (selectedValue1 == "") {
            picker.selectRow(0, inComponent: 0, animated: true)
            picker.selectRow(0, inComponent: 1, animated: true)
            return
        }
        for index in 0...pickerData.count {
            if (pickerData[index] == selectedValue1) {
                picker.selectRow(index, inComponent: 0, animated: true)
                break
            }
        }
    }
    
    //set maximu quantity to user picker
    func setMaxQuantity(maxValue:Int) {
        pickerData = []
        for value in 1...maxValue {
            pickerData.append("\(value)")
        }
    }
    
    //MARK: - IBAction methods
    @IBAction func closePicker() {
        self.view.layoutIfNeeded()
        UIView.animateWithDuration(0.5, animations: {
            self.pickerBottomSpace.constant = -1 * self.pickerViewHeight
            self.backgroundView.backgroundColor = UIColor.clearColor()
            self.view.layoutIfNeeded()
            }, completion: {
                (value: Bool) in
                self.delegate?.closepickerScreen()
        })
    }
    
    //when user press save, pass the value
    @IBAction func savePickerValue() {
        let res:String = "\(pickerData[picker.selectedRowInComponent(0)])"
        delegate?.pickerValueChanged(res,index: picker.selectedRowInComponent(0))
        closePicker()
    }
    
    //MARK: - Show picker animation
    func showPicker() {
        self.view.layoutIfNeeded()
        UIView.animateWithDuration(0.5, animations: {
            self.pickerBottomSpace.constant = 0
            self.backgroundView.backgroundColor = UIColor(red: 0/255, green: 0/255, blue: 0/255, alpha: 0.2)
            self.view.layoutIfNeeded()
        })
    }
}