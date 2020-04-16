package com.alps.job.admin.controller;

import com.alps.job.admin.controller.annotation.PermissionLimit;
import com.alps.job.admin.core.conf.AlpsJobAdminConfig;
import com.alps.job.admin.core.exception.AlpsJobException;
import com.alps.job.admin.core.util.JacksonUtil;
import com.alps.job.common.biz.AdminBiz;
import com.alps.job.common.biz.model.HandleCallbackParam;
import com.alps.job.common.biz.model.RegistryParam;
import com.alps.job.common.biz.model.ReturnT;
import com.alps.job.common.util.AlpsJobRemotingUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  Yujie.lee 2020/4/10
 */
@Controller
@RequestMapping("/api")
public class JobApiController {

    @Resource
    private AdminBiz adminBiz;


    // ---------------------- base ----------------------

    /**
     * valid access token
     */
    private void validAccessToken(HttpServletRequest request){
        if (AlpsJobAdminConfig.getAdminConfig().getAccessToken()!=null
                && AlpsJobAdminConfig.getAdminConfig().getAccessToken().trim().length()>0
                && !AlpsJobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(AlpsJobRemotingUtil.ALPS_RPC_ACCESS_TOKEN))) {
            throw new AlpsJobException("The access token is wrong.");
        }
    }

    /**
     * parse Param
     */
    private Object parseParam(String data, Class<?> parametrized, Class<?>... parameterClasses){
        Object param = null;
        try {
            if (parameterClasses != null) {
                param = JacksonUtil.readValue(data, parametrized, parameterClasses);
            } else {
                param = JacksonUtil.readValue(data, parametrized);
            }
        } catch (Exception e) { }
        if (param==null) {
            throw new AlpsJobException("The request data invalid.");
        }
        return param;
    }

    // ---------------------- admin biz ----------------------

    /**
     * callback
     *
     * @param data
     * @return
     */
    @RequestMapping("/callback")
    @ResponseBody
    @PermissionLimit(limit=false)
    public ReturnT<String> callback(HttpServletRequest request, @RequestBody(required = false) String data) {
        // valid
        validAccessToken(request);

        // param
        List<HandleCallbackParam> callbackParamList = (List<HandleCallbackParam>) parseParam(data, List.class, HandleCallbackParam.class);

        // invoke
        return adminBiz.callback(callbackParamList);
    }



    /**
     * registry
     *
     * @param data
     * @return
     */
    @RequestMapping("/registry")
    @ResponseBody
    @PermissionLimit(limit=false)
    public ReturnT<String> registry(HttpServletRequest request, @RequestBody(required = false) String data) {
        // valid
        validAccessToken(request);

        // param
        RegistryParam registryParam = (RegistryParam) parseParam(data, RegistryParam.class);

        // invoke
        return adminBiz.registry(registryParam);
    }

    /**
     * registry remove
     *
     * @param data
     * @return
     */
    @RequestMapping("/registryRemove")
    @ResponseBody
    @PermissionLimit(limit=false)
    public ReturnT<String> registryRemove(HttpServletRequest request, @RequestBody(required = false) String data) {
        // valid
        validAccessToken(request);

        // param
        RegistryParam registryParam = (RegistryParam) parseParam(data, RegistryParam.class);

        // invoke
        return adminBiz.registryRemove(registryParam);
    }

    // ---------------------- job biz ----------------------

}
