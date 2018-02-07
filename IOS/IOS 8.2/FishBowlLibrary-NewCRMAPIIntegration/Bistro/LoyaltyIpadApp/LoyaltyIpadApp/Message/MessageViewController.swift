//
//  MessageViewController.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 09/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class MessageViewController: UIViewController
{
    // MARK: - ------------ IBOutlets ----------
    @IBOutlet weak var lblNoMessageAvailable: UILabel!
    @IBOutlet weak var viewContainMessageTable: UIView!
    @IBOutlet weak var btnMoreOutlet: UIButton!
    @IBOutlet weak var viewOnTop: UIView!
    @IBOutlet weak var viewContainMarkedTableView: UIView!
    @IBOutlet weak var tblMarkAsReadUnRead: UITableView!
    @IBOutlet weak var btnDeleteoutlet: UIButton!
    @IBOutlet weak var lblMoreBtn: UILabel!
    @IBOutlet weak var viewSubMessage: UIView!
    @IBOutlet weak var imgBackG: UIImageView!
    @IBOutlet weak var btnLogOutOutlet: UIButton!
    @IBOutlet weak var imgTopHeaderRewardOffer: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgLineOnTopToSpaceBetweenNmae: UIImageView!
    @IBOutlet weak var imgrewardCompnyLogo: UIImageView!
    @IBOutlet weak var tblViewMessage: UITableView!
    // MARK: - ------------ Private variables ----------
    var messageArray:NSMutableArray=NSMutableArray()
    var color1:UIColor!
    var markReadUnReasArray:NSMutableArray=NSMutableArray()
    var check1 = false
    var strIdCustID:String!
    var deleteMessageArray:NSMutableArray=NSMutableArray()
    var markAsReadArray:NSMutableArray=NSMutableArray()
    var markAsUnReadArray:NSMutableArray=NSMutableArray()
    var arraySelectCheckMark:NSMutableArray=NSMutableArray()
    var arraySelected = NSMutableArray ()
    var dicts = NSMutableDictionary ()
    // MARK: - ------------ ViewMethods ----------
    override func viewDidLoad() {
        super.viewDidLoad()
        viewOnTop.layer.shadowColor = UIColor.blackColor().CGColor
        viewOnTop.layer.shadowOffset = CGSizeMake(2, 2);
        viewOnTop.layer.shadowOpacity = 0.5;
        viewContainMarkedTableView.hidden=true
        tblMarkAsReadUnRead.hidden=true
        btnDeleteoutlet.userInteractionEnabled = false
        tblMarkAsReadUnRead.hidden = true
        tblMarkAsReadUnRead.registerClass(UITableViewCell.self, forCellReuseIdentifier: "cell")
        markReadUnReasArray = ["Mark as Read","Mark as UnRead"]
        self.setScreenValue()
        self.tblViewMessage.tableFooterView = UIView.init()
        tblViewMessage.registerNib(UINib.init(nibName: "MessageTableViewCell", bundle: nil), forCellReuseIdentifier: "MessageTableViewCell")
        // Do any additional setup after loading the view.
        self.messageStatusApi()
        
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    // MARK: - ------------ Private Methods ----------
    //set screen color and images
    func setScreenValue()
    {
        self.viewSubMessage.layer.borderColor = UIColor.lightGrayColor().CGColor
        self.viewSubMessage.layer.borderWidth = 1
        viewSubMessage.clipsToBounds = true
        
        let colorHex: String = NSUserDefaults.standardUserDefaults().stringForKey("checkInButtonColor")!
        color1 = modelSharedInstance.hexStringToUIColor(colorHex)
        dispatch_async(dispatch_get_main_queue(),
                       {
                        self.lblName.text = NSUserDefaults.standardUserDefaults().stringForKey("userName");
                        self.btnDeleteoutlet.backgroundColor=self.color1
                        self.lblMoreBtn.backgroundColor=self.color1
                        
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
    // MARK: - ------------ Actions ----------
    @IBAction func tapBackMenu(sender: UIButton)
    {
        self.navigationController!.popViewControllerAnimated(true)
    }
    @IBAction func tapBtnMore(sender: UIButton)
    {
        
        //tblMarkAsReadUnRead.hidden=false
        btnMoreOutlet.selected = !btnMoreOutlet.selected
        let mArray = NSMutableArray ()
        for i in 0 ..< Int(self.messageArray.count)
        {
            let dict = (self.messageArray[i] as? [String : AnyObject])! as! NSMutableDictionary
            if ((dict.valueForKey("is_read_copy")?.boolValue) != nil && (dict.valueForKey("is_read_copy")?.boolValue)!) {
                mArray .addObject(dict.valueForKey("id")!)
            }
        }
        
        if(mArray.count == 0)
        {
            btnMoreOutlet.selected=false
            tblMarkAsReadUnRead.hidden = true
            viewContainMarkedTableView.hidden=true
        }
        else
        {
            
            if btnMoreOutlet.selected==true {
                viewContainMarkedTableView.hidden=false
                tblMarkAsReadUnRead.hidden=false
            }
            else
            {
                viewContainMarkedTableView.hidden=true
                tblMarkAsReadUnRead.hidden=true
            }
            
            //            tblMarkAsReadUnRead.hidden = false
            //            viewContainMarkedTableView.hidden=false
        }
    }
    @IBAction func tapDelete(sender: UIButton)
    {
        print(deleteMessageArray)
        print(markAsUnReadArray)
        print(markAsReadArray)
        let urlStr: String = "/loyalty/setMessageStatus"
        var inputs: [NSObject : AnyObject] = [NSObject : AnyObject]()
        
        let mArray = NSMutableArray ()
        for i in 0 ..< Int(self.messageArray.count)
        {
            let dict = (self.messageArray[i] as? [String : AnyObject])! as! NSMutableDictionary
            if ((dict.valueForKey("is_read_copy")?.boolValue) != nil && (dict.valueForKey("is_read_copy")?.boolValue)!) {
                mArray .addObject(dict.valueForKey("id")!)
            }
        }
        
        inputs["deletedMessage"] = mArray
        inputs["memberId"] = strIdCustID
        inputs["count"] = "10"
        let  objAPI = ApiClass()
        
        dispatch_async(dispatch_get_main_queue(), {
            MBProgressHUD.showHUDAddedTo(self.view, animated: true)
        })
        
        objAPI.setMessageStatusAPI(urlStr,dictValue: inputs, withTarget: self, withSelector: #selector(MessageViewController.MessageStatusResponseApi(_:)))
    }
    @IBAction func tapCheck(sender:UIButton)
    {
        let indexPath = NSIndexPath(forRow: sender.tag, inSection: 0)
        
        NSLog("index path --------- %d",indexPath.row);
        NSLog("sender tag --------- %d",[sender.tag]);
        
        let dict = (messageArray[sender.tag] as? [String : AnyObject])! as! NSMutableDictionary
        
        //var dicts = NSMutableDictionary()
        dicts = dict.mutableCopy() as! NSMutableDictionary
        
        
        if  (dicts.valueForKey("is_read_copy") != nil) &&  (dicts.valueForKey("is_read_copy")?.boolValue)! {
            dicts .setValue(NSNumber.init(bool: false), forKey: "is_read_copy")
        }
        else{
            dicts .setValue(NSNumber.init(bool: true), forKey: "is_read_copy")
        }
        
        let mArray = NSMutableArray ()
        for i in 0 ..< Int(self.messageArray.count)
        {
            let dict = (self.messageArray[i] as? [String : AnyObject])! as! NSMutableDictionary
            if ((dict.valueForKey("is_read_copy")?.boolValue) != nil && (dict.valueForKey("is_read_copy")?.boolValue)!) {
                mArray .addObject(dict.valueForKey("id")!)
            }
        }
        
        
        if(mArray.count == 0)
        {
            btnDeleteoutlet.userInteractionEnabled = true
        }
        else
        {
            btnDeleteoutlet.userInteractionEnabled = false
        }
        
        messageArray.replaceObjectAtIndex(sender.tag, withObject: dicts)
        tblViewMessage.reloadData()
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
            
            //   self.btnLogOutOutlet.userInteractionEnabled=true
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
    
    // MARK: - ------ UITableView Delegates And Datasource -------
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat
    {
        if tableView==tblViewMessage
        {
            return UITableViewAutomaticDimension
        }
        else
        {
            return 30
        }
    }
    
    func tableView(tableView: UITableView, estimatedHeightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if tableView==tblViewMessage
        {
            return UITableViewAutomaticDimension
        }
        else
        {
            return 30
        }
    }
    // number of rows in table view
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if tableView==tblMarkAsReadUnRead
        {
            return markReadUnReasArray.count;
        }
        else
        {
            return messageArray.count;
        }
    }
    // create a cell for each table view row
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        if tableView==tblMarkAsReadUnRead {
            
            let cell:UITableViewCell = tableView.dequeueReusableCellWithIdentifier("cell")! as UITableViewCell
            tableView.tableFooterView = UIView()
            cell.selectionStyle = .None
            //            cell.textLabel!.adjustsFontSizeToFitWidth=true
            cell.textLabel?.text=markReadUnReasArray[indexPath.row] as? String
            cell.contentView.backgroundColor=UIColor.whiteColor()
            cell.textLabel?.textColor=UIColor.blackColor()
            return cell
        }
        else
        {
            // create a new cell if needed or reuse an old one
            let cell:MessageTableViewCell = tableView.dequeueReusableCellWithIdentifier("MessageTableViewCell") as! MessageTableViewCell!
            tableView.tableFooterView = UIView()
            cell.selectionStyle = .None
            if let dict : [String: AnyObject] = messageArray[indexPath.row] as? [String : AnyObject]
            {
                
                if (dict["isRead"] as? Bool)! == true
                {
                    cell.lblMessage.font = UIFont(name:"Proxima Nova Regular", size: 18.0)
                    cell.lblMessage.alpha = 0.7
                    let image = UIImage(named: "checked.png")
                    cell.btnCheckUnCheckOutlet.setBackgroundImage(image, forState: .Normal)
                    // let color2:UIColor = modelSharedInstance.hexStringToUIColor("CCCCCC")
                    cell.contentView.backgroundColor = UIColor.clearColor()
                }
                else
                {
                    cell.lblMessage.alpha = 1.0
                    cell.lblMessage.font = UIFont(name:"Proxima Nova Bold", size: 18.0)
                    let image = UIImage(named: "unChecked.png")
                    cell.btnCheckUnCheckOutlet.setBackgroundImage(image, forState: .Normal)
                    cell.contentView.backgroundColor = UIColor.clearColor()
                }
                
                
                if (dict["is_read_copy"] as? Bool)! == true
                {
                    let image = UIImage(named: "checked.png")
                    cell.btnCheckUnCheckOutlet.setBackgroundImage(image, forState: .Normal)
                    let color2:UIColor = modelSharedInstance.hexStringToUIColor("CCCCCC")
                    cell.contentView.backgroundColor = color2
                    
                }
                else
                {
                    //cell.lblMessage.alpha = 1.0
                    cell.lblMessage.font = UIFont(name:"Proxima Nova Bold", size: 18.0)
                    let image = UIImage(named: "unChecked.png")
                    cell.btnCheckUnCheckOutlet.setBackgroundImage(image, forState: .Normal)
                    cell.contentView.backgroundColor = UIColor.clearColor()
                }
                cell.configureCellWithData(dict,index: indexPath.row)
                
            }
            else
            {
                cell.configureCellWithData([:],index: 0)
            }
            cell.btnCheckUnCheckOutlet.addTarget(self, action: #selector(MessageViewController.tapCheck(_:)), forControlEvents: .TouchUpInside)
            cell.btnCheckUnCheckOutlet.tag=indexPath.row
            
            return cell
        }
    }
  
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath)
    {
        if tableView==tblMarkAsReadUnRead
        {
            tblMarkAsReadUnRead.reloadData()
            btnMoreOutlet.selected=false
            tblMarkAsReadUnRead.hidden=true
            viewContainMarkedTableView.hidden=true
            
//            selectedCell.textLabel?.textColor=UIColor.whiteColor()
            if(indexPath.row == 0)
            {
                let selectedCell:UITableViewCell = tableView.cellForRowAtIndexPath(indexPath)!
                selectedCell.contentView.backgroundColor = color1
                selectedCell.textLabel?.textColor=UIColor.whiteColor()
                NSLog("check mark read-------%@",arraySelectCheckMark)
                let urlStr: String = "/loyalty/setMessageStatus"
                var inputs: [NSObject : AnyObject] = [NSObject : AnyObject]()
                let mArray = NSMutableArray ()
                for i in 0 ..< Int(self.messageArray.count)
                {
                    let dict = (self.messageArray[i] as? [String : AnyObject])! as! NSMutableDictionary
                    if ((dict.valueForKey("is_read_copy")?.boolValue) != nil && (dict.valueForKey("is_read_copy")?.boolValue)!) {
                        mArray .addObject(dict.valueForKey("id")!)
                    }
                }
                inputs["markedMessage"] = mArray
                inputs["memberId"] = strIdCustID
                inputs["count"] = "10"
                let  objAPI = ApiClass()
                MBProgressHUD.showHUDAddedTo(self.view, animated: true)
                objAPI.setMessageStatusAPI(urlStr,dictValue: inputs, withTarget: self, withSelector: #selector(MessageViewController.MessageMarkedMessageApi(_:)))
            }
            else
            {
                let selectedCell:UITableViewCell = tableView.cellForRowAtIndexPath(indexPath)!
                selectedCell.contentView.backgroundColor = color1
                selectedCell.textLabel?.textColor=UIColor.whiteColor()
                NSLog("check mark unread-------%@",arraySelectCheckMark)
                
                let urlStr: String = "/loyalty/setMessageStatus"
                var inputs: [NSObject : AnyObject] = [NSObject : AnyObject]()
                
                let mArray = NSMutableArray ()
                for i in 0 ..< Int(self.messageArray.count)
                {
                    let dict = (self.messageArray[i] as? [String : AnyObject])! as! NSMutableDictionary
                    if ((dict.valueForKey("is_read_copy")?.boolValue) != nil && (dict.valueForKey("is_read_copy")?.boolValue)!) {
                        mArray .addObject(dict.valueForKey("id")!)
                    }
                }
                
                inputs["unMarkedMessage"] = mArray
                inputs["memberId"] = strIdCustID
                inputs["count"] = "10"
                let  objAPI = ApiClass()
                
                dispatch_async(dispatch_get_main_queue(), {
                    MBProgressHUD.showHUDAddedTo(self.view, animated: true)
                })
                
                objAPI.setMessageStatusAPI(urlStr,dictValue: inputs, withTarget: self, withSelector: #selector(MessageViewController.MessageUnMarkedMessageApi(_:)))
            }
        }
    }

    // MARK: - ------------ Api responses ----------
    func MessageStatusResponseApi(responseDict: AnyObject)
    {
        print(responseDict)
        var successFlag: Bool
        successFlag = responseDict["successFlag"] as! Bool!
        let message: String = responseDict["message"]! as! String
        
        dispatch_async(dispatch_get_main_queue(), {
            MBProgressHUD.hideHUDForView(self.view, animated: true)
        })
        
        if successFlag
        {
            self.messageStatusApi()
        }
        else
        {
            callAlert(message)
        }
    }

    func messageStatusApi()
    {
        strIdCustID = NSUserDefaults.standardUserDefaults().stringForKey("customerID")!
        let urlStr: String = "/loyalty/getLoyaltyMessages"
        
        var inputs: [NSObject : AnyObject] = [NSObject : AnyObject]()
        inputs["startIndex"] = "0"
        inputs["memberId"] = strIdCustID
        inputs["count"] = "10"
        
        
        dispatch_async(dispatch_get_main_queue(), {
            MBProgressHUD.showHUDAddedTo(self.view, animated: true)
        })
        
        let  objAPI = ApiClass()
        
        objAPI.getLoyaltyMessageAPI(urlStr,dictValue: inputs, withTarget: self, withSelector: #selector(MessageViewController.LoyaltyMessageResponseApi(_:)))
    }
    
    func LoyaltyMessageResponseApi(responseDict: AnyObject)
    {
        
        print(responseDict)
        var successFlag: Bool
        successFlag = responseDict["successFlag"] as! Bool!
        let message: String = responseDict["message"]! as! String
        if successFlag
        {
            
            deleteMessageArray=[]
            markAsReadArray=[]
            markAsUnReadArray=[]
            messageArray=[]
            var mydata: [AnyObject] = (responseDict["loyaltyCutomerMessageArray"] as? [AnyObject])!
            for i in 0 ..< Int(mydata.count)
            {
                let dict = mydata[i].mutableCopy() as! NSMutableDictionary
                dict .setValue(NSNumber.init(bool: false), forKey: "is_read_copy");
                messageArray.addObject(dict)
                //btnDeleteoutlet.userInteractionEnabled = true
            }
            
            dispatch_async(dispatch_get_main_queue(), {
                self.lblNoMessageAvailable.hidden=true
                self.viewContainMessageTable.hidden=false
                MBProgressHUD.hideHUDForView(self.view, animated: true)
                self.tblViewMessage.reloadData()
                
            })
        }
        else
        {
            dispatch_async(dispatch_get_main_queue(), {
                self.lblNoMessageAvailable.hidden=false
                self.viewContainMessageTable.hidden=true
            })
            callAlert(message)
            
        }
    }

    func MessageMarkedMessageApi(responseDict: AnyObject)
    {
        print(responseDict)
        
        dispatch_async(dispatch_get_main_queue(), {
            MBProgressHUD.hideHUDForView(self.view, animated: true)
        })
        var successFlag: Bool
        successFlag = responseDict["successFlag"] as! Bool!
        if successFlag
        {
            for i in 0 ..< Int(self.messageArray.count)
            {
                let dict = (self.messageArray[i] as? [String : AnyObject])! as! NSMutableDictionary
                let dicts = NSMutableDictionary()
                dicts .setValue(NSNumber.init(bool: false), forKey: "is_read_copy")
                print(dicts)
                self.messageArray.replaceObjectAtIndex(i, withObject: dicts)
            }
            self.tblViewMessage.reloadData()
            self.messageStatusApi()
        }
        
    }
    
    func MessageUnMarkedMessageApi(responseDict: AnyObject)
    {
        print(responseDict)
        
        dispatch_async(dispatch_get_main_queue(), {
            MBProgressHUD.hideHUDForView(self.view, animated: true)
        })
        
        var successFlag: Bool
        successFlag = responseDict["successFlag"] as! Bool!
        
        if successFlag
        {
            
            for i in 0 ..< Int(self.messageArray.count)
            {
                let dict = (self.messageArray[i] as? [String : AnyObject])! as! NSMutableDictionary
                let dicts = NSMutableDictionary()
                dicts .setValue(NSNumber.init(bool: false), forKey: "is_read_copy")
                
                print(dicts)
                self.messageArray.replaceObjectAtIndex(i, withObject: dicts)
            }
            
            self.tblViewMessage.reloadData()
            self.messageStatusApi()
            
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
