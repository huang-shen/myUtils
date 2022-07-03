package com.huang.utils;

import com.huang.utils.http.HttpClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author HuangShen
 * @Date 2022/7/3 09:13
 * @Describe
 */
@Slf4j
public class HttpClientTest {

    @Test
    public void demoTest() {

        GetTestClient getTestClient = new GetTestClient();
        String res = null;
        try {
            UserRequest request = new UserRequest();
            request.setId(12342l);
            request.setPassword("sdcnsdkjv");
            request.setUserName("sdkjcvbsd");
            res = getTestClient.sendRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(res);
        Thread.yield();


    }


    @Test
    public void postTest() throws IOException {
        PostTestClient postTestClient = new PostTestClient();
        UserRequest request = new UserRequest();
        request.setId(12342l);
        request.setPassword("sdcnsdkjv");
        request.setUserName("sdkjcvbsd");
        String res = postTestClient.sendRequest(request);
        log.info(res);
    }
}
