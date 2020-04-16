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
import com.alps.pf.service.IPfAcitonService;
import com.alps.pf.service.IPfApiService;
import com.alps.pf.service.IPfAppService;
import com.alps.provider.pf.domain.PfAciton;
import com.alps.provider.pf.domain.PfApi;
import com.alps.provider.system.domain.SysRole;
import com.alps.common.annotation.Log;
import com.alps.common.core.controller.BaseController;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.domain.Ztrees;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.enums.BusinessType;
import com.alps.common.utils.GetUUID;
import com.alps.common.utils.poi.ExcelUtil;
import com.alps.framework.util.ShiroUtils;
/**
 * action 信息操作处理
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Controller
@RequestMapping("/pf/pfAciton")
public class PfAcitonController extends BaseController
{
    private String prefix = "pf/pfAciton";
	
	@Autowired
	private IPfAcitonService pfAcitonService;
	
	@Autowired
	private IPfApiService iPfApiService;
	
	@Autowired
	private IPfAppService iPfAppService;
	
	@RequiresPermissions("pf:pfAciton:view")
	@GetMapping()
	public String pfAciton( ModelMap mmap)
	{
		PfApi pfApi = new PfApi();
		mmap.put("apiList",iPfApiService.selectPfApiList(pfApi) );
	    return prefix + "/pfAciton";
	}
	
	/**
	 * 查询action列表
	 */
	@RequiresPermissions("pf:pfAciton:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PfAciton pfAciton)
	{
		startPage();
        List<PfAciton> list = pfAcitonService.selectPfAcitonList(pfAciton);
		return getDataTable(list);
	}
	
	/**
	 * 查询action列表
	 */
	@GetMapping("/getApiList/{memuId}")
	@ResponseBody
	public List<Ztrees> getApiList(@PathVariable("memuId") Long memuId,SysRole role)
	{
		System.out.println("====>>" + memuId);
		List<Ztrees> ztrees = pfAcitonService.getApiList(memuId,role);
		return ztrees;
	}
	
	
	/**
	 * 导出action列表
	 */
	@RequiresPermissions("pf:pfAciton:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PfAciton pfAciton)
    {
    	List<PfAciton> list = pfAcitonService.selectPfAcitonList(pfAciton);
        ExcelUtil<PfAciton> util = new ExcelUtil<PfAciton>(PfAciton.class);
        return util.exportExcel(list, "pfAciton");
    }
	
	/**
	 * 新增action
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap)
	{
		PfApi pfApi = new PfApi();
		mmap.put("apiList",iPfApiService.selectPfApiList(pfApi) );
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存action
	 */
	@RequiresPermissions("pf:pfAciton:add")
	@Log(title = "action", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PfAciton pfAciton)
	{		
		pfAciton.setCreateBy(ShiroUtils.getLoginName());
		pfAciton.setActionId(GetUUID.getGUID());
		pfAciton.setCreateTime(new Date());
		return toAjax(pfAcitonService.insertPfAciton(pfAciton));
	}

	/**
	 * 修改action
	 */
	@GetMapping("/edit/{actionId}")
	public String edit(@PathVariable("actionId") String actionId, ModelMap mmap)
	{
		PfApi pfApi = new PfApi();
		mmap.put("apiList",iPfApiService.selectPfApiList(pfApi) );
		PfAciton pfAciton = pfAcitonService.selectPfAcitonById(actionId);
		mmap.put("pfAciton", pfAciton);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存action
	 */
	@RequiresPermissions("pf:pfAciton:edit")
	@Log(title = "action", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PfAciton pfAciton)
	{		
		pfAciton.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(pfAcitonService.updatePfAciton(pfAciton));
	}
	
	/**
	 * 删除action
	 */
	@RequiresPermissions("pf:pfAciton:remove")
	@Log(title = "action", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(pfAcitonService.deletePfAcitonByIds(ids));
	}
	
}
