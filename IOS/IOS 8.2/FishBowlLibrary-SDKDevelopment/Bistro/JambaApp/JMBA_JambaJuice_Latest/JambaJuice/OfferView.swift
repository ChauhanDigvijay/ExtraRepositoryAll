//
//  OfferView.swift
//  JambaJuice
//
//  Created by Puneet  on 4/4/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import UIKit


protocol ProtocolDelegate:class {
    func myMethod(controller:UIView)
}


class OfferView: UIView {
    
    var timer :NSTimer?
    
        weak var delegate:ProtocolDelegate?
    
    
    
    func addCustomView() {
        

        
        let bgImage: UIImageView!
     
        let vwBg =  UIView(frame:CGRectMake(0, 0, self.frame.size.width, 100))
        vwBg.backgroundColor = UIColor(hex: Constants.offerBgColor)
        self.addSubview(vwBg)
        
        UIApplication.sharedApplication().statusBarHidden = true


        
        let yCodd : CGFloat = 4.0
        
        
         let image = UIImage(named:"JambaJuiceLogo")!
        bgImage = UIImageView(image: image)
        bgImage.frame = CGRect(x: 8, y: 25, width: 65, height: 47)
        vwBg.addSubview(bgImage)
        
 
        
        let lblSep = UILabel(frame: CGRectMake(80, yCodd+12, 1, 70))
        lblSep.backgroundColor = UIColor.whiteColor()
        self.addSubview(lblSep)
        
        let lblOffer = UILabel(frame: CGRectMake(90, yCodd+10, self.frame.size.width-115, 21))
        lblOffer.font = UIFont(name: "Archer-Bold", size: 15)
        lblOffer.numberOfLines = 3
        lblOffer.lineBreakMode = NSLineBreakMode.ByWordWrapping
        lblOffer.textColor = UIColor(hex: Constants.jambaPinkColor)
        let appdelgate = UIApplication.sharedApplication().delegate as! AppDelegate
        lblOffer.text = appdelgate.offerTitle
        vwBg.addSubview(lblOffer)
        lblOffer.sizeToFit()
        
        

        
        let myString:String = "Your offer is available in the Rewards & Promotions section, and can be redeemed when you checkout"
        
/* If it a mutable String

        //        var myMutableString = NSMutableAttributedString()
        //myMutableString = NSMutableAttributedString(string: myString, attributes: [NSFontAttributeName:UIFont(name: "Helvetica", size: 13.0)!])
        
      //  myMutableString.addAttribute(NSForegroundColorAttributeName, value:UIColor(hex: Constants.jambsRewardsTitleColor), range: NSRange(location:27,length:89))

    If it a mutable String*/


        // set label Attribute
        
        let lblBasket = UILabel(frame: CGRectMake(90, yCodd+10+lblOffer.frame.size.height-5, self.frame.size.width-135, 70))
        lblBasket.font = UIFont(name: "Helvetica", size: 11)
        lblBasket.numberOfLines = 4;
        lblBasket.textColor = UIColor(hex: Constants.jambsRewardsTitleColor)
        lblBasket.text = myString
        vwBg.addSubview(lblBasket)
        
        vwBg.frame = CGRectMake(0, 0, self.frame.size.width, yCodd+lblOffer.frame.size.height + lblBasket.frame.size.height)
        
        
        let btnClose = UIButton()
        btnClose.setImage(UIImage(named: "btnClose"), forState: UIControlState.Normal)
        btnClose.frame = CGRectMake(self.frame.size.width-50, (yCodd+10+lblOffer.frame.size.height + lblBasket.frame.size.height)/2, 45, 45)
        btnClose.addTarget(self, action: #selector(OfferView.btnClosePressed), forControlEvents: .TouchUpInside)
        vwBg.addSubview(btnClose)
        
    }
    
    func btnClosePressed()
    {
        UIView.animateWithDuration(0.4, animations: {() -> Void in
            self.frame = CGRectMake(0, -130, self.frame.size.width, self.frame.size.height)
                  })
        
        UIApplication.sharedApplication().statusBarHidden = false
        
        //Swift <2.2 selector syntax
        timer = NSTimer.scheduledTimerWithTimeInterval(2.0, target: self, selector: #selector(OfferView.update), userInfo: nil, repeats: false)
        

    }
    
    // must be internal or public.
    internal func update() {
        // Something cool
        
        self .removeFromSuperview()
        timer?.invalidate()

    }
    

    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        if let touch = touches.first {
            let position :CGPoint = touch.locationInView(self)
            
            if position.y < 100
            {
                
                delegate?.myMethod(self)
                
//                if ([self.delegate respondsToSelector:@selector(myMethod:)]) {
//                    [self.delegate myMethod:self text:@"you Text"];
//                }
//                
                UIView.animateWithDuration(0.4, animations: {() -> Void in
                    self.frame = CGRectMake(0, -130, self.frame.size.width, self.frame.size.height)
                    UIApplication.sharedApplication().statusBarHidden = false

            })
            }
            
        }
    
    }


    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */

}
