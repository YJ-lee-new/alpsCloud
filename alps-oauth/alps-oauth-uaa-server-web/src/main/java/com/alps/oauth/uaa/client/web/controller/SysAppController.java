package com.alps.oauth.uaa.client.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alps.base.api.model.entity.SysApp;
import com.alps.common.annotation.Log;
import com.alps.oauth.uaa.client.web.service.ISysAppService;
import com.alps.oauth.uaa.client.web.utils.WebUserHelper;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.enums.BusinessType;
import com.alps.common.oauth2.constants.AlpsRestTemplate;

/**
 * sys_app 信息操作处理
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Controller
@RequestMapping("/system/app")
public class SysAppController extends BaseController
{
    private String prefix = "system/app";
	
	@Autowired
	private ISysAppService sysAppService;
	
    @Autowired
    private AlpsRestTemplate alpsRestTemplate;
	
	@GetMapping()
	public String sysApp()
	{
	    return prefix + "/app";
	}
	
	/**
	 * 查询sys_app列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysApp sysApp)
	{
		startPage();
        List<SysApp> list = sysAppService.selectSysAppList(sysApp);
		return getDataTable(list);
	}
	
	
	/**
	 * 新增sys_app
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存sys_app
	 */
	@Log(title = "sys_app", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SysApp sysApp)
	{
		sysApp.setCreateBy(WebUserHelper.getUser().getUserId().toString());
		sysApp = sysAppService.addAppInfo(sysApp);
		int n = 0;
		if(sysApp != null) {
			// 刷新网关
	        alpsRestTemplate.refreshGateway();
	        n=1;
		}
		return toAjax(n);
	}

	/**
	 * 修改sys_app
	 */
	@GetMapping("/edit/{appId}")
	public String edit(@PathVariable("appId") Long appId, ModelMap mmap)
	{
		SysApp sysApp = sysAppService.getAppInfo(appId);
		mmap.put("sysApp", sysApp);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存sys_app
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SysApp sysApp)
	{		
		sysApp.setUpdateBy(WebUserHelper.getUser().getUserId().toString());
		sysApp = sysAppService.updateInfo(sysApp);
		int n = 0;
		if(sysApp != null) {
			// 刷新网关
	        alpsRestTemplate.refreshGateway();
	        n=1;
		}
		return toAjax(n);
	}
	
	/**
	 * 删除sys_app
	 */
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		int n = sysAppService.deleteSysAppByIds(ids);
		return toAjax(n);
	}
	
}
