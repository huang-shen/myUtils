package com.huang.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Author HuangShen
 * @Date 2022/7/3 09:13
 * @Describe
 */
public class HttpClientTest {

    @Test
    public void demoTest() {


        // 打开浏览器
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 输入url地址
        HttpGet httpGet = new HttpGet("https://www.baidu.com/");
        CloseableHttpResponse response = null;
        try {
            // 敲回车，发送请求，获取响应
            response = httpClient.execute(httpGet);
            // 获取内容
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpGet.releaseConnection();
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    @Test
    public void postTest() throws IOException {
        // 打开浏览器
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("");
        StringEntity stringEntity = new StringEntity("","");
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse execute = httpClient.execute(httpPost);
        HttpEntity entity = execute.getEntity();
    }
}
