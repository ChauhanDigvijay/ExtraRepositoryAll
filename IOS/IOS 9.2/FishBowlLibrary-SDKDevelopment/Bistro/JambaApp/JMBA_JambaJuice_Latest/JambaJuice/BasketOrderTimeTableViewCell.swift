//
//  BasketOrderTimeTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 26/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol BasketOrderTimeTableViewCellDelegate: class {
    func orderTimeCellDidChangeSelectedDay(cell: BasketOrderTimeTableViewCell)
    func orderTimeCellSliderValueDidChange(cell: BasketOrderTimeTableViewCell)
}

class BasketOrderTimeTableViewCell: UITableViewCell {
    
    @IBOutlet weak var asapButton: SelectableButton!
    @IBOutlet weak var todayButton: SelectableButton!
    @IBOutlet weak var tomorrowButton: SelectableButton!

    @IBOutlet weak var pickupSliderView: UIView!
    @IBOutlet weak var pickupSliderViewHeight: NSLayoutConstraint!
    @IBOutlet weak var selectedTimeLabel: UILabel!
    @IBOutlet weak var startTimeLabel: UILabel!
    @IBOutlet weak var endTimeLabel: UILabel!
    @IBOutlet weak var slider: UISlider!
    
    weak var delegate: BasketOrderTimeTableViewCellDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()

        // Add target so we can ensure that only one is selected at a time
        asapButton.addTarget(self, action: #selector(BasketOrderTimeTableViewCell.asapSelected(_:)), forControlEvents: .TouchUpInside)
        todayButton.addTarget(self, action: #selector(BasketOrderTimeTableViewCell.todaySelected(_:)), forControlEvents: .TouchUpInside)
        tomorrowButton.addTarget(self, action: #selector(BasketOrderTimeTableViewCell.tomorrowSelected(_:)), forControlEvents: .TouchUpInside)
        slider.addTarget(self, action: #selector(BasketOrderTimeTableViewCell.sliderValueChanged(_:)), forControlEvents: .ValueChanged)
        
        let ASAPFont:UIFont = UIFont(name: "Archer-SemiBold", size: 17.0)!
        let in15MinFont:UIFont = UIFont(name: "Archer-SemiBold", size: 12.0)!
        let textColor:UIColor = UIColor.whiteColor()
        
        let ASAPText = "ASAP\n"
        let in15MinText = "in 15 min"
        
        let ASAPAttribute = [NSFontAttributeName: ASAPFont, NSForegroundColorAttributeName: textColor]
        let ASAPAttributeString = NSMutableAttributedString(string:ASAPText, attributes:ASAPAttribute)
        let in15MinAttribute =  [NSFontAttributeName:in15MinFont, NSForegroundColorAttributeName: textColor]
        let sin15MinAttributeString = NSAttributedString(string:in15MinText, attributes:in15MinAttribute)
        ASAPAttributeString.appendAttributedString(sin15MinAttributeString)
        asapButton.setAttributedTitle(ASAPAttributeString,forState: .Normal)
    }
    
    func asapSelected(button: SelectableButton) {
        log.verbose("Pickup day: ASAP")
        selectDay(.ASAP)
        delegate?.orderTimeCellDidChangeSelectedDay(self)
    }
    
    func todaySelected(button: SelectableButton) {
        log.verbose("Pickup day: Today")
        selectDay(.Today)
        delegate?.orderTimeCellDidChangeSelectedDay(self)
    }
    
    func tomorrowSelected(button: SelectableButton) {
        log.verbose("Pickup day: Tomorrow")
        selectDay(.Tomorrow)
        delegate?.orderTimeCellDidChangeSelectedDay(self)
    }
    
    func sliderValueChanged(slider: UISlider) {
        delegate?.orderTimeCellSliderValueDidChange(self)
    }

    func selectDay(day: PickupDay) {
        switch day {
        case .ASAP:
            log.verbose("Select pickup day: ASAP")
            asapButton.selected = true
            todayButton.selected = false
            tomorrowButton.selected = false
            pickupSliderView.hidden = true
            pickupSliderViewHeight.constant = 0
            slider.enabled = false
        case .Today:
            log.verbose("Select pickup day: Today")
            asapButton.selected = false
            todayButton.selected = true
            tomorrowButton.selected = false
            pickupSliderView.hidden = false
            pickupSliderViewHeight.constant = 82
            slider.enabled = true
        case .Tomorrow:
            log.verbose("Select pickup day: Tomorrow")
            asapButton.selected = false
            todayButton.selected = false
            tomorrowButton.selected = true
            pickupSliderView.hidden = false
            pickupSliderViewHeight.constant = 82
            slider.enabled = true
        }
    }
    
    func selectedDay() -> PickupDay {
        if todayButton.selected {
            log.verbose("Pickup day selected: Today")
            return .Today
        }
        else if tomorrowButton.selected {
            log.verbose("Pickup day selected: Tomorrow")
            return .Tomorrow
        }
        
        log.verbose("Pickup day selected: ASAP")
        return .ASAP
    }
    
    override func prepareForReuse() {
        log.verbose("BasketOrderTimeTableViewCell Prepare for reuse")
       // selectDay(.ASAP)
    }
    
}
