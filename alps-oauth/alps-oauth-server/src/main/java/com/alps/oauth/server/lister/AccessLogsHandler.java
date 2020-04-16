package com.alps.oauth.server.lister;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

import com.alps.common.oauth2.constants.QueueConstants;
import com.alps.common.oauth2.utils.BeanConvertUtils;
import com.alps.oauth.server.service.IpRegionService;

import java.util.Map;

/**
 * mq消息接收者
 *
 * @author YJ.lee
 */
@Configuration
@Slf4j
public class AccessLogsHandler {


    /**
     * 临时存放减少io
     */
    @Autowired
    private IpRegionService ipRegionService;

}
