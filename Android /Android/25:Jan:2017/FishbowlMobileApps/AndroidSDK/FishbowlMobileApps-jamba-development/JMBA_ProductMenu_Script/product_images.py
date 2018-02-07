#!/usr/bin/env python

#
# Generate product outline file from Parse.com family, category and product lists
#

import ParsePy
import os
import re

ParsePy.APPLICATION_ID = "ZDoQmWb85Uif38XWJAnVlAmVYiDRnQfGfwbOlNkd"
ParsePy.MASTER_KEY = "P0jpdWxsu1jv7bWOPXMHb2M253dc3Is9hl59xVxT"

CloudFrontBaseUrl = "https://dgojkv04mcfoc.cloudfront.net"

productImages = {}

def loadProductImages():
    filenames = os.listdir("products")
    for filename in filenames:
        parts = re.findall(r'(\d{6})[^\d]+(\d+x\d+)', filename)
        if len(parts) > 0:
            productId = int(parts[0][0])
            size = parts[0][1]
            if productId in productImages:
                productImages[productId][size] = filename
            else:
                productImages[productId] = { size: filename }


def updateProducts():
    print("Loading products...")
    query = ParsePy.ParseQuery("Product")
    rows = query.fetch()
    for row in rows:
        if row.productId in productImages:
            print("Updating product: " + row.name)
            row.thumbImageUrl = CloudFrontBaseUrl + "/products/" + productImages[row.productId]["621x490"]
            row.orderImageUrl = CloudFrontBaseUrl + "/products/" + productImages[row.productId]["1242x698"]
            row.imageUrl      = CloudFrontBaseUrl + "/products/" + productImages[row.productId]["1242x911"]
            row.save()

loadProductImages()
updateProducts()
