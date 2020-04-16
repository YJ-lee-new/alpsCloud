package com.alps.job.admin.controller;

import com.alps.job.admin.core.model.AlpsJobGroup;
import com.alps.job.admin.core.model.AlpsJobRegistry;
import com.alps.job.admin.core.util.I18nUtil;
import com.alps.job.admin.dao.AlpsJobGroupDao;
import com.alps.job.admin.dao.AlpsJobInfoDao;
import com.alps.job.admin.dao.AlpsJobRegistryDao;
import com.alps.job.common.biz.model.ReturnT;
import com.alps.job.common.enums.RegistryConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * job group controller
 * @author  Yujie.lee 2020/4/10
 */
@Controller
@RequestMapping("/jobgroup")
public class JobGroupController {

	@Resource
	public AlpsJobInfoDao alpsJobInfoDao;
	@Resource
	public AlpsJobGroupDao alpsJobGroupDao;
	@Resource
	private AlpsJobRegistryDao alpsJobRegistryDao;

	@RequestMapping
	public String index(Model model) {

		// job group (executor)
		List<AlpsJobGroup> list = alpsJobGroupDao.findAll();

		model.addAttribute("list", list);
		return "jobgroup/jobgroup.index";
	}

	@RequestMapping("/save")
	@ResponseBody
	public ReturnT<String> save(AlpsJobGroup alpsJobGroup){

		// valid
		if (alpsJobGroup.getAppName()==null || alpsJobGroup.getAppName().trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input")+"AppName") );
		}
		if (alpsJobGroup.getAppName().length()<4 || alpsJobGroup.getAppName().length()>64) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appName_length") );
		}
		if (alpsJobGroup.getTitle()==null || alpsJobGroup.getTitle().trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
		}
		if (alpsJobGroup.getAddressType()!=0) {
			if (alpsJobGroup.getAddressList()==null || alpsJobGroup.getAddressList().trim().length()==0) {
				return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit") );
			}
			String[] addresss = alpsJobGroup.getAddressList().split(",");
			for (String item: addresss) {
				if (item==null || item.trim().length()==0) {
					return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid") );
				}
			}
		}

		int ret = alpsJobGroupDao.save(alpsJobGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(AlpsJobGroup alpsJobGroup){
		// valid
		if (alpsJobGroup.getAppName()==null || alpsJobGroup.getAppName().trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input")+"AppName") );
		}
		if (alpsJobGroup.getAppName().length()<4 || alpsJobGroup.getAppName().length()>64) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appName_length") );
		}
		if (alpsJobGroup.getTitle()==null || alpsJobGroup.getTitle().trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
		}
		if (alpsJobGroup.getAddressType() == 0) {
			// 0=自动注册
			List<String> registryList = findRegistryByAppName(alpsJobGroup.getAppName());
			String addressListStr = null;
			if (registryList!=null && !registryList.isEmpty()) {
				Collections.sort(registryList);
				addressListStr = "";
				for (String item:registryList) {
					addressListStr += item + ",";
				}
				addressListStr = addressListStr.substring(0, addressListStr.length()-1);
			}
			alpsJobGroup.setAddressList(addressListStr);
		} else {
			// 1=手动录入
			if (alpsJobGroup.getAddressList()==null || alpsJobGroup.getAddressList().trim().length()==0) {
				return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit") );
			}
			String[] addresss = alpsJobGroup.getAddressList().split(",");
			for (String item: addresss) {
				if (item==null || item.trim().length()==0) {
					return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid") );
				}
			}
		}

		int ret = alpsJobGroupDao.update(alpsJobGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	private List<String> findRegistryByAppName(String appNameParam){
		HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
		List<AlpsJobRegistry> list = alpsJobRegistryDao.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
		if (list != null) {
			for (AlpsJobRegistry item: list) {
				if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
					String appName = item.getRegistryKey();
					List<String> registryList = appAddressMap.get(appName);
					if (registryList == null) {
						registryList = new ArrayList<String>();
					}

					if (!registryList.contains(item.getRegistryValue())) {
						registryList.add(item.getRegistryValue());
					}
					appAddressMap.put(appName, registryList);
				}
			}
		}
		return appAddressMap.get(appNameParam);
	}

	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(int id){

		// valid
		int count = alpsJobInfoDao.pageListCount(0, 10, id, -1,  null, null, null);
		if (count > 0) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_0") );
		}

		List<AlpsJobGroup> allList = alpsJobGroupDao.findAll();
		if (allList.size() == 1) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_1") );
		}

		int ret = alpsJobGroupDao.remove(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/loadById")
	@ResponseBody
	public ReturnT<AlpsJobGroup> loadById(int id){
		AlpsJobGroup jobGroup = alpsJobGroupDao.load(id);
		return jobGroup!=null?new ReturnT<AlpsJobGroup>(jobGroup):new ReturnT<AlpsJobGroup>(ReturnT.FAIL_CODE, null);
	}

}
