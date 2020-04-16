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
import com.alps.pf.service.IPfApiService;
import com.alps.pf.service.IPfAppService;
import com.alps.provider.pf.domain.PfApi;
import com.alps.provider.pf.domain.PfApp;
import com.alps.common.annotation.Log;
import com.alps.common.core.controller.BaseController;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.enums.BusinessType;
import com.alps.common.utils.GetUUID;
import com.alps.common.utils.poi.ExcelUtil;
import com.alps.framework.util.ShiroUtils;

/**
 * pf_api 信息操作处理
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Controller
@RequestMapping("/pf/pfApi")
public class PfApiController extends BaseController
{
    private String prefix = "pf/pfApi";
	
	@Autowired
	private IPfApiService pfApiService;
	@Autowired
	private IPfAppService iPfAppService;
	
	@RequiresPermissions("pf:pfApi:view")
	@GetMapping()
	public String pfApi(ModelMap mmap)
	{
		PfApp pfApp = new PfApp();
		mmap.put("appList", iPfAppService.selectPfAppList(pfApp));
	    return prefix + "/pfApi";
	}
	
	/**
	 * 查询pf_api列表
	 */
	@RequiresPermissions("pf:pfApi:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PfApi pfApi)
	{
		startPage();
        List<PfApi> list = pfApiService.selectPfApiList(pfApi);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出pf_api列表
	 */
	@RequiresPermissions("pf:pfApi:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PfApi pfApi)
    {
    	List<PfApi> list = pfApiService.selectPfApiList(pfApi);
        ExcelUtil<PfApi> util = new ExcelUtil<PfApi>(PfApi.class);
        return util.exportExcel(list, "pfApi");
    }
	
	/**
	 * 新增pf_api
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap)
	{
		PfApp pfApp = new PfApp();
		mmap.put("appList", iPfAppService.selectPfAppList(pfApp));
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存pf_api
	 */
	@RequiresPermissions("pf:pfApi:add")
	@Log(title = "pf_api", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PfApi pfApi)
	{		
		pfApi.setCreateBy(ShiroUtils.getLoginName());
		pfApi.setApiId(GetUUID.getGUID());
		pfApi.setCreateTime(new Date());
		return toAjax(pfApiService.insertPfApi(pfApi));
	}

	/**
	 * 修改pf_api
	 */
	@GetMapping("/edit/{apiId}")
	public String edit(@PathVariable("apiId") String apiId, ModelMap mmap)
	{
		PfApi pfApi = pfApiService.selectPfApiById(apiId);
		mmap.put("pfApi", pfApi);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存pf_api
	 */
	@RequiresPermissions("pf:pfApi:edit")
	@Log(title = "pf_api", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PfApi pfApi)
	{		
		pfApi.setUpdateBy(ShiroUtils.getLoginName());
		pfApi.setUpdateTime(new Date());
		return toAjax(pfApiService.updatePfApi(pfApi));
	}
	
	/**
	 * 删除pf_api
	 */
	@RequiresPermissions("pf:pfApi:remove")
	@Log(title = "pf_api", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(pfApiService.deletePfApiByIds(ids));
	}
	
}
