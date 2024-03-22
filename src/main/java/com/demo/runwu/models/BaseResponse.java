package com.demo.runwu.models;

import org.springframework.lang.Nullable;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {
    protected int code;
    protected String message;

    @Nullable
    private T data;

    public BaseResponse(String message) {
        this.code = 200;
        this.message = message;
        this.data = null;
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public BaseResponse(int code, String message, @Nullable T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(@Nullable T data) {
        this(200, "success", data);
    }

    public BaseResponse<T> isSuccess() {
        this.setCode(200);
        return this;
    }

    public BaseResponse<T> isSuccess(int code) {
        this.setCode(code);
        return this;
    }

    public BaseResponse<T> isError() {
        this.setCode(500);
        return this;
    }

    public BaseResponse<T> isError(int code) {
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

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }
}
