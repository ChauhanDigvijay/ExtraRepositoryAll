package com.olo.jambajuice.BusinessLogic.Services;

import android.os.Build;

import com.olo.jambajuice.BuildConfig;
import com.olo.jambajuice.BusinessLogic.Models.AuditOrder;
import com.olo.jambajuice.BusinessLogic.Models.AuditUser;
import com.olo.jambajuice.BusinessLogic.Models.Basket;
import com.olo.jambajuice.BusinessLogic.Models.OrderStatus;
import com.olo.jambajuice.BusinessLogic.Models.User;
import com.olo.jambajuice.Utils.ParseUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.wearehathway.apps.olo.Utils.Logger;

/**
 * Created by Nauman Afzaal on 01/09/15.
 */
public class AuditService {
    // Track user access to the app
    public static void trackUserAccess(final User user, String action) {
        final AuditUser auditUser = new AuditUser(user, action);
        saveAuditRecord(auditUser.serializeAsParseObject());
    }

    // Track orders submitted from the app
    public static void trackOrder(Basket basket, OrderStatus orderStatus) {
        AuditOrder auditOrder = new AuditOrder(basket, orderStatus, UserService.getUser());
        saveAuditRecord(auditOrder.serializeAsParseObject());
    }

    private static void saveAuditRecord(ParseObject parseObject) {
        parseObject.put("device_name", ParseUtils.sanitizeValue(Build.MODEL));
        parseObject.put("device_model", ParseUtils.sanitizeValue(Build.MANUFACTURER));
        parseObject.put("device_os_version", ParseUtils.sanitizeValue(Build.VERSION.RELEASE));
        parseObject.put("application_version", ParseUtils.sanitizeValue(BuildConfig.VERSION_NAME));
        parseObject.put("application_build", ParseUtils.sanitizeValue(BuildConfig.VERSION_CODE + ""));
        parseObject.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Logger.i("Audit Log Save Callback + " + e);
            }
        });
    }

}
