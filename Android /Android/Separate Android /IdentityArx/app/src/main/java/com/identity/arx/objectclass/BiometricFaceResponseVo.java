package com.identity.arx.objectclass;

public class BiometricFaceResponseVo {
    private String distance;
    private String message;
    private String passImage;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassImage() {
        return this.passImage;
    }

    public void setPassImage(String passImage) {
        this.passImage = passImage;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
