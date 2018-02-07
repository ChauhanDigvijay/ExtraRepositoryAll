//
//  FeedbackTypeTableViewController.swift
//  JambaJuice
//
//  Created by Taha Samad on 8/25/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation

class FeedbackTypeTableViewController: UITableViewController {
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        trackScreenView()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavigationBar(.lightBlue)
        tableView.tableFooterView = UIView()
    }
    
    deinit {
        StatusBarStyleManager.popStyle(self)
    }
    
    //MARK: TableView Delegate
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "FeedbackVC", sender: self)
    }
    
    //MARK: --
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let indexPath = tableView.indexPathForSelectedRow {
            if let feedbackType = FeedbackType(index: UInt(indexPath.row)) {
                (segue.destination as! FeedbackViewController).feedback.feedbackType = feedbackType
            }
            else {
                assert(false, "Unexpected feedback type row")
            }
        }
    }
}
