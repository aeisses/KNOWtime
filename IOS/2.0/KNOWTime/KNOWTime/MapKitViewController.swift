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

class MapKitViewController: UIViewController {
  
//  let webSocketClient:WebSocketClient
 
  override func viewDidLoad() {
    super.viewDidLoad()
    // Do any additional setup after loading the view, typically from a nib.
  }
  
  override func didReceiveMemoryWarning() {
    super.didReceiveMemoryWarning()
    // Dispose of any resources that can be recreated.
  }
  
  override func viewDidAppear(animated: Bool) {
    super.viewDidAppear(animated);
    // Do any additional setup after loading the view, typically from a nib.
    let serverUrl = NSURL(string: "https://www.espn.com")
    let webSocketClient = WebSocketClient(url: serverUrl!)
  }

}