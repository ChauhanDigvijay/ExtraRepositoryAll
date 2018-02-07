//
//  BasketOrderTimeTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 26/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol BasketOrderTimeTableViewCellDelegate: class {
    func orderTimeCellDidChangeSelectedDay(_ cell: BasketOrderTimeTableViewCell)
    func orderTimeCellSliderValueDidChange(_ cell: BasketOrderTimeTableViewCell)
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
        asapButton.addTarget(self, action: #selector(BasketOrderTimeTableViewCell.asapSelected(_:)), for: .touchUpInside)
        todayButton.addTarget(self, action: #selector(BasketOrderTimeTableViewCell.todaySelected(_:)), for: .touchUpInside)
        tomorrowButton.addTarget(self, action: #selector(BasketOrderTimeTableViewCell.tomorrowSelected(_:)), for: .touchUpInside)
        slider.addTarget(self, action: #selector(BasketOrderTimeTableViewCell.sliderValueChanged(_:)), for: .valueChanged)
        
        
    }
    
    func updateButtonlabel() {
        let ASAPFont:UIFont = UIFont.init(name: "Archer-SemiBold", size: 17.0)!
        let in15MinFont:UIFont = UIFont.init(name: "Archer-SemiBold", size: 12.0)!
        let textColor:UIColor = UIColor.white
        
        let ASAPText = "ASAP\n"
        var in15MinText = ""
        if BasketService.sharedBasket!.deliveryMode == deliveryMode.pickup.rawValue {
            in15MinText = String(format: "in %.0f mins", BasketService.sharedBasket!.leadTimeEstimateMinutes )
        } else {
            in15MinText = String(format: "in %.0f mins", BasketService.sharedBasket!.leadTimeEstimateMinutes )
        }
        
        
        let ASAPAttribute = [NSFontAttributeName: ASAPFont, NSForegroundColorAttributeName: textColor]
        let ASAPAttributeString = NSMutableAttributedString(string:ASAPText, attributes:ASAPAttribute)
        let in15MinAttribute =  [NSFontAttributeName:in15MinFont, NSForegroundColorAttributeName: textColor]
        let sin15MinAttributeString = NSAttributedString(string:in15MinText, attributes:in15MinAttribute)
        ASAPAttributeString.append(sin15MinAttributeString)
        asapButton.setAttributedTitle(ASAPAttributeString,for: .normal)
        
        if BasketService.sharedBasket!.deliveryMode == deliveryMode.delivery.rawValue {
            tomorrowButton.setTitle("Later", for: .normal)
        } else {
            tomorrowButton.setTitle("Tomorrow", for: .normal)
        }
    }
    
    func asapSelected(_ button: SelectableButton) {
        log.verbose("Pickup day: ASAP")
        selectDay(.asap)
        delegate?.orderTimeCellDidChangeSelectedDay(self)
    }
    
    func todaySelected(_ button: SelectableButton) {
        log.verbose("Pickup day: Today")
        selectDay(.today)
        delegate?.orderTimeCellDidChangeSelectedDay(self)
    }
    
    func tomorrowSelected(_ button: SelectableButton) {
        log.verbose("Pickup day: Tomorrow")
        selectDay(.tomorrow)
        delegate?.orderTimeCellDidChangeSelectedDay(self)
    }
    
    func sliderValueChanged(_ slider: UISlider) {
        delegate?.orderTimeCellSliderValueDidChange(self)
    }

    func selectDay(_ day: PickupDay) {
        startTimeLabel.isHidden = false
        endTimeLabel.isHidden = false
        switch day {
        case .asap:
            log.verbose("Select pickup day: ASAP")
            asapButton.isSelected = true
            todayButton.isSelected = false
            tomorrowButton.isSelected = false
            pickupSliderView.isHidden = true
            pickupSliderViewHeight.constant = 0
            slider.isEnabled = false
        case .today:
            log.verbose("Select pickup day: Today")
            asapButton.isSelected = false
            todayButton.isSelected = true
            tomorrowButton.isSelected = false
            pickupSliderView.isHidden = false
            pickupSliderViewHeight.constant = 82
            slider.isEnabled = true
        case .tomorrow:
            log.verbose("Select pickup day: Tomorrow")
            asapButton.isSelected = false
            todayButton.isSelected = false
            tomorrowButton.isSelected = true
            pickupSliderView.isHidden = false
            pickupSliderViewHeight.constant = 82
            slider.isEnabled = true
            
            if BasketService.sharedBasket!.deliveryMode == deliveryMode.delivery.rawValue {
                tomorrowButton.setTitle("Later", for: .normal)
                slider.isHidden = true
                pickupSliderViewHeight.constant = 20
                startTimeLabel.isHidden = true
                endTimeLabel.isHidden = true
            } else {
                tomorrowButton.setTitle("Tomorrow", for: .normal)
                slider.isHidden = false
            }
        }
    }
    
    func selectedDay() -> PickupDay {
        if todayButton.isSelected {
            log.verbose("Pickup day selected: Today")
            return .today
        }
        else if tomorrowButton.isSelected {
            log.verbose("Pickup day selected: Tomorrow")
            return .tomorrow
        }
        
        log.verbose("Pickup day selected: ASAP")
        return .asap
    }
    
    override func prepareForReuse() {
        log.verbose("BasketOrderTimeTableViewCell Prepare for reuse")
       // selectDay(.ASAP)
    }
    
}
