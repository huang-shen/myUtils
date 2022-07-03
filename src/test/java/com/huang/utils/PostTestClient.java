package com.huang.utils;

import com.alibaba.fastjson2.JSON;
import com.huang.utils.http.AbstractHttpClient;
import com.huang.utils.http.HttpRequestHeader;
import com.huang.utils.http.HttpRequestParam;
import org.springframework.http.HttpMethod;

/**
 * @Author HuangShen
 * @Date 2022/7/3 22:30
 * @Describe
 */
public class PostTestClient extends AbstractHttpClient<UserRequest,String> {
    @Override
    protected HttpMethod getHttpRequestMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected HttpRequestParam getHttpRequestParam(UserRequest userRequest) {
        HttpRequestParam httpRequestParam = new HttpRequestParam();
        httpRequestParam.bodyParamBuild(JSON.toJSONString(userRequest));
        return httpRequestParam;
    }

    @Override
    protected HttpRequestHeader getHttpRequestHeader(UserRequest userRequest, HttpRequestParam httpRequestParam) {
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader();
        httpRequestHeader.addHeader("header1","test");
        return httpRequestHeader;
    }

    @Override
    protected String getHttpUrl() {
        return "http://127.0.0.1:8899/httpclient-demo/test2";
    }

    @Override
    protected String onSuccess(UserRequest userRequest, String result) {
        return result;
    }
}
