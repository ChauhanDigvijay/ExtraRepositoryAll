//
//  ContactUsViewController.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 09/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

class ContactUsViewController: UIViewController
{
    // MARK: - ------------ IBOutlets ----------
    @IBOutlet weak var constViewContainAreaTypeHeight: NSLayoutConstraint!
    @IBOutlet weak var constViewContainMessageTypeHeight: NSLayoutConstraint!
    @IBOutlet weak var btnSelectAreaTypeOutlet: UIButton!
    @IBOutlet weak var btnSelectMessageTypeOutlet: UIButton!
    @IBOutlet weak var viewOnTop: UIView!
    @IBOutlet weak var viewContainAreaTable: UIView!
    @IBOutlet weak var viewContainOptionTable: UIView!
    @IBOutlet weak var tblSelectTypeOfMessage: UITableView!
    @IBOutlet weak var btnResetOutlet: UIButton!
    @IBOutlet weak var tblSelectArea: UITableView!
    @IBOutlet weak var btnSubmitOutlet: UIButton!
    @IBOutlet weak var imgBackG: UIImageView!
    @IBOutlet weak var txtMessageSubject: UITextField!
    @IBOutlet weak var viewSubContactUs: UIView!
    @IBOutlet weak var btnLogOutOutlet: UIButton!
    @IBOutlet weak var lblSelctTypeOfMessage: UILabel!
    @IBOutlet weak var txtWriteMessage: UITextField!
    @IBOutlet weak var imgTopHeaderRewardOffer: UIImageView!
    @IBOutlet weak var lblSelectTypeOfOption: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgLineOnTopToSpaceBetweenNmae: UIImageView!
    @IBOutlet weak var imgrewardCompnyLogo: UIImageView!
    // MARK: - ------------ Private variables ----------
    var color1:UIColor!
    var messageTypeArray:NSMutableArray=NSMutableArray()
    var areaTypeArray:NSMutableArray=NSMutableArray()
    var strMassageTypeId:String?
    var strAreaTypeId:String?
    // MARK: - ------------ ViewMethods ----------
    override func viewDidLoad() {
        super.viewDidLoad()
        viewOnTop.layer.shadowColor = UIColor.blackColor().CGColor
        viewOnTop.layer.shadowOffset = CGSizeMake(2, 2);
        viewOnTop.layer.shadowOpacity = 0.5;
        let  objAPI = ApiClass()
        objAPI.getLoyaltyMessageTypeAPI(withTarget: self, withSelector: #selector(ContactUsViewController.LoyaltyMessageTypeResponseApi(_:)))
        MBProgressHUD.showHUDAddedTo(self.view, animated: true)
        tblSelectArea.hidden = true;
        tblSelectTypeOfMessage.hidden = true;
        viewContainAreaTable.hidden=true
        viewContainOptionTable.hidden=true
        // Register the table view cell class and its reuse id
        tblSelectTypeOfMessage.registerClass(UITableViewCell.self, forCellReuseIdentifier: "cell")
        tblSelectArea.registerClass(UITableViewCell.self, forCellReuseIdentifier: "cell")
        self.setScreenValue()
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    // MARK: - ------------ Private Methods ----------
    
    //set screen color and images
    func setScreenValue()
    {
        self.viewSubContactUs.layer.borderColor = UIColor.lightGrayColor().CGColor
        self.viewSubContactUs.layer.borderWidth = 0.5
        viewSubContactUs.clipsToBounds = true
        lblSelctTypeOfMessage.textColor=UIColor.lightGrayColor()
        lblSelectTypeOfOption.textColor=UIColor.lightGrayColor()
        self.lblSelctTypeOfMessage.layer.borderColor = UIColor.lightGrayColor().CGColor
        self.lblSelctTypeOfMessage.layer.borderWidth = 0.5
        lblSelctTypeOfMessage.clipsToBounds = true
        self.lblSelectTypeOfOption.layer.borderColor = UIColor.lightGrayColor().CGColor
        self.lblSelectTypeOfOption.layer.borderWidth = 0.5
        lblSelectTypeOfOption.clipsToBounds = true
        
        //        let paddingView = UIView(frame: CGRectMake(0, 0, 10, self.txtWriteMessage.frame.height-1))
        //        txtWriteMessage.leftView = paddingView
        //        txtWriteMessage.leftViewMode = UITextFieldViewMode.Always
        //        let paddingView2 = UIView(frame: CGRectMake(0, 0, 10, self.txtMessageSubject.frame.height-1))
        //        txtMessageSubject.leftView = paddingView2
        //        txtMessageSubject.leftViewMode = UITextFieldViewMode.Always
        
        let colorHex: String = NSUserDefaults.standardUserDefaults().stringForKey("checkInButtonColor")!
        color1 = modelSharedInstance.hexStringToUIColor(colorHex)
        dispatch_async(dispatch_get_main_queue(),
                       {
                        self.lblName.text = NSUserDefaults.standardUserDefaults().stringForKey("userName");
                        self.btnSubmitOutlet.backgroundColor=self.color1
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
    @IBAction func tapTypeOfArea(sender: UIButton)
    {
        if areaTypeArray.count>0
        {
            btnSelectAreaTypeOutlet.selected = !btnSelectAreaTypeOutlet.selected
            if btnSelectAreaTypeOutlet.selected {
                self.lblSelectTypeOfOption.textColor=UIColor.blackColor()
                self.tblSelectArea.hidden=false;
                self.tblSelectTypeOfMessage.hidden=true;
                self.viewContainAreaTable.hidden=false
                self.viewContainOptionTable.hidden=true
                self.txtWriteMessage.userInteractionEnabled=false
            }
            else
            {
                self.tblSelectArea.hidden=true;
                self.tblSelectTypeOfMessage.hidden=true;
                self.viewContainAreaTable.hidden=true
                self.viewContainOptionTable.hidden=true
                self.txtWriteMessage.userInteractionEnabled=true
            }
            
        }
        else
        {
            self.callAlert("Data not loaded")
        }
    }
    @IBAction func tapTypeOfMessage(sender: UIButton)
    {
        if messageTypeArray.count>0
        {
            btnSelectMessageTypeOutlet.selected = !btnSelectMessageTypeOutlet.selected
            if btnSelectMessageTypeOutlet.selected {
                self.lblSelctTypeOfMessage.textColor=UIColor.blackColor()
                self.tblSelectArea.hidden=true;
                self.tblSelectTypeOfMessage.hidden=false;
                self.viewContainAreaTable.hidden=true
                self.viewContainOptionTable.hidden=false
                self.txtWriteMessage.userInteractionEnabled=false
            }
            else
            {
                self.tblSelectArea.hidden=true;
                self.tblSelectTypeOfMessage.hidden=true;
                self.viewContainAreaTable.hidden=true
                self.viewContainOptionTable.hidden=true
                self.txtWriteMessage.userInteractionEnabled=true
            }
        }
        else
        {
            self.callAlert("Data not loaded")
        }
        
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
    @IBAction func tapSubmit(sender: UIButton)
    {
        
        if self.txtMessageSubject.text?.isEmpty == true
        {
            self.callAlert("Enter your subject")
        }
        else if self.lblSelctTypeOfMessage.text == "Select Option"
        {
            self.callAlert("Select message type")
        }
        else if self.lblSelectTypeOfOption.text == "Select Option"
        {
            self.callAlert("Select area type")
        }
        else if self.txtWriteMessage.text?.isEmpty == true
        {
            self.callAlert("Please write your message")
        }
        else
        {
            MBProgressHUD.showHUDAddedTo(self.view, animated: true)
            let strId:String = NSUserDefaults.standardUserDefaults().stringForKey("customerID")!
            var inputs: [NSObject : AnyObject] = [NSObject : AnyObject]()
            inputs["messageType"] = strMassageTypeId
            inputs["areaType"] = strAreaTypeId
            inputs["subject"] = txtMessageSubject.text
            inputs["description"] = txtWriteMessage.text
            inputs["memberId"] = strId
            let  objAPI = ApiClass()
            objAPI.ContactUsAPI(inputs, withTarget: self, withSelector: #selector(ContactUsViewController.ContactUsResponseApi(_:)))
        }
    }
    
    
      @IBAction func tapReset(sender: UIButton)
    {
        txtMessageSubject.text = ""
        txtWriteMessage.text = ""
        lblSelectTypeOfOption.text=" Select Option"
        lblSelctTypeOfMessage.text=" Select Option"
    }
    @IBAction func tapLogout(sender: UIButton)
    {
        let alert=UIAlertController(title: "", message: "Do you want to logout", preferredStyle: UIAlertControllerStyle.Alert);
        let NOAction = UIAlertAction(title: "NO", style: .Default)
        { (action:UIAlertAction!) in
            
            //self.btnLogOutOutlet.userInteractionEnabled=true
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

    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if tableView==tblSelectTypeOfMessage {
            return messageTypeArray.count;
        }
        else
        {
            return areaTypeArray.count;
        }
    }
    
    // create a cell for each table view row
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        // create a new cell if needed or reuse an old one
        let cell:UITableViewCell = tableView.dequeueReusableCellWithIdentifier("cell")! as UITableViewCell
        tableView.tableFooterView = UIView()
        
        if tableView==tblSelectTypeOfMessage
        {
            let dict : [String: AnyObject] = (messageTypeArray[indexPath.row] as? [String : AnyObject])!
            if (dict["messageType"] as? String != nil)
            {
                cell.textLabel?.text=dict["messageType"] as? String
            }
            
        }
        else
        {
            let dict : [String: AnyObject] = (areaTypeArray[indexPath.row] as? [String : AnyObject])!
            if (dict["areaType"] as? String != nil)
            {
                cell.textLabel?.text=dict["areaType"] as? String
            }
        }
        cell.contentView.backgroundColor = UIColor.whiteColor()
        cell.textLabel?.textColor=UIColor.blackColor()
        
        return cell
    }
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath)
    {
        
        if tableView==tblSelectTypeOfMessage
        {
            btnSelectMessageTypeOutlet.selected = false
            tblSelectTypeOfMessage.reloadData()
            let dict : [String: AnyObject] = (messageTypeArray[indexPath.row] as? [String : AnyObject])!
            if (dict["messageType"] as? String != nil)
            {
                let strType = dict["messageType"] as! String
                lblSelctTypeOfMessage.text = "  " + strType
                strMassageTypeId=(dict["id"]?.stringValue)!
            }
            
            
            tblSelectTypeOfMessage.hidden=true
            tblSelectArea.hidden=true
            viewContainAreaTable.hidden=true
            viewContainOptionTable.hidden=true
            txtWriteMessage.userInteractionEnabled=true
            let selectedCell:UITableViewCell = tableView.cellForRowAtIndexPath(indexPath)!
            selectedCell.contentView.backgroundColor = color1
            selectedCell.textLabel?.textColor=UIColor.whiteColor()
            
        }
        else
        {
            btnSelectAreaTypeOutlet.selected = false
            tblSelectArea.reloadData()
            let dict : [String: AnyObject] = (areaTypeArray[indexPath.row] as? [String : AnyObject])!
            if (dict["areaType"] as? String != nil)
            {
                let strType = dict["areaType"] as! String
                lblSelectTypeOfOption.text = "  " + strType
                strAreaTypeId=(dict["id"]?.stringValue)!
            }
            
            tblSelectTypeOfMessage.hidden=true
            tblSelectArea.hidden=true
            viewContainAreaTable.hidden=true
            viewContainOptionTable.hidden=true
            txtWriteMessage.userInteractionEnabled=true
            let selectedCell:UITableViewCell = tableView.cellForRowAtIndexPath(indexPath)!
            selectedCell.contentView.backgroundColor = color1
            selectedCell.textLabel?.textColor=UIColor.whiteColor()
            
        }
    }
    
    
   
    // MARK: - ------------ Api responses ----------
    func LoyaltyAreaTypeResponseApi(responseDict: AnyObject)
    {
        
        var successFlag: Bool
        successFlag = responseDict["successFlag"] as! Bool!
        let message: String = responseDict["message"]! as! String
        if successFlag {
            
            var mydata: [AnyObject] = (responseDict["loyaltyAreaType"] as? [AnyObject])!
            for i in 0 ..< Int(mydata.count)
            {
                areaTypeArray.addObject(mydata[i])
            }
            dispatch_async(dispatch_get_main_queue(),
                           {
                            self.constViewContainAreaTypeHeight.constant = 30 * CGFloat(self.areaTypeArray.count)
                            self.tblSelectArea.reloadData()
                            MBProgressHUD.hideHUDForView(self.view, animated: true)
            })
            
        }
        else
        {
            callAlert(message)
            
        }
        
    }
    func LoyaltyMessageTypeResponseApi(responseDict: AnyObject)
    {
        
        var successFlag: Bool
        successFlag = responseDict["successFlag"] as! Bool!
        let message: String = responseDict["message"]! as! String
        if successFlag {
            let  objAPI = ApiClass()
            objAPI.getLoyaltyAreaTypeAPI(withTarget: self, withSelector: #selector(ContactUsViewController.LoyaltyAreaTypeResponseApi(_:)))
            var mydata: [AnyObject] = (responseDict["loyaltyMessageType"] as? [AnyObject])!
            for i in 0 ..< Int(mydata.count)
            {
                messageTypeArray.addObject(mydata[i])
            }
            dispatch_async(dispatch_get_main_queue(), {
                
                self.constViewContainMessageTypeHeight.constant = 30 * CGFloat(self.messageTypeArray.count)
                self.tblSelectTypeOfMessage.reloadData()
            })
            
        }
        else
        {
            let  objAPI = ApiClass()
            objAPI.getLoyaltyAreaTypeAPI(withTarget: self, withSelector: #selector(ContactUsViewController.LoyaltyAreaTypeResponseApi(_:)))
            callAlert(message)
            
        }
        
        
    }
    //pragma mark - ContactUsResponseApi Response
    func ContactUsResponseApi(response: NSDictionary)
    {
        
        var successFlag: Bool
        successFlag = response["successFlag"] as! Bool!
        let message: String = response["message"]! as! String
        if successFlag
        {
            print(message)
            self.callAlert(message)
            
            
        }
        else
        {
            self.callAlert(message)
        }
    }
        // MARK: - ------------ UITextField Delegates ----------
    func animateTextField(textField: UITextField, up: Bool)
    {
        let movementDistance:CGFloat = -130
        let movementDuration: Double = 0.3
        
        var movement:CGFloat = 0
        if up
        {
            movement = movementDistance
        }
        else
        {
            movement = -movementDistance
        }
        UIView.beginAnimations("animateTextField", context: nil)
        UIView.setAnimationBeginsFromCurrentState(true)
        UIView.setAnimationDuration(movementDuration)
        self.view.frame = CGRectOffset(self.view.frame, 0, movement)
        UIView.commitAnimations()
    }
    
    
    func textFieldDidBeginEditing(textField: UITextField)
    {
        self.animateTextField(txtWriteMessage, up:true)
    }
    
    func textFieldDidEndEditing(textField: UITextField)
    {
        self.animateTextField(txtWriteMessage, up:false)
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
