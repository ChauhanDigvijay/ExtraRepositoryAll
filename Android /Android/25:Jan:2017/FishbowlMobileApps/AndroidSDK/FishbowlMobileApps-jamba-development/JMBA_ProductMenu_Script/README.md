# JMBA_ProductMenu_Script
Product Menu Import Script

## Usage

1. Run the import script
```
$ ./import_products.py
```

WARNING: This will override any existing products on the Parse.com product collection. Please ensure the `product_menu.csv` file contains the latest product menu and that it is accurate and contains no errors.

## Updating the products from the Master Product Menu sheet

1. Verify the products on the Master Product Sheet are correct (see https://docs.google.com/spreadsheets/d/1O_0eSm9ppGasvsiQ2jM727ftSqPGA8PfFCcU6tQmKKk/edit#gid=1648627668)
2. Download sheet as CSV and save as `product_menu.csv`
3. Commit the file
4. Run the import script as seen above


