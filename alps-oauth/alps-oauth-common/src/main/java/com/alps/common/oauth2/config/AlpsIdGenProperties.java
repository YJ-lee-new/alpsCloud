package com.alps.common.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 *  自定义ID生成器配置
 */
@ConfigurationProperties(prefix = "alps.id")
public class AlpsIdGenProperties {
    /**
     * 工作ID (0~31)
     */
    private long workId = 0;
    /**
     * 数据中心ID (0~31)
     */
    private long centerId = 0;

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public long getCenterId() {
        return centerId;
    }

    public void setCenterId(long centerId) {
        this.centerId = centerId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AlpsIdGenProperties{");
        sb.append("workId=").append(workId);
        sb.append(", centerId=").append(centerId);
        sb.append('}');
        return sb.toString();
    }
}
