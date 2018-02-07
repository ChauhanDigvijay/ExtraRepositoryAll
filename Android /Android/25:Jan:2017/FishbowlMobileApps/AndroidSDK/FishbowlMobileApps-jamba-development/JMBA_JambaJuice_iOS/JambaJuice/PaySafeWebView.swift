//
//  PaySafeWebView.swift
//  JambaJuice
//
//  Created by VT010 on 10/5/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import SVProgressHUD

protocol PaySafeWebViewDelegate{
    func getDetails(webview: PaySafeWebView) -> (orgId: String, sessionId: String)
}

class PaySafeWebView: UIView, UIWebViewDelegate {
    var webview           : UIWebView!
    var delegate          : PaySafeWebViewDelegate!
    var status            : Bool = false
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        webview = UIWebView.init(frame: frame)
        guard webview != nil else {
            return
        }
        self.addSubview(webview)
        webview.delegate = self
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func start() {
        dispatch_async(dispatch_get_main_queue()) {
            self.refreshWebview()
        }
    }
    
    func refreshWebview() {
        let info = delegate.getDetails(self)
        var htmlContent = "<html><head></head><body><p style=\'background:url(https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/clear.png?org_id=__VestaOrgId__&session_id=__VestaSessionId__&m=1)\'></p><img src=\'https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/clear.png?org_id=__VestaOrgId__&session_id=__VestaSessionId__&m=2\' alt=\'\' /><script src=\'https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/check.js?org_id=__VestaOrgId__&session_id=__VestaSessionId__\' type=\'text/javascript\'></script><object data=\'https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/fp.swf?org_id=__VestaOrgId__&session_id=__VestaSessionId__\' type=\'application/x-shockwave-flash\' width=\'1\' height=\'1\' id=\'obj_id\'><param value=\'https://paysafe.ecustomerpayments.com/PaySafeUIRedirector/fp/fp.swf?org_id=__VestaOrgId__&session_id=__VestaSessionId__\' name=\'movie\'/></object></body></html>"
        htmlContent = htmlContent.stringByReplacingOccurrencesOfString("__VestaSessionId__", withString: info.1)
        htmlContent = htmlContent.stringByReplacingOccurrencesOfString("__VestaOrgId__", withString: info.0)
        webview.loadHTMLString(htmlContent, baseURL: nil)
    }
    
    // MARK: - Webview delegate
    func webViewDidFinishLoad(webView: UIWebView) {
        status = true
    }
    
    func webView(webView: UIWebView, didFailLoadWithError error: NSError) {
        status = false
    }
    
}

