package com.alps.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.alps.job.admin.core.model.AlpsJobLogReport;

import java.util.Date;
import java.util.List;

/**
 * job log
 * @author  Yujie.lee 2019-11-22
 */
@Mapper
public interface AlpsJobLogReportDao {

	public int save(AlpsJobLogReport alpsJobLogReport);

	public int update(AlpsJobLogReport alpsJobLogReport);

	public List<AlpsJobLogReport> queryLogReport(@Param("triggerDayFrom") Date triggerDayFrom,
												@Param("triggerDayTo") Date triggerDayTo);

	public AlpsJobLogReport queryLogReportTotal();

}
