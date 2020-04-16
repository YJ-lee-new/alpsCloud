package com.alps.pf.service;

import com.alps.provider.pf.domain.PfApiLogic;
import java.util.List;

/**
 * pf_api_logic 服务层
 * 
 * @author leopards
 * @date 2020-03-19
 */
public interface IPfApiLogicService 
{
	/**
     * 查询pf_api_logic信息
     * 
     * @param id pf_api_logicID
     * @return pf_api_logic信息
     */
	public PfApiLogic selectPfApiLogicById(String id);
	
	/**
     * 查询pf_api_logic列表
     * 
     * @param pfApiLogic pf_api_logic信息
     * @return pf_api_logic集合
     */
	public List<PfApiLogic> selectPfApiLogicList(PfApiLogic pfApiLogic);
	
	/**
     * 新增pf_api_logic
     * 
     * @param pfApiLogic pf_api_logic信息
     * @return 结果
     */
	public int insertPfApiLogic(PfApiLogic pfApiLogic);
	
	/**
     * 修改pf_api_logic
     * 
     * @param pfApiLogic pf_api_logic信息
     * @return 结果
     */
	public int updatePfApiLogic(PfApiLogic pfApiLogic);
		
	/**
     * 删除pf_api_logic信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePfApiLogicByIds(String ids);
	
}
