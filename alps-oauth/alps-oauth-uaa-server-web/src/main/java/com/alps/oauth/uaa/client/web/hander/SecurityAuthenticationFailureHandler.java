package com.alps.oauth.uaa.client.web.hander;

import com.alps.common.json.JSONObject;
import com.alps.oauth.uaa.client.web.priorities.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author:Yujie.lee
 * Date:2020年2月14日
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

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws
            IOException,
            ServletException {
        log.info("登陆失败...{}",exception.getMessage());
        JSONObject res = new JSONObject();
        res.put("msg",exception.getMessage());
        res.put("code","1");
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().append(res.toString());
        //super.onAuthenticationFailure(request, response, exception);
    }

}
