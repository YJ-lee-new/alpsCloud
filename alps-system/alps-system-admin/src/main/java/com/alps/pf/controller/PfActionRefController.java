package com.alps.pf.controller;

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
import com.alps.pf.service.IPfActionRefService;
import com.alps.provider.pf.domain.PfActionRef;
import com.alps.common.annotation.Log;
import com.alps.common.core.controller.BaseController;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.enums.BusinessType;
import com.alps.common.utils.poi.ExcelUtil;
/**
 * pf_action_ref 信息操作处理
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Controller
@RequestMapping("/pf/pfActionRef")
public class PfActionRefController extends BaseController
{
    private String prefix = "pf/pfActionRef";
	
	@Autowired
	private IPfActionRefService pfActionRefService;
	
	@RequiresPermissions("pf:pfActionRef:view")
	@GetMapping()
	public String pfActionRef()
	{
	    return prefix + "/pfActionRef";
	}
	
	/**
	 * 查询pf_action_ref列表
	 */
	@RequiresPermissions("pf:pfActionRef:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PfActionRef pfActionRef)
	{
		startPage();
        List<PfActionRef> list = pfActionRefService.selectPfActionRefList(pfActionRef);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出pf_action_ref列表
	 */
	@RequiresPermissions("pf:pfActionRef:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PfActionRef pfActionRef)
    {
    	List<PfActionRef> list = pfActionRefService.selectPfActionRefList(pfActionRef);
        ExcelUtil<PfActionRef> util = new ExcelUtil<PfActionRef>(PfActionRef.class);
        return util.exportExcel(list, "pfActionRef");
    }
	
	/**
	 * 新增pf_action_ref
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存pf_action_ref
	 */
	@RequiresPermissions("pf:pfActionRef:add")
	@Log(title = "pf_action_ref", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PfActionRef pfActionRef)
	{		
		return toAjax(pfActionRefService.insertPfActionRef(pfActionRef));
	}

	/**
	 * 修改pf_action_ref
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		PfActionRef pfActionRef = pfActionRefService.selectPfActionRefById(id);
		mmap.put("pfActionRef", pfActionRef);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存pf_action_ref
	 */
	@RequiresPermissions("pf:pfActionRef:edit")
	@Log(title = "pf_action_ref", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PfActionRef pfActionRef)
	{		
		return toAjax(pfActionRefService.updatePfActionRef(pfActionRef));
	}
	
	/**
	 * 删除pf_action_ref
	 */
	@RequiresPermissions("pf:pfActionRef:remove")
	@Log(title = "pf_action_ref", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(pfActionRefService.deletePfActionRefByIds(ids));
	}
	
}
