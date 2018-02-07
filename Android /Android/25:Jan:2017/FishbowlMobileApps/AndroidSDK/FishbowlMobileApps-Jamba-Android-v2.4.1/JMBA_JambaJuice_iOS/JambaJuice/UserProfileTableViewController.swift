//
//  UserProfileTableViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/18/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD
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


//https://stackoverflow.com/questions/24066170/swift-ns-options-style-bitmask-enumerations/24066171#24066171
//http://natecook.com/blog/2014/07/swift-options-bitmask-generator/
struct InformationUpdated : OptionSet {
    let rawValue: Int
    init(rawValue: Int) { self.rawValue = rawValue }
    
    static var None: InformationUpdated { return InformationUpdated(rawValue:0) }
    static var FirstNameUpdated: InformationUpdated { return InformationUpdated(rawValue: 1 << 0) }
    static var LastNameUpdated: InformationUpdated { return InformationUpdated(rawValue: 1 << 1) }
    static var ProfileImage: InformationUpdated { return InformationUpdated(rawValue: 1 << 2) }
    static var EmailOptIn: InformationUpdated { return InformationUpdated(rawValue: 1 << 3) }
    static var SmsOptIn: InformationUpdated { return InformationUpdated(rawValue: 1 << 4) }
    static var PushOptIn: InformationUpdated { return InformationUpdated(rawValue: 1 << 5) }

}

private enum UserProfileSections: Int {
    case personalInformation
    case contactInformation
    case preferredStore
    case notifications
    case termsAndPrivacy
    case version
}

class UserProfileTableViewController : UITableViewController, UITextFieldDelegate, PagedImagesViewControllerDelegate {
    
    @IBOutlet weak var firstNameTextField: UITextField!
    @IBOutlet weak var lastNameTextField: UITextField!
    @IBOutlet weak var dateOfBirthTextField: UITextField!
    @IBOutlet weak var saveBarButtonItem: UIBarButtonItem!
    @IBOutlet weak var logoutButton: UIButton!
    @IBOutlet weak var preferredStoreLabel: UILabel!
    @IBOutlet weak var emailNotificationSwitch: UISwitch!
    @IBOutlet weak var textNotificationSwitch: UISwitch!
    @IBOutlet weak var pushNotificationSwitch: UISwitch!
    @IBOutlet weak var emailAddressLabel: UILabel!
    @IBOutlet weak var phoneNumberLabel: UILabel!
    
    @IBOutlet weak var versionLabel: UILabel!
    
    weak var pagedImagesViewController: PagedImagesViewController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.lightBlue)
        tableView.tableFooterView = UIView()
            
        
        saveBarButtonItem.isEnabled = false
        NotificationCenter.default.addObserver(self, selector: #selector(UserProfileTableViewController.userEmailChanged(_:)), name: NSNotification.Name(rawValue: JambaNotification.UserEmailAddressChanged.rawValue), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(UserProfileTableViewController.userPhoneNumberChanged(_:)), name: NSNotification.Name(rawValue: JambaNotification.UserPhoneNumberChanged.rawValue), object: nil)
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)
        
        
        // User preferred store changed
             NotificationCenter.default.addObserver(self, selector: #selector(UserProfileTableViewController.showUserPreferredStore), name: NSNotification.Name(rawValue: JambaNotification.PreferredStoreChanged.rawValue), object: nil)
        
        populateUserInfo()
        fetchUserUpdate()
        
        let year = Date().yearInGregorianCalendar()
        versionLabel.text = "Version \(UIApplication.versionNumber()).\(UIApplication.buildNumber())\n\nCopyright © \(year) Jamba, Inc."
        tableView.reloadData()
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    fileprivate func populateUserInfo() {
        
        if let user = UserService.sharedUser
        {
        firstNameTextField.text = user.firstName
        lastNameTextField.text = user.lastName
        if user.dateOfBirth != nil {
            dateOfBirthTextField.text = user.dateOfBirth!.dateString()
        }
        else {
            dateOfBirthTextField.text = ""
        }
        phoneNumberLabel.text = user.phoneNumber
        populateEmailAddress()
        emailNotificationSwitch.isOn = user.emailOptIn
        textNotificationSwitch.isOn = user.smsOptIn
        pushNotificationSwitch.isOn = user.pushOptIn
        //Should never really happen
            if user.favoriteStore != nil {
                preferredStoreLabel.text = "\(user.favoriteStore!.name)\n\(user.favoriteStore!.address)"
            }
            else {
                preferredStoreLabel.text = "No preferred store found."
            }
        }
    }
    
    fileprivate func fetchUserUpdate() {
        UserService.fetchUserUpdate { (error) -> Void in
            if error == nil {
                self.populateUserInfo()
                self.saveBarButtonItem.isEnabled = self.informationUpdated() != .None
            }
            else {
                log.error(error!.localizedDescription)
            }
        }
    }
    
    func userEmailChanged(_ notification: Notification) {
        populateEmailAddress()
    }
    
    func userPhoneNumberChanged(_ notification: Notification) {
        if let user = UserService.sharedUser {
        phoneNumberLabel.text = user.phoneNumber
        }
    }
    
    fileprivate func populateEmailAddress() {
       if let user = UserService.sharedUser
       {
        if user.emailAddress == nil {
            emailAddressLabel.text = "Email address not set."
        }
        else {
            emailAddressLabel.text = user.emailAddress
        }
        }
    }
    
    // MARK: Navigation
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        if segue.identifier == "ProfileImagesViewController" {
            pagedImagesViewController = (segue.destination as! PagedImagesViewController)
            if let user = UserService.sharedUser {
                pagedImagesViewController.loadIntialPageWithName(user.profileImageName)
            }
            pagedImagesViewController.delegate = self
        }
        else if segue.identifier == "PreferredStoreSegue" {
            let vc = segue.destination as! PreferredStoreViewController
            if let user = UserService.sharedUser {
            vc.store = user.favoriteStore
            }
        }
    }
    
    @IBAction func logout(_ sender: UIButton) {
        trackButtonPressWithName("Logout")
        logout()
    }
    
    @IBAction func saveData(_ sender: UIButton) {
        trackButtonPressWithName("Save")
        save()
    }
    
    fileprivate func save() {
        if validateFields() == false {
            return
        }
        let updatedInfo = informationUpdated()
        
        // Should never happen as save button is disabled if no changes to save.
        if updatedInfo == .None {
            dismissModalController()
            return
        }
        
        // Resign first responder & diable buttons
        view.endEditing(true)
        //By default all are nil. We pass only those that are updated.
        var firstName: String? = nil
        var lastName: String? = nil
        var profileImageName: String? = nil
        
        //Check if profile has changed and assign if yes
        var shouldFireProfileImageNotif = false
        if updatedInfo.contains(.ProfileImage) {
            profileImageName = pagedImagesViewController.currentImageName()
            shouldFireProfileImageNotif = true
        }
        //Check if user name and email changed and assign if yes
        var shouldFireUserFirstNameChangeNotif = false
        if updatedInfo.contains(.FirstNameUpdated) {
            firstName = firstNameTextField.text
            shouldFireUserFirstNameChangeNotif = true
        }
        if updatedInfo.contains(.LastNameUpdated) {
            lastName = lastNameTextField.text
        }
        
        //Make call & save
        SVProgressHUD.show(withStatus: "Saving profile...")
        SVProgressHUD.setDefaultMaskType(.clear)
        UserService.updateUserInfo(firstName, lastName: lastName, profileImageName: profileImageName, emailOptIn: emailNotificationSwitch.isOn, smsOptIn: textNotificationSwitch.isOn,dateOfBirth: nil, pushOpt:pushNotificationSwitch.isOn) { error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.saveBarButtonItem.isEnabled = true
                self.presentError(error)
                return
            }
            
            // Fire the needed notifications
            if shouldFireProfileImageNotif {
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.UserProfileImageChanged.rawValue), object: self)
            }
            if shouldFireUserFirstNameChangeNotif {
                NotificationCenter.default.post(name: Notification.Name(rawValue: JambaNotification.UserFirstNameChanged.rawValue), object: self)
            }
            
            // Populate info only if we were successful. Otherwise keep user's entered data.
            self.populateUserInfo()
            self.dismissModalController()
        }
    }
    
    @IBAction func textFieldEditingChanged(_ sender: UITextField) {
        saveBarButtonItem.isEnabled = informationUpdated() != .None
    }
    
    @IBAction func notificationSwitchValueChanged(_ sender: UISwitch) {
        if sender == pushNotificationSwitch{
            if pushNotificationSwitch.isOn{
                let appDelegate = UIApplication.shared.delegate as! AppDelegate
                let pushStatus = appDelegate.getPushNotificationEnableStatus()
                if pushStatus == true{
                    saveBarButtonItem.isEnabled = informationUpdated() != .None
                }else{
                    pushNotificationSwitch.isOn = false
                    self.presentOkAlert("Push Notification Service Disabled", message: "To enable push notification, please enable Push Notification Service on the Settings app.")
                }
            }else{
                 saveBarButtonItem.isEnabled = informationUpdated() != .None
            }
        }else{
            saveBarButtonItem.isEnabled = informationUpdated() != .None
        }
    }
    
    // MARK: Field validation
    
    fileprivate func validateFields() -> Bool {
        view.endEditing(true)
        // Validate first name
        if firstNameTextField.text == nil || firstNameTextField.text!.trim().isEmpty {
            presentOkAlert("First name required", message: "Please enter your first name") {
                self.firstNameTextField.becomeFirstResponder()
            }
            return false
        }
        
        // Validate last name
        if lastNameTextField.text == nil || lastNameTextField.text!.trim().isEmpty {
            presentOkAlert("Last name required", message: "Please enter your last name") {
                self.lastNameTextField.becomeFirstResponder()
            }
            return false
        }
        
        return true
    }
    
    fileprivate func informationUpdated() -> InformationUpdated {
        
        var infoUpdated = InformationUpdated.None
        
        if let user = UserService.sharedUser
        {
            if isUserInfoTextFieldUpdated(firstNameTextField, comparedToText: user.firstName) {
                infoUpdated = infoUpdated.union(.FirstNameUpdated)
            }
            if isUserInfoTextFieldUpdated(lastNameTextField, comparedToText: user.lastName) {
                infoUpdated = infoUpdated.union(.LastNameUpdated)
            }
            if pagedImagesViewController.currentImageName() != user.profileImageName {
                infoUpdated = infoUpdated.union(.ProfileImage)
            }
            if user.emailOptIn != emailNotificationSwitch.isOn {
                infoUpdated = infoUpdated.union(.EmailOptIn)
            }
            if user.smsOptIn != textNotificationSwitch.isOn {
                infoUpdated = infoUpdated.union(.SmsOptIn)
            }
            if user.pushOptIn != pushNotificationSwitch.isOn{
              infoUpdated = infoUpdated.union(.PushOptIn)
            }
        }
        return infoUpdated
    }
    
    fileprivate func isUserInfoTextFieldUpdated(_ textField: UITextField, comparedToText: String?) -> Bool {
        let toBeComparedText = comparedToText != nil ? comparedToText : ""
        return textField.text != toBeComparedText
    }
    
    
    // MARK: UITextFieldDelagate
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == firstNameTextField {
            return firstNameTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoFirstNameLimit)
        }
        else if textField == lastNameTextField {
            return lastNameTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoLastNameLimit)
        }
        return true
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        trackKeyboardReturn()
        if textField == firstNameTextField {
            lastNameTextField.becomeFirstResponder()
        }
        else if textField == lastNameTextField {
            save()
        }
        return true
    }
    
    // MARK: PagedImagesViewController
    
    func pagedImagesViewControllerDidChangePage(_ viewController: PagedImagesViewController) {
        saveBarButtonItem.isEnabled = informationUpdated() != .None
    }
    
    //MARK: TableView DataSource Delegate
    
    override func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 18)
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        switch UserProfileSections(rawValue: indexPath.section)! {
        case .preferredStore:
            //This is rather hacky fix. But static table view seems to behave wierdly
            //to Auto-Layout based height.
            let size = CGSize(width: tableView.frame.width - 100, height: 1000)//33*2 + 8 + 20//Horzontal Constraints + 6 for margin
            let textBoundingRect = (preferredStoreLabel.text as NSString?)?.boundingRect(with: size, options: NSStringDrawingOptions.usesLineFragmentOrigin, attributes: [NSFontAttributeName : preferredStoreLabel.font], context: nil)
            if textBoundingRect != nil {
                return textBoundingRect!.height + 23//8+9+6//Vertical Constraints + some space for margin
            }
            return 94
        case .version:
            if indexPath.row == 0 { // Version
                return 90
            }
            else if indexPath.row == 1 { // Logout
                return 50
            }
        default:
            break
        }
        return tableView.rowHeight
    }
    
    override func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        switch UserProfileSections(rawValue: section)! {
        case .contactInformation: return 5
        case .version: return 0
        default: break
        }
        return tableView.sectionHeaderHeight
    }
    
    //MARK: Log out
    
    fileprivate func logout() {
        if BasketService.sharedBasket?.products.count > 0 {
            //Present confirmation alert controller
            presentConfirmation("Log Out Confirmation", message: "Your existing basket will be lost upon logging out", buttonTitle: "Log out") { (confirmed) -> Void in
                if confirmed {
                    self.logoutAndDismiss()
                }
            }
        }
        else {
            presentConfirmation("Log Out Confirmation", message: "Do you want to continue?", buttonTitle: "Log out") { (confirmed) -> Void in
                if confirmed {
                    self.logoutAndDismiss()
                }
            }
        }
    }
    
    
    func logoutAndDismiss(){
        UserService.logoutUser {
            SVProgressHUD.show(withStatus: "Logging out...")
            SVProgressHUD.setDefaultMaskType(.clear)
            FishbowlApiClassService.sharedInstance.fishbowlLogout {
                SVProgressHUD.dismiss()
                UIApplication.inMainThread {
                    self.dismissModalController()
                    self.showInitialScreen()
                }
            }
        }
    }
    
    func showInitialScreen() {
        // Precaution delete fishbowl member details
        FishbowlApiClassService.sharedInstance.deleteFishbowlDetailsForLogin()
        let viewController = self.storyboard!.instantiateViewController(withIdentifier: "NonSignedInViewController")
        UIApplication.shared.keyWindow?.rootViewController = viewController;
    }
    
    func showUserPreferredStore(){
       fetchUserUpdate()
    }
    
 

}
