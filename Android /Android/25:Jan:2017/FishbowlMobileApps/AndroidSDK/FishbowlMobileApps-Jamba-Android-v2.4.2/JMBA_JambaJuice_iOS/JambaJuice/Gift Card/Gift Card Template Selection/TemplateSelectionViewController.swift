//
//  TemplateSelectionViewController.swift
//  JambaGiftCards
//
//  Created by vThink on 5/23/16.
//  Copyright © 2016 Fishbowl. All rights reserved.
//

import UIKit
import InCommSDK

import SVProgressHUD

//MARK: TemplateSelectionViewController Delegate
protocol TemplateSelectionViewControllerDelegate: class {
    func cardSelected(_ value:InCommBrandCardImage)
}
class TemplateSelectionViewController: UIViewController,UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout {
    
    @IBOutlet weak var templateCollectionView   :UICollectionView!
    @IBOutlet weak var continueButton           :UIButton!
    fileprivate var selectedCardImage               :InCommBrandCardImage?
    fileprivate var cardImages                      :[InCommBrandCardImage]! = []
    var selectedBrandImageCode                  :String?
    weak var delegate                           : TemplateSelectionViewControllerDelegate?
    
    override func viewDidLoad() {
        getAllTemplateDesigns()
        
        templateCollectionView.collectionViewLayout.invalidateLayout()
        super.viewDidLoad()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    //MARK: - Collection view data source
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.cardImages.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = templateCollectionView.dequeueReusableCell(withReuseIdentifier: "GiftCardTempalteCollectionViewCell", for: indexPath) as! GiftCardTempalteCollectionViewCell
        
        var incommBrandCardImage:InCommBrandCardImage?
        incommBrandCardImage = cardImages[indexPath.item]
        cell.incommBrandCardImage = incommBrandCardImage
        cell.updateSelectionImageView(cell)
        if(selectedBrandImageCode != nil || selectedBrandImageCode == ""){
            if(selectedBrandImageCode == cell.incommBrandCardImage?.imageCode){
                cell.updateSelection()
            }
        }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return collectionViewCellSize()
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let cell = collectionView.cellForItem(at: indexPath)
        if let giftCardCell = cell as! GiftCardTempalteCollectionViewCell?{
            selectedCardImage = giftCardCell.incommBrandCardImage
            
            delegate?.cardSelected(selectedCardImage!)
            popViewController()
        }
    }
    
    func collectionViewCellSize() -> CGSize{
        let collectionViewWidth = self.view.frame.size.width
        var collectionViewCellWidth:CGFloat!
        var collectionViewCellHeight:CGFloat!
        collectionViewCellWidth = (collectionViewWidth-60)/2
        collectionViewCellHeight = collectionViewCellWidth * CGFloat(GiftCardAppConstants.GiftCardRatioHeight)
        return CGSize(width: collectionViewCellWidth,height: collectionViewCellHeight)
    }
    
    //MARK: - IBAction methods
    @IBAction func close(){
        popViewController()
    }
    
    @IBAction func continueButtonPressed(_ sender:UIButton){
        delegate?.cardSelected(selectedCardImage!)
        popViewController()
    }
    
    //MARK: - API calls
    func getAllTemplateDesigns() {
        if let GiftCardBrand = InCommGiftCardBrandDetails.sharedInstance.brand{
            self.cardImages = GiftCardBrand.cardImages
            self.templateCollectionView.reloadData()
        }
        else{
            SVProgressHUD.show(withStatus: "Fetching Template...")
            SVProgressHUD.setDefaultMaskType(.clear)
            InCommGiftCardBrandDetails.sharedInstance.loadBrandDetails({ (brand, error) in
                SVProgressHUD.dismiss()
                if error != nil{
                    self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry") {(confirmed) -> Void in
                        if confirmed{
                            self.getAllTemplateDesigns()
                        }
                    }
                    return
                }
                else{
                    InCommGiftCardBrandDetails.sharedInstance.reSetBrandDetails(brand!)
                    self.cardImages = brand!.cardImages
                    self.templateCollectionView.reloadData()
                }
            })
        }
    }
    
}
