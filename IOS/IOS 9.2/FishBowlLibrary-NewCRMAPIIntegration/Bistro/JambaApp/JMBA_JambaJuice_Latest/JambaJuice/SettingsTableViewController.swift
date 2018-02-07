//
//  SettingsTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 8/24/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

class SettingsTableViewController: UITableViewController {
    
    @IBOutlet weak var versionLabel: UILabel!
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.LightBlue)
        tableView.tableFooterView = UIView()
        
        let year = NSDate().yearInGregorianCalendar()
        versionLabel.text = "Version \(UIApplication.versionNumber()).\(UIApplication.buildNumber())\n\nCopyright © \(year) Jamba, Inc."
    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    override func tableView(tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        let headerView = view as? UITableViewHeaderFooterView
        headerView?.textLabel?.textColor = UIColor(hex: Constants.jambaDarkGrayColor)
        headerView?.textLabel?.font = UIFont(name: Constants.archerMedium, size: 18)
    }
    
}