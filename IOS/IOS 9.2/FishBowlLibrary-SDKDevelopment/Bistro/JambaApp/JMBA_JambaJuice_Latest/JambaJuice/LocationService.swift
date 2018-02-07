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

typealias LocationCallback = (location: CLLocation?, error: NSError?) -> Void
typealias ZipCodeCallback = (zipcode: String, error: NSError?) -> Void

class LocationService: NSObject, CLLocationManagerDelegate {
   
    private var locationManager: CLLocationManager?
    private var userLocationCallback: LocationCallback?
    private var timeoutTimer: NSTimer?

    static let sharedInstance = LocationService()

    override init() {
        super.init()
        locationManager = CLLocationManager()
        locationManager?.delegate = self
    }

    func getUserLocation(callback: LocationCallback) {
        log.verbose("Getting user location")
        userLocationCallback = callback
        let status = CLLocationManager.authorizationStatus()
        if status == .NotDetermined {
           locationManager?.requestAlwaysAuthorization()
        
        }
        if status == .AuthorizedWhenInUse || status == .AuthorizedAlways {
            locationManager?.startUpdatingLocation()
            setTimeoutTimer()
        }
        else {
            userDeclinedAccess()
        }
    }
    
    class func locationForString(search: String, completion: LocationCallback) {
        let geocoder = CLGeocoder()
        geocoder.geocodeAddressString(search) { (placemarkList, error) in
            guard let placemarks = placemarkList else {
                completion(location: nil, error: NSError(description: "Could not determine location, please enter a more precise address."))
                return
            }
            if error == nil && placemarks.count > 0 {
                let placemark = placemarks.first
                completion(location: placemark?.location, error: nil)
            } else {
                completion(location: nil, error: NSError(description: "Could not determine location"))
            }
        }
    }
    
    class func zipcodeForLocation(location: CLLocation, callback: ZipCodeCallback) {
        CLGeocoder().reverseGeocodeLocation(location) { (placemarkList, error) -> Void in
            log.verbose("\(location)")
            
            if error != nil {
                log.error("\(error!.localizedDescription)")
                callback(zipcode: "", error: error)
                return
            }

            guard let placemarks = placemarkList else {
                log.error("No placemarks returned")
                callback(zipcode: "", error: NSError(description: "No placemarks returned"))
                return
            }

            if placemarks.count < 1 {
                log.error("No placemarks returned")
                callback(zipcode: "", error: NSError(description: "No placemarks returned"))
                return
            }
            
            let placemark: CLPlacemark = placemarks[0]
            log.verbose("Country: \(placemark.country), City: \(placemark.locality), Zip: \(placemark.postalCode)")
            if placemark.country != "United States" {
                callback(zipcode: "", error: NSError(description: "Can only search for US locations."))
                return
            }
            if placemark.postalCode == nil {
                callback(zipcode: "", error: NSError(description: "Could not determine zip code."))
                return
            }
            
            callback(zipcode: placemark.postalCode!, error: nil)
        }
    }
    
    // Calculate the distance between two locations in meters
    class func distanceBetweenLocations(locationA: CLLocation, locationB: CLLocation) -> Double {
        return abs(locationA.distanceFromLocation(locationB))
    }
    
    class func userHasAuthorizeLocation() -> Bool {
        let status = CLLocationManager.authorizationStatus()
        return status == .AuthorizedAlways || status == .AuthorizedWhenInUse
    }
    
    class func userHasDeniedLocation() -> Bool {
        let status = CLLocationManager.authorizationStatus()
        return status == .Denied || status == .Restricted
    }

    func userLocation() -> CLLocation? {
        return locationManager?.location
    }
    
    // MARK: - Location Manager delegate
    
    func locationManager(manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        log.verbose("CLLocationManager.didUpdateLocations")
        log.verbose("\(locations)")
        if locations.count > 0 {
            unsetTimeoutTimer()
            userLocationCallback?(location: locations.first, error: nil)
            userLocationCallback = nil
        }
        manager.stopUpdatingLocation()
    }
    
    func locationManager(manager: CLLocationManager, didChangeAuthorizationStatus status: CLAuthorizationStatus) {
        log.verbose("CLLocationManager.didChangeAuthorizationStatus")
        if status == .Denied {
            // User declined access to their location
            userDeclinedAccess()
        } else {
            locationManager?.startUpdatingLocation()
        }
    }

    private func userDeclinedAccess() {
        log.info("User declined access")
        userLocationCallback?(location: nil, error: nil) // No error in this case
        userLocationCallback = nil
    }
    
    // MARK: Timeout timer
    
    private func setTimeoutTimer() {
        log.verbose("Set timeout timer")
        timeoutTimer = NSTimer.scheduledTimerWithTimeInterval(5.0, target: self, selector: #selector(LocationService.timeout), userInfo: nil, repeats: false)
    }

    private func unsetTimeoutTimer() {
        log.verbose("Unset timeout timer")
        timeoutTimer?.invalidate()
        timeoutTimer = nil
    }
    
    func timeout() {
        log.warning("Failed to determine user location in time")
        userLocationCallback?(location: nil, error: NSError(description: "Could not determine user location"))
        userLocationCallback = nil
    }
    
}
