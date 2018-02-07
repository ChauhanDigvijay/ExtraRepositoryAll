//
//  PagedImagesViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 21/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol PagedImagesViewControllerDelegate: class {
    func pagedImagesViewControllerDidChangePage(_ viewController: PagedImagesViewController)
}

class PagedImagesViewController: UIViewController, UIScrollViewDelegate {

    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var pageControl: UIPageControl!

    weak var delegate: PagedImagesViewControllerDelegate?

    fileprivate var initialPage: Int = 0
    fileprivate var pages = Array(Constants.userProfileAvatars.keys)
    fileprivate var pageSize: CGRect = CGRect(x: 0, y: 0, width: 0, height: 0)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // For whatever reason, neither scrollView nor view have the rigth frame
        pageSize = CGRect(x: 0, y: 0, width: view.bounds.width, height: 160)
        pageControl.numberOfPages = pages.count
        scrollView.contentSize = CGSize(width: pageSize.width * CGFloat(pages.count), height: pageSize.height)

        // Add pages
        for index in 0..<pages.count {
            addPage(index)
        }

        moveToPage(initialPage)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    func currentImageName() -> String {
        return pages[pageControl.currentPage]
    }
    
    // FIXME: This is called before viewDidLoad, not sure if this is a good thing
    func loadIntialPageWithName(_ name: String) {
        initialPage = pages.index(of: name) ?? 0
    }

    
    // MARK: Private logic
    
    fileprivate func moveToPage(_ page: Int) {
        scrollView.contentOffset.x = pageSize.width * CGFloat(page)
        updateFromPosition()
    }
    
    fileprivate func addPage(_ index: Int) {
        let imageView = UIImageView(image: UIImage(named: pages[index]))
        imageView.contentMode = .center
        imageView.frame = CGRect(x: CGFloat(index) * pageSize.width, y: 0, width: pageSize.width, height: pageSize.height)
        scrollView.addSubview(imageView)
    }

    fileprivate func updateFromPosition() {
        pageControl.currentPage = Int(scrollView.contentOffset.x + (pageSize.width / 2)) / Int(pageSize.width)
        UIView.animate(withDuration: 0.2, animations: {
            let bgColor = Constants.userProfileAvatars[self.pages[self.pageControl.currentPage]]!
            self.scrollView.backgroundColor = UIColor(hex: bgColor)
        }) 
        delegate?.pagedImagesViewControllerDidChangePage(self)
    }
    
    // MARK: ScrollView Delegate
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        updateFromPosition()
    }

    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if decelerate == false {
            updateFromPosition()
        }
    }
    
//    func scrollViewDidScroll(scrollView: UIScrollView) {
//        updateFromPosition()
//    }
    
}
