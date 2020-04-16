package com.alps.pf.service;

import java.util.List;

import com.alps.common.core.domain.Ztree;
import com.alps.common.core.domain.Ztrees;
import com.alps.provider.pf.domain.PfAciton;
import com.alps.provider.system.domain.SysRole;

/**
 * action 服务层
 * 
 * @author leopards
 * @date 2020-03-19
 */
public interface IPfAcitonService 
{
	/**
     * 查询action信息
     * 
     * @param actionId actionID
     * @return action信息
     */
	public PfAciton selectPfAcitonById(String actionId);
	
	/**
     * 查询action列表
     * 
     * @param pfAciton action信息
     * @return action集合
     */
	public List<PfAciton> selectPfAcitonList(PfAciton pfAciton);
	
	/**
     * 新增action
     * 
     * @param pfAciton action信息
     * @return 结果
     */
	public int insertPfAciton(PfAciton pfAciton);
	
	/**
     * 修改action
     * 
     * @param pfAciton action信息
     * @return 结果
     */
	public int updatePfAciton(PfAciton pfAciton);
		
	/**
     * 删除action信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePfAcitonByIds(String ids);

	/**
	 *date:2020年3月23日
	 *Author:Yujie.lee
	 */
	public List<Ztrees> getApiList(Long memuId,SysRole role);
	
}
