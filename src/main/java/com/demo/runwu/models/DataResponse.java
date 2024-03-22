package com.demo.runwu.models;

import org.springframework.lang.Nullable;

import java.io.Serializable;

public class DataResponse<T> extends BaseResponse {

    @Nullable
    private T data;

    public DataResponse(int code, String message, @Nullable T data) {
        super(code,message);
        this.data = data;
    }

    public DataResponse(@Nullable T data) {
        this(200, "success", data);
    }

    public DataResponse<T> isSuccess() {
        this.setCode(200);
        return this;
    }

    public DataResponse<T> isSuccess(int code) {
        this.setCode(code);
        return this;
    }

    public DataResponse<T> isError() {
        this.setCode(500);
        return this;
    }

    public DataResponse<T> isError(int code) {
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
