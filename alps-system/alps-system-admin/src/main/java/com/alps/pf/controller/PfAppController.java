package com.alps.pf.controller;

import java.util.Date;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alps.pf.service.IPfAppService;
import com.alps.provider.pf.domain.PfApp;
import com.alps.common.annotation.Log;
import com.alps.common.core.controller.BaseController;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.enums.BusinessType;
import com.alps.common.utils.GetUUID;
import com.alps.common.utils.auth.RandomValueUtils;
import com.alps.common.utils.poi.ExcelUtil;
import com.alps.framework.util.ShiroUtils;

/**
 * pf_app 信息操作处理
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Controller
@RequestMapping("/pf/pfApp")
public class PfAppController extends BaseController
{
    private String prefix = "pf/pfApp";
	
	@Autowired
	private IPfAppService pfAppService;
	
	@RequiresPermissions("pf:pfApp:view")
	@GetMapping()
	public String pfApp()
	{
	    return prefix + "/pfApp";
	}
	
	/**
	 * 查询pf_app列表
	 */
	@RequiresPermissions("pf:pfApp:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PfApp pfApp)
	{
		startPage();
        List<PfApp> list = pfAppService.selectPfAppList(pfApp);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出pf_app列表
	 */
	@RequiresPermissions("pf:pfApp:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PfApp pfApp)
    {
    	List<PfApp> list = pfAppService.selectPfAppList(pfApp);
        ExcelUtil<PfApp> util = new ExcelUtil<PfApp>(PfApp.class);
        return util.exportExcel(list, "pfApp");
    }
	
	/**
	 * 新增pf_app
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存pf_app
	 */
	@RequiresPermissions("pf:pfApp:add")
	@Log(title = "pf_app", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PfApp pfApp)
	{
		pfApp.setCreateBy(ShiroUtils.getLoginName());
		pfApp.setAppId(GetUUID.getGUID());
		pfApp.setCreateTime(new Date());
		String appKey = RandomValueUtils.randomAlphanumeric(32);
        String appSecretKey = RandomValueUtils.randomAlphanumeric(32);
        pfApp.setAppKey(appKey);
        pfApp.setSecretKey(appSecretKey);
		return toAjax(pfAppService.insertPfApp(pfApp));
	}

	/**
	 * 修改pf_app
	 */
	@GetMapping("/edit/{appId}")
	public String edit(@PathVariable("appId") String appId, ModelMap mmap)
	{
		PfApp pfApp = pfAppService.selectPfAppById(appId);
		mmap.put("pfApp", pfApp);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存pf_app
	 */
	@RequiresPermissions("pf:pfApp:edit")
	@Log(title = "pf_app", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PfApp pfApp)
	{		
		pfApp.setUpdateBy(ShiroUtils.getLoginName());
		pfApp.setUpdateTime(new Date());
		return toAjax(pfAppService.updatePfApp(pfApp));
	}
	
	/**
	 * 删除pf_app
	 */
	@RequiresPermissions("pf:pfApp:remove")
	@Log(title = "pf_app", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(pfAppService.deletePfAppByIds(ids));
	}
	
}
