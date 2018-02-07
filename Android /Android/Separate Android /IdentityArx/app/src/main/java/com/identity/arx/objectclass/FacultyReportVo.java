package com.identity.arx.objectclass;

public class FacultyReportVo {
    private String StudentName;
    private int attendancePercentage;
    private String contactNumber;
    private String courseID;
    private String deptName;
    private String email;
    private int loginStatus;
    private String persuingSemester;
    private String persuingYear;
    private String rollNo;

    public String getStudentName() {
        return this.StudentName;
    }

    public void setStudentName(String StudentName) {
        this.StudentName = StudentName;
    }

    public String getRollNo() {
        return this.rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getPersuingYear() {
        return this.persuingYear;
    }

    public void setPersuingYear(String persuingYear) {
        this.persuingYear = persuingYear;
    }

    public String getPersuingSemester() {
        return this.persuingSemester;
    }

    public void setPersuingSemester(String persuingSemester) {
        this.persuingSemester = persuingSemester;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAttendancePercentage() {
        return this.attendancePercentage;
    }

    public void setAttendancePercentage(int attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public String getCourseID() {
        return this.courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public int getLoginStatus() {
        return this.loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
}
