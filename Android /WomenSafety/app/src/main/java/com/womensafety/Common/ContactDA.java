package com.womensafety.Common;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;

import com.womensafety.object.ContactDO;

import java.util.ArrayList;

public class ContactDA {
    public ArrayList<ContactDO> readContacts(Context context) {
        ArrayList<ContactDO> arrayList = new ArrayList();
        try {
            Cursor cur = context.getContentResolver().query(Contacts.CONTENT_URI, null, null, null, "display_name ASC");
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    ContactDO contactDO = new ContactDO();
                    contactDO.dname = cur.getString(cur.getColumnIndex("display_name"));
                    contactDO.name = cur.getString(cur.getColumnIndex("display_name"));
                    contactDO.imagePath = cur.getString(cur.getColumnIndex("photo_uri"));
                    if (!TextUtils.isEmpty(contactDO.imagePath)) {
                        arrayList.add(contactDO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
