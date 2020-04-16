package com.alps.job.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.alps.job.admin.core.model.AlpsJobUser;

import java.util.List;

/**
 * @author  Yujie.lee 2019-05-04 16:44:59
 */
@Mapper
public interface AlpsJobUserDao {

	public List<AlpsJobUser> pageList(@Param("offset") int offset,
                                     @Param("pagesize") int pagesize,
                                     @Param("username") String username,
									 @Param("role") int role);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("username") String username,
							 @Param("role") int role);

	public AlpsJobUser loadByUserName(@Param("username") String username);

	public int save(AlpsJobUser alpsJobUser);

	public int update(AlpsJobUser alpsJobUser);
	
	public int delete(@Param("id") int id);

}
