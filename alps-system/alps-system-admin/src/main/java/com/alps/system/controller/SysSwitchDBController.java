package com.alps.system.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alps.common.core.controller.BaseController;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.enums.DataSourceType;

/**
 * 用户信息
 * 
 * @author : yujie.lee
 */
@Controller
@RequestMapping("/system/switchDb")
public class SysSwitchDBController extends BaseController
{
	@Autowired
    private HttpSession session;

    @PostMapping("")
    @ResponseBody
    public AjaxResult  ajaxSwitch(String  dbName)
    {
    	 if("HPNB".equalsIgnoreCase(dbName)) {
    		 session.setAttribute("BSdb", DataSourceType.NBDS);
    	 }
    	 if("HPDT".equalsIgnoreCase(dbName)) {
    		 session.setAttribute("BSdb", DataSourceType.BUSDS);
    	 }
    	 if("APPLE".equalsIgnoreCase(dbName)) {
    		 session.setAttribute("BSdb", DataSourceType.PORTALDS);
    	 }
    	 session.setAttribute("BSName", dbName);
    	 return success();
    }
    
}