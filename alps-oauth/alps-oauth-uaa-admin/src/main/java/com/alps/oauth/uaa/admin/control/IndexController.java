package com.alps.oauth.uaa.admin.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alps.oauth.uaa.admin.feign.BaseAppServiceClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @author:Yujie.lee
 * Date:2019年12月7日
 * TodoTODO
 */
@Controller
public class IndexController {
    @Autowired
    private BaseAppServiceClient baseAppRemoteService;

    /**
     * 欢迎页
     *
     * @return
     */
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    /**
     * 登录页
     *
     * @return
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
    	System.out.println("=====>>");
        return "login";
    }
    
    /**
     * 登录页
     *
     * @return
     */
    @GetMapping("/login2")
    public String login2(HttpServletRequest request) {
    	System.out.println("=====>>");
        return "login";
    }

    /**
     * 确认授权页
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/oauth/confirm_access")
    public String confirm_access(HttpServletRequest request, HttpSession session, Map model) {
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request.getAttribute("scopes"));
        List<String> scopeList = new ArrayList<String>();
        for (String scope : scopes.keySet()) {
            scopeList.add(scope);
        }
        model.put("scopeList", scopeList);
        Object auth = session.getAttribute("authorizationRequest");
        if (auth != null) {
            try {
                AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
                ClientDetails clientDetails = baseAppRemoteService.getAppClientInfo(authorizationRequest.getClientId()).getData();
                model.put("app", clientDetails.getAdditionalInformation());
            } catch (Exception e) {

            }
        }
        return "confirm_access";
    }

    /**
     * 自定义oauth2错误页
     * @param request
     * @return
     */
    @RequestMapping("/oauth/error")
    @ResponseBody
    public Object handleError(HttpServletRequest request) {
        Object error = request.getAttribute("error");
        return error;
    }
}
