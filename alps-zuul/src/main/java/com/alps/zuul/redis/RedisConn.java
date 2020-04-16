package com.alps.zuul.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:Yujie.lee
 * Date:2019年10月27日
 * TodoTODO
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConn {
    private String host;

    private int port;

    private String password;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

