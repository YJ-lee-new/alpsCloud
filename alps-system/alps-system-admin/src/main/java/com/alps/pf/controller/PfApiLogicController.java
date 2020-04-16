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
import com.alps.pf.service.IPfApiLogicService;
import com.alps.provider.pf.domain.PfApiLogic;
import com.alps.common.annotation.Log;
import com.alps.common.core.controller.BaseController;
import com.alps.common.core.domain.AjaxResult;
import com.alps.common.core.page.TableDataInfo;
import com.alps.common.enums.BusinessType;
import com.alps.common.utils.poi.ExcelUtil;

/**
 * pf_api_logic 信息操作处理
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Controller
@RequestMapping("/pf/pfApiLogic")
public class PfApiLogicController extends BaseController
{
    private String prefix = "pf/pfApiLogic";
	
	@Autowired
	private IPfApiLogicService pfApiLogicService;
	
	@RequiresPermissions("pf:pfApiLogic:view")
	@GetMapping()
	public String pfApiLogic()
	{
	    return prefix + "/pfApiLogic";
	}
	
	/**
	 * 查询pf_api_logic列表
	 */
	@RequiresPermissions("pf:pfApiLogic:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(PfApiLogic pfApiLogic)
	{
		startPage();
        List<PfApiLogic> list = pfApiLogicService.selectPfApiLogicList(pfApiLogic);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出pf_api_logic列表
	 */
	@RequiresPermissions("pf:pfApiLogic:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PfApiLogic pfApiLogic)
    {
    	List<PfApiLogic> list = pfApiLogicService.selectPfApiLogicList(pfApiLogic);
        ExcelUtil<PfApiLogic> util = new ExcelUtil<PfApiLogic>(PfApiLogic.class);
        return util.exportExcel(list, "pfApiLogic");
    }
	
	/**
	 * 新增pf_api_logic
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存pf_api_logic
	 */
	@RequiresPermissions("pf:pfApiLogic:add")
	@Log(title = "pf_api_logic", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(PfApiLogic pfApiLogic)
	{		
		return toAjax(pfApiLogicService.insertPfApiLogic(pfApiLogic));
	}

	/**
	 * 修改pf_api_logic
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		PfApiLogic pfApiLogic = pfApiLogicService.selectPfApiLogicById(id);
		mmap.put("pfApiLogic", pfApiLogic);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存pf_api_logic
	 */
	@RequiresPermissions("pf:pfApiLogic:edit")
	@Log(title = "pf_api_logic", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(PfApiLogic pfApiLogic)
	{		
		return toAjax(pfApiLogicService.updatePfApiLogic(pfApiLogic));
	}
	
	/**
	 * 删除pf_api_logic
	 */
	@RequiresPermissions("pf:pfApiLogic:remove")
	@Log(title = "pf_api_logic", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(pfApiLogicService.deletePfApiLogicByIds(ids));
	}
	
}
