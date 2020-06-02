package com.alps.oauth.uaa.client.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alps.oauth.uaa.client.web.priorities.SecurityProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author:Yujie.lee
 * Date:2020年2月14日
 */
@Controller
@RequestMapping("/alps")
public class AuthController {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    private SecurityProperties securityProperties;

    @RequestMapping("/requrie")
    @ResponseBody
    public String requrie(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, "/alps/login");
        }
        return "引导到登录页";
    }
    
    @RequestMapping("/test")
    public ModelAndView test() {
        return new ModelAndView("/index2");
    }

    @RequestMapping("/login")
    public ModelAndView toLogin() {
        return new ModelAndView(securityProperties.getLoginPageHtml());
    }
    

}
