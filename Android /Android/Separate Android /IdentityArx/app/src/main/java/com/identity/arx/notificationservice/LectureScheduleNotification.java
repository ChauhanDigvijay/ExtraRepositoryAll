package com.identity.arx.notificationservice;

import android.content.Context;

import com.identity.arx.db.CourseDetailTable;
import com.identity.arx.db.LectureDetailTable;
import com.identity.arx.faculty.TakeAttendanceActivity;
import com.identity.arx.general.AppNotification;
import com.identity.arx.general.AppSharedPrefrence;
import com.identity.arx.general.CalanderFormat;
import com.identity.arx.general.TimeTrap;
import com.identity.arx.objectclass.CourseDetailsObject;
import com.identity.arx.objectclass.LectureScheduleObject;
import com.identity.arx.student.GiveAttendanceActivity;

public class LectureScheduleNotification {
    CourseDetailTable courseDetailTable;
    Context mContext;

    public LectureScheduleNotification(Context mContext) {
        this.mContext = mContext;
    }

    public void currentLecture() {
        this.courseDetailTable = new CourseDetailTable(this.mContext);
        LectureDetailTable lectureDetailTable = new LectureDetailTable(this.mContext);
        LectureScheduleObject lectureScheduleObject = getCurrentLecture(lectureDetailTable);
        if (lectureScheduleObject != null && lectureScheduleObject != null) {
            CourseDetailsObject courseDetailsObject = this.courseDetailTable.getCourseOverId(lectureScheduleObject.getCourseId() + "");
            String userType = AppSharedPrefrence.getSharedPrefrence(this.mContext).getString("USER_TYPE", "");
            if (lectureScheduleObject.getNotifaction_status().equals("0")) {
                if (userType.equalsIgnoreCase("Faculty")) {
                    AppNotification.setNotificationMessage(this.mContext, "Lecture Reminder", "Lecture Time : " + lectureScheduleObject.getLectureStartTime() + "\n Course          :" + courseDetailsObject.getCourseName() + "\n Room No.      :" + lectureScheduleObject.getLectureLocation() + "\n Course Id       : " + courseDetailsObject.getCourseId(), 2, TakeAttendanceActivity.class);
                    lectureDetailTable.updateLectureNotification(Integer.valueOf(lectureScheduleObject.getId()), "1");
                    return;
                }
                AppNotification.setNotificationMessage(this.mContext, "Lecture Reminder", "Lecture Time :" + lectureScheduleObject.getLectureStartTime() + "\n Course          :" + courseDetailsObject.getCourseName() + "\n Room No.      :" + lectureScheduleObject.getLectureLocation() + "\n Faculty       :" + courseDetailsObject.getAssignFaculty(), 2, GiveAttendanceActivity.class);
                lectureDetailTable.updateLectureNotification(Integer.valueOf(lectureScheduleObject.getId()), "1");
            } else if (TimeTrap.getTimeDifference(lectureScheduleObject.getLectureStartTime(), TimeTrap.getAdditionTime(10)) > 0) {
                lectureDetailTable.updateLectureNotification(Integer.valueOf(lectureScheduleObject.getId()), "0");
            }
        }
    }

    LectureScheduleObject getCurrentLecture(LectureDetailTable lectureDetailTable) {
        for (LectureScheduleObject lectureScheduleObject : lectureDetailTable.getCurrentDate(CalanderFormat.getDayofWeek())) {
            long timeDiff = TimeTrap.getTimeDifference(CalanderFormat.getTimeCalender(), lectureScheduleObject.getLectureStartTime());
            if (timeDiff <= 10 && timeDiff >= 0) {
                return lectureScheduleObject;
            }
        }
        return null;
    }
}
