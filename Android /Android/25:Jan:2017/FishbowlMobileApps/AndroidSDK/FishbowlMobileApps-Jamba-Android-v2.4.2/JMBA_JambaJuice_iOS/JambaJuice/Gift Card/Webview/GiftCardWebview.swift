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
    func webviewURLPath(_ webview: GiftCardWebview) -> String
    func webviewHeaderText(_ webview: GiftCardWebview) -> String
}

class GiftCardWebview: UIViewController, UIWebViewDelegate {
    @IBOutlet weak var webview      : UIWebView!
    @IBOutlet weak var headerLabel  : UILabel!
    var delegate                    : GiftCardWebviewDelegate!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        headerLabel.text = delegate.webviewHeaderText(self)
        self.perform(#selector(GiftCardWebview.refreshWebview), with: nil, afterDelay: 0.35)
        webview.delegate = self
    }
    
    
    @objc func refreshWebview() {
        if let url = URL.init(string: delegate.webviewURLPath(self)) {
            SVProgressHUD.show(withStatus: "Loading...")
            SVProgressHUD.setDefaultMaskType(.clear)
            webview.loadRequest(URLRequest.init(url: url, cachePolicy: .useProtocolCachePolicy, timeoutInterval: 30))
        }
    }
    
    //MARK: - IBAction method
    @IBAction func closeScreen(_ sender: UIButton) {
        SVProgressHUD.dismiss()
        popViewController()
    }
    
    // MARK: - Webview delegate
    func webViewDidFinishLoad(_ webView: UIWebView) {
        SVProgressHUD.dismiss()
    }
    
    func webView(_ webView: UIWebView, didFailLoadWithError error: Error) {
        SVProgressHUD.dismiss()
        self.presentConfirmation("Error", message: GiftCardAppConstants.generalError.localizedDescription, buttonTitle: "Retry") { (confirmed) in
            if confirmed {
                self.refreshWebview()
            }
        }
    }
}
