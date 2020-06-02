package com.alps.oauth.uaa.client.web.mapper;


import com.alps.base.api.model.entity.SysApp;
import com.alps.plaform.database.mapper.SuperMapper;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * @author YJ.lee
 */
@Repository
public interface SysAppMapper extends SuperMapper<SysApp> {
	

	/**
     * 查询sys_app列表
     * 
     * @param SysApp sys_app信息
     * @return sys_app集合
     */
	public List<SysApp> selectSysAppList(SysApp SysApp);
	
	/**
     * 批量删除sys_app
     * 
     * @param appIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteSysAppByIds(String[] appIds);
   
}
