//
//  RecentOrdersCarouselViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/2/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK

protocol RecentOrdersCarouselViewControllerDelegate: class {
    func recentOrdersCarouselViewControllerDidGetTapped(vc: RecentOrdersCarouselViewController)
    func recentOrdersCarouselViewControllerCloseMenu()
}

class RecentOrdersCarouselViewController: UIViewController, UIScrollViewDelegate {

    weak var delegate: RecentOrdersCarouselViewControllerDelegate?
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var contentViewWidthConstraint: NSLayoutConstraint!
    @IBOutlet weak var pageControl: UIPageControl!
    
    private var recentlyOrderedProducts: ProductList = []
    private var orderViews = Array<RecentOrderView>()

    private let pageWidth = UIScreen.mainScreen().bounds.width

    var currentIndex: Int {
        get {
            return pageControl.currentPage
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        pageControl.hidesForSinglePage = true
    }
    
    // Create new views for each order and update carousel
    func updateRecentlyOrderedProducts(recentlyOrderedProducts: ProductList) {
        self.recentlyOrderedProducts = recentlyOrderedProducts
        clearOrders()

        var index = 0
        for product in recentlyOrderedProducts {
            // Add view
            let recentOrderView = RecentOrderView.recentOrderViewFromNib()
            recentOrderView.nameLabel.text = product.name
            if let orderedTime = product.lastOrderedTime {
                recentOrderView.timeAgoLabel.text = "ordered \(orderedTime.currentDateIfDateInFuture().timeAgoWithDayAsMinUnit())".uppercaseString
                recentOrderView.timeAgoLabel.hidden = false
                recentOrderView.titleBottomSpaceConstraint.constant = 58
            } else {
                recentOrderView.timeAgoLabel.hidden = true
                recentOrderView.titleBottomSpaceConstraint.constant = 36
            }
            if let url = NSURL(string: product.orderImageUrl) {
                recentOrderView.imageView.hnk_setImageFromURL(url, placeholder: UIImage(named: "product-placeholder"))
            }
            recentOrderView.widthConstraint.constant = pageWidth
            contentView.addSubview(recentOrderView)

            // Adjust layout
            let x = pageWidth * CGFloat(index)
            let leftConstraint = NSLayoutConstraint.constraintsWithVisualFormat("H:|-spacing-[v]", options: NSLayoutFormatOptions.DirectionLeftToRight, metrics: ["spacing": x], views: ["v": recentOrderView])[0]
            let topConstraint = NSLayoutConstraint.constraintsWithVisualFormat("V:|[v]|", options: NSLayoutFormatOptions.DirectionLeftToRight, metrics: nil, views: ["v": recentOrderView])[0]
            contentView.addConstraint(leftConstraint)
            contentView.addConstraint(topConstraint)

            // TODO: Should use tap gesture recognizer instead of transparent button on top
            recentOrderView.transparentButton.addTarget(self, action: #selector(RecentOrdersCarouselViewController.recentOrderTapped(_:)), forControlEvents: .TouchUpInside)
            orderViews.append(recentOrderView)
            
            index += 1;
        }
        contentViewWidthConstraint.constant = pageWidth * CGFloat(recentlyOrderedProducts.count)
        pageControl.numberOfPages = recentlyOrderedProducts.count
        moveToPage(pageControl.currentPage)
    }
    
    func recentOrderTapped(sender: UIButton) {
        delegate?.recentOrdersCarouselViewControllerDidGetTapped(self)
    }
    
    @IBAction func closeMenu(sender: UIButton) {
        trackButtonPressWithName("swipe-down-button")
        if(UserService.sharedUser==nil){
            NonSignedInViewController.sharedInstance().closeModalScreen()
        } else {
            SignedInMainViewController.sharedInstance().closeModalScreen()
        }
//        delegate?.recentOrdersCarouselViewControllerCloseMenu()
//          dismissModalController()
    }
    
    // Remove all subviews
    private func clearOrders() {
        for view in orderViews {
            view.removeFromSuperview()
        }
        orderViews = []
    }
    
    private func updateFromPosition() {
        pageControl.currentPage = Int(scrollView.contentOffset.x + (pageWidth / 2)) / Int(pageWidth)
    }
    
    private func moveToPage(page: Int) {
        scrollView.contentOffset.x = pageWidth * CGFloat(page)
        updateFromPosition()
    }

    // MARK: ScrollView delegate
    
    func scrollViewDidScroll(scrollView: UIScrollView) {
        updateFromPosition()
    }
    
    @IBAction func navigateSelectedPageView(sender: UIPageControl) {
        pageControl.currentPage = sender.currentPage
//        moveToPage(pageControl.currentPage)
        var frame:CGRect = scrollView.frame;
        frame.origin.x = pageWidth * CGFloat(pageControl.currentPage)
        frame.origin.y = 0;
        scrollView.scrollRectToVisible(frame, animated: true)
    }
    
}
