//
//  UpSellProductImagesCollectionTableViewCell.swift
//  AutoScrollCollectionView
//
//  Created by VT010 on 10/25/17.
//  Copyright © 2017 VT010. All rights reserved.
//

import UIKit

class UpSellProductImagesCollectionTableViewCell: UITableViewCell,UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout  {

    var timer:Timer?
    
    var upSellProductImages: [UpSellProductsImages]{
        return UpSellProductsService.sharedInstance.upSellProductImages
    }
    
    var upSellConfig:UpSellConfig{
        return UpSellProductsService.sharedInstance.upSellConfig
    }
    
    @IBOutlet weak var productImagesCollectionView:UICollectionView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    //MARK: - Update CollectionView()
    func setTimer(){
        if self.upSellProductImages.count > 0 {
            stopTimer()
            self.timer = Timer.scheduledTimer(timeInterval: Double(self.upSellConfig.rotation_int), target: self, selector: #selector(self.scrollToNextCell), userInfo: nil, repeats: true)
        }
    }
    
    @objc func scrollToNextCell(){
        //get cell size
        let cellSize = CGSize(width: self.frame.width, height: self.frame.height);
        
        //get current content Offset of the Collection view
        let contentOffset = productImagesCollectionView.contentOffset;
        
        if productImagesCollectionView.contentSize.width <= productImagesCollectionView.contentOffset.x + cellSize.width
        {
            productImagesCollectionView.scrollRectToVisible(CGRect(x:0, y:contentOffset.y, width:cellSize.width, height:cellSize.height), animated: true);
            
        } else {
            productImagesCollectionView.scrollRectToVisible(CGRect(x:contentOffset.x + cellSize.width, y:contentOffset.y, width:cellSize.width, height:cellSize.height), animated: true);
        }
    }
        
        
        /* @objc func scrollToNextCell(){
         if self.x < self.upSellProductImages.count {
         let indexPath = IndexPath(item: x, section: 0)
         self.productImagesCollectionView.scrollToItem(at: indexPath, at: .centeredHorizontally, animated: true)
         self.x = self.x + 1
         } else {
         self.x = 0
         self.productImagesCollectionView.scrollToItem(at: IndexPath(item: 0, section: 0), at: .centeredHorizontally, animated: false)
         }
         } */
        
     func stopTimer() {
            timer?.invalidate()
            timer = nil
        }
        
        func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
            return CGSize(width: self.frame.width, height: self.frame.height)
        }
        
        func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
            return upSellProductImages.count
        }
        
        func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "UpSellProductImagesCollectionViewCell", for: indexPath) as! UpSellProductImagesCollectionViewCell
            let image = self.upSellProductImages[indexPath.row]
            var imageUrl = image.campaignImageURL
            if imageUrl.isEmpty{
                imageUrl = image.defaultImageURL
            }
            cell.update(imageUrl)
            return cell
        }
}
