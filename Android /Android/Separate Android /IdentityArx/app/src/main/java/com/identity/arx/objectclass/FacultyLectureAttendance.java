package com.identity.arx.objectclass;

import java.io.Serializable;

public class FacultyLectureAttendance implements Serializable {
    private double altitudeQr;
    private int courseId;
    private int createdBy;
    private String id;
    private double latQr;
    private int lectureId;
    private double longQr;
    private int qrExpirationTime;
    private long qrGenerationTime;
    private String randomUniqueNo;
    private String status;

    public double getLatQr() {
        return this.latQr;
    }

    public void setLatQr(double latQr) {
        this.latQr = latQr;
    }

    public double getLongQr() {
        return this.longQr;
    }

    public String getRandomUniqueNo() {
        return this.randomUniqueNo;
    }

    public void setRandomUniqueNo(String randomUniqueId) {
        this.randomUniqueNo = this.randomUniqueNo;
    }

    public void setLongQr(double longQr) {
        this.longQr = longQr;
    }

    public double getAltitudeQr() {
        return this.altitudeQr;
    }

    public void setAltitudeQr(double altitudeQr) {
        this.altitudeQr = altitudeQr;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLectureId() {
        return this.lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public long getQrGenerationTime() {
        return this.qrGenerationTime;
    }

    public void setQrGenerationTime(long qrGenerationTime) {
        this.qrGenerationTime = qrGenerationTime;
    }

    public int getQrExpirationTime() {
        return this.qrExpirationTime;
    }

    public void setQrExpirationTime(int qrExpirationTime) {
        this.qrExpirationTime = qrExpirationTime;
    }

    public int getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
