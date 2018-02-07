//
//  RecentOrdersCarouselViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/2/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK
import AlamofireImage

protocol RecentOrdersCarouselViewControllerDelegate: class {
    func recentOrdersCarouselViewControllerDidGetTapped(_ vc: RecentOrdersCarouselViewController)
    func recentOrdersCarouselViewControllerCloseMenu()
}

class RecentOrdersCarouselViewController: UIViewController, UIScrollViewDelegate {

    weak var delegate: RecentOrdersCarouselViewControllerDelegate?
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var contentViewWidthConstraint: NSLayoutConstraint!
    @IBOutlet weak var pageControl: UIPageControl!
    
    fileprivate var adsList: AdProductsList = AdClass.init()
    fileprivate var orderViews = Array<RecentOrderView>()
    
    let defaultAdImageRatio:CGFloat = 0.56
    
    var timer:Timer?

    fileprivate let pageWidth = UIScreen.main.bounds.width

    var currentIndex: Int {
        get {
            return pageControl.currentPage
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        pageControl.hidesForSinglePage = true
        pageControl.isHidden = true
    }

    // Create new views for each order and update carousel
    func updateRecentlyOrderedProducts(_ adsList: AdProductsList) {
        if adsList.adsDetailList.count > 0 {
            timer?.invalidate()
            timer = nil
            timer = Timer.scheduledTimer(timeInterval: Double(adsList.rotation_int), target: self, selector: #selector(self.scrollToNextPage), userInfo: nil, repeats: true)
        }
        self.adsList = adsList
        clearOrders()
        
        var index = 0
        for product in adsList.adsDetailList {
            // Add view
            let recentOrderView = RecentOrderView.recentOrderViewFromNib()
            let width = UIScreen.main.bounds.width
            recentOrderView.widthConstraint.constant = width
            recentOrderView.heightConstraint.constant = width * Constants.adImageAspectRatio
            recentOrderView.nameLabel.isHidden = true;
            recentOrderView.timeAgoLabel.isHidden = true
            if let url = URL(string: product.image_url) {
                recentOrderView.imageView.af_setImage(withURL: url, placeholderImage: UIImage(named: "product-placeholder"), filter: nil, progress: nil, progressQueue: .main, imageTransition: .noTransition, runImageTransitionIfCached: false, completion: { (image) in
                    
                })
            }
            recentOrderView.widthConstraint.constant = pageWidth
            contentView.addSubview(recentOrderView)
            
            // Adjust layout
            let x = pageWidth * CGFloat(index)
            let leftConstraint = NSLayoutConstraint.constraints(withVisualFormat: "H:|-spacing-[v]", options: NSLayoutFormatOptions.directionLeftToRight, metrics: ["spacing": x], views: ["v": recentOrderView])[0]
            let topConstraint = NSLayoutConstraint.constraints(withVisualFormat: "V:|[v]|", options: NSLayoutFormatOptions.directionLeftToRight, metrics: nil, views: ["v": recentOrderView])[0]
            contentView.addConstraint(leftConstraint)
            contentView.addConstraint(topConstraint)
            
            // TODO: Should use tap gesture recognizer instead of transparent button on top
            recentOrderView.transparentButton.addTarget(self, action: #selector(RecentOrdersCarouselViewController.recentOrderTapped(_:)), for: .touchUpInside)
            orderViews.append(recentOrderView)
            
            index += 1;
        }
        contentViewWidthConstraint.constant = pageWidth * CGFloat(adsList.adsDetailList.count)
        pageControl.numberOfPages = adsList.adsDetailList.count
        pageControl.isHidden = true
        moveToPage(pageControl.currentPage)
    }
    
    func recentOrderTapped(_ sender: UIButton) {
        delegate?.recentOrdersCarouselViewControllerDidGetTapped(self)
    }
    
    @IBAction func closeMenu(_ sender: UIButton) {
        trackButtonPressWithName("swipe-down-button")
        if(UserService.sharedUser==nil){
            NonSignedInViewController.sharedInstance().closeModalScreen()
        } else {
            SignedInMainViewController.sharedInstance().closeModalScreen()
        }
    }
    
    // Remove all subviews
    fileprivate func clearOrders() {
        for view in orderViews {
            view.removeFromSuperview()
        }
        orderViews = []
    }
    
    fileprivate func updateFromPosition() {
        pageControl.currentPage = Int(scrollView.contentOffset.x + (pageWidth / 2)) / Int(pageWidth)
    }
    
    fileprivate func moveToPage(_ page: Int) {
        scrollView.contentOffset.x = pageWidth * CGFloat(page)
        updateFromPosition()
    }

    // MARK: ScrollView delegate
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        updateFromPosition()
    }
    
    @IBAction func navigateSelectedPageView(_ sender: UIPageControl) {
        pageControl.currentPage = sender.currentPage
        var frame:CGRect = scrollView.frame;
        frame.origin.x = pageWidth * CGFloat(pageControl.currentPage)
        frame.origin.y = 0;
        scrollView.scrollRectToVisible(frame, animated: true)
    }
    
    func scrollToNextPage() {
        var currentPage = pageControl.currentPage + 1
        if currentPage >= self.adsList.adsDetailList.count {
            currentPage = 0
        }
        var frame:CGRect = scrollView.frame;
        frame.origin.x = pageWidth * CGFloat(currentPage)
        frame.origin.y = 0;
        pageControl.currentPage = currentPage + 1;
        if currentPage > 0 {
        scrollView.scrollRectToVisible(frame, animated: true)
        } else {
            scrollView.scrollRectToVisible(frame, animated: false)
        }
    }
}
