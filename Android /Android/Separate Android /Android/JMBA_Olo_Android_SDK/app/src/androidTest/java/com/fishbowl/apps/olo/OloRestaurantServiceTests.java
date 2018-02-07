package com.fishbowl.apps.olo;

import com.fishbowl.apps.olo.Interfaces.OloRestaurantCalendarCallback;
import com.fishbowl.apps.olo.Interfaces.OloRestaurantServiceCallback;
import com.fishbowl.apps.olo.Models.OloRestaurant;
import com.fishbowl.apps.olo.Models.OloRestaurantCalenders;
import com.fishbowl.apps.olo.Models.OloTimeRange;
import com.fishbowl.apps.olo.Services.OloRestaurantService;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nauman Afzaal on 24/04/15.
 */
public class OloRestaurantServiceTests extends BaseTests implements OloRestaurantServiceCallback
{
    int restaurantId = 9496;

    public void testAllRestaurants()
    {
        OloRestaurantService.getAllRestaurants(this);
        waitForCompletion();
    }

    // Currently expecting ~6 restaurants
    public void testRestaurantsNearSanFrancisco()
    {
        double latitude = 37.774929;
        double longitude = -122.419416;
        int radius = 2;
        int limit = 10;
        OloRestaurantService.getAllRestaurantsNear(latitude, longitude, radius, limit, this);
        waitForCompletion();
    }

    public void testRestaurantsNearSanLuisObispo()
    {
        double latitude = 35.268275;
        double longitude = -120.669934;
        int radius = 2;
        int limit = 10;

        OloRestaurantService.getAllRestaurantsNear(latitude, longitude, radius, limit, new OloRestaurantServiceCallback()
        {
            @Override
            public void onRestaurantServiceCallback(OloRestaurant[] restaurants, Exception exception)
            {
                Assert.assertNull("Restaurant Request failed", exception);
                Assert.assertNotNull("Invalid restaurnat response", restaurants);
                Assert.assertFalse("Restaurants available", restaurants.length > 0);
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testRestaurantCalendars()
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.YEAR, 2015);
        Date from = calendar.getTime();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, 20);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.YEAR, 2015);
        Date to = calendar.getTime();

        OloRestaurantService.getRestaurantCalendar(restaurantId, from, to, new OloRestaurantCalendarCallback()
        {
            @Override
            public void onCalendarCallback(OloRestaurantCalenders calendars, Exception exception)
            {
                Assert.assertNull("Restaurant Calendar Request failed", exception);
                Assert.assertNotNull("Invalid calendar restaurant response", calendars);
                Assert.assertNotNull("Invalid calendars", calendars.getCalendar());
                Assert.assertTrue("Invalid calendars", calendars.getCalendar().size() > 0);
                ArrayList<OloTimeRange> ranges = calendars.getCalendar().get(0).getRanges();
                Assert.assertTrue("Invalid calendars", ranges.size() == 6);
                latch.countDown();
            }
        });
        waitForCompletion();
    }

    public void testRestaurantInfoByReference()
    {
        String storeCode = "0091";
        OloRestaurantService.getRestaurantByRef(storeCode, this);
        waitForCompletion();
    }
    @Override
    public void onRestaurantServiceCallback(OloRestaurant[] restaurants, Exception exception)
    {
        Assert.assertNull("Restaurant Request failed", exception);
        Assert.assertNotNull("Invalid restaurnat response", restaurants);
        Assert.assertTrue("No Restaurants available", restaurants.length > 0);
        for (int i = 0; i < restaurants.length; i++)
        {
            Assert.assertNotNull("Name Shoud not be null", restaurants[i].getName());
            Assert.assertNotNull("Id Shoud not be null", restaurants[i].getId());
        }
        latch.countDown();
    }

}
