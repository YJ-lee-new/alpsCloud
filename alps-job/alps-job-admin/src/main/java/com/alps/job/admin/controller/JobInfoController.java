package com.alps.job.admin.controller;

import com.alps.job.admin.core.cron.CronExpression;
import com.alps.job.admin.core.exception.AlpsJobException;
import com.alps.job.admin.core.model.AlpsJobGroup;
import com.alps.job.admin.core.model.AlpsJobInfo;
import com.alps.job.admin.core.model.AlpsJobUser;
import com.alps.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.alps.job.admin.core.thread.JobTriggerPoolHelper;
import com.alps.job.admin.core.trigger.TriggerTypeEnum;
import com.alps.job.admin.core.util.I18nUtil;
import com.alps.job.admin.dao.AlpsJobGroupDao;
import com.alps.job.admin.service.LoginService;
import com.alps.job.admin.service.AlpsJobService;
import com.alps.job.common.biz.model.ReturnT;
import com.alps.job.common.enums.ExecutorBlockStrategyEnum;
import com.alps.job.common.glue.GlueTypeEnum;
import com.alps.job.common.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * index controller
 * @author  Yujie.lee 2020/4/10
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {

	@Resource
	private AlpsJobGroupDao alpsJobGroupDao;
	@Resource
	private AlpsJobService alpsJobService;
	
	@RequestMapping
	public String index(HttpServletRequest request, Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

		// 枚举-字典
		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());	    // 路由策略-列表
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());								// Glue类型-字典
		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());	    // 阻塞处理策略-字典

		// 执行器列表
		List<AlpsJobGroup> jobGroupList_all =  alpsJobGroupDao.findAll();

		// filter group
		List<AlpsJobGroup> jobGroupList = filterJobGroupByRole(request, jobGroupList_all);
		if (jobGroupList==null || jobGroupList.size()==0) {
			throw new AlpsJobException(I18nUtil.getString("jobgroup_empty"));
		}

		model.addAttribute("JobGroupList", jobGroupList);
		model.addAttribute("jobGroup", jobGroup);

		return "jobinfo/jobinfo.index";
	}

	public static List<AlpsJobGroup> filterJobGroupByRole(HttpServletRequest request, List<AlpsJobGroup> jobGroupList_all){
		List<AlpsJobGroup> jobGroupList = new ArrayList<>();
		if (jobGroupList_all!=null && jobGroupList_all.size()>0) {
			AlpsJobUser loginUser = (AlpsJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
			if (loginUser.getRole() == 1) {
				jobGroupList = jobGroupList_all;
			} else {
				List<String> groupIdStrs = new ArrayList<>();
				if (loginUser.getPermission()!=null && loginUser.getPermission().trim().length()>0) {
					groupIdStrs = Arrays.asList(loginUser.getPermission().trim().split(","));
				}
				for (AlpsJobGroup groupItem:jobGroupList_all) {
					if (groupIdStrs.contains(String.valueOf(groupItem.getId()))) {
						jobGroupList.add(groupItem);
					}
				}
			}
		}
		return jobGroupList;
	}
	public static void validPermission(HttpServletRequest request, int jobGroup) {
		AlpsJobUser loginUser = (AlpsJobUser) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
		if (!loginUser.validPermission(jobGroup)) {
			throw new RuntimeException(I18nUtil.getString("system_permission_limit") + "[username="+ loginUser.getUsername() +"]");
		}
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,  
			@RequestParam(required = false, defaultValue = "10") int length,
			int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
		
		return alpsJobService.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(AlpsJobInfo jobInfo) {
		return alpsJobService.add(jobInfo);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(AlpsJobInfo jobInfo) {
		return alpsJobService.update(jobInfo);
	}
	
	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(int id) {
		return alpsJobService.remove(id);
	}
	
	@RequestMapping("/stop")
	@ResponseBody
	public ReturnT<String> pause(int id) {
		return alpsJobService.stop(id);
	}
	
	@RequestMapping("/start")
	@ResponseBody
	public ReturnT<String> start(int id) {
		return alpsJobService.start(id);
	}
	
	@RequestMapping("/trigger")
	@ResponseBody
	//@PermissionLimit(limit = false)
	public ReturnT<String> triggerJob(int id, String executorParam) {
		// force cover job param
		if (executorParam == null) {
			executorParam = "";
		}

		JobTriggerPoolHelper.trigger(id, TriggerTypeEnum.MANUAL, -1, null, executorParam);
		return ReturnT.SUCCESS;
	}

	@RequestMapping("/nextTriggerTime")
	@ResponseBody
	public ReturnT<List<String>> nextTriggerTime(String cron) {
		List<String> result = new ArrayList<>();
		try {
			CronExpression cronExpression = new CronExpression(cron);
			Date lastTime = new Date();
			for (int i = 0; i < 5; i++) {
				lastTime = cronExpression.getNextValidTimeAfter(lastTime);
				if (lastTime != null) {
					result.add(DateUtil.formatDateTime(lastTime));
				} else {
					break;
				}
			}
		} catch (ParseException e) {
			return new ReturnT<List<String>>(ReturnT.FAIL_CODE, I18nUtil.getString("jobinfo_field_cron_unvalid"));
		}
		return new ReturnT<List<String>>(result);
	}
	
}
