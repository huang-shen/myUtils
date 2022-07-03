package com.huang.utils.http;


import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangshen
 */
@Slf4j
public class HttpClientFactory {
    private static final CloseableHttpClient closeableHttpClient;
    private static final PoolingHttpClientConnectionManager cm;

    static {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        /*
         一、绕过不安全的https请求的证书验证
         */
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", trustHttpsCertificates()).build();
        /*
          二、创建连接池管理对象
         */
        cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(50); // 连接池最大有50个连接,<=20
        /*
            roadjava.com域名/ip+port 就是一个路由，
            http://www.roadjava.com/s/spsb/beanvalidation/
            http://www.roadjava.com/s/1.html
            https://www.baidu.com/一个域名，又是一个新的路由
         */
        cm.setDefaultMaxPerRoute(50); // 每个路由默认有多少连接,<=2
        httpClientBuilder.setConnectionManager(cm);
        /*
        三、设置请求默认配置
         */
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(3000).setConnectionRequestTimeout(5000).build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        /*
        四、设置默认的一些header
         */
        List<Header> defaultHeaders = new ArrayList<>();
        BasicHeader userAgentHeader = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        defaultHeaders.add(userAgentHeader);
        httpClientBuilder.setDefaultHeaders(defaultHeaders);

        // 线程安全,此处初始化一次即可,通过上面的配置来生成一个用于管理多个连接的连接池closeableHttpClient
        closeableHttpClient = httpClientBuilder.build();
    }

    /**
     * 构造安全连接工厂
     *
     * @return SSLConnectionSocketFactory
     */
    private static ConnectionSocketFactory trustHttpsCertificates() {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        try {
            // 判断是否信任url
            sslContextBuilder.loadTrustMaterial(null, (chain, authType) -> true);
            SSLContext sslContext = sslContextBuilder.build();
            return new SSLConnectionSocketFactory(sslContext, new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            log.error("构造安全连接工厂失败", e);
            throw new RuntimeException("构造安全连接工厂失败");
        }
    }


    public static ResponseEntity<String> executeRequest(String url, HttpMethod httpMethod, HttpRequestHeader headers, HttpRequestParam httpRequestParam) {


        HttpRequestParam.HttpRequestParamType paramType = httpRequestParam.getParamType();
        // 去除结尾的"/"
        if (StringUtils.endsWith(url, "/")) {
            url = url.substring(0, url.lastIndexOf("/") + 1);
        }
        switch (httpMethod) {
            case GET: {
                return executeGet(url, httpRequestParam.getParameter(), headers);
            }
            case POST: {
                if (paramType == HttpRequestParam.HttpRequestParamType.BODY) {
                    return postJson(url, httpRequestParam.getParameter(), headers);
                } else {
                    return postForm(url, httpRequestParam.getParameter(), headers);
                }
            }
        }
        throw new IllegalArgumentException("不支持的请求方式" + httpMethod);

    }

    /**
     * 发送get请求
     *
     * @param url     请求url,参数需经过URLEncode编码处理
     * @param headers 自定义请求头
     * @return 返回结果
     */
    private static ResponseEntity<String> executeGet(String url, JSONObject params, HttpRequestHeader headers) {
        if (params != null) {
            ArrayList<NameValuePair> parameters = new ArrayList<>();
            params.forEach((key, value) -> parameters.add(new BasicHeader(key, value.toString())));
            url = url + "?" + URLEncodedUtils.format(parameters, StandardCharsets.UTF_8);
        }
        // 构造httpGet请求对象
        HttpGet httpGet = new HttpGet(url);
        // 自定义请求头设置
        if (headers != null) {
            for (Header header : headers.getHeaders()) {
                httpGet.addHeader(header);
            }
        }
        return getStringResponseEntity(url, httpGet);
    }


    /**
     * 发送表单类型的post请求
     *
     * @param url     要请求的url
     * @param body    参数列表
     * @param headers 自定义头
     * @return 返回结果
     */
    private static ResponseEntity<String> postForm(String url, JSONObject body, HttpRequestHeader headers) {
        HttpPost httpPost = new HttpPost(url);

        if (headers != null) {
            for (Header header : headers.getHeaders()) {
                httpPost.addHeader(header);
            }
        }
        // 确保请求头一定是form类型
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");
        if (body != null) {
            ArrayList<NameValuePair> parameters = new ArrayList<>();
            body.forEach((key, value) -> parameters.add(new BasicHeader(key, value.toString())));
            // 给post对象设置参数
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, Consts.UTF_8);
            httpPost.setEntity(formEntity);
        }

        return getStringResponseEntity(url, httpPost);
    }


    /**
     * 发送json类型的post请求
     *
     * @param url     请求url
     * @param body    json字符串
     * @param headers 自定义header
     * @return 返回结果
     */
    private static ResponseEntity<String> postJson(String url, JSONObject body, HttpRequestHeader headers) {
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        if (headers != null) {
            for (Header header : headers.getHeaders()) {
                httpPost.addHeader(header);
            }
        }
        // 确保请求头是json类型
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        /*
        设置请求体
         */
        StringEntity jsonEntity = new StringEntity(body.toJSONString(), Consts.UTF_8);
        jsonEntity.setContentType("application/json; charset=utf-8");
        jsonEntity.setContentEncoding(Consts.UTF_8.name());
        httpPost.setEntity(jsonEntity);

        return getStringResponseEntity(url, httpPost);
    }


    private static void printStat() {
        // 连接池的最大连接数 50
//        log.info("cm.getMaxTotal():{}",cm.getMaxTotal());
        // 每一个路由的最大连接数 50
//        log.info("cm.getDefaultMaxPerRoute():{}",cm.getDefaultMaxPerRoute());
//        PoolStats totalStats = cm.getTotalStats();
        // 连接池的最大连接数 50
//        log.info("totalStats.getMax():{}",totalStats.getMax());
        // 连接池里面有多少连接是被占用了
//        log.info("totalStats.getLeased():{}", totalStats.getLeased());
        // 连接池里面有多少连接是可用的
//        log.info("totalStats.getAvailable():{}", totalStats.getAvailable());
    }

    private static void consumeRes(CloseableHttpResponse response) {
        // response.close();是关闭连接,不是归还连接到连接池
        if (response != null) {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                log.error("consume出错", e);
            }
        }
    }

    private static ResponseEntity<String> getStringResponseEntity(String url, HttpRequestBase httpRequestBase) {
        // 可关闭的响应
        CloseableHttpResponse response = null;
        try {
            log.info("prepare to execute url:{}", url);
            response = closeableHttpClient.execute(httpRequestBase);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.OK.value()) {
                HttpEntity entity = response.getEntity();
                String resBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                return new ResponseEntity<>(resBody, HttpStatus.valueOf(statusLine.getStatusCode()));
            } else {
                log.error("executeGet error,url:{}, reason:{}", url, HttpStatus.valueOf(statusLine.getStatusCode()));
                return null;
            }
        } catch (Exception e) {
            log.error("executeGet error,url:{}, reason:{}", url, e.getMessage());
        } finally {
            consumeRes(response);
        }
        return null;
    }
}
