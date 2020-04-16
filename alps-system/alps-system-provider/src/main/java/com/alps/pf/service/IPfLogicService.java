package com.alps.pf.service;

import com.alps.provider.pf.domain.PfLogic;
import java.util.List;

/**
 * pf_logic 服务层
 * 
 * @author leopards
 * @date 2020-03-19
 */
public interface IPfLogicService 
{
	/**
     * 查询pf_logic信息
     * 
     * @param logicId pf_logicID
     * @return pf_logic信息
     */
	public PfLogic selectPfLogicById(String logicId);
	
	/**
     * 查询pf_logic列表
     * 
     * @param pfLogic pf_logic信息
     * @return pf_logic集合
     */
	public List<PfLogic> selectPfLogicList(PfLogic pfLogic);
	
	/**
     * 新增pf_logic
     * 
     * @param pfLogic pf_logic信息
     * @return 结果
     */
	public int insertPfLogic(PfLogic pfLogic);
	
	/**
     * 修改pf_logic
     * 
     * @param pfLogic pf_logic信息
     * @return 结果
     */
	public int updatePfLogic(PfLogic pfLogic);
		
	/**
     * 删除pf_logic信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePfLogicByIds(String ids);
	
}
