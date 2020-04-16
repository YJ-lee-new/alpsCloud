package com.alps.pf.mapper;

import com.alps.provider.pf.domain.PfLogic;
import java.util.List;	

/**
 * pf_logic 数据层
 * 
 * @author leopards
 * @date 2020-03-19
 */
public interface PfLogicMapper 
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
     * 删除pf_logic
     * 
     * @param logicId pf_logicID
     * @return 结果
     */
	public int deletePfLogicById(String logicId);
	
	/**
     * 批量删除pf_logic
     * 
     * @param logicIds 需要删除的数据ID
     * @return 结果
     */
	public int deletePfLogicByIds(String[] logicIds);
	
}