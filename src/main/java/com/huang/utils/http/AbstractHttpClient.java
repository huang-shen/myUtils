package com.huang.utils.http;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author HuangShen
 * @Date 2022/7/3 14:16
 * @Describe
 */
@Slf4j
public abstract class AbstractHttpClient<REQUEST, RESPONSE> {

    /**
     * 请求方式
     *
     * @return String
     */
    protected abstract HttpMethod getHttpRequestMethod();

    /**
     * 请求参数
     *
     * @param request 参数
     * @return HttpRequestParam
     */
    protected abstract HttpRequestParam getHttpRequestParam(REQUEST request);

    /**
     * 请求头
     *
     * @param request          请求参数
     * @param httpRequestParam 请求参数
     * @return HttpHeaders
     */
    protected abstract HttpRequestHeader getHttpRequestHeader(REQUEST request, HttpRequestParam httpRequestParam);


    /**
     * 请求路径
     *
     * @return String
     */
    protected abstract String getHttpUrl();


    /**
     * 发送请求
     *
     * @param request
     * @return
     */
    public RESPONSE sendRequest(REQUEST request) {
        ResponseEntity<String> responseEntity = null;
        String httpUrl = getHttpUrl();
        HttpMethod httpMethod = getHttpRequestMethod();
        HttpRequestParam requestParam = getHttpRequestParam(request);
        HttpRequestHeader httpRequestHeader = getHttpRequestHeader(request, requestParam);
        try {
            responseEntity = retryer.call(() -> {
                ResponseEntity<String> response = HttpClientFactory.executeRequest(httpUrl, httpMethod, httpRequestHeader, requestParam);
                if (response == null) {
                    throw new Exception();
                }
                return response;
            });

        } catch (ExecutionException | RetryException e) {
            log.error("请求出现异常:{}", e.getMessage(), e);
            throw new RuntimeException("第三方接口调用异常");
        }


        return onSuccess(request, responseEntity.getBody());
    }


    /**
     * 重试工具类变量
     */
    private final Retryer<ResponseEntity<String>> retryer = RetryerBuilder.<ResponseEntity<String>>newBuilder().withRetryListener(new RetryListener() {
                @Override
                public <V> void onRetry(Attempt<V> attempt) {
                    if (attempt.hasException()) {
                        log.error("请求失败，{}毫秒后开始第{}次重试", attempt.getDelaySinceFirstAttempt(), attempt.getAttemptNumber(), attempt.getExceptionCause());
                    }
                }
            }).withWaitStrategy(WaitStrategies.fixedWait(1000, TimeUnit.MILLISECONDS))
            .withStopStrategy(StopStrategies.stopAfterAttempt(3)).retryIfException().build();

    /**
     * 远程请求成功（不报错、返回值不为Null）后触发执行
     *
     * @param request 请求参数
     * @param result  响应体
     * @return
     */
    protected abstract RESPONSE onSuccess(REQUEST request, String result);


}
