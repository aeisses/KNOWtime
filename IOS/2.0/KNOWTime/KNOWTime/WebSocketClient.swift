//
//  WebSocketClient.swift
//  KNOWTime
//
//  Created by Aaron Eisses on 12/31/14.
//  Copyright (c) 2014 KNOWTime. All rights reserved.
//

import Foundation
import SwiftyJSON

protocol WebSocketClientDelegate {
    func process(response: NSDictionary)
}

class WebSocketClient: NSObject, SRWebSocketDelegate {
  
    var url: NSURL
    var srWebSocket:SRWebSocket
    var delegate: WebSocketClientDelegate?
    
    init(url: NSURL) {
        self.url = url
        self.srWebSocket = SRWebSocket(URL: url)
        super.init()
        srWebSocket.delegate = self;
    }
  
    func initializeWebSocketClient() {
    }
    
    func openWebSocketClient() {
        srWebSocket.open()
    }

    func closeWebSocketClient() {
        srWebSocket.close()
    }

    func sendData(data: NSString) {
        srWebSocket.send(data)
    }

    // SRWebSocket Delegate method functions
    // Required
    func webSocket(webSocket: SRWebSocket!, didReceiveMessage message: AnyObject) {
        println("Websocket message: ")
        println(message)
        // Need to take string to data to JSON
        var parseError: NSError?
        let data = message.dataUsingEncoding(NSUTF8StringEncoding);
        let parsedObject: NSDictionary = NSJSONSerialization.JSONObjectWithData(data!, options: NSJSONReadingOptions.AllowFragments, error: &parseError) as NSDictionary
        self.delegate?.process(parsedObject)
//        var getRequest: Dictionary<String, String> = [:];
//        getRequest["action"] = "GET"
//        getRequest["type"] = "kGetRouteByRouteId"
//        getRequest["route"] = "90"
//        
//        var parseError: NSError?
//        let data: NSData? = NSJSONSerialization.dataWithJSONObject(getRequest, options: NSJSONWritingOptions.PrettyPrinted, error:&parseError)
//        
//        let resstr = NSString(data: data!, encoding: NSUTF8StringEncoding)
//        self.sendData(resstr!)
    }

    // Optional
    func webSocketDidOpen(webSocket: SRWebSocket) {
        println("WebSocket did open")

    }

    func webSocket(webSocket: SRWebSocket!, didFailWithError error: NSError) {
        println("WebSocket error: ")
        println(error)
    }

    func webSocket(webSocket: SRWebSocket!, didCloseWithCode code: NSInteger!, reason: NSString!, wasClean: ObjCBool) {
        println("Reason, socket closed")
        println(reason)
    }
}