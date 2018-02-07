package com.identity.arx.objectclass;

public class AssignmentObject {
    private String assignment_Title;
    private String courseId;
    private String courseType;
    private String due_Date;
    private String labelId;
    private Integer loginStatus;
    private String message;
    private String path;
    private String rollNo;
    private String tot_marks;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAssignment_Title() {
        return this.assignment_Title;
    }

    public void setAssignment_Title(String assignment_Title) {
        this.assignment_Title = assignment_Title;
    }

    public String getDue_Date() {
        return this.due_Date;
    }

    public String getLabelId() {
        return this.labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public void setDue_Date(String due_Date) {
        this.due_Date = due_Date;
    }

    public String getTot_marks() {
        return this.tot_marks;
    }

    public void setTot_marks(String tot_marks) {
        this.tot_marks = tot_marks;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseType() {
        return this.courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public Integer getLoginStatus() {
        return this.loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getRollNo() {
        return this.rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
