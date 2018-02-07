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

    fileprivate var webView: WKWebView?
    
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
        
        configureNavigationBar(.lightBlue)
        
        if let url = URL(string: linkToLoad) {
            let request = URLRequest(url: url)
            _ = webView?.load(request)
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
    
    @IBAction func close() {
        if isAdPresented {
            self.dismiss(animated: true, completion: nil)
        } else {
            popViewController()
        }
    }
    
}
