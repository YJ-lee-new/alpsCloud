package com.alps.oauth.uaa.admin.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理器
 */
@Component
@Slf4j
public class SecurityAuthenticationSuccessHandler extends
                                                  SavedRequestAwareAuthenticationSuccessHandler{

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws
                                                                       IOException,
            ServletException {
        log.info("登陆成功...{}",authentication);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
