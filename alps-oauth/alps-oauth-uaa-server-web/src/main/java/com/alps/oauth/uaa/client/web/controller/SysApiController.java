package com.alps.oauth.uaa.client.web.controller;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alps.base.api.model.entity.SysApi;
import com.alps.base.api.model.entity.SysApp;
import com.alps.common.constant.UserConstants;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.oauth2.constants.AlpsRestTemplate;
import com.alps.oauth.uaa.client.web.service.ISysApiService;
import com.alps.oauth.uaa.client.web.service.ISysAppService;
import com.alps.oauth.uaa.client.web.utils.WebUserHelper;

/**
 * sys_api 信息操作处理
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Controller
@RequestMapping("/system/api")
public class SysApiController extends BaseController
{
    private String prefix = "system/api";
	
	@Autowired
	private ISysApiService sysApiService;
	@Autowired
	private ISysAppService sysAppService;
	
    @Autowired
    private AlpsRestTemplate alpsRestTemplate;
	
	@GetMapping()
	public String sysApi(ModelMap mmap)
	{
		SysApp sysApp = new SysApp();
		mmap.put("appList", sysAppService.selectSysAppList(sysApp));
	    return prefix + "/api";
	}
	
	/**
	 * 查询sys_api列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysApi sysApi)
	{
		startPage();
        List<SysApi> list = sysApiService.selectSysApiList(sysApi);
		return getDataTable(list);
	}
	
	/**
	 * 新增sys_api
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap)
	{
		SysApp sysApp = new SysApp();
		mmap.put("appList", sysAppService.selectSysAppList(sysApp));
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存sys_api
	 */
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SysApi sysApi)
	{		
		sysApi.setCreateBy(WebUserHelper.getUser().getUserId().toString());
		sysApi.setCreateTime(new Date());
		int n = sysApiService.insertSysApi(sysApi);
		if(n>0) {
			// 刷新网关
	        alpsRestTemplate.refreshGateway();
		}
		return toAjax(n);
	}

	/**
	 * 修改sys_api
	 */
	@GetMapping("/edit/{apiId}")
	public String edit(@PathVariable("apiId") String apiId, ModelMap mmap)
	{
		SysApi SysApi = sysApiService.getById(apiId);
		SysApp sysApp = new SysApp();
		mmap.put("appList", sysAppService.selectSysAppList(sysApp));
		mmap.put("SysApi", SysApi);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存sys_api
	 */
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SysApi SysApi)
	{		
		SysApi.setUpdateBy(WebUserHelper.getUser().getUserId().toString());
		SysApi.setUpdateTime(new Date());
		int n = sysApiService.updateSysApi(SysApi);
		if(n>0) {
			// 刷新网关
	        alpsRestTemplate.refreshGateway();
		}
		return toAjax(n);
	}
	
	/**
	 * 删除sys_api
	 */
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{	
		int n = sysApiService.deleteSysApiByIds(ids);
		if(n>0) {
			// 刷新网关
	        alpsRestTemplate.refreshGateway();
		}
		return toAjax(n);
	}
	
	/**
	 * 确保访问的apiCode唯一性
	 */
	@PostMapping( "/checkApiCodeUnique")
	@ResponseBody
	public String checkApiCodeUnique(String apiCode)
	{		
		return sysApiService.isExist(apiCode) > 0 ?UserConstants.ROLE_NAME_NOT_UNIQUE :UserConstants.ROLE_NAME_UNIQUE;
	}
	
	/**
	 * 确保访问的apiCode唯一性
	 */
	@PostMapping( "/checkApiCodeUniqueUpdate")
	@ResponseBody
	public String checkApiCodeUniqueUpdate(String apiCode,String apiId)
	{		
		return sysApiService.isExistCodeBeside(apiCode, apiId) > 0 ?UserConstants.ROLE_NAME_NOT_UNIQUE :UserConstants.ROLE_NAME_UNIQUE;
	}
	
	/**
	 * 保存时确保访问的path唯一性
	 */
	@PostMapping( "/checkPathUnique")
	@ResponseBody
	public String checkPathUnique(String path)
	{		
		return sysApiService.isPathExist(path)> 0 ?UserConstants.ROLE_NAME_NOT_UNIQUE :UserConstants.ROLE_NAME_UNIQUE;
	}
	
	/**
	 * 修改时确保访问的path唯一性
	 */
	@PostMapping( "/checkPathUniqueUpdate")
	@ResponseBody
	public String checkPathUniqueUpdate(String path,String apiId)
	{		
		return sysApiService.isPathHasBesideThis(path,apiId)> 0 ?UserConstants.ROLE_NAME_NOT_UNIQUE :UserConstants.ROLE_NAME_UNIQUE;
	}
	
}
