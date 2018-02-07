
import UIKit
import SVProgressHUD
import OloSDK

class FavouriteOrderDetailViewController: UIViewController , UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var orderAgainButton: UIButton!
    @IBOutlet weak var removeFavoriteButton: UIButton!
    
    var favourite: FavouriteOrder!
    
    
    
    enum SeeMore :Int {
        case kSeeMoreNone = -1
        case kSeeMoreExpand = 0
        case kSeeMoreCollapse = 1
    }
    var seeMoreArray:[Int] = []
    
    override func viewDidLoad() {
        configureNavigationBar(.lightBlue)
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 94
        tableView.rowHeight = UITableViewAutomaticDimension
        
        fetchStoreIfNeededByRestaurantId()
        getProducts()
        
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        self.navigationItem.title = "Order Detail"
    }
    @IBAction func orderAgain(_ sender: UIButton) {
        trackButtonPress(sender)
        orderAgain()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //fetch store details, if it is not available (order History)
    //fetch store details, if it is not available (favourite)
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return favourite!.store == nil ? 2 : 3
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 1 {
            return favourite.products.count
        } else {
            return 1
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "FavoriteHeaderTableViewCell", for: indexPath) as! FavoriteHeaderTableViewCell
            cell.favoriteTitle.text = favourite.name
            return cell
        } else if indexPath.section == 1{
            let cell = tableView.dequeueReusableCell(withIdentifier: "FavoriteItemsOrderedTableViewCell", for: indexPath) as! FavoriteItemsOrderedTableViewCell
            
            var orderStatusProduct:OrderStatusProduct?
            var index = 0
            orderStatusProduct = favourite!.products[indexPath.row ]
            index = indexPath.row
            cell.productNameLabel.text = orderStatusProduct!.name.capitalized
            let spInstr = orderStatusProduct!.specialInstructions
            cell.modifiersLabel.text = spInstr
            cell.modifiersLabel.isHidden = false
            //            if spInstr.isEmpty {
            //                cell.modifiersLabel.isHidden = true
            //            } else {
            //                cell.modifiersLabel.text = spInstr
            //            }
            let costString = String(format: "$%.2f",  orderStatusProduct!.totalCost)
            cell.priceLabel.text = costString
            let optionNamesAlreadyIncludedInDescription = NSMutableOrderedSet()
            let optionSizeNamesAlreadyIncludedInDescription = NSMutableOrderedSet()
            
            for basketChoice in  orderStatusProduct!.choices {
                if !basketChoice.name.lowercased().hasPrefix("click here to") {//Filter out click here to...
                    //Check if we have already included a choice with same name
                    //If we haven't return the choice name and include it in the set for future.
                    if !optionNamesAlreadyIncludedInDescription.contains(basketChoice.name.uppercased()) || !optionSizeNamesAlreadyIncludedInDescription.contains(basketChoice.name.uppercased()) {
                        if !orderStatusProduct!.isSizeModifier(name: basketChoice.name) {
                            optionNamesAlreadyIncludedInDescription.add(basketChoice.name.uppercased())
                        } else {
                            optionSizeNamesAlreadyIncludedInDescription.add(basketChoice.name.uppercased())
                        }
                    }
                }
            }
            let sizeName = (optionSizeNamesAlreadyIncludedInDescription.array as! [String]).joined(separator: ", ")
            let optionName = (optionNamesAlreadyIncludedInDescription.array as! [String]).joined(separator: ", ")
            cell.modifiersLabel.text = optionName
            cell.sizeLabel.text = sizeName
            
            cell.seeMore.tag=index;
            if (seeMoreArray.count == index) {
                let noOfLines = BasketItemTableViewCell.lines(120,label: cell.modifiersLabel)
                if (noOfLines > BasketItemTableViewCell.defaultNoOfLines) {
                    seeMoreArray.insert(SeeMore.kSeeMoreCollapse.rawValue, at: index)
                    cell.modifiersLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.modifiersLabellHeightConstraint.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.setTitle("See More", for: UIControlState())
                    cell.seeMore.isHidden = false
                    cell.seeMoreBottomConstraintConstant.constant = 32
                } else {
                    cell.modifiersLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    if (cell.modifiersLabel.text!.isEmpty) || cell.modifiersLabel.text == ""{
                        cell.modifiersLabellHeightConstraint.constant = 0
                        cell.modifiersLabel.isHidden = true
                        
                    } else{
                        cell.modifiersLabellHeightConstraint.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                        cell.modifiersLabel.isHidden = false
                    }
                    seeMoreArray.insert(SeeMore.kSeeMoreNone.rawValue, at: index)
                    cell.seeMore.isHidden = true
                    cell.seeMoreBottomConstraintConstant.constant = 0
                }
            } else if (seeMoreArray.count > index){
                let lines = BasketItemTableViewCell.lines(120, label: cell.modifiersLabel)
                if (lines <= BasketItemTableViewCell.defaultNoOfLines) {
                    if (cell.modifiersLabel.text!.isEmpty) || cell.modifiersLabel.text == ""{
                        cell.modifiersLabellHeightConstraint.constant = 0
                    } else{
                        cell.modifiersLabellHeightConstraint.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    }
                    cell.seeMore.isHidden = true
                    cell.seeMoreBottomConstraintConstant.constant = 0
                } else if (seeMoreArray[index] == SeeMore.kSeeMoreCollapse.rawValue){
                    cell.modifiersLabel.numberOfLines = BasketItemTableViewCell.defaultNoOfLines
                    cell.modifiersLabellHeightConstraint.constant = CGFloat(BasketItemTableViewCell.defaultNoOfLines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.setTitle("See More", for: UIControlState())
                    cell.seeMore.isHidden = false
                    cell.seeMoreBottomConstraintConstant.constant = 32
                } else if (seeMoreArray[index] == SeeMore.kSeeMoreExpand.rawValue){
                    cell.modifiersLabel.numberOfLines = lines
                    cell.modifiersLabellHeightConstraint.constant = CGFloat(lines * BasketItemTableViewCell.defaultLineHeight)
                    cell.seeMore.setTitle("See Less", for: UIControlState())
                    cell.seeMore.isHidden = false
                    cell.seeMoreBottomConstraintConstant.constant = 32
                }
            }
            cell.selectionStyle = .none
            cell.layoutIfNeeded()
            return cell
        }
        if indexPath.section == 2 {
            var store:Store?
            store = favourite.store
            let cell = tableView.dequeueReusableCell(withIdentifier: "FavoriteStoreDetailTableViewCell", for: indexPath) as! FavoriteStoreDetailTableViewCell
            cell.storeNameAndAddress.text = "\(store!.name)\n\(store!.street)\n\(store!.city), \(store!.state), \(store!.zip)"
            return cell
        }
        let cell = tableView.dequeueReusableCell(withIdentifier: "FavouriteHeaderTableCell", for: indexPath)
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 20)
        }
    
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if section == 1  {
            return "Items Ordered"
        } else if section == 2 {
            return "Ordered at"
        }
        return nil
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if section == 0{
            return 0.0
        }
        return tableView.sectionHeaderHeight
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if (indexPath.section == 2){
            self.performSegue(withIdentifier: "FavourtiteStoreDetailViewSegue", sender: self)
        }
    }
    
    //     func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
    //        if indexPath.section == 1{
    //            retur
    //        }
    //    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "FavourtiteStoreDetailViewSegue"{
            let vc = segue.destination as! StoreViewController
            vc.myOrderViewController = true
            vc.store = favourite.store
        }
    }
    
    @IBAction func expandCollapseDescription(_ sender: UIButton) {
        if (seeMoreArray[sender.tag] == SeeMore.kSeeMoreCollapse.rawValue) {
            seeMoreArray[sender.tag] = SeeMore.kSeeMoreExpand.rawValue
            self.tableView.reloadData()
        } else if (seeMoreArray[sender.tag] == SeeMore.kSeeMoreExpand.rawValue) {
            seeMoreArray[sender.tag] = SeeMore.kSeeMoreCollapse.rawValue
            self.tableView.reloadData()
        }
    }
    
    func fetchStoreIfNeededByRestaurantId() {
        if favourite != nil && favourite.store == nil {
            SVProgressHUD.show()
            SVProgressHUD.setDefaultMaskType(.clear)
            StoreService.storeByRestaurantId(favourite.vendorId, callback: { (stores, location, error) in
                SVProgressHUD.dismiss()
                if error != nil {
                    log.error("Error:\(error!.localizedDescription)")
                    return
                }
                if stores.count == 0 {
                    log.error("Error: 0 stores returned")
                    return
                }
                self.favourite.store = stores[0]
                self.tableView.reloadData()
            })
        }
    }
    
    func orderAgain() {
        if favourite != nil{
            //update favourite store
            if favourite.store == nil {
                SVProgressHUD.show(withStatus: "Preparing for new order...")
                SVProgressHUD.setDefaultMaskType(.clear)
                StoreService.storeByRestaurantId(favourite.vendorId , callback: { (stores, location, error) in
                    SVProgressHUD.dismiss()
                    if error != nil {
                        self.presentError(error)
                        return
                    }
                    if stores.count == 0 {
                        self.presentError(NSError(description: "Unable to retrieve store information"))
                        return
                    }
                    let store = stores[0]
                    self.favourite.store = store
                    self.validateSameStoreForFavouriteOrder()
                })
            } else {
                self.validateSameStoreForFavouriteOrder()
            }
        }
    }
    //get favorite products
    func getProducts(){
        SVProgressHUD.show()
        SVProgressHUD.setDefaultMaskType(.clear)
        OloBasketService.createFromFave(favourite.id, callback: { (basket, error) in
            if error != nil {
                SVProgressHUD.dismiss()
                self.presentConfirmation("Error", message: "Please try again", buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed {
                        self.getProducts()
                    }
                    return
                })
                return
            } else {
                self.favourite.updateProducts(basket!)
            }
            self.tableView.reloadData()
        })
    }
    
    func validateSameStoreForFavouriteOrder() {
        if CurrentStoreService.sharedInstance.currentStore == nil {
            changeStoreAndStartOrderAgain()
        }
        else if favourite.store!.storeCode == CurrentStoreService.sharedInstance.currentStore!.storeCode {
            normalStartOrderAgain()
        }else{
            changeStoreConfirmation()
        }
    }
    
    func changeStoreAndStartOrderAgain() {
        SVProgressHUD.show(withStatus: "Preparing for new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        var store:Store?
        store = favourite.store!
        CurrentStoreService.sharedInstance.startNewOrder(store!, callback: { (status, error) in
            SVProgressHUD.dismiss()
            if (error != nil){
                self.presentError(error)
            }else {
                if(status == "Success"){
                    self.ReOrderFavouriteOrder()
                }
                else{
                    self.presentError(NSError.init(description: Constants.genericErrorMessage))
                }
            }
        })
    }
    
    func changeStoreAndReOrderFavouriteOrder() {
        SVProgressHUD.show(withStatus: "Preparing for new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        CurrentStoreService.sharedInstance.startNewOrder(self.favourite.store!, callback: { (status, error) in
            SVProgressHUD.dismiss()
            if (error != nil){
                self.presentError(error)
            }else {
                if(status == "Success"){
                    self.ReOrderFavouriteOrder()
                }
                else{
                    self.presentError(NSError.init(description: Constants.genericErrorMessage))
                }
            }
        })
    }
    
    func normalStartOrderAgain() {
        if BasketService.sharedBasket != nil && (BasketService.sharedBasket?.products.count)! > 0  { //Need to ask user
            presentConfirmation("Empty Basket", message: "Re-ordering this order will delete the current basket. Continue?", buttonTitle: "Delete Basket") { confirmed in
                if confirmed {
                    self.ReOrderFavouriteOrder()
                }else{
                    return
                }
            }
        }
        else{
            self.ReOrderFavouriteOrder()
        }
    }
    
    func changeStoreConfirmation() {
        if BasketService.sharedBasket != nil && (BasketService.sharedBasket?.products.count)! > 0  { //Need to ask user
            
            // Precaution for crash
            var storeName = ""
            if favourite != nil && favourite.store != nil{
                storeName = favourite.store!.name
            }
            
            presentConfirmation("Start Order", message: " Starting a new order will empty the basket and change the store to \(storeName). Continue?", buttonTitle: "Start Order") { confirmed in
                if confirmed {
                    self.changeStoreAndReOrderFavouriteOrder()
                }
            }
            return
        }
        var storeName = ""
        storeName = favourite.store!.name
        presentConfirmation("Start Order", message: "Start a new order at \(storeName)?", buttonTitle: "Ok") { confirmed in
            if confirmed {
                self.changeStoreAndReOrderFavouriteOrder()
            }
        }
    }
    func ReOrderFavouriteOrder()  {
        self.orderAgainButton.isEnabled = false
        SVProgressHUD.show(withStatus: "Starting new order...")
        SVProgressHUD.setDefaultMaskType(.clear)
        BasketService.reOrderFavouriteOrder(favourite) { (basket, error) in
            self.orderAgainButton.isEnabled = true
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentError(error)
                return
            }
            //Dismiss
            self.dismissModalController()
            //And Open Basket Screen
            BasketFlagViewController.sharedInstance.openBasketVC()
        }
    }
    @IBAction func removeFavouriteOrder() {
        removeFavoriteButton.isEnabled = false
        SVProgressHUD.show(withStatus: "Removing...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.removeFavouriteOrder(favourite!.id, callback: { (error) in
            self.removeFavoriteButton.isEnabled = true
            SVProgressHUD.dismiss()
            if error != nil {
                self.presentConfirmation("Error", message: error!.localizedDescription, buttonTitle: "Retry", callback: { (confirmed) in
                    if confirmed {
                        self.removeFavouriteOrder()
                    }
                })
            } else {
                self.presentOkAlert("Success", message: "Order successfully removed from favorites.", callback: {
                    self.popViewController()
                })
            }
        })
    }
    
}






/*
 // MARK: - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
 // Get the new view controller using segue.destinationViewController.
 // Pass the selected object to the new view controller.
 }
 */

