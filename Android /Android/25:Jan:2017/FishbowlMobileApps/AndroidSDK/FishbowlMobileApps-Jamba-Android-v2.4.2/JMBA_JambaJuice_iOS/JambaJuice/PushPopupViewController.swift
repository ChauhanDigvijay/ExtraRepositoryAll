//
//  PushPopupViewController.swift
//  JambaJuice
//
//  Created by VT010 on 3/10/17.
//  Copyright © 2017 Jamba Juice. All rights reserved.
//

import UIKit
import SwiftyJSON

private typealias pushPopupViewAnimationCallback = () -> Void

class PushPopupViewController: UIViewController, PushPopupWebviewControllerDelegate {
    
    static let sharedInstance = PushPopupViewController.makePushPopupViewController()
    var pushNotificationResponse:PushNotificationResponse?
    var pushWebURL:URL?
    var generalPushMessage:Bool = false
    let linkBGColor = UIColor(red: 222.0/255.0, green: 122.0/255.0, blue: 16.0/255.0, alpha: 1.0);
    let iphoneRatio:CGFloat = 0.7
    let ipadHeight:CGFloat = 525
    
    @IBOutlet weak var offerDescription:UILabel!
    @IBOutlet weak var generalPushDescription:UILabel!
    @IBOutlet weak var viewOfferButton:UIButton!
    @IBOutlet weak var closeButton:UIButton!
    @IBOutlet weak var offerTitle:UILabel!
    @IBOutlet weak var offerPushView:UIView!
    @IBOutlet weak var generalPushView:UIView!
    @IBOutlet weak var offerMessageView:UIView!
    @IBOutlet weak var viewRewardsButtonBottomSpace:NSLayoutConstraint!
    @IBOutlet weak var offerMessageViewHeight:NSLayoutConstraint!
    
    class func makePushPopupViewController() -> PushPopupViewController {
        return UIViewController.instantiate("PushPopupViewController", storyboard: "PushPopupView") as! PushPopupViewController
    }
    
    /// Only called when app closes
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.pushOfferTitle()
        
        if self.generalPushMessage {
//            self.offerMessageView.frame.size.height = self.offerMessageView.bounds.height - 100
//            self.viewRewardsButtonBottomSpace.constant = 100
        }
        //    trackScreenView()
    }
    
    /// Attach basket flag to main application window
    func attachToMainWindow() {
        let window = UIApplication.shared.delegate!.window!!
       // window.backgroundColor = UIColor.init(white: 0.5, alpha: 1.0)
        window.addSubview(self.view)
        self.pushOfferTitle()
        self.view.alpha = 0.0
        UIView.animate(withDuration: 0.25, animations: {
            self.view.alpha = 1.0
        })
        
    }
    
    //MARK:- IBAction
    @IBAction func showWebViewTappingLabel() {
        if let url = self.pushWebURL {
            self.openWebview(url: url)
        }
    }
    
    @IBAction  func removeFromMainWindow(){
        self.animateOut {
            self.view.removeFromSuperview()
        }
    }
    
    @IBAction func viewOfferPage(){
        if pushNotificationResponse?.promoCode == nil || pushNotificationResponse?.promoCode == "" {
            if let desc = pushNotificationResponse?.aps?.alert?.body{
                //show web page
                if let url = verifyURL(urlString: desc) {
                    self.openWebview(url: url)
                //dismiss screen
                }else{
                    self.view.removeFromSuperview()
                }
            }else{
                self.view.removeFromSuperview()
            }
        }else{
            self.showOffersPage()
        }
    }
    
    func showOffersPage() {
        self.animateOut {
            self.view.removeFromSuperview()
            if self.pushNotificationResponse?.offerid != nil{
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.PushNotificationViewOffer.rawValue), object: nil, userInfo: nil)
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.OpenRewardsAndOfferDetail.rawValue), object: nil, userInfo: nil)
            }
        }
    }
    
    /// Slide the navigation controller view out of screen
    fileprivate func animateOut(_ callback: @escaping pushPopupViewAnimationCallback) {
        UIView.animate(withDuration: 0.25, animations: {
             self.view.alpha = 0.0
        }, completion: { (finished) in
            callback()
        }) 
    }
    
    func pushOfferTitle(){
        if pushNotificationResponse?.promoCode == nil || pushNotificationResponse?.promoCode == "" {
            self.showGeneralNotificationTitle()
            self.generalPushDescription.text = pushNotificationResponse?.aps?.alert?.body
            if let desc = pushNotificationResponse?.aps?.alert?.body{
                if let pushURL = verifyURL(urlString: desc){
                //if (verifyURL(urlString: desc) != nil){
                    self.viewOfferButton.setTitle("Take Me There", for: UIControlState())
                    self.pushWebURL = pushURL
                    
                    let urlInString = "\(pushURL)"
                    print("str \(urlInString) length \(urlInString.characters.count)")
                    
                    let description = pushNotificationResponse?.aps?.alert?.body ?? ""
                    if let range = description.range(of: urlInString) {
                        //start & ending position
                        let startPos = description.distance(from: description.startIndex, to: range.lowerBound)
                        
                        let descriptionLabelString:NSString = description as NSString;
                        var descriptionLabelMutableString = NSMutableAttributedString();
                        descriptionLabelMutableString  = NSMutableAttributedString(string: descriptionLabelString as String, attributes: [NSAttributedStringKey.font:UIFont.init(name: "Helvetica Neue", size: 17.0)!])
                        
                        let textRange = NSMakeRange(startPos, (urlInString.characters.count))
                        descriptionLabelMutableString.addAttribute(NSAttributedStringKey.foregroundColor, value: linkBGColor, range: textRange)
                        
                        descriptionLabelMutableString.addAttribute(NSAttributedStringKey.underlineStyle , value: NSUnderlineStyle.styleSingle.rawValue, range: textRange)
                        
                        descriptionLabelMutableString.addAttribute(NSAttributedStringKey.font, value: UIFont.italicSystemFont(ofSize: 17), range: textRange)
                        
                        self.generalPushDescription?.attributedText=descriptionLabelMutableString;

                    }
                    
                }
            }
        }else{
            self.viewOfferButton.setTitle("View Offer", for: UIControlState())
            self.offerTitle.text = "Squeeze The Day!"
            self.offerPushView.isHidden = false
            self.generalPushView.isHidden = true
            self.viewRewardsButtonBottomSpace.constant = 0
            self.generalPushMessage = false
        }
        self.offerDescription.text = pushNotificationResponse?.aps?.alert?.body
        
        var height:CGFloat = 0
        if UIDevice.current.userInterfaceIdiom == .pad {
            height = ipadHeight
        } else if UIDevice.current.userInterfaceIdiom == .phone {
            height = UIScreen.main.bounds.height * iphoneRatio
        }
        
        if self.generalPushMessage {
            height = height - 100
        }
        
        self.offerMessageViewHeight.constant = height
    }
    
    func showGeneralNotificationTitle(){
//        self.offerMessageView.frame.size.height = self.offerMessageView.bounds.height - 100
//        self.viewRewardsButtonBottomSpace.constant = 100
        self.generalPushMessage = true
        if pushWebURL != nil {
            self.viewOfferButton.setTitle("Take Me There", for: UIControlState())
        } else {
            self.viewOfferButton.setTitle("OK", for: UIControlState())
        }
        self.offerTitle.text = "Sip Sip Hooray!"
        self.offerPushView.isHidden = true
        self.generalPushView.isHidden = false
    }
    
    // Process push notification
    func processPushNotification(_ userInfo:[AnyHashable: Any]){
        NSLog("push response: %@", userInfo)
        let pushResponse = PushNotificationResponse.init(json: JSON(userInfo))
        if pushResponse.custid == nil{
            return
        }
        UIApplication.afterDelay(3) {
            self.validateUser(pushResponse.custid!, callback: { (status) in
                if status{
                    if self.pushNotificationResponse != nil{
                        let newPush = PushNotificationResponse.init(json: JSON(userInfo))
                        self.dismissWhenNewPushNotificationArrived(newPush)
                    }else{
                        self.pushNotificationResponse = PushNotificationResponse.init(json: JSON(userInfo))
                        self.attachToMainWindow()
                    }
                }else{
                    return
                }
            })
        }
    }
    
    func dismissWhenNewPushNotificationArrived(_ newPush:PushNotificationResponse){
        self.animateOut { 
            self.view.removeFromSuperview()
            self.pushNotificationResponse = newPush
            self.attachToMainWindow()
        }
    }
    
    func validateUser(_ pushUserId:String, callback:FishbowlCredentialValidationCallback){
        FishbowlApiClassService.sharedInstance.validateFishbowlCredentials { (status) in
            var flag:Bool = false
            if status &&  FishbowlApiClassService.sharedInstance.validatePushCustomerId(pushUserId) == true{
                flag = true
            }else{
                 flag = false
            }
            return callback(flag)
        }
    }
    
    // MARK: - Verify URL
//    func verifyURL(urlString:String?) -> Bool{
//        if let urlString = urlString{
//            if let url = NSURL(string: urlString){
//                return UIApplication.shared.canOpenURL(url as URL)
//            }
//        }
//        return false
//    }
    
    func verifyURL(urlString:String) -> URL?{

        let types: NSTextCheckingResult.CheckingType = .link
        var URLStrings = [NSURL]()
        let detector = try? NSDataDetector(types: types.rawValue)
        detector?.enumerateMatches(in: urlString as String, options: [], range: NSMakeRange(0, (urlString as NSString).length)) { (result, flags, _) in
            print(result!.url!)
            URLStrings.append(result!.url! as NSURL)
        }
        if URLStrings.count > 0{
            return URLStrings.first as URL?
        }else{
            return nil
        }
    }
    
    func openWebview(url:URL!){
        performSegue(withIdentifier: "PushWebView", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "PushWebView"{
            let vc = segue.destination as! PushPopupWebviewController
            vc.linkToLoad = pushWebURL?.absoluteString
            vc.delegate = self
        }else{
            return
        }
    }
    
    //MARK:- PushPopupWebviewControllerDelegate
    func closeParentScreen() {
        self.view.removeFromSuperview()
    }
    

    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}
