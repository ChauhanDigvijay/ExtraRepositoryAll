//
//  PushPopupWebviewController.swift
//  JambaJuice
//
//  Created by VT010 on 6/12/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import Foundation
import UIKit
import WebKit

protocol PushPopupWebviewControllerDelegate: class {
    func closeParentScreen()
}

class PushPopupWebviewController: UIViewController, UIWebViewDelegate {
    
    var linkToLoad: String!
    
    weak var delegate:PushPopupWebviewControllerDelegate?
    
    var isAdPresented:Bool = false
    @IBOutlet weak var webUIView:UIWebView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        configureNavigationBar(.lightBlue)
        
        if let url = URL(string: linkToLoad) {
            let request = URLRequest(url: url)
            webUIView.loadRequest(request)
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        if navigationAction.navigationType == .linkActivated {
            if let url = navigationAction.request.url {
                UIApplication.shared.openURL(url)
            }
            decisionHandler(.cancel)
        } else {
            decisionHandler(.allow)
        }
    }
    
    //MARK:- IBAction
    @IBAction func close() {
        self.dismiss(animated: true) { 
            self.delegate?.closeParentScreen()
        }
    }
    
    @IBAction func showWebPageinAppBrowser() {
        UIApplication.shared.openURL(URL(string: linkToLoad)!)
    }
    
}
