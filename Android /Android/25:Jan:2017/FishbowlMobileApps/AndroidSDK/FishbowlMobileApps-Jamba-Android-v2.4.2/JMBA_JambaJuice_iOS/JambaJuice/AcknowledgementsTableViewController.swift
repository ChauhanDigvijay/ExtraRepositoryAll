//
//  AcknowledgementsTableViewController.swift
//  JambaJuice
//
//  Created by Eneko Alonso on 8/26/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

class AcknowledgementsTableViewController: UITableViewController {

    var libraries: [[String:String]] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.tableFooterView = UIView()
        tableView.estimatedRowHeight = 100
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.reloadData() // Autolayout
        
        let path = NSBundle.mainBundle().pathForResource("ThirdPartyLibraries", ofType: "plist")
        libraries = NSArray(contentsOfFile: path!) as! [[String:String]]
        libraries.sortInPlace { $0["name"] < $1["name"] }
    }

    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
        tableView.reloadData() // Autolayout
    }

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return libraries.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let library = libraries[indexPath.row]
        let cell = tableView.dequeueReusableCellWithIdentifier("LibraryCell") as! ThirdPartyLibraryTableViewCell
        cell.libraryNameLabel.text = library["name"]
        cell.libraryVersionLabel.text = "Version " + (library["version"] ?? "(unknown)")
        cell.libraryLicenseLabel.text = library["license"]
        return cell
    }

}
