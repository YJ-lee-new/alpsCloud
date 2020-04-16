package com.alps.job.admin.controller;

import com.alps.job.admin.core.model.AlpsJobInfo;
import com.alps.job.admin.core.model.AlpsJobLogGlue;
import com.alps.job.admin.core.util.I18nUtil;
import com.alps.job.admin.dao.AlpsJobInfoDao;
import com.alps.job.admin.dao.AlpsJobLogGlueDao;
import com.alps.job.common.biz.model.ReturnT;
import com.alps.job.common.glue.GlueTypeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * job code controller
 * @author  Yujie.lee 2020/4/10
 */
@Controller
@RequestMapping("/jobcode")
public class JobCodeController {
	
	@Resource
	private AlpsJobInfoDao alpsJobInfoDao;
	@Resource
	private AlpsJobLogGlueDao alpsJobLogGlueDao;

	@RequestMapping
	public String index(HttpServletRequest request, Model model, int jobId) {
		AlpsJobInfo jobInfo = alpsJobInfoDao.loadById(jobId);
		List<AlpsJobLogGlue> jobLogGlues = alpsJobLogGlueDao.findByJobId(jobId);

		if (jobInfo == null) {
			throw new RuntimeException(I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}
		if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
			throw new RuntimeException(I18nUtil.getString("jobinfo_glue_gluetype_unvalid"));
		}

		// valid permission
		JobInfoController.validPermission(request, jobInfo.getJobGroup());

		// Glue类型-字典
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());

		model.addAttribute("jobInfo", jobInfo);
		model.addAttribute("jobLogGlues", jobLogGlues);
		return "jobcode/jobcode.index";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public ReturnT<String> save(Model model, int id, String glueSource, String glueRemark) {
		// valid
		if (glueRemark==null) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")) );
		}
		if (glueRemark.length()<4 || glueRemark.length()>100) {
			return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_remark_limit"));
		}
		AlpsJobInfo exists_jobInfo = alpsJobInfoDao.loadById(id);
		if (exists_jobInfo == null) {
			return new ReturnT<String>(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
		}
		
		// update new code
		exists_jobInfo.setGlueSource(glueSource);
		exists_jobInfo.setGlueRemark(glueRemark);
		exists_jobInfo.setGlueUpdatetime(new Date());

		exists_jobInfo.setUpdateTime(new Date());
		alpsJobInfoDao.update(exists_jobInfo);

		// log old code
		AlpsJobLogGlue alpsJobLogGlue = new AlpsJobLogGlue();
		alpsJobLogGlue.setJobId(exists_jobInfo.getId());
		alpsJobLogGlue.setGlueType(exists_jobInfo.getGlueType());
		alpsJobLogGlue.setGlueSource(glueSource);
		alpsJobLogGlue.setGlueRemark(glueRemark);

		alpsJobLogGlue.setAddTime(new Date());
		alpsJobLogGlue.setUpdateTime(new Date());
		alpsJobLogGlueDao.save(alpsJobLogGlue);

		// remove code backup more than 30
		alpsJobLogGlueDao.removeOld(exists_jobInfo.getId(), 30);

		return ReturnT.SUCCESS;
	}
	
}
