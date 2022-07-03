package com.huang.utils.key;

/**
 * @author HuangShen
 * @Description
 * @create 2021-10-15 21:26
 */
public class ObjectIdUtils {

    // 读取配置 获取
    /**
     * 工作机器ID(0~31)
     */
    private static final long workerId;
    /**
     * 数据中心ID(0~31)
     */
    private static final long dataCenterId;

    static {
        workerId = Long.parseLong(System.getProperty("workerId"));
        dataCenterId = Long.parseLong(System.getProperty("dataCenterId"));
    }

    private static final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(workerId, dataCenterId);

    private ObjectIdUtils() {
    }


    public static SnowflakeIdWorker getInstance() {
        return snowflakeIdWorker;
    }



}
