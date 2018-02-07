//
//  MapViewController.swift
//  JambaJuice
//
//  Created by VT02 on 3/15/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation
import HDK


typealias UserLocationCallback = (_ location: CLLocation?, _ error: NSError?) -> Void

class MapViewController: UIViewController, MKMapViewDelegate, CLLocationManagerDelegate {
    
    
    @IBOutlet weak var storeMapView: MKMapView!
    @IBOutlet weak var navigationIcon:UIButton!
    
    var storeDetails:Store?
    var myRoute : MKRoute!
    var locationmanager = CLLocationManager()
    
    var sourceLocationLatitude:CLLocationDegrees?
    var sourceLocationLongtitude:CLLocationDegrees?
    
    var userLocationLatitude:CLLocationDegrees?
    var userLocationLongtitude:CLLocationDegrees?
    
    let source = MKPointAnnotation()
    let destination = MKPointAnnotation()
    var locationManager: CLLocationManager?
    
    
    override func viewDidLoad() {
        locationManager = CLLocationManager()
        locationManager?.delegate = self
        self.storeMapView.showsUserLocation = true
        locationManager?.distanceFilter = kCLDistanceFilterNone
        locationManager?.desiredAccuracy = kCLLocationAccuracyBest
        navigationIcon.tag = 0
        self.getUserLocation()
        //  self.storeDetails?.latitude = 12.9144
        //   self.storeDetails?.longitude = 80.2018
        super.viewDidLoad()
        
        // showStoreDirection()
    }
    
    //Close
    @IBAction func closeScreen(_ sender: AnyObject) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    // when user click default map
    @IBAction func openDefaultMap(_ sender: AnyObject) {
        let location = CLLocation(latitude: (storeDetails?.latitude)!, longitude: (storeDetails?.longitude)!)
        MapsManager.openMapsWithDirectionsToLocation(location, name: (storeDetails?.name)!)
        AnalyticsService.trackEvent("stores", action: "get_directions", label: storeDetails?.name)
    }
    
    // when user click pan to currentLocation
    @IBAction func panToCurrentLocation(_ sender: AnyObject) {
        regionCenterWhenPan()
    }
    
    // drawing overlay path for source to destination
    func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
        let myLineRenderer = MKPolylineRenderer(polyline: myRoute.polyline)
        myLineRenderer.strokeColor = UIColor(hex: 0x007AFF)
        myLineRenderer.lineWidth = 5
        return myLineRenderer
    }
    
    // source and destinatio annotation
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        if annotation.isKind(of: MKUserLocation.self) {
            return nil
        }
        let annotationView = MKAnnotationView(annotation: annotation, reuseIdentifier: "pin")
        annotationView.frame.size = CGSize(width: 43.0, height: 49.0)
        annotationView.canShowCallout = true
        if annotation.coordinate.latitude == destination.coordinate.latitude{
            annotationView.image = UIImage(named: "destination-icon.png")
        }else{
            annotationView.image = UIImage(named: "source-icon.png")
            
        }
        return annotationView
    }
    
    // create source and destination path
    func sourceAndDestination(){
        
        // set annotation
        let sourceCoOrdinate = CLLocationCoordinate2DMake(sourceLocationLatitude!, sourceLocationLongtitude!)
        self.source.coordinate = sourceCoOrdinate
        self.source.title = "Your Starting Location"
        self.storeMapView.addAnnotation(self.source)
        self.destination.title = self.storeDetails?.name
        self.destination.coordinate = CLLocationCoordinate2DMake(self.storeDetails!.latitude, self.storeDetails!.longitude)
        self.destination.title = self.storeDetails?.name
        self.storeMapView.addAnnotation(self.destination)
        
        // calculate center co ordinates
        let midLat = (self.source.coordinate.latitude + self.destination.coordinate.latitude)/2
        let midLang = (self.source.coordinate.longitude + self.destination.coordinate.longitude)/2
        let centerCoOrdinate = CLLocationCoordinate2DMake(midLat, midLang)
        self.storeMapView.setRegion(MKCoordinateRegionMake(centerCoOrdinate, MKCoordinateSpanMake(1.5, 1.5)), animated: true)
        self.deActiveNavigationIcon()
        
        // set direction request
        let request = MKDirectionsRequest()
        request.source =  MKMapItem(placemark: MKPlacemark(coordinate: CLLocationCoordinate2D(latitude: self.source.coordinate.latitude, longitude: self.source.coordinate.longitude), addressDictionary: nil))
        request.destination = MKMapItem(placemark: MKPlacemark(coordinate: CLLocationCoordinate2D(latitude: self.destination.coordinate.latitude, longitude: self.destination.coordinate.longitude), addressDictionary: nil))
        request.requestsAlternateRoutes = false
        request.transportType = .automobile
        let directions = MKDirections(request: request)
        directions.calculate { (response, error) in
            if error == nil {
                self.myRoute = response!.routes[0] as MKRoute
                self.storeMapView.add(self.myRoute.polyline)
                let rect = self.myRoute.polyline.boundingMapRect
//                self.storeMapView.setRegion(MKCoordinateRegionForMapRect(rect), animated: true)
                self.storeMapView.setVisibleMapRect(rect, edgePadding: UIEdgeInsets(top: 50.0, left: 50.0, bottom: 50.0, right: 50.0), animated: true)
            }else{
                self.presentOkAlert("Alert", message: "No route found.", callback: {
                    self.panToDestinationLocation()
                })
            }
        }
    }
    
    // validate and get user location
    func getUserLocation() {
        let status = CLLocationManager.authorizationStatus()
        if status == .notDetermined {
            locationManager?.requestWhenInUseAuthorization()
        }else if status == .denied || status == .restricted {
            showSettingAlert()
        }
        else if status == .authorizedWhenInUse || status == .authorizedAlways {
            if userLocationLatitude == nil && userLocationLongtitude == nil{
               locationManager?.startUpdatingLocation()
            }
        }
    }
    
    // show enable location service
    func showSettingAlert(){
        let alertController = UIAlertController (title: "Location Services Disabled",message: "To use your own location to find nearby Jamba Juice® stores, please enable Location Services on the Settings app.", preferredStyle: .alert)
        
        let settingsAction = UIAlertAction(title: "Settings", style: .default) { (_) -> Void in
            let settingsUrl = URL(string: UIApplicationOpenSettingsURLString)
            if let url = settingsUrl {
                UIApplication.shared.openURL(url)
            }
        }
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .default, handler: nil)
        alertController.addAction(settingsAction)
        alertController.addAction(cancelAction)
        
        present(alertController, animated: true, completion: nil)
    }
    
    // MARK: - Location Manager delegate
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if locations.count > 0 {
            let location = locations.first! as CLLocation
            if sourceLocationLatitude == nil && sourceLocationLongtitude == nil{
                sourceLocationLatitude = location.coordinate.latitude
                sourceLocationLongtitude = location.coordinate.longitude
                userLocationLatitude = location.coordinate.latitude
                userLocationLongtitude = location.coordinate.longitude
                self.sourceAndDestination()
            }else{
                let location = locations.last! as CLLocation
                if userLocationLatitude != location.coordinate.latitude || userLocationLongtitude != location.coordinate.longitude{
                    userLocationLatitude = location.coordinate.latitude
                    userLocationLongtitude = location.coordinate.longitude
                    self.activeNavigationIcon()
                }
            }
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if touches.count > 0{
           self.deActiveNavigationIcon()
        }
        super.touchesBegan(touches, with: event)
    }
    
    // when navigation button click
    func regionCenterWhenPan(){
        if navigationIcon.tag == 0 {
            let status = CLLocationManager.authorizationStatus()
            if status == .notDetermined {
                locationManager?.requestWhenInUseAuthorization()
            }else if status == .denied || status == .restricted {
                showSettingAlert()
            }
            else if status == .authorizedWhenInUse || status == .authorizedAlways {
               self.activeNavigationIcon()
            }
        }
    }
    
    // Delegate user change the status
//    func locationManager(manager: CLLocationManager, didChangeAuthorizationStatus status: CLAuthorizationStatus) {
//        let status = CLLocationManager.authorizationStatus()
//        if status == .NotDetermined {
//            locationManager?.requestAlwaysAuthorization()
//        }else if status == .Denied || status == .Restricted {
//            showSettingAlert()
//        }
//        else if status == .AuthorizedWhenInUse || status == .AuthorizedAlways {
//            if userLocationLatitude == nil && userLocationLongtitude == nil{
//                locationManager?.startUpdatingLocation()
//            }
//        }
//    }
    
    func setUserLocationRegion(){
        if userLocationLatitude != nil && userLocationLongtitude != nil{
        
            let regionCoordinate = CLLocationCoordinate2DMake(userLocationLatitude!, userLocationLongtitude!)
            
            let region = MKCoordinateRegion(center: regionCoordinate, span: MKCoordinateSpan(latitudeDelta: 0.007, longitudeDelta: 0.007))
            //set region on the map
            storeMapView.setRegion(region, animated: true)
        }
    }
    
    func activeNavigationIcon(){
        navigationIcon.tag = 1
        navigationIcon.setImage(UIImage(named: Constants.locationIconActive), for: UIControlState())
        self.setUserLocationRegion()
        locationManager?.startUpdatingLocation()
    }
    
    func deActiveNavigationIcon(){
        navigationIcon.tag = 0
        navigationIcon.setImage(UIImage(named: Constants.locationIconInactive), for: UIControlState())
        locationManager?.stopUpdatingLocation()
        
    }
    
    // Navigating to Destination location
    func panToDestinationLocation(){
        if self.storeDetails?.latitude != nil && self.storeDetails?.longitude != nil{
            let regionCoordinate = CLLocationCoordinate2DMake(storeDetails!.latitude, storeDetails!.longitude)
            let region = MKCoordinateRegion(center: regionCoordinate, span: MKCoordinateSpan(latitudeDelta: 0.007, longitudeDelta: 0.007))
            //set region on the map
            storeMapView.setRegion(region, animated: true)
        }
    }
}
