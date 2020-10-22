package com.example.demo;

import java.io.Serializable;

/**
 * @ClassName RestResponse
 * @Description TODO
 * @Author ma.kangkang
 * @Date 2020/10/22 12:12
 **/
public class RestResponse <T> implements Serializable {

    private static final long serialVersionUID = -4577255781088498763L;
    private static final String OK = "0";
    private static final String FAIL = "1";
    private static final String UNAUTHORIZED = "2";
    private T data;
    private String code = "0";
    private String msg = "";

    public static RestResponse ok() {
        return (new RestResponse()).setCode("0");
    }

    public static <T> RestResponse<T> ok(T data) {
        return (new RestResponse()).setCode("0").setData(data);
    }

    public static RestResponse fail() {
        return (new RestResponse()).setCode("1");
    }

    public static RestResponse unauthorized() {
        return (new RestResponse()).setCode("2");
    }

    public static RestResponse fail(Throwable e) {
        return fail().msg(e);
    }

    public RestResponse msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public RestResponse msg(Throwable e) {
        this.setMsg(e.toString());
        return this;
    }

    public RestResponse data(T data) {
        this.setData(data);
        return this;
    }

    private RestResponse() {
    }

    public T getData() {
        return this.data;
    }

    public RestResponse setData(T data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public RestResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public RestResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
