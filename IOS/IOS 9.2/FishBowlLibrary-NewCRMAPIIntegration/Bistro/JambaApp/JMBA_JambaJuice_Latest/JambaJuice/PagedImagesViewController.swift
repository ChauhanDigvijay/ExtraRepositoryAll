//
//  PagedImagesViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 21/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol PagedImagesViewControllerDelegate: class {
    func pagedImagesViewControllerDidChangePage(viewController: PagedImagesViewController)
}

class PagedImagesViewController: UIViewController, UIScrollViewDelegate {

    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var pageControl: UIPageControl!

    weak var delegate: PagedImagesViewControllerDelegate?

    private var initialPage: Int = 0
    private var pages = Array(Constants.userProfileAvatars.keys)
    private var pageSize: CGRect = CGRectMake(0, 0, 0, 0)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // For whatever reason, neither scrollView nor view have the rigth frame
        pageSize = CGRectMake(0, 0, view.bounds.width, 160)
        pageControl.numberOfPages = pages.count
        scrollView.contentSize = CGSize(width: pageSize.width * CGFloat(pages.count), height: pageSize.height)

        // Add pages
        for index in 0..<pages.count {
            addPage(index)
        }

        moveToPage(initialPage)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    func currentImageName() -> String {
        return pages[pageControl.currentPage]
    }
    
    // FIXME: This is called before viewDidLoad, not sure if this is a good thing
    func loadIntialPageWithName(name: String) {
        initialPage = pages.indexOf(name) ?? 0
    }

    
    // MARK: Private logic
    
    private func moveToPage(page: Int) {
        scrollView.contentOffset.x = pageSize.width * CGFloat(page)
        updateFromPosition()
    }
    
    private func addPage(index: Int) {
        let imageView = UIImageView(image: UIImage(named: pages[index]))
        imageView.contentMode = .Center
        imageView.frame = CGRectMake(CGFloat(index) * pageSize.width, 0, pageSize.width, pageSize.height)
        scrollView.addSubview(imageView)
    }

    private func updateFromPosition() {
        pageControl.currentPage = Int(scrollView.contentOffset.x + (pageSize.width / 2)) / Int(pageSize.width)
        UIView.animateWithDuration(0.2) {
            let bgColor = Constants.userProfileAvatars[self.pages[self.pageControl.currentPage]]!
            self.scrollView.backgroundColor = UIColor(hex: bgColor)
        }
        delegate?.pagedImagesViewControllerDidChangePage(self)
    }
    
    // MARK: ScrollView Delegate
    
    func scrollViewDidEndDecelerating(scrollView: UIScrollView) {
        updateFromPosition()
    }

    func scrollViewDidEndDragging(scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if decelerate == false {
            updateFromPosition()
        }
    }
    
//    func scrollViewDidScroll(scrollView: UIScrollView) {
//        updateFromPosition()
//    }
    
}
