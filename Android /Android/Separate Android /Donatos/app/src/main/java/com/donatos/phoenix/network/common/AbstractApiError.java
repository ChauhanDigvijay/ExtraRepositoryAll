package com.donatos.phoenix.network.common;

abstract class AbstractApiError {
    private String errorCode;
    private String errorMessage;
    private String result;

    AbstractApiError() {
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getResult() {
        return this.result;
    }
}
