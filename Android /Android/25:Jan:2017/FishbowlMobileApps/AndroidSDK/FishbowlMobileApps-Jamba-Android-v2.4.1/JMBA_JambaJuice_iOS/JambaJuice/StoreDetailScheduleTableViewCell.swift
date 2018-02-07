//
//  StoreDetailScheduleTableViewCell.swift
//  JambaJuice
//
//  Created by Taha Samad on 19/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class StoreDetailScheduleTableViewCell: UITableViewCell {

    @IBOutlet weak var mondayLabel: UILabel!
    @IBOutlet weak var tuesdayLabel: UILabel!
    @IBOutlet weak var wednesdayLabel: UILabel!
    @IBOutlet weak var thursdayLabel: UILabel!
    @IBOutlet weak var fridayLabel: UILabel!
    @IBOutlet weak var saturdayLabel: UILabel!
    @IBOutlet weak var sundayLabel: UILabel!

    @IBOutlet weak var mondayTimeLabel: UILabel!
    @IBOutlet weak var tuesdayTimeLabel: UILabel!
    @IBOutlet weak var wednesdayTimeLabel: UILabel!
    @IBOutlet weak var thursdayTimeLabel: UILabel!
    @IBOutlet weak var fridayTimeLabel: UILabel!
    @IBOutlet weak var saturdayTimeLabel: UILabel!
    @IBOutlet weak var sundayTimeLabel: UILabel!

    func update(_ scheduleDictionary: StoreScheduleDictionary) {
        mondayTimeLabel.text    = scheduleDictionary["Monday"]    ?? "Closed"
        tuesdayTimeLabel.text   = scheduleDictionary["Tuesday"]   ?? "Closed"
        wednesdayTimeLabel.text = scheduleDictionary["Wednesday"] ?? "Closed"
        thursdayTimeLabel.text  = scheduleDictionary["Thursday"]  ?? "Closed"
        fridayTimeLabel.text    = scheduleDictionary["Friday"]    ?? "Closed"
        saturdayTimeLabel.text  = scheduleDictionary["Saturday"]  ?? "Closed"
        sundayTimeLabel.text    = scheduleDictionary["Sunday"]    ?? "Closed"

        let formatter = DateFormatter()
        formatter.dateFormat = "EEEE"
        let today = formatter.string(from: NSDate() as Date).lowercased()
        let darkGray = UIColor(hex: Constants.jambaDarkGrayColor)

        switch today {
        case "monday":
            mondayLabel.textColor = darkGray
            mondayTimeLabel.textColor = darkGray
        case "tuesday":
            tuesdayLabel.textColor = darkGray
            tuesdayTimeLabel.textColor = darkGray
        case "wednesday":
            wednesdayLabel.textColor = darkGray
            wednesdayTimeLabel.textColor = darkGray
        case "thursday":
            thursdayLabel.textColor = darkGray
            thursdayTimeLabel.textColor = darkGray
        case "friday":
            fridayLabel.textColor = darkGray
            fridayTimeLabel.textColor = darkGray
        case "saturday":
            saturdayLabel.textColor = darkGray
            saturdayTimeLabel.textColor = darkGray
        case "sunday":
            sundayLabel.textColor = darkGray
            sundayTimeLabel.textColor = darkGray
        default:
            break
        }
    }
    
}
