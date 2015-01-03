//
//  MapKitViewController.swift
//  KNOWTime
//
//  Created by Aaron Eisses on 12/31/14.
//  Copyright (c) 2014 KNOWTime. All rights reserved.
//

import Foundation
import MapKit
import UIKit

class MapKitViewController: UIViewController, WebSocketClientDelegate, MKMapViewDelegate {

    required init(coder aDecoder: NSCoder) {
        let serverUrl = NSURL(string: serverUrlString)
        self.webSocketClient = WebSocketClient(url: serverUrl!)
        self.annotationsArray = NSMutableArray()
        super.init(coder: aDecoder);
    }
    
    @IBOutlet weak var mapView: MKMapView!
    
    let serverUrlString = "https://afternoon-eyrie-7322.herokuapp.com"
    var webSocketClient:WebSocketClient
    let annotationsArray:NSMutableArray
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.webSocketClient.delegate = self;
        // Do any additional setup after loading the view, typically from a nib.
        let location = CLLocationCoordinate2D(
            latitude: 44.64745,
            longitude: -63.601491
        )

        let span = MKCoordinateSpanMake(0.014200, 0.011654)
        let region = MKCoordinateRegion(center: location, span: span)
        mapView.setRegion(region, animated: true)
    }
  
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
  
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated);
        // Do any additional setup after loading the view, typically from a nib.
        webSocketClient.openWebSocketClient()
    }
    
    func process(response: NSDictionary) {
        let latitude:CLLocationDegrees = response["latitude"] as CLLocationDegrees
        let longitude:CLLocationDegrees = response["longitide"] as CLLocationDegrees
        var location:CLLocationCoordinate2D = CLLocationCoordinate2DMake(latitude, longitude)
        var information = MKPointAnnotation()
        information.coordinate = location
        information.title = response["tripId"] as NSString
        var foundValue:Bool = false
        for element in self.annotationsArray {
            var annotation:MKPointAnnotation = element as MKPointAnnotation
            if (annotation.title == information.title) {
//                mapView.removeAnnotation(annotation)
                annotationsArray.removeObject(annotation)
                annotationsArray.addObject(information)
//                mapView.addAnnotation(annotation)
                foundValue = true
            }
        }
        if (!foundValue) {
            self.annotationsArray.addObject(information)
//            mapView.addAnnotation(information);
        }
        mapView.removeAnnotations(mapView.annotations)
        mapView.addAnnotations(self.annotationsArray)
    }
    
}