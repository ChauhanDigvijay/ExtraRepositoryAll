//
//  MenuViewController.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 08/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class MenuViewController: UIViewController
{
    // MARK: - ------------ IBOutlets ----------
    @IBOutlet weak var viewOnTop: UIView!
    @IBOutlet weak var viewLoyaltyBtn: UIView!
    @IBOutlet weak var viewMyProfileBtn: UIView!
    @IBOutlet weak var viewActivityBtn: UIView!
    @IBOutlet weak var btnMenuOutlet: UIButton!
    @IBOutlet weak var imgBackG: UIImageView!
    @IBOutlet weak var viewMessageBtn: UIView!
    @IBOutlet weak var btnLogOutOutlet: UIButton!
    @IBOutlet weak var imgTopHeaderRewardOffer: UIImageView!
    @IBOutlet weak var viewFAQBtn: UIView!
    @IBOutlet weak var viewLContactUsBtn: UIView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgLineOnTopToSpaceBetweenNmae: UIImageView!
    @IBOutlet weak var imgrewardCompnyLogo: UIImageView!
    @IBOutlet weak var btnContactUsOultlet: UIButton!
    @IBOutlet weak var btnMyProfileOultlet: UIButton!
    @IBOutlet weak var btnFAQOultlet: UIButton!
    @IBOutlet weak var btnActivityOultlet: UIButton!
    @IBOutlet weak var btnMessageOultlet: UIButton!
    @IBOutlet weak var btnHomeOultlet: UIButton!

    // MARK: - ------------ ViewMethods ----------
    override func viewDidLoad() {
        super.viewDidLoad()
        viewOnTop.layer.shadowColor = UIColor.blackColor().CGColor
        viewOnTop.layer.shadowOffset = CGSizeMake(2, 2);
        viewOnTop.layer.shadowOpacity = 0.5;
        self.setScreenValue()
        let color2:UIColor = modelSharedInstance.hexStringToUIColor("EFEFEF")
        btnMenuOutlet.backgroundColor=color2
        self.navigationController?.navigationBarHidden=true
        let  objAPI = ApiClass()
        objAPI.getStoreCityStateAPI("/states", withTarget: self, withSelector: #selector(MenuViewController.StateResponseApi(_:)))
      
        
        // Do any additional setup after loading the view.
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    // MARK: - ------------ Private Methods ----------
    //set screen color and images
    func callAlert(message: String)
    {
        dispatch_async(dispatch_get_main_queue(), {
            
            let alertController = UIAlertController(title: nil, message:
                message, preferredStyle: UIAlertControllerStyle.Alert)
            alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.Default,handler: nil))
            MBProgressHUD.hideHUDForView(self.view, animated: true)
            self.presentViewController(alertController, animated: true, completion: nil)
        })
        
    }
    func setScreenValue()
    {
        dispatch_async(dispatch_get_main_queue(),
                       {
                        self.lblName.text = NSUserDefaults.standardUserDefaults().stringForKey("userName");
                        //            self.btnContactUsOultlet.backgroundColor=color1
                        //            self.btnMyProfileOultlet.backgroundColor=color1
                        //            self.btnFAQOultlet.backgroundColor=color1
                        //           self.btnActivityOultlet.backgroundColor=color1
                        //           self.btnMessageOultlet.backgroundColor=color1
                        //            self.btnHomeOultlet.backgroundColor=color1
                           self.viewLoyaltyBtn.layer.cornerRadius = 10
                        self.viewLoyaltyBtn.clipsToBounds = true
                        self.viewLoyaltyBtn.layer.borderWidth=2.0
                        self.viewLoyaltyBtn.layer.borderColor=UIColor.clearColor().CGColor
                        
                        self.viewMyProfileBtn.layer.cornerRadius = 10
                        self.viewMyProfileBtn.clipsToBounds = true
                        self.viewMyProfileBtn.layer.borderWidth=2.0
                        self.viewMyProfileBtn.layer.borderColor=UIColor.clearColor().CGColor
                        
                        self.viewActivityBtn.layer.cornerRadius = 10
                        self.viewActivityBtn.clipsToBounds = true
                        self.viewActivityBtn.layer.borderWidth=2.0
                        self.viewActivityBtn.layer.borderColor=UIColor.clearColor().CGColor
                        
                      
                        self.viewLContactUsBtn.layer.cornerRadius = 10
                        self.viewLContactUsBtn.clipsToBounds = true
                        self.viewLContactUsBtn.layer.borderWidth=2.0
                        self.viewLContactUsBtn.layer.borderColor=UIColor.clearColor().CGColor
                        
                        self.viewFAQBtn.layer.cornerRadius = 10
                        self.viewFAQBtn.clipsToBounds = true
                        self.viewFAQBtn.layer.borderWidth=2.0
                        self.viewFAQBtn.layer.borderColor=UIColor.clearColor().CGColor
                        
                        
                        
                        
                        self.viewMessageBtn.layer.cornerRadius = 10
                        self.viewMessageBtn.clipsToBounds = true
                        self.viewMessageBtn.layer.borderWidth=2.0
                        self.viewMessageBtn.layer.borderColor=UIColor.clearColor().CGColor
                        
                 
                        
               
                        
                        
//                        self.viewMessageBtn.layer.masksToBounds = false;
//                        self.viewMessageBtn.layer.shadowOffset = CGSizeMake(0, 0);
//                        self.viewMessageBtn.layer.shadowRadius = 5;
//                        self.viewMessageBtn.layer.shadowOpacity = 0.1;
//                        self.viewLoyaltyBtn.layer.masksToBounds = false;
//                        self.viewLoyaltyBtn.layer.shadowOffset = CGSizeMake(0, 0);
//                        self.viewLoyaltyBtn.layer.shadowRadius = 5;
//                        self.viewLoyaltyBtn.layer.shadowOpacity = 0.1;
                        
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
                        case 2:
                            let blurEffect = UIBlurEffect(style: UIBlurEffectStyle.Dark)
                            let blurEffectView = UIVisualEffectView(effect: blurEffect)
                            blurEffectView.frame = self.imgBackG.bounds
                            blurEffectView.autoresizingMask = [.FlexibleWidth, .FlexibleHeight] // for supporting device rotation
                            blurEffectView.alpha = 0.8
                            self.imgBackG.addSubview(blurEffectView)
                            let tempImgView = UIImageView(frame: CGRect(x: 0, y: 0, width: 1024.0, height: 698.0))
                            tempImgView.image=UIImage(data: data!)
                            tempImgView.center = self.view!.center
                            self.imgBackG.image = tempImgView.image
                            
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
    // MARK: - ------------ Actions ----------
    @IBAction func tapBackMenu(sender: UIButton)
    {
        self.navigationController!.popViewControllerAnimated(true)
    }
    @IBAction func tapHome(sender: AnyObject)
    {
        self.navigationController?.popViewControllerAnimated(true)
    }
    
    @IBAction func tapFAQ(sender: AnyObject)
    {
        let obj = self.storyboard!.instantiateViewControllerWithIdentifier("FAQViewController")
        self.navigationController!.pushViewController(obj, animated: true)
    }
    @IBAction func tapContactUs(sender: AnyObject)
    {
        let obj = self.storyboard!.instantiateViewControllerWithIdentifier("ContactUsViewController")
        self.navigationController!.pushViewController(obj, animated: true)
    }
    @IBAction func tapMessage(sender: AnyObject)
    {
        let obj = self.storyboard!.instantiateViewControllerWithIdentifier("MessageViewController")
        self.navigationController!.pushViewController(obj, animated: true)
    }
    @IBAction func tapMyProfile(sender: AnyObject)
    {
        let obj = self.storyboard!.instantiateViewControllerWithIdentifier("UpdateProfileViewController")
        self.navigationController!.pushViewController(obj, animated: true)
    }
    @IBAction func tapActivity(sender: AnyObject)
    {
        let obj = self.storyboard!.instantiateViewControllerWithIdentifier("ActivityViewController")
        self.navigationController!.pushViewController(obj, animated: true)
    }
    @IBAction func tapProfile(sender: UIButton)
    {
        
        self.navigationController!.popViewControllerAnimated(true)
    }
    @IBAction func tapLogout(sender: UIButton)
    {
        let alert=UIAlertController(title: "", message: "Do you want to logout", preferredStyle: UIAlertControllerStyle.Alert);
        let NOAction = UIAlertAction(title: "NO", style: .Default)
        { (action:UIAlertAction!) in
            
//            self.btnLogOutOutlet.userInteractionEnabled=true
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
    // MARK: - ------------ Api responses ----------
    func StateResponseApi(responseDict: NSDictionary)
    {
        let  objAPI = ApiClass()
        objAPI.getStoreCityStateAPI("/states/getAllCountry", withTarget: self, withSelector: #selector(MenuViewController.CountryResponseApi(_:)))
        var successFlag: Bool
        successFlag = responseDict["successFlag"] as! Bool!
        let message: String = responseDict["message"]! as! String
        if successFlag {
            
            let data = NSKeyedArchiver.archivedDataWithRootObject((responseDict["stateList"] as? [AnyObject])!)
            NSUserDefaults.standardUserDefaults().setObject(data, forKey: "StateData")
        }
        else
        {
            callAlert(message)
            
        }
        
    }
    func CountryResponseApi(responseDict: NSDictionary)
    {
        
        var successFlag: Bool
        successFlag = responseDict["successFlag"] as! Bool!
        let message: String = responseDict["message"]! as! String
        if successFlag {
            
            let data = NSKeyedArchiver.archivedDataWithRootObject((responseDict["countryList"] as? [AnyObject])!)
            NSUserDefaults.standardUserDefaults().setObject(data, forKey: "CountryData")
        }
        else
        {
            callAlert(message)
            
        }
        
    }

}
