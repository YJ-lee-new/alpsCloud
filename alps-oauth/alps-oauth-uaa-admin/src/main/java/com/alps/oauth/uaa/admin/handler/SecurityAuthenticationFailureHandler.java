package com.alps.oauth.uaa.admin.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 */
@Component
@Slf4j
public class SecurityAuthenticationFailureHandler extends
        SimpleUrlAuthenticationFailureHandler {
    @Autowired
    public SecurityAuthenticationFailureHandler() {
        super("/");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws
            IOException,
            ServletException {
        log.info("登陆失败...{}",exception.getMessage());
        super.onAuthenticationFailure(request, response, exception);
    }

}
