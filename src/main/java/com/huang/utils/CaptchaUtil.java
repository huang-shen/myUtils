package com.huang.utils;

import com.huang.utils.entity.to.CaptchaTO;
import com.wf.captcha.*;

/**
 * @author HuangShen
 * @Description 图形验证码 工具类
 * @create 2021-10-15 8:34
 */
public class CaptchaUtil {


    /**
     * 创建图形验证码
     * @param type 验证码类型
     * @return CaptchaTO
     */
    public static CaptchaTO creatCaptcha(String type) {


        switch (type) {
            case "Spec":
                SpecCaptcha specCaptcha = new SpecCaptcha(130, 48);
                return CaptchaTO.builder()
                        .img(specCaptcha.toBase64())
                        .text(specCaptcha.text().toLowerCase())
                        .type(type).build();
            case "Gif":
                GifCaptcha gifCaptcha = new GifCaptcha(130, 48);
                return CaptchaTO.builder()
                        .img(gifCaptcha.toBase64())
                        .text(gifCaptcha.text().toLowerCase())
                        .type(type).build();
            case "Chinese":
                ChineseCaptcha chineseCaptcha = new ChineseCaptcha(130, 48);
                return CaptchaTO.builder()
                        .img(chineseCaptcha.toBase64())
                        .text(chineseCaptcha.text().toLowerCase())
                        .type(type).build();
            case "ChineseGif":
                ChineseGifCaptcha chineseGifCaptcha = new ChineseGifCaptcha(130, 48);
                return CaptchaTO.builder()
                        .img(chineseGifCaptcha.toBase64())
                        .text(chineseGifCaptcha.text().toLowerCase())
                        .type(type).build();
            case "Arithmetic":
                ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(130, 48);
                return CaptchaTO.builder()
                        .img(arithmeticCaptcha.toBase64())
                        .text(arithmeticCaptcha.text().toLowerCase())
                        .type(type).build();
            default:
                return  null;
        }


    }
}
