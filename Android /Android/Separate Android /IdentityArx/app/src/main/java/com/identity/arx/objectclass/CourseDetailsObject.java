package com.identity.arx.objectclass;

public class CourseDetailsObject {
    private String assignFaculty;
    private String courseId;
    private String courseName;
    private int id;
    private int noOfLectures;

    public int getNoOfLectures() {
        return this.noOfLectures;
    }

    public void setNoOfLectures(int noOfLectures) {
        this.noOfLectures = noOfLectures;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssignFaculty() {
        return this.assignFaculty;
    }

    public void setAssignFaculty(String assignFaculty) {
        this.assignFaculty = assignFaculty;
    }
}
