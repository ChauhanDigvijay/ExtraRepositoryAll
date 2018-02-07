package com.identity.arx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.identity.arx.objectclass.CourseDetailsObject;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailTable extends IdentityAxsOpenHelper {
    public CourseDetailTable(Context mContext) {
        super(mContext);
    }

    public long insert(CourseDetailsObject courseDetailsObject) {
        long lg = 0;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(IdentityAxsOpenHelper.KEY_COURSE_ID, Integer.valueOf(courseDetailsObject.getId()));
            contentValues.put(IdentityAxsOpenHelper.KEY_COURSE_NAME, courseDetailsObject.getCourseName());
            contentValues.put(IdentityAxsOpenHelper.KEY_FACULTY_NAME, courseDetailsObject.getAssignFaculty());
            contentValues.put(IdentityAxsOpenHelper.KEY_COURSE_CODE, courseDetailsObject.getCourseId());
            contentValues.put(IdentityAxsOpenHelper.KEY_NO_OF_LECTURES, courseDetailsObject.getNoOfLectures() + "");
            lg = db.insert(IdentityAxsOpenHelper.COURSE_MASTER_TABLE, null, contentValues);
            db.close();
            return lg;
        } catch (Exception e) {
            return lg;
        }
    }

    private boolean isInstituteAvailable(String intId) {
        if (getWritableDatabase().query(IdentityAxsOpenHelper.INSTITUTE_TABLE, null, "inst_db =?", new String[]{intId}, null, null, null).getCount() > 0) {
            return false;
        }
        return true;
    }

    public CourseDetailsObject getCourseOverId(String courseId) {
        CourseDetailsObject info = new CourseDetailsObject();
        Cursor cursor = getWritableDatabase().query(IdentityAxsOpenHelper.COURSE_MASTER_TABLE, null, "course_id =?", new String[]{courseId}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                info.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_COURSE_ID))));
                info.setCourseId(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_COURSE_CODE)));
                info.setCourseName(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_COURSE_NAME)));
                info.setAssignFaculty(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_FACULTY_NAME)));
                info.setNoOfLectures(cursor.getInt(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_NO_OF_LECTURES)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return info;
    }

    public List<CourseDetailsObject> getAllCourses() {
        List<CourseDetailsObject> info = new ArrayList();
        Cursor cursor = getWritableDatabase().query(IdentityAxsOpenHelper.COURSE_MASTER_TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CourseDetailsObject courseDetailsObject = new CourseDetailsObject();
                courseDetailsObject.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_COURSE_ID))));
                courseDetailsObject.setCourseId(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_COURSE_CODE)));
                courseDetailsObject.setCourseName(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_COURSE_NAME)));
                courseDetailsObject.setAssignFaculty(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_FACULTY_NAME)));
                courseDetailsObject.setNoOfLectures(Integer.valueOf(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_NO_OF_LECTURES))).intValue());
                info.add(courseDetailsObject);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return info;
    }
}
