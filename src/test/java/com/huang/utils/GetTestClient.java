package com.huang.utils;

import com.alibaba.fastjson2.JSON;
import com.huang.utils.http.AbstractHttpClient;
import com.huang.utils.http.HttpRequestHeader;
import com.huang.utils.http.HttpRequestParam;
import org.springframework.http.HttpMethod;

/**
 * @Author HuangShen
 * @Date 2022/7/3 20:33
 * @Describe
 */
public class GetTestClient  extends AbstractHttpClient<UserRequest,String> {

    @Override
    protected HttpMethod getHttpRequestMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected HttpRequestParam getHttpRequestParam(UserRequest unused) {
        HttpRequestParam httpRequestParam = new HttpRequestParam();
        httpRequestParam.UrlParamBuild(JSON.toJSONString(unused));
        return httpRequestParam;
    }

    @Override
    protected HttpRequestHeader getHttpRequestHeader(UserRequest unused, HttpRequestParam httpRequestParam) {
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader();
        httpRequestHeader.addHeader("header1","test");
        return httpRequestHeader;
    }

    @Override
    protected String getHttpUrl() {
        return "http://127.0.0.1:8899/httpclient-demo/test1/";
    }

    @Override
    protected String onSuccess(UserRequest unused, String result) {
        return result;
    }
}
