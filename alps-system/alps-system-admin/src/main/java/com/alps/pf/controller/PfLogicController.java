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

import com.alps.common.annotation.Log;
import com.alps.common.core.controller.BaseController;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.enums.BusinessType;
import com.alps.common.utils.poi.ExcelUtil;
import com.alps.pf.service.IPfLogicService;
import com.alps.provider.pf.domain.PfLogic;

/**
 * pf_logic 信息操作处理
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Controller
@RequestMapping("/pf/pfLogic")
public class PfLogicController extends BaseController
{
    private String prefix = "pf/pfLogic";
	
	@Autowired
	private IPfLogicService pfLogicService;
	
	@RequiresPermissions("pf:pfLogic:view")
	@GetMapping()
	public String pfLogic()
	{
	    return prefix + "/pfLogic";
	}
	
	/**
	 * 查询pf_logic列表
	 */
	@RequiresPermissions("pf:pfLogic:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PfLogic pfLogic)
	{
		startPage();
        List<PfLogic> list = pfLogicService.selectPfLogicList(pfLogic);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出pf_logic列表
	 */
	@RequiresPermissions("pf:pfLogic:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PfLogic pfLogic)
    {
    	List<PfLogic> list = pfLogicService.selectPfLogicList(pfLogic);
        ExcelUtil<PfLogic> util = new ExcelUtil<PfLogic>(PfLogic.class);
        return util.exportExcel(list, "pfLogic");
    }
	
	/**
	 * 新增pf_logic
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存pf_logic
	 */
	@RequiresPermissions("pf:pfLogic:add")
	@Log(title = "pf_logic", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PfLogic pfLogic)
	{		
		return toAjax(pfLogicService.insertPfLogic(pfLogic));
	}

	/**
	 * 修改pf_logic
	 */
	@GetMapping("/edit/{logicId}")
	public String edit(@PathVariable("logicId") String logicId, ModelMap mmap)
	{
		PfLogic pfLogic = pfLogicService.selectPfLogicById(logicId);
		mmap.put("pfLogic", pfLogic);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存pf_logic
	 */
	@RequiresPermissions("pf:pfLogic:edit")
	@Log(title = "pf_logic", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PfLogic pfLogic)
	{		
		return toAjax(pfLogicService.updatePfLogic(pfLogic));
	}
	
	/**
	 * 删除pf_logic
	 */
	@RequiresPermissions("pf:pfLogic:remove")
	@Log(title = "pf_logic", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(pfLogicService.deletePfLogicByIds(ids));
	}
	
}
