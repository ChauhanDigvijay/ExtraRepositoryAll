package com.identity.arx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.identity.arx.objectclass.LectureScheduleObject;

import java.util.ArrayList;
import java.util.List;

public class LectureDetailTable extends IdentityAxsOpenHelper {
    Context mContext;

    public LectureDetailTable(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    public boolean addLectureDetails(LectureScheduleObject UserVO) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(IdentityAxsOpenHelper.KEY_LECTURE_ID, Integer.valueOf(UserVO.getId()));
            contentValues.put(IdentityAxsOpenHelper.KEY_DEPT_ID, Integer.valueOf(UserVO.getDeptId()));
            contentValues.put(IdentityAxsOpenHelper.KEY_START_TIME, UserVO.getLectureStartTime());
            contentValues.put(IdentityAxsOpenHelper.KEY_END_TIME, UserVO.getLectureEndTime());
            contentValues.put(IdentityAxsOpenHelper.KEY_LECTURE_LOCATION, UserVO.getLectureLocation());
            contentValues.put(IdentityAxsOpenHelper.KEY_LECTURE_DAY, UserVO.getLectureDay());
            contentValues.put(IdentityAxsOpenHelper.KEY_LECTURE_DATE, UserVO.getLectureDate());
            contentValues.put(IdentityAxsOpenHelper.KEY_ASSIGN_FACULTY, Integer.valueOf(UserVO.getAssignedFacultyId()));
            contentValues.put(IdentityAxsOpenHelper.KEY_COURSE_ID, Integer.valueOf(UserVO.getCourseId()));
            contentValues.put(IdentityAxsOpenHelper.KEY_LECTURE_STATUS, "P");
            contentValues.put(IdentityAxsOpenHelper.KEY_LECTURE_START_TIME, "NA");
            contentValues.put(IdentityAxsOpenHelper.KEY_LECTURE_END_TIME, "NA");
            contentValues.put(IdentityAxsOpenHelper.KEY_NOTIFICATION_STATUS, "0");
            contentValues.put(IdentityAxsOpenHelper.KEY_LATITUDE, Double.valueOf(UserVO.getLatitude()));
            contentValues.put(IdentityAxsOpenHelper.KEY_LONGITUDE, Double.valueOf(UserVO.getLongitude()));
            db.insert("lecture_schedule_table", null, contentValues);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public List<LectureScheduleObject> getLectureDetails(String day) {
        List<LectureScheduleObject> lectureSchedule = new ArrayList();
        Cursor cursor = getWritableDatabase().query("lecture_schedule_table", null, "lecture_day like '%" + day + "%'", null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LectureScheduleObject info = new LectureScheduleObject();
                info.setId(cursor.getInt(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LECTURE_ID)));
                info.setDeptId(cursor.getInt(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_DEPT_ID)));
                info.setLectureStartTime(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_START_TIME)));
                info.setLectureEndTime(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_END_TIME)));
                info.setAssignedFacultyId(cursor.getInt(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_ASSIGN_FACULTY)));
                info.setLectureDay(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LECTURE_DAY)));
                info.setLectureLocation(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LECTURE_LOCATION)));
                info.setNotifaction_status(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_NOTIFICATION_STATUS)));
                info.setLectureDate(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LECTURE_DATE)));
                info.setCourseId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_COURSE_ID))));
                info.setLongitude(cursor.getDouble(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LATITUDE)));
                info.setLongitude(cursor.getDouble(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LONGITUDE)));
                lectureSchedule.add(info);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return lectureSchedule;
    }

    public List<LectureScheduleObject> getCurrentDate(String day) {
        List<LectureScheduleObject> lectureScheduleObject = new ArrayList();
        Cursor cursor = getWritableDatabase().query("lecture_schedule_table", null, "lecture_day =?", new String[]{day}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LectureScheduleObject info = new LectureScheduleObject();
                info.setId(cursor.getInt(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LECTURE_ID)));
                info.setDeptId(cursor.getInt(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_DEPT_ID)));
                info.setLectureStartTime(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_START_TIME)));
                info.setLectureEndTime(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_END_TIME)));
                info.setAssignedFacultyId(cursor.getInt(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_ASSIGN_FACULTY)));
                info.setLectureDay(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LECTURE_DAY)));
                info.setLectureLocation(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LECTURE_LOCATION)));
                info.setNotifaction_status(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_NOTIFICATION_STATUS)));
                info.setLectureDate(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LECTURE_DATE)));
                info.setCourseId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_COURSE_ID))));
                info.setLongitude(cursor.getDouble(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LATITUDE)));
                info.setLongitude(cursor.getDouble(cursor.getColumnIndex(IdentityAxsOpenHelper.KEY_LONGITUDE)));
                lectureScheduleObject.add(info);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return lectureScheduleObject;
    }

    public boolean updateLectureNotification(Integer lectureId, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(IdentityAxsOpenHelper.KEY_LECTURE_ID, lectureId);
            contentValues.put(IdentityAxsOpenHelper.KEY_NOTIFICATION_STATUS, status);
            db.update("lecture_schedule_table", contentValues, null, null);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void deleteTableData() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("lecture_schedule_table", "1", null);
        db.close();
    }
}
