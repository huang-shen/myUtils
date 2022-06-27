package com.huang.utils.entity.enums;

/**
 * @author HuangShen
 * @Description redis key
 * @create 2021-10-17 20:43
 */
public enum RedisKeyPrefixes {
    CaptchaKeyString("图形验证码key","FOUNDATION:CAPTCH:","String"),
    SmsCodeKeyString("短信验证码key","FOUNDATION:SMS:","String"),
    SensitiveWordsList("敏感词词库key","FOUNDATION:SENSW:","List"),
    VideoActionKeyHash("视频操作key","VIDEO:ACTION:","Hash"),
    VideoActionPlayKeyHash("视频播放key","VIDEO:ACTION:PLAY","Hash"),
    VideoActionCollectionKeyHash("视频收藏key","VIDEO:ACTION:COLLECTION","Hash"),
    VideoActionForwardingKeyHash("视频转发key","VIDEO:ACTION:FORWARDING","Hash"),
    VideoActionPraiseKeyHash("视频点赞key","VIDEO:ACTION:PRAISE","Hash"),

    ;
    // key 描述
    private String des;
    // key 前缀
    private String keyPrefix;
    // key 类型
    private String type;

    RedisKeyPrefixes(String des, String key, String type) {
        this.des = des;
        this.keyPrefix = key;
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
