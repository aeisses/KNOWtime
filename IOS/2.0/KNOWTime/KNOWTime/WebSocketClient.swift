//
//  WebSocketClient.swift
//  KNOWTime
//
//  Created by Aaron Eisses on 12/31/14.
//  Copyright (c) 2014 KNOWTime. All rights reserved.
//

import Foundation

class WebSocketClient: NSObject, SRWebSocketDelegate {
  
  var url: NSURL
  let srWebSocket:SRWebSocket
  
  init(url: NSURL) {
    self.url = url
    self.srWebSocket = SRWebSocket(URL: url)
  }
  
  func openWebSocketClient() {
    srWebSocket.open()
  }
  
  func closeWebSocketClient() {
    srWebSocket.close()
  }
  
  func sendData(data: NSData) {
    srWebSocket.send(data)
    
  }

  // SRWebSocket Delegate method functions
  // Required
  func webSocket(webSocket: SRWebSocket!, didReceiveMessage message: AnyObject) {
    
  }

  // Optional
  func webSocketDidOpen(webSocket: SRWebSocket) {
    
  }

  func webSocket(webSocket: SRWebSocket!, didFailWithError error: NSError) {
    println(error)
  }

  func webSocket(webSocket: SRWebSocket!, didCloseWithCode code: NSInteger!, reason: NSString!, wasClean: ObjCBool) {
    
  }
}