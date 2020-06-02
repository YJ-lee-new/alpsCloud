package com.alps.oauth.uaa.client.web.mapper;

import com.alps.base.api.model.entity.SysApi;
import com.alps.plaform.database.mapper.SuperMapper;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * @author YJ.lee
 */
@Repository
public interface SysApiMapper extends SuperMapper<SysApi> {

	/**
     * 查询sys_app列表
     * 
     * @param SysApp sys_app信息
     * @return sys_app集合
     */
	public List<SysApi> selectSysApiList(SysApi sysApi);
	
	  
    /**
     * 查询sys_api信息
     * 
     * @param apiId sys_apiID
     * @return sys_api信息
     */
	public SysApi selectSysApiById(String apiId);
	
	/**
     * 删除sys_api信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteSysApiByIds(String[]  ids);
}
