#!/usr/bin/env python

#
# Load product CSV file
# Import products, catgories and families to Parse.com
#

import ParsePy
import csv

print "USE UPDATE PRODUCTS INSTEAD"
exit(-1)


ParsePy.APPLICATION_ID = "ZDoQmWb85Uif38XWJAnVlAmVYiDRnQfGfwbOlNkd"
ParsePy.MASTER_KEY = "P0jpdWxsu1jv7bWOPXMHb2M253dc3Is9hl59xVxT"

families = {}
categories = {}

def loadProducts():
    with open('product_menu.csv', 'rb') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            if not row["Published"] or not row["Product ID"]:
                continue
            saveProduct(row)


def saveProduct(productInfo):
    product = ParsePy.ParseObject("Product")
    product.productId = int(productInfo["Product ID"])
    product.name = productInfo["Product Name"]
    product.ingredients = productInfo["Simplified ingredients"]
    product.order = int(productInfo["Order"])
    product.featured = True if productInfo["Featured"] == "Yes" else False
    product.published = True if productInfo["Published"] == "Yes" else False
    product.size1 = productInfo["Size 1"]
    product.size2 = productInfo["Size 2"]
    product.size3 = productInfo["Size 3"]
    product.size4 = productInfo["Size 4"]

    # Save product category, if new
    category = saveCategory(productInfo)
    product.category = category
    product.family = category.family

    print("Saving new product: " + product.name)
    product.save()


def saveCategory(productInfo):
    categoryName = productInfo["App Category"]
    if categoryName in categories:
        return categories[categoryName]

    category = ParsePy.ParseObject("ProductCategory")
    category.name = categoryName
    category.tagLine = productInfo["Category Tagline"]
    category.desc = productInfo["Category Description"]
    category.imageUrl = productInfo["Category Image Url"]
    category.order = int(productInfo["Order"])
    category.published = True

    # Save product family, if new
    family = saveFamily(productInfo)
    category.family = family

    print("Saving new product category: " + categoryName)
    category.save()

    categories[categoryName] = category
    return category


def saveFamily(productInfo):
    familyName = productInfo["App Family"]
    if familyName in families:
        return families[familyName]

    family = ParsePy.ParseObject("ProductFamily")
    family.name = familyName
    family.imageUrl = productInfo["Family Image Url"]
    family.order = int(productInfo["Order"])
    family.published = True

    print("Saving new product family: " + familyName)
    family.save()

    families[familyName] = family
    return family


def resetCollections():
    classes = ["ProductFamily", "ProductCategory", "Product"]
    for className in classes:
        print("Deleting " + className)
        items = ParsePy.ParseQuery(className).fetch()
        for item in items:
            item.delete()


# resetCollections()
#loadProducts()
