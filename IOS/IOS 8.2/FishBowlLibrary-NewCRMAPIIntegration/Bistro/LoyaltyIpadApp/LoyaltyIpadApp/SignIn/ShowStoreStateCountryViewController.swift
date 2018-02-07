//
//  ShowStoreStateCountryViewController.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 25/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit
var isStringStoreCity:String!
var indexSelectData:Int!
var countryCode:String?
var strStoreCode:NSNumber?
var stateCode:String?
var isRewardRule:Bool!
var strRuleID:String!

class ShowStoreStateCountryViewController: UIViewController {
      // MARK: - ------------ IBOutlets ----------
    @IBOutlet weak var tblView1: UITableView!
    @IBOutlet weak var imgBackG: UIImageView!
    @IBOutlet weak var lblTopSignUp: UILabel!
    // MARK: - ------------ Private variables ----------
    var mydata:AnyObject!
    var storeCityCountryArray:NSMutableArray=NSMutableArray()
    // MARK: - ------------ ViewMethods ----------
    override func viewDidLoad()
    {
        super.viewDidLoad()
        self.tblView1.tableFooterView = UIView.init()
        tblView1.registerNib(UINib.init(nibName: "BonusTableViewCell", bundle: nil), forCellReuseIdentifier: "BonusTableViewCell")
        let  objAPI = ApiClass()
        if isStringStoreCity=="state"
        {
            lblTopSignUp.text="Select state"
            if NSUserDefaults.standardUserDefaults().objectForKey("StateData") != nil
            {
                let data = NSUserDefaults.standardUserDefaults().objectForKey("StateData") as? NSData
                mydata = NSKeyedUnarchiver.unarchiveObjectWithData(data!)!
                for i in 0 ..< Int(mydata.count)
                {
                    storeCityCountryArray.addObject(mydata[i])
                }
                dispatch_async(dispatch_get_main_queue(), {
                    MBProgressHUD.hideHUDForView(self.view, animated: true)
                    self.tblView1.reloadData()
                })
            }
            else
            {
                let  objAPI = ApiClass()
               objAPI.getStoreCityStateAPI("/states", withTarget: self, withSelector: #selector(ShowStoreStateCountryViewController.StoreCityStateResponseApi(_:)))
            }
        }
        if isStringStoreCity=="FavoriteStore"
        {
            lblTopSignUp.text="Select Store"
//            let  objAPI = ApiClass()
//            objAPI.getStoreCityStateAPI("/mobile/stores/getstores", withTarget: self, withSelector: #selector(NonSignedInViewController.StoreCityStateResponseApi(_:)))
            if NSUserDefaults.standardUserDefaults().objectForKey("FavoriteStoreData") != nil
            {
                let data = NSUserDefaults.standardUserDefaults().objectForKey("FavoriteStoreData") as? NSData
                mydata = NSKeyedUnarchiver.unarchiveObjectWithData(data!)!
                for i in 0 ..< Int(mydata.count)
                {
                    storeCityCountryArray.addObject(mydata[i])
                }
                dispatch_async(dispatch_get_main_queue(), {
                    MBProgressHUD.hideHUDForView(self.view, animated: true)
                    self.tblView1.reloadData()
                })
            }
            else
            {
                let  objAPI = ApiClass()
            objAPI.getStoreCityStateAPI("/mobile/stores/getstores", withTarget: self, withSelector: #selector(ShowStoreStateCountryViewController.StoreCityStateResponseApi(_:)))
            }
        }
        if isStringStoreCity=="country"
        {
            lblTopSignUp.text="Select country"
            if NSUserDefaults.standardUserDefaults().objectForKey("CountryData") != nil
            {
                let data = NSUserDefaults.standardUserDefaults().objectForKey("CountryData") as? NSData
                mydata = NSKeyedUnarchiver.unarchiveObjectWithData(data!)!
                for i in 0 ..< Int(mydata.count)
                {
                    storeCityCountryArray.addObject(mydata[i])
                }
                dispatch_async(dispatch_get_main_queue(), {
                    MBProgressHUD.hideHUDForView(self.view, animated: true)
                    self.tblView1.reloadData()
                })
            }
            else
            {
                let  objAPI = ApiClass()
                     objAPI.getStoreCityStateAPI("/states/getAllCountry", withTarget: self, withSelector: #selector(ShowStoreStateCountryViewController.StoreCityStateResponseApi(_:)))
            }
        }
        if isStringStoreCity=="Bonus"
        {
            objAPI.getSignupRuleListAPI("/loyalty/signupRuleList", withTarget: self, withSelector: #selector(ShowStoreStateCountryViewController.SignUpRuleBonusResponseApi(_:)))
        }
        MBProgressHUD.showHUDAddedTo(self.view, animated: true)
        //        let data = NSUserDefaults.standardUserDefaults().objectForKey("loyaltySetting") as? NSData
        //        let mydata1 = NSKeyedUnarchiver.unarchiveObjectWithData(data!)!
        
        self.tblView1.tableFooterView = UIView.init()
        tblView1.registerClass(UITableViewCell.self, forCellReuseIdentifier: "cell")
        self.navigationController?.navigationBarHidden=true
        self.setScreenValue()
        // Do any additional setup after loading the view.
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    // MARK: - ------------ Private methods ----------
    func setScreenValue()
    {
        if (NSUserDefaults.standardUserDefaults().objectForKey("checkInButtonColor") != nil)
        {
            colorHex = NSUserDefaults.standardUserDefaults().stringForKey("checkInButtonColor")!
            let color1 = modelSharedInstance.hexStringToUIColor(colorHex)
            dispatch_async(dispatch_get_main_queue(), {
                
                //  self.lblTopSignUp.textColor =  color1
                // self.btnGetStartedOutlet.layer.cornerRadius = 6
            })
        }
        // var colorHex: String =
        var backImageUrl: String!
        if (NSUserDefaults.standardUserDefaults().objectForKey("signUpBackgroundImageUrl") != nil)
        {
            backImageUrl = NSUserDefaults.standardUserDefaults().stringForKey("signUpBackgroundImageUrl")!
            let strurlLogo: String = "http://\(backImageUrl)"
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
                        let blurEffect = UIBlurEffect(style: UIBlurEffectStyle.Dark)
                        let blurEffectView = UIVisualEffectView(effect: blurEffect)
                        blurEffectView.frame = self.imgBackG.bounds
                        blurEffectView.autoresizingMask = [.FlexibleWidth, .FlexibleHeight] // for supporting device rotation
                        blurEffectView.alpha = 0.8
                        self.imgBackG.addSubview(blurEffectView)
                        let tempImgView = UIImageView(frame: CGRect(x: 0, y: 0, width: 512.0, height: 768.0))
                        tempImgView.image=UIImage(data: data!)
                        tempImgView.center = self.view!.center
                        self.imgBackG.image = tempImgView.image
                    }
                    dispatch_async(dispatch_get_main_queue(), display_image)
                }
            }
            task.resume()
        }
    }
    func callAlert(message: String)
    {
        dispatch_async(dispatch_get_main_queue(), {
            MBProgressHUD.hideHUDForView(self.view, animated: true)
            let alertController = UIAlertController(title: nil, message:
                message, preferredStyle: UIAlertControllerStyle.Alert)
            alertController.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.Default,handler: nil))
            self.presentViewController(alertController, animated: true, completion: nil)
        })
    }
    // MARK: - ------------ Actions ----------
    @IBAction func tapBack(sender: UIButton)
    {
        self.navigationController?.popViewControllerAnimated(true)
    }
    // MARK: - ------------ Api responses ----------
    func StoreCityStateResponseApi(responseDict: NSDictionary)
    {
        
        var successFlag: Bool
        successFlag = responseDict["successFlag"] as! Bool!
        let message: String = responseDict["message"]! as! String
        if successFlag {
            
            if isStringStoreCity=="state"
            {
                let data = NSKeyedArchiver.archivedDataWithRootObject((responseDict["stateList"] as? [AnyObject])!)
                NSUserDefaults.standardUserDefaults().setObject(data, forKey: "StateData")
                mydata = (responseDict["stateList"] as? [AnyObject])!
            }
            if isStringStoreCity=="FavoriteStore"
            {
                
                let data = NSKeyedArchiver.archivedDataWithRootObject((responseDict["stores"] as? [AnyObject])!)
                                    NSUserDefaults.standardUserDefaults().setObject(data, forKey: "FavoriteStoreData")
                mydata = (responseDict["stores"] as? [AnyObject])!
            }
            if isStringStoreCity=="country"
            {
                let data = NSKeyedArchiver.archivedDataWithRootObject((responseDict["countryList"] as? [AnyObject])!)
                NSUserDefaults.standardUserDefaults().setObject(data, forKey: "CountryData")
                mydata = (responseDict["countryList"] as? [AnyObject])!
            }
            for i in 0 ..< Int(mydata.count)
            {
                storeCityCountryArray.addObject(mydata[i])
            }
            dispatch_async(dispatch_get_main_queue(), {
                MBProgressHUD.hideHUDForView(self.view, animated: true)
                self.tblView1.reloadData()
            })
        }
        else
        {
            callAlert(message)
        }
    }
    func SignUpRuleBonusResponseApi(responseDict: NSMutableArray)
    {
        if responseDict.count != 0
        {
            lblTopSignUp.text="Select SignUp Bonus"
            storeCityCountryArray=[]
         
            
            
            storeCityCountryArray=responseDict
            
            dispatch_async(dispatch_get_main_queue(), {
                MBProgressHUD.hideHUDForView(self.view, animated: true)
                self.tblView1.reloadData()
            })
        }
        else
        {
            lblTopSignUp.text="No Bonus"
            let alert=UIAlertController(title: "", message: "No bonus", preferredStyle: UIAlertControllerStyle.Alert);
            let YESAction = UIAlertAction(title: "OK", style: .Default)
            { (action:UIAlertAction!) in
                
                dispatch_async(dispatch_get_main_queue(),
                               {
                                self.navigationController?.popViewControllerAnimated(true)
                })
            }
            alert.addAction(YESAction)
            dispatch_async(dispatch_get_main_queue(), {
                self.presentViewController(alert, animated: true, completion:nil)
            })
            
            
            
        }
        
    }
   
      // MARK: - ------ UITableView Delegates And Datasource -------
    // number of rows in table view
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return storeCityCountryArray.count;
    }
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat
    {
        if isStringStoreCity=="Bonus"
        {
            return 80
        }
        else
        {
            return 45
        }
    }
    // create a cell for each table view row
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
            if isStringStoreCity=="Bonus" {
            let cell:BonusTableViewCell = tableView.dequeueReusableCellWithIdentifier("BonusTableViewCell") as! BonusTableViewCell!
            tableView.tableFooterView = UIView()
            cell.selectionStyle = .None
            tblView1.separatorStyle = .None
            if let dict : [String: AnyObject] = storeCityCountryArray[indexPath.row] as? [String : AnyObject]
            {
                cell.configureCellWithData(dict,index: indexPath.row)
            }
            return cell
        }
        else
        {
            let cell:UITableViewCell = tableView.dequeueReusableCellWithIdentifier("cell")! as UITableViewCell
            
            tableView.tableFooterView = UIView()
            cell.selectionStyle = .None
            if let dict : [String: AnyObject] = storeCityCountryArray[indexPath.row] as? [String : AnyObject]
            {
                if isStringStoreCity=="state"
                {
                    cell.textLabel?.text=dict["stateName"] as? String
                }
                if isStringStoreCity=="FavoriteStore"
                {
                    cell.textLabel?.text=dict["storeName"] as? String
                }
                if isStringStoreCity=="country"
                {
                    cell.textLabel?.text=dict["countryName"] as? String
                }
                
            }
            cell.contentView.backgroundColor=UIColor.clearColor()
            cell.textLabel?.textColor=UIColor.lightGrayColor()
            // set the text from the data model
            //        cell.textLabel?.text = self.profileMenuArray[indexPath.row]
            cell.textLabel?.backgroundColor=UIColor.clearColor()
            return cell
    }
    }
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath)
    {
    if let dict : [String: AnyObject] = storeCityCountryArray[indexPath.row] as? [String : AnyObject]
        {
            if isStringStoreCity=="Bonus"
            {
                isRewardRule = dict["rewardRule"] as? Bool
                strRuleID = String(dict["id"]! as! NSNumber)
                
                NSUserDefaults.standardUserDefaults().setObject(dict["description"] as? String, forKey: "Bonus")
            }
            else
            {
                var strSelectState:String!
                if isStringStoreCity=="state"
                {
                    strSelectState=dict["stateName"] as? String
                    stateCode = dict["stateCode"] as? String
                    NSUserDefaults.standardUserDefaults().setObject(strSelectState, forKey: "state")
                }
                if isStringStoreCity=="FavoriteStore"
                {
                    strSelectState=dict["storeName"] as? String
                    strStoreCode = dict["storeID"] as? NSNumber
                    
                    NSUserDefaults.standardUserDefaults().setObject(strSelectState, forKey: "FavoriteStore")
                }
                if isStringStoreCity=="country"
                {
                    countryCode=dict["countryCode"] as? String
                    strSelectState=dict["countryName"] as? String
                    NSUserDefaults.standardUserDefaults().setObject(strSelectState, forKey: "country")
                }
            }
        }
        self.navigationController?.popViewControllerAnimated(true)
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
