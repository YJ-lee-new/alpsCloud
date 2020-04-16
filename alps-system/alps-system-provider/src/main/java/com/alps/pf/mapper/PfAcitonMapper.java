package com.alps.pf.mapper;

import java.util.List;

import com.alps.provider.pf.domain.PfAciton;	

/**
 * action 数据层
 * 
 * @author leopards
 * @date 2020-03-19
 */
public interface PfAcitonMapper 
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
     * 删除action
     * 
     * @param actionId actionID
     * @return 结果
     */
	public int deletePfAcitonById(String actionId);
	
	/**
     * 批量删除action
     * 
     * @param actionIds 需要删除的数据ID
     * @return 结果
     */
	public int deletePfAcitonByIds(String[] actionIds);

	/**
	 *date:2020年3月24日
	 *Author:Yujie.lee
	 */
	public List<PfAciton> selectPfAcitonListByApp(String appId);
	
}