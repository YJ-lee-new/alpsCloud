package com.alps.oauth.uaa.client.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.alps.base.api.model.entity.SysMenu;
import com.alps.common.config.Global;
import com.alps.common.oauth2.security.AlpsUserDetails;
import com.alps.oauth.uaa.client.web.service.ISysMenuService;
import com.alps.oauth.uaa.client.web.utils.WebUserHelper;

/**
 * 首页 业务处理
 * 
 * @author : yujie.lee
 */
@Controller
public class SysIndexController {
    @Autowired
    private ISysMenuService sysMenuService;

    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap)
    {
    	AlpsUserDetails user = WebUserHelper.getUser();
    	String companyId = "HP";
    	List<SysMenu> menus = sysMenuService.selectMenuTreeByUserId(user.getUsername(), companyId);
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        return "index";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
    	System.out.println("===>main");
        mmap.put("version", Global.getVersion());
        return "main";
    }
}
