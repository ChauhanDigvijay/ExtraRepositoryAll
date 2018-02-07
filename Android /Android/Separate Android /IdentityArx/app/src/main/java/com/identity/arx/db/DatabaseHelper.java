package com.identity.arx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper {
    public static final String COURSE_SCHEDULE_TABLE = "course_schedule_table";
    private static final String DATABASE_NAME = "himanshu.db";
    private static final int DATABASE_VERSION = 17;
    public static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String FACULTY_AADHAAR = "student_aadhaar";
    public static final String FACULTY_CONTACT = "student_contact";
    public static final String FACULTY_DEPT = "student_dept";
    public static final String FACULTY_ID = "student_id";
    public static final String FACULTY_NAME = "student_name";
    public static final String FACULTY_TABLE = "faculty_table";
    public static final String LECTURE_SCHEDULE_TABLE = "lecture_schedule_table";
    public static final String LOGIN_STATUS = "login_status";
    public static final String LOGIN_TABLE = "login_table";
    public static final String STUDENT_AADHAAR = "student_aadhaar";
    public static final String STUDENT_CONTACT = "student_contact";
    public static final String STUDENT_DEPT = "student_dept";
    public static final String STUDENT_EMAIL = "student_email";
    public static final String STUDENT_ID = "student_id";
    public static final String STUDENT_IMAGE = "student_image";
    public static final String STUDENT_INSTID = "student_instId";
    public static final String STUDENT_LABEL_ID = "student_label_id";
    public static final String STUDENT_NAME = "student_name";
    public static final String STUDENT_PROGRAM = "student_program";
    public static final String STUDENT_SEMESTER = "student_sememester";
    public static final String STUDENT_TABLE = "student_table";
    public static final String STUDENT_YEAR = "student_year";
    public static final String USER_INSTID = "user_instId";
    public static final String USER_NAME = "user_name";
    public static final String USER_ROLE = "user_role";
    public static final String WEEKLY_LECTURE_SCHEDULE_TABLE = "Weekly_lecture_schedule_table";
    private OpenHelper DBHelper = new OpenHelper(this.context);
    private Context context;
    private SQLiteDatabase db;

    private static class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context) {
            super(context, DatabaseHelper.DATABASE_NAME, null, 17);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS login_table (user_name TEXT,user_role INTEGER,login_status INTEGER,user_instId INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS student_table (id LONG PRIMARY KEY,student_name TEXT,student_id TEXT,student_contact TEXT,student_dept TEXT,student_aadhaar TEXT,student_label_id INTEGER,student_image BLOB,student_year TEXT,student_program TEXT,student_email TEXT,student_sememester TEXT,date_of_birth TEXT,student_instId INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS faculty_table (id LONG PRIMARY KEY,faculty_name TEXT,faculty_id INTEGER,faculty_contact TEXT,faculty_dept TEXT,faculty_design TEXT,faculty_aadhaar TEXT,faculty_label_id INTEGER,faculty_image BLOB,faculty_email TEXT,faculty_instId INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS course_schedule_table (id LONG PRIMARY KEY,course_name TEXT,course_code TEXT,course_login_status INTEGER,lect_start_time TEXT,lect_end_time TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS lecture_schedule_table (id LONG PRIMARY KEY,course_name TEXT,course_code TEXT,course_login_status INTEGER,lect_start_time TEXT,lect_end_time TEXT,lecture_date TEXT,lecture_day TEXT,venue TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS Weekly_lecture_schedule_table (id LONG PRIMARY KEY,course_name TEXT,course_code TEXT,course_login_status INTEGER,lect_start_time TEXT,lect_end_time TEXT,lecture_date TEXT,lecture_day TEXT,venue TEXT)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < 20) {
                db.execSQL("DROP TABLE IF EXISTS login_table");
                db.execSQL("DROP TABLE IF EXISTS student_table");
                db.execSQL("DROP TABLE IF EXISTS faculty_table");
                db.execSQL("DROP TABLE IF EXISTS course_schedule_table");
                db.execSQL("DROP TABLE IF EXISTS lecture_schedule_table");
                db.execSQL("DROP TABLE IF EXISTS Weekly_lecture_schedule_table");
            }
            onCreate(db);
        }
    }

    public DatabaseHelper(Context context) {
        this.context = context;
    }

    public DatabaseHelper open() throws SQLException {
        this.db = this.DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.DBHelper.close();
    }

    public void beginTransaction() {
        this.db.beginTransaction();
    }

    public void successfullTransaction() {
        this.db.setTransactionSuccessful();
    }

    public void endTransaction() {
        this.db.endTransaction();
    }

    public boolean insertInTable(String tableName, ContentValues contentValue) throws SQLException {
        try {
            if (this.db.insert(tableName, null, contentValue) > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean insertWithOnConflict(String tableName, String tableName1, ContentValues contentValue, int conflictReplace) throws SQLException {
        try {
            if (this.db.insert(tableName, null, contentValue) > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean updateTable(String TableName, String whereClause, ContentValues values) {
        if (this.db.update(TableName, values, whereClause, null) > 0) {
            return true;
        }
        return false;
    }

    public Cursor getDataFromDB(String rawQuery) throws SQLException {
        try {
            Cursor cursor = this.db.rawQuery(rawQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            return cursor;
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean deleteRecordsFromTable(String tableName, String wherclause) throws SQLException {
        try {
            if (this.db.delete(tableName, null, null) > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw e;
        }
    }
}
