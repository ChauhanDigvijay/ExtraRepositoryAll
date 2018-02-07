//
//  RewardService.swift
//  JambaKiosk
//
//  Created by Eneko Alonso on 11/19/15.
//  Copyright © 2015 Jamba Juice. All rights reserved.
//

import Foundation
import OloSDK

typealias RewardsCallback = (rewards: [Reward], error: NSError?) -> Void
typealias RewardErrorCallback = (error: NSError?) -> Void

class RewardService {

    static let sharedInstance = RewardService()
    private(set) var availableRewards: [Reward] = []

    /// Retrieve available rewards for the current basket / restaurant
    /// We need to ensure loyalty accounts are linked, which we do by posting the phonenumber to loyaltyschemes
    ///  1. Get loyatly schemes, if membership is null, post phone number and get loyalty schemes
    ///  2. Get rewards available for that membership id
    func loadAvailableRewards(callback: RewardsCallback) {
        guard let basketId = BasketService.sharedInstance.currentBasket?.basketId else {
            callback(rewards: [], error: NSError(description: "Basket not found."))
            return
        }
        guard let userPhone = UserService.sharedUser?.phoneNumber else {
            callback(rewards: [], error: NSError(description: "User phone number not found"))
            return
        }

        OloBasketService.loyaltySchemes(basketId) { (loyaltySchemes, error) -> Void in
            if error != nil {
                callback(rewards: [], error: error)
                return
            }

            // Get Jamba Juice scheme
            let filteredSchemes = loyaltySchemes.filter { $0.name == "Jamba Insider Rewards" }
            guard let jambaJuiceScheme = filteredSchemes.first else {
                callback(rewards: [], error: nil)  // No rewards available for the user, might be an error
                return
            }

            // Check if user is member yet, if not, make it a member and try again
            guard let membershipId = jambaJuiceScheme.membership?.id else {
                self.connectRewardsPhoneNumber(basketId, schemeId: jambaJuiceScheme.id, phoneNumber: userPhone, callback: callback)
                return  // Nothing to do, let the callback roll again
            }

            self.loadLoyaltyRewards(basketId, membershipId: membershipId, callback: callback)
        }
    }

    private func connectRewardsPhoneNumber(basketId: String, schemeId: Int64, phoneNumber: String, callback: RewardsCallback) {
        OloBasketService.configureLoyaltySchemes(basketId, schemeId: schemeId, phoneNumber: phoneNumber) { (error) -> Void in
            if error != nil {
                callback(rewards: [], error: error)
                return
            }
            self.loadAvailableRewards(callback) // Try again
        }
    }

    private func loadLoyaltyRewards(basketId: String, membershipId: Int64, callback: RewardsCallback) {
        OloBasketService.loyaltyRewards(basketId, membershipId: membershipId) { (loyaltyRewards, error) -> Void in
            if error != nil {
                callback(rewards: [], error: error)
                return
            }
            self.availableRewards = loyaltyRewards.map { Reward(oloReward: $0) }
            callback(rewards: self.availableRewards, error: nil)
        }
    }

    /// Apply new reward to basket
    /// If rewards were already applied, remove them
    func applyReward(reward: Reward, callback: RewardErrorCallback) {
        if validateReward(reward, callback: callback) == false {
            return
        }

        guard let basket = BasketService.sharedInstance.currentBasket else {
            callback(error: NSError(description: "Basket not found."))
            return
        }

        // Verify if basket has already a reward, if so, remove it and try again
        if basket.appliedRewards.count > 0 {
            removeRewards { error in
                if error != nil {
                    callback(error: error)
                    return
                }
                self.applyReward(reward, callback: callback)
            }
            return
        }

        // Add reward to basket
        OloBasketService.applyReward(basket.basketId, membershipId: reward.membershipId!, rewardReference: reward.reference!) { (basket, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            BasketService.sharedInstance.updateBasket(Basket(oloBasket: basket!))
            callback(error: nil)
            AnalyticsService.trackEvent("order_ahead", action: "apply_reward", label: reward.name)
        }
    }

    private func validateReward(reward: Reward, callback: RewardErrorCallback) -> Bool {
        if BasketService.sharedInstance.currentBasket == nil {
            callback(error: NSError(description: "Basket not found."))
            return false
        }
        if reward.reference == nil {
            callback(error: NSError(description: "Missing reward reference"))
            return false
        }
        if reward.membershipId == nil {
            callback(error: NSError(description: "Missing reward membership Id"))
            return false
        }
        return true
    }

    /// Remove any applied rewards
    func removeRewards(callback: RewardErrorCallback) {
        guard let basket = BasketService.sharedInstance.currentBasket else {
            callback(error: NSError(description: "Basket not found."))
            return
        }
        guard let reward = basket.appliedRewards.first else {
            callback(error: nil)
            return
        }
        guard let rewardId = reward.rewardId else {
            callback(error: NSError(description: "Missing reward Id"))
            return
        }

        OloBasketService.removeReward(basket.basketId, rewardId: rewardId) { (basket, error) -> Void in
            if error != nil {
                callback(error: error)
                return
            }
            // Log before removing next, if any
            AnalyticsService.trackEvent("order_ahead", action: "remove_reward", label: reward.name)
            if basket!.appliedRewards.count > 0 {
                self.removeRewards(callback)
                return
            }
            BasketService.sharedInstance.updateBasket(Basket(oloBasket: basket!))
            callback(error: nil)
        }
    }

    func reset() {
        availableRewards = []
    }

}
