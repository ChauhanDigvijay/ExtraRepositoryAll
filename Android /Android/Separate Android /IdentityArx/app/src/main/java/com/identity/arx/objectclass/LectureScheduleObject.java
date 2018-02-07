package com.identity.arx.objectclass;

public class LectureScheduleObject {
    private int assignedFacultyId;
    private int courseId;
    private int deptId;
    FacultyLectureAttendance fac;
    private int id;
    private double latitude;
    private String lectureDate;
    private String lectureDay;
    private String lectureEndTime;
    private String lectureLocation;
    private String lectureStartTime;
    private double longitude;
    private String notifaction_status;

    public FacultyLectureAttendance getFac() {
        return this.fac;
    }

    public void setFac(FacultyLectureAttendance fac) {
        this.fac = fac;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNotifaction_status() {
        return this.notifaction_status;
    }

    public void setNotifaction_status(String notifaction_status) {
        this.notifaction_status = notifaction_status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getDeptId() {
        return this.deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getLectureStartTime() {
        return this.lectureStartTime;
    }

    public void setLectureStartTime(String lectureStartTime) {
        this.lectureStartTime = lectureStartTime;
    }

    public String getLectureEndTime() {
        return this.lectureEndTime;
    }

    public void setLectureEndTime(String lectureEndTime) {
        this.lectureEndTime = lectureEndTime;
    }

    public String getLectureLocation() {
        return this.lectureLocation;
    }

    public void setLectureLocation(String lectureLocation) {
        this.lectureLocation = lectureLocation;
    }

    public String getLectureDay() {
        return this.lectureDay;
    }

    public void setLectureDay(String lectureDay) {
        this.lectureDay = lectureDay;
    }

    public int getAssignedFacultyId() {
        return this.assignedFacultyId;
    }

    public void setAssignedFacultyId(int assignedFacultyId) {
        this.assignedFacultyId = assignedFacultyId;
    }

    public String getLectureDate() {
        return this.lectureDate;
    }

    public void setLectureDate(String lectureDate) {
        this.lectureDate = lectureDate;
    }
}
