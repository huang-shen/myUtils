package com.huang.utils.entity.to;

import lombok.Builder;
import lombok.Data;

/**
 * @author HuangShen
 * @Description 图形验证码
 * @create 2021-10-15 8:49
 */
@Data
@Builder
public class CaptchaTO {

    // 验证嘛类型
    String type;
    // 验证码 字符串
    String text;
    // base64 编码 图片
    String img;
}
