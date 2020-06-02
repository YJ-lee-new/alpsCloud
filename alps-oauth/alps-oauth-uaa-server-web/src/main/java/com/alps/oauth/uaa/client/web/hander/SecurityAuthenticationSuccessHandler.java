package com.alps.oauth.uaa.client.web.hander;

import com.alps.common.json.JSONObject;
import com.alps.oauth.uaa.client.web.priorities.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author:Yujie.lee
 * Date:2020年2月14日
 * 登录成功处理器
 */
@Component
@Slf4j
public class SecurityAuthenticationSuccessHandler extends
                                                  SavedRequestAwareAuthenticationSuccessHandler{
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws
                                                                       IOException,
            ServletException {
        log.info("登陆成功...{}",authentication);
        JSONObject res = new JSONObject();
        res.put("success",true);
        res.put("msg","登录成功");
        res.put("code","0");
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().append(res.toString());
       // super.onAuthenticationSuccess(request, response, authentication);
    }

}
