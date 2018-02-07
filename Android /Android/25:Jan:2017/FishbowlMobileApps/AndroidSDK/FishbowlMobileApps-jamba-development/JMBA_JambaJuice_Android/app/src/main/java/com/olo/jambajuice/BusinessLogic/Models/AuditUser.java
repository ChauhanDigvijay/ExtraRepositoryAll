package com.olo.jambajuice.BusinessLogic.Models;

import com.olo.jambajuice.Utils.ParseUtils;
import com.parse.ParseObject;

/**
 * Created by Nauman Afzaal on 01/09/15.
 */
public class AuditUser
{
    public static String parseClassName = "AuditUser";
    User user;
    String action;

    public AuditUser(User user, String action)
    {
        this.user = user;
        this.action = action;
    }

    public ParseObject serializeAsParseObject()
    {
        ParseObject parseObject = ParseObject.create(AuditUser.parseClassName);
        parseObject.put("id", ParseUtils.sanitizeValue(user.getSpendGoId()));
        parseObject.put("email", ParseUtils.sanitizeValue(user.getEmailaddress()));
        parseObject.put("phone", ParseUtils.sanitizeValue(user.getContactnumber()));
        parseObject.put("firstName", ParseUtils.sanitizeValue(user.getFirstname()));
        parseObject.put("lastName", ParseUtils.sanitizeValue(user.getLastname()));
        parseObject.put("dateOfBirth",ParseUtils.sanitizeValue(user.getDob()));
        parseObject.put("smsOptIn", ParseUtils.sanitizeValue(user.isEnableSmsOpt()));
        parseObject.put("emailOptIn", ParseUtils.sanitizeValue(user.isEnableEmailOpt()));
        parseObject.put("action", ParseUtils.sanitizeValue(action));
        return parseObject;
    }
}
