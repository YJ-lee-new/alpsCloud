package com.alps.base.api.service;

import com.alps.base.api.model.Authority2Api;
import com.alps.base.api.model.AuthorityMenu;
import com.alps.base.api.model.AuthorityResource;
import com.alps.base.api.model.entity.SysAuthority;
import com.alps.base.api.model.entity.SysMenu;
import com.alps.common.core.domain.ResultBody;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ISysAuthorityServiceClient {

    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @GetMapping("/authority/menu")
    ResultBody<List<SysMenu>> findAuthorityMenu(
    		@RequestParam(value = "userName", required = true) String userName,
   		    @RequestParam(value = "companyId", required = true) String companyId
    		);
    
    /**
     * 获取所访问API的权限清单
     * @param userName
     * @param companyId(暂时不分公司,刷出所有租户下面的所有接口清单)
     * @return apiList
     */
    @GetMapping("/authority/apiList")
    public ResultBody<List<SysAuthority>> findAuthorityApiList(
            @RequestParam(value = "userName", required = false) String userName
    );
}
