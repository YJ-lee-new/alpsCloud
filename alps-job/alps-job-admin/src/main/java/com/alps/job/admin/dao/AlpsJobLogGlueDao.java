package com.alps.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.alps.job.admin.core.model.AlpsJobLogGlue;

import java.util.List;

/**
 * job log for glue
 * @author  Yujie.lee 2020-5-19 18:04:56
 */
@Mapper
public interface AlpsJobLogGlueDao {
	
	public int save(AlpsJobLogGlue alpsJobLogGlue);
	
	public List<AlpsJobLogGlue> findByJobId(@Param("jobId") int jobId);

	public int removeOld(@Param("jobId") int jobId, @Param("limit") int limit);

	public int deleteByJobId(@Param("jobId") int jobId);
	
}
