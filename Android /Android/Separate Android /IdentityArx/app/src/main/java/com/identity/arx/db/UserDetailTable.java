package com.identity.arx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.identity.arx.objectclass.LoginResponseObject;


public class UserDetailTable extends IdentityAxsOpenHelper {
    SQLiteDatabase db = getWritableDatabase();

    public UserDetailTable(Context mContext) {
        super(mContext);
    }

    public boolean addUserDetails(LoginResponseObject UserVO) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("user_name", UserVO.getName());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_ID, UserVO.getUserId());
            contentValues.put(IdentityAxsOpenHelper.KEY_UNIVERSITY_NAME, UserVO.getUniversityName());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_AADHAAR, UserVO.getAdhaarNum());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_CONTACT, UserVO.getContact());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_DEPT, UserVO.getDeptName());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_DESIGN, UserVO.getDesignation());
            contentValues.put("date_of_birth", UserVO.getDob());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_EMAIL, UserVO.getEmail());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_LABEL_ID, UserVO.getLebelId());
            contentValues.put("login_status", UserVO.getLoginStatus());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_YEAR, UserVO.getPersuingYear());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_SEMESTER, UserVO.getSemYear());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_PROGRAM, UserVO.getProgramName());
            contentValues.put(IdentityAxsOpenHelper.KEY_ROLL_NO, UserVO.getRollnum());
            contentValues.put(IdentityAxsOpenHelper.KEY_CURRENT_STATUS, UserVO.getStatus());
            contentValues.put(IdentityAxsOpenHelper.KEY_USER_IMAGE, UserVO.getProfileImg());
            contentValues.put(IdentityAxsOpenHelper.KEY_INSTITUTE_NAME, UserVO.getInstitutName());
            if (getUserDetails().getUserId() != null) {
                this.db.update(IdentityAxsOpenHelper.USER_TABLE, contentValues, null, null);
            } else {
                this.db.insert(IdentityAxsOpenHelper.USER_TABLE, null, contentValues);
            }
            this.db.close();
        } catch (Exception e) {
        }
        return true;
    }

    public LoginResponseObject getUserDetails() {
        LoginResponseObject info = new LoginResponseObject();
        Cursor cursor = this.db.query(IdentityAxsOpenHelper.USER_TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                info.setUserId(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_ID)));
                info.setAdhaarNum(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_AADHAAR)));
                info.setContact(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_CONTACT)));
                info.setDeptName(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_DEPT)));
                info.setDesignation(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_DESIGN)));
                info.setDob(cursor.getString(cursor.getColumnIndex("date_of_birth")));
                info.setEmail(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_EMAIL)));
                info.setLebelId(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_LABEL_ID)));
                info.setName(cursor.getString(cursor.getColumnIndex("user_name")));
                info.setPersuingYear(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_YEAR)));
                info.setProfileImg(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_IMAGE)));
                info.setProgramName(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_PROGRAM)));
                info.setRollnum(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_ROLL_NO)));
                info.setSemYear(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_USER_SEMESTER)));
                info.setUniversityName(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_UNIVERSITY_NAME)));
                info.setStatus(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_CURRENT_STATUS)));
                info.setLoginStatus(cursor.getString(cursor.getColumnIndex("login_status")));
                info.setInstitutName(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_INSTITUTE_NAME)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return info;
    }
}
