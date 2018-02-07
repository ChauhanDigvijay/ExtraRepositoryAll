//
//  AuthenticatedUserViewController.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/17/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import UIKit

protocol AuthenticatedUserViewControllerDelegate {
    func applyRewardToOrder(reward: Reward)
    func removeRewardFromOrder()
}

class AuthenticatedUserViewController: UIViewController {

    @IBOutlet var youHaveRewardsLabel: UILabel!
    @IBOutlet var rewardNameLabel: UILabel!
    @IBOutlet var rewardStatusSwitch: UISwitch!

    var delegate: AuthenticatedUserViewControllerDelegate?
    private var reward: Reward!

    override func viewDidLoad() {
        super.viewDidLoad()
        guard let firstName = UserService.sharedUser?.firstName else {
            fatalError("Could not load logged in user")
        }
        guard let reward = RewardService.sharedInstance.availableRewards.first else {
            fatalError("Could not load reward")
        }

        self.reward = reward
        youHaveRewardsLabel.text = "You have rewards available, \(firstName)!"
        rewardNameLabel.text = reward.name
        rewardStatusSwitch.on = (BasketService.sharedInstance.currentBasket?.appliedRewards.first != nil)
    }


    // MARK: User actions

    @IBAction func toggleRewardsSwitch(sender: UISwitch) {
        trackButtonPressWithName("Reward Switch")
        SessionExpirationService.sharedInstance.trackUserActivity()
        if sender.on {
            delegate?.applyRewardToOrder(reward)
        } else {
            delegate?.removeRewardFromOrder()
        }
    }

}
