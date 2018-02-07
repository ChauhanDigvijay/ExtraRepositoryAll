//
//  WebViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/9/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit
import WebKit

class WebViewController: UIViewController, WKNavigationDelegate {

    @IBOutlet var progressIndicatorView: UIActivityIndicatorView!

    var urlString: String?
    private var webView: WKWebView!

    override func viewDidLoad() {
        super.viewDidLoad()

        webView = WKWebView()
        webView.navigationDelegate = self
        webView.allowsBackForwardNavigationGestures = false
        webView.allowsLinkPreview = false
        webView.navigationDelegate = self
        webView.frame = view.frame
        view.addSubview(webView)
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()

        if let url = NSURL(string: urlString ?? "") {
            let request = NSURLRequest(URL:url)
            webView.loadRequest(request)
            webView.addSubview(progressIndicatorView)
            progressIndicatorView.hidden = false
            progressIndicatorView.startAnimating()
        }
    }


    // MARK: - WKNavigationDelegate

    func webView(webView: WKWebView, decidePolicyForNavigationAction navigationAction: WKNavigationAction, decisionHandler: (WKNavigationActionPolicy) -> Void) {
        if navigationAction.navigationType == .LinkActivated {
            decisionHandler(.Cancel)
        } else {
            decisionHandler(.Allow)
        }
    }

    func webView(webView: WKWebView, didFinishNavigation navigation: WKNavigation!) {
        progressIndicatorView.hidden = true
    }

    func webView(webView: WKWebView, didFailNavigation navigation: WKNavigation!, withError error: NSError) {
        progressIndicatorView.hidden = true
        presentError(error)
    }

}
