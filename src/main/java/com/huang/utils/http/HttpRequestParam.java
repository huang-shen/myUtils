package com.huang.utils.http;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

/**
 * 请求对象。k-v形式参数和body两种方式只能二选一。如果两者都有则以k-v为准。
 */
public class HttpRequestParam implements Serializable {
    public enum HttpRequestParamType {
        //参数是请求体
        BODY,
        //参数将拼接到url上
        URL
    }

    // 参数类型
    @Getter
    private HttpRequestParamType paramType;

    @Getter
    private JSONObject parameter;

    /**
     * 构造请求体参数对象
     *
     * @param body
     */
    public void bodyParamBuild(String body) {
        this.parameter = JSON.parseObject(body);
        this.paramType = HttpRequestParamType.BODY;
    }

    /**
     * 构造拼接URL的请求参数对象
     *
     * @param parameter url参数
     */
    public void UrlParamBuild(String parameter) {
        this.parameter = JSON.parseObject(parameter);
        this.paramType = HttpRequestParamType.URL;
    }

    /**
     * 根据参数构造相应的请求参数对象
     *
     * @param paramType 参数类型
     * @param parameter 参数
     */
    public HttpRequestParam(HttpRequestParamType paramType, String parameter) {
        this.parameter = JSON.parseObject(parameter);
        this.paramType = paramType;
    }

    public HttpRequestParam() {
    }
}