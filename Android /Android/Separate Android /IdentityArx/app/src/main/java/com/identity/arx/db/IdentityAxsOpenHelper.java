package com.identity.arx.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IdentityAxsOpenHelper extends SQLiteOpenHelper {
    public static final String COURSE_MASTER_TABLE = "course_mastere_table";
    public static final String DATABASE_NAME = "IdentityAxsData";
    private static final int DATABASE_VERSION = 1;
    public static final String FACULTY_TABLE = "faculty_table";
    public static final String ID = "id";
    public static final String INSTITUTE_TABLE = "institute_table";
    public static final String KEY_ASSIGN_FACULTY = "assign_faculty";
    public static final String KEY_COURSE_CODE = "CourseCode";
    public static final String KEY_COURSE_ID = "course_id";
    public static final String KEY_COURSE_NAME = "course_name";
    public static final String KEY_CURRENT_STATUS = "current_status";
    public static final String KEY_DATE_OF_BIRTH = "date_of_birth";
    public static final String KEY_DEPT_ID = "dept_id";
    public static final String KEY_END_TIME = "end_time";
    public static final String KEY_FACULTY_NAME = "faculty_name";
    public static final String KEY_INSTITUTE_NAME = "user_institute";
    public static final String KEY_INST_DB = "inst_db";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LECTURE_DATE = "lecture_date";
    public static final String KEY_LECTURE_DAY = "lecture_day";
    public static final String KEY_LECTURE_END_TIME = "lecture_end_time";
    public static final String KEY_LECTURE_ID = "lecture_id";
    public static final String KEY_LECTURE_LOCATION = "lecture_location";
    public static final String KEY_LECTURE_START_TIME = "lecture_start_time";
    public static final String KEY_LECTURE_STATUS = "lecture_status";
    public static final String KEY_LOGIN_STATUS = "login_status";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_NOTIFICATION_STATUS = "notification_status";
    public static final String KEY_NO_OF_LECTURES = "no_of_lectures";
    public static final String KEY_ROLL_NO = "roll_no";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_UNIVERSITY_NAME = "user_university";
    public static final String KEY_USER_AADHAAR = "user_aadhaar";
    public static final String KEY_USER_CONTACT = "user_contact";
    public static final String KEY_USER_DEPT = "user_dept";
    public static final String KEY_USER_DESIGN = "user_design";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_USER_INSTID = "user_instId";
    public static final String KEY_USER_LABEL_ID = "user_label_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_PROGRAM = "user_program";
    public static final String KEY_USER_SEMESTER = "user_semester";
    public static final String KEY_USER_YEAR = "user_year";
    public static final String LECTURE_SCHEDULE_TABLE = "lecture_schedule_table";
    public static final String LOGIN_TABLE = "login_table";
    public static final String STUDENT_TABLE = "student_table";
    public static final String USER_ROLE = "user_role";
    public static final String USER_TABLE = "user_table";
    private String CREATE_COURSE_MASTER_TABLE;
    private String CREATE_INSTITUTE_TABLE;
    private String CREATE_LECTURE_SCHEDULE_TABLE;
    private String CREATE_USER_TABLE;

    public IdentityAxsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        createTables();
        db.execSQL(this.CREATE_USER_TABLE);
        db.execSQL(this.CREATE_INSTITUTE_TABLE);
        db.execSQL(this.CREATE_COURSE_MASTER_TABLE);
        db.execSQL(this.CREATE_LECTURE_SCHEDULE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createTables() {
        this.CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS user_table ( user_name TEXT,user_id TEXT,user_contact TEXT,user_dept TEXT,roll_no TEXT,user_aadhaar TEXT,user_label_id TEXT,user_image TEXT,current_status TEXT,user_design TEXT,user_year TEXT,login_status TEXT,user_program TEXT,user_email TEXT,user_semester TEXT,user_institute TEXT,user_university TEXT,date_of_birth TEXT,user_instId TEXT)";
        this.CREATE_LECTURE_SCHEDULE_TABLE = "CREATE TABLE IF NOT EXISTS lecture_schedule_table (lecture_id INTEGER,dept_id INTEGER,start_time TEXT,end_time TEXT,lecture_location TEXT,lecture_day TEXT,lecture_date TEXT,assign_faculty INTEGER,lecture_status TEXT,lecture_start_time TEXT,lecture_end_time TEXT,course_id TEXT,notification_status TEXT,latitude TEXT,longitude TEXT)";
        this.CREATE_INSTITUTE_TABLE = "CREATE TABLE IF NOT EXISTS institute_table (inst_db TEXT,user_institute TEXT)";
        this.CREATE_COURSE_MASTER_TABLE = "CREATE TABLE IF NOT EXISTS course_mastere_table (course_id TEXT,course_name TEXT,CourseCode TEXT,faculty_name TEXT,no_of_lectures TEXT)";
    }

    public void delete(String course) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CREATE_COURSE_MASTER_TABLE");
        db.execSQL("DELETE FROM CREATE_USER_TABLE");
        db.close();
    }

    public boolean deleteRecordsFromTable(String tableName, String wherclause) throws SQLException {
        try {
            if (getWritableDatabase().delete(tableName, null, null) > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw e;
        }
    }
}
