//
//  TermsAndConditionsTableViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/9/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

private typealias TermsUrl = (url: String, title: String)

class TermsAndConditionsTableViewController: UITableViewController {

    @IBOutlet var versionLabel: UILabel!

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
        tableView.tableFooterView = UIView()
        versionLabel.text = "Version \(UIApplication.versionNumber()).\(UIApplication.buildNumber())"
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }

    override func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont(name: Constants.archerMedium, size: 20)
    }

    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        tableView.deselectRowAtIndexPath(indexPath, animated: true)
        if urls.count > indexPath.section && urls[indexPath.section].count > indexPath.row {
            destinationUrl = urls[indexPath.section][indexPath.row]
            performSegueWithIdentifier("ShowWebView", sender: self)
        }
    }

    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "ShowWebView" && destinationUrl != nil {
            let vc = segue.destinationViewController as? WebViewController
            vc?.urlString = destinationUrl?.url
            vc?.title = destinationUrl?.title
            destinationUrl = nil
        }
    }

}
