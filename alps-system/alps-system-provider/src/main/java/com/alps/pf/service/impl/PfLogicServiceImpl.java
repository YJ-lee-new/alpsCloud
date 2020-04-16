package com.alps.pf.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alps.common.core.text.Convert;
import com.alps.pf.mapper.PfLogicMapper;
import com.alps.provider.pf.domain.PfLogic;
import com.alps.pf.service.IPfLogicService;

/**
 * pf_logic 服务层实现
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Service
public class PfLogicServiceImpl implements IPfLogicService 
{
	@Autowired
	private PfLogicMapper pfLogicMapper;

	/**
     * 查询pf_logic信息
     * 
     * @param logicId pf_logicID
     * @return pf_logic信息
     */
    @Override
	public PfLogic selectPfLogicById(String logicId)
	{
	    return pfLogicMapper.selectPfLogicById(logicId);
	}
	
	/**
     * 查询pf_logic列表
     * 
     * @param pfLogic pf_logic信息
     * @return pf_logic集合
     */
	@Override
	public List<PfLogic> selectPfLogicList(PfLogic pfLogic)
	{
	    return pfLogicMapper.selectPfLogicList(pfLogic);
	}
	
    /**
     * 新增pf_logic
     * 
     * @param pfLogic pf_logic信息
     * @return 结果
     */
	@Override
	public int insertPfLogic(PfLogic pfLogic)
	{
	    return pfLogicMapper.insertPfLogic(pfLogic);
	}
	
	/**
     * 修改pf_logic
     * 
     * @param pfLogic pf_logic信息
     * @return 结果
     */
	@Override
	public int updatePfLogic(PfLogic pfLogic)
	{
	    return pfLogicMapper.updatePfLogic(pfLogic);
	}

	/**
     * 删除pf_logic对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePfLogicByIds(String ids)
	{
		return pfLogicMapper.deletePfLogicByIds(Convert.toStrArray(ids));
	}
	
}
