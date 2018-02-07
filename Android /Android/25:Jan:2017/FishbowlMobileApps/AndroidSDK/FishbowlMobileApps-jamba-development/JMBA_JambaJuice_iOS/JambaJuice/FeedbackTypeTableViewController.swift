//
//  FeedbackTypeTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 8/25/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

class FeedbackTypeTableViewController: UITableViewController {
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.LightBlue)
        tableView.tableFooterView = UIView()
    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    //MARK: TableView Delegate
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        performSegueWithIdentifier("FeedbackVC", sender: self)
    }
    
    //MARK: --
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if let indexPath = tableView.indexPathForSelectedRow {
            if let feedbackType = FeedbackType(index: UInt(indexPath.row)) {
                (segue.destinationViewController as! FeedbackViewController).feedback.feedbackType = feedbackType
            }
            else {
                assert(false, "Unexpected feedback type row")
            }
        }
    }
}
