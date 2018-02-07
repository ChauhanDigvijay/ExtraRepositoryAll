//
//  UserProfileTableViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/18/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import SVProgressHUD

//https://stackoverflow.com/questions/24066170/swift-ns-options-style-bitmask-enumerations/24066171#24066171
//http://natecook.com/blog/2014/07/swift-options-bitmask-generator/
struct InformationUpdated : OptionSetType {
    let rawValue: Int
    init(rawValue: Int) { self.rawValue = rawValue }
    
    static var None: InformationUpdated { return InformationUpdated(rawValue:0) }
    static var FirstNameUpdated: InformationUpdated { return InformationUpdated(rawValue: 1 << 0) }
    static var LastNameUpdated: InformationUpdated { return InformationUpdated(rawValue: 1 << 1) }
    static var ProfileImage: InformationUpdated { return InformationUpdated(rawValue: 1 << 2) }
    static var EmailOptIn: InformationUpdated { return InformationUpdated(rawValue: 1 << 3) }
    static var SmsOptIn: InformationUpdated { return InformationUpdated(rawValue: 1 << 4) }
    static var pushOptIn: InformationUpdated { return InformationUpdated(rawValue: 1 << 5) }

}

private enum UserProfileSections: Int {
    case PersonalInformation
    case ContactInformation
    case PreferredStore
    case Notifications
    case TermsAndPrivacy
    case Version
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
    @IBOutlet weak var emailAddressLabel: UILabel!
    @IBOutlet weak var phoneNumberLabel: UILabel!
    
    @IBOutlet weak var versionLabel: UILabel!
    
    weak var pagedImagesViewController: PagedImagesViewController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.LightBlue)
        tableView.tableFooterView = UIView()
            
        
        saveBarButtonItem.enabled = false
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(UserProfileTableViewController.userEmailChanged(_:)), name: JambaNotification.UserEmailAddressChanged.rawValue, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(UserProfileTableViewController.userPhoneNumberChanged(_:)), name: JambaNotification.UserPhoneNumberChanged.rawValue, object: nil)
        UITextField.appearance().tintColor = UIColor(hex: Constants.jambaOrangeColor)
        populateUserInfo()
        fetchUserUpdate()
        
        let year = NSDate().yearInGregorianCalendar()
        versionLabel.text = "Version \(UIApplication.versionNumber()).\(UIApplication.buildNumber())\n\nCopyright © \(year) Jamba, Inc."
        tableView.reloadData()
    }
    
    deinit {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    private func populateUserInfo() {
        
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
        emailNotificationSwitch.on = user.emailOptIn
        textNotificationSwitch.on = user.smsOptIn
        //Should never really happen
        if user.favoriteStore != nil {
            preferredStoreLabel.text = "\(user.favoriteStore!.name)\n\(user.favoriteStore!.address)"
        }
        else {
            preferredStoreLabel.text = "No preferred store found."
        }
        }
    }
    
    private func fetchUserUpdate() {
        UserService.fetchUserUpdate { (error) -> Void in
            if error == nil {
                self.populateUserInfo()
                self.saveBarButtonItem.enabled = self.informationUpdated() != .None
            }
            else {
                log.error(error!.localizedDescription)
            }
        }
    }
    
    func userEmailChanged(notification: NSNotification) {
        populateEmailAddress()
    }
    
    func userPhoneNumberChanged(notification: NSNotification) {
        if let user = UserService.sharedUser {
        phoneNumberLabel.text = user.phoneNumber
        }
    }
    
    private func populateEmailAddress() {
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
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        super.prepareForSegue(segue, sender: sender)
        if segue.identifier == "ProfileImagesViewController" {
            pagedImagesViewController = (segue.destinationViewController as! PagedImagesViewController)
            if let user = UserService.sharedUser {
                pagedImagesViewController.loadIntialPageWithName(user.profileImageName)
            }
            pagedImagesViewController.delegate = self
        }
        else if segue.identifier == "PreferredStoreSegue" {
            let vc = segue.destinationViewController as! PreferredStoreViewController
            if let user = UserService.sharedUser {
            vc.store = user.favoriteStore
            }
        }
    }
    
    @IBAction func logout(sender: UIButton) {
        trackButtonPressWithName("Logout")
        logout()
    }
    
    @IBAction func saveData(sender: UIButton) {
        trackButtonPressWithName("Save")
        save()
    }
    
    private func save() {
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
        SVProgressHUD.showWithStatus("Saving profile...", maskType: .Clear)
        UserService.updateUserInfo(firstName, lastName: lastName, profileImageName: profileImageName, emailOptIn: emailNotificationSwitch.on, smsOptIn: textNotificationSwitch.on, dateOfBirth: nil) { error in
            SVProgressHUD.dismiss()
            if error != nil {
                self.saveBarButtonItem.enabled = true
                self.presentError(error)
                return
            }
            
            // Fire the needed notifications
            if shouldFireProfileImageNotif {
                NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.UserProfileImageChanged.rawValue, object: self)
            }
            if shouldFireUserFirstNameChangeNotif {
                NSNotificationCenter.defaultCenter().postNotificationName(JambaNotification.UserFirstNameChanged.rawValue, object: self)
            }
            
            // Populate info only if we were successful. Otherwise keep user's entered data.
            self.populateUserInfo()
            self.dismissModalController()
        }
    }
    
    @IBAction func textFieldEditingChanged(sender: UITextField) {
        saveBarButtonItem.enabled = informationUpdated() != .None
    }
    
    @IBAction func notificationSwitchValueChanged(sender: UISwitch) {
        saveBarButtonItem.enabled = informationUpdated() != .None
    }
    
    // MARK: Field validation
    
    private func validateFields() -> Bool {
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
    
    private func informationUpdated() -> InformationUpdated {
        
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
        if user.emailOptIn != emailNotificationSwitch.on {
            infoUpdated = infoUpdated.union(.EmailOptIn)
        }
        if user.smsOptIn != textNotificationSwitch.on {
            infoUpdated = infoUpdated.union(.SmsOptIn)
        }
     
        }
        return infoUpdated
    }
    
    private func isUserInfoTextFieldUpdated(textField: UITextField, comparedToText: String?) -> Bool {
        let toBeComparedText = comparedToText != nil ? comparedToText : ""
        return textField.text != toBeComparedText
    }
    
    
    // MARK: UITextFieldDelagate
    
    
    func textField(textField: UITextField, shouldChangeCharactersInRange range: NSRange, replacementString string: String) -> Bool {
        if textField == firstNameTextField {
            return firstNameTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoFirstNameLimit)
        }
        else if textField == lastNameTextField {
            return lastNameTextField.shouldChangeInRange(range, replacementString: string, maxAllowedLength: Constants.spendGoLastNameLimit)
        }
        return true
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
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
    
    func pagedImagesViewControllerDidChangePage(viewController: PagedImagesViewController) {
        saveBarButtonItem.enabled = informationUpdated() != .None
    }
    
    //MARK: TableView DataSource Delegate
    
    override func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont(name: Constants.archerMedium, size: 18)
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        switch UserProfileSections(rawValue: indexPath.section)! {
        case .PreferredStore:
            //This is rather hacky fix. But static table view seems to behave wierdly
            //to Auto-Layout based height.
            let size = CGSizeMake(tableView.frame.width - 100, 1000)//33*2 + 8 + 20//Horzontal Constraints + 6 for margin
            let textBoundingRect = (preferredStoreLabel.text as NSString?)?.boundingRectWithSize(size, options: NSStringDrawingOptions.UsesLineFragmentOrigin, attributes: [NSFontAttributeName : preferredStoreLabel.font], context: nil)
            if textBoundingRect != nil {
                return textBoundingRect!.height + 23//8+9+6//Vertical Constraints + some space for margin
            }
            return 94
        case .Version:
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
    
    override func tableView(tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        switch UserProfileSections(rawValue: section)! {
        case .ContactInformation: return 5
        case .Version: return 0
        default: break
        }
        return tableView.sectionHeaderHeight
    }
    
    //MARK: Log out
    
    private func logout() {
        if BasketService.sharedBasket?.products.count > 0 {
            //Present confirmation alert controller
            presentConfirmation("Log Out Confirmation", message: "Your existing basket will be lost upon logging out", buttonTitle: "Log out") { (confirmed) -> Void in
                if confirmed {
                    self.dismissModalController()
                    UserService.logoutUser()
                    self.showInitialScreen()
                }
            }
        }
        else {
            self.dismissModalController()
            UserService.logoutUser()
            showInitialScreen()
        }
    }
    
    func showInitialScreen() {
        let viewController = self.storyboard!.instantiateViewControllerWithIdentifier("NonSignedInViewController")
        UIApplication.sharedApplication().keyWindow?.rootViewController = viewController;
    }
    
}
