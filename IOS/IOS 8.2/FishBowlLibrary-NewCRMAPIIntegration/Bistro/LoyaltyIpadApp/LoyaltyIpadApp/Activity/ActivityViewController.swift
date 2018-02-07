//
//  ActivityViewController.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 09/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class ActivityViewController: UIViewController
{
    // MARK: - ------------ IBOutlets ----------
    @IBOutlet weak var lblNoActivity: UILabel!
    @IBOutlet weak var viewContainActivityTable: UIView!
    @IBOutlet weak var btnFilterActivityOutlet: UIButton!
    @IBOutlet weak var viewOnTop: UIView!
    @IBOutlet weak var viewContainTableActivityType: UIView!
    @IBOutlet weak var tblFilter: UITableView!
    @IBOutlet weak var lblfilterType: UILabel!
    @IBOutlet weak var btnApply: UIButton!
    @IBOutlet weak var viewSubActivity: UIView!
    @IBOutlet weak var imgBackG: UIImageView!
    @IBOutlet weak var btnLogOutOutlet: UIButton!
    @IBOutlet weak var imgTopHeaderRewardOffer: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgLineOnTopToSpaceBetweenNmae: UIImageView!
    @IBOutlet weak var imgrewardCompnyLogo: UIImageView!
    @IBOutlet weak var tblViewActivity: UITableView!
    // MARK: - ------------ Private variables ----------
    var strTypeActivity:String!
    var activityArray:NSMutableArray=NSMutableArray()
    var filterArray:NSMutableArray=NSMutableArray()
    var filterApiArray:NSMutableArray=NSMutableArray()
    var dictData = [String : AnyObject]()
    var color1:UIColor!
    // MARK: - ------------ ViewMethods ----------
    override func viewDidLoad() {
        super.viewDidLoad()
        viewOnTop.layer.shadowColor = UIColor.blackColor().CGColor
        viewOnTop.layer.shadowOffset = CGSizeMake(2, 2);
        viewOnTop.layer.shadowOpacity = 0.5;
        tblFilter.hidden=true
        viewContainTableActivityType.hidden=true
        tblFilter.registerClass(UITableViewCell.self, forCellReuseIdentifier: "cell")
        filterArray = ["POINT_RULE","REWARD_RULE","REDEMPTION","POINT_CONVERSION"]
        filterApiArray = ["Point Rule","Reward Rule","Redemption","Point Conversion"]
        MBProgressHUD.showHUDAddedTo(self.view, animated: true)
        let strId:String = NSUserDefaults.standardUserDefaults().stringForKey("customerID")!
        var inputs: [NSObject : AnyObject] = [NSObject : AnyObject]()
        //        "memberId":53,
        //        "activityType":"POINT_RULE",
        //        "startIndex":0
        inputs["activityType"] = "POINT_RULE"
        inputs["startIndex"] = "0"
        inputs["memberId"] = strId
        inputs["count"] = "10"
        
        let  objAPI = ApiClass()
        objAPI.ActivityAPI(inputs, withTarget: self, withSelector: #selector(ActivityViewController.ActivityResponseApi(_:)))
        self.setScreenValue()
        // self.setValueInArrayOfDictionary()
        
        self.tblViewActivity.tableFooterView = UIView.init()
        tblViewActivity.registerNib(UINib.init(nibName: "ActivityTableViewCell", bundle: nil), forCellReuseIdentifier: "ActivityTableViewCell")
        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
   
    // MARK: - ------------ Actions ----------
    @IBAction func tapBackMenu(sender: UIButton)
    {
        self.navigationController!.popViewControllerAnimated(true)
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
    
    @IBAction func tapToLoyalty(sender: UIButton)
    {
        let switchViewController = self.navigationController?.viewControllers[1] as! RewardsOffersViewController
        
        self.navigationController?.popToViewController(switchViewController, animated: true)
    }
    @IBAction func tapApplyFilter(sender: UIButton)
    {
        var inputs: [NSObject : AnyObject] = [NSObject : AnyObject]()
        //        "memberId":53,
        //        "activityType":"POINT_RULE",
        //        "startIndex":0
        let strId:String = NSUserDefaults.standardUserDefaults().stringForKey("customerID")!
        inputs["activityType"] = strTypeActivity
        inputs["startIndex"] = "0"
        inputs["memberId"] = strId
        inputs["count"] = "10"
        MBProgressHUD.showHUDAddedTo(self.view, animated: true)
        let  objAPI = ApiClass()
        objAPI.ActivityAPI(inputs, withTarget: self, withSelector: #selector(ActivityViewController.ActivityResponseApi(_:)))
        
    }
    
    @IBAction func tapFilterType(sender: UIButton)
    {
        btnFilterActivityOutlet.selected = !btnFilterActivityOutlet.selected
        if sender.selected {
            
            tblFilter.hidden=false
            viewContainTableActivityType.hidden=false
        }
        else
        {
            tblFilter.hidden=true
            viewContainTableActivityType.hidden=true
        }
        
    }

    // MARK: - ------ UITableView Delegates And Datasource -------
    // number of rows in table view
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if tableView==tblViewActivity
        {
            return activityArray.count
        }
        else
        {
            return filterArray.count;
        }
    }
    // create a cell for each table view row
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if tableView==tblViewActivity
        {
            // create a new cell if needed or reuse an old one
            let cell:ActivityTableViewCell = tableView.dequeueReusableCellWithIdentifier("ActivityTableViewCell") as! ActivityTableViewCell!
            
            tableView.tableFooterView = UIView()
            
            if let dict : [String: AnyObject] = activityArray[indexPath.row] as? [String : AnyObject]
            {
                cell.configureCellWithData(dict)
                cell.lblCheckPoints.textColor = color1
            }
            else
            {
                cell.configureCellWithData([:])
            }
            cell.contentView.backgroundColor = UIColor.whiteColor()
            cell.textLabel?.textColor=UIColor.blackColor()
            return cell
        }
        else
        {
            let cell:UITableViewCell = tableView.dequeueReusableCellWithIdentifier("cell")! as UITableViewCell
            tableView.tableFooterView = UIView()
            cell.selectionStyle = .None
            cell.textLabel?.text=filterApiArray[indexPath.row] as? String
            cell.contentView.backgroundColor = UIColor.whiteColor()
            cell.textLabel?.textColor=UIColor.blackColor()
            //            let view1 = UIView()
            //            view1.backgroundColor = color1
            //            cell.selectedBackgroundView=view1
            return cell
            
        }
    }
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat
    {
        if tableView==tblViewActivity
        {
            return UITableViewAutomaticDimension
        }
        else
        {
            return 30
        }
    }
    func tableView(tableView: UITableView, estimatedHeightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if tableView==tblViewActivity
        {
            return UITableViewAutomaticDimension
        }
        else
        {
            return 30
        }
    }
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath)
    {
        
        if tableView==tblFilter
        {
            tblFilter .reloadData()
            btnFilterActivityOutlet.selected=false
            let selectedCell:UITableViewCell = tableView.cellForRowAtIndexPath(indexPath)!
            selectedCell.contentView.backgroundColor = color1
            selectedCell.textLabel?.textColor=UIColor.whiteColor()
            lblfilterType.text = "  " + (filterApiArray[indexPath.row] as! String)
            strTypeActivity = filterArray[indexPath.row] as! String
            tblFilter.hidden=true
            viewContainTableActivityType.hidden=true
        }
        
        
    }
    
    // MARK: - ------------ Private Methods ----------

    func setScreenValue()
    {
        self.viewSubActivity.layer.borderColor = UIColor.lightGrayColor().CGColor
        self.viewSubActivity.layer.borderWidth = 1
        viewSubActivity.clipsToBounds = true
        
        let colorHex: String = NSUserDefaults.standardUserDefaults().stringForKey("checkInButtonColor")!
        color1 = modelSharedInstance.hexStringToUIColor(colorHex)
        dispatch_async(dispatch_get_main_queue(),
                       {
                        self.lblName.text = NSUserDefaults.standardUserDefaults().stringForKey("userName");
                        self.btnApply.backgroundColor=self.color1
                        
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
      // MARK: - ------------ MessageResponseApi Response ----------
    func ActivityResponseApi(responseDict: NSDictionary)
    {
        var successFlag: Bool
        print(responseDict)
        successFlag = responseDict["successFlag"] as! Bool!
        let message: String = responseDict["message"] as! String
        if successFlag
        {
            activityArray=[]
            NSLog("%@", responseDict)
            var mydata: [AnyObject] = (responseDict["loyaltyActivityList"] as? [AnyObject])!
            for i in 0 ..< Int(mydata.count)
            {
                activityArray.addObject(mydata[i])
            }
            NSLog("%@", activityArray)
            dispatch_async(dispatch_get_main_queue(),
                           {
                            self.lblNoActivity.hidden=true
                            self.viewContainActivityTable.hidden=false
                            self.tblViewActivity.reloadData()
                            MBProgressHUD.hideHUDForView(self.view, animated: true)
            })
        }
        else
        {
            dispatch_async(dispatch_get_main_queue(),
                           {
                            self.lblNoActivity.hidden=false
                            self.viewContainActivityTable.hidden=true
            })
            
            self.callAlert(message)
        }
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
