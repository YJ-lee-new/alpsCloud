package com.alps.pf.service;

import com.alps.provider.pf.domain.PfApp;
import java.util.List;

/**
 * pf_app 服务层
 * 
 * @author leopards
 * @date 2020-03-19
 */
public interface IPfAppService 
{
	/**
     * 查询pf_app信息
     * 
     * @param appId pf_appID
     * @return pf_app信息
     */
	public PfApp selectPfAppById(String appId);
	
	/**
     * 查询pf_app列表
     * 
     * @param pfApp pf_app信息
     * @return pf_app集合
     */
	public List<PfApp> selectPfAppList(PfApp pfApp);
	
	/**
     * 新增pf_app
     * 
     * @param pfApp pf_app信息
     * @return 结果
     */
	public int insertPfApp(PfApp pfApp);
	
	/**
     * 修改pf_app
     * 
     * @param pfApp pf_app信息
     * @return 结果
     */
	public int updatePfApp(PfApp pfApp);
		
	/**
     * 删除pf_app信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePfAppByIds(String ids);
	
}
