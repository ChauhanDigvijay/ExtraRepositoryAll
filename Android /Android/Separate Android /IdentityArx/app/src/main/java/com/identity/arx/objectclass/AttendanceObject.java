package com.identity.arx.objectclass;

import java.io.Serializable;

public class AttendanceObject implements Serializable {
    private long attendanceTime;
    private String attendence;
    private String courseId;
    private String deviceId;
    private int facultyQr;
    private String isProxy;
    private int lectureid;
    private String reason;
    private String rollNum;
    private String status;
    private int studentId;

    public int getFacultyQr() {
        return this.facultyQr;
    }

    public void setFacultyQr(int facultyQr) {
        this.facultyQr = facultyQr;
    }

    public String getReason() {
        return this.reason;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getLectureid() {
        return this.lectureid;
    }

    public void setLectureid(int lectureid) {
        this.lectureid = lectureid;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRollNum() {
        return this.rollNum;
    }

    public void setRollNum(String rollNum) {
        this.rollNum = rollNum;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getAttendence() {
        return this.attendence;
    }

    public void setAttendence(String attendence) {
        this.attendence = attendence;
    }

    public String getIsProxy() {
        return this.isProxy;
    }

    public void setIsProxy(String isProxy) {
        this.isProxy = isProxy;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getAttendanceTime() {
        return this.attendanceTime;
    }

    public void setAttendanceTime(long attendanceTime) {
        this.attendanceTime = attendanceTime;
    }
}
