//
//  SubstitutionsViewController.swift
//  JambaJuice
//
//  Created by vThink on 3/25/16.
//  Copyright © 2016 Jamba Juice. All rights reserved.
//

import Foundation
import UIKit
// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func < <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l < r
  case (nil, _?):
    return true
  default:
    return false
  }
}

// FIXME: comparison operators with optionals were removed from the Swift Standard Libary.
// Consider refactoring the code to use the non-optional operators.
fileprivate func > <T : Comparable>(lhs: T?, rhs: T?) -> Bool {
  switch (lhs, rhs) {
  case let (l?, r?):
    return l > r
  default:
    return rhs < lhs
  }
}


//# MARK: - Controller Delegate

// Substitution delegate used when apply the substitution
protocol SubstitutionsViewControllerDelegate:class{
    func closeModelScreen(_ optionIds:[Int64],removeSubstitutionModels:[RemoveSubstitutionModel]);
}


class SubstitutionsViewController: UIViewController,UITableViewDataSource,UITableViewDelegate,UISearchBarDelegate,UIScrollViewDelegate{
    
    //# MARK: - Variable Declaration
    
    weak var delegate: SubstitutionsViewControllerDelegate!
    var customizeProductModifier:StoreMenuProductModifier!
    
    // Remove Section Options
    var removeSectionOptions:[StoreMenuProductModifierOption]=[]
    
    // Sections
    var substitutionSections:[SubstitutionModel]=[]
    
    // Selected Option ID's
    var selectedOptionIds = Set<Int64>()
    
    // Remove Substitution Models
    var removeSubstitutionModels : [RemoveSubstitutionModel] = []
    
    // Previous Selected Cell IndexPath
    var prevSelectedCellIndexPath: IndexPath?
    
    // Substitution Modifier
    var substitutionModifier:StoreMenuProductModifier?
    
    // Remove Modifier
    var removeModifier:StoreMenuProductModifier?
    
    var customizeItOptionID: Int64!
    
    // Constants
    let removeSectionName       = "remove"
    let substitutionSectionName = "subs"
    let substitutionOptionName  = "select substitution"
    
    @IBOutlet weak var tableView: UITableView!
    
    //# MARK: - Default Methods
    
    override func viewDidLoad() {
        super.viewDidLoad();
        tableView.estimatedSectionHeaderHeight = 60
        tableView.estimatedRowHeight = 60
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.sectionHeaderHeight = UITableViewAutomaticDimension
        prepareSubstitutionData()
        tableView.reloadData()
        StatusBarStyleManager.pushStyle(.lightContent, viewController: self)
        
        let window = UIApplication.shared.delegate!.window!!
        BasketFlagViewController.sharedInstance.view.frame = CGRect(x: window.bounds.width - BasketFlagViewController.basketFlagViewSize.width, y: 24, width: BasketFlagViewController.basketFlagViewSize.width, height: BasketFlagViewController.basketFlagViewSize.height)

    }
    
    override func viewWillDisappear(_ animated: Bool) {
        
        let window = UIApplication.shared.delegate!.window!!
        BasketFlagViewController.sharedInstance.view.frame = CGRect(x: window.bounds.width - BasketFlagViewController.basketFlagViewSize.width, y: 120, width: BasketFlagViewController.basketFlagViewSize.width, height: BasketFlagViewController.basketFlagViewSize.height)
        
        
    }
    
    //# MARK: - Tableview Delegates
    
    // TableView Delegate
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // Return two different section for header and content
        let substitutionModel:SubstitutionModel!
        substitutionModel = substitutionSections[section]
        if(substitutionModel.sectionName.lowercased().hasPrefix(substitutionOptionName) && substitutionModel.expandable == true){
            if (substitutionModel.option?.modifiers.count == 0) {
                return 0
            }
            return (substitutionModel.option?.modifiers[0].options.count)!
        }
        
        if(substitutionModel.sectionName.lowercased().hasPrefix(removeSectionName)){
            print(removeSectionOptions.count)
            return removeSectionOptions.count
        }
        return 0
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return substitutionSections.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let optionCell = tableView.dequeueReusableCell(withIdentifier: "SubstitutionOptionTableViewCell") as! SubstitutionOptionTableViewCell
        
        let substitutionModel:SubstitutionModel!
        substitutionModel = substitutionSections[indexPath.section]
        // Return only substitution option cell
        if(substitutionModel.sectionName.lowercased().hasPrefix(substitutionOptionName) && substitutionModel.expandable == true){
            let storeMenuProductOption:StoreMenuProductModifierOption!
            storeMenuProductOption = substitutionModel.option?.modifiers[0].options[indexPath.row]
            optionCell.optionName.text = storeMenuProductOption.name
            optionCell.checkImageView.isHidden = true
            optionCell.optionId = storeMenuProductOption.id
            optionCell.indexPath = indexPath
            optionCell.backgroundColor = UIColor(red: 242.0/255.0, green: 242.0/255.0, blue: 242.0/255.0, alpha: 1.0)
            if(selectedOptionIds.contains(optionCell.optionId)){
                optionCell.checkImageView.isHidden = false
            }
            return optionCell
        }
        
        // Return only remove option cell
        if(substitutionModel.sectionName.lowercased().hasPrefix(removeSectionName)){
            let storeMenuProductOption:StoreMenuProductModifierOption!
            storeMenuProductOption = removeSectionOptions[indexPath.row]
            optionCell.optionName.text = storeMenuProductOption.name
            optionCell.checkImageView.isHidden = true
            optionCell.optionId = storeMenuProductOption.id
            optionCell.indexPath = indexPath
            optionCell.backgroundColor = UIColor.white
            if(selectedOptionIds.contains(optionCell.optionId)){
                optionCell.checkImageView.isHidden = false
            }
            return optionCell
        }
        
        assert(false, "unexpected index path")
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        let substitutionModel:SubstitutionModel!
        substitutionModel = substitutionSections[section]
        // Height for header section
        if(substitutionModel.sectionName.lowercased().hasPrefix(substitutionSectionName) || substitutionModel.sectionName.lowercased().hasPrefix(removeSectionName)){
            return 40.0
        }
        // Height for content section
        return 60.0
    }
    
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerCell=tableView.dequeueReusableCell(withIdentifier: "SubstitutionHeaderSectionTableViewCell") as! SubstitutionHeaderSectionTableViewCell
        
        let substitutionModel:SubstitutionModel!
        substitutionModel = substitutionSections[section]
        
        // Check header substitution section or not
        if(substitutionModel.sectionName.lowercased().hasPrefix(substitutionSectionName)){
            headerCell.headerLabel.text = "I'd like to substitute..."
            headerCell.isUserInteractionEnabled = false
            return headerCell
        }
        
        // Check header remove section or not
        if(substitutionModel.sectionName.lowercased().hasPrefix(removeSectionName)){
            headerCell.headerLabel.text = "Can you remove..."
            headerCell.isUserInteractionEnabled = false
            return headerCell
        }
        
        let contentCell = tableView.dequeueReusableCell(withIdentifier: "SubstitutionContentSectionTableViewCell") as! SubstitutionContentSectionTableViewCell
        
        // Check substitution header section or not
        if(substitutionModel.sectionName.lowercased().hasPrefix(substitutionOptionName)){
            let imageName = "expand-icon"
//            let imageName = "less-button"
//            if(substitutionModel.expandable == true){
//                imageName = "less-button"
//            }

//            substitutionModel.option!.name = "jbdgngnfbiodgfjhijikldfgjsjiohjsiojlbdfnndbfgkshnohndikofgshihdiosiobdfghniohiodsiosdioghiodiohfiohdtioidhsiodhsihsdtiiodhsiodfhiohiodghiodfhiohdfiohdfiods   Data"
            // Default section name
            contentCell.contentLabel.text = substitutionModel.option!.name
            contentCell.checkImageView.isHidden = true
            contentCell.contentLabelLeadingSpace.constant=20    //20 is the default left space
            
            // Check whether any substitution is selected or not if selected change text of section
            for option in removeSubstitutionModels {
                if(option.removeOptionId == substitutionModel.option?.id){
                    
                    let removeOptionFont:UIFont = UIFont.init(name: "Helvetica Neue", size: 17.0)!
                    let substitutionFont:UIFont = UIFont.init(name: "Helvetica Neue", size: 12.0)!
                    let removeOptionColor:UIColor = UIColor(red: 87.0/255.0, green: 39.0/255.0, blue: 0.0/255.0, alpha: 1.0)
                    let substitutionColor:UIColor = UIColor(red: 246.0/255.0, green: 150.0/255.0, blue: 49.0/255.0, alpha: 1.0)
                    
                    let removeOptionString = substitutionModel.option!.name + "\n"
                    let substitutionString = "And " + option.substitutionName
                    
                    let removeOptionAttribute = [NSFontAttributeName: removeOptionFont, NSForegroundColorAttributeName: removeOptionColor]
                    let removeOptionAttributeString = NSMutableAttributedString(string:removeOptionString, attributes:removeOptionAttribute)
                    let substitutionAttribute =  [NSFontAttributeName:substitutionFont, NSForegroundColorAttributeName: substitutionColor]
                    let substitutionAttributeString = NSAttributedString(string:substitutionString, attributes:substitutionAttribute)
                    removeOptionAttributeString.append(substitutionAttributeString)
                    contentCell.contentLabel.attributedText = removeOptionAttributeString
            
                    break
                }
            }
            
            // Substitution content cell expand collapse image change and action gesture added
            contentCell.extendCollapseImageView.image = UIImage(named: imageName)
            contentCell.tag = section
            let cellTapRecognizer = UITapGestureRecognizer(target: self, action:#selector(SubstitutionsViewController.modifierOptionClicked(_:)))
            cellTapRecognizer.numberOfTapsRequired = 1
            cellTapRecognizer.numberOfTouchesRequired = 1
            contentCell.addGestureRecognizer(cellTapRecognizer)
            return contentCell
        }
        return contentCell
    }
    
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let cell = tableView.cellForRow(at: indexPath)
        if let optionCell = cell as? SubstitutionOptionTableViewCell {
            if((optionCell.checkImageView?.isHidden) == true){
                optionCell.checkImageView?.isHidden=false
                
                // Check single substitution selected or not if remove already any substituion is selected for the option
                let substitutionModel:SubstitutionModel!
                substitutionModel = substitutionSections[indexPath.section]
                if(substitutionModel.sectionName.lowercased().hasPrefix(substitutionOptionName)){
                    var storeMenuProductOption:StoreMenuProductModifierOption!
                    var storeProductOptions:[StoreMenuProductModifierOption]=[]
                    
                    storeProductOptions =  (substitutionModel.option?.modifiers[0].options)!
                    if(selectedOptionIds.contains((substitutionModel.option?.id)!)){
                        removeSelectedOptionId((substitutionModel.option?.id)!)
                        removeRemoveSubstitutionModel((substitutionModel.option?.id)!)
                    }
                    for (i,option) in storeProductOptions.enumerated() {
//                    for(var i=0; i<storeProductOptions.count; i++){
                        storeMenuProductOption = option
                        if(selectedOptionIds.contains(storeMenuProductOption.id)){
                            removeSelectedOptionId(storeMenuProductOption.id)
                            let cell = tableView.cellForRow(at: IndexPath(row: i, section: indexPath.section)) as? SubstitutionOptionTableViewCell
                            if(cell != nil){
                                cell!.checkImageView.isHidden = true
                            }
                        }
                    }
                    
                    addSelectedOptionId((substitutionModel.option?.id)!)
                    let removeSubstitutionModel = RemoveSubstitutionModel(removeOptionId:substitutionModel.option?.id, substitutionName:optionCell.optionName.text)
                    
                    addRemoveSubstitutionModel(removeSubstitutionModel)
                    addSelectedOptionId(optionCell.optionId)
                }
                else{
                    addSelectedOptionId(optionCell.optionId)
                }
            }
            else{
                
                // Deselect for selected option
                optionCell.checkImageView?.isHidden=true
                removeSelectedOptionId(optionCell.optionId)
                let substitutionModel:SubstitutionModel!
                substitutionModel = substitutionSections[indexPath.section]
                if(substitutionModel.sectionName.lowercased().hasPrefix(substitutionOptionName)){
                    removeSelectedOptionId(substitutionModel.option!.id)
                    removeRemoveSubstitutionModel((substitutionModel.option?.id)!)
                }
            }
            
        }
        tableView.reloadData()
    }
    
    //# MARK: - Close Button Click Action
    
    // Close button clicked close substitution screen
    @IBAction func closeScreen(_ sender: AnyObject) {
        StatusBarStyleManager.pushStyle(.default, viewController: self)
        dismissModalController()
    }
    
    //# MARK: - Data Preparation
    // reformat the input data for UI
    func prepareSubstitutionData(){
        // Split Substitution and Remove Modifier
        let customizeOption = customizeProductModifier?.options.first
        if(customizeOption != nil){
            customizeItOptionID = customizeOption?.id
        }
        if(customizeOption != nil){
            for modifier in customizeOption!.modifiers {
                if(modifier.name.lowercased().hasPrefix(substitutionSectionName)){
                    substitutionModifier = modifier
                }
                if(modifier.name.lowercased().hasPrefix(removeSectionName)){
                    removeModifier = modifier
                }
                // based on json structure
                //if the option has modifier then it is substitution modifier Otherwise Remove Modifier
//                if(modifier.options.count > 0){
//                    if (modifier.options[0].modifiers.count >= 1)   {
//                        substitutionModifier = modifier
//                    }
//                    if(modifier.options[0].modifiers.count == 0){
//                        removeModifier = modifier
//                    }
//                }
            }
        }
        
        // can i change section
        if (substitutionModifier != nil && substitutionModifier?.options != nil && substitutionModifier?.options.count>0) {
            // Section Model Preparation
            let subst = SubstitutionModel(sectionName:substitutionSectionName, option:nil,expandable:false )
            substitutionSections.append(subst)
            
            // Substitution Section Options
            for option in substitutionModifier!.options {
                let subst = SubstitutionModel(sectionName:substitutionOptionName, option:option, expandable:false )
                substitutionSections.append(subst)
            }
        }
        
        // can you remove section
        if (removeModifier != nil && removeModifier?.options != nil && removeModifier?.options.count>0){
            // Remove Header Section
            let remove = SubstitutionModel(sectionName:removeSectionName, option:nil, expandable:false )
            substitutionSections.append(remove)
            
            // Remove Section Option
            for option in removeModifier!.options {
//            for(var i=0; i<removeModifier?.options.count; i += 1){
                removeSectionOptions.append(option)
            }
        }
    }
    
    //# MARK: - Remove Sections Expand/Collapse to view options
    
    // Expand collapse when remove option clicked
    func modifierOptionClicked(_ gestureRecognizer: UITapGestureRecognizer) {
        let sectionIndexValue = gestureRecognizer.view?.tag
        if( substitutionSections[sectionIndexValue!].expandable == true){
            substitutionSections[sectionIndexValue!].expandable = false
        }
        else{
            substitutionSections[sectionIndexValue!].expandable = true
        }
        tableView.reloadData()
    }
    
    //# MARK: - Add/Remove Options
    
    // Add selected option
    func addSelectedOptionId(_ optionId: Int64) {
        selectedOptionIds.insert(optionId)
    }
    
    // Remove Deselect option
    func removeSelectedOptionId(_ optionId: Int64) {
        if selectedOptionIds.contains(optionId) {
            selectedOptionIds.remove(optionId)
        }
    }
    
    // Add remove option only
    func addRemoveSubstitutionModel(_ subRemoveModel: RemoveSubstitutionModel){
        removeSubstitutionModels.append(subRemoveModel)
    }
    
    // Remove remove option
    func removeRemoveSubstitutionModel(_ optionId: Int64){
        for (i,option) in removeSubstitutionModels.enumerated() {
            if(option.removeOptionId == optionId){
                removeSubstitutionModels.remove(at: i)
                break
            }
        }
    }
    
    //# MARK: - Apply Substitution Button Clicked
    
    // Close the screen when substitution applied
    @IBAction func applySubstitution(_ sender:UIButton){
//        if(selectedOptionIds.isEmpty){
//            presentOkAlert("Selection Required", message: "Please select any one customize option for apply substitute")
//            return
//        }
        var selectedOptionIdsAry = Array(selectedOptionIds)
        if selectedOptionIdsAry.count>0 {
            if selectedOptionIdsAry.count == 1 && selectedOptionIdsAry[0] == customizeItOptionID {
                selectedOptionIds.removeAll()
                selectedOptionIdsAry = Array(selectedOptionIds)
            }else{
                selectedOptionIds.insert(customizeItOptionID)
                selectedOptionIdsAry = Array(selectedOptionIds)
            }
        }
        
    
        var itemId = ""
        var itemName = ""
        for (index,option) in removeSubstitutionModels.enumerated(){
            if index != removeSubstitutionModels.count - 1{
                itemId = itemId + "\(option.removeOptionId);"
                itemName = itemName + "\(option.substitutionName);"
            }else{
                  break
            }
        }
        
        // To track fishbowl events
        var removedOptions:[StoreMenuProductModifierOption] = []
        for removeOption in removeSectionOptions{
            for selectedOptionId in selectedOptionIds{
                if removeOption.id == selectedOptionId{
                    removedOptions.append(removeOption)
                }
            }
        }
        
        if removedOptions.count > 0{
            if let lastSubstituteOption = removeSubstitutionModels.last{
                itemId = itemId + "\(lastSubstituteOption.removeOptionId);"
                itemName = itemName + "\(lastSubstituteOption.substitutionName);"
            }
            for (index,removedOption) in removedOptions.enumerated(){
                if index == removedOptions.count - 1{
                    itemId = itemId + "\(removedOption.id)"
                    itemName = itemName + "\(removedOption.name)"
                }else{
                    itemId = itemId + "\(removedOption.id);"
                    itemName = itemName + "\(removedOption.name);"
                }
            }
        }else{
            if let lastSubstituteOption = removeSubstitutionModels.last{
                itemId = itemId + "\(lastSubstituteOption.removeOptionId)"
                itemName = itemName + "\(lastSubstituteOption.substitutionName)"
            }
        }
   
        
        FishbowlApiClassService.sharedInstance.submitMobileAppEvent(itemId, item_name: itemName, event_name: "CHECKOUT_WITHMODIFIERS")
        
        delegate?.closeModelScreen(selectedOptionIdsAry, removeSubstitutionModels: removeSubstitutionModels)
        dismissModalController()
    }
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if scrollView.contentOffset.y < Constants.scrollViewThresholdToDismissModal {
            trackScreenEvent("close_screen", label: "drag_down")
            dismissModalController()
        }
    }
    
    
}
