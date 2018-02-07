package com.fishbowl.apps.olo;

import com.android.volley.VolleyError;
import com.fishbowl.apps.olo.Interfaces.OloBasketServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloBasketSubmitServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloBasketValidationCallback;
import com.fishbowl.apps.olo.Interfaces.OloBatchProductBasketServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloBillingSchemeServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloMenuServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloProductModifierCallback;
import com.fishbowl.apps.olo.Interfaces.OloRestaurantServiceCallback;
import com.fishbowl.apps.olo.Models.OloBasket;
import com.fishbowl.apps.olo.Models.OloBasketProduct;
import com.fishbowl.apps.olo.Models.OloBasketProductBatchResult;
import com.fishbowl.apps.olo.Models.OloBasketValidation;
import com.fishbowl.apps.olo.Models.OloBillingAccount;
import com.fishbowl.apps.olo.Models.OloBillingScheme;
import com.fishbowl.apps.olo.Models.OloCategory;
import com.fishbowl.apps.olo.Models.OloMenu;
import com.fishbowl.apps.olo.Models.OloModifier;
import com.fishbowl.apps.olo.Models.OloOption;
import com.fishbowl.apps.olo.Models.OloOrderInfo;
import com.fishbowl.apps.olo.Models.OloOrderStatus;
import com.fishbowl.apps.olo.Models.OloProduct;
import com.fishbowl.apps.olo.Models.OloRestaurant;
import com.fishbowl.apps.olo.Services.OloBasketService;
import com.fishbowl.apps.olo.Services.OloMenuService;
import com.fishbowl.apps.olo.Services.OloRestaurantService;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nauman Afzaal on 05/05/15.
 */
public class OloBasketServiceTests extends BaseTests
{
    private static final int vendorId = 9496;

    public void testGetBasketWithInvalidId()
    {
        OloBasketService.getBasketWithId("invalidBasketId", new OloBasketServiceCallback()
        {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error)
            {
                Assert.assertNotNull("Basket Id is invalid", error);
                Assert.assertNull("Invalid response", basket);
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testBasketCreateService()
    {
        OloBasketService.createBasket(vendorId, new OloBasketServiceCallback()
        {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error)
            {
                Assert.assertNull("Basket Request failed", error);
                Assert.assertNotNull("Invalid Basket response", basket);
                Assert.assertTrue("Invalid basket id", !basket.getId().equals(""));
                Assert.assertTrue("Invalid vendor id", basket.getVendorId() > 0);
                Assert.assertTrue("Invalid total", basket.getTotal() == 0);
                helperAddProductToBasket(basket.getId());
            }
        });
        waitForCompletion();
    }

    public void testBasketCreateFromOrderService()
    {
        OloBasketService.createFromOrder("1", "abc", new OloBasketServiceCallback() {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error) {
                Assert.assertNull("Basket Request failed", error);
                Assert.assertNotNull("Invalid Basket response", basket);
                Assert.assertTrue("Invalid basket id", !basket.getId().equals(""));
                Assert.assertTrue("Invalid vendor id", basket.getVendorId() > 0);
                Assert.assertTrue("Invalid total", basket.getTotal() == 0);
                helperAddProductToBasket(basket.getId());
            }
        });
        waitForCompletion();
    }

    private void helperAddProductToBasket(final String basketId)
    {
        OloRestaurantService.getAllRestaurants(new OloRestaurantServiceCallback()
        {
            @Override
            public void onRestaurantServiceCallback(OloRestaurant[] restaurants, Exception exception)
            {
                Assert.assertNull("Restaurant Request failed", exception);
                Assert.assertNotNull("Invalid restaurnat response", restaurants);
                Assert.assertTrue("Restaurants not available", restaurants.length > 0);

                OloRestaurant restaurant = restaurants[0];
                OloMenuService.getRestaurantMenu(restaurant.getId(), new OloMenuServiceCallback()
                {
                    @Override
                    public void onRestaurantMenuCallback(OloMenu menu, Exception exception)
                    {
                        Assert.assertNull("Menu Request failed", exception);
                        Assert.assertNotNull("Invalid menu response", menu);
                        OloCategory category = menu.getCategories()[0];
                        final OloProduct product = category.getProducts().get(0);

                        OloMenuService.getProductOptions(product.getId(), new OloProductModifierCallback()
                        {
                            @Override
                            public void onProductModifierCallback(final OloModifier[] modifiers, final Exception exception)
                            {
                                Assert.assertNull("Product modifier request failed", exception);
                                Assert.assertNotNull("Invalid product modifier response", modifiers);
                                OloModifier modifier = modifiers[0];
                                final OloOption option = modifier.getOptions().get(0);
                                final OloBasketProduct basketProduct = new OloBasketProduct();
                                final int id = product.getId();
                                final int quantity = 1;
                                String instruction = "Instructions";
                                basketProduct.setProductId(id);
                                basketProduct.setQuantity(quantity);
                                basketProduct.setSpecialinstructions(instruction);
                                basketProduct.setOptions(option.getId() + "");

                                OloBasketService.addProductToBasket(basketId, basketProduct, new OloBasketServiceCallback()
                                {
                                    @Override
                                    public void onBasketServiceCallback(OloBasket basket, Exception error)
                                    {
                                        Assert.assertNull("Adding Product request failed", error);
                                        Assert.assertNotNull("Invalid product addition response", basket);
                                        assertNotNull("No Product added to basket", basket.getProducts());
                                        Assert.assertTrue("Products should not be zero", basket.getProducts().size() > 0);
                                        Assert.assertTrue("Products Id should be same", basket.getProducts().get(0).getProductId() == id);
                                        Assert.assertTrue("Quantity should be 1", basket.getProducts().get(0).getQuantity() == quantity);
                                        Assert.assertTrue("There should be 1 product", basket.getProducts().size() == 1);
                                        OloBasketProduct prod = basket.getProducts().get(0);
                                        prod.setOptions(option.getId() + "");
                                        helperTestUpdateProduct(basketId, prod);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void helperTestUpdateProduct(final String basketId, final OloBasketProduct basketProduct)
    {
        basketProduct.setQuantity(2); //Update Quantity
        OloBasketService.updateProduct(basketId, basketProduct, new OloBasketServiceCallback()
        {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error)
            {
                Assert.assertNull("Updating Product request failed", error);
                Assert.assertNotNull("Invalid product update response", basket);
                Assert.assertTrue("Products should not be zero", basket.getProducts().size() > 0);
                Assert.assertTrue("Products Id should be same", basket.getProducts().get(0).getProductId() == basketProduct.getProductId());
                Assert.assertTrue("Quantity should be 2", basket.getProducts().get(0).getQuantity() == 2);
                helperDeleteProduct(basketId, basketProduct);
            }
        });
    }

    private void helperDeleteProduct(final String basketId, final OloBasketProduct product)
    {
        OloBasketService.deleteProduct(basketId, product.getId(), new OloBasketServiceCallback()
        {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error)
            {
                Assert.assertNull("Deleting Product request failed", error);
                Assert.assertNotNull("Invalid product delete response", basket);
                for (OloBasketProduct prod : basket.getProducts())
                {
                    if (prod.getId() == product.getId())
                    {
                        Assert.assertTrue("Product Not Deleted", false);
                    }
                }
                //Add Product Again.
                OloBasketService.addProductToBasket(basketId, product, new OloBasketServiceCallback()
                {
                    @Override
                    public void onBasketServiceCallback(OloBasket basket, Exception error)
                    {
                        Assert.assertTrue("Invalid Products", basket.getProducts().size() > 0);
                        helperSetTimeWanted(basketId);
                    }
                });

            }
        });
    }

    private void helperSetTimeWanted(final String basketId)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int year = calendar.get(Calendar.YEAR);
        final int hour = 11;
        final int min = 15;
        OloBasketService.setTimeWanted(basketId, false, year, month, day, hour, min, new OloBasketServiceCallback()
        {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error)
            {
                Assert.assertNull("Request Failed", error);
                Assert.assertNotNull("Invalid Basket response", basket);
                String monthStr = "" + month;
                if (month < 10)
                {
                    monthStr = "0" + month;
                }
                String dayStr = "" + day;
                if (day < 10)
                {
                    dayStr = "0" + day;
                }

                String timewanted = year + monthStr + dayStr + " " + hour + ":" + min;
                Assert.assertEquals("Invalid time", timewanted, basket.getTimeWanted());
                helperAddCoupon(basketId);
            }
        });
    }

    private void helperAddCoupon(final String basketId)
    {
        String couponCode = "wah_LQWTMCQM";
        OloBasketService.applyCoupon(couponCode, basketId, new OloBasketServiceCallback()
        {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error)
            {
                Assert.assertNull("Apply coupon request failed", error);
                Assert.assertNotNull("Invalid coupon response", basket);
                helperValidateBasket(basketId);
            }
        });
    }

    private void helperValidateBasket(final String basketId)
    {
        OloBasketService.validateBasket(basketId, new OloBasketValidationCallback()
        {
            @Override
            public void onOloBasketValidationCallback(OloBasketValidation validation, Exception exception)
            {
                Assert.assertNull("Validation request failed", exception);
                Assert.assertNotNull("Invalid Validation response", validation);
                Assert.assertTrue("Invalid basket Id", validation.getBasketid().equals(basketId));
                helperSubmitBasket(basketId);
            }
        });
    }

    private void helperSubmitBasket(final String basketId)
    {
        OloOrderInfo oloOrderInfo = new OloOrderInfo();
        String cardnumber = "4556892528054091";
        int expiryyear = 2025;
        int expirymonth = 12;
        int cvv = 334;
        String zip = "541029";
        oloOrderInfo.setBasketId(basketId);
        oloOrderInfo.setBillingMethod("creditcard");
        oloOrderInfo.setBillingAccountId(0);
        oloOrderInfo.setUserType("guest");
        oloOrderInfo.setFirstName("Nauman");
        oloOrderInfo.setLastName("Afzaal");
        oloOrderInfo.setEmail("androidTester@gmail.com");
        oloOrderInfo.setContactNumber("1234567890");
        oloOrderInfo.setReference("");
        oloOrderInfo.setCardnumber(cardnumber);
        oloOrderInfo.setExpiryyear(expiryyear);
        oloOrderInfo.setExpirymonth(expirymonth);
        oloOrderInfo.setCvv(String.valueOf(cvv));
        oloOrderInfo.setZip(zip);
        oloOrderInfo.setSaveonfile("true");
        oloOrderInfo.setOrderref("");

        OloBasketService.submitOrder(oloOrderInfo, new OloBasketSubmitServiceCallback()
        {
            @Override
            public void onOloBasketSubmitServiceCallback(OloOrderStatus orderStatus, Exception exception)
            {
                Assert.assertNull("Submission request failed", exception);
                Assert.assertNotNull("Invalid Submission response", orderStatus);

                latch.countDown();
            }
        });
    }

    public void testBillingSchemes()
    {
        String basketId = "46074393-6A51-4924-B581-46284F3882AB";
        OloBasketService.getBillingSchemes(basketId, new OloBillingSchemeServiceCallback()
        {
            @Override
            public void onBillingSchemeServiceCallback(ArrayList<OloBillingScheme> oloBillingSchemes, Exception error)
            {
                Assert.assertNull("Billing schemes request failed", error);
                Assert.assertNotNull("Invalid scheme response", oloBillingSchemes);

                String cardSuffix = "4091";
                boolean found = false;
                for(OloBillingScheme oloBillingScheme : oloBillingSchemes)
                {
                    if(oloBillingScheme.getType().equals("creditcard"))
                    {
                        ArrayList<OloBillingAccount> oloBillingAccounts = oloBillingScheme.getAccounts();
                        for(OloBillingAccount oloBillingAccount : oloBillingAccounts)
                        {
                            if(oloBillingAccount.getCardsuffix().equals(cardSuffix))
                            {
                                found = true;
                            }
                        }
                    }
                }
                Assert.assertTrue("Card not added to billing schemes", found);
            }
        });

    }
    public void testDeleteTimeWanted()
    {
        OloBasketService.createBasket(vendorId, new OloBasketServiceCallback()
        {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error)
            {
                Assert.assertNull("Request Failed", error);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int month = calendar.get(Calendar.MONTH) + 1;
                final int year = calendar.get(Calendar.YEAR);
                final int hour = 11;
                final int min = 15;
                OloBasketService.setTimeWanted(basket.getId(), false, year, month, day, hour, min, new OloBasketServiceCallback()
                {
                    @Override
                    public void onBasketServiceCallback(OloBasket basket, Exception error)
                    {
                        Assert.assertNull("Request Failed", error);
                        Assert.assertNotNull("Invalid Basket response", basket);
                        OloBasketService.deleteTimeWanted(basket.getId(), new OloBasketServiceCallback()
                        {
                            @Override
                            public void onBasketServiceCallback(OloBasket basket, Exception error)
                            {
                                if (error != null)
                                {
                                    if (error instanceof VolleyError)
                                    {
                                        VolleyError volleyError = (VolleyError) error;
                                        int statusCode = volleyError.networkResponse.statusCode;
                                        if (statusCode != 200)
                                        {
                                            Assert.assertTrue("Request Failed", false);
                                        }
                                    }
                                }
                                else
                                {
                                    Assert.assertNotNull("Invalid Basket response", basket);
                                    Assert.assertTrue("Time not deleted", basket.getTimeWanted() == null || basket.getTimeWanted().equalsIgnoreCase(""));
                                }
                                latch.countDown();
                            }
                        });
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void notestAddMultipleProducts()
    {
        OloBasketService.createBasket(vendorId, new OloBasketServiceCallback()
        {
            @Override
            public void onBasketServiceCallback(OloBasket basket, Exception error)
            {
                Assert.assertNotNull("Invalid Basket response", basket);
                final String basketId = basket.getId();
                OloRestaurantService.getAllRestaurants(new OloRestaurantServiceCallback()
                {
                    @Override
                    public void onRestaurantServiceCallback(OloRestaurant[] restaurants, Exception exception)
                    {
                        Assert.assertNotNull("Invalid restaurnat response", restaurants);
                        OloRestaurant restaurant = restaurants[0];
                        OloMenuService.getRestaurantMenu(restaurant.getId(), new OloMenuServiceCallback()
                        {
                            @Override
                            public void onRestaurantMenuCallback(OloMenu menu, Exception exception)
                            {
                                Assert.assertNotNull("Invalid menu response", menu);
                                final OloCategory category = menu.getCategories()[0];
                                final OloProduct product1 = category.getProducts().get(0);
                                OloMenuService.getProductOptions(product1.getId(), new OloProductModifierCallback()
                                {
                                    @Override
                                    public void onProductModifierCallback(final OloModifier[] modifiers1, final Exception exception)
                                    {
                                        Assert.assertNotNull("Invalid product modifier response", modifiers1);
                                        final OloProduct product2 = category.getProducts().get(1);
                                        OloMenuService.getProductOptions(product2.getId(), new OloProductModifierCallback()
                                        {
                                            @Override
                                            public void onProductModifierCallback(OloModifier[] modifiers2, Exception exception)
                                            {
                                                OloModifier modifier1 = modifiers1[0];
                                                final OloOption option1 = modifier1.getOptions().get(0);
                                                final OloBasketProduct basketProduct1 = new OloBasketProduct();
                                                final int id1 = product1.getId();
                                                final int quantity1 = 1;
                                                String instruction1 = "Instructions";
                                                basketProduct1.setProductId(id1);
                                                basketProduct1.setQuantity(quantity1);
                                                basketProduct1.setSpecialinstructions(instruction1);
                                                basketProduct1.setOptions(option1.getId() + "");

                                                OloModifier modifier2 = modifiers2[0];
                                                final OloOption option2 = modifier2.getOptions().get(0);
                                                final OloBasketProduct basketProduct2 = new OloBasketProduct();
                                                final int id2 = product2.getId();
                                                final int quantity2 = 1;
                                                String instruction2 = "Instructions";
                                                basketProduct2.setProductId(id2);
                                                basketProduct2.setQuantity(quantity2);
                                                basketProduct2.setSpecialinstructions(instruction2);
                                                basketProduct2.setOptions(option2.getId() + "");

                                                ArrayList<OloBasketProduct> products = new ArrayList<OloBasketProduct>();
                                                products.add(basketProduct1);
                                                products.add(basketProduct2);
                                                OloBasketService.addMultipleProducts(basketId, products, new OloBatchProductBasketServiceCallback()
                                                {
                                                    @Override
                                                    public void onBatchProductBasketServiceCallback(OloBasketProductBatchResult result, Exception error)
                                                    {
                                                        Assert.assertNull("Adding Multipl Product request failed", error);
                                                        Assert.assertNotNull("Invalid product addition result", result);
                                                        OloBasket basket = result.getBasket();
                                                        Assert.assertNotNull("Invalid product addition response", basket);
                                                        assertNotNull("No Product added to basket", basket.getProducts());
                                                        Assert.assertTrue("There should be 2 product", basket.getProducts().size() == 2);
                                                        OloBasketProduct basketProduct1 = basket.getProducts().get(0);
                                                        OloBasketProduct basketProduct2 = basket.getProducts().get(1);
                                                        Assert.assertTrue("Products Id1 should be same", basketProduct1.getProductId() == id1);
                                                        Assert.assertTrue("Products Id2 should be same", basketProduct2.getProductId() == id2);
                                                        latch.countDown();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        waitForCompletion();
    }
}
