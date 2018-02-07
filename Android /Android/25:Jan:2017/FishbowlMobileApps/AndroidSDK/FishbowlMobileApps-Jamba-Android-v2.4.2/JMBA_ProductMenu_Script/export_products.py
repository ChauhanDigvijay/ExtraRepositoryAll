#!/usr/bin/env python

#
# Generate product outline file from Parse.com family, category and product lists
#

import ParsePy

ParsePy.APPLICATION_ID = "ZDoQmWb85Uif38XWJAnVlAmVYiDRnQfGfwbOlNkd"
ParsePy.MASTER_KEY = "P0jpdWxsu1jv7bWOPXMHb2M253dc3Is9hl59xVxT"

families = []
familyHash = {}
categoryHash = {}

def queryFamilies():
    print("Loading families...")
    query = ParsePy.ParseQuery("ProductFamily")
    query.order("order")
    rows = query.fetch()
    for row in rows:
        print("Family: " + row.name)
        row.categories = []
        familyHash[row.name] = row
        families.append(row)

def queryCategories():
    print("Loading categories...")
    query = ParsePy.ParseQuery("ProductCategory")
    query.order("order")
    rows = query.fetch()
    for row in rows:
        print("Category: " + row.name + ", family: " + row.family.name)
        row.products = []
        categoryHash[row.name] = row
        familyHash[row.family.name].categories.append(row)

def queryProducts():
    print("Loading products...")
    query = ParsePy.ParseQuery("Product")
    query.order("order")
    rows = query.fetch()
    for row in rows:
        print("Product: " + row.name + ", category: " + row.category.name + ", family: " + row.category.family.name)
        categoryHash[row.category.name].products.append(row)

def dumpOutline():
    print("Generating outline...")
    for family in families:
        print("- " + family.name)
        for category in familyHash[family.name].categories:
            print("-- " + category.name + " (" + category.tagLine + ")")
            for product in category.products:
                print("--- " + product.name)


queryFamilies()
queryCategories()
queryProducts()
dumpOutline()
