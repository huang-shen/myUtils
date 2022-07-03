package com.huang.utils.check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author HuangShen
 * @Date 2022/3/14 08:38
 * @Describe
 */
public class HUtil {

    /**
     * 判断字符串是否为URL
     * @param urls 须要判断的String类型url
     * @return true:是URL；false:不是URL
     */
    public static boolean isHttpUrl(String urls) {
        boolean isurl = true;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        return true;
    }
}
