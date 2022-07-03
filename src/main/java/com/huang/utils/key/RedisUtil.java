package com.huang.utils.key;

import com.huang.utils.entity.enums.RedisKeyPrefixes;

/**
 * @author HuangShen
 * @Description redis 工具类
 * @create 2021-10-17 21:13
 */
public class RedisUtil {

    /**
     * 创建 redis key
     * @param keyPrefixes RedisKeyPrefixes
     * @param key key
     * @return String
     */
    public static String createKey(RedisKeyPrefixes keyPrefixes,String key){
        return  keyPrefixes.getKeyPrefix()+key;
    }
}
