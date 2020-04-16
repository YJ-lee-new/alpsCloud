package com.alps.gateway.client.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
/**
 * @author:Yujie.lee
 * Date:2019年11月23日
 * TodoTODO
 */
@Mapper
public interface SysClientDao {

	 
	@Select("select * from oauth_client_details t where t.client_id = #{clientId}")
	Map<?, ?> getClient(String clientId);
	
	
	@SuppressWarnings("rawtypes")
	@Select("select * from oauth_client_details t where status=1 ")
	List<Map> findAll();

 
}
