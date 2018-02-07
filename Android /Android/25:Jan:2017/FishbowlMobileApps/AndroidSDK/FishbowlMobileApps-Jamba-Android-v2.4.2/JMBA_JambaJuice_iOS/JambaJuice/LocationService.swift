//
//  LocationService.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/19/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import CoreLocation
import MapKit
import HDK

typealias LocationCallback = (_ location: CLLocation?, _ error: NSError?) -> Void
typealias ZipCodeCallback = (_ zipcode: String, _ error: NSError?) -> Void

class LocationService: NSObject, CLLocationManagerDelegate {
   
    fileprivate var locationManager: CLLocationManager?
    fileprivate var userLocationCallback: LocationCallback?
    fileprivate var timeoutTimer: Timer?

    static let sharedInstance = LocationService()

    override init() {
        super.init()
        locationManager = CLLocationManager()
        locationManager?.delegate = self
    }

    func getUserLocation(_ callback: @escaping LocationCallback) {
        log.verbose("Getting user location")
        userLocationCallback = callback
        let status = CLLocationManager.authorizationStatus()
        if status == .notDetermined {
           locationManager?.requestWhenInUseAuthorization()
        }
        if status == .authorizedWhenInUse || status == .authorizedAlways {
            locationManager?.startUpdatingLocation()
            setTimeoutTimer()
        }
        else {
            userDeclinedAccess()
        }
    }
    
    class func locationForString(_ search: String, completion: @escaping LocationCallback) {
        let geocoder = CLGeocoder()
        geocoder.geocodeAddressString(search) { (placemarkList, error) in
            guard let placemarks = placemarkList else {
                completion(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Could not determine location, please enter a more precise address."]))
                return
            }
            if error == nil && placemarks.count > 0 {
                let placemark = placemarks.first
                completion(placemark?.location, nil)
            } else {
                completion(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Could not determine location"]))
            }
        }
    }
    
    class func zipcodeForLocation(_ location: CLLocation, callback: @escaping ZipCodeCallback) {
        CLGeocoder().reverseGeocodeLocation(location) { (placemarkList, error) -> Void in
            log.verbose("\(location)")
            
            if error != nil {
                log.error("\(error!.localizedDescription)")
                callback("", error as NSError?)
                return
            }

            guard let placemarks = placemarkList else {
                log.error("No placemarks returned")
                callback("", NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"No placemarks returned"]))
                return
            }

            if placemarks.count < 1 {
                log.error("No placemarks returned")
                callback("", NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"No placemarks returned"]))
                return
            }
            
            let placemark: CLPlacemark = placemarks[0]
            log.verbose("Country: \(String(describing: placemark.country)), City: \(String(describing: placemark.locality)), Zip: \(String(describing: placemark.postalCode))")
            if placemark.country != "United States" {
                callback("", NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Can only search for US locations."]))
                return
            }
            if placemark.postalCode == nil {
                callback("", NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Could not determine zip code."]))
                return
            }
            
            callback(placemark.postalCode!, nil)
        }
    }
    
    // Calculate the distance between two locations in meters
    class func distanceBetweenLocations(_ locationA: CLLocation, locationB: CLLocation) -> Double {
        return abs(locationA.distance(from: locationB))
    }
    
    class func userHasAuthorizeLocation() -> Bool {
        let status = CLLocationManager.authorizationStatus()
        return status == .authorizedAlways || status == .authorizedWhenInUse
    }
    
    class func userHasDeniedLocation() -> Bool {
        let status = CLLocationManager.authorizationStatus()
        return status == .denied || status == .restricted
    }

    func userLocation() -> CLLocation? {
        return locationManager?.location
    }
    
    // MARK: - Location Manager delegate
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        log.verbose("CLLocationManager.didUpdateLocations")
        log.verbose("\(locations)")
        if locations.count > 0 {
            unsetTimeoutTimer()
            userLocationCallback?(locations.first, nil)
            userLocationCallback = nil
            
            let coordinate = locations.first!.coordinate
            userLocationLat = "\(coordinate.latitude)"
            userLocationLong = "\(coordinate.longitude)"
        }
        manager.stopUpdatingLocation()
    }
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        log.verbose("CLLocationManager.didChangeAuthorizationStatus")
        if status == .denied {
            // User declined access to their location
            userDeclinedAccess()
        } else {
            locationManager?.startUpdatingLocation()
        }
    }

    fileprivate func userDeclinedAccess() {
        log.info("User declined access")
        userLocationCallback?(nil, nil) // No error in this case
        userLocationCallback = nil
    }
    
    // MARK: Timeout timer
    
    fileprivate func setTimeoutTimer() {
        log.verbose("Set timeout timer")
        timeoutTimer = Timer.scheduledTimer(timeInterval: 5.0, target: self, selector: #selector(LocationService.timeout), userInfo: nil, repeats: false)
    }

    fileprivate func unsetTimeoutTimer() {
        log.verbose("Unset timeout timer")
        timeoutTimer?.invalidate()
        timeoutTimer = nil
    }
    
    @objc func timeout() {
        log.warning("Failed to determine user location in time")
        userLocationCallback?(nil, NSError(domain: "Error", code: 100, userInfo: [NSLocalizedDescriptionKey:"Could not determine user location"]))
        userLocationCallback = nil
    }
    
}
