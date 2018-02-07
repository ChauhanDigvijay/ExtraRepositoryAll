//
//  TermsAndPrivacyTableViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 5/14/15.
//  Copyright (c) 2015 Jamba Juice. All rights reserved.
//

import UIKit
import HDK

private typealias TermsUrl = (url: String, title: String)

class TermsAndPrivacyTableViewController: UITableViewController {
    
    @IBOutlet var leftBarButtonItem: UIBarButtonItem!
    
    private let urls = [
        [
            (url: Constants.jambaPrivacyUrl, title: "Privacy Policy"),
            (url: Constants.jambaTermsUrl, title: "Legal")
        ],
        [
            (url: Constants.spendGoPrivacyUrl, title: "Privacy Policy"),
            (url: Constants.spendGoTermsUrl, title: "Terms and Conditions")
        ],
        [
            (url: Constants.oloPrivacyUrl, title: "Privacy Policy"),
            (url: Constants.oloTermsUrl, title: "User Agreement")
        ]
    ]
    private var destinationUrl: TermsUrl?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.LightBlue)
        
        tableView.estimatedRowHeight = 50
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.tableFooterView = UIView()
        
        // Close vs. back button
        if navigationController?.viewControllers.count > 1 {
            leftBarButtonItem.image = UIImage(named: "back-button")
        } else {
            leftBarButtonItem.image = UIImage(named: "close-button")
        }
    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    @IBAction func backOrClose(sender: AnyObject) {
        if navigationController?.viewControllers.count > 1 {
            popViewController()
        } else {
            dismissModalController()
        }
    }
    
    override func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont.init(name: Constants.archerMedium, size: 20)
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        if urls.count > indexPath.section && urls[indexPath.section].count > indexPath.row {
            destinationUrl = urls[indexPath.section][indexPath.row]
            performSegueWithIdentifier("PrivacyAndTermsSegue", sender: self)
        }
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "PrivacyAndTermsSegue" && destinationUrl != nil {
            let vc = segue.destinationViewController as! WebViewController
            vc.linkToLoad = destinationUrl!.url
            vc.title = destinationUrl!.title
            destinationUrl = nil
        }
    }
    
}
