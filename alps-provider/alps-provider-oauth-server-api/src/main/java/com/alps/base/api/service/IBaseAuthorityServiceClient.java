package com.alps.base.api.service;

import com.alps.base.api.model.Authority2Api;
import com.alps.base.api.model.AuthorityApi;
import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.AuthorityResource;
import com.alps.common.core.domain.ResultBody;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IBaseAuthorityServiceClient {
    /**
     * 获取所有访问权限列表
     * @return
     */
    @GetMapping("/authority/access")
    ResultBody<List<AuthorityResource>> findAuthorityResource();

    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @GetMapping("/authority/menu")
    ResultBody<List<AuthorityMenu>> findAuthorityMenu();
    
    /**
     * 获取所访问API的权限清单
     * @param userName
     * @param companyId(暂时不分公司,刷出所有租户下面的所有接口清单)
     * @return apiList
     */
    @GetMapping("/authority/apiList")
    public ResultBody<List<Authority2Api>> findAuthorityApiList(
            @RequestParam(value = "userName", required = false) String userName
    );
}
