package com.demo.runwu.models;

import org.springframework.lang.Nullable;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    protected int code;
    protected String message;


    public BaseResponse(String message) {
        this.code = 200;
        this.message = message;
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse isSuccess() {
        this.setCode(200);
        return this;
    }

    public BaseResponse isSuccess(int code) {
        this.setCode(code);
        return this;
    }

    public BaseResponse isError() {
        this.setCode(500);
        return this;
    }

    public BaseResponse isError(int code) {
        this.setCode(code);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

}
