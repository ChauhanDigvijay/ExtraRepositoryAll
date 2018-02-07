//
//  ProductCollectionViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/9/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol ProductCollectionViewControllerDelegate {
    func didSelectProductFromCollection(product: Product)
}

class ProductCollectionViewController: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate, UIScrollViewDelegate {

    // Currenlty supports only 4 product families
    @IBOutlet var productFamily1Button: UIButton!
    @IBOutlet var productFamily2Button: UIButton!
    @IBOutlet var productFamily3Button: UIButton!
    @IBOutlet var productFamily4Button: UIButton!

    @IBOutlet var productCollectionView: UICollectionView!

    var delegate: ProductCollectionViewControllerDelegate?

    private var familyButtons: [UIButton]!
    private var familyCategoryMap: [Int: Int] = [:]
    private var categoryFamilyMap: [Int: Int] = [:]
    private var scrollingToFamily: Bool = false

    override func viewDidLoad() {
        super.viewDidLoad()
        setupProductFamilies()
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    private func setupProductFamilies() {
        let families = ProductDataProvider.productTree
        let categories = ProductDataProvider.productCategories
        familyButtons = [productFamily1Button, productFamily2Button, productFamily3Button, productFamily4Button]

        // Configure family buttons
        for (index, familyButton) in familyButtons.enumerate() {
            if let family = families.getAt(index) {
                familyButton.setTitle(family.name, forState: .Normal)
                familyButton.tag = index
                familyButton.hidden = false
            } else {
                familyButton.hidden = true
            }
        }

        // Configure family-to-category and category-to-family index maps
        for (familyIndex, family) in families.enumerate() {
            let firstCategory = family.categories[0]
            familyCategoryMap[familyIndex] = categories.indexOf(firstCategory)
            for category in family.categories {
                if let categoryIndex = categories.indexOf(category) {
                    categoryFamilyMap[categoryIndex] = familyIndex
                }
            }
        }

        selectFamilyButton(productFamily1Button)
    }


    // MARK: Navigation

    @IBAction func selectProductFamily(sender: UIButton) {
        trackButtonPress(sender)
        SessionExpirationService.sharedInstance.trackUserActivity()
        selectFamilyButton(sender)

        // Scroll to first category of selected family
        scrollingToFamily = true
        let categoryIndex = familyCategoryMap[sender.tag] ?? 0
        let indexPath = NSIndexPath(forRow: 0, inSection: categoryIndex)
        let offsetY = productCollectionView.layoutAttributesForSupplementaryElementOfKind(UICollectionElementKindSectionHeader, atIndexPath: indexPath)!.frame.origin.y
        productCollectionView.setContentOffset(CGPointMake(productCollectionView.contentOffset.x, offsetY), animated: true)
    }

    private func selectFamilyButton(selectedButton: UIButton?) {
        for button in familyButtons {
            button.selected = false
            button.backgroundColor = UIColor.whiteColor()
        }
        selectedButton?.selected = true
        selectedButton?.backgroundColor = UIColor(hex: Constants.jambaLightBlueColor)
    }

    private func selectFamilyButton(index: Int) {
        let button = familyButtons.getAt(index)
        selectFamilyButton(button)
    }


    // MARK: UICollectionViewDataSource

    func numberOfSectionsInCollectionView(collectionView: UICollectionView) -> Int {
        return ProductDataProvider.productCategories.count
    }

    func collectionView(collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, atIndexPath indexPath: NSIndexPath) -> UICollectionReusableView {
        if kind == UICollectionElementKindSectionHeader {
            guard let productCategory = ProductDataProvider.productCategories.getAt(indexPath.section) else {
                fatalError("Invalid section/row")
            }
            guard let view = collectionView.dequeueReusableSupplementaryViewOfKind(UICollectionElementKindSectionHeader, withReuseIdentifier: "CategoryHeader", forIndexPath: indexPath) as? ProductCategoryCollectionReusableView else {
                fatalError("Could not get header view")
            }
            view.categoryNameLabel.text = productCategory.name
            return view
        } else if kind == UICollectionElementKindSectionFooter {
            let view = collectionView.dequeueReusableSupplementaryViewOfKind(UICollectionElementKindSectionFooter, withReuseIdentifier: "CategoryFooter", forIndexPath: indexPath)
            return view
        }
        return UICollectionReusableView()
    }

    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        let productCategory = ProductDataProvider.productCategories[section]
        return productCategory.products.count
    }

    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        guard let productCategory = ProductDataProvider.productCategories.getAt(indexPath.section) else {
            fatalError("Invalid section/row")
        }
        guard let product = productCategory.products.getAt(indexPath.row) else {
            fatalError("Invalid section/row")
        }
        guard let cell = collectionView.dequeueReusableCellWithReuseIdentifier("Cell", forIndexPath: indexPath) as? ProductCollectionViewCell else {
            fatalError("Could not load cell")
        }
        cell.update(product)
        return cell
    }


    // MARK: UICollectionViewDelegate

    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        let cell = collectionView.cellForItemAtIndexPath(indexPath) as? ProductCollectionViewCell
        if let product = cell?.product {
            delegate?.didSelectProductFromCollection(product)
        }
    }


    // MARK: UIScrollViewDelegate

    func scrollViewWillBeginDragging(scrollView: UIScrollView) {
        SessionExpirationService.sharedInstance.trackUserActivity()
    }

    func scrollViewDidEndDecelerating(scrollView: UIScrollView) {
        SessionExpirationService.sharedInstance.trackUserActivity()
        scrollingToFamily = false
    }

    func scrollViewDidScroll(scrollView: UIScrollView) {
        if scrollingToFamily {
            return
        }
        if let indexPath = productCollectionView.indexPathsForVisibleItems().first {
            let categoryIndex = indexPath.section
            if let familyIndex = categoryFamilyMap[categoryIndex] {
                selectFamilyButton(familyIndex)
            }
        }
    }

    func scrollViewDidEndScrollingAnimation(scrollView: UIScrollView) {
        scrollingToFamily = false
    }

}
