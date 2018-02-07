//
//  ScrollableModalViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/23/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//
//  TODO: Find better alternative to base classes
//  TODO: Move this to HDK
//
//  ScrollableModalViewContoller is a base class for modal View Controllers
//  with enhanced ScrollView functionality:
//   - Pull down to reveal the screen behind it
//   - Pull down further to close the screen

import UIKit

class ScrollableModalViewController: UIViewController, UIScrollViewDelegate {

    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var scrollViewContentHeightConstraint: NSLayoutConstraint!
    var scrollViewBackgroundColor = UIColor.clear
    
    override func viewDidLoad() {
        super.viewDidLoad()
        updateScreen()
    }

  @objc  internal func updateScreen() {
        // Adjust scroll view content height
        let minScrollViewContentHeight = UIScreen.main.bounds.height + 1 // +1 to make it always scrollable
        scrollViewContentHeightConstraint.constant = max(heightForScrollViewContent(), minScrollViewContentHeight)
    }
    
    // Override in child controllers to return the height of the scroll view content
    internal func heightForScrollViewContent() -> CGFloat {
        return UIScreen.main.bounds.height
    }
    
    
    // MARK: UIScrollView delegate
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        // Adjust background color to reveal home screen at the top only
        if scrollView.contentOffset.y < 0 {
            scrollView.backgroundColor = UIColor.clear
        } else {
            scrollView.backgroundColor = scrollViewBackgroundColor
        }
    }

    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if scrollView.contentOffset.y < Constants.scrollViewThresholdToDismissModal {
            trackScreenEvent("close_screen", label: "drag_down")
            dismissModalController()
        }
    }
    
}
