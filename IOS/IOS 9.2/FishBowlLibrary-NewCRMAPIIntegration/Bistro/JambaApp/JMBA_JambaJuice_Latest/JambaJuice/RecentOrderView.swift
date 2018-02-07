//
//  RecentOrderView.swift
//  JambaJuice
//
//  Created by Taha Samad on 24/06/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

class RecentOrderView: UIView {
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var timeAgoLabel: UILabel!
    @IBOutlet weak var widthConstraint: NSLayoutConstraint!
    @IBOutlet weak var heightConstraint: NSLayoutConstraint!
    @IBOutlet weak var transparentButton: UIButton!
    @IBOutlet weak var titleBottomSpaceConstraint: NSLayoutConstraint!
    
    class func recentOrderViewFromNib() -> RecentOrderView {
        return UINib(nibName: "RecentOrderView", bundle: nil).instantiateWithOwner(nil, options: nil)[0] as! RecentOrderView
    }

}
