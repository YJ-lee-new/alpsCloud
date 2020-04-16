package com.alps.pf.service;

import com.alps.provider.pf.domain.PfApi;
import java.util.List;

/**
 * pf_api 服务层
 * 
 * @author leopards
 * @date 2020-03-19
 */
public interface IPfApiService 
{
	/**
     * 查询pf_api信息
     * 
     * @param apiId pf_apiID
     * @return pf_api信息
     */
	public PfApi selectPfApiById(String apiId);
	
	/**
     * 查询pf_api列表
     * 
     * @param pfApi pf_api信息
     * @return pf_api集合
     */
	public List<PfApi> selectPfApiList(PfApi pfApi);
	
	/**
     * 新增pf_api
     * 
     * @param pfApi pf_api信息
     * @return 结果
     */
	public int insertPfApi(PfApi pfApi);
	
	/**
     * 修改pf_api
     * 
     * @param pfApi pf_api信息
     * @return 结果
     */
	public int updatePfApi(PfApi pfApi);
		
	/**
     * 删除pf_api信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePfApiByIds(String ids);
	
}
