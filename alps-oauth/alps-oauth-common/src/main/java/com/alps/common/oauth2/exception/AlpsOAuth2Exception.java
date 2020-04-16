package com.alps.common.oauth2.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@JsonSerialize(using = AlpsOAuth2ExceptionSerializer.class)
public class AlpsOAuth2Exception extends org.springframework.security.oauth2.common.exceptions.OAuth2Exception {
    private static final long serialVersionUID = 4257807899611076101L;

    public AlpsOAuth2Exception(String msg) {
        super(msg);
    }
}