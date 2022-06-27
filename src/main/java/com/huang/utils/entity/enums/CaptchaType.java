package com.huang.utils.entity.enums;

/**
 * @author HuangShen
 * @Description 图形验证码 类型
 * @create 2021-10-17 20:11
 */
public enum CaptchaType {
    SpecCaptcha("普通类型","Spec"),
    GifCaptcha("GIF类型","Gif"),
    ChineseCaptcha("中文类型","Chinese"),
    ChineseGifCaptcha("中文GIF类型","ChineseGif"),
    ArithmeticCaptcha("算术类型","Arithmetic"),
    ;


    private String name;
    private String code;

    CaptchaType(String name, String code) {
        this.name = name;
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
