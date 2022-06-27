package com.huang.utils.entity.to;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HuangShen
 * @Description 验证token
 * @create 2022-01-23 8:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenValResultTO {
    /**
     * 响应 状态码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String msg;

    /**
     * 有效期
     */
    private Long expiresAt;

    /**
     *  token信息
     */
    private String tokenInfo;


}
