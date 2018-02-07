//
//  SignUpStoreDetailViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 13/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import MapKit

class SignUpStoreDetailViewController: UIViewController {

    @IBOutlet weak var dotsPageControl: UIPageControl!

    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var storeNameLabel: UILabel!
    @IBOutlet weak var storeAddressLabel: UILabel!
    @IBOutlet weak var orderAheadIconImageView: UIImageView!
    @IBOutlet weak var orderAheadLabel: UILabel!
    
    var store: Store!

    override func viewDidLoad() {
        super.viewDidLoad()
        storeNameLabel.text = store.name
        storeAddressLabel.text = store.addressAndDistance

        if UIScreen.mainScreen().is35inch() {
            dotsPageControl.removeFromSuperview()
        }
        
        // Update map
        let annotation = MKPointAnnotation()
        annotation.coordinate = CLLocationCoordinate2DMake(store.latitude, store.longitude)
        mapView.addAnnotation(annotation)
        mapView.showsUserLocation = LocationService.userHasAuthorizeLocation()
        mapView.showAnnotations(mapView.annotations, animated: false)

        if store.supportsOrderAhead {
            orderAheadIconImageView.image = UIImage(named: "order-ahead-icon")
            orderAheadLabel.text = "Order Ahead is available at this store"
            orderAheadLabel.textColor = UIColor(hex: Constants.jambaDarkGreenColor)
        } else {
            orderAheadIconImageView.image = UIImage(named: "order-ahead-icon-gray")
            orderAheadLabel.text = "Order Ahead is not available at this store"
            orderAheadLabel.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    override func shouldPerformSegueWithIdentifier(identifier: String?, sender: AnyObject?) -> Bool {
        // This should be SpendGo Store ID. SpendGo Store ID should always be present.
        UserService.signUpUserInfo?.favoriteStoreCode = store.storeCode
        log.verbose("\(UserService.signUpUserInfo)")
        return true
    }
    
    @IBAction func ValidateStoreAhead(){

            UserService.signUpUserInfo?.favoriteStoreCode = store.storeCode
            log.verbose("\(UserService.signUpUserInfo)")

            self.performSegueWithIdentifier("name&email", sender: self)

    }
    
}
