//
//  GiftCardWebview.swift
//  JambaJuice
//
//  Created by VT007 on 19/09/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

protocol GiftCardWebviewDelegate {
    func webviewURLPath(webview: GiftCardWebview) -> String
    func webviewHeaderText(webview: GiftCardWebview) -> String
}

class GiftCardWebview: UIViewController, UIWebViewDelegate {
    @IBOutlet weak var webview      : UIWebView!
    @IBOutlet weak var headerLabel  : UILabel!
    var delegate                    : GiftCardWebviewDelegate!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        headerLabel.text = delegate.webviewHeaderText(self)
        self.performSelector(#selector(GiftCardWebview.refreshWebview), withObject: nil, afterDelay: 0.35)
        webview.delegate = self
    }
    
    
    func refreshWebview() {
        if let url = NSURL.init(string: delegate.webviewURLPath(self)) {
            SVProgressHUD.showWithStatus("Loading...", maskType: .None)
            webview.loadRequest(NSURLRequest.init(URL: url, cachePolicy: .UseProtocolCachePolicy, timeoutInterval: 30))
        }
    }
    
    //MARK: - IBAction method
    @IBAction func closeScreen(sender: UIButton) {
        SVProgressHUD.dismiss()
        popViewController()
    }
    
    // MARK: - Webview delegate
    func webViewDidFinishLoad(webView: UIWebView) {
        SVProgressHUD.dismiss()
    }
    
    func webView(webView: UIWebView, didFailLoadWithError error: NSError) {
        SVProgressHUD.dismiss()
        self.presentConfirmation("Error", message: GiftCardAppConstants.generalError.localizedDescription, buttonTitle: "Retry") { (confirmed) in
            if confirmed {
                self.refreshWebview()
            }
        }
    }
}
