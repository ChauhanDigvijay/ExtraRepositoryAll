#!/usr/bin/env python

import csv
from subprocess import call

def downloadImages():
    with open('product_menu.csv', 'rb') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            if not row["Published"]:
                continue
            print("Downloading image for " + row["Product Name"])
            call(["curl", "-o", row["Product Name"] + ".jpg", row["Image"]])


downloadImages()
