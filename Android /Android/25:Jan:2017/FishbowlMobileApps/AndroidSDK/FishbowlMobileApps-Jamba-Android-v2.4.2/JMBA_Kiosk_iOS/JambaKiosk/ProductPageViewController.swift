//
//  ProductPageViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/11/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class ProductPageViewController: UIPageViewController, UIPageViewControllerDataSource {

    var productDetailDelegate: ProductDetailViewControllerDelegate? // Pass through delegate
    var initialProduct: Product?
    private var products: ProductList = []

    override func viewDidLoad() {
        super.viewDidLoad()
        dataSource = self

        products = ProductDataProvider.allProducts

        let product = initialProduct ?? products[0]
        initializeWithProduct(product)
    }

    func initializeWithProduct(product: Product) {
        let initialViewController = viewControllerWithProduct(product)
        setViewControllers([initialViewController], direction: .Forward, animated: false, completion: nil)
    }


    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    func presentationCountForPageViewController(pageViewController: UIPageViewController) -> Int {
        return products.count
    }

    func pageViewController(pageViewController: UIPageViewController, viewControllerBeforeViewController viewController: UIViewController) -> UIViewController? {
        let index = indexForViewController(viewController)
        if index > 0 {
            return viewControllerForIndex(index - 1)
        } else {
            return nil
        }
    }

    func pageViewController(pageViewController: UIPageViewController, viewControllerAfterViewController viewController: UIViewController) -> UIViewController? {
        let index = indexForViewController(viewController)
        if index < products.count - 1 {
            return viewControllerForIndex(index + 1)
        } else {
            return nil
        }
    }

    private func indexForViewController(viewController: UIViewController) -> Int {
        guard let vc = viewController as? ProductDetailViewController else {
            fatalError("Could not find season controller")
        }
        guard let index = products.indexOf(vc.product) else {
            fatalError("Could not find index for season controller")
        }
        return index
    }

    private func viewControllerForIndex(index: Int) -> ProductDetailViewController {
        let product = products[index]
        return viewControllerWithProduct(product)
    }

    private func viewControllerWithProduct(product: Product) -> ProductDetailViewController {
        guard let vc = storyboard?.instantiateViewControllerWithIdentifier("ProductDetailViewController") as? ProductDetailViewController else {
            fatalError("Could not instantiate season controller")
        }
        vc.delegate = productDetailDelegate // Passthrough delegate
        vc.product = product
        return vc
    }

}
