//
//  ProgramDetailViewController.swift
//

//  LoyaltyIpadApp
//
//  Created by surendra pathak on 08/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class ProgramDetailViewController: UIViewController
{
    // MARK: - ------------ IBOutlets ----------

    @IBOutlet weak var webViewFAQ: UIWebView!
    @IBOutlet weak var viewOnTop: UIView!
    @IBOutlet weak var imgBackG: UIImageView!
    @IBOutlet weak var btnLogOutOutlet: UIButton!
    @IBOutlet weak var imgTopHeaderRewardOffer: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgLineOnTopToSpaceBetweenNmae: UIImageView!
    @IBOutlet weak var imgrewardCompnyLogo: UIImageView!
    // MARK: - ------------ ViewMethods ----------
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setScreenValue()
        viewOnTop.layer.shadowColor = UIColor.blackColor().CGColor
        viewOnTop.layer.shadowOffset = CGSizeMake(2, 2);
        viewOnTop.layer.shadowOpacity = 0.5;
        self.navigationController?.navigationBarHidden=true
        self.navigationItem.title="Frequently asked questions"
        
        if (NSUserDefaults.standardUserDefaults().objectForKey("checkInButtonColor") != nil)
        {
            colorHex = NSUserDefaults.standardUserDefaults().stringForKey("checkInButtonColor")!
            let color1 = modelSharedInstance.hexStringToUIColor(colorHex)
            //nav backbutton Color
            navigationController?.navigationBar.tintColor = color1
            //nav background color
            //  navigationController?.navigationBar.barTintColor = UIColor.greenColor()
            //nav title color
            navigationController!.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: color1]
        }
        
        
        let data = NSUserDefaults.standardUserDefaults().objectForKey("loyaltySetting") as? NSData
        let mydata = NSKeyedUnarchiver.unarchiveObjectWithData(data!)!
        
        
        
        let strMydata:String = mydata["programDetail"] as! String
        let str = "https://"
        
        let termsAndConditions:String = str + strMydata
        let url = NSURL (string: termsAndConditions)
        let requestObj = NSURLRequest(URL: url!)
        self.webViewFAQ.loadRequest(requestObj)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillDisappear(animated: Bool) {
        super.viewWillDisappear(true)
        self.navigationController?.navigationBarHidden=true
    }

    // MARK: - ------------ Actions ----------
    //set screen color and images
    @IBAction func tapMenu(sender: UIButton)
    {
        let obj = self.storyboard!.instantiateViewControllerWithIdentifier("MenuViewController")
        self.navigationController!.pushViewController(obj, animated: true)
    }

    @IBAction func tapBackMenu(sender: UIButton)
    {
        self.navigationController!.popViewControllerAnimated(true)
    }
    @IBAction func tapProfile(sender: UIButton)
    {
        
        self.navigationController!.popViewControllerAnimated(true)
    }
    
    @IBAction func tapToLoyalty(sender: UIButton)
    {
        let switchViewController = self.navigationController?.viewControllers[1] as! RewardsOffersViewController
        
        self.navigationController?.popToViewController(switchViewController, animated: true)
    }
    
    @IBAction func tapLogout(sender: UIButton)
    {
        let alert=UIAlertController(title: "", message: "Do you want to logout", preferredStyle: UIAlertControllerStyle.Alert);
        let NOAction = UIAlertAction(title: "NO", style: .Default)
        { (action:UIAlertAction!) in
            
            // self.btnLogOutOutlet.userInteractionEnabled=true
        }
        let YESAction = UIAlertAction(title: "YES", style: .Default)
        { (action:UIAlertAction!) in
            
            dispatch_async(dispatch_get_main_queue(),
                           {
                            self.navigationController!.popToRootViewControllerAnimated(true)
                            
            })
        }
        alert.addAction(NOAction)
        alert.addAction(YESAction)
        self.presentViewController(alert, animated: true, completion:nil)
    }
    // MARK: - ------------ Private Methods ----------
    func setScreenValue()
    {
      
        dispatch_async(dispatch_get_main_queue(),
                       {
                        self.lblName.text = NSUserDefaults.standardUserDefaults().stringForKey("userName");
                        })
        let backImageUrl: String = NSUserDefaults.standardUserDefaults().stringForKey("companyLogoImageUrl")!
        let backTopHeaderImageUrl: String = NSUserDefaults.standardUserDefaults().stringForKey("loginHeaderImageUrl")!
        if (NSUserDefaults.standardUserDefaults().objectForKey("signUpBackgroundImageUrl") != nil)
        {
            let backGroundImageUrl = NSUserDefaults.standardUserDefaults().stringForKey("signUpBackgroundImageUrl")!
            self.callImage(backGroundImageUrl, setIndex: 2)
        }
        self.callImage(backImageUrl, setIndex: 0)
        self.callImage(backTopHeaderImageUrl, setIndex: 1)
        
    }
    func callImage(strImagecompanyLogoImageUrl: String, setIndex i: Int)
    {
        if strImagecompanyLogoImageUrl.hasSuffix(".html") {
            print(".html")
        }
        else
        {
            let strurlLogo: String = "http://\(strImagecompanyLogoImageUrl)"
            let companyLogoImageUrl: NSURL = NSURL(string: strurlLogo)!
            print(companyLogoImageUrl)
            
            var request: NSURLRequest = NSURLRequest(URL: companyLogoImageUrl)
            let session = NSURLSession.sharedSession()
            let task = session.dataTaskWithRequest(request){
                (data, response, error) -> Void in
                if (error == nil && data != nil)
                {
                    func display_image()
                    {
                        switch i
                        {
                        case 0:
                            self.imgrewardCompnyLogo.image = self.resizeImage(UIImage(data: data!)!, newWidth: self.imgrewardCompnyLogo.frame.size.width)
                            break
                            
                        case 1:
                            //self.imgTopHeaderRewardOffer.image = UIImage(data: data!)!
                            break
                        case 2: break
                            //                            let blurEffect = UIBlurEffect(style: UIBlurEffectStyle.Dark)
                            //                            let blurEffectView = UIVisualEffectView(effect: blurEffect)
                            //                            blurEffectView.frame = self.imgBackG.bounds
                            //                            blurEffectView.autoresizingMask = [.FlexibleWidth, .FlexibleHeight] // for supporting device rotation
                            //                            blurEffectView.alpha = 0.8
                            //                            self.imgBackG.addSubview(blurEffectView)
                            //                            let tempImgView = UIImageView(frame: CGRect(x: 0, y: 0, width: 1024.0, height: 698.0))
                            //                            tempImgView.image=UIImage(data: data!)
                            //                            tempImgView.center = self.view!.center
                            //                            self.imgBackG.image = tempImgView.image
                            
                        default: break
                        }
                        
                        //img = UIImage(data: data!)!
                    }
                    
                    dispatch_async(dispatch_get_main_queue(), display_image)
                }
            }
            task.resume()
        }
    }
    func resizeImage(image: UIImage, newWidth: CGFloat) -> UIImage {
        
        let scale = newWidth / image.size.width
        let newHeight = image.size.height * scale
        UIGraphicsBeginImageContext(CGSizeMake(newWidth, newHeight))
        image.drawInRect(CGRectMake(0, 0, newWidth, newHeight))
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage
    }
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}

