package com.alps.pf.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alps.common.core.text.Convert;
import com.alps.pf.mapper.PfApiLogicMapper;
import com.alps.provider.pf.domain.PfApiLogic;
import com.alps.pf.service.IPfApiLogicService;

/**
 * pf_api_logic 服务层实现
 * 
 * @author leopards
 * @date 2020-03-19
 */
@Service
public class PfApiLogicServiceImpl implements IPfApiLogicService 
{
	@Autowired
	private PfApiLogicMapper pfApiLogicMapper;

	/**
     * 查询pf_api_logic信息
     * 
     * @param id pf_api_logicID
     * @return pf_api_logic信息
     */
    @Override
	public PfApiLogic selectPfApiLogicById(String id)
	{
	    return pfApiLogicMapper.selectPfApiLogicById(id);
	}
	
	/**
     * 查询pf_api_logic列表
     * 
     * @param pfApiLogic pf_api_logic信息
     * @return pf_api_logic集合
     */
	@Override
	public List<PfApiLogic> selectPfApiLogicList(PfApiLogic pfApiLogic)
	{
	    return pfApiLogicMapper.selectPfApiLogicList(pfApiLogic);
	}
	
    /**
     * 新增pf_api_logic
     * 
     * @param pfApiLogic pf_api_logic信息
     * @return 结果
     */
	@Override
	public int insertPfApiLogic(PfApiLogic pfApiLogic)
	{
	    return pfApiLogicMapper.insertPfApiLogic(pfApiLogic);
	}
	
	/**
     * 修改pf_api_logic
     * 
     * @param pfApiLogic pf_api_logic信息
     * @return 结果
     */
	@Override
	public int updatePfApiLogic(PfApiLogic pfApiLogic)
	{
	    return pfApiLogicMapper.updatePfApiLogic(pfApiLogic);
	}

	/**
     * 删除pf_api_logic对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePfApiLogicByIds(String ids)
	{
		return pfApiLogicMapper.deletePfApiLogicByIds(Convert.toStrArray(ids));
	}
	
}
