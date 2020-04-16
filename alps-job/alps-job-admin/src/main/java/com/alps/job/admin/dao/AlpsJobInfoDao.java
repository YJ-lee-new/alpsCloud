package com.alps.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.alps.job.admin.core.model.AlpsJobInfo;

import java.util.List;


/**
 * job info
 * @author  Yujie.lee 2020-1-12 18:03:45
 */
@Mapper
public interface AlpsJobInfoDao {

	public List<AlpsJobInfo> pageList(@Param("offset") int offset,
									 @Param("pagesize") int pagesize,
									 @Param("jobGroup") int jobGroup,
									 @Param("triggerStatus") int triggerStatus,
									 @Param("jobDesc") String jobDesc,
									 @Param("executorHandler") String executorHandler,
									 @Param("author") String author);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("triggerStatus") int triggerStatus,
							 @Param("jobDesc") String jobDesc,
							 @Param("executorHandler") String executorHandler,
							 @Param("author") String author);
	
	public int save(AlpsJobInfo info);

	public AlpsJobInfo loadById(@Param("id") int id);
	
	public int update(AlpsJobInfo alpsJobInfo);
	
	public int delete(@Param("id") long id);

	public List<AlpsJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

	public int findAllCount();

	public List<AlpsJobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize );

	public int scheduleUpdate(AlpsJobInfo alpsJobInfo);


}
