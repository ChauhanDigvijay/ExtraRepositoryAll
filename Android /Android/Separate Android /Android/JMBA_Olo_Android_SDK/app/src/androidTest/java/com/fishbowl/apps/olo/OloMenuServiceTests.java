package com.fishbowl.apps.olo;

import com.fishbowl.apps.olo.Interfaces.OloMenuServiceCallback;
import com.fishbowl.apps.olo.Interfaces.OloProductModifierCallback;
import com.fishbowl.apps.olo.Interfaces.OloRestaurantServiceCallback;
import com.fishbowl.apps.olo.Models.OloCategory;
import com.fishbowl.apps.olo.Models.OloCustomField;
import com.fishbowl.apps.olo.Models.OloMenu;
import com.fishbowl.apps.olo.Models.OloModifier;
import com.fishbowl.apps.olo.Models.OloOption;
import com.fishbowl.apps.olo.Models.OloProduct;
import com.fishbowl.apps.olo.Models.OloRestaurant;
import com.fishbowl.apps.olo.Services.OloMenuService;
import com.fishbowl.apps.olo.Services.OloRestaurantService;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Nauman on 25/04/15.
 */
public class OloMenuServiceTests extends BaseTests
{
    public void testRestaurantMenu()
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
                        int size = menu.getCategories().length;
                        Assert.assertTrue("No category found", size > 0);
                        for (int i = 0; i < size; i++)
                        {
                            OloCategory category = menu.getCategories()[i];
                            Assert.assertTrue("Category has name", !category.getName().equals(""));
                            Assert.assertTrue("Category has id", category.getId() > 0);
                            int productSize = category.getProducts().size();
                            for (int j = 0; j < productSize; j++)
                            {
                                OloProduct product = category.getProducts().get(j);
                                Assert.assertTrue("Product has name", !product.getName().equals(""));
                                Assert.assertTrue("Product has id", product.getId() > 0);
                            }
                        }
                        latch.countDown();
                    }
                });
            }
        });
        waitForCompletion();
    }

    public void testProductModifier()
    {
        helperTestModifierCall(true);
    }

    public void testProductOptions()
    {
        helperTestModifierCall(false);
    }

    private void helperTestModifierCall(final boolean isModifier)
    {
        latch = new CountDownLatch(1);
        OloRestaurantService.getAllRestaurants(new OloRestaurantServiceCallback()
        {
            @Override
            public void onRestaurantServiceCallback(OloRestaurant[] restaurants, Exception exception)
            {
                Assert.assertNull("Restaurant Request failed", exception);
                Assert.assertNotNull("Invalid restaurant response", restaurants);
                Assert.assertTrue("No Restaurants available", restaurants.length > 0);
                OloRestaurant restaurant = restaurants[0];
                OloMenuService.getRestaurantMenu(restaurant.getId(), new OloMenuServiceCallback()
                {
                    @Override
                    public void onRestaurantMenuCallback(OloMenu menu, Exception exception)
                    {
                        Assert.assertNull("Menu Request failed", exception);
                        Assert.assertNotNull("Invalid menu response", menu);
                        int size = menu.getCategories().length;
                        Assert.assertTrue("No category found", size > 0);
                        OloCategory category = menu.getCategories()[0];
                        Assert.assertTrue("Category has no products", category.getProducts().size() > 0);
                        OloProduct product = category.getProducts().get(0);
                        if (isModifier)
                        {
                            OloMenuService.getProductModifiers(product.getId(), new OloProductModifierCallback()
                            {
                                @Override
                                public void onProductModifierCallback(OloModifier[] modifiers, Exception exception)
                                {
                                    helperTestAllModifiers(modifiers, exception);
                                }
                            });
                        }
                        else
                        {
                            OloMenuService.getProductOptions(product.getId(), new OloProductModifierCallback()
                            {
                                @Override
                                public void onProductModifierCallback(OloModifier[] modifiers, Exception exception)
                                {
                                    helperTestAllModifiers(modifiers, exception);
                                }
                            });
                        }

                    }
                });
            }
        });
    }

    private void helperTestAllModifiers(OloModifier[] modifiers, Exception exception)
    {
        Assert.assertNull("Product modifier request failed", exception);
        Assert.assertNotNull("Invalid product modifier response", modifiers);
        int size = modifiers.length;
        for (int i = 0; i < size; i++)
        {
            OloModifier modifier = modifiers[0];
            helperTestModifier(modifier);
            helperTestOptions(modifier.getOptions());
        }
    }

    private void helperTestOptions(ArrayList<OloOption> options)
    {
        Assert.assertNotNull("No option avaliable", options);
        for (OloOption option : options)
        {
            Assert.assertTrue("Option Id cannot be null", option.getId() > 0);
            Assert.assertFalse("Option name cannot be empty", option.getName().equals(""));
            ArrayList<OloModifier> modifiers = option.getModifiers();
            if (modifiers != null)
            {
                for (OloModifier modifier : modifiers)
                {
                    helperTestModifier(modifier);
                }
            }
            ArrayList<OloCustomField> fields = option.getFields();
            if (fields != null)
            {
                for (OloCustomField customField : fields)
                {
                    helperTestCustomField(customField);
                }
            }
        }
    }

    private void helperTestModifier(OloModifier modifier)
    {
        Assert.assertTrue("Modifier Id cannot be null", modifier.getId() > 0);
        Assert.assertFalse("Modifier description cannot be empty", modifier.getDescription().equals(""));
    }

    private void helperTestCustomField(OloCustomField customField)
    {
        Assert.assertTrue("Custom Field Id cannot be null", customField.getId() > 0);
        Assert.assertFalse("Custom Field name cannot be empty", customField.getLabel().equals(""));
    }
}
