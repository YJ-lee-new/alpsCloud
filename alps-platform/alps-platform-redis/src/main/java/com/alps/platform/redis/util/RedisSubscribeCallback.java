package com.alps.platform.redis.util;
 
/**
 * @author:Yujie.lee
 * Date:2019年11月21日
 * TodoTODO
 */
public interface RedisSubscribeCallback {
    void callback(String msg);
}
