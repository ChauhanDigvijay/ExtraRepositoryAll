package com.olo.jambajuice.BusinessLogic.Models;

import com.olo.jambajuice.BusinessLogic.Services.UserService;
import com.olo.jambajuice.Utils.ParseUtils;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.wearehathway.apps.olo.Utils.Constants.Server_Time_Format;

/**
 * Created by Nauman Afzaal on 01/09/15.
 */
public class AuditOrder
{
    public static String parseClassName = "AuditOrder";

    Basket basket;
    OrderStatus orderStatus;
    User user;

    public AuditOrder(Basket basket, OrderStatus orderStatus, User user)
    {
        this.basket = basket;
        this.orderStatus = orderStatus;
        this.user = user;
    }

    public ParseObject serializeAsParseObject()
    {
        ParseObject parseObject = ParseObject.create(AuditOrder.parseClassName);

        // Order Details
        parseObject.put("orderId", ParseUtils.sanitizeValue(orderStatus.getId()));
        parseObject.put("vendorName", ParseUtils.sanitizeValue(orderStatus.getVendorname()));
        parseObject.put("vendorExtRef", ParseUtils.sanitizeValue(orderStatus.getVendorExtRef()));
        SimpleDateFormat format = new SimpleDateFormat(Server_Time_Format);
        Date placedTime = new Date();
        Date readyTime = new Date();
        try
        {
            placedTime = format.parse(orderStatus.getTimePlaced());
            readyTime = format.parse(orderStatus.getReadytime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        parseObject.put("orderPlacedTime", ParseUtils.sanitizeValue(placedTime));
        parseObject.put("orderReadyTime", ParseUtils.sanitizeValue(readyTime));
        parseObject.put("orderTotal", ParseUtils.sanitizeValue(orderStatus.getTotal()));
        parseObject.put("orderSubtotal", ParseUtils.sanitizeValue(orderStatus.getSubTotal()));
        parseObject.put("orderTax", ParseUtils.sanitizeValue(orderStatus.getSaleTax()));
        parseObject.put("productCount", ParseUtils.sanitizeValue(orderStatus.getProductsCount()));

        // User details
        if (UserService.isUserAuthenticated())
        {
            parseObject.put("userId", ParseUtils.sanitizeValue(user.getSpendGoId()));
            parseObject.put("userEmailAddress", ParseUtils.sanitizeValue(user.getEmailaddress()));
            parseObject.put("userPhone", ParseUtils.sanitizeValue(user.getContactnumber()));
            parseObject.put("userFirstName", ParseUtils.sanitizeValue(user.getFirstname()));
            parseObject.put("userLastName", ParseUtils.sanitizeValue(user.getLastname()));
            parseObject.put("userDateOfBirth", ParseUtils.sanitizeValue(user.getDob()));
        }

        return parseObject;
    }
}
