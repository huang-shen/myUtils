package com.huang.utils.http;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author HuangShen
 * @Date 2022/7/3 18:51
 * @Describe
 */
public class HttpRequestHeader {

    private final List<Header> headers = new ArrayList<>(32);


    public boolean addHeader(String name, String value) {
        return headers.add(new BasicHeader(name, value));
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public HttpRequestHeader() {
    }
}
