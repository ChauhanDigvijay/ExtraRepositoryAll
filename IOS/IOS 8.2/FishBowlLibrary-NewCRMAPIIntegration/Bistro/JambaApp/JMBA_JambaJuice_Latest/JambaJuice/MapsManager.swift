//
//  MapsManager.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 6/5/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import Foundation
import MapKit

class MapsManager {

    /// Open Maps application with driving directions to the given location
    class func openMapsWithDirectionsToLocation(location: CLLocation, name: String) {

        let placeMark = MKPlacemark(coordinate: location.coordinate, addressDictionary: nil)
        let mapItem = MKMapItem(placemark: placeMark)
        mapItem.name = name
        let launchOptions = [MKLaunchOptionsDirectionsModeKey : MKLaunchOptionsDirectionsModeDriving]
        // Get the "Current User Location" MKMapItem
        let currentLocationMapItem = MKMapItem.mapItemForCurrentLocation()
        MKMapItem.openMapsWithItems([currentLocationMapItem, mapItem], launchOptions: launchOptions)
    }
    
    /// Search for Jamba Juice stores on Maps application
    class func openMapsSearchingStoresNearLocation(name: String?) {

        let query = name == nil || name!.isEmpty ? "Jamba Juice" : "Jamba Juice near \(name!)"
        let urlString = "http://maps.apple.com?q=" + (query.stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.URLQueryAllowedCharacterSet()) ?? "")
        log.verbose("Maps url: \(urlString)")
        if let url = NSURL(string: urlString) {
            UIApplication.sharedApplication().openURL(url)
        }
    }
    
}
