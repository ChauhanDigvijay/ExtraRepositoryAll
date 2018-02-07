#!/usr/bin/env python

#
# Load product CSV file
# Update exisitng products and add new ones. Will not delete products from Parse.com
#

import ParsePy
import csv
import os
import re
import argparse


CloudFrontBaseUrl = "https://dgojkv04mcfoc.cloudfront.net"

def loadConfiguration(environment):
    import json
    with open('config.json') as config_file:
        configuration = json.load(config_file)

    if configuration == None or not environment in ["staging", "production"]:
        print("Invalid environment: " + environment)
        exit(-1)

    ParsePy.APPLICATION_ID = configuration[environment]["application_key"]
    ParsePy.MASTER_KEY = configuration[environment]["master_key"]

def updateCardsFromCSV():
    with open('card_designs.csv', 'rb') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            if not row["Card ID"]:
                continue
            updateCard(row)

def updateCard(cardInfo):
    print("Loading card: " + cardInfo["Card ID"])
    card = loadCardOrNew(cardInfo["Card ID"])
    
    # Update fields
    card.imageCode = cardInfo["Card ID"]
    card.incommImageUrl = cardInfo["InComm URL"]
    if cardInfo["Image"] != None and cardInfo["Image"] != "":
        card.imageUrl = CloudFrontBaseUrl + "/cards/" + cardInfo["Image"]
    else:
        card.imageUrl = None
    card.published = cardInfo["Published"] == "Yes"

    print("Saving card: " + card.imageCode)
    card.save()

def loadCardOrNew(cardId):
    query = ParsePy.ParseQuery("GiftCardDesign")
    query.eq("imageCode", cardId)
    rows = query.fetch()
    if len(rows) > 0:
        return rows[0]

    print("Card not found, adding new one...")
    return ParsePy.ParseObject("GiftCardDesign")

def main():
    parser = argparse.ArgumentParser(description='Update Jamba Juice cards from CSV')
    parser.add_argument('environment', type=str, nargs=1,
                   help='Environment to update (staging|production)')

    args = parser.parse_args()

    loadConfiguration(args.environment[0])
    updateCardsFromCSV()


if __name__ == "__main__":
    main()
