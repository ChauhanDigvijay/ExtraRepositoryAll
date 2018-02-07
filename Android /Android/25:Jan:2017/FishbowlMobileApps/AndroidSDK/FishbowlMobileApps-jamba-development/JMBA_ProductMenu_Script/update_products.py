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

class ProductUploader:

    application = ""
    environment = ""
    productIds = []

    families = {}
    categories = {}
    productImages = {}

    def __init__(self, application, environment, productIds):
        import json
        with open('config.json') as config_file:
            configuration = json.load(config_file)

        self.application = application
        self.environment = environment
        self.productIds = productIds

        try:
            ParsePy.APPLICATION_ID = configuration[application][environment]["application_key"]
            ParsePy.MASTER_KEY = configuration[application][environment]["master_key"]
        except:
            print("Invalid configuration: " + environment + " (" + application + ")")
            exit(-1)

        print("---------------------------")
        print("Application: " + application)
        print("Environment: " + environment)
        print("---------------------------")

    def loadProductImages(self):
        filenames = os.listdir("products")
        for filename in filenames:
            parts = re.findall(r'(\d{6})[^\d]+(\d+x\d+|Kiosk)', filename)
            if len(parts) > 0:
                productId = int(parts[0][0])
                size = parts[0][1]
                if productId in self.productImages:
                    self.productImages[productId][size] = filename
                else:
                    self.productImages[productId] = { size: filename }

    def updateProductsFromCSV(self):
        with open('product_menu.csv', 'rb') as csvfile:
            reader = csv.DictReader(csvfile)
            for row in reader:
                if not row["Published"] or not row["Product ID"]:
                    continue
                if len(self.productIds) > 0 and not int(row["Product ID"]) in self.productIds:
                    continue
                print("Processing product: " + row["Product Name"] + " (" + row["Product ID"] + ")")
                self.updateProduct(row)

    def updateProduct(self, productInfo):
        product = self.loadEntityOrNew("Product", "productId", int(productInfo["Product ID"]))
        
        # Update fields
        product.productId = int(productInfo["Product ID"])
        product.name = productInfo["Product Name"]
        product.ingredients = productInfo["Simplified ingredients"]
        product.order = int(productInfo["Order"])
        product.featured = True if productInfo["Featured"] == "Yes" else False
        
        if self.application == "kiosk":
            product.published = True if productInfo["Kiosk Published"] == "Yes" else False
        else:
            product.published = True if productInfo["Published"] == "Yes" else False

        product.size1 = productInfo["Size 1"]
        product.size2 = productInfo["Size 2"]
        product.size3 = productInfo["Size 3"]
        product.size4 = productInfo["Size 4"]

        # Product images
        if product.productId in self.productImages:
            product.thumbImageUrl = CloudFrontBaseUrl + "/products/" + self.productImages[product.productId]["621x490"]
            product.orderImageUrl = CloudFrontBaseUrl + "/products/" + self.productImages[product.productId]["1242x698"]
            product.imageUrl      = CloudFrontBaseUrl + "/products/" + self.productImages[product.productId]["1242x911"]
            if "Kiosk" in self.productImages[product.productId]:
                product.kioskImageUrl = CloudFrontBaseUrl + "/products/" + self.productImages[product.productId]["Kiosk"]

        # Save product category, if new
        category = self.updateOrCreateCategory(productInfo)
        product.category = category
        product.family = category.family

        print("Saving product: " + product.name)
        product.save()

    def updateOrCreateCategory(self, productInfo):
        categoryName = productInfo["App Category"]
        if categoryName in self.categories:
            return self.categories[categoryName]

        category = self.loadEntityOrNew("ProductCategory", "name", categoryName)
        category.name = categoryName
        category.tagLine = productInfo["Category Tagline"]
        category.desc = productInfo["Category Description"]
        category.imageUrl = productInfo["Category Image Url"]
        category.order = int(productInfo["Order"])
        category.published = True

        # Save product family, if new
        family = self.updateOrCreateFamily(productInfo)
        category.family = family

        print("Saving product category: " + categoryName)
        category.save()

        self.categories[categoryName] = category
        return category

    def updateOrCreateFamily(self, productInfo):
        if self.application == "kiosk":
            familyName = productInfo["Kiosk Family"]
        else:
            familyName = productInfo["App Family"]
        
        if familyName in self.families:
            return self.families[familyName]

        family = self.loadEntityOrNew("ProductFamily", "name", familyName)
        family.name = familyName
        family.imageUrl = productInfo["Family Image Url"]
        family.order = int(productInfo["Order"])
        family.published = True

        print("Saving product family: " + familyName)
        family.save()

        self.families[familyName] = family
        return family

    def loadEntityOrNew(self, parseClass, entityIdFieldName, entityId):
        query = ParsePy.ParseQuery(parseClass)
        query.eq(entityIdFieldName, entityId)
        rows = query.fetch()
        if len(rows) > 0:
            return rows[0]

        print(parseClass + " with Id '" + str(entityId) + "' not found, adding new one...")
        return ParsePy.ParseObject(parseClass)



def main():
    parser = argparse.ArgumentParser(description='Update Jamba Juice products from CSV')
    parser.add_argument('-e', '--environment', choices=['staging', 'production'], required=True,
                   help='Environment to update (staging|production)')
    parser.add_argument('-a', '--application', choices=['mobile', 'kiosk'], default='mobile',
                   help='Application to update (mobile|kiosk) (default: mobile)')
    parser.add_argument('productIds', type=int, nargs='*',
                   help='Product IDs to be updated')

    args = parser.parse_args()

    uploader = ProductUploader(args.application, args.environment, args.productIds)
    uploader.loadProductImages()
    uploader.updateProductsFromCSV()


if __name__ == "__main__":
    main()

