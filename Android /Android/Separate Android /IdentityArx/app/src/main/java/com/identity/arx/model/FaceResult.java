package com.identity.arx.model;

import android.graphics.PointF;

public class FaceResult {
    private float confidence = 0.4f;
    private float eyeDist = 0.0f;
    private int id = 0;
    private PointF midEye = new PointF(0.0f, 0.0f);
    private float pose = 0.0f;
    private long time = System.currentTimeMillis();

    public void setFace(int id, PointF midEye, float eyeDist, float confidence, float pose, long time) {
        set(id, midEye, eyeDist, confidence, pose, time);
    }

    public void clear() {
        set(0, new PointF(0.0f, 0.0f), 0.0f, 0.4f, 0.0f, System.currentTimeMillis());
    }

    public synchronized void set(int id, PointF midEye, float eyeDist, float confidence, float pose, long time) {
        this.id = id;
        this.midEye.set(midEye);
        this.eyeDist = eyeDist;
        this.confidence = confidence;
        this.pose = pose;
        this.time = time;
    }

    public float eyesDistance() {
        return this.eyeDist;
    }

    public void setEyeDist(float eyeDist) {
        this.eyeDist = eyeDist;
    }

    public void getMidPoint(PointF pt) {
        pt.set(this.midEye);
    }

    public PointF getMidEye() {
        return this.midEye;
    }

    public void setMidEye(PointF midEye) {
        this.midEye = midEye;
    }

    public float getConfidence() {
        return this.confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public float getPose() {
        return this.pose;
    }

    public void setPose(float pose) {
        this.pose = pose;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
