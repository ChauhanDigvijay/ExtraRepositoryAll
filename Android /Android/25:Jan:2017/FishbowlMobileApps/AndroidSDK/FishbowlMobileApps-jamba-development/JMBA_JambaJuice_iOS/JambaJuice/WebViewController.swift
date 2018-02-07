//
//  WebViewViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 22/05/2015.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import WebKit

class WebViewController: UIViewController, UIWebViewDelegate, WKNavigationDelegate {
    
    var linkToLoad: String!
    var headerTitle: String = ""

    
    var isAdPresented:Bool = false

    private var webView: WKWebView?
    
    override func loadView() {
        super.loadView()
        webView = WKWebView()
        webView?.allowsBackForwardNavigationGestures = false
        webView?.navigationDelegate = self
        view = webView
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if isAdPresented{
            self.navigationItem.leftBarButtonItem?.image = UIImage.init(named: "close-button")
        }else{
             self.navigationItem.leftBarButtonItem?.image = UIImage.init(named: "back-button")
        }
        
        configureNavigationBar(.LightBlue)
        
        if let url = NSURL(string: linkToLoad) {
            let request = NSURLRequest(URL: url)
            webView?.loadRequest(request)
        }
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    func webView(webView: WKWebView, decidePolicyForNavigationAction navigationAction: WKNavigationAction, decisionHandler: (WKNavigationActionPolicy) -> Void) {
        if navigationAction.navigationType == .LinkActivated {
            decisionHandler(.Cancel)
        } else {
            decisionHandler(.Allow)
        }
    }
    
    @IBAction func close() {
        if isAdPresented {
            self.dismissViewControllerAnimated(true, completion: nil)
        } else {
            popViewController()
        }
    }
    
}
