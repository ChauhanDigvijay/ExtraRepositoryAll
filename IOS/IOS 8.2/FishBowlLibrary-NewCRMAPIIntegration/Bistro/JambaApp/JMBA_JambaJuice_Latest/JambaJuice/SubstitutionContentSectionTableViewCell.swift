//
//  SubstitutionContentSectionTableViewCell.swift
//  JambaJuice
//
//  Created by Sridhar R on 4/11/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit

class SubstitutionContentSectionTableViewCell: UITableViewCell {
    
//    @IBOutlet weak var substitutionDetailsView:UIView!
//    @IBOutlet weak var contentLabel:UILabel!
//    @IBOutlet weak var extendCollapseImageView:UIImageView!
//    @IBOutlet weak var checkImageView:UIImageView!
//    
//    @IBOutlet weak var detailView:UIView!
//    @IBOutlet weak var substitutionTitleLabel:UILabel!
//    @IBOutlet weak var detailExpandCollapseImageView:UIImageView!
    
    @IBOutlet weak var contentLabel:UILabel!
    @IBOutlet weak var extendCollapseImageView:UIImageView!
    @IBOutlet weak var checkImageView:UIImageView!
    @IBOutlet weak var contentLabelLeadingSpace:NSLayoutConstraint!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
}
