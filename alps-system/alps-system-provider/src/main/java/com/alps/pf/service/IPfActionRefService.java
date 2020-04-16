package com.alps.pf.service;

import com.alps.provider.pf.domain.PfActionRef;
import java.util.List;

/**
 * pf_action_ref 服务层
 * 
 * @author leopards
 * @date 2020-03-19
 */
public interface IPfActionRefService 
{
	/**
     * 查询pf_action_ref信息
     * 
     * @param id pf_action_refID
     * @return pf_action_ref信息
     */
	public PfActionRef selectPfActionRefById(String id);
	
	/**
     * 查询pf_action_ref列表
     * 
     * @param pfActionRef pf_action_ref信息
     * @return pf_action_ref集合
     */
	public List<PfActionRef> selectPfActionRefList(PfActionRef pfActionRef);
	
	/**
     * 新增pf_action_ref
     * 
     * @param pfActionRef pf_action_ref信息
     * @return 结果
     */
	public int insertPfActionRef(PfActionRef pfActionRef);
	
	/**
     * 修改pf_action_ref
     * 
     * @param pfActionRef pf_action_ref信息
     * @return 结果
     */
	public int updatePfActionRef(PfActionRef pfActionRef);
		
	/**
     * 删除pf_action_ref信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePfActionRefByIds(String ids);
	
}
