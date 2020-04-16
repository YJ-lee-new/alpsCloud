package com.alps.pf.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alps.common.core.text.Convert;
import com.alps.pf.mapper.PfActionRefMapper;
import com.alps.provider.pf.domain.PfActionRef;
import com.alps.pf.service.IPfActionRefService;

/**
 * pf_action_ref 服务层实现
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Service
public class PfActionRefServiceImpl implements IPfActionRefService 
{
	@Autowired
	private PfActionRefMapper pfActionRefMapper;

	/**
     * 查询pf_action_ref信息
     * 
     * @param id pf_action_refID
     * @return pf_action_ref信息
     */
    @Override
	public PfActionRef selectPfActionRefById(String id)
	{
	    return pfActionRefMapper.selectPfActionRefById(id);
	}
	
	/**
     * 查询pf_action_ref列表
     * 
     * @param pfActionRef pf_action_ref信息
     * @return pf_action_ref集合
     */
	@Override
	public List<PfActionRef> selectPfActionRefList(PfActionRef pfActionRef)
	{
	    return pfActionRefMapper.selectPfActionRefList(pfActionRef);
	}
	
    /**
     * 新增pf_action_ref
     * 
     * @param pfActionRef pf_action_ref信息
     * @return 结果
     */
	@Override
	public int insertPfActionRef(PfActionRef pfActionRef)
	{
	    return pfActionRefMapper.insertPfActionRef(pfActionRef);
	}
	
	/**
     * 修改pf_action_ref
     * 
     * @param pfActionRef pf_action_ref信息
     * @return 结果
     */
	@Override
	public int updatePfActionRef(PfActionRef pfActionRef)
	{
	    return pfActionRefMapper.updatePfActionRef(pfActionRef);
	}

	/**
     * 删除pf_action_ref对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePfActionRefByIds(String ids)
	{
		return pfActionRefMapper.deletePfActionRefByIds(Convert.toStrArray(ids));
	}
	
}
