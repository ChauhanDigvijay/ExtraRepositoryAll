//
//  SignUpTableViewCell.swift
//  LoyaltyIpadApp
//
//  Created by surendra pathak on 25/11/16.
//  Copyright © 2016 fishbowl. All rights reserved.
//

import UIKit

protocol ImageCheck:class {
    func arrayreplace(intTag : NSInteger , stringValue :String , textFieldValue:String)
    func arrayGender(intTag : NSInteger , stringValue :String)
    func keyBoardResign(textField : UITextField)
}

var arrayTextfield:NSMutableArray = NSMutableArray();
class SignUpTableViewCell: UITableViewCell,UITextFieldDelegate
{
    weak var delegate:ImageCheck?
    @IBOutlet weak var lblOptionType: UILabel!
    @IBOutlet weak var imgArrow: UIImageView!
    @IBOutlet weak var btnOptionCheck: UIButton!
    @IBOutlet weak var viewContainOptionCheckBox: UIView!
    @IBOutlet weak var btnFemale: UIButton!
    @IBOutlet weak var btnMale: UIButton!
    @IBOutlet weak var viewContainGenders: UIView!
    @IBOutlet weak var textField: UITextField!
    @IBOutlet var lblOther: UILabel!
    
    //   let arrayTextfield:NSMutableArray = NSMutableArray();
    var check1 = false
    override func awakeFromNib() {
        super.awakeFromNib()
        self.selectionStyle = .None
        self.contentView.backgroundColor=UIColor.clearColor()
        self.backgroundColor=UIColor.clearColor()
        self.textLabel?.textColor=UIColor.whiteColor()
        self.textLabel?.backgroundColor=UIColor.clearColor()
        // Initialization code
    }
    @IBAction func tapMale(sender: AnyObject)
    {
        NSUserDefaults.standardUserDefaults().setObject("M", forKey: "maleFemale")
        btnFemale.setImage(UIImage(named: "radioUnSel.png"), forState: .Normal)
        btnMale.setImage(UIImage(named: "radioSel.png"), forState: .Normal)
        self.delegate?.arrayGender(sender.tag,stringValue: "M")
    }
    @IBAction func tapfemale(sender: AnyObject)
    {
        NSUserDefaults.standardUserDefaults().setObject("F", forKey: "maleFemale")
        btnFemale.setImage(UIImage(named: "radioSel.png"), forState: .Normal)
        btnMale.setImage(UIImage(named: "radioUnSel.png"), forState: .Normal)
        self.delegate?.arrayGender(sender.tag,stringValue: "F")
    }
    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
    func configureCellWithSignUpData(dict : [String: AnyObject],index : Int) -> Void
    {
        let strFieldName:String = dict["field"] as! String
        // let sequenceField:NSNumber = dict["configDisplaySeq"] as! NSNumber
        
        if(strFieldName == "FirstName")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=false
            lblOther.hidden = true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=true
            textField.tag = index
        }
        else if(strFieldName == "LastName")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=false
            lblOther.hidden = true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=true
            textField.tag = index
        }
        else if(strFieldName == "EmailAddress")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=false
            lblOther.hidden = true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=true
            textField.tag = index
        }
        else if(strFieldName == "PhoneNumber")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=false
            lblOther.hidden = true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=true
            textField.tag = index
            
        }
        else if(strFieldName == "Password")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=false
            lblOther.hidden = true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=true
            //textField.secureTextEntry = true
            textField.tag = index
        }
        else if(strFieldName == "DOB")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=false
            lblOther.hidden = true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=true
            textField.tag = index
            let btn: UIButton = UIButton(frame: CGRectMake(-5,0,430,60))
            btn.backgroundColor = UIColor.clearColor()
            btn.setTitle("Click Me", forState: UIControlState.Normal)
            textField.addSubview(btn)
            btn.addTarget(self, action: #selector(SignUpTableViewCell.keyBoardResign(_:)), forControlEvents:.TouchUpInside)
            btn.tag = textField.tag
        }
        else if(strFieldName == "Gender")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=true
            viewContainOptionCheckBox.hidden=true
            viewContainGenders.hidden=false
            lblOther.hidden = true
            btnMale.tag=index
            btnFemale.tag=index
            btnFemale.setImage(UIImage(named: "radioUnSel.png"), forState: .Normal)
            btnMale.setImage(UIImage(named: "radioSel.png"), forState: .Normal)
        }
        else if(strFieldName == "Address")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=false
            lblOther.hidden = true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=true
            textField.tag = index
        }
        else if(strFieldName == "Country")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=false
            textField.hidden=true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=false
            lblOther.tag=index
            lblOther.hidden = false
            textField.tag = index
        }
        else if(strFieldName == "State")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=false
            textField.hidden=true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=false
            lblOther.tag=index
            lblOther.hidden = false
        }
        else if(strFieldName == "City")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=false
            lblOther.hidden = true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=true
            textField.tag = index
        }
        else if(strFieldName == "FavoriteStore")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=false
            textField.hidden=true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=false
            lblOther.tag=index
            lblOther.hidden = false
            textField.tag = index
        }
        else if(strFieldName == "ZipCode")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=false
            lblOther.hidden = true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=true
            textField.tag = index
            
        }
        else if(strFieldName == "EmailOptIn")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=false
            lblOther.hidden = true
            textField.tag = index;
            btnOptionCheck.tag=index
        }
        else if(strFieldName == "SMSOptIn")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=false
            lblOther.hidden = true
            textField.tag = index;
            btnOptionCheck.tag=index
        }
        else if(strFieldName == "pushOptIn")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=true
            textField.hidden=true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=false
            lblOther.hidden = true
            textField.tag = index;
            btnOptionCheck.tag=index
        }
        else if(strFieldName == "Bonus")
        {
            textField.placeholder=strFieldName
            imgArrow.hidden=false
            textField.hidden=true
            viewContainGenders.hidden=true
            viewContainOptionCheckBox.hidden=true
            textField.userInteractionEnabled=false
            lblOther.tag=index
            lblOther.hidden = false
            
        }
    }
    
    // key board resign
    
    func keyBoardResign(sdender : UIButton) {
        
        self.delegate?.keyBoardResign(textField);
     
    }
    
    @IBAction func tapOption(sender: UIButton)
    {
        NSLog("sender tag --------- %d", sender.tag);
        if check1==true
        {
            check1=false
            btnOptionCheck.setImage(UIImage(named: "unChecked.png"), forState: .Normal)
            self.delegate?.arrayreplace(sender.tag,stringValue: "false", textFieldValue:textField.placeholder!)
        }
        else
        {
            check1=true
            btnOptionCheck.setImage(UIImage(named: "checked.png"), forState: .Normal)
            self.delegate?.arrayreplace(sender.tag,stringValue: "true", textFieldValue:textField.placeholder!)
        }
    }
}
