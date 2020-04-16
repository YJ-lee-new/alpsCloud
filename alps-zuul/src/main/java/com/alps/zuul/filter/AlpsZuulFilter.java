package com.alps.zuul.filter;

import com.alps.common.core.domain.ReturnResult;
import com.alps.provider.system.domain.SysUser;
import com.alps.zuul.redis.JsonMapperTool;
import com.alps.zuul.redis.RedisUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author:Yujie.lee
 * Date:2019年11月2日
 * TodoTODO
 */
@Component //加入到Spring容器
public class AlpsZuulFilter extends ZuulFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean shouldFilter() {
        return true; // 该过滤器需要执行
    }

    @Override
    public Object run() { //编写业务逻辑
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        String requestURI = request.getRequestURI();
        if (requestURI.contains("web")) {
            return dealWeb(request, response, requestContext);
        }
        requestContext.setSendZuulResponse(false);
        return null;

    }

    public Object dealWeb(HttpServletRequest request, HttpServletResponse response, RequestContext requestContext) {
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/user/login")) {
            String account = request.getParameter("account");
            String password = request.getParameter("password");
            String loginCode = request.getParameter("loginCode");
            Cookie[] cookies = request.getCookies();
            String systemCode = null;
            for (Cookie c : cookies) {
                if (c.getName().equals("captchaCode")) {
                    String uuidCode = c.getValue();
                    systemCode = (String) redisUtil.get(uuidCode);
                    break;
                }
            }
            if (StringUtils.isEmpty(loginCode)) {
                requestContext.setSendZuulResponse(false);
                response.setCharacterEncoding("UTF-8");
                requestContext.setResponseBody(JsonMapperTool.objectToJson(ReturnResult.build(400, "验证码为空,please login again1.")));
                return null;
            }
            if (StringUtils.isEmpty(account)) {
                requestContext.setSendZuulResponse(false);
                response.setCharacterEncoding("UTF-8");
                requestContext.setResponseBody(JsonMapperTool.objectToJson(ReturnResult.build(400, "用户名为空,please login again2.")));
                return null;
            }
            if (StringUtils.isEmpty(password)) {
                requestContext.setSendZuulResponse(false);
                response.setCharacterEncoding("UTF-8");
                requestContext.setResponseBody(JsonMapperTool.objectToJson(ReturnResult.build(400, "密码为空")));
                return null;
            }
            if (!loginCode.equals(systemCode)) {
                requestContext.setSendZuulResponse(false);
                response.setCharacterEncoding("UTF-8");
                requestContext.setResponseBody(JsonMapperTool.objectToJson(ReturnResult.build(400, "验证码错误")));
                return null;
            }
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(account, password);
            token.setRememberMe(true);
            try {
                subject.login(token);
                SysUser SysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
                requestContext.setSendZuulResponse(false);
                requestContext.setResponseBody(JsonMapperTool.objectToJson(ReturnResult.ok(SysUser)));
                return null;
            } catch (AuthenticationException e) {
                e.printStackTrace();
                requestContext.setSendZuulResponse(false);
                response.setCharacterEncoding("UTF-8");
                requestContext.setResponseBody(JsonMapperTool.objectToJson(ReturnResult.build(400, "用户名或密码错误")));
                return null;
            }
        }


        return null;
    }



    @Override
    public String filterType() {
        return "pre"; // 设置过滤器类型为：pre
    }

    @Override
    public int filterOrder() {
        return 0;// 设置执行顺序
    }

}
